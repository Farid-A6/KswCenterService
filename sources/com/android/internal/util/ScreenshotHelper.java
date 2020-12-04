package com.android.internal.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.UserHandle;

public class ScreenshotHelper {
    private static final String SYSUI_PACKAGE = "com.android.systemui";
    private static final String SYSUI_SCREENSHOT_ERROR_RECEIVER = "com.android.systemui.screenshot.ScreenshotServiceErrorReceiver";
    private static final String SYSUI_SCREENSHOT_SERVICE = "com.android.systemui.screenshot.TakeScreenshotService";
    private static final String TAG = "ScreenshotHelper";
    private final int SCREENSHOT_TIMEOUT_MS = 10000;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public ServiceConnection mScreenshotConnection = null;
    /* access modifiers changed from: private */
    public final Object mScreenshotLock = new Object();

    public ScreenshotHelper(Context context) {
        this.mContext = context;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x004b, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void takeScreenshot(int r14, boolean r15, boolean r16, android.os.Handler r17) {
        /*
            r13 = this;
            r8 = r13
            java.lang.Object r9 = r8.mScreenshotLock
            monitor-enter(r9)
            android.content.ServiceConnection r0 = r8.mScreenshotConnection     // Catch:{ all -> 0x004c }
            if (r0 == 0) goto L_0x000a
            monitor-exit(r9)     // Catch:{ all -> 0x004c }
            return
        L_0x000a:
            android.content.ComponentName r0 = new android.content.ComponentName     // Catch:{ all -> 0x004c }
            java.lang.String r1 = "com.android.systemui"
            java.lang.String r2 = "com.android.systemui.screenshot.TakeScreenshotService"
            r0.<init>((java.lang.String) r1, (java.lang.String) r2)     // Catch:{ all -> 0x004c }
            android.content.Intent r1 = new android.content.Intent     // Catch:{ all -> 0x004c }
            r1.<init>()     // Catch:{ all -> 0x004c }
            r10 = r1
            com.android.internal.util.ScreenshotHelper$1 r1 = new com.android.internal.util.ScreenshotHelper$1     // Catch:{ all -> 0x004c }
            r1.<init>()     // Catch:{ all -> 0x004c }
            r11 = r1
            r10.setComponent(r0)     // Catch:{ all -> 0x004c }
            com.android.internal.util.ScreenshotHelper$2 r12 = new com.android.internal.util.ScreenshotHelper$2     // Catch:{ all -> 0x004c }
            r1 = r12
            r2 = r13
            r3 = r14
            r4 = r17
            r5 = r11
            r6 = r15
            r7 = r16
            r1.<init>(r3, r4, r5, r6, r7)     // Catch:{ all -> 0x004c }
            r1 = r12
            android.content.Context r2 = r8.mContext     // Catch:{ all -> 0x004c }
            r3 = 33554433(0x2000001, float:9.403956E-38)
            android.os.UserHandle r4 = android.os.UserHandle.CURRENT     // Catch:{ all -> 0x004c }
            boolean r2 = r2.bindServiceAsUser(r10, r1, r3, r4)     // Catch:{ all -> 0x004c }
            if (r2 == 0) goto L_0x0048
            r8.mScreenshotConnection = r1     // Catch:{ all -> 0x004c }
            r2 = 10000(0x2710, double:4.9407E-320)
            r4 = r17
            r4.postDelayed(r11, r2)     // Catch:{ all -> 0x0051 }
            goto L_0x004a
        L_0x0048:
            r4 = r17
        L_0x004a:
            monitor-exit(r9)     // Catch:{ all -> 0x0051 }
            return
        L_0x004c:
            r0 = move-exception
            r4 = r17
        L_0x004f:
            monitor-exit(r9)     // Catch:{ all -> 0x0051 }
            throw r0
        L_0x0051:
            r0 = move-exception
            goto L_0x004f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.util.ScreenshotHelper.takeScreenshot(int, boolean, boolean, android.os.Handler):void");
    }

    /* access modifiers changed from: private */
    public void notifyScreenshotError() {
        ComponentName errorComponent = new ComponentName(SYSUI_PACKAGE, SYSUI_SCREENSHOT_ERROR_RECEIVER);
        Intent errorIntent = new Intent(Intent.ACTION_USER_PRESENT);
        errorIntent.setComponent(errorComponent);
        errorIntent.addFlags(335544320);
        this.mContext.sendBroadcastAsUser(errorIntent, UserHandle.CURRENT);
    }
}
