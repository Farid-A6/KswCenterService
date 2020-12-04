package android.text.style;

import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent;
import android.os.Parcel;
import android.text.ParcelableSpan;

public class EasyEditSpan implements ParcelableSpan {
    public static final String EXTRA_TEXT_CHANGED_TYPE = "android.text.style.EXTRA_TEXT_CHANGED_TYPE";
    public static final int TEXT_DELETED = 1;
    public static final int TEXT_MODIFIED = 2;
    private boolean mDeleteEnabled;
    private final PendingIntent mPendingIntent;

    public EasyEditSpan() {
        this.mPendingIntent = null;
        this.mDeleteEnabled = true;
    }

    public EasyEditSpan(PendingIntent pendingIntent) {
        this.mPendingIntent = pendingIntent;
        this.mDeleteEnabled = true;
    }

    public EasyEditSpan(Parcel source) {
        this.mPendingIntent = (PendingIntent) source.readParcelable((ClassLoader) null);
        this.mDeleteEnabled = source.readByte() != 1 ? false : true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    public void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeParcelable(this.mPendingIntent, 0);
        dest.writeByte(this.mDeleteEnabled ? (byte) 1 : 0);
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    public int getSpanTypeIdInternal() {
        return 22;
    }

    @UnsupportedAppUsage
    public boolean isDeleteEnabled() {
        return this.mDeleteEnabled;
    }

    @UnsupportedAppUsage
    public void setDeleteEnabled(boolean value) {
        this.mDeleteEnabled = value;
    }

    @UnsupportedAppUsage
    public PendingIntent getPendingIntent() {
        return this.mPendingIntent;
    }
}
