package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ShapeDrawable extends Drawable {
    private BlendModeColorFilter mBlendModeColorFilter;
    private boolean mMutated;
    private ShapeState mShapeState;

    public static abstract class ShaderFactory {
        public abstract Shader resize(int i, int i2);
    }

    public ShapeDrawable() {
        this(new ShapeState(), (Resources) null);
    }

    public ShapeDrawable(Shape s) {
        this(new ShapeState(), (Resources) null);
        this.mShapeState.mShape = s;
    }

    public Shape getShape() {
        return this.mShapeState.mShape;
    }

    public void setShape(Shape s) {
        this.mShapeState.mShape = s;
        updateShape();
    }

    public void setShaderFactory(ShaderFactory fact) {
        this.mShapeState.mShaderFactory = fact;
    }

    public ShaderFactory getShaderFactory() {
        return this.mShapeState.mShaderFactory;
    }

    public Paint getPaint() {
        return this.mShapeState.mPaint;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        if ((left | top | right | bottom) == 0) {
            this.mShapeState.mPadding = null;
        } else {
            if (this.mShapeState.mPadding == null) {
                this.mShapeState.mPadding = new Rect();
            }
            this.mShapeState.mPadding.set(left, top, right, bottom);
        }
        invalidateSelf();
    }

    public void setPadding(Rect padding) {
        if (padding == null) {
            this.mShapeState.mPadding = null;
        } else {
            if (this.mShapeState.mPadding == null) {
                this.mShapeState.mPadding = new Rect();
            }
            this.mShapeState.mPadding.set(padding);
        }
        invalidateSelf();
    }

    public void setIntrinsicWidth(int width) {
        this.mShapeState.mIntrinsicWidth = width;
        invalidateSelf();
    }

    public void setIntrinsicHeight(int height) {
        this.mShapeState.mIntrinsicHeight = height;
        invalidateSelf();
    }

    public int getIntrinsicWidth() {
        return this.mShapeState.mIntrinsicWidth;
    }

    public int getIntrinsicHeight() {
        return this.mShapeState.mIntrinsicHeight;
    }

    public boolean getPadding(Rect padding) {
        if (this.mShapeState.mPadding == null) {
            return super.getPadding(padding);
        }
        padding.set(this.mShapeState.mPadding);
        return true;
    }

    private static int modulateAlpha(int paintAlpha, int alpha) {
        return (paintAlpha * ((alpha >>> 7) + alpha)) >>> 8;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Shape shape, Canvas canvas, Paint paint) {
        shape.draw(canvas, paint);
    }

    public void draw(Canvas canvas) {
        boolean clearColorFilter;
        Rect r = getBounds();
        ShapeState state = this.mShapeState;
        Paint paint = state.mPaint;
        int prevAlpha = paint.getAlpha();
        paint.setAlpha(modulateAlpha(prevAlpha, state.mAlpha));
        if (!(paint.getAlpha() == 0 && paint.getXfermode() == null && !paint.hasShadowLayer())) {
            if (this.mBlendModeColorFilter == null || paint.getColorFilter() != null) {
                clearColorFilter = false;
            } else {
                paint.setColorFilter(this.mBlendModeColorFilter);
                clearColorFilter = true;
            }
            if (state.mShape != null) {
                int count = canvas.save();
                canvas.translate((float) r.left, (float) r.top);
                onDraw(state.mShape, canvas, paint);
                canvas.restoreToCount(count);
            } else {
                canvas.drawRect(r, paint);
            }
            if (clearColorFilter) {
                paint.setColorFilter((ColorFilter) null);
            }
        }
        paint.setAlpha(prevAlpha);
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mShapeState.getChangingConfigurations();
    }

    public void setAlpha(int alpha) {
        this.mShapeState.mAlpha = alpha;
        invalidateSelf();
    }

    public int getAlpha() {
        return this.mShapeState.mAlpha;
    }

    public void setTintList(ColorStateList tint) {
        this.mShapeState.mTint = tint;
        this.mBlendModeColorFilter = updateBlendModeFilter(this.mBlendModeColorFilter, tint, this.mShapeState.mBlendMode);
        invalidateSelf();
    }

    public void setTintBlendMode(BlendMode blendMode) {
        this.mShapeState.mBlendMode = blendMode;
        this.mBlendModeColorFilter = updateBlendModeFilter(this.mBlendModeColorFilter, this.mShapeState.mTint, blendMode);
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mShapeState.mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public void setXfermode(Xfermode mode) {
        this.mShapeState.mPaint.setXfermode(mode);
        invalidateSelf();
    }

    public int getOpacity() {
        if (this.mShapeState.mShape != null) {
            return -3;
        }
        Paint p = this.mShapeState.mPaint;
        if (p.getXfermode() != null) {
            return -3;
        }
        int alpha = p.getAlpha();
        if (alpha == 0) {
            return -2;
        }
        if (alpha == 255) {
            return -1;
        }
        return -3;
    }

    public void setDither(boolean dither) {
        this.mShapeState.mPaint.setDither(dither);
        invalidateSelf();
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updateShape();
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] stateSet) {
        ShapeState state = this.mShapeState;
        if (state.mTint == null || state.mBlendMode == null) {
            return false;
        }
        this.mBlendModeColorFilter = updateBlendModeFilter(this.mBlendModeColorFilter, state.mTint, state.mBlendMode);
        return true;
    }

    public boolean isStateful() {
        ShapeState s = this.mShapeState;
        return super.isStateful() || (s.mTint != null && s.mTint.isStateful());
    }

    public boolean hasFocusStateSpecified() {
        return this.mShapeState.mTint != null && this.mShapeState.mTint.hasFocusStateSpecified();
    }

    /* access modifiers changed from: protected */
    public boolean inflateTag(String name, Resources r, XmlPullParser parser, AttributeSet attrs) {
        if (!"padding".equals(name)) {
            return false;
        }
        TypedArray a = r.obtainAttributes(attrs, R.styleable.ShapeDrawablePadding);
        setPadding(a.getDimensionPixelOffset(0, 0), a.getDimensionPixelOffset(1, 0), a.getDimensionPixelOffset(2, 0), a.getDimensionPixelOffset(3, 0));
        a.recycle();
        return true;
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs, theme);
        TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.ShapeDrawable);
        updateStateFromTypedArray(a);
        a.recycle();
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                updateLocalState();
            } else if (type == 2) {
                String name = parser.getName();
                if (!inflateTag(name, r, parser, attrs)) {
                    Log.w("drawable", "Unknown element: " + name + " for ShapeDrawable " + this);
                }
            }
        }
        updateLocalState();
    }

    public void applyTheme(Resources.Theme t) {
        super.applyTheme(t);
        ShapeState state = this.mShapeState;
        if (state != null) {
            if (state.mThemeAttrs != null) {
                TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.ShapeDrawable);
                updateStateFromTypedArray(a);
                a.recycle();
            }
            if (state.mTint != null && state.mTint.canApplyTheme()) {
                state.mTint = state.mTint.obtainForTheme(t);
            }
            updateLocalState();
        }
    }

    private void updateStateFromTypedArray(TypedArray a) {
        ShapeState state = this.mShapeState;
        Paint paint = state.mPaint;
        state.mChangingConfigurations |= a.getChangingConfigurations();
        state.mThemeAttrs = a.extractThemeAttrs();
        paint.setColor(a.getColor(4, paint.getColor()));
        paint.setDither(a.getBoolean(0, paint.isDither()));
        state.mIntrinsicWidth = (int) a.getDimension(3, (float) state.mIntrinsicWidth);
        state.mIntrinsicHeight = (int) a.getDimension(2, (float) state.mIntrinsicHeight);
        int tintMode = a.getInt(5, -1);
        if (tintMode != -1) {
            state.mBlendMode = Drawable.parseBlendMode(tintMode, BlendMode.SRC_IN);
        }
        ColorStateList tint = a.getColorStateList(1);
        if (tint != null) {
            state.mTint = tint;
        }
    }

    private void updateShape() {
        if (this.mShapeState.mShape != null) {
            Rect r = getBounds();
            int w = r.width();
            int h = r.height();
            this.mShapeState.mShape.resize((float) w, (float) h);
            if (this.mShapeState.mShaderFactory != null) {
                this.mShapeState.mPaint.setShader(this.mShapeState.mShaderFactory.resize(w, h));
            }
        }
        invalidateSelf();
    }

    public void getOutline(Outline outline) {
        if (this.mShapeState.mShape != null) {
            this.mShapeState.mShape.getOutline(outline);
            outline.setAlpha(((float) getAlpha()) / 255.0f);
        }
    }

    public Drawable.ConstantState getConstantState() {
        this.mShapeState.mChangingConfigurations = getChangingConfigurations();
        return this.mShapeState;
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mShapeState = new ShapeState(this.mShapeState);
            updateLocalState();
            this.mMutated = true;
        }
        return this;
    }

    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    static final class ShapeState extends Drawable.ConstantState {
        int mAlpha;
        BlendMode mBlendMode;
        int mChangingConfigurations;
        int mIntrinsicHeight;
        int mIntrinsicWidth;
        Rect mPadding;
        final Paint mPaint;
        ShaderFactory mShaderFactory;
        Shape mShape;
        int[] mThemeAttrs;
        ColorStateList mTint;

        ShapeState() {
            this.mBlendMode = Drawable.DEFAULT_BLEND_MODE;
            this.mAlpha = 255;
            this.mPaint = new Paint(1);
        }

        ShapeState(ShapeState orig) {
            this.mBlendMode = Drawable.DEFAULT_BLEND_MODE;
            this.mAlpha = 255;
            this.mChangingConfigurations = orig.mChangingConfigurations;
            this.mPaint = new Paint(orig.mPaint);
            this.mThemeAttrs = orig.mThemeAttrs;
            if (orig.mShape != null) {
                try {
                    this.mShape = orig.mShape.clone();
                } catch (CloneNotSupportedException e) {
                    this.mShape = orig.mShape;
                }
            }
            this.mTint = orig.mTint;
            this.mBlendMode = orig.mBlendMode;
            if (orig.mPadding != null) {
                this.mPadding = new Rect(orig.mPadding);
            }
            this.mIntrinsicWidth = orig.mIntrinsicWidth;
            this.mIntrinsicHeight = orig.mIntrinsicHeight;
            this.mAlpha = orig.mAlpha;
            this.mShaderFactory = orig.mShaderFactory;
        }

        public boolean canApplyTheme() {
            return this.mThemeAttrs != null || (this.mTint != null && this.mTint.canApplyTheme());
        }

        public Drawable newDrawable() {
            return new ShapeDrawable(new ShapeState(this), (Resources) null);
        }

        public Drawable newDrawable(Resources res) {
            return new ShapeDrawable(new ShapeState(this), res);
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations | (this.mTint != null ? this.mTint.getChangingConfigurations() : 0);
        }
    }

    private ShapeDrawable(ShapeState state, Resources res) {
        this.mShapeState = state;
        updateLocalState();
    }

    private void updateLocalState() {
        this.mBlendModeColorFilter = updateBlendModeFilter(this.mBlendModeColorFilter, this.mShapeState.mTint, this.mShapeState.mBlendMode);
    }
}
