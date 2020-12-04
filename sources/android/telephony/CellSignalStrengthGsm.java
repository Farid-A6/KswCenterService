package android.telephony;

import android.annotation.UnsupportedAppUsage;
import android.hardware.radio.V1_0.GsmSignalStrength;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import java.util.Objects;

public final class CellSignalStrengthGsm extends CellSignalStrength implements Parcelable {
    public static final Parcelable.Creator<CellSignalStrengthGsm> CREATOR = new Parcelable.Creator<CellSignalStrengthGsm>() {
        public CellSignalStrengthGsm createFromParcel(Parcel in) {
            return new CellSignalStrengthGsm(in);
        }

        public CellSignalStrengthGsm[] newArray(int size) {
            return new CellSignalStrengthGsm[size];
        }
    };
    private static final boolean DBG = false;
    private static final int GSM_RSSI_GOOD = -97;
    private static final int GSM_RSSI_GREAT = -89;
    private static final int GSM_RSSI_MAX = -51;
    private static final int GSM_RSSI_MIN = -113;
    private static final int GSM_RSSI_MODERATE = -103;
    private static final int GSM_RSSI_POOR = -107;
    private static final String LOG_TAG = "CellSignalStrengthGsm";
    private static final CellSignalStrengthGsm sInvalid = new CellSignalStrengthGsm();
    private static final int[] sRssiThresholds = {-107, -103, -97, -89};
    @UnsupportedAppUsage
    private int mBitErrorRate;
    private int mLevel;
    private int mRssi;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mTimingAdvance;

    @UnsupportedAppUsage
    public CellSignalStrengthGsm() {
        setDefaultValues();
    }

    public CellSignalStrengthGsm(int rssi, int ber, int ta) {
        this.mRssi = inRangeOrUnavailable(rssi, -113, -51);
        this.mBitErrorRate = inRangeOrUnavailable(ber, 0, 7, 99);
        this.mTimingAdvance = inRangeOrUnavailable(ta, 0, 219);
        updateLevel((PersistableBundle) null, (ServiceState) null);
    }

    public CellSignalStrengthGsm(GsmSignalStrength gsm) {
        this(getRssiDbmFromAsu(gsm.signalStrength), gsm.bitErrorRate, gsm.timingAdvance);
        if (this.mRssi == Integer.MAX_VALUE) {
            setDefaultValues();
        }
    }

    public CellSignalStrengthGsm(CellSignalStrengthGsm s) {
        copyFrom(s);
    }

    /* access modifiers changed from: protected */
    public void copyFrom(CellSignalStrengthGsm s) {
        this.mRssi = s.mRssi;
        this.mBitErrorRate = s.mBitErrorRate;
        this.mTimingAdvance = s.mTimingAdvance;
        this.mLevel = s.mLevel;
    }

    public CellSignalStrengthGsm copy() {
        return new CellSignalStrengthGsm(this);
    }

    public void setDefaultValues() {
        this.mRssi = Integer.MAX_VALUE;
        this.mBitErrorRate = Integer.MAX_VALUE;
        this.mTimingAdvance = Integer.MAX_VALUE;
        this.mLevel = 0;
    }

    public int getLevel() {
        return this.mLevel;
    }

    public void updateLevel(PersistableBundle cc, ServiceState ss) {
        int[] rssiThresholds;
        if (cc == null) {
            rssiThresholds = sRssiThresholds;
        } else {
            rssiThresholds = cc.getIntArray(CarrierConfigManager.KEY_GSM_RSSI_THRESHOLDS_INT_ARRAY);
            if (rssiThresholds == null || rssiThresholds.length != 4) {
                rssiThresholds = sRssiThresholds;
            }
        }
        int level = 4;
        if (this.mRssi < -113 || this.mRssi > -51) {
            this.mLevel = 0;
            return;
        }
        while (level > 0 && this.mRssi < rssiThresholds[level - 1]) {
            level--;
        }
        this.mLevel = level;
    }

    public int getTimingAdvance() {
        return this.mTimingAdvance;
    }

    public int getDbm() {
        return this.mRssi;
    }

    public int getAsuLevel() {
        return getAsuFromRssiDbm(this.mRssi);
    }

    public int getRssi() {
        return this.mRssi;
    }

    public int getBitErrorRate() {
        return this.mBitErrorRate;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mRssi), Integer.valueOf(this.mBitErrorRate), Integer.valueOf(this.mTimingAdvance)});
    }

    public boolean isValid() {
        return !equals(sInvalid);
    }

    public boolean equals(Object o) {
        if (!(o instanceof CellSignalStrengthGsm)) {
            return false;
        }
        CellSignalStrengthGsm s = (CellSignalStrengthGsm) o;
        if (this.mRssi == s.mRssi && this.mBitErrorRate == s.mBitErrorRate && this.mTimingAdvance == s.mTimingAdvance && this.mLevel == s.mLevel) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "CellSignalStrengthGsm: rssi=" + this.mRssi + " ber=" + this.mBitErrorRate + " mTa=" + this.mTimingAdvance + " mLevel=" + this.mLevel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mRssi);
        dest.writeInt(this.mBitErrorRate);
        dest.writeInt(this.mTimingAdvance);
        dest.writeInt(this.mLevel);
    }

    private CellSignalStrengthGsm(Parcel in) {
        this.mRssi = in.readInt();
        this.mBitErrorRate = in.readInt();
        this.mTimingAdvance = in.readInt();
        this.mLevel = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
