package android.net.wifi.p2p.nsd;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class WifiP2pServiceInfo implements Parcelable {
    @UnsupportedAppUsage
    public static final Parcelable.Creator<WifiP2pServiceInfo> CREATOR = new Parcelable.Creator<WifiP2pServiceInfo>() {
        public WifiP2pServiceInfo createFromParcel(Parcel in) {
            List<String> data = new ArrayList<>();
            in.readStringList(data);
            return new WifiP2pServiceInfo(data);
        }

        public WifiP2pServiceInfo[] newArray(int size) {
            return new WifiP2pServiceInfo[size];
        }
    };
    public static final int SERVICE_TYPE_ALL = 0;
    public static final int SERVICE_TYPE_BONJOUR = 1;
    public static final int SERVICE_TYPE_UPNP = 2;
    public static final int SERVICE_TYPE_VENDOR_SPECIFIC = 255;
    public static final int SERVICE_TYPE_WS_DISCOVERY = 3;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private List<String> mQueryList;

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    protected WifiP2pServiceInfo(List<String> queryList) {
        if (queryList != null) {
            this.mQueryList = queryList;
            return;
        }
        throw new IllegalArgumentException("query list cannot be null");
    }

    public List<String> getSupplicantQueryList() {
        return this.mQueryList;
    }

    static String bin2HexStr(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int length = data.length;
        int i = 0;
        while (i < length) {
            try {
                String s = Integer.toHexString(data[i] & 255);
                if (s.length() == 1) {
                    sb.append('0');
                }
                sb.append(s);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WifiP2pServiceInfo)) {
            return false;
        }
        return this.mQueryList.equals(((WifiP2pServiceInfo) o).mQueryList);
    }

    public int hashCode() {
        return (17 * 31) + (this.mQueryList == null ? 0 : this.mQueryList.hashCode());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.mQueryList);
    }
}
