package android.os;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.BugreportManager;
import android.os.IDumpstateListener;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.Executor;
import libcore.io.IoUtils;

@SystemApi
public final class BugreportManager {
    private static final String TAG = "BugreportManager";
    private final IDumpstate mBinder;
    private final Context mContext;

    public BugreportManager(Context context, IDumpstate binder) {
        this.mContext = context;
        this.mBinder = binder;
    }

    public static abstract class BugreportCallback {
        public static final int BUGREPORT_ERROR_ANOTHER_REPORT_IN_PROGRESS = 5;
        public static final int BUGREPORT_ERROR_INVALID_INPUT = 1;
        public static final int BUGREPORT_ERROR_RUNTIME = 2;
        public static final int BUGREPORT_ERROR_USER_CONSENT_TIMED_OUT = 4;
        public static final int BUGREPORT_ERROR_USER_DENIED_CONSENT = 3;

        @Retention(RetentionPolicy.SOURCE)
        public @interface BugreportErrorCode {
        }

        public void onProgress(float progress) {
        }

        public void onError(int errorCode) {
        }

        public void onFinished() {
        }
    }

    public void startBugreport(ParcelFileDescriptor bugreportFd, ParcelFileDescriptor screenshotFd, BugreportParams params, Executor executor, BugreportCallback callback) {
        try {
            Preconditions.checkNotNull(bugreportFd);
            Preconditions.checkNotNull(params);
            Preconditions.checkNotNull(executor);
            Preconditions.checkNotNull(callback);
            if (screenshotFd == null) {
                screenshotFd = ParcelFileDescriptor.open(new File("/dev/null"), 268435456);
            }
            this.mBinder.startBugreport(-1, this.mContext.getOpPackageName(), bugreportFd.getFileDescriptor(), screenshotFd.getFileDescriptor(), params.getMode(), new DumpstateListener(executor, callback));
            IoUtils.closeQuietly(bugreportFd);
            if (screenshotFd == null) {
                return;
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        } catch (FileNotFoundException e2) {
            Log.wtf(TAG, "Not able to find /dev/null file: ", e2);
            IoUtils.closeQuietly(bugreportFd);
            if (screenshotFd == null) {
                return;
            }
        } catch (Throwable th) {
            IoUtils.closeQuietly(bugreportFd);
            if (screenshotFd != null) {
                IoUtils.closeQuietly(screenshotFd);
            }
            throw th;
        }
        IoUtils.closeQuietly(screenshotFd);
    }

    public void cancelBugreport() {
        try {
            this.mBinder.cancelBugreport();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private final class DumpstateListener extends IDumpstateListener.Stub {
        private final BugreportCallback mCallback;
        private final Executor mExecutor;

        DumpstateListener(Executor executor, BugreportCallback callback) {
            this.mExecutor = executor;
            this.mCallback = callback;
        }

        public void onProgress(int progress) throws RemoteException {
            long identity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable(progress) {
                    private final /* synthetic */ int f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void run() {
                        BugreportManager.DumpstateListener.this.mCallback.onProgress((float) this.f$1);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(identity);
            }
        }

        public void onError(int errorCode) throws RemoteException {
            long identity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable(errorCode) {
                    private final /* synthetic */ int f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void run() {
                        BugreportManager.DumpstateListener.this.mCallback.onError(this.f$1);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(identity);
            }
        }

        public void onFinished() throws RemoteException {
            long identity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() {
                    public final void run() {
                        BugreportManager.DumpstateListener.this.mCallback.onFinished();
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(identity);
            }
        }

        public void onProgressUpdated(int progress) throws RemoteException {
        }

        public void onMaxProgressUpdated(int maxProgress) throws RemoteException {
        }

        public void onSectionComplete(String title, int status, int size, int durationMs) throws RemoteException {
        }
    }
}
