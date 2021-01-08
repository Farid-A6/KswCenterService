package com.wits.pms.core;

import android.app.ActivityManager;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothHidHost;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiScanner;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.telephony.PreciseDisconnectCause;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.internal.logging.nano.MetricsProto;
import com.wits.pms.ICmdListener;
import com.wits.pms.R;
import com.wits.pms.bean.AutoKitMessage;
import com.wits.pms.bean.EcarMessage;
import com.wits.pms.bean.TxzMessage;
import com.wits.pms.bean.ZlinkMessage;
import com.wits.pms.custom.BtController;
import com.wits.pms.custom.KswFunction;
import com.wits.pms.custom.KswSettings;
import com.wits.pms.interfaces.CenterControl;
import com.wits.pms.interfaces.LogicSystem;
import com.wits.pms.interfaces.MemoryKiller;
import com.wits.pms.mcu.McuSender;
import com.wits.pms.mcu.custom.KswMcuLogic;
import com.wits.pms.mcu.custom.KswMcuSender;
import com.wits.pms.mcu.custom.KswMessage;
import com.wits.pms.mcu.custom.McuCommandListener;
import com.wits.pms.mcu.custom.utils.SplashFlasher;
import com.wits.pms.mcu.custom.utils.UpdateHelper;
import com.wits.pms.receiver.AutoKitCallBackImpl;
import com.wits.pms.receiver.CenterCallBackImpl;
import com.wits.pms.receiver.ReceiverHandler;
import com.wits.pms.statuscontrol.BtPhoneStatus;
import com.wits.pms.statuscontrol.McuStatus;
import com.wits.pms.statuscontrol.PowerManagerApp;
import com.wits.pms.statuscontrol.WitsCommand;
import com.wits.pms.utils.AmsUtil;
import com.wits.pms.utils.KeyUtils;
import com.wits.pms.utils.SystemProperties;
import com.wits.pms.utils.Test;
import com.wits.pms.utils.UsbUtil;
import java.io.File;
import java.util.Set;

public class CenterControlImpl extends ICmdListener.Stub implements CenterControl, KswFunction {
    static final String TAG = "CenterControl";
    private static CenterControlImpl mCenterControl;
    private int beforeMuteVolume;
    private AlertDialog installAppDialog;
    private ActivityManager mActivityManager;
    private final AudioManager mAudioManager;
    /* access modifiers changed from: private */
    public Context mContext;
    private final Handler mHandler;
    private LogicSystem mLogicSystem;
    private McuSender mMcuSender;
    private MemoryKiller mMemoryKiller;
    private boolean muteStatus;
    int originMode;
    private ProgressBar progressBar;
    private int[] sourceArray = {1, 2, 3, 5, 6, 9};
    private boolean statusChange;
    private Runnable stopNaviProtocolRunnable = $$Lambda$CenterControlImpl$23q0S7nyA9AiGRMC2fProAbCiZ0.INSTANCE;

    public CenterControlImpl(Context context) {
        this.mContext = context;
        this.mActivityManager = (ActivityManager) this.mContext.getSystemService(Context.ACTIVITY_SERVICE);
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mHandler = new Handler(context.getMainLooper());
        PowerManagerApp.registerICmdListener(this);
        startTxzService();
        Test.test();
    }

    public static void init(Context context) {
        if (context != null) {
            mCenterControl = new CenterControlImpl(context);
            CenterCallBackImpl.init(context);
            AutoKitCallBackImpl.init(context);
            ReceiverHandler.init(context);
        }
    }

    public static KswMcuSender getMcuSender() {
        return KswMcuSender.getSender();
    }

    public static CenterControlImpl getImpl() {
        if (mCenterControl == null) {
            init(PowerManagerAppService.serviceContext);
        }
        return mCenterControl;
    }

    public void setMemoryKiller(MemoryKiller memoryKiller) {
        this.mMemoryKiller = memoryKiller;
    }

    public boolean isAppInstalled(String uri) {
        try {
            this.mContext.getPackageManager().getPackageInfo(uri, 1);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:156:0x02c1, code lost:
        if (r0.equals("music.prev") != false) goto L_0x02e6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00b2, code lost:
        if (r0.equals("music.continue") != false) goto L_0x00b6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handle(com.wits.pms.bean.TxzMessage r19) {
        /*
            r18 = this;
            r1 = r18
            r2 = r19
            java.lang.String r3 = r2.action
            int r0 = r2.keyType
            r4 = 13
            r5 = 1000(0x3e8, float:1.401E-42)
            if (r0 == r5) goto L_0x0535
            r6 = 1010(0x3f2, float:1.415E-42)
            r7 = 3
            r8 = 0
            r9 = 1
            if (r0 == r6) goto L_0x04f8
            r6 = 1020(0x3fc, float:1.43E-42)
            if (r0 == r6) goto L_0x047c
            r6 = 1030(0x406, float:1.443E-42)
            r10 = 7
            r11 = 5
            r12 = 6
            r13 = 4
            r14 = -1
            r15 = 2
            if (r0 == r6) goto L_0x03fe
            r6 = 1040(0x410, float:1.457E-42)
            r16 = 9
            r17 = 8
            if (r0 == r6) goto L_0x0245
            r4 = 1050(0x41a, float:1.471E-42)
            if (r0 == r4) goto L_0x01ed
            r4 = 1091(0x443, float:1.529E-42)
            if (r0 == r4) goto L_0x00ee
            switch(r0) {
                case 1060: goto L_0x0038;
                case 1061: goto L_0x0038;
                default: goto L_0x0036;
            }
        L_0x0036:
            goto L_0x0571
        L_0x0038:
            java.lang.String r0 = r2.action
            int r4 = r0.hashCode()
            switch(r4) {
                case -1362146736: goto L_0x00ac;
                case -825432804: goto L_0x00a2;
                case -825393037: goto L_0x0098;
                case -825367203: goto L_0x008e;
                case -825361316: goto L_0x0084;
                case -770549500: goto L_0x0079;
                case -734529399: goto L_0x006e;
                case 171428079: goto L_0x0064;
                case 183111917: goto L_0x005a;
                case 1438537900: goto L_0x004f;
                case 1461409024: goto L_0x0043;
                default: goto L_0x0041;
            }
        L_0x0041:
            goto L_0x00b5
        L_0x0043:
            java.lang.String r4 = "music.loop.all"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x00b5
            r7 = r17
            goto L_0x00b6
        L_0x004f:
            java.lang.String r4 = "music.random"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x00b5
            r7 = r10
            goto L_0x00b6
        L_0x005a:
            java.lang.String r4 = "music.pause"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x00b5
            r7 = r13
            goto L_0x00b6
        L_0x0064:
            java.lang.String r4 = "music.close"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x00b5
            r7 = r9
            goto L_0x00b6
        L_0x006e:
            java.lang.String r4 = "music.loop.single"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x00b5
            r7 = r16
            goto L_0x00b6
        L_0x0079:
            java.lang.String r4 = "music.loop.random"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x00b5
            r7 = 10
            goto L_0x00b6
        L_0x0084:
            java.lang.String r4 = "music.prev"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x00b5
            r7 = r12
            goto L_0x00b6
        L_0x008e:
            java.lang.String r4 = "music.play"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x00b5
            r7 = r15
            goto L_0x00b6
        L_0x0098:
            java.lang.String r4 = "music.open"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x00b5
            r7 = r8
            goto L_0x00b6
        L_0x00a2:
            java.lang.String r4 = "music.next"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x00b5
            r7 = r11
            goto L_0x00b6
        L_0x00ac:
            java.lang.String r4 = "music.continue"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x00b5
            goto L_0x00b6
        L_0x00b5:
            r7 = r14
        L_0x00b6:
            switch(r7) {
                case 0: goto L_0x00e8;
                case 1: goto L_0x00e4;
                case 2: goto L_0x00cd;
                case 3: goto L_0x00c9;
                case 4: goto L_0x00c5;
                case 5: goto L_0x00c1;
                case 6: goto L_0x00bd;
                case 7: goto L_0x00bc;
                case 8: goto L_0x00bb;
                case 9: goto L_0x00ba;
                default: goto L_0x00b9;
            }
        L_0x00b9:
            goto L_0x00ec
        L_0x00ba:
            goto L_0x00ec
        L_0x00bb:
            goto L_0x00ec
        L_0x00bc:
            goto L_0x00ec
        L_0x00bd:
            r18.mediaPrevious()
            goto L_0x00ec
        L_0x00c1:
            r18.mediaNext()
            goto L_0x00ec
        L_0x00c5:
            r18.mediaPause()
            goto L_0x00ec
        L_0x00c9:
            r18.mediaPlay()
            goto L_0x00ec
        L_0x00cd:
            android.os.Bundle r0 = new android.os.Bundle
            r0.<init>()
            java.lang.String r4 = "package"
            java.lang.String r6 = "com.txznet.music"
            r0.putString(r4, r6)
            com.wits.pms.bean.TxzMessage r4 = new com.wits.pms.bean.TxzMessage
            java.lang.String r6 = "app.open"
            r4.<init>(r5, r6, r0)
            r1.handle((com.wits.pms.bean.TxzMessage) r4)
            goto L_0x00ec
        L_0x00e4:
            r18.closeMusic()
            goto L_0x00ec
        L_0x00e8:
            r18.openMusic()
        L_0x00ec:
            goto L_0x0571
        L_0x00ee:
            java.lang.String r0 = "dvr.open"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0103
            com.wits.pms.custom.KswSettings r0 = com.wits.pms.custom.KswSettings.getSettings()
            java.lang.String r4 = "DVRApk_PackageName"
            java.lang.String r0 = r0.getSettingsString(r4)
            r1.openApp(r0)
        L_0x0103:
            java.lang.String r0 = "dvr.close"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0118
            com.wits.pms.custom.KswSettings r0 = com.wits.pms.custom.KswSettings.getSettings()
            java.lang.String r4 = "DVRApk_PackageName"
            java.lang.String r0 = r0.getSettingsString(r4)
            r1.closeApp(r0)
        L_0x0118:
            java.lang.String r0 = "video.open"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0128
            java.lang.String r0 = "com.txznet.music"
            r1.killApp(r0)
            r18.openVideo()
        L_0x0128:
            java.lang.String r0 = "video.close"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0133
            r18.closeVideo()
        L_0x0133:
            java.lang.String r0 = "tv.open"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x013e
            r1.openDtv(r9)
        L_0x013e:
            java.lang.String r0 = "tv.close"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0149
            r1.systemModeSwitch(r9)
        L_0x0149:
            java.lang.String r0 = "aux.open"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0154
            r1.openAux(r9)
        L_0x0154:
            java.lang.String r0 = "aux.close"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x015f
            r1.systemModeSwitch(r9)
        L_0x015f:
            java.lang.String r0 = "original_car.open"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0182
            com.wits.pms.mcu.custom.KswMcuSender r0 = com.wits.pms.mcu.custom.KswMcuSender.getSender()
            r4 = 103(0x67, float:1.44E-43)
            byte[] r5 = new byte[r9]
            r5[r8] = r8
            r0.sendMessage(r4, r5)
            com.wits.pms.mcu.custom.KswMcuSender r0 = com.wits.pms.mcu.custom.KswMcuSender.getSender()
            r4 = 105(0x69, float:1.47E-43)
            byte[] r5 = new byte[r15]
            r5 = {18, 2} // fill-array
            r0.sendMessage(r4, r5)
        L_0x0182:
            java.lang.String r0 = "mobile_internet.open"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x018f
            java.lang.String r0 = "net.easyconn"
            r1.openApp(r0)
        L_0x018f:
            java.lang.String r0 = "android.home"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x01a5
            com.wits.pms.mcu.custom.KswMcuSender r0 = com.wits.pms.mcu.custom.KswMcuSender.getSender()
            r4 = 105(0x69, float:1.47E-43)
            byte[] r5 = new byte[r15]
            r5 = {18, 1} // fill-array
            r0.sendMessage(r4, r5)
        L_0x01a5:
            java.lang.String r0 = "music.play.local"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0571
            android.os.Bundle r0 = r2.bundle
            java.lang.String r4 = "path"
            java.lang.String r4 = r0.getString(r4)
            java.lang.String r0 = "CenterControl"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "music.play.local   path = "
            r5.append(r6)
            r5.append(r4)
            java.lang.String r5 = r5.toString()
            android.util.Log.d(r0, r5)
            android.content.Intent r0 = new android.content.Intent
            r0.<init>()
            r5 = r0
            java.lang.String r0 = "com.wits.media.MUSIC"
            r5.setAction(r0)
            java.lang.String r0 = "txzPlay"
            r5.putExtra((java.lang.String) r0, (boolean) r9)
            java.lang.String r0 = "path"
            r5.putExtra((java.lang.String) r0, (java.lang.String) r4)
            r1.startAction((android.content.Intent) r5)     // Catch:{ Exception -> 0x01e5 }
            goto L_0x0571
        L_0x01e5:
            r0 = move-exception
            r6 = r0
            r0 = r6
            r0.printStackTrace()
            goto L_0x0571
        L_0x01ed:
            java.lang.String r0 = r2.action
            int r4 = r0.hashCode()
            r5 = -1480935867(0xffffffffa7bab645, float:-5.1822988E-15)
            if (r4 == r5) goto L_0x0226
            r5 = -47450874(0xfffffffffd2bf506, float:-1.4285639E37)
            if (r4 == r5) goto L_0x021c
            r5 = -47411107(0xfffffffffd2c905d, float:-1.433605E37)
            if (r4 == r5) goto L_0x0212
            r5 = -47379386(0xfffffffffd2d0c46, float:-1.4376261E37)
            if (r4 == r5) goto L_0x0208
            goto L_0x022f
        L_0x0208:
            java.lang.String r4 = "radio.prev"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x022f
            r14 = r15
            goto L_0x022f
        L_0x0212:
            java.lang.String r4 = "radio.open"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x022f
            r14 = r8
            goto L_0x022f
        L_0x021c:
            java.lang.String r4 = "radio.next"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x022f
            r14 = r7
            goto L_0x022f
        L_0x0226:
            java.lang.String r4 = "radio.close"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x022f
            r14 = r9
        L_0x022f:
            switch(r14) {
                case 0: goto L_0x023f;
                case 1: goto L_0x023b;
                case 2: goto L_0x0237;
                case 3: goto L_0x0233;
                default: goto L_0x0232;
            }
        L_0x0232:
            goto L_0x0243
        L_0x0233:
            r18.fmNext()
            goto L_0x0243
        L_0x0237:
            r18.fmPrevious()
            goto L_0x0243
        L_0x023b:
            r18.fmClose()
            goto L_0x0243
        L_0x023f:
            r18.fmOpen()
        L_0x0243:
            goto L_0x0571
        L_0x0245:
            java.lang.String r0 = r2.action
            int r5 = r0.hashCode()
            switch(r5) {
                case -1362146736: goto L_0x02da;
                case -825432804: goto L_0x02cf;
                case -825393037: goto L_0x02c4;
                case -825361316: goto L_0x02bb;
                case 19491040: goto L_0x02b1;
                case 171428079: goto L_0x02a6;
                case 179334558: goto L_0x029c;
                case 179706250: goto L_0x0292;
                case 183111917: goto L_0x0287;
                case 486135880: goto L_0x027c;
                case 685022669: goto L_0x0271;
                case 974886623: goto L_0x0266;
                case 1017115442: goto L_0x025b;
                case 1264734904: goto L_0x0250;
                default: goto L_0x024e;
            }
        L_0x024e:
            goto L_0x02e5
        L_0x0250:
            java.lang.String r4 = "bluetooth.close"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x02e5
            r4 = r9
            goto L_0x02e6
        L_0x025b:
            java.lang.String r4 = "bluetooth.status"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x02e5
            r4 = r12
            goto L_0x02e6
        L_0x0266:
            java.lang.String r4 = "bluetooth.reject"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x02e5
            r4 = r11
            goto L_0x02e6
        L_0x0271:
            java.lang.String r4 = "bluetooth.hangup"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x02e5
            r4 = r7
            goto L_0x02e6
        L_0x027c:
            java.lang.String r4 = "bluetooth.accept"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x02e5
            r4 = r13
            goto L_0x02e6
        L_0x0287:
            java.lang.String r4 = "music.pause"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x02e5
            r4 = 10
            goto L_0x02e6
        L_0x0292:
            java.lang.String r4 = "bluetooth.open"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x02e5
            r4 = r8
            goto L_0x02e6
        L_0x029c:
            java.lang.String r4 = "bluetooth.call"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x02e5
            r4 = r15
            goto L_0x02e6
        L_0x02a6:
            java.lang.String r4 = "music.close"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x02e5
            r4 = r16
            goto L_0x02e6
        L_0x02b1:
            java.lang.String r4 = "bluetooth.contact"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x02e5
            r4 = r10
            goto L_0x02e6
        L_0x02bb:
            java.lang.String r5 = "music.prev"
            boolean r0 = r0.equals(r5)
            if (r0 == 0) goto L_0x02e5
            goto L_0x02e6
        L_0x02c4:
            java.lang.String r4 = "music.open"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x02e5
            r4 = r17
            goto L_0x02e6
        L_0x02cf:
            java.lang.String r4 = "music.next"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x02e5
            r4 = 12
            goto L_0x02e6
        L_0x02da:
            java.lang.String r4 = "music.continue"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x02e5
            r4 = 11
            goto L_0x02e6
        L_0x02e5:
            r4 = r14
        L_0x02e6:
            switch(r4) {
                case 0: goto L_0x03f8;
                case 1: goto L_0x039b;
                case 2: goto L_0x0387;
                case 3: goto L_0x0383;
                case 4: goto L_0x037e;
                case 5: goto L_0x0379;
                case 6: goto L_0x0312;
                case 7: goto L_0x0309;
                case 8: goto L_0x0304;
                case 9: goto L_0x02ff;
                case 10: goto L_0x02fa;
                case 11: goto L_0x02f5;
                case 12: goto L_0x02f0;
                case 13: goto L_0x02eb;
                default: goto L_0x02e9;
            }
        L_0x02e9:
            goto L_0x03fc
        L_0x02eb:
            r18.mediaPrevious()
            goto L_0x03fc
        L_0x02f0:
            r18.mediaNext()
            goto L_0x03fc
        L_0x02f5:
            r18.mediaPlay()
            goto L_0x03fc
        L_0x02fa:
            r18.mediaPause()
            goto L_0x03fc
        L_0x02ff:
            r18.btMusicStop()
            goto L_0x03fc
        L_0x0304:
            r18.btMusicOpen()
            goto L_0x03fc
        L_0x0309:
            r0 = 200(0xc8, float:2.8E-43)
            java.lang.String r4 = ""
            com.wits.pms.statuscontrol.WitsCommand.sendCommand(r7, r0, r4)
            goto L_0x03fc
        L_0x0312:
            android.os.Bundle r0 = new android.os.Bundle
            r0.<init>()
            r4 = r0
            r5 = r8
            java.lang.String r0 = "isConnected"
            boolean r0 = com.wits.pms.statuscontrol.PowerManagerApp.getStatusBoolean(r0)     // Catch:{ RemoteException -> 0x0373 }
            r6 = 2040(0x7f8, float:2.859E-42)
            if (r0 == 0) goto L_0x0360
            java.lang.String r7 = "callStatus"
            int r7 = com.wits.pms.statuscontrol.PowerManagerApp.getStatusInt(r7)     // Catch:{ RemoteException -> 0x0373 }
            r5 = r7
            switch(r5) {
                case 4: goto L_0x0348;
                case 5: goto L_0x0340;
                case 6: goto L_0x0338;
                case 7: goto L_0x0330;
                default: goto L_0x032d;
            }     // Catch:{ RemoteException -> 0x0373 }
        L_0x032d:
            com.wits.pms.bean.TxzMessage r7 = new com.wits.pms.bean.TxzMessage     // Catch:{ RemoteException -> 0x0373 }
            goto L_0x0350
        L_0x0330:
            com.wits.pms.bean.TxzMessage r7 = new com.wits.pms.bean.TxzMessage     // Catch:{ RemoteException -> 0x0373 }
            java.lang.String r8 = "bluetooth.hangup"
            r7.<init>(r6, r8, r4)     // Catch:{ RemoteException -> 0x0373 }
            goto L_0x0355
        L_0x0338:
            com.wits.pms.bean.TxzMessage r7 = new com.wits.pms.bean.TxzMessage     // Catch:{ RemoteException -> 0x0373 }
            java.lang.String r8 = "bluetooth.offhook"
            r7.<init>(r6, r8, r4)     // Catch:{ RemoteException -> 0x0373 }
            goto L_0x0355
        L_0x0340:
            com.wits.pms.bean.TxzMessage r7 = new com.wits.pms.bean.TxzMessage     // Catch:{ RemoteException -> 0x0373 }
            java.lang.String r8 = "bluetooth.incoming"
            r7.<init>(r6, r8, r4)     // Catch:{ RemoteException -> 0x0373 }
            goto L_0x0355
        L_0x0348:
            com.wits.pms.bean.TxzMessage r7 = new com.wits.pms.bean.TxzMessage     // Catch:{ RemoteException -> 0x0373 }
            java.lang.String r8 = "bluetooth.call"
            r7.<init>(r6, r8, r4)     // Catch:{ RemoteException -> 0x0373 }
            goto L_0x0355
        L_0x0350:
            java.lang.String r8 = "bluetooth.idle"
            r7.<init>(r6, r8, r4)     // Catch:{ RemoteException -> 0x0373 }
        L_0x0355:
            if (r5 != 0) goto L_0x0367
            com.wits.pms.bean.TxzMessage r8 = new com.wits.pms.bean.TxzMessage     // Catch:{ RemoteException -> 0x0373 }
            java.lang.String r9 = "bluetooth.connect"
            r8.<init>(r6, r9, r4)     // Catch:{ RemoteException -> 0x0373 }
            r7 = r8
            goto L_0x0367
        L_0x0360:
            com.wits.pms.bean.TxzMessage r7 = new com.wits.pms.bean.TxzMessage     // Catch:{ RemoteException -> 0x0373 }
            java.lang.String r8 = "bluetooth.disconnect"
            r7.<init>(r6, r8, r4)     // Catch:{ RemoteException -> 0x0373 }
        L_0x0367:
            r6 = r7
            android.content.Context r7 = r1.mContext     // Catch:{ RemoteException -> 0x0373 }
            com.wits.pms.receiver.CenterCallBackImpl r7 = com.wits.pms.receiver.CenterCallBackImpl.getImpl(r7)     // Catch:{ RemoteException -> 0x0373 }
            r7.sendBroadCast(r6)     // Catch:{ RemoteException -> 0x0373 }
            goto L_0x03fc
        L_0x0373:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x03fc
        L_0x0379:
            r18.handUpPhone()
            goto L_0x03fc
        L_0x037e:
            r18.acceptPhone()
            goto L_0x03fc
        L_0x0383:
            r18.handUpPhone()
            goto L_0x03fc
        L_0x0387:
            android.os.Bundle r0 = r2.bundle
            java.lang.String r4 = "name"
            java.lang.String r0 = r0.getString(r4)
            android.os.Bundle r4 = r2.bundle
            java.lang.String r5 = "number"
            java.lang.String r4 = r4.getString(r5)
            r1.btPhoneCall(r0, r4)
            goto L_0x03fc
        L_0x039b:
            java.lang.String r0 = "CenterControl"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "topApp = "
            r4.append(r5)
            com.wits.pms.statuscontrol.SystemStatus r5 = com.wits.pms.core.SystemStatusControl.getStatus()
            java.lang.String r5 = r5.topApp
            r4.append(r5)
            java.lang.String r5 = "   isPlayingMusic = "
            r4.append(r5)
            com.wits.pms.interfaces.LogicSystem r5 = r1.mLogicSystem
            com.wits.pms.statuscontrol.BtPhoneStatus r5 = r5.getBtPhoneStatus()
            boolean r5 = r5.isPlayingMusic
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            android.util.Log.d(r0, r4)
            com.wits.pms.statuscontrol.SystemStatus r0 = com.wits.pms.core.SystemStatusControl.getStatus()
            java.lang.String r0 = r0.topApp
            java.lang.String r4 = "com.wits.ksw.bt"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x03e1
            android.os.Handler r0 = r1.mHandler
            com.wits.pms.core.-$$Lambda$CenterControlImpl$MU0h5QLUe0voNYR_9jssAIq6o_U r4 = new com.wits.pms.core.-$$Lambda$CenterControlImpl$MU0h5QLUe0voNYR_9jssAIq6o_U
            r4.<init>()
            r5 = 800(0x320, double:3.953E-321)
            r0.postDelayed(r4, r5)
        L_0x03e1:
            com.wits.pms.interfaces.LogicSystem r0 = r1.mLogicSystem
            com.wits.pms.statuscontrol.BtPhoneStatus r0 = r0.getBtPhoneStatus()
            boolean r0 = r0.isPlayingMusic
            if (r0 == 0) goto L_0x03fc
            android.os.Handler r0 = r1.mHandler
            com.wits.pms.core.-$$Lambda$MnsNT1fc1_cXAfpJhxBta4fOU_k r4 = new com.wits.pms.core.-$$Lambda$MnsNT1fc1_cXAfpJhxBta4fOU_k
            r4.<init>()
            r5 = 0
            r0.postDelayed(r4, r5)
            goto L_0x03fc
        L_0x03f8:
            r1.openBluetooth(r9)
        L_0x03fc:
            goto L_0x0571
        L_0x03fe:
            java.lang.String r0 = r2.action
            int r4 = r0.hashCode()
            switch(r4) {
                case -2128329265: goto L_0x044e;
                case -2128329233: goto L_0x0444;
                case -1553713362: goto L_0x043a;
                case -1553708278: goto L_0x0430;
                case -1553704816: goto L_0x0426;
                case -1553704578: goto L_0x041c;
                case -920463626: goto L_0x0412;
                case -920189843: goto L_0x0408;
                default: goto L_0x0407;
            }
        L_0x0407:
            goto L_0x0457
        L_0x0408:
            java.lang.String r4 = "volume.mute"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x0457
            r14 = r10
            goto L_0x0457
        L_0x0412:
            java.lang.String r4 = "volume.down"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x0457
            r14 = r9
            goto L_0x0457
        L_0x041c:
            java.lang.String r4 = "volume.min"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x0457
            r14 = r7
            goto L_0x0457
        L_0x0426:
            java.lang.String r4 = "volume.max"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x0457
            r14 = r15
            goto L_0x0457
        L_0x0430:
            java.lang.String r4 = "volume.inc"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x0457
            r14 = r11
            goto L_0x0457
        L_0x043a:
            java.lang.String r4 = "volume.dec"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x0457
            r14 = r12
            goto L_0x0457
        L_0x0444:
            java.lang.String r4 = "volume.up"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x0457
            r14 = r8
            goto L_0x0457
        L_0x044e:
            java.lang.String r4 = "volume.to"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x0457
            r14 = r13
        L_0x0457:
            switch(r14) {
                case 0: goto L_0x0476;
                case 1: goto L_0x0472;
                case 2: goto L_0x046e;
                case 3: goto L_0x046a;
                case 4: goto L_0x0469;
                case 5: goto L_0x0468;
                case 6: goto L_0x0467;
                case 7: goto L_0x045b;
                default: goto L_0x045a;
            }
        L_0x045a:
            goto L_0x047a
        L_0x045b:
            android.os.Bundle r0 = r2.bundle
            java.lang.String r4 = "mute"
            boolean r0 = r0.getBoolean(r4)
            r1.muteWithUI(r0)
            goto L_0x047a
        L_0x0467:
            goto L_0x047a
        L_0x0468:
            goto L_0x047a
        L_0x0469:
            goto L_0x047a
        L_0x046a:
            r18.volumeMin()
            goto L_0x047a
        L_0x046e:
            r18.volumeMax()
            goto L_0x047a
        L_0x0472:
            r18.volumeDownBySystem()
            goto L_0x047a
        L_0x0476:
            r18.volumeUpBySystem()
        L_0x047a:
            goto L_0x0571
        L_0x047c:
            android.content.Context r0 = r1.mContext
            android.content.ContentResolver r0 = r0.getContentResolver()
            java.lang.String r4 = "screen_brightness"
            r5 = 30
            int r0 = android.provider.Settings.System.getInt(r0, r4, r5)
            java.lang.String r4 = "light_up"
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L_0x049f
            int r0 = r0 + 42
            android.content.Context r4 = r1.mContext
            android.content.ContentResolver r4 = r4.getContentResolver()
            java.lang.String r6 = "screen_brightness"
            android.provider.Settings.System.putInt(r4, r6, r0)
        L_0x049f:
            java.lang.String r4 = "light_down"
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L_0x04b4
            int r0 = r0 + -42
            android.content.Context r4 = r1.mContext
            android.content.ContentResolver r4 = r4.getContentResolver()
            java.lang.String r6 = "screen_brightness"
            android.provider.Settings.System.putInt(r4, r6, r0)
        L_0x04b4:
            java.lang.String r4 = "light_min"
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L_0x04c9
            r0 = 30
            android.content.Context r4 = r1.mContext
            android.content.ContentResolver r4 = r4.getContentResolver()
            java.lang.String r6 = "screen_brightness"
            android.provider.Settings.System.putInt(r4, r6, r5)
        L_0x04c9:
            java.lang.String r4 = "light_max"
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L_0x04e0
            r0 = 255(0xff, float:3.57E-43)
            android.content.Context r4 = r1.mContext
            android.content.ContentResolver r4 = r4.getContentResolver()
            java.lang.String r5 = "screen_brightness"
            r6 = 255(0xff, float:3.57E-43)
            android.provider.Settings.System.putInt(r4, r5, r6)
        L_0x04e0:
            java.lang.String r4 = "CenterControl"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Brightness set to "
            r5.append(r6)
            r5.append(r0)
            java.lang.String r5 = r5.toString()
            android.util.Log.i(r4, r5)
            goto L_0x0571
        L_0x04f8:
            java.lang.String r0 = "wifi.open"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0505
            r1.wifiOperation(r9)
            goto L_0x0571
        L_0x0505:
            java.lang.String r0 = "wifi.close"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0511
            r1.wifiOperation(r8)
            goto L_0x0571
        L_0x0511:
            java.lang.String r0 = "go.home"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x051d
            com.wits.pms.utils.KeyUtils.pressKey(r7)
            goto L_0x0571
        L_0x051d:
            java.lang.String r0 = "screen.close"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0529
            r1.displayScreen(r8)
            goto L_0x0571
        L_0x0529:
            java.lang.String r0 = "screen.open"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0571
            r1.displayScreen(r9)
            goto L_0x0571
        L_0x0535:
            java.lang.String r0 = "app.open"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x055e
            android.os.Bundle r0 = r2.bundle
            java.lang.String r5 = "package"
            java.lang.String r0 = r0.getString(r5)
            java.lang.String r5 = "com.txznet.music"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0557
            boolean r5 = r1.isAppInstalled(r0)
            if (r5 != 0) goto L_0x0557
            r18.openMusic()
            return
        L_0x0557:
            r1.openSourceMode(r4)
            r1.openApp(r0)
            goto L_0x0571
        L_0x055e:
            java.lang.String r0 = "app.close"
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0571
            android.os.Bundle r0 = r2.bundle
            java.lang.String r4 = "package"
            java.lang.String r0 = r0.getString(r4)
            r1.closeApp(r0)
        L_0x0571:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.wits.pms.core.CenterControlImpl.handle(com.wits.pms.bean.TxzMessage):void");
    }

    public static /* synthetic */ void lambda$handle$1(CenterControlImpl centerControlImpl) {
        KeyUtils.pressKey(4);
        centerControlImpl.mHandler.postDelayed($$Lambda$CenterControlImpl$XWPDQAXolU7D2iszQ__axpMrITU.INSTANCE, 200);
    }

    public boolean isUsingCarPlay() {
        return SystemProperties.get(ZlinkMessage.ZLINK_CONNECT).equals("1");
    }

    public boolean isUsingUsbCarPlay() {
        return SystemProperties.get(AutoKitMessage.AUTOBOX_CONNECT).equals("1");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0050, code lost:
        if (r0.equals("ENTER") != false) goto L_0x007c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x01bf, code lost:
        if (r0.equals("ACTION_ZJ_IPODFOUND") != false) goto L_0x01eb;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handle(com.wits.pms.bean.ZlinkMessage r9) {
        /*
            r8 = this;
            java.lang.String r0 = r9.status
            r1 = 2
            r2 = 3
            r3 = 4
            r4 = -1
            r5 = 1
            r6 = 0
            if (r0 == 0) goto L_0x01ab
            java.lang.String r0 = r9.status
            int r7 = r0.hashCode()
            switch(r7) {
                case -2087582999: goto L_0x0071;
                case -1843701849: goto L_0x0067;
                case -497207953: goto L_0x005d;
                case 2142494: goto L_0x0053;
                case 66129592: goto L_0x004a;
                case 143012129: goto L_0x003f;
                case 143018830: goto L_0x0034;
                case 1015497884: goto L_0x002a;
                case 1709675476: goto L_0x0020;
                case 1766422463: goto L_0x0015;
                default: goto L_0x0013;
            }
        L_0x0013:
            goto L_0x007b
        L_0x0015:
            java.lang.String r1 = "PHONE_CALL_OFF"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x007b
            r1 = 7
            goto L_0x007c
        L_0x0020:
            java.lang.String r1 = "MAIN_PAGE_HIDDEN"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x007b
            r1 = 5
            goto L_0x007c
        L_0x002a:
            java.lang.String r1 = "DISCONNECT"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x007b
            r1 = r5
            goto L_0x007c
        L_0x0034:
            java.lang.String r1 = "SWITCHOTG"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x007b
            r1 = 8
            goto L_0x007c
        L_0x003f:
            java.lang.String r1 = "SWITCHHUB"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x007b
            r1 = 9
            goto L_0x007c
        L_0x004a:
            java.lang.String r2 = "ENTER"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x007b
            goto L_0x007c
        L_0x0053:
            java.lang.String r1 = "EXIT"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x007b
            r1 = r2
            goto L_0x007c
        L_0x005d:
            java.lang.String r1 = "PHONE_CALL_ON"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x007b
            r1 = 6
            goto L_0x007c
        L_0x0067:
            java.lang.String r1 = "MAIN_PAGE_SHOW"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x007b
            r1 = r3
            goto L_0x007c
        L_0x0071:
            java.lang.String r1 = "CONNECTED"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x007b
            r1 = r6
            goto L_0x007c
        L_0x007b:
            r1 = r4
        L_0x007c:
            switch(r1) {
                case 0: goto L_0x0166;
                case 1: goto L_0x011b;
                case 2: goto L_0x0112;
                case 3: goto L_0x0109;
                case 4: goto L_0x0100;
                case 5: goto L_0x00f7;
                case 6: goto L_0x00c9;
                case 7: goto L_0x0093;
                case 8: goto L_0x008a;
                case 9: goto L_0x0081;
                default: goto L_0x007f;
            }
        L_0x007f:
            goto L_0x01a9
        L_0x0081:
            java.lang.String r0 = "CenterControl"
            java.lang.String r1 = "handle  SWITCHHUB"
            android.util.Log.d(r0, r1)
            goto L_0x01a9
        L_0x008a:
            java.lang.String r0 = "CenterControl"
            java.lang.String r1 = "handle  SWITCHOTG"
            android.util.Log.d(r0, r1)
            goto L_0x01a9
        L_0x0093:
            java.lang.String r0 = "CenterControl"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ RemoteException -> 0x00c3 }
            r1.<init>()     // Catch:{ RemoteException -> 0x00c3 }
            java.lang.String r2 = "handle  PHONE_CALL_OFF   originMode = "
            r1.append(r2)     // Catch:{ RemoteException -> 0x00c3 }
            int r2 = r8.originMode     // Catch:{ RemoteException -> 0x00c3 }
            r1.append(r2)     // Catch:{ RemoteException -> 0x00c3 }
            java.lang.String r1 = r1.toString()     // Catch:{ RemoteException -> 0x00c3 }
            android.util.Log.d(r0, r1)     // Catch:{ RemoteException -> 0x00c3 }
            int r0 = r8.originMode     // Catch:{ RemoteException -> 0x00c3 }
            java.lang.String r1 = "systemMode"
            int r1 = com.wits.pms.statuscontrol.PowerManagerApp.getStatusInt(r1)     // Catch:{ RemoteException -> 0x00c3 }
            if (r0 == r1) goto L_0x00ba
            int r0 = r8.originMode     // Catch:{ RemoteException -> 0x00c3 }
            r8.systemModeSwitch(r0)     // Catch:{ RemoteException -> 0x00c3 }
        L_0x00ba:
            java.lang.String r0 = "vendor.wits.zlink.call"
            java.lang.String r1 = "0"
            com.wits.pms.utils.SystemProperties.set(r0, r1)     // Catch:{ RemoteException -> 0x00c3 }
            goto L_0x01a9
        L_0x00c3:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x01a9
        L_0x00c9:
            java.lang.String r0 = "systemMode"
            int r0 = com.wits.pms.statuscontrol.PowerManagerApp.getStatusInt(r0)     // Catch:{ RemoteException -> 0x00ea }
            r8.originMode = r0     // Catch:{ RemoteException -> 0x00ea }
            java.lang.String r0 = "CenterControl"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ RemoteException -> 0x00ea }
            r1.<init>()     // Catch:{ RemoteException -> 0x00ea }
            java.lang.String r2 = "handle  PHONE_CALL_ON  mode = "
            r1.append(r2)     // Catch:{ RemoteException -> 0x00ea }
            int r2 = r8.originMode     // Catch:{ RemoteException -> 0x00ea }
            r1.append(r2)     // Catch:{ RemoteException -> 0x00ea }
            java.lang.String r1 = r1.toString()     // Catch:{ RemoteException -> 0x00ea }
            android.util.Log.d(r0, r1)     // Catch:{ RemoteException -> 0x00ea }
            goto L_0x00ee
        L_0x00ea:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00ee:
            java.lang.String r0 = "vendor.wits.zlink.call"
            java.lang.String r1 = "1"
            com.wits.pms.utils.SystemProperties.set(r0, r1)
            goto L_0x01a9
        L_0x00f7:
            java.lang.String r0 = "CenterControl"
            java.lang.String r1 = "handle  MAIN_PAGE_HIDDEN"
            android.util.Log.d(r0, r1)
            goto L_0x01a9
        L_0x0100:
            java.lang.String r0 = "CenterControl"
            java.lang.String r1 = "handle  MAIN_PAGE_SHOW"
            android.util.Log.d(r0, r1)
            goto L_0x01a9
        L_0x0109:
            java.lang.String r0 = "CenterControl"
            java.lang.String r1 = "handle  EXIT"
            android.util.Log.d(r0, r1)
            goto L_0x01a9
        L_0x0112:
            java.lang.String r0 = "CenterControl"
            java.lang.String r1 = "handle  ENTER"
            android.util.Log.d(r0, r1)
            goto L_0x01a9
        L_0x011b:
            android.os.Bundle r0 = r9.bundle
            java.lang.String r1 = "phoneMode"
            java.lang.String r0 = r0.getString(r1)
            java.lang.String r1 = "CenterControl"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "handle  DISCONNECT  "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            android.util.Log.d(r1, r2)
            if (r0 == 0) goto L_0x014d
            java.lang.String r1 = "carplay_wireless"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x014d
            java.lang.String r1 = "vendor.wits.zlink.connected"
            java.lang.String r2 = "0"
            com.wits.pms.utils.SystemProperties.set(r1, r2)
            r8.openBluetooth(r6)
        L_0x014d:
            java.lang.String r1 = "Support_TXZ"
            int r1 = com.wits.pms.statuscontrol.PowerManagerApp.getSettingsInt(r1)     // Catch:{ Exception -> 0x0161 }
            if (r1 == 0) goto L_0x0160
            java.lang.String r2 = "systemMode"
            int r2 = com.wits.pms.statuscontrol.PowerManagerApp.getStatusInt(r2)     // Catch:{ Exception -> 0x0161 }
            if (r2 != r5) goto L_0x0160
            r8.setTxzSleep(r6)     // Catch:{ Exception -> 0x0161 }
        L_0x0160:
            goto L_0x01a9
        L_0x0161:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x01a9
        L_0x0166:
            android.os.Bundle r0 = r9.bundle
            java.lang.String r1 = "phoneMode"
            java.lang.String r0 = r0.getString(r1)
            java.lang.String r1 = "CenterControl"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "handle  CONNECTED   "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            android.util.Log.d(r1, r2)
            if (r0 == 0) goto L_0x0198
            java.lang.String r1 = "carplay_wireless"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0198
            java.lang.String r1 = "vendor.wits.zlink.connected"
            java.lang.String r2 = "1"
            com.wits.pms.utils.SystemProperties.set(r1, r2)
            r8.closeBluetooth()
        L_0x0198:
            java.lang.String r1 = "Support_TXZ"
            int r1 = com.wits.pms.statuscontrol.PowerManagerApp.getSettingsInt(r1)     // Catch:{ Exception -> 0x01a4 }
            if (r1 == 0) goto L_0x01a3
            r8.setTxzSleep(r5)     // Catch:{ Exception -> 0x01a4 }
        L_0x01a3:
            goto L_0x01a9
        L_0x01a4:
            r1 = move-exception
            r1.printStackTrace()
        L_0x01a9:
            goto L_0x0208
        L_0x01ab:
            java.lang.String r0 = r9.command
            if (r0 == 0) goto L_0x0208
            java.lang.String r0 = r9.command
            int r7 = r0.hashCode()
            switch(r7) {
                case -2126047073: goto L_0x01e0;
                case -1482963131: goto L_0x01d6;
                case -150661894: goto L_0x01cc;
                case 871291637: goto L_0x01c2;
                case 1920379072: goto L_0x01b9;
                default: goto L_0x01b8;
            }
        L_0x01b8:
            goto L_0x01ea
        L_0x01b9:
            java.lang.String r2 = "ACTION_ZJ_IPODFOUND"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x01ea
            goto L_0x01eb
        L_0x01c2:
            java.lang.String r1 = "REQ_OS_AUDIO_FOCUS"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x01ea
            r1 = r6
            goto L_0x01eb
        L_0x01cc:
            java.lang.String r1 = "ACTION_ZJ_PHONEFOUND"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x01ea
            r1 = r5
            goto L_0x01eb
        L_0x01d6:
            java.lang.String r1 = "CMD_MIC_START"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x01ea
            r1 = r2
            goto L_0x01eb
        L_0x01e0:
            java.lang.String r1 = "CMD_MIC_STOP"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x01ea
            r1 = r3
            goto L_0x01eb
        L_0x01ea:
            r1 = r4
        L_0x01eb:
            switch(r1) {
                case 0: goto L_0x0207;
                case 1: goto L_0x0206;
                case 2: goto L_0x0205;
                case 3: goto L_0x01ef;
                default: goto L_0x01ee;
            }
        L_0x01ee:
            goto L_0x0208
        L_0x01ef:
            java.lang.String r0 = "vendor.wits.zlink.call"
            java.lang.String r0 = com.wits.pms.utils.SystemProperties.get(r0)
            java.lang.String r1 = "1"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0208
            java.lang.String r0 = "vendor.wits.zlink.call"
            java.lang.String r1 = "2"
            com.wits.pms.utils.SystemProperties.set(r0, r1)
            goto L_0x0208
        L_0x0205:
            goto L_0x0208
        L_0x0206:
            goto L_0x0208
        L_0x0207:
        L_0x0208:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.wits.pms.core.CenterControlImpl.handle(com.wits.pms.bean.ZlinkMessage):void");
    }

    public void handle(EcarMessage msg) {
        if (msg.ecarSendKey != null) {
            String str = msg.ecarSendKey;
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != -1909808850) {
                if (hashCode == -1362160651 && str.equals("EcarCallStart")) {
                    c = 0;
                }
            } else if (str.equals("EcarCallEnd")) {
                c = 1;
            }
            switch (c) {
                case 0:
                    Log.d(TAG, "handle  EcarCallStart");
                    new TxzMessage(2040, "bluetooth.call", (Bundle) null).sendBroadCast(this.mContext);
                    return;
                case 1:
                    Log.d(TAG, "handle  EcarCallEnd");
                    new TxzMessage(2040, "bluetooth.hangup", (Bundle) null).sendBroadCast(this.mContext);
                    return;
                default:
                    return;
            }
        }
    }

    public void handle(AutoKitMessage msg) {
        int i = msg.receiveKey;
        if (i != 12) {
            switch (i) {
                case 2:
                    Log.d(TAG, "handle  AUTO BOX DISCONNECT");
                    SystemProperties.set(AutoKitMessage.AUTOBOX_CONNECT, "0");
                    openBluetooth(false);
                    try {
                        if (PowerManagerApp.getSettingsInt("Support_TXZ") != 0 && PowerManagerApp.getStatusInt("systemMode") == 1) {
                            setTxzSleep(false);
                            return;
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                case 3:
                    getImpl().openSourceMode(3);
                    Log.d(TAG, "handle  PHONE_CALL_ON");
                    SystemProperties.set(AutoKitMessage.AUTOBOX_CALL, "1");
                    try {
                        if (!PowerManagerApp.getStatusString("topApp").equals(AutoKitCallBackImpl.AutoKitPkgName)) {
                            openApp(AutoKitCallBackImpl.AutoKitPkgName);
                            return;
                        }
                        return;
                    } catch (RemoteException e2) {
                        e2.printStackTrace();
                        return;
                    }
                case 4:
                    Log.d(TAG, "handle  PHONE_CALL_ON");
                    SystemProperties.set(AutoKitMessage.AUTOBOX_CALL, "0");
                    return;
                case 5:
                    Log.d(TAG, "handle  AUTO BOX CONNECT");
                    SystemProperties.set(AutoKitMessage.AUTOBOX_CONNECT, "1");
                    closeBluetooth();
                    try {
                        if (PowerManagerApp.getSettingsInt("Support_TXZ") != 0 && PowerManagerApp.getStatusInt("systemMode") == 1) {
                            setTxzSleep(true);
                            return;
                        }
                        return;
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        return;
                    }
                default:
                    return;
            }
        } else if (SystemProperties.get(AutoKitMessage.AUTOBOX_CALL).equals("1")) {
            SystemProperties.set(AutoKitMessage.AUTOBOX_CALL, "2");
        }
    }

    public void openSpeech() {
        new TxzMessage(PreciseDisconnectCause.IWLAN_DPD_FAILURE, "txz.window.open", (Bundle) null).sendBroadCast(this.mContext);
    }

    public void closeSpeech() {
        new TxzMessage(PreciseDisconnectCause.IWLAN_DPD_FAILURE, "txz.window.close", (Bundle) null).sendBroadCast(this.mContext);
    }

    public void setTxzWindow(boolean on) {
        Bundle bundle = new Bundle();
        bundle.putString("mode", on ? "top" : "none");
        new TxzMessage(PreciseDisconnectCause.IWLAN_DPD_FAILURE, "txz.float.mode", bundle).sendBroadCast(this.mContext);
    }

    public void setTxzSleep(boolean on) {
        Bundle bundle = new Bundle();
        bundle.putString("status", on ? "before.sleep" : "wakeup");
        new TxzMessage(2010, "system.status", bundle).sendBroadCast(this.mContext);
    }

    public void setTxzQuickQuit(boolean on) {
        Bundle bundle = new Bundle();
        bundle.putString("status", on ? "reverse.enter" : "reverse.quit");
        new TxzMessage(2010, "system.status", bundle).sendBroadCast(this.mContext);
    }

    public void setTxzSwitch(boolean on) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("enable", on);
        new TxzMessage(PreciseDisconnectCause.IWLAN_DPD_FAILURE, "txz.enable", bundle).sendBroadCast(this.mContext);
    }

    public void openNavi() {
        try {
            closeSpeech();
            openApp(PowerManagerApp.getSettingsString("NaviApp"));
        } catch (RemoteException e) {
        }
    }

    public void openMusic() {
        openMusic(false);
    }

    public void openMusic(boolean isAcc) {
        Intent intent = new Intent("com.wits.media.MUSIC");
        if (isAcc) {
            intent.putExtra("isAcc", true);
        }
        try {
            startAction(intent);
        } catch (Exception e) {
        }
    }

    public void openVideo() {
        openVideo(false);
    }

    public void openVideo(boolean isAcc) {
        Intent intent = new Intent("com.wits.media.VIDEO");
        if (isAcc) {
            intent.putExtra("isAcc", true);
        }
        try {
            startAction(intent);
        } catch (Exception e) {
        }
    }

    public void openGallery() {
    }

    public void openAndroidBluetooth() {
        try {
            startAction("com.wits.bt.action.OPEN");
        } catch (Exception e) {
        }
    }

    public void closeVideo() {
        WitsCommand.sendCommand(2, 112, "");
    }

    public void closeMusic() {
        WitsCommand.sendCommand(2, 106, "");
    }

    public void closePIP() {
        WitsCommand.sendCommand(2, 118, "");
    }

    public void btMusicOpen() {
    }

    public void wifiOperation(boolean open) {
        ((WifiManager) this.mContext.getSystemService("wifi")).setWifiEnabled(open);
    }

    public void closeApp(String pkgName) {
        Log.i(TAG, "close App in center control - " + pkgName);
        if ("com.txznet.music".equals(pkgName)) {
            this.mContext.sendBroadcast(new Intent("txz.tongting.exit"));
        } else {
            killApp(pkgName);
        }
    }

    public void openApp(String pkgName) {
        Intent lastIntent = this.mContext.getPackageManager().getLaunchIntentForPackage(pkgName);
        if (!TextUtils.isEmpty(pkgName) && !filter(pkgName)) {
            try {
                this.mContext.startActivity(lastIntent);
            } catch (Exception e) {
                if (pkgName.equals("com.autonavi.amapauto")) {
                    Intent launchIntent = new Intent();
                    launchIntent.setComponent(new ComponentName("com.autonavi.amapauto", "com.autonavi.auto.remote.fill.UsbFillActivity"));
                    this.mContext.startActivity(launchIntent);
                }
            }
        }
    }

    public void openApp(String pkgName, String action) {
        Intent lastIntent = this.mContext.getPackageManager().getLaunchIntentForPackage(pkgName);
        lastIntent.setAction(action);
        if (!filter(pkgName)) {
            try {
                this.mContext.startActivity(lastIntent);
            } catch (Exception e) {
                if (pkgName.equals("com.autonavi.amapauto")) {
                    Intent launchIntent = new Intent();
                    launchIntent.setComponent(new ComponentName("com.autonavi.amapauto", "com.autonavi.auto.remote.fill.UsbFillActivity"));
                    this.mContext.startActivity(launchIntent);
                }
            }
        }
    }

    public void kuWoMusicPlay(boolean coldBoot) {
        Log.d(TAG, "kuWoMusicPlay  coldBoot = " + coldBoot);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                new TxzMessage(2060, "music.play", (Bundle) null).sendBroadCast(CenterControlImpl.this.mContext);
            }
        }, coldBoot ? 3000 : 500);
    }

    public void kuWoMusicPause() {
        int pid = AmsUtil.getPid("cn.kuwo.kwmusiccar");
        Log.d(TAG, "kuWoMusicPause  pid = " + pid);
        if (pid != -1) {
            KeyUtils.pressKey(127);
        }
        KeyUtils.pressKey(127);
    }

    public void txzMusicPlay(boolean coldBoot) {
        Log.d(TAG, "txzMusicPlay  coldBoot = " + coldBoot);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                new TxzMessage(2060, "music.play", (Bundle) null).sendBroadCast(CenterControlImpl.this.mContext);
            }
        }, coldBoot ? 500 : 100);
    }

    public void startAction(String action) throws Exception {
        Intent lastIntent = new Intent();
        lastIntent.setAction(action);
        lastIntent.setFlags(268435456);
        this.mContext.startActivity(lastIntent);
    }

    public void startAction(Intent intent) throws Exception {
        intent.setFlags(268435456);
        this.mContext.startActivity(intent);
    }

    private boolean filter(String pkg) {
        pkg.getClass();
        return false;
    }

    public void stopMedia() {
        CenterCallBackImpl.getImpl(this.mContext).sendBroadCast(new TxzMessage(2062, "mcu.open", (Bundle) null));
        this.mContext.sendBroadcast(new Intent("mcu.car_mode.open"));
        this.mContext.sendBroadcast(new Intent("com.android.minwin.clos"));
        if (Settings.System.getInt(this.mContext.getContentResolver(), "CarDisplay", 1) != 0) {
            backToHome();
        }
    }

    private void stopMusic() {
        KeyUtils.pressKey(86);
    }

    public void stopZlinkMusic() {
        Log.d(TAG, "stopZlinkMusic");
        if (SystemProperties.get(ZlinkMessage.ZLINK_CONNECT).equals("1")) {
            new ZlinkMessage(ZlinkMessage.ZLINK_POWER_OFF_ACTION).sendBroadCast(this.mContext);
        }
    }

    public void exitZlink() {
        Log.d(TAG, "exitZlink");
        Bundle bundle = new Bundle();
        bundle.putString("command", "ACTION_EXIT");
        new ZlinkMessage(ZlinkMessage.ZLINK_NORMAL_ACTION, "ACTION_EXIT", bundle).sendBroadCast(this.mContext);
    }

    public void enterZlink() {
        Log.d(TAG, "enterZlink");
        Bundle bundle = new Bundle();
        bundle.putString("command", "ACTION_ENTER");
        new ZlinkMessage(ZlinkMessage.ZLINK_NORMAL_ACTION, "ACTION_ENTER", bundle).sendBroadCast(this.mContext);
    }

    public void handupEcarPhone() {
        Log.d(TAG, "handupEcarPhone");
        this.mContext.sendBroadcast(new Intent("com.ecat.video.HangOnEvent"));
    }

    public void zlinkHandleCall() {
        String callStatus = SystemProperties.get(ZlinkMessage.ZLINK_CALL);
        Log.d(TAG, "zlinkHandleCall  callStatus = " + callStatus);
        if (callStatus.equals("1")) {
            zlinkCallOn();
        } else if (callStatus.equals("2")) {
            zlinkCallOff();
        }
    }

    public void autoKitHandleCall() {
        String callStatus = SystemProperties.get(AutoKitMessage.AUTOBOX_CALL);
        Log.d(TAG, "autoKitHandleCall  callStatus = " + callStatus);
        if (callStatus.equals("1")) {
            AutoKitCallBackImpl.getImpl(this.mContext).acceptPhone();
        } else if (callStatus.equals("2")) {
            AutoKitCallBackImpl.getImpl(this.mContext).rejectPhone();
        }
    }

    private void zlinkCallOn() {
        Bundle bundle = new Bundle();
        bundle.putString("command", "REQ_SPEC_FUNC_CMD");
        bundle.putInt("specFuncCode", 5);
        new ZlinkMessage(ZlinkMessage.ZLINK_NORMAL_ACTION, "REQ_SPEC_FUNC_CMD", bundle).sendBroadCast(this.mContext);
    }

    private void zlinkCallOff() {
        Bundle bundle = new Bundle();
        bundle.putString("command", "REQ_SPEC_FUNC_CMD");
        bundle.putInt("specFuncCode", 6);
        new ZlinkMessage(ZlinkMessage.ZLINK_NORMAL_ACTION, "REQ_SPEC_FUNC_CMD", bundle).sendBroadCast(this.mContext);
    }

    private void playZlinkMusic() {
        Bundle bundle = new Bundle();
        bundle.putString("command", "REQ_SPEC_FUNC_CMD");
        bundle.putInt("specFuncCode", 126);
        new ZlinkMessage(ZlinkMessage.ZLINK_NORMAL_ACTION, "REQ_SPEC_FUNC_CMD", bundle).sendBroadCast(this.mContext);
    }

    public void home() {
        try {
            String naviApp = Settings.System.getString(this.mContext.getContentResolver(), "NaviApp");
            String currentPkg = Settings.System.getString(this.mContext.getContentResolver(), "currentPkg");
            boolean isTalking = false;
            boolean isNavi = (currentPkg == null || naviApp == null || !currentPkg.equals(naviApp)) ? false : true;
            if (Settings.System.getInt(this.mContext.getContentResolver(), "isCalling") == 1) {
                isTalking = true;
            }
            if (!isNavi && !isTalking) {
                KeyUtils.pressKey(3);
            }
        } catch (Exception e) {
            KeyUtils.pressKey(3);
        }
    }

    public void backToHome() {
        KeyUtils.pressKey(86);
        try {
            String naviApp = Settings.System.getString(this.mContext.getContentResolver(), "NaviApp");
            String currentPkg = Settings.System.getString(this.mContext.getContentResolver(), "currentPkg");
            boolean isTalking = false;
            boolean isNavi = (currentPkg == null || naviApp == null || !currentPkg.equals(naviApp)) ? false : true;
            if (Settings.Global.getInt(this.mContext.getContentResolver(), "ksw_home_ban") == 1) {
                isTalking = true;
            }
            if (!isNavi && !isTalking) {
                KeyUtils.pressKey(3);
            }
        } catch (Exception e) {
            KeyUtils.pressKey(3);
        }
    }

    public void mediaPrevious() {
        KeyUtils.pressKey(88);
    }

    public void mediaNext() {
        KeyUtils.pressKey(87);
    }

    public void mediaPause() {
        KeyUtils.pressKey(127);
    }

    public void mediaPlay() {
        KeyUtils.pressKey(126);
    }

    public void mediaPlayPause() {
        KeyUtils.pressKey(85);
    }

    public void muteSwitch() {
        KeyUtils.pressKey(164);
    }

    public void mute(boolean on) {
        if (on) {
            this.mAudioManager.adjustStreamVolume(3, -100, 8);
        } else {
            this.mAudioManager.adjustStreamVolume(3, 100, 8);
        }
    }

    private void muteWithUI(boolean on) {
        if (on) {
            if (this.mLogicSystem.getBtPhoneStatus().isPlayingMusic) {
                this.mAudioManager.adjustStreamVolume(2, -100, 1);
            } else {
                this.mAudioManager.adjustStreamVolume(3, -100, 1);
            }
        } else if (this.mLogicSystem.getBtPhoneStatus().isPlayingMusic) {
            this.mAudioManager.adjustStreamVolume(2, 100, 1);
        } else {
            this.mAudioManager.adjustStreamVolume(3, 100, 1);
        }
    }

    public void volumeUp() {
        this.mAudioManager.adjustStreamVolume(3, 1, 1);
    }

    public void volumeDown() {
        this.mAudioManager.adjustStreamVolume(3, -1, 1);
    }

    private void volumeUpBySystem() {
        if (this.mLogicSystem.getBtPhoneStatus().isPlayingMusic) {
            this.mAudioManager.adjustStreamVolume(2, 1, 1);
        } else {
            this.mAudioManager.adjustStreamVolume(3, 1, 1);
        }
    }

    private void volumeDownBySystem() {
        if (this.mLogicSystem.getBtPhoneStatus().isPlayingMusic) {
            this.mAudioManager.adjustStreamVolume(2, -1, 1);
        } else {
            this.mAudioManager.adjustStreamVolume(3, -1, 1);
        }
    }

    public void volumeMax() {
        this.mAudioManager.setStreamVolume(3, this.mAudioManager.getStreamMaxVolume(3), 1);
    }

    public void volumeMin() {
        this.mAudioManager.setStreamVolume(3, 1, 1);
    }

    public void powerOff() {
        SystemStatusControl.getDefault().dormant(true);
    }

    public void sourceSwitch() {
        int nextMode = 1;
        for (int i = 0; i < this.sourceArray.length; i++) {
            if (this.sourceArray[i] == SystemStatusControl.getDefault().getSystemStatus().lastMode) {
                if (i == this.sourceArray.length - 1) {
                    nextMode = this.sourceArray[0];
                } else {
                    nextMode = this.sourceArray[i + 1];
                }
            }
        }
        if (nextMode == 5) {
            try {
                if (PowerManagerApp.getSettingsInt("DVR_Type") != 1) {
                    nextMode = 6;
                }
            } catch (RemoteException e) {
            }
        }
        if (nextMode == 6) {
            try {
                if (PowerManagerApp.getSettingsInt("AUX_Type") == 0) {
                    nextMode = 9;
                }
            } catch (RemoteException e2) {
            }
        }
        if (nextMode == 9) {
            try {
                if (PowerManagerApp.getSettingsInt("DTV_Type") == 0) {
                    nextMode = 1;
                }
            } catch (RemoteException e3) {
            }
        }
        String topApp = SystemStatusControl.getStatus().topApp;
        openSource(nextMode);
        if (nextMode != 2 && !topApp.equals("com.suding.speedplay")) {
            killApp(topApp);
        }
    }

    private void killApp(String pkgName) {
        if (!pkgName.equals("com.wits.ksw") && !pkgName.equals("com.wits.ksw.bt")) {
            AmsUtil.forceStopPackage(this.mContext, pkgName);
        }
    }

    public void openSource(int source) {
        Log.i(TAG, "try to open source:" + source);
        switch (source) {
            case 0:
                systemModeSwitch(2);
                return;
            case 1:
                openMusic();
                return;
            case 2:
                openVideo();
                return;
            case 3:
                openBluetooth(true);
                return;
            case 5:
                openCarDvr();
                return;
            case 6:
                openAux(true);
                return;
            case 9:
                openDtv(true);
                return;
            case 11:
                openAux(true);
                return;
            default:
                return;
        }
    }

    public void fmOpen() {
    }

    public void fmClose() {
    }

    public void fmPrevious() {
    }

    public void fmNext() {
    }

    public void openSettings() {
        try {
            startAction("com.on.systemUi.start.voice");
        } catch (Exception e) {
        }
    }

    public void call() {
        btPhoneCall("", "");
    }

    public void unCall() {
        handUpPhone();
    }

    public void displayScreen(boolean on) {
        SystemStatusControl.getDefault().getSystemStatus().screenSwitch = on;
        closeScreen(!on);
    }

    public void openBluetooth(boolean open) {
        try {
            if (PowerManagerApp.getSettingsInt("BT_Type") == 1) {
                openCarBt();
            } else {
                this.mHandler.post(new Runnable(open) {
                    private final /* synthetic */ boolean f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void run() {
                        CenterControlImpl.lambda$openBluetooth$2(CenterControlImpl.this, this.f$1);
                    }
                });
            }
        } catch (RemoteException e) {
            openAndroidBluetooth();
        }
    }

    public static /* synthetic */ void lambda$openBluetooth$2(CenterControlImpl centerControlImpl, boolean open) {
        if (!open || !SystemProperties.get(ZlinkMessage.ZLINK_CONNECT).equals("1") || !SystemProperties.get(AutoKitMessage.AUTOBOX_CONNECT).equals("1")) {
            Settings.System.putInt(centerControlImpl.mContext.getContentResolver(), "btSwitch", 1);
        }
        if (!WitsCommand.sendCommandGetResult(3, 107, open ? "true" : "false")) {
            centerControlImpl.openAndroidBluetooth();
        }
    }

    public void closeBluetooth() {
        Settings.System.putInt(this.mContext.getContentResolver(), "btSwitch", 0);
        WitsCommand.sendCommand(3, 105, "");
    }

    public void btPhoneCall(String name, String number) {
        WitsCommand.sendCommand(3, 108, "{number:" + number + "}");
    }

    public void handUpPhone() {
        try {
            if (PowerManagerApp.getStatusBoolean("isConnected")) {
                WitsCommand.sendCommand(3, 109, "");
            }
        } catch (RemoteException e) {
        }
    }

    public void acceptPhone() {
        try {
            if (PowerManagerApp.getStatusBoolean("isConnected")) {
                WitsCommand.sendCommand(3, 112, "");
            }
        } catch (RemoteException e) {
        }
    }

    public void btMusicPlayPause() {
        WitsCommand.sendCommand(3, 102, "");
    }

    public void btMusicPrev() {
        WitsCommand.sendCommand(3, 100, "");
    }

    public void btMusicNext() {
        WitsCommand.sendCommand(3, 101, "");
    }

    public void btMusicContinue() {
        WitsCommand.sendCommand(3, 103, "");
    }

    public void btMusicStop() {
        WitsCommand.sendCommand(3, 104, "");
    }

    public void btMusicRelease() {
        WitsCommand.sendCommand(3, 104, "");
    }

    public void switchSoundToCar() {
        WitsCommand.sendCommand(3, 111, "");
    }

    public void switchSoundToPhone() {
        WitsCommand.sendCommand(3, 110, "");
    }

    public boolean handleCommand(String jsonMsg) throws RemoteException {
        WitsCommand witsCommand = WitsCommand.getWitsCommandFormJson(jsonMsg);
        if (witsCommand.getCommand() == 1) {
            int subCommand = witsCommand.getSubCommand();
            if (subCommand == 200) {
                File config = new File(witsCommand.getJsonArg());
                if (config.exists()) {
                    KswSettings.getSettings().updateConfig(config);
                }
                return true;
            } else if (subCommand == 300) {
                clearMemory();
            } else if (subCommand == 801) {
                sendBenzControlData(McuStatus.BenzData.getStatusFromJson(witsCommand.getJsonArg()));
                return true;
            } else if (subCommand != 1001) {
                switch (subCommand) {
                    case 100:
                        mute(true);
                        return true;
                    case 101:
                        volumeUp();
                        return true;
                    case 102:
                        volumeDown();
                        return true;
                    case 103:
                        mediaPrevious();
                        return true;
                    case 104:
                        mediaNext();
                        return true;
                    case 105:
                        mediaPlay();
                        return true;
                    case 106:
                        mediaPause();
                        return true;
                    case 107:
                        sourceSwitch();
                        return true;
                    case 108:
                        openNavi();
                        return true;
                    case 109:
                        openSpeech();
                        return true;
                    case 110:
                        fmOpen();
                        return true;
                    case 111:
                        openSettings();
                        return true;
                    case 112:
                        displayScreen(true);
                        return true;
                    case 113:
                        displayScreen(false);
                        return true;
                    case 114:
                        KeyUtils.pressKey(3);
                        return true;
                    case 115:
                        KeyUtils.pressKey(4);
                        return true;
                    case 116:
                        acceptPhone();
                        return true;
                    case 117:
                        handUpPhone();
                        return true;
                    case 118:
                        powerOff();
                        return true;
                    case 119:
                        fmPrevious();
                        return true;
                    case 120:
                        fmNext();
                        return true;
                    case 121:
                        mediaPlayPause();
                        return true;
                    case 122:
                        if (TextUtils.isEmpty(witsCommand.getJsonArg())) {
                            return false;
                        }
                        usbHost("on".equals(witsCommand.getJsonArg()));
                        return true;
                    case 123:
                        callButton();
                        return true;
                    case 124:
                        SplashFlasher.flashLogo(witsCommand.getJsonArg());
                        break;
                    case 125:
                        KswSettings.getSettings().saveAndReboot();
                        break;
                    case 126:
                        if (this.installAppDialog != null) {
                            if (this.installAppDialog.isShowing()) {
                                String arg = witsCommand.getJsonArg();
                                if (!TextUtils.isEmpty(arg)) {
                                    this.progressBar.setProgress(Integer.parseInt(arg));
                                    break;
                                }
                            }
                        } else {
                            this.progressBar = new ProgressBar(this.mContext, (AttributeSet) null, 16842872);
                            this.progressBar.setPadding(20, 20, 20, 20);
                            this.installAppDialog = new AlertDialog.Builder(this.mContext, R.style.AlertDialogCustom).setCancelable(true).setTitle((int) R.string.install_app).setView((View) this.progressBar).create();
                            this.installAppDialog.getWindow().setType(2003);
                            this.installAppDialog.show();
                            break;
                        }
                        break;
                    case 127:
                        if (this.installAppDialog != null && this.installAppDialog.isShowing()) {
                            this.installAppDialog.dismiss();
                        }
                        Toast.makeText(this.mContext, (int) R.string.install_app_done, 0).show();
                        break;
                    default:
                        switch (subCommand) {
                            case 601:
                                systemModeSwitch(2);
                                return true;
                            case 602:
                                systemModeSwitch(1);
                                return true;
                            case 603:
                                try {
                                    String jsonArg = witsCommand.getJsonArg();
                                    if (TextUtils.isEmpty(jsonArg)) {
                                        exitSource(Integer.parseInt(jsonArg));
                                    }
                                    return true;
                                } catch (Exception e) {
                                    return false;
                                }
                            case 604:
                                try {
                                    openSourceMode(Integer.parseInt(witsCommand.getJsonArg()));
                                    return true;
                                } catch (Exception e2) {
                                    return false;
                                }
                            case 605:
                                openAux(false);
                                return true;
                            case 606:
                                openDtv(false);
                                return true;
                            case 607:
                                openBluetooth(true);
                                return true;
                            case 608:
                                boolean using = witsCommand.getJsonArg().equals("true");
                                if (getCallingPid() == AmsUtil.getPid("com.papago.M11General") || getCallingPid() == AmsUtil.getPid("com.papago.s1OBU")) {
                                    if (using) {
                                        this.mAudioManager.requestAudioFocus((AudioManager.OnAudioFocusChangeListener) null, 3, -3);
                                    } else {
                                        this.mAudioManager.abandonAudioFocus((AudioManager.OnAudioFocusChangeListener) null);
                                    }
                                }
                                usingNaviProtocol(using);
                                return true;
                            case 609:
                                openCarDvr();
                                return true;
                            case 610:
                                openFrontCamera(false);
                                return true;
                            case 611:
                                checkCarToMcu();
                                break;
                            case 612:
                                try {
                                    String[] data = witsCommand.getJsonArg().split(SmsManager.REGEX_PREFIX_DELIMITER);
                                    sendAirControl(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
                                } catch (Exception e3) {
                                    Log.i(TAG, "error AIRCON_CONTROL", e3);
                                }
                                return true;
                            case 613:
                                KswMcuSender.getSender().sendMessage(104, new byte[]{10, 0});
                                return true;
                        }
                }
            } else {
                BtController.rebootBt();
            }
        } else if (witsCommand.getCommand() == 5) {
            McuCommandListener.handleCommand(witsCommand);
        }
        return false;
    }

    private void sendAirControl(int data1, int data2) {
        KswMcuSender.getSender().sendMessage(122, new byte[]{(byte) data1, (byte) data2});
    }

    public void clearMemory() {
    }

    private void sendBenzControlData(McuStatus.BenzData benzData) {
        KswMcuSender sender = KswMcuSender.getSender();
        byte[] bArr = new byte[6];
        int i = 0;
        bArr[0] = (byte) (benzData.pressButton == 1 ? 1 : 0);
        bArr[1] = (byte) (benzData.pressButton == 2 ? 1 : 0);
        if (benzData.pressButton == 3) {
            i = 1;
        }
        bArr[2] = (byte) i;
        bArr[3] = (byte) benzData.light1;
        bArr[4] = (byte) benzData.light2;
        bArr[5] = (byte) benzData.key3;
        sender.sendMessage(121, bArr);
    }

    private void callButton() {
        KeyUtils.pressKey(5);
    }

    public void handleKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            Log.i(TAG, "handleKeyEvent keycode - " + event.getKeyCode());
            if (event.getKeyCode() == 10000) {
                UpdateHelper.getInstance().sendUpdateMessage(new File("/sdcard/ksw_mcu.bin"));
            }
            if (SystemStatusControl.getStatus().lastMode == 3 || (this.mLogicSystem != null && this.mLogicSystem.getBtPhoneStatus().isPlayingMusic)) {
                switch (event.getKeyCode()) {
                    case 85:
                        btMusicPlayPause();
                        break;
                    case 87:
                        btMusicNext();
                        break;
                    case 88:
                        btMusicPrev();
                        break;
                    case 126:
                        btMusicContinue();
                        break;
                    case 127:
                        btMusicStop();
                        break;
                }
            }
            if (event.getKeyCode() >= 300) {
                Log.i(TAG, "catch custom keycode - " + event.getKeyCode());
            }
            switch (event.getKeyCode()) {
                case 300:
                    closeScreen(true);
                    return;
                case 301:
                    sourceSwitch();
                    return;
                case 302:
                    openSpeech();
                    return;
                case 303:
                    openNavi();
                    return;
                case 320:
                    openBluetooth(false);
                    return;
                case 321:
                    closeBluetooth();
                    return;
                case 380:
                    systemModeSwitch(2);
                    break;
                case 381:
                    break;
                case MetricsProto.MetricsEvent.ACTION_WINDOW_UNDOCK_MAX /*390*/:
                    openCarDvr();
                    return;
                case 400:
                    setTxzSwitch(false);
                    return;
                case 401:
                    setTxzSwitch(true);
                    return;
                case 500:
                    closeScreen(true);
                    return;
                case 501:
                    closeScreen(false);
                    return;
                case 1314:
                    usbHost(false);
                    return;
                case 1315:
                    muteWithUI(true);
                    return;
                case 1316:
                    muteWithUI(false);
                    return;
                case 5000:
                    KswSettings.getSettings().clearKeys();
                    return;
                case BluetoothHidHost.INPUT_CONNECT_FAILED_ALREADY_CONNECTED /*5001*/:
                    KswSettings.getSettings().syncStatus();
                    return;
                case BluetoothHidHost.INPUT_CONNECT_FAILED_ATTEMPT_FAILED /*5002*/:
                    KswSettings.getSettings().setInt("testabcdefg", 123);
                    Set<String> intKeysFromSp = KswSettings.getSettings().getIntKeysFromSp();
                    Log.i("lktTest", intKeysFromSp.toArray()[0] + "");
                    return;
                case BluetoothHidHost.INPUT_OPERATION_GENERIC_FAILURE /*5003*/:
                    new Thread() {
                        public void run() {
                            int i = 0;
                            while (i < 120) {
                                i++;
                                KswMcuLogic.getTestMcu().getListener().onMcuMessage(McuStatus.ACData.getTestMsg2());
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    }.start();
                    return;
                case BluetoothHidHost.INPUT_OPERATION_SUCCESS /*5004*/:
                    KswMcuLogic.getTestMcu().getListener().onMcuMessage(new KswMessage(31, new byte[]{1, 2, 41, BluetoothHidDevice.ERROR_RSP_UNKNOWN, 4}));
                    return;
                case 5005:
                    KswMcuLogic.getTestMcu().getListener().onMcuMessage(new KswMessage(31, new byte[]{WifiScanner.PnoSettings.PnoNetwork.FLAG_SAME_NETWORK, 3, 6, 0, 0, 3, 22}));
                    return;
                default:
                    return;
            }
            systemModeSwitch(1);
        }
    }

    public void startTxzService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.txznet.adapter", "com.txznet.sdk.TXZService"));
        this.mContext.startService(intent);
    }

    public void openSourceMode(int i) {
        getMcuSender().sendMessage(103, new byte[]{(byte) i});
    }

    public void usbHost(boolean on) {
        UsbUtil.updateUsbMode(on);
    }

    private void openCarDvr() {
        backToHome();
        try {
            if (PowerManagerApp.getSettingsInt("DVR_Type") == 1) {
                getMcuSender().sendMessage(103, new byte[]{5});
            }
        } catch (RemoteException e) {
        }
    }

    public void openDtv(boolean home) {
        try {
            if (PowerManagerApp.getSettingsInt("DTV_Type") == 1) {
                if (home) {
                    home();
                }
                stopMusic();
                getMcuSender().sendMessage(103, new byte[]{9});
            }
        } catch (RemoteException e) {
        }
    }

    public void checkCarToMcu() {
        getMcuSender().sendMessage(KswMessage.obtain(104, new byte[]{9, 0}));
    }

    public void openAux(boolean home) {
        try {
            if (PowerManagerApp.getSettingsInt("AUX_Type") == 1) {
                if (home) {
                    home();
                }
                stopMusic();
                getMcuSender().sendMessage(103, new byte[]{6});
            }
        } catch (RemoteException e) {
        }
    }

    public void openFrontCamera(boolean home) {
        try {
            if (PowerManagerApp.getSettingsInt("Front_view_camera") == 1) {
                if (home) {
                    home();
                }
                stopMusic();
                getMcuSender().sendMessage(103, new byte[]{11});
            }
        } catch (RemoteException e) {
        }
    }

    public void openCarBt() {
        getMcuSender().sendMessage(103, new byte[]{0});
        getMcuSender().sendMessage(105, new byte[]{18, 2});
    }

    public void updateStatusInfo(String jsonMsg) throws RemoteException {
    }

    public void setBrightness(int brightness) {
        KswMcuSender.getSender().sendMessage(108, new byte[]{1, (byte) brightness});
    }

    public void systemModeSwitch(int mode) {
        KswMcuSender sender = KswMcuSender.getSender();
        int i = 2;
        byte[] bArr = new byte[2];
        bArr[0] = 18;
        if (mode != 2) {
            i = 1;
        }
        bArr[1] = (byte) i;
        sender.sendMessage(105, bArr);
    }

    public void closeScreen(boolean isClose) {
        KswMcuSender.getSender().sendMessage(108, new byte[]{2, isClose ^ true ? (byte) 1 : 0});
        SystemStatusControl.getDefault().getSystemStatus().screenSwitch = isClose ^ true ? 1 : 0;
        SystemStatusControl.getDefault().handleSystemStatus();
    }

    public void exitSource(int source) {
        KswMcuSender.getSender().sendMessage(104, new byte[]{4, (byte) source});
    }

    public void usingNaviProtocol(boolean using) {
        if (using) {
            this.mHandler.removeCallbacks(this.stopNaviProtocolRunnable);
            KswMcuSender.getSender().sendMessage(105, new byte[]{19, 1, 0});
            return;
        }
        this.mHandler.postDelayed(this.stopNaviProtocolRunnable, 500);
    }

    public void getCarData() {
        KswMcuSender.getSender().sendMessage(104, new byte[]{5, 0});
    }

    public void setLogic(LogicSystem logicSystem) {
        this.mLogicSystem = logicSystem;
    }

    public boolean handleCall() throws RemoteException {
        if (SystemProperties.get(ZlinkMessage.ZLINK_CONNECT).equals("1")) {
            zlinkHandleCall();
            return true;
        } else if (SystemProperties.get(AutoKitMessage.AUTOBOX_CONNECT).equals("1")) {
            autoKitHandleCall();
            return true;
        } else if (!SystemStatusControl.getStatus().topApp.equals(KswSettings.getSettings().getSettingsString("NaviApp"))) {
            return false;
        } else {
            int callStatus = PowerManagerApp.getStatusInt("callStatus");
            switch (callStatus) {
                case 4:
                case 6:
                    handUpPhone();
                    break;
                case 5:
                    acceptPhone();
                    break;
            }
            return BtPhoneStatus.isCalling(callStatus);
        }
    }
}
