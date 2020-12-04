package android.media;

import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.session.MediaSession;
import android.media.session.MediaSessionLegacyHelper;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

@Deprecated
public class RemoteControlClient {
    private static final boolean DEBUG = false;
    public static final int DEFAULT_PLAYBACK_VOLUME = 15;
    public static final int DEFAULT_PLAYBACK_VOLUME_HANDLING = 1;
    public static final int FLAGS_KEY_MEDIA_NONE = 0;
    public static final int FLAG_INFORMATION_REQUEST_ALBUM_ART = 8;
    public static final int FLAG_INFORMATION_REQUEST_KEY_MEDIA = 2;
    public static final int FLAG_INFORMATION_REQUEST_METADATA = 1;
    public static final int FLAG_INFORMATION_REQUEST_PLAYSTATE = 4;
    public static final int FLAG_KEY_MEDIA_FAST_FORWARD = 64;
    public static final int FLAG_KEY_MEDIA_NEXT = 128;
    public static final int FLAG_KEY_MEDIA_PAUSE = 16;
    public static final int FLAG_KEY_MEDIA_PLAY = 4;
    public static final int FLAG_KEY_MEDIA_PLAY_PAUSE = 8;
    public static final int FLAG_KEY_MEDIA_POSITION_UPDATE = 256;
    public static final int FLAG_KEY_MEDIA_PREVIOUS = 1;
    public static final int FLAG_KEY_MEDIA_RATING = 512;
    public static final int FLAG_KEY_MEDIA_REWIND = 2;
    public static final int FLAG_KEY_MEDIA_STOP = 32;
    @UnsupportedAppUsage
    public static int MEDIA_POSITION_READABLE = 1;
    @UnsupportedAppUsage
    public static int MEDIA_POSITION_WRITABLE = 2;
    public static final int PLAYBACKINFO_INVALID_VALUE = Integer.MIN_VALUE;
    public static final int PLAYBACKINFO_PLAYBACK_TYPE = 1;
    public static final int PLAYBACKINFO_USES_STREAM = 5;
    public static final int PLAYBACKINFO_VOLUME = 2;
    public static final int PLAYBACKINFO_VOLUME_HANDLING = 4;
    public static final int PLAYBACKINFO_VOLUME_MAX = 3;
    public static final long PLAYBACK_POSITION_ALWAYS_UNKNOWN = -9216204211029966080L;
    public static final long PLAYBACK_POSITION_INVALID = -1;
    public static final float PLAYBACK_SPEED_1X = 1.0f;
    public static final int PLAYBACK_TYPE_LOCAL = 0;
    private static final int PLAYBACK_TYPE_MAX = 1;
    private static final int PLAYBACK_TYPE_MIN = 0;
    public static final int PLAYBACK_TYPE_REMOTE = 1;
    public static final int PLAYBACK_VOLUME_FIXED = 0;
    public static final int PLAYBACK_VOLUME_VARIABLE = 1;
    public static final int PLAYSTATE_BUFFERING = 8;
    public static final int PLAYSTATE_ERROR = 9;
    public static final int PLAYSTATE_FAST_FORWARDING = 4;
    public static final int PLAYSTATE_NONE = 0;
    public static final int PLAYSTATE_PAUSED = 2;
    public static final int PLAYSTATE_PLAYING = 3;
    public static final int PLAYSTATE_REWINDING = 5;
    public static final int PLAYSTATE_SKIPPING_BACKWARDS = 7;
    public static final int PLAYSTATE_SKIPPING_FORWARDS = 6;
    public static final int PLAYSTATE_STOPPED = 1;
    private static final long POSITION_DRIFT_MAX_MS = 500;
    private static final long POSITION_REFRESH_PERIOD_MIN_MS = 2000;
    private static final long POSITION_REFRESH_PERIOD_PLAYING_MS = 15000;
    public static final int RCSE_ID_UNREGISTERED = -1;
    private static final String TAG = "RemoteControlClient";
    /* access modifiers changed from: private */
    public final Object mCacheLock = new Object();
    /* access modifiers changed from: private */
    public int mCurrentClientGenId = -1;
    /* access modifiers changed from: private */
    public MediaMetadata mMediaMetadata;
    /* access modifiers changed from: private */
    public Bundle mMetadata = new Bundle();
    private OnMetadataUpdateListener mMetadataUpdateListener;
    private boolean mNeedsPositionSync = false;
    /* access modifiers changed from: private */
    public Bitmap mOriginalArtwork;
    private long mPlaybackPositionMs = -1;
    private float mPlaybackSpeed = 1.0f;
    private int mPlaybackState = 0;
    private long mPlaybackStateChangeTimeMs = 0;
    private OnGetPlaybackPositionListener mPositionProvider;
    private OnPlaybackPositionUpdateListener mPositionUpdateListener;
    private final PendingIntent mRcMediaIntent;
    /* access modifiers changed from: private */
    public MediaSession mSession;
    private PlaybackState mSessionPlaybackState = null;
    /* access modifiers changed from: private */
    public int mTransportControlFlags = 0;
    private MediaSession.Callback mTransportListener = new MediaSession.Callback() {
        public void onSeekTo(long pos) {
            RemoteControlClient.this.onSeekTo(RemoteControlClient.this.mCurrentClientGenId, pos);
        }

        public void onSetRating(Rating rating) {
            if ((RemoteControlClient.this.mTransportControlFlags & 512) != 0) {
                RemoteControlClient.this.onUpdateMetadata(RemoteControlClient.this.mCurrentClientGenId, MediaMetadataEditor.RATING_KEY_BY_USER, rating);
            }
        }
    };

    public interface OnGetPlaybackPositionListener {
        long onGetPlaybackPosition();
    }

    public interface OnMetadataUpdateListener {
        void onMetadataUpdate(int i, Object obj);
    }

    public interface OnPlaybackPositionUpdateListener {
        void onPlaybackPositionUpdate(long j);
    }

    public RemoteControlClient(PendingIntent mediaButtonIntent) {
        this.mRcMediaIntent = mediaButtonIntent;
    }

    public RemoteControlClient(PendingIntent mediaButtonIntent, Looper looper) {
        this.mRcMediaIntent = mediaButtonIntent;
    }

    public void registerWithSession(MediaSessionLegacyHelper helper) {
        helper.addRccListener(this.mRcMediaIntent, this.mTransportListener);
        this.mSession = helper.getSession(this.mRcMediaIntent);
        setTransportControlFlags(this.mTransportControlFlags);
    }

    public void unregisterWithSession(MediaSessionLegacyHelper helper) {
        helper.removeRccListener(this.mRcMediaIntent);
        this.mSession = null;
    }

    public MediaSession getMediaSession() {
        return this.mSession;
    }

    @Deprecated
    public class MetadataEditor extends MediaMetadataEditor {
        public static final int BITMAP_KEY_ARTWORK = 100;
        public static final int METADATA_KEY_ARTWORK = 100;

        private MetadataEditor() {
        }

        public Object clone() throws CloneNotSupportedException {
            throw new CloneNotSupportedException();
        }

        public synchronized MetadataEditor putString(int key, String value) throws IllegalArgumentException {
            String metadataKey;
            super.putString(key, value);
            if (!(this.mMetadataBuilder == null || (metadataKey = MediaMetadata.getKeyFromMetadataEditorKey(key)) == null)) {
                this.mMetadataBuilder.putText(metadataKey, value);
            }
            return this;
        }

        public synchronized MetadataEditor putLong(int key, long value) throws IllegalArgumentException {
            String metadataKey;
            super.putLong(key, value);
            if (!(this.mMetadataBuilder == null || (metadataKey = MediaMetadata.getKeyFromMetadataEditorKey(key)) == null)) {
                this.mMetadataBuilder.putLong(metadataKey, value);
            }
            return this;
        }

        public synchronized MetadataEditor putBitmap(int key, Bitmap bitmap) throws IllegalArgumentException {
            String metadataKey;
            super.putBitmap(key, bitmap);
            if (!(this.mMetadataBuilder == null || (metadataKey = MediaMetadata.getKeyFromMetadataEditorKey(key)) == null)) {
                this.mMetadataBuilder.putBitmap(metadataKey, bitmap);
            }
            return this;
        }

        public synchronized MetadataEditor putObject(int key, Object object) throws IllegalArgumentException {
            String metadataKey;
            super.putObject(key, object);
            if (this.mMetadataBuilder != null && ((key == 268435457 || key == 101) && (metadataKey = MediaMetadata.getKeyFromMetadataEditorKey(key)) != null)) {
                this.mMetadataBuilder.putRating(metadataKey, (Rating) object);
            }
            return this;
        }

        public synchronized void clear() {
            super.clear();
        }

        public synchronized void apply() {
            if (this.mApplied) {
                Log.e(RemoteControlClient.TAG, "Can't apply a previously applied MetadataEditor");
                return;
            }
            synchronized (RemoteControlClient.this.mCacheLock) {
                Bundle unused = RemoteControlClient.this.mMetadata = new Bundle(this.mEditorMetadata);
                RemoteControlClient.this.mMetadata.putLong(String.valueOf(MediaMetadataEditor.KEY_EDITABLE_MASK), this.mEditableKeys);
                if (RemoteControlClient.this.mOriginalArtwork != null && !RemoteControlClient.this.mOriginalArtwork.equals(this.mEditorArtwork)) {
                    RemoteControlClient.this.mOriginalArtwork.recycle();
                }
                Bitmap unused2 = RemoteControlClient.this.mOriginalArtwork = this.mEditorArtwork;
                this.mEditorArtwork = null;
                if (!(RemoteControlClient.this.mSession == null || this.mMetadataBuilder == null)) {
                    MediaMetadata unused3 = RemoteControlClient.this.mMediaMetadata = this.mMetadataBuilder.build();
                    RemoteControlClient.this.mSession.setMetadata(RemoteControlClient.this.mMediaMetadata);
                }
                this.mApplied = true;
            }
        }
    }

    public MetadataEditor editMetadata(boolean startEmpty) {
        MetadataEditor editor = new MetadataEditor();
        if (startEmpty) {
            editor.mEditorMetadata = new Bundle();
            editor.mEditorArtwork = null;
            editor.mMetadataChanged = true;
            editor.mArtworkChanged = true;
            editor.mEditableKeys = 0;
        } else {
            editor.mEditorMetadata = new Bundle(this.mMetadata);
            editor.mEditorArtwork = this.mOriginalArtwork;
            editor.mMetadataChanged = false;
            editor.mArtworkChanged = false;
        }
        if (startEmpty || this.mMediaMetadata == null) {
            editor.mMetadataBuilder = new MediaMetadata.Builder();
        } else {
            editor.mMetadataBuilder = new MediaMetadata.Builder(this.mMediaMetadata);
        }
        return editor;
    }

    public void setPlaybackState(int state) {
        setPlaybackStateInt(state, PLAYBACK_POSITION_ALWAYS_UNKNOWN, 1.0f, false);
    }

    public void setPlaybackState(int state, long timeInMs, float playbackSpeed) {
        setPlaybackStateInt(state, timeInMs, playbackSpeed, true);
    }

    private void setPlaybackStateInt(int state, long timeInMs, float playbackSpeed, boolean hasPosition) {
        int i = state;
        long j = timeInMs;
        float f = playbackSpeed;
        synchronized (this.mCacheLock) {
            if (!(this.mPlaybackState == i && this.mPlaybackPositionMs == j && this.mPlaybackSpeed == f)) {
                this.mPlaybackState = i;
                long position = -1;
                if (!hasPosition) {
                    this.mPlaybackPositionMs = PLAYBACK_POSITION_ALWAYS_UNKNOWN;
                } else if (j < 0) {
                    this.mPlaybackPositionMs = -1;
                } else {
                    this.mPlaybackPositionMs = j;
                }
                this.mPlaybackSpeed = f;
                this.mPlaybackStateChangeTimeMs = SystemClock.elapsedRealtime();
                if (this.mSession != null) {
                    int pbState = getStateFromRccState(state);
                    if (hasPosition) {
                        position = this.mPlaybackPositionMs;
                    }
                    PlaybackState.Builder bob = new PlaybackState.Builder(this.mSessionPlaybackState);
                    bob.setState(pbState, position, playbackSpeed, SystemClock.elapsedRealtime());
                    bob.setErrorMessage((CharSequence) null);
                    this.mSessionPlaybackState = bob.build();
                    this.mSession.setPlaybackState(this.mSessionPlaybackState);
                }
            }
        }
    }

    public void setTransportControlFlags(int transportControlFlags) {
        synchronized (this.mCacheLock) {
            this.mTransportControlFlags = transportControlFlags;
            if (this.mSession != null) {
                PlaybackState.Builder bob = new PlaybackState.Builder(this.mSessionPlaybackState);
                bob.setActions(getActionsFromRccControlFlags(transportControlFlags));
                this.mSessionPlaybackState = bob.build();
                this.mSession.setPlaybackState(this.mSessionPlaybackState);
            }
        }
    }

    public void setMetadataUpdateListener(OnMetadataUpdateListener l) {
        synchronized (this.mCacheLock) {
            this.mMetadataUpdateListener = l;
        }
    }

    public void setPlaybackPositionUpdateListener(OnPlaybackPositionUpdateListener l) {
        synchronized (this.mCacheLock) {
            this.mPositionUpdateListener = l;
        }
    }

    public void setOnGetPlaybackPositionListener(OnGetPlaybackPositionListener l) {
        synchronized (this.mCacheLock) {
            this.mPositionProvider = l;
        }
    }

    public PendingIntent getRcMediaIntent() {
        return this.mRcMediaIntent;
    }

    /* access modifiers changed from: private */
    public void onSeekTo(int generationId, long timeMs) {
        synchronized (this.mCacheLock) {
            if (this.mCurrentClientGenId == generationId && this.mPositionUpdateListener != null) {
                this.mPositionUpdateListener.onPlaybackPositionUpdate(timeMs);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onUpdateMetadata(int generationId, int key, Object value) {
        synchronized (this.mCacheLock) {
            if (this.mCurrentClientGenId == generationId && this.mMetadataUpdateListener != null) {
                this.mMetadataUpdateListener.onMetadataUpdate(key, value);
            }
        }
    }

    static boolean playbackPositionShouldMove(int playstate) {
        switch (playstate) {
            case 1:
            case 2:
            case 6:
            case 7:
            case 8:
            case 9:
                return false;
            default:
                return true;
        }
    }

    private static long getCheckPeriodFromSpeed(float speed) {
        if (Math.abs(speed) <= 1.0f) {
            return POSITION_REFRESH_PERIOD_PLAYING_MS;
        }
        return Math.max((long) (15000.0f / Math.abs(speed)), POSITION_REFRESH_PERIOD_MIN_MS);
    }

    private static int getStateFromRccState(int rccState) {
        switch (rccState) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 10;
            case 7:
                return 9;
            case 8:
                return 6;
            case 9:
                return 7;
            default:
                return -1;
        }
    }

    static int getRccStateFromState(int state) {
        switch (state) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 8;
            case 7:
                return 9;
            case 9:
                return 7;
            case 10:
                return 6;
            default:
                return -1;
        }
    }

    private static long getActionsFromRccControlFlags(int rccFlags) {
        long actions = 0;
        for (long flag = 1; flag <= ((long) rccFlags); flag <<= 1) {
            if ((((long) rccFlags) & flag) != 0) {
                actions |= getActionForRccFlag((int) flag);
            }
        }
        return actions;
    }

    static int getRccControlFlagsFromActions(long actions) {
        int rccFlags = 0;
        long action = 1;
        while (action <= actions && action < 2147483647L) {
            if ((action & actions) != 0) {
                rccFlags |= getRccFlagForAction(action);
            }
            action <<= 1;
        }
        return rccFlags;
    }

    private static long getActionForRccFlag(int flag) {
        if (flag == 4) {
            return 4;
        }
        if (flag == 8) {
            return 512;
        }
        if (flag == 16) {
            return 2;
        }
        if (flag == 32) {
            return 1;
        }
        if (flag == 64) {
            return 64;
        }
        if (flag == 128) {
            return 32;
        }
        if (flag == 256) {
            return 256;
        }
        if (flag == 512) {
            return 128;
        }
        switch (flag) {
            case 1:
                return 16;
            case 2:
                return 8;
            default:
                return 0;
        }
    }

    private static int getRccFlagForAction(long action) {
        int testAction = action < 2147483647L ? (int) action : 0;
        if (testAction == 4) {
            return 4;
        }
        if (testAction == 8) {
            return 2;
        }
        if (testAction == 16) {
            return 1;
        }
        if (testAction == 32) {
            return 128;
        }
        if (testAction == 64) {
            return 64;
        }
        if (testAction == 128) {
            return 512;
        }
        if (testAction == 256) {
            return 256;
        }
        if (testAction == 512) {
            return 8;
        }
        switch (testAction) {
            case 1:
                return 32;
            case 2:
                return 16;
            default:
                return 0;
        }
    }
}
