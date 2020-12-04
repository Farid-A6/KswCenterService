package android.media;

import android.content.res.AssetFileDescriptor;
import android.media.DrmInitData;
import android.media.MediaCas;
import android.media.MediaCodec;
import android.os.IBinder;
import android.os.IHwBinder;
import android.os.PersistableBundle;
import com.android.internal.util.Preconditions;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class MediaExtractor {
    public static final int SAMPLE_FLAG_ENCRYPTED = 2;
    public static final int SAMPLE_FLAG_PARTIAL_FRAME = 4;
    public static final int SAMPLE_FLAG_SYNC = 1;
    public static final int SEEK_TO_CLOSEST_SYNC = 2;
    public static final int SEEK_TO_NEXT_SYNC = 1;
    public static final int SEEK_TO_PREVIOUS_SYNC = 0;
    private MediaCas mMediaCas;
    private long mNativeContext;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SampleFlag {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SeekMode {
    }

    private native Map<String, Object> getFileFormatNative();

    private native Map<String, Object> getTrackFormatNative(int i);

    private final native void nativeSetDataSource(IBinder iBinder, String str, String[] strArr, String[] strArr2) throws IOException;

    private final native void nativeSetMediaCas(IHwBinder iHwBinder);

    private final native void native_finalize();

    private native List<AudioPresentation> native_getAudioPresentations(int i);

    private native PersistableBundle native_getMetrics();

    private static final native void native_init();

    private final native void native_setup();

    public native boolean advance();

    public native long getCachedDuration();

    public native boolean getSampleCryptoInfo(MediaCodec.CryptoInfo cryptoInfo);

    public native int getSampleFlags();

    public native long getSampleSize();

    public native long getSampleTime();

    public native int getSampleTrackIndex();

    public final native int getTrackCount();

    public native boolean hasCacheReachedEndOfStream();

    public native int readSampleData(ByteBuffer byteBuffer, int i);

    public final native void release();

    public native void seekTo(long j, int i);

    public native void selectTrack(int i);

    public final native void setDataSource(MediaDataSource mediaDataSource) throws IOException;

    public final native void setDataSource(FileDescriptor fileDescriptor, long j, long j2) throws IOException;

    public native void unselectTrack(int i);

    public MediaExtractor() {
        native_setup();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0055, code lost:
        if (r1 == null) goto L_0x005f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0057, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x005c, code lost:
        if (r1 == null) goto L_0x005f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x005f, code lost:
        setDataSource(r11.toString(), r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0066, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setDataSource(android.content.Context r10, android.net.Uri r11, java.util.Map<java.lang.String, java.lang.String> r12) throws java.io.IOException {
        /*
            r9 = this;
            java.lang.String r0 = r11.getScheme()
            if (r0 == 0) goto L_0x0067
            java.lang.String r1 = "file"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x000f
            goto L_0x0067
        L_0x000f:
            r1 = 0
            android.content.ContentResolver r2 = r10.getContentResolver()     // Catch:{ SecurityException -> 0x005b, IOException -> 0x0054, all -> 0x004d }
            java.lang.String r3 = "r"
            android.content.res.AssetFileDescriptor r3 = r2.openAssetFileDescriptor(r11, r3)     // Catch:{ SecurityException -> 0x005b, IOException -> 0x0054, all -> 0x004d }
            r1 = r3
            if (r1 != 0) goto L_0x0024
            if (r1 == 0) goto L_0x0023
            r1.close()
        L_0x0023:
            return
        L_0x0024:
            long r3 = r1.getDeclaredLength()     // Catch:{ SecurityException -> 0x005b, IOException -> 0x0054, all -> 0x004d }
            r5 = 0
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 >= 0) goto L_0x0036
            java.io.FileDescriptor r3 = r1.getFileDescriptor()     // Catch:{ SecurityException -> 0x005b, IOException -> 0x0054, all -> 0x004d }
            r9.setDataSource((java.io.FileDescriptor) r3)     // Catch:{ SecurityException -> 0x005b, IOException -> 0x0054, all -> 0x004d }
            goto L_0x0047
        L_0x0036:
            java.io.FileDescriptor r4 = r1.getFileDescriptor()     // Catch:{ SecurityException -> 0x005b, IOException -> 0x0054, all -> 0x004d }
            long r5 = r1.getStartOffset()     // Catch:{ SecurityException -> 0x005b, IOException -> 0x0054, all -> 0x004d }
            long r7 = r1.getDeclaredLength()     // Catch:{ SecurityException -> 0x005b, IOException -> 0x0054, all -> 0x004d }
            r3 = r9
            r3.setDataSource((java.io.FileDescriptor) r4, (long) r5, (long) r7)     // Catch:{ SecurityException -> 0x005b, IOException -> 0x0054, all -> 0x004d }
        L_0x0047:
            if (r1 == 0) goto L_0x004c
            r1.close()
        L_0x004c:
            return
        L_0x004d:
            r2 = move-exception
            if (r1 == 0) goto L_0x0053
            r1.close()
        L_0x0053:
            throw r2
        L_0x0054:
            r2 = move-exception
            if (r1 == 0) goto L_0x005f
        L_0x0057:
            r1.close()
            goto L_0x005f
        L_0x005b:
            r2 = move-exception
            if (r1 == 0) goto L_0x005f
            goto L_0x0057
        L_0x005f:
            java.lang.String r2 = r11.toString()
            r9.setDataSource(r2, r12)
            return
        L_0x0067:
            java.lang.String r1 = r11.getPath()
            r9.setDataSource((java.lang.String) r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaExtractor.setDataSource(android.content.Context, android.net.Uri, java.util.Map):void");
    }

    public final void setDataSource(String path, Map<String, String> headers) throws IOException {
        String[] keys = null;
        String[] values = null;
        if (headers != null) {
            keys = new String[headers.size()];
            values = new String[headers.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                keys[i] = entry.getKey();
                values[i] = entry.getValue();
                i++;
            }
        }
        nativeSetDataSource(MediaHTTPService.createHttpServiceBinderIfNecessary(path), path, keys, values);
    }

    public final void setDataSource(String path) throws IOException {
        nativeSetDataSource(MediaHTTPService.createHttpServiceBinderIfNecessary(path), path, (String[]) null, (String[]) null);
    }

    public final void setDataSource(AssetFileDescriptor afd) throws IOException, IllegalArgumentException, IllegalStateException {
        Preconditions.checkNotNull(afd);
        if (afd.getDeclaredLength() < 0) {
            setDataSource(afd.getFileDescriptor());
            return;
        }
        setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
    }

    public final void setDataSource(FileDescriptor fd) throws IOException {
        setDataSource(fd, 0, 576460752303423487L);
    }

    public final void setMediaCas(MediaCas mediaCas) {
        this.mMediaCas = mediaCas;
        nativeSetMediaCas(mediaCas.getBinder());
    }

    public static final class CasInfo {
        private final byte[] mPrivateData;
        private final MediaCas.Session mSession;
        private final int mSystemId;

        CasInfo(int systemId, MediaCas.Session session, byte[] privateData) {
            this.mSystemId = systemId;
            this.mSession = session;
            this.mPrivateData = privateData;
        }

        public int getSystemId() {
            return this.mSystemId;
        }

        public byte[] getPrivateData() {
            return this.mPrivateData;
        }

        public MediaCas.Session getSession() {
            return this.mSession;
        }
    }

    private ArrayList<Byte> toByteArray(byte[] data) {
        ArrayList<Byte> byteArray = new ArrayList<>(data.length);
        for (int i = 0; i < data.length; i++) {
            byteArray.add(i, Byte.valueOf(data[i]));
        }
        return byteArray;
    }

    public CasInfo getCasInfo(int index) {
        Map<String, Object> formatMap = getTrackFormatNative(index);
        if (!formatMap.containsKey(MediaFormat.KEY_CA_SYSTEM_ID)) {
            return null;
        }
        int systemId = ((Integer) formatMap.get(MediaFormat.KEY_CA_SYSTEM_ID)).intValue();
        MediaCas.Session session = null;
        byte[] privateData = null;
        if (formatMap.containsKey(MediaFormat.KEY_CA_PRIVATE_DATA)) {
            ByteBuffer buf = (ByteBuffer) formatMap.get(MediaFormat.KEY_CA_PRIVATE_DATA);
            buf.rewind();
            privateData = new byte[buf.remaining()];
            buf.get(privateData);
        }
        if (this.mMediaCas != null && formatMap.containsKey(MediaFormat.KEY_CA_SESSION_ID)) {
            ByteBuffer buf2 = (ByteBuffer) formatMap.get(MediaFormat.KEY_CA_SESSION_ID);
            buf2.rewind();
            byte[] sessionId = new byte[buf2.remaining()];
            buf2.get(sessionId);
            session = this.mMediaCas.createFromSessionId(toByteArray(sessionId));
        }
        return new CasInfo(systemId, session, privateData);
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        native_finalize();
    }

    public DrmInitData getDrmInitData() {
        Map<String, Object> formatMap = getFileFormatNative();
        if (formatMap == null) {
            return null;
        }
        if (formatMap.containsKey("pssh")) {
            Map<UUID, byte[]> psshMap = getPsshInfo();
            final Map<UUID, DrmInitData.SchemeInitData> initDataMap = new HashMap<>();
            for (Map.Entry<UUID, byte[]> e : psshMap.entrySet()) {
                initDataMap.put(e.getKey(), new DrmInitData.SchemeInitData("cenc", e.getValue()));
            }
            return new DrmInitData() {
                public DrmInitData.SchemeInitData get(UUID schemeUuid) {
                    return (DrmInitData.SchemeInitData) initDataMap.get(schemeUuid);
                }
            };
        }
        int numTracks = getTrackCount();
        int i = 0;
        while (i < numTracks) {
            Map<String, Object> trackFormatMap = getTrackFormatNative(i);
            if (!trackFormatMap.containsKey("crypto-key")) {
                i++;
            } else {
                ByteBuffer buf = (ByteBuffer) trackFormatMap.get("crypto-key");
                buf.rewind();
                final byte[] data = new byte[buf.remaining()];
                buf.get(data);
                return new DrmInitData() {
                    public DrmInitData.SchemeInitData get(UUID schemeUuid) {
                        return new DrmInitData.SchemeInitData("webm", data);
                    }
                };
            }
        }
        return null;
    }

    public List<AudioPresentation> getAudioPresentations(int trackIndex) {
        return native_getAudioPresentations(trackIndex);
    }

    public Map<UUID, byte[]> getPsshInfo() {
        Map<UUID, byte[]> psshMap = null;
        Map<String, Object> formatMap = getFileFormatNative();
        if (formatMap != null && formatMap.containsKey("pssh")) {
            ByteBuffer rawpssh = (ByteBuffer) formatMap.get("pssh");
            rawpssh.order(ByteOrder.nativeOrder());
            rawpssh.rewind();
            formatMap.remove("pssh");
            psshMap = new HashMap<>();
            while (rawpssh.remaining() > 0) {
                rawpssh.order(ByteOrder.BIG_ENDIAN);
                UUID uuid = new UUID(rawpssh.getLong(), rawpssh.getLong());
                rawpssh.order(ByteOrder.nativeOrder());
                byte[] psshdata = new byte[rawpssh.getInt()];
                rawpssh.get(psshdata);
                psshMap.put(uuid, psshdata);
            }
        }
        return psshMap;
    }

    public MediaFormat getTrackFormat(int index) {
        return new MediaFormat(getTrackFormatNative(index));
    }

    public PersistableBundle getMetrics() {
        return native_getMetrics();
    }

    static {
        System.loadLibrary("media_jni");
        native_init();
    }

    public static final class MetricsConstants {
        public static final String FORMAT = "android.media.mediaextractor.fmt";
        public static final String MIME_TYPE = "android.media.mediaextractor.mime";
        public static final String TRACKS = "android.media.mediaextractor.ntrk";

        private MetricsConstants() {
        }
    }
}
