package android.support.v4.text;

import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

@RequiresApi(14)
class ICUCompatIcs {
    private static final String TAG = "ICUCompatIcs";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    ICUCompatIcs() {
    }

    static {
        try {
            Class<?> clazz = Class.forName("libcore.icu.ICU");
            if (clazz != null) {
                sGetScriptMethod = clazz.getMethod("getScript", new Class[]{String.class});
                sAddLikelySubtagsMethod = clazz.getMethod("addLikelySubtags", new Class[]{String.class});
            }
        } catch (Exception e) {
            sGetScriptMethod = null;
            sAddLikelySubtagsMethod = null;
            Log.w(TAG, (Throwable) e);
        }
    }

    public static String maximizeAndGetScript(Locale locale) {
        String localeWithSubtags = addLikelySubtags(locale);
        if (localeWithSubtags != null) {
            return getScript(localeWithSubtags);
        }
        return null;
    }

    private static String getScript(String localeStr) {
        try {
            if (sGetScriptMethod != null) {
                return (String) sGetScriptMethod.invoke((Object) null, new Object[]{localeStr});
            }
        } catch (IllegalAccessException e) {
            Log.w(TAG, (Throwable) e);
        } catch (InvocationTargetException e2) {
            Log.w(TAG, (Throwable) e2);
        }
        return null;
    }

    private static String addLikelySubtags(Locale locale) {
        String localeStr = locale.toString();
        try {
            if (sAddLikelySubtagsMethod != null) {
                return (String) sAddLikelySubtagsMethod.invoke((Object) null, new Object[]{localeStr});
            }
        } catch (IllegalAccessException e) {
            Log.w(TAG, (Throwable) e);
        } catch (InvocationTargetException e2) {
            Log.w(TAG, (Throwable) e2);
        }
        return localeStr;
    }
}