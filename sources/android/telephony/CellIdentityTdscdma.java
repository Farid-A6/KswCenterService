package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.gsm.GsmCellLocation;
import java.util.Objects;

public final class CellIdentityTdscdma extends CellIdentity {
    public static final Parcelable.Creator<CellIdentityTdscdma> CREATOR = new Parcelable.Creator<CellIdentityTdscdma>() {
        public CellIdentityTdscdma createFromParcel(Parcel in) {
            in.readInt();
            return CellIdentityTdscdma.createFromParcelBody(in);
        }

        public CellIdentityTdscdma[] newArray(int size) {
            return new CellIdentityTdscdma[size];
        }
    };
    private static final boolean DBG = false;
    private static final int MAX_CID = 268435455;
    private static final int MAX_CPID = 127;
    private static final int MAX_LAC = 65535;
    private static final int MAX_UARFCN = 65535;
    private static final String TAG = CellIdentityTdscdma.class.getSimpleName();
    private final int mCid;
    private final int mCpid;
    private final int mLac;
    private final int mUarfcn;

    public CellIdentityTdscdma() {
        super(TAG, 5, (String) null, (String) null, (String) null, (String) null);
        this.mLac = Integer.MAX_VALUE;
        this.mCid = Integer.MAX_VALUE;
        this.mCpid = Integer.MAX_VALUE;
        this.mUarfcn = Integer.MAX_VALUE;
    }

    public CellIdentityTdscdma(String mcc, String mnc, int lac, int cid, int cpid, int uarfcn, String alphal, String alphas) {
        super(TAG, 5, mcc, mnc, alphal, alphas);
        this.mLac = inRangeOrUnavailable(lac, 0, 65535);
        this.mCid = inRangeOrUnavailable(cid, 0, (int) MAX_CID);
        this.mCpid = inRangeOrUnavailable(cpid, 0, 127);
        this.mUarfcn = inRangeOrUnavailable(uarfcn, 0, 65535);
    }

    private CellIdentityTdscdma(CellIdentityTdscdma cid) {
        this(cid.mMccStr, cid.mMncStr, cid.mLac, cid.mCid, cid.mCpid, cid.mUarfcn, cid.mAlphaLong, cid.mAlphaShort);
    }

    public CellIdentityTdscdma(android.hardware.radio.V1_0.CellIdentityTdscdma cid) {
        this(cid.mcc, cid.mnc, cid.lac, cid.cid, cid.cpid, Integer.MAX_VALUE, "", "");
    }

    public CellIdentityTdscdma(android.hardware.radio.V1_2.CellIdentityTdscdma cid) {
        this(cid.base.mcc, cid.base.mnc, cid.base.lac, cid.base.cid, cid.base.cpid, cid.uarfcn, cid.operatorNames.alphaLong, cid.operatorNames.alphaShort);
    }

    public CellIdentityTdscdma sanitizeLocationInfo() {
        return new CellIdentityTdscdma(this.mMccStr, this.mMncStr, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, this.mAlphaLong, this.mAlphaShort);
    }

    /* access modifiers changed from: package-private */
    public CellIdentityTdscdma copy() {
        return new CellIdentityTdscdma(this);
    }

    public String getMccString() {
        return this.mMccStr;
    }

    public String getMncString() {
        return this.mMncStr;
    }

    public String getMobileNetworkOperator() {
        if (this.mMccStr == null || this.mMncStr == null) {
            return null;
        }
        return this.mMccStr + this.mMncStr;
    }

    public int getLac() {
        return this.mLac;
    }

    public int getCid() {
        return this.mCid;
    }

    public int getCpid() {
        return this.mCpid;
    }

    public int getUarfcn() {
        return this.mUarfcn;
    }

    public int getChannelNumber() {
        return this.mUarfcn;
    }

    public GsmCellLocation asCellLocation() {
        GsmCellLocation cl = new GsmCellLocation();
        cl.setLacAndCid(this.mLac != Integer.MAX_VALUE ? this.mLac : -1, this.mCid != Integer.MAX_VALUE ? this.mCid : -1);
        cl.setPsc(-1);
        return cl;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CellIdentityTdscdma)) {
            return false;
        }
        CellIdentityTdscdma o = (CellIdentityTdscdma) other;
        if (this.mLac == o.mLac && this.mCid == o.mCid && this.mCpid == o.mCpid && this.mUarfcn == o.mUarfcn && super.equals(other)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mLac), Integer.valueOf(this.mCid), Integer.valueOf(this.mCpid), Integer.valueOf(this.mUarfcn), Integer.valueOf(super.hashCode())});
    }

    public String toString() {
        return TAG + ":{ mMcc=" + this.mMccStr + " mMnc=" + this.mMncStr + " mAlphaLong=" + this.mAlphaLong + " mAlphaShort=" + this.mAlphaShort + " mLac=" + this.mLac + " mCid=" + this.mCid + " mCpid=" + this.mCpid + " mUarfcn=" + this.mUarfcn + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, 5);
        dest.writeInt(this.mLac);
        dest.writeInt(this.mCid);
        dest.writeInt(this.mCpid);
        dest.writeInt(this.mUarfcn);
    }

    private CellIdentityTdscdma(Parcel in) {
        super(TAG, 5, in);
        this.mLac = in.readInt();
        this.mCid = in.readInt();
        this.mCpid = in.readInt();
        this.mUarfcn = in.readInt();
    }

    protected static CellIdentityTdscdma createFromParcelBody(Parcel in) {
        return new CellIdentityTdscdma(in);
    }
}