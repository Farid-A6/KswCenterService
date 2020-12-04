package android.telephony.ims;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.annotations.VisibleForTesting;

public class RcsGroupThreadParticipantJoinedEventDescriptor extends RcsGroupThreadEventDescriptor {
    public static final Parcelable.Creator<RcsGroupThreadParticipantJoinedEventDescriptor> CREATOR = new Parcelable.Creator<RcsGroupThreadParticipantJoinedEventDescriptor>() {
        public RcsGroupThreadParticipantJoinedEventDescriptor createFromParcel(Parcel in) {
            return new RcsGroupThreadParticipantJoinedEventDescriptor(in);
        }

        public RcsGroupThreadParticipantJoinedEventDescriptor[] newArray(int size) {
            return new RcsGroupThreadParticipantJoinedEventDescriptor[size];
        }
    };
    private final int mJoinedParticipantId;

    public RcsGroupThreadParticipantJoinedEventDescriptor(long timestamp, int rcsGroupThreadId, int originatingParticipantId, int joinedParticipantId) {
        super(timestamp, rcsGroupThreadId, originatingParticipantId);
        this.mJoinedParticipantId = joinedParticipantId;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PROTECTED)
    public RcsGroupThreadParticipantJoinedEvent createRcsEvent(RcsControllerCall rcsControllerCall) {
        return new RcsGroupThreadParticipantJoinedEvent(this.mTimestamp, new RcsGroupThread(rcsControllerCall, this.mRcsGroupThreadId), new RcsParticipant(rcsControllerCall, this.mOriginatingParticipantId), new RcsParticipant(rcsControllerCall, this.mJoinedParticipantId));
    }

    protected RcsGroupThreadParticipantJoinedEventDescriptor(Parcel in) {
        super(in);
        this.mJoinedParticipantId = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mJoinedParticipantId);
    }
}
