package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class ImageTransformation extends InternalTransformation implements Transformation, Parcelable {
    public static final Parcelable.Creator<ImageTransformation> CREATOR = new Parcelable.Creator<ImageTransformation>() {
        public ImageTransformation createFromParcel(Parcel parcel) {
            Builder builder;
            AutofillId id = (AutofillId) parcel.readParcelable((ClassLoader) null);
            Pattern[] regexs = (Pattern[]) parcel.readSerializable();
            int[] resIds = parcel.createIntArray();
            CharSequence[] contentDescriptions = parcel.readCharSequenceArray();
            CharSequence contentDescription = contentDescriptions[0];
            if (contentDescription != null) {
                builder = new Builder(id, regexs[0], resIds[0], contentDescription);
            } else {
                builder = new Builder(id, regexs[0], resIds[0]);
            }
            Builder builder2 = builder;
            int size = regexs.length;
            for (int i = 1; i < size; i++) {
                if (contentDescriptions[i] != null) {
                    builder2.addOption(regexs[i], resIds[i], contentDescriptions[i]);
                } else {
                    builder2.addOption(regexs[i], resIds[i]);
                }
            }
            return builder2.build();
        }

        public ImageTransformation[] newArray(int size) {
            return new ImageTransformation[size];
        }
    };
    private static final String TAG = "ImageTransformation";
    private final AutofillId mId;
    private final ArrayList<Option> mOptions;

    private ImageTransformation(Builder builder) {
        this.mId = builder.mId;
        this.mOptions = builder.mOptions;
    }

    public void apply(ValueFinder finder, RemoteViews parentTemplate, int childViewId) throws Exception {
        String value = finder.findByAutofillId(this.mId);
        if (value == null) {
            Log.w(TAG, "No view for id " + this.mId);
            return;
        }
        int size = this.mOptions.size();
        if (Helper.sDebug) {
            Log.d(TAG, size + " multiple options on id " + childViewId + " to compare against");
        }
        int i = 0;
        while (i < size) {
            Option option = this.mOptions.get(i);
            try {
                if (option.pattern.matcher(value).matches()) {
                    Log.d(TAG, "Found match at " + i + ": " + option);
                    parentTemplate.setImageViewResource(childViewId, option.resId);
                    if (option.contentDescription != null) {
                        parentTemplate.setContentDescription(childViewId, option.contentDescription);
                        return;
                    }
                    return;
                }
                i++;
            } catch (Exception e) {
                Log.w(TAG, "Error matching regex #" + i + "(" + option.pattern + ") on id " + option.resId + ": " + e.getClass());
                throw e;
            }
        }
        if (Helper.sDebug != 0) {
            Log.d(TAG, "No match for " + value);
        }
    }

    public static class Builder {
        private boolean mDestroyed;
        /* access modifiers changed from: private */
        public final AutofillId mId;
        /* access modifiers changed from: private */
        public final ArrayList<Option> mOptions = new ArrayList<>();

        @Deprecated
        public Builder(AutofillId id, Pattern regex, int resId) {
            this.mId = (AutofillId) Preconditions.checkNotNull(id);
            addOption(regex, resId);
        }

        public Builder(AutofillId id, Pattern regex, int resId, CharSequence contentDescription) {
            this.mId = (AutofillId) Preconditions.checkNotNull(id);
            addOption(regex, resId, contentDescription);
        }

        @Deprecated
        public Builder addOption(Pattern regex, int resId) {
            addOptionInternal(regex, resId, (CharSequence) null);
            return this;
        }

        public Builder addOption(Pattern regex, int resId, CharSequence contentDescription) {
            addOptionInternal(regex, resId, (CharSequence) Preconditions.checkNotNull(contentDescription));
            return this;
        }

        private void addOptionInternal(Pattern regex, int resId, CharSequence contentDescription) {
            throwIfDestroyed();
            Preconditions.checkNotNull(regex);
            Preconditions.checkArgument(resId != 0);
            this.mOptions.add(new Option(regex, resId, contentDescription));
        }

        public ImageTransformation build() {
            throwIfDestroyed();
            this.mDestroyed = true;
            return new ImageTransformation(this);
        }

        private void throwIfDestroyed() {
            Preconditions.checkState(!this.mDestroyed, "Already called build()");
        }
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        return "ImageTransformation: [id=" + this.mId + ", options=" + this.mOptions + "]";
    }

    public int describeContents() {
        return 0;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: java.lang.String[]} */
    /* JADX WARNING: type inference failed for: r1v0, types: [java.util.regex.Pattern[], java.io.Serializable] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeToParcel(android.os.Parcel r8, int r9) {
        /*
            r7 = this;
            android.view.autofill.AutofillId r0 = r7.mId
            r8.writeParcelable(r0, r9)
            java.util.ArrayList<android.service.autofill.ImageTransformation$Option> r0 = r7.mOptions
            int r0 = r0.size()
            java.util.regex.Pattern[] r1 = new java.util.regex.Pattern[r0]
            int[] r2 = new int[r0]
            java.lang.String[] r3 = new java.lang.String[r0]
            r4 = 0
        L_0x0012:
            if (r4 >= r0) goto L_0x002b
            java.util.ArrayList<android.service.autofill.ImageTransformation$Option> r5 = r7.mOptions
            java.lang.Object r5 = r5.get(r4)
            android.service.autofill.ImageTransformation$Option r5 = (android.service.autofill.ImageTransformation.Option) r5
            java.util.regex.Pattern r6 = r5.pattern
            r1[r4] = r6
            int r6 = r5.resId
            r2[r4] = r6
            java.lang.CharSequence r6 = r5.contentDescription
            r3[r4] = r6
            int r4 = r4 + 1
            goto L_0x0012
        L_0x002b:
            r8.writeSerializable(r1)
            r8.writeIntArray(r2)
            r8.writeCharSequenceArray(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.service.autofill.ImageTransformation.writeToParcel(android.os.Parcel, int):void");
    }

    private static final class Option {
        public final CharSequence contentDescription;
        public final Pattern pattern;
        public final int resId;

        Option(Pattern pattern2, int resId2, CharSequence contentDescription2) {
            this.pattern = pattern2;
            this.resId = resId2;
            this.contentDescription = TextUtils.trimNoCopySpans(contentDescription2);
        }
    }
}
