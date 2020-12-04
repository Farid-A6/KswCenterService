package android.security.keystore.recovery;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

@SystemApi
public final class KeyChainProtectionParams implements Parcelable {
    public static final Parcelable.Creator<KeyChainProtectionParams> CREATOR = new Parcelable.Creator<KeyChainProtectionParams>() {
        public KeyChainProtectionParams createFromParcel(Parcel in) {
            return new KeyChainProtectionParams(in);
        }

        public KeyChainProtectionParams[] newArray(int length) {
            return new KeyChainProtectionParams[length];
        }
    };
    public static final int TYPE_LOCKSCREEN = 100;
    public static final int UI_FORMAT_PASSWORD = 2;
    public static final int UI_FORMAT_PATTERN = 3;
    public static final int UI_FORMAT_PIN = 1;
    /* access modifiers changed from: private */
    public KeyDerivationParams mKeyDerivationParams;
    /* access modifiers changed from: private */
    public Integer mLockScreenUiFormat;
    /* access modifiers changed from: private */
    public byte[] mSecret;
    /* access modifiers changed from: private */
    public Integer mUserSecretType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface LockScreenUiFormat {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface UserSecretType {
    }

    private KeyChainProtectionParams() {
    }

    public int getUserSecretType() {
        return this.mUserSecretType.intValue();
    }

    public int getLockScreenUiFormat() {
        return this.mLockScreenUiFormat.intValue();
    }

    public KeyDerivationParams getKeyDerivationParams() {
        return this.mKeyDerivationParams;
    }

    public byte[] getSecret() {
        return this.mSecret;
    }

    public static class Builder {
        private KeyChainProtectionParams mInstance = new KeyChainProtectionParams();

        public Builder setUserSecretType(int userSecretType) {
            Integer unused = this.mInstance.mUserSecretType = Integer.valueOf(userSecretType);
            return this;
        }

        public Builder setLockScreenUiFormat(int lockScreenUiFormat) {
            Integer unused = this.mInstance.mLockScreenUiFormat = Integer.valueOf(lockScreenUiFormat);
            return this;
        }

        public Builder setKeyDerivationParams(KeyDerivationParams keyDerivationParams) {
            KeyDerivationParams unused = this.mInstance.mKeyDerivationParams = keyDerivationParams;
            return this;
        }

        public Builder setSecret(byte[] secret) {
            byte[] unused = this.mInstance.mSecret = secret;
            return this;
        }

        public KeyChainProtectionParams build() {
            if (this.mInstance.mUserSecretType == null) {
                Integer unused = this.mInstance.mUserSecretType = 100;
            }
            Preconditions.checkNotNull(this.mInstance.mLockScreenUiFormat);
            Preconditions.checkNotNull(this.mInstance.mKeyDerivationParams);
            if (this.mInstance.mSecret == null) {
                byte[] unused2 = this.mInstance.mSecret = new byte[0];
            }
            return this.mInstance;
        }
    }

    public void clearSecret() {
        Arrays.fill(this.mSecret, (byte) 0);
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mUserSecretType.intValue());
        out.writeInt(this.mLockScreenUiFormat.intValue());
        out.writeTypedObject(this.mKeyDerivationParams, flags);
        out.writeByteArray(this.mSecret);
    }

    protected KeyChainProtectionParams(Parcel in) {
        this.mUserSecretType = Integer.valueOf(in.readInt());
        this.mLockScreenUiFormat = Integer.valueOf(in.readInt());
        this.mKeyDerivationParams = (KeyDerivationParams) in.readTypedObject(KeyDerivationParams.CREATOR);
        this.mSecret = in.createByteArray();
    }

    public int describeContents() {
        return 0;
    }
}
