package android.os;

import android.animation.ValueAnimator;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.IActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.INetworkManagementService;
import android.os.MessageQueue;
import android.os.Parcelable;
import android.os.StrictMode;
import android.os.storage.IStorageManager;
import android.os.strictmode.CleartextNetworkViolation;
import android.os.strictmode.ContentUriWithoutPermissionViolation;
import android.os.strictmode.CredentialProtectedWhileLockedViolation;
import android.os.strictmode.CustomViolation;
import android.os.strictmode.DiskReadViolation;
import android.os.strictmode.DiskWriteViolation;
import android.os.strictmode.ExplicitGcViolation;
import android.os.strictmode.FileUriExposedViolation;
import android.os.strictmode.ImplicitDirectBootViolation;
import android.os.strictmode.InstanceCountViolation;
import android.os.strictmode.IntentReceiverLeakedViolation;
import android.os.strictmode.LeakedClosableViolation;
import android.os.strictmode.NetworkViolation;
import android.os.strictmode.ResourceMismatchViolation;
import android.os.strictmode.ServiceConnectionLeakedViolation;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.os.strictmode.UnbufferedIoViolation;
import android.os.strictmode.UntaggedSocketViolation;
import android.os.strictmode.Violation;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Printer;
import android.util.Singleton;
import android.view.IWindowManager;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.BackgroundThread;
import com.android.internal.os.RuntimeInit;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.HexDump;
import dalvik.system.BlockGuard;
import dalvik.system.CloseGuard;
import dalvik.system.VMDebug;
import dalvik.system.VMRuntime;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class StrictMode {
    private static final String CLEARTEXT_PROPERTY = "persist.sys.strictmode.clear";
    private static final int DETECT_THREAD_ALL = 65535;
    private static final int DETECT_THREAD_CUSTOM = 8;
    private static final int DETECT_THREAD_DISK_READ = 2;
    private static final int DETECT_THREAD_DISK_WRITE = 1;
    private static final int DETECT_THREAD_EXPLICIT_GC = 64;
    private static final int DETECT_THREAD_NETWORK = 4;
    private static final int DETECT_THREAD_RESOURCE_MISMATCH = 16;
    private static final int DETECT_THREAD_UNBUFFERED_IO = 32;
    private static final int DETECT_VM_ACTIVITY_LEAKS = 4;
    private static final int DETECT_VM_ALL = 65535;
    private static final int DETECT_VM_CLEARTEXT_NETWORK = 64;
    private static final int DETECT_VM_CLOSABLE_LEAKS = 2;
    private static final int DETECT_VM_CONTENT_URI_WITHOUT_PERMISSION = 128;
    private static final int DETECT_VM_CREDENTIAL_PROTECTED_WHILE_LOCKED = 2048;
    private static final int DETECT_VM_CURSOR_LEAKS = 1;
    private static final int DETECT_VM_FILE_URI_EXPOSURE = 32;
    private static final int DETECT_VM_IMPLICIT_DIRECT_BOOT = 1024;
    private static final int DETECT_VM_INSTANCE_LEAKS = 8;
    private static final int DETECT_VM_NON_SDK_API_USAGE = 512;
    private static final int DETECT_VM_REGISTRATION_LEAKS = 16;
    private static final int DETECT_VM_UNTAGGED_SOCKET = 256;
    private static final boolean DISABLE = false;
    public static final String DISABLE_PROPERTY = "persist.sys.strictmode.disable";
    /* access modifiers changed from: private */
    public static final HashMap<Class, Integer> EMPTY_CLASS_LIMIT_MAP = new HashMap<>();
    private static final ViolationLogger LOGCAT_LOGGER = $$Lambda$StrictMode$1yH8AK0bTwVwZOb9x8HoiSBdzr0.INSTANCE;
    /* access modifiers changed from: private */
    public static final boolean LOG_V = Log.isLoggable(TAG, 2);
    private static final int MAX_OFFENSES_PER_LOOP = 10;
    private static final int MAX_SPAN_TAGS = 20;
    private static final long MIN_DIALOG_INTERVAL_MS = 30000;
    private static final long MIN_LOG_INTERVAL_MS = 1000;
    private static final long MIN_VM_INTERVAL_MS = 1000;
    public static final int NETWORK_POLICY_ACCEPT = 0;
    public static final int NETWORK_POLICY_LOG = 1;
    public static final int NETWORK_POLICY_REJECT = 2;
    private static final Span NO_OP_SPAN = new Span() {
        public void finish() {
        }
    };
    public static final int PENALTY_ALL = -65536;
    public static final int PENALTY_DEATH = 268435456;
    public static final int PENALTY_DEATH_ON_CLEARTEXT_NETWORK = 16777216;
    public static final int PENALTY_DEATH_ON_FILE_URI_EXPOSURE = 8388608;
    public static final int PENALTY_DEATH_ON_NETWORK = 33554432;
    public static final int PENALTY_DIALOG = 536870912;
    public static final int PENALTY_DROPBOX = 67108864;
    public static final int PENALTY_FLASH = 134217728;
    public static final int PENALTY_GATHER = Integer.MIN_VALUE;
    public static final int PENALTY_LOG = 1073741824;
    private static final String TAG = "StrictMode";
    private static final ThreadLocal<AndroidBlockGuardPolicy> THREAD_ANDROID_POLICY = new ThreadLocal<AndroidBlockGuardPolicy>() {
        /* access modifiers changed from: protected */
        public AndroidBlockGuardPolicy initialValue() {
            return new AndroidBlockGuardPolicy(0);
        }
    };
    /* access modifiers changed from: private */
    public static final ThreadLocal<Handler> THREAD_HANDLER = new ThreadLocal<Handler>() {
        /* access modifiers changed from: protected */
        public Handler initialValue() {
            return new Handler();
        }
    };
    public static final String VISUAL_PROPERTY = "persist.sys.strictmode.visual";
    private static final BlockGuard.VmPolicy VM_ANDROID_POLICY = new BlockGuard.VmPolicy() {
        public void onPathAccess(String path) {
            if (path != null) {
                if (path.startsWith("/data/user/") || path.startsWith("/data/media/") || path.startsWith("/data/system_ce/") || path.startsWith("/data/misc_ce/") || path.startsWith("/data/vendor_ce/") || path.startsWith("/storage/emulated/")) {
                    int third = path.indexOf(47, path.indexOf(47, 1) + 1);
                    int fourth = path.indexOf(47, third + 1);
                    if (fourth != -1) {
                        try {
                            StrictMode.onCredentialProtectedPathAccess(path, Integer.parseInt(path.substring(third + 1, fourth)));
                        } catch (NumberFormatException e) {
                        }
                    }
                } else if (path.startsWith("/data/data/")) {
                    StrictMode.onCredentialProtectedPathAccess(path, 0);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public static final ThreadLocal<ArrayList<ViolationInfo>> gatheredViolations = new ThreadLocal<ArrayList<ViolationInfo>>() {
        /* access modifiers changed from: protected */
        public ArrayList<ViolationInfo> initialValue() {
            return null;
        }
    };
    private static final AtomicInteger sDropboxCallsInFlight = new AtomicInteger(0);
    @GuardedBy({"StrictMode.class"})
    private static final HashMap<Class, Integer> sExpectedActivityInstanceCount = new HashMap<>();
    private static boolean sIsIdlerRegistered = false;
    /* access modifiers changed from: private */
    public static long sLastInstanceCountCheckMillis = 0;
    @UnsupportedAppUsage
    private static final HashMap<Integer, Long> sLastVmViolationTime = new HashMap<>();
    /* access modifiers changed from: private */
    public static volatile ViolationLogger sLogger = LOGCAT_LOGGER;
    private static final Consumer<String> sNonSdkApiUsageConsumer = $$Lambda$StrictMode$lu9ekkHJ2HMz0jd3F8K8MnhenxQ.INSTANCE;
    private static final MessageQueue.IdleHandler sProcessIdleHandler = new MessageQueue.IdleHandler() {
        public boolean queueIdle() {
            long now = SystemClock.uptimeMillis();
            if (now - StrictMode.sLastInstanceCountCheckMillis <= 30000) {
                return true;
            }
            long unused = StrictMode.sLastInstanceCountCheckMillis = now;
            StrictMode.conditionallyCheckInstanceCounts();
            return true;
        }
    };
    /* access modifiers changed from: private */
    public static final ThreadLocal<ThreadSpanState> sThisThreadSpanState = new ThreadLocal<ThreadSpanState>() {
        /* access modifiers changed from: protected */
        public ThreadSpanState initialValue() {
            return new ThreadSpanState();
        }
    };
    /* access modifiers changed from: private */
    public static final ThreadLocal<Executor> sThreadViolationExecutor = new ThreadLocal<>();
    /* access modifiers changed from: private */
    public static final ThreadLocal<OnThreadViolationListener> sThreadViolationListener = new ThreadLocal<>();
    private static volatile boolean sUserKeyUnlocked = false;
    private static volatile VmPolicy sVmPolicy = VmPolicy.LAX;
    /* access modifiers changed from: private */
    @UnsupportedAppUsage
    public static Singleton<IWindowManager> sWindowManager = new Singleton<IWindowManager>() {
        /* access modifiers changed from: protected */
        public IWindowManager create() {
            return IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
        }
    };
    /* access modifiers changed from: private */
    @UnsupportedAppUsage
    public static final ThreadLocal<ArrayList<ViolationInfo>> violationsBeingTimed = new ThreadLocal<ArrayList<ViolationInfo>>() {
        /* access modifiers changed from: protected */
        public ArrayList<ViolationInfo> initialValue() {
            return new ArrayList<>();
        }
    };

    public interface OnThreadViolationListener {
        void onThreadViolation(Violation violation);
    }

    public interface OnVmViolationListener {
        void onVmViolation(Violation violation);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ThreadPolicyMask {
    }

    public interface ViolationLogger {
        void log(ViolationInfo violationInfo);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface VmPolicyMask {
    }

    static /* synthetic */ void lambda$static$0(ViolationInfo info) {
        String msg;
        if (info.durationMillis != -1) {
            msg = "StrictMode policy violation; ~duration=" + info.durationMillis + " ms:";
        } else {
            msg = "StrictMode policy violation:";
        }
        Log.d(TAG, msg + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + info.getStackTrace());
    }

    public static void setViolationLogger(ViolationLogger listener) {
        if (listener == null) {
            listener = LOGCAT_LOGGER;
        }
        sLogger = listener;
    }

    private StrictMode() {
    }

    public static final class ThreadPolicy {
        public static final ThreadPolicy LAX = new ThreadPolicy(0, (OnThreadViolationListener) null, (Executor) null);
        final Executor mCallbackExecutor;
        final OnThreadViolationListener mListener;
        @UnsupportedAppUsage
        final int mask;

        private ThreadPolicy(int mask2, OnThreadViolationListener listener, Executor executor) {
            this.mask = mask2;
            this.mListener = listener;
            this.mCallbackExecutor = executor;
        }

        public String toString() {
            return "[StrictMode.ThreadPolicy; mask=" + this.mask + "]";
        }

        public static final class Builder {
            private Executor mExecutor;
            private OnThreadViolationListener mListener;
            private int mMask;

            public Builder() {
                this.mMask = 0;
                this.mMask = 0;
            }

            public Builder(ThreadPolicy policy) {
                this.mMask = 0;
                this.mMask = policy.mask;
                this.mListener = policy.mListener;
                this.mExecutor = policy.mCallbackExecutor;
            }

            public Builder detectAll() {
                detectDiskReads();
                detectDiskWrites();
                detectNetwork();
                int targetSdk = VMRuntime.getRuntime().getTargetSdkVersion();
                if (targetSdk >= 11) {
                    detectCustomSlowCalls();
                }
                if (targetSdk >= 23) {
                    detectResourceMismatches();
                }
                if (targetSdk >= 26) {
                    detectUnbufferedIo();
                }
                return this;
            }

            public Builder permitAll() {
                return disable(65535);
            }

            public Builder detectNetwork() {
                return enable(4);
            }

            public Builder permitNetwork() {
                return disable(4);
            }

            public Builder detectDiskReads() {
                return enable(2);
            }

            public Builder permitDiskReads() {
                return disable(2);
            }

            public Builder detectCustomSlowCalls() {
                return enable(8);
            }

            public Builder permitCustomSlowCalls() {
                return disable(8);
            }

            public Builder permitResourceMismatches() {
                return disable(16);
            }

            public Builder detectUnbufferedIo() {
                return enable(32);
            }

            public Builder permitUnbufferedIo() {
                return disable(32);
            }

            public Builder detectResourceMismatches() {
                return enable(16);
            }

            public Builder detectDiskWrites() {
                return enable(1);
            }

            public Builder permitDiskWrites() {
                return disable(1);
            }

            public Builder detectExplicitGc() {
                return enable(64);
            }

            public Builder permitExplicitGc() {
                return disable(64);
            }

            public Builder penaltyDialog() {
                return enable(536870912);
            }

            public Builder penaltyDeath() {
                return enable(268435456);
            }

            public Builder penaltyDeathOnNetwork() {
                return enable(33554432);
            }

            public Builder penaltyFlashScreen() {
                return enable(134217728);
            }

            public Builder penaltyLog() {
                return enable(1073741824);
            }

            public Builder penaltyDropBox() {
                return enable(67108864);
            }

            public Builder penaltyListener(Executor executor, OnThreadViolationListener listener) {
                if (executor != null) {
                    this.mListener = listener;
                    this.mExecutor = executor;
                    return this;
                }
                throw new NullPointerException("executor must not be null");
            }

            public Builder penaltyListener(OnThreadViolationListener listener, Executor executor) {
                return penaltyListener(executor, listener);
            }

            private Builder enable(int mask) {
                this.mMask |= mask;
                return this;
            }

            private Builder disable(int mask) {
                this.mMask &= ~mask;
                return this;
            }

            public ThreadPolicy build() {
                if (this.mListener == null && this.mMask != 0 && (this.mMask & 1946157056) == 0) {
                    penaltyLog();
                }
                return new ThreadPolicy(this.mMask, this.mListener, this.mExecutor);
            }
        }
    }

    public static final class VmPolicy {
        public static final VmPolicy LAX = new VmPolicy(0, StrictMode.EMPTY_CLASS_LIMIT_MAP, (OnVmViolationListener) null, (Executor) null);
        final HashMap<Class, Integer> classInstanceLimit;
        final Executor mCallbackExecutor;
        final OnVmViolationListener mListener;
        @UnsupportedAppUsage
        final int mask;

        private VmPolicy(int mask2, HashMap<Class, Integer> classInstanceLimit2, OnVmViolationListener listener, Executor executor) {
            if (classInstanceLimit2 != null) {
                this.mask = mask2;
                this.classInstanceLimit = classInstanceLimit2;
                this.mListener = listener;
                this.mCallbackExecutor = executor;
                return;
            }
            throw new NullPointerException("classInstanceLimit == null");
        }

        public String toString() {
            return "[StrictMode.VmPolicy; mask=" + this.mask + "]";
        }

        public static final class Builder {
            private HashMap<Class, Integer> mClassInstanceLimit;
            private boolean mClassInstanceLimitNeedCow;
            private Executor mExecutor;
            private OnVmViolationListener mListener;
            @UnsupportedAppUsage
            private int mMask;

            public Builder() {
                this.mClassInstanceLimitNeedCow = false;
                this.mMask = 0;
            }

            public Builder(VmPolicy base) {
                this.mClassInstanceLimitNeedCow = false;
                this.mMask = base.mask;
                this.mClassInstanceLimitNeedCow = true;
                this.mClassInstanceLimit = base.classInstanceLimit;
                this.mListener = base.mListener;
                this.mExecutor = base.mCallbackExecutor;
            }

            public Builder setClassInstanceLimit(Class klass, int instanceLimit) {
                if (klass != null) {
                    if (this.mClassInstanceLimitNeedCow) {
                        if (this.mClassInstanceLimit.containsKey(klass) && this.mClassInstanceLimit.get(klass).intValue() == instanceLimit) {
                            return this;
                        }
                        this.mClassInstanceLimitNeedCow = false;
                        this.mClassInstanceLimit = (HashMap) this.mClassInstanceLimit.clone();
                    } else if (this.mClassInstanceLimit == null) {
                        this.mClassInstanceLimit = new HashMap<>();
                    }
                    this.mMask |= 8;
                    this.mClassInstanceLimit.put(klass, Integer.valueOf(instanceLimit));
                    return this;
                }
                throw new NullPointerException("klass == null");
            }

            public Builder detectActivityLeaks() {
                return enable(4);
            }

            public Builder permitActivityLeaks() {
                return disable(4);
            }

            public Builder detectNonSdkApiUsage() {
                return enable(512);
            }

            public Builder permitNonSdkApiUsage() {
                return disable(512);
            }

            public Builder detectAll() {
                detectLeakedSqlLiteObjects();
                int targetSdk = VMRuntime.getRuntime().getTargetSdkVersion();
                if (targetSdk >= 11) {
                    detectActivityLeaks();
                    detectLeakedClosableObjects();
                }
                if (targetSdk >= 16) {
                    detectLeakedRegistrationObjects();
                }
                if (targetSdk >= 18) {
                    detectFileUriExposure();
                }
                if (targetSdk >= 23 && SystemProperties.getBoolean(StrictMode.CLEARTEXT_PROPERTY, false)) {
                    detectCleartextNetwork();
                }
                if (targetSdk >= 26) {
                    detectContentUriWithoutPermission();
                    detectUntaggedSockets();
                }
                if (targetSdk >= 29) {
                    detectCredentialProtectedWhileLocked();
                }
                return this;
            }

            public Builder detectLeakedSqlLiteObjects() {
                return enable(1);
            }

            public Builder detectLeakedClosableObjects() {
                return enable(2);
            }

            public Builder detectLeakedRegistrationObjects() {
                return enable(16);
            }

            public Builder detectFileUriExposure() {
                return enable(32);
            }

            public Builder detectCleartextNetwork() {
                return enable(64);
            }

            public Builder detectContentUriWithoutPermission() {
                return enable(128);
            }

            public Builder detectUntaggedSockets() {
                return enable(256);
            }

            public Builder permitUntaggedSockets() {
                return disable(256);
            }

            public Builder detectImplicitDirectBoot() {
                return enable(1024);
            }

            public Builder permitImplicitDirectBoot() {
                return disable(1024);
            }

            public Builder detectCredentialProtectedWhileLocked() {
                return enable(2048);
            }

            public Builder permitCredentialProtectedWhileLocked() {
                return disable(2048);
            }

            public Builder penaltyDeath() {
                return enable(268435456);
            }

            public Builder penaltyDeathOnCleartextNetwork() {
                return enable(16777216);
            }

            public Builder penaltyDeathOnFileUriExposure() {
                return enable(8388608);
            }

            public Builder penaltyLog() {
                return enable(1073741824);
            }

            public Builder penaltyDropBox() {
                return enable(67108864);
            }

            public Builder penaltyListener(Executor executor, OnVmViolationListener listener) {
                if (executor != null) {
                    this.mListener = listener;
                    this.mExecutor = executor;
                    return this;
                }
                throw new NullPointerException("executor must not be null");
            }

            public Builder penaltyListener(OnVmViolationListener listener, Executor executor) {
                return penaltyListener(executor, listener);
            }

            private Builder enable(int mask) {
                this.mMask |= mask;
                return this;
            }

            /* access modifiers changed from: package-private */
            public Builder disable(int mask) {
                this.mMask &= ~mask;
                return this;
            }

            public VmPolicy build() {
                if (this.mListener == null && this.mMask != 0 && (this.mMask & 1946157056) == 0) {
                    penaltyLog();
                }
                return new VmPolicy(this.mMask, this.mClassInstanceLimit != null ? this.mClassInstanceLimit : StrictMode.EMPTY_CLASS_LIMIT_MAP, this.mListener, this.mExecutor);
            }
        }
    }

    public static void setThreadPolicy(ThreadPolicy policy) {
        setThreadPolicyMask(policy.mask);
        sThreadViolationListener.set(policy.mListener);
        sThreadViolationExecutor.set(policy.mCallbackExecutor);
    }

    public static void setThreadPolicyMask(int threadPolicyMask) {
        setBlockGuardPolicy(threadPolicyMask);
        Binder.setThreadStrictModePolicy(threadPolicyMask);
    }

    private static void setBlockGuardPolicy(int threadPolicyMask) {
        AndroidBlockGuardPolicy androidPolicy;
        if (threadPolicyMask == 0) {
            BlockGuard.setThreadPolicy(BlockGuard.LAX_POLICY);
            return;
        }
        AndroidBlockGuardPolicy threadPolicy = BlockGuard.getThreadPolicy();
        if (threadPolicy instanceof AndroidBlockGuardPolicy) {
            androidPolicy = threadPolicy;
        } else {
            androidPolicy = THREAD_ANDROID_POLICY.get();
            BlockGuard.setThreadPolicy(androidPolicy);
        }
        androidPolicy.setThreadPolicyMask(threadPolicyMask);
    }

    private static void setBlockGuardVmPolicy(int vmPolicyMask) {
        if ((vmPolicyMask & 2048) != 0) {
            BlockGuard.setVmPolicy(VM_ANDROID_POLICY);
        } else {
            BlockGuard.setVmPolicy(BlockGuard.LAX_VM_POLICY);
        }
    }

    private static void setCloseGuardEnabled(boolean enabled) {
        if (!(CloseGuard.getReporter() instanceof AndroidCloseGuardReporter)) {
            CloseGuard.setReporter(new AndroidCloseGuardReporter());
        }
        CloseGuard.setEnabled(enabled);
    }

    @UnsupportedAppUsage
    public static int getThreadPolicyMask() {
        AndroidBlockGuardPolicy threadPolicy = BlockGuard.getThreadPolicy();
        if (threadPolicy instanceof AndroidBlockGuardPolicy) {
            return threadPolicy.getThreadPolicyMask();
        }
        return 0;
    }

    public static ThreadPolicy getThreadPolicy() {
        return new ThreadPolicy(getThreadPolicyMask(), sThreadViolationListener.get(), sThreadViolationExecutor.get());
    }

    public static ThreadPolicy allowThreadDiskWrites() {
        return new ThreadPolicy(allowThreadDiskWritesMask(), sThreadViolationListener.get(), sThreadViolationExecutor.get());
    }

    public static int allowThreadDiskWritesMask() {
        int oldPolicyMask = getThreadPolicyMask();
        int newPolicyMask = oldPolicyMask & -4;
        if (newPolicyMask != oldPolicyMask) {
            setThreadPolicyMask(newPolicyMask);
        }
        return oldPolicyMask;
    }

    public static ThreadPolicy allowThreadDiskReads() {
        return new ThreadPolicy(allowThreadDiskReadsMask(), sThreadViolationListener.get(), sThreadViolationExecutor.get());
    }

    public static int allowThreadDiskReadsMask() {
        int oldPolicyMask = getThreadPolicyMask();
        int newPolicyMask = oldPolicyMask & -3;
        if (newPolicyMask != oldPolicyMask) {
            setThreadPolicyMask(newPolicyMask);
        }
        return oldPolicyMask;
    }

    public static ThreadPolicy allowThreadViolations() {
        ThreadPolicy oldPolicy = getThreadPolicy();
        setThreadPolicyMask(0);
        return oldPolicy;
    }

    public static VmPolicy allowVmViolations() {
        VmPolicy oldPolicy = getVmPolicy();
        sVmPolicy = VmPolicy.LAX;
        return oldPolicy;
    }

    public static boolean isBundledSystemApp(ApplicationInfo ai) {
        if (ai == null || ai.packageName == null) {
            return true;
        }
        if (!ai.isSystemApp() || ai.packageName.equals("com.android.vending") || ai.packageName.equals("com.android.chrome") || ai.packageName.equals(TelephonyManager.PHONE_PROCESS_NAME)) {
            return false;
        }
        if (ai.packageName.equals("android") || ai.packageName.startsWith("android.") || ai.packageName.startsWith("com.android.")) {
            return true;
        }
        return false;
    }

    public static void initThreadDefaults(ApplicationInfo ai) {
        ThreadPolicy.Builder builder = new ThreadPolicy.Builder();
        if ((ai != null ? ai.targetSdkVersion : 10000) >= 11) {
            builder.detectNetwork();
            builder.penaltyDeathOnNetwork();
        }
        if (!Build.IS_USER && !SystemProperties.getBoolean(DISABLE_PROPERTY, false)) {
            if (Build.IS_USERDEBUG) {
                if (isBundledSystemApp(ai)) {
                    builder.detectAll();
                    builder.penaltyDropBox();
                    if (SystemProperties.getBoolean(VISUAL_PROPERTY, false)) {
                        builder.penaltyFlashScreen();
                    }
                }
            } else if (Build.IS_ENG && isBundledSystemApp(ai)) {
                builder.detectAll();
                builder.penaltyDropBox();
                builder.penaltyLog();
                builder.penaltyFlashScreen();
            }
        }
        setThreadPolicy(builder.build());
    }

    public static void initVmDefaults(ApplicationInfo ai) {
        VmPolicy.Builder builder = new VmPolicy.Builder();
        if ((ai != null ? ai.targetSdkVersion : 10000) >= 24) {
            builder.detectFileUriExposure();
            builder.penaltyDeathOnFileUriExposure();
        }
        if (!Build.IS_USER && !SystemProperties.getBoolean(DISABLE_PROPERTY, false)) {
            if (Build.IS_USERDEBUG) {
                if (isBundledSystemApp(ai)) {
                    builder.detectAll();
                    builder.permitActivityLeaks();
                    builder.penaltyDropBox();
                }
            } else if (Build.IS_ENG && isBundledSystemApp(ai)) {
                builder.detectAll();
                builder.penaltyDropBox();
                builder.penaltyLog();
            }
        }
        setVmPolicy(builder.build());
    }

    @UnsupportedAppUsage
    public static void enableDeathOnFileUriExposure() {
        sVmPolicy = new VmPolicy(8388608 | sVmPolicy.mask | 32, sVmPolicy.classInstanceLimit, sVmPolicy.mListener, sVmPolicy.mCallbackExecutor);
    }

    @UnsupportedAppUsage
    public static void disableDeathOnFileUriExposure() {
        sVmPolicy = new VmPolicy(-8388641 & sVmPolicy.mask, sVmPolicy.classInstanceLimit, sVmPolicy.mListener, sVmPolicy.mCallbackExecutor);
    }

    /* access modifiers changed from: private */
    public static boolean tooManyViolationsThisLoop() {
        return violationsBeingTimed.get().size() >= 10;
    }

    private static class AndroidBlockGuardPolicy implements BlockGuard.Policy {
        private ArrayMap<Integer, Long> mLastViolationTime;
        private int mThreadPolicyMask;

        public AndroidBlockGuardPolicy(int threadPolicyMask) {
            this.mThreadPolicyMask = threadPolicyMask;
        }

        public String toString() {
            return "AndroidBlockGuardPolicy; mPolicyMask=" + this.mThreadPolicyMask;
        }

        public int getPolicyMask() {
            return this.mThreadPolicyMask;
        }

        public void onWriteToDisk() {
            if ((this.mThreadPolicyMask & 1) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                startHandlingViolationException(new DiskWriteViolation());
            }
        }

        /* access modifiers changed from: package-private */
        public void onCustomSlowCall(String name) {
            if ((this.mThreadPolicyMask & 8) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                startHandlingViolationException(new CustomViolation(name));
            }
        }

        /* access modifiers changed from: package-private */
        public void onResourceMismatch(Object tag) {
            if ((this.mThreadPolicyMask & 16) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                startHandlingViolationException(new ResourceMismatchViolation(tag));
            }
        }

        public void onUnbufferedIO() {
            if ((this.mThreadPolicyMask & 32) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                startHandlingViolationException(new UnbufferedIoViolation());
            }
        }

        public void onReadFromDisk() {
            if ((this.mThreadPolicyMask & 2) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                startHandlingViolationException(new DiskReadViolation());
            }
        }

        public void onNetwork() {
            if ((this.mThreadPolicyMask & 4) != 0) {
                if ((this.mThreadPolicyMask & 33554432) != 0) {
                    throw new NetworkOnMainThreadException();
                } else if (!StrictMode.tooManyViolationsThisLoop()) {
                    startHandlingViolationException(new NetworkViolation());
                }
            }
        }

        public void onExplicitGc() {
            if ((this.mThreadPolicyMask & 64) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                startHandlingViolationException(new ExplicitGcViolation());
            }
        }

        public int getThreadPolicyMask() {
            return this.mThreadPolicyMask;
        }

        public void setThreadPolicyMask(int threadPolicyMask) {
            this.mThreadPolicyMask = threadPolicyMask;
        }

        /* access modifiers changed from: package-private */
        public void startHandlingViolationException(Violation e) {
            ViolationInfo info = new ViolationInfo(e, this.mThreadPolicyMask & -65536);
            info.violationUptimeMillis = SystemClock.uptimeMillis();
            handleViolationWithTimingAttempt(info);
        }

        /* access modifiers changed from: package-private */
        public void handleViolationWithTimingAttempt(ViolationInfo info) {
            if (Looper.myLooper() == null || info.mPenaltyMask == 268435456) {
                info.durationMillis = -1;
                onThreadPolicyViolation(info);
                return;
            }
            ArrayList<ViolationInfo> records = (ArrayList) StrictMode.violationsBeingTimed.get();
            if (records.size() < 10) {
                records.add(info);
                if (records.size() <= 1) {
                    IWindowManager windowManager = info.penaltyEnabled(134217728) ? (IWindowManager) StrictMode.sWindowManager.get() : null;
                    if (windowManager != null) {
                        try {
                            windowManager.showStrictModeViolation(true);
                        } catch (RemoteException e) {
                        }
                    }
                    ((Handler) StrictMode.THREAD_HANDLER.get()).postAtFrontOfQueue(new Runnable(windowManager, records) {
                        private final /* synthetic */ IWindowManager f$1;
                        private final /* synthetic */ ArrayList f$2;

                        {
                            this.f$1 = r2;
                            this.f$2 = r3;
                        }

                        public final void run() {
                            StrictMode.AndroidBlockGuardPolicy.lambda$handleViolationWithTimingAttempt$0(StrictMode.AndroidBlockGuardPolicy.this, this.f$1, this.f$2);
                        }
                    });
                }
            }
        }

        public static /* synthetic */ void lambda$handleViolationWithTimingAttempt$0(AndroidBlockGuardPolicy androidBlockGuardPolicy, IWindowManager windowManager, ArrayList records) {
            long loopFinishTime = SystemClock.uptimeMillis();
            if (windowManager != null) {
                try {
                    windowManager.showStrictModeViolation(false);
                } catch (RemoteException e) {
                }
            }
            for (int n = 0; n < records.size(); n++) {
                ViolationInfo v = (ViolationInfo) records.get(n);
                v.violationNumThisLoop = n + 1;
                v.durationMillis = (int) (loopFinishTime - v.violationUptimeMillis);
                androidBlockGuardPolicy.onThreadPolicyViolation(v);
            }
            records.clear();
        }

        /* access modifiers changed from: package-private */
        public void onThreadPolicyViolation(ViolationInfo info) {
            ViolationInfo violationInfo = info;
            if (StrictMode.LOG_V) {
                Log.d(StrictMode.TAG, "onThreadPolicyViolation; penalty=" + info.mPenaltyMask);
            }
            boolean justDropBox = true;
            if (violationInfo.penaltyEnabled(Integer.MIN_VALUE)) {
                ArrayList<ViolationInfo> violations = (ArrayList) StrictMode.gatheredViolations.get();
                if (violations == null) {
                    violations = new ArrayList<>(1);
                    StrictMode.gatheredViolations.set(violations);
                }
                Iterator<ViolationInfo> it = violations.iterator();
                while (it.hasNext()) {
                    if (info.getStackTrace().equals(it.next().getStackTrace())) {
                        return;
                    }
                }
                violations.add(violationInfo);
                return;
            }
            Integer crashFingerprint = Integer.valueOf(info.hashCode());
            long lastViolationTime = 0;
            if (this.mLastViolationTime != null) {
                Long vtime = this.mLastViolationTime.get(crashFingerprint);
                if (vtime != null) {
                    lastViolationTime = vtime.longValue();
                }
            } else {
                this.mLastViolationTime = new ArrayMap<>(1);
            }
            long now = SystemClock.uptimeMillis();
            this.mLastViolationTime.put(crashFingerprint, Long.valueOf(now));
            long timeSinceLastViolationMillis = lastViolationTime == 0 ? Long.MAX_VALUE : now - lastViolationTime;
            if (violationInfo.penaltyEnabled(1073741824) && timeSinceLastViolationMillis > 1000) {
                StrictMode.sLogger.log(violationInfo);
            }
            Violation violation = info.mViolation;
            int penaltyMask = 0;
            if (violationInfo.penaltyEnabled(536870912) && timeSinceLastViolationMillis > 30000) {
                penaltyMask = 0 | 536870912;
            }
            if (violationInfo.penaltyEnabled(67108864) && lastViolationTime == 0) {
                penaltyMask |= 67108864;
            }
            int penaltyMask2 = penaltyMask;
            if (penaltyMask2 != 0) {
                if (info.mPenaltyMask != 67108864) {
                    justDropBox = false;
                }
                if (justDropBox) {
                    StrictMode.dropboxViolationAsync(penaltyMask2, violationInfo);
                } else {
                    StrictMode.handleApplicationStrictModeViolation(penaltyMask2, violationInfo);
                }
            }
            if (!violationInfo.penaltyEnabled(268435456)) {
                OnThreadViolationListener listener = (OnThreadViolationListener) StrictMode.sThreadViolationListener.get();
                Executor executor = (Executor) StrictMode.sThreadViolationExecutor.get();
                if (listener != null && executor != null) {
                    try {
                        executor.execute(new Runnable(violation) {
                            private final /* synthetic */ Violation f$1;

                            {
                                this.f$1 = r2;
                            }

                            public final void run() {
                                StrictMode.AndroidBlockGuardPolicy.lambda$onThreadPolicyViolation$1(StrictMode.OnThreadViolationListener.this, this.f$1);
                            }
                        });
                    } catch (RejectedExecutionException e) {
                        Log.e(StrictMode.TAG, "ThreadPolicy penaltyCallback failed", e);
                    }
                }
            } else {
                throw new RuntimeException("StrictMode ThreadPolicy violation", violation);
            }
        }

        static /* synthetic */ void lambda$onThreadPolicyViolation$1(OnThreadViolationListener listener, Violation violation) {
            ThreadPolicy oldPolicy = StrictMode.allowThreadViolations();
            try {
                listener.onThreadViolation(violation);
            } finally {
                StrictMode.setThreadPolicy(oldPolicy);
            }
        }
    }

    /* access modifiers changed from: private */
    public static void dropboxViolationAsync(int penaltyMask, ViolationInfo info) {
        int outstanding = sDropboxCallsInFlight.incrementAndGet();
        if (outstanding > 20) {
            sDropboxCallsInFlight.decrementAndGet();
            return;
        }
        if (LOG_V) {
            Log.d(TAG, "Dropboxing async; in-flight=" + outstanding);
        }
        BackgroundThread.getHandler().post(new Runnable(penaltyMask, info) {
            private final /* synthetic */ int f$0;
            private final /* synthetic */ StrictMode.ViolationInfo f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            public final void run() {
                StrictMode.lambda$dropboxViolationAsync$2(this.f$0, this.f$1);
            }
        });
    }

    static /* synthetic */ void lambda$dropboxViolationAsync$2(int penaltyMask, ViolationInfo info) {
        handleApplicationStrictModeViolation(penaltyMask, info);
        int outstandingInner = sDropboxCallsInFlight.decrementAndGet();
        if (LOG_V) {
            Log.d(TAG, "Dropbox complete; in-flight=" + outstandingInner);
        }
    }

    /* access modifiers changed from: private */
    public static void handleApplicationStrictModeViolation(int penaltyMask, ViolationInfo info) {
        int oldMask = getThreadPolicyMask();
        try {
            setThreadPolicyMask(0);
            IActivityManager am = ActivityManager.getService();
            if (am == null) {
                Log.w(TAG, "No activity manager; failed to Dropbox violation.");
            } else {
                am.handleApplicationStrictModeViolation(RuntimeInit.getApplicationObject(), penaltyMask, info);
            }
        } catch (RemoteException e) {
            if (!(e instanceof DeadObjectException)) {
                Log.e(TAG, "RemoteException handling StrictMode violation", e);
            }
        } catch (Throwable th) {
            setThreadPolicyMask(oldMask);
            throw th;
        }
        setThreadPolicyMask(oldMask);
    }

    private static class AndroidCloseGuardReporter implements CloseGuard.Reporter {
        private AndroidCloseGuardReporter() {
        }

        public void report(String message, Throwable allocationSite) {
            StrictMode.onVmPolicyViolation(new LeakedClosableViolation(message, allocationSite));
        }
    }

    static boolean hasGatheredViolations() {
        return gatheredViolations.get() != null;
    }

    static void clearGatheredViolations() {
        gatheredViolations.set((Object) null);
    }

    public static void conditionallyCheckInstanceCounts() {
        VmPolicy policy = getVmPolicy();
        int policySize = policy.classInstanceLimit.size();
        if (policySize != 0) {
            System.gc();
            System.runFinalization();
            System.gc();
            Class[] classes = (Class[]) policy.classInstanceLimit.keySet().toArray(new Class[policySize]);
            long[] instanceCounts = VMDebug.countInstancesOfClasses(classes, false);
            for (int i = 0; i < classes.length; i++) {
                Class klass = classes[i];
                int limit = policy.classInstanceLimit.get(klass).intValue();
                long instances = instanceCounts[i];
                if (instances > ((long) limit)) {
                    onVmPolicyViolation(new InstanceCountViolation(klass, instances, limit));
                }
            }
        }
    }

    public static void setVmPolicy(VmPolicy policy) {
        synchronized (StrictMode.class) {
            sVmPolicy = policy;
            setCloseGuardEnabled(vmClosableObjectLeaksEnabled());
            Looper looper = Looper.getMainLooper();
            if (looper != null) {
                MessageQueue mq = looper.mQueue;
                if (policy.classInstanceLimit.size() != 0) {
                    if ((sVmPolicy.mask & -65536) != 0) {
                        if (!sIsIdlerRegistered) {
                            mq.addIdleHandler(sProcessIdleHandler);
                            sIsIdlerRegistered = true;
                        }
                    }
                }
                mq.removeIdleHandler(sProcessIdleHandler);
                sIsIdlerRegistered = false;
            }
            int networkPolicy = 0;
            if ((sVmPolicy.mask & 64) != 0) {
                if ((sVmPolicy.mask & 268435456) == 0) {
                    if ((sVmPolicy.mask & 16777216) == 0) {
                        networkPolicy = 1;
                    }
                }
                networkPolicy = 2;
            }
            INetworkManagementService netd = INetworkManagementService.Stub.asInterface(ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE));
            if (netd != null) {
                try {
                    netd.setUidCleartextNetworkPolicy(Process.myUid(), networkPolicy);
                } catch (RemoteException e) {
                }
            } else if (networkPolicy != 0) {
                Log.w(TAG, "Dropping requested network policy due to missing service!");
            }
            if ((sVmPolicy.mask & 512) != 0) {
                VMRuntime.setNonSdkApiUsageConsumer(sNonSdkApiUsageConsumer);
                VMRuntime.setDedupeHiddenApiWarnings(false);
            } else {
                VMRuntime.setNonSdkApiUsageConsumer((Consumer) null);
                VMRuntime.setDedupeHiddenApiWarnings(true);
            }
            setBlockGuardVmPolicy(sVmPolicy.mask);
        }
    }

    public static VmPolicy getVmPolicy() {
        VmPolicy vmPolicy;
        synchronized (StrictMode.class) {
            vmPolicy = sVmPolicy;
        }
        return vmPolicy;
    }

    public static void enableDefaults() {
        setThreadPolicy(new ThreadPolicy.Builder().detectAll().penaltyLog().build());
        setVmPolicy(new VmPolicy.Builder().detectAll().penaltyLog().build());
    }

    public static boolean vmSqliteObjectLeaksEnabled() {
        return (sVmPolicy.mask & 1) != 0;
    }

    public static boolean vmClosableObjectLeaksEnabled() {
        return (sVmPolicy.mask & 2) != 0;
    }

    public static boolean vmRegistrationLeaksEnabled() {
        return (sVmPolicy.mask & 16) != 0;
    }

    public static boolean vmFileUriExposureEnabled() {
        return (sVmPolicy.mask & 32) != 0;
    }

    public static boolean vmCleartextNetworkEnabled() {
        return (sVmPolicy.mask & 64) != 0;
    }

    public static boolean vmContentUriWithoutPermissionEnabled() {
        return (sVmPolicy.mask & 128) != 0;
    }

    public static boolean vmUntaggedSocketEnabled() {
        return (sVmPolicy.mask & 256) != 0;
    }

    public static boolean vmImplicitDirectBootEnabled() {
        return (sVmPolicy.mask & 1024) != 0;
    }

    public static boolean vmCredentialProtectedWhileLockedEnabled() {
        return (sVmPolicy.mask & 2048) != 0;
    }

    public static void onSqliteObjectLeaked(String message, Throwable originStack) {
        onVmPolicyViolation(new SqliteObjectLeakedViolation(message, originStack));
    }

    @UnsupportedAppUsage
    public static void onWebViewMethodCalledOnWrongThread(Throwable originStack) {
        onVmPolicyViolation(new WebViewMethodCalledOnWrongThreadViolation(originStack));
    }

    public static void onIntentReceiverLeaked(Throwable originStack) {
        onVmPolicyViolation(new IntentReceiverLeakedViolation(originStack));
    }

    public static void onServiceConnectionLeaked(Throwable originStack) {
        onVmPolicyViolation(new ServiceConnectionLeakedViolation(originStack));
    }

    public static void onFileUriExposed(Uri uri, String location) {
        String message = uri + " exposed beyond app through " + location;
        if ((sVmPolicy.mask & 8388608) == 0) {
            onVmPolicyViolation(new FileUriExposedViolation(message));
            return;
        }
        throw new FileUriExposedException(message);
    }

    public static void onContentUriWithoutPermission(Uri uri, String location) {
        onVmPolicyViolation(new ContentUriWithoutPermissionViolation(uri, location));
    }

    public static void onCleartextNetworkDetected(byte[] firstPacket) {
        byte[] rawAddr = null;
        boolean forceDeath = false;
        if (firstPacket != null) {
            if (firstPacket.length >= 20 && (firstPacket[0] & 240) == 64) {
                rawAddr = new byte[4];
                System.arraycopy(firstPacket, 16, rawAddr, 0, 4);
            } else if (firstPacket.length >= 40 && (firstPacket[0] & 240) == 96) {
                rawAddr = new byte[16];
                System.arraycopy(firstPacket, 24, rawAddr, 0, 16);
            }
        }
        String msg = "Detected cleartext network traffic from UID " + Process.myUid();
        if (rawAddr != null) {
            try {
                msg = msg + " to " + InetAddress.getByAddress(rawAddr);
            } catch (UnknownHostException e) {
            }
        }
        String msg2 = msg + HexDump.dumpHexString(firstPacket).trim() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        if ((sVmPolicy.mask & 16777216) != 0) {
            forceDeath = true;
        }
        onVmPolicyViolation(new CleartextNetworkViolation(msg2), forceDeath);
    }

    public static void onUntaggedSocket() {
        onVmPolicyViolation(new UntaggedSocketViolation());
    }

    public static void onImplicitDirectBoot() {
        onVmPolicyViolation(new ImplicitDirectBootViolation());
    }

    private static boolean isUserKeyUnlocked(int userId) {
        IStorageManager storage = IStorageManager.Stub.asInterface(ServiceManager.getService("mount"));
        if (storage == null) {
            return false;
        }
        try {
            return storage.isUserKeyUnlocked(userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public static void onCredentialProtectedPathAccess(String path, int userId) {
        if (userId == UserHandle.myUserId()) {
            if (!sUserKeyUnlocked) {
                if (isUserKeyUnlocked(userId)) {
                    sUserKeyUnlocked = true;
                    return;
                }
            } else {
                return;
            }
        } else if (isUserKeyUnlocked(userId)) {
            return;
        }
        onVmPolicyViolation(new CredentialProtectedWhileLockedViolation("Accessed credential protected path " + path + " while user " + userId + " was locked"));
    }

    public static void onVmPolicyViolation(Violation originStack) {
        onVmPolicyViolation(originStack, false);
    }

    public static void onVmPolicyViolation(Violation violation, boolean forceDeath) {
        Violation violation2 = violation;
        boolean penaltyLog = true;
        boolean penaltyDropbox = (sVmPolicy.mask & 67108864) != 0;
        boolean penaltyDeath = (sVmPolicy.mask & 268435456) != 0 || forceDeath;
        if ((sVmPolicy.mask & 1073741824) == 0) {
            penaltyLog = false;
        }
        ViolationInfo info = new ViolationInfo(violation2, -65536 & sVmPolicy.mask);
        info.numAnimationsRunning = 0;
        info.tags = null;
        info.broadcastIntentAction = null;
        Integer fingerprint = Integer.valueOf(info.hashCode());
        long now = SystemClock.uptimeMillis();
        long timeSinceLastViolationMillis = Long.MAX_VALUE;
        synchronized (sLastVmViolationTime) {
            if (sLastVmViolationTime.containsKey(fingerprint)) {
                timeSinceLastViolationMillis = now - sLastVmViolationTime.get(fingerprint).longValue();
            }
            if (timeSinceLastViolationMillis > 1000) {
                sLastVmViolationTime.put(fingerprint, Long.valueOf(now));
            }
        }
        if (timeSinceLastViolationMillis > 1000) {
            if (penaltyLog && sLogger != null && timeSinceLastViolationMillis > 1000) {
                sLogger.log(info);
            }
            if (penaltyDropbox) {
                if (penaltyDeath) {
                    handleApplicationStrictModeViolation(67108864, info);
                } else {
                    dropboxViolationAsync(67108864, info);
                }
            }
            if (penaltyDeath) {
                System.err.println("StrictMode VmPolicy violation with POLICY_DEATH; shutting down.");
                Process.killProcess(Process.myPid());
                System.exit(10);
            }
            if (sVmPolicy.mListener != null && sVmPolicy.mCallbackExecutor != null) {
                try {
                    sVmPolicy.mCallbackExecutor.execute(new Runnable(violation2) {
                        private final /* synthetic */ Violation f$1;

                        {
                            this.f$1 = r2;
                        }

                        public final void run() {
                            StrictMode.lambda$onVmPolicyViolation$3(StrictMode.OnVmViolationListener.this, this.f$1);
                        }
                    });
                } catch (RejectedExecutionException e) {
                    Log.e(TAG, "VmPolicy penaltyCallback failed", e);
                }
            }
        }
    }

    static /* synthetic */ void lambda$onVmPolicyViolation$3(OnVmViolationListener listener, Violation violation) {
        VmPolicy oldPolicy = allowVmViolations();
        try {
            listener.onVmViolation(violation);
        } finally {
            setVmPolicy(oldPolicy);
        }
    }

    static void writeGatheredViolationsToParcel(Parcel p) {
        ArrayList<ViolationInfo> violations = gatheredViolations.get();
        if (violations == null) {
            p.writeInt(0);
        } else {
            int size = Math.min(violations.size(), 3);
            p.writeInt(size);
            for (int i = 0; i < size; i++) {
                violations.get(i).writeToParcel(p, 0);
            }
        }
        gatheredViolations.set((Object) null);
    }

    static void readAndHandleBinderCallViolations(Parcel p) {
        Throwable localCallSite = new Throwable();
        boolean currentlyGathering = (Integer.MIN_VALUE & getThreadPolicyMask()) != 0;
        int size = p.readInt();
        for (int i = 0; i < size; i++) {
            ViolationInfo info = new ViolationInfo(p, !currentlyGathering);
            info.addLocalStack(localCallSite);
            AndroidBlockGuardPolicy threadPolicy = BlockGuard.getThreadPolicy();
            if (threadPolicy instanceof AndroidBlockGuardPolicy) {
                threadPolicy.handleViolationWithTimingAttempt(info);
            }
        }
    }

    @UnsupportedAppUsage
    private static void onBinderStrictModePolicyChange(int newPolicy) {
        setBlockGuardPolicy(newPolicy);
    }

    public static class Span {
        private final ThreadSpanState mContainerState;
        /* access modifiers changed from: private */
        public long mCreateMillis;
        /* access modifiers changed from: private */
        public String mName;
        /* access modifiers changed from: private */
        public Span mNext;
        /* access modifiers changed from: private */
        public Span mPrev;

        Span(ThreadSpanState threadState) {
            this.mContainerState = threadState;
        }

        protected Span() {
            this.mContainerState = null;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0070, code lost:
            return;
         */
        @android.annotation.UnsupportedAppUsage
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void finish() {
            /*
                r4 = this;
                android.os.StrictMode$ThreadSpanState r0 = r4.mContainerState
                monitor-enter(r0)
                java.lang.String r1 = r4.mName     // Catch:{ all -> 0x0071 }
                if (r1 != 0) goto L_0x0009
                monitor-exit(r0)     // Catch:{ all -> 0x0071 }
                return
            L_0x0009:
                android.os.StrictMode$Span r1 = r4.mPrev     // Catch:{ all -> 0x0071 }
                if (r1 == 0) goto L_0x0013
                android.os.StrictMode$Span r1 = r4.mPrev     // Catch:{ all -> 0x0071 }
                android.os.StrictMode$Span r2 = r4.mNext     // Catch:{ all -> 0x0071 }
                r1.mNext = r2     // Catch:{ all -> 0x0071 }
            L_0x0013:
                android.os.StrictMode$Span r1 = r4.mNext     // Catch:{ all -> 0x0071 }
                if (r1 == 0) goto L_0x001d
                android.os.StrictMode$Span r1 = r4.mNext     // Catch:{ all -> 0x0071 }
                android.os.StrictMode$Span r2 = r4.mPrev     // Catch:{ all -> 0x0071 }
                r1.mPrev = r2     // Catch:{ all -> 0x0071 }
            L_0x001d:
                android.os.StrictMode$Span r1 = r0.mActiveHead     // Catch:{ all -> 0x0071 }
                if (r1 != r4) goto L_0x0025
                android.os.StrictMode$Span r1 = r4.mNext     // Catch:{ all -> 0x0071 }
                r0.mActiveHead = r1     // Catch:{ all -> 0x0071 }
            L_0x0025:
                int r1 = r0.mActiveSize     // Catch:{ all -> 0x0071 }
                int r1 = r1 + -1
                r0.mActiveSize = r1     // Catch:{ all -> 0x0071 }
                boolean r1 = android.os.StrictMode.LOG_V     // Catch:{ all -> 0x0071 }
                if (r1 == 0) goto L_0x0053
                java.lang.String r1 = "StrictMode"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0071 }
                r2.<init>()     // Catch:{ all -> 0x0071 }
                java.lang.String r3 = "Span finished="
                r2.append(r3)     // Catch:{ all -> 0x0071 }
                java.lang.String r3 = r4.mName     // Catch:{ all -> 0x0071 }
                r2.append(r3)     // Catch:{ all -> 0x0071 }
                java.lang.String r3 = "; size="
                r2.append(r3)     // Catch:{ all -> 0x0071 }
                int r3 = r0.mActiveSize     // Catch:{ all -> 0x0071 }
                r2.append(r3)     // Catch:{ all -> 0x0071 }
                java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0071 }
                android.util.Log.d(r1, r2)     // Catch:{ all -> 0x0071 }
            L_0x0053:
                r1 = -1
                r4.mCreateMillis = r1     // Catch:{ all -> 0x0071 }
                r1 = 0
                r4.mName = r1     // Catch:{ all -> 0x0071 }
                r4.mPrev = r1     // Catch:{ all -> 0x0071 }
                r4.mNext = r1     // Catch:{ all -> 0x0071 }
                int r1 = r0.mFreeListSize     // Catch:{ all -> 0x0071 }
                r2 = 5
                if (r1 >= r2) goto L_0x006f
                android.os.StrictMode$Span r1 = r0.mFreeListHead     // Catch:{ all -> 0x0071 }
                r4.mNext = r1     // Catch:{ all -> 0x0071 }
                r0.mFreeListHead = r4     // Catch:{ all -> 0x0071 }
                int r1 = r0.mFreeListSize     // Catch:{ all -> 0x0071 }
                int r1 = r1 + 1
                r0.mFreeListSize = r1     // Catch:{ all -> 0x0071 }
            L_0x006f:
                monitor-exit(r0)     // Catch:{ all -> 0x0071 }
                return
            L_0x0071:
                r1 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0071 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: android.os.StrictMode.Span.finish():void");
        }
    }

    private static class ThreadSpanState {
        public Span mActiveHead;
        public int mActiveSize;
        public Span mFreeListHead;
        public int mFreeListSize;

        private ThreadSpanState() {
        }
    }

    @UnsupportedAppUsage
    public static Span enterCriticalSpan(String name) {
        Span span;
        if (Build.IS_USER) {
            return NO_OP_SPAN;
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name must be non-null and non-empty");
        }
        ThreadSpanState state = sThisThreadSpanState.get();
        synchronized (state) {
            if (state.mFreeListHead != null) {
                span = state.mFreeListHead;
                state.mFreeListHead = span.mNext;
                state.mFreeListSize--;
            } else {
                span = new Span(state);
            }
            String unused = span.mName = name;
            long unused2 = span.mCreateMillis = SystemClock.uptimeMillis();
            Span unused3 = span.mNext = state.mActiveHead;
            Span unused4 = span.mPrev = null;
            state.mActiveHead = span;
            state.mActiveSize++;
            if (span.mNext != null) {
                Span unused5 = span.mNext.mPrev = span;
            }
            if (LOG_V) {
                Log.d(TAG, "Span enter=" + name + "; size=" + state.mActiveSize);
            }
        }
        return span;
    }

    public static void noteSlowCall(String name) {
        AndroidBlockGuardPolicy threadPolicy = BlockGuard.getThreadPolicy();
        if (threadPolicy instanceof AndroidBlockGuardPolicy) {
            threadPolicy.onCustomSlowCall(name);
        }
    }

    public static void noteResourceMismatch(Object tag) {
        AndroidBlockGuardPolicy threadPolicy = BlockGuard.getThreadPolicy();
        if (threadPolicy instanceof AndroidBlockGuardPolicy) {
            threadPolicy.onResourceMismatch(tag);
        }
    }

    public static void noteUnbufferedIO() {
        BlockGuard.Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            policy.onUnbufferedIO();
        }
    }

    public static void noteDiskRead() {
        BlockGuard.Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            policy.onReadFromDisk();
        }
    }

    public static void noteDiskWrite() {
        BlockGuard.Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            policy.onWriteToDisk();
        }
    }

    public static Object trackActivity(Object instance) {
        return new InstanceTracker(instance);
    }

    @UnsupportedAppUsage
    public static void incrementExpectedActivityCount(Class klass) {
        if (klass != null) {
            synchronized (StrictMode.class) {
                if ((sVmPolicy.mask & 4) != 0) {
                    Integer expected = sExpectedActivityInstanceCount.get(klass);
                    int i = 1;
                    if (expected != null) {
                        i = 1 + expected.intValue();
                    }
                    sExpectedActivityInstanceCount.put(klass, Integer.valueOf(i));
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0042, code lost:
        if (android.os.StrictMode.InstanceTracker.getInstanceCount(r6) > r3) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0044, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0045, code lost:
        java.lang.System.gc();
        java.lang.System.runFinalization();
        java.lang.System.gc();
        r1 = dalvik.system.VMDebug.countInstancesOfClass(r6, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0055, code lost:
        if (r1 <= ((long) r3)) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0057, code lost:
        onVmPolicyViolation(new android.os.strictmode.InstanceCountViolation(r6, r1, r3));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        return;
     */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x002c  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0032  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void decrementExpectedActivityCount(java.lang.Class r6) {
        /*
            if (r6 != 0) goto L_0x0003
            return
        L_0x0003:
            java.lang.Class<android.os.StrictMode> r0 = android.os.StrictMode.class
            monitor-enter(r0)
            android.os.StrictMode$VmPolicy r1 = sVmPolicy     // Catch:{ all -> 0x0060 }
            int r1 = r1.mask     // Catch:{ all -> 0x0060 }
            r1 = r1 & 4
            if (r1 != 0) goto L_0x0010
            monitor-exit(r0)     // Catch:{ all -> 0x0060 }
            return
        L_0x0010:
            java.util.HashMap<java.lang.Class, java.lang.Integer> r1 = sExpectedActivityInstanceCount     // Catch:{ all -> 0x0060 }
            java.lang.Object r1 = r1.get(r6)     // Catch:{ all -> 0x0060 }
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ all -> 0x0060 }
            r2 = 0
            if (r1 == 0) goto L_0x0029
            int r3 = r1.intValue()     // Catch:{ all -> 0x0060 }
            if (r3 != 0) goto L_0x0022
            goto L_0x0029
        L_0x0022:
            int r3 = r1.intValue()     // Catch:{ all -> 0x0060 }
            int r3 = r3 + -1
            goto L_0x002a
        L_0x0029:
            r3 = r2
        L_0x002a:
            if (r3 != 0) goto L_0x0032
            java.util.HashMap<java.lang.Class, java.lang.Integer> r4 = sExpectedActivityInstanceCount     // Catch:{ all -> 0x0060 }
            r4.remove(r6)     // Catch:{ all -> 0x0060 }
            goto L_0x003b
        L_0x0032:
            java.util.HashMap<java.lang.Class, java.lang.Integer> r4 = sExpectedActivityInstanceCount     // Catch:{ all -> 0x0060 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x0060 }
            r4.put(r6, r5)     // Catch:{ all -> 0x0060 }
        L_0x003b:
            int r3 = r3 + 1
            monitor-exit(r0)     // Catch:{ all -> 0x0060 }
            int r0 = android.os.StrictMode.InstanceTracker.getInstanceCount(r6)
            if (r0 > r3) goto L_0x0045
            return
        L_0x0045:
            java.lang.System.gc()
            java.lang.System.runFinalization()
            java.lang.System.gc()
            long r1 = dalvik.system.VMDebug.countInstancesOfClass(r6, r2)
            long r4 = (long) r3
            int r4 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r4 <= 0) goto L_0x005f
            android.os.strictmode.InstanceCountViolation r4 = new android.os.strictmode.InstanceCountViolation
            r4.<init>(r6, r1, r3)
            onVmPolicyViolation(r4)
        L_0x005f:
            return
        L_0x0060:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0060 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.StrictMode.decrementExpectedActivityCount(java.lang.Class):void");
    }

    public static final class ViolationInfo implements Parcelable {
        public static final Parcelable.Creator<ViolationInfo> CREATOR = new Parcelable.Creator<ViolationInfo>() {
            public ViolationInfo createFromParcel(Parcel in) {
                return new ViolationInfo(in);
            }

            public ViolationInfo[] newArray(int size) {
                return new ViolationInfo[size];
            }
        };
        public String broadcastIntentAction;
        public int durationMillis;
        private final Deque<StackTraceElement[]> mBinderStack;
        /* access modifiers changed from: private */
        public final int mPenaltyMask;
        private String mStackTrace;
        /* access modifiers changed from: private */
        public final Violation mViolation;
        public int numAnimationsRunning;
        public long numInstances;
        public String[] tags;
        public int violationNumThisLoop;
        public long violationUptimeMillis;

        ViolationInfo(Violation tr, int penaltyMask) {
            this.mBinderStack = new ArrayDeque();
            this.durationMillis = -1;
            int index = 0;
            this.numAnimationsRunning = 0;
            this.numInstances = -1;
            this.mViolation = tr;
            this.mPenaltyMask = penaltyMask;
            this.violationUptimeMillis = SystemClock.uptimeMillis();
            this.numAnimationsRunning = ValueAnimator.getCurrentAnimationsCount();
            Intent broadcastIntent = ActivityThread.getIntentBeingBroadcast();
            if (broadcastIntent != null) {
                this.broadcastIntentAction = broadcastIntent.getAction();
            }
            ThreadSpanState state = (ThreadSpanState) StrictMode.sThisThreadSpanState.get();
            if (tr instanceof InstanceCountViolation) {
                this.numInstances = ((InstanceCountViolation) tr).getNumberOfInstances();
            }
            synchronized (state) {
                int spanActiveCount = state.mActiveSize;
                spanActiveCount = spanActiveCount > 20 ? 20 : spanActiveCount;
                if (spanActiveCount != 0) {
                    this.tags = new String[spanActiveCount];
                    for (Span iter = state.mActiveHead; iter != null && index < spanActiveCount; iter = iter.mNext) {
                        this.tags[index] = iter.mName;
                        index++;
                    }
                }
            }
        }

        public String getStackTrace() {
            if (this.mStackTrace == null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new FastPrintWriter((Writer) sw, false, 256);
                this.mViolation.printStackTrace(pw);
                for (StackTraceElement[] traces : this.mBinderStack) {
                    pw.append("# via Binder call with stack:\n");
                    for (StackTraceElement traceElement : r2.next()) {
                        pw.append("\tat ");
                        pw.append(traceElement.toString());
                        pw.append(10);
                    }
                }
                pw.flush();
                pw.close();
                this.mStackTrace = sw.toString();
            }
            return this.mStackTrace;
        }

        public Class<? extends Violation> getViolationClass() {
            return this.mViolation.getClass();
        }

        public String getViolationDetails() {
            return this.mViolation.getMessage();
        }

        /* access modifiers changed from: package-private */
        public boolean penaltyEnabled(int p) {
            return (this.mPenaltyMask & p) != 0;
        }

        /* access modifiers changed from: package-private */
        public void addLocalStack(Throwable t) {
            this.mBinderStack.addFirst(t.getStackTrace());
        }

        public int hashCode() {
            int result = 17;
            if (this.mViolation != null) {
                result = (17 * 37) + this.mViolation.hashCode();
            }
            if (this.numAnimationsRunning != 0) {
                result *= 37;
            }
            if (this.broadcastIntentAction != null) {
                result = (result * 37) + this.broadcastIntentAction.hashCode();
            }
            if (this.tags != null) {
                for (String tag : this.tags) {
                    result = (result * 37) + tag.hashCode();
                }
            }
            return result;
        }

        public ViolationInfo(Parcel in) {
            this(in, false);
        }

        public ViolationInfo(Parcel in, boolean unsetGatheringBit) {
            this.mBinderStack = new ArrayDeque();
            this.durationMillis = -1;
            this.numAnimationsRunning = 0;
            this.numInstances = -1;
            this.mViolation = (Violation) in.readSerializable();
            int binderStackSize = in.readInt();
            for (int i = 0; i < binderStackSize; i++) {
                StackTraceElement[] traceElements = new StackTraceElement[in.readInt()];
                for (int j = 0; j < traceElements.length; j++) {
                    traceElements[j] = new StackTraceElement(in.readString(), in.readString(), in.readString(), in.readInt());
                }
                this.mBinderStack.add(traceElements);
            }
            int rawPenaltyMask = in.readInt();
            if (unsetGatheringBit) {
                this.mPenaltyMask = Integer.MAX_VALUE & rawPenaltyMask;
            } else {
                this.mPenaltyMask = rawPenaltyMask;
            }
            this.durationMillis = in.readInt();
            this.violationNumThisLoop = in.readInt();
            this.numAnimationsRunning = in.readInt();
            this.violationUptimeMillis = in.readLong();
            this.numInstances = in.readLong();
            this.broadcastIntentAction = in.readString();
            this.tags = in.readStringArray();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeSerializable(this.mViolation);
            dest.writeInt(this.mBinderStack.size());
            for (StackTraceElement[] traceElements : this.mBinderStack) {
                dest.writeInt(traceElements.length);
                for (StackTraceElement element : traceElements) {
                    dest.writeString(element.getClassName());
                    dest.writeString(element.getMethodName());
                    dest.writeString(element.getFileName());
                    dest.writeInt(element.getLineNumber());
                }
            }
            int start = dest.dataPosition();
            dest.writeInt(this.mPenaltyMask);
            dest.writeInt(this.durationMillis);
            dest.writeInt(this.violationNumThisLoop);
            dest.writeInt(this.numAnimationsRunning);
            dest.writeLong(this.violationUptimeMillis);
            dest.writeLong(this.numInstances);
            dest.writeString(this.broadcastIntentAction);
            dest.writeStringArray(this.tags);
            int dataPosition = dest.dataPosition() - start;
        }

        public void dump(Printer pw, String prefix) {
            pw.println(prefix + "stackTrace: " + getStackTrace());
            pw.println(prefix + "penalty: " + this.mPenaltyMask);
            if (this.durationMillis != -1) {
                pw.println(prefix + "durationMillis: " + this.durationMillis);
            }
            if (this.numInstances != -1) {
                pw.println(prefix + "numInstances: " + this.numInstances);
            }
            if (this.violationNumThisLoop != 0) {
                pw.println(prefix + "violationNumThisLoop: " + this.violationNumThisLoop);
            }
            if (this.numAnimationsRunning != 0) {
                pw.println(prefix + "numAnimationsRunning: " + this.numAnimationsRunning);
            }
            pw.println(prefix + "violationUptimeMillis: " + this.violationUptimeMillis);
            if (this.broadcastIntentAction != null) {
                pw.println(prefix + "broadcastIntentAction: " + this.broadcastIntentAction);
            }
            if (this.tags != null) {
                int index = 0;
                String[] strArr = this.tags;
                int length = strArr.length;
                int i = 0;
                while (i < length) {
                    String tag = strArr[i];
                    pw.println(prefix + "tag[" + index + "]: " + tag);
                    i++;
                    index++;
                }
            }
        }

        public int describeContents() {
            return 0;
        }
    }

    private static final class InstanceTracker {
        private static final HashMap<Class<?>, Integer> sInstanceCounts = new HashMap<>();
        private final Class<?> mKlass;

        public InstanceTracker(Object instance) {
            this.mKlass = instance.getClass();
            synchronized (sInstanceCounts) {
                Integer value = sInstanceCounts.get(this.mKlass);
                sInstanceCounts.put(this.mKlass, Integer.valueOf(value != null ? 1 + value.intValue() : 1));
            }
        }

        /* access modifiers changed from: protected */
        public void finalize() throws Throwable {
            try {
                synchronized (sInstanceCounts) {
                    Integer value = sInstanceCounts.get(this.mKlass);
                    if (value != null) {
                        int newValue = value.intValue() - 1;
                        if (newValue > 0) {
                            sInstanceCounts.put(this.mKlass, Integer.valueOf(newValue));
                        } else {
                            sInstanceCounts.remove(this.mKlass);
                        }
                    }
                }
                super.finalize();
            } catch (Throwable th) {
                super.finalize();
                throw th;
            }
        }

        public static int getInstanceCount(Class<?> klass) {
            int intValue;
            synchronized (sInstanceCounts) {
                Integer value = sInstanceCounts.get(klass);
                intValue = value != null ? value.intValue() : 0;
            }
            return intValue;
        }
    }
}
