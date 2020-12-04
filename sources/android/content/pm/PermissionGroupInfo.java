package android.content.pm;

import android.annotation.SystemApi;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PermissionGroupInfo extends PackageItemInfo implements Parcelable {
    public static final Parcelable.Creator<PermissionGroupInfo> CREATOR = new Parcelable.Creator<PermissionGroupInfo>() {
        public PermissionGroupInfo createFromParcel(Parcel source) {
            return new PermissionGroupInfo(source);
        }

        public PermissionGroupInfo[] newArray(int size) {
            return new PermissionGroupInfo[size];
        }
    };
    public static final int FLAG_PERSONAL_INFO = 1;
    @SystemApi
    public final int backgroundRequestDetailResourceId;
    @SystemApi
    public final int backgroundRequestResourceId;
    public int descriptionRes;
    public int flags;
    public CharSequence nonLocalizedDescription;
    public int priority;
    @SystemApi
    public final int requestDetailResourceId;
    @SystemApi
    public int requestRes;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    public PermissionGroupInfo(int requestDetailResourceId2, int backgroundRequestResourceId2, int backgroundRequestDetailResourceId2) {
        this.requestDetailResourceId = requestDetailResourceId2;
        this.backgroundRequestResourceId = backgroundRequestResourceId2;
        this.backgroundRequestDetailResourceId = backgroundRequestDetailResourceId2;
    }

    @Deprecated
    public PermissionGroupInfo() {
        this(0, 0, 0);
    }

    @Deprecated
    public PermissionGroupInfo(PermissionGroupInfo orig) {
        super((PackageItemInfo) orig);
        this.descriptionRes = orig.descriptionRes;
        this.requestRes = orig.requestRes;
        this.requestDetailResourceId = orig.requestDetailResourceId;
        this.backgroundRequestResourceId = orig.backgroundRequestResourceId;
        this.backgroundRequestDetailResourceId = orig.backgroundRequestDetailResourceId;
        this.nonLocalizedDescription = orig.nonLocalizedDescription;
        this.flags = orig.flags;
        this.priority = orig.priority;
    }

    public CharSequence loadDescription(PackageManager pm) {
        CharSequence label;
        if (this.nonLocalizedDescription != null) {
            return this.nonLocalizedDescription;
        }
        if (this.descriptionRes == 0 || (label = pm.getText(this.packageName, this.descriptionRes, (ApplicationInfo) null)) == null) {
            return null;
        }
        return label;
    }

    public String toString() {
        return "PermissionGroupInfo{" + Integer.toHexString(System.identityHashCode(this)) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.name + " flgs=0x" + Integer.toHexString(this.flags) + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        super.writeToParcel(dest, parcelableFlags);
        dest.writeInt(this.descriptionRes);
        dest.writeInt(this.requestRes);
        dest.writeInt(this.requestDetailResourceId);
        dest.writeInt(this.backgroundRequestResourceId);
        dest.writeInt(this.backgroundRequestDetailResourceId);
        TextUtils.writeToParcel(this.nonLocalizedDescription, dest, parcelableFlags);
        dest.writeInt(this.flags);
        dest.writeInt(this.priority);
    }

    private PermissionGroupInfo(Parcel source) {
        super(source);
        this.descriptionRes = source.readInt();
        this.requestRes = source.readInt();
        this.requestDetailResourceId = source.readInt();
        this.backgroundRequestResourceId = source.readInt();
        this.backgroundRequestDetailResourceId = source.readInt();
        this.nonLocalizedDescription = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
        this.flags = source.readInt();
        this.priority = source.readInt();
    }
}
