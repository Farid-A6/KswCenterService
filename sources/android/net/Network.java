package android.net;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.proto.ProtoOutputStream;
import com.android.okhttp.internalandroidapi.Dns;
import com.android.okhttp.internalandroidapi.HttpURLConnectionFactory;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import libcore.io.IoUtils;

public class Network implements Parcelable {
    public static final Parcelable.Creator<Network> CREATOR = new Parcelable.Creator<Network>() {
        public Network createFromParcel(Parcel in) {
            return new Network(in.readInt());
        }

        public Network[] newArray(int size) {
            return new Network[size];
        }
    };
    private static final long HANDLE_MAGIC = 3405697037L;
    private static final int HANDLE_MAGIC_SIZE = 32;
    private static final boolean httpKeepAlive = Boolean.parseBoolean(System.getProperty("http.keepAlive", "true"));
    private static final long httpKeepAliveDurationMs = Long.parseLong(System.getProperty("http.keepAliveDuration", "300000"));
    private static final int httpMaxConnections = (httpKeepAlive ? Integer.parseInt(System.getProperty("http.maxConnections", "5")) : 0);
    private final Object mLock;
    private volatile NetworkBoundSocketFactory mNetworkBoundSocketFactory;
    private final transient boolean mPrivateDnsBypass;
    private volatile HttpURLConnectionFactory mUrlConnectionFactory;
    @UnsupportedAppUsage
    public final int netId;

    @UnsupportedAppUsage
    public Network(int netId2) {
        this(netId2, false);
    }

    public Network(int netId2, boolean privateDnsBypass) {
        this.mNetworkBoundSocketFactory = null;
        this.mLock = new Object();
        this.netId = netId2;
        this.mPrivateDnsBypass = privateDnsBypass;
    }

    @SystemApi
    public Network(Network that) {
        this(that.netId, that.mPrivateDnsBypass);
    }

    public InetAddress[] getAllByName(String host) throws UnknownHostException {
        return InetAddress.getAllByNameOnNet(host, getNetIdForResolv());
    }

    public InetAddress getByName(String host) throws UnknownHostException {
        return InetAddress.getByNameOnNet(host, getNetIdForResolv());
    }

    @SystemApi
    public Network getPrivateDnsBypassingCopy() {
        return new Network(this.netId, true);
    }

    public int getNetIdForResolv() {
        if (this.mPrivateDnsBypass) {
            return (int) (2147483648L | ((long) this.netId));
        }
        return this.netId;
    }

    private class NetworkBoundSocketFactory extends SocketFactory {
        private NetworkBoundSocketFactory() {
        }

        private Socket connectToHost(String host, int port, SocketAddress localAddress) throws IOException {
            Socket socket;
            InetAddress[] hostAddresses = Network.this.getAllByName(host);
            int i = 0;
            while (i < hostAddresses.length) {
                try {
                    socket = createSocket();
                    if (localAddress != null) {
                        socket.bind(localAddress);
                    }
                    socket.connect(new InetSocketAddress(hostAddresses[i], port));
                    if (0 != 0) {
                        IoUtils.closeQuietly(socket);
                    }
                    return socket;
                } catch (IOException e) {
                    if (i != hostAddresses.length - 1) {
                        i++;
                    } else {
                        throw e;
                    }
                } catch (Throwable th) {
                    if (1 != 0) {
                        IoUtils.closeQuietly(socket);
                    }
                    throw th;
                }
            }
            throw new UnknownHostException(host);
        }

        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
            return connectToHost(host, port, new InetSocketAddress(localHost, localPort));
        }

        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            Socket socket = createSocket();
            boolean failed = true;
            try {
                socket.bind(new InetSocketAddress(localAddress, localPort));
                socket.connect(new InetSocketAddress(address, port));
                failed = false;
                return socket;
            } finally {
                if (failed) {
                    IoUtils.closeQuietly(socket);
                }
            }
        }

        public Socket createSocket(InetAddress host, int port) throws IOException {
            Socket socket = createSocket();
            boolean failed = true;
            try {
                socket.connect(new InetSocketAddress(host, port));
                failed = false;
                return socket;
            } finally {
                if (failed) {
                    IoUtils.closeQuietly(socket);
                }
            }
        }

        public Socket createSocket(String host, int port) throws IOException {
            return connectToHost(host, port, (SocketAddress) null);
        }

        public Socket createSocket() throws IOException {
            Socket socket = new Socket();
            boolean failed = true;
            try {
                Network.this.bindSocket(socket);
                failed = false;
                return socket;
            } finally {
                if (failed) {
                    IoUtils.closeQuietly(socket);
                }
            }
        }
    }

    public SocketFactory getSocketFactory() {
        if (this.mNetworkBoundSocketFactory == null) {
            synchronized (this.mLock) {
                if (this.mNetworkBoundSocketFactory == null) {
                    this.mNetworkBoundSocketFactory = new NetworkBoundSocketFactory();
                }
            }
        }
        return this.mNetworkBoundSocketFactory;
    }

    private void maybeInitUrlConnectionFactory() {
        synchronized (this.mLock) {
            if (this.mUrlConnectionFactory == null) {
                Dns dnsLookup = new Dns() {
                    public final List lookup(String str) {
                        return Arrays.asList(Network.this.getAllByName(str));
                    }
                };
                HttpURLConnectionFactory urlConnectionFactory = new HttpURLConnectionFactory();
                urlConnectionFactory.setDns(dnsLookup);
                urlConnectionFactory.setNewConnectionPool(httpMaxConnections, httpKeepAliveDurationMs, TimeUnit.MILLISECONDS);
                this.mUrlConnectionFactory = urlConnectionFactory;
            }
        }
    }

    public URLConnection openConnection(URL url) throws IOException {
        Proxy proxy;
        ConnectivityManager cm = ConnectivityManager.getInstanceOrNull();
        if (cm != null) {
            ProxyInfo proxyInfo = cm.getProxyForNetwork(this);
            if (proxyInfo != null) {
                proxy = proxyInfo.makeProxy();
            } else {
                proxy = Proxy.NO_PROXY;
            }
            return openConnection(url, proxy);
        }
        throw new IOException("No ConnectivityManager yet constructed, please construct one");
    }

    public URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        if (proxy != null) {
            maybeInitUrlConnectionFactory();
            return this.mUrlConnectionFactory.openConnection(url, getSocketFactory(), proxy);
        }
        throw new IllegalArgumentException("proxy is null");
    }

    public void bindSocket(DatagramSocket socket) throws IOException {
        socket.getReuseAddress();
        bindSocket(socket.getFileDescriptor$());
    }

    public void bindSocket(Socket socket) throws IOException {
        socket.getReuseAddress();
        bindSocket(socket.getFileDescriptor$());
    }

    public void bindSocket(FileDescriptor fd) throws IOException {
        try {
            if (((InetSocketAddress) Os.getpeername(fd)).getAddress().isAnyLocalAddress()) {
                int err = NetworkUtils.bindSocketToNetwork(fd.getInt$(), this.netId);
                if (err != 0) {
                    throw new ErrnoException("Binding socket to network " + this.netId, -err).rethrowAsSocketException();
                }
                return;
            }
            throw new SocketException("Socket is connected");
        } catch (ErrnoException e) {
            if (e.errno != OsConstants.ENOTCONN) {
                throw e.rethrowAsSocketException();
            }
        } catch (ClassCastException e2) {
            throw new SocketException("Only AF_INET/AF_INET6 sockets supported");
        }
    }

    public static Network fromNetworkHandle(long networkHandle) {
        if (networkHandle == 0) {
            throw new IllegalArgumentException("Network.fromNetworkHandle refusing to instantiate NETID_UNSET Network.");
        } else if ((4294967295L & networkHandle) == HANDLE_MAGIC && networkHandle >= 0) {
            return new Network((int) (networkHandle >> 32));
        } else {
            throw new IllegalArgumentException("Value passed to fromNetworkHandle() is not a network handle.");
        }
    }

    public long getNetworkHandle() {
        if (this.netId == 0) {
            return 0;
        }
        return (((long) this.netId) << 32) | HANDLE_MAGIC;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.netId);
    }

    public boolean equals(Object obj) {
        if ((obj instanceof Network) && this.netId == ((Network) obj).netId) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.netId * 11;
    }

    public String toString() {
        return Integer.toString(this.netId);
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, this.netId);
        proto.end(token);
    }
}
