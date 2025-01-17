package android.media.midi;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.SmsManager;

public final class MidiDeviceStatus implements Parcelable {
    public static final Parcelable.Creator<MidiDeviceStatus> CREATOR = new Parcelable.Creator<MidiDeviceStatus>() {
        public MidiDeviceStatus createFromParcel(Parcel in) {
            return new MidiDeviceStatus((MidiDeviceInfo) in.readParcelable(MidiDeviceInfo.class.getClassLoader()), in.createBooleanArray(), in.createIntArray());
        }

        public MidiDeviceStatus[] newArray(int size) {
            return new MidiDeviceStatus[size];
        }
    };
    private static final String TAG = "MidiDeviceStatus";
    private final MidiDeviceInfo mDeviceInfo;
    private final boolean[] mInputPortOpen;
    private final int[] mOutputPortOpenCount;

    public MidiDeviceStatus(MidiDeviceInfo deviceInfo, boolean[] inputPortOpen, int[] outputPortOpenCount) {
        this.mDeviceInfo = deviceInfo;
        this.mInputPortOpen = new boolean[inputPortOpen.length];
        System.arraycopy(inputPortOpen, 0, this.mInputPortOpen, 0, inputPortOpen.length);
        this.mOutputPortOpenCount = new int[outputPortOpenCount.length];
        System.arraycopy(outputPortOpenCount, 0, this.mOutputPortOpenCount, 0, outputPortOpenCount.length);
    }

    public MidiDeviceStatus(MidiDeviceInfo deviceInfo) {
        this.mDeviceInfo = deviceInfo;
        this.mInputPortOpen = new boolean[deviceInfo.getInputPortCount()];
        this.mOutputPortOpenCount = new int[deviceInfo.getOutputPortCount()];
    }

    public MidiDeviceInfo getDeviceInfo() {
        return this.mDeviceInfo;
    }

    public boolean isInputPortOpen(int portNumber) {
        return this.mInputPortOpen[portNumber];
    }

    public int getOutputPortOpenCount(int portNumber) {
        return this.mOutputPortOpenCount[portNumber];
    }

    public String toString() {
        int inputPortCount = this.mDeviceInfo.getInputPortCount();
        int outputPortCount = this.mDeviceInfo.getOutputPortCount();
        StringBuilder builder = new StringBuilder("mInputPortOpen=[");
        for (int i = 0; i < inputPortCount; i++) {
            builder.append(this.mInputPortOpen[i]);
            if (i < inputPortCount - 1) {
                builder.append(SmsManager.REGEX_PREFIX_DELIMITER);
            }
        }
        builder.append("] mOutputPortOpenCount=[");
        for (int i2 = 0; i2 < outputPortCount; i2++) {
            builder.append(this.mOutputPortOpenCount[i2]);
            if (i2 < outputPortCount - 1) {
                builder.append(SmsManager.REGEX_PREFIX_DELIMITER);
            }
        }
        builder.append("]");
        return builder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(this.mDeviceInfo, flags);
        parcel.writeBooleanArray(this.mInputPortOpen);
        parcel.writeIntArray(this.mOutputPortOpenCount);
    }
}
