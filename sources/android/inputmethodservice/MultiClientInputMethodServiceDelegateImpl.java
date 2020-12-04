package android.inputmethodservice;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.MultiClientInputMethodServiceDelegate;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.InputChannel;
import android.view.KeyEvent;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.inputmethod.IMultiClientInputMethod;
import com.android.internal.inputmethod.IMultiClientInputMethodPrivilegedOperations;
import com.android.internal.inputmethod.MultiClientInputMethodPrivilegedOperations;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

final class MultiClientInputMethodServiceDelegateImpl {
    private static final String TAG = "MultiClientInputMethodServiceDelegateImpl";
    private final Context mContext;
    /* access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public int mInitializationPhase = 1;
    /* access modifiers changed from: private */
    public final Object mLock = new Object();
    /* access modifiers changed from: private */
    public final MultiClientInputMethodPrivilegedOperations mPrivOps = new MultiClientInputMethodPrivilegedOperations();
    /* access modifiers changed from: private */
    public final MultiClientInputMethodServiceDelegate.ServiceCallback mServiceCallback;

    @Retention(RetentionPolicy.SOURCE)
    private @interface InitializationPhase {
        public static final int INITIALIZE_CALLED = 3;
        public static final int INSTANTIATED = 1;
        public static final int ON_BIND_CALLED = 2;
        public static final int ON_DESTROY_CALLED = 5;
        public static final int ON_UNBIND_CALLED = 4;
    }

    MultiClientInputMethodServiceDelegateImpl(Context context, MultiClientInputMethodServiceDelegate.ServiceCallback serviceCallback) {
        this.mContext = context;
        this.mServiceCallback = serviceCallback;
    }

    /* access modifiers changed from: package-private */
    public void onDestroy() {
        synchronized (this.mLock) {
            int i = this.mInitializationPhase;
            if (i == 1 || i == 4) {
                this.mInitializationPhase = 5;
            } else {
                Log.e(TAG, "unexpected state=" + this.mInitializationPhase);
            }
        }
    }

    private static final class ServiceImpl extends IMultiClientInputMethod.Stub {
        private final WeakReference<MultiClientInputMethodServiceDelegateImpl> mImpl;

        ServiceImpl(MultiClientInputMethodServiceDelegateImpl service) {
            this.mImpl = new WeakReference<>(service);
        }

        public void initialize(IMultiClientInputMethodPrivilegedOperations privOps) {
            MultiClientInputMethodServiceDelegateImpl service = (MultiClientInputMethodServiceDelegateImpl) this.mImpl.get();
            if (service != null) {
                synchronized (service.mLock) {
                    if (service.mInitializationPhase != 2) {
                        Log.e(MultiClientInputMethodServiceDelegateImpl.TAG, "unexpected state=" + service.mInitializationPhase);
                    } else {
                        service.mPrivOps.set(privOps);
                        int unused = service.mInitializationPhase = 3;
                        service.mServiceCallback.initialized();
                    }
                }
            }
        }

        public void addClient(int clientId, int uid, int pid, int selfReportedDisplayId) {
            MultiClientInputMethodServiceDelegateImpl service = (MultiClientInputMethodServiceDelegateImpl) this.mImpl.get();
            if (service != null) {
                service.mServiceCallback.addClient(clientId, uid, pid, selfReportedDisplayId);
            }
        }

        public void removeClient(int clientId) {
            MultiClientInputMethodServiceDelegateImpl service = (MultiClientInputMethodServiceDelegateImpl) this.mImpl.get();
            if (service != null) {
                service.mServiceCallback.removeClient(clientId);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public IBinder onBind(Intent intent) {
        synchronized (this.mLock) {
            if (this.mInitializationPhase != 1) {
                Log.e(TAG, "unexpected state=" + this.mInitializationPhase);
                return null;
            }
            this.mInitializationPhase = 2;
            ServiceImpl serviceImpl = new ServiceImpl(this);
            return serviceImpl;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean onUnbind(Intent intent) {
        synchronized (this.mLock) {
            switch (this.mInitializationPhase) {
                case 2:
                case 3:
                    this.mInitializationPhase = 4;
                    this.mPrivOps.dispose();
                    break;
                default:
                    Log.e(TAG, "unexpected state=" + this.mInitializationPhase);
                    break;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public IBinder createInputMethodWindowToken(int displayId) {
        return this.mPrivOps.createInputMethodWindowToken(displayId);
    }

    /* access modifiers changed from: package-private */
    public void acceptClient(int clientId, MultiClientInputMethodServiceDelegate.ClientCallback clientCallback, KeyEvent.DispatcherState dispatcherState, Looper looper) {
        InputChannel[] channels = InputChannel.openInputChannelPair("MSIMS-session");
        InputChannel writeChannel = channels[0];
        try {
            MultiClientInputMethodClientCallbackAdaptor callbackAdaptor = new MultiClientInputMethodClientCallbackAdaptor(clientCallback, looper, dispatcherState, channels[1]);
            this.mPrivOps.acceptClient(clientId, callbackAdaptor.createIInputMethodSession(), callbackAdaptor.createIMultiClientInputMethodSession(), writeChannel);
        } finally {
            writeChannel.dispose();
        }
    }

    /* access modifiers changed from: package-private */
    public void reportImeWindowTarget(int clientId, int targetWindowHandle, IBinder imeWindowToken) {
        this.mPrivOps.reportImeWindowTarget(clientId, targetWindowHandle, imeWindowToken);
    }

    /* access modifiers changed from: package-private */
    public boolean isUidAllowedOnDisplay(int displayId, int uid) {
        return this.mPrivOps.isUidAllowedOnDisplay(displayId, uid);
    }

    /* access modifiers changed from: package-private */
    public void setActive(int clientId, boolean active) {
        this.mPrivOps.setActive(clientId, active);
    }
}
