package android.net;

import android.os.Parcel;
import android.os.Parcelable;

public class NetworkMisc implements Parcelable {
    public static final Parcelable.Creator<NetworkMisc> CREATOR = new Parcelable.Creator<NetworkMisc>() {
        public NetworkMisc createFromParcel(Parcel in) {
            NetworkMisc networkMisc = new NetworkMisc();
            boolean z = false;
            networkMisc.allowBypass = in.readInt() != 0;
            networkMisc.explicitlySelected = in.readInt() != 0;
            networkMisc.acceptUnvalidated = in.readInt() != 0;
            networkMisc.subscriberId = in.readString();
            networkMisc.provisioningNotificationDisabled = in.readInt() != 0;
            if (in.readInt() != 0) {
                z = true;
            }
            networkMisc.skip464xlat = z;
            return networkMisc;
        }

        public NetworkMisc[] newArray(int size) {
            return new NetworkMisc[size];
        }
    };
    public boolean acceptPartialConnectivity;
    public boolean acceptUnvalidated;
    public boolean allowBypass;
    public boolean explicitlySelected;
    public boolean provisioningNotificationDisabled;
    public boolean skip464xlat;
    public String subscriberId;

    public NetworkMisc() {
    }

    public NetworkMisc(NetworkMisc nm) {
        if (nm != null) {
            this.allowBypass = nm.allowBypass;
            this.explicitlySelected = nm.explicitlySelected;
            this.acceptUnvalidated = nm.acceptUnvalidated;
            this.subscriberId = nm.subscriberId;
            this.provisioningNotificationDisabled = nm.provisioningNotificationDisabled;
            this.skip464xlat = nm.skip464xlat;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.allowBypass ? 1 : 0);
        out.writeInt(this.explicitlySelected ? 1 : 0);
        out.writeInt(this.acceptUnvalidated ? 1 : 0);
        out.writeString(this.subscriberId);
        out.writeInt(this.provisioningNotificationDisabled ? 1 : 0);
        out.writeInt(this.skip464xlat ? 1 : 0);
    }
}
