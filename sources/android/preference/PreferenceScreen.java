package android.preference;

import android.annotation.UnsupportedAppUsage;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.R;

@Deprecated
public final class PreferenceScreen extends PreferenceGroup implements AdapterView.OnItemClickListener, DialogInterface.OnDismissListener {
    private Dialog mDialog;
    private Drawable mDividerDrawable;
    private boolean mDividerSpecified;
    private int mLayoutResId = R.layout.preference_list_fragment;
    @UnsupportedAppUsage
    private ListView mListView;
    @UnsupportedAppUsage
    private ListAdapter mRootAdapter;

    @UnsupportedAppUsage
    public PreferenceScreen(Context context, AttributeSet attrs) {
        super(context, attrs, 16842891);
        TypedArray a = context.obtainStyledAttributes((AttributeSet) null, R.styleable.PreferenceScreen, 16842891, 0);
        this.mLayoutResId = a.getResourceId(1, this.mLayoutResId);
        if (a.hasValueOrEmpty(0)) {
            this.mDividerDrawable = a.getDrawable(0);
            this.mDividerSpecified = true;
        }
        a.recycle();
    }

    public ListAdapter getRootAdapter() {
        if (this.mRootAdapter == null) {
            this.mRootAdapter = onCreateRootAdapter();
        }
        return this.mRootAdapter;
    }

    /* access modifiers changed from: protected */
    public ListAdapter onCreateRootAdapter() {
        return new PreferenceGroupAdapter(this);
    }

    public void bind(ListView listView) {
        listView.setOnItemClickListener(this);
        listView.setAdapter(getRootAdapter());
        onAttachedToActivity();
    }

    /* access modifiers changed from: protected */
    public void onClick() {
        if (getIntent() == null && getFragment() == null && getPreferenceCount() != 0) {
            showDialog((Bundle) null);
        }
    }

    private void showDialog(Bundle state) {
        Context context = getContext();
        if (this.mListView != null) {
            this.mListView.setAdapter((ListAdapter) null);
        }
        View childPrefScreen = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(this.mLayoutResId, (ViewGroup) null);
        View titleView = childPrefScreen.findViewById(16908310);
        this.mListView = (ListView) childPrefScreen.findViewById(16908298);
        if (this.mDividerSpecified) {
            this.mListView.setDivider(this.mDividerDrawable);
        }
        bind(this.mListView);
        CharSequence title = getTitle();
        Dialog dialog = new Dialog(context, context.getThemeResId());
        this.mDialog = dialog;
        if (TextUtils.isEmpty(title)) {
            if (titleView != null) {
                titleView.setVisibility(8);
            }
            dialog.getWindow().requestFeature(1);
        } else if (titleView instanceof TextView) {
            ((TextView) titleView).setText(title);
            titleView.setVisibility(0);
        } else {
            dialog.setTitle(title);
        }
        dialog.setContentView(childPrefScreen);
        dialog.setOnDismissListener(this);
        if (state != null) {
            dialog.onRestoreInstanceState(state);
        }
        getPreferenceManager().addPreferencesScreen(dialog);
        dialog.show();
    }

    public void onDismiss(DialogInterface dialog) {
        this.mDialog = null;
        getPreferenceManager().removePreferencesScreen(dialog);
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    public void onItemClick(AdapterView parent, View view, int position, long id) {
        if (parent instanceof ListView) {
            position -= ((ListView) parent).getHeaderViewsCount();
        }
        Object item = getRootAdapter().getItem(position);
        if (item instanceof Preference) {
            ((Preference) item).performClick(this);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isOnSameScreenAsChildren() {
        return false;
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            return superState;
        }
        SavedState myState = new SavedState(superState);
        myState.isDialogShowing = true;
        myState.dialogBundle = dialog.onSaveInstanceState();
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
        if (myState.isDialogShowing) {
            showDialog(myState.dialogBundle);
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
        Bundle dialogBundle;
        boolean isDialogShowing;

        public SavedState(Parcel source) {
            super(source);
            this.isDialogShowing = source.readInt() != 1 ? false : true;
            this.dialogBundle = source.readBundle();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.isDialogShowing ? 1 : 0);
            dest.writeBundle(this.dialogBundle);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }
}
