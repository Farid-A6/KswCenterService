package com.android.ims.internal.uce.presence;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;

public class PresSubscriptionState implements Parcelable {
    public static final Parcelable.Creator<PresSubscriptionState> CREATOR = new Parcelable.Creator<PresSubscriptionState>() {
        public PresSubscriptionState createFromParcel(Parcel source) {
            return new PresSubscriptionState(source);
        }

        public PresSubscriptionState[] newArray(int size) {
            return new PresSubscriptionState[size];
        }
    };
    public static final int UCE_PRES_SUBSCRIPTION_STATE_ACTIVE = 0;
    public static final int UCE_PRES_SUBSCRIPTION_STATE_PENDING = 1;
    public static final int UCE_PRES_SUBSCRIPTION_STATE_TERMINATED = 2;
    public static final int UCE_PRES_SUBSCRIPTION_STATE_UNKNOWN = 3;
    private int mPresSubscriptionState;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mPresSubscriptionState);
    }

    private PresSubscriptionState(Parcel source) {
        this.mPresSubscriptionState = 3;
        readFromParcel(source);
    }

    public void readFromParcel(Parcel source) {
        this.mPresSubscriptionState = source.readInt();
    }

    @UnsupportedAppUsage
    public PresSubscriptionState() {
        this.mPresSubscriptionState = 3;
    }

    public int getPresSubscriptionStateValue() {
        return this.mPresSubscriptionState;
    }

    @UnsupportedAppUsage
    public void setPresSubscriptionState(int nPresSubscriptionState) {
        this.mPresSubscriptionState = nPresSubscriptionState;
    }
}
