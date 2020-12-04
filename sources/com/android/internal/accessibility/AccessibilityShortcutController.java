package com.android.internal.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;
import com.android.internal.R;
import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class AccessibilityShortcutController {
    public static final ComponentName COLOR_INVERSION_COMPONENT_NAME = new ComponentName("com.android.server.accessibility", "ColorInversion");
    public static final ComponentName DALTONIZER_COMPONENT_NAME = new ComponentName("com.android.server.accessibility", "Daltonizer");
    private static final String TAG = "AccessibilityShortcutController";
    private static final AudioAttributes VIBRATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(11).build();
    private static Map<ComponentName, ToggleableFrameworkFeatureInfo> sFrameworkShortcutFeaturesMap;
    private AlertDialog mAlertDialog;
    /* access modifiers changed from: private */
    public final Context mContext;
    private boolean mEnabledOnLockScreen;
    public FrameworkObjectProvider mFrameworkObjectProvider = new FrameworkObjectProvider();
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private boolean mIsShortcutEnabled;
    /* access modifiers changed from: private */
    public int mUserId;

    public static String getTargetServiceComponentNameString(Context context, int userId) {
        String currentShortcutServiceId = Settings.Secure.getStringForUser(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_SHORTCUT_TARGET_SERVICE, userId);
        if (currentShortcutServiceId != null) {
            return currentShortcutServiceId;
        }
        return context.getString(R.string.config_defaultAccessibilityService);
    }

    public static Map<ComponentName, ToggleableFrameworkFeatureInfo> getFrameworkShortcutFeaturesMap() {
        if (sFrameworkShortcutFeaturesMap == null) {
            Map<ComponentName, ToggleableFrameworkFeatureInfo> featuresMap = new ArrayMap<>(2);
            featuresMap.put(COLOR_INVERSION_COMPONENT_NAME, new ToggleableFrameworkFeatureInfo(Settings.Secure.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED, "1", "0", R.string.color_inversion_feature_name));
            featuresMap.put(DALTONIZER_COMPONENT_NAME, new ToggleableFrameworkFeatureInfo(Settings.Secure.ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, "1", "0", R.string.color_correction_feature_name));
            sFrameworkShortcutFeaturesMap = Collections.unmodifiableMap(featuresMap);
        }
        return sFrameworkShortcutFeaturesMap;
    }

    public AccessibilityShortcutController(Context context, Handler handler, int initialUserId) {
        this.mContext = context;
        this.mHandler = handler;
        this.mUserId = initialUserId;
        ContentObserver co = new ContentObserver(handler) {
            public void onChange(boolean selfChange, Uri uri, int userId) {
                if (userId == AccessibilityShortcutController.this.mUserId) {
                    AccessibilityShortcutController.this.onSettingsChanged();
                }
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.ACCESSIBILITY_SHORTCUT_TARGET_SERVICE), false, co, -1);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.ACCESSIBILITY_SHORTCUT_ENABLED), false, co, -1);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.ACCESSIBILITY_SHORTCUT_ON_LOCK_SCREEN), false, co, -1);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.ACCESSIBILITY_SHORTCUT_DIALOG_SHOWN), false, co, -1);
        setCurrentUser(this.mUserId);
    }

    public void setCurrentUser(int currentUserId) {
        this.mUserId = currentUserId;
        onSettingsChanged();
    }

    public boolean isAccessibilityShortcutAvailable(boolean phoneLocked) {
        return this.mIsShortcutEnabled && (!phoneLocked || this.mEnabledOnLockScreen);
    }

    public void onSettingsChanged() {
        boolean z = true;
        boolean haveValidService = !TextUtils.isEmpty(getTargetServiceComponentNameString(this.mContext, this.mUserId));
        ContentResolver cr = this.mContext.getContentResolver();
        boolean enabled = Settings.Secure.getIntForUser(cr, Settings.Secure.ACCESSIBILITY_SHORTCUT_ENABLED, 1, this.mUserId) == 1;
        this.mEnabledOnLockScreen = Settings.Secure.getIntForUser(cr, Settings.Secure.ACCESSIBILITY_SHORTCUT_ON_LOCK_SCREEN, Settings.Secure.getIntForUser(cr, Settings.Secure.ACCESSIBILITY_SHORTCUT_DIALOG_SHOWN, 0, this.mUserId), this.mUserId) == 1;
        if (!enabled || !haveValidService) {
            z = false;
        }
        this.mIsShortcutEnabled = z;
    }

    public void performAccessibilityShortcut() {
        int i;
        Slog.d(TAG, "Accessibility shortcut activated");
        ContentResolver cr = this.mContext.getContentResolver();
        int userId = ActivityManager.getCurrentUser();
        int dialogAlreadyShown = Settings.Secure.getIntForUser(cr, Settings.Secure.ACCESSIBILITY_SHORTCUT_DIALOG_SHOWN, 0, userId);
        Vibrator vibrator = (Vibrator) this.mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(ArrayUtils.convertToLongArray(this.mContext.getResources().getIntArray(R.array.config_longPressVibePattern)), -1, VIBRATION_ATTRIBUTES);
        }
        if (dialogAlreadyShown == 0) {
            this.mAlertDialog = createShortcutWarningDialog(userId);
            if (this.mAlertDialog != null) {
                if (!performTtsPrompt(this.mAlertDialog)) {
                    playNotificationTone();
                }
                Window w = this.mAlertDialog.getWindow();
                WindowManager.LayoutParams attr = w.getAttributes();
                attr.type = 2009;
                w.setAttributes(attr);
                this.mAlertDialog.show();
                Settings.Secure.putIntForUser(cr, Settings.Secure.ACCESSIBILITY_SHORTCUT_DIALOG_SHOWN, 1, userId);
                return;
            }
            return;
        }
        playNotificationTone();
        if (this.mAlertDialog != null) {
            this.mAlertDialog.dismiss();
            this.mAlertDialog = null;
        }
        String serviceName = getShortcutFeatureDescription(false);
        if (serviceName == null) {
            Slog.e(TAG, "Accessibility shortcut set to invalid service");
            return;
        }
        AccessibilityServiceInfo serviceInfo = getInfoForTargetService();
        if (serviceInfo != null) {
            Context context = this.mContext;
            if (isServiceEnabled(serviceInfo)) {
                i = R.string.accessibility_shortcut_disabling_service;
            } else {
                i = R.string.accessibility_shortcut_enabling_service;
            }
            Toast warningToast = this.mFrameworkObjectProvider.makeToastFromText(this.mContext, String.format(context.getString(i), new Object[]{serviceName}), 1);
            warningToast.getWindowParams().privateFlags |= 16;
            warningToast.show();
        }
        this.mFrameworkObjectProvider.getAccessibilityManagerInstance(this.mContext).performAccessibilityShortcut();
    }

    private AlertDialog createShortcutWarningDialog(int userId) {
        String serviceDescription = getShortcutFeatureDescription(true);
        if (serviceDescription == null) {
            return null;
        }
        return this.mFrameworkObjectProvider.getAlertDialogBuilder(this.mFrameworkObjectProvider.getSystemUiContext()).setTitle((int) R.string.accessibility_shortcut_warning_dialog_title).setMessage((CharSequence) String.format(this.mContext.getString(R.string.accessibility_shortcut_toogle_warning), new Object[]{serviceDescription})).setCancelable(false).setPositiveButton((int) R.string.leave_accessibility_shortcut_on, (DialogInterface.OnClickListener) null).setNegativeButton((int) R.string.disable_accessibility_shortcut, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener(userId) {
            private final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(DialogInterface dialogInterface, int i) {
                Settings.Secure.putStringForUser(AccessibilityShortcutController.this.mContext.getContentResolver(), Settings.Secure.ACCESSIBILITY_SHORTCUT_TARGET_SERVICE, "", this.f$1);
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener(userId) {
            private final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            public final void onCancel(DialogInterface dialogInterface) {
                Settings.Secure.putIntForUser(AccessibilityShortcutController.this.mContext.getContentResolver(), Settings.Secure.ACCESSIBILITY_SHORTCUT_DIALOG_SHOWN, 0, this.f$1);
            }
        }).create();
    }

    private AccessibilityServiceInfo getInfoForTargetService() {
        String currentShortcutServiceString = getTargetServiceComponentNameString(this.mContext, -2);
        if (currentShortcutServiceString == null) {
            return null;
        }
        return this.mFrameworkObjectProvider.getAccessibilityManagerInstance(this.mContext).getInstalledServiceInfoWithComponentName(ComponentName.unflattenFromString(currentShortcutServiceString));
    }

    private String getShortcutFeatureDescription(boolean includeSummary) {
        String currentShortcutServiceString = getTargetServiceComponentNameString(this.mContext, -2);
        if (currentShortcutServiceString == null) {
            return null;
        }
        ComponentName targetComponentName = ComponentName.unflattenFromString(currentShortcutServiceString);
        ToggleableFrameworkFeatureInfo frameworkFeatureInfo = getFrameworkShortcutFeaturesMap().get(targetComponentName);
        if (frameworkFeatureInfo != null) {
            return frameworkFeatureInfo.getLabel(this.mContext);
        }
        AccessibilityServiceInfo serviceInfo = this.mFrameworkObjectProvider.getAccessibilityManagerInstance(this.mContext).getInstalledServiceInfoWithComponentName(targetComponentName);
        if (serviceInfo == null) {
            return null;
        }
        PackageManager pm = this.mContext.getPackageManager();
        String label = serviceInfo.getResolveInfo().loadLabel(pm).toString();
        CharSequence summary = serviceInfo.loadSummary(pm);
        if (!includeSummary || TextUtils.isEmpty(summary)) {
            return label;
        }
        return String.format("%s\n%s", new Object[]{label, summary});
    }

    private boolean isServiceEnabled(AccessibilityServiceInfo serviceInfo) {
        return this.mFrameworkObjectProvider.getAccessibilityManagerInstance(this.mContext).getEnabledAccessibilityServiceList(-1).contains(serviceInfo);
    }

    private boolean hasFeatureLeanback() {
        return this.mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LEANBACK);
    }

    /* access modifiers changed from: private */
    public void playNotificationTone() {
        int audioAttributesUsage;
        if (hasFeatureLeanback()) {
            audioAttributesUsage = 11;
        } else {
            audioAttributesUsage = 10;
        }
        Ringtone tone = this.mFrameworkObjectProvider.getRingtone(this.mContext, Settings.System.DEFAULT_NOTIFICATION_URI);
        if (tone != null) {
            tone.setAudioAttributes(new AudioAttributes.Builder().setUsage(audioAttributesUsage).build());
            tone.play();
        }
    }

    private boolean performTtsPrompt(AlertDialog alertDialog) {
        String serviceName = getShortcutFeatureDescription(false);
        AccessibilityServiceInfo serviceInfo = getInfoForTargetService();
        if (TextUtils.isEmpty(serviceName) || serviceInfo == null || (serviceInfo.flags & 1024) == 0) {
            return false;
        }
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public final void onDismiss(DialogInterface dialogInterface) {
                AccessibilityShortcutController.TtsPrompt.this.dismiss();
            }
        });
        return true;
    }

    private class TtsPrompt implements TextToSpeech.OnInitListener {
        private boolean mDismiss;
        private final CharSequence mText;
        private TextToSpeech mTts;

        TtsPrompt(String serviceName) {
            this.mText = AccessibilityShortcutController.this.mContext.getString(R.string.accessibility_shortcut_spoken_feedback, serviceName);
            this.mTts = AccessibilityShortcutController.this.mFrameworkObjectProvider.getTextToSpeech(AccessibilityShortcutController.this.mContext, this);
        }

        public void dismiss() {
            this.mDismiss = true;
            AccessibilityShortcutController.this.mHandler.sendMessage(PooledLambda.obtainMessage($$Lambda$qdzoyIBhDB17ZFWPp1Rf8ICvR8.INSTANCE, this.mTts));
        }

        public void onInit(int status) {
            if (status != 0) {
                Slog.d(AccessibilityShortcutController.TAG, "Tts init fail, status=" + Integer.toString(status));
                AccessibilityShortcutController.this.playNotificationTone();
                return;
            }
            AccessibilityShortcutController.this.mHandler.sendMessage(PooledLambda.obtainMessage($$Lambda$AccessibilityShortcutController$TtsPrompt$HwizF4cvqRFiaqAcMrC7W8y6zYA.INSTANCE, this));
        }

        /* access modifiers changed from: private */
        public void play() {
            if (!this.mDismiss) {
                int status = -1;
                if (setLanguage(Locale.getDefault())) {
                    status = this.mTts.speak(this.mText, 0, (Bundle) null, (String) null);
                }
                if (status != 0) {
                    Slog.d(AccessibilityShortcutController.TAG, "Tts play fail");
                    AccessibilityShortcutController.this.playNotificationTone();
                }
            }
        }

        private boolean setLanguage(Locale locale) {
            int status = this.mTts.isLanguageAvailable(locale);
            if (status == -1 || status == -2) {
                return false;
            }
            this.mTts.setLanguage(locale);
            Voice voice = this.mTts.getVoice();
            if (voice == null || (voice.getFeatures() != null && voice.getFeatures().contains(TextToSpeech.Engine.KEY_FEATURE_NOT_INSTALLED))) {
                return false;
            }
            return true;
        }
    }

    public static class ToggleableFrameworkFeatureInfo {
        private int mIconDrawableId;
        private final int mLabelStringResourceId;
        private final String mSettingKey;
        private final String mSettingOffValue;
        private final String mSettingOnValue;

        ToggleableFrameworkFeatureInfo(String settingKey, String settingOnValue, String settingOffValue, int labelStringResourceId) {
            this.mSettingKey = settingKey;
            this.mSettingOnValue = settingOnValue;
            this.mSettingOffValue = settingOffValue;
            this.mLabelStringResourceId = labelStringResourceId;
        }

        public String getSettingKey() {
            return this.mSettingKey;
        }

        public String getSettingOnValue() {
            return this.mSettingOnValue;
        }

        public String getSettingOffValue() {
            return this.mSettingOffValue;
        }

        public String getLabel(Context context) {
            return context.getString(this.mLabelStringResourceId);
        }
    }

    public static class FrameworkObjectProvider {
        public AccessibilityManager getAccessibilityManagerInstance(Context context) {
            return AccessibilityManager.getInstance(context);
        }

        public AlertDialog.Builder getAlertDialogBuilder(Context context) {
            return new AlertDialog.Builder(context);
        }

        public Toast makeToastFromText(Context context, CharSequence charSequence, int duration) {
            return Toast.makeText(context, charSequence, duration);
        }

        public Context getSystemUiContext() {
            return ActivityThread.currentActivityThread().getSystemUiContext();
        }

        public TextToSpeech getTextToSpeech(Context ctx, TextToSpeech.OnInitListener listener) {
            return new TextToSpeech(ctx, listener);
        }

        public Ringtone getRingtone(Context ctx, Uri uri) {
            return RingtoneManager.getRingtone(ctx, uri);
        }
    }
}
