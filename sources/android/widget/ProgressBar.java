package android.widget;

import android.animation.ObjectAnimator;
import android.annotation.UnsupportedAppUsage;
import android.app.slice.Slice;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.MathUtils;
import android.util.Pools;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewHierarchyEncoder;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.view.inspector.InspectionCompanion;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.widget.RemoteViews;
import com.android.internal.R;
import java.util.ArrayList;

@RemoteViews.RemoteView
public class ProgressBar extends View {
    private static final int MAX_LEVEL = 10000;
    private static final int PROGRESS_ANIM_DURATION = 80;
    private static final DecelerateInterpolator PROGRESS_ANIM_INTERPOLATOR = new DecelerateInterpolator();
    private static final int TIMEOUT_SEND_ACCESSIBILITY_EVENT = 200;
    private final FloatProperty<ProgressBar> VISUAL_PROGRESS;
    private AccessibilityEventSender mAccessibilityEventSender;
    private boolean mAggregatedIsVisible;
    private AlphaAnimation mAnimation;
    private boolean mAttached;
    private int mBehavior;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private Drawable mCurrentDrawable;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124052713)
    private int mDuration;
    private boolean mHasAnimation;
    private boolean mInDrawing;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private boolean mIndeterminate;
    private Drawable mIndeterminateDrawable;
    private Interpolator mInterpolator;
    private int mMax;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    int mMaxHeight;
    private boolean mMaxInitialized;
    int mMaxWidth;
    private int mMin;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    int mMinHeight;
    private boolean mMinInitialized;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    int mMinWidth;
    @UnsupportedAppUsage
    boolean mMirrorForRtl;
    private boolean mNoInvalidate;
    @UnsupportedAppUsage(trackingBug = 124049927)
    private boolean mOnlyIndeterminate;
    private int mProgress;
    private Drawable mProgressDrawable;
    private ProgressTintInfo mProgressTintInfo;
    /* access modifiers changed from: private */
    public final ArrayList<RefreshData> mRefreshData;
    /* access modifiers changed from: private */
    public boolean mRefreshIsPosted;
    private RefreshProgressRunnable mRefreshProgressRunnable;
    int mSampleWidth;
    private int mSecondaryProgress;
    private boolean mShouldStartAnimationDrawable;
    private Transformation mTransformation;
    private long mUiThreadId;
    /* access modifiers changed from: private */
    public float mVisualProgress;

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<ProgressBar> {
        private int mIndeterminateDrawableId;
        private int mIndeterminateId;
        private int mIndeterminateTintBlendModeId;
        private int mIndeterminateTintId;
        private int mIndeterminateTintModeId;
        private int mInterpolatorId;
        private int mMaxId;
        private int mMinId;
        private int mMirrorForRtlId;
        private int mProgressBackgroundTintBlendModeId;
        private int mProgressBackgroundTintId;
        private int mProgressBackgroundTintModeId;
        private int mProgressDrawableId;
        private int mProgressId;
        private int mProgressTintBlendModeId;
        private int mProgressTintId;
        private int mProgressTintModeId;
        private boolean mPropertiesMapped = false;
        private int mSecondaryProgressId;
        private int mSecondaryProgressTintBlendModeId;
        private int mSecondaryProgressTintId;
        private int mSecondaryProgressTintModeId;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mIndeterminateId = propertyMapper.mapBoolean("indeterminate", 16843065);
            this.mIndeterminateDrawableId = propertyMapper.mapObject("indeterminateDrawable", 16843067);
            this.mIndeterminateTintId = propertyMapper.mapObject("indeterminateTint", 16843881);
            this.mIndeterminateTintBlendModeId = propertyMapper.mapObject("indeterminateTintBlendMode", 23);
            this.mIndeterminateTintModeId = propertyMapper.mapObject("indeterminateTintMode", 16843882);
            this.mInterpolatorId = propertyMapper.mapObject("interpolator", 16843073);
            this.mMaxId = propertyMapper.mapInt(Slice.SUBTYPE_MAX, 16843062);
            this.mMinId = propertyMapper.mapInt("min", 16844089);
            this.mMirrorForRtlId = propertyMapper.mapBoolean("mirrorForRtl", 16843726);
            this.mProgressId = propertyMapper.mapInt("progress", 16843063);
            this.mProgressBackgroundTintId = propertyMapper.mapObject("progressBackgroundTint", 16843877);
            this.mProgressBackgroundTintBlendModeId = propertyMapper.mapObject("progressBackgroundTintBlendMode", 19);
            this.mProgressBackgroundTintModeId = propertyMapper.mapObject("progressBackgroundTintMode", 16843878);
            this.mProgressDrawableId = propertyMapper.mapObject("progressDrawable", 16843068);
            this.mProgressTintId = propertyMapper.mapObject("progressTint", 16843875);
            this.mProgressTintBlendModeId = propertyMapper.mapObject("progressTintBlendMode", 17);
            this.mProgressTintModeId = propertyMapper.mapObject("progressTintMode", 16843876);
            this.mSecondaryProgressId = propertyMapper.mapInt("secondaryProgress", 16843064);
            this.mSecondaryProgressTintId = propertyMapper.mapObject("secondaryProgressTint", 16843879);
            this.mSecondaryProgressTintBlendModeId = propertyMapper.mapObject("secondaryProgressTintBlendMode", 21);
            this.mSecondaryProgressTintModeId = propertyMapper.mapObject("secondaryProgressTintMode", 16843880);
            this.mPropertiesMapped = true;
        }

        public void readProperties(ProgressBar node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readBoolean(this.mIndeterminateId, node.isIndeterminate());
                propertyReader.readObject(this.mIndeterminateDrawableId, node.getIndeterminateDrawable());
                propertyReader.readObject(this.mIndeterminateTintId, node.getIndeterminateTintList());
                propertyReader.readObject(this.mIndeterminateTintBlendModeId, node.getIndeterminateTintBlendMode());
                propertyReader.readObject(this.mIndeterminateTintModeId, node.getIndeterminateTintMode());
                propertyReader.readObject(this.mInterpolatorId, node.getInterpolator());
                propertyReader.readInt(this.mMaxId, node.getMax());
                propertyReader.readInt(this.mMinId, node.getMin());
                propertyReader.readBoolean(this.mMirrorForRtlId, node.getMirrorForRtl());
                propertyReader.readInt(this.mProgressId, node.getProgress());
                propertyReader.readObject(this.mProgressBackgroundTintId, node.getProgressBackgroundTintList());
                propertyReader.readObject(this.mProgressBackgroundTintBlendModeId, node.getProgressBackgroundTintBlendMode());
                propertyReader.readObject(this.mProgressBackgroundTintModeId, node.getProgressBackgroundTintMode());
                propertyReader.readObject(this.mProgressDrawableId, node.getProgressDrawable());
                propertyReader.readObject(this.mProgressTintId, node.getProgressTintList());
                propertyReader.readObject(this.mProgressTintBlendModeId, node.getProgressTintBlendMode());
                propertyReader.readObject(this.mProgressTintModeId, node.getProgressTintMode());
                propertyReader.readInt(this.mSecondaryProgressId, node.getSecondaryProgress());
                propertyReader.readObject(this.mSecondaryProgressTintId, node.getSecondaryProgressTintList());
                propertyReader.readObject(this.mSecondaryProgressTintBlendModeId, node.getSecondaryProgressTintBlendMode());
                propertyReader.readObject(this.mSecondaryProgressTintModeId, node.getSecondaryProgressTintMode());
                return;
            }
            throw new InspectionCompanion.UninitializedPropertyMapException();
        }
    }

    public ProgressBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 16842871);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        boolean z = false;
        this.mSampleWidth = 0;
        this.mMirrorForRtl = false;
        this.mRefreshData = new ArrayList<>();
        this.VISUAL_PROGRESS = new FloatProperty<ProgressBar>("visual_progress") {
            public void setValue(ProgressBar object, float value) {
                object.setVisualProgress(16908301, value);
                float unused = object.mVisualProgress = value;
            }

            public Float get(ProgressBar object) {
                return Float.valueOf(object.mVisualProgress);
            }
        };
        this.mUiThreadId = Thread.currentThread().getId();
        initProgressBar();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.ProgressBar, attrs, a, defStyleAttr, defStyleRes);
        this.mNoInvalidate = true;
        Drawable progressDrawable = a.getDrawable(8);
        if (progressDrawable != null) {
            if (needsTileify(progressDrawable)) {
                setProgressDrawableTiled(progressDrawable);
            } else {
                setProgressDrawable(progressDrawable);
            }
        }
        this.mDuration = a.getInt(9, this.mDuration);
        this.mMinWidth = a.getDimensionPixelSize(11, this.mMinWidth);
        this.mMaxWidth = a.getDimensionPixelSize(0, this.mMaxWidth);
        this.mMinHeight = a.getDimensionPixelSize(12, this.mMinHeight);
        this.mMaxHeight = a.getDimensionPixelSize(1, this.mMaxHeight);
        this.mBehavior = a.getInt(10, this.mBehavior);
        int resID = a.getResourceId(13, 17432587);
        if (resID > 0) {
            setInterpolator(context, resID);
        }
        setMin(a.getInt(26, this.mMin));
        setMax(a.getInt(2, this.mMax));
        setProgress(a.getInt(3, this.mProgress));
        setSecondaryProgress(a.getInt(4, this.mSecondaryProgress));
        Drawable indeterminateDrawable = a.getDrawable(7);
        if (indeterminateDrawable != null) {
            if (needsTileify(indeterminateDrawable)) {
                setIndeterminateDrawableTiled(indeterminateDrawable);
            } else {
                setIndeterminateDrawable(indeterminateDrawable);
            }
        }
        this.mOnlyIndeterminate = a.getBoolean(6, this.mOnlyIndeterminate);
        this.mNoInvalidate = false;
        setIndeterminate((this.mOnlyIndeterminate || a.getBoolean(5, this.mIndeterminate)) ? true : z);
        this.mMirrorForRtl = a.getBoolean(15, this.mMirrorForRtl);
        if (a.hasValue(17)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mProgressBlendMode = Drawable.parseBlendMode(a.getInt(17, -1), (BlendMode) null);
            this.mProgressTintInfo.mHasProgressTintMode = true;
        }
        if (a.hasValue(16)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mProgressTintList = a.getColorStateList(16);
            this.mProgressTintInfo.mHasProgressTint = true;
        }
        if (a.hasValue(19)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mProgressBackgroundBlendMode = Drawable.parseBlendMode(a.getInt(19, -1), (BlendMode) null);
            this.mProgressTintInfo.mHasProgressBackgroundTintMode = true;
        }
        if (a.hasValue(18)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mProgressBackgroundTintList = a.getColorStateList(18);
            this.mProgressTintInfo.mHasProgressBackgroundTint = true;
        }
        if (a.hasValue(21)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mSecondaryProgressBlendMode = Drawable.parseBlendMode(a.getInt(21, -1), (BlendMode) null);
            this.mProgressTintInfo.mHasSecondaryProgressTintMode = true;
        }
        if (a.hasValue(20)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mSecondaryProgressTintList = a.getColorStateList(20);
            this.mProgressTintInfo.mHasSecondaryProgressTint = true;
        }
        if (a.hasValue(23)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mIndeterminateBlendMode = Drawable.parseBlendMode(a.getInt(23, -1), (BlendMode) null);
            this.mProgressTintInfo.mHasIndeterminateTintMode = true;
        }
        if (a.hasValue(22)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mIndeterminateTintList = a.getColorStateList(22);
            this.mProgressTintInfo.mHasIndeterminateTint = true;
        }
        a.recycle();
        applyProgressTints();
        applyIndeterminateTint();
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
    }

    public void setMinWidth(int minWidth) {
        this.mMinWidth = minWidth;
        requestLayout();
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.mMaxWidth = maxWidth;
        requestLayout();
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public void setMinHeight(int minHeight) {
        this.mMinHeight = minHeight;
        requestLayout();
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.mMaxHeight = maxHeight;
        requestLayout();
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    private static boolean needsTileify(Drawable dr) {
        if (dr instanceof LayerDrawable) {
            LayerDrawable orig = (LayerDrawable) dr;
            int N = orig.getNumberOfLayers();
            for (int i = 0; i < N; i++) {
                if (needsTileify(orig.getDrawable(i))) {
                    return true;
                }
            }
            return false;
        } else if (!(dr instanceof StateListDrawable)) {
            return dr instanceof BitmapDrawable;
        } else {
            StateListDrawable in = (StateListDrawable) dr;
            int N2 = in.getStateCount();
            for (int i2 = 0; i2 < N2; i2++) {
                if (needsTileify(in.getStateDrawable(i2))) {
                    return true;
                }
            }
            return false;
        }
    }

    @UnsupportedAppUsage
    private Drawable tileify(Drawable drawable, boolean clip) {
        int i = 0;
        if (drawable instanceof LayerDrawable) {
            LayerDrawable orig = (LayerDrawable) drawable;
            int N = orig.getNumberOfLayers();
            Drawable[] outDrawables = new Drawable[N];
            for (int i2 = 0; i2 < N; i2++) {
                int id = orig.getId(i2);
                outDrawables[i2] = tileify(orig.getDrawable(i2), id == 16908301 || id == 16908303);
            }
            LayerDrawable clone = new LayerDrawable(outDrawables);
            while (i < N) {
                clone.setId(i, orig.getId(i));
                clone.setLayerGravity(i, orig.getLayerGravity(i));
                clone.setLayerWidth(i, orig.getLayerWidth(i));
                clone.setLayerHeight(i, orig.getLayerHeight(i));
                clone.setLayerInsetLeft(i, orig.getLayerInsetLeft(i));
                clone.setLayerInsetRight(i, orig.getLayerInsetRight(i));
                clone.setLayerInsetTop(i, orig.getLayerInsetTop(i));
                clone.setLayerInsetBottom(i, orig.getLayerInsetBottom(i));
                clone.setLayerInsetStart(i, orig.getLayerInsetStart(i));
                clone.setLayerInsetEnd(i, orig.getLayerInsetEnd(i));
                i++;
            }
            return clone;
        } else if (drawable instanceof StateListDrawable) {
            StateListDrawable in = (StateListDrawable) drawable;
            StateListDrawable out = new StateListDrawable();
            int N2 = in.getStateCount();
            while (i < N2) {
                out.addState(in.getStateSet(i), tileify(in.getStateDrawable(i), clip));
                i++;
            }
            return out;
        } else if (!(drawable instanceof BitmapDrawable)) {
            return drawable;
        } else {
            BitmapDrawable clone2 = (BitmapDrawable) drawable.getConstantState().newDrawable(getResources());
            clone2.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            if (this.mSampleWidth <= 0) {
                this.mSampleWidth = clone2.getIntrinsicWidth();
            }
            if (clip) {
                return new ClipDrawable((Drawable) clone2, 3, 1);
            }
            return clone2;
        }
    }

    /* access modifiers changed from: package-private */
    public Shape getDrawableShape() {
        return new RoundRectShape(new float[]{5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f}, (RectF) null, (float[]) null);
    }

    private Drawable tileifyIndeterminate(Drawable drawable) {
        if (!(drawable instanceof AnimationDrawable)) {
            return drawable;
        }
        AnimationDrawable background = (AnimationDrawable) drawable;
        int N = background.getNumberOfFrames();
        AnimationDrawable newBg = new AnimationDrawable();
        newBg.setOneShot(background.isOneShot());
        for (int i = 0; i < N; i++) {
            Drawable frame = tileify(background.getFrame(i), true);
            frame.setLevel(10000);
            newBg.addFrame(frame, background.getDuration(i));
        }
        newBg.setLevel(10000);
        return newBg;
    }

    private void initProgressBar() {
        this.mMin = 0;
        this.mMax = 100;
        this.mProgress = 0;
        this.mSecondaryProgress = 0;
        this.mIndeterminate = false;
        this.mOnlyIndeterminate = false;
        this.mDuration = 4000;
        this.mBehavior = 1;
        this.mMinWidth = 24;
        this.mMaxWidth = 48;
        this.mMinHeight = 24;
        this.mMaxHeight = 48;
    }

    @ViewDebug.ExportedProperty(category = "progress")
    public synchronized boolean isIndeterminate() {
        return this.mIndeterminate;
    }

    @RemotableViewMethod
    public synchronized void setIndeterminate(boolean indeterminate) {
        if ((!this.mOnlyIndeterminate || !this.mIndeterminate) && indeterminate != this.mIndeterminate) {
            this.mIndeterminate = indeterminate;
            if (indeterminate) {
                swapCurrentDrawable(this.mIndeterminateDrawable);
                startAnimation();
            } else {
                swapCurrentDrawable(this.mProgressDrawable);
                stopAnimation();
            }
        }
    }

    private void swapCurrentDrawable(Drawable newDrawable) {
        Drawable oldDrawable = this.mCurrentDrawable;
        this.mCurrentDrawable = newDrawable;
        if (oldDrawable != this.mCurrentDrawable) {
            if (oldDrawable != null) {
                oldDrawable.setVisible(false, false);
            }
            if (this.mCurrentDrawable != null) {
                this.mCurrentDrawable.setVisible(getWindowVisibility() == 0 && isShown(), false);
            }
        }
    }

    public Drawable getIndeterminateDrawable() {
        return this.mIndeterminateDrawable;
    }

    public void setIndeterminateDrawable(Drawable d) {
        if (this.mIndeterminateDrawable != d) {
            if (this.mIndeterminateDrawable != null) {
                this.mIndeterminateDrawable.setCallback((Drawable.Callback) null);
                unscheduleDrawable(this.mIndeterminateDrawable);
            }
            this.mIndeterminateDrawable = d;
            if (d != null) {
                d.setCallback(this);
                d.setLayoutDirection(getLayoutDirection());
                if (d.isStateful()) {
                    d.setState(getDrawableState());
                }
                applyIndeterminateTint();
            }
            if (this.mIndeterminate) {
                swapCurrentDrawable(d);
                postInvalidate();
            }
        }
    }

    @RemotableViewMethod
    public void setIndeterminateTintList(ColorStateList tint) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mIndeterminateTintList = tint;
        this.mProgressTintInfo.mHasIndeterminateTint = true;
        applyIndeterminateTint();
    }

    public ColorStateList getIndeterminateTintList() {
        if (this.mProgressTintInfo != null) {
            return this.mProgressTintInfo.mIndeterminateTintList;
        }
        return null;
    }

    public void setIndeterminateTintMode(PorterDuff.Mode tintMode) {
        setIndeterminateTintBlendMode(tintMode != null ? BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    public void setIndeterminateTintBlendMode(BlendMode blendMode) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mIndeterminateBlendMode = blendMode;
        this.mProgressTintInfo.mHasIndeterminateTintMode = true;
        applyIndeterminateTint();
    }

    public PorterDuff.Mode getIndeterminateTintMode() {
        BlendMode mode = getIndeterminateTintBlendMode();
        if (mode != null) {
            return BlendMode.blendModeToPorterDuffMode(mode);
        }
        return null;
    }

    public BlendMode getIndeterminateTintBlendMode() {
        if (this.mProgressTintInfo != null) {
            return this.mProgressTintInfo.mIndeterminateBlendMode;
        }
        return null;
    }

    private void applyIndeterminateTint() {
        if (this.mIndeterminateDrawable != null && this.mProgressTintInfo != null) {
            ProgressTintInfo tintInfo = this.mProgressTintInfo;
            if (tintInfo.mHasIndeterminateTint || tintInfo.mHasIndeterminateTintMode) {
                this.mIndeterminateDrawable = this.mIndeterminateDrawable.mutate();
                if (tintInfo.mHasIndeterminateTint) {
                    this.mIndeterminateDrawable.setTintList(tintInfo.mIndeterminateTintList);
                }
                if (tintInfo.mHasIndeterminateTintMode) {
                    this.mIndeterminateDrawable.setTintBlendMode(tintInfo.mIndeterminateBlendMode);
                }
                if (this.mIndeterminateDrawable.isStateful()) {
                    this.mIndeterminateDrawable.setState(getDrawableState());
                }
            }
        }
    }

    public void setIndeterminateDrawableTiled(Drawable d) {
        if (d != null) {
            d = tileifyIndeterminate(d);
        }
        setIndeterminateDrawable(d);
    }

    public Drawable getProgressDrawable() {
        return this.mProgressDrawable;
    }

    public void setProgressDrawable(Drawable d) {
        if (this.mProgressDrawable != d) {
            if (this.mProgressDrawable != null) {
                this.mProgressDrawable.setCallback((Drawable.Callback) null);
                unscheduleDrawable(this.mProgressDrawable);
            }
            this.mProgressDrawable = d;
            if (d != null) {
                d.setCallback(this);
                d.setLayoutDirection(getLayoutDirection());
                if (d.isStateful()) {
                    d.setState(getDrawableState());
                }
                int drawableHeight = d.getMinimumHeight();
                if (this.mMaxHeight < drawableHeight) {
                    this.mMaxHeight = drawableHeight;
                    requestLayout();
                }
                applyProgressTints();
            }
            if (this.mIndeterminate == 0) {
                swapCurrentDrawable(d);
                postInvalidate();
            }
            updateDrawableBounds(getWidth(), getHeight());
            updateDrawableState();
            doRefreshProgress(16908301, this.mProgress, false, false, false);
            doRefreshProgress(16908303, this.mSecondaryProgress, false, false, false);
        }
    }

    public boolean getMirrorForRtl() {
        return this.mMirrorForRtl;
    }

    private void applyProgressTints() {
        if (this.mProgressDrawable != null && this.mProgressTintInfo != null) {
            applyPrimaryProgressTint();
            applyProgressBackgroundTint();
            applySecondaryProgressTint();
        }
    }

    private void applyPrimaryProgressTint() {
        Drawable target;
        if ((this.mProgressTintInfo.mHasProgressTint || this.mProgressTintInfo.mHasProgressTintMode) && (target = getTintTarget(16908301, true)) != null) {
            if (this.mProgressTintInfo.mHasProgressTint) {
                target.setTintList(this.mProgressTintInfo.mProgressTintList);
            }
            if (this.mProgressTintInfo.mHasProgressTintMode) {
                target.setTintBlendMode(this.mProgressTintInfo.mProgressBlendMode);
            }
            if (target.isStateful()) {
                target.setState(getDrawableState());
            }
        }
    }

    private void applyProgressBackgroundTint() {
        Drawable target;
        if ((this.mProgressTintInfo.mHasProgressBackgroundTint || this.mProgressTintInfo.mHasProgressBackgroundTintMode) && (target = getTintTarget(16908288, false)) != null) {
            if (this.mProgressTintInfo.mHasProgressBackgroundTint) {
                target.setTintList(this.mProgressTintInfo.mProgressBackgroundTintList);
            }
            if (this.mProgressTintInfo.mHasProgressBackgroundTintMode) {
                target.setTintBlendMode(this.mProgressTintInfo.mProgressBackgroundBlendMode);
            }
            if (target.isStateful()) {
                target.setState(getDrawableState());
            }
        }
    }

    private void applySecondaryProgressTint() {
        Drawable target;
        if ((this.mProgressTintInfo.mHasSecondaryProgressTint || this.mProgressTintInfo.mHasSecondaryProgressTintMode) && (target = getTintTarget(16908303, false)) != null) {
            if (this.mProgressTintInfo.mHasSecondaryProgressTint) {
                target.setTintList(this.mProgressTintInfo.mSecondaryProgressTintList);
            }
            if (this.mProgressTintInfo.mHasSecondaryProgressTintMode) {
                target.setTintBlendMode(this.mProgressTintInfo.mSecondaryProgressBlendMode);
            }
            if (target.isStateful()) {
                target.setState(getDrawableState());
            }
        }
    }

    @RemotableViewMethod
    public void setProgressTintList(ColorStateList tint) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mProgressTintList = tint;
        this.mProgressTintInfo.mHasProgressTint = true;
        if (this.mProgressDrawable != null) {
            applyPrimaryProgressTint();
        }
    }

    public ColorStateList getProgressTintList() {
        if (this.mProgressTintInfo != null) {
            return this.mProgressTintInfo.mProgressTintList;
        }
        return null;
    }

    public void setProgressTintMode(PorterDuff.Mode tintMode) {
        setProgressTintBlendMode(tintMode != null ? BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    public void setProgressTintBlendMode(BlendMode blendMode) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mProgressBlendMode = blendMode;
        this.mProgressTintInfo.mHasProgressTintMode = true;
        if (this.mProgressDrawable != null) {
            applyPrimaryProgressTint();
        }
    }

    public PorterDuff.Mode getProgressTintMode() {
        BlendMode mode = getProgressTintBlendMode();
        if (mode != null) {
            return BlendMode.blendModeToPorterDuffMode(mode);
        }
        return null;
    }

    public BlendMode getProgressTintBlendMode() {
        if (this.mProgressTintInfo != null) {
            return this.mProgressTintInfo.mProgressBlendMode;
        }
        return null;
    }

    @RemotableViewMethod
    public void setProgressBackgroundTintList(ColorStateList tint) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mProgressBackgroundTintList = tint;
        this.mProgressTintInfo.mHasProgressBackgroundTint = true;
        if (this.mProgressDrawable != null) {
            applyProgressBackgroundTint();
        }
    }

    public ColorStateList getProgressBackgroundTintList() {
        if (this.mProgressTintInfo != null) {
            return this.mProgressTintInfo.mProgressBackgroundTintList;
        }
        return null;
    }

    public void setProgressBackgroundTintMode(PorterDuff.Mode tintMode) {
        setProgressBackgroundTintBlendMode(tintMode != null ? BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    public void setProgressBackgroundTintBlendMode(BlendMode blendMode) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mProgressBackgroundBlendMode = blendMode;
        this.mProgressTintInfo.mHasProgressBackgroundTintMode = true;
        if (this.mProgressDrawable != null) {
            applyProgressBackgroundTint();
        }
    }

    public PorterDuff.Mode getProgressBackgroundTintMode() {
        BlendMode mode = getProgressBackgroundTintBlendMode();
        if (mode != null) {
            return BlendMode.blendModeToPorterDuffMode(mode);
        }
        return null;
    }

    public BlendMode getProgressBackgroundTintBlendMode() {
        if (this.mProgressTintInfo != null) {
            return this.mProgressTintInfo.mProgressBackgroundBlendMode;
        }
        return null;
    }

    public void setSecondaryProgressTintList(ColorStateList tint) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mSecondaryProgressTintList = tint;
        this.mProgressTintInfo.mHasSecondaryProgressTint = true;
        if (this.mProgressDrawable != null) {
            applySecondaryProgressTint();
        }
    }

    public ColorStateList getSecondaryProgressTintList() {
        if (this.mProgressTintInfo != null) {
            return this.mProgressTintInfo.mSecondaryProgressTintList;
        }
        return null;
    }

    public void setSecondaryProgressTintMode(PorterDuff.Mode tintMode) {
        setSecondaryProgressTintBlendMode(tintMode != null ? BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    public void setSecondaryProgressTintBlendMode(BlendMode blendMode) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mSecondaryProgressBlendMode = blendMode;
        this.mProgressTintInfo.mHasSecondaryProgressTintMode = true;
        if (this.mProgressDrawable != null) {
            applySecondaryProgressTint();
        }
    }

    public PorterDuff.Mode getSecondaryProgressTintMode() {
        BlendMode mode = getSecondaryProgressTintBlendMode();
        if (mode != null) {
            return BlendMode.blendModeToPorterDuffMode(mode);
        }
        return null;
    }

    public BlendMode getSecondaryProgressTintBlendMode() {
        if (this.mProgressTintInfo != null) {
            return this.mProgressTintInfo.mSecondaryProgressBlendMode;
        }
        return null;
    }

    private Drawable getTintTarget(int layerId, boolean shouldFallback) {
        Drawable layer = null;
        Drawable d = this.mProgressDrawable;
        if (d == null) {
            return null;
        }
        this.mProgressDrawable = d.mutate();
        if (d instanceof LayerDrawable) {
            layer = ((LayerDrawable) d).findDrawableByLayerId(layerId);
        }
        if (!shouldFallback || layer != null) {
            return layer;
        }
        return d;
    }

    public void setProgressDrawableTiled(Drawable d) {
        if (d != null) {
            d = tileify(d, false);
        }
        setProgressDrawable(d);
    }

    public Drawable getCurrentDrawable() {
        return this.mCurrentDrawable;
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable who) {
        return who == this.mProgressDrawable || who == this.mIndeterminateDrawable || super.verifyDrawable(who);
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.jumpToCurrentState();
        }
        if (this.mIndeterminateDrawable != null) {
            this.mIndeterminateDrawable.jumpToCurrentState();
        }
    }

    public void onResolveDrawables(int layoutDirection) {
        Drawable d = this.mCurrentDrawable;
        if (d != null) {
            d.setLayoutDirection(layoutDirection);
        }
        if (this.mIndeterminateDrawable != null) {
            this.mIndeterminateDrawable.setLayoutDirection(layoutDirection);
        }
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.setLayoutDirection(layoutDirection);
        }
    }

    public void postInvalidate() {
        if (!this.mNoInvalidate) {
            super.postInvalidate();
        }
    }

    private class RefreshProgressRunnable implements Runnable {
        private RefreshProgressRunnable() {
        }

        public void run() {
            synchronized (ProgressBar.this) {
                int count = ProgressBar.this.mRefreshData.size();
                for (int i = 0; i < count; i++) {
                    RefreshData rd = (RefreshData) ProgressBar.this.mRefreshData.get(i);
                    ProgressBar.this.doRefreshProgress(rd.id, rd.progress, rd.fromUser, true, rd.animate);
                    rd.recycle();
                }
                ProgressBar.this.mRefreshData.clear();
                boolean unused = ProgressBar.this.mRefreshIsPosted = false;
            }
        }
    }

    private static class RefreshData {
        private static final int POOL_MAX = 24;
        private static final Pools.SynchronizedPool<RefreshData> sPool = new Pools.SynchronizedPool<>(24);
        public boolean animate;
        public boolean fromUser;
        public int id;
        public int progress;

        private RefreshData() {
        }

        public static RefreshData obtain(int id2, int progress2, boolean fromUser2, boolean animate2) {
            RefreshData rd = sPool.acquire();
            if (rd == null) {
                rd = new RefreshData();
            }
            rd.id = id2;
            rd.progress = progress2;
            rd.fromUser = fromUser2;
            rd.animate = animate2;
            return rd;
        }

        public void recycle() {
            sPool.release(this);
        }
    }

    /* access modifiers changed from: private */
    public synchronized void doRefreshProgress(int id, int progress, boolean fromUser, boolean callBackToApp, boolean animate) {
        int range = this.mMax - this.mMin;
        float scale = range > 0 ? ((float) (progress - this.mMin)) / ((float) range) : 0.0f;
        boolean isPrimary = id == 16908301;
        if (!isPrimary || !animate) {
            setVisualProgress(id, scale);
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, this.VISUAL_PROGRESS, scale);
            animator.setAutoCancel(true);
            animator.setDuration(80);
            animator.setInterpolator(PROGRESS_ANIM_INTERPOLATOR);
            animator.start();
        }
        if (isPrimary && callBackToApp) {
            onProgressRefresh(scale, fromUser, progress);
        }
    }

    /* access modifiers changed from: package-private */
    public void onProgressRefresh(float scale, boolean fromUser, int progress) {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            scheduleAccessibilityEventSender();
        }
    }

    /* access modifiers changed from: private */
    public void setVisualProgress(int id, float progress) {
        this.mVisualProgress = progress;
        Drawable d = this.mCurrentDrawable;
        if ((d instanceof LayerDrawable) && (d = ((LayerDrawable) d).findDrawableByLayerId(id)) == null) {
            d = this.mCurrentDrawable;
        }
        if (d != null) {
            d.setLevel((int) (10000.0f * progress));
        } else {
            invalidate();
        }
        onVisualProgressChanged(id, progress);
    }

    /* access modifiers changed from: package-private */
    public void onVisualProgressChanged(int id, float progress) {
    }

    @UnsupportedAppUsage
    private synchronized void refreshProgress(int id, int progress, boolean fromUser, boolean animate) {
        if (this.mUiThreadId == Thread.currentThread().getId()) {
            doRefreshProgress(id, progress, fromUser, true, animate);
        } else {
            if (this.mRefreshProgressRunnable == null) {
                this.mRefreshProgressRunnable = new RefreshProgressRunnable();
            }
            this.mRefreshData.add(RefreshData.obtain(id, progress, fromUser, animate));
            if (this.mAttached && !this.mRefreshIsPosted) {
                post(this.mRefreshProgressRunnable);
                this.mRefreshIsPosted = true;
            }
        }
    }

    @RemotableViewMethod
    public synchronized void setProgress(int progress) {
        setProgressInternal(progress, false, false);
    }

    public void setProgress(int progress, boolean animate) {
        setProgressInternal(progress, false, animate);
    }

    /* access modifiers changed from: package-private */
    @RemotableViewMethod
    @UnsupportedAppUsage
    public synchronized boolean setProgressInternal(int progress, boolean fromUser, boolean animate) {
        if (this.mIndeterminate) {
            return false;
        }
        int progress2 = MathUtils.constrain(progress, this.mMin, this.mMax);
        if (progress2 == this.mProgress) {
            return false;
        }
        this.mProgress = progress2;
        refreshProgress(16908301, this.mProgress, fromUser, animate);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0025, code lost:
        return;
     */
    @android.view.RemotableViewMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void setSecondaryProgress(int r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r0 = r3.mIndeterminate     // Catch:{ all -> 0x0026 }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r3)
            return
        L_0x0007:
            int r0 = r3.mMin     // Catch:{ all -> 0x0026 }
            if (r4 >= r0) goto L_0x000e
            int r0 = r3.mMin     // Catch:{ all -> 0x0026 }
            r4 = r0
        L_0x000e:
            int r0 = r3.mMax     // Catch:{ all -> 0x0026 }
            if (r4 <= r0) goto L_0x0015
            int r0 = r3.mMax     // Catch:{ all -> 0x0026 }
            r4 = r0
        L_0x0015:
            int r0 = r3.mSecondaryProgress     // Catch:{ all -> 0x0026 }
            if (r4 == r0) goto L_0x0024
            r3.mSecondaryProgress = r4     // Catch:{ all -> 0x0026 }
            r0 = 16908303(0x102000f, float:2.387727E-38)
            int r1 = r3.mSecondaryProgress     // Catch:{ all -> 0x0026 }
            r2 = 0
            r3.refreshProgress(r0, r1, r2, r2)     // Catch:{ all -> 0x0026 }
        L_0x0024:
            monitor-exit(r3)
            return
        L_0x0026:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.ProgressBar.setSecondaryProgress(int):void");
    }

    @ViewDebug.ExportedProperty(category = "progress")
    public synchronized int getProgress() {
        return this.mIndeterminate ? 0 : this.mProgress;
    }

    @ViewDebug.ExportedProperty(category = "progress")
    public synchronized int getSecondaryProgress() {
        return this.mIndeterminate ? 0 : this.mSecondaryProgress;
    }

    @ViewDebug.ExportedProperty(category = "progress")
    public synchronized int getMin() {
        return this.mMin;
    }

    @ViewDebug.ExportedProperty(category = "progress")
    public synchronized int getMax() {
        return this.mMax;
    }

    @RemotableViewMethod
    public synchronized void setMin(int min) {
        if (this.mMaxInitialized && min > this.mMax) {
            min = this.mMax;
        }
        this.mMinInitialized = true;
        if (!this.mMaxInitialized || min == this.mMin) {
            this.mMin = min;
        } else {
            this.mMin = min;
            postInvalidate();
            if (this.mProgress < min) {
                this.mProgress = min;
            }
            refreshProgress(16908301, this.mProgress, false, false);
        }
    }

    @RemotableViewMethod
    public synchronized void setMax(int max) {
        if (this.mMinInitialized && max < this.mMin) {
            max = this.mMin;
        }
        this.mMaxInitialized = true;
        if (!this.mMinInitialized || max == this.mMax) {
            this.mMax = max;
        } else {
            this.mMax = max;
            postInvalidate();
            if (this.mProgress > max) {
                this.mProgress = max;
            }
            refreshProgress(16908301, this.mProgress, false, false);
        }
    }

    public final synchronized void incrementProgressBy(int diff) {
        setProgress(this.mProgress + diff);
    }

    public final synchronized void incrementSecondaryProgressBy(int diff) {
        setSecondaryProgress(this.mSecondaryProgress + diff);
    }

    /* access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public void startAnimation() {
        if (getVisibility() == 0 && getWindowVisibility() == 0) {
            if (this.mIndeterminateDrawable instanceof Animatable) {
                this.mShouldStartAnimationDrawable = true;
                this.mHasAnimation = false;
            } else {
                this.mHasAnimation = true;
                if (this.mInterpolator == null) {
                    this.mInterpolator = new LinearInterpolator();
                }
                if (this.mTransformation == null) {
                    this.mTransformation = new Transformation();
                } else {
                    this.mTransformation.clear();
                }
                if (this.mAnimation == null) {
                    this.mAnimation = new AlphaAnimation(0.0f, 1.0f);
                } else {
                    this.mAnimation.reset();
                }
                this.mAnimation.setRepeatMode(this.mBehavior);
                this.mAnimation.setRepeatCount(-1);
                this.mAnimation.setDuration((long) this.mDuration);
                this.mAnimation.setInterpolator(this.mInterpolator);
                this.mAnimation.setStartTime(-1);
            }
            postInvalidate();
        }
    }

    /* access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public void stopAnimation() {
        this.mHasAnimation = false;
        if (this.mIndeterminateDrawable instanceof Animatable) {
            ((Animatable) this.mIndeterminateDrawable).stop();
            this.mShouldStartAnimationDrawable = false;
        }
        postInvalidate();
    }

    public void setInterpolator(Context context, int resID) {
        setInterpolator(AnimationUtils.loadInterpolator(context, resID));
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public Interpolator getInterpolator() {
        return this.mInterpolator;
    }

    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        if (isVisible != this.mAggregatedIsVisible) {
            this.mAggregatedIsVisible = isVisible;
            if (this.mIndeterminate) {
                if (isVisible) {
                    startAnimation();
                } else {
                    stopAnimation();
                }
            }
            if (this.mCurrentDrawable != null) {
                this.mCurrentDrawable.setVisible(isVisible, false);
            }
        }
    }

    public void invalidateDrawable(Drawable dr) {
        if (this.mInDrawing) {
            return;
        }
        if (verifyDrawable(dr)) {
            Rect dirty = dr.getBounds();
            int scrollX = this.mScrollX + this.mPaddingLeft;
            int scrollY = this.mScrollY + this.mPaddingTop;
            invalidate(dirty.left + scrollX, dirty.top + scrollY, dirty.right + scrollX, dirty.bottom + scrollY);
            return;
        }
        super.invalidateDrawable(dr);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        updateDrawableBounds(w, h);
    }

    private void updateDrawableBounds(int w, int h) {
        int w2 = w - (this.mPaddingRight + this.mPaddingLeft);
        int h2 = h - (this.mPaddingTop + this.mPaddingBottom);
        int right = w2;
        int bottom = h2;
        int top = 0;
        int left = 0;
        if (this.mIndeterminateDrawable != null) {
            if (this.mOnlyIndeterminate && !(this.mIndeterminateDrawable instanceof AnimationDrawable)) {
                float intrinsicAspect = ((float) this.mIndeterminateDrawable.getIntrinsicWidth()) / ((float) this.mIndeterminateDrawable.getIntrinsicHeight());
                float boundAspect = ((float) w2) / ((float) h2);
                if (intrinsicAspect != boundAspect) {
                    if (boundAspect > intrinsicAspect) {
                        int width = (int) (((float) h2) * intrinsicAspect);
                        left = (w2 - width) / 2;
                        right = left + width;
                    } else {
                        int height = (int) (((float) w2) * (1.0f / intrinsicAspect));
                        int top2 = (h2 - height) / 2;
                        bottom = height + top2;
                        top = top2;
                    }
                }
            }
            if (isLayoutRtl() != 0 && this.mMirrorForRtl) {
                int tempLeft = left;
                left = w2 - right;
                right = w2 - tempLeft;
            }
            this.mIndeterminateDrawable.setBounds(left, top, right, bottom);
        }
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.setBounds(0, 0, right, bottom);
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTrack(canvas);
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: package-private */
    public void drawTrack(Canvas canvas) {
        Drawable d = this.mCurrentDrawable;
        if (d != null) {
            int saveCount = canvas.save();
            if (!isLayoutRtl() || !this.mMirrorForRtl) {
                canvas.translate((float) this.mPaddingLeft, (float) this.mPaddingTop);
            } else {
                canvas.translate((float) (getWidth() - this.mPaddingRight), (float) this.mPaddingTop);
                canvas.scale(-1.0f, 1.0f);
            }
            long time = getDrawingTime();
            if (this.mHasAnimation) {
                this.mAnimation.getTransformation(time, this.mTransformation);
                float scale = this.mTransformation.getAlpha();
                try {
                    this.mInDrawing = true;
                    d.setLevel((int) (10000.0f * scale));
                    this.mInDrawing = false;
                    postInvalidateOnAnimation();
                } catch (Throwable th) {
                    this.mInDrawing = false;
                    throw th;
                }
            }
            d.draw(canvas);
            canvas.restoreToCount(saveCount);
            if (this.mShouldStartAnimationDrawable && (d instanceof Animatable)) {
                ((Animatable) d).start();
                this.mShouldStartAnimationDrawable = false;
            }
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dw = 0;
        int dh = 0;
        Drawable d = this.mCurrentDrawable;
        if (d != null) {
            dw = Math.max(this.mMinWidth, Math.min(this.mMaxWidth, d.getIntrinsicWidth()));
            dh = Math.max(this.mMinHeight, Math.min(this.mMaxHeight, d.getIntrinsicHeight()));
        }
        updateDrawableState();
        setMeasuredDimension(resolveSizeAndState(dw + this.mPaddingLeft + this.mPaddingRight, widthMeasureSpec, 0), resolveSizeAndState(dh + this.mPaddingTop + this.mPaddingBottom, heightMeasureSpec, 0));
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        updateDrawableState();
    }

    private void updateDrawableState() {
        int[] state = getDrawableState();
        boolean changed = false;
        Drawable progressDrawable = this.mProgressDrawable;
        if (progressDrawable != null && progressDrawable.isStateful()) {
            changed = false | progressDrawable.setState(state);
        }
        Drawable indeterminateDrawable = this.mIndeterminateDrawable;
        if (indeterminateDrawable != null && indeterminateDrawable.isStateful()) {
            changed |= indeterminateDrawable.setState(state);
        }
        if (changed) {
            invalidate();
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.setHotspot(x, y);
        }
        if (this.mIndeterminateDrawable != null) {
            this.mIndeterminateDrawable.setHotspot(x, y);
        }
    }

    static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int progress;
        int secondaryProgress;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.progress = in.readInt();
            this.secondaryProgress = in.readInt();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.progress);
            out.writeInt(this.secondaryProgress);
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.progress = this.mProgress;
        ss.secondaryProgress = this.mSecondaryProgress;
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setProgress(ss.progress);
        setSecondaryProgress(ss.secondaryProgress);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mIndeterminate) {
            startAnimation();
        }
        if (this.mRefreshData != null) {
            synchronized (this) {
                int count = this.mRefreshData.size();
                for (int i = 0; i < count; i++) {
                    RefreshData rd = this.mRefreshData.get(i);
                    doRefreshProgress(rd.id, rd.progress, rd.fromUser, true, rd.animate);
                    rd.recycle();
                }
                this.mRefreshData.clear();
            }
        }
        this.mAttached = true;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        if (this.mIndeterminate) {
            stopAnimation();
        }
        if (this.mRefreshProgressRunnable != null) {
            removeCallbacks(this.mRefreshProgressRunnable);
            this.mRefreshIsPosted = false;
        }
        if (this.mAccessibilityEventSender != null) {
            removeCallbacks(this.mAccessibilityEventSender);
        }
        super.onDetachedFromWindow();
        this.mAttached = false;
    }

    public CharSequence getAccessibilityClassName() {
        return ProgressBar.class.getName();
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setItemCount(this.mMax - this.mMin);
        event.setCurrentItemIndex(this.mProgress);
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (!isIndeterminate()) {
            info.setRangeInfo(AccessibilityNodeInfo.RangeInfo.obtain(0, (float) getMin(), (float) getMax(), (float) getProgress()));
        }
    }

    private void scheduleAccessibilityEventSender() {
        if (this.mAccessibilityEventSender == null) {
            this.mAccessibilityEventSender = new AccessibilityEventSender();
        } else {
            removeCallbacks(this.mAccessibilityEventSender);
        }
        postDelayed(this.mAccessibilityEventSender, 200);
    }

    /* access modifiers changed from: protected */
    public void encodeProperties(ViewHierarchyEncoder stream) {
        super.encodeProperties(stream);
        stream.addProperty("progress:max", getMax());
        stream.addProperty("progress:progress", getProgress());
        stream.addProperty("progress:secondaryProgress", getSecondaryProgress());
        stream.addProperty("progress:indeterminate", isIndeterminate());
    }

    public boolean isAnimating() {
        return isIndeterminate() && getWindowVisibility() == 0 && isShown();
    }

    private class AccessibilityEventSender implements Runnable {
        private AccessibilityEventSender() {
        }

        public void run() {
            ProgressBar.this.sendAccessibilityEvent(4);
        }
    }

    private static class ProgressTintInfo {
        boolean mHasIndeterminateTint;
        boolean mHasIndeterminateTintMode;
        boolean mHasProgressBackgroundTint;
        boolean mHasProgressBackgroundTintMode;
        boolean mHasProgressTint;
        boolean mHasProgressTintMode;
        boolean mHasSecondaryProgressTint;
        boolean mHasSecondaryProgressTintMode;
        BlendMode mIndeterminateBlendMode;
        ColorStateList mIndeterminateTintList;
        BlendMode mProgressBackgroundBlendMode;
        ColorStateList mProgressBackgroundTintList;
        BlendMode mProgressBlendMode;
        ColorStateList mProgressTintList;
        BlendMode mSecondaryProgressBlendMode;
        ColorStateList mSecondaryProgressTintList;

        private ProgressTintInfo() {
        }
    }
}
