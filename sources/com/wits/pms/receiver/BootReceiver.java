package com.wits.pms.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.telecom.ParcelableCallAnalytics;
import android.util.Log;
import android.util.TimedRemoteCaller;
import com.wits.pms.core.CenterControlImpl;
import com.wits.pms.core.TempControllerService;
import com.wits.pms.statuscontrol.PowerManagerApp;
import com.wits.pms.utils.AmsUtil;

public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("centerService", "started");
            if (context.getPackageManager().getLaunchIntentForPackage("com.txznet.adapter") != null) {
                try {
                    int txzStatus = PowerManagerApp.getSettingsInt("Support_TXZ");
                    Log.d("centerService", "boot start txz != null  set Support_TXZ " + txzStatus);
                    PowerManagerApp.setSettingsInt("Support_TXZ", txzStatus);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            Intent accIntent = new Intent("com.wits.ksw.ACC_ON");
            accIntent.addFlags(16777216);
            context.sendBroadcastAsUser(accIntent, UserHandle.getUserHandleForUid(context.getApplicationInfo().uid));
            long triggerAtTime = SystemClock.elapsedRealtime() + TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS;
            if (Build.DISPLAY.contains("8937")) {
                ((AlarmManager) context.getSystemService("alarm")).setRepeating(2, triggerAtTime, ParcelableCallAnalytics.MILLIS_IN_5_MINUTES, PendingIntent.getService(context, 0, new Intent(context, (Class<?>) TempControllerService.class), 134217728));
            }
        }
        "com.wits.boot.Start".equals(intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
            AmsUtil.forceStopPackage(context, "com.nng.igo.primong.igoworld");
            AmsUtil.forceStopPackage(context, "com.nng.igoprimoisrael.javaclient");
            AmsUtil.forceStopPackage(context, "com.nng.igo.primong.hun10th");
            AmsUtil.forceStopPackage(context, "com.estrongs.android.pop");
        }
        if (intent.getAction().equals("com.wits.pms.APPLY_MCU_CHECK_CAR")) {
            CenterControlImpl.getImpl().checkCarToMcu();
        }
    }
}
