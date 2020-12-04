package android.webkit;

import android.annotation.SystemApi;
import java.util.Map;

public class WebStorage {

    @Deprecated
    public interface QuotaUpdater {
        void updateQuota(long j);
    }

    public static class Origin {
        private String mOrigin = null;
        private long mQuota = 0;
        private long mUsage = 0;

        @SystemApi
        protected Origin(String origin, long quota, long usage) {
            this.mOrigin = origin;
            this.mQuota = quota;
            this.mUsage = usage;
        }

        public String getOrigin() {
            return this.mOrigin;
        }

        public long getQuota() {
            return this.mQuota;
        }

        public long getUsage() {
            return this.mUsage;
        }
    }

    public void getOrigins(ValueCallback<Map> valueCallback) {
    }

    public void getUsageForOrigin(String origin, ValueCallback<Long> valueCallback) {
    }

    public void getQuotaForOrigin(String origin, ValueCallback<Long> valueCallback) {
    }

    @Deprecated
    public void setQuotaForOrigin(String origin, long quota) {
    }

    public void deleteOrigin(String origin) {
    }

    public void deleteAllData() {
    }

    public static WebStorage getInstance() {
        return WebViewFactory.getProvider().getWebStorage();
    }
}
