package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.annotations.VisibleForTesting;
import java.util.EnumMap;

@Deprecated
public class NetworkInfo implements Parcelable {
    public static final Parcelable.Creator<NetworkInfo> CREATOR = new Parcelable.Creator<NetworkInfo>() {
        public NetworkInfo createFromParcel(Parcel in) {
            NetworkInfo netInfo = new NetworkInfo(in.readInt(), in.readInt(), in.readString(), in.readString());
            State unused = netInfo.mState = State.valueOf(in.readString());
            DetailedState unused2 = netInfo.mDetailedState = DetailedState.valueOf(in.readString());
            boolean z = false;
            boolean unused3 = netInfo.mIsFailover = in.readInt() != 0;
            boolean unused4 = netInfo.mIsAvailable = in.readInt() != 0;
            if (in.readInt() != 0) {
                z = true;
            }
            boolean unused5 = netInfo.mIsRoaming = z;
            String unused6 = netInfo.mReason = in.readString();
            String unused7 = netInfo.mExtraInfo = in.readString();
            return netInfo;
        }

        public NetworkInfo[] newArray(int size) {
            return new NetworkInfo[size];
        }
    };
    private static final EnumMap<DetailedState, State> stateMap = new EnumMap<>(DetailedState.class);
    /* access modifiers changed from: private */
    public DetailedState mDetailedState;
    /* access modifiers changed from: private */
    public String mExtraInfo;
    /* access modifiers changed from: private */
    public boolean mIsAvailable;
    /* access modifiers changed from: private */
    public boolean mIsFailover;
    /* access modifiers changed from: private */
    public boolean mIsRoaming;
    private int mNetworkType;
    /* access modifiers changed from: private */
    public String mReason;
    /* access modifiers changed from: private */
    public State mState;
    private int mSubtype;
    private String mSubtypeName;
    private String mTypeName;

    @Deprecated
    public enum DetailedState {
        IDLE,
        SCANNING,
        CONNECTING,
        AUTHENTICATING,
        OBTAINING_IPADDR,
        CONNECTED,
        SUSPENDED,
        DISCONNECTING,
        DISCONNECTED,
        FAILED,
        BLOCKED,
        VERIFYING_POOR_LINK,
        CAPTIVE_PORTAL_CHECK
    }

    @Deprecated
    public enum State {
        CONNECTING,
        CONNECTED,
        SUSPENDED,
        DISCONNECTING,
        DISCONNECTED,
        UNKNOWN
    }

    static {
        stateMap.put(DetailedState.IDLE, State.DISCONNECTED);
        stateMap.put(DetailedState.SCANNING, State.DISCONNECTED);
        stateMap.put(DetailedState.CONNECTING, State.CONNECTING);
        stateMap.put(DetailedState.AUTHENTICATING, State.CONNECTING);
        stateMap.put(DetailedState.OBTAINING_IPADDR, State.CONNECTING);
        stateMap.put(DetailedState.VERIFYING_POOR_LINK, State.CONNECTING);
        stateMap.put(DetailedState.CAPTIVE_PORTAL_CHECK, State.CONNECTING);
        stateMap.put(DetailedState.CONNECTED, State.CONNECTED);
        stateMap.put(DetailedState.SUSPENDED, State.SUSPENDED);
        stateMap.put(DetailedState.DISCONNECTING, State.DISCONNECTING);
        stateMap.put(DetailedState.DISCONNECTED, State.DISCONNECTED);
        stateMap.put(DetailedState.FAILED, State.DISCONNECTED);
        stateMap.put(DetailedState.BLOCKED, State.DISCONNECTED);
    }

    @UnsupportedAppUsage
    public NetworkInfo(int type, int subtype, String typeName, String subtypeName) {
        if (ConnectivityManager.isNetworkTypeValid(type) || type == -1) {
            this.mNetworkType = type;
            this.mSubtype = subtype;
            this.mTypeName = typeName;
            this.mSubtypeName = subtypeName;
            setDetailedState(DetailedState.IDLE, (String) null, (String) null);
            this.mState = State.UNKNOWN;
            return;
        }
        throw new IllegalArgumentException("Invalid network type: " + type);
    }

    @UnsupportedAppUsage
    public NetworkInfo(NetworkInfo source) {
        if (source != null) {
            synchronized (source) {
                this.mNetworkType = source.mNetworkType;
                this.mSubtype = source.mSubtype;
                this.mTypeName = source.mTypeName;
                this.mSubtypeName = source.mSubtypeName;
                this.mState = source.mState;
                this.mDetailedState = source.mDetailedState;
                this.mReason = source.mReason;
                this.mExtraInfo = source.mExtraInfo;
                this.mIsFailover = source.mIsFailover;
                this.mIsAvailable = source.mIsAvailable;
                this.mIsRoaming = source.mIsRoaming;
            }
        }
    }

    @Deprecated
    public int getType() {
        int i;
        synchronized (this) {
            i = this.mNetworkType;
        }
        return i;
    }

    @Deprecated
    public void setType(int type) {
        synchronized (this) {
            this.mNetworkType = type;
        }
    }

    @Deprecated
    public int getSubtype() {
        int i;
        synchronized (this) {
            i = this.mSubtype;
        }
        return i;
    }

    @UnsupportedAppUsage
    public void setSubtype(int subtype, String subtypeName) {
        synchronized (this) {
            this.mSubtype = subtype;
            this.mSubtypeName = subtypeName;
        }
    }

    @Deprecated
    public String getTypeName() {
        String str;
        synchronized (this) {
            str = this.mTypeName;
        }
        return str;
    }

    @Deprecated
    public String getSubtypeName() {
        String str;
        synchronized (this) {
            str = this.mSubtypeName;
        }
        return str;
    }

    @Deprecated
    public boolean isConnectedOrConnecting() {
        boolean z;
        synchronized (this) {
            if (this.mState != State.CONNECTED) {
                if (this.mState != State.CONNECTING) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    @Deprecated
    public boolean isConnected() {
        boolean z;
        synchronized (this) {
            z = this.mState == State.CONNECTED;
        }
        return z;
    }

    @Deprecated
    public boolean isAvailable() {
        boolean z;
        synchronized (this) {
            z = this.mIsAvailable;
        }
        return z;
    }

    @Deprecated
    @UnsupportedAppUsage
    public void setIsAvailable(boolean isAvailable) {
        synchronized (this) {
            this.mIsAvailable = isAvailable;
        }
    }

    @Deprecated
    public boolean isFailover() {
        boolean z;
        synchronized (this) {
            z = this.mIsFailover;
        }
        return z;
    }

    @Deprecated
    @UnsupportedAppUsage
    public void setFailover(boolean isFailover) {
        synchronized (this) {
            this.mIsFailover = isFailover;
        }
    }

    @Deprecated
    public boolean isRoaming() {
        boolean z;
        synchronized (this) {
            z = this.mIsRoaming;
        }
        return z;
    }

    @VisibleForTesting
    @Deprecated
    @UnsupportedAppUsage
    public void setRoaming(boolean isRoaming) {
        synchronized (this) {
            this.mIsRoaming = isRoaming;
        }
    }

    @Deprecated
    public State getState() {
        State state;
        synchronized (this) {
            state = this.mState;
        }
        return state;
    }

    @Deprecated
    public DetailedState getDetailedState() {
        DetailedState detailedState;
        synchronized (this) {
            detailedState = this.mDetailedState;
        }
        return detailedState;
    }

    @Deprecated
    @UnsupportedAppUsage
    public void setDetailedState(DetailedState detailedState, String reason, String extraInfo) {
        synchronized (this) {
            this.mDetailedState = detailedState;
            this.mState = stateMap.get(detailedState);
            this.mReason = reason;
            this.mExtraInfo = extraInfo;
        }
    }

    @Deprecated
    public void setExtraInfo(String extraInfo) {
        synchronized (this) {
            this.mExtraInfo = extraInfo;
        }
    }

    public String getReason() {
        String str;
        synchronized (this) {
            str = this.mReason;
        }
        return str;
    }

    @Deprecated
    public String getExtraInfo() {
        String str;
        synchronized (this) {
            str = this.mExtraInfo;
        }
        return str;
    }

    public String toString() {
        String sb;
        synchronized (this) {
            StringBuilder builder = new StringBuilder("[");
            builder.append("type: ");
            builder.append(getTypeName());
            builder.append("[");
            builder.append(getSubtypeName());
            builder.append("], state: ");
            builder.append(this.mState);
            builder.append("/");
            builder.append(this.mDetailedState);
            builder.append(", reason: ");
            builder.append(this.mReason == null ? "(unspecified)" : this.mReason);
            builder.append(", extra: ");
            builder.append(this.mExtraInfo == null ? "(none)" : this.mExtraInfo);
            builder.append(", failover: ");
            builder.append(this.mIsFailover);
            builder.append(", available: ");
            builder.append(this.mIsAvailable);
            builder.append(", roaming: ");
            builder.append(this.mIsRoaming);
            builder.append("]");
            sb = builder.toString();
        }
        return sb;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        synchronized (this) {
            dest.writeInt(this.mNetworkType);
            dest.writeInt(this.mSubtype);
            dest.writeString(this.mTypeName);
            dest.writeString(this.mSubtypeName);
            dest.writeString(this.mState.name());
            dest.writeString(this.mDetailedState.name());
            dest.writeInt(this.mIsFailover ? 1 : 0);
            dest.writeInt(this.mIsAvailable ? 1 : 0);
            dest.writeInt(this.mIsRoaming ? 1 : 0);
            dest.writeString(this.mReason);
            dest.writeString(this.mExtraInfo);
        }
    }
}
