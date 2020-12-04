package android.preference;

import android.annotation.UnsupportedAppUsage;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.SeekBarVolumizer;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import com.android.internal.R;

@Deprecated
public class VolumePreference extends SeekBarDialogPreference implements PreferenceManager.OnActivityStopListener, View.OnKeyListener, SeekBarVolumizer.Callback {
    private SeekBarVolumizer mSeekBarVolumizer;
    @UnsupportedAppUsage
    private int mStreamType;

    public static class VolumeStore {
        @UnsupportedAppUsage
        public int originalVolume = -1;
        @UnsupportedAppUsage
        public int volume = -1;
    }

    public VolumePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VolumePreference, defStyleAttr, defStyleRes);
        this.mStreamType = a.getInt(0, 0);
        a.recycle();
    }

    public VolumePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @UnsupportedAppUsage
    public VolumePreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarDialogPreferenceStyle);
    }

    public VolumePreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public void setStreamType(int streamType) {
        this.mStreamType = streamType;
    }

    /* access modifiers changed from: protected */
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.mSeekBarVolumizer = new SeekBarVolumizer(getContext(), this.mStreamType, (Uri) null, this);
        this.mSeekBarVolumizer.start();
        this.mSeekBarVolumizer.setSeekBar((SeekBar) view.findViewById(R.id.seekbar));
        getPreferenceManager().registerOnActivityStopListener(this);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (this.mSeekBarVolumizer == null) {
            return true;
        }
        boolean isdown = event.getAction() == 0;
        if (keyCode != 164) {
            switch (keyCode) {
                case 24:
                    if (isdown) {
                        this.mSeekBarVolumizer.changeVolumeBy(1);
                    }
                    return true;
                case 25:
                    if (isdown) {
                        this.mSeekBarVolumizer.changeVolumeBy(-1);
                    }
                    return true;
                default:
                    return false;
            }
        } else {
            if (isdown) {
                this.mSeekBarVolumizer.muteVolume();
            }
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (!positiveResult && this.mSeekBarVolumizer != null) {
            this.mSeekBarVolumizer.revertVolume();
        }
        cleanup();
    }

    public void onActivityStop() {
        if (this.mSeekBarVolumizer != null) {
            this.mSeekBarVolumizer.stopSample();
        }
    }

    private void cleanup() {
        getPreferenceManager().unregisterOnActivityStopListener(this);
        if (this.mSeekBarVolumizer != null) {
            Dialog dialog = getDialog();
            if (dialog != null && dialog.isShowing()) {
                View view = dialog.getWindow().getDecorView().findViewById(R.id.seekbar);
                if (view != null) {
                    view.setOnKeyListener((View.OnKeyListener) null);
                }
                this.mSeekBarVolumizer.revertVolume();
            }
            this.mSeekBarVolumizer.stop();
            this.mSeekBarVolumizer = null;
        }
    }

    public void onSampleStarting(SeekBarVolumizer volumizer) {
        if (this.mSeekBarVolumizer != null && volumizer != this.mSeekBarVolumizer) {
            this.mSeekBarVolumizer.stopSample();
        }
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
    }

    public void onMuted(boolean muted, boolean zenMuted) {
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }
        SavedState myState = new SavedState(superState);
        if (this.mSeekBarVolumizer != null) {
            this.mSeekBarVolumizer.onSaveInstanceState(myState.getVolumeStore());
        }
        return myState;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        if (this.mSeekBarVolumizer != null) {
            this.mSeekBarVolumizer.onRestoreInstanceState(myState.getVolumeStore());
        }
    }

    private static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        VolumeStore mVolumeStore = new VolumeStore();

        public SavedState(Parcel source) {
            super(source);
            this.mVolumeStore.volume = source.readInt();
            this.mVolumeStore.originalVolume = source.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.mVolumeStore.volume);
            dest.writeInt(this.mVolumeStore.originalVolume);
        }

        /* access modifiers changed from: package-private */
        public VolumeStore getVolumeStore() {
            return this.mVolumeStore;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }
}
