package android.service.notification;

import android.app.INotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.service.notification.IConditionProvider;
import android.util.Log;

@Deprecated
public abstract class ConditionProviderService extends Service {
    @Deprecated
    public static final String EXTRA_RULE_ID = "android.service.notification.extra.RULE_ID";
    @Deprecated
    public static final String META_DATA_CONFIGURATION_ACTIVITY = "android.service.zen.automatic.configurationActivity";
    @Deprecated
    public static final String META_DATA_RULE_INSTANCE_LIMIT = "android.service.zen.automatic.ruleInstanceLimit";
    @Deprecated
    public static final String META_DATA_RULE_TYPE = "android.service.zen.automatic.ruleType";
    public static final String SERVICE_INTERFACE = "android.service.notification.ConditionProviderService";
    /* access modifiers changed from: private */
    public final String TAG = (ConditionProviderService.class.getSimpleName() + "[" + getClass().getSimpleName() + "]");
    /* access modifiers changed from: private */
    public final H mHandler = new H();
    boolean mIsConnected;
    private INotificationManager mNoMan;
    private Provider mProvider;

    public abstract void onConnected();

    public abstract void onSubscribe(Uri uri);

    public abstract void onUnsubscribe(Uri uri);

    public void onRequestConditions(int relevance) {
    }

    private final INotificationManager getNotificationInterface() {
        if (this.mNoMan == null) {
            this.mNoMan = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        }
        return this.mNoMan;
    }

    public static final void requestRebind(ComponentName componentName) {
        try {
            INotificationManager.Stub.asInterface(ServiceManager.getService("notification")).requestBindProvider(componentName);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public final void requestUnbind() {
        try {
            getNotificationInterface().requestUnbindProvider(this.mProvider);
            this.mIsConnected = false;
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public final void notifyCondition(Condition condition) {
        if (condition != null) {
            notifyConditions(condition);
        }
    }

    @Deprecated
    public final void notifyConditions(Condition... conditions) {
        if (isBound() && conditions != null) {
            try {
                getNotificationInterface().notifyConditions(getPackageName(), this.mProvider, conditions);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public IBinder onBind(Intent intent) {
        if (this.mProvider == null) {
            this.mProvider = new Provider();
        }
        return this.mProvider;
    }

    public boolean isBound() {
        if (!this.mIsConnected) {
            Log.w(this.TAG, "Condition provider service not yet bound.");
        }
        return this.mIsConnected;
    }

    private final class Provider extends IConditionProvider.Stub {
        private Provider() {
        }

        public void onConnected() {
            ConditionProviderService.this.mIsConnected = true;
            ConditionProviderService.this.mHandler.obtainMessage(1).sendToTarget();
        }

        public void onSubscribe(Uri conditionId) {
            ConditionProviderService.this.mHandler.obtainMessage(3, conditionId).sendToTarget();
        }

        public void onUnsubscribe(Uri conditionId) {
            ConditionProviderService.this.mHandler.obtainMessage(4, conditionId).sendToTarget();
        }
    }

    private final class H extends Handler {
        private static final int ON_CONNECTED = 1;
        private static final int ON_SUBSCRIBE = 3;
        private static final int ON_UNSUBSCRIBE = 4;

        private H() {
        }

        public void handleMessage(Message msg) {
            if (ConditionProviderService.this.mIsConnected) {
                try {
                    int i = msg.what;
                    if (i != 1) {
                        switch (i) {
                            case 3:
                                String name = "onSubscribe";
                                ConditionProviderService.this.onSubscribe((Uri) msg.obj);
                                return;
                            case 4:
                                String name2 = "onUnsubscribe";
                                ConditionProviderService.this.onUnsubscribe((Uri) msg.obj);
                                return;
                            default:
                                return;
                        }
                    } else {
                        String name3 = "onConnected";
                        ConditionProviderService.this.onConnected();
                    }
                } catch (Throwable t) {
                    String access$300 = ConditionProviderService.this.TAG;
                    Log.w(access$300, "Error running " + null, t);
                }
            }
        }
    }
}
