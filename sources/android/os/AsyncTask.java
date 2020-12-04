package android.os;

import android.annotation.UnsupportedAppUsage;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AsyncTask<Params, Progress, Result> {
    private static final int BACKUP_POOL_SIZE = 5;
    private static final int CORE_POOL_SIZE = 1;
    private static final int KEEP_ALIVE_SECONDS = 3;
    private static final String LOG_TAG = "AsyncTask";
    private static final int MAXIMUM_POOL_SIZE = 20;
    private static final int MESSAGE_POST_PROGRESS = 2;
    private static final int MESSAGE_POST_RESULT = 1;
    public static final Executor SERIAL_EXECUTOR = new SerialExecutor();
    public static final Executor THREAD_POOL_EXECUTOR;
    /* access modifiers changed from: private */
    public static ThreadPoolExecutor sBackupExecutor;
    /* access modifiers changed from: private */
    public static LinkedBlockingQueue<Runnable> sBackupExecutorQueue;
    @UnsupportedAppUsage
    private static volatile Executor sDefaultExecutor = SERIAL_EXECUTOR;
    private static InternalHandler sHandler;
    private static final RejectedExecutionHandler sRunOnSerialPolicy = new RejectedExecutionHandler() {
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            Log.w(AsyncTask.LOG_TAG, "Exceeded ThreadPoolExecutor pool size");
            synchronized (this) {
                if (AsyncTask.sBackupExecutor == null) {
                    LinkedBlockingQueue unused = AsyncTask.sBackupExecutorQueue = new LinkedBlockingQueue();
                    ThreadPoolExecutor unused2 = AsyncTask.sBackupExecutor = new ThreadPoolExecutor(5, 5, 3, TimeUnit.SECONDS, AsyncTask.sBackupExecutorQueue, AsyncTask.sThreadFactory);
                    AsyncTask.sBackupExecutor.allowCoreThreadTimeOut(true);
                }
            }
            AsyncTask.sBackupExecutor.execute(r);
        }
    };
    /* access modifiers changed from: private */
    public static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + this.mCount.getAndIncrement());
        }
    };
    /* access modifiers changed from: private */
    public final AtomicBoolean mCancelled;
    @UnsupportedAppUsage
    private final FutureTask<Result> mFuture;
    private final Handler mHandler;
    @UnsupportedAppUsage
    private volatile Status mStatus;
    /* access modifiers changed from: private */
    @UnsupportedAppUsage
    public final AtomicBoolean mTaskInvoked;
    @UnsupportedAppUsage
    private final WorkerRunnable<Params, Result> mWorker;

    public enum Status {
        PENDING,
        RUNNING,
        FINISHED
    }

    /* access modifiers changed from: protected */
    public abstract Result doInBackground(Params... paramsArr);

    static {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 3, TimeUnit.SECONDS, new SynchronousQueue(), sThreadFactory);
        threadPoolExecutor.setRejectedExecutionHandler(sRunOnSerialPolicy);
        THREAD_POOL_EXECUTOR = threadPoolExecutor;
    }

    private static class SerialExecutor implements Executor {
        Runnable mActive;
        final ArrayDeque<Runnable> mTasks;

        private SerialExecutor() {
            this.mTasks = new ArrayDeque<>();
        }

        public synchronized void execute(final Runnable r) {
            this.mTasks.offer(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } finally {
                        SerialExecutor.this.scheduleNext();
                    }
                }
            });
            if (this.mActive == null) {
                scheduleNext();
            }
        }

        /* access modifiers changed from: protected */
        public synchronized void scheduleNext() {
            Runnable poll = this.mTasks.poll();
            this.mActive = poll;
            if (poll != null) {
                AsyncTask.THREAD_POOL_EXECUTOR.execute(this.mActive);
            }
        }
    }

    private static Handler getMainHandler() {
        InternalHandler internalHandler;
        synchronized (AsyncTask.class) {
            if (sHandler == null) {
                sHandler = new InternalHandler(Looper.getMainLooper());
            }
            internalHandler = sHandler;
        }
        return internalHandler;
    }

    private Handler getHandler() {
        return this.mHandler;
    }

    @UnsupportedAppUsage
    public static void setDefaultExecutor(Executor exec) {
        sDefaultExecutor = exec;
    }

    public AsyncTask() {
        this((Looper) null);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public AsyncTask(Handler handler) {
        this(handler != null ? handler.getLooper() : null);
    }

    public AsyncTask(Looper callbackLooper) {
        Handler handler;
        this.mStatus = Status.PENDING;
        this.mCancelled = new AtomicBoolean();
        this.mTaskInvoked = new AtomicBoolean();
        if (callbackLooper == null || callbackLooper == Looper.getMainLooper()) {
            handler = getMainHandler();
        } else {
            handler = new Handler(callbackLooper);
        }
        this.mHandler = handler;
        this.mWorker = new WorkerRunnable<Params, Result>() {
            public Result call() throws Exception {
                AsyncTask.this.mTaskInvoked.set(true);
                Result result = null;
                try {
                    Process.setThreadPriority(10);
                    result = AsyncTask.this.doInBackground(this.mParams);
                    Binder.flushPendingCommands();
                    Object unused = AsyncTask.this.postResult(result);
                    return result;
                } catch (Throwable th) {
                    Object unused2 = AsyncTask.this.postResult(result);
                    throw th;
                }
            }
        };
        this.mFuture = new FutureTask<Result>(this.mWorker) {
            /* access modifiers changed from: protected */
            public void done() {
                try {
                    AsyncTask.this.postResultIfNotInvoked(get());
                } catch (InterruptedException e) {
                    Log.w(AsyncTask.LOG_TAG, (Throwable) e);
                } catch (ExecutionException e2) {
                    throw new RuntimeException("An error occurred while executing doInBackground()", e2.getCause());
                } catch (CancellationException e3) {
                    AsyncTask.this.postResultIfNotInvoked(null);
                }
            }
        };
    }

    /* access modifiers changed from: private */
    public void postResultIfNotInvoked(Result result) {
        if (!this.mTaskInvoked.get()) {
            postResult(result);
        }
    }

    /* access modifiers changed from: private */
    public Result postResult(Result result) {
        getHandler().obtainMessage(1, new AsyncTaskResult(this, result)).sendToTarget();
        return result;
    }

    public final Status getStatus() {
        return this.mStatus;
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Result result) {
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Progress... progressArr) {
    }

    /* access modifiers changed from: protected */
    public void onCancelled(Result result) {
        onCancelled();
    }

    /* access modifiers changed from: protected */
    public void onCancelled() {
    }

    public final boolean isCancelled() {
        return this.mCancelled.get();
    }

    public final boolean cancel(boolean mayInterruptIfRunning) {
        this.mCancelled.set(true);
        return this.mFuture.cancel(mayInterruptIfRunning);
    }

    public final Result get() throws InterruptedException, ExecutionException {
        return this.mFuture.get();
    }

    public final Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.mFuture.get(timeout, unit);
    }

    public final AsyncTask<Params, Progress, Result> execute(Params... params) {
        return executeOnExecutor(sDefaultExecutor, params);
    }

    public final AsyncTask<Params, Progress, Result> executeOnExecutor(Executor exec, Params... params) {
        if (this.mStatus != Status.PENDING) {
            switch (this.mStatus) {
                case RUNNING:
                    throw new IllegalStateException("Cannot execute task: the task is already running.");
                case FINISHED:
                    throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
            }
        }
        this.mStatus = Status.RUNNING;
        onPreExecute();
        this.mWorker.mParams = params;
        exec.execute(this.mFuture);
        return this;
    }

    public static void execute(Runnable runnable) {
        sDefaultExecutor.execute(runnable);
    }

    /* access modifiers changed from: protected */
    public final void publishProgress(Progress... values) {
        if (!isCancelled()) {
            getHandler().obtainMessage(2, new AsyncTaskResult(this, values)).sendToTarget();
        }
    }

    /* access modifiers changed from: private */
    public void finish(Result result) {
        if (isCancelled()) {
            onCancelled(result);
        } else {
            onPostExecute(result);
        }
        this.mStatus = Status.FINISHED;
    }

    private static class InternalHandler extends Handler {
        public InternalHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            AsyncTaskResult<?> result = (AsyncTaskResult) msg.obj;
            switch (msg.what) {
                case 1:
                    result.mTask.finish(result.mData[0]);
                    return;
                case 2:
                    result.mTask.onProgressUpdate(result.mData);
                    return;
                default:
                    return;
            }
        }
    }

    private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
        Params[] mParams;

        private WorkerRunnable() {
        }
    }

    private static class AsyncTaskResult<Data> {
        final Data[] mData;
        final AsyncTask mTask;

        AsyncTaskResult(AsyncTask task, Data... data) {
            this.mTask = task;
            this.mData = data;
        }
    }
}
