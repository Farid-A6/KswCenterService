package android.view.textclassifier;

import android.provider.DeviceConfig;
import android.util.ArrayMap;
import android.util.KeyValueListParser;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
public final class ConfigParser {
    static final boolean ENABLE_DEVICE_CONFIG = true;
    private static final String STRING_LIST_DELIMITER = ":";
    private static final String TAG = "ConfigParser";
    @GuardedBy({"mLock"})
    private final Map<String, Object> mCache = new ArrayMap();
    private final Supplier<String> mLegacySettingsSupplier;
    private final Object mLock = new Object();
    @GuardedBy({"mLock"})
    private KeyValueListParser mSettingsParser;

    /* JADX WARNING: type inference failed for: r2v0, types: [java.util.function.Supplier<java.lang.String>, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ConfigParser(java.util.function.Supplier<java.lang.String> r2) {
        /*
            r1 = this;
            r1.<init>()
            java.lang.Object r0 = new java.lang.Object
            r0.<init>()
            r1.mLock = r0
            android.util.ArrayMap r0 = new android.util.ArrayMap
            r0.<init>()
            r1.mCache = r0
            java.lang.Object r0 = com.android.internal.util.Preconditions.checkNotNull(r2)
            java.util.function.Supplier r0 = (java.util.function.Supplier) r0
            r1.mLegacySettingsSupplier = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.textclassifier.ConfigParser.<init>(java.util.function.Supplier):void");
    }

    private KeyValueListParser getLegacySettings() {
        KeyValueListParser keyValueListParser;
        synchronized (this.mLock) {
            if (this.mSettingsParser == null) {
                String legacySettings = this.mLegacySettingsSupplier.get();
                try {
                    this.mSettingsParser = new KeyValueListParser(',');
                    this.mSettingsParser.setString(legacySettings);
                } catch (IllegalArgumentException e) {
                    Log.w(TAG, "Bad text_classifier_constants: " + legacySettings);
                }
            }
            keyValueListParser = this.mSettingsParser;
        }
        return keyValueListParser;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        synchronized (this.mLock) {
            Object cached = this.mCache.get(key);
            if (cached instanceof Boolean) {
                boolean booleanValue = ((Boolean) cached).booleanValue();
                return booleanValue;
            }
            boolean value = DeviceConfig.getBoolean(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, getLegacySettings().getBoolean(key, defaultValue));
            this.mCache.put(key, Boolean.valueOf(value));
            return value;
        }
    }

    public int getInt(String key, int defaultValue) {
        synchronized (this.mLock) {
            Object cached = this.mCache.get(key);
            if (cached instanceof Integer) {
                int intValue = ((Integer) cached).intValue();
                return intValue;
            }
            int value = DeviceConfig.getInt(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, getLegacySettings().getInt(key, defaultValue));
            this.mCache.put(key, Integer.valueOf(value));
            return value;
        }
    }

    public float getFloat(String key, float defaultValue) {
        synchronized (this.mLock) {
            Object cached = this.mCache.get(key);
            if (cached instanceof Float) {
                float floatValue = ((Float) cached).floatValue();
                return floatValue;
            }
            float value = DeviceConfig.getFloat(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, getLegacySettings().getFloat(key, defaultValue));
            this.mCache.put(key, Float.valueOf(value));
            return value;
        }
    }

    public String getString(String key, String defaultValue) {
        synchronized (this.mLock) {
            Object cached = this.mCache.get(key);
            if (cached instanceof String) {
                String str = (String) cached;
                return str;
            }
            String value = DeviceConfig.getString(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, getLegacySettings().getString(key, defaultValue));
            this.mCache.put(key, value);
            return value;
        }
    }

    public List<String> getStringList(String key, List<String> defaultValue) {
        synchronized (this.mLock) {
            Object cached = this.mCache.get(key);
            if (cached instanceof List) {
                List asList = (List) cached;
                if (asList.isEmpty()) {
                    List<String> emptyList = Collections.emptyList();
                    return emptyList;
                } else if (asList.get(0) instanceof String) {
                    List<String> list = (List) cached;
                    return list;
                }
            }
            List<String> value = getDeviceConfigStringList(key, getSettingsStringList(key, defaultValue));
            this.mCache.put(key, value);
            return value;
        }
    }

    public float[] getFloatArray(String key, float[] defaultValue) {
        synchronized (this.mLock) {
            Object cached = this.mCache.get(key);
            if (cached instanceof float[]) {
                float[] fArr = (float[]) cached;
                return fArr;
            }
            float[] value = getDeviceConfigFloatArray(key, getSettingsFloatArray(key, defaultValue));
            this.mCache.put(key, value);
            return value;
        }
    }

    private List<String> getSettingsStringList(String key, List<String> defaultValue) {
        return parse(this.mSettingsParser.getString(key, (String) null), defaultValue);
    }

    private static List<String> getDeviceConfigStringList(String key, List<String> defaultValue) {
        return parse(DeviceConfig.getString(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, (String) null), defaultValue);
    }

    private static float[] getDeviceConfigFloatArray(String key, float[] defaultValue) {
        return parse(DeviceConfig.getString(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, (String) null), defaultValue);
    }

    private float[] getSettingsFloatArray(String key, float[] defaultValue) {
        return parse(this.mSettingsParser.getString(key, (String) null), defaultValue);
    }

    private static List<String> parse(String listStr, List<String> defaultValue) {
        if (listStr != null) {
            return Collections.unmodifiableList(Arrays.asList(listStr.split(":")));
        }
        return defaultValue;
    }

    private static float[] parse(String arrayStr, float[] defaultValue) {
        if (arrayStr == null) {
            return defaultValue;
        }
        String[] split = arrayStr.split(":");
        if (split.length != defaultValue.length) {
            return defaultValue;
        }
        float[] result = new float[split.length];
        int i = 0;
        while (i < split.length) {
            try {
                result[i] = Float.parseFloat(split[i]);
                i++;
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return result;
    }
}
