package android.app;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.AlertDialog;
import android.app.Application;
import android.app.IRequestFinishCallback;
import android.app.Instrumentation;
import android.app.PictureInPictureParams;
import android.app.VoiceInteractor;
import android.app.assist.AssistContent;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.GraphicsEnvironment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.TextKeyListener;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.EventLog;
import android.util.Log;
import android.util.SparseArray;
import android.util.SuperNotCalledException;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.KeyboardShortcutInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.RemoteAnimationDefinition;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewRootImpl;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityEvent;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillPopupWindow;
import android.view.autofill.Helper;
import android.view.autofill.IAutofillWindowPresenter;
import android.view.contentcapture.ContentCaptureManager;
import android.webkit.WebView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.app.ToolbarActionBar;
import com.android.internal.app.WindowDecorActionBar;
import com.android.internal.policy.PhoneWindow;
import dalvik.system.VMRuntime;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Activity extends ContextThemeWrapper implements LayoutInflater.Factory2, Window.Callback, KeyEvent.Callback, View.OnCreateContextMenuListener, ComponentCallbacks2, Window.OnWindowDismissedCallback, Window.WindowControllerCallback, AutofillManager.AutofillClient, ContentCaptureManager.ContentCaptureClient {
    private static final String AUTOFILL_RESET_NEEDED = "@android:autofillResetNeeded";
    private static final String AUTO_FILL_AUTH_WHO_PREFIX = "@android:autoFillAuth:";
    private static final int CONTENT_CAPTURE_PAUSE = 3;
    private static final int CONTENT_CAPTURE_RESUME = 2;
    private static final int CONTENT_CAPTURE_START = 1;
    private static final int CONTENT_CAPTURE_STOP = 4;
    private static final boolean DEBUG_LIFECYCLE = false;
    public static final int DEFAULT_KEYS_DIALER = 1;
    public static final int DEFAULT_KEYS_DISABLE = 0;
    public static final int DEFAULT_KEYS_SEARCH_GLOBAL = 4;
    public static final int DEFAULT_KEYS_SEARCH_LOCAL = 3;
    public static final int DEFAULT_KEYS_SHORTCUT = 2;
    public static final int DONT_FINISH_TASK_WITH_ACTIVITY = 0;
    public static final int FINISH_TASK_WITH_ACTIVITY = 2;
    public static final int FINISH_TASK_WITH_ROOT_ACTIVITY = 1;
    protected static final int[] FOCUSED_STATE_SET = {16842908};
    @UnsupportedAppUsage
    static final String FRAGMENTS_TAG = "android:fragments";
    private static final String HAS_CURENT_PERMISSIONS_REQUEST_KEY = "android:hasCurrentPermissionsRequest";
    private static final String KEYBOARD_SHORTCUTS_RECEIVER_PKG_NAME = "com.android.systemui";
    private static final String LAST_AUTOFILL_ID = "android:lastAutofillId";
    private static final int LOG_AM_ON_ACTIVITY_RESULT_CALLED = 30062;
    private static final int LOG_AM_ON_CREATE_CALLED = 30057;
    private static final int LOG_AM_ON_DESTROY_CALLED = 30060;
    private static final int LOG_AM_ON_PAUSE_CALLED = 30021;
    private static final int LOG_AM_ON_RESTART_CALLED = 30058;
    private static final int LOG_AM_ON_RESUME_CALLED = 30022;
    private static final int LOG_AM_ON_START_CALLED = 30059;
    private static final int LOG_AM_ON_STOP_CALLED = 30049;
    private static final int LOG_AM_ON_TOP_RESUMED_GAINED_CALLED = 30064;
    private static final int LOG_AM_ON_TOP_RESUMED_LOST_CALLED = 30065;
    private static final String REQUEST_PERMISSIONS_WHO_PREFIX = "@android:requestPermissions:";
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_FIRST_USER = 1;
    public static final int RESULT_OK = -1;
    private static final String SAVED_DIALOGS_TAG = "android:savedDialogs";
    private static final String SAVED_DIALOG_ARGS_KEY_PREFIX = "android:dialog_args_";
    private static final String SAVED_DIALOG_IDS_KEY = "android:savedDialogIds";
    private static final String SAVED_DIALOG_KEY_PREFIX = "android:dialog_";
    private static final String TAG = "Activity";
    private static final String WINDOW_HIERARCHY_TAG = "android:viewHierarchyState";
    ActionBar mActionBar = null;
    private int mActionModeTypeStarting = 0;
    @UnsupportedAppUsage
    ActivityInfo mActivityInfo;
    private final ArrayList<Application.ActivityLifecycleCallbacks> mActivityLifecycleCallbacks = new ArrayList<>();
    @UnsupportedAppUsage
    ActivityTransitionState mActivityTransitionState = new ActivityTransitionState();
    @UnsupportedAppUsage
    private Application mApplication;
    private IBinder mAssistToken;
    private boolean mAutoFillIgnoreFirstResumePause;
    private boolean mAutoFillResetNeeded;
    private AutofillManager mAutofillManager;
    private AutofillPopupWindow mAutofillPopupWindow;
    @UnsupportedAppUsage
    boolean mCalled;
    private boolean mCanEnterPictureInPicture = false;
    private boolean mChangeCanvasToTranslucent;
    boolean mChangingConfigurations = false;
    @UnsupportedAppUsage
    private ComponentName mComponent;
    @UnsupportedAppUsage
    int mConfigChangeFlags;
    private ContentCaptureManager mContentCaptureManager;
    @UnsupportedAppUsage
    Configuration mCurrentConfig;
    View mDecor = null;
    private int mDefaultKeyMode = 0;
    private SpannableStringBuilder mDefaultKeySsb = null;
    @UnsupportedAppUsage
    private boolean mDestroyed;
    private boolean mDoReportFullyDrawn = true;
    @UnsupportedAppUsage
    String mEmbeddedID;
    private boolean mEnableDefaultActionBarUp;
    boolean mEnterAnimationComplete;
    SharedElementCallback mEnterTransitionListener = SharedElementCallback.NULL_CALLBACK;
    SharedElementCallback mExitTransitionListener = SharedElementCallback.NULL_CALLBACK;
    @UnsupportedAppUsage
    boolean mFinished;
    @UnsupportedAppUsage
    final FragmentController mFragments = FragmentController.createController(new HostCallbacks());
    @UnsupportedAppUsage
    final Handler mHandler = new Handler();
    private boolean mHasCurrentPermissionsRequest;
    @UnsupportedAppUsage
    private int mIdent;
    private final Object mInstanceTracker = StrictMode.trackActivity(this);
    @UnsupportedAppUsage
    private Instrumentation mInstrumentation;
    @UnsupportedAppUsage
    Intent mIntent;
    private int mLastAutofillId = View.LAST_APP_AUTOFILL_ID;
    @UnsupportedAppUsage
    NonConfigurationInstances mLastNonConfigurationInstances;
    @UnsupportedAppUsage
    ActivityThread mMainThread;
    @GuardedBy({"mManagedCursors"})
    private final ArrayList<ManagedCursor> mManagedCursors = new ArrayList<>();
    private SparseArray<ManagedDialog> mManagedDialogs;
    private MenuInflater mMenuInflater;
    @UnsupportedAppUsage
    Activity mParent;
    @UnsupportedAppUsage
    String mReferrer;
    private boolean mRestoredFromBundle;
    @GuardedBy({"this"})
    @UnsupportedAppUsage
    int mResultCode = 0;
    @GuardedBy({"this"})
    @UnsupportedAppUsage
    Intent mResultData = null;
    @UnsupportedAppUsage
    boolean mResumed;
    private SearchEvent mSearchEvent;
    private SearchManager mSearchManager;
    boolean mStartedActivity;
    @UnsupportedAppUsage
    boolean mStopped;
    private ActivityManager.TaskDescription mTaskDescription = new ActivityManager.TaskDescription();
    @UnsupportedAppUsage
    private CharSequence mTitle;
    private int mTitleColor = 0;
    private boolean mTitleReady = false;
    @UnsupportedAppUsage
    private IBinder mToken;
    private TranslucentConversionListener mTranslucentCallback;
    private Thread mUiThread;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    boolean mVisibleFromClient = true;
    boolean mVisibleFromServer = false;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    VoiceInteractor mVoiceInteractor;
    @UnsupportedAppUsage
    private Window mWindow;
    @UnsupportedAppUsage
    boolean mWindowAdded = false;
    @UnsupportedAppUsage
    private WindowManager mWindowManager;

    @Retention(RetentionPolicy.SOURCE)
    @interface ContentCaptureNotificationType {
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface DefaultKeyMode {
    }

    @SystemApi
    public interface TranslucentConversionListener {
        void onTranslucentConversionComplete(boolean z);
    }

    private static native String getDlWarning();

    private static class ManagedDialog {
        Bundle mArgs;
        Dialog mDialog;

        private ManagedDialog() {
        }
    }

    static final class NonConfigurationInstances {
        Object activity;
        HashMap<String, Object> children;
        FragmentManagerNonConfig fragments;
        ArrayMap<String, LoaderManager> loaders;
        VoiceInteractor voiceInteractor;

        NonConfigurationInstances() {
        }
    }

    private static final class ManagedCursor {
        /* access modifiers changed from: private */
        public final Cursor mCursor;
        /* access modifiers changed from: private */
        public boolean mReleased = false;
        /* access modifiers changed from: private */
        public boolean mUpdated = false;

        ManagedCursor(Cursor cursor) {
            this.mCursor = cursor;
        }
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public void setIntent(Intent newIntent) {
        this.mIntent = newIntent;
    }

    public final Application getApplication() {
        return this.mApplication;
    }

    public final boolean isChild() {
        return this.mParent != null;
    }

    public final Activity getParent() {
        return this.mParent;
    }

    public WindowManager getWindowManager() {
        return this.mWindowManager;
    }

    public Window getWindow() {
        return this.mWindow;
    }

    @Deprecated
    public LoaderManager getLoaderManager() {
        return this.mFragments.getLoaderManager();
    }

    public View getCurrentFocus() {
        if (this.mWindow != null) {
            return this.mWindow.getCurrentFocus();
        }
        return null;
    }

    private AutofillManager getAutofillManager() {
        if (this.mAutofillManager == null) {
            this.mAutofillManager = (AutofillManager) getSystemService(AutofillManager.class);
        }
        return this.mAutofillManager;
    }

    private ContentCaptureManager getContentCaptureManager() {
        if (!UserHandle.isApp(Process.myUid())) {
            return null;
        }
        if (this.mContentCaptureManager == null) {
            this.mContentCaptureManager = (ContentCaptureManager) getSystemService(ContentCaptureManager.class);
        }
        return this.mContentCaptureManager;
    }

    private String getContentCaptureTypeAsString(int type) {
        switch (type) {
            case 1:
                return "START";
            case 2:
                return "RESUME";
            case 3:
                return "PAUSE";
            case 4:
                return "STOP";
            default:
                return "UNKNOW-" + type;
        }
    }

    private void notifyContentCaptureManagerIfNeeded(int type) {
        if (Trace.isTagEnabled(64)) {
            Trace.traceBegin(64, "notifyContentCapture(" + getContentCaptureTypeAsString(type) + ") for " + this.mComponent.toShortString());
        }
        try {
            ContentCaptureManager cm = getContentCaptureManager();
            if (cm != null) {
                switch (type) {
                    case 1:
                        Window window = getWindow();
                        if (window != null) {
                            cm.updateWindowAttributes(window.getAttributes());
                        }
                        cm.onActivityCreated(this.mToken, getComponentName());
                        break;
                    case 2:
                        cm.onActivityResumed();
                        break;
                    case 3:
                        cm.onActivityPaused();
                        break;
                    case 4:
                        cm.onActivityDestroyed();
                        break;
                    default:
                        Log.wtf(TAG, "Invalid @ContentCaptureNotificationType: " + type);
                        break;
                }
                Trace.traceEnd(64);
            }
        } finally {
            Trace.traceEnd(64);
        }
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        if (newBase != null) {
            newBase.setAutofillClient(this);
            newBase.setContentCaptureOptions(getContentCaptureOptions());
        }
    }

    public final AutofillManager.AutofillClient getAutofillClient() {
        return this;
    }

    public final ContentCaptureManager.ContentCaptureClient getContentCaptureClient() {
        return this;
    }

    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        synchronized (this.mActivityLifecycleCallbacks) {
            this.mActivityLifecycleCallbacks.add(callback);
        }
    }

    public void unregisterActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        synchronized (this.mActivityLifecycleCallbacks) {
            this.mActivityLifecycleCallbacks.remove(callback);
        }
    }

    private void dispatchActivityPreCreated(Bundle savedInstanceState) {
        getApplication().dispatchActivityPreCreated(this, savedInstanceState);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((Application.ActivityLifecycleCallbacks) obj).onActivityPreCreated(this, savedInstanceState);
            }
        }
    }

    private void dispatchActivityCreated(Bundle savedInstanceState) {
        getApplication().dispatchActivityCreated(this, savedInstanceState);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((Application.ActivityLifecycleCallbacks) obj).onActivityCreated(this, savedInstanceState);
            }
        }
    }

    private void dispatchActivityPostCreated(Bundle savedInstanceState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((Application.ActivityLifecycleCallbacks) obj).onActivityPostCreated(this, savedInstanceState);
            }
        }
        getApplication().dispatchActivityPostCreated(this, savedInstanceState);
    }

    private void dispatchActivityPreStarted() {
        getApplication().dispatchActivityPreStarted(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((Application.ActivityLifecycleCallbacks) obj).onActivityPreStarted(this);
            }
        }
    }

    private void dispatchActivityStarted() {
        getApplication().dispatchActivityStarted(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((Application.ActivityLifecycleCallbacks) obj).onActivityStarted(this);
            }
        }
    }

    private void dispatchActivityPostStarted() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((Application.ActivityLifecycleCallbacks) obj).onActivityPostStarted(this);
            }
        }
        getApplication().dispatchActivityPostStarted(this);
    }

    private void dispatchActivityPreResumed() {
        getApplication().dispatchActivityPreResumed(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((Application.ActivityLifecycleCallbacks) obj).onActivityPreResumed(this);
            }
        }
    }

    private void dispatchActivityResumed() {
        getApplication().dispatchActivityResumed(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((Application.ActivityLifecycleCallbacks) obj).onActivityResumed(this);
            }
        }
    }

    private void dispatchActivityPostResumed() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((Application.ActivityLifecycleCallbacks) obj).onActivityPostResumed(this);
            }
        }
        getApplication().dispatchActivityPostResumed(this);
    }

    private void dispatchActivityPrePaused() {
        getApplication().dispatchActivityPrePaused(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((Application.ActivityLifecycleCallbacks) callbacks[i]).onActivityPrePaused(this);
            }
        }
    }

    private void dispatchActivityPaused() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((Application.ActivityLifecycleCallbacks) callbacks[i]).onActivityPaused(this);
            }
        }
        getApplication().dispatchActivityPaused(this);
    }

    private void dispatchActivityPostPaused() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((Application.ActivityLifecycleCallbacks) callbacks[i]).onActivityPostPaused(this);
            }
        }
        getApplication().dispatchActivityPostPaused(this);
    }

    private void dispatchActivityPreStopped() {
        getApplication().dispatchActivityPreStopped(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((Application.ActivityLifecycleCallbacks) callbacks[i]).onActivityPreStopped(this);
            }
        }
    }

    private void dispatchActivityStopped() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((Application.ActivityLifecycleCallbacks) callbacks[i]).onActivityStopped(this);
            }
        }
        getApplication().dispatchActivityStopped(this);
    }

    private void dispatchActivityPostStopped() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((Application.ActivityLifecycleCallbacks) callbacks[i]).onActivityPostStopped(this);
            }
        }
        getApplication().dispatchActivityPostStopped(this);
    }

    private void dispatchActivityPreSaveInstanceState(Bundle outState) {
        getApplication().dispatchActivityPreSaveInstanceState(this, outState);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((Application.ActivityLifecycleCallbacks) callbacks[i]).onActivityPreSaveInstanceState(this, outState);
            }
        }
    }

    private void dispatchActivitySaveInstanceState(Bundle outState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((Application.ActivityLifecycleCallbacks) callbacks[i]).onActivitySaveInstanceState(this, outState);
            }
        }
        getApplication().dispatchActivitySaveInstanceState(this, outState);
    }

    private void dispatchActivityPostSaveInstanceState(Bundle outState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((Application.ActivityLifecycleCallbacks) callbacks[i]).onActivityPostSaveInstanceState(this, outState);
            }
        }
        getApplication().dispatchActivityPostSaveInstanceState(this, outState);
    }

    private void dispatchActivityPreDestroyed() {
        getApplication().dispatchActivityPreDestroyed(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((Application.ActivityLifecycleCallbacks) callbacks[i]).onActivityPreDestroyed(this);
            }
        }
    }

    private void dispatchActivityDestroyed() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((Application.ActivityLifecycleCallbacks) callbacks[i]).onActivityDestroyed(this);
            }
        }
        getApplication().dispatchActivityDestroyed(this);
    }

    private void dispatchActivityPostDestroyed() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((Application.ActivityLifecycleCallbacks) callbacks[i]).onActivityPostDestroyed(this);
            }
        }
        getApplication().dispatchActivityPostDestroyed(this);
    }

    private Object[] collectActivityLifecycleCallbacks() {
        Object[] callbacks = null;
        synchronized (this.mActivityLifecycleCallbacks) {
            if (this.mActivityLifecycleCallbacks.size() > 0) {
                callbacks = this.mActivityLifecycleCallbacks.toArray();
            }
        }
        return callbacks;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        if (this.mLastNonConfigurationInstances != null) {
            this.mFragments.restoreLoaderNonConfig(this.mLastNonConfigurationInstances.loaders);
        }
        if (this.mActivityInfo.parentActivityName != null) {
            if (this.mActionBar == null) {
                this.mEnableDefaultActionBarUp = true;
            } else {
                this.mActionBar.setDefaultDisplayHomeAsUpEnabled(true);
            }
        }
        boolean z = false;
        if (savedInstanceState != null) {
            this.mAutoFillResetNeeded = savedInstanceState.getBoolean(AUTOFILL_RESET_NEEDED, false);
            this.mLastAutofillId = savedInstanceState.getInt(LAST_AUTOFILL_ID, View.LAST_APP_AUTOFILL_ID);
            if (this.mAutoFillResetNeeded) {
                getAutofillManager().onCreate(savedInstanceState);
            }
            this.mFragments.restoreAllState(savedInstanceState.getParcelable(FRAGMENTS_TAG), this.mLastNonConfigurationInstances != null ? this.mLastNonConfigurationInstances.fragments : null);
        }
        this.mFragments.dispatchCreate();
        dispatchActivityCreated(savedInstanceState);
        if (this.mVoiceInteractor != null) {
            this.mVoiceInteractor.attachActivity(this);
        }
        if (savedInstanceState != null) {
            z = true;
        }
        this.mRestoredFromBundle = z;
        this.mCalled = true;
    }

    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        onCreate(savedInstanceState);
    }

    /* access modifiers changed from: package-private */
    public final void performRestoreInstanceState(Bundle savedInstanceState) {
        onRestoreInstanceState(savedInstanceState);
        restoreManagedDialogs(savedInstanceState);
    }

    /* access modifiers changed from: package-private */
    public final void performRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        onRestoreInstanceState(savedInstanceState, persistentState);
        if (savedInstanceState != null) {
            restoreManagedDialogs(savedInstanceState);
        }
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Bundle windowState;
        if (this.mWindow != null && (windowState = savedInstanceState.getBundle(WINDOW_HIERARCHY_TAG)) != null) {
            this.mWindow.restoreHierarchyState(windowState);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    private void restoreManagedDialogs(Bundle savedInstanceState) {
        Bundle b = savedInstanceState.getBundle(SAVED_DIALOGS_TAG);
        if (b != null) {
            this.mManagedDialogs = new SparseArray<>(numDialogs);
            for (int valueOf : b.getIntArray(SAVED_DIALOG_IDS_KEY)) {
                Integer dialogId = Integer.valueOf(valueOf);
                Bundle dialogState = b.getBundle(savedDialogKeyFor(dialogId.intValue()));
                if (dialogState != null) {
                    ManagedDialog md = new ManagedDialog();
                    md.mArgs = b.getBundle(savedDialogArgsKeyFor(dialogId.intValue()));
                    md.mDialog = createDialog(dialogId, dialogState, md.mArgs);
                    if (md.mDialog != null) {
                        this.mManagedDialogs.put(dialogId.intValue(), md);
                        onPrepareDialog(dialogId.intValue(), md.mDialog, md.mArgs);
                        md.mDialog.onRestoreInstanceState(dialogState);
                    }
                }
            }
        }
    }

    private Dialog createDialog(Integer dialogId, Bundle state, Bundle args) {
        Dialog dialog = onCreateDialog(dialogId.intValue(), args);
        if (dialog == null) {
            return null;
        }
        dialog.dispatchOnCreate(state);
        return dialog;
    }

    private static String savedDialogKeyFor(int key) {
        return SAVED_DIALOG_KEY_PREFIX + key;
    }

    private static String savedDialogArgsKeyFor(int key) {
        return SAVED_DIALOG_ARGS_KEY_PREFIX + key;
    }

    /* access modifiers changed from: protected */
    public void onPostCreate(Bundle savedInstanceState) {
        if (!isChild()) {
            this.mTitleReady = true;
            onTitleChanged(getTitle(), getTitleColor());
        }
        this.mCalled = true;
        notifyContentCaptureManagerIfNeeded(1);
    }

    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        onPostCreate(savedInstanceState);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.mCalled = true;
        this.mFragments.doLoaderStart();
        dispatchActivityStarted();
        if (this.mAutoFillResetNeeded) {
            getAutofillManager().onVisibleForAutofill();
        }
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        this.mCalled = true;
    }

    @Deprecated
    public void onStateNotSaved() {
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        View focus;
        dispatchActivityResumed();
        this.mActivityTransitionState.onResume(this);
        enableAutofillCompatibilityIfNeeded();
        if (this.mAutoFillResetNeeded && !this.mAutoFillIgnoreFirstResumePause && (focus = getCurrentFocus()) != null && focus.canNotifyAutofillEnterExitEvent()) {
            getAutofillManager().notifyViewEntered(focus);
        }
        notifyContentCaptureManagerIfNeeded(2);
        this.mCalled = true;
    }

    /* access modifiers changed from: protected */
    public void onPostResume() {
        Window win = getWindow();
        if (win != null) {
            win.makeActive();
        }
        if (this.mActionBar != null) {
            this.mActionBar.setShowHideAnimationEnabled(true);
        }
        this.mCalled = true;
    }

    public void onTopResumedActivityChanged(boolean isTopResumedActivity) {
    }

    /* access modifiers changed from: package-private */
    public final void performTopResumedActivityChanged(boolean isTopResumedActivity, String reason) {
        onTopResumedActivityChanged(isTopResumedActivity);
        writeEventLog(isTopResumedActivity ? LOG_AM_ON_TOP_RESUMED_GAINED_CALLED : LOG_AM_ON_TOP_RESUMED_LOST_CALLED, reason);
    }

    /* access modifiers changed from: package-private */
    public void setVoiceInteractor(IVoiceInteractor voiceInteractor) {
        if (!(this.mVoiceInteractor == null || this.mVoiceInteractor.getActiveRequests() == null)) {
            for (VoiceInteractor.Request activeRequest : this.mVoiceInteractor.getActiveRequests()) {
                activeRequest.cancel();
                activeRequest.clear();
            }
        }
        if (voiceInteractor == null) {
            this.mVoiceInteractor = null;
        } else {
            this.mVoiceInteractor = new VoiceInteractor(voiceInteractor, this, this, Looper.myLooper());
        }
    }

    public int getNextAutofillId() {
        if (this.mLastAutofillId == 2147483646) {
            this.mLastAutofillId = View.LAST_APP_AUTOFILL_ID;
        }
        this.mLastAutofillId++;
        return this.mLastAutofillId;
    }

    public AutofillId autofillClientGetNextAutofillId() {
        return new AutofillId(getNextAutofillId());
    }

    public boolean isVoiceInteraction() {
        return this.mVoiceInteractor != null;
    }

    public boolean isVoiceInteractionRoot() {
        try {
            if (this.mVoiceInteractor == null || !ActivityTaskManager.getService().isRootVoiceInteraction(this.mToken)) {
                return false;
            }
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public VoiceInteractor getVoiceInteractor() {
        return this.mVoiceInteractor;
    }

    public boolean isLocalVoiceInteractionSupported() {
        try {
            return ActivityTaskManager.getService().supportsLocalVoiceInteraction();
        } catch (RemoteException e) {
            return false;
        }
    }

    public void startLocalVoiceInteraction(Bundle privateOptions) {
        try {
            ActivityTaskManager.getService().startLocalVoiceInteraction(this.mToken, privateOptions);
        } catch (RemoteException e) {
        }
    }

    public void onLocalVoiceInteractionStarted() {
    }

    public void onLocalVoiceInteractionStopped() {
    }

    public void stopLocalVoiceInteraction() {
        try {
            ActivityTaskManager.getService().stopLocalVoiceInteraction(this.mToken);
        } catch (RemoteException e) {
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
    }

    /* access modifiers changed from: package-private */
    public final void performSaveInstanceState(Bundle outState) {
        dispatchActivityPreSaveInstanceState(outState);
        onSaveInstanceState(outState);
        saveManagedDialogs(outState);
        this.mActivityTransitionState.saveState(outState);
        storeHasCurrentPermissionRequest(outState);
        dispatchActivityPostSaveInstanceState(outState);
    }

    /* access modifiers changed from: package-private */
    public final void performSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        dispatchActivityPreSaveInstanceState(outState);
        onSaveInstanceState(outState, outPersistentState);
        saveManagedDialogs(outState);
        storeHasCurrentPermissionRequest(outState);
        dispatchActivityPostSaveInstanceState(outState);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(WINDOW_HIERARCHY_TAG, this.mWindow.saveHierarchyState());
        outState.putInt(LAST_AUTOFILL_ID, this.mLastAutofillId);
        Parcelable p = this.mFragments.saveAllState();
        if (p != null) {
            outState.putParcelable(FRAGMENTS_TAG, p);
        }
        if (this.mAutoFillResetNeeded) {
            outState.putBoolean(AUTOFILL_RESET_NEEDED, true);
            getAutofillManager().onSaveInstanceState(outState);
        }
        dispatchActivitySaveInstanceState(outState);
    }

    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        onSaveInstanceState(outState);
    }

    @UnsupportedAppUsage
    private void saveManagedDialogs(Bundle outState) {
        int numDialogs;
        if (this.mManagedDialogs != null && (numDialogs = this.mManagedDialogs.size()) != 0) {
            Bundle dialogState = new Bundle();
            int[] ids = new int[this.mManagedDialogs.size()];
            for (int i = 0; i < numDialogs; i++) {
                int key = this.mManagedDialogs.keyAt(i);
                ids[i] = key;
                ManagedDialog md = this.mManagedDialogs.valueAt(i);
                dialogState.putBundle(savedDialogKeyFor(key), md.mDialog.onSaveInstanceState());
                if (md.mArgs != null) {
                    dialogState.putBundle(savedDialogArgsKeyFor(key), md.mArgs);
                }
            }
            dialogState.putIntArray(SAVED_DIALOG_IDS_KEY, ids);
            outState.putBundle(SAVED_DIALOGS_TAG, dialogState);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        dispatchActivityPaused();
        if (this.mAutoFillResetNeeded) {
            if (!this.mAutoFillIgnoreFirstResumePause) {
                View focus = getCurrentFocus();
                if (focus != null && focus.canNotifyAutofillEnterExitEvent()) {
                    getAutofillManager().notifyViewExited(focus);
                }
            } else {
                this.mAutoFillIgnoreFirstResumePause = false;
            }
        }
        notifyContentCaptureManagerIfNeeded(3);
        this.mCalled = true;
    }

    /* access modifiers changed from: protected */
    public void onUserLeaveHint() {
    }

    @Deprecated
    public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
        return false;
    }

    public CharSequence onCreateDescription() {
        return null;
    }

    public void onProvideAssistData(Bundle data) {
    }

    public void onProvideAssistContent(AssistContent outContent) {
    }

    public void onGetDirectActions(CancellationSignal cancellationSignal, Consumer<List<DirectAction>> callback) {
        callback.accept(Collections.emptyList());
    }

    public void onPerformDirectAction(String actionId, Bundle arguments, CancellationSignal cancellationSignal, Consumer<Bundle> consumer) {
    }

    public final void requestShowKeyboardShortcuts() {
        Intent intent = new Intent(Intent.ACTION_SHOW_KEYBOARD_SHORTCUTS);
        intent.setPackage(KEYBOARD_SHORTCUTS_RECEIVER_PKG_NAME);
        sendBroadcastAsUser(intent, Process.myUserHandle());
    }

    public final void dismissKeyboardShortcutsHelper() {
        Intent intent = new Intent(Intent.ACTION_DISMISS_KEYBOARD_SHORTCUTS);
        intent.setPackage(KEYBOARD_SHORTCUTS_RECEIVER_PKG_NAME);
        sendBroadcastAsUser(intent, Process.myUserHandle());
    }

    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {
        if (menu != null) {
            KeyboardShortcutGroup group = null;
            int menuSize = menu.size();
            for (int i = 0; i < menuSize; i++) {
                MenuItem item = menu.getItem(i);
                CharSequence title = item.getTitle();
                char alphaShortcut = item.getAlphabeticShortcut();
                int alphaModifiers = item.getAlphabeticModifiers();
                if (!(title == null || alphaShortcut == 0)) {
                    if (group == null) {
                        int resource = this.mApplication.getApplicationInfo().labelRes;
                        group = new KeyboardShortcutGroup((CharSequence) resource != 0 ? getString(resource) : null);
                    }
                    group.addItem(new KeyboardShortcutInfo(title, alphaShortcut, alphaModifiers));
                }
            }
            if (group != null) {
                data.add(group);
            }
        }
    }

    public boolean showAssist(Bundle args) {
        try {
            return ActivityTaskManager.getService().showAssistFromActivity(this.mToken, args);
        } catch (RemoteException e) {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        if (this.mActionBar != null) {
            this.mActionBar.setShowHideAnimationEnabled(false);
        }
        this.mActivityTransitionState.onStop();
        dispatchActivityStopped();
        this.mTranslucentCallback = null;
        this.mCalled = true;
        if (this.mAutoFillResetNeeded) {
            getAutofillManager().onInvisibleForAutofill();
        }
        if (isFinishing()) {
            if (this.mAutoFillResetNeeded) {
                getAutofillManager().onActivityFinishing();
            } else if (this.mIntent != null && this.mIntent.hasExtra(AutofillManager.EXTRA_RESTORE_SESSION_TOKEN)) {
                getAutofillManager().onPendingSaveUi(1, this.mIntent.getIBinderExtra(AutofillManager.EXTRA_RESTORE_SESSION_TOKEN));
            }
        }
        this.mEnterAnimationComplete = false;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mCalled = true;
        if (this.mManagedDialogs != null) {
            int numDialogs = this.mManagedDialogs.size();
            for (int i = 0; i < numDialogs; i++) {
                ManagedDialog md = this.mManagedDialogs.valueAt(i);
                if (md.mDialog.isShowing()) {
                    md.mDialog.dismiss();
                }
            }
            this.mManagedDialogs = null;
        }
        synchronized (this.mManagedCursors) {
            int numCursors = this.mManagedCursors.size();
            for (int i2 = 0; i2 < numCursors; i2++) {
                ManagedCursor c = this.mManagedCursors.get(i2);
                if (c != null) {
                    c.mCursor.close();
                }
            }
            this.mManagedCursors.clear();
        }
        if (this.mSearchManager != null) {
            this.mSearchManager.stopSearch();
        }
        if (this.mActionBar != null) {
            this.mActionBar.onDestroy();
        }
        dispatchActivityDestroyed();
        notifyContentCaptureManagerIfNeeded(4);
    }

    public void reportFullyDrawn() {
        if (this.mDoReportFullyDrawn) {
            this.mDoReportFullyDrawn = false;
            try {
                ActivityTaskManager.getService().reportActivityFullyDrawn(this.mToken, this.mRestoredFromBundle);
                VMRuntime.getRuntime().notifyStartupCompleted();
            } catch (RemoteException e) {
            }
        }
    }

    public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        Log.d("ActivityRoot", "onMultiWindowModeChanged=" + isInMultiWindowMode);
        sendBroadcastAsUser(new Intent("com.wits.on_multi_window_mode").putExtra("multi_window", isInMultiWindowMode), Process.myUserHandle());
        onMultiWindowModeChanged(isInMultiWindowMode);
    }

    @Deprecated
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
    }

    public boolean isInMultiWindowMode() {
        try {
            return ActivityTaskManager.getService().isInMultiWindowMode(this.mToken);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        onPictureInPictureModeChanged(isInPictureInPictureMode);
    }

    @Deprecated
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
    }

    public boolean isInPictureInPictureMode() {
        try {
            return ActivityTaskManager.getService().isInPictureInPictureMode(this.mToken);
        } catch (RemoteException e) {
            return false;
        }
    }

    @Deprecated
    public void enterPictureInPictureMode() {
        enterPictureInPictureMode(new PictureInPictureParams.Builder().build());
    }

    @Deprecated
    public boolean enterPictureInPictureMode(PictureInPictureArgs args) {
        return enterPictureInPictureMode(PictureInPictureArgs.convert(args));
    }

    public boolean enterPictureInPictureMode(PictureInPictureParams params) {
        try {
            if (!deviceSupportsPictureInPictureMode()) {
                return false;
            }
            if (params == null) {
                throw new IllegalArgumentException("Expected non-null picture-in-picture params");
            } else if (this.mCanEnterPictureInPicture) {
                return ActivityTaskManager.getService().enterPictureInPictureMode(this.mToken, params);
            } else {
                throw new IllegalStateException("Activity must be resumed to enter picture-in-picture");
            }
        } catch (RemoteException e) {
            return false;
        }
    }

    @Deprecated
    public void setPictureInPictureArgs(PictureInPictureArgs args) {
        setPictureInPictureParams(PictureInPictureArgs.convert(args));
    }

    public void setPictureInPictureParams(PictureInPictureParams params) {
        try {
            if (deviceSupportsPictureInPictureMode()) {
                if (params != null) {
                    ActivityTaskManager.getService().setPictureInPictureParams(this.mToken, params);
                    return;
                }
                throw new IllegalArgumentException("Expected non-null picture-in-picture params");
            }
        } catch (RemoteException e) {
        }
    }

    public int getMaxNumPictureInPictureActions() {
        try {
            return ActivityTaskManager.getService().getMaxNumPictureInPictureActions(this.mToken);
        } catch (RemoteException e) {
            return 0;
        }
    }

    private boolean deviceSupportsPictureInPictureMode() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
    }

    /* access modifiers changed from: package-private */
    public void dispatchMovedToDisplay(int displayId, Configuration config) {
        updateDisplay(displayId);
        onMovedToDisplay(displayId, config);
    }

    public void onMovedToDisplay(int displayId, Configuration config) {
    }

    public void onConfigurationChanged(Configuration newConfig) {
        this.mCalled = true;
        this.mFragments.dispatchConfigurationChanged(newConfig);
        if (this.mWindow != null) {
            this.mWindow.onConfigurationChanged(newConfig);
        }
        if (this.mActionBar != null) {
            this.mActionBar.onConfigurationChanged(newConfig);
        }
    }

    public int getChangingConfigurations() {
        return this.mConfigChangeFlags;
    }

    public Object getLastNonConfigurationInstance() {
        if (this.mLastNonConfigurationInstances != null) {
            return this.mLastNonConfigurationInstances.activity;
        }
        return null;
    }

    public Object onRetainNonConfigurationInstance() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public HashMap<String, Object> getLastNonConfigurationChildInstances() {
        if (this.mLastNonConfigurationInstances != null) {
            return this.mLastNonConfigurationInstances.children;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public HashMap<String, Object> onRetainNonConfigurationChildInstances() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public NonConfigurationInstances retainNonConfigurationInstances() {
        Object activity = onRetainNonConfigurationInstance();
        HashMap<String, Object> children = onRetainNonConfigurationChildInstances();
        FragmentManagerNonConfig fragments = this.mFragments.retainNestedNonConfig();
        this.mFragments.doLoaderStart();
        this.mFragments.doLoaderStop(true);
        ArrayMap<String, LoaderManager> loaders = this.mFragments.retainLoaderNonConfig();
        if (activity == null && children == null && fragments == null && loaders == null && this.mVoiceInteractor == null) {
            return null;
        }
        NonConfigurationInstances nci = new NonConfigurationInstances();
        nci.activity = activity;
        nci.children = children;
        nci.fragments = fragments;
        nci.loaders = loaders;
        if (this.mVoiceInteractor != null) {
            this.mVoiceInteractor.retainInstance();
            nci.voiceInteractor = this.mVoiceInteractor;
        }
        return nci;
    }

    public void onLowMemory() {
        this.mCalled = true;
        this.mFragments.dispatchLowMemory();
    }

    public void onTrimMemory(int level) {
        this.mCalled = true;
        this.mFragments.dispatchTrimMemory(level);
    }

    @Deprecated
    public FragmentManager getFragmentManager() {
        return this.mFragments.getFragmentManager();
    }

    @Deprecated
    public void onAttachFragment(Fragment fragment) {
    }

    @Deprecated
    @UnsupportedAppUsage
    public final Cursor managedQuery(Uri uri, String[] projection, String selection, String sortOrder) {
        Cursor c = getContentResolver().query(uri, projection, selection, (String[]) null, sortOrder);
        if (c != null) {
            startManagingCursor(c);
        }
        return c;
    }

    @Deprecated
    public final Cursor managedQuery(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        if (c != null) {
            startManagingCursor(c);
        }
        return c;
    }

    @Deprecated
    public void startManagingCursor(Cursor c) {
        synchronized (this.mManagedCursors) {
            this.mManagedCursors.add(new ManagedCursor(c));
        }
    }

    @Deprecated
    public void stopManagingCursor(Cursor c) {
        synchronized (this.mManagedCursors) {
            int N = this.mManagedCursors.size();
            int i = 0;
            while (true) {
                if (i >= N) {
                    break;
                } else if (this.mManagedCursors.get(i).mCursor == c) {
                    this.mManagedCursors.remove(i);
                    break;
                } else {
                    i++;
                }
            }
        }
    }

    @Deprecated
    @UnsupportedAppUsage
    public void setPersistent(boolean isPersistent) {
    }

    public <T extends View> T findViewById(int id) {
        return getWindow().findViewById(id);
    }

    public final <T extends View> T requireViewById(int id) {
        T view = findViewById(id);
        if (view != null) {
            return view;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this Activity");
    }

    public ActionBar getActionBar() {
        initWindowDecorActionBar();
        return this.mActionBar;
    }

    public void setActionBar(Toolbar toolbar) {
        ActionBar ab = getActionBar();
        if (!(ab instanceof WindowDecorActionBar)) {
            this.mMenuInflater = null;
            if (ab != null) {
                ab.onDestroy();
            }
            if (toolbar != null) {
                ToolbarActionBar tbab = new ToolbarActionBar(toolbar, getTitle(), this);
                this.mActionBar = tbab;
                this.mWindow.setCallback(tbab.getWrappedWindowCallback());
            } else {
                this.mActionBar = null;
                this.mWindow.setCallback(this);
            }
            invalidateOptionsMenu();
            return;
        }
        throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_ACTION_BAR and set android:windowActionBar to false in your theme to use a Toolbar instead.");
    }

    private void initWindowDecorActionBar() {
        Window window = getWindow();
        window.getDecorView();
        if (!isChild() && window.hasFeature(8) && this.mActionBar == null) {
            this.mActionBar = new WindowDecorActionBar(this);
            this.mActionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
            this.mWindow.setDefaultIcon(this.mActivityInfo.getIconResource());
            this.mWindow.setDefaultLogo(this.mActivityInfo.getLogoResource());
        }
    }

    public void setContentView(int layoutResID) {
        getWindow().setContentView(layoutResID);
        initWindowDecorActionBar();
    }

    public void setContentView(View view) {
        getWindow().setContentView(view);
        initWindowDecorActionBar();
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getWindow().setContentView(view, params);
        initWindowDecorActionBar();
    }

    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getWindow().addContentView(view, params);
        initWindowDecorActionBar();
    }

    public TransitionManager getContentTransitionManager() {
        return getWindow().getTransitionManager();
    }

    public void setContentTransitionManager(TransitionManager tm) {
        getWindow().setTransitionManager(tm);
    }

    public Scene getContentScene() {
        return getWindow().getContentScene();
    }

    public void setFinishOnTouchOutside(boolean finish) {
        this.mWindow.setCloseOnTouchOutside(finish);
    }

    public final void setDefaultKeyMode(int mode) {
        this.mDefaultKeyMode = mode;
        switch (mode) {
            case 0:
            case 2:
                this.mDefaultKeySsb = null;
                return;
            case 1:
            case 3:
            case 4:
                this.mDefaultKeySsb = new SpannableStringBuilder();
                Selection.setSelection(this.mDefaultKeySsb, 0);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled;
        if (keyCode == 4) {
            if (getApplicationInfo().targetSdkVersion >= 5) {
                event.startTracking();
            } else {
                onBackPressed();
            }
            return true;
        } else if (this.mDefaultKeyMode == 0) {
            return false;
        } else {
            if (this.mDefaultKeyMode == 2) {
                Window w = getWindow();
                if (!w.hasFeature(0) || !w.performPanelShortcut(0, keyCode, event, 2)) {
                    return false;
                }
                return true;
            } else if (keyCode == 61) {
                return false;
            } else {
                boolean clearSpannable = false;
                if (event.getRepeatCount() != 0 || event.isSystem()) {
                    clearSpannable = true;
                    handled = false;
                } else {
                    handled = TextKeyListener.getInstance().onKeyDown((View) null, this.mDefaultKeySsb, keyCode, event);
                    if (handled && this.mDefaultKeySsb.length() > 0) {
                        String str = this.mDefaultKeySsb.toString();
                        clearSpannable = true;
                        int i = this.mDefaultKeyMode;
                        if (i != 1) {
                            switch (i) {
                                case 3:
                                    startSearch(str, false, (Bundle) null, false);
                                    break;
                                case 4:
                                    startSearch(str, false, (Bundle) null, true);
                                    break;
                            }
                        } else {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(WebView.SCHEME_TEL + str));
                            intent.addFlags(268435456);
                            startActivity(intent);
                        }
                    }
                }
                if (clearSpannable) {
                    this.mDefaultKeySsb.clear();
                    this.mDefaultKeySsb.clearSpans();
                    Selection.setSelection(this.mDefaultKeySsb, 0);
                }
                return handled;
            }
        }
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (getApplicationInfo().targetSdkVersion < 5 || keyCode != 4 || !event.isTracking() || event.isCanceled()) {
            return false;
        }
        onBackPressed();
        return true;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    public void onBackPressed() {
        if (this.mActionBar == null || !this.mActionBar.collapseActionView()) {
            FragmentManager fragmentManager = this.mFragments.getFragmentManager();
            if (!fragmentManager.isStateSaved() && fragmentManager.popBackStackImmediate()) {
                return;
            }
            if (!isTaskRoot()) {
                finishAfterTransition();
                return;
            }
            try {
                ActivityTaskManager.getService().onBackPressedOnTaskRoot(this.mToken, new IRequestFinishCallback.Stub() {
                    public void requestFinish() {
                        Activity.this.mHandler.post(new Runnable() {
                            public final void run() {
                                Activity.this.finishAfterTransition();
                            }
                        });
                    }
                });
            } catch (RemoteException e) {
                finishAfterTransition();
            }
        }
    }

    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        ActionBar actionBar = getActionBar();
        return actionBar != null && actionBar.onKeyShortcut(keyCode, event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mWindow.shouldCloseOnTouch(this, event)) {
            return false;
        }
        finish();
        return true;
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return false;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        return false;
    }

    public void onUserInteraction() {
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        View decor;
        if (this.mParent == null && (decor = this.mDecor) != null && decor.getParent() != null) {
            getWindowManager().updateViewLayout(decor, params);
            if (this.mContentCaptureManager != null) {
                this.mContentCaptureManager.updateWindowAttributes(params);
            }
        }
    }

    public void onContentChanged() {
    }

    public void onWindowFocusChanged(boolean hasFocus) {
    }

    public void onAttachedToWindow() {
    }

    public void onDetachedFromWindow() {
    }

    public boolean hasWindowFocus() {
        View d;
        Window w = getWindow();
        if (w == null || (d = w.getDecorView()) == null) {
            return false;
        }
        return d.hasWindowFocus();
    }

    public void onWindowDismissed(boolean finishTask, boolean suppressWindowTransition) {
        finish(finishTask ? 2 : 0);
        if (suppressWindowTransition) {
            overridePendingTransition(0, 0);
        }
    }

    public void toggleFreeformWindowingMode() throws RemoteException {
        ActivityTaskManager.getService().toggleFreeformWindowingMode(this.mToken);
    }

    public void enterPictureInPictureModeIfPossible() {
        if (this.mActivityInfo.supportsPictureInPicture()) {
            enterPictureInPictureMode();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        onUserInteraction();
        if (event.getKeyCode() == 82 && this.mActionBar != null && this.mActionBar.onMenuKeyEvent(event)) {
            return true;
        }
        Window win = getWindow();
        if (win.superDispatchKeyEvent(event)) {
            return true;
        }
        View decor = this.mDecor;
        if (decor == null) {
            decor = win.getDecorView();
        }
        return event.dispatch(this, decor != null ? decor.getKeyDispatcherState() : null, this);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        onUserInteraction();
        if (getWindow().superDispatchKeyShortcutEvent(event)) {
            return true;
        }
        return onKeyShortcut(event.getKeyCode(), event);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            onUserInteraction();
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean dispatchTrackballEvent(MotionEvent ev) {
        onUserInteraction();
        if (getWindow().superDispatchTrackballEvent(ev)) {
            return true;
        }
        return onTrackballEvent(ev);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        onUserInteraction();
        if (getWindow().superDispatchGenericMotionEvent(ev)) {
            return true;
        }
        return onGenericMotionEvent(ev);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        event.setClassName(getClass().getName());
        event.setPackageName(getPackageName());
        ViewGroup.LayoutParams params = getWindow().getAttributes();
        event.setFullScreen(params.width == -1 && params.height == -1);
        CharSequence title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            event.getText().add(title);
        }
        return true;
    }

    public View onCreatePanelView(int featureId) {
        return null;
    }

    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == 0) {
            return onCreateOptionsMenu(menu) | this.mFragments.dispatchCreateOptionsMenu(menu, getMenuInflater());
        }
        return false;
    }

    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (featureId == 0) {
            return onPrepareOptionsMenu(menu) | this.mFragments.dispatchPrepareOptionsMenu(menu);
        }
        return true;
    }

    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == 8) {
            initWindowDecorActionBar();
            if (this.mActionBar != null) {
                this.mActionBar.dispatchMenuVisibilityChanged(true);
            } else {
                Log.e(TAG, "Tried to open action bar menu with no action bar");
            }
        }
        return true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        CharSequence titleCondensed = item.getTitleCondensed();
        if (featureId == 0) {
            if (titleCondensed != null) {
                EventLog.writeEvent(50000, 0, titleCondensed.toString());
            }
            if (onOptionsItemSelected(item) || this.mFragments.dispatchOptionsItemSelected(item)) {
                return true;
            }
            if (item.getItemId() != 16908332 || this.mActionBar == null || (this.mActionBar.getDisplayOptions() & 4) == 0) {
                return false;
            }
            if (this.mParent == null) {
                return onNavigateUp();
            }
            return this.mParent.onNavigateUpFromChild(this);
        } else if (featureId != 6) {
            return false;
        } else {
            if (titleCondensed != null) {
                EventLog.writeEvent(50000, 1, titleCondensed.toString());
            }
            if (onContextItemSelected(item)) {
                return true;
            }
            return this.mFragments.dispatchContextItemSelected(item);
        }
    }

    public void onPanelClosed(int featureId, Menu menu) {
        if (featureId == 0) {
            this.mFragments.dispatchOptionsMenuClosed(menu);
            onOptionsMenuClosed(menu);
        } else if (featureId == 6) {
            onContextMenuClosed(menu);
        } else if (featureId == 8) {
            initWindowDecorActionBar();
            this.mActionBar.dispatchMenuVisibilityChanged(false);
        }
    }

    public void invalidateOptionsMenu() {
        if (!this.mWindow.hasFeature(0)) {
            return;
        }
        if (this.mActionBar == null || !this.mActionBar.invalidateOptionsMenu()) {
            this.mWindow.invalidatePanelMenu(0);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.mParent != null) {
            return this.mParent.onCreateOptionsMenu(menu);
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.mParent != null) {
            return this.mParent.onPrepareOptionsMenu(menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.mParent != null) {
            return this.mParent.onOptionsItemSelected(item);
        }
        return false;
    }

    public boolean onNavigateUp() {
        Intent upIntent = getParentActivityIntent();
        if (upIntent == null) {
            return false;
        }
        if (this.mActivityInfo.taskAffinity == null) {
            finish();
            return true;
        } else if (shouldUpRecreateTask(upIntent)) {
            TaskStackBuilder b = TaskStackBuilder.create(this);
            onCreateNavigateUpTaskStack(b);
            onPrepareNavigateUpTaskStack(b);
            b.startActivities();
            if (this.mResultCode == 0 && this.mResultData == null) {
                finishAffinity();
                return true;
            }
            Log.i(TAG, "onNavigateUp only finishing topmost activity to return a result");
            finish();
            return true;
        } else {
            navigateUpTo(upIntent);
            return true;
        }
    }

    public boolean onNavigateUpFromChild(Activity child) {
        return onNavigateUp();
    }

    public void onCreateNavigateUpTaskStack(TaskStackBuilder builder) {
        builder.addParentStack(this);
    }

    public void onPrepareNavigateUpTaskStack(TaskStackBuilder builder) {
    }

    public void onOptionsMenuClosed(Menu menu) {
        if (this.mParent != null) {
            this.mParent.onOptionsMenuClosed(menu);
        }
    }

    public void openOptionsMenu() {
        if (!this.mWindow.hasFeature(0)) {
            return;
        }
        if (this.mActionBar == null || !this.mActionBar.openOptionsMenu()) {
            this.mWindow.openPanel(0, (KeyEvent) null);
        }
    }

    public void closeOptionsMenu() {
        if (!this.mWindow.hasFeature(0)) {
            return;
        }
        if (this.mActionBar == null || !this.mActionBar.closeOptionsMenu()) {
            this.mWindow.closePanel(0);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    }

    public void registerForContextMenu(View view) {
        view.setOnCreateContextMenuListener(this);
    }

    public void unregisterForContextMenu(View view) {
        view.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) null);
    }

    public void openContextMenu(View view) {
        view.showContextMenu();
    }

    public void closeContextMenu() {
        if (this.mWindow.hasFeature(6)) {
            this.mWindow.closePanel(6);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (this.mParent != null) {
            return this.mParent.onContextItemSelected(item);
        }
        return false;
    }

    public void onContextMenuClosed(Menu menu) {
        if (this.mParent != null) {
            this.mParent.onContextMenuClosed(menu);
        }
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public Dialog onCreateDialog(int id) {
        return null;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public Dialog onCreateDialog(int id, Bundle args) {
        return onCreateDialog(id);
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void onPrepareDialog(int id, Dialog dialog) {
        dialog.setOwnerActivity(this);
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        onPrepareDialog(id, dialog);
    }

    @Deprecated
    public final void showDialog(int id) {
        showDialog(id, (Bundle) null);
    }

    @Deprecated
    public final boolean showDialog(int id, Bundle args) {
        if (this.mManagedDialogs == null) {
            this.mManagedDialogs = new SparseArray<>();
        }
        ManagedDialog md = this.mManagedDialogs.get(id);
        if (md == null) {
            md = new ManagedDialog();
            md.mDialog = createDialog(Integer.valueOf(id), (Bundle) null, args);
            if (md.mDialog == null) {
                return false;
            }
            this.mManagedDialogs.put(id, md);
        }
        md.mArgs = args;
        onPrepareDialog(id, md.mDialog, args);
        md.mDialog.show();
        return true;
    }

    @Deprecated
    public final void dismissDialog(int id) {
        if (this.mManagedDialogs != null) {
            ManagedDialog md = this.mManagedDialogs.get(id);
            if (md != null) {
                md.mDialog.dismiss();
                return;
            }
            throw missingDialog(id);
        }
        throw missingDialog(id);
    }

    private IllegalArgumentException missingDialog(int id) {
        return new IllegalArgumentException("no dialog with id " + id + " was ever shown via Activity#showDialog");
    }

    @Deprecated
    public final void removeDialog(int id) {
        ManagedDialog md;
        if (this.mManagedDialogs != null && (md = this.mManagedDialogs.get(id)) != null) {
            md.mDialog.dismiss();
            this.mManagedDialogs.remove(id);
        }
    }

    public boolean onSearchRequested(SearchEvent searchEvent) {
        this.mSearchEvent = searchEvent;
        boolean result = onSearchRequested();
        this.mSearchEvent = null;
        return result;
    }

    public boolean onSearchRequested() {
        int uiMode = getResources().getConfiguration().uiMode & 15;
        if (uiMode == 4 || uiMode == 6) {
            return false;
        }
        startSearch((String) null, false, (Bundle) null, false);
        return true;
    }

    public final SearchEvent getSearchEvent() {
        return this.mSearchEvent;
    }

    public void startSearch(String initialQuery, boolean selectInitialQuery, Bundle appSearchData, boolean globalSearch) {
        ensureSearchManager();
        this.mSearchManager.startSearch(initialQuery, selectInitialQuery, getComponentName(), appSearchData, globalSearch);
    }

    public void triggerSearch(String query, Bundle appSearchData) {
        ensureSearchManager();
        this.mSearchManager.triggerSearch(query, getComponentName(), appSearchData);
    }

    public void takeKeyEvents(boolean get) {
        getWindow().takeKeyEvents(get);
    }

    public final boolean requestWindowFeature(int featureId) {
        return getWindow().requestFeature(featureId);
    }

    public final void setFeatureDrawableResource(int featureId, int resId) {
        getWindow().setFeatureDrawableResource(featureId, resId);
    }

    public final void setFeatureDrawableUri(int featureId, Uri uri) {
        getWindow().setFeatureDrawableUri(featureId, uri);
    }

    public final void setFeatureDrawable(int featureId, Drawable drawable) {
        getWindow().setFeatureDrawable(featureId, drawable);
    }

    public final void setFeatureDrawableAlpha(int featureId, int alpha) {
        getWindow().setFeatureDrawableAlpha(featureId, alpha);
    }

    public LayoutInflater getLayoutInflater() {
        return getWindow().getLayoutInflater();
    }

    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            initWindowDecorActionBar();
            if (this.mActionBar != null) {
                this.mMenuInflater = new MenuInflater(this.mActionBar.getThemedContext(), this);
            } else {
                this.mMenuInflater = new MenuInflater(this);
            }
        }
        return this.mMenuInflater;
    }

    public void setTheme(int resid) {
        super.setTheme(resid);
        this.mWindow.setTheme(resid);
    }

    /* access modifiers changed from: protected */
    public void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        int colorPrimary;
        if (this.mParent == null) {
            super.onApplyThemeResource(theme, resid, first);
        } else {
            try {
                theme.setTo(this.mParent.getTheme());
            } catch (Exception e) {
            }
            theme.applyStyle(resid, false);
        }
        TypedArray a = theme.obtainStyledAttributes(R.styleable.ActivityTaskDescription);
        if (this.mTaskDescription.getPrimaryColor() == 0 && (colorPrimary = a.getColor(1, 0)) != 0 && Color.alpha(colorPrimary) == 255) {
            this.mTaskDescription.setPrimaryColor(colorPrimary);
        }
        int colorPrimary2 = a.getColor(0, 0);
        if (colorPrimary2 != 0 && Color.alpha(colorPrimary2) == 255) {
            this.mTaskDescription.setBackgroundColor(colorPrimary2);
        }
        int statusBarColor = a.getColor(2, 0);
        if (statusBarColor != 0) {
            this.mTaskDescription.setStatusBarColor(statusBarColor);
        }
        int navigationBarColor = a.getColor(3, 0);
        if (navigationBarColor != 0) {
            this.mTaskDescription.setNavigationBarColor(navigationBarColor);
        }
        if (!(getApplicationInfo().targetSdkVersion < 29)) {
            this.mTaskDescription.setEnsureStatusBarContrastWhenTransparent(a.getBoolean(4, false));
            this.mTaskDescription.setEnsureNavigationBarContrastWhenTransparent(a.getBoolean(5, true));
        }
        a.recycle();
        setTaskDescription(this.mTaskDescription);
    }

    public final void requestPermissions(String[] permissions, int requestCode) {
        if (requestCode < 0) {
            throw new IllegalArgumentException("requestCode should be >= 0");
        } else if (this.mHasCurrentPermissionsRequest) {
            Log.w(TAG, "Can request only one set of permissions at a time");
            onRequestPermissionsResult(requestCode, new String[0], new int[0]);
        } else {
            startActivityForResult(REQUEST_PERMISSIONS_WHO_PREFIX, getPackageManager().buildRequestPermissionsIntent(permissions), requestCode, (Bundle) null);
            this.mHasCurrentPermissionsRequest = true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    public boolean shouldShowRequestPermissionRationale(String permission) {
        return getPackageManager().shouldShowRequestPermissionRationale(permission);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode, (Bundle) null);
    }

    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (this.mParent == null) {
            Bundle options2 = transferSpringboardActivityOptions(options);
            Instrumentation.ActivityResult ar = this.mInstrumentation.execStartActivity((Context) this, (IBinder) this.mMainThread.getApplicationThread(), this.mToken, this, intent, requestCode, options2);
            if (ar != null) {
                this.mMainThread.sendActivityResult(this.mToken, this.mEmbeddedID, requestCode, ar.getResultCode(), ar.getResultData());
            }
            if (requestCode >= 0) {
                this.mStartedActivity = true;
            }
            cancelInputsAndStartExitTransition(options2);
        } else if (options != null) {
            this.mParent.startActivityFromChild(this, intent, requestCode, options);
        } else {
            this.mParent.startActivityFromChild(this, intent, requestCode);
        }
    }

    private void cancelInputsAndStartExitTransition(Bundle options) {
        View decor = this.mWindow != null ? this.mWindow.peekDecorView() : null;
        if (decor != null) {
            decor.cancelPendingInputEvents();
        }
        if (options != null) {
            this.mActivityTransitionState.startExitOutTransition(this, options);
        }
    }

    public boolean isActivityTransitionRunning() {
        return this.mActivityTransitionState.isTransitionRunning();
    }

    private Bundle transferSpringboardActivityOptions(Bundle options) {
        ActivityOptions activityOptions;
        if (options != null || this.mWindow == null || this.mWindow.isActive() || (activityOptions = getActivityOptions()) == null || activityOptions.getAnimationType() != 5) {
            return options;
        }
        return activityOptions.toBundle();
    }

    @UnsupportedAppUsage
    public void startActivityForResultAsUser(Intent intent, int requestCode, UserHandle user) {
        startActivityForResultAsUser(intent, requestCode, (Bundle) null, user);
    }

    public void startActivityForResultAsUser(Intent intent, int requestCode, Bundle options, UserHandle user) {
        startActivityForResultAsUser(intent, this.mEmbeddedID, requestCode, options, user);
    }

    public void startActivityForResultAsUser(Intent intent, String resultWho, int requestCode, Bundle options, UserHandle user) {
        if (this.mParent == null) {
            Bundle options2 = transferSpringboardActivityOptions(options);
            Instrumentation.ActivityResult ar = this.mInstrumentation.execStartActivity(this, this.mMainThread.getApplicationThread(), this.mToken, resultWho, intent, requestCode, options2, user);
            if (ar != null) {
                this.mMainThread.sendActivityResult(this.mToken, this.mEmbeddedID, requestCode, ar.getResultCode(), ar.getResultData());
            }
            if (requestCode >= 0) {
                this.mStartedActivity = true;
            }
            cancelInputsAndStartExitTransition(options2);
            return;
        }
        throw new RuntimeException("Can't be called from a child");
    }

    public void startActivityAsUser(Intent intent, UserHandle user) {
        startActivityAsUser(intent, (Bundle) null, user);
    }

    public void startActivityAsUser(Intent intent, Bundle options, UserHandle user) {
        if (this.mParent == null) {
            Bundle options2 = transferSpringboardActivityOptions(options);
            Instrumentation.ActivityResult ar = this.mInstrumentation.execStartActivity(this, this.mMainThread.getApplicationThread(), this.mToken, this.mEmbeddedID, intent, -1, options2, user);
            if (ar != null) {
                this.mMainThread.sendActivityResult(this.mToken, this.mEmbeddedID, -1, ar.getResultCode(), ar.getResultData());
            }
            cancelInputsAndStartExitTransition(options2);
            return;
        }
        throw new RuntimeException("Can't be called from a child");
    }

    public void startActivityAsCaller(Intent intent, Bundle options, IBinder permissionToken, boolean ignoreTargetSecurity, int userId) {
        if (this.mParent == null) {
            Bundle bundle = options;
            Bundle options2 = transferSpringboardActivityOptions(options);
            Instrumentation.ActivityResult ar = this.mInstrumentation.execStartActivityAsCaller(this, this.mMainThread.getApplicationThread(), this.mToken, this, intent, -1, options2, permissionToken, ignoreTargetSecurity, userId);
            if (ar != null) {
                this.mMainThread.sendActivityResult(this.mToken, this.mEmbeddedID, -1, ar.getResultCode(), ar.getResultData());
            }
            cancelInputsAndStartExitTransition(options2);
            return;
        }
        Bundle bundle2 = options;
        throw new RuntimeException("Can't be called from a child");
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {
        startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, (Bundle) null);
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        if (this.mParent == null) {
            startIntentSenderForResultInner(intent, this.mEmbeddedID, requestCode, fillInIntent, flagsMask, flagsValues, options);
        } else if (options != null) {
            this.mParent.startIntentSenderFromChild(this, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        } else {
            this.mParent.startIntentSenderFromChild(this, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags);
        }
    }

    /* access modifiers changed from: private */
    public void startIntentSenderForResultInner(IntentSender intent, String who, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, Bundle options) throws IntentSender.SendIntentException {
        IIntentSender iIntentSender;
        Intent intent2 = fillInIntent;
        Bundle bundle = options;
        try {
            Bundle options2 = transferSpringboardActivityOptions(bundle);
            String resolvedType = null;
            if (intent2 != null) {
                try {
                    fillInIntent.migrateExtraStreamToClipData();
                    intent2.prepareToLeaveProcess((Context) this);
                    resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
                } catch (RemoteException e) {
                }
            }
            try {
                IActivityTaskManager service = ActivityTaskManager.getService();
                ActivityThread.ApplicationThread applicationThread = this.mMainThread.getApplicationThread();
                if (intent != null) {
                    iIntentSender = intent.getTarget();
                } else {
                    iIntentSender = null;
                }
                String str = resolvedType;
                Bundle options3 = options2;
                try {
                    int result = service.startActivityIntentSender(applicationThread, iIntentSender, intent != null ? intent.getWhitelistToken() : null, fillInIntent, resolvedType, this.mToken, who, requestCode, flagsMask, flagsValues, options2);
                    if (result != -96) {
                        Instrumentation.checkStartActivityResult(result, (Object) null);
                        Bundle options4 = options3;
                        if (options4 != null) {
                            try {
                                cancelInputsAndStartExitTransition(options4);
                            } catch (RemoteException e2) {
                                Bundle bundle2 = options4;
                            }
                        }
                        Bundle bundle3 = options4;
                        if (requestCode >= 0) {
                            this.mStartedActivity = true;
                            return;
                        }
                        return;
                    }
                    throw new IntentSender.SendIntentException();
                } catch (RemoteException e3) {
                    Bundle bundle4 = options3;
                }
            } catch (RemoteException e4) {
                Bundle bundle5 = options2;
            }
        } catch (RemoteException e5) {
            Bundle bundle6 = bundle;
        }
    }

    public void startActivity(Intent intent) {
        startActivity(intent, (Bundle) null);
    }

    public void startActivity(Intent intent, Bundle options) {
        if (options != null) {
            startActivityForResult(intent, -1, options);
        } else {
            startActivityForResult(intent, -1);
        }
    }

    public void startActivities(Intent[] intents) {
        startActivities(intents, (Bundle) null);
    }

    public void startActivities(Intent[] intents, Bundle options) {
        this.mInstrumentation.execStartActivities(this, this.mMainThread.getApplicationThread(), this.mToken, this, intents, options);
    }

    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {
        startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, (Bundle) null);
    }

    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        if (options != null) {
            startIntentSenderForResult(intent, -1, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        } else {
            startIntentSenderForResult(intent, -1, fillInIntent, flagsMask, flagsValues, extraFlags);
        }
    }

    public boolean startActivityIfNeeded(Intent intent, int requestCode) {
        return startActivityIfNeeded(intent, requestCode, (Bundle) null);
    }

    public boolean startActivityIfNeeded(Intent intent, int requestCode, Bundle options) {
        Intent intent2 = intent;
        if (this.mParent == null) {
            int result = 1;
            try {
                Uri referrer = onProvideReferrer();
                if (referrer != null) {
                    intent2.putExtra(Intent.EXTRA_REFERRER, (Parcelable) referrer);
                }
                intent.migrateExtraStreamToClipData();
                intent2.prepareToLeaveProcess((Context) this);
                result = ActivityTaskManager.getService().startActivity(this.mMainThread.getApplicationThread(), getBasePackageName(), intent, intent2.resolveTypeIfNeeded(getContentResolver()), this.mToken, this.mEmbeddedID, requestCode, 1, (ProfilerInfo) null, options);
            } catch (RemoteException e) {
            }
            Instrumentation.checkStartActivityResult(result, intent2);
            if (requestCode >= 0) {
                this.mStartedActivity = true;
            }
            if (result != 1) {
                return true;
            }
            return false;
        }
        throw new UnsupportedOperationException("startActivityIfNeeded can only be called from a top-level activity");
    }

    public boolean startNextMatchingActivity(Intent intent) {
        return startNextMatchingActivity(intent, (Bundle) null);
    }

    public boolean startNextMatchingActivity(Intent intent, Bundle options) {
        if (this.mParent == null) {
            try {
                intent.migrateExtraStreamToClipData();
                intent.prepareToLeaveProcess((Context) this);
                return ActivityTaskManager.getService().startNextMatchingActivity(this.mToken, intent, options);
            } catch (RemoteException e) {
                return false;
            }
        } else {
            throw new UnsupportedOperationException("startNextMatchingActivity can only be called from a top-level activity");
        }
    }

    public void startActivityFromChild(Activity child, Intent intent, int requestCode) {
        startActivityFromChild(child, intent, requestCode, (Bundle) null);
    }

    public void startActivityFromChild(Activity child, Intent intent, int requestCode, Bundle options) {
        Bundle options2 = transferSpringboardActivityOptions(options);
        Instrumentation.ActivityResult ar = this.mInstrumentation.execStartActivity((Context) this, (IBinder) this.mMainThread.getApplicationThread(), this.mToken, child, intent, requestCode, options2);
        if (ar != null) {
            this.mMainThread.sendActivityResult(this.mToken, child.mEmbeddedID, requestCode, ar.getResultCode(), ar.getResultData());
        }
        cancelInputsAndStartExitTransition(options2);
    }

    @Deprecated
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        startActivityFromFragment(fragment, intent, requestCode, (Bundle) null);
    }

    @Deprecated
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options) {
        startActivityForResult(fragment.mWho, intent, requestCode, options);
    }

    public void startActivityAsUserFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options, UserHandle user) {
        startActivityForResultAsUser(intent, fragment.mWho, requestCode, options, user);
    }

    @UnsupportedAppUsage
    public void startActivityForResult(String who, Intent intent, int requestCode, Bundle options) {
        Uri referrer = onProvideReferrer();
        if (referrer != null) {
            intent.putExtra(Intent.EXTRA_REFERRER, (Parcelable) referrer);
        }
        Bundle options2 = transferSpringboardActivityOptions(options);
        Instrumentation.ActivityResult ar = this.mInstrumentation.execStartActivity((Context) this, (IBinder) this.mMainThread.getApplicationThread(), this.mToken, who, intent, requestCode, options2);
        if (ar != null) {
            this.mMainThread.sendActivityResult(this.mToken, who, requestCode, ar.getResultCode(), ar.getResultData());
        }
        cancelInputsAndStartExitTransition(options2);
    }

    public boolean canStartActivityForResult() {
        return true;
    }

    public void startIntentSenderFromChild(Activity child, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {
        startIntentSenderFromChild(child, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, (Bundle) null);
    }

    public void startIntentSenderFromChild(Activity child, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        startIntentSenderForResultInner(intent, child.mEmbeddedID, requestCode, fillInIntent, flagsMask, flagsValues, options);
    }

    public void startIntentSenderFromChildFragment(Fragment child, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        startIntentSenderForResultInner(intent, child.mWho, requestCode, fillInIntent, flagsMask, flagsValues, options);
    }

    public void overridePendingTransition(int enterAnim, int exitAnim) {
        try {
            ActivityTaskManager.getService().overridePendingTransition(this.mToken, getPackageName(), enterAnim, exitAnim);
        } catch (RemoteException e) {
        }
    }

    public final void setResult(int resultCode) {
        synchronized (this) {
            this.mResultCode = resultCode;
            this.mResultData = null;
        }
    }

    public final void setResult(int resultCode, Intent data) {
        synchronized (this) {
            this.mResultCode = resultCode;
            this.mResultData = data;
        }
    }

    public Uri getReferrer() {
        Intent intent = getIntent();
        try {
            Uri referrer = (Uri) intent.getParcelableExtra(Intent.EXTRA_REFERRER);
            if (referrer != null) {
                return referrer;
            }
            String referrerName = intent.getStringExtra(Intent.EXTRA_REFERRER_NAME);
            if (referrerName != null) {
                return Uri.parse(referrerName);
            }
            if (this.mReferrer != null) {
                return new Uri.Builder().scheme("android-app").authority(this.mReferrer).build();
            }
            return null;
        } catch (BadParcelableException e) {
            Log.w(TAG, "Cannot read referrer from intent; intent extras contain unknown custom Parcelable objects");
        }
    }

    public Uri onProvideReferrer() {
        return null;
    }

    public String getCallingPackage() {
        try {
            return ActivityTaskManager.getService().getCallingPackage(this.mToken);
        } catch (RemoteException e) {
            return null;
        }
    }

    public ComponentName getCallingActivity() {
        try {
            return ActivityTaskManager.getService().getCallingActivity(this.mToken);
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setVisible(boolean visible) {
        if (this.mVisibleFromClient != visible) {
            this.mVisibleFromClient = visible;
            if (!this.mVisibleFromServer) {
                return;
            }
            if (visible) {
                makeVisible();
            } else {
                this.mDecor.setVisibility(4);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void makeVisible() {
        if (!this.mWindowAdded) {
            getWindowManager().addView(this.mDecor, getWindow().getAttributes());
            this.mWindowAdded = true;
        }
        this.mDecor.setVisibility(0);
    }

    public boolean isFinishing() {
        return this.mFinished;
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    public boolean isChangingConfigurations() {
        return this.mChangingConfigurations;
    }

    public void recreate() {
        if (this.mParent != null) {
            throw new IllegalStateException("Can only be called on top-level activity");
        } else if (Looper.myLooper() == this.mMainThread.getLooper()) {
            this.mMainThread.scheduleRelaunchActivity(this.mToken);
        } else {
            throw new IllegalStateException("Must be called from main thread");
        }
    }

    @UnsupportedAppUsage
    private void finish(int finishTask) {
        int resultCode;
        Intent resultData;
        if (this.mParent == null) {
            synchronized (this) {
                resultCode = this.mResultCode;
                resultData = this.mResultData;
            }
            if (resultData != null) {
                try {
                    resultData.prepareToLeaveProcess((Context) this);
                } catch (RemoteException e) {
                }
            }
            if (ActivityTaskManager.getService().finishActivity(this.mToken, resultCode, resultData, finishTask)) {
                this.mFinished = true;
            }
        } else {
            this.mParent.finishFromChild(this);
        }
        if (this.mIntent != null && this.mIntent.hasExtra(AutofillManager.EXTRA_RESTORE_SESSION_TOKEN)) {
            getAutofillManager().onPendingSaveUi(2, this.mIntent.getIBinderExtra(AutofillManager.EXTRA_RESTORE_SESSION_TOKEN));
        }
    }

    public void finish() {
        finish(0);
    }

    public void finishAffinity() {
        if (this.mParent != null) {
            throw new IllegalStateException("Can not be called from an embedded activity");
        } else if (this.mResultCode == 0 && this.mResultData == null) {
            try {
                if (ActivityTaskManager.getService().finishActivityAffinity(this.mToken)) {
                    this.mFinished = true;
                }
            } catch (RemoteException e) {
            }
        } else {
            throw new IllegalStateException("Can not be called to deliver a result");
        }
    }

    public void finishFromChild(Activity child) {
        finish();
    }

    public void finishAfterTransition() {
        if (!this.mActivityTransitionState.startExitBackTransition(this)) {
            finish();
        }
    }

    public void finishActivity(int requestCode) {
        if (this.mParent == null) {
            try {
                ActivityTaskManager.getService().finishSubActivity(this.mToken, this.mEmbeddedID, requestCode);
            } catch (RemoteException e) {
            }
        } else {
            this.mParent.finishActivityFromChild(this, requestCode);
        }
    }

    public void finishActivityFromChild(Activity child, int requestCode) {
        try {
            ActivityTaskManager.getService().finishSubActivity(this.mToken, child.mEmbeddedID, requestCode);
        } catch (RemoteException e) {
        }
    }

    public void finishAndRemoveTask() {
        finish(1);
    }

    public boolean releaseInstance() {
        try {
            return ActivityTaskManager.getService().releaseActivityInstance(this.mToken);
        } catch (RemoteException e) {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void onActivityReenter(int resultCode, Intent data) {
    }

    public PendingIntent createPendingResult(int requestCode, Intent data, int flags) {
        String packageName = getPackageName();
        try {
            data.prepareToLeaveProcess((Context) this);
            IIntentSender target = ActivityManager.getService().getIntentSender(3, packageName, this.mParent == null ? this.mToken : this.mParent.mToken, this.mEmbeddedID, requestCode, new Intent[]{data}, (String[]) null, flags, (Bundle) null, getUserId());
            if (target != null) {
                return new PendingIntent(target);
            }
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setRequestedOrientation(int requestedOrientation) {
        if (this.mParent == null) {
            try {
                ActivityTaskManager.getService().setRequestedOrientation(this.mToken, requestedOrientation);
            } catch (RemoteException e) {
            }
        } else {
            this.mParent.setRequestedOrientation(requestedOrientation);
        }
    }

    public int getRequestedOrientation() {
        if (this.mParent != null) {
            return this.mParent.getRequestedOrientation();
        }
        try {
            return ActivityTaskManager.getService().getRequestedOrientation(this.mToken);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int getTaskId() {
        try {
            return ActivityTaskManager.getService().getTaskForActivity(this.mToken, false);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean isTaskRoot() {
        try {
            return ActivityTaskManager.getService().getTaskForActivity(this.mToken, true) >= 0;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean moveTaskToBack(boolean nonRoot) {
        try {
            return ActivityTaskManager.getService().moveActivityTaskToBack(this.mToken, nonRoot);
        } catch (RemoteException e) {
            return false;
        }
    }

    public String getLocalClassName() {
        String pkg = getPackageName();
        String cls = this.mComponent.getClassName();
        int packageLen = pkg.length();
        if (!cls.startsWith(pkg) || cls.length() <= packageLen || cls.charAt(packageLen) != '.') {
            return cls;
        }
        return cls.substring(packageLen + 1);
    }

    public ComponentName getComponentName() {
        return this.mComponent;
    }

    public final ComponentName autofillClientGetComponentName() {
        return getComponentName();
    }

    public final ComponentName contentCaptureClientGetComponentName() {
        return getComponentName();
    }

    public SharedPreferences getPreferences(int mode) {
        return getSharedPreferences(getLocalClassName(), mode);
    }

    private void ensureSearchManager() {
        if (this.mSearchManager == null) {
            try {
                this.mSearchManager = new SearchManager(this, (Handler) null);
            } catch (ServiceManager.ServiceNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public Object getSystemService(String name) {
        if (getBaseContext() == null) {
            throw new IllegalStateException("System services not available to Activities before onCreate()");
        } else if (Context.WINDOW_SERVICE.equals(name)) {
            return this.mWindowManager;
        } else {
            if (!"search".equals(name)) {
                return super.getSystemService(name);
            }
            ensureSearchManager();
            return this.mSearchManager;
        }
    }

    public void setTitle(CharSequence title) {
        this.mTitle = title;
        onTitleChanged(title, this.mTitleColor);
        if (this.mParent != null) {
            this.mParent.onChildTitleChanged(this, title);
        }
    }

    public void setTitle(int titleId) {
        setTitle(getText(titleId));
    }

    @Deprecated
    public void setTitleColor(int textColor) {
        this.mTitleColor = textColor;
        onTitleChanged(this.mTitle, textColor);
    }

    public final CharSequence getTitle() {
        return this.mTitle;
    }

    public final int getTitleColor() {
        return this.mTitleColor;
    }

    /* access modifiers changed from: protected */
    public void onTitleChanged(CharSequence title, int color) {
        if (this.mTitleReady) {
            Window win = getWindow();
            if (win != null) {
                win.setTitle(title);
                if (color != 0) {
                    win.setTitleColor(color);
                }
            }
            if (this.mActionBar != null) {
                this.mActionBar.setWindowTitle(title);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onChildTitleChanged(Activity childActivity, CharSequence title) {
    }

    public void setTaskDescription(ActivityManager.TaskDescription taskDescription) {
        if (this.mTaskDescription != taskDescription) {
            this.mTaskDescription.copyFromPreserveHiddenFields(taskDescription);
            if (taskDescription.getIconFilename() == null && taskDescription.getIcon() != null) {
                int size = ActivityManager.getLauncherLargeIconSizeInner(this);
                this.mTaskDescription.setIcon(Bitmap.createScaledBitmap(taskDescription.getIcon(), size, size, true));
            }
        }
        try {
            ActivityTaskManager.getService().setTaskDescription(this.mToken, this.mTaskDescription);
        } catch (RemoteException e) {
        }
    }

    @Deprecated
    public final void setProgressBarVisibility(boolean visible) {
        int i;
        Window window = getWindow();
        if (visible) {
            i = -1;
        } else {
            i = -2;
        }
        window.setFeatureInt(2, i);
    }

    @Deprecated
    public final void setProgressBarIndeterminateVisibility(boolean visible) {
        getWindow().setFeatureInt(5, visible ? -1 : -2);
    }

    @Deprecated
    public final void setProgressBarIndeterminate(boolean indeterminate) {
        int i;
        Window window = getWindow();
        if (indeterminate) {
            i = -3;
        } else {
            i = -4;
        }
        window.setFeatureInt(2, i);
    }

    @Deprecated
    public final void setProgress(int progress) {
        getWindow().setFeatureInt(2, progress + 0);
    }

    @Deprecated
    public final void setSecondaryProgress(int secondaryProgress) {
        getWindow().setFeatureInt(2, secondaryProgress + 20000);
    }

    public final void setVolumeControlStream(int streamType) {
        getWindow().setVolumeControlStream(streamType);
    }

    public final int getVolumeControlStream() {
        return getWindow().getVolumeControlStream();
    }

    public final void setMediaController(MediaController controller) {
        getWindow().setMediaController(controller);
    }

    public final MediaController getMediaController() {
        return getWindow().getMediaController();
    }

    public final void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != this.mUiThread) {
            this.mHandler.post(action);
        } else {
            action.run();
        }
    }

    public final void autofillClientRunOnUiThread(Runnable action) {
        runOnUiThread(action);
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (!"fragment".equals(name)) {
            return onCreateView(name, context, attrs);
        }
        return this.mFragments.onCreateView(parent, name, context, attrs);
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        dumpInner(prefix, fd, writer, args);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001e, code lost:
        if (r1.equals("--autofill") == false) goto L_0x002b;
     */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0030  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0034  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dumpInner(java.lang.String r6, java.io.FileDescriptor r7, java.io.PrintWriter r8, java.lang.String[] r9) {
        /*
            r5 = this;
            if (r9 == 0) goto L_0x0038
            int r0 = r9.length
            if (r0 <= 0) goto L_0x0038
            r0 = 0
            r1 = r9[r0]
            r2 = -1
            int r3 = r1.hashCode()
            r4 = 1159329357(0x4519f64d, float:2463.3938)
            if (r3 == r4) goto L_0x0021
            r4 = 1455016274(0x56b9c952, float:1.02137158E14)
            if (r3 == r4) goto L_0x0018
            goto L_0x002b
        L_0x0018:
            java.lang.String r3 = "--autofill"
            boolean r1 = r1.equals(r3)
            if (r1 == 0) goto L_0x002b
            goto L_0x002c
        L_0x0021:
            java.lang.String r0 = "--contentcapture"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x002b
            r0 = 1
            goto L_0x002c
        L_0x002b:
            r0 = r2
        L_0x002c:
            switch(r0) {
                case 0: goto L_0x0034;
                case 1: goto L_0x0030;
                default: goto L_0x002f;
            }
        L_0x002f:
            goto L_0x0038
        L_0x0030:
            r5.dumpContentCaptureManager(r6, r8)
            return
        L_0x0034:
            r5.dumpAutofillManager(r6, r8)
            return
        L_0x0038:
            r8.print(r6)
            java.lang.String r0 = "Local Activity "
            r8.print(r0)
            int r0 = java.lang.System.identityHashCode(r5)
            java.lang.String r0 = java.lang.Integer.toHexString(r0)
            r8.print(r0)
            java.lang.String r0 = " State:"
            r8.println(r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r6)
            java.lang.String r1 = "  "
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r8.print(r0)
            java.lang.String r1 = "mResumed="
            r8.print(r1)
            boolean r1 = r5.mResumed
            r8.print(r1)
            java.lang.String r1 = " mStopped="
            r8.print(r1)
            boolean r1 = r5.mStopped
            r8.print(r1)
            java.lang.String r1 = " mFinished="
            r8.print(r1)
            boolean r1 = r5.mFinished
            r8.println(r1)
            r8.print(r0)
            java.lang.String r1 = "mChangingConfigurations="
            r8.print(r1)
            boolean r1 = r5.mChangingConfigurations
            r8.println(r1)
            r8.print(r0)
            java.lang.String r1 = "mCurrentConfig="
            r8.print(r1)
            android.content.res.Configuration r1 = r5.mCurrentConfig
            r8.println(r1)
            android.app.FragmentController r1 = r5.mFragments
            r1.dumpLoaders(r0, r7, r8, r9)
            android.app.FragmentController r1 = r5.mFragments
            android.app.FragmentManager r1 = r1.getFragmentManager()
            r1.dump(r0, r7, r8, r9)
            android.app.VoiceInteractor r1 = r5.mVoiceInteractor
            if (r1 == 0) goto L_0x00b3
            android.app.VoiceInteractor r1 = r5.mVoiceInteractor
            r1.dump(r0, r7, r8, r9)
        L_0x00b3:
            android.view.Window r1 = r5.getWindow()
            if (r1 == 0) goto L_0x00e0
            android.view.Window r1 = r5.getWindow()
            android.view.View r1 = r1.peekDecorView()
            if (r1 == 0) goto L_0x00e0
            android.view.Window r1 = r5.getWindow()
            android.view.View r1 = r1.peekDecorView()
            android.view.ViewRootImpl r1 = r1.getViewRootImpl()
            if (r1 == 0) goto L_0x00e0
            android.view.Window r1 = r5.getWindow()
            android.view.View r1 = r1.peekDecorView()
            android.view.ViewRootImpl r1 = r1.getViewRootImpl()
            r1.dump(r6, r7, r8, r9)
        L_0x00e0:
            android.os.Handler r1 = r5.mHandler
            android.os.Looper r1 = r1.getLooper()
            android.util.PrintWriterPrinter r2 = new android.util.PrintWriterPrinter
            r2.<init>(r8)
            r1.dump(r2, r6)
            r5.dumpAutofillManager(r6, r8)
            r5.dumpContentCaptureManager(r6, r8)
            android.app.ResourcesManager r1 = android.app.ResourcesManager.getInstance()
            r1.dump(r6, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Activity.dumpInner(java.lang.String, java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
    }

    /* access modifiers changed from: package-private */
    public void dumpAutofillManager(String prefix, PrintWriter writer) {
        AutofillManager afm = getAutofillManager();
        if (afm != null) {
            afm.dump(prefix, writer);
            writer.print(prefix);
            writer.print("Autofill Compat Mode: ");
            writer.println(isAutofillCompatibilityEnabled());
            return;
        }
        writer.print(prefix);
        writer.println("No AutofillManager");
    }

    /* access modifiers changed from: package-private */
    public void dumpContentCaptureManager(String prefix, PrintWriter writer) {
        ContentCaptureManager cm = getContentCaptureManager();
        if (cm != null) {
            cm.dump(prefix, writer);
            return;
        }
        writer.print(prefix);
        writer.println("No ContentCaptureManager");
    }

    public boolean isImmersive() {
        try {
            return ActivityTaskManager.getService().isImmersive(this.mToken);
        } catch (RemoteException e) {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean isTopOfTask() {
        if (this.mToken == null || this.mWindow == null) {
            return false;
        }
        try {
            return ActivityTaskManager.getService().isTopOfTask(getActivityToken());
        } catch (RemoteException e) {
            return false;
        }
    }

    @SystemApi
    public void convertFromTranslucent() {
        try {
            this.mTranslucentCallback = null;
            if (ActivityTaskManager.getService().convertFromTranslucent(this.mToken)) {
                WindowManagerGlobal.getInstance().changeCanvasOpacity(this.mToken, true);
            }
        } catch (RemoteException e) {
        }
    }

    @SystemApi
    public boolean convertToTranslucent(TranslucentConversionListener callback, ActivityOptions options) {
        boolean drawComplete = false;
        try {
            this.mTranslucentCallback = callback;
            this.mChangeCanvasToTranslucent = ActivityTaskManager.getService().convertToTranslucent(this.mToken, options == null ? null : options.toBundle());
            WindowManagerGlobal.getInstance().changeCanvasOpacity(this.mToken, false);
            drawComplete = true;
        } catch (RemoteException e) {
            this.mChangeCanvasToTranslucent = false;
        }
        if (!this.mChangeCanvasToTranslucent && this.mTranslucentCallback != null) {
            this.mTranslucentCallback.onTranslucentConversionComplete(drawComplete);
        }
        return this.mChangeCanvasToTranslucent;
    }

    /* access modifiers changed from: package-private */
    public void onTranslucentConversionComplete(boolean drawComplete) {
        if (this.mTranslucentCallback != null) {
            this.mTranslucentCallback.onTranslucentConversionComplete(drawComplete);
            this.mTranslucentCallback = null;
        }
        if (this.mChangeCanvasToTranslucent) {
            WindowManagerGlobal.getInstance().changeCanvasOpacity(this.mToken, false);
        }
    }

    public void onNewActivityOptions(ActivityOptions options) {
        this.mActivityTransitionState.setEnterActivityOptions(this, options);
        if (!this.mStopped) {
            this.mActivityTransitionState.enterReady(this);
        }
    }

    /* access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public ActivityOptions getActivityOptions() {
        try {
            return ActivityOptions.fromBundle(ActivityTaskManager.getService().getActivityOptions(this.mToken));
        } catch (RemoteException e) {
            return null;
        }
    }

    @Deprecated
    public boolean requestVisibleBehind(boolean visible) {
        return false;
    }

    @Deprecated
    public void onVisibleBehindCanceled() {
        this.mCalled = true;
    }

    @SystemApi
    @Deprecated
    public boolean isBackgroundVisibleBehind() {
        return false;
    }

    @SystemApi
    @Deprecated
    public void onBackgroundVisibleBehindChanged(boolean visible) {
    }

    public void onEnterAnimationComplete() {
    }

    public void dispatchEnterAnimationComplete() {
        this.mEnterAnimationComplete = true;
        this.mInstrumentation.onEnterAnimationComplete();
        onEnterAnimationComplete();
        if (getWindow() != null && getWindow().getDecorView() != null) {
            getWindow().getDecorView().getViewTreeObserver().dispatchOnEnterAnimationComplete();
        }
    }

    public void setImmersive(boolean i) {
        try {
            ActivityTaskManager.getService().setImmersive(this.mToken, i);
        } catch (RemoteException e) {
        }
    }

    public void setVrModeEnabled(boolean enabled, ComponentName requestedComponent) throws PackageManager.NameNotFoundException {
        try {
            if (ActivityTaskManager.getService().setVrMode(this.mToken, enabled, requestedComponent) != 0) {
                throw new PackageManager.NameNotFoundException(requestedComponent.flattenToString());
            }
        } catch (RemoteException e) {
        }
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        return this.mWindow.getDecorView().startActionMode(callback);
    }

    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        return this.mWindow.getDecorView().startActionMode(callback, type);
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        if (this.mActionModeTypeStarting != 0) {
            return null;
        }
        initWindowDecorActionBar();
        if (this.mActionBar != null) {
            return this.mActionBar.startActionMode(callback);
        }
        return null;
    }

    /* JADX INFO: finally extract failed */
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        try {
            this.mActionModeTypeStarting = type;
            ActionMode onWindowStartingActionMode = onWindowStartingActionMode(callback);
            this.mActionModeTypeStarting = 0;
            return onWindowStartingActionMode;
        } catch (Throwable th) {
            this.mActionModeTypeStarting = 0;
            throw th;
        }
    }

    public void onActionModeStarted(ActionMode mode) {
    }

    public void onActionModeFinished(ActionMode mode) {
    }

    public boolean shouldUpRecreateTask(Intent targetIntent) {
        try {
            PackageManager pm = getPackageManager();
            ComponentName cn = targetIntent.getComponent();
            if (cn == null) {
                cn = targetIntent.resolveActivity(pm);
            }
            ActivityInfo info = pm.getActivityInfo(cn, 0);
            if (info.taskAffinity == null) {
                return false;
            }
            return ActivityTaskManager.getService().shouldUpRecreateTask(this.mToken, info.taskAffinity);
        } catch (RemoteException e) {
            return false;
        } catch (PackageManager.NameNotFoundException e2) {
            return false;
        }
    }

    public boolean navigateUpTo(Intent upIntent) {
        int resultCode;
        Intent resultData;
        if (this.mParent != null) {
            return this.mParent.navigateUpToFromChild(this, upIntent);
        }
        ComponentName destInfo = upIntent.getComponent();
        if (destInfo == null) {
            destInfo = upIntent.resolveActivity(getPackageManager());
            if (destInfo == null) {
                return false;
            }
            upIntent = new Intent(upIntent);
            upIntent.setComponent(destInfo);
        }
        Intent upIntent2 = upIntent;
        synchronized (this) {
            resultCode = this.mResultCode;
            resultData = this.mResultData;
        }
        if (resultData != null) {
            resultData.prepareToLeaveProcess((Context) this);
        }
        try {
            upIntent2.prepareToLeaveProcess((Context) this);
            return ActivityTaskManager.getService().navigateUpTo(this.mToken, upIntent2, resultCode, resultData);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean navigateUpToFromChild(Activity child, Intent upIntent) {
        return navigateUpTo(upIntent);
    }

    public Intent getParentActivityIntent() {
        Intent parentIntent;
        String parentName = this.mActivityInfo.parentActivityName;
        if (TextUtils.isEmpty(parentName)) {
            return null;
        }
        ComponentName target = new ComponentName((Context) this, parentName);
        try {
            if (getPackageManager().getActivityInfo(target, 0).parentActivityName == null) {
                parentIntent = Intent.makeMainActivity(target);
            } else {
                parentIntent = new Intent().setComponent(target);
            }
            return parentIntent;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getParentActivityIntent: bad parentActivityName '" + parentName + "' in manifest");
            return null;
        }
    }

    public void setEnterSharedElementCallback(SharedElementCallback callback) {
        if (callback == null) {
            callback = SharedElementCallback.NULL_CALLBACK;
        }
        this.mEnterTransitionListener = callback;
    }

    public void setExitSharedElementCallback(SharedElementCallback callback) {
        if (callback == null) {
            callback = SharedElementCallback.NULL_CALLBACK;
        }
        this.mExitTransitionListener = callback;
    }

    public void postponeEnterTransition() {
        this.mActivityTransitionState.postponeEnterTransition();
    }

    public void startPostponedEnterTransition() {
        this.mActivityTransitionState.startPostponedEnterTransition();
    }

    public DragAndDropPermissions requestDragAndDropPermissions(DragEvent event) {
        DragAndDropPermissions dragAndDropPermissions = DragAndDropPermissions.obtain(event);
        if (dragAndDropPermissions == null || !dragAndDropPermissions.take(getActivityToken())) {
            return null;
        }
        return dragAndDropPermissions;
    }

    /* access modifiers changed from: package-private */
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final void setParent(Activity parent) {
        this.mParent = parent;
    }

    /* access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public final void attach(Context context, ActivityThread aThread, Instrumentation instr, IBinder token, int ident, Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id, NonConfigurationInstances lastNonConfigurationInstances, Configuration config, String referrer, IVoiceInteractor voiceInteractor, Window window, ViewRootImpl.ActivityConfigCallback activityConfigCallback, IBinder assistToken) {
        ActivityInfo activityInfo = info;
        NonConfigurationInstances nonConfigurationInstances = lastNonConfigurationInstances;
        IVoiceInteractor iVoiceInteractor = voiceInteractor;
        attachBaseContext(context);
        this.mFragments.attachHost((Fragment) null);
        this.mWindow = new PhoneWindow(this, window, activityConfigCallback);
        this.mWindow.setWindowControllerCallback(this);
        this.mWindow.setCallback(this);
        this.mWindow.setOnWindowDismissedCallback(this);
        this.mWindow.getLayoutInflater().setPrivateFactory(this);
        if (activityInfo.softInputMode != 0) {
            this.mWindow.setSoftInputMode(activityInfo.softInputMode);
        }
        if (activityInfo.uiOptions != 0) {
            this.mWindow.setUiOptions(activityInfo.uiOptions);
        }
        this.mUiThread = Thread.currentThread();
        this.mMainThread = aThread;
        this.mInstrumentation = instr;
        this.mToken = token;
        this.mAssistToken = assistToken;
        this.mIdent = ident;
        this.mApplication = application;
        this.mIntent = intent;
        this.mReferrer = referrer;
        this.mComponent = intent.getComponent();
        this.mActivityInfo = activityInfo;
        this.mTitle = title;
        this.mParent = parent;
        this.mEmbeddedID = id;
        this.mLastNonConfigurationInstances = nonConfigurationInstances;
        if (iVoiceInteractor != null) {
            if (nonConfigurationInstances != null) {
                this.mVoiceInteractor = nonConfigurationInstances.voiceInteractor;
            } else {
                this.mVoiceInteractor = new VoiceInteractor(iVoiceInteractor, this, this, Looper.myLooper());
            }
        }
        this.mWindow.setWindowManager((WindowManager) context.getSystemService(Context.WINDOW_SERVICE), this.mToken, this.mComponent.flattenToString(), (activityInfo.flags & 512) != 0);
        if (this.mParent != null) {
            this.mWindow.setContainer(this.mParent.getWindow());
        }
        this.mWindowManager = this.mWindow.getWindowManager();
        this.mCurrentConfig = config;
        this.mWindow.setColorMode(activityInfo.colorMode);
        setAutofillOptions(application.getAutofillOptions());
        setContentCaptureOptions(application.getContentCaptureOptions());
    }

    private void enableAutofillCompatibilityIfNeeded() {
        AutofillManager afm;
        if (isAutofillCompatibilityEnabled() && (afm = (AutofillManager) getSystemService(AutofillManager.class)) != null) {
            afm.enableCompatibilityMode();
        }
    }

    @UnsupportedAppUsage
    public final IBinder getActivityToken() {
        return this.mParent != null ? this.mParent.getActivityToken() : this.mToken;
    }

    public final IBinder getAssistToken() {
        return this.mParent != null ? this.mParent.getAssistToken() : this.mAssistToken;
    }

    @VisibleForTesting
    public final ActivityThread getActivityThread() {
        return this.mMainThread;
    }

    /* access modifiers changed from: package-private */
    public final void performCreate(Bundle icicle) {
        performCreate(icicle, (PersistableBundle) null);
    }

    /* access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public final void performCreate(Bundle icicle, PersistableBundle persistentState) {
        dispatchActivityPreCreated(icicle);
        this.mCanEnterPictureInPicture = true;
        restoreHasCurrentPermissionRequest(icicle);
        if (persistentState != null) {
            onCreate(icicle, persistentState);
        } else {
            onCreate(icicle);
        }
        writeEventLog(LOG_AM_ON_CREATE_CALLED, "performCreate");
        this.mActivityTransitionState.readState(icicle);
        this.mVisibleFromClient = true ^ this.mWindow.getWindowStyle().getBoolean(10, false);
        this.mFragments.dispatchActivityCreated();
        this.mActivityTransitionState.setEnterActivityOptions(this, getActivityOptions());
        dispatchActivityPostCreated(icicle);
    }

    /* access modifiers changed from: package-private */
    public final void performNewIntent(Intent intent) {
        this.mCanEnterPictureInPicture = true;
        onNewIntent(intent);
    }

    /* access modifiers changed from: package-private */
    public final void performStart(String reason) {
        String dlwarning;
        dispatchActivityPreStarted();
        this.mActivityTransitionState.setEnterActivityOptions(this, getActivityOptions());
        this.mFragments.noteStateNotSaved();
        this.mCalled = false;
        this.mFragments.execPendingActions();
        this.mInstrumentation.callActivityOnStart(this);
        writeEventLog(LOG_AM_ON_START_CALLED, reason);
        if (this.mCalled) {
            this.mFragments.dispatchStart();
            this.mFragments.reportLoaderStart();
            boolean isAppDebuggable = (this.mApplication.getApplicationInfo().flags & 2) != 0;
            boolean isDlwarningEnabled = SystemProperties.getInt("ro.bionic.ld.warning", 0) == 1;
            if ((isAppDebuggable || isDlwarningEnabled) && (dlwarning = getDlWarning()) != null) {
                String appName = getApplicationInfo().loadLabel(getPackageManager()).toString();
                String warning = "Detected problems with app native libraries\n(please consult log for detail):\n" + dlwarning;
                if (isAppDebuggable) {
                    new AlertDialog.Builder(this).setTitle((CharSequence) appName).setMessage((CharSequence) warning).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).setCancelable(false).show();
                } else {
                    Toast.makeText((Context) this, (CharSequence) appName + "\n" + warning, 1).show();
                }
            }
            GraphicsEnvironment.getInstance().showAngleInUseDialogBox(this);
            this.mActivityTransitionState.enterReady(this);
            dispatchActivityPostStarted();
            return;
        }
        throw new SuperNotCalledException("Activity " + this.mComponent.toShortString() + " did not call through to super.onStart()");
    }

    /* access modifiers changed from: package-private */
    public final void performRestart(boolean start, String reason) {
        this.mCanEnterPictureInPicture = true;
        this.mFragments.noteStateNotSaved();
        if (this.mToken != null && this.mParent == null) {
            WindowManagerGlobal.getInstance().setStoppedState(this.mToken, false);
        }
        if (this.mStopped) {
            this.mStopped = false;
            synchronized (this.mManagedCursors) {
                int N = this.mManagedCursors.size();
                for (int i = 0; i < N; i++) {
                    ManagedCursor mc = this.mManagedCursors.get(i);
                    if (mc.mReleased || mc.mUpdated) {
                        if (!mc.mCursor.requery()) {
                            if (getApplicationInfo().targetSdkVersion >= 14) {
                                throw new IllegalStateException("trying to requery an already closed cursor  " + mc.mCursor);
                            }
                        }
                        boolean unused = mc.mReleased = false;
                        boolean unused2 = mc.mUpdated = false;
                    }
                }
            }
            this.mCalled = false;
            this.mInstrumentation.callActivityOnRestart(this);
            writeEventLog(LOG_AM_ON_RESTART_CALLED, reason);
            if (!this.mCalled) {
                throw new SuperNotCalledException("Activity " + this.mComponent.toShortString() + " did not call through to super.onRestart()");
            } else if (start) {
                performStart(reason);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void performResume(boolean followedByPause, String reason) {
        dispatchActivityPreResumed();
        performRestart(true, reason);
        this.mFragments.execPendingActions();
        this.mLastNonConfigurationInstances = null;
        if (this.mAutoFillResetNeeded) {
            this.mAutoFillIgnoreFirstResumePause = followedByPause;
            boolean z = this.mAutoFillIgnoreFirstResumePause;
        }
        this.mCalled = false;
        this.mInstrumentation.callActivityOnResume(this);
        writeEventLog(LOG_AM_ON_RESUME_CALLED, reason);
        if (this.mCalled) {
            if (!this.mVisibleFromClient && !this.mFinished) {
                Log.w(TAG, "An activity without a UI must call finish() before onResume() completes");
                if (getApplicationInfo().targetSdkVersion > 22) {
                    throw new IllegalStateException("Activity " + this.mComponent.toShortString() + " did not call finish() prior to onResume() completing");
                }
            }
            this.mCalled = false;
            this.mFragments.dispatchResume();
            this.mFragments.execPendingActions();
            onPostResume();
            if (this.mCalled) {
                dispatchActivityPostResumed();
                return;
            }
            throw new SuperNotCalledException("Activity " + this.mComponent.toShortString() + " did not call through to super.onPostResume()");
        }
        throw new SuperNotCalledException("Activity " + this.mComponent.toShortString() + " did not call through to super.onResume()");
    }

    /* access modifiers changed from: package-private */
    public final void performPause() {
        dispatchActivityPrePaused();
        this.mDoReportFullyDrawn = false;
        this.mFragments.dispatchPause();
        this.mCalled = false;
        onPause();
        writeEventLog(LOG_AM_ON_PAUSE_CALLED, "performPause");
        this.mResumed = false;
        if (this.mCalled || getApplicationInfo().targetSdkVersion < 9) {
            dispatchActivityPostPaused();
            return;
        }
        throw new SuperNotCalledException("Activity " + this.mComponent.toShortString() + " did not call through to super.onPause()");
    }

    /* access modifiers changed from: package-private */
    public final void performUserLeaving() {
        onUserInteraction();
        onUserLeaveHint();
    }

    /* access modifiers changed from: package-private */
    public final void performStop(boolean preserveWindow, String reason) {
        this.mDoReportFullyDrawn = false;
        this.mFragments.doLoaderStop(this.mChangingConfigurations);
        this.mCanEnterPictureInPicture = false;
        if (!this.mStopped) {
            dispatchActivityPreStopped();
            if (this.mWindow != null) {
                this.mWindow.closeAllPanels();
            }
            if (!preserveWindow && this.mToken != null && this.mParent == null) {
                WindowManagerGlobal.getInstance().setStoppedState(this.mToken, true);
            }
            this.mFragments.dispatchStop();
            this.mCalled = false;
            this.mInstrumentation.callActivityOnStop(this);
            writeEventLog(LOG_AM_ON_STOP_CALLED, reason);
            if (this.mCalled) {
                synchronized (this.mManagedCursors) {
                    int N = this.mManagedCursors.size();
                    for (int i = 0; i < N; i++) {
                        ManagedCursor mc = this.mManagedCursors.get(i);
                        if (!mc.mReleased) {
                            mc.mCursor.deactivate();
                            boolean unused = mc.mReleased = true;
                        }
                    }
                }
                this.mStopped = true;
                dispatchActivityPostStopped();
            } else {
                throw new SuperNotCalledException("Activity " + this.mComponent.toShortString() + " did not call through to super.onStop()");
            }
        }
        this.mResumed = false;
    }

    /* access modifiers changed from: package-private */
    public final void performDestroy() {
        dispatchActivityPreDestroyed();
        this.mDestroyed = true;
        this.mWindow.destroy();
        this.mFragments.dispatchDestroy();
        onDestroy();
        writeEventLog(LOG_AM_ON_DESTROY_CALLED, "performDestroy");
        this.mFragments.doLoaderDestroy();
        if (this.mVoiceInteractor != null) {
            this.mVoiceInteractor.detachActivity();
        }
        dispatchActivityPostDestroyed();
    }

    /* access modifiers changed from: package-private */
    public final void dispatchMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        this.mFragments.dispatchMultiWindowModeChanged(isInMultiWindowMode, newConfig);
        if (this.mWindow != null) {
            this.mWindow.onMultiWindowModeChanged();
        }
        onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
    }

    /* access modifiers changed from: package-private */
    public final void dispatchPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        this.mFragments.dispatchPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (this.mWindow != null) {
            this.mWindow.onPictureInPictureModeChanged(isInPictureInPictureMode);
        }
        onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
    }

    @UnsupportedAppUsage
    public final boolean isResumed() {
        return this.mResumed;
    }

    private void storeHasCurrentPermissionRequest(Bundle bundle) {
        if (bundle != null && this.mHasCurrentPermissionsRequest) {
            bundle.putBoolean(HAS_CURENT_PERMISSIONS_REQUEST_KEY, true);
        }
    }

    private void restoreHasCurrentPermissionRequest(Bundle bundle) {
        if (bundle != null) {
            this.mHasCurrentPermissionsRequest = bundle.getBoolean(HAS_CURENT_PERMISSIONS_REQUEST_KEY, false);
        }
    }

    /* access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public void dispatchActivityResult(String who, int requestCode, int resultCode, Intent data, String reason) {
        this.mFragments.noteStateNotSaved();
        if (who == null) {
            onActivityResult(requestCode, resultCode, data);
        } else if (who.startsWith(REQUEST_PERMISSIONS_WHO_PREFIX)) {
            String who2 = who.substring(REQUEST_PERMISSIONS_WHO_PREFIX.length());
            if (TextUtils.isEmpty(who2)) {
                dispatchRequestPermissionsResult(requestCode, data);
            } else {
                Fragment frag = this.mFragments.findFragmentByWho(who2);
                if (frag != null) {
                    dispatchRequestPermissionsResultToFragment(requestCode, data, frag);
                }
            }
        } else if (who.startsWith("@android:view:")) {
            Iterator<ViewRootImpl> it = WindowManagerGlobal.getInstance().getRootViews(getActivityToken()).iterator();
            while (it.hasNext()) {
                ViewRootImpl viewRoot = it.next();
                if (viewRoot.getView() != null && viewRoot.getView().dispatchActivityResult(who, requestCode, resultCode, data)) {
                    return;
                }
            }
        } else if (who.startsWith(AUTO_FILL_AUTH_WHO_PREFIX)) {
            getAutofillManager().onAuthenticationResult(requestCode, resultCode == -1 ? data : null, getCurrentFocus());
        } else {
            Fragment frag2 = this.mFragments.findFragmentByWho(who);
            if (frag2 != null) {
                frag2.onActivityResult(requestCode, resultCode, data);
            }
        }
        writeEventLog(LOG_AM_ON_ACTIVITY_RESULT_CALLED, reason);
    }

    public void startLockTask() {
        try {
            ActivityTaskManager.getService().startLockTaskModeByToken(this.mToken);
        } catch (RemoteException e) {
        }
    }

    public void stopLockTask() {
        try {
            ActivityTaskManager.getService().stopLockTaskModeByToken(this.mToken);
        } catch (RemoteException e) {
        }
    }

    public void showLockTaskEscapeMessage() {
        try {
            ActivityTaskManager.getService().showLockTaskEscapeMessage(this.mToken);
        } catch (RemoteException e) {
        }
    }

    public boolean isOverlayWithDecorCaptionEnabled() {
        return this.mWindow.isOverlayWithDecorCaptionEnabled();
    }

    public void setOverlayWithDecorCaptionEnabled(boolean enabled) {
        this.mWindow.setOverlayWithDecorCaptionEnabled(enabled);
    }

    private void dispatchRequestPermissionsResult(int requestCode, Intent data) {
        String[] permissions;
        int[] grantResults;
        this.mHasCurrentPermissionsRequest = false;
        if (data != null) {
            permissions = data.getStringArrayExtra(PackageManager.EXTRA_REQUEST_PERMISSIONS_NAMES);
        } else {
            permissions = new String[0];
        }
        if (data != null) {
            grantResults = data.getIntArrayExtra(PackageManager.EXTRA_REQUEST_PERMISSIONS_RESULTS);
        } else {
            grantResults = new int[0];
        }
        onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void dispatchRequestPermissionsResultToFragment(int requestCode, Intent data, Fragment fragment) {
        String[] permissions;
        int[] grantResults;
        if (data != null) {
            permissions = data.getStringArrayExtra(PackageManager.EXTRA_REQUEST_PERMISSIONS_NAMES);
        } else {
            permissions = new String[0];
        }
        if (data != null) {
            grantResults = data.getIntArrayExtra(PackageManager.EXTRA_REQUEST_PERMISSIONS_RESULTS);
        } else {
            grantResults = new int[0];
        }
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public final void autofillClientAuthenticate(int authenticationId, IntentSender intent, Intent fillInIntent) {
        try {
            startIntentSenderForResultInner(intent, AUTO_FILL_AUTH_WHO_PREFIX, authenticationId, fillInIntent, 0, 0, (Bundle) null);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "authenticate() failed for intent:" + intent, e);
        }
    }

    public final void autofillClientResetableStateAvailable() {
        this.mAutoFillResetNeeded = true;
    }

    public final boolean autofillClientRequestShowFillUi(View anchor, int width, int height, Rect anchorBounds, IAutofillWindowPresenter presenter) {
        boolean wasShowing;
        if (this.mAutofillPopupWindow == null) {
            wasShowing = false;
            this.mAutofillPopupWindow = new AutofillPopupWindow(presenter);
        } else {
            wasShowing = this.mAutofillPopupWindow.isShowing();
        }
        this.mAutofillPopupWindow.update(anchor, 0, 0, width, height, anchorBounds);
        return !wasShowing && this.mAutofillPopupWindow.isShowing();
    }

    public final void autofillClientDispatchUnhandledKey(View anchor, KeyEvent keyEvent) {
        ViewRootImpl rootImpl = anchor.getViewRootImpl();
        if (rootImpl != null) {
            rootImpl.dispatchKeyFromAutofill(keyEvent);
        }
    }

    public final boolean autofillClientRequestHideFillUi() {
        if (this.mAutofillPopupWindow == null) {
            return false;
        }
        this.mAutofillPopupWindow.dismiss();
        this.mAutofillPopupWindow = null;
        return true;
    }

    public final boolean autofillClientIsFillUiShowing() {
        return this.mAutofillPopupWindow != null && this.mAutofillPopupWindow.isShowing();
    }

    public final View[] autofillClientFindViewsByAutofillIdTraversal(AutofillId[] autofillId) {
        View[] views = new View[autofillId.length];
        ArrayList<ViewRootImpl> roots = WindowManagerGlobal.getInstance().getRootViews(getActivityToken());
        for (int rootNum = 0; rootNum < roots.size(); rootNum++) {
            View rootView = roots.get(rootNum).getView();
            if (rootView != null) {
                int viewCount = autofillId.length;
                for (int viewNum = 0; viewNum < viewCount; viewNum++) {
                    if (views[viewNum] == null) {
                        views[viewNum] = rootView.findViewByAutofillIdTraversal(autofillId[viewNum].getViewId());
                    }
                }
            }
        }
        return views;
    }

    public final View autofillClientFindViewByAutofillIdTraversal(AutofillId autofillId) {
        View view;
        ArrayList<ViewRootImpl> roots = WindowManagerGlobal.getInstance().getRootViews(getActivityToken());
        for (int rootNum = 0; rootNum < roots.size(); rootNum++) {
            View rootView = roots.get(rootNum).getView();
            if (rootView != null && (view = rootView.findViewByAutofillIdTraversal(autofillId.getViewId())) != null) {
                return view;
            }
        }
        return null;
    }

    public final boolean[] autofillClientGetViewVisibility(AutofillId[] autofillIds) {
        int autofillIdCount = autofillIds.length;
        boolean[] visible = new boolean[autofillIdCount];
        for (int i = 0; i < autofillIdCount; i++) {
            AutofillId autofillId = autofillIds[i];
            View view = autofillClientFindViewByAutofillIdTraversal(autofillId);
            if (view != null) {
                if (!autofillId.isVirtualInt()) {
                    visible[i] = view.isVisibleToUser();
                } else {
                    visible[i] = view.isVisibleToUserForAutofill(autofillId.getVirtualChildIntId());
                }
            }
        }
        if (Helper.sVerbose != 0) {
            Log.v(TAG, "autofillClientGetViewVisibility(): " + Arrays.toString(visible));
        }
        return visible;
    }

    public final View autofillClientFindViewByAccessibilityIdTraversal(int viewId, int windowId) {
        View view;
        ArrayList<ViewRootImpl> roots = WindowManagerGlobal.getInstance().getRootViews(getActivityToken());
        for (int rootNum = 0; rootNum < roots.size(); rootNum++) {
            View rootView = roots.get(rootNum).getView();
            if (rootView != null && rootView.getAccessibilityWindowId() == windowId && (view = rootView.findViewByAccessibilityIdTraversal(viewId)) != null) {
                return view;
            }
        }
        return null;
    }

    public final IBinder autofillClientGetActivityToken() {
        return getActivityToken();
    }

    public final boolean autofillClientIsVisibleForAutofill() {
        return !this.mStopped;
    }

    public final boolean autofillClientIsCompatibilityModeEnabled() {
        return isAutofillCompatibilityEnabled();
    }

    public final boolean isDisablingEnterExitEventForAutofill() {
        return this.mAutoFillIgnoreFirstResumePause || !this.mResumed;
    }

    @UnsupportedAppUsage
    public void setDisablePreviewScreenshots(boolean disable) {
        try {
            ActivityTaskManager.getService().setDisablePreviewScreenshots(this.mToken, disable);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setShowWhenLocked(boolean showWhenLocked) {
        try {
            ActivityTaskManager.getService().setShowWhenLocked(this.mToken, showWhenLocked);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setInheritShowWhenLocked(boolean inheritShowWhenLocked) {
        try {
            ActivityTaskManager.getService().setInheritShowWhenLocked(this.mToken, inheritShowWhenLocked);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setTurnScreenOn(boolean turnScreenOn) {
        try {
            ActivityTaskManager.getService().setTurnScreenOn(this.mToken, turnScreenOn);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public void registerRemoteAnimations(RemoteAnimationDefinition definition) {
        try {
            ActivityTaskManager.getService().registerRemoteAnimations(this.mToken, definition);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void writeEventLog(int event, String reason) {
        EventLog.writeEvent(event, Integer.valueOf(UserHandle.myUserId()), getComponentName().getClassName(), reason);
    }

    class HostCallbacks extends FragmentHostCallback<Activity> {
        public HostCallbacks() {
            super(Activity.this);
        }

        public void onDump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            Activity.this.dump(prefix, fd, writer, args);
        }

        public boolean onShouldSaveFragmentState(Fragment fragment) {
            return !Activity.this.isFinishing();
        }

        public LayoutInflater onGetLayoutInflater() {
            LayoutInflater result = Activity.this.getLayoutInflater();
            if (onUseFragmentManagerInflaterFactory()) {
                return result.cloneInContext(Activity.this);
            }
            return result;
        }

        public boolean onUseFragmentManagerInflaterFactory() {
            return Activity.this.getApplicationInfo().targetSdkVersion >= 21;
        }

        public Activity onGetHost() {
            return Activity.this;
        }

        public void onInvalidateOptionsMenu() {
            Activity.this.invalidateOptionsMenu();
        }

        public void onStartActivityFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options) {
            Activity.this.startActivityFromFragment(fragment, intent, requestCode, options);
        }

        public void onStartActivityAsUserFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options, UserHandle user) {
            Activity.this.startActivityAsUserFromFragment(fragment, intent, requestCode, options, user);
        }

        public void onStartIntentSenderFromFragment(Fragment fragment, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
            if (Activity.this.mParent == null) {
                Activity.this.startIntentSenderForResultInner(intent, fragment.mWho, requestCode, fillInIntent, flagsMask, flagsValues, options);
                return;
            }
            Fragment fragment2 = fragment;
            if (options != null) {
                Activity.this.mParent.startIntentSenderFromChildFragment(fragment, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
            }
        }

        public void onRequestPermissionsFromFragment(Fragment fragment, String[] permissions, int requestCode) {
            Activity.this.startActivityForResult(Activity.REQUEST_PERMISSIONS_WHO_PREFIX + fragment.mWho, Activity.this.getPackageManager().buildRequestPermissionsIntent(permissions), requestCode, (Bundle) null);
        }

        public boolean onHasWindowAnimations() {
            return Activity.this.getWindow() != null;
        }

        public int onGetWindowAnimations() {
            Window w = Activity.this.getWindow();
            if (w == null) {
                return 0;
            }
            return w.getAttributes().windowAnimations;
        }

        public void onAttachFragment(Fragment fragment) {
            Activity.this.onAttachFragment(fragment);
        }

        public <T extends View> T onFindViewById(int id) {
            return Activity.this.findViewById(id);
        }

        public boolean onHasView() {
            Window w = Activity.this.getWindow();
            return (w == null || w.peekDecorView() == null) ? false : true;
        }
    }
}
