package android.accessibilityservice;

import android.accessibilityservice.GestureDescription;
import android.accessibilityservice.IAccessibilityServiceClient;
import android.annotation.UnsupportedAppUsage;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.graphics.Region;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityInteractionClient;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import com.android.internal.os.HandlerCaller;
import com.android.internal.os.SomeArgs;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public abstract class AccessibilityService extends Service {
    public static final int GESTURE_SWIPE_DOWN = 2;
    public static final int GESTURE_SWIPE_DOWN_AND_LEFT = 15;
    public static final int GESTURE_SWIPE_DOWN_AND_RIGHT = 16;
    public static final int GESTURE_SWIPE_DOWN_AND_UP = 8;
    public static final int GESTURE_SWIPE_LEFT = 3;
    public static final int GESTURE_SWIPE_LEFT_AND_DOWN = 10;
    public static final int GESTURE_SWIPE_LEFT_AND_RIGHT = 5;
    public static final int GESTURE_SWIPE_LEFT_AND_UP = 9;
    public static final int GESTURE_SWIPE_RIGHT = 4;
    public static final int GESTURE_SWIPE_RIGHT_AND_DOWN = 12;
    public static final int GESTURE_SWIPE_RIGHT_AND_LEFT = 6;
    public static final int GESTURE_SWIPE_RIGHT_AND_UP = 11;
    public static final int GESTURE_SWIPE_UP = 1;
    public static final int GESTURE_SWIPE_UP_AND_DOWN = 7;
    public static final int GESTURE_SWIPE_UP_AND_LEFT = 13;
    public static final int GESTURE_SWIPE_UP_AND_RIGHT = 14;
    public static final int GLOBAL_ACTION_BACK = 1;
    public static final int GLOBAL_ACTION_HOME = 2;
    public static final int GLOBAL_ACTION_LOCK_SCREEN = 8;
    public static final int GLOBAL_ACTION_NOTIFICATIONS = 4;
    public static final int GLOBAL_ACTION_POWER_DIALOG = 6;
    public static final int GLOBAL_ACTION_QUICK_SETTINGS = 5;
    public static final int GLOBAL_ACTION_RECENTS = 3;
    public static final int GLOBAL_ACTION_TAKE_SCREENSHOT = 9;
    public static final int GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN = 7;
    private static final String LOG_TAG = "AccessibilityService";
    public static final String SERVICE_INTERFACE = "android.accessibilityservice.AccessibilityService";
    public static final String SERVICE_META_DATA = "android.accessibilityservice";
    public static final int SHOW_MODE_AUTO = 0;
    public static final int SHOW_MODE_HARD_KEYBOARD_ORIGINAL_VALUE = 536870912;
    public static final int SHOW_MODE_HARD_KEYBOARD_OVERRIDDEN = 1073741824;
    public static final int SHOW_MODE_HIDDEN = 1;
    public static final int SHOW_MODE_IGNORE_HARD_KEYBOARD = 2;
    public static final int SHOW_MODE_MASK = 3;
    private AccessibilityButtonController mAccessibilityButtonController;
    /* access modifiers changed from: private */
    public int mConnectionId = -1;
    private FingerprintGestureController mFingerprintGestureController;
    private SparseArray<GestureResultCallbackInfo> mGestureStatusCallbackInfos;
    private int mGestureStatusCallbackSequence;
    @UnsupportedAppUsage
    private AccessibilityServiceInfo mInfo;
    private final Object mLock = new Object();
    private final SparseArray<MagnificationController> mMagnificationControllers = new SparseArray<>(0);
    private SoftKeyboardController mSoftKeyboardController;
    private WindowManager mWindowManager;
    /* access modifiers changed from: private */
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public IBinder mWindowToken;

    public interface Callbacks {
        void init(int i, IBinder iBinder);

        void onAccessibilityButtonAvailabilityChanged(boolean z);

        void onAccessibilityButtonClicked();

        void onAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        void onFingerprintCapturingGesturesChanged(boolean z);

        void onFingerprintGesture(int i);

        boolean onGesture(int i);

        void onInterrupt();

        boolean onKeyEvent(KeyEvent keyEvent);

        void onMagnificationChanged(int i, Region region, float f, float f2, float f3);

        void onPerformGestureResult(int i, boolean z);

        void onServiceConnected();

        void onSoftKeyboardShowModeChanged(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SoftKeyboardShowMode {
    }

    public abstract void onAccessibilityEvent(AccessibilityEvent accessibilityEvent);

    public abstract void onInterrupt();

    /* access modifiers changed from: private */
    public void dispatchServiceConnected() {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mMagnificationControllers.size(); i++) {
                this.mMagnificationControllers.valueAt(i).onServiceConnectedLocked();
            }
        }
        if (this.mSoftKeyboardController != null) {
            this.mSoftKeyboardController.onServiceConnected();
        }
        onServiceConnected();
    }

    /* access modifiers changed from: protected */
    public void onServiceConnected() {
    }

    /* access modifiers changed from: protected */
    public boolean onGesture(int gestureId) {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean onKeyEvent(KeyEvent event) {
        return false;
    }

    public List<AccessibilityWindowInfo> getWindows() {
        return AccessibilityInteractionClient.getInstance().getWindows(this.mConnectionId);
    }

    public AccessibilityNodeInfo getRootInActiveWindow() {
        return AccessibilityInteractionClient.getInstance().getRootInActiveWindow(this.mConnectionId);
    }

    public final void disableSelf() {
        AccessibilityInteractionClient.getInstance();
        IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mConnectionId);
        if (connection != null) {
            try {
                connection.disableSelf();
            } catch (RemoteException re) {
                throw new RuntimeException(re);
            }
        }
    }

    public final MagnificationController getMagnificationController() {
        return getMagnificationController(0);
    }

    public final MagnificationController getMagnificationController(int displayId) {
        MagnificationController controller;
        synchronized (this.mLock) {
            controller = this.mMagnificationControllers.get(displayId);
            if (controller == null) {
                controller = new MagnificationController(this, this.mLock, displayId);
                this.mMagnificationControllers.put(displayId, controller);
            }
        }
        return controller;
    }

    public final FingerprintGestureController getFingerprintGestureController() {
        if (this.mFingerprintGestureController == null) {
            AccessibilityInteractionClient.getInstance();
            this.mFingerprintGestureController = new FingerprintGestureController(AccessibilityInteractionClient.getConnection(this.mConnectionId));
        }
        return this.mFingerprintGestureController;
    }

    public final boolean dispatchGesture(GestureDescription gesture, GestureResultCallback callback, Handler handler) {
        AccessibilityInteractionClient.getInstance();
        IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mConnectionId);
        if (connection == null) {
            return false;
        }
        List<GestureDescription.GestureStep> steps = GestureDescription.MotionEventGenerator.getGestureStepsFromGestureDescription(gesture, 100);
        try {
            synchronized (this.mLock) {
                this.mGestureStatusCallbackSequence++;
                if (callback != null) {
                    if (this.mGestureStatusCallbackInfos == null) {
                        this.mGestureStatusCallbackInfos = new SparseArray<>();
                    }
                    this.mGestureStatusCallbackInfos.put(this.mGestureStatusCallbackSequence, new GestureResultCallbackInfo(gesture, callback, handler));
                }
                connection.sendGesture(this.mGestureStatusCallbackSequence, new ParceledListSlice(steps));
            }
            return true;
        } catch (RemoteException re) {
            throw new RuntimeException(re);
        }
    }

    /* access modifiers changed from: package-private */
    public void onPerformGestureResult(int sequence, final boolean completedSuccessfully) {
        GestureResultCallbackInfo callbackInfo;
        if (this.mGestureStatusCallbackInfos != null) {
            synchronized (this.mLock) {
                callbackInfo = this.mGestureStatusCallbackInfos.get(sequence);
            }
            final GestureResultCallbackInfo finalCallbackInfo = callbackInfo;
            if (callbackInfo != null && callbackInfo.gestureDescription != null && callbackInfo.callback != null) {
                if (callbackInfo.handler != null) {
                    callbackInfo.handler.post(new Runnable() {
                        public void run() {
                            if (completedSuccessfully) {
                                finalCallbackInfo.callback.onCompleted(finalCallbackInfo.gestureDescription);
                            } else {
                                finalCallbackInfo.callback.onCancelled(finalCallbackInfo.gestureDescription);
                            }
                        }
                    });
                } else if (completedSuccessfully) {
                    callbackInfo.callback.onCompleted(callbackInfo.gestureDescription);
                } else {
                    callbackInfo.callback.onCancelled(callbackInfo.gestureDescription);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onMagnificationChanged(int displayId, Region region, float scale, float centerX, float centerY) {
        MagnificationController controller;
        synchronized (this.mLock) {
            controller = this.mMagnificationControllers.get(displayId);
        }
        if (controller != null) {
            controller.dispatchMagnificationChanged(region, scale, centerX, centerY);
        }
    }

    /* access modifiers changed from: private */
    public void onFingerprintCapturingGesturesChanged(boolean active) {
        getFingerprintGestureController().onGestureDetectionActiveChanged(active);
    }

    /* access modifiers changed from: private */
    public void onFingerprintGesture(int gesture) {
        getFingerprintGestureController().onGesture(gesture);
    }

    public static final class MagnificationController {
        private final int mDisplayId;
        private ArrayMap<OnMagnificationChangedListener, Handler> mListeners;
        private final Object mLock;
        private final AccessibilityService mService;

        public interface OnMagnificationChangedListener {
            void onMagnificationChanged(MagnificationController magnificationController, Region region, float f, float f2, float f3);
        }

        MagnificationController(AccessibilityService service, Object lock, int displayId) {
            this.mService = service;
            this.mLock = lock;
            this.mDisplayId = displayId;
        }

        /* access modifiers changed from: package-private */
        public void onServiceConnectedLocked() {
            if (this.mListeners != null && !this.mListeners.isEmpty()) {
                setMagnificationCallbackEnabled(true);
            }
        }

        public void addListener(OnMagnificationChangedListener listener) {
            addListener(listener, (Handler) null);
        }

        public void addListener(OnMagnificationChangedListener listener, Handler handler) {
            synchronized (this.mLock) {
                if (this.mListeners == null) {
                    this.mListeners = new ArrayMap<>();
                }
                boolean shouldEnableCallback = this.mListeners.isEmpty();
                this.mListeners.put(listener, handler);
                if (shouldEnableCallback) {
                    setMagnificationCallbackEnabled(true);
                }
            }
        }

        public boolean removeListener(OnMagnificationChangedListener listener) {
            boolean hasKey;
            if (this.mListeners == null) {
                return false;
            }
            synchronized (this.mLock) {
                int keyIndex = this.mListeners.indexOfKey(listener);
                hasKey = keyIndex >= 0;
                if (hasKey) {
                    this.mListeners.removeAt(keyIndex);
                }
                if (hasKey && this.mListeners.isEmpty()) {
                    setMagnificationCallbackEnabled(false);
                }
            }
            return hasKey;
        }

        private void setMagnificationCallbackEnabled(boolean enabled) {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection != null) {
                try {
                    connection.setMagnificationCallbackEnabled(this.mDisplayId, enabled);
                } catch (RemoteException re) {
                    throw new RuntimeException(re);
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0019, code lost:
            r2 = r0.size();
            r9 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001f, code lost:
            r10 = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0020, code lost:
            if (r9 >= r10) goto L_0x0055;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0022, code lost:
            r11 = r0.keyAt(r9);
            r12 = r0.valueAt(r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0030, code lost:
            if (r12 == null) goto L_0x0045;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0032, code lost:
            r3 = r11;
            r4 = r15;
            r5 = r16;
            r6 = r17;
            r7 = r18;
            r12.post(new android.accessibilityservice.AccessibilityService.MagnificationController.AnonymousClass1(r14));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0045, code lost:
            r11.onMagnificationChanged(r14, r15, r16, r17, r18);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0051, code lost:
            r9 = r9 + 1;
            r2 = r10;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0055, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void dispatchMagnificationChanged(android.graphics.Region r15, float r16, float r17, float r18) {
            /*
                r14 = this;
                r8 = r14
                java.lang.Object r1 = r8.mLock
                monitor-enter(r1)
                android.util.ArrayMap<android.accessibilityservice.AccessibilityService$MagnificationController$OnMagnificationChangedListener, android.os.Handler> r0 = r8.mListeners     // Catch:{ all -> 0x0063 }
                if (r0 == 0) goto L_0x0056
                android.util.ArrayMap<android.accessibilityservice.AccessibilityService$MagnificationController$OnMagnificationChangedListener, android.os.Handler> r0 = r8.mListeners     // Catch:{ all -> 0x0063 }
                boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x0063 }
                if (r0 == 0) goto L_0x0011
                goto L_0x0056
            L_0x0011:
                android.util.ArrayMap r0 = new android.util.ArrayMap     // Catch:{ all -> 0x0063 }
                android.util.ArrayMap<android.accessibilityservice.AccessibilityService$MagnificationController$OnMagnificationChangedListener, android.os.Handler> r2 = r8.mListeners     // Catch:{ all -> 0x0063 }
                r0.<init>(r2)     // Catch:{ all -> 0x0063 }
                monitor-exit(r1)     // Catch:{ all -> 0x0063 }
                r1 = 0
                int r2 = r0.size()
                r9 = r1
            L_0x001f:
                r10 = r2
                if (r9 >= r10) goto L_0x0055
                java.lang.Object r1 = r0.keyAt(r9)
                r11 = r1
                android.accessibilityservice.AccessibilityService$MagnificationController$OnMagnificationChangedListener r11 = (android.accessibilityservice.AccessibilityService.MagnificationController.OnMagnificationChangedListener) r11
                java.lang.Object r1 = r0.valueAt(r9)
                r12 = r1
                android.os.Handler r12 = (android.os.Handler) r12
                if (r12 == 0) goto L_0x0045
                android.accessibilityservice.AccessibilityService$MagnificationController$1 r13 = new android.accessibilityservice.AccessibilityService$MagnificationController$1
                r1 = r13
                r2 = r14
                r3 = r11
                r4 = r15
                r5 = r16
                r6 = r17
                r7 = r18
                r1.<init>(r3, r4, r5, r6, r7)
                r12.post(r13)
                goto L_0x0051
            L_0x0045:
                r1 = r11
                r2 = r14
                r3 = r15
                r4 = r16
                r5 = r17
                r6 = r18
                r1.onMagnificationChanged(r2, r3, r4, r5, r6)
            L_0x0051:
                int r9 = r9 + 1
                r2 = r10
                goto L_0x001f
            L_0x0055:
                return
            L_0x0056:
                java.lang.String r0 = "AccessibilityService"
                java.lang.String r2 = "Received magnification changed callback with no listeners registered!"
                android.util.Slog.d(r0, r2)     // Catch:{ all -> 0x0063 }
                r0 = 0
                r14.setMagnificationCallbackEnabled(r0)     // Catch:{ all -> 0x0063 }
                monitor-exit(r1)     // Catch:{ all -> 0x0063 }
                return
            L_0x0063:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0063 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: android.accessibilityservice.AccessibilityService.MagnificationController.dispatchMagnificationChanged(android.graphics.Region, float, float, float):void");
        }

        public float getScale() {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection == null) {
                return 1.0f;
            }
            try {
                return connection.getMagnificationScale(this.mDisplayId);
            } catch (RemoteException re) {
                Log.w(AccessibilityService.LOG_TAG, "Failed to obtain scale", re);
                re.rethrowFromSystemServer();
                return 1.0f;
            }
        }

        public float getCenterX() {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection == null) {
                return 0.0f;
            }
            try {
                return connection.getMagnificationCenterX(this.mDisplayId);
            } catch (RemoteException re) {
                Log.w(AccessibilityService.LOG_TAG, "Failed to obtain center X", re);
                re.rethrowFromSystemServer();
                return 0.0f;
            }
        }

        public float getCenterY() {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection == null) {
                return 0.0f;
            }
            try {
                return connection.getMagnificationCenterY(this.mDisplayId);
            } catch (RemoteException re) {
                Log.w(AccessibilityService.LOG_TAG, "Failed to obtain center Y", re);
                re.rethrowFromSystemServer();
                return 0.0f;
            }
        }

        public Region getMagnificationRegion() {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection != null) {
                try {
                    return connection.getMagnificationRegion(this.mDisplayId);
                } catch (RemoteException re) {
                    Log.w(AccessibilityService.LOG_TAG, "Failed to obtain magnified region", re);
                    re.rethrowFromSystemServer();
                }
            }
            return Region.obtain();
        }

        public boolean reset(boolean animate) {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection == null) {
                return false;
            }
            try {
                return connection.resetMagnification(this.mDisplayId, animate);
            } catch (RemoteException re) {
                Log.w(AccessibilityService.LOG_TAG, "Failed to reset", re);
                re.rethrowFromSystemServer();
                return false;
            }
        }

        public boolean setScale(float scale, boolean animate) {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection == null) {
                return false;
            }
            try {
                return connection.setMagnificationScaleAndCenter(this.mDisplayId, scale, Float.NaN, Float.NaN, animate);
            } catch (RemoteException re) {
                Log.w(AccessibilityService.LOG_TAG, "Failed to set scale", re);
                re.rethrowFromSystemServer();
                return false;
            }
        }

        public boolean setCenter(float centerX, float centerY, boolean animate) {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection == null) {
                return false;
            }
            try {
                return connection.setMagnificationScaleAndCenter(this.mDisplayId, Float.NaN, centerX, centerY, animate);
            } catch (RemoteException re) {
                Log.w(AccessibilityService.LOG_TAG, "Failed to set center", re);
                re.rethrowFromSystemServer();
                return false;
            }
        }
    }

    public final SoftKeyboardController getSoftKeyboardController() {
        SoftKeyboardController softKeyboardController;
        synchronized (this.mLock) {
            if (this.mSoftKeyboardController == null) {
                this.mSoftKeyboardController = new SoftKeyboardController(this, this.mLock);
            }
            softKeyboardController = this.mSoftKeyboardController;
        }
        return softKeyboardController;
    }

    /* access modifiers changed from: private */
    public void onSoftKeyboardShowModeChanged(int showMode) {
        if (this.mSoftKeyboardController != null) {
            this.mSoftKeyboardController.dispatchSoftKeyboardShowModeChanged(showMode);
        }
    }

    public static final class SoftKeyboardController {
        private ArrayMap<OnShowModeChangedListener, Handler> mListeners;
        private final Object mLock;
        private final AccessibilityService mService;

        public interface OnShowModeChangedListener {
            void onShowModeChanged(SoftKeyboardController softKeyboardController, int i);
        }

        SoftKeyboardController(AccessibilityService service, Object lock) {
            this.mService = service;
            this.mLock = lock;
        }

        /* access modifiers changed from: package-private */
        public void onServiceConnected() {
            synchronized (this.mLock) {
                if (this.mListeners != null && !this.mListeners.isEmpty()) {
                    setSoftKeyboardCallbackEnabled(true);
                }
            }
        }

        public void addOnShowModeChangedListener(OnShowModeChangedListener listener) {
            addOnShowModeChangedListener(listener, (Handler) null);
        }

        public void addOnShowModeChangedListener(OnShowModeChangedListener listener, Handler handler) {
            synchronized (this.mLock) {
                if (this.mListeners == null) {
                    this.mListeners = new ArrayMap<>();
                }
                boolean shouldEnableCallback = this.mListeners.isEmpty();
                this.mListeners.put(listener, handler);
                if (shouldEnableCallback) {
                    setSoftKeyboardCallbackEnabled(true);
                }
            }
        }

        public boolean removeOnShowModeChangedListener(OnShowModeChangedListener listener) {
            boolean hasKey;
            if (this.mListeners == null) {
                return false;
            }
            synchronized (this.mLock) {
                int keyIndex = this.mListeners.indexOfKey(listener);
                hasKey = keyIndex >= 0;
                if (hasKey) {
                    this.mListeners.removeAt(keyIndex);
                }
                if (hasKey && this.mListeners.isEmpty()) {
                    setSoftKeyboardCallbackEnabled(false);
                }
            }
            return hasKey;
        }

        private void setSoftKeyboardCallbackEnabled(boolean enabled) {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection != null) {
                try {
                    connection.setSoftKeyboardCallbackEnabled(enabled);
                } catch (RemoteException re) {
                    throw new RuntimeException(re);
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0018, code lost:
            r0 = 0;
            r2 = r1.size();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001d, code lost:
            if (r0 >= r2) goto L_0x003c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x001f, code lost:
            r3 = r1.keyAt(r0);
            r4 = r1.valueAt(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x002b, code lost:
            if (r4 == null) goto L_0x0036;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x002d, code lost:
            r4.post(new android.accessibilityservice.AccessibilityService.SoftKeyboardController.AnonymousClass1(r6));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0036, code lost:
            r3.onShowModeChanged(r6, r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0039, code lost:
            r0 = r0 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x003c, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void dispatchSoftKeyboardShowModeChanged(final int r7) {
            /*
                r6 = this;
                java.lang.Object r0 = r6.mLock
                monitor-enter(r0)
                android.util.ArrayMap<android.accessibilityservice.AccessibilityService$SoftKeyboardController$OnShowModeChangedListener, android.os.Handler> r1 = r6.mListeners     // Catch:{ all -> 0x004a }
                if (r1 == 0) goto L_0x003d
                android.util.ArrayMap<android.accessibilityservice.AccessibilityService$SoftKeyboardController$OnShowModeChangedListener, android.os.Handler> r1 = r6.mListeners     // Catch:{ all -> 0x004a }
                boolean r1 = r1.isEmpty()     // Catch:{ all -> 0x004a }
                if (r1 == 0) goto L_0x0010
                goto L_0x003d
            L_0x0010:
                android.util.ArrayMap r1 = new android.util.ArrayMap     // Catch:{ all -> 0x004a }
                android.util.ArrayMap<android.accessibilityservice.AccessibilityService$SoftKeyboardController$OnShowModeChangedListener, android.os.Handler> r2 = r6.mListeners     // Catch:{ all -> 0x004a }
                r1.<init>(r2)     // Catch:{ all -> 0x004a }
                monitor-exit(r0)     // Catch:{ all -> 0x004a }
                r0 = 0
                int r2 = r1.size()
            L_0x001d:
                if (r0 >= r2) goto L_0x003c
                java.lang.Object r3 = r1.keyAt(r0)
                android.accessibilityservice.AccessibilityService$SoftKeyboardController$OnShowModeChangedListener r3 = (android.accessibilityservice.AccessibilityService.SoftKeyboardController.OnShowModeChangedListener) r3
                java.lang.Object r4 = r1.valueAt(r0)
                android.os.Handler r4 = (android.os.Handler) r4
                if (r4 == 0) goto L_0x0036
                android.accessibilityservice.AccessibilityService$SoftKeyboardController$1 r5 = new android.accessibilityservice.AccessibilityService$SoftKeyboardController$1
                r5.<init>(r3, r7)
                r4.post(r5)
                goto L_0x0039
            L_0x0036:
                r3.onShowModeChanged(r6, r7)
            L_0x0039:
                int r0 = r0 + 1
                goto L_0x001d
            L_0x003c:
                return
            L_0x003d:
                java.lang.String r1 = "AccessibilityService"
                java.lang.String r2 = "Received soft keyboard show mode changed callback with no listeners registered!"
                android.util.Slog.w((java.lang.String) r1, (java.lang.String) r2)     // Catch:{ all -> 0x004a }
                r1 = 0
                r6.setSoftKeyboardCallbackEnabled(r1)     // Catch:{ all -> 0x004a }
                monitor-exit(r0)     // Catch:{ all -> 0x004a }
                return
            L_0x004a:
                r1 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x004a }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: android.accessibilityservice.AccessibilityService.SoftKeyboardController.dispatchSoftKeyboardShowModeChanged(int):void");
        }

        public int getShowMode() {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection == null) {
                return 0;
            }
            try {
                return connection.getSoftKeyboardShowMode();
            } catch (RemoteException re) {
                Log.w(AccessibilityService.LOG_TAG, "Failed to set soft keyboard behavior", re);
                re.rethrowFromSystemServer();
                return 0;
            }
        }

        public boolean setShowMode(int showMode) {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection == null) {
                return false;
            }
            try {
                return connection.setSoftKeyboardShowMode(showMode);
            } catch (RemoteException re) {
                Log.w(AccessibilityService.LOG_TAG, "Failed to set soft keyboard behavior", re);
                re.rethrowFromSystemServer();
                return false;
            }
        }
    }

    public final AccessibilityButtonController getAccessibilityButtonController() {
        AccessibilityButtonController accessibilityButtonController;
        synchronized (this.mLock) {
            if (this.mAccessibilityButtonController == null) {
                AccessibilityInteractionClient.getInstance();
                this.mAccessibilityButtonController = new AccessibilityButtonController(AccessibilityInteractionClient.getConnection(this.mConnectionId));
            }
            accessibilityButtonController = this.mAccessibilityButtonController;
        }
        return accessibilityButtonController;
    }

    /* access modifiers changed from: private */
    public void onAccessibilityButtonClicked() {
        getAccessibilityButtonController().dispatchAccessibilityButtonClicked();
    }

    /* access modifiers changed from: private */
    public void onAccessibilityButtonAvailabilityChanged(boolean available) {
        getAccessibilityButtonController().dispatchAccessibilityButtonAvailabilityChanged(available);
    }

    public final boolean performGlobalAction(int action) {
        AccessibilityInteractionClient.getInstance();
        IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mConnectionId);
        if (connection == null) {
            return false;
        }
        try {
            return connection.performGlobalAction(action);
        } catch (RemoteException re) {
            Log.w(LOG_TAG, "Error while calling performGlobalAction", re);
            re.rethrowFromSystemServer();
            return false;
        }
    }

    public AccessibilityNodeInfo findFocus(int focus) {
        return AccessibilityInteractionClient.getInstance().findFocus(this.mConnectionId, -2, AccessibilityNodeInfo.ROOT_NODE_ID, focus);
    }

    public final AccessibilityServiceInfo getServiceInfo() {
        AccessibilityInteractionClient.getInstance();
        IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mConnectionId);
        if (connection == null) {
            return null;
        }
        try {
            return connection.getServiceInfo();
        } catch (RemoteException re) {
            Log.w(LOG_TAG, "Error while getting AccessibilityServiceInfo", re);
            re.rethrowFromSystemServer();
            return null;
        }
    }

    public final void setServiceInfo(AccessibilityServiceInfo info) {
        this.mInfo = info;
        sendServiceInfo();
    }

    private void sendServiceInfo() {
        AccessibilityInteractionClient.getInstance();
        IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mConnectionId);
        if (this.mInfo != null && connection != null) {
            try {
                connection.setServiceInfo(this.mInfo);
                this.mInfo = null;
                AccessibilityInteractionClient.getInstance().clearCache();
            } catch (RemoteException re) {
                Log.w(LOG_TAG, "Error while setting AccessibilityServiceInfo", re);
                re.rethrowFromSystemServer();
            }
        }
    }

    public Object getSystemService(String name) {
        if (getBaseContext() == null) {
            throw new IllegalStateException("System services not available to Activities before onCreate()");
        } else if (!Context.WINDOW_SERVICE.equals(name)) {
            return super.getSystemService(name);
        } else {
            if (this.mWindowManager == null) {
                this.mWindowManager = (WindowManager) getBaseContext().getSystemService(name);
            }
            return this.mWindowManager;
        }
    }

    public final IBinder onBind(Intent intent) {
        return new IAccessibilityServiceClientWrapper(this, getMainLooper(), new Callbacks() {
            public void onServiceConnected() {
                AccessibilityService.this.dispatchServiceConnected();
            }

            public void onInterrupt() {
                AccessibilityService.this.onInterrupt();
            }

            public void onAccessibilityEvent(AccessibilityEvent event) {
                AccessibilityService.this.onAccessibilityEvent(event);
            }

            public void init(int connectionId, IBinder windowToken) {
                int unused = AccessibilityService.this.mConnectionId = connectionId;
                IBinder unused2 = AccessibilityService.this.mWindowToken = windowToken;
                ((WindowManagerImpl) AccessibilityService.this.getSystemService(Context.WINDOW_SERVICE)).setDefaultToken(windowToken);
            }

            public boolean onGesture(int gestureId) {
                return AccessibilityService.this.onGesture(gestureId);
            }

            public boolean onKeyEvent(KeyEvent event) {
                return AccessibilityService.this.onKeyEvent(event);
            }

            public void onMagnificationChanged(int displayId, Region region, float scale, float centerX, float centerY) {
                AccessibilityService.this.onMagnificationChanged(displayId, region, scale, centerX, centerY);
            }

            public void onSoftKeyboardShowModeChanged(int showMode) {
                AccessibilityService.this.onSoftKeyboardShowModeChanged(showMode);
            }

            public void onPerformGestureResult(int sequence, boolean completedSuccessfully) {
                AccessibilityService.this.onPerformGestureResult(sequence, completedSuccessfully);
            }

            public void onFingerprintCapturingGesturesChanged(boolean active) {
                AccessibilityService.this.onFingerprintCapturingGesturesChanged(active);
            }

            public void onFingerprintGesture(int gesture) {
                AccessibilityService.this.onFingerprintGesture(gesture);
            }

            public void onAccessibilityButtonClicked() {
                AccessibilityService.this.onAccessibilityButtonClicked();
            }

            public void onAccessibilityButtonAvailabilityChanged(boolean available) {
                AccessibilityService.this.onAccessibilityButtonAvailabilityChanged(available);
            }
        });
    }

    public static class IAccessibilityServiceClientWrapper extends IAccessibilityServiceClient.Stub implements HandlerCaller.Callback {
        private static final int DO_ACCESSIBILITY_BUTTON_AVAILABILITY_CHANGED = 13;
        private static final int DO_ACCESSIBILITY_BUTTON_CLICKED = 12;
        private static final int DO_CLEAR_ACCESSIBILITY_CACHE = 5;
        private static final int DO_GESTURE_COMPLETE = 9;
        private static final int DO_INIT = 1;
        private static final int DO_ON_ACCESSIBILITY_EVENT = 3;
        private static final int DO_ON_FINGERPRINT_ACTIVE_CHANGED = 10;
        private static final int DO_ON_FINGERPRINT_GESTURE = 11;
        private static final int DO_ON_GESTURE = 4;
        private static final int DO_ON_INTERRUPT = 2;
        private static final int DO_ON_KEY_EVENT = 6;
        private static final int DO_ON_MAGNIFICATION_CHANGED = 7;
        private static final int DO_ON_SOFT_KEYBOARD_SHOW_MODE_CHANGED = 8;
        private final Callbacks mCallback;
        private final HandlerCaller mCaller;
        private int mConnectionId = -1;

        public IAccessibilityServiceClientWrapper(Context context, Looper looper, Callbacks callback) {
            this.mCallback = callback;
            this.mCaller = new HandlerCaller(context, looper, this, true);
        }

        public void init(IAccessibilityServiceConnection connection, int connectionId, IBinder windowToken) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageIOO(1, connectionId, connection, windowToken));
        }

        public void onInterrupt() {
            this.mCaller.sendMessage(this.mCaller.obtainMessage(2));
        }

        public void onAccessibilityEvent(AccessibilityEvent event, boolean serviceWantsEvent) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageBO(3, serviceWantsEvent, event));
        }

        public void onGesture(int gestureId) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageI(4, gestureId));
        }

        public void clearAccessibilityCache() {
            this.mCaller.sendMessage(this.mCaller.obtainMessage(5));
        }

        public void onKeyEvent(KeyEvent event, int sequence) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageIO(6, sequence, event));
        }

        public void onMagnificationChanged(int displayId, Region region, float scale, float centerX, float centerY) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = region;
            args.arg2 = Float.valueOf(scale);
            args.arg3 = Float.valueOf(centerX);
            args.arg4 = Float.valueOf(centerY);
            args.argi1 = displayId;
            this.mCaller.sendMessage(this.mCaller.obtainMessageO(7, args));
        }

        public void onSoftKeyboardShowModeChanged(int showMode) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageI(8, showMode));
        }

        public void onPerformGestureResult(int sequence, boolean successfully) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageII(9, sequence, successfully));
        }

        public void onFingerprintCapturingGesturesChanged(boolean active) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageI(10, active));
        }

        public void onFingerprintGesture(int gesture) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageI(11, gesture));
        }

        public void onAccessibilityButtonClicked() {
            this.mCaller.sendMessage(this.mCaller.obtainMessage(12));
        }

        public void onAccessibilityButtonAvailabilityChanged(boolean available) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageI(13, available));
        }

        public void executeMessage(Message message) {
            boolean serviceWantsEvent = false;
            switch (message.what) {
                case 1:
                    this.mConnectionId = message.arg1;
                    SomeArgs args = (SomeArgs) message.obj;
                    IAccessibilityServiceConnection connection = (IAccessibilityServiceConnection) args.arg1;
                    IBinder windowToken = (IBinder) args.arg2;
                    args.recycle();
                    if (connection != null) {
                        AccessibilityInteractionClient.getInstance();
                        AccessibilityInteractionClient.addConnection(this.mConnectionId, connection);
                        this.mCallback.init(this.mConnectionId, windowToken);
                        this.mCallback.onServiceConnected();
                        return;
                    }
                    AccessibilityInteractionClient.getInstance();
                    AccessibilityInteractionClient.removeConnection(this.mConnectionId);
                    this.mConnectionId = -1;
                    AccessibilityInteractionClient.getInstance().clearCache();
                    this.mCallback.init(-1, (IBinder) null);
                    return;
                case 2:
                    if (this.mConnectionId != -1) {
                        this.mCallback.onInterrupt();
                        return;
                    }
                    return;
                case 3:
                    AccessibilityEvent event = (AccessibilityEvent) message.obj;
                    if (message.arg1 != 0) {
                        serviceWantsEvent = true;
                    }
                    if (event != null) {
                        AccessibilityInteractionClient.getInstance().onAccessibilityEvent(event);
                        if (serviceWantsEvent && this.mConnectionId != -1) {
                            this.mCallback.onAccessibilityEvent(event);
                        }
                        try {
                            event.recycle();
                            return;
                        } catch (IllegalStateException e) {
                            return;
                        }
                    } else {
                        return;
                    }
                case 4:
                    if (this.mConnectionId != -1) {
                        this.mCallback.onGesture(message.arg1);
                        return;
                    }
                    return;
                case 5:
                    AccessibilityInteractionClient.getInstance().clearCache();
                    return;
                case 6:
                    KeyEvent event2 = (KeyEvent) message.obj;
                    try {
                        AccessibilityInteractionClient.getInstance();
                        IAccessibilityServiceConnection connection2 = AccessibilityInteractionClient.getConnection(this.mConnectionId);
                        if (connection2 != null) {
                            try {
                                connection2.setOnKeyEventResult(this.mCallback.onKeyEvent(event2), message.arg1);
                            } catch (RemoteException e2) {
                            }
                        }
                        try {
                            return;
                        } catch (IllegalStateException e3) {
                            return;
                        }
                    } finally {
                        try {
                            event2.recycle();
                        } catch (IllegalStateException e4) {
                        }
                    }
                case 7:
                    if (this.mConnectionId != -1) {
                        SomeArgs args2 = (SomeArgs) message.obj;
                        float scale = ((Float) args2.arg2).floatValue();
                        float centerX = ((Float) args2.arg3).floatValue();
                        float centerY = ((Float) args2.arg4).floatValue();
                        int displayId = args2.argi1;
                        args2.recycle();
                        this.mCallback.onMagnificationChanged(displayId, (Region) args2.arg1, scale, centerX, centerY);
                        return;
                    }
                    return;
                case 8:
                    if (this.mConnectionId != -1) {
                        this.mCallback.onSoftKeyboardShowModeChanged(message.arg1);
                        return;
                    }
                    return;
                case 9:
                    if (this.mConnectionId != -1) {
                        if (message.arg2 == 1) {
                            serviceWantsEvent = true;
                        }
                        this.mCallback.onPerformGestureResult(message.arg1, serviceWantsEvent);
                        return;
                    }
                    return;
                case 10:
                    if (this.mConnectionId != -1) {
                        Callbacks callbacks = this.mCallback;
                        if (message.arg1 == 1) {
                            serviceWantsEvent = true;
                        }
                        callbacks.onFingerprintCapturingGesturesChanged(serviceWantsEvent);
                        return;
                    }
                    return;
                case 11:
                    if (this.mConnectionId != -1) {
                        this.mCallback.onFingerprintGesture(message.arg1);
                        return;
                    }
                    return;
                case 12:
                    if (this.mConnectionId != -1) {
                        this.mCallback.onAccessibilityButtonClicked();
                        return;
                    }
                    return;
                case 13:
                    if (this.mConnectionId != -1) {
                        if (message.arg1 != 0) {
                            serviceWantsEvent = true;
                        }
                        this.mCallback.onAccessibilityButtonAvailabilityChanged(serviceWantsEvent);
                        return;
                    }
                    return;
                default:
                    Log.w(AccessibilityService.LOG_TAG, "Unknown message type " + message.what);
                    return;
            }
        }
    }

    public static abstract class GestureResultCallback {
        public void onCompleted(GestureDescription gestureDescription) {
        }

        public void onCancelled(GestureDescription gestureDescription) {
        }
    }

    private static class GestureResultCallbackInfo {
        GestureResultCallback callback;
        GestureDescription gestureDescription;
        Handler handler;

        GestureResultCallbackInfo(GestureDescription gestureDescription2, GestureResultCallback callback2, Handler handler2) {
            this.gestureDescription = gestureDescription2;
            this.callback = callback2;
            this.handler = handler2;
        }
    }
}
