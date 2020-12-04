package android.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SystemApi
public final class GnssMeasurementCorrections implements Parcelable {
    public static final Parcelable.Creator<GnssMeasurementCorrections> CREATOR = new Parcelable.Creator<GnssMeasurementCorrections>() {
        public GnssMeasurementCorrections createFromParcel(Parcel parcel) {
            Builder gnssMeasurementCorrectons = new Builder().setLatitudeDegrees(parcel.readDouble()).setLongitudeDegrees(parcel.readDouble()).setAltitudeMeters(parcel.readDouble()).setHorizontalPositionUncertaintyMeters(parcel.readDouble()).setVerticalPositionUncertaintyMeters(parcel.readDouble()).setToaGpsNanosecondsOfWeek(parcel.readLong());
            List<GnssSingleSatCorrection> singleSatCorrectionList = new ArrayList<>();
            parcel.readTypedList(singleSatCorrectionList, GnssSingleSatCorrection.CREATOR);
            gnssMeasurementCorrectons.setSingleSatelliteCorrectionList(singleSatCorrectionList);
            return gnssMeasurementCorrectons.build();
        }

        public GnssMeasurementCorrections[] newArray(int i) {
            return new GnssMeasurementCorrections[i];
        }
    };
    private final double mAltitudeMeters;
    private final double mHorizontalPositionUncertaintyMeters;
    private final double mLatitudeDegrees;
    private final double mLongitudeDegrees;
    private final List<GnssSingleSatCorrection> mSingleSatCorrectionList;
    private final long mToaGpsNanosecondsOfWeek;
    private final double mVerticalPositionUncertaintyMeters;

    private GnssMeasurementCorrections(Builder builder) {
        this.mLatitudeDegrees = builder.mLatitudeDegrees;
        this.mLongitudeDegrees = builder.mLongitudeDegrees;
        this.mAltitudeMeters = builder.mAltitudeMeters;
        this.mHorizontalPositionUncertaintyMeters = builder.mHorizontalPositionUncertaintyMeters;
        this.mVerticalPositionUncertaintyMeters = builder.mVerticalPositionUncertaintyMeters;
        this.mToaGpsNanosecondsOfWeek = builder.mToaGpsNanosecondsOfWeek;
        List<GnssSingleSatCorrection> singleSatCorrList = builder.mSingleSatCorrectionList;
        Preconditions.checkArgument(singleSatCorrList != null && !singleSatCorrList.isEmpty());
        this.mSingleSatCorrectionList = Collections.unmodifiableList(new ArrayList(singleSatCorrList));
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

    public double getHorizontalPositionUncertaintyMeters() {
        return this.mHorizontalPositionUncertaintyMeters;
    }

    public double getVerticalPositionUncertaintyMeters() {
        return this.mVerticalPositionUncertaintyMeters;
    }

    public long getToaGpsNanosecondsOfWeek() {
        return this.mToaGpsNanosecondsOfWeek;
    }

    public List<GnssSingleSatCorrection> getSingleSatelliteCorrectionList() {
        return this.mSingleSatCorrectionList;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "GnssMeasurementCorrections:\n" + String.format("   %-29s = %s\n", new Object[]{"LatitudeDegrees = ", Double.valueOf(this.mLatitudeDegrees)}) + String.format("   %-29s = %s\n", new Object[]{"LongitudeDegrees = ", Double.valueOf(this.mLongitudeDegrees)}) + String.format("   %-29s = %s\n", new Object[]{"AltitudeMeters = ", Double.valueOf(this.mAltitudeMeters)}) + String.format("   %-29s = %s\n", new Object[]{"HorizontalPositionUncertaintyMeters = ", Double.valueOf(this.mHorizontalPositionUncertaintyMeters)}) + String.format("   %-29s = %s\n", new Object[]{"VerticalPositionUncertaintyMeters = ", Double.valueOf(this.mVerticalPositionUncertaintyMeters)}) + String.format("   %-29s = %s\n", new Object[]{"ToaGpsNanosecondsOfWeek = ", Long.valueOf(this.mToaGpsNanosecondsOfWeek)}) + String.format("   %-29s = %s\n", new Object[]{"mSingleSatCorrectionList = ", this.mSingleSatCorrectionList});
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(this.mLatitudeDegrees);
        parcel.writeDouble(this.mLongitudeDegrees);
        parcel.writeDouble(this.mAltitudeMeters);
        parcel.writeDouble(this.mHorizontalPositionUncertaintyMeters);
        parcel.writeDouble(this.mVerticalPositionUncertaintyMeters);
        parcel.writeLong(this.mToaGpsNanosecondsOfWeek);
        parcel.writeTypedList(this.mSingleSatCorrectionList);
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public double mAltitudeMeters;
        /* access modifiers changed from: private */
        public double mHorizontalPositionUncertaintyMeters;
        /* access modifiers changed from: private */
        public double mLatitudeDegrees;
        /* access modifiers changed from: private */
        public double mLongitudeDegrees;
        /* access modifiers changed from: private */
        public List<GnssSingleSatCorrection> mSingleSatCorrectionList;
        /* access modifiers changed from: private */
        public long mToaGpsNanosecondsOfWeek;
        /* access modifiers changed from: private */
        public double mVerticalPositionUncertaintyMeters;

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

        public Builder setHorizontalPositionUncertaintyMeters(double horizontalPositionUncertaintyMeters) {
            this.mHorizontalPositionUncertaintyMeters = horizontalPositionUncertaintyMeters;
            return this;
        }

        public Builder setVerticalPositionUncertaintyMeters(double verticalPositionUncertaintyMeters) {
            this.mVerticalPositionUncertaintyMeters = verticalPositionUncertaintyMeters;
            return this;
        }

        public Builder setToaGpsNanosecondsOfWeek(long toaGpsNanosecondsOfWeek) {
            this.mToaGpsNanosecondsOfWeek = toaGpsNanosecondsOfWeek;
            return this;
        }

        public Builder setSingleSatelliteCorrectionList(List<GnssSingleSatCorrection> singleSatCorrectionList) {
            this.mSingleSatCorrectionList = singleSatCorrectionList;
            return this;
        }

        public GnssMeasurementCorrections build() {
            return new GnssMeasurementCorrections(this);
        }
    }
}
