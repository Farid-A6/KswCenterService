package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.SystemClock;
import android.util.Log;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.util.TrafficStatsConstants;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class SntpClient {
    private static final boolean DBG = true;
    private static final int NTP_LEAP_NOSYNC = 3;
    private static final int NTP_MODE_BROADCAST = 5;
    private static final int NTP_MODE_CLIENT = 3;
    private static final int NTP_MODE_SERVER = 4;
    private static final int NTP_PACKET_SIZE = 48;
    private static final int NTP_PORT = 123;
    private static final int NTP_STRATUM_DEATH = 0;
    private static final int NTP_STRATUM_MAX = 15;
    private static final int NTP_VERSION = 3;
    private static final long OFFSET_1900_TO_1970 = 2208988800L;
    private static final int ORIGINATE_TIME_OFFSET = 24;
    private static final int RECEIVE_TIME_OFFSET = 32;
    private static final int REFERENCE_TIME_OFFSET = 16;
    private static final String TAG = "SntpClient";
    private static final int TRANSMIT_TIME_OFFSET = 40;
    private long mNtpTime;
    private long mNtpTimeReference;
    private long mRoundTripTime;

    private static class InvalidServerReplyException extends Exception {
        public InvalidServerReplyException(String message) {
            super(message);
        }
    }

    public boolean requestTime(String host, int timeout, Network network) {
        Network networkForResolv = network.getPrivateDnsBypassingCopy();
        try {
            return requestTime(networkForResolv.getByName(host), 123, timeout, networkForResolv);
        } catch (Exception e) {
            EventLogTags.writeNtpFailure(host, e.toString());
            Log.d(TAG, "request time failed: " + e);
            return false;
        }
    }

    public boolean requestTime(InetAddress address, int port, int timeout, Network network) {
        DatagramSocket socket = null;
        int oldTag = TrafficStats.getAndSetThreadStatsTag(TrafficStatsConstants.TAG_SYSTEM_NTP);
        try {
            socket = new DatagramSocket();
            network.bindSocket(socket);
            socket.setSoTimeout(timeout);
            byte[] buffer = new byte[48];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
            buffer[0] = GsmAlphabet.GSM_EXTENDED_ESCAPE;
            long requestTime = System.currentTimeMillis();
            long requestTicks = SystemClock.elapsedRealtime();
            writeTimeStamp(buffer, 40, requestTime);
            socket.send(request);
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);
            long responseTicks = SystemClock.elapsedRealtime();
            long responseTime = requestTime + (responseTicks - requestTicks);
            byte mode = (byte) (buffer[0] & 7);
            DatagramPacket datagramPacket = request;
            byte b = buffer[1] & 255;
            DatagramPacket datagramPacket2 = response;
            long originateTime = readTimeStamp(buffer, 24);
            long receiveTime = readTimeStamp(buffer, 32);
            long transmitTime = readTimeStamp(buffer, 40);
            checkValidServerReply((byte) ((buffer[0] >> 6) & 3), mode, b, transmitTime);
            byte b2 = mode;
            byte b3 = b;
            long roundTripTime = (responseTicks - requestTicks) - (transmitTime - receiveTime);
            byte[] bArr = buffer;
            long j = transmitTime;
            long clockOffset = ((receiveTime - originateTime) + (transmitTime - responseTime)) / 2;
            EventLogTags.writeNtpSuccess(address.toString(), roundTripTime, clockOffset);
            StringBuilder sb = new StringBuilder();
            long j2 = requestTime;
            sb.append("round trip: ");
            sb.append(roundTripTime);
            sb.append("ms, clock offset: ");
            sb.append(clockOffset);
            sb.append("ms");
            Log.d(TAG, sb.toString());
            this.mNtpTime = responseTime + clockOffset;
            this.mNtpTimeReference = responseTicks;
            this.mRoundTripTime = roundTripTime;
            socket.close();
            TrafficStats.setThreadStatsTag(oldTag);
            return true;
        } catch (Exception e) {
            EventLogTags.writeNtpFailure(address.toString(), e.toString());
            Log.d(TAG, "request time failed: " + e);
            if (socket != null) {
                socket.close();
            }
            TrafficStats.setThreadStatsTag(oldTag);
            return false;
        } catch (Throwable th) {
            if (socket != null) {
                socket.close();
            }
            TrafficStats.setThreadStatsTag(oldTag);
            throw th;
        }
    }

    @Deprecated
    @UnsupportedAppUsage
    public boolean requestTime(String host, int timeout) {
        Log.w(TAG, "Shame on you for calling the hidden API requestTime()!");
        return false;
    }

    @UnsupportedAppUsage
    public long getNtpTime() {
        return this.mNtpTime;
    }

    @UnsupportedAppUsage
    public long getNtpTimeReference() {
        return this.mNtpTimeReference;
    }

    @UnsupportedAppUsage
    public long getRoundTripTime() {
        return this.mRoundTripTime;
    }

    private static void checkValidServerReply(byte leap, byte mode, int stratum, long transmitTime) throws InvalidServerReplyException {
        if (leap == 3) {
            throw new InvalidServerReplyException("unsynchronized server");
        } else if (mode != 4 && mode != 5) {
            throw new InvalidServerReplyException("untrusted mode: " + mode);
        } else if (stratum == 0 || stratum > 15) {
            throw new InvalidServerReplyException("untrusted stratum: " + stratum);
        } else if (transmitTime == 0) {
            throw new InvalidServerReplyException("zero transmitTime");
        }
    }

    private long read32(byte[] buffer, int offset) {
        byte b0 = buffer[offset];
        byte b1 = buffer[offset + 1];
        byte b2 = buffer[offset + 2];
        byte b3 = buffer[offset + 3];
        return (((long) ((b0 & 128) == 128 ? (b0 & Byte.MAX_VALUE) + 128 : b0)) << 24) + (((long) ((b1 & 128) == 128 ? (b1 & Byte.MAX_VALUE) + 128 : b1)) << 16) + (((long) ((b2 & 128) == 128 ? (b2 & Byte.MAX_VALUE) + 128 : b2)) << 8) + ((long) ((b3 & 128) == 128 ? 128 + (b3 & Byte.MAX_VALUE) : b3));
    }

    private long readTimeStamp(byte[] buffer, int offset) {
        long seconds = read32(buffer, offset);
        long fraction = read32(buffer, offset + 4);
        if (seconds == 0 && fraction == 0) {
            return 0;
        }
        return ((seconds - OFFSET_1900_TO_1970) * 1000) + ((1000 * fraction) / 4294967296L);
    }

    private void writeTimeStamp(byte[] buffer, int offset, long time) {
        byte[] bArr = buffer;
        int i = offset;
        if (time == 0) {
            Arrays.fill(buffer, i, i + 8, (byte) 0);
            return;
        }
        long seconds = time / 1000;
        long seconds2 = seconds + OFFSET_1900_TO_1970;
        int offset2 = i + 1;
        bArr[i] = (byte) ((int) (seconds2 >> 24));
        int offset3 = offset2 + 1;
        bArr[offset2] = (byte) ((int) (seconds2 >> 16));
        int offset4 = offset3 + 1;
        bArr[offset3] = (byte) ((int) (seconds2 >> 8));
        int offset5 = offset4 + 1;
        bArr[offset4] = (byte) ((int) (seconds2 >> 0));
        long fraction = (4294967296L * (time - (seconds * 1000))) / 1000;
        int offset6 = offset5 + 1;
        bArr[offset5] = (byte) ((int) (fraction >> 24));
        int offset7 = offset6 + 1;
        bArr[offset6] = (byte) ((int) (fraction >> 16));
        int offset8 = offset7 + 1;
        bArr[offset7] = (byte) ((int) (fraction >> 8));
        int offset9 = offset8 + 1;
        bArr[offset8] = (byte) ((int) (Math.random() * 255.0d));
    }
}
