package android.media;

import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import android.hardware.camera2.utils.SurfaceUtils;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Size;
import android.view.Surface;
import dalvik.system.VMRuntime;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.NioUtils;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ImageWriter implements AutoCloseable {
    private List<Image> mDequeuedImages = new CopyOnWriteArrayList();
    private int mEstimatedNativeAllocBytes;
    /* access modifiers changed from: private */
    public OnImageReleasedListener mListener;
    private ListenerHandler mListenerHandler;
    /* access modifiers changed from: private */
    public final Object mListenerLock = new Object();
    private final int mMaxImages;
    private long mNativeContext;
    private int mWriterFormat;

    public interface OnImageReleasedListener {
        void onImageReleased(ImageWriter imageWriter);
    }

    private native synchronized void cancelImage(long j, Image image);

    private native synchronized int nativeAttachAndQueueImage(long j, long j2, int i, long j3, int i2, int i3, int i4, int i5, int i6, int i7);

    private static native void nativeClassInit();

    private native synchronized void nativeClose(long j);

    private native synchronized void nativeDequeueInputImage(long j, Image image);

    private native synchronized long nativeInit(Object obj, Surface surface, int i, int i2);

    private native synchronized void nativeQueueInputImage(long j, Image image, long j2, int i, int i2, int i3, int i4, int i5, int i6);

    public static ImageWriter newInstance(Surface surface, int maxImages) {
        return new ImageWriter(surface, maxImages, 0);
    }

    public static ImageWriter newInstance(Surface surface, int maxImages, int format) {
        if (ImageFormat.isPublicFormat(format) || PixelFormat.isPublicFormat(format)) {
            return new ImageWriter(surface, maxImages, format);
        }
        throw new IllegalArgumentException("Invalid format is specified: " + format);
    }

    protected ImageWriter(Surface surface, int maxImages, int format) {
        if (surface == null || maxImages < 1) {
            throw new IllegalArgumentException("Illegal input argument: surface " + surface + ", maxImages: " + maxImages);
        }
        this.mMaxImages = maxImages;
        format = format == 0 ? SurfaceUtils.getSurfaceFormat(surface) : format;
        this.mNativeContext = nativeInit(new WeakReference(this), surface, maxImages, format);
        Size surfSize = SurfaceUtils.getSurfaceSize(surface);
        this.mEstimatedNativeAllocBytes = ImageUtils.getEstimatedNativeAllocBytes(surfSize.getWidth(), surfSize.getHeight(), format, 1);
        VMRuntime.getRuntime().registerNativeAllocation(this.mEstimatedNativeAllocBytes);
    }

    public int getMaxImages() {
        return this.mMaxImages;
    }

    public Image dequeueInputImage() {
        if (this.mDequeuedImages.size() < this.mMaxImages) {
            WriterSurfaceImage newImage = new WriterSurfaceImage(this);
            nativeDequeueInputImage(this.mNativeContext, newImage);
            this.mDequeuedImages.add(newImage);
            newImage.mIsImageValid = true;
            return newImage;
        }
        throw new IllegalStateException("Already dequeued max number of Images " + this.mMaxImages);
    }

    public void queueInputImage(Image image) {
        if (image != null) {
            boolean ownedByMe = isImageOwnedByMe(image);
            if (ownedByMe && !((WriterSurfaceImage) image).mIsImageValid) {
                throw new IllegalStateException("Image from ImageWriter is invalid");
            } else if (ownedByMe) {
                Rect crop = image.getCropRect();
                nativeQueueInputImage(this.mNativeContext, image, image.getTimestamp(), crop.left, crop.top, crop.right, crop.bottom, image.getTransform(), image.getScalingMode());
                if (ownedByMe) {
                    this.mDequeuedImages.remove(image);
                    WriterSurfaceImage wi = (WriterSurfaceImage) image;
                    wi.clearSurfacePlanes();
                    wi.mIsImageValid = false;
                }
            } else if (image.getOwner() instanceof ImageReader) {
                ((ImageReader) image.getOwner()).detachImage(image);
                attachAndQueueInputImage(image);
                image.close();
            } else {
                throw new IllegalArgumentException("Only images from ImageReader can be queued to ImageWriter, other image source is not supported yet!");
            }
        } else {
            throw new IllegalArgumentException("image shouldn't be null");
        }
    }

    public int getFormat() {
        return this.mWriterFormat;
    }

    public void setOnImageReleasedListener(OnImageReleasedListener listener, Handler handler) {
        synchronized (this.mListenerLock) {
            if (listener != null) {
                Looper looper = handler != null ? handler.getLooper() : Looper.myLooper();
                if (looper != null) {
                    if (this.mListenerHandler == null || this.mListenerHandler.getLooper() != looper) {
                        this.mListenerHandler = new ListenerHandler(looper);
                    }
                    this.mListener = listener;
                } else {
                    throw new IllegalArgumentException("handler is null but the current thread is not a looper");
                }
            } else {
                this.mListener = null;
                this.mListenerHandler = null;
            }
        }
    }

    public void close() {
        setOnImageReleasedListener((OnImageReleasedListener) null, (Handler) null);
        for (Image image : this.mDequeuedImages) {
            image.close();
        }
        this.mDequeuedImages.clear();
        nativeClose(this.mNativeContext);
        this.mNativeContext = 0;
        if (this.mEstimatedNativeAllocBytes > 0) {
            VMRuntime.getRuntime().registerNativeFree(this.mEstimatedNativeAllocBytes);
            this.mEstimatedNativeAllocBytes = 0;
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

    private void attachAndQueueInputImage(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("image shouldn't be null");
        } else if (isImageOwnedByMe(image)) {
            throw new IllegalArgumentException("Can not attach an image that is owned ImageWriter already");
        } else if (image.isAttachable()) {
            Rect crop = image.getCropRect();
            nativeAttachAndQueueImage(this.mNativeContext, image.getNativeContext(), image.getFormat(), image.getTimestamp(), crop.left, crop.top, crop.right, crop.bottom, image.getTransform(), image.getScalingMode());
        } else {
            throw new IllegalStateException("Image was not detached from last owner, or image  is not detachable");
        }
    }

    private final class ListenerHandler extends Handler {
        public ListenerHandler(Looper looper) {
            super(looper, (Handler.Callback) null, true);
        }

        public void handleMessage(Message msg) {
            OnImageReleasedListener listener;
            synchronized (ImageWriter.this.mListenerLock) {
                listener = ImageWriter.this.mListener;
            }
            if (listener != null) {
                listener.onImageReleased(ImageWriter.this);
            }
        }
    }

    private static void postEventFromNative(Object selfRef) {
        Handler handler;
        ImageWriter iw = (ImageWriter) ((WeakReference) selfRef).get();
        if (iw != null) {
            synchronized (iw.mListenerLock) {
                handler = iw.mListenerHandler;
            }
            if (handler != null) {
                handler.sendEmptyMessage(0);
            }
        }
    }

    /* access modifiers changed from: private */
    public void abortImage(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("image shouldn't be null");
        } else if (this.mDequeuedImages.contains(image)) {
            WriterSurfaceImage wi = (WriterSurfaceImage) image;
            if (wi.mIsImageValid) {
                cancelImage(this.mNativeContext, image);
                this.mDequeuedImages.remove(image);
                wi.clearSurfacePlanes();
                wi.mIsImageValid = false;
            }
        } else {
            throw new IllegalStateException("It is illegal to abort some image that is not dequeued yet");
        }
    }

    private boolean isImageOwnedByMe(Image image) {
        if ((image instanceof WriterSurfaceImage) && ((WriterSurfaceImage) image).getOwner() == this) {
            return true;
        }
        return false;
    }

    private static class WriterSurfaceImage extends Image {
        private final long DEFAULT_TIMESTAMP = Long.MIN_VALUE;
        private int mFormat = -1;
        private int mHeight = -1;
        private long mNativeBuffer;
        private int mNativeFenceFd = -1;
        private ImageWriter mOwner;
        private SurfacePlane[] mPlanes;
        private int mScalingMode = 0;
        private long mTimestamp = Long.MIN_VALUE;
        private int mTransform = 0;
        private int mWidth = -1;

        private native synchronized SurfacePlane[] nativeCreatePlanes(int i, int i2);

        private native synchronized int nativeGetFormat();

        private native synchronized HardwareBuffer nativeGetHardwareBuffer();

        private native synchronized int nativeGetHeight();

        private native synchronized int nativeGetWidth();

        public WriterSurfaceImage(ImageWriter writer) {
            this.mOwner = writer;
        }

        public int getFormat() {
            throwISEIfImageIsInvalid();
            if (this.mFormat == -1) {
                this.mFormat = nativeGetFormat();
            }
            return this.mFormat;
        }

        public int getWidth() {
            throwISEIfImageIsInvalid();
            if (this.mWidth == -1) {
                this.mWidth = nativeGetWidth();
            }
            return this.mWidth;
        }

        public int getHeight() {
            throwISEIfImageIsInvalid();
            if (this.mHeight == -1) {
                this.mHeight = nativeGetHeight();
            }
            return this.mHeight;
        }

        public int getTransform() {
            throwISEIfImageIsInvalid();
            return this.mTransform;
        }

        public int getScalingMode() {
            throwISEIfImageIsInvalid();
            return this.mScalingMode;
        }

        public long getTimestamp() {
            throwISEIfImageIsInvalid();
            return this.mTimestamp;
        }

        public void setTimestamp(long timestamp) {
            throwISEIfImageIsInvalid();
            this.mTimestamp = timestamp;
        }

        public HardwareBuffer getHardwareBuffer() {
            throwISEIfImageIsInvalid();
            return nativeGetHardwareBuffer();
        }

        public Image.Plane[] getPlanes() {
            throwISEIfImageIsInvalid();
            if (this.mPlanes == null) {
                this.mPlanes = nativeCreatePlanes(ImageUtils.getNumPlanesForFormat(getFormat()), getOwner().getFormat());
            }
            return (Image.Plane[]) this.mPlanes.clone();
        }

        /* access modifiers changed from: package-private */
        public boolean isAttachable() {
            throwISEIfImageIsInvalid();
            return false;
        }

        /* access modifiers changed from: package-private */
        public ImageWriter getOwner() {
            throwISEIfImageIsInvalid();
            return this.mOwner;
        }

        /* access modifiers changed from: package-private */
        public long getNativeContext() {
            throwISEIfImageIsInvalid();
            return this.mNativeBuffer;
        }

        public void close() {
            if (this.mIsImageValid) {
                getOwner().abortImage(this);
            }
        }

        /* access modifiers changed from: protected */
        public final void finalize() throws Throwable {
            try {
                close();
            } finally {
                super.finalize();
            }
        }

        /* access modifiers changed from: private */
        public void clearSurfacePlanes() {
            if (this.mIsImageValid && this.mPlanes != null) {
                for (int i = 0; i < this.mPlanes.length; i++) {
                    if (this.mPlanes[i] != null) {
                        this.mPlanes[i].clearBuffer();
                        this.mPlanes[i] = null;
                    }
                }
            }
        }

        private class SurfacePlane extends Image.Plane {
            private ByteBuffer mBuffer;
            private final int mPixelStride;
            private final int mRowStride;

            private SurfacePlane(int rowStride, int pixelStride, ByteBuffer buffer) {
                this.mRowStride = rowStride;
                this.mPixelStride = pixelStride;
                this.mBuffer = buffer;
                this.mBuffer.order(ByteOrder.nativeOrder());
            }

            public int getRowStride() {
                WriterSurfaceImage.this.throwISEIfImageIsInvalid();
                return this.mRowStride;
            }

            public int getPixelStride() {
                WriterSurfaceImage.this.throwISEIfImageIsInvalid();
                return this.mPixelStride;
            }

            public ByteBuffer getBuffer() {
                WriterSurfaceImage.this.throwISEIfImageIsInvalid();
                return this.mBuffer;
            }

            /* access modifiers changed from: private */
            public void clearBuffer() {
                if (this.mBuffer != null) {
                    if (this.mBuffer.isDirect()) {
                        NioUtils.freeDirectBuffer(this.mBuffer);
                    }
                    this.mBuffer = null;
                }
            }
        }
    }

    static {
        System.loadLibrary("media_jni");
        nativeClassInit();
    }
}
