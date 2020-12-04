package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

public class TextureView extends View {
    private static final String LOG_TAG = "TextureView";
    private Canvas mCanvas;
    private boolean mHadSurface;
    @UnsupportedAppUsage
    private TextureLayer mLayer;
    private SurfaceTextureListener mListener;
    private final Object[] mLock = new Object[0];
    private final Matrix mMatrix = new Matrix();
    private boolean mMatrixChanged;
    @UnsupportedAppUsage
    private long mNativeWindow;
    private final Object[] mNativeWindowLock = new Object[0];
    @UnsupportedAppUsage
    private boolean mOpaque = true;
    private int mSaveCount;
    @UnsupportedAppUsage
    private SurfaceTexture mSurface;
    private boolean mUpdateLayer;
    @UnsupportedAppUsage
    private final SurfaceTexture.OnFrameAvailableListener mUpdateListener = new SurfaceTexture.OnFrameAvailableListener() {
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            TextureView.this.updateLayer();
            TextureView.this.invalidate();
        }
    };
    @UnsupportedAppUsage
    private boolean mUpdateSurface;

    public interface SurfaceTextureListener {
        void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2);

        boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture);

        void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2);

        void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture);
    }

    @UnsupportedAppUsage
    private native void nCreateNativeWindow(SurfaceTexture surfaceTexture);

    @UnsupportedAppUsage
    private native void nDestroyNativeWindow();

    private static native boolean nLockCanvas(long j, Canvas canvas, Rect rect);

    private static native void nUnlockCanvasAndPost(long j, Canvas canvas);

    public TextureView(Context context) {
        super(context);
    }

    public TextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean isOpaque() {
        return this.mOpaque;
    }

    public void setOpaque(boolean opaque) {
        if (opaque != this.mOpaque) {
            this.mOpaque = opaque;
            if (this.mLayer != null) {
                updateLayerAndInvalidate();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isHardwareAccelerated()) {
            Log.w(LOG_TAG, "A TextureView or a subclass can only be used with hardware acceleration enabled.");
        }
        if (this.mHadSurface) {
            invalidate(true);
            this.mHadSurface = false;
        }
    }

    /* access modifiers changed from: protected */
    @UnsupportedAppUsage
    public void onDetachedFromWindowInternal() {
        destroyHardwareLayer();
        releaseSurfaceTexture();
        super.onDetachedFromWindowInternal();
    }

    /* access modifiers changed from: protected */
    @UnsupportedAppUsage
    public void destroyHardwareResources() {
        super.destroyHardwareResources();
        destroyHardwareLayer();
    }

    @UnsupportedAppUsage
    private void destroyHardwareLayer() {
        if (this.mLayer != null) {
            this.mLayer.detachSurfaceTexture();
            this.mLayer.destroy();
            this.mLayer = null;
            this.mMatrixChanged = true;
        }
    }

    private void releaseSurfaceTexture() {
        if (this.mSurface != null) {
            boolean shouldRelease = true;
            if (this.mListener != null) {
                shouldRelease = this.mListener.onSurfaceTextureDestroyed(this.mSurface);
            }
            synchronized (this.mNativeWindowLock) {
                nDestroyNativeWindow();
            }
            if (shouldRelease) {
                this.mSurface.release();
            }
            this.mSurface = null;
            this.mHadSurface = true;
        }
    }

    public void setLayerType(int layerType, Paint paint) {
        setLayerPaint(paint);
    }

    public void setLayerPaint(Paint paint) {
        if (paint != this.mLayerPaint) {
            this.mLayerPaint = paint;
            invalidate();
        }
    }

    public int getLayerType() {
        return 2;
    }

    public void buildLayer() {
    }

    public void setForeground(Drawable foreground) {
        if (foreground != null && !sTextureViewIgnoresDrawableSetters) {
            throw new UnsupportedOperationException("TextureView doesn't support displaying a foreground drawable");
        }
    }

    public void setBackgroundDrawable(Drawable background) {
        if (background != null && !sTextureViewIgnoresDrawableSetters) {
            throw new UnsupportedOperationException("TextureView doesn't support displaying a background drawable");
        }
    }

    public final void draw(Canvas canvas) {
        this.mPrivateFlags = (this.mPrivateFlags & -2097153) | 32;
        if (canvas.isHardwareAccelerated()) {
            RecordingCanvas recordingCanvas = (RecordingCanvas) canvas;
            TextureLayer layer = getTextureLayer();
            if (layer != null) {
                applyUpdate();
                applyTransformMatrix();
                this.mLayer.setLayerPaint(this.mLayerPaint);
                recordingCanvas.drawTextureLayer(layer);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void onDraw(Canvas canvas) {
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.mSurface != null) {
            this.mSurface.setDefaultBufferSize(getWidth(), getHeight());
            updateLayer();
            if (this.mListener != null) {
                this.mListener.onSurfaceTextureSizeChanged(this.mSurface, getWidth(), getHeight());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public TextureLayer getTextureLayer() {
        if (this.mLayer == null) {
            if (this.mAttachInfo == null || this.mAttachInfo.mThreadedRenderer == null) {
                return null;
            }
            this.mLayer = this.mAttachInfo.mThreadedRenderer.createTextureLayer();
            boolean createNewSurface = this.mSurface == null;
            if (createNewSurface) {
                this.mSurface = new SurfaceTexture(false);
                nCreateNativeWindow(this.mSurface);
            }
            this.mLayer.setSurfaceTexture(this.mSurface);
            this.mSurface.setDefaultBufferSize(getWidth(), getHeight());
            this.mSurface.setOnFrameAvailableListener(this.mUpdateListener, this.mAttachInfo.mHandler);
            if (this.mListener != null && createNewSurface) {
                this.mListener.onSurfaceTextureAvailable(this.mSurface, getWidth(), getHeight());
            }
            this.mLayer.setLayerPaint(this.mLayerPaint);
        }
        if (this.mUpdateSurface) {
            this.mUpdateSurface = false;
            updateLayer();
            this.mMatrixChanged = true;
            this.mLayer.setSurfaceTexture(this.mSurface);
            this.mSurface.setDefaultBufferSize(getWidth(), getHeight());
        }
        return this.mLayer;
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (this.mSurface == null) {
            return;
        }
        if (visibility == 0) {
            if (this.mLayer != null) {
                this.mSurface.setOnFrameAvailableListener(this.mUpdateListener, this.mAttachInfo.mHandler);
            }
            updateLayerAndInvalidate();
            return;
        }
        this.mSurface.setOnFrameAvailableListener((SurfaceTexture.OnFrameAvailableListener) null);
    }

    /* access modifiers changed from: private */
    public void updateLayer() {
        synchronized (this.mLock) {
            this.mUpdateLayer = true;
        }
    }

    private void updateLayerAndInvalidate() {
        synchronized (this.mLock) {
            this.mUpdateLayer = true;
        }
        invalidate();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0010, code lost:
        r4.mLayer.prepare(getWidth(), getHeight(), r4.mOpaque);
        r4.mLayer.updateSurfaceTexture();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0026, code lost:
        if (r4.mListener == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0028, code lost:
        r4.mListener.onSurfaceTextureUpdated(r4.mSurface);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyUpdate() {
        /*
            r4 = this;
            android.view.TextureLayer r0 = r4.mLayer
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            java.lang.Object[] r0 = r4.mLock
            monitor-enter(r0)
            boolean r1 = r4.mUpdateLayer     // Catch:{ all -> 0x0032 }
            if (r1 == 0) goto L_0x0030
            r1 = 0
            r4.mUpdateLayer = r1     // Catch:{ all -> 0x0032 }
            monitor-exit(r0)     // Catch:{ all -> 0x0032 }
            android.view.TextureLayer r0 = r4.mLayer
            int r1 = r4.getWidth()
            int r2 = r4.getHeight()
            boolean r3 = r4.mOpaque
            r0.prepare(r1, r2, r3)
            android.view.TextureLayer r0 = r4.mLayer
            r0.updateSurfaceTexture()
            android.view.TextureView$SurfaceTextureListener r0 = r4.mListener
            if (r0 == 0) goto L_0x002f
            android.view.TextureView$SurfaceTextureListener r0 = r4.mListener
            android.graphics.SurfaceTexture r1 = r4.mSurface
            r0.onSurfaceTextureUpdated(r1)
        L_0x002f:
            return
        L_0x0030:
            monitor-exit(r0)     // Catch:{ all -> 0x0032 }
            return
        L_0x0032:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0032 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.TextureView.applyUpdate():void");
    }

    public void setTransform(Matrix transform) {
        this.mMatrix.set(transform);
        this.mMatrixChanged = true;
        invalidateParentIfNeeded();
    }

    public Matrix getTransform(Matrix transform) {
        if (transform == null) {
            transform = new Matrix();
        }
        transform.set(this.mMatrix);
        return transform;
    }

    private void applyTransformMatrix() {
        if (this.mMatrixChanged && this.mLayer != null) {
            this.mLayer.setTransform(this.mMatrix);
            this.mMatrixChanged = false;
        }
    }

    public Bitmap getBitmap() {
        return getBitmap(getWidth(), getHeight());
    }

    public Bitmap getBitmap(int width, int height) {
        if (!isAvailable() || width <= 0 || height <= 0) {
            return null;
        }
        return getBitmap(Bitmap.createBitmap(getResources().getDisplayMetrics(), width, height, Bitmap.Config.ARGB_8888));
    }

    public Bitmap getBitmap(Bitmap bitmap) {
        if (bitmap != null && isAvailable()) {
            applyUpdate();
            applyTransformMatrix();
            if (this.mLayer == null && this.mUpdateSurface) {
                getTextureLayer();
            }
            if (this.mLayer != null) {
                this.mLayer.copyInto(bitmap);
            }
        }
        return bitmap;
    }

    public boolean isAvailable() {
        return this.mSurface != null;
    }

    public Canvas lockCanvas() {
        return lockCanvas((Rect) null);
    }

    public Canvas lockCanvas(Rect dirty) {
        if (!isAvailable()) {
            return null;
        }
        if (this.mCanvas == null) {
            this.mCanvas = new Canvas();
        }
        synchronized (this.mNativeWindowLock) {
            if (!nLockCanvas(this.mNativeWindow, this.mCanvas, dirty)) {
                return null;
            }
            this.mSaveCount = this.mCanvas.save();
            return this.mCanvas;
        }
    }

    public void unlockCanvasAndPost(Canvas canvas) {
        if (this.mCanvas != null && canvas == this.mCanvas) {
            canvas.restoreToCount(this.mSaveCount);
            this.mSaveCount = 0;
            synchronized (this.mNativeWindowLock) {
                nUnlockCanvasAndPost(this.mNativeWindow, this.mCanvas);
            }
        }
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.mSurface;
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        if (surfaceTexture == null) {
            throw new NullPointerException("surfaceTexture must not be null");
        } else if (surfaceTexture == this.mSurface) {
            throw new IllegalArgumentException("Trying to setSurfaceTexture to the same SurfaceTexture that's already set.");
        } else if (!surfaceTexture.isReleased()) {
            if (this.mSurface != null) {
                nDestroyNativeWindow();
                this.mSurface.release();
            }
            this.mSurface = surfaceTexture;
            nCreateNativeWindow(this.mSurface);
            if ((this.mViewFlags & 12) == 0 && this.mLayer != null) {
                this.mSurface.setOnFrameAvailableListener(this.mUpdateListener, this.mAttachInfo.mHandler);
            }
            this.mUpdateSurface = true;
            invalidateParentIfNeeded();
        } else {
            throw new IllegalArgumentException("Cannot setSurfaceTexture to a released SurfaceTexture");
        }
    }

    public SurfaceTextureListener getSurfaceTextureListener() {
        return this.mListener;
    }

    public void setSurfaceTextureListener(SurfaceTextureListener listener) {
        this.mListener = listener;
    }
}
