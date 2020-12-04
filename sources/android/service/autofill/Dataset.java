package android.service.autofill;

import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class Dataset implements Parcelable {
    public static final Parcelable.Creator<Dataset> CREATOR = new Parcelable.Creator<Dataset>() {
        public Dataset createFromParcel(Parcel parcel) {
            Builder builder;
            RemoteViews presentation = (RemoteViews) parcel.readParcelable((ClassLoader) null);
            if (presentation == null) {
                builder = new Builder();
            } else {
                builder = new Builder(presentation);
            }
            ArrayList<AutofillId> ids = parcel.createTypedArrayList(AutofillId.CREATOR);
            ArrayList<AutofillValue> values = parcel.createTypedArrayList(AutofillValue.CREATOR);
            ArrayList<RemoteViews> presentations = parcel.createTypedArrayList(RemoteViews.CREATOR);
            ArrayList<DatasetFieldFilter> filters = parcel.createTypedArrayList(DatasetFieldFilter.CREATOR);
            for (int i = 0; i < ids.size(); i++) {
                builder.setLifeTheUniverseAndEverything(ids.get(i), values.get(i), presentations.get(i), filters.get(i));
            }
            builder.setAuthentication((IntentSender) parcel.readParcelable((ClassLoader) null));
            builder.setId(parcel.readString());
            return builder.build();
        }

        public Dataset[] newArray(int size) {
            return new Dataset[size];
        }
    };
    private final IntentSender mAuthentication;
    private final ArrayList<DatasetFieldFilter> mFieldFilters;
    private final ArrayList<AutofillId> mFieldIds;
    private final ArrayList<RemoteViews> mFieldPresentations;
    private final ArrayList<AutofillValue> mFieldValues;
    String mId;
    private final RemoteViews mPresentation;

    private Dataset(Builder builder) {
        this.mFieldIds = builder.mFieldIds;
        this.mFieldValues = builder.mFieldValues;
        this.mFieldPresentations = builder.mFieldPresentations;
        this.mFieldFilters = builder.mFieldFilters;
        this.mPresentation = builder.mPresentation;
        this.mAuthentication = builder.mAuthentication;
        this.mId = builder.mId;
    }

    public ArrayList<AutofillId> getFieldIds() {
        return this.mFieldIds;
    }

    public ArrayList<AutofillValue> getFieldValues() {
        return this.mFieldValues;
    }

    public RemoteViews getFieldPresentation(int index) {
        RemoteViews customPresentation = this.mFieldPresentations.get(index);
        return customPresentation != null ? customPresentation : this.mPresentation;
    }

    public DatasetFieldFilter getFilter(int index) {
        return this.mFieldFilters.get(index);
    }

    public IntentSender getAuthentication() {
        return this.mAuthentication;
    }

    public boolean isEmpty() {
        return this.mFieldIds == null || this.mFieldIds.isEmpty();
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        StringBuilder builder = new StringBuilder("Dataset[");
        if (this.mId == null) {
            builder.append("noId");
        } else {
            builder.append("id=");
            builder.append(this.mId.length());
            builder.append("_chars");
        }
        if (this.mFieldIds != null) {
            builder.append(", fieldIds=");
            builder.append(this.mFieldIds);
        }
        if (this.mFieldValues != null) {
            builder.append(", fieldValues=");
            builder.append(this.mFieldValues);
        }
        if (this.mFieldPresentations != null) {
            builder.append(", fieldPresentations=");
            builder.append(this.mFieldPresentations.size());
        }
        if (this.mFieldFilters != null) {
            builder.append(", fieldFilters=");
            builder.append(this.mFieldFilters.size());
        }
        if (this.mPresentation != null) {
            builder.append(", hasPresentation");
        }
        if (this.mAuthentication != null) {
            builder.append(", hasAuthentication");
        }
        builder.append(']');
        return builder.toString();
    }

    public String getId() {
        return this.mId;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public IntentSender mAuthentication;
        private boolean mDestroyed;
        /* access modifiers changed from: private */
        public ArrayList<DatasetFieldFilter> mFieldFilters;
        /* access modifiers changed from: private */
        public ArrayList<AutofillId> mFieldIds;
        /* access modifiers changed from: private */
        public ArrayList<RemoteViews> mFieldPresentations;
        /* access modifiers changed from: private */
        public ArrayList<AutofillValue> mFieldValues;
        /* access modifiers changed from: private */
        public String mId;
        /* access modifiers changed from: private */
        public RemoteViews mPresentation;

        public Builder(RemoteViews presentation) {
            Preconditions.checkNotNull(presentation, "presentation must be non-null");
            this.mPresentation = presentation;
        }

        public Builder() {
        }

        public Builder setAuthentication(IntentSender authentication) {
            throwIfDestroyed();
            this.mAuthentication = authentication;
            return this;
        }

        public Builder setId(String id) {
            throwIfDestroyed();
            this.mId = id;
            return this;
        }

        public Builder setValue(AutofillId id, AutofillValue value) {
            throwIfDestroyed();
            setLifeTheUniverseAndEverything(id, value, (RemoteViews) null, (DatasetFieldFilter) null);
            return this;
        }

        public Builder setValue(AutofillId id, AutofillValue value, RemoteViews presentation) {
            throwIfDestroyed();
            Preconditions.checkNotNull(presentation, "presentation cannot be null");
            setLifeTheUniverseAndEverything(id, value, presentation, (DatasetFieldFilter) null);
            return this;
        }

        public Builder setValue(AutofillId id, AutofillValue value, Pattern filter) {
            throwIfDestroyed();
            Preconditions.checkState(this.mPresentation != null, "Dataset presentation not set on constructor");
            setLifeTheUniverseAndEverything(id, value, (RemoteViews) null, new DatasetFieldFilter(filter));
            return this;
        }

        public Builder setValue(AutofillId id, AutofillValue value, Pattern filter, RemoteViews presentation) {
            throwIfDestroyed();
            Preconditions.checkNotNull(presentation, "presentation cannot be null");
            setLifeTheUniverseAndEverything(id, value, presentation, new DatasetFieldFilter(filter));
            return this;
        }

        /* access modifiers changed from: private */
        public void setLifeTheUniverseAndEverything(AutofillId id, AutofillValue value, RemoteViews presentation, DatasetFieldFilter filter) {
            Preconditions.checkNotNull(id, "id cannot be null");
            if (this.mFieldIds != null) {
                int existingIdx = this.mFieldIds.indexOf(id);
                if (existingIdx >= 0) {
                    this.mFieldValues.set(existingIdx, value);
                    this.mFieldPresentations.set(existingIdx, presentation);
                    this.mFieldFilters.set(existingIdx, filter);
                    return;
                }
            } else {
                this.mFieldIds = new ArrayList<>();
                this.mFieldValues = new ArrayList<>();
                this.mFieldPresentations = new ArrayList<>();
                this.mFieldFilters = new ArrayList<>();
            }
            this.mFieldIds.add(id);
            this.mFieldValues.add(value);
            this.mFieldPresentations.add(presentation);
            this.mFieldFilters.add(filter);
        }

        public Dataset build() {
            throwIfDestroyed();
            this.mDestroyed = true;
            if (this.mFieldIds != null) {
                return new Dataset(this);
            }
            throw new IllegalStateException("at least one value must be set");
        }

        private void throwIfDestroyed() {
            if (this.mDestroyed) {
                throw new IllegalStateException("Already called #build()");
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(this.mPresentation, flags);
        parcel.writeTypedList(this.mFieldIds, flags);
        parcel.writeTypedList(this.mFieldValues, flags);
        parcel.writeTypedList(this.mFieldPresentations, flags);
        parcel.writeTypedList(this.mFieldFilters, flags);
        parcel.writeParcelable(this.mAuthentication, flags);
        parcel.writeString(this.mId);
    }

    public static final class DatasetFieldFilter implements Parcelable {
        public static final Parcelable.Creator<DatasetFieldFilter> CREATOR = new Parcelable.Creator<DatasetFieldFilter>() {
            public DatasetFieldFilter createFromParcel(Parcel parcel) {
                return new DatasetFieldFilter((Pattern) parcel.readSerializable());
            }

            public DatasetFieldFilter[] newArray(int size) {
                return new DatasetFieldFilter[size];
            }
        };
        public final Pattern pattern;

        private DatasetFieldFilter(Pattern pattern2) {
            this.pattern = pattern2;
        }

        public String toString() {
            if (!Helper.sDebug) {
                return super.toString();
            }
            if (this.pattern == null) {
                return "null";
            }
            return this.pattern.pattern().length() + "_chars";
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeSerializable(this.pattern);
        }
    }
}
