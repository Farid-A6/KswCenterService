package android.telephony.ims;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.annotations.VisibleForTesting;

public class RcsGroupThreadIconChangedEventDescriptor extends RcsGroupThreadEventDescriptor {
    public static final Parcelable.Creator<RcsGroupThreadIconChangedEventDescriptor> CREATOR = new Parcelable.Creator<RcsGroupThreadIconChangedEventDescriptor>() {
        public RcsGroupThreadIconChangedEventDescriptor createFromParcel(Parcel in) {
            return new RcsGroupThreadIconChangedEventDescriptor(in);
        }

        public RcsGroupThreadIconChangedEventDescriptor[] newArray(int size) {
            return new RcsGroupThreadIconChangedEventDescriptor[size];
        }
    };
    private final Uri mNewIcon;

    public RcsGroupThreadIconChangedEventDescriptor(long timestamp, int rcsGroupThreadId, int originatingParticipantId, Uri newIcon) {
        super(timestamp, rcsGroupThreadId, originatingParticipantId);
        this.mNewIcon = newIcon;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PROTECTED)
    public RcsGroupThreadIconChangedEvent createRcsEvent(RcsControllerCall rcsControllerCall) {
        return new RcsGroupThreadIconChangedEvent(this.mTimestamp, new RcsGroupThread(rcsControllerCall, this.mRcsGroupThreadId), new RcsParticipant(rcsControllerCall, this.mOriginatingParticipantId), this.mNewIcon);
    }

    protected RcsGroupThreadIconChangedEventDescriptor(Parcel in) {
        super(in);
        this.mNewIcon = (Uri) in.readParcelable(Uri.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.mNewIcon, flags);
    }
}
