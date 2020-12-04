package android.net.wifi;

import android.net.MacAddress;
import android.net.MatchAllNetworkSpecifier;
import android.net.NetworkSpecifier;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.android.internal.util.Preconditions;
import java.util.Objects;

public final class WifiNetworkAgentSpecifier extends NetworkSpecifier implements Parcelable {
    public static final Parcelable.Creator<WifiNetworkAgentSpecifier> CREATOR = new Parcelable.Creator<WifiNetworkAgentSpecifier>() {
        public WifiNetworkAgentSpecifier createFromParcel(Parcel in) {
            return new WifiNetworkAgentSpecifier((WifiConfiguration) in.readParcelable((ClassLoader) null), in.readInt(), in.readString());
        }

        public WifiNetworkAgentSpecifier[] newArray(int size) {
            return new WifiNetworkAgentSpecifier[size];
        }
    };
    private final String mOriginalRequestorPackageName;
    private final int mOriginalRequestorUid;
    private final WifiConfiguration mWifiConfiguration;

    public WifiNetworkAgentSpecifier(WifiConfiguration wifiConfiguration, int originalRequestorUid, String originalRequestorPackageName) {
        Preconditions.checkNotNull(wifiConfiguration);
        this.mWifiConfiguration = wifiConfiguration;
        this.mOriginalRequestorUid = originalRequestorUid;
        this.mOriginalRequestorPackageName = originalRequestorPackageName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mWifiConfiguration, flags);
        dest.writeInt(this.mOriginalRequestorUid);
        dest.writeString(this.mOriginalRequestorPackageName);
    }

    public boolean satisfiedBy(NetworkSpecifier other) {
        if (this == other || other == null || (other instanceof MatchAllNetworkSpecifier)) {
            return true;
        }
        if (other instanceof WifiNetworkSpecifier) {
            return satisfiesNetworkSpecifier((WifiNetworkSpecifier) other);
        }
        return equals(other);
    }

    public boolean satisfiesNetworkSpecifier(WifiNetworkSpecifier ns) {
        Preconditions.checkNotNull(ns);
        Preconditions.checkNotNull(ns.ssidPatternMatcher);
        Preconditions.checkNotNull(ns.bssidPatternMatcher);
        Preconditions.checkNotNull(ns.wifiConfiguration.allowedKeyManagement);
        Preconditions.checkNotNull(this.mWifiConfiguration.SSID);
        Preconditions.checkNotNull(this.mWifiConfiguration.BSSID);
        Preconditions.checkNotNull(this.mWifiConfiguration.allowedKeyManagement);
        String ssidWithQuotes = this.mWifiConfiguration.SSID;
        Preconditions.checkState(ssidWithQuotes.startsWith("\"") && ssidWithQuotes.endsWith("\""));
        return ns.ssidPatternMatcher.match(ssidWithQuotes.substring(1, ssidWithQuotes.length() - 1)) && MacAddress.fromString(this.mWifiConfiguration.BSSID).matches((MacAddress) ns.bssidPatternMatcher.first, (MacAddress) ns.bssidPatternMatcher.second) && ns.wifiConfiguration.allowedKeyManagement.equals(this.mWifiConfiguration.allowedKeyManagement) && ns.requestorUid == this.mOriginalRequestorUid && TextUtils.equals(ns.requestorPackageName, this.mOriginalRequestorPackageName);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mWifiConfiguration.SSID, this.mWifiConfiguration.BSSID, this.mWifiConfiguration.allowedKeyManagement, Integer.valueOf(this.mOriginalRequestorUid), this.mOriginalRequestorPackageName});
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiNetworkAgentSpecifier)) {
            return false;
        }
        WifiNetworkAgentSpecifier lhs = (WifiNetworkAgentSpecifier) obj;
        if (!Objects.equals(this.mWifiConfiguration.SSID, lhs.mWifiConfiguration.SSID) || !Objects.equals(this.mWifiConfiguration.BSSID, lhs.mWifiConfiguration.BSSID) || !Objects.equals(this.mWifiConfiguration.allowedKeyManagement, lhs.mWifiConfiguration.allowedKeyManagement) || this.mOriginalRequestorUid != lhs.mOriginalRequestorUid || !TextUtils.equals(this.mOriginalRequestorPackageName, lhs.mOriginalRequestorPackageName)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "WifiNetworkAgentSpecifier [" + "WifiConfiguration=" + ", SSID=" + this.mWifiConfiguration.SSID + ", BSSID=" + this.mWifiConfiguration.BSSID + ", mOriginalRequestorUid=" + this.mOriginalRequestorUid + ", mOriginalRequestorPackageName=" + this.mOriginalRequestorPackageName + "]";
    }

    public void assertValidFromUid(int requestorUid) {
        throw new IllegalStateException("WifiNetworkAgentSpecifier should never be used for requests.");
    }

    public NetworkSpecifier redact() {
        return null;
    }
}
