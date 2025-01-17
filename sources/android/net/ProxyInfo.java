package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.text.TextUtils;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Locale;

public class ProxyInfo implements Parcelable {
    public static final Parcelable.Creator<ProxyInfo> CREATOR = new Parcelable.Creator<ProxyInfo>() {
        public ProxyInfo createFromParcel(Parcel in) {
            String host = null;
            int port = 0;
            if (in.readByte() != 0) {
                return new ProxyInfo(Uri.CREATOR.createFromParcel(in), in.readInt());
            }
            if (in.readByte() != 0) {
                host = in.readString();
                port = in.readInt();
            }
            return new ProxyInfo(host, port, in.readString(), in.readStringArray());
        }

        public ProxyInfo[] newArray(int size) {
            return new ProxyInfo[size];
        }
    };
    public static final String LOCAL_EXCL_LIST = "";
    public static final String LOCAL_HOST = "localhost";
    public static final int LOCAL_PORT = -1;
    private final String mExclusionList;
    private final String mHost;
    private final Uri mPacFileUrl;
    private final String[] mParsedExclusionList;
    private final int mPort;

    public static ProxyInfo buildDirectProxy(String host, int port) {
        return new ProxyInfo(host, port, (String) null);
    }

    public static ProxyInfo buildDirectProxy(String host, int port, List<String> exclList) {
        String[] array = (String[]) exclList.toArray(new String[exclList.size()]);
        return new ProxyInfo(host, port, TextUtils.join((CharSequence) SmsManager.REGEX_PREFIX_DELIMITER, (Object[]) array), array);
    }

    public static ProxyInfo buildPacProxy(Uri pacUri) {
        return new ProxyInfo(pacUri);
    }

    @UnsupportedAppUsage
    public ProxyInfo(String host, int port, String exclList) {
        this.mHost = host;
        this.mPort = port;
        this.mExclusionList = exclList;
        this.mParsedExclusionList = parseExclusionList(this.mExclusionList);
        this.mPacFileUrl = Uri.EMPTY;
    }

    public ProxyInfo(Uri pacFileUrl) {
        this.mHost = LOCAL_HOST;
        this.mPort = -1;
        this.mExclusionList = "";
        this.mParsedExclusionList = parseExclusionList(this.mExclusionList);
        if (pacFileUrl != null) {
            this.mPacFileUrl = pacFileUrl;
            return;
        }
        throw new NullPointerException();
    }

    public ProxyInfo(String pacFileUrl) {
        this.mHost = LOCAL_HOST;
        this.mPort = -1;
        this.mExclusionList = "";
        this.mParsedExclusionList = parseExclusionList(this.mExclusionList);
        this.mPacFileUrl = Uri.parse(pacFileUrl);
    }

    public ProxyInfo(Uri pacFileUrl, int localProxyPort) {
        this.mHost = LOCAL_HOST;
        this.mPort = localProxyPort;
        this.mExclusionList = "";
        this.mParsedExclusionList = parseExclusionList(this.mExclusionList);
        if (pacFileUrl != null) {
            this.mPacFileUrl = pacFileUrl;
            return;
        }
        throw new NullPointerException();
    }

    private static String[] parseExclusionList(String exclusionList) {
        if (exclusionList == null) {
            return new String[0];
        }
        return exclusionList.toLowerCase(Locale.ROOT).split(SmsManager.REGEX_PREFIX_DELIMITER);
    }

    private ProxyInfo(String host, int port, String exclList, String[] parsedExclList) {
        this.mHost = host;
        this.mPort = port;
        this.mExclusionList = exclList;
        this.mParsedExclusionList = parsedExclList;
        this.mPacFileUrl = Uri.EMPTY;
    }

    public ProxyInfo(ProxyInfo source) {
        if (source != null) {
            this.mHost = source.getHost();
            this.mPort = source.getPort();
            this.mPacFileUrl = source.mPacFileUrl;
            this.mExclusionList = source.getExclusionListAsString();
            this.mParsedExclusionList = source.mParsedExclusionList;
            return;
        }
        this.mHost = null;
        this.mPort = 0;
        this.mExclusionList = null;
        this.mParsedExclusionList = null;
        this.mPacFileUrl = Uri.EMPTY;
    }

    public InetSocketAddress getSocketAddress() {
        try {
            return new InetSocketAddress(this.mHost, this.mPort);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Uri getPacFileUrl() {
        return this.mPacFileUrl;
    }

    public String getHost() {
        return this.mHost;
    }

    public int getPort() {
        return this.mPort;
    }

    public String[] getExclusionList() {
        return this.mParsedExclusionList;
    }

    public String getExclusionListAsString() {
        return this.mExclusionList;
    }

    public boolean isValid() {
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            return true;
        }
        if (Proxy.validate(this.mHost == null ? "" : this.mHost, this.mPort == 0 ? "" : Integer.toString(this.mPort), this.mExclusionList == null ? "" : this.mExclusionList) == 0) {
            return true;
        }
        return false;
    }

    public Proxy makeProxy() {
        Proxy proxy = Proxy.NO_PROXY;
        if (this.mHost == null) {
            return proxy;
        }
        try {
            return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.mHost, this.mPort));
        } catch (IllegalArgumentException e) {
            return proxy;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            sb.append("PAC Script: ");
            sb.append(this.mPacFileUrl);
        }
        if (this.mHost != null) {
            sb.append("[");
            sb.append(this.mHost);
            sb.append("] ");
            sb.append(Integer.toString(this.mPort));
            if (this.mExclusionList != null) {
                sb.append(" xl=");
                sb.append(this.mExclusionList);
            }
        } else {
            sb.append("[ProxyProperties.mHost == null]");
        }
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (!(o instanceof ProxyInfo)) {
            return false;
        }
        ProxyInfo p = (ProxyInfo) o;
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            if (!this.mPacFileUrl.equals(p.getPacFileUrl()) || this.mPort != p.mPort) {
                return false;
            }
            return true;
        } else if (!Uri.EMPTY.equals(p.mPacFileUrl)) {
            return false;
        } else {
            if (this.mExclusionList != null && !this.mExclusionList.equals(p.getExclusionListAsString())) {
                return false;
            }
            if (this.mHost != null && p.getHost() != null && !this.mHost.equals(p.getHost())) {
                return false;
            }
            if (this.mHost != null && p.mHost == null) {
                return false;
            }
            if ((this.mHost != null || p.mHost == null) && this.mPort == p.mPort) {
                return true;
            }
            return false;
        }
    }

    public int describeContents() {
        return 0;
    }

    public int hashCode() {
        int i;
        int i2 = 0;
        if (this.mHost == null) {
            i = 0;
        } else {
            i = this.mHost.hashCode();
        }
        if (this.mExclusionList != null) {
            i2 = this.mExclusionList.hashCode();
        }
        return i + i2 + this.mPort;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            dest.writeByte((byte) 1);
            this.mPacFileUrl.writeToParcel(dest, 0);
            dest.writeInt(this.mPort);
            return;
        }
        dest.writeByte((byte) 0);
        if (this.mHost != null) {
            dest.writeByte((byte) 1);
            dest.writeString(this.mHost);
            dest.writeInt(this.mPort);
        } else {
            dest.writeByte((byte) 0);
        }
        dest.writeString(this.mExclusionList);
        dest.writeStringArray(this.mParsedExclusionList);
    }
}
