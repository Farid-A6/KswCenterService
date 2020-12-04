package android.view.contentcapture;

import android.os.Build;
import android.provider.DeviceConfig;
import android.util.ArraySet;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class ContentCaptureHelper {
    private static final String TAG = ContentCaptureHelper.class.getSimpleName();
    public static boolean sDebug = true;
    public static boolean sVerbose = false;

    public static String getSanitizedString(CharSequence text) {
        if (text == null) {
            return null;
        }
        return text.length() + "_chars";
    }

    public static int getDefaultLoggingLevel() {
        return Build.IS_DEBUGGABLE ? 1 : 0;
    }

    public static void setLoggingLevel() {
        setLoggingLevel(DeviceConfig.getInt("content_capture", ContentCaptureManager.DEVICE_CONFIG_PROPERTY_LOGGING_LEVEL, getDefaultLoggingLevel()));
    }

    public static void setLoggingLevel(int level) {
        String str = TAG;
        Log.i(str, "Setting logging level to " + getLoggingLevelAsString(level));
        sDebug = false;
        sVerbose = false;
        switch (level) {
            case 0:
                return;
            case 1:
                break;
            case 2:
                sVerbose = true;
                break;
            default:
                String str2 = TAG;
                Log.w(str2, "setLoggingLevel(): invalud level: " + level);
                return;
        }
        sDebug = true;
    }

    public static String getLoggingLevelAsString(int level) {
        switch (level) {
            case 0:
                return "OFF";
            case 1:
                return "DEBUG";
            case 2:
                return "VERBOSE";
            default:
                return "UNKNOWN-" + level;
        }
    }

    public static <T> ArrayList<T> toList(Set<T> set) {
        if (set == null) {
            return null;
        }
        return new ArrayList<>(set);
    }

    public static <T> ArraySet<T> toSet(List<T> list) {
        if (list == null) {
            return null;
        }
        return new ArraySet<>(list);
    }

    private ContentCaptureHelper() {
        throw new UnsupportedOperationException("contains only static methods");
    }
}
