package android.bluetooth;

import android.annotation.UnsupportedAppUsage;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import java.io.Closeable;
import java.io.IOException;

public final class BluetoothServerSocket implements Closeable {
    private static final boolean DBG = false;
    private static final String TAG = "BluetoothServerSocket";
    private int mChannel;
    private Handler mHandler;
    private int mMessage;
    @UnsupportedAppUsage
    final BluetoothSocket mSocket;

    BluetoothServerSocket(int type, boolean auth, boolean encrypt, int port) throws IOException {
        this.mChannel = port;
        this.mSocket = new BluetoothSocket(type, -1, auth, encrypt, (BluetoothDevice) null, port, (ParcelUuid) null);
        if (port == -2) {
            this.mSocket.setExcludeSdp(true);
        }
    }

    BluetoothServerSocket(int type, boolean auth, boolean encrypt, int port, boolean mitm, boolean min16DigitPin) throws IOException {
        int i = port;
        this.mChannel = i;
        this.mSocket = new BluetoothSocket(type, -1, auth, encrypt, (BluetoothDevice) null, port, (ParcelUuid) null, mitm, min16DigitPin);
        if (i == -2) {
            this.mSocket.setExcludeSdp(true);
        }
    }

    BluetoothServerSocket(int type, boolean auth, boolean encrypt, ParcelUuid uuid) throws IOException {
        this.mSocket = new BluetoothSocket(type, -1, auth, encrypt, (BluetoothDevice) null, -1, uuid);
        this.mChannel = this.mSocket.getPort();
    }

    public BluetoothSocket accept() throws IOException {
        return accept(-1);
    }

    public BluetoothSocket accept(int timeout) throws IOException {
        return this.mSocket.accept(timeout);
    }

    public void close() throws IOException {
        synchronized (this) {
            if (this.mHandler != null) {
                this.mHandler.obtainMessage(this.mMessage).sendToTarget();
            }
        }
        this.mSocket.close();
    }

    /* access modifiers changed from: package-private */
    public synchronized void setCloseHandler(Handler handler, int message) {
        this.mHandler = handler;
        this.mMessage = message;
    }

    /* access modifiers changed from: package-private */
    public void setServiceName(String serviceName) {
        this.mSocket.setServiceName(serviceName);
    }

    public int getChannel() {
        return this.mChannel;
    }

    public int getPsm() {
        return this.mChannel;
    }

    /* access modifiers changed from: package-private */
    public void setChannel(int newChannel) {
        if (!(this.mSocket == null || this.mSocket.getPort() == newChannel)) {
            Log.w(TAG, "The port set is different that the underlying port. mSocket.getPort(): " + this.mSocket.getPort() + " requested newChannel: " + newChannel);
        }
        this.mChannel = newChannel;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ServerSocket: Type: ");
        switch (this.mSocket.getConnectionType()) {
            case 1:
                sb.append("TYPE_RFCOMM");
                break;
            case 2:
                sb.append("TYPE_SCO");
                break;
            case 3:
                sb.append("TYPE_L2CAP");
                break;
            case 4:
                sb.append("TYPE_L2CAP_LE");
                break;
        }
        sb.append(" Channel: ");
        sb.append(this.mChannel);
        return sb.toString();
    }
}
