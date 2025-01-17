package android.graphics.pdf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import dalvik.system.CloseGuard;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PdfDocument {
    private final byte[] mChunk = new byte[4096];
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private Page mCurrentPage;
    private long mNativeDocument = nativeCreateDocument();
    private final List<PageInfo> mPages = new ArrayList();

    private native void nativeClose(long j);

    private native long nativeCreateDocument();

    private native void nativeFinishPage(long j);

    private static native long nativeStartPage(long j, int i, int i2, int i3, int i4, int i5, int i6);

    private native void nativeWriteTo(long j, OutputStream outputStream, byte[] bArr);

    public PdfDocument() {
        this.mCloseGuard.open("close");
    }

    public Page startPage(PageInfo pageInfo) {
        throwIfClosed();
        throwIfCurrentPageNotFinished();
        if (pageInfo != null) {
            this.mCurrentPage = new Page(new PdfCanvas(nativeStartPage(this.mNativeDocument, pageInfo.mPageWidth, pageInfo.mPageHeight, pageInfo.mContentRect.left, pageInfo.mContentRect.top, pageInfo.mContentRect.right, pageInfo.mContentRect.bottom)), pageInfo);
            return this.mCurrentPage;
        }
        throw new IllegalArgumentException("page cannot be null");
    }

    public void finishPage(Page page) {
        throwIfClosed();
        if (page == null) {
            throw new IllegalArgumentException("page cannot be null");
        } else if (page != this.mCurrentPage) {
            throw new IllegalStateException("invalid page");
        } else if (!page.isFinished()) {
            this.mPages.add(page.getInfo());
            this.mCurrentPage = null;
            nativeFinishPage(this.mNativeDocument);
            page.finish();
        } else {
            throw new IllegalStateException("page already finished");
        }
    }

    public void writeTo(OutputStream out) throws IOException {
        throwIfClosed();
        throwIfCurrentPageNotFinished();
        if (out != null) {
            nativeWriteTo(this.mNativeDocument, out, this.mChunk);
            return;
        }
        throw new IllegalArgumentException("out cannot be null!");
    }

    public List<PageInfo> getPages() {
        return Collections.unmodifiableList(this.mPages);
    }

    public void close() {
        throwIfCurrentPageNotFinished();
        dispose();
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            dispose();
        } finally {
            super.finalize();
        }
    }

    private void dispose() {
        if (this.mNativeDocument != 0) {
            nativeClose(this.mNativeDocument);
            this.mCloseGuard.close();
            this.mNativeDocument = 0;
        }
    }

    private void throwIfClosed() {
        if (this.mNativeDocument == 0) {
            throw new IllegalStateException("document is closed!");
        }
    }

    private void throwIfCurrentPageNotFinished() {
        if (this.mCurrentPage != null) {
            throw new IllegalStateException("Current page not finished!");
        }
    }

    private final class PdfCanvas extends Canvas {
        public PdfCanvas(long nativeCanvas) {
            super(nativeCanvas);
        }

        public void setBitmap(Bitmap bitmap) {
            throw new UnsupportedOperationException();
        }
    }

    public static final class PageInfo {
        /* access modifiers changed from: private */
        public Rect mContentRect;
        /* access modifiers changed from: private */
        public int mPageHeight;
        /* access modifiers changed from: private */
        public int mPageNumber;
        /* access modifiers changed from: private */
        public int mPageWidth;

        private PageInfo() {
        }

        public int getPageWidth() {
            return this.mPageWidth;
        }

        public int getPageHeight() {
            return this.mPageHeight;
        }

        public Rect getContentRect() {
            return this.mContentRect;
        }

        public int getPageNumber() {
            return this.mPageNumber;
        }

        public static final class Builder {
            private final PageInfo mPageInfo = new PageInfo();

            public Builder(int pageWidth, int pageHeight, int pageNumber) {
                if (pageWidth <= 0) {
                    throw new IllegalArgumentException("page width must be positive");
                } else if (pageHeight <= 0) {
                    throw new IllegalArgumentException("page width must be positive");
                } else if (pageNumber >= 0) {
                    int unused = this.mPageInfo.mPageWidth = pageWidth;
                    int unused2 = this.mPageInfo.mPageHeight = pageHeight;
                    int unused3 = this.mPageInfo.mPageNumber = pageNumber;
                } else {
                    throw new IllegalArgumentException("pageNumber must be non negative");
                }
            }

            public Builder setContentRect(Rect contentRect) {
                if (contentRect == null || (contentRect.left >= 0 && contentRect.top >= 0 && contentRect.right <= this.mPageInfo.mPageWidth && contentRect.bottom <= this.mPageInfo.mPageHeight)) {
                    Rect unused = this.mPageInfo.mContentRect = contentRect;
                    return this;
                }
                throw new IllegalArgumentException("contentRect does not fit the page");
            }

            public PageInfo create() {
                if (this.mPageInfo.mContentRect == null) {
                    Rect unused = this.mPageInfo.mContentRect = new Rect(0, 0, this.mPageInfo.mPageWidth, this.mPageInfo.mPageHeight);
                }
                return this.mPageInfo;
            }
        }
    }

    public static final class Page {
        private Canvas mCanvas;
        private final PageInfo mPageInfo;

        private Page(Canvas canvas, PageInfo pageInfo) {
            this.mCanvas = canvas;
            this.mPageInfo = pageInfo;
        }

        public Canvas getCanvas() {
            return this.mCanvas;
        }

        public PageInfo getInfo() {
            return this.mPageInfo;
        }

        /* access modifiers changed from: package-private */
        public boolean isFinished() {
            return this.mCanvas == null;
        }

        /* access modifiers changed from: private */
        public void finish() {
            if (this.mCanvas != null) {
                this.mCanvas.release();
                this.mCanvas = null;
            }
        }
    }
}
