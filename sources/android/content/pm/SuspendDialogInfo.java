package android.content.pm;

import android.annotation.SystemApi;
import android.content.res.ResourceId;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Slog;
import com.android.internal.util.Preconditions;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

@SystemApi
public final class SuspendDialogInfo implements Parcelable {
    public static final Parcelable.Creator<SuspendDialogInfo> CREATOR = new Parcelable.Creator<SuspendDialogInfo>() {
        public SuspendDialogInfo createFromParcel(Parcel source) {
            return new SuspendDialogInfo(source);
        }

        public SuspendDialogInfo[] newArray(int size) {
            return new SuspendDialogInfo[size];
        }
    };
    private static final String TAG = SuspendDialogInfo.class.getSimpleName();
    private static final String XML_ATTR_BUTTON_TEXT_RES_ID = "buttonTextResId";
    private static final String XML_ATTR_DIALOG_MESSAGE = "dialogMessage";
    private static final String XML_ATTR_DIALOG_MESSAGE_RES_ID = "dialogMessageResId";
    private static final String XML_ATTR_ICON_RES_ID = "iconResId";
    private static final String XML_ATTR_TITLE_RES_ID = "titleResId";
    private final String mDialogMessage;
    private final int mDialogMessageResId;
    private final int mIconResId;
    private final int mNeutralButtonTextResId;
    private final int mTitleResId;

    public int getIconResId() {
        return this.mIconResId;
    }

    public int getTitleResId() {
        return this.mTitleResId;
    }

    public int getDialogMessageResId() {
        return this.mDialogMessageResId;
    }

    public String getDialogMessage() {
        return this.mDialogMessage;
    }

    public int getNeutralButtonTextResId() {
        return this.mNeutralButtonTextResId;
    }

    public void saveToXml(XmlSerializer out) throws IOException {
        if (this.mIconResId != 0) {
            XmlUtils.writeIntAttribute(out, "iconResId", this.mIconResId);
        }
        if (this.mTitleResId != 0) {
            XmlUtils.writeIntAttribute(out, XML_ATTR_TITLE_RES_ID, this.mTitleResId);
        }
        if (this.mDialogMessageResId != 0) {
            XmlUtils.writeIntAttribute(out, XML_ATTR_DIALOG_MESSAGE_RES_ID, this.mDialogMessageResId);
        } else {
            XmlUtils.writeStringAttribute(out, XML_ATTR_DIALOG_MESSAGE, this.mDialogMessage);
        }
        if (this.mNeutralButtonTextResId != 0) {
            XmlUtils.writeIntAttribute(out, XML_ATTR_BUTTON_TEXT_RES_ID, this.mNeutralButtonTextResId);
        }
    }

    public static SuspendDialogInfo restoreFromXml(XmlPullParser in) {
        Builder dialogInfoBuilder = new Builder();
        try {
            int iconId = XmlUtils.readIntAttribute(in, "iconResId", 0);
            int titleId = XmlUtils.readIntAttribute(in, XML_ATTR_TITLE_RES_ID, 0);
            int buttonTextId = XmlUtils.readIntAttribute(in, XML_ATTR_BUTTON_TEXT_RES_ID, 0);
            int dialogMessageResId = XmlUtils.readIntAttribute(in, XML_ATTR_DIALOG_MESSAGE_RES_ID, 0);
            String dialogMessage = XmlUtils.readStringAttribute(in, XML_ATTR_DIALOG_MESSAGE);
            if (iconId != 0) {
                dialogInfoBuilder.setIcon(iconId);
            }
            if (titleId != 0) {
                dialogInfoBuilder.setTitle(titleId);
            }
            if (buttonTextId != 0) {
                dialogInfoBuilder.setNeutralButtonText(buttonTextId);
            }
            if (dialogMessageResId != 0) {
                dialogInfoBuilder.setMessage(dialogMessageResId);
            } else if (dialogMessage != null) {
                dialogInfoBuilder.setMessage(dialogMessage);
            }
        } catch (Exception e) {
            Slog.e(TAG, "Exception while parsing from xml. Some fields may default", e);
        }
        return dialogInfoBuilder.build();
    }

    public int hashCode() {
        return (((((((this.mIconResId * 31) + this.mTitleResId) * 31) + this.mNeutralButtonTextResId) * 31) + this.mDialogMessageResId) * 31) + Objects.hashCode(this.mDialogMessage);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SuspendDialogInfo)) {
            return false;
        }
        SuspendDialogInfo otherDialogInfo = (SuspendDialogInfo) obj;
        if (this.mIconResId == otherDialogInfo.mIconResId && this.mTitleResId == otherDialogInfo.mTitleResId && this.mDialogMessageResId == otherDialogInfo.mDialogMessageResId && this.mNeutralButtonTextResId == otherDialogInfo.mNeutralButtonTextResId && Objects.equals(this.mDialogMessage, otherDialogInfo.mDialogMessage)) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("SuspendDialogInfo: {");
        if (this.mIconResId != 0) {
            builder.append("mIconId = 0x");
            builder.append(Integer.toHexString(this.mIconResId));
            builder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        if (this.mTitleResId != 0) {
            builder.append("mTitleResId = 0x");
            builder.append(Integer.toHexString(this.mTitleResId));
            builder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        if (this.mNeutralButtonTextResId != 0) {
            builder.append("mNeutralButtonTextResId = 0x");
            builder.append(Integer.toHexString(this.mNeutralButtonTextResId));
            builder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        if (this.mDialogMessageResId != 0) {
            builder.append("mDialogMessageResId = 0x");
            builder.append(Integer.toHexString(this.mDialogMessageResId));
            builder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        } else if (this.mDialogMessage != null) {
            builder.append("mDialogMessage = \"");
            builder.append(this.mDialogMessage);
            builder.append("\" ");
        }
        builder.append("}");
        return builder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeInt(this.mIconResId);
        dest.writeInt(this.mTitleResId);
        dest.writeInt(this.mDialogMessageResId);
        dest.writeString(this.mDialogMessage);
        dest.writeInt(this.mNeutralButtonTextResId);
    }

    private SuspendDialogInfo(Parcel source) {
        this.mIconResId = source.readInt();
        this.mTitleResId = source.readInt();
        this.mDialogMessageResId = source.readInt();
        this.mDialogMessage = source.readString();
        this.mNeutralButtonTextResId = source.readInt();
    }

    SuspendDialogInfo(Builder b) {
        this.mIconResId = b.mIconResId;
        this.mTitleResId = b.mTitleResId;
        this.mDialogMessageResId = b.mDialogMessageResId;
        this.mDialogMessage = this.mDialogMessageResId == 0 ? b.mDialogMessage : null;
        this.mNeutralButtonTextResId = b.mNeutralButtonTextResId;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public String mDialogMessage;
        /* access modifiers changed from: private */
        public int mDialogMessageResId = 0;
        /* access modifiers changed from: private */
        public int mIconResId = 0;
        /* access modifiers changed from: private */
        public int mNeutralButtonTextResId = 0;
        /* access modifiers changed from: private */
        public int mTitleResId = 0;

        public Builder setIcon(int resId) {
            Preconditions.checkArgument(ResourceId.isValid(resId), "Invalid resource id provided");
            this.mIconResId = resId;
            return this;
        }

        public Builder setTitle(int resId) {
            Preconditions.checkArgument(ResourceId.isValid(resId), "Invalid resource id provided");
            this.mTitleResId = resId;
            return this;
        }

        public Builder setMessage(String message) {
            Preconditions.checkStringNotEmpty(message, "Message cannot be null or empty");
            this.mDialogMessage = message;
            return this;
        }

        public Builder setMessage(int resId) {
            Preconditions.checkArgument(ResourceId.isValid(resId), "Invalid resource id provided");
            this.mDialogMessageResId = resId;
            return this;
        }

        public Builder setNeutralButtonText(int resId) {
            Preconditions.checkArgument(ResourceId.isValid(resId), "Invalid resource id provided");
            this.mNeutralButtonTextResId = resId;
            return this;
        }

        public SuspendDialogInfo build() {
            return new SuspendDialogInfo(this);
        }
    }
}
