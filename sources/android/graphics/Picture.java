package android.graphics;

import android.annotation.UnsupportedAppUsage;
import java.io.InputStream;
import java.io.OutputStream;

public class Picture {
    private static final int WORKING_STREAM_STORAGE = 16384;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private long mNativePicture;
    private PictureCanvas mRecordingCanvas;
    private boolean mRequiresHwAcceleration;

    private static native long nativeBeginRecording(long j, int i, int i2);

    private static native long nativeConstructor(long j);

    private static native long nativeCreateFromStream(InputStream inputStream, byte[] bArr);

    private static native void nativeDestructor(long j);

    private static native void nativeDraw(long j, long j2);

    private static native void nativeEndRecording(long j);

    private static native int nativeGetHeight(long j);

    private static native int nativeGetWidth(long j);

    private static native boolean nativeWriteToStream(long j, OutputStream outputStream, byte[] bArr);

    public Picture() {
        this(nativeConstructor(0));
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public Picture(Picture src) {
        this(nativeConstructor(src != null ? src.mNativePicture : 0));
    }

    public Picture(long nativePicture) {
        if (nativePicture != 0) {
            this.mNativePicture = nativePicture;
            return;
        }
        throw new IllegalArgumentException();
    }

    public void close() {
        if (this.mNativePicture != 0) {
            nativeDestructor(this.mNativePicture);
            this.mNativePicture = 0;
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private void verifyValid() {
        if (this.mNativePicture == 0) {
            throw new IllegalStateException("Picture is destroyed");
        }
    }

    public Canvas beginRecording(int width, int height) {
        verifyValid();
        if (this.mRecordingCanvas == null) {
            this.mRecordingCanvas = new PictureCanvas(this, nativeBeginRecording(this.mNativePicture, width, height));
            this.mRequiresHwAcceleration = false;
            return this.mRecordingCanvas;
        }
        throw new IllegalStateException("Picture already recording, must call #endRecording()");
    }

    public void endRecording() {
        verifyValid();
        if (this.mRecordingCanvas != null) {
            this.mRequiresHwAcceleration = this.mRecordingCanvas.mHoldsHwBitmap;
            this.mRecordingCanvas = null;
            nativeEndRecording(this.mNativePicture);
        }
    }

    public int getWidth() {
        verifyValid();
        return nativeGetWidth(this.mNativePicture);
    }

    public int getHeight() {
        verifyValid();
        return nativeGetHeight(this.mNativePicture);
    }

    public boolean requiresHardwareAcceleration() {
        verifyValid();
        return this.mRequiresHwAcceleration;
    }

    public void draw(Canvas canvas) {
        verifyValid();
        if (this.mRecordingCanvas != null) {
            endRecording();
        }
        if (this.mRequiresHwAcceleration && !canvas.isHardwareAccelerated()) {
            canvas.onHwBitmapInSwMode();
        }
        nativeDraw(canvas.getNativeCanvasWrapper(), this.mNativePicture);
    }

    @Deprecated
    public static Picture createFromStream(InputStream stream) {
        return new Picture(nativeCreateFromStream(stream, new byte[16384]));
    }

    @Deprecated
    public void writeToStream(OutputStream stream) {
        verifyValid();
        if (stream == null) {
            throw new IllegalArgumentException("stream cannot be null");
        } else if (!nativeWriteToStream(this.mNativePicture, stream, new byte[16384])) {
            throw new RuntimeException();
        }
    }

    private static class PictureCanvas extends Canvas {
        boolean mHoldsHwBitmap;
        private final Picture mPicture;

        public PictureCanvas(Picture pict, long nativeCanvas) {
            super(nativeCanvas);
            this.mPicture = pict;
            this.mDensity = 0;
        }

        public void setBitmap(Bitmap bitmap) {
            throw new RuntimeException("Cannot call setBitmap on a picture canvas");
        }

        public void drawPicture(Picture picture) {
            if (this.mPicture != picture) {
                super.drawPicture(picture);
                return;
            }
            throw new RuntimeException("Cannot draw a picture into its recording canvas");
        }

        /* access modifiers changed from: protected */
        public void onHwBitmapInSwMode() {
            this.mHoldsHwBitmap = true;
        }
    }
}
