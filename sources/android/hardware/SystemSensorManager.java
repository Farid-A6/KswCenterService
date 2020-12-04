package android.hardware;

import android.annotation.UnsupportedAppUsage;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.MemoryFile;
import android.os.MessageQueue;
import android.util.Log;
import android.util.SeempLog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.android.internal.annotations.GuardedBy;
import dalvik.system.CloseGuard;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemSensorManager extends SensorManager {
    private static final boolean DEBUG_DYNAMIC_SENSOR = true;
    private static final int MAX_LISTENER_COUNT = 128;
    private static final int MIN_DIRECT_CHANNEL_BUFFER_SIZE = 104;
    @GuardedBy({"sLock"})
    private static InjectEventQueue sInjectEventQueue = null;
    private static final Object sLock = new Object();
    @GuardedBy({"sLock"})
    private static boolean sNativeClassInited = false;
    /* access modifiers changed from: private */
    public final Context mContext;
    private BroadcastReceiver mDynamicSensorBroadcastReceiver;
    private HashMap<SensorManager.DynamicSensorCallback, Handler> mDynamicSensorCallbacks = new HashMap<>();
    /* access modifiers changed from: private */
    public boolean mDynamicSensorListDirty = true;
    private List<Sensor> mFullDynamicSensorsList = new ArrayList();
    private final ArrayList<Sensor> mFullSensorsList = new ArrayList<>();
    /* access modifiers changed from: private */
    public final HashMap<Integer, Sensor> mHandleToSensor = new HashMap<>();
    private final Looper mMainLooper;
    /* access modifiers changed from: private */
    public final long mNativeInstance;
    private final HashMap<SensorEventListener, SensorEventQueue> mSensorListeners = new HashMap<>();
    /* access modifiers changed from: private */
    public final int mTargetSdkLevel;
    private final HashMap<TriggerEventListener, TriggerEventQueue> mTriggerListeners = new HashMap<>();

    private static native void nativeClassInit();

    private static native int nativeConfigDirectChannel(long j, int i, int i2, int i3);

    private static native long nativeCreate(String str);

    private static native int nativeCreateDirectChannel(long j, long j2, int i, int i2, HardwareBuffer hardwareBuffer);

    private static native void nativeDestroyDirectChannel(long j, int i);

    private static native void nativeGetDynamicSensors(long j, List<Sensor> list);

    private static native boolean nativeGetSensorAtIndex(long j, Sensor sensor, int i);

    private static native boolean nativeIsDataInjectionEnabled(long j);

    private static native int nativeSetOperationParameter(long j, int i, int i2, float[] fArr, int[] iArr);

    public SystemSensorManager(Context context, Looper mainLooper) {
        synchronized (sLock) {
            if (!sNativeClassInited) {
                sNativeClassInited = true;
                nativeClassInit();
            }
        }
        this.mMainLooper = mainLooper;
        this.mTargetSdkLevel = context.getApplicationInfo().targetSdkVersion;
        this.mContext = context;
        this.mNativeInstance = nativeCreate(context.getOpPackageName());
        int index = 0;
        while (true) {
            Sensor sensor = new Sensor();
            if (nativeGetSensorAtIndex(this.mNativeInstance, sensor, index)) {
                this.mFullSensorsList.add(sensor);
                this.mHandleToSensor.put(Integer.valueOf(sensor.getHandle()), sensor);
                index++;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public List<Sensor> getFullSensorList() {
        return this.mFullSensorsList;
    }

    /* access modifiers changed from: protected */
    public List<Sensor> getFullDynamicSensorList() {
        setupDynamicSensorBroadcastReceiver();
        updateDynamicSensorList();
        return this.mFullDynamicSensorsList;
    }

    /* access modifiers changed from: protected */
    public boolean registerListenerImpl(SensorEventListener listener, Sensor sensor, int delayUs, Handler handler, int maxBatchReportLatencyUs, int reservedFlags) {
        String fullClassName;
        SeempLog.record_sensor_rate(381, sensor, delayUs);
        if (listener == null || sensor == null) {
            Log.e("SensorManager", "sensor or listener is null");
            return false;
        } else if (sensor.getReportingMode() == 2) {
            Log.e("SensorManager", "Trigger Sensors should use the requestTriggerSensor.");
            return false;
        } else if (maxBatchReportLatencyUs < 0 || delayUs < 0) {
            Log.e("SensorManager", "maxBatchReportLatencyUs and delayUs should be non-negative");
            return false;
        } else if (this.mSensorListeners.size() < 128) {
            synchronized (this.mSensorListeners) {
                SensorEventQueue queue = this.mSensorListeners.get(listener);
                if (queue == null) {
                    Looper looper = handler != null ? handler.getLooper() : this.mMainLooper;
                    if (listener.getClass().getEnclosingClass() != null) {
                        fullClassName = listener.getClass().getEnclosingClass().getName();
                    } else {
                        fullClassName = listener.getClass().getName();
                    }
                    SensorEventQueue queue2 = new SensorEventQueue(listener, looper, this, fullClassName);
                    if (!queue2.addSensor(sensor, delayUs, maxBatchReportLatencyUs)) {
                        queue2.dispose();
                        return false;
                    }
                    this.mSensorListeners.put(listener, queue2);
                    return true;
                }
                boolean addSensor = queue.addSensor(sensor, delayUs, maxBatchReportLatencyUs);
                return addSensor;
            }
        } else {
            throw new IllegalStateException("register failed, the sensor listeners size has exceeded the maximum limit 128");
        }
    }

    /* access modifiers changed from: protected */
    public void unregisterListenerImpl(SensorEventListener listener, Sensor sensor) {
        boolean result;
        SeempLog.record_sensor(382, sensor);
        if (sensor == null || sensor.getReportingMode() != 2) {
            synchronized (this.mSensorListeners) {
                SensorEventQueue queue = this.mSensorListeners.get(listener);
                if (queue != null) {
                    if (sensor == null) {
                        result = queue.removeAllSensors();
                    } else {
                        result = queue.removeSensor(sensor, true);
                    }
                    if (result && !queue.hasSensors()) {
                        this.mSensorListeners.remove(listener);
                        queue.dispose();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean requestTriggerSensorImpl(TriggerEventListener listener, Sensor sensor) {
        String fullClassName;
        if (sensor == null) {
            throw new IllegalArgumentException("sensor cannot be null");
        } else if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        } else if (sensor.getReportingMode() != 2) {
            return false;
        } else {
            if (this.mTriggerListeners.size() < 128) {
                synchronized (this.mTriggerListeners) {
                    TriggerEventQueue queue = this.mTriggerListeners.get(listener);
                    if (queue == null) {
                        if (listener.getClass().getEnclosingClass() != null) {
                            fullClassName = listener.getClass().getEnclosingClass().getName();
                        } else {
                            fullClassName = listener.getClass().getName();
                        }
                        TriggerEventQueue queue2 = new TriggerEventQueue(listener, this.mMainLooper, this, fullClassName);
                        if (!queue2.addSensor(sensor, 0, 0)) {
                            queue2.dispose();
                            return false;
                        }
                        this.mTriggerListeners.put(listener, queue2);
                        return true;
                    }
                    boolean addSensor = queue.addSensor(sensor, 0, 0);
                    return addSensor;
                }
            }
            throw new IllegalStateException("request failed, the trigger listeners size has exceeded the maximum limit 128");
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0034, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean cancelTriggerSensorImpl(android.hardware.TriggerEventListener r5, android.hardware.Sensor r6, boolean r7) {
        /*
            r4 = this;
            r0 = 0
            if (r6 == 0) goto L_0x000b
            int r1 = r6.getReportingMode()
            r2 = 2
            if (r1 == r2) goto L_0x000b
            return r0
        L_0x000b:
            java.util.HashMap<android.hardware.TriggerEventListener, android.hardware.SystemSensorManager$TriggerEventQueue> r1 = r4.mTriggerListeners
            monitor-enter(r1)
            java.util.HashMap<android.hardware.TriggerEventListener, android.hardware.SystemSensorManager$TriggerEventQueue> r2 = r4.mTriggerListeners     // Catch:{ all -> 0x0037 }
            java.lang.Object r2 = r2.get(r5)     // Catch:{ all -> 0x0037 }
            android.hardware.SystemSensorManager$TriggerEventQueue r2 = (android.hardware.SystemSensorManager.TriggerEventQueue) r2     // Catch:{ all -> 0x0037 }
            if (r2 == 0) goto L_0x0035
            if (r6 != 0) goto L_0x001f
            boolean r0 = r2.removeAllSensors()     // Catch:{ all -> 0x0037 }
            goto L_0x0023
        L_0x001f:
            boolean r0 = r2.removeSensor(r6, r7)     // Catch:{ all -> 0x0037 }
        L_0x0023:
            if (r0 == 0) goto L_0x0033
            boolean r3 = r2.hasSensors()     // Catch:{ all -> 0x0037 }
            if (r3 != 0) goto L_0x0033
            java.util.HashMap<android.hardware.TriggerEventListener, android.hardware.SystemSensorManager$TriggerEventQueue> r3 = r4.mTriggerListeners     // Catch:{ all -> 0x0037 }
            r3.remove(r5)     // Catch:{ all -> 0x0037 }
            r2.dispose()     // Catch:{ all -> 0x0037 }
        L_0x0033:
            monitor-exit(r1)     // Catch:{ all -> 0x0037 }
            return r0
        L_0x0035:
            monitor-exit(r1)     // Catch:{ all -> 0x0037 }
            return r0
        L_0x0037:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0037 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.SystemSensorManager.cancelTriggerSensorImpl(android.hardware.TriggerEventListener, android.hardware.Sensor, boolean):boolean");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001b, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean flushImpl(android.hardware.SensorEventListener r5) {
        /*
            r4 = this;
            if (r5 == 0) goto L_0x001f
            java.util.HashMap<android.hardware.SensorEventListener, android.hardware.SystemSensorManager$SensorEventQueue> r0 = r4.mSensorListeners
            monitor-enter(r0)
            java.util.HashMap<android.hardware.SensorEventListener, android.hardware.SystemSensorManager$SensorEventQueue> r1 = r4.mSensorListeners     // Catch:{ all -> 0x001c }
            java.lang.Object r1 = r1.get(r5)     // Catch:{ all -> 0x001c }
            android.hardware.SystemSensorManager$SensorEventQueue r1 = (android.hardware.SystemSensorManager.SensorEventQueue) r1     // Catch:{ all -> 0x001c }
            r2 = 0
            if (r1 != 0) goto L_0x0012
            monitor-exit(r0)     // Catch:{ all -> 0x001c }
            return r2
        L_0x0012:
            int r3 = r1.flush()     // Catch:{ all -> 0x001c }
            if (r3 != 0) goto L_0x001a
            r2 = 1
        L_0x001a:
            monitor-exit(r0)     // Catch:{ all -> 0x001c }
            return r2
        L_0x001c:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x001c }
            throw r1
        L_0x001f:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "listener cannot be null"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.SystemSensorManager.flushImpl(android.hardware.SensorEventListener):boolean");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004a, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x005a, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean initDataInjectionImpl(boolean r9) {
        /*
            r8 = this;
            java.lang.Object r0 = sLock
            monitor-enter(r0)
            r1 = 1
            if (r9 == 0) goto L_0x004d
            long r2 = r8.mNativeInstance     // Catch:{ all -> 0x004b }
            boolean r2 = nativeIsDataInjectionEnabled(r2)     // Catch:{ all -> 0x004b }
            r3 = 0
            if (r2 != 0) goto L_0x0018
            java.lang.String r1 = "SensorManager"
            java.lang.String r4 = "Data Injection mode not enabled"
            android.util.Log.e(r1, r4)     // Catch:{ all -> 0x004b }
            monitor-exit(r0)     // Catch:{ all -> 0x004b }
            return r3
        L_0x0018:
            android.hardware.SystemSensorManager$InjectEventQueue r4 = sInjectEventQueue     // Catch:{ all -> 0x004b }
            if (r4 != 0) goto L_0x0043
            android.hardware.SystemSensorManager$InjectEventQueue r4 = new android.hardware.SystemSensorManager$InjectEventQueue     // Catch:{ RuntimeException -> 0x002c }
            android.os.Looper r5 = r8.mMainLooper     // Catch:{ RuntimeException -> 0x002c }
            android.content.Context r6 = r8.mContext     // Catch:{ RuntimeException -> 0x002c }
            java.lang.String r6 = r6.getPackageName()     // Catch:{ RuntimeException -> 0x002c }
            r4.<init>(r5, r8, r6)     // Catch:{ RuntimeException -> 0x002c }
            sInjectEventQueue = r4     // Catch:{ RuntimeException -> 0x002c }
            goto L_0x0043
        L_0x002c:
            r4 = move-exception
            java.lang.String r5 = "SensorManager"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x004b }
            r6.<init>()     // Catch:{ all -> 0x004b }
            java.lang.String r7 = "Cannot create InjectEventQueue: "
            r6.append(r7)     // Catch:{ all -> 0x004b }
            r6.append(r4)     // Catch:{ all -> 0x004b }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x004b }
            android.util.Log.e(r5, r6)     // Catch:{ all -> 0x004b }
        L_0x0043:
            android.hardware.SystemSensorManager$InjectEventQueue r4 = sInjectEventQueue     // Catch:{ all -> 0x004b }
            if (r4 == 0) goto L_0x0048
            goto L_0x0049
        L_0x0048:
            r1 = r3
        L_0x0049:
            monitor-exit(r0)     // Catch:{ all -> 0x004b }
            return r1
        L_0x004b:
            r1 = move-exception
            goto L_0x005b
        L_0x004d:
            android.hardware.SystemSensorManager$InjectEventQueue r2 = sInjectEventQueue     // Catch:{ all -> 0x004b }
            if (r2 == 0) goto L_0x0059
            android.hardware.SystemSensorManager$InjectEventQueue r2 = sInjectEventQueue     // Catch:{ all -> 0x004b }
            r2.dispose()     // Catch:{ all -> 0x004b }
            r2 = 0
            sInjectEventQueue = r2     // Catch:{ all -> 0x004b }
        L_0x0059:
            monitor-exit(r0)     // Catch:{ all -> 0x004b }
            return r1
        L_0x005b:
            monitor-exit(r0)     // Catch:{ all -> 0x004b }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.SystemSensorManager.initDataInjectionImpl(boolean):boolean");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002d, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean injectSensorDataImpl(android.hardware.Sensor r10, float[] r11, int r12, long r13) {
        /*
            r9 = this;
            java.lang.Object r0 = sLock
            monitor-enter(r0)
            android.hardware.SystemSensorManager$InjectEventQueue r1 = sInjectEventQueue     // Catch:{ all -> 0x002e }
            r2 = 0
            if (r1 != 0) goto L_0x0011
            java.lang.String r1 = "SensorManager"
            java.lang.String r3 = "Data injection mode not activated before calling injectSensorData"
            android.util.Log.e(r1, r3)     // Catch:{ all -> 0x002e }
            monitor-exit(r0)     // Catch:{ all -> 0x002e }
            return r2
        L_0x0011:
            android.hardware.SystemSensorManager$InjectEventQueue r3 = sInjectEventQueue     // Catch:{ all -> 0x002e }
            int r4 = r10.getHandle()     // Catch:{ all -> 0x002e }
            r5 = r11
            r6 = r12
            r7 = r13
            int r1 = r3.injectSensorData(r4, r5, r6, r7)     // Catch:{ all -> 0x002e }
            if (r1 == 0) goto L_0x0028
            android.hardware.SystemSensorManager$InjectEventQueue r3 = sInjectEventQueue     // Catch:{ all -> 0x002e }
            r3.dispose()     // Catch:{ all -> 0x002e }
            r3 = 0
            sInjectEventQueue = r3     // Catch:{ all -> 0x002e }
        L_0x0028:
            if (r1 != 0) goto L_0x002c
            r2 = 1
        L_0x002c:
            monitor-exit(r0)     // Catch:{ all -> 0x002e }
            return r2
        L_0x002e:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002e }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.SystemSensorManager.injectSensorDataImpl(android.hardware.Sensor, float[], int, long):boolean");
    }

    private void cleanupSensorConnection(Sensor sensor) {
        this.mHandleToSensor.remove(Integer.valueOf(sensor.getHandle()));
        if (sensor.getReportingMode() == 2) {
            synchronized (this.mTriggerListeners) {
                for (TriggerEventListener l : new HashMap<>(this.mTriggerListeners).keySet()) {
                    Log.i("SensorManager", "removed trigger listener" + l.toString() + " due to sensor disconnection");
                    cancelTriggerSensorImpl(l, sensor, true);
                }
            }
            return;
        }
        synchronized (this.mSensorListeners) {
            for (SensorEventListener l2 : new HashMap<>(this.mSensorListeners).keySet()) {
                Log.i("SensorManager", "removed event listener" + l2.toString() + " due to sensor disconnection");
                unregisterListenerImpl(l2, sensor);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateDynamicSensorList() {
        synchronized (this.mFullDynamicSensorsList) {
            if (this.mDynamicSensorListDirty) {
                List<Sensor> list = new ArrayList<>();
                nativeGetDynamicSensors(this.mNativeInstance, list);
                List<Sensor> updatedList = new ArrayList<>();
                final List<Sensor> addedList = new ArrayList<>();
                final List<Sensor> removedList = new ArrayList<>();
                if (diffSortedSensorList(this.mFullDynamicSensorsList, list, updatedList, addedList, removedList)) {
                    Log.i("SensorManager", "DYNS dynamic sensor list cached should be updated");
                    this.mFullDynamicSensorsList = updatedList;
                    for (Sensor s : addedList) {
                        this.mHandleToSensor.put(Integer.valueOf(s.getHandle()), s);
                    }
                    Handler mainHandler = new Handler(this.mContext.getMainLooper());
                    for (Map.Entry<SensorManager.DynamicSensorCallback, Handler> entry : this.mDynamicSensorCallbacks.entrySet()) {
                        final SensorManager.DynamicSensorCallback callback = entry.getKey();
                        (entry.getValue() == null ? mainHandler : entry.getValue()).post(new Runnable() {
                            public void run() {
                                for (Sensor s : addedList) {
                                    callback.onDynamicSensorConnected(s);
                                }
                                for (Sensor s2 : removedList) {
                                    callback.onDynamicSensorDisconnected(s2);
                                }
                            }
                        });
                    }
                    for (Sensor s2 : removedList) {
                        cleanupSensorConnection(s2);
                    }
                }
                this.mDynamicSensorListDirty = false;
            }
        }
    }

    private void setupDynamicSensorBroadcastReceiver() {
        if (this.mDynamicSensorBroadcastReceiver == null) {
            this.mDynamicSensorBroadcastReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction() == Intent.ACTION_DYNAMIC_SENSOR_CHANGED) {
                        Log.i("SensorManager", "DYNS received DYNAMIC_SENSOR_CHANED broadcast");
                        boolean unused = SystemSensorManager.this.mDynamicSensorListDirty = true;
                        SystemSensorManager.this.updateDynamicSensorList();
                    }
                }
            };
            IntentFilter filter = new IntentFilter("dynamic_sensor_change");
            filter.addAction(Intent.ACTION_DYNAMIC_SENSOR_CHANGED);
            this.mContext.registerReceiver(this.mDynamicSensorBroadcastReceiver, filter);
        }
    }

    private void teardownDynamicSensorBroadcastReceiver() {
        this.mDynamicSensorCallbacks.clear();
        this.mContext.unregisterReceiver(this.mDynamicSensorBroadcastReceiver);
        this.mDynamicSensorBroadcastReceiver = null;
    }

    /* access modifiers changed from: protected */
    public void registerDynamicSensorCallbackImpl(SensorManager.DynamicSensorCallback callback, Handler handler) {
        Log.i("SensorManager", "DYNS Register dynamic sensor callback");
        if (callback == null) {
            throw new IllegalArgumentException("callback cannot be null");
        } else if (!this.mDynamicSensorCallbacks.containsKey(callback)) {
            setupDynamicSensorBroadcastReceiver();
            this.mDynamicSensorCallbacks.put(callback, handler);
        }
    }

    /* access modifiers changed from: protected */
    public void unregisterDynamicSensorCallbackImpl(SensorManager.DynamicSensorCallback callback) {
        Log.i("SensorManager", "Removing dynamic sensor listerner");
        this.mDynamicSensorCallbacks.remove(callback);
    }

    private static boolean diffSortedSensorList(List<Sensor> oldList, List<Sensor> newList, List<Sensor> updated, List<Sensor> added, List<Sensor> removed) {
        boolean changed = false;
        int i = 0;
        int j = 0;
        while (true) {
            if (j < oldList.size() && (i >= newList.size() || newList.get(i).getHandle() > oldList.get(j).getHandle())) {
                changed = true;
                if (removed != null) {
                    removed.add(oldList.get(j));
                }
                j++;
            } else if (i < newList.size() && (j >= oldList.size() || newList.get(i).getHandle() < oldList.get(j).getHandle())) {
                changed = true;
                if (added != null) {
                    added.add(newList.get(i));
                }
                if (updated != null) {
                    updated.add(newList.get(i));
                }
                i++;
            } else if (i >= newList.size() || j >= oldList.size() || newList.get(i).getHandle() != oldList.get(j).getHandle()) {
                return changed;
            } else {
                if (updated != null) {
                    updated.add(oldList.get(j));
                }
                i++;
                j++;
            }
        }
        return changed;
    }

    /* access modifiers changed from: protected */
    public int configureDirectChannelImpl(SensorDirectChannel channel, Sensor sensor, int rate) {
        if (!channel.isOpen()) {
            throw new IllegalStateException("channel is closed");
        } else if (rate < 0 || rate > 3) {
            throw new IllegalArgumentException("rate parameter invalid");
        } else if (sensor != null || rate == 0) {
            int ret = nativeConfigDirectChannel(this.mNativeInstance, channel.getNativeHandle(), sensor == null ? -1 : sensor.getHandle(), rate);
            if (rate == 0) {
                if (ret == 0) {
                    return 1;
                }
                return 0;
            } else if (ret > 0) {
                return ret;
            } else {
                return 0;
            }
        } else {
            throw new IllegalArgumentException("when sensor is null, rate can only be DIRECT_RATE_STOP");
        }
    }

    /* access modifiers changed from: protected */
    public SensorDirectChannel createDirectChannelImpl(MemoryFile memoryFile, HardwareBuffer hardwareBuffer) {
        long size;
        int id;
        int type;
        if (memoryFile != null) {
            try {
                int fd = memoryFile.getFileDescriptor().getInt$();
                if (memoryFile.length() >= 104) {
                    size = (long) memoryFile.length();
                    id = nativeCreateDirectChannel(this.mNativeInstance, size, 1, fd, (HardwareBuffer) null);
                    if (id > 0) {
                        type = 1;
                    } else {
                        throw new UncheckedIOException(new IOException("create MemoryFile direct channel failed " + id));
                    }
                } else {
                    throw new IllegalArgumentException("Size of MemoryFile has to be greater than 104");
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("MemoryFile object is not valid");
            }
        } else if (hardwareBuffer == null) {
            throw new NullPointerException("shared memory object cannot be null");
        } else if (hardwareBuffer.getFormat() != 33) {
            throw new IllegalArgumentException("Format of HardwareBuffer must be BLOB");
        } else if (hardwareBuffer.getHeight() != 1) {
            throw new IllegalArgumentException("Height of HardwareBuffer must be 1");
        } else if (hardwareBuffer.getWidth() < 104) {
            throw new IllegalArgumentException("Width if HaradwareBuffer must be greater than 104");
        } else if ((hardwareBuffer.getUsage() & 8388608) != 0) {
            size = (long) hardwareBuffer.getWidth();
            id = nativeCreateDirectChannel(this.mNativeInstance, size, 2, -1, hardwareBuffer);
            if (id > 0) {
                type = 2;
            } else {
                throw new UncheckedIOException(new IOException("create HardwareBuffer direct channel failed " + id));
            }
        } else {
            throw new IllegalArgumentException("HardwareBuffer must set usage flag USAGE_SENSOR_DIRECT_DATA");
        }
        int type2 = type;
        return new SensorDirectChannel(this, id, type2, size);
    }

    /* access modifiers changed from: protected */
    public void destroyDirectChannelImpl(SensorDirectChannel channel) {
        if (channel != null) {
            nativeDestroyDirectChannel(this.mNativeInstance, channel.getNativeHandle());
        }
    }

    private static abstract class BaseEventQueue {
        protected static final int OPERATING_MODE_DATA_INJECTION = 1;
        protected static final int OPERATING_MODE_NORMAL = 0;
        private final SparseBooleanArray mActiveSensors = new SparseBooleanArray();
        private final CloseGuard mCloseGuard = CloseGuard.get();
        protected final SystemSensorManager mManager;
        private long mNativeSensorEventQueue;
        protected final SparseIntArray mSensorAccuracies = new SparseIntArray();

        private static native void nativeDestroySensorEventQueue(long j);

        private static native int nativeDisableSensor(long j, int i);

        private static native int nativeEnableSensor(long j, int i, int i2, int i3);

        private static native int nativeFlushSensor(long j);

        private static native long nativeInitBaseEventQueue(long j, WeakReference<BaseEventQueue> weakReference, MessageQueue messageQueue, String str, int i, String str2);

        private static native int nativeInjectSensorData(long j, int i, float[] fArr, int i2, long j2);

        /* access modifiers changed from: protected */
        public abstract void addSensorEvent(Sensor sensor);

        /* access modifiers changed from: protected */
        @UnsupportedAppUsage
        public abstract void dispatchFlushCompleteEvent(int i);

        /* access modifiers changed from: protected */
        @UnsupportedAppUsage
        public abstract void dispatchSensorEvent(int i, float[] fArr, int i2, long j);

        /* access modifiers changed from: protected */
        public abstract void removeSensorEvent(Sensor sensor);

        BaseEventQueue(Looper looper, SystemSensorManager manager, int mode, String packageName) {
            this.mNativeSensorEventQueue = nativeInitBaseEventQueue(manager.mNativeInstance, new WeakReference(this), looper.getQueue(), packageName == null ? "" : packageName, mode, manager.mContext.getOpPackageName());
            this.mCloseGuard.open("dispose");
            this.mManager = manager;
        }

        public void dispose() {
            dispose(false);
        }

        public boolean addSensor(Sensor sensor, int delayUs, int maxBatchReportLatencyUs) {
            int handle = sensor.getHandle();
            if (this.mActiveSensors.get(handle)) {
                return false;
            }
            this.mActiveSensors.put(handle, true);
            addSensorEvent(sensor);
            if (enableSensor(sensor, delayUs, maxBatchReportLatencyUs) == 0 || (maxBatchReportLatencyUs != 0 && (maxBatchReportLatencyUs <= 0 || enableSensor(sensor, delayUs, 0) == 0))) {
                return true;
            }
            removeSensor(sensor, false);
            return false;
        }

        public boolean removeAllSensors() {
            for (int i = 0; i < this.mActiveSensors.size(); i++) {
                if (this.mActiveSensors.valueAt(i)) {
                    int handle = this.mActiveSensors.keyAt(i);
                    Sensor sensor = (Sensor) this.mManager.mHandleToSensor.get(Integer.valueOf(handle));
                    if (sensor != null) {
                        disableSensor(sensor);
                        this.mActiveSensors.put(handle, false);
                        removeSensorEvent(sensor);
                    }
                }
            }
            return true;
        }

        public boolean removeSensor(Sensor sensor, boolean disable) {
            if (!this.mActiveSensors.get(sensor.getHandle())) {
                return false;
            }
            if (disable) {
                disableSensor(sensor);
            }
            this.mActiveSensors.put(sensor.getHandle(), false);
            removeSensorEvent(sensor);
            return true;
        }

        public int flush() {
            if (this.mNativeSensorEventQueue != 0) {
                return nativeFlushSensor(this.mNativeSensorEventQueue);
            }
            throw new NullPointerException();
        }

        public boolean hasSensors() {
            return this.mActiveSensors.indexOfValue(true) >= 0;
        }

        /* access modifiers changed from: protected */
        public void finalize() throws Throwable {
            try {
                dispose(true);
            } finally {
                super.finalize();
            }
        }

        private void dispose(boolean finalized) {
            if (this.mCloseGuard != null) {
                if (finalized) {
                    this.mCloseGuard.warnIfOpen();
                }
                this.mCloseGuard.close();
            }
            if (this.mNativeSensorEventQueue != 0) {
                nativeDestroySensorEventQueue(this.mNativeSensorEventQueue);
                this.mNativeSensorEventQueue = 0;
            }
        }

        private int enableSensor(Sensor sensor, int rateUs, int maxBatchReportLatencyUs) {
            if (this.mNativeSensorEventQueue == 0) {
                throw new NullPointerException();
            } else if (sensor != null) {
                return nativeEnableSensor(this.mNativeSensorEventQueue, sensor.getHandle(), rateUs, maxBatchReportLatencyUs);
            } else {
                throw new NullPointerException();
            }
        }

        /* access modifiers changed from: protected */
        public int injectSensorDataBase(int handle, float[] values, int accuracy, long timestamp) {
            return nativeInjectSensorData(this.mNativeSensorEventQueue, handle, values, accuracy, timestamp);
        }

        private int disableSensor(Sensor sensor) {
            if (this.mNativeSensorEventQueue == 0) {
                throw new NullPointerException();
            } else if (sensor != null) {
                return nativeDisableSensor(this.mNativeSensorEventQueue, sensor.getHandle());
            } else {
                throw new NullPointerException();
            }
        }

        /* access modifiers changed from: protected */
        @UnsupportedAppUsage
        public void dispatchAdditionalInfoEvent(int handle, int type, int serial, float[] floatValues, int[] intValues) {
        }
    }

    static final class SensorEventQueue extends BaseEventQueue {
        private final SensorEventListener mListener;
        private final SparseArray<SensorEvent> mSensorsEvents = new SparseArray<>();

        public SensorEventQueue(SensorEventListener listener, Looper looper, SystemSensorManager manager, String packageName) {
            super(looper, manager, 0, packageName);
            this.mListener = listener;
        }

        public void addSensorEvent(Sensor sensor) {
            SensorEvent t = new SensorEvent(Sensor.getMaxLengthValuesArray(sensor, this.mManager.mTargetSdkLevel));
            synchronized (this.mSensorsEvents) {
                this.mSensorsEvents.put(sensor.getHandle(), t);
            }
        }

        public void removeSensorEvent(Sensor sensor) {
            synchronized (this.mSensorsEvents) {
                this.mSensorsEvents.delete(sensor.getHandle());
            }
        }

        /* access modifiers changed from: protected */
        public void dispatchSensorEvent(int handle, float[] values, int inAccuracy, long timestamp) {
            SensorEvent t;
            Sensor sensor = (Sensor) this.mManager.mHandleToSensor.get(Integer.valueOf(handle));
            if (sensor != null) {
                synchronized (this.mSensorsEvents) {
                    t = this.mSensorsEvents.get(handle);
                }
                if (t != null) {
                    System.arraycopy(values, 0, t.values, 0, t.values.length);
                    t.timestamp = timestamp;
                    t.accuracy = inAccuracy;
                    t.sensor = sensor;
                    int accuracy = this.mSensorAccuracies.get(handle);
                    if (t.accuracy >= 0 && accuracy != t.accuracy) {
                        this.mSensorAccuracies.put(handle, t.accuracy);
                        this.mListener.onAccuracyChanged(t.sensor, t.accuracy);
                    }
                    this.mListener.onSensorChanged(t);
                }
            }
        }

        /* access modifiers changed from: protected */
        public void dispatchFlushCompleteEvent(int handle) {
            Sensor sensor;
            if ((this.mListener instanceof SensorEventListener2) && (sensor = (Sensor) this.mManager.mHandleToSensor.get(Integer.valueOf(handle))) != null) {
                ((SensorEventListener2) this.mListener).onFlushCompleted(sensor);
            }
        }

        /* access modifiers changed from: protected */
        public void dispatchAdditionalInfoEvent(int handle, int type, int serial, float[] floatValues, int[] intValues) {
            Sensor sensor;
            if ((this.mListener instanceof SensorEventCallback) && (sensor = (Sensor) this.mManager.mHandleToSensor.get(Integer.valueOf(handle))) != null) {
                ((SensorEventCallback) this.mListener).onSensorAdditionalInfo(new SensorAdditionalInfo(sensor, type, serial, intValues, floatValues));
            }
        }
    }

    static final class TriggerEventQueue extends BaseEventQueue {
        private final TriggerEventListener mListener;
        private final SparseArray<TriggerEvent> mTriggerEvents = new SparseArray<>();

        public TriggerEventQueue(TriggerEventListener listener, Looper looper, SystemSensorManager manager, String packageName) {
            super(looper, manager, 0, packageName);
            this.mListener = listener;
        }

        public void addSensorEvent(Sensor sensor) {
            TriggerEvent t = new TriggerEvent(Sensor.getMaxLengthValuesArray(sensor, this.mManager.mTargetSdkLevel));
            synchronized (this.mTriggerEvents) {
                this.mTriggerEvents.put(sensor.getHandle(), t);
            }
        }

        public void removeSensorEvent(Sensor sensor) {
            synchronized (this.mTriggerEvents) {
                this.mTriggerEvents.delete(sensor.getHandle());
            }
        }

        /* access modifiers changed from: protected */
        public void dispatchSensorEvent(int handle, float[] values, int accuracy, long timestamp) {
            TriggerEvent t;
            Sensor sensor = (Sensor) this.mManager.mHandleToSensor.get(Integer.valueOf(handle));
            if (sensor != null) {
                synchronized (this.mTriggerEvents) {
                    t = this.mTriggerEvents.get(handle);
                }
                if (t == null) {
                    Log.e("SensorManager", "Error: Trigger Event is null for Sensor: " + sensor);
                    return;
                }
                System.arraycopy(values, 0, t.values, 0, t.values.length);
                t.timestamp = timestamp;
                t.sensor = sensor;
                this.mManager.cancelTriggerSensorImpl(this.mListener, sensor, false);
                this.mListener.onTrigger(t);
            }
        }

        /* access modifiers changed from: protected */
        public void dispatchFlushCompleteEvent(int handle) {
        }
    }

    final class InjectEventQueue extends BaseEventQueue {
        public InjectEventQueue(Looper looper, SystemSensorManager manager, String packageName) {
            super(looper, manager, 1, packageName);
        }

        /* access modifiers changed from: package-private */
        public int injectSensorData(int handle, float[] values, int accuracy, long timestamp) {
            return injectSensorDataBase(handle, values, accuracy, timestamp);
        }

        /* access modifiers changed from: protected */
        public void dispatchSensorEvent(int handle, float[] values, int accuracy, long timestamp) {
        }

        /* access modifiers changed from: protected */
        public void dispatchFlushCompleteEvent(int handle) {
        }

        /* access modifiers changed from: protected */
        public void addSensorEvent(Sensor sensor) {
        }

        /* access modifiers changed from: protected */
        public void removeSensorEvent(Sensor sensor) {
        }
    }

    /* access modifiers changed from: protected */
    public boolean setOperationParameterImpl(SensorAdditionalInfo parameter) {
        int handle = -1;
        if (parameter.sensor != null) {
            handle = parameter.sensor.getHandle();
        }
        return nativeSetOperationParameter(this.mNativeInstance, handle, parameter.type, parameter.floatValues, parameter.intValues) == 0;
    }
}
