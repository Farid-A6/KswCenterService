package android.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;

@SystemApi
public final class GnssReflectingPlane implements Parcelable {
    public static final Parcelable.Creator<GnssReflectingPlane> CREATOR = new Parcelable.Creator<GnssReflectingPlane>() {
        public GnssReflectingPlane createFromParcel(Parcel parcel) {
            return new Builder().setLatitudeDegrees(parcel.readDouble()).setLongitudeDegrees(parcel.readDouble()).setAltitudeMeters(parcel.readDouble()).setAzimuthDegrees(parcel.readDouble()).build();
        }

        public GnssReflectingPlane[] newArray(int i) {
            return new GnssReflectingPlane[i];
        }
    };
    private final double mAltitudeMeters;
    private final double mAzimuthDegrees;
    private final double mLatitudeDegrees;
    private final double mLongitudeDegrees;

    private GnssReflectingPlane(Builder builder) {
        this.mLatitudeDegrees = builder.mLatitudeDegrees;
        this.mLongitudeDegrees = builder.mLongitudeDegrees;
        this.mAltitudeMeters = builder.mAltitudeMeters;
        this.mAzimuthDegrees = builder.mAzimuthDegrees;
    }

    public double getLatitudeDegrees() {
        return this.mLatitudeDegrees;
    }

    public double getLongitudeDegrees() {
        return this.mLongitudeDegrees;
    }

    public double getAltitudeMeters() {
        return this.mAltitudeMeters;
    }

    public double getAzimuthDegrees() {
        return this.mAzimuthDegrees;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "ReflectingPlane:\n" + String.format("   %-29s = %s\n", new Object[]{"LatitudeDegrees = ", Double.valueOf(this.mLatitudeDegrees)}) + String.format("   %-29s = %s\n", new Object[]{"LongitudeDegrees = ", Double.valueOf(this.mLongitudeDegrees)}) + String.format("   %-29s = %s\n", new Object[]{"AltitudeMeters = ", Double.valueOf(this.mAltitudeMeters)}) + String.format("   %-29s = %s\n", new Object[]{"AzimuthDegrees = ", Double.valueOf(this.mAzimuthDegrees)});
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(this.mLatitudeDegrees);
        parcel.writeDouble(this.mLongitudeDegrees);
        parcel.writeDouble(this.mAltitudeMeters);
        parcel.writeDouble(this.mAzimuthDegrees);
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public double mAltitudeMeters;
        /* access modifiers changed from: private */
        public double mAzimuthDegrees;
        /* access modifiers changed from: private */
        public double mLatitudeDegrees;
        /* access modifiers changed from: private */
        public double mLongitudeDegrees;

        public Builder setLatitudeDegrees(double latitudeDegrees) {
            this.mLatitudeDegrees = latitudeDegrees;
            return this;
        }

        public Builder setLongitudeDegrees(double longitudeDegrees) {
            this.mLongitudeDegrees = longitudeDegrees;
            return this;
        }

        public Builder setAltitudeMeters(double altitudeMeters) {
            this.mAltitudeMeters = altitudeMeters;
            return this;
        }

        public Builder setAzimuthDegrees(double azimuthDegrees) {
            this.mAzimuthDegrees = azimuthDegrees;
            return this;
        }

        public GnssReflectingPlane build() {
            return new GnssReflectingPlane(this);
        }
    }
}
