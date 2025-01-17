package android.webkit;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class WebSettings {
    public static final int FORCE_DARK_AUTO = 1;
    public static final int FORCE_DARK_OFF = 0;
    public static final int FORCE_DARK_ON = 2;
    public static final int LOAD_CACHE_ELSE_NETWORK = 1;
    public static final int LOAD_CACHE_ONLY = 3;
    public static final int LOAD_DEFAULT = -1;
    @Deprecated
    public static final int LOAD_NORMAL = 0;
    public static final int LOAD_NO_CACHE = 2;
    public static final int MENU_ITEM_NONE = 0;
    public static final int MENU_ITEM_PROCESS_TEXT = 4;
    public static final int MENU_ITEM_SHARE = 1;
    public static final int MENU_ITEM_WEB_SEARCH = 2;
    public static final int MIXED_CONTENT_ALWAYS_ALLOW = 0;
    public static final int MIXED_CONTENT_COMPATIBILITY_MODE = 2;
    public static final int MIXED_CONTENT_NEVER_ALLOW = 1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CacheMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ForceDark {
    }

    public enum LayoutAlgorithm {
        NORMAL,
        SINGLE_COLUMN,
        NARROW_COLUMNS,
        TEXT_AUTOSIZING
    }

    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    private @interface MenuItemFlags {
    }

    public enum PluginState {
        ON,
        ON_DEMAND,
        OFF
    }

    public enum RenderPriority {
        NORMAL,
        HIGH,
        LOW
    }

    @Deprecated
    public abstract boolean enableSmoothTransition();

    @SystemApi
    public abstract boolean getAcceptThirdPartyCookies();

    public abstract boolean getAllowContentAccess();

    public abstract boolean getAllowFileAccess();

    public abstract boolean getAllowFileAccessFromFileURLs();

    public abstract boolean getAllowUniversalAccessFromFileURLs();

    public abstract boolean getBlockNetworkImage();

    public abstract boolean getBlockNetworkLoads();

    public abstract boolean getBuiltInZoomControls();

    public abstract int getCacheMode();

    public abstract String getCursiveFontFamily();

    public abstract boolean getDatabaseEnabled();

    @Deprecated
    public abstract String getDatabasePath();

    public abstract int getDefaultFixedFontSize();

    public abstract int getDefaultFontSize();

    public abstract String getDefaultTextEncodingName();

    @Deprecated
    public abstract ZoomDensity getDefaultZoom();

    public abstract int getDisabledActionModeMenuItems();

    public abstract boolean getDisplayZoomControls();

    public abstract boolean getDomStorageEnabled();

    public abstract String getFantasyFontFamily();

    public abstract String getFixedFontFamily();

    public abstract boolean getJavaScriptCanOpenWindowsAutomatically();

    public abstract boolean getJavaScriptEnabled();

    public abstract LayoutAlgorithm getLayoutAlgorithm();

    @Deprecated
    public abstract boolean getLightTouchEnabled();

    public abstract boolean getLoadWithOverviewMode();

    public abstract boolean getLoadsImagesAutomatically();

    public abstract boolean getMediaPlaybackRequiresUserGesture();

    public abstract int getMinimumFontSize();

    public abstract int getMinimumLogicalFontSize();

    public abstract int getMixedContentMode();

    @SystemApi
    @Deprecated
    public abstract boolean getNavDump();

    public abstract boolean getOffscreenPreRaster();

    @Deprecated
    public abstract PluginState getPluginState();

    @SystemApi
    @Deprecated
    public abstract boolean getPluginsEnabled();

    public abstract boolean getSafeBrowsingEnabled();

    public abstract String getSansSerifFontFamily();

    @Deprecated
    public abstract boolean getSaveFormData();

    @Deprecated
    public abstract boolean getSavePassword();

    public abstract String getSerifFontFamily();

    public abstract String getStandardFontFamily();

    public abstract int getTextZoom();

    @SystemApi
    @Deprecated
    public abstract boolean getUseWebViewBackgroundForOverscrollBackground();

    public abstract boolean getUseWideViewPort();

    @SystemApi
    @Deprecated
    public abstract int getUserAgent();

    public abstract String getUserAgentString();

    @SystemApi
    public abstract boolean getVideoOverlayForEmbeddedEncryptedVideoEnabled();

    @SystemApi
    public abstract void setAcceptThirdPartyCookies(boolean z);

    public abstract void setAllowContentAccess(boolean z);

    public abstract void setAllowFileAccess(boolean z);

    public abstract void setAllowFileAccessFromFileURLs(boolean z);

    public abstract void setAllowUniversalAccessFromFileURLs(boolean z);

    public abstract void setAppCacheEnabled(boolean z);

    @Deprecated
    public abstract void setAppCacheMaxSize(long j);

    public abstract void setAppCachePath(String str);

    public abstract void setBlockNetworkImage(boolean z);

    public abstract void setBlockNetworkLoads(boolean z);

    public abstract void setBuiltInZoomControls(boolean z);

    public abstract void setCacheMode(int i);

    public abstract void setCursiveFontFamily(String str);

    public abstract void setDatabaseEnabled(boolean z);

    @Deprecated
    public abstract void setDatabasePath(String str);

    public abstract void setDefaultFixedFontSize(int i);

    public abstract void setDefaultFontSize(int i);

    public abstract void setDefaultTextEncodingName(String str);

    @Deprecated
    public abstract void setDefaultZoom(ZoomDensity zoomDensity);

    public abstract void setDisabledActionModeMenuItems(int i);

    public abstract void setDisplayZoomControls(boolean z);

    public abstract void setDomStorageEnabled(boolean z);

    @Deprecated
    public abstract void setEnableSmoothTransition(boolean z);

    public abstract void setFantasyFontFamily(String str);

    public abstract void setFixedFontFamily(String str);

    @Deprecated
    public abstract void setGeolocationDatabasePath(String str);

    public abstract void setGeolocationEnabled(boolean z);

    public abstract void setJavaScriptCanOpenWindowsAutomatically(boolean z);

    public abstract void setJavaScriptEnabled(boolean z);

    public abstract void setLayoutAlgorithm(LayoutAlgorithm layoutAlgorithm);

    @Deprecated
    public abstract void setLightTouchEnabled(boolean z);

    public abstract void setLoadWithOverviewMode(boolean z);

    public abstract void setLoadsImagesAutomatically(boolean z);

    public abstract void setMediaPlaybackRequiresUserGesture(boolean z);

    public abstract void setMinimumFontSize(int i);

    public abstract void setMinimumLogicalFontSize(int i);

    public abstract void setMixedContentMode(int i);

    @SystemApi
    @Deprecated
    public abstract void setNavDump(boolean z);

    public abstract void setNeedInitialFocus(boolean z);

    public abstract void setOffscreenPreRaster(boolean z);

    @Deprecated
    public abstract void setPluginState(PluginState pluginState);

    @SystemApi
    @Deprecated
    public abstract void setPluginsEnabled(boolean z);

    @Deprecated
    public abstract void setRenderPriority(RenderPriority renderPriority);

    public abstract void setSafeBrowsingEnabled(boolean z);

    public abstract void setSansSerifFontFamily(String str);

    @Deprecated
    public abstract void setSaveFormData(boolean z);

    @Deprecated
    public abstract void setSavePassword(boolean z);

    public abstract void setSerifFontFamily(String str);

    public abstract void setStandardFontFamily(String str);

    public abstract void setSupportMultipleWindows(boolean z);

    public abstract void setSupportZoom(boolean z);

    public abstract void setTextZoom(int i);

    @SystemApi
    @Deprecated
    public abstract void setUseWebViewBackgroundForOverscrollBackground(boolean z);

    public abstract void setUseWideViewPort(boolean z);

    @SystemApi
    @Deprecated
    public abstract void setUserAgent(int i);

    public abstract void setUserAgentString(String str);

    @SystemApi
    public abstract void setVideoOverlayForEmbeddedEncryptedVideoEnabled(boolean z);

    public abstract boolean supportMultipleWindows();

    public abstract boolean supportZoom();

    @Deprecated
    public enum TextSize {
        SMALLEST(50),
        SMALLER(75),
        NORMAL(100),
        LARGER(150),
        LARGEST(200);
        
        @UnsupportedAppUsage
        int value;

        private TextSize(int size) {
            this.value = size;
        }
    }

    public enum ZoomDensity {
        FAR(150),
        MEDIUM(100),
        CLOSE(75);
        
        int value;

        private ZoomDensity(int size) {
            this.value = size;
        }

        public int getValue() {
            return this.value;
        }
    }

    @Deprecated
    public synchronized void setTextSize(TextSize t) {
        setTextZoom(t.value);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002d, code lost:
        return r0 != null ? r0 : android.webkit.WebSettings.TextSize.NORMAL;
     */
    @java.lang.Deprecated
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized android.webkit.WebSettings.TextSize getTextSize() {
        /*
            r8 = this;
            monitor-enter(r8)
            r0 = 0
            r1 = 2147483647(0x7fffffff, float:NaN)
            int r2 = r8.getTextZoom()     // Catch:{ all -> 0x002e }
            android.webkit.WebSettings$TextSize[] r3 = android.webkit.WebSettings.TextSize.values()     // Catch:{ all -> 0x002e }
            int r4 = r3.length     // Catch:{ all -> 0x002e }
            r5 = 0
        L_0x000f:
            if (r5 >= r4) goto L_0x0026
            r6 = r3[r5]     // Catch:{ all -> 0x002e }
            int r7 = r6.value     // Catch:{ all -> 0x002e }
            int r7 = r2 - r7
            int r7 = java.lang.Math.abs(r7)     // Catch:{ all -> 0x002e }
            if (r7 != 0) goto L_0x001f
            monitor-exit(r8)
            return r6
        L_0x001f:
            if (r7 >= r1) goto L_0x0023
            r1 = r7
            r0 = r6
        L_0x0023:
            int r5 = r5 + 1
            goto L_0x000f
        L_0x0026:
            if (r0 == 0) goto L_0x002a
            r3 = r0
            goto L_0x002c
        L_0x002a:
            android.webkit.WebSettings$TextSize r3 = android.webkit.WebSettings.TextSize.NORMAL     // Catch:{ all -> 0x002e }
        L_0x002c:
            monitor-exit(r8)
            return r3
        L_0x002e:
            r0 = move-exception
            monitor-exit(r8)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.webkit.WebSettings.getTextSize():android.webkit.WebSettings$TextSize");
    }

    @Deprecated
    @UnsupportedAppUsage
    public void setUseDoubleTree(boolean use) {
    }

    @Deprecated
    @UnsupportedAppUsage
    public boolean getUseDoubleTree() {
        return false;
    }

    @Deprecated
    @UnsupportedAppUsage
    public void setPluginsPath(String pluginsPath) {
    }

    @Deprecated
    @UnsupportedAppUsage
    public String getPluginsPath() {
        return "";
    }

    public static String getDefaultUserAgent(Context context) {
        return WebViewFactory.getProvider().getStatics().getDefaultUserAgent(context);
    }

    public void setForceDark(int forceDark) {
    }

    public int getForceDark() {
        return 1;
    }
}
