package android.net.wifi.hotspot2.pps;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Policy implements Parcelable {
    public static final Parcelable.Creator<Policy> CREATOR = new Parcelable.Creator<Policy>() {
        public Policy createFromParcel(Parcel in) {
            Policy policy = new Policy();
            policy.setMinHomeDownlinkBandwidth(in.readLong());
            policy.setMinHomeUplinkBandwidth(in.readLong());
            policy.setMinRoamingDownlinkBandwidth(in.readLong());
            policy.setMinRoamingUplinkBandwidth(in.readLong());
            policy.setExcludedSsidList(in.createStringArray());
            policy.setRequiredProtoPortMap(readProtoPortMap(in));
            policy.setMaximumBssLoadValue(in.readInt());
            policy.setPreferredRoamingPartnerList(readRoamingPartnerList(in));
            policy.setPolicyUpdate((UpdateParameter) in.readParcelable((ClassLoader) null));
            return policy;
        }

        public Policy[] newArray(int size) {
            return new Policy[size];
        }

        private Map<Integer, String> readProtoPortMap(Parcel in) {
            int size = in.readInt();
            if (size == -1) {
                return null;
            }
            Map<Integer, String> protoPortMap = new HashMap<>(size);
            for (int i = 0; i < size; i++) {
                int key = in.readInt();
                protoPortMap.put(Integer.valueOf(key), in.readString());
            }
            return protoPortMap;
        }

        private List<RoamingPartner> readRoamingPartnerList(Parcel in) {
            int size = in.readInt();
            if (size == -1) {
                return null;
            }
            List<RoamingPartner> partnerList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                partnerList.add((RoamingPartner) in.readParcelable((ClassLoader) null));
            }
            return partnerList;
        }
    };
    private static final int MAX_EXCLUSION_SSIDS = 128;
    private static final int MAX_PORT_STRING_BYTES = 64;
    private static final int MAX_SSID_BYTES = 32;
    private static final int NULL_VALUE = -1;
    private static final String TAG = "Policy";
    private String[] mExcludedSsidList = null;
    private int mMaximumBssLoadValue = Integer.MIN_VALUE;
    private long mMinHomeDownlinkBandwidth = Long.MIN_VALUE;
    private long mMinHomeUplinkBandwidth = Long.MIN_VALUE;
    private long mMinRoamingDownlinkBandwidth = Long.MIN_VALUE;
    private long mMinRoamingUplinkBandwidth = Long.MIN_VALUE;
    private UpdateParameter mPolicyUpdate = null;
    private List<RoamingPartner> mPreferredRoamingPartnerList = null;
    private Map<Integer, String> mRequiredProtoPortMap = null;

    public void setMinHomeDownlinkBandwidth(long minHomeDownlinkBandwidth) {
        this.mMinHomeDownlinkBandwidth = minHomeDownlinkBandwidth;
    }

    public long getMinHomeDownlinkBandwidth() {
        return this.mMinHomeDownlinkBandwidth;
    }

    public void setMinHomeUplinkBandwidth(long minHomeUplinkBandwidth) {
        this.mMinHomeUplinkBandwidth = minHomeUplinkBandwidth;
    }

    public long getMinHomeUplinkBandwidth() {
        return this.mMinHomeUplinkBandwidth;
    }

    public void setMinRoamingDownlinkBandwidth(long minRoamingDownlinkBandwidth) {
        this.mMinRoamingDownlinkBandwidth = minRoamingDownlinkBandwidth;
    }

    public long getMinRoamingDownlinkBandwidth() {
        return this.mMinRoamingDownlinkBandwidth;
    }

    public void setMinRoamingUplinkBandwidth(long minRoamingUplinkBandwidth) {
        this.mMinRoamingUplinkBandwidth = minRoamingUplinkBandwidth;
    }

    public long getMinRoamingUplinkBandwidth() {
        return this.mMinRoamingUplinkBandwidth;
    }

    public void setExcludedSsidList(String[] excludedSsidList) {
        this.mExcludedSsidList = excludedSsidList;
    }

    public String[] getExcludedSsidList() {
        return this.mExcludedSsidList;
    }

    public void setRequiredProtoPortMap(Map<Integer, String> requiredProtoPortMap) {
        this.mRequiredProtoPortMap = requiredProtoPortMap;
    }

    public Map<Integer, String> getRequiredProtoPortMap() {
        return this.mRequiredProtoPortMap;
    }

    public void setMaximumBssLoadValue(int maximumBssLoadValue) {
        this.mMaximumBssLoadValue = maximumBssLoadValue;
    }

    public int getMaximumBssLoadValue() {
        return this.mMaximumBssLoadValue;
    }

    public static final class RoamingPartner implements Parcelable {
        public static final Parcelable.Creator<RoamingPartner> CREATOR = new Parcelable.Creator<RoamingPartner>() {
            public RoamingPartner createFromParcel(Parcel in) {
                RoamingPartner roamingPartner = new RoamingPartner();
                roamingPartner.setFqdn(in.readString());
                roamingPartner.setFqdnExactMatch(in.readInt() != 0);
                roamingPartner.setPriority(in.readInt());
                roamingPartner.setCountries(in.readString());
                return roamingPartner;
            }

            public RoamingPartner[] newArray(int size) {
                return new RoamingPartner[size];
            }
        };
        private String mCountries = null;
        private String mFqdn = null;
        private boolean mFqdnExactMatch = false;
        private int mPriority = Integer.MIN_VALUE;

        public void setFqdn(String fqdn) {
            this.mFqdn = fqdn;
        }

        public String getFqdn() {
            return this.mFqdn;
        }

        public void setFqdnExactMatch(boolean fqdnExactMatch) {
            this.mFqdnExactMatch = fqdnExactMatch;
        }

        public boolean getFqdnExactMatch() {
            return this.mFqdnExactMatch;
        }

        public void setPriority(int priority) {
            this.mPriority = priority;
        }

        public int getPriority() {
            return this.mPriority;
        }

        public void setCountries(String countries) {
            this.mCountries = countries;
        }

        public String getCountries() {
            return this.mCountries;
        }

        public RoamingPartner() {
        }

        public RoamingPartner(RoamingPartner source) {
            if (source != null) {
                this.mFqdn = source.mFqdn;
                this.mFqdnExactMatch = source.mFqdnExactMatch;
                this.mPriority = source.mPriority;
                this.mCountries = source.mCountries;
            }
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mFqdn);
            dest.writeInt(this.mFqdnExactMatch ? 1 : 0);
            dest.writeInt(this.mPriority);
            dest.writeString(this.mCountries);
        }

        public boolean equals(Object thatObject) {
            if (this == thatObject) {
                return true;
            }
            if (!(thatObject instanceof RoamingPartner)) {
                return false;
            }
            RoamingPartner that = (RoamingPartner) thatObject;
            if (!TextUtils.equals(this.mFqdn, that.mFqdn) || this.mFqdnExactMatch != that.mFqdnExactMatch || this.mPriority != that.mPriority || !TextUtils.equals(this.mCountries, that.mCountries)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mFqdn, Boolean.valueOf(this.mFqdnExactMatch), Integer.valueOf(this.mPriority), this.mCountries});
        }

        public String toString() {
            return "FQDN: " + this.mFqdn + "\n" + "ExactMatch: " + "mFqdnExactMatch" + "\n" + "Priority: " + this.mPriority + "\n" + "Countries: " + this.mCountries + "\n";
        }

        public boolean validate() {
            if (TextUtils.isEmpty(this.mFqdn)) {
                Log.d(Policy.TAG, "Missing FQDN");
                return false;
            } else if (!TextUtils.isEmpty(this.mCountries)) {
                return true;
            } else {
                Log.d(Policy.TAG, "Missing countries");
                return false;
            }
        }
    }

    public void setPreferredRoamingPartnerList(List<RoamingPartner> partnerList) {
        this.mPreferredRoamingPartnerList = partnerList;
    }

    public List<RoamingPartner> getPreferredRoamingPartnerList() {
        return this.mPreferredRoamingPartnerList;
    }

    public void setPolicyUpdate(UpdateParameter policyUpdate) {
        this.mPolicyUpdate = policyUpdate;
    }

    public UpdateParameter getPolicyUpdate() {
        return this.mPolicyUpdate;
    }

    public Policy() {
    }

    public Policy(Policy source) {
        if (source != null) {
            this.mMinHomeDownlinkBandwidth = source.mMinHomeDownlinkBandwidth;
            this.mMinHomeUplinkBandwidth = source.mMinHomeUplinkBandwidth;
            this.mMinRoamingDownlinkBandwidth = source.mMinRoamingDownlinkBandwidth;
            this.mMinRoamingUplinkBandwidth = source.mMinRoamingUplinkBandwidth;
            this.mMaximumBssLoadValue = source.mMaximumBssLoadValue;
            if (source.mExcludedSsidList != null) {
                this.mExcludedSsidList = (String[]) Arrays.copyOf(source.mExcludedSsidList, source.mExcludedSsidList.length);
            }
            if (source.mRequiredProtoPortMap != null) {
                this.mRequiredProtoPortMap = Collections.unmodifiableMap(source.mRequiredProtoPortMap);
            }
            if (source.mPreferredRoamingPartnerList != null) {
                this.mPreferredRoamingPartnerList = Collections.unmodifiableList(source.mPreferredRoamingPartnerList);
            }
            if (source.mPolicyUpdate != null) {
                this.mPolicyUpdate = new UpdateParameter(source.mPolicyUpdate);
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mMinHomeDownlinkBandwidth);
        dest.writeLong(this.mMinHomeUplinkBandwidth);
        dest.writeLong(this.mMinRoamingDownlinkBandwidth);
        dest.writeLong(this.mMinRoamingUplinkBandwidth);
        dest.writeStringArray(this.mExcludedSsidList);
        writeProtoPortMap(dest, this.mRequiredProtoPortMap);
        dest.writeInt(this.mMaximumBssLoadValue);
        writeRoamingPartnerList(dest, flags, this.mPreferredRoamingPartnerList);
        dest.writeParcelable(this.mPolicyUpdate, flags);
    }

    public boolean equals(Object thatObject) {
        if (this == thatObject) {
            return true;
        }
        if (!(thatObject instanceof Policy)) {
            return false;
        }
        Policy that = (Policy) thatObject;
        if (this.mMinHomeDownlinkBandwidth == that.mMinHomeDownlinkBandwidth && this.mMinHomeUplinkBandwidth == that.mMinHomeUplinkBandwidth && this.mMinRoamingDownlinkBandwidth == that.mMinRoamingDownlinkBandwidth && this.mMinRoamingUplinkBandwidth == that.mMinRoamingUplinkBandwidth && Arrays.equals(this.mExcludedSsidList, that.mExcludedSsidList) && (this.mRequiredProtoPortMap != null ? this.mRequiredProtoPortMap.equals(that.mRequiredProtoPortMap) : that.mRequiredProtoPortMap == null) && this.mMaximumBssLoadValue == that.mMaximumBssLoadValue && (this.mPreferredRoamingPartnerList != null ? this.mPreferredRoamingPartnerList.equals(that.mPreferredRoamingPartnerList) : that.mPreferredRoamingPartnerList == null)) {
            if (this.mPolicyUpdate == null) {
                if (that.mPolicyUpdate == null) {
                    return true;
                }
            } else if (this.mPolicyUpdate.equals(that.mPolicyUpdate)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Long.valueOf(this.mMinHomeDownlinkBandwidth), Long.valueOf(this.mMinHomeUplinkBandwidth), Long.valueOf(this.mMinRoamingDownlinkBandwidth), Long.valueOf(this.mMinRoamingUplinkBandwidth), this.mExcludedSsidList, this.mRequiredProtoPortMap, Integer.valueOf(this.mMaximumBssLoadValue), this.mPreferredRoamingPartnerList, this.mPolicyUpdate});
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MinHomeDownlinkBandwidth: ");
        builder.append(this.mMinHomeDownlinkBandwidth);
        builder.append("\n");
        builder.append("MinHomeUplinkBandwidth: ");
        builder.append(this.mMinHomeUplinkBandwidth);
        builder.append("\n");
        builder.append("MinRoamingDownlinkBandwidth: ");
        builder.append(this.mMinRoamingDownlinkBandwidth);
        builder.append("\n");
        builder.append("MinRoamingUplinkBandwidth: ");
        builder.append(this.mMinRoamingUplinkBandwidth);
        builder.append("\n");
        builder.append("ExcludedSSIDList: ");
        builder.append(this.mExcludedSsidList);
        builder.append("\n");
        builder.append("RequiredProtoPortMap: ");
        builder.append(this.mRequiredProtoPortMap);
        builder.append("\n");
        builder.append("MaximumBSSLoadValue: ");
        builder.append(this.mMaximumBssLoadValue);
        builder.append("\n");
        builder.append("PreferredRoamingPartnerList: ");
        builder.append(this.mPreferredRoamingPartnerList);
        builder.append("\n");
        if (this.mPolicyUpdate != null) {
            builder.append("PolicyUpdate Begin ---\n");
            builder.append(this.mPolicyUpdate);
            builder.append("PolicyUpdate End ---\n");
        }
        return builder.toString();
    }

    public boolean validate() {
        if (this.mPolicyUpdate == null) {
            Log.d(TAG, "PolicyUpdate not specified");
            return false;
        } else if (!this.mPolicyUpdate.validate()) {
            return false;
        } else {
            if (this.mExcludedSsidList != null) {
                if (this.mExcludedSsidList.length > 128) {
                    Log.d(TAG, "SSID exclusion list size exceeded the max: " + this.mExcludedSsidList.length);
                    return false;
                }
                for (String ssid : this.mExcludedSsidList) {
                    if (ssid.getBytes(StandardCharsets.UTF_8).length > 32) {
                        Log.d(TAG, "Invalid SSID: " + ssid);
                        return false;
                    }
                }
            }
            if (this.mRequiredProtoPortMap != null) {
                for (Map.Entry<Integer, String> entry : this.mRequiredProtoPortMap.entrySet()) {
                    String portNumber = entry.getValue();
                    if (portNumber.getBytes(StandardCharsets.UTF_8).length > 64) {
                        Log.d(TAG, "PortNumber string bytes exceeded the max: " + portNumber);
                        return false;
                    }
                }
            }
            if (this.mPreferredRoamingPartnerList == null) {
                return true;
            }
            for (RoamingPartner partner : this.mPreferredRoamingPartnerList) {
                if (!partner.validate()) {
                    return false;
                }
            }
            return true;
        }
    }

    private static void writeProtoPortMap(Parcel dest, Map<Integer, String> protoPortMap) {
        if (protoPortMap == null) {
            dest.writeInt(-1);
            return;
        }
        dest.writeInt(protoPortMap.size());
        for (Map.Entry<Integer, String> entry : protoPortMap.entrySet()) {
            dest.writeInt(entry.getKey().intValue());
            dest.writeString(entry.getValue());
        }
    }

    private static void writeRoamingPartnerList(Parcel dest, int flags, List<RoamingPartner> partnerList) {
        if (partnerList == null) {
            dest.writeInt(-1);
            return;
        }
        dest.writeInt(partnerList.size());
        for (RoamingPartner partner : partnerList) {
            dest.writeParcelable(partner, flags);
        }
    }
}
