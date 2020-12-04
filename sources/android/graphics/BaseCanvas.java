package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.text.MeasuredText;
import android.text.GraphicsOperations;
import android.text.PrecomputedText;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.TextUtils;

public abstract class BaseCanvas {
    private boolean mAllowHwBitmapsInSwMode = false;
    protected int mDensity = 0;
    @UnsupportedAppUsage
    protected long mNativeCanvasWrapper;
    protected int mScreenDensity = 0;

    private static native void nDrawArc(long j, float f, float f2, float f3, float f4, float f5, float f6, boolean z, long j2);

    private static native void nDrawBitmap(long j, long j2, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, long j3, int i, int i2);

    private static native void nDrawBitmap(long j, long j2, float f, float f2, long j3, int i, int i2, int i3);

    private static native void nDrawBitmap(long j, int[] iArr, int i, int i2, float f, float f2, int i3, int i4, boolean z, long j2);

    private static native void nDrawBitmapMatrix(long j, long j2, long j3, long j4);

    private static native void nDrawBitmapMesh(long j, long j2, int i, int i2, float[] fArr, int i3, int[] iArr, int i4, long j3);

    private static native void nDrawCircle(long j, float f, float f2, float f3, long j2);

    private static native void nDrawColor(long j, int i, int i2);

    private static native void nDrawColor(long j, long j2, long j3, int i);

    private static native void nDrawDoubleRoundRect(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, long j2);

    private static native void nDrawDoubleRoundRect(long j, float f, float f2, float f3, float f4, float[] fArr, float f5, float f6, float f7, float f8, float[] fArr2, long j2);

    private static native void nDrawLine(long j, float f, float f2, float f3, float f4, long j2);

    private static native void nDrawLines(long j, float[] fArr, int i, int i2, long j2);

    private static native void nDrawNinePatch(long j, long j2, long j3, float f, float f2, float f3, float f4, long j4, int i, int i2);

    private static native void nDrawOval(long j, float f, float f2, float f3, float f4, long j2);

    private static native void nDrawPaint(long j, long j2);

    private static native void nDrawPath(long j, long j2, long j3);

    private static native void nDrawPoint(long j, float f, float f2, long j2);

    private static native void nDrawPoints(long j, float[] fArr, int i, int i2, long j2);

    private static native void nDrawRect(long j, float f, float f2, float f3, float f4, long j2);

    private static native void nDrawRegion(long j, long j2, long j3);

    private static native void nDrawRoundRect(long j, float f, float f2, float f3, float f4, float f5, float f6, long j2);

    private static native void nDrawText(long j, String str, int i, int i2, float f, float f2, int i3, long j2);

    private static native void nDrawText(long j, char[] cArr, int i, int i2, float f, float f2, int i3, long j2);

    private static native void nDrawTextOnPath(long j, String str, long j2, float f, float f2, int i, long j3);

    private static native void nDrawTextOnPath(long j, char[] cArr, int i, int i2, long j2, float f, float f2, int i3, long j3);

    private static native void nDrawTextRun(long j, String str, int i, int i2, int i3, int i4, float f, float f2, boolean z, long j2);

    private static native void nDrawTextRun(long j, char[] cArr, int i, int i2, int i3, int i4, float f, float f2, boolean z, long j2, long j3);

    private static native void nDrawVertices(long j, int i, int i2, float[] fArr, int i3, float[] fArr2, int i4, int[] iArr, int i5, short[] sArr, int i6, int i7, long j2);

    /* access modifiers changed from: protected */
    public void throwIfCannotDraw(Bitmap bitmap) {
        if (bitmap.isRecycled()) {
            throw new RuntimeException("Canvas: trying to use a recycled bitmap " + bitmap);
        } else if (bitmap.isPremultiplied() || bitmap.getConfig() != Bitmap.Config.ARGB_8888 || !bitmap.hasAlpha()) {
            throwIfHwBitmapInSwMode(bitmap);
        } else {
            throw new RuntimeException("Canvas: trying to use a non-premultiplied bitmap " + bitmap);
        }
    }

    protected static final void checkRange(int length, int offset, int count) {
        if ((offset | count) < 0 || offset + count > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public boolean isHardwareAccelerated() {
        return false;
    }

    public void drawArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean useCenter, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        nDrawArc(this.mNativeCanvasWrapper, left, top, right, bottom, startAngle, sweepAngle, useCenter, paint.getNativeInstance());
    }

    public void drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        drawArc(oval.left, oval.top, oval.right, oval.bottom, startAngle, sweepAngle, useCenter, paint);
    }

    public void drawARGB(int a, int r, int g, int b) {
        drawColor(Color.argb(a, r, g, b));
    }

    public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint) {
        throwIfCannotDraw(bitmap);
        throwIfHasHwBitmapInSwMode(paint);
        nDrawBitmap(this.mNativeCanvasWrapper, bitmap.getNativeInstance(), left, top, paint != null ? paint.getNativeInstance() : 0, this.mDensity, this.mScreenDensity, bitmap.mDensity);
    }

    public void drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        nDrawBitmapMatrix(this.mNativeCanvasWrapper, bitmap.getNativeInstance(), matrix.ni(), paint != null ? paint.getNativeInstance() : 0);
    }

    public void drawBitmap(Bitmap bitmap, Rect src, Rect dst, Paint paint) {
        int left;
        int right;
        int top;
        int bottom;
        Rect rect = src;
        Rect rect2 = dst;
        Paint paint2 = paint;
        if (rect2 != null) {
            throwIfCannotDraw(bitmap);
            throwIfHasHwBitmapInSwMode(paint2);
            long nativePaint = paint2 == null ? 0 : paint.getNativeInstance();
            if (rect == null) {
                left = 0;
                top = 0;
                right = bitmap.getWidth();
                bottom = bitmap.getHeight();
            } else {
                left = rect.left;
                right = rect.right;
                top = rect.top;
                bottom = rect.bottom;
            }
            int right2 = right;
            int bottom2 = bottom;
            int i = left;
            int i2 = top;
            int i3 = bottom2;
            int i4 = right2;
            nDrawBitmap(this.mNativeCanvasWrapper, bitmap.getNativeInstance(), (float) left, (float) top, (float) right2, (float) bottom2, (float) rect2.left, (float) rect2.top, (float) rect2.right, (float) rect2.bottom, nativePaint, this.mScreenDensity, bitmap.mDensity);
            return;
        }
        Bitmap bitmap2 = bitmap;
        throw new NullPointerException();
    }

    public void drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint) {
        float left;
        float f;
        float right;
        float top;
        Rect rect = src;
        RectF rectF = dst;
        Paint paint2 = paint;
        if (rectF != null) {
            throwIfCannotDraw(bitmap);
            throwIfHasHwBitmapInSwMode(paint2);
            long nativePaint = paint2 == null ? 0 : paint.getNativeInstance();
            if (rect == null) {
                left = 0.0f;
                top = 0.0f;
                f = (float) bitmap.getHeight();
                right = (float) bitmap.getWidth();
            } else {
                left = (float) rect.left;
                f = (float) rect.bottom;
                right = (float) rect.right;
                top = (float) rect.top;
            }
            float bottom = f;
            nDrawBitmap(this.mNativeCanvasWrapper, bitmap.getNativeInstance(), left, top, right, bottom, rectF.left, rectF.top, rectF.right, rectF.bottom, nativePaint, this.mScreenDensity, bitmap.mDensity);
            return;
        }
        Bitmap bitmap2 = bitmap;
        throw new NullPointerException();
    }

    @Deprecated
    public void drawBitmap(int[] colors, int offset, int stride, float x, float y, int width, int height, boolean hasAlpha, Paint paint) {
        int i = width;
        Paint paint2 = paint;
        if (i < 0) {
            throw new IllegalArgumentException("width must be >= 0");
        } else if (height < 0) {
            throw new IllegalArgumentException("height must be >= 0");
        } else if (Math.abs(stride) >= i) {
            int lastScanline = offset + ((height - 1) * stride);
            int length = colors.length;
            if (offset < 0 || offset + i > length || lastScanline < 0 || lastScanline + i > length) {
                throw new ArrayIndexOutOfBoundsException();
            }
            throwIfHasHwBitmapInSwMode(paint2);
            if (i == 0) {
            } else if (height == 0) {
                int i2 = length;
            } else {
                int i3 = length;
                nDrawBitmap(this.mNativeCanvasWrapper, colors, offset, stride, x, y, width, height, hasAlpha, paint2 != null ? paint.getNativeInstance() : 0);
            }
        } else {
            throw new IllegalArgumentException("abs(stride) must be >= width");
        }
    }

    @Deprecated
    public void drawBitmap(int[] colors, int offset, int stride, int x, int y, int width, int height, boolean hasAlpha, Paint paint) {
        drawBitmap(colors, offset, stride, (float) x, (float) y, width, height, hasAlpha, paint);
    }

    public void drawBitmapMesh(Bitmap bitmap, int meshWidth, int meshHeight, float[] verts, int vertOffset, int[] colors, int colorOffset, Paint paint) {
        int i = vertOffset;
        int[] iArr = colors;
        int i2 = colorOffset;
        Paint paint2 = paint;
        if ((meshWidth | meshHeight | i | i2) >= 0) {
            throwIfHasHwBitmapInSwMode(paint2);
            if (meshWidth != 0 && meshHeight != 0) {
                int count = (meshWidth + 1) * (meshHeight + 1);
                checkRange(verts.length, i, count * 2);
                if (iArr != null) {
                    checkRange(iArr.length, i2, count);
                }
                int i3 = count;
                nDrawBitmapMesh(this.mNativeCanvasWrapper, bitmap.getNativeInstance(), meshWidth, meshHeight, verts, vertOffset, colors, colorOffset, paint2 != null ? paint.getNativeInstance() : 0);
                return;
            }
            return;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void drawCircle(float cx, float cy, float radius, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        nDrawCircle(this.mNativeCanvasWrapper, cx, cy, radius, paint.getNativeInstance());
    }

    public void drawColor(int color) {
        nDrawColor(this.mNativeCanvasWrapper, color, BlendMode.SRC_OVER.getXfermode().porterDuffMode);
    }

    public void drawColor(int color, PorterDuff.Mode mode) {
        nDrawColor(this.mNativeCanvasWrapper, color, mode.nativeInt);
    }

    public void drawColor(int color, BlendMode mode) {
        nDrawColor(this.mNativeCanvasWrapper, color, mode.getXfermode().porterDuffMode);
    }

    public void drawColor(long color, BlendMode mode) {
        ColorSpace cs = Color.colorSpace(color);
        nDrawColor(this.mNativeCanvasWrapper, cs.getNativeInstance(), color, mode.getXfermode().porterDuffMode);
    }

    public void drawLine(float startX, float startY, float stopX, float stopY, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        nDrawLine(this.mNativeCanvasWrapper, startX, startY, stopX, stopY, paint.getNativeInstance());
    }

    public void drawLines(float[] pts, int offset, int count, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        nDrawLines(this.mNativeCanvasWrapper, pts, offset, count, paint.getNativeInstance());
    }

    public void drawLines(float[] pts, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        drawLines(pts, 0, pts.length, paint);
    }

    public void drawOval(float left, float top, float right, float bottom, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        nDrawOval(this.mNativeCanvasWrapper, left, top, right, bottom, paint.getNativeInstance());
    }

    public void drawOval(RectF oval, Paint paint) {
        if (oval != null) {
            throwIfHasHwBitmapInSwMode(paint);
            drawOval(oval.left, oval.top, oval.right, oval.bottom, paint);
            return;
        }
        throw new NullPointerException();
    }

    public void drawPaint(Paint paint) {
        nDrawPaint(this.mNativeCanvasWrapper, paint.getNativeInstance());
    }

    public void drawPatch(NinePatch patch, Rect dst, Paint paint) {
        Rect rect = dst;
        Paint paint2 = paint;
        Bitmap bitmap = patch.getBitmap();
        throwIfCannotDraw(bitmap);
        throwIfHasHwBitmapInSwMode(paint2);
        nDrawNinePatch(this.mNativeCanvasWrapper, bitmap.getNativeInstance(), patch.mNativeChunk, (float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bottom, paint2 == null ? 0 : paint.getNativeInstance(), this.mDensity, patch.getDensity());
    }

    public void drawPatch(NinePatch patch, RectF dst, Paint paint) {
        RectF rectF = dst;
        Paint paint2 = paint;
        Bitmap bitmap = patch.getBitmap();
        throwIfCannotDraw(bitmap);
        throwIfHasHwBitmapInSwMode(paint2);
        nDrawNinePatch(this.mNativeCanvasWrapper, bitmap.getNativeInstance(), patch.mNativeChunk, rectF.left, rectF.top, rectF.right, rectF.bottom, paint2 == null ? 0 : paint.getNativeInstance(), this.mDensity, patch.getDensity());
    }

    public void drawPath(Path path, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        if (!path.isSimplePath || path.rects == null) {
            nDrawPath(this.mNativeCanvasWrapper, path.readOnlyNI(), paint.getNativeInstance());
        } else {
            nDrawRegion(this.mNativeCanvasWrapper, path.rects.mNativeRegion, paint.getNativeInstance());
        }
    }

    public void drawPoint(float x, float y, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        nDrawPoint(this.mNativeCanvasWrapper, x, y, paint.getNativeInstance());
    }

    public void drawPoints(float[] pts, int offset, int count, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        nDrawPoints(this.mNativeCanvasWrapper, pts, offset, count, paint.getNativeInstance());
    }

    public void drawPoints(float[] pts, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        drawPoints(pts, 0, pts.length, paint);
    }

    @Deprecated
    public void drawPosText(char[] text, int index, int count, float[] pos, Paint paint) {
        if (index < 0 || index + count > text.length || count * 2 > pos.length) {
            throw new IndexOutOfBoundsException();
        }
        throwIfHasHwBitmapInSwMode(paint);
        for (int i = 0; i < count; i++) {
            char[] cArr = text;
            drawText(cArr, index + i, 1, pos[i * 2], pos[(i * 2) + 1], paint);
        }
    }

    @Deprecated
    public void drawPosText(String text, float[] pos, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        drawPosText(text.toCharArray(), 0, text.length(), pos, paint);
    }

    public void drawRect(float left, float top, float right, float bottom, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        nDrawRect(this.mNativeCanvasWrapper, left, top, right, bottom, paint.getNativeInstance());
    }

    public void drawRect(Rect r, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        drawRect((float) r.left, (float) r.top, (float) r.right, (float) r.bottom, paint);
    }

    public void drawRect(RectF rect, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        nDrawRect(this.mNativeCanvasWrapper, rect.left, rect.top, rect.right, rect.bottom, paint.getNativeInstance());
    }

    public void drawRGB(int r, int g, int b) {
        drawColor(Color.rgb(r, g, b));
    }

    public void drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        nDrawRoundRect(this.mNativeCanvasWrapper, left, top, right, bottom, rx, ry, paint.getNativeInstance());
    }

    public void drawRoundRect(RectF rect, float rx, float ry, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        drawRoundRect(rect.left, rect.top, rect.right, rect.bottom, rx, ry, paint);
    }

    public void drawDoubleRoundRect(RectF outer, float outerRx, float outerRy, RectF inner, float innerRx, float innerRy, Paint paint) {
        RectF rectF = outer;
        RectF rectF2 = inner;
        throwIfHasHwBitmapInSwMode(paint);
        float outerLeft = rectF.left;
        float outerTop = rectF.top;
        float outerRight = rectF.right;
        float outerBottom = rectF.bottom;
        float innerLeft = rectF2.left;
        float innerTop = rectF2.top;
        float innerRight = rectF2.right;
        float innerBottom = rectF2.bottom;
        float innerRight2 = innerRight;
        float innerTop2 = innerTop;
        float innerLeft2 = innerLeft;
        float f = outerBottom;
        float f2 = outerRight;
        float f3 = outerTop;
        float f4 = outerLeft;
        nDrawDoubleRoundRect(this.mNativeCanvasWrapper, outerLeft, outerTop, outerRight, outerBottom, outerRx, outerRy, innerLeft2, innerTop2, innerRight2, innerBottom, innerRx, innerRy, paint.getNativeInstance());
    }

    public void drawDoubleRoundRect(RectF outer, float[] outerRadii, RectF inner, float[] innerRadii, Paint paint) {
        RectF rectF = outer;
        float[] fArr = outerRadii;
        RectF rectF2 = inner;
        float[] fArr2 = innerRadii;
        throwIfHasHwBitmapInSwMode(paint);
        if (fArr2 == null || fArr == null || fArr2.length != 8 || fArr.length != 8) {
            throw new IllegalArgumentException("Both inner and outer radii arrays must contain exactly 8 values");
        }
        float outerLeft = rectF.left;
        float outerTop = rectF.top;
        float outerRight = rectF.right;
        float outerBottom = rectF.bottom;
        float innerLeft = rectF2.left;
        float innerTop = rectF2.top;
        float innerRight = rectF2.right;
        float innerRight2 = innerRight;
        float innerTop2 = innerTop;
        float innerLeft2 = innerLeft;
        float f = outerBottom;
        float f2 = outerRight;
        float f3 = outerTop;
        float f4 = outerLeft;
        nDrawDoubleRoundRect(this.mNativeCanvasWrapper, outerLeft, outerTop, outerRight, outerBottom, outerRadii, innerLeft2, innerTop2, innerRight2, rectF2.bottom, innerRadii, paint.getNativeInstance());
    }

    public void drawText(char[] text, int index, int count, float x, float y, Paint paint) {
        Paint paint2 = paint;
        if ((index | count | (index + count) | ((text.length - index) - count)) >= 0) {
            throwIfHasHwBitmapInSwMode(paint2);
            nDrawText(this.mNativeCanvasWrapper, text, index, count, x, y, paint2.mBidiFlags, paint.getNativeInstance());
            return;
        }
        throw new IndexOutOfBoundsException();
    }

    public void drawText(CharSequence text, int start, int end, float x, float y, Paint paint) {
        CharSequence charSequence = text;
        int i = start;
        int i2 = end;
        Paint paint2 = paint;
        if ((i | i2 | (i2 - i) | (text.length() - i2)) >= 0) {
            throwIfHasHwBitmapInSwMode(paint2);
            if ((charSequence instanceof String) || (charSequence instanceof SpannedString) || (charSequence instanceof SpannableString)) {
                long j = this.mNativeCanvasWrapper;
                String charSequence2 = text.toString();
                int i3 = paint2.mBidiFlags;
                nDrawText(j, charSequence2, start, end, x, y, i3, paint.getNativeInstance());
            } else if (charSequence instanceof GraphicsOperations) {
                ((GraphicsOperations) charSequence).drawText(this, start, end, x, y, paint);
            } else {
                char[] buf = TemporaryBuffer.obtain(i2 - i);
                TextUtils.getChars(charSequence, i, i2, buf, 0);
                long j2 = this.mNativeCanvasWrapper;
                nDrawText(j2, buf, 0, i2 - i, x, y, paint2.mBidiFlags, paint.getNativeInstance());
                TemporaryBuffer.recycle(buf);
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void drawText(String text, float x, float y, Paint paint) {
        throwIfHasHwBitmapInSwMode(paint);
        nDrawText(this.mNativeCanvasWrapper, text, 0, text.length(), x, y, paint.mBidiFlags, paint.getNativeInstance());
    }

    public void drawText(String text, int start, int end, float x, float y, Paint paint) {
        Paint paint2 = paint;
        if ((start | end | (end - start) | (text.length() - end)) >= 0) {
            throwIfHasHwBitmapInSwMode(paint2);
            nDrawText(this.mNativeCanvasWrapper, text, start, end, x, y, paint2.mBidiFlags, paint.getNativeInstance());
            return;
        }
        throw new IndexOutOfBoundsException();
    }

    public void drawTextOnPath(char[] text, int index, int count, Path path, float hOffset, float vOffset, Paint paint) {
        Paint paint2 = paint;
        if (index < 0 || index + count > text.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        throwIfHasHwBitmapInSwMode(paint2);
        nDrawTextOnPath(this.mNativeCanvasWrapper, text, index, count, path.readOnlyNI(), hOffset, vOffset, paint2.mBidiFlags, paint.getNativeInstance());
    }

    public void drawTextOnPath(String text, Path path, float hOffset, float vOffset, Paint paint) {
        Paint paint2 = paint;
        if (text.length() > 0) {
            throwIfHasHwBitmapInSwMode(paint2);
            nDrawTextOnPath(this.mNativeCanvasWrapper, text, path.readOnlyNI(), hOffset, vOffset, paint2.mBidiFlags, paint.getNativeInstance());
        }
    }

    public void drawTextRun(char[] text, int index, int count, int contextIndex, int contextCount, float x, float y, boolean isRtl, Paint paint) {
        char[] cArr = text;
        Paint paint2 = paint;
        if (cArr == null) {
            throw new NullPointerException("text is null");
        } else if (paint2 == null) {
            throw new NullPointerException("paint is null");
        } else if ((index | count | contextIndex | contextCount | (index - contextIndex) | ((contextIndex + contextCount) - (index + count)) | (cArr.length - (contextIndex + contextCount))) >= 0) {
            throwIfHasHwBitmapInSwMode(paint2);
            nDrawTextRun(this.mNativeCanvasWrapper, text, index, count, contextIndex, contextCount, x, y, isRtl, paint.getNativeInstance(), 0);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void drawTextRun(CharSequence text, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, Paint paint) {
        CharSequence charSequence = text;
        int i = start;
        int i2 = end;
        int i3 = contextStart;
        int i4 = contextEnd;
        Paint paint2 = paint;
        if (charSequence == null) {
            throw new NullPointerException("text is null");
        } else if (paint2 == null) {
            throw new NullPointerException("paint is null");
        } else if ((i | i2 | i3 | i4 | (i - i3) | (i2 - i) | (i4 - i2) | (text.length() - i4)) >= 0) {
            throwIfHasHwBitmapInSwMode(paint2);
            if ((charSequence instanceof String) || (charSequence instanceof SpannedString) || (charSequence instanceof SpannableString)) {
                long j = this.mNativeCanvasWrapper;
                nDrawTextRun(j, text.toString(), start, end, contextStart, contextEnd, x, y, isRtl, paint.getNativeInstance());
            } else if (charSequence instanceof GraphicsOperations) {
                ((GraphicsOperations) charSequence).drawTextRun(this, start, end, contextStart, contextEnd, x, y, isRtl, paint);
            } else {
                if (charSequence instanceof PrecomputedText) {
                    PrecomputedText pt = (PrecomputedText) charSequence;
                    int paraIndex = pt.findParaIndex(i);
                    if (i2 <= pt.getParagraphEnd(paraIndex)) {
                        int paraStart = pt.getParagraphStart(paraIndex);
                        int i5 = paraIndex;
                        PrecomputedText precomputedText = pt;
                        drawTextRun(pt.getMeasuredParagraph(paraIndex).getMeasuredText(), i - paraStart, i2 - paraStart, i3 - paraStart, i4 - paraStart, x, y, isRtl, paint);
                        return;
                    }
                }
                int contextLen = i4 - i3;
                char[] buf = TemporaryBuffer.obtain(contextLen);
                TextUtils.getChars(charSequence, i3, i4, buf, 0);
                nDrawTextRun(this.mNativeCanvasWrapper, buf, i - i3, i2 - i, 0, contextLen, x, y, isRtl, paint.getNativeInstance(), 0);
                TemporaryBuffer.recycle(buf);
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void drawTextRun(MeasuredText measuredText, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, Paint paint) {
        nDrawTextRun(this.mNativeCanvasWrapper, measuredText.getChars(), start, end - start, contextStart, contextEnd - contextStart, x, y, isRtl, paint.getNativeInstance(), measuredText.getNativePtr());
    }

    public void drawVertices(Canvas.VertexMode mode, int vertexCount, float[] verts, int vertOffset, float[] texs, int texOffset, int[] colors, int colorOffset, short[] indices, int indexOffset, int indexCount, Paint paint) {
        int i = vertexCount;
        float[] fArr = texs;
        int[] iArr = colors;
        short[] sArr = indices;
        checkRange(verts.length, vertOffset, i);
        if (fArr != null) {
            checkRange(fArr.length, texOffset, i);
        } else {
            int i2 = texOffset;
        }
        if (iArr != null) {
            checkRange(iArr.length, colorOffset, i / 2);
        } else {
            int i3 = colorOffset;
        }
        if (sArr != null) {
            checkRange(sArr.length, indexOffset, indexCount);
        } else {
            int i4 = indexOffset;
            int i5 = indexCount;
        }
        throwIfHasHwBitmapInSwMode(paint);
        nDrawVertices(this.mNativeCanvasWrapper, mode.nativeInt, vertexCount, verts, vertOffset, texs, texOffset, colors, colorOffset, indices, indexOffset, indexCount, paint.getNativeInstance());
    }

    public void setHwBitmapsInSwModeEnabled(boolean enabled) {
        this.mAllowHwBitmapsInSwMode = enabled;
    }

    public boolean isHwBitmapsInSwModeEnabled() {
        return this.mAllowHwBitmapsInSwMode;
    }

    /* access modifiers changed from: protected */
    public void onHwBitmapInSwMode() {
        if (!this.mAllowHwBitmapsInSwMode) {
            throw new IllegalArgumentException("Software rendering doesn't support hardware bitmaps");
        }
    }

    private void throwIfHwBitmapInSwMode(Bitmap bitmap) {
        if (!isHardwareAccelerated() && bitmap.getConfig() == Bitmap.Config.HARDWARE) {
            onHwBitmapInSwMode();
        }
    }

    private void throwIfHasHwBitmapInSwMode(Paint p) {
        if (!isHardwareAccelerated() && p != null) {
            throwIfHasHwBitmapInSwMode(p.getShader());
        }
    }

    private void throwIfHasHwBitmapInSwMode(Shader shader) {
        if (shader != null) {
            if (shader instanceof BitmapShader) {
                throwIfHwBitmapInSwMode(((BitmapShader) shader).mBitmap);
            }
            if (shader instanceof ComposeShader) {
                throwIfHasHwBitmapInSwMode(((ComposeShader) shader).mShaderA);
                throwIfHasHwBitmapInSwMode(((ComposeShader) shader).mShaderB);
            }
        }
    }
}
