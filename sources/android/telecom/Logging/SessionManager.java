package android.telecom.Logging;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telecom.Log;
import android.telecom.Logging.Session;
import android.util.Base64;
import com.android.internal.annotations.VisibleForTesting;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final long DEFAULT_SESSION_TIMEOUT_MS = 30000;
    private static final String LOGGING_TAG = "Logging";
    private static final long SESSION_ID_ROLLOVER_THRESHOLD = 262144;
    private static final String TIMEOUTS_PREFIX = "telecom.";
    @VisibleForTesting
    public Runnable mCleanStaleSessions = new Runnable() {
        public final void run() {
            SessionManager.this.cleanupStaleSessions(SessionManager.this.getSessionCleanupTimeoutMs());
        }
    };
    private Context mContext;
    @VisibleForTesting
    public ICurrentThreadId mCurrentThreadId = $$Lambda$L5F_SL2jOCUETYvgdB36aGwY50E.INSTANCE;
    private Handler mSessionCleanupHandler = new Handler(Looper.getMainLooper());
    private ISessionCleanupTimeoutMs mSessionCleanupTimeoutMs = new ISessionCleanupTimeoutMs() {
        public final long get() {
            return SessionManager.lambda$new$1(SessionManager.this);
        }
    };
    private List<ISessionListener> mSessionListeners = new ArrayList();
    @VisibleForTesting
    public ConcurrentHashMap<Integer, Session> mSessionMapper = new ConcurrentHashMap<>(100);
    private int sCodeEntryCounter = 0;

    public interface ICurrentThreadId {
        int get();
    }

    private interface ISessionCleanupTimeoutMs {
        long get();
    }

    public interface ISessionIdQueryHandler {
        String getSessionId();
    }

    public interface ISessionListener {
        void sessionComplete(String str, long j);
    }

    public static /* synthetic */ long lambda$new$1(SessionManager sessionManager) {
        if (sessionManager.mContext == null) {
            return 30000;
        }
        return sessionManager.getCleanupTimeout(sessionManager.mContext);
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    private long getSessionCleanupTimeoutMs() {
        return this.mSessionCleanupTimeoutMs.get();
    }

    private synchronized void resetStaleSessionTimer() {
        this.mSessionCleanupHandler.removeCallbacksAndMessages((Object) null);
        if (this.mCleanStaleSessions != null) {
            this.mSessionCleanupHandler.postDelayed(this.mCleanStaleSessions, getSessionCleanupTimeoutMs());
        }
    }

    public synchronized void startSession(Session.Info info, String shortMethodName, String callerIdentification) {
        if (info == null) {
            try {
                startSession(shortMethodName, callerIdentification);
            } catch (Throwable th) {
                throw th;
            }
        } else {
            startExternalSession(info, shortMethodName);
        }
    }

    public synchronized void startSession(String shortMethodName, String callerIdentification) {
        resetStaleSessionTimer();
        int threadId = getCallingThreadId();
        if (this.mSessionMapper.get(Integer.valueOf(threadId)) != null) {
            continueSession(createSubsession(true), shortMethodName);
            return;
        }
        Log.d(LOGGING_TAG, Session.START_SESSION, new Object[0]);
        this.mSessionMapper.put(Integer.valueOf(threadId), new Session(getNextSessionID(), shortMethodName, System.currentTimeMillis(), false, callerIdentification));
    }

    public synchronized void startExternalSession(Session.Info sessionInfo, String shortMethodName) {
        if (sessionInfo != null) {
            int threadId = getCallingThreadId();
            if (this.mSessionMapper.get(Integer.valueOf(threadId)) != null) {
                Log.w(LOGGING_TAG, "trying to start an external session with a session already active.", new Object[0]);
                return;
            }
            Log.d(LOGGING_TAG, Session.START_EXTERNAL_SESSION, new Object[0]);
            Session session = new Session(Session.EXTERNAL_INDICATOR + sessionInfo.sessionId, sessionInfo.methodPath, System.currentTimeMillis(), false, (String) null);
            session.setIsExternal(true);
            session.markSessionCompleted(-1);
            this.mSessionMapper.put(Integer.valueOf(threadId), session);
            continueSession(createSubsession(), shortMethodName);
        }
    }

    public Session createSubsession() {
        return createSubsession(false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0064, code lost:
        return r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized android.telecom.Logging.Session createSubsession(boolean r12) {
        /*
            r11 = this;
            monitor-enter(r11)
            int r0 = r11.getCallingThreadId()     // Catch:{ all -> 0x0065 }
            java.util.concurrent.ConcurrentHashMap<java.lang.Integer, android.telecom.Logging.Session> r1 = r11.mSessionMapper     // Catch:{ all -> 0x0065 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0065 }
            java.lang.Object r1 = r1.get(r2)     // Catch:{ all -> 0x0065 }
            android.telecom.Logging.Session r1 = (android.telecom.Logging.Session) r1     // Catch:{ all -> 0x0065 }
            r2 = 0
            if (r1 != 0) goto L_0x0020
            java.lang.String r3 = "Logging"
            java.lang.String r4 = "Log.createSubsession was called with no session active."
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0065 }
            android.telecom.Log.d((java.lang.String) r3, (java.lang.String) r4, (java.lang.Object[]) r2)     // Catch:{ all -> 0x0065 }
            r2 = 0
            monitor-exit(r11)
            return r2
        L_0x0020:
            android.telecom.Logging.Session r10 = new android.telecom.Logging.Session     // Catch:{ all -> 0x0065 }
            java.lang.String r4 = r1.getNextChildId()     // Catch:{ all -> 0x0065 }
            java.lang.String r5 = r1.getShortMethodName()     // Catch:{ all -> 0x0065 }
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0065 }
            r9 = 0
            r3 = r10
            r8 = r12
            r3.<init>(r4, r5, r6, r8, r9)     // Catch:{ all -> 0x0065 }
            r3 = r10
            r1.addChild(r3)     // Catch:{ all -> 0x0065 }
            r3.setParentSession(r1)     // Catch:{ all -> 0x0065 }
            if (r12 != 0) goto L_0x005a
            java.lang.String r4 = "Logging"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0065 }
            r5.<init>()     // Catch:{ all -> 0x0065 }
            java.lang.String r6 = "CREATE_SUBSESSION "
            r5.append(r6)     // Catch:{ all -> 0x0065 }
            java.lang.String r6 = r3.toString()     // Catch:{ all -> 0x0065 }
            r5.append(r6)     // Catch:{ all -> 0x0065 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0065 }
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0065 }
            android.telecom.Log.v((java.lang.String) r4, (java.lang.String) r5, (java.lang.Object[]) r2)     // Catch:{ all -> 0x0065 }
            goto L_0x0063
        L_0x005a:
            java.lang.String r4 = "Logging"
            java.lang.String r5 = "CREATE_SUBSESSION (Invisible subsession)"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0065 }
            android.telecom.Log.v((java.lang.String) r4, (java.lang.String) r5, (java.lang.Object[]) r2)     // Catch:{ all -> 0x0065 }
        L_0x0063:
            monitor-exit(r11)
            return r3
        L_0x0065:
            r12 = move-exception
            monitor-exit(r11)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telecom.Logging.SessionManager.createSubsession(boolean):android.telecom.Logging.Session");
    }

    public synchronized Session.Info getExternalSession() {
        Session threadSession = this.mSessionMapper.get(Integer.valueOf(getCallingThreadId()));
        if (threadSession == null) {
            Log.d(LOGGING_TAG, "Log.getExternalSession was called with no session active.", new Object[0]);
            return null;
        }
        return threadSession.getInfo();
    }

    public synchronized void cancelSubsession(Session subsession) {
        if (subsession != null) {
            subsession.markSessionCompleted(-1);
            endParentSessions(subsession);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0069, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void continueSession(android.telecom.Logging.Session r6, java.lang.String r7) {
        /*
            r5 = this;
            monitor-enter(r5)
            if (r6 != 0) goto L_0x0005
            monitor-exit(r5)
            return
        L_0x0005:
            r5.resetStaleSessionTimer()     // Catch:{ all -> 0x006a }
            r6.setShortMethodName(r7)     // Catch:{ all -> 0x006a }
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x006a }
            r6.setExecutionStartTimeMs(r0)     // Catch:{ all -> 0x006a }
            android.telecom.Logging.Session r0 = r6.getParentSession()     // Catch:{ all -> 0x006a }
            r1 = 0
            if (r0 != 0) goto L_0x0033
            java.lang.String r2 = "Logging"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x006a }
            r3.<init>()     // Catch:{ all -> 0x006a }
            java.lang.String r4 = "Log.continueSession was called with no session active for method "
            r3.append(r4)     // Catch:{ all -> 0x006a }
            r3.append(r7)     // Catch:{ all -> 0x006a }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x006a }
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x006a }
            android.telecom.Log.i((java.lang.String) r2, (java.lang.String) r3, (java.lang.Object[]) r1)     // Catch:{ all -> 0x006a }
            monitor-exit(r5)
            return
        L_0x0033:
            java.util.concurrent.ConcurrentHashMap<java.lang.Integer, android.telecom.Logging.Session> r2 = r5.mSessionMapper     // Catch:{ all -> 0x006a }
            int r3 = r5.getCallingThreadId()     // Catch:{ all -> 0x006a }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x006a }
            r2.put(r3, r6)     // Catch:{ all -> 0x006a }
            boolean r2 = r6.isStartedFromActiveSession()     // Catch:{ all -> 0x006a }
            if (r2 != 0) goto L_0x0050
            java.lang.String r2 = "Logging"
            java.lang.String r3 = "CONTINUE_SUBSESSION"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x006a }
            android.telecom.Log.v((java.lang.String) r2, (java.lang.String) r3, (java.lang.Object[]) r1)     // Catch:{ all -> 0x006a }
            goto L_0x0068
        L_0x0050:
            java.lang.String r2 = "Logging"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x006a }
            r3.<init>()     // Catch:{ all -> 0x006a }
            java.lang.String r4 = "CONTINUE_SUBSESSION (Invisible Subsession) with Method "
            r3.append(r4)     // Catch:{ all -> 0x006a }
            r3.append(r7)     // Catch:{ all -> 0x006a }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x006a }
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x006a }
            android.telecom.Log.v((java.lang.String) r2, (java.lang.String) r3, (java.lang.Object[]) r1)     // Catch:{ all -> 0x006a }
        L_0x0068:
            monitor-exit(r5)
            return
        L_0x006a:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telecom.Logging.SessionManager.continueSession(android.telecom.Logging.Session, java.lang.String):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0097, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void endSession() {
        /*
            r7 = this;
            monitor-enter(r7)
            int r0 = r7.getCallingThreadId()     // Catch:{ all -> 0x0098 }
            java.util.concurrent.ConcurrentHashMap<java.lang.Integer, android.telecom.Logging.Session> r1 = r7.mSessionMapper     // Catch:{ all -> 0x0098 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0098 }
            java.lang.Object r1 = r1.get(r2)     // Catch:{ all -> 0x0098 }
            android.telecom.Logging.Session r1 = (android.telecom.Logging.Session) r1     // Catch:{ all -> 0x0098 }
            r2 = 0
            if (r1 != 0) goto L_0x001f
            java.lang.String r3 = "Logging"
            java.lang.String r4 = "Log.endSession was called with no session active."
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0098 }
            android.telecom.Log.w((java.lang.String) r3, (java.lang.String) r4, (java.lang.Object[]) r2)     // Catch:{ all -> 0x0098 }
            monitor-exit(r7)
            return
        L_0x001f:
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0098 }
            r1.markSessionCompleted(r3)     // Catch:{ all -> 0x0098 }
            boolean r3 = r1.isStartedFromActiveSession()     // Catch:{ all -> 0x0098 }
            if (r3 != 0) goto L_0x004e
            java.lang.String r3 = "Logging"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0098 }
            r4.<init>()     // Catch:{ all -> 0x0098 }
            java.lang.String r5 = "END_SUBSESSION (dur: "
            r4.append(r5)     // Catch:{ all -> 0x0098 }
            long r5 = r1.getLocalExecutionTime()     // Catch:{ all -> 0x0098 }
            r4.append(r5)     // Catch:{ all -> 0x0098 }
            java.lang.String r5 = " mS)"
            r4.append(r5)     // Catch:{ all -> 0x0098 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0098 }
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0098 }
            android.telecom.Log.v((java.lang.String) r3, (java.lang.String) r4, (java.lang.Object[]) r2)     // Catch:{ all -> 0x0098 }
            goto L_0x006f
        L_0x004e:
            java.lang.String r3 = "Logging"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0098 }
            r4.<init>()     // Catch:{ all -> 0x0098 }
            java.lang.String r5 = "END_SUBSESSION (Invisible Subsession) (dur: "
            r4.append(r5)     // Catch:{ all -> 0x0098 }
            long r5 = r1.getLocalExecutionTime()     // Catch:{ all -> 0x0098 }
            r4.append(r5)     // Catch:{ all -> 0x0098 }
            java.lang.String r5 = " ms)"
            r4.append(r5)     // Catch:{ all -> 0x0098 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0098 }
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0098 }
            android.telecom.Log.v((java.lang.String) r3, (java.lang.String) r4, (java.lang.Object[]) r2)     // Catch:{ all -> 0x0098 }
        L_0x006f:
            android.telecom.Logging.Session r2 = r1.getParentSession()     // Catch:{ all -> 0x0098 }
            java.util.concurrent.ConcurrentHashMap<java.lang.Integer, android.telecom.Logging.Session> r3 = r7.mSessionMapper     // Catch:{ all -> 0x0098 }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0098 }
            r3.remove(r4)     // Catch:{ all -> 0x0098 }
            r7.endParentSessions(r1)     // Catch:{ all -> 0x0098 }
            if (r2 == 0) goto L_0x0096
            boolean r3 = r2.isSessionCompleted()     // Catch:{ all -> 0x0098 }
            if (r3 != 0) goto L_0x0096
            boolean r3 = r1.isStartedFromActiveSession()     // Catch:{ all -> 0x0098 }
            if (r3 == 0) goto L_0x0096
            java.util.concurrent.ConcurrentHashMap<java.lang.Integer, android.telecom.Logging.Session> r3 = r7.mSessionMapper     // Catch:{ all -> 0x0098 }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0098 }
            r3.put(r4, r2)     // Catch:{ all -> 0x0098 }
        L_0x0096:
            monitor-exit(r7)
            return
        L_0x0098:
            r0 = move-exception
            monitor-exit(r7)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telecom.Logging.SessionManager.endSession():void");
    }

    private void endParentSessions(Session subsession) {
        if (subsession.isSessionCompleted() && subsession.getChildSessions().size() == 0) {
            Session parentSession = subsession.getParentSession();
            if (parentSession != null) {
                subsession.setParentSession((Session) null);
                parentSession.removeChild(subsession);
                if (parentSession.isExternal()) {
                    notifySessionCompleteListeners(subsession.getShortMethodName(), System.currentTimeMillis() - subsession.getExecutionStartTimeMilliseconds());
                }
                endParentSessions(parentSession);
                return;
            }
            long fullSessionTimeMs = System.currentTimeMillis() - subsession.getExecutionStartTimeMilliseconds();
            Log.d(LOGGING_TAG, "END_SESSION (dur: " + fullSessionTimeMs + " ms): " + subsession.toString(), new Object[0]);
            if (!subsession.isExternal()) {
                notifySessionCompleteListeners(subsession.getShortMethodName(), fullSessionTimeMs);
            }
        }
    }

    private void notifySessionCompleteListeners(String methodName, long sessionTimeMs) {
        for (ISessionListener l : this.mSessionListeners) {
            l.sessionComplete(methodName, sessionTimeMs);
        }
    }

    public String getSessionId() {
        Session currentSession = this.mSessionMapper.get(Integer.valueOf(getCallingThreadId()));
        return currentSession != null ? currentSession.toString() : "";
    }

    /* Debug info: failed to restart local var, previous not found, register: 1 */
    public synchronized void registerSessionListener(ISessionListener l) {
        if (l != null) {
            this.mSessionListeners.add(l);
        }
    }

    private synchronized String getNextSessionID() {
        Integer nextId;
        int i = this.sCodeEntryCounter;
        this.sCodeEntryCounter = i + 1;
        nextId = Integer.valueOf(i);
        if (((long) nextId.intValue()) >= 262144) {
            restartSessionCounter();
            int i2 = this.sCodeEntryCounter;
            this.sCodeEntryCounter = i2 + 1;
            nextId = Integer.valueOf(i2);
        }
        return getBase64Encoding(nextId.intValue());
    }

    private synchronized void restartSessionCounter() {
        this.sCodeEntryCounter = 0;
    }

    private String getBase64Encoding(int number) {
        return Base64.encodeToString(Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(number).array(), 2, 4), 3);
    }

    private int getCallingThreadId() {
        return this.mCurrentThreadId.get();
    }

    @VisibleForTesting
    public synchronized void cleanupStaleSessions(long timeoutMs) {
        String logMessage = "Stale Sessions Cleaned:\n";
        boolean isSessionsStale = false;
        long currentTimeMs = System.currentTimeMillis();
        Iterator<Map.Entry<Integer, Session>> it = this.mSessionMapper.entrySet().iterator();
        while (it.hasNext()) {
            Session session = it.next().getValue();
            if (currentTimeMs - session.getExecutionStartTimeMilliseconds() > timeoutMs) {
                it.remove();
                logMessage = logMessage + session.printFullSessionTree() + "\n";
                isSessionsStale = true;
            }
        }
        if (isSessionsStale) {
            Log.w(LOGGING_TAG, logMessage, new Object[0]);
        } else {
            Log.v(LOGGING_TAG, "No stale logging sessions needed to be cleaned...", new Object[0]);
        }
    }

    private long getCleanupTimeout(Context context) {
        return Settings.Secure.getLong(context.getContentResolver(), "telecom.stale_session_cleanup_timeout_millis", 30000);
    }
}
