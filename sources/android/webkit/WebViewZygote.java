package android.webkit;

import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.ChildZygoteProcess;
import android.os.Process;
import android.os.ZygoteProcess;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;

public class WebViewZygote {
    private static final String LOGTAG = "WebViewZygote";
    private static final Object sLock = new Object();
    @GuardedBy({"sLock"})
    private static boolean sMultiprocessEnabled = false;
    @GuardedBy({"sLock"})
    private static PackageInfo sPackage;
    @GuardedBy({"sLock"})
    private static ChildZygoteProcess sZygote;

    public static ZygoteProcess getProcess() {
        synchronized (sLock) {
            if (sZygote != null) {
                ChildZygoteProcess childZygoteProcess = sZygote;
                return childZygoteProcess;
            }
            connectToZygoteIfNeededLocked();
            ChildZygoteProcess childZygoteProcess2 = sZygote;
            return childZygoteProcess2;
        }
    }

    public static String getPackageName() {
        String str;
        synchronized (sLock) {
            str = sPackage.packageName;
        }
        return str;
    }

    public static boolean isMultiprocessEnabled() {
        boolean z;
        synchronized (sLock) {
            z = sMultiprocessEnabled && sPackage != null;
        }
        return z;
    }

    public static void setMultiprocessEnabled(boolean enabled) {
        synchronized (sLock) {
            sMultiprocessEnabled = enabled;
            if (!enabled) {
                stopZygoteLocked();
            }
        }
    }

    static void onWebViewProviderChanged(PackageInfo packageInfo) {
        synchronized (sLock) {
            sPackage = packageInfo;
            if (sMultiprocessEnabled) {
                stopZygoteLocked();
            }
        }
    }

    @GuardedBy({"sLock"})
    private static void stopZygoteLocked() {
        if (sZygote != null) {
            sZygote.close();
            Process.killProcess(sZygote.getPid());
            sZygote = null;
        }
    }

    @GuardedBy({"sLock"})
    private static void connectToZygoteIfNeededLocked() {
        if (sZygote == null) {
            if (sPackage == null) {
                Log.e(LOGTAG, "Cannot connect to zygote, no package specified");
                return;
            }
            try {
                String abi = sPackage.applicationInfo.primaryCpuAbi;
                sZygote = Process.ZYGOTE_PROCESS.startChildZygote("com.android.internal.os.WebViewZygoteInit", "webview_zygote", 1053, 1053, (int[]) null, 0, "webview_zygote", abi, TextUtils.join((CharSequence) SmsManager.REGEX_PREFIX_DELIMITER, (Object[]) Build.SUPPORTED_ABIS), (String) null, Process.FIRST_ISOLATED_UID, Integer.MAX_VALUE);
                ZygoteProcess.waitForConnectionToZygote(sZygote.getPrimarySocketAddress());
                sZygote.preloadApp(sPackage.applicationInfo, abi);
            } catch (Exception e) {
                Log.e(LOGTAG, "Error connecting to webview zygote", e);
                stopZygoteLocked();
            }
        }
    }
}
