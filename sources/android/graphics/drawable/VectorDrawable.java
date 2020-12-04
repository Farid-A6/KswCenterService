package android.graphics.drawable;

import android.annotation.UnsupportedAppUsage;
import android.content.res.ColorStateList;
import android.content.res.ComplexColor;
import android.content.res.GradientColor;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Trace;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.IntProperty;
import android.util.PathParser;
import android.util.Property;
import com.android.internal.R;
import com.android.internal.util.VirtualRefBasePtr;
import dalvik.system.VMRuntime;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class VectorDrawable extends Drawable {
    private static final String LOGTAG = VectorDrawable.class.getSimpleName();
    private static final String SHAPE_CLIP_PATH = "clip-path";
    private static final String SHAPE_GROUP = "group";
    private static final String SHAPE_PATH = "path";
    private static final String SHAPE_VECTOR = "vector";
    private BlendModeColorFilter mBlendModeColorFilter;
    private ColorFilter mColorFilter;
    private boolean mDpiScaledDirty;
    private int mDpiScaledHeight;
    private Insets mDpiScaledInsets;
    private int mDpiScaledWidth;
    private boolean mMutated;
    private int mTargetDensity;
    @UnsupportedAppUsage
    private PorterDuffColorFilter mTintFilter;
    private final Rect mTmpBounds;
    private VectorDrawableState mVectorState;

    /* access modifiers changed from: private */
    public static native void nAddChild(long j, long j2);

    /* access modifiers changed from: private */
    public static native long nCreateClipPath();

    /* access modifiers changed from: private */
    public static native long nCreateClipPath(long j);

    /* access modifiers changed from: private */
    public static native long nCreateFullPath();

    /* access modifiers changed from: private */
    public static native long nCreateFullPath(long j);

    /* access modifiers changed from: private */
    public static native long nCreateGroup();

    /* access modifiers changed from: private */
    public static native long nCreateGroup(long j);

    /* access modifiers changed from: private */
    public static native long nCreateTree(long j);

    /* access modifiers changed from: private */
    public static native long nCreateTreeFromCopy(long j, long j2);

    private static native int nDraw(long j, long j2, long j3, Rect rect, boolean z, boolean z2);

    /* access modifiers changed from: private */
    public static native float nGetFillAlpha(long j);

    /* access modifiers changed from: private */
    public static native int nGetFillColor(long j);

    /* access modifiers changed from: private */
    public static native boolean nGetFullPathProperties(long j, byte[] bArr, int i);

    /* access modifiers changed from: private */
    public static native boolean nGetGroupProperties(long j, float[] fArr, int i);

    /* access modifiers changed from: private */
    public static native float nGetPivotX(long j);

    /* access modifiers changed from: private */
    public static native float nGetPivotY(long j);

    /* access modifiers changed from: private */
    public static native float nGetRootAlpha(long j);

    /* access modifiers changed from: private */
    public static native float nGetRotation(long j);

    /* access modifiers changed from: private */
    public static native float nGetScaleX(long j);

    /* access modifiers changed from: private */
    public static native float nGetScaleY(long j);

    /* access modifiers changed from: private */
    public static native float nGetStrokeAlpha(long j);

    /* access modifiers changed from: private */
    public static native int nGetStrokeColor(long j);

    /* access modifiers changed from: private */
    public static native float nGetStrokeWidth(long j);

    /* access modifiers changed from: private */
    public static native float nGetTranslateX(long j);

    /* access modifiers changed from: private */
    public static native float nGetTranslateY(long j);

    /* access modifiers changed from: private */
    public static native float nGetTrimPathEnd(long j);

    /* access modifiers changed from: private */
    public static native float nGetTrimPathOffset(long j);

    /* access modifiers changed from: private */
    public static native float nGetTrimPathStart(long j);

    private static native void nSetAllowCaching(long j, boolean z);

    private static native void nSetAntiAlias(long j, boolean z);

    /* access modifiers changed from: private */
    public static native void nSetFillAlpha(long j, float f);

    /* access modifiers changed from: private */
    public static native void nSetFillColor(long j, int i);

    /* access modifiers changed from: private */
    public static native void nSetName(long j, String str);

    /* access modifiers changed from: private */
    public static native void nSetPathData(long j, long j2);

    /* access modifiers changed from: private */
    public static native void nSetPathString(long j, String str, int i);

    /* access modifiers changed from: private */
    public static native void nSetPivotX(long j, float f);

    /* access modifiers changed from: private */
    public static native void nSetPivotY(long j, float f);

    /* access modifiers changed from: private */
    public static native void nSetRendererViewportSize(long j, float f, float f2);

    /* access modifiers changed from: private */
    public static native boolean nSetRootAlpha(long j, float f);

    /* access modifiers changed from: private */
    public static native void nSetRotation(long j, float f);

    /* access modifiers changed from: private */
    public static native void nSetScaleX(long j, float f);

    /* access modifiers changed from: private */
    public static native void nSetScaleY(long j, float f);

    /* access modifiers changed from: private */
    public static native void nSetStrokeAlpha(long j, float f);

    /* access modifiers changed from: private */
    public static native void nSetStrokeColor(long j, int i);

    /* access modifiers changed from: private */
    public static native void nSetStrokeWidth(long j, float f);

    /* access modifiers changed from: private */
    public static native void nSetTranslateX(long j, float f);

    /* access modifiers changed from: private */
    public static native void nSetTranslateY(long j, float f);

    /* access modifiers changed from: private */
    public static native void nSetTrimPathEnd(long j, float f);

    /* access modifiers changed from: private */
    public static native void nSetTrimPathOffset(long j, float f);

    /* access modifiers changed from: private */
    public static native void nSetTrimPathStart(long j, float f);

    /* access modifiers changed from: private */
    public static native void nUpdateFullPathFillGradient(long j, long j2);

    /* access modifiers changed from: private */
    public static native void nUpdateFullPathProperties(long j, float f, int i, float f2, int i2, float f3, float f4, float f5, float f6, float f7, int i3, int i4, int i5);

    /* access modifiers changed from: private */
    public static native void nUpdateFullPathStrokeGradient(long j, long j2);

    /* access modifiers changed from: private */
    public static native void nUpdateGroupProperties(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7);

    public VectorDrawable() {
        this(new VectorDrawableState((VectorDrawableState) null), (Resources) null);
    }

    private VectorDrawable(VectorDrawableState state, Resources res) {
        this.mDpiScaledWidth = 0;
        this.mDpiScaledHeight = 0;
        this.mDpiScaledInsets = Insets.NONE;
        this.mDpiScaledDirty = true;
        this.mTmpBounds = new Rect();
        this.mVectorState = state;
        updateLocalState(res);
    }

    private void updateLocalState(Resources res) {
        int density = Drawable.resolveDensity(res, this.mVectorState.mDensity);
        if (this.mTargetDensity != density) {
            this.mTargetDensity = density;
            this.mDpiScaledDirty = true;
        }
        updateColorFilters(this.mVectorState.mBlendMode, this.mVectorState.mTint);
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mVectorState = new VectorDrawableState(this.mVectorState);
            this.mMutated = true;
        }
        return this;
    }

    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    /* access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public Object getTargetByName(String name) {
        return this.mVectorState.mVGTargetsMap.get(name);
    }

    public Drawable.ConstantState getConstantState() {
        this.mVectorState.mChangingConfigurations = getChangingConfigurations();
        return this.mVectorState;
    }

    public void draw(Canvas canvas) {
        ColorFilter colorFilter;
        long nativeInstance;
        int deltaInBytes;
        copyBounds(this.mTmpBounds);
        if (this.mTmpBounds.width() > 0 && this.mTmpBounds.height() > 0) {
            if (this.mColorFilter == null) {
                colorFilter = this.mBlendModeColorFilter;
            } else {
                colorFilter = this.mColorFilter;
            }
            if (colorFilter == null) {
                nativeInstance = 0;
            } else {
                nativeInstance = colorFilter.getNativeInstance();
            }
            long colorFilterNativeInstance = nativeInstance;
            int pixelCount = nDraw(this.mVectorState.getNativeRenderer(), canvas.getNativeCanvasWrapper(), colorFilterNativeInstance, this.mTmpBounds, needMirroring(), this.mVectorState.canReuseCache());
            if (pixelCount != 0) {
                if (canvas.isHardwareAccelerated()) {
                    deltaInBytes = (pixelCount - this.mVectorState.mLastHWCachePixelCount) * 4;
                    this.mVectorState.mLastHWCachePixelCount = pixelCount;
                } else {
                    deltaInBytes = (pixelCount - this.mVectorState.mLastSWCachePixelCount) * 4;
                    this.mVectorState.mLastSWCachePixelCount = pixelCount;
                }
                if (deltaInBytes > 0) {
                    VMRuntime.getRuntime().registerNativeAllocation(deltaInBytes);
                } else if (deltaInBytes < 0) {
                    VMRuntime.getRuntime().registerNativeFree(-deltaInBytes);
                }
            }
        }
    }

    public int getAlpha() {
        return (int) (this.mVectorState.getAlpha() * 255.0f);
    }

    public void setAlpha(int alpha) {
        if (this.mVectorState.setAlpha(((float) alpha) / 255.0f)) {
            invalidateSelf();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
        invalidateSelf();
    }

    public ColorFilter getColorFilter() {
        return this.mColorFilter;
    }

    public void setTintList(ColorStateList tint) {
        VectorDrawableState state = this.mVectorState;
        if (state.mTint != tint) {
            state.mTint = tint;
            updateColorFilters(this.mVectorState.mBlendMode, tint);
            invalidateSelf();
        }
    }

    public void setTintBlendMode(BlendMode blendMode) {
        VectorDrawableState state = this.mVectorState;
        if (state.mBlendMode != blendMode) {
            state.mBlendMode = blendMode;
            updateColorFilters(state.mBlendMode, state.mTint);
            invalidateSelf();
        }
    }

    public boolean isStateful() {
        return super.isStateful() || (this.mVectorState != null && this.mVectorState.isStateful());
    }

    public boolean hasFocusStateSpecified() {
        return this.mVectorState != null && this.mVectorState.hasFocusStateSpecified();
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] stateSet) {
        boolean changed = false;
        if (isStateful()) {
            mutate();
        }
        VectorDrawableState state = this.mVectorState;
        if (state.onStateChange(stateSet)) {
            changed = true;
            state.mCacheDirty = true;
        }
        if (state.mTint == null || state.mBlendMode == null) {
            return changed;
        }
        updateColorFilters(state.mBlendMode, state.mTint);
        return true;
    }

    private void updateColorFilters(BlendMode blendMode, ColorStateList tint) {
        this.mTintFilter = updateTintFilter(this.mTintFilter, tint, BlendMode.blendModeToPorterDuffMode(blendMode));
        this.mBlendModeColorFilter = updateBlendModeFilter(this.mBlendModeColorFilter, tint, blendMode);
    }

    public int getOpacity() {
        return getAlpha() == 0 ? -2 : -3;
    }

    public int getIntrinsicWidth() {
        if (this.mDpiScaledDirty) {
            computeVectorSize();
        }
        return this.mDpiScaledWidth;
    }

    public int getIntrinsicHeight() {
        if (this.mDpiScaledDirty) {
            computeVectorSize();
        }
        return this.mDpiScaledHeight;
    }

    public Insets getOpticalInsets() {
        if (this.mDpiScaledDirty) {
            computeVectorSize();
        }
        return this.mDpiScaledInsets;
    }

    /* access modifiers changed from: package-private */
    public void computeVectorSize() {
        Insets opticalInsets = this.mVectorState.mOpticalInsets;
        int sourceDensity = this.mVectorState.mDensity;
        int targetDensity = this.mTargetDensity;
        if (targetDensity != sourceDensity) {
            this.mDpiScaledWidth = Drawable.scaleFromDensity(this.mVectorState.mBaseWidth, sourceDensity, targetDensity, true);
            this.mDpiScaledHeight = Drawable.scaleFromDensity(this.mVectorState.mBaseHeight, sourceDensity, targetDensity, true);
            this.mDpiScaledInsets = Insets.of(Drawable.scaleFromDensity(opticalInsets.left, sourceDensity, targetDensity, false), Drawable.scaleFromDensity(opticalInsets.top, sourceDensity, targetDensity, false), Drawable.scaleFromDensity(opticalInsets.right, sourceDensity, targetDensity, false), Drawable.scaleFromDensity(opticalInsets.bottom, sourceDensity, targetDensity, false));
        } else {
            this.mDpiScaledWidth = this.mVectorState.mBaseWidth;
            this.mDpiScaledHeight = this.mVectorState.mBaseHeight;
            this.mDpiScaledInsets = opticalInsets;
        }
        this.mDpiScaledDirty = false;
    }

    public boolean canApplyTheme() {
        return (this.mVectorState != null && this.mVectorState.canApplyTheme()) || super.canApplyTheme();
    }

    public void applyTheme(Resources.Theme t) {
        super.applyTheme(t);
        VectorDrawableState state = this.mVectorState;
        if (state != null) {
            this.mDpiScaledDirty |= this.mVectorState.setDensity(Drawable.resolveDensity(t.getResources(), 0));
            if (state.mThemeAttrs != null) {
                TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.VectorDrawable);
                try {
                    state.mCacheDirty = true;
                    updateStateFromTypedArray(a);
                    a.recycle();
                    this.mDpiScaledDirty = true;
                } catch (XmlPullParserException e) {
                    throw new RuntimeException(e);
                } catch (Throwable th) {
                    a.recycle();
                    throw th;
                }
            }
            if (state.mTint != null && state.mTint.canApplyTheme()) {
                state.mTint = state.mTint.obtainForTheme(t);
            }
            if (this.mVectorState != null && this.mVectorState.canApplyTheme()) {
                this.mVectorState.applyTheme(t);
            }
            updateLocalState(t.getResources());
        }
    }

    public float getPixelSize() {
        if (this.mVectorState == null || this.mVectorState.mBaseWidth == 0 || this.mVectorState.mBaseHeight == 0 || this.mVectorState.mViewportHeight == 0.0f || this.mVectorState.mViewportWidth == 0.0f) {
            return 1.0f;
        }
        return Math.min(this.mVectorState.mViewportWidth / ((float) this.mVectorState.mBaseWidth), this.mVectorState.mViewportHeight / ((float) this.mVectorState.mBaseHeight));
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x001f A[Catch:{ XmlPullParserException -> 0x0031, IOException -> 0x0027 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0016 A[Catch:{ XmlPullParserException -> 0x0031, IOException -> 0x0027 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.drawable.VectorDrawable create(android.content.res.Resources r5, int r6) {
        /*
            android.content.res.XmlResourceParser r0 = r5.getXml(r6)     // Catch:{ XmlPullParserException -> 0x0031, IOException -> 0x0027 }
            android.util.AttributeSet r1 = android.util.Xml.asAttributeSet(r0)     // Catch:{ XmlPullParserException -> 0x0031, IOException -> 0x0027 }
        L_0x0008:
            int r2 = r0.next()     // Catch:{ XmlPullParserException -> 0x0031, IOException -> 0x0027 }
            r3 = r2
            r4 = 2
            if (r2 == r4) goto L_0x0014
            r2 = 1
            if (r3 == r2) goto L_0x0014
            goto L_0x0008
        L_0x0014:
            if (r3 != r4) goto L_0x001f
            android.graphics.drawable.VectorDrawable r2 = new android.graphics.drawable.VectorDrawable     // Catch:{ XmlPullParserException -> 0x0031, IOException -> 0x0027 }
            r2.<init>()     // Catch:{ XmlPullParserException -> 0x0031, IOException -> 0x0027 }
            r2.inflate(r5, r0, r1)     // Catch:{ XmlPullParserException -> 0x0031, IOException -> 0x0027 }
            return r2
        L_0x001f:
            org.xmlpull.v1.XmlPullParserException r2 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ XmlPullParserException -> 0x0031, IOException -> 0x0027 }
            java.lang.String r4 = "No start tag found"
            r2.<init>(r4)     // Catch:{ XmlPullParserException -> 0x0031, IOException -> 0x0027 }
            throw r2     // Catch:{ XmlPullParserException -> 0x0031, IOException -> 0x0027 }
        L_0x0027:
            r0 = move-exception
            java.lang.String r1 = LOGTAG
            java.lang.String r2 = "parser error"
            android.util.Log.e(r1, r2, r0)
            goto L_0x003b
        L_0x0031:
            r0 = move-exception
            java.lang.String r1 = LOGTAG
            java.lang.String r2 = "parser error"
            android.util.Log.e(r1, r2, r0)
        L_0x003b:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.VectorDrawable.create(android.content.res.Resources, int):android.graphics.drawable.VectorDrawable");
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        try {
            Trace.traceBegin(8192, "VectorDrawable#inflate");
            if (!(this.mVectorState.mRootGroup == null && this.mVectorState.mNativeTree == null)) {
                if (this.mVectorState.mRootGroup != null) {
                    VMRuntime.getRuntime().registerNativeFree(this.mVectorState.mRootGroup.getNativeSize());
                    this.mVectorState.mRootGroup.setTree((VirtualRefBasePtr) null);
                }
                this.mVectorState.mRootGroup = new VGroup();
                if (this.mVectorState.mNativeTree != null) {
                    VMRuntime.getRuntime().registerNativeFree(316);
                    this.mVectorState.mNativeTree.release();
                }
                this.mVectorState.createNativeTree(this.mVectorState.mRootGroup);
            }
            VectorDrawableState state = this.mVectorState;
            state.setDensity(Drawable.resolveDensity(r, 0));
            TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.VectorDrawable);
            updateStateFromTypedArray(a);
            a.recycle();
            this.mDpiScaledDirty = true;
            state.mCacheDirty = true;
            inflateChildElements(r, parser, attrs, theme);
            state.onTreeConstructionFinished();
            updateLocalState(r);
        } finally {
            Trace.traceEnd(8192);
        }
    }

    private void updateStateFromTypedArray(TypedArray a) throws XmlPullParserException {
        VectorDrawableState state = this.mVectorState;
        state.mChangingConfigurations |= a.getChangingConfigurations();
        state.mThemeAttrs = a.extractThemeAttrs();
        int tintMode = a.getInt(6, -1);
        if (tintMode != -1) {
            state.mBlendMode = Drawable.parseBlendMode(tintMode, BlendMode.SRC_IN);
        }
        ColorStateList tint = a.getColorStateList(1);
        if (tint != null) {
            state.mTint = tint;
        }
        state.mAutoMirrored = a.getBoolean(5, state.mAutoMirrored);
        state.setViewportSize(a.getFloat(7, state.mViewportWidth), a.getFloat(8, state.mViewportHeight));
        if (state.mViewportWidth <= 0.0f) {
            throw new XmlPullParserException(a.getPositionDescription() + "<vector> tag requires viewportWidth > 0");
        } else if (state.mViewportHeight > 0.0f) {
            state.mBaseWidth = a.getDimensionPixelSize(3, state.mBaseWidth);
            state.mBaseHeight = a.getDimensionPixelSize(2, state.mBaseHeight);
            if (state.mBaseWidth <= 0) {
                throw new XmlPullParserException(a.getPositionDescription() + "<vector> tag requires width > 0");
            } else if (state.mBaseHeight > 0) {
                state.mOpticalInsets = Insets.of(a.getDimensionPixelOffset(9, state.mOpticalInsets.left), a.getDimensionPixelOffset(10, state.mOpticalInsets.top), a.getDimensionPixelOffset(11, state.mOpticalInsets.right), a.getDimensionPixelOffset(12, state.mOpticalInsets.bottom));
                state.setAlpha(a.getFloat(4, state.getAlpha()));
                String name = a.getString(0);
                if (name != null) {
                    state.mRootName = name;
                    state.mVGTargetsMap.put(name, state);
                }
            } else {
                throw new XmlPullParserException(a.getPositionDescription() + "<vector> tag requires height > 0");
            }
        } else {
            throw new XmlPullParserException(a.getPositionDescription() + "<vector> tag requires viewportHeight > 0");
        }
    }

    private void inflateChildElements(Resources res, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        VectorDrawableState state = this.mVectorState;
        boolean noPathTag = true;
        Stack<VGroup> groupStack = new Stack<>();
        groupStack.push(state.mRootGroup);
        int eventType = parser.getEventType();
        int innerDepth = parser.getDepth() + 1;
        while (eventType != 1 && (parser.getDepth() >= innerDepth || eventType != 3)) {
            if (eventType == 2) {
                String tagName = parser.getName();
                VGroup currentGroup = groupStack.peek();
                if (SHAPE_PATH.equals(tagName)) {
                    VFullPath path = new VFullPath();
                    path.inflate(res, attrs, theme);
                    currentGroup.addChild(path);
                    if (path.getPathName() != null) {
                        state.mVGTargetsMap.put(path.getPathName(), path);
                    }
                    noPathTag = false;
                    state.mChangingConfigurations |= path.mChangingConfigurations;
                } else if (SHAPE_CLIP_PATH.equals(tagName)) {
                    VClipPath path2 = new VClipPath();
                    path2.inflate(res, attrs, theme);
                    currentGroup.addChild(path2);
                    if (path2.getPathName() != null) {
                        state.mVGTargetsMap.put(path2.getPathName(), path2);
                    }
                    state.mChangingConfigurations |= path2.mChangingConfigurations;
                } else if ("group".equals(tagName)) {
                    VGroup newChildGroup = new VGroup();
                    newChildGroup.inflate(res, attrs, theme);
                    currentGroup.addChild(newChildGroup);
                    groupStack.push(newChildGroup);
                    if (newChildGroup.getGroupName() != null) {
                        state.mVGTargetsMap.put(newChildGroup.getGroupName(), newChildGroup);
                    }
                    state.mChangingConfigurations |= newChildGroup.mChangingConfigurations;
                }
            } else if (eventType == 3 && "group".equals(parser.getName())) {
                groupStack.pop();
            }
            eventType = parser.next();
        }
        if (noPathTag) {
            StringBuffer tag = new StringBuffer();
            if (tag.length() > 0) {
                tag.append(" or ");
            }
            tag.append(SHAPE_PATH);
            throw new XmlPullParserException("no " + tag + " defined");
        }
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mVectorState.getChangingConfigurations();
    }

    /* access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public void setAllowCaching(boolean allowCaching) {
        nSetAllowCaching(this.mVectorState.getNativeRenderer(), allowCaching);
    }

    private boolean needMirroring() {
        return isAutoMirrored() && getLayoutDirection() == 1;
    }

    public void setAutoMirrored(boolean mirrored) {
        if (this.mVectorState.mAutoMirrored != mirrored) {
            this.mVectorState.mAutoMirrored = mirrored;
            invalidateSelf();
        }
    }

    public boolean isAutoMirrored() {
        return this.mVectorState.mAutoMirrored;
    }

    public long getNativeTree() {
        return this.mVectorState.getNativeRenderer();
    }

    public void setAntiAlias(boolean aa) {
        nSetAntiAlias(this.mVectorState.mNativeTree.get(), aa);
    }

    static class VectorDrawableState extends Drawable.ConstantState {
        static final Property<VectorDrawableState, Float> ALPHA = new FloatProperty<VectorDrawableState>("alpha") {
            public void setValue(VectorDrawableState state, float value) {
                state.setAlpha(value);
            }

            public Float get(VectorDrawableState state) {
                return Float.valueOf(state.getAlpha());
            }
        };
        private static final int NATIVE_ALLOCATION_SIZE = 316;
        private int mAllocationOfAllNodes = 0;
        boolean mAutoMirrored;
        int mBaseHeight = 0;
        int mBaseWidth = 0;
        BlendMode mBlendMode = Drawable.DEFAULT_BLEND_MODE;
        boolean mCacheDirty;
        boolean mCachedAutoMirrored;
        BlendMode mCachedBlendMode;
        int[] mCachedThemeAttrs;
        ColorStateList mCachedTint;
        int mChangingConfigurations;
        int mDensity = 160;
        int mLastHWCachePixelCount = 0;
        int mLastSWCachePixelCount = 0;
        VirtualRefBasePtr mNativeTree = null;
        Insets mOpticalInsets = Insets.NONE;
        VGroup mRootGroup;
        String mRootName = null;
        int[] mThemeAttrs;
        ColorStateList mTint = null;
        final ArrayMap<String, Object> mVGTargetsMap = new ArrayMap<>();
        float mViewportHeight = 0.0f;
        float mViewportWidth = 0.0f;

        /* access modifiers changed from: package-private */
        public Property getProperty(String propertyName) {
            if (ALPHA.getName().equals(propertyName)) {
                return ALPHA;
            }
            return null;
        }

        public VectorDrawableState(VectorDrawableState copy) {
            if (copy != null) {
                this.mThemeAttrs = copy.mThemeAttrs;
                this.mChangingConfigurations = copy.mChangingConfigurations;
                this.mTint = copy.mTint;
                this.mBlendMode = copy.mBlendMode;
                this.mAutoMirrored = copy.mAutoMirrored;
                this.mRootGroup = new VGroup(copy.mRootGroup, this.mVGTargetsMap);
                createNativeTreeFromCopy(copy, this.mRootGroup);
                this.mBaseWidth = copy.mBaseWidth;
                this.mBaseHeight = copy.mBaseHeight;
                setViewportSize(copy.mViewportWidth, copy.mViewportHeight);
                this.mOpticalInsets = copy.mOpticalInsets;
                this.mRootName = copy.mRootName;
                this.mDensity = copy.mDensity;
                if (copy.mRootName != null) {
                    this.mVGTargetsMap.put(copy.mRootName, this);
                }
            } else {
                this.mRootGroup = new VGroup();
                createNativeTree(this.mRootGroup);
            }
            onTreeConstructionFinished();
        }

        /* access modifiers changed from: private */
        public void createNativeTree(VGroup rootGroup) {
            this.mNativeTree = new VirtualRefBasePtr(VectorDrawable.nCreateTree(rootGroup.mNativePtr));
            VMRuntime.getRuntime().registerNativeAllocation(316);
        }

        private void createNativeTreeFromCopy(VectorDrawableState copy, VGroup rootGroup) {
            this.mNativeTree = new VirtualRefBasePtr(VectorDrawable.nCreateTreeFromCopy(copy.mNativeTree.get(), rootGroup.mNativePtr));
            VMRuntime.getRuntime().registerNativeAllocation(316);
        }

        /* access modifiers changed from: package-private */
        public void onTreeConstructionFinished() {
            this.mRootGroup.setTree(this.mNativeTree);
            this.mAllocationOfAllNodes = this.mRootGroup.getNativeSize();
            VMRuntime.getRuntime().registerNativeAllocation(this.mAllocationOfAllNodes);
        }

        /* access modifiers changed from: package-private */
        public long getNativeRenderer() {
            if (this.mNativeTree == null) {
                return 0;
            }
            return this.mNativeTree.get();
        }

        public boolean canReuseCache() {
            if (!this.mCacheDirty && this.mCachedThemeAttrs == this.mThemeAttrs && this.mCachedTint == this.mTint && this.mCachedBlendMode == this.mBlendMode && this.mCachedAutoMirrored == this.mAutoMirrored) {
                return true;
            }
            updateCacheStates();
            return false;
        }

        public void updateCacheStates() {
            this.mCachedThemeAttrs = this.mThemeAttrs;
            this.mCachedTint = this.mTint;
            this.mCachedBlendMode = this.mBlendMode;
            this.mCachedAutoMirrored = this.mAutoMirrored;
            this.mCacheDirty = false;
        }

        public void applyTheme(Resources.Theme t) {
            this.mRootGroup.applyTheme(t);
        }

        public boolean canApplyTheme() {
            return this.mThemeAttrs != null || (this.mRootGroup != null && this.mRootGroup.canApplyTheme()) || ((this.mTint != null && this.mTint.canApplyTheme()) || super.canApplyTheme());
        }

        public Drawable newDrawable() {
            return new VectorDrawable(this, (Resources) null);
        }

        public Drawable newDrawable(Resources res) {
            return new VectorDrawable(this, res);
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations | (this.mTint != null ? this.mTint.getChangingConfigurations() : 0);
        }

        public boolean isStateful() {
            return (this.mTint != null && this.mTint.isStateful()) || (this.mRootGroup != null && this.mRootGroup.isStateful());
        }

        public boolean hasFocusStateSpecified() {
            return (this.mTint != null && this.mTint.hasFocusStateSpecified()) || (this.mRootGroup != null && this.mRootGroup.hasFocusStateSpecified());
        }

        /* access modifiers changed from: package-private */
        public void setViewportSize(float viewportWidth, float viewportHeight) {
            this.mViewportWidth = viewportWidth;
            this.mViewportHeight = viewportHeight;
            VectorDrawable.nSetRendererViewportSize(getNativeRenderer(), viewportWidth, viewportHeight);
        }

        public final boolean setDensity(int targetDensity) {
            if (this.mDensity == targetDensity) {
                return false;
            }
            int sourceDensity = this.mDensity;
            this.mDensity = targetDensity;
            applyDensityScaling(sourceDensity, targetDensity);
            return true;
        }

        private void applyDensityScaling(int sourceDensity, int targetDensity) {
            this.mBaseWidth = Drawable.scaleFromDensity(this.mBaseWidth, sourceDensity, targetDensity, true);
            this.mBaseHeight = Drawable.scaleFromDensity(this.mBaseHeight, sourceDensity, targetDensity, true);
            this.mOpticalInsets = Insets.of(Drawable.scaleFromDensity(this.mOpticalInsets.left, sourceDensity, targetDensity, false), Drawable.scaleFromDensity(this.mOpticalInsets.top, sourceDensity, targetDensity, false), Drawable.scaleFromDensity(this.mOpticalInsets.right, sourceDensity, targetDensity, false), Drawable.scaleFromDensity(this.mOpticalInsets.bottom, sourceDensity, targetDensity, false));
        }

        public boolean onStateChange(int[] stateSet) {
            return this.mRootGroup.onStateChange(stateSet);
        }

        public void finalize() throws Throwable {
            super.finalize();
            VMRuntime.getRuntime().registerNativeFree(this.mAllocationOfAllNodes + 316 + (this.mLastHWCachePixelCount * 4) + (this.mLastSWCachePixelCount * 4));
        }

        public boolean setAlpha(float alpha) {
            return VectorDrawable.nSetRootAlpha(this.mNativeTree.get(), alpha);
        }

        public float getAlpha() {
            return VectorDrawable.nGetRootAlpha(this.mNativeTree.get());
        }
    }

    static class VGroup extends VObject {
        private static final int NATIVE_ALLOCATION_SIZE = 100;
        /* access modifiers changed from: private */
        public static final Property<VGroup, Float> PIVOT_X = new FloatProperty<VGroup>("pivotX") {
            public void setValue(VGroup object, float value) {
                object.setPivotX(value);
            }

            public Float get(VGroup object) {
                return Float.valueOf(object.getPivotX());
            }
        };
        private static final int PIVOT_X_INDEX = 1;
        /* access modifiers changed from: private */
        public static final Property<VGroup, Float> PIVOT_Y = new FloatProperty<VGroup>("pivotY") {
            public void setValue(VGroup object, float value) {
                object.setPivotY(value);
            }

            public Float get(VGroup object) {
                return Float.valueOf(object.getPivotY());
            }
        };
        private static final int PIVOT_Y_INDEX = 2;
        /* access modifiers changed from: private */
        public static final Property<VGroup, Float> ROTATION = new FloatProperty<VGroup>("rotation") {
            public void setValue(VGroup object, float value) {
                object.setRotation(value);
            }

            public Float get(VGroup object) {
                return Float.valueOf(object.getRotation());
            }
        };
        private static final int ROTATION_INDEX = 0;
        /* access modifiers changed from: private */
        public static final Property<VGroup, Float> SCALE_X = new FloatProperty<VGroup>("scaleX") {
            public void setValue(VGroup object, float value) {
                object.setScaleX(value);
            }

            public Float get(VGroup object) {
                return Float.valueOf(object.getScaleX());
            }
        };
        private static final int SCALE_X_INDEX = 3;
        /* access modifiers changed from: private */
        public static final Property<VGroup, Float> SCALE_Y = new FloatProperty<VGroup>("scaleY") {
            public void setValue(VGroup object, float value) {
                object.setScaleY(value);
            }

            public Float get(VGroup object) {
                return Float.valueOf(object.getScaleY());
            }
        };
        private static final int SCALE_Y_INDEX = 4;
        private static final int TRANSFORM_PROPERTY_COUNT = 7;
        /* access modifiers changed from: private */
        public static final Property<VGroup, Float> TRANSLATE_X = new FloatProperty<VGroup>("translateX") {
            public void setValue(VGroup object, float value) {
                object.setTranslateX(value);
            }

            public Float get(VGroup object) {
                return Float.valueOf(object.getTranslateX());
            }
        };
        private static final int TRANSLATE_X_INDEX = 5;
        /* access modifiers changed from: private */
        public static final Property<VGroup, Float> TRANSLATE_Y = new FloatProperty<VGroup>("translateY") {
            public void setValue(VGroup object, float value) {
                object.setTranslateY(value);
            }

            public Float get(VGroup object) {
                return Float.valueOf(object.getTranslateY());
            }
        };
        private static final int TRANSLATE_Y_INDEX = 6;
        private static final HashMap<String, Integer> sPropertyIndexMap = new HashMap<String, Integer>() {
            {
                put("translateX", 5);
                put("translateY", 6);
                put("scaleX", 3);
                put("scaleY", 4);
                put("pivotX", 1);
                put("pivotY", 2);
                put("rotation", 0);
            }
        };
        private static final HashMap<String, Property> sPropertyMap = new HashMap<String, Property>() {
            {
                put("translateX", VGroup.TRANSLATE_X);
                put("translateY", VGroup.TRANSLATE_Y);
                put("scaleX", VGroup.SCALE_X);
                put("scaleY", VGroup.SCALE_Y);
                put("pivotX", VGroup.PIVOT_X);
                put("pivotY", VGroup.PIVOT_Y);
                put("rotation", VGroup.ROTATION);
            }
        };
        /* access modifiers changed from: private */
        public int mChangingConfigurations;
        private final ArrayList<VObject> mChildren;
        private String mGroupName;
        private boolean mIsStateful;
        /* access modifiers changed from: private */
        public final long mNativePtr;
        private int[] mThemeAttrs;
        private float[] mTransform;

        static int getPropertyIndex(String propertyName) {
            if (sPropertyIndexMap.containsKey(propertyName)) {
                return sPropertyIndexMap.get(propertyName).intValue();
            }
            return -1;
        }

        public VGroup(VGroup copy, ArrayMap<String, Object> targetsMap) {
            VPath newPath;
            this.mChildren = new ArrayList<>();
            this.mGroupName = null;
            this.mIsStateful = copy.mIsStateful;
            this.mThemeAttrs = copy.mThemeAttrs;
            this.mGroupName = copy.mGroupName;
            this.mChangingConfigurations = copy.mChangingConfigurations;
            if (this.mGroupName != null) {
                targetsMap.put(this.mGroupName, this);
            }
            this.mNativePtr = VectorDrawable.nCreateGroup(copy.mNativePtr);
            ArrayList<VObject> children = copy.mChildren;
            for (int i = 0; i < children.size(); i++) {
                VObject copyChild = children.get(i);
                if (copyChild instanceof VGroup) {
                    addChild(new VGroup((VGroup) copyChild, targetsMap));
                } else {
                    if (copyChild instanceof VFullPath) {
                        newPath = new VFullPath((VFullPath) copyChild);
                    } else if (copyChild instanceof VClipPath) {
                        newPath = new VClipPath((VClipPath) copyChild);
                    } else {
                        throw new IllegalStateException("Unknown object in the tree!");
                    }
                    addChild(newPath);
                    if (newPath.mPathName != null) {
                        targetsMap.put(newPath.mPathName, newPath);
                    }
                }
            }
        }

        public VGroup() {
            this.mChildren = new ArrayList<>();
            this.mGroupName = null;
            this.mNativePtr = VectorDrawable.nCreateGroup();
        }

        /* access modifiers changed from: package-private */
        public Property getProperty(String propertyName) {
            if (sPropertyMap.containsKey(propertyName)) {
                return sPropertyMap.get(propertyName);
            }
            return null;
        }

        public String getGroupName() {
            return this.mGroupName;
        }

        public void addChild(VObject child) {
            VectorDrawable.nAddChild(this.mNativePtr, child.getNativePtr());
            this.mChildren.add(child);
            this.mIsStateful |= child.isStateful();
        }

        public void setTree(VirtualRefBasePtr treeRoot) {
            super.setTree(treeRoot);
            for (int i = 0; i < this.mChildren.size(); i++) {
                this.mChildren.get(i).setTree(treeRoot);
            }
        }

        public long getNativePtr() {
            return this.mNativePtr;
        }

        public void inflate(Resources res, AttributeSet attrs, Resources.Theme theme) {
            TypedArray a = Drawable.obtainAttributes(res, theme, attrs, R.styleable.VectorDrawableGroup);
            updateStateFromTypedArray(a);
            a.recycle();
        }

        /* access modifiers changed from: package-private */
        public void updateStateFromTypedArray(TypedArray a) {
            TypedArray typedArray = a;
            this.mChangingConfigurations |= a.getChangingConfigurations();
            this.mThemeAttrs = a.extractThemeAttrs();
            if (this.mTransform == null) {
                this.mTransform = new float[7];
            }
            if (VectorDrawable.nGetGroupProperties(this.mNativePtr, this.mTransform, 7)) {
                float rotate = typedArray.getFloat(5, this.mTransform[0]);
                float pivotX = typedArray.getFloat(1, this.mTransform[1]);
                float pivotY = typedArray.getFloat(2, this.mTransform[2]);
                float scaleX = typedArray.getFloat(3, this.mTransform[3]);
                float scaleY = typedArray.getFloat(4, this.mTransform[4]);
                float translateX = typedArray.getFloat(6, this.mTransform[5]);
                float translateY = typedArray.getFloat(7, this.mTransform[6]);
                String groupName = typedArray.getString(0);
                if (groupName != null) {
                    this.mGroupName = groupName;
                    VectorDrawable.nSetName(this.mNativePtr, this.mGroupName);
                }
                VectorDrawable.nUpdateGroupProperties(this.mNativePtr, rotate, pivotX, pivotY, scaleX, scaleY, translateX, translateY);
                return;
            }
            throw new RuntimeException("Error: inconsistent property count");
        }

        public boolean onStateChange(int[] stateSet) {
            boolean changed = false;
            ArrayList<VObject> children = this.mChildren;
            int count = children.size();
            for (int i = 0; i < count; i++) {
                VObject child = children.get(i);
                if (child.isStateful()) {
                    changed |= child.onStateChange(stateSet);
                }
            }
            return changed;
        }

        public boolean isStateful() {
            return this.mIsStateful;
        }

        public boolean hasFocusStateSpecified() {
            boolean result = false;
            ArrayList<VObject> children = this.mChildren;
            int count = children.size();
            for (int i = 0; i < count; i++) {
                VObject child = children.get(i);
                if (child.isStateful()) {
                    result |= child.hasFocusStateSpecified();
                }
            }
            return result;
        }

        /* access modifiers changed from: package-private */
        public int getNativeSize() {
            int size = 100;
            for (int i = 0; i < this.mChildren.size(); i++) {
                size += this.mChildren.get(i).getNativeSize();
            }
            return size;
        }

        public boolean canApplyTheme() {
            if (this.mThemeAttrs != null) {
                return true;
            }
            ArrayList<VObject> children = this.mChildren;
            int count = children.size();
            for (int i = 0; i < count; i++) {
                if (children.get(i).canApplyTheme()) {
                    return true;
                }
            }
            return false;
        }

        public void applyTheme(Resources.Theme t) {
            if (this.mThemeAttrs != null) {
                TypedArray a = t.resolveAttributes(this.mThemeAttrs, R.styleable.VectorDrawableGroup);
                updateStateFromTypedArray(a);
                a.recycle();
            }
            ArrayList<VObject> children = this.mChildren;
            int count = children.size();
            for (int i = 0; i < count; i++) {
                VObject child = children.get(i);
                if (child.canApplyTheme()) {
                    child.applyTheme(t);
                    this.mIsStateful |= child.isStateful();
                }
            }
        }

        public float getRotation() {
            if (isTreeValid()) {
                return VectorDrawable.nGetRotation(this.mNativePtr);
            }
            return 0.0f;
        }

        @UnsupportedAppUsage
        public void setRotation(float rotation) {
            if (isTreeValid()) {
                VectorDrawable.nSetRotation(this.mNativePtr, rotation);
            }
        }

        public float getPivotX() {
            if (isTreeValid()) {
                return VectorDrawable.nGetPivotX(this.mNativePtr);
            }
            return 0.0f;
        }

        @UnsupportedAppUsage
        public void setPivotX(float pivotX) {
            if (isTreeValid()) {
                VectorDrawable.nSetPivotX(this.mNativePtr, pivotX);
            }
        }

        public float getPivotY() {
            if (isTreeValid()) {
                return VectorDrawable.nGetPivotY(this.mNativePtr);
            }
            return 0.0f;
        }

        @UnsupportedAppUsage
        public void setPivotY(float pivotY) {
            if (isTreeValid()) {
                VectorDrawable.nSetPivotY(this.mNativePtr, pivotY);
            }
        }

        public float getScaleX() {
            if (isTreeValid()) {
                return VectorDrawable.nGetScaleX(this.mNativePtr);
            }
            return 0.0f;
        }

        public void setScaleX(float scaleX) {
            if (isTreeValid()) {
                VectorDrawable.nSetScaleX(this.mNativePtr, scaleX);
            }
        }

        public float getScaleY() {
            if (isTreeValid()) {
                return VectorDrawable.nGetScaleY(this.mNativePtr);
            }
            return 0.0f;
        }

        public void setScaleY(float scaleY) {
            if (isTreeValid()) {
                VectorDrawable.nSetScaleY(this.mNativePtr, scaleY);
            }
        }

        public float getTranslateX() {
            if (isTreeValid()) {
                return VectorDrawable.nGetTranslateX(this.mNativePtr);
            }
            return 0.0f;
        }

        @UnsupportedAppUsage
        public void setTranslateX(float translateX) {
            if (isTreeValid()) {
                VectorDrawable.nSetTranslateX(this.mNativePtr, translateX);
            }
        }

        public float getTranslateY() {
            if (isTreeValid()) {
                return VectorDrawable.nGetTranslateY(this.mNativePtr);
            }
            return 0.0f;
        }

        @UnsupportedAppUsage
        public void setTranslateY(float translateY) {
            if (isTreeValid()) {
                VectorDrawable.nSetTranslateY(this.mNativePtr, translateY);
            }
        }
    }

    static abstract class VPath extends VObject {
        private static final Property<VPath, PathParser.PathData> PATH_DATA = new Property<VPath, PathParser.PathData>(PathParser.PathData.class, "pathData") {
            public void set(VPath object, PathParser.PathData data) {
                object.setPathData(data);
            }

            public PathParser.PathData get(VPath object) {
                return object.getPathData();
            }
        };
        int mChangingConfigurations;
        protected PathParser.PathData mPathData = null;
        String mPathName;

        /* access modifiers changed from: package-private */
        public Property getProperty(String propertyName) {
            if (PATH_DATA.getName().equals(propertyName)) {
                return PATH_DATA;
            }
            return null;
        }

        public VPath() {
        }

        public VPath(VPath copy) {
            PathParser.PathData pathData = null;
            this.mPathName = copy.mPathName;
            this.mChangingConfigurations = copy.mChangingConfigurations;
            this.mPathData = copy.mPathData != null ? new PathParser.PathData(copy.mPathData) : pathData;
        }

        public String getPathName() {
            return this.mPathName;
        }

        public PathParser.PathData getPathData() {
            return this.mPathData;
        }

        public void setPathData(PathParser.PathData pathData) {
            this.mPathData.setPathData(pathData);
            if (isTreeValid()) {
                VectorDrawable.nSetPathData(getNativePtr(), this.mPathData.getNativePtr());
            }
        }
    }

    private static class VClipPath extends VPath {
        private static final int NATIVE_ALLOCATION_SIZE = 120;
        private final long mNativePtr;

        public VClipPath() {
            this.mNativePtr = VectorDrawable.nCreateClipPath();
        }

        public VClipPath(VClipPath copy) {
            super(copy);
            this.mNativePtr = VectorDrawable.nCreateClipPath(copy.mNativePtr);
        }

        public long getNativePtr() {
            return this.mNativePtr;
        }

        public void inflate(Resources r, AttributeSet attrs, Resources.Theme theme) {
            TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.VectorDrawableClipPath);
            updateStateFromTypedArray(a);
            a.recycle();
        }

        public boolean canApplyTheme() {
            return false;
        }

        public void applyTheme(Resources.Theme theme) {
        }

        public boolean onStateChange(int[] stateSet) {
            return false;
        }

        public boolean isStateful() {
            return false;
        }

        public boolean hasFocusStateSpecified() {
            return false;
        }

        /* access modifiers changed from: package-private */
        public int getNativeSize() {
            return 120;
        }

        private void updateStateFromTypedArray(TypedArray a) {
            this.mChangingConfigurations |= a.getChangingConfigurations();
            String pathName = a.getString(0);
            if (pathName != null) {
                this.mPathName = pathName;
                VectorDrawable.nSetName(this.mNativePtr, this.mPathName);
            }
            String pathDataString = a.getString(1);
            if (pathDataString != null) {
                this.mPathData = new PathParser.PathData(pathDataString);
                VectorDrawable.nSetPathString(this.mNativePtr, pathDataString, pathDataString.length());
            }
        }
    }

    static class VFullPath extends VPath {
        /* access modifiers changed from: private */
        public static final Property<VFullPath, Float> FILL_ALPHA = new FloatProperty<VFullPath>("fillAlpha") {
            public void setValue(VFullPath object, float value) {
                object.setFillAlpha(value);
            }

            public Float get(VFullPath object) {
                return Float.valueOf(object.getFillAlpha());
            }
        };
        private static final int FILL_ALPHA_INDEX = 4;
        /* access modifiers changed from: private */
        public static final Property<VFullPath, Integer> FILL_COLOR = new IntProperty<VFullPath>("fillColor") {
            public void setValue(VFullPath object, int value) {
                object.setFillColor(value);
            }

            public Integer get(VFullPath object) {
                return Integer.valueOf(object.getFillColor());
            }
        };
        private static final int FILL_COLOR_INDEX = 3;
        private static final int FILL_TYPE_INDEX = 11;
        private static final int NATIVE_ALLOCATION_SIZE = 264;
        /* access modifiers changed from: private */
        public static final Property<VFullPath, Float> STROKE_ALPHA = new FloatProperty<VFullPath>("strokeAlpha") {
            public void setValue(VFullPath object, float value) {
                object.setStrokeAlpha(value);
            }

            public Float get(VFullPath object) {
                return Float.valueOf(object.getStrokeAlpha());
            }
        };
        private static final int STROKE_ALPHA_INDEX = 2;
        /* access modifiers changed from: private */
        public static final Property<VFullPath, Integer> STROKE_COLOR = new IntProperty<VFullPath>("strokeColor") {
            public void setValue(VFullPath object, int value) {
                object.setStrokeColor(value);
            }

            public Integer get(VFullPath object) {
                return Integer.valueOf(object.getStrokeColor());
            }
        };
        private static final int STROKE_COLOR_INDEX = 1;
        private static final int STROKE_LINE_CAP_INDEX = 8;
        private static final int STROKE_LINE_JOIN_INDEX = 9;
        private static final int STROKE_MITER_LIMIT_INDEX = 10;
        /* access modifiers changed from: private */
        public static final Property<VFullPath, Float> STROKE_WIDTH = new FloatProperty<VFullPath>("strokeWidth") {
            public void setValue(VFullPath object, float value) {
                object.setStrokeWidth(value);
            }

            public Float get(VFullPath object) {
                return Float.valueOf(object.getStrokeWidth());
            }
        };
        private static final int STROKE_WIDTH_INDEX = 0;
        private static final int TOTAL_PROPERTY_COUNT = 12;
        /* access modifiers changed from: private */
        public static final Property<VFullPath, Float> TRIM_PATH_END = new FloatProperty<VFullPath>("trimPathEnd") {
            public void setValue(VFullPath object, float value) {
                object.setTrimPathEnd(value);
            }

            public Float get(VFullPath object) {
                return Float.valueOf(object.getTrimPathEnd());
            }
        };
        private static final int TRIM_PATH_END_INDEX = 6;
        /* access modifiers changed from: private */
        public static final Property<VFullPath, Float> TRIM_PATH_OFFSET = new FloatProperty<VFullPath>("trimPathOffset") {
            public void setValue(VFullPath object, float value) {
                object.setTrimPathOffset(value);
            }

            public Float get(VFullPath object) {
                return Float.valueOf(object.getTrimPathOffset());
            }
        };
        private static final int TRIM_PATH_OFFSET_INDEX = 7;
        /* access modifiers changed from: private */
        public static final Property<VFullPath, Float> TRIM_PATH_START = new FloatProperty<VFullPath>("trimPathStart") {
            public void setValue(VFullPath object, float value) {
                object.setTrimPathStart(value);
            }

            public Float get(VFullPath object) {
                return Float.valueOf(object.getTrimPathStart());
            }
        };
        private static final int TRIM_PATH_START_INDEX = 5;
        private static final HashMap<String, Integer> sPropertyIndexMap = new HashMap<String, Integer>() {
            {
                put("strokeWidth", 0);
                put("strokeColor", 1);
                put("strokeAlpha", 2);
                put("fillColor", 3);
                put("fillAlpha", 4);
                put("trimPathStart", 5);
                put("trimPathEnd", 6);
                put("trimPathOffset", 7);
            }
        };
        private static final HashMap<String, Property> sPropertyMap = new HashMap<String, Property>() {
            {
                put("strokeWidth", VFullPath.STROKE_WIDTH);
                put("strokeColor", VFullPath.STROKE_COLOR);
                put("strokeAlpha", VFullPath.STROKE_ALPHA);
                put("fillColor", VFullPath.FILL_COLOR);
                put("fillAlpha", VFullPath.FILL_ALPHA);
                put("trimPathStart", VFullPath.TRIM_PATH_START);
                put("trimPathEnd", VFullPath.TRIM_PATH_END);
                put("trimPathOffset", VFullPath.TRIM_PATH_OFFSET);
            }
        };
        ComplexColor mFillColors;
        private final long mNativePtr;
        private byte[] mPropertyData;
        ComplexColor mStrokeColors;
        private int[] mThemeAttrs;

        public VFullPath() {
            this.mStrokeColors = null;
            this.mFillColors = null;
            this.mNativePtr = VectorDrawable.nCreateFullPath();
        }

        public VFullPath(VFullPath copy) {
            super(copy);
            this.mStrokeColors = null;
            this.mFillColors = null;
            this.mNativePtr = VectorDrawable.nCreateFullPath(copy.mNativePtr);
            this.mThemeAttrs = copy.mThemeAttrs;
            this.mStrokeColors = copy.mStrokeColors;
            this.mFillColors = copy.mFillColors;
        }

        /* access modifiers changed from: package-private */
        public Property getProperty(String propertyName) {
            Property p = super.getProperty(propertyName);
            if (p != null) {
                return p;
            }
            if (sPropertyMap.containsKey(propertyName)) {
                return sPropertyMap.get(propertyName);
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public int getPropertyIndex(String propertyName) {
            if (!sPropertyIndexMap.containsKey(propertyName)) {
                return -1;
            }
            return sPropertyIndexMap.get(propertyName).intValue();
        }

        public boolean onStateChange(int[] stateSet) {
            boolean changed = false;
            boolean z = false;
            if (this.mStrokeColors != null && (this.mStrokeColors instanceof ColorStateList)) {
                int oldStrokeColor = getStrokeColor();
                int newStrokeColor = ((ColorStateList) this.mStrokeColors).getColorForState(stateSet, oldStrokeColor);
                changed = false | (oldStrokeColor != newStrokeColor);
                if (oldStrokeColor != newStrokeColor) {
                    VectorDrawable.nSetStrokeColor(this.mNativePtr, newStrokeColor);
                }
            }
            if (this.mFillColors != null && (this.mFillColors instanceof ColorStateList)) {
                int oldFillColor = getFillColor();
                int newFillColor = ((ColorStateList) this.mFillColors).getColorForState(stateSet, oldFillColor);
                if (oldFillColor != newFillColor) {
                    z = true;
                }
                changed |= z;
                if (oldFillColor != newFillColor) {
                    VectorDrawable.nSetFillColor(this.mNativePtr, newFillColor);
                }
            }
            return changed;
        }

        public boolean isStateful() {
            return (this.mStrokeColors == null && this.mFillColors == null) ? false : true;
        }

        public boolean hasFocusStateSpecified() {
            return this.mStrokeColors != null && (this.mStrokeColors instanceof ColorStateList) && ((ColorStateList) this.mStrokeColors).hasFocusStateSpecified() && this.mFillColors != null && (this.mFillColors instanceof ColorStateList) && ((ColorStateList) this.mFillColors).hasFocusStateSpecified();
        }

        /* access modifiers changed from: package-private */
        public int getNativeSize() {
            return 264;
        }

        public long getNativePtr() {
            return this.mNativePtr;
        }

        public void inflate(Resources r, AttributeSet attrs, Resources.Theme theme) {
            TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.VectorDrawablePath);
            updateStateFromTypedArray(a);
            a.recycle();
        }

        private void updateStateFromTypedArray(TypedArray a) {
            int strokeColor;
            float trimPathOffset;
            int strokeColor2;
            int strokeColor3;
            long j;
            TypedArray typedArray = a;
            if (this.mPropertyData == null) {
                this.mPropertyData = new byte[48];
            }
            boolean success = VectorDrawable.nGetFullPathProperties(this.mNativePtr, this.mPropertyData, 48);
            if (success) {
                ByteBuffer properties = ByteBuffer.wrap(this.mPropertyData);
                properties.order(ByteOrder.nativeOrder());
                float strokeWidth = properties.getFloat(0);
                int strokeColor4 = properties.getInt(4);
                float strokeAlpha = properties.getFloat(8);
                int fillColor = properties.getInt(12);
                float fillAlpha = properties.getFloat(16);
                float trimPathStart = properties.getFloat(20);
                float trimPathEnd = properties.getFloat(24);
                float trimPathOffset2 = properties.getFloat(28);
                int strokeLineCap = properties.getInt(32);
                int strokeLineJoin = properties.getInt(36);
                float strokeMiterLimit = properties.getFloat(40);
                int fillType = properties.getInt(44);
                Shader fillGradient = null;
                Shader strokeGradient = null;
                boolean z = success;
                this.mChangingConfigurations |= a.getChangingConfigurations();
                this.mThemeAttrs = a.extractThemeAttrs();
                String pathName = typedArray.getString(0);
                if (pathName != null) {
                    this.mPathName = pathName;
                    String str = pathName;
                    ByteBuffer byteBuffer = properties;
                    strokeColor = strokeColor4;
                    VectorDrawable.nSetName(this.mNativePtr, this.mPathName);
                } else {
                    ByteBuffer byteBuffer2 = properties;
                    strokeColor = strokeColor4;
                }
                String pathString = typedArray.getString(2);
                if (pathString != null) {
                    this.mPathData = new PathParser.PathData(pathString);
                    trimPathOffset = trimPathOffset2;
                    VectorDrawable.nSetPathString(this.mNativePtr, pathString, pathString.length());
                } else {
                    trimPathOffset = trimPathOffset2;
                }
                ComplexColor fillColors = typedArray.getComplexColor(1);
                if (fillColors != null) {
                    if (fillColors instanceof GradientColor) {
                        this.mFillColors = fillColors;
                        fillGradient = ((GradientColor) fillColors).getShader();
                    } else if (fillColors.isStateful() || fillColors.canApplyTheme()) {
                        this.mFillColors = fillColors;
                    } else {
                        this.mFillColors = null;
                    }
                    fillColor = fillColors.getDefaultColor();
                }
                ComplexColor strokeColors = typedArray.getComplexColor(3);
                if (strokeColors != null) {
                    if (strokeColors instanceof GradientColor) {
                        this.mStrokeColors = strokeColors;
                        strokeGradient = ((GradientColor) strokeColors).getShader();
                    } else if (strokeColors.isStateful() || strokeColors.canApplyTheme()) {
                        this.mStrokeColors = strokeColors;
                    } else {
                        this.mStrokeColors = null;
                    }
                    strokeColor2 = strokeColors.getDefaultColor();
                } else {
                    strokeColor2 = strokeColor;
                }
                String str2 = pathString;
                ComplexColor complexColor = fillColors;
                long j2 = this.mNativePtr;
                long j3 = 0;
                if (fillGradient != null) {
                    strokeColor3 = strokeColor2;
                    ComplexColor complexColor2 = strokeColors;
                    j = fillGradient.getNativeInstance();
                } else {
                    strokeColor3 = strokeColor2;
                    ComplexColor complexColor3 = strokeColors;
                    j = 0;
                }
                VectorDrawable.nUpdateFullPathFillGradient(j2, j);
                long j4 = this.mNativePtr;
                if (strokeGradient != null) {
                    j3 = strokeGradient.getNativeInstance();
                }
                VectorDrawable.nUpdateFullPathStrokeGradient(j4, j3);
                float fillAlpha2 = typedArray.getFloat(12, fillAlpha);
                int strokeLineCap2 = typedArray.getInt(8, strokeLineCap);
                int strokeLineJoin2 = typedArray.getInt(9, strokeLineJoin);
                float strokeMiterLimit2 = typedArray.getFloat(10, strokeMiterLimit);
                float strokeAlpha2 = typedArray.getFloat(11, strokeAlpha);
                VectorDrawable.nUpdateFullPathProperties(this.mNativePtr, typedArray.getFloat(4, strokeWidth), strokeColor3, strokeAlpha2, fillColor, fillAlpha2, typedArray.getFloat(5, trimPathStart), typedArray.getFloat(6, trimPathEnd), typedArray.getFloat(7, trimPathOffset), strokeMiterLimit2, strokeLineCap2, strokeLineJoin2, typedArray.getInt(13, fillType));
                return;
            }
            boolean z2 = success;
            throw new RuntimeException("Error: inconsistent property count");
        }

        public boolean canApplyTheme() {
            if (this.mThemeAttrs != null) {
                return true;
            }
            boolean fillCanApplyTheme = canComplexColorApplyTheme(this.mFillColors);
            boolean strokeCanApplyTheme = canComplexColorApplyTheme(this.mStrokeColors);
            if (fillCanApplyTheme || strokeCanApplyTheme) {
                return true;
            }
            return false;
        }

        public void applyTheme(Resources.Theme t) {
            if (this.mThemeAttrs != null) {
                TypedArray a = t.resolveAttributes(this.mThemeAttrs, R.styleable.VectorDrawablePath);
                updateStateFromTypedArray(a);
                a.recycle();
            }
            boolean fillCanApplyTheme = canComplexColorApplyTheme(this.mFillColors);
            boolean strokeCanApplyTheme = canComplexColorApplyTheme(this.mStrokeColors);
            if (fillCanApplyTheme) {
                this.mFillColors = this.mFillColors.obtainForTheme(t);
                if (this.mFillColors instanceof GradientColor) {
                    VectorDrawable.nUpdateFullPathFillGradient(this.mNativePtr, ((GradientColor) this.mFillColors).getShader().getNativeInstance());
                } else if (this.mFillColors instanceof ColorStateList) {
                    VectorDrawable.nSetFillColor(this.mNativePtr, this.mFillColors.getDefaultColor());
                }
            }
            if (strokeCanApplyTheme) {
                this.mStrokeColors = this.mStrokeColors.obtainForTheme(t);
                if (this.mStrokeColors instanceof GradientColor) {
                    VectorDrawable.nUpdateFullPathStrokeGradient(this.mNativePtr, ((GradientColor) this.mStrokeColors).getShader().getNativeInstance());
                } else if (this.mStrokeColors instanceof ColorStateList) {
                    VectorDrawable.nSetStrokeColor(this.mNativePtr, this.mStrokeColors.getDefaultColor());
                }
            }
        }

        private boolean canComplexColorApplyTheme(ComplexColor complexColor) {
            return complexColor != null && complexColor.canApplyTheme();
        }

        /* access modifiers changed from: package-private */
        public int getStrokeColor() {
            if (isTreeValid()) {
                return VectorDrawable.nGetStrokeColor(this.mNativePtr);
            }
            return 0;
        }

        /* access modifiers changed from: package-private */
        public void setStrokeColor(int strokeColor) {
            this.mStrokeColors = null;
            if (isTreeValid()) {
                VectorDrawable.nSetStrokeColor(this.mNativePtr, strokeColor);
            }
        }

        /* access modifiers changed from: package-private */
        public float getStrokeWidth() {
            if (isTreeValid()) {
                return VectorDrawable.nGetStrokeWidth(this.mNativePtr);
            }
            return 0.0f;
        }

        /* access modifiers changed from: package-private */
        public void setStrokeWidth(float strokeWidth) {
            if (isTreeValid()) {
                VectorDrawable.nSetStrokeWidth(this.mNativePtr, strokeWidth);
            }
        }

        /* access modifiers changed from: package-private */
        public float getStrokeAlpha() {
            if (isTreeValid()) {
                return VectorDrawable.nGetStrokeAlpha(this.mNativePtr);
            }
            return 0.0f;
        }

        /* access modifiers changed from: package-private */
        public void setStrokeAlpha(float strokeAlpha) {
            if (isTreeValid()) {
                VectorDrawable.nSetStrokeAlpha(this.mNativePtr, strokeAlpha);
            }
        }

        /* access modifiers changed from: package-private */
        public int getFillColor() {
            if (isTreeValid()) {
                return VectorDrawable.nGetFillColor(this.mNativePtr);
            }
            return 0;
        }

        /* access modifiers changed from: package-private */
        public void setFillColor(int fillColor) {
            this.mFillColors = null;
            if (isTreeValid()) {
                VectorDrawable.nSetFillColor(this.mNativePtr, fillColor);
            }
        }

        /* access modifiers changed from: package-private */
        public float getFillAlpha() {
            if (isTreeValid()) {
                return VectorDrawable.nGetFillAlpha(this.mNativePtr);
            }
            return 0.0f;
        }

        /* access modifiers changed from: package-private */
        public void setFillAlpha(float fillAlpha) {
            if (isTreeValid()) {
                VectorDrawable.nSetFillAlpha(this.mNativePtr, fillAlpha);
            }
        }

        /* access modifiers changed from: package-private */
        public float getTrimPathStart() {
            if (isTreeValid()) {
                return VectorDrawable.nGetTrimPathStart(this.mNativePtr);
            }
            return 0.0f;
        }

        /* access modifiers changed from: package-private */
        public void setTrimPathStart(float trimPathStart) {
            if (isTreeValid()) {
                VectorDrawable.nSetTrimPathStart(this.mNativePtr, trimPathStart);
            }
        }

        /* access modifiers changed from: package-private */
        public float getTrimPathEnd() {
            if (isTreeValid()) {
                return VectorDrawable.nGetTrimPathEnd(this.mNativePtr);
            }
            return 0.0f;
        }

        /* access modifiers changed from: package-private */
        public void setTrimPathEnd(float trimPathEnd) {
            if (isTreeValid()) {
                VectorDrawable.nSetTrimPathEnd(this.mNativePtr, trimPathEnd);
            }
        }

        /* access modifiers changed from: package-private */
        public float getTrimPathOffset() {
            if (isTreeValid()) {
                return VectorDrawable.nGetTrimPathOffset(this.mNativePtr);
            }
            return 0.0f;
        }

        /* access modifiers changed from: package-private */
        public void setTrimPathOffset(float trimPathOffset) {
            if (isTreeValid()) {
                VectorDrawable.nSetTrimPathOffset(this.mNativePtr, trimPathOffset);
            }
        }
    }

    static abstract class VObject {
        VirtualRefBasePtr mTreePtr = null;

        /* access modifiers changed from: package-private */
        public abstract void applyTheme(Resources.Theme theme);

        /* access modifiers changed from: package-private */
        public abstract boolean canApplyTheme();

        /* access modifiers changed from: package-private */
        public abstract long getNativePtr();

        /* access modifiers changed from: package-private */
        public abstract int getNativeSize();

        /* access modifiers changed from: package-private */
        public abstract Property getProperty(String str);

        /* access modifiers changed from: package-private */
        public abstract boolean hasFocusStateSpecified();

        /* access modifiers changed from: package-private */
        public abstract void inflate(Resources resources, AttributeSet attributeSet, Resources.Theme theme);

        /* access modifiers changed from: package-private */
        public abstract boolean isStateful();

        /* access modifiers changed from: package-private */
        public abstract boolean onStateChange(int[] iArr);

        VObject() {
        }

        /* access modifiers changed from: package-private */
        public boolean isTreeValid() {
            return (this.mTreePtr == null || this.mTreePtr.get() == 0) ? false : true;
        }

        /* access modifiers changed from: package-private */
        public void setTree(VirtualRefBasePtr ptr) {
            this.mTreePtr = ptr;
        }
    }
}
