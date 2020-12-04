package android.hardware.thermal.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class Temperature {
    public float currentValue;
    public String name = new String();
    public float shutdownThreshold;
    public float throttlingThreshold;
    public int type;
    public float vrThrottlingThreshold;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != Temperature.class) {
            return false;
        }
        Temperature other = (Temperature) otherObject;
        if (this.type == other.type && HidlSupport.deepEquals(this.name, other.name) && this.currentValue == other.currentValue && this.throttlingThreshold == other.throttlingThreshold && this.shutdownThreshold == other.shutdownThreshold && this.vrThrottlingThreshold == other.vrThrottlingThreshold) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.type))), Integer.valueOf(HidlSupport.deepHashCode(this.name)), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.currentValue))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.throttlingThreshold))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.shutdownThreshold))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.vrThrottlingThreshold)))});
    }

    public final String toString() {
        return "{" + ".type = " + TemperatureType.toString(this.type) + ", .name = " + this.name + ", .currentValue = " + this.currentValue + ", .throttlingThreshold = " + this.throttlingThreshold + ", .shutdownThreshold = " + this.shutdownThreshold + ", .vrThrottlingThreshold = " + this.vrThrottlingThreshold + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        readEmbeddedFromParcel(parcel, parcel.readBuffer(40), 0);
    }

    public static final ArrayList<Temperature> readVectorFromParcel(HwParcel parcel) {
        ArrayList<Temperature> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16);
        int _hidl_vec_size = _hidl_blob.getInt32(8);
        HwBlob childBlob = parcel.readEmbeddedBuffer((long) (_hidl_vec_size * 40), _hidl_blob.handle(), 0, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            Temperature _hidl_vec_element = new Temperature();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, (long) (_hidl_index_0 * 40));
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        HwBlob hwBlob = _hidl_blob;
        this.type = hwBlob.getInt32(_hidl_offset + 0);
        this.name = hwBlob.getString(_hidl_offset + 8);
        parcel.readEmbeddedBuffer((long) (this.name.getBytes().length + 1), _hidl_blob.handle(), _hidl_offset + 8 + 0, false);
        this.currentValue = hwBlob.getFloat(_hidl_offset + 24);
        this.throttlingThreshold = hwBlob.getFloat(_hidl_offset + 28);
        this.shutdownThreshold = hwBlob.getFloat(_hidl_offset + 32);
        this.vrThrottlingThreshold = hwBlob.getFloat(_hidl_offset + 36);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(40);
        writeEmbeddedToBlob(_hidl_blob, 0);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<Temperature> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8, _hidl_vec_size);
        _hidl_blob.putBool(12, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 40);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, (long) (_hidl_index_0 * 40));
        }
        _hidl_blob.putBlob(0, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.type);
        _hidl_blob.putString(8 + _hidl_offset, this.name);
        _hidl_blob.putFloat(24 + _hidl_offset, this.currentValue);
        _hidl_blob.putFloat(28 + _hidl_offset, this.throttlingThreshold);
        _hidl_blob.putFloat(32 + _hidl_offset, this.shutdownThreshold);
        _hidl_blob.putFloat(36 + _hidl_offset, this.vrThrottlingThreshold);
    }
}
