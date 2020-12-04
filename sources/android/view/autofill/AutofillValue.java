package android.view.autofill;

import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.Objects;

public final class AutofillValue implements Parcelable {
    public static final Parcelable.Creator<AutofillValue> CREATOR = new Parcelable.Creator<AutofillValue>() {
        public AutofillValue createFromParcel(Parcel source) {
            return new AutofillValue(source);
        }

        public AutofillValue[] newArray(int size) {
            return new AutofillValue[size];
        }
    };
    private static final String TAG = "AutofillValue";
    private final int mType;
    private final Object mValue;

    private AutofillValue(int type, Object value) {
        this.mType = type;
        this.mValue = value;
    }

    public CharSequence getTextValue() {
        boolean isText = isText();
        Preconditions.checkState(isText, "value must be a text value, not type=" + this.mType);
        return (CharSequence) this.mValue;
    }

    public boolean isText() {
        return this.mType == 1;
    }

    public boolean getToggleValue() {
        boolean isToggle = isToggle();
        Preconditions.checkState(isToggle, "value must be a toggle value, not type=" + this.mType);
        return ((Boolean) this.mValue).booleanValue();
    }

    public boolean isToggle() {
        return this.mType == 2;
    }

    public int getListValue() {
        boolean isList = isList();
        Preconditions.checkState(isList, "value must be a list value, not type=" + this.mType);
        return ((Integer) this.mValue).intValue();
    }

    public boolean isList() {
        return this.mType == 3;
    }

    public long getDateValue() {
        boolean isDate = isDate();
        Preconditions.checkState(isDate, "value must be a date value, not type=" + this.mType);
        return ((Long) this.mValue).longValue();
    }

    public boolean isDate() {
        return this.mType == 4;
    }

    public boolean isEmpty() {
        return isText() && ((CharSequence) this.mValue).length() == 0;
    }

    public int hashCode() {
        return this.mType + this.mValue.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AutofillValue other = (AutofillValue) obj;
        if (this.mType != other.mType) {
            return false;
        }
        if (isText()) {
            return this.mValue.toString().equals(other.mValue.toString());
        }
        return Objects.equals(this.mValue, other.mValue);
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[type=");
        sb.append(this.mType);
        StringBuilder string = sb.append(", value=");
        if (isText()) {
            Helper.appendRedacted(string, (CharSequence) this.mValue);
        } else {
            string.append(this.mValue);
        }
        string.append(']');
        return string.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mType);
        switch (this.mType) {
            case 1:
                parcel.writeCharSequence((CharSequence) this.mValue);
                return;
            case 2:
                parcel.writeInt(((Boolean) this.mValue).booleanValue() ? 1 : 0);
                return;
            case 3:
                parcel.writeInt(((Integer) this.mValue).intValue());
                return;
            case 4:
                parcel.writeLong(((Long) this.mValue).longValue());
                return;
            default:
                return;
        }
    }

    private AutofillValue(Parcel parcel) {
        this.mType = parcel.readInt();
        switch (this.mType) {
            case 1:
                this.mValue = parcel.readCharSequence();
                return;
            case 2:
                this.mValue = Boolean.valueOf(parcel.readInt() != 0);
                return;
            case 3:
                this.mValue = Integer.valueOf(parcel.readInt());
                return;
            case 4:
                this.mValue = Long.valueOf(parcel.readLong());
                return;
            default:
                throw new IllegalArgumentException("type=" + this.mType + " not valid");
        }
    }

    public static AutofillValue forText(CharSequence value) {
        if (Helper.sVerbose && !Looper.getMainLooper().isCurrentThread()) {
            Log.v(TAG, "forText() not called on main thread: " + Thread.currentThread());
        }
        if (value == null) {
            return null;
        }
        return new AutofillValue(1, (Object) TextUtils.trimNoCopySpans(value));
    }

    public static AutofillValue forToggle(boolean value) {
        return new AutofillValue(2, (Object) Boolean.valueOf(value));
    }

    public static AutofillValue forList(int value) {
        return new AutofillValue(3, (Object) Integer.valueOf(value));
    }

    public static AutofillValue forDate(long value) {
        return new AutofillValue(4, (Object) Long.valueOf(value));
    }
}
