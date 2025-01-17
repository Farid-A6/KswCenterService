package android.net;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import com.android.internal.util.AsyncChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class NetworkAgent extends Handler {
    private static final int BASE = 528384;
    private static final long BW_REFRESH_MIN_WIN_MS = 500;
    public static final int CMD_ADD_KEEPALIVE_PACKET_FILTER = 528400;
    public static final int CMD_PREVENT_AUTOMATIC_RECONNECT = 528399;
    public static final int CMD_REMOVE_KEEPALIVE_PACKET_FILTER = 528401;
    public static final int CMD_REPORT_NETWORK_STATUS = 528391;
    public static final int CMD_REQUEST_BANDWIDTH_UPDATE = 528394;
    public static final int CMD_SAVE_ACCEPT_UNVALIDATED = 528393;
    public static final int CMD_SET_SIGNAL_STRENGTH_THRESHOLDS = 528398;
    public static final int CMD_START_SOCKET_KEEPALIVE = 528395;
    public static final int CMD_STOP_SOCKET_KEEPALIVE = 528396;
    public static final int CMD_SUSPECT_BAD = 528384;
    private static final boolean DBG = true;
    public static final int EVENT_NETWORK_CAPABILITIES_CHANGED = 528386;
    public static final int EVENT_NETWORK_INFO_CHANGED = 528385;
    public static final int EVENT_NETWORK_PROPERTIES_CHANGED = 528387;
    public static final int EVENT_NETWORK_SCORE_CHANGED = 528388;
    public static final int EVENT_SET_EXPLICITLY_SELECTED = 528392;
    public static final int EVENT_SOCKET_KEEPALIVE = 528397;
    public static final int INVALID_NETWORK = 2;
    public static String REDIRECT_URL_KEY = "redirect URL";
    public static final int VALID_NETWORK = 1;
    private static final boolean VDBG = false;
    public static final int WIFI_BASE_SCORE = 60;
    private final String LOG_TAG;
    private volatile AsyncChannel mAsyncChannel;
    private final Context mContext;
    public final int mFactorySerialNumber;
    private volatile long mLastBwRefreshTime;
    private AtomicBoolean mPollLcePending;
    private boolean mPollLceScheduled;
    private final ArrayList<Message> mPreConnectedQueue;
    public final int netId;

    /* access modifiers changed from: protected */
    public abstract void unwanted();

    public NetworkAgent(Looper looper, Context context, String logTag, NetworkInfo ni, NetworkCapabilities nc, LinkProperties lp, int score) {
        this(looper, context, logTag, ni, nc, lp, score, (NetworkMisc) null, -1);
    }

    public NetworkAgent(Looper looper, Context context, String logTag, NetworkInfo ni, NetworkCapabilities nc, LinkProperties lp, int score, NetworkMisc misc) {
        this(looper, context, logTag, ni, nc, lp, score, misc, -1);
    }

    public NetworkAgent(Looper looper, Context context, String logTag, NetworkInfo ni, NetworkCapabilities nc, LinkProperties lp, int score, int factorySerialNumber) {
        this(looper, context, logTag, ni, nc, lp, score, (NetworkMisc) null, factorySerialNumber);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NetworkAgent(Looper looper, Context context, String logTag, NetworkInfo ni, NetworkCapabilities nc, LinkProperties lp, int score, NetworkMisc misc, int factorySerialNumber) {
        super(looper);
        NetworkInfo networkInfo = ni;
        NetworkCapabilities networkCapabilities = nc;
        LinkProperties linkProperties = lp;
        this.mPreConnectedQueue = new ArrayList<>();
        this.mLastBwRefreshTime = 0;
        this.mPollLceScheduled = false;
        this.mPollLcePending = new AtomicBoolean(false);
        this.LOG_TAG = logTag;
        this.mContext = context;
        this.mFactorySerialNumber = factorySerialNumber;
        if (networkInfo == null || networkCapabilities == null || linkProperties == null) {
            throw new IllegalArgumentException();
        }
        this.netId = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).registerNetworkAgent(new Messenger((Handler) this), new NetworkInfo(networkInfo), new LinkProperties(linkProperties), new NetworkCapabilities(networkCapabilities), score, misc, factorySerialNumber);
    }

    public void handleMessage(Message msg) {
        int i = msg.what;
        int i2 = 0;
        if (i != 69633) {
            if (i == 528384) {
                log("Unhandled Message " + msg);
            } else if (i != 528391) {
                switch (i) {
                    case AsyncChannel.CMD_CHANNEL_DISCONNECT /*69635*/:
                        if (this.mAsyncChannel != null) {
                            this.mAsyncChannel.disconnect();
                            return;
                        }
                        return;
                    case AsyncChannel.CMD_CHANNEL_DISCONNECTED /*69636*/:
                        log("NetworkAgent channel lost");
                        unwanted();
                        synchronized (this.mPreConnectedQueue) {
                            this.mAsyncChannel = null;
                        }
                        return;
                    default:
                        boolean z = true;
                        switch (i) {
                            case CMD_SAVE_ACCEPT_UNVALIDATED /*528393*/:
                                if (msg.arg1 == 0) {
                                    z = false;
                                }
                                saveAcceptUnvalidated(z);
                                return;
                            case CMD_REQUEST_BANDWIDTH_UPDATE /*528394*/:
                                long currentTimeMs = System.currentTimeMillis();
                                if (currentTimeMs >= this.mLastBwRefreshTime + BW_REFRESH_MIN_WIN_MS) {
                                    this.mPollLceScheduled = false;
                                    if (!this.mPollLcePending.getAndSet(true)) {
                                        pollLceData();
                                        return;
                                    }
                                    return;
                                } else if (!this.mPollLceScheduled) {
                                    this.mPollLceScheduled = sendEmptyMessageDelayed(CMD_REQUEST_BANDWIDTH_UPDATE, ((this.mLastBwRefreshTime + BW_REFRESH_MIN_WIN_MS) - currentTimeMs) + 1);
                                    return;
                                } else {
                                    return;
                                }
                            case CMD_START_SOCKET_KEEPALIVE /*528395*/:
                                startSocketKeepalive(msg);
                                return;
                            case CMD_STOP_SOCKET_KEEPALIVE /*528396*/:
                                stopSocketKeepalive(msg);
                                return;
                            default:
                                switch (i) {
                                    case CMD_SET_SIGNAL_STRENGTH_THRESHOLDS /*528398*/:
                                        ArrayList<Integer> thresholds = ((Bundle) msg.obj).getIntegerArrayList("thresholds");
                                        int[] intThresholds = new int[(thresholds != null ? thresholds.size() : 0)];
                                        while (true) {
                                            int i3 = i2;
                                            if (i3 < intThresholds.length) {
                                                intThresholds[i3] = thresholds.get(i3).intValue();
                                                i2 = i3 + 1;
                                            } else {
                                                setSignalStrengthThresholds(intThresholds);
                                                return;
                                            }
                                        }
                                    case CMD_PREVENT_AUTOMATIC_RECONNECT /*528399*/:
                                        preventAutomaticReconnect();
                                        return;
                                    case CMD_ADD_KEEPALIVE_PACKET_FILTER /*528400*/:
                                        addKeepalivePacketFilter(msg);
                                        return;
                                    case CMD_REMOVE_KEEPALIVE_PACKET_FILTER /*528401*/:
                                        removeKeepalivePacketFilter(msg);
                                        return;
                                    default:
                                        return;
                                }
                        }
                }
            } else {
                networkStatus(msg.arg1, ((Bundle) msg.obj).getString(REDIRECT_URL_KEY));
            }
        } else if (this.mAsyncChannel != null) {
            log("Received new connection while already connected!");
        } else {
            AsyncChannel ac = new AsyncChannel();
            ac.connected((Context) null, this, msg.replyTo);
            ac.replyToMessage(msg, (int) AsyncChannel.CMD_CHANNEL_FULLY_CONNECTED, 0);
            synchronized (this.mPreConnectedQueue) {
                this.mAsyncChannel = ac;
                Iterator<Message> it = this.mPreConnectedQueue.iterator();
                while (it.hasNext()) {
                    ac.sendMessage(it.next());
                }
                this.mPreConnectedQueue.clear();
            }
        }
    }

    private void queueOrSendMessage(int what, Object obj) {
        queueOrSendMessage(what, 0, 0, obj);
    }

    private void queueOrSendMessage(int what, int arg1, int arg2) {
        queueOrSendMessage(what, arg1, arg2, (Object) null);
    }

    private void queueOrSendMessage(int what, int arg1, int arg2, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        queueOrSendMessage(msg);
    }

    private void queueOrSendMessage(Message msg) {
        synchronized (this.mPreConnectedQueue) {
            if (this.mAsyncChannel != null) {
                this.mAsyncChannel.sendMessage(msg);
            } else {
                this.mPreConnectedQueue.add(msg);
            }
        }
    }

    public void sendLinkProperties(LinkProperties linkProperties) {
        queueOrSendMessage(EVENT_NETWORK_PROPERTIES_CHANGED, new LinkProperties(linkProperties));
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void sendNetworkInfo(NetworkInfo networkInfo) {
        queueOrSendMessage(EVENT_NETWORK_INFO_CHANGED, new NetworkInfo(networkInfo));
    }

    public void sendNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        this.mPollLcePending.set(false);
        this.mLastBwRefreshTime = System.currentTimeMillis();
        queueOrSendMessage(EVENT_NETWORK_CAPABILITIES_CHANGED, new NetworkCapabilities(networkCapabilities));
    }

    public void sendNetworkScore(int score) {
        if (score >= 0) {
            queueOrSendMessage(EVENT_NETWORK_SCORE_CHANGED, score, 0);
            return;
        }
        throw new IllegalArgumentException("Score must be >= 0");
    }

    public void explicitlySelected(boolean acceptUnvalidated) {
        explicitlySelected(true, acceptUnvalidated);
    }

    public void explicitlySelected(boolean explicitlySelected, boolean acceptUnvalidated) {
        queueOrSendMessage(EVENT_SET_EXPLICITLY_SELECTED, explicitlySelected, acceptUnvalidated);
    }

    /* access modifiers changed from: protected */
    public void pollLceData() {
    }

    /* access modifiers changed from: protected */
    public void networkStatus(int status, String redirectUrl) {
    }

    /* access modifiers changed from: protected */
    public void saveAcceptUnvalidated(boolean accept) {
    }

    /* access modifiers changed from: protected */
    public void startSocketKeepalive(Message msg) {
        onSocketKeepaliveEvent(msg.arg1, -30);
    }

    /* access modifiers changed from: protected */
    public void stopSocketKeepalive(Message msg) {
        onSocketKeepaliveEvent(msg.arg1, -30);
    }

    public void onSocketKeepaliveEvent(int slot, int reason) {
        queueOrSendMessage(EVENT_SOCKET_KEEPALIVE, slot, reason);
    }

    /* access modifiers changed from: protected */
    public void addKeepalivePacketFilter(Message msg) {
    }

    /* access modifiers changed from: protected */
    public void removeKeepalivePacketFilter(Message msg) {
    }

    /* access modifiers changed from: protected */
    public void setSignalStrengthThresholds(int[] thresholds) {
    }

    /* access modifiers changed from: protected */
    public void preventAutomaticReconnect() {
    }

    /* access modifiers changed from: protected */
    public void log(String s) {
        String str = this.LOG_TAG;
        Log.d(str, "NetworkAgent: " + s);
    }
}
