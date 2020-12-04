package android.view.textclassifier;

import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannedString;
import android.util.ArrayMap;
import android.view.textclassifier.TextClassifier;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import java.util.Locale;
import java.util.Map;

public final class TextSelection implements Parcelable {
    public static final Parcelable.Creator<TextSelection> CREATOR = new Parcelable.Creator<TextSelection>() {
        public TextSelection createFromParcel(Parcel in) {
            return new TextSelection(in);
        }

        public TextSelection[] newArray(int size) {
            return new TextSelection[size];
        }
    };
    private final int mEndIndex;
    private final EntityConfidence mEntityConfidence;
    private final Bundle mExtras;
    private final String mId;
    private final int mStartIndex;

    private TextSelection(int startIndex, int endIndex, Map<String, Float> entityConfidence, String id, Bundle extras) {
        this.mStartIndex = startIndex;
        this.mEndIndex = endIndex;
        this.mEntityConfidence = new EntityConfidence(entityConfidence);
        this.mId = id;
        this.mExtras = extras;
    }

    public int getSelectionStartIndex() {
        return this.mStartIndex;
    }

    public int getSelectionEndIndex() {
        return this.mEndIndex;
    }

    public int getEntityCount() {
        return this.mEntityConfidence.getEntities().size();
    }

    public String getEntity(int index) {
        return this.mEntityConfidence.getEntities().get(index);
    }

    public float getConfidenceScore(String entity) {
        return this.mEntityConfidence.getConfidenceScore(entity);
    }

    public String getId() {
        return this.mId;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public String toString() {
        return String.format(Locale.US, "TextSelection {id=%s, startIndex=%d, endIndex=%d, entities=%s}", new Object[]{this.mId, Integer.valueOf(this.mStartIndex), Integer.valueOf(this.mEndIndex), this.mEntityConfidence});
    }

    public static final class Builder {
        private final int mEndIndex;
        private final Map<String, Float> mEntityConfidence = new ArrayMap();
        private Bundle mExtras;
        private String mId;
        private final int mStartIndex;

        public Builder(int startIndex, int endIndex) {
            boolean z = false;
            Preconditions.checkArgument(startIndex >= 0);
            Preconditions.checkArgument(endIndex > startIndex ? true : z);
            this.mStartIndex = startIndex;
            this.mEndIndex = endIndex;
        }

        public Builder setEntityType(String type, float confidenceScore) {
            Preconditions.checkNotNull(type);
            this.mEntityConfidence.put(type, Float.valueOf(confidenceScore));
            return this;
        }

        public Builder setId(String id) {
            this.mId = id;
            return this;
        }

        public Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        public TextSelection build() {
            return new TextSelection(this.mStartIndex, this.mEndIndex, this.mEntityConfidence, this.mId, this.mExtras == null ? Bundle.EMPTY : this.mExtras);
        }
    }

    public static final class Request implements Parcelable {
        public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator<Request>() {
            public Request createFromParcel(Parcel in) {
                return Request.readFromParcel(in);
            }

            public Request[] newArray(int size) {
                return new Request[size];
            }
        };
        private String mCallingPackageName;
        private final boolean mDarkLaunchAllowed;
        private final LocaleList mDefaultLocales;
        private final int mEndIndex;
        private final Bundle mExtras;
        private final int mStartIndex;
        private final CharSequence mText;

        private Request(CharSequence text, int startIndex, int endIndex, LocaleList defaultLocales, boolean darkLaunchAllowed, Bundle extras) {
            this.mText = text;
            this.mStartIndex = startIndex;
            this.mEndIndex = endIndex;
            this.mDefaultLocales = defaultLocales;
            this.mDarkLaunchAllowed = darkLaunchAllowed;
            this.mExtras = extras;
        }

        public CharSequence getText() {
            return this.mText;
        }

        public int getStartIndex() {
            return this.mStartIndex;
        }

        public int getEndIndex() {
            return this.mEndIndex;
        }

        public boolean isDarkLaunchAllowed() {
            return this.mDarkLaunchAllowed;
        }

        public LocaleList getDefaultLocales() {
            return this.mDefaultLocales;
        }

        @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
        public void setCallingPackageName(String callingPackageName) {
            this.mCallingPackageName = callingPackageName;
        }

        public String getCallingPackageName() {
            return this.mCallingPackageName;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public static final class Builder {
            private boolean mDarkLaunchAllowed;
            private LocaleList mDefaultLocales;
            private final int mEndIndex;
            private Bundle mExtras;
            private final int mStartIndex;
            private final CharSequence mText;

            public Builder(CharSequence text, int startIndex, int endIndex) {
                TextClassifier.Utils.checkArgument(text, startIndex, endIndex);
                this.mText = text;
                this.mStartIndex = startIndex;
                this.mEndIndex = endIndex;
            }

            public Builder setDefaultLocales(LocaleList defaultLocales) {
                this.mDefaultLocales = defaultLocales;
                return this;
            }

            public Builder setDarkLaunchAllowed(boolean allowed) {
                this.mDarkLaunchAllowed = allowed;
                return this;
            }

            public Builder setExtras(Bundle extras) {
                this.mExtras = extras;
                return this;
            }

            public Request build() {
                return new Request(new SpannedString(this.mText), this.mStartIndex, this.mEndIndex, this.mDefaultLocales, this.mDarkLaunchAllowed, this.mExtras == null ? Bundle.EMPTY : this.mExtras);
            }
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeCharSequence(this.mText);
            dest.writeInt(this.mStartIndex);
            dest.writeInt(this.mEndIndex);
            dest.writeParcelable(this.mDefaultLocales, flags);
            dest.writeString(this.mCallingPackageName);
            dest.writeBundle(this.mExtras);
        }

        /* access modifiers changed from: private */
        public static Request readFromParcel(Parcel in) {
            CharSequence text = in.readCharSequence();
            int startIndex = in.readInt();
            int endIndex = in.readInt();
            String callingPackageName = in.readString();
            Request request = new Request(text, startIndex, endIndex, (LocaleList) in.readParcelable((ClassLoader) null), false, in.readBundle());
            request.setCallingPackageName(callingPackageName);
            return request;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mStartIndex);
        dest.writeInt(this.mEndIndex);
        this.mEntityConfidence.writeToParcel(dest, flags);
        dest.writeString(this.mId);
        dest.writeBundle(this.mExtras);
    }

    private TextSelection(Parcel in) {
        this.mStartIndex = in.readInt();
        this.mEndIndex = in.readInt();
        this.mEntityConfidence = EntityConfidence.CREATOR.createFromParcel(in);
        this.mId = in.readString();
        this.mExtras = in.readBundle();
    }
}
