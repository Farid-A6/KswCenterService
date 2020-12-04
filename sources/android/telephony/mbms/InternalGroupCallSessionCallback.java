package android.telephony.mbms;

import android.os.Binder;
import android.telephony.mbms.IMbmsGroupCallSessionCallback;
import java.util.List;
import java.util.concurrent.Executor;

public class InternalGroupCallSessionCallback extends IMbmsGroupCallSessionCallback.Stub {
    /* access modifiers changed from: private */
    public final MbmsGroupCallSessionCallback mAppCallback;
    private final Executor mExecutor;
    private volatile boolean mIsStopped = false;

    public InternalGroupCallSessionCallback(MbmsGroupCallSessionCallback appCallback, Executor executor) {
        this.mAppCallback = appCallback;
        this.mExecutor = executor;
    }

    public void onError(final int errorCode, final String message) {
        if (!this.mIsStopped) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() {
                    public void run() {
                        InternalGroupCallSessionCallback.this.mAppCallback.onError(errorCode, message);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }
    }

    public void onAvailableSaisUpdated(final List currentSais, final List availableSais) {
        if (!this.mIsStopped) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() {
                    public void run() {
                        InternalGroupCallSessionCallback.this.mAppCallback.onAvailableSaisUpdated(currentSais, availableSais);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }
    }

    public void onServiceInterfaceAvailable(final String interfaceName, final int index) {
        if (!this.mIsStopped) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() {
                    public void run() {
                        InternalGroupCallSessionCallback.this.mAppCallback.onServiceInterfaceAvailable(interfaceName, index);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }
    }

    public void onMiddlewareReady() {
        if (!this.mIsStopped) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() {
                    public void run() {
                        InternalGroupCallSessionCallback.this.mAppCallback.onMiddlewareReady();
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }
    }

    public void stop() {
        this.mIsStopped = true;
    }
}
