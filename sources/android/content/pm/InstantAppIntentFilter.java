package android.content.pm;

import android.annotation.SystemApi;
import android.content.IntentFilter;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

@SystemApi
public final class InstantAppIntentFilter implements Parcelable {
    public static final Parcelable.Creator<InstantAppIntentFilter> CREATOR = new Parcelable.Creator<InstantAppIntentFilter>() {
        public InstantAppIntentFilter createFromParcel(Parcel in) {
            return new InstantAppIntentFilter(in);
        }

        public InstantAppIntentFilter[] newArray(int size) {
            return new InstantAppIntentFilter[size];
        }
    };
    private final List<IntentFilter> mFilters = new ArrayList();
    private final String mSplitName;

    public InstantAppIntentFilter(String splitName, List<IntentFilter> filters) {
        if (filters == null || filters.size() == 0) {
            throw new IllegalArgumentException();
        }
        this.mSplitName = splitName;
        this.mFilters.addAll(filters);
    }

    InstantAppIntentFilter(Parcel in) {
        this.mSplitName = in.readString();
        in.readList(this.mFilters, (ClassLoader) null);
    }

    public String getSplitName() {
        return this.mSplitName;
    }

    public List<IntentFilter> getFilters() {
        return this.mFilters;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mSplitName);
        out.writeList(this.mFilters);
    }
}
