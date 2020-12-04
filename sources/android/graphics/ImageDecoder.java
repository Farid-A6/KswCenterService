package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Size;
import android.util.TypedValue;
import dalvik.system.CloseGuard;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import libcore.io.IoUtils;

public final class ImageDecoder implements AutoCloseable {
    public static final int ALLOCATOR_DEFAULT = 0;
    public static final int ALLOCATOR_HARDWARE = 3;
    public static final int ALLOCATOR_SHARED_MEMORY = 2;
    public static final int ALLOCATOR_SOFTWARE = 1;
    @Deprecated
    public static final int ERROR_SOURCE_ERROR = 3;
    @Deprecated
    public static final int ERROR_SOURCE_EXCEPTION = 1;
    @Deprecated
    public static final int ERROR_SOURCE_INCOMPLETE = 2;
    public static final int MEMORY_POLICY_DEFAULT = 1;
    public static final int MEMORY_POLICY_LOW_RAM = 0;
    public static int sApiLevel;
    private int mAllocator = 0;
    /* access modifiers changed from: private */
    public final boolean mAnimated;
    private AssetFileDescriptor mAssetFd;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final AtomicBoolean mClosed = new AtomicBoolean();
    private boolean mConserveMemory = false;
    private Rect mCropRect;
    private boolean mDecodeAsAlphaMask = false;
    private ColorSpace mDesiredColorSpace = null;
    private int mDesiredHeight;
    private int mDesiredWidth;
    /* access modifiers changed from: private */
    public final int mHeight;
    private InputStream mInputStream;
    private final boolean mIsNinePatch;
    private boolean mMutable = false;
    private long mNativePtr;
    private OnPartialImageListener mOnPartialImageListener;
    private Rect mOutPaddingRect;
    private boolean mOwnsInputStream;
    private PostProcessor mPostProcessor;
    private Source mSource;
    private byte[] mTempStorage;
    private boolean mUnpremultipliedRequired = false;
    /* access modifiers changed from: private */
    public final int mWidth;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Allocator {
    }

    @Deprecated
    public static class IncompleteException extends IOException {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MemoryPolicy {
    }

    public interface OnHeaderDecodedListener {
        void onHeaderDecoded(ImageDecoder imageDecoder, ImageInfo imageInfo, Source source);
    }

    public interface OnPartialImageListener {
        boolean onPartialImage(DecodeException decodeException);
    }

    private static native void nClose(long j);

    private static native ImageDecoder nCreate(long j, Source source) throws IOException;

    private static native ImageDecoder nCreate(FileDescriptor fileDescriptor, Source source) throws IOException;

    private static native ImageDecoder nCreate(InputStream inputStream, byte[] bArr, Source source) throws IOException;

    /* access modifiers changed from: private */
    public static native ImageDecoder nCreate(ByteBuffer byteBuffer, int i, int i2, Source source) throws IOException;

    /* access modifiers changed from: private */
    public static native ImageDecoder nCreate(byte[] bArr, int i, int i2, Source source) throws IOException;

    private static native Bitmap nDecodeBitmap(long j, ImageDecoder imageDecoder, boolean z, int i, int i2, Rect rect, boolean z2, int i3, boolean z3, boolean z4, boolean z5, long j2, boolean z6) throws IOException;

    private static native ColorSpace nGetColorSpace(long j);

    private static native String nGetMimeType(long j);

    private static native void nGetPadding(long j, Rect rect);

    private static native Size nGetSampledSize(long j, int i);

    public static abstract class Source {
        /* access modifiers changed from: package-private */
        public abstract ImageDecoder createImageDecoder() throws IOException;

        private Source() {
        }

        /* access modifiers changed from: package-private */
        public Resources getResources() {
            return null;
        }

        /* access modifiers changed from: package-private */
        public int getDensity() {
            return 0;
        }

        /* access modifiers changed from: package-private */
        public final int computeDstDensity() {
            Resources res = getResources();
            if (res == null) {
                return Bitmap.getDefaultDensity();
            }
            return res.getDisplayMetrics().densityDpi;
        }
    }

    private static class ByteArraySource extends Source {
        private final byte[] mData;
        private final int mLength;
        private final int mOffset;

        ByteArraySource(byte[] data, int offset, int length) {
            super();
            this.mData = data;
            this.mOffset = offset;
            this.mLength = length;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            return ImageDecoder.nCreate(this.mData, this.mOffset, this.mLength, (Source) this);
        }
    }

    private static class ByteBufferSource extends Source {
        private final ByteBuffer mBuffer;

        ByteBufferSource(ByteBuffer buffer) {
            super();
            this.mBuffer = buffer;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            if (this.mBuffer.isDirect() || !this.mBuffer.hasArray()) {
                ByteBuffer buffer = this.mBuffer.slice();
                return ImageDecoder.nCreate(buffer, buffer.position(), buffer.limit(), (Source) this);
            }
            return ImageDecoder.nCreate(this.mBuffer.array(), this.mBuffer.arrayOffset() + this.mBuffer.position(), this.mBuffer.limit() - this.mBuffer.position(), (Source) this);
        }
    }

    private static class ContentResolverSource extends Source {
        private final ContentResolver mResolver;
        private final Resources mResources;
        private final Uri mUri;

        ContentResolverSource(ContentResolver resolver, Uri uri, Resources res) {
            super();
            this.mResolver = resolver;
            this.mUri = uri;
            this.mResources = res;
        }

        /* access modifiers changed from: package-private */
        public Resources getResources() {
            return this.mResources;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            AssetFileDescriptor assetFd;
            try {
                if (this.mUri.getScheme() == "content") {
                    assetFd = this.mResolver.openTypedAssetFileDescriptor(this.mUri, "image/*", (Bundle) null);
                } else {
                    assetFd = this.mResolver.openAssetFileDescriptor(this.mUri, "r");
                }
                return ImageDecoder.createFromAssetFileDescriptor(assetFd, this);
            } catch (FileNotFoundException e) {
                InputStream is = this.mResolver.openInputStream(this.mUri);
                if (is != null) {
                    return ImageDecoder.createFromStream(is, true, this);
                }
                throw new FileNotFoundException(this.mUri.toString());
            }
        }
    }

    /* access modifiers changed from: private */
    public static ImageDecoder createFromFile(File file, Source source) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        FileDescriptor fd = stream.getFD();
        try {
            Os.lseek(fd, 0, OsConstants.SEEK_CUR);
            ImageDecoder decoder = null;
            try {
                decoder = nCreate(fd, source);
                if (decoder != null) {
                    decoder.mInputStream = stream;
                    decoder.mOwnsInputStream = true;
                }
                return decoder;
            } finally {
                if (decoder == null) {
                    IoUtils.closeQuietly(stream);
                } else {
                    decoder.mInputStream = stream;
                    decoder.mOwnsInputStream = true;
                }
            }
        } catch (ErrnoException e) {
            return createFromStream(stream, true, source);
        }
    }

    /* access modifiers changed from: private */
    public static ImageDecoder createFromStream(InputStream is, boolean closeInputStream, Source source) throws IOException {
        byte[] storage = new byte[16384];
        ImageDecoder decoder = null;
        try {
            decoder = nCreate(is, storage, source);
            if (decoder != null) {
                decoder.mInputStream = is;
                decoder.mOwnsInputStream = closeInputStream;
                decoder.mTempStorage = storage;
            }
            return decoder;
        } finally {
            if (decoder != null) {
                decoder.mInputStream = is;
                decoder.mOwnsInputStream = closeInputStream;
                decoder.mTempStorage = storage;
            } else if (closeInputStream) {
                IoUtils.closeQuietly(is);
            }
        }
    }

    /* access modifiers changed from: private */
    public static ImageDecoder createFromAssetFileDescriptor(AssetFileDescriptor assetFd, Source source) throws IOException {
        ImageDecoder decoder;
        FileDescriptor fd = assetFd.getFileDescriptor();
        try {
            Os.lseek(fd, assetFd.getStartOffset(), OsConstants.SEEK_SET);
            decoder = nCreate(fd, source);
        } catch (ErrnoException e) {
            decoder = createFromStream(new FileInputStream(fd), true, source);
        } catch (Throwable th) {
            if (0 == 0) {
                IoUtils.closeQuietly(assetFd);
            } else {
                null.mAssetFd = assetFd;
            }
            throw th;
        }
        if (decoder == null) {
            IoUtils.closeQuietly(assetFd);
        } else {
            decoder.mAssetFd = assetFd;
        }
        return decoder;
    }

    private static class InputStreamSource extends Source {
        final int mInputDensity;
        InputStream mInputStream;
        final Resources mResources;

        InputStreamSource(Resources res, InputStream is, int inputDensity) {
            super();
            if (is != null) {
                this.mResources = res;
                this.mInputStream = is;
                this.mInputDensity = inputDensity;
                return;
            }
            throw new IllegalArgumentException("The InputStream cannot be null");
        }

        public Resources getResources() {
            return this.mResources;
        }

        public int getDensity() {
            return this.mInputDensity;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            ImageDecoder access$300;
            synchronized (this) {
                if (this.mInputStream != null) {
                    InputStream is = this.mInputStream;
                    this.mInputStream = null;
                    access$300 = ImageDecoder.createFromStream(is, false, this);
                } else {
                    throw new IOException("Cannot reuse InputStreamSource");
                }
            }
            return access$300;
        }
    }

    public static class AssetInputStreamSource extends Source {
        private AssetManager.AssetInputStream mAssetInputStream;
        private final int mDensity;
        private final Resources mResources;

        public AssetInputStreamSource(AssetManager.AssetInputStream ais, Resources res, TypedValue value) {
            super();
            this.mAssetInputStream = ais;
            this.mResources = res;
            if (value.density == 0) {
                this.mDensity = 160;
            } else if (value.density != 65535) {
                this.mDensity = value.density;
            } else {
                this.mDensity = 0;
            }
        }

        public Resources getResources() {
            return this.mResources;
        }

        public int getDensity() {
            return this.mDensity;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            ImageDecoder access$500;
            synchronized (this) {
                if (this.mAssetInputStream != null) {
                    AssetManager.AssetInputStream ais = this.mAssetInputStream;
                    this.mAssetInputStream = null;
                    access$500 = ImageDecoder.createFromAsset(ais, this);
                } else {
                    throw new IOException("Cannot reuse AssetInputStreamSource");
                }
            }
            return access$500;
        }
    }

    private static class ResourceSource extends Source {
        private Object mLock = new Object();
        int mResDensity;
        final int mResId;
        final Resources mResources;

        ResourceSource(Resources res, int resId) {
            super();
            this.mResources = res;
            this.mResId = resId;
            this.mResDensity = 0;
        }

        public Resources getResources() {
            return this.mResources;
        }

        public int getDensity() {
            int i;
            synchronized (this.mLock) {
                i = this.mResDensity;
            }
            return i;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            TypedValue value = new TypedValue();
            InputStream is = this.mResources.openRawResource(this.mResId, value);
            synchronized (this.mLock) {
                if (value.density == 0) {
                    this.mResDensity = 160;
                } else if (value.density != 65535) {
                    this.mResDensity = value.density;
                }
            }
            return ImageDecoder.createFromAsset((AssetManager.AssetInputStream) is, this);
        }
    }

    /* access modifiers changed from: private */
    public static ImageDecoder createFromAsset(AssetManager.AssetInputStream ais, Source source) throws IOException {
        ImageDecoder decoder = null;
        try {
            decoder = nCreate(ais.getNativeAsset(), source);
            if (decoder != null) {
                decoder.mInputStream = ais;
                decoder.mOwnsInputStream = true;
            }
            return decoder;
        } finally {
            if (decoder == null) {
                IoUtils.closeQuietly(ais);
            } else {
                decoder.mInputStream = ais;
                decoder.mOwnsInputStream = true;
            }
        }
    }

    private static class AssetSource extends Source {
        private final AssetManager mAssets;
        private final String mFileName;

        AssetSource(AssetManager assets, String fileName) {
            super();
            this.mAssets = assets;
            this.mFileName = fileName;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            return ImageDecoder.createFromAsset((AssetManager.AssetInputStream) this.mAssets.open(this.mFileName), this);
        }
    }

    private static class FileSource extends Source {
        private final File mFile;

        FileSource(File file) {
            super();
            this.mFile = file;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            return ImageDecoder.createFromFile(this.mFile, this);
        }
    }

    private static class CallableSource extends Source {
        private final Callable<AssetFileDescriptor> mCallable;

        CallableSource(Callable<AssetFileDescriptor> callable) {
            super();
            this.mCallable = callable;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            try {
                return ImageDecoder.createFromAssetFileDescriptor(this.mCallable.call(), this);
            } catch (Exception e) {
                if (e instanceof IOException) {
                    throw ((IOException) e);
                }
                throw new IOException(e);
            }
        }
    }

    public static class ImageInfo {
        /* access modifiers changed from: private */
        public ImageDecoder mDecoder;
        private final Size mSize;

        private ImageInfo(ImageDecoder decoder) {
            this.mSize = new Size(decoder.mWidth, decoder.mHeight);
            this.mDecoder = decoder;
        }

        public Size getSize() {
            return this.mSize;
        }

        public String getMimeType() {
            return this.mDecoder.getMimeType();
        }

        public boolean isAnimated() {
            return this.mDecoder.mAnimated;
        }

        public ColorSpace getColorSpace() {
            return this.mDecoder.getColorSpace();
        }
    }

    public static final class DecodeException extends IOException {
        public static final int SOURCE_EXCEPTION = 1;
        public static final int SOURCE_INCOMPLETE = 2;
        public static final int SOURCE_MALFORMED_DATA = 3;
        final int mError;
        final Source mSource;

        @Retention(RetentionPolicy.SOURCE)
        public @interface Error {
        }

        DecodeException(int error, Throwable cause, Source source) {
            super(errorMessage(error, cause), cause);
            this.mError = error;
            this.mSource = source;
        }

        DecodeException(int error, String msg, Throwable cause, Source source) {
            super(msg + errorMessage(error, cause), cause);
            this.mError = error;
            this.mSource = source;
        }

        public int getError() {
            return this.mError;
        }

        public Source getSource() {
            return this.mSource;
        }

        private static String errorMessage(int error, Throwable cause) {
            switch (error) {
                case 1:
                    return "Exception in input: " + cause;
                case 2:
                    return "Input was incomplete.";
                case 3:
                    return "Input contained an error.";
                default:
                    return "";
            }
        }
    }

    private ImageDecoder(long nativePtr, int width, int height, boolean animated, boolean isNinePatch) {
        this.mNativePtr = nativePtr;
        this.mWidth = width;
        this.mHeight = height;
        this.mDesiredWidth = width;
        this.mDesiredHeight = height;
        this.mAnimated = animated;
        this.mIsNinePatch = isNinePatch;
        this.mCloseGuard.open("close");
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            this.mInputStream = null;
            this.mAssetFd = null;
            close();
        } finally {
            super.finalize();
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isMimeTypeSupported(java.lang.String r4) {
        /*
            java.util.Objects.requireNonNull(r4)
            java.util.Locale r0 = java.util.Locale.US
            java.lang.String r0 = r4.toLowerCase(r0)
            int r1 = r0.hashCode()
            r2 = 1
            r3 = 0
            switch(r1) {
                case -1875291391: goto L_0x00dc;
                case -1635437028: goto L_0x00d1;
                case -1594371159: goto L_0x00c6;
                case -1487464693: goto L_0x00bc;
                case -1487464690: goto L_0x00b2;
                case -1487394660: goto L_0x00a8;
                case -1487018032: goto L_0x009e;
                case -1423313290: goto L_0x0093;
                case -985160897: goto L_0x0088;
                case -879272239: goto L_0x007d;
                case -879267568: goto L_0x0072;
                case -879258763: goto L_0x0067;
                case -332763809: goto L_0x005b;
                case 741270252: goto L_0x004f;
                case 1146342924: goto L_0x0044;
                case 1378106698: goto L_0x0038;
                case 2099152104: goto L_0x002c;
                case 2099152524: goto L_0x0020;
                case 2111234748: goto L_0x0014;
                default: goto L_0x0012;
            }
        L_0x0012:
            goto L_0x00e7
        L_0x0014:
            java.lang.String r1 = "image/x-canon-cr2"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 10
            goto L_0x00e8
        L_0x0020:
            java.lang.String r1 = "image/x-nikon-nrw"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 13
            goto L_0x00e8
        L_0x002c:
            java.lang.String r1 = "image/x-nikon-nef"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 12
            goto L_0x00e8
        L_0x0038:
            java.lang.String r1 = "image/x-olympus-orf"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 14
            goto L_0x00e8
        L_0x0044:
            java.lang.String r1 = "image/x-ico"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 7
            goto L_0x00e8
        L_0x004f:
            java.lang.String r1 = "image/vnd.wap.wbmp"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 8
            goto L_0x00e8
        L_0x005b:
            java.lang.String r1 = "image/x-pentax-pef"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 17
            goto L_0x00e8
        L_0x0067:
            java.lang.String r1 = "image/png"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = r3
            goto L_0x00e8
        L_0x0072:
            java.lang.String r1 = "image/gif"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 3
            goto L_0x00e8
        L_0x007d:
            java.lang.String r1 = "image/bmp"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 6
            goto L_0x00e8
        L_0x0088:
            java.lang.String r1 = "image/x-panasonic-rw2"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 16
            goto L_0x00e8
        L_0x0093:
            java.lang.String r1 = "image/x-adobe-dng"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 11
            goto L_0x00e8
        L_0x009e:
            java.lang.String r1 = "image/webp"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 2
            goto L_0x00e8
        L_0x00a8:
            java.lang.String r1 = "image/jpeg"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = r2
            goto L_0x00e8
        L_0x00b2:
            java.lang.String r1 = "image/heif"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 4
            goto L_0x00e8
        L_0x00bc:
            java.lang.String r1 = "image/heic"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 5
            goto L_0x00e8
        L_0x00c6:
            java.lang.String r1 = "image/x-sony-arw"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 9
            goto L_0x00e8
        L_0x00d1:
            java.lang.String r1 = "image/x-samsung-srw"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 18
            goto L_0x00e8
        L_0x00dc:
            java.lang.String r1 = "image/x-fuji-raf"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00e7
            r0 = 15
            goto L_0x00e8
        L_0x00e7:
            r0 = -1
        L_0x00e8:
            switch(r0) {
                case 0: goto L_0x00ec;
                case 1: goto L_0x00ec;
                case 2: goto L_0x00ec;
                case 3: goto L_0x00ec;
                case 4: goto L_0x00ec;
                case 5: goto L_0x00ec;
                case 6: goto L_0x00ec;
                case 7: goto L_0x00ec;
                case 8: goto L_0x00ec;
                case 9: goto L_0x00ec;
                case 10: goto L_0x00ec;
                case 11: goto L_0x00ec;
                case 12: goto L_0x00ec;
                case 13: goto L_0x00ec;
                case 14: goto L_0x00ec;
                case 15: goto L_0x00ec;
                case 16: goto L_0x00ec;
                case 17: goto L_0x00ec;
                case 18: goto L_0x00ec;
                default: goto L_0x00eb;
            }
        L_0x00eb:
            return r3
        L_0x00ec:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.ImageDecoder.isMimeTypeSupported(java.lang.String):boolean");
    }

    public static Source createSource(Resources res, int resId) {
        return new ResourceSource(res, resId);
    }

    public static Source createSource(ContentResolver cr, Uri uri) {
        return new ContentResolverSource(cr, uri, (Resources) null);
    }

    public static Source createSource(ContentResolver cr, Uri uri, Resources res) {
        return new ContentResolverSource(cr, uri, res);
    }

    public static Source createSource(AssetManager assets, String fileName) {
        return new AssetSource(assets, fileName);
    }

    public static Source createSource(byte[] data, int offset, int length) throws ArrayIndexOutOfBoundsException {
        if (data == null) {
            throw new NullPointerException("null byte[] in createSource!");
        } else if (offset >= 0 && length >= 0 && offset < data.length && offset + length <= data.length) {
            return new ByteArraySource(data, offset, length);
        } else {
            throw new ArrayIndexOutOfBoundsException("invalid offset/length!");
        }
    }

    public static Source createSource(byte[] data) {
        return createSource(data, 0, data.length);
    }

    public static Source createSource(ByteBuffer buffer) {
        return new ByteBufferSource(buffer);
    }

    public static Source createSource(Resources res, InputStream is) {
        return new InputStreamSource(res, is, Bitmap.getDefaultDensity());
    }

    public static Source createSource(Resources res, InputStream is, int density) {
        return new InputStreamSource(res, is, density);
    }

    public static Source createSource(File file) {
        return new FileSource(file);
    }

    public static Source createSource(Callable<AssetFileDescriptor> callable) {
        return new CallableSource(callable);
    }

    public Size getSampledSize(int sampleSize) {
        if (sampleSize <= 0) {
            throw new IllegalArgumentException("sampleSize must be positive! provided " + sampleSize);
        } else if (this.mNativePtr != 0) {
            return nGetSampledSize(this.mNativePtr, sampleSize);
        } else {
            throw new IllegalStateException("ImageDecoder is closed!");
        }
    }

    @Deprecated
    public ImageDecoder setResize(int width, int height) {
        setTargetSize(width, height);
        return this;
    }

    public void setTargetSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive! provided (" + width + ", " + height + ")");
        }
        this.mDesiredWidth = width;
        this.mDesiredHeight = height;
    }

    @Deprecated
    public ImageDecoder setResize(int sampleSize) {
        setTargetSampleSize(sampleSize);
        return this;
    }

    private int getTargetDimension(int original, int sampleSize, int computed) {
        if (sampleSize >= original) {
            return 1;
        }
        int target = original / sampleSize;
        if (computed != target && Math.abs((computed * sampleSize) - original) >= sampleSize) {
            return target;
        }
        return computed;
    }

    public void setTargetSampleSize(int sampleSize) {
        Size size = getSampledSize(sampleSize);
        setTargetSize(getTargetDimension(this.mWidth, sampleSize, size.getWidth()), getTargetDimension(this.mHeight, sampleSize, size.getHeight()));
    }

    private boolean requestedResize() {
        return (this.mWidth == this.mDesiredWidth && this.mHeight == this.mDesiredHeight) ? false : true;
    }

    public void setAllocator(int allocator) {
        if (allocator < 0 || allocator > 3) {
            throw new IllegalArgumentException("invalid allocator " + allocator);
        }
        this.mAllocator = allocator;
    }

    public int getAllocator() {
        return this.mAllocator;
    }

    public void setUnpremultipliedRequired(boolean unpremultipliedRequired) {
        this.mUnpremultipliedRequired = unpremultipliedRequired;
    }

    @Deprecated
    public ImageDecoder setRequireUnpremultiplied(boolean unpremultipliedRequired) {
        setUnpremultipliedRequired(unpremultipliedRequired);
        return this;
    }

    public boolean isUnpremultipliedRequired() {
        return this.mUnpremultipliedRequired;
    }

    @Deprecated
    public boolean getRequireUnpremultiplied() {
        return isUnpremultipliedRequired();
    }

    public void setPostProcessor(PostProcessor postProcessor) {
        this.mPostProcessor = postProcessor;
    }

    public PostProcessor getPostProcessor() {
        return this.mPostProcessor;
    }

    public void setOnPartialImageListener(OnPartialImageListener listener) {
        this.mOnPartialImageListener = listener;
    }

    public OnPartialImageListener getOnPartialImageListener() {
        return this.mOnPartialImageListener;
    }

    public void setCrop(Rect subset) {
        this.mCropRect = subset;
    }

    public Rect getCrop() {
        return this.mCropRect;
    }

    public void setOutPaddingRect(Rect outPadding) {
        this.mOutPaddingRect = outPadding;
    }

    public void setMutableRequired(boolean mutable) {
        this.mMutable = mutable;
    }

    @Deprecated
    public ImageDecoder setMutable(boolean mutable) {
        setMutableRequired(mutable);
        return this;
    }

    public boolean isMutableRequired() {
        return this.mMutable;
    }

    @Deprecated
    public boolean getMutable() {
        return isMutableRequired();
    }

    public void setMemorySizePolicy(int policy) {
        this.mConserveMemory = policy == 0;
    }

    public int getMemorySizePolicy() {
        return this.mConserveMemory ^ true ? 1 : 0;
    }

    @Deprecated
    public void setConserveMemory(boolean conserveMemory) {
        this.mConserveMemory = conserveMemory;
    }

    @Deprecated
    public boolean getConserveMemory() {
        return this.mConserveMemory;
    }

    public void setDecodeAsAlphaMaskEnabled(boolean enabled) {
        this.mDecodeAsAlphaMask = enabled;
    }

    @Deprecated
    public ImageDecoder setDecodeAsAlphaMask(boolean enabled) {
        setDecodeAsAlphaMaskEnabled(enabled);
        return this;
    }

    @Deprecated
    public ImageDecoder setAsAlphaMask(boolean asAlphaMask) {
        setDecodeAsAlphaMask(asAlphaMask);
        return this;
    }

    public boolean isDecodeAsAlphaMaskEnabled() {
        return this.mDecodeAsAlphaMask;
    }

    @Deprecated
    public boolean getDecodeAsAlphaMask() {
        return this.mDecodeAsAlphaMask;
    }

    @Deprecated
    public boolean getAsAlphaMask() {
        return getDecodeAsAlphaMask();
    }

    public void setTargetColorSpace(ColorSpace colorSpace) {
        this.mDesiredColorSpace = colorSpace;
    }

    public void close() {
        this.mCloseGuard.close();
        if (this.mClosed.compareAndSet(false, true)) {
            nClose(this.mNativePtr);
            this.mNativePtr = 0;
            if (this.mOwnsInputStream) {
                IoUtils.closeQuietly(this.mInputStream);
            }
            IoUtils.closeQuietly(this.mAssetFd);
            this.mInputStream = null;
            this.mAssetFd = null;
            this.mTempStorage = null;
        }
    }

    private void checkState(boolean animated) {
        if (this.mNativePtr != 0) {
            checkSubset(this.mDesiredWidth, this.mDesiredHeight, this.mCropRect);
            if (!animated && this.mAllocator == 3) {
                if (this.mMutable) {
                    throw new IllegalStateException("Cannot make mutable HARDWARE Bitmap!");
                } else if (this.mDecodeAsAlphaMask) {
                    throw new IllegalStateException("Cannot make HARDWARE Alpha mask Bitmap!");
                }
            }
            if (this.mPostProcessor != null && this.mUnpremultipliedRequired) {
                throw new IllegalStateException("Cannot draw to unpremultiplied pixels!");
            }
            return;
        }
        throw new IllegalStateException("Cannot use closed ImageDecoder!");
    }

    private static void checkSubset(int width, int height, Rect r) {
        if (r != null) {
            if (r.left < 0 || r.top < 0 || r.right > width || r.bottom > height) {
                throw new IllegalStateException("Subset " + r + " not contained by scaled image bounds: (" + width + " x " + height + ")");
            }
        }
    }

    private boolean checkForExtended() {
        if (this.mDesiredColorSpace == null) {
            return false;
        }
        if (this.mDesiredColorSpace == ColorSpace.get(ColorSpace.Named.EXTENDED_SRGB) || this.mDesiredColorSpace == ColorSpace.get(ColorSpace.Named.LINEAR_EXTENDED_SRGB)) {
            return true;
        }
        return false;
    }

    private long getColorSpacePtr() {
        if (this.mDesiredColorSpace == null) {
            return 0;
        }
        return this.mDesiredColorSpace.getNativeInstance();
    }

    private Bitmap decodeBitmapInternal() throws IOException {
        boolean z = false;
        checkState(false);
        long j = this.mNativePtr;
        if (this.mPostProcessor != null) {
            z = true;
        }
        boolean z2 = z;
        return nDecodeBitmap(j, this, z2, this.mDesiredWidth, this.mDesiredHeight, this.mCropRect, this.mMutable, this.mAllocator, this.mUnpremultipliedRequired, this.mConserveMemory, this.mDecodeAsAlphaMask, getColorSpacePtr(), checkForExtended());
    }

    private void callHeaderDecoded(OnHeaderDecodedListener listener, Source src) {
        if (listener != null) {
            ImageInfo info = new ImageInfo();
            try {
                listener.onHeaderDecoded(this, info, src);
            } finally {
                ImageDecoder unused = info.mDecoder = null;
            }
        }
    }

    public static Drawable decodeDrawable(Source src, OnHeaderDecodedListener listener) throws IOException {
        if (listener != null) {
            return decodeDrawableImpl(src, listener);
        }
        throw new IllegalArgumentException("listener cannot be null! Use decodeDrawable(Source) to not have a listener");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00c2, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00c4, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00d4, code lost:
        $closeResource(r6, r2);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00c4 A[ExcHandler: Throwable (th java.lang.Throwable), Splitter:B:4:0x000a] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00d4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.graphics.drawable.Drawable decodeDrawableImpl(android.graphics.ImageDecoder.Source r20, android.graphics.ImageDecoder.OnHeaderDecodedListener r21) throws java.io.IOException {
        /*
            r1 = r20
            android.graphics.ImageDecoder r2 = r20.createImageDecoder()
            r2.mSource = r1     // Catch:{ Throwable -> 0x00cb, all -> 0x00c6 }
            r4 = r21
            r2.callHeaderDecoded(r4, r1)     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            boolean r0 = r2.mUnpremultipliedRequired     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            if (r0 != 0) goto L_0x00b7
            boolean r0 = r2.mMutable     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            if (r0 != 0) goto L_0x00ae
            int r0 = r2.computeDensity(r1)     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            boolean r5 = r2.mAnimated     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            if (r5 == 0) goto L_0x005c
            android.graphics.PostProcessor r5 = r2.mPostProcessor     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            if (r5 != 0) goto L_0x0023
            r8 = 0
            goto L_0x0024
        L_0x0023:
            r8 = r2
        L_0x0024:
            r5 = 1
            r2.checkState(r5)     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            android.graphics.drawable.AnimatedImageDrawable r19 = new android.graphics.drawable.AnimatedImageDrawable     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            long r6 = r2.mNativePtr     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            int r9 = r2.mDesiredWidth     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            int r10 = r2.mDesiredHeight     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            long r11 = r2.getColorSpacePtr()     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            boolean r13 = r2.checkForExtended()     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            int r15 = r20.computeDstDensity()     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            android.graphics.Rect r14 = r2.mCropRect     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            java.io.InputStream r5 = r2.mInputStream     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            android.content.res.AssetFileDescriptor r3 = r2.mAssetFd     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r17 = r5
            r5 = r19
            r16 = r14
            r14 = r0
            r18 = r3
            r5.<init>(r6, r8, r9, r10, r11, r13, r14, r15, r16, r17, r18)     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r3 = r19
            r5 = 0
            r2.mInputStream = r5     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r2.mAssetFd = r5     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            if (r2 == 0) goto L_0x005b
            $closeResource(r5, r2)
        L_0x005b:
            return r3
        L_0x005c:
            android.graphics.Bitmap r3 = r2.decodeBitmapInternal()     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r3.setDensity(r0)     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            android.content.res.Resources r5 = r20.getResources()     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r12 = r5
            byte[] r5 = r3.getNinePatchChunk()     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r13 = r5
            if (r13 == 0) goto L_0x00a2
            boolean r5 = android.graphics.NinePatch.isNinePatchChunk(r13)     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            if (r5 == 0) goto L_0x00a2
            android.graphics.Rect r5 = new android.graphics.Rect     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r5.<init>()     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r14 = r5
            r3.getOpticalInsets(r14)     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            android.graphics.Rect r5 = r2.mOutPaddingRect     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            if (r5 != 0) goto L_0x0088
            android.graphics.Rect r6 = new android.graphics.Rect     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r6.<init>()     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r5 = r6
        L_0x0088:
            r15 = r5
            long r5 = r2.mNativePtr     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            nGetPadding(r5, r15)     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            android.graphics.drawable.NinePatchDrawable r16 = new android.graphics.drawable.NinePatchDrawable     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r11 = 0
            r5 = r16
            r6 = r12
            r7 = r3
            r8 = r13
            r9 = r15
            r10 = r14
            r5.<init>(r6, r7, r8, r9, r10, r11)     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            if (r2 == 0) goto L_0x00a1
            r5 = 0
            $closeResource(r5, r2)
        L_0x00a1:
            return r16
        L_0x00a2:
            android.graphics.drawable.BitmapDrawable r5 = new android.graphics.drawable.BitmapDrawable     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r5.<init>((android.content.res.Resources) r12, (android.graphics.Bitmap) r3)     // Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            if (r2 == 0) goto L_0x00ad
            r6 = 0
            $closeResource(r6, r2)
        L_0x00ad:
            return r5
        L_0x00ae:
            r6 = 0
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException     // Catch:{ Throwable -> 0x00c4, all -> 0x00c0 }
            java.lang.String r3 = "Cannot decode a mutable Drawable!"
            r0.<init>(r3)     // Catch:{ Throwable -> 0x00c4, all -> 0x00c0 }
            throw r0     // Catch:{ Throwable -> 0x00c4, all -> 0x00c0 }
        L_0x00b7:
            r6 = 0
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException     // Catch:{ Throwable -> 0x00c4, all -> 0x00c0 }
            java.lang.String r3 = "Cannot decode a Drawable with unpremultiplied pixels!"
            r0.<init>(r3)     // Catch:{ Throwable -> 0x00c4, all -> 0x00c0 }
            throw r0     // Catch:{ Throwable -> 0x00c4, all -> 0x00c0 }
        L_0x00c0:
            r0 = move-exception
            goto L_0x00d2
        L_0x00c2:
            r0 = move-exception
            goto L_0x00c9
        L_0x00c4:
            r0 = move-exception
            goto L_0x00ce
        L_0x00c6:
            r0 = move-exception
            r4 = r21
        L_0x00c9:
            r6 = 0
            goto L_0x00d2
        L_0x00cb:
            r0 = move-exception
            r4 = r21
        L_0x00ce:
            r3 = r0
            throw r3     // Catch:{ all -> 0x00d0 }
        L_0x00d0:
            r0 = move-exception
            r6 = r3
        L_0x00d2:
            if (r2 == 0) goto L_0x00d7
            $closeResource(r6, r2)
        L_0x00d7:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.ImageDecoder.decodeDrawableImpl(android.graphics.ImageDecoder$Source, android.graphics.ImageDecoder$OnHeaderDecodedListener):android.graphics.drawable.Drawable");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
            } catch (Throwable th) {
                x0.addSuppressed(th);
            }
        } else {
            x1.close();
        }
    }

    public static Drawable decodeDrawable(Source src) throws IOException {
        return decodeDrawableImpl(src, (OnHeaderDecodedListener) null);
    }

    public static Bitmap decodeBitmap(Source src, OnHeaderDecodedListener listener) throws IOException {
        if (listener != null) {
            return decodeBitmapImpl(src, listener);
        }
        throw new IllegalArgumentException("listener cannot be null! Use decodeBitmap(Source) to not have a listener");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0031, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0035, code lost:
        if (r0 != null) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0037, code lost:
        $closeResource(r1, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003a, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.graphics.Bitmap decodeBitmapImpl(android.graphics.ImageDecoder.Source r8, android.graphics.ImageDecoder.OnHeaderDecodedListener r9) throws java.io.IOException {
        /*
            android.graphics.ImageDecoder r0 = r8.createImageDecoder()
            r1 = 0
            r0.mSource = r8     // Catch:{ Throwable -> 0x0033 }
            r0.callHeaderDecoded(r9, r8)     // Catch:{ Throwable -> 0x0033 }
            int r2 = r0.computeDensity(r8)     // Catch:{ Throwable -> 0x0033 }
            android.graphics.Bitmap r3 = r0.decodeBitmapInternal()     // Catch:{ Throwable -> 0x0033 }
            r3.setDensity(r2)     // Catch:{ Throwable -> 0x0033 }
            android.graphics.Rect r4 = r0.mOutPaddingRect     // Catch:{ Throwable -> 0x0033 }
            if (r4 == 0) goto L_0x002a
            byte[] r5 = r3.getNinePatchChunk()     // Catch:{ Throwable -> 0x0033 }
            if (r5 == 0) goto L_0x002a
            boolean r6 = android.graphics.NinePatch.isNinePatchChunk(r5)     // Catch:{ Throwable -> 0x0033 }
            if (r6 == 0) goto L_0x002a
            long r6 = r0.mNativePtr     // Catch:{ Throwable -> 0x0033 }
            nGetPadding(r6, r4)     // Catch:{ Throwable -> 0x0033 }
        L_0x002a:
            if (r0 == 0) goto L_0x0030
            $closeResource(r1, r0)
        L_0x0030:
            return r3
        L_0x0031:
            r2 = move-exception
            goto L_0x0035
        L_0x0033:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x0031 }
        L_0x0035:
            if (r0 == 0) goto L_0x003a
            $closeResource(r1, r0)
        L_0x003a:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.ImageDecoder.decodeBitmapImpl(android.graphics.ImageDecoder$Source, android.graphics.ImageDecoder$OnHeaderDecodedListener):android.graphics.Bitmap");
    }

    private int computeDensity(Source src) {
        int dstDensity;
        if (requestedResize()) {
            return 0;
        }
        int srcDensity = src.getDensity();
        if (srcDensity == 0) {
            return srcDensity;
        }
        if (this.mIsNinePatch && this.mPostProcessor == null) {
            return srcDensity;
        }
        Resources res = src.getResources();
        if ((res != null && res.getDisplayMetrics().noncompatDensityDpi == srcDensity) || srcDensity == (dstDensity = src.computeDstDensity())) {
            return srcDensity;
        }
        if (srcDensity < dstDensity && sApiLevel >= 28) {
            return srcDensity;
        }
        float scale = ((float) dstDensity) / ((float) srcDensity);
        setTargetSize(Math.max((int) ((((float) this.mWidth) * scale) + 0.5f), 1), Math.max((int) ((((float) this.mHeight) * scale) + 0.5f), 1));
        return dstDensity;
    }

    /* access modifiers changed from: private */
    public String getMimeType() {
        return nGetMimeType(this.mNativePtr);
    }

    /* access modifiers changed from: private */
    public ColorSpace getColorSpace() {
        return nGetColorSpace(this.mNativePtr);
    }

    public static Bitmap decodeBitmap(Source src) throws IOException {
        return decodeBitmapImpl(src, (OnHeaderDecodedListener) null);
    }

    @UnsupportedAppUsage
    private int postProcessAndRelease(Canvas canvas) {
        try {
            return this.mPostProcessor.onPostProcess(canvas);
        } finally {
            canvas.release();
        }
    }

    private void onPartialImage(int error, Throwable cause) throws DecodeException {
        DecodeException exception = new DecodeException(error, cause, this.mSource);
        if (this.mOnPartialImageListener == null || !this.mOnPartialImageListener.onPartialImage(exception)) {
            throw exception;
        }
    }
}
