package android.telephony;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

public final class CellInfoNr extends CellInfo {
    public static final Parcelable.Creator<CellInfoNr> CREATOR = new Parcelable.Creator<CellInfoNr>() {
        public CellInfoNr createFromParcel(Parcel in) {
            in.readInt();
            return new CellInfoNr(in);
        }

        public CellInfoNr[] newArray(int size) {
            return new CellInfoNr[size];
        }
    };
    private static final String TAG = "CellInfoNr";
    private final CellIdentityNr mCellIdentity;
    private final CellSignalStrengthNr mCellSignalStrength;

    private CellInfoNr(Parcel in) {
        super(in);
        this.mCellIdentity = CellIdentityNr.CREATOR.createFromParcel(in);
        this.mCellSignalStrength = CellSignalStrengthNr.CREATOR.createFromParcel(in);
    }

    private CellInfoNr(CellInfoNr other, boolean sanitizeLocationInfo) {
        super((CellInfo) other);
        CellIdentityNr cellIdentityNr;
        if (sanitizeLocationInfo) {
            cellIdentityNr = other.mCellIdentity.sanitizeLocationInfo();
        } else {
            cellIdentityNr = other.mCellIdentity;
        }
        this.mCellIdentity = cellIdentityNr;
        this.mCellSignalStrength = other.mCellSignalStrength;
    }

    public CellIdentity getCellIdentity() {
        return this.mCellIdentity;
    }

    public CellSignalStrength getCellSignalStrength() {
        return this.mCellSignalStrength;
    }

    public CellInfo sanitizeLocationInfo() {
        return new CellInfoNr(this, true);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), this.mCellIdentity, this.mCellSignalStrength});
    }

    public boolean equals(Object other) {
        if (!(other instanceof CellInfoNr)) {
            return false;
        }
        CellInfoNr o = (CellInfoNr) other;
        if (!super.equals(o) || !this.mCellIdentity.equals(o.mCellIdentity) || !this.mCellSignalStrength.equals(o.mCellSignalStrength)) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CellInfoNr:{");
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + super.toString());
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.mCellIdentity);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.mCellSignalStrength);
        sb.append(" }");
        return sb.toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags, 6);
        this.mCellIdentity.writeToParcel(dest, flags);
        this.mCellSignalStrength.writeToParcel(dest, flags);
    }

    protected static CellInfoNr createFromParcelBody(Parcel in) {
        return new CellInfoNr(in);
    }
}
