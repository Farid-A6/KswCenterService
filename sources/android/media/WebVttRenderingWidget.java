package android.media;

import android.content.Context;
import android.graphics.Paint;
import android.media.SubtitleTrack;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.CaptioningManager;
import android.widget.LinearLayout;
import com.android.internal.widget.SubtitleView;
import java.util.ArrayList;
import java.util.Vector;

/* compiled from: WebVttRenderer */
class WebVttRenderingWidget extends ViewGroup implements SubtitleTrack.RenderingWidget {
    private static final boolean DEBUG = false;
    private static final int DEBUG_CUE_BACKGROUND = -2130771968;
    private static final int DEBUG_REGION_BACKGROUND = -2147483393;
    private static final CaptioningManager.CaptionStyle DEFAULT_CAPTION_STYLE = CaptioningManager.CaptionStyle.DEFAULT;
    private static final float LINE_HEIGHT_RATIO = 0.0533f;
    /* access modifiers changed from: private */
    public CaptioningManager.CaptionStyle mCaptionStyle;
    private final CaptioningManager.CaptioningChangeListener mCaptioningListener;
    private final ArrayMap<TextTrackCue, CueLayout> mCueBoxes;
    /* access modifiers changed from: private */
    public float mFontSize;
    private boolean mHasChangeListener;
    private SubtitleTrack.RenderingWidget.OnChangedListener mListener;
    private final CaptioningManager mManager;
    private final ArrayMap<TextTrackRegion, RegionLayout> mRegionBoxes;

    public WebVttRenderingWidget(Context context) {
        this(context, (AttributeSet) null);
    }

    public WebVttRenderingWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebVttRenderingWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public WebVttRenderingWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mRegionBoxes = new ArrayMap<>();
        this.mCueBoxes = new ArrayMap<>();
        this.mCaptioningListener = new CaptioningManager.CaptioningChangeListener() {
            public void onFontScaleChanged(float fontScale) {
                WebVttRenderingWidget.this.setCaptionStyle(WebVttRenderingWidget.this.mCaptionStyle, ((float) WebVttRenderingWidget.this.getHeight()) * fontScale * WebVttRenderingWidget.LINE_HEIGHT_RATIO);
            }

            public void onUserStyleChanged(CaptioningManager.CaptionStyle userStyle) {
                WebVttRenderingWidget.this.setCaptionStyle(userStyle, WebVttRenderingWidget.this.mFontSize);
            }
        };
        setLayerType(1, (Paint) null);
        this.mManager = (CaptioningManager) context.getSystemService(Context.CAPTIONING_SERVICE);
        this.mCaptionStyle = this.mManager.getUserStyle();
        this.mFontSize = this.mManager.getFontScale() * ((float) getHeight()) * LINE_HEIGHT_RATIO;
    }

    public void setSize(int width, int height) {
        measure(View.MeasureSpec.makeMeasureSpec(width, 1073741824), View.MeasureSpec.makeMeasureSpec(height, 1073741824));
        layout(0, 0, width, height);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        manageChangeListener();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        manageChangeListener();
    }

    public void setOnChangedListener(SubtitleTrack.RenderingWidget.OnChangedListener listener) {
        this.mListener = listener;
    }

    public void setVisible(boolean visible) {
        if (visible) {
            setVisibility(0);
        } else {
            setVisibility(8);
        }
        manageChangeListener();
    }

    private void manageChangeListener() {
        boolean needsListener = isAttachedToWindow() && getVisibility() == 0;
        if (this.mHasChangeListener != needsListener) {
            this.mHasChangeListener = needsListener;
            if (needsListener) {
                this.mManager.addCaptioningChangeListener(this.mCaptioningListener);
                setCaptionStyle(this.mManager.getUserStyle(), this.mManager.getFontScale() * ((float) getHeight()) * LINE_HEIGHT_RATIO);
                return;
            }
            this.mManager.removeCaptioningChangeListener(this.mCaptioningListener);
        }
    }

    public void setActiveCues(Vector<SubtitleTrack.Cue> activeCues) {
        Context context = getContext();
        CaptioningManager.CaptionStyle captionStyle = this.mCaptionStyle;
        float fontSize = this.mFontSize;
        prepForPrune();
        int count = activeCues.size();
        for (int i = 0; i < count; i++) {
            TextTrackCue cue = (TextTrackCue) activeCues.get(i);
            TextTrackRegion region = cue.mRegion;
            if (region != null) {
                RegionLayout regionBox = this.mRegionBoxes.get(region);
                if (regionBox == null) {
                    regionBox = new RegionLayout(context, region, captionStyle, fontSize);
                    this.mRegionBoxes.put(region, regionBox);
                    addView((View) regionBox, -2, -2);
                }
                regionBox.put(cue);
            } else {
                CueLayout cueBox = this.mCueBoxes.get(cue);
                if (cueBox == null) {
                    cueBox = new CueLayout(context, cue, captionStyle, fontSize);
                    this.mCueBoxes.put(cue, cueBox);
                    addView((View) cueBox, -2, -2);
                }
                cueBox.update();
                cueBox.setOrder(i);
            }
        }
        prune();
        setSize(getWidth(), getHeight());
        if (this.mListener != null) {
            this.mListener.onChanged(this);
        }
    }

    /* access modifiers changed from: private */
    public void setCaptionStyle(CaptioningManager.CaptionStyle captionStyle, float fontSize) {
        CaptioningManager.CaptionStyle captionStyle2 = DEFAULT_CAPTION_STYLE.applyStyle(captionStyle);
        this.mCaptionStyle = captionStyle2;
        this.mFontSize = fontSize;
        int cueCount = this.mCueBoxes.size();
        for (int i = 0; i < cueCount; i++) {
            this.mCueBoxes.valueAt(i).setCaptionStyle(captionStyle2, fontSize);
        }
        int regionCount = this.mRegionBoxes.size();
        for (int i2 = 0; i2 < regionCount; i2++) {
            this.mRegionBoxes.valueAt(i2).setCaptionStyle(captionStyle2, fontSize);
        }
    }

    private void prune() {
        int i = 0;
        int regionCount = this.mRegionBoxes.size();
        int i2 = 0;
        while (i2 < regionCount) {
            RegionLayout regionBox = this.mRegionBoxes.valueAt(i2);
            if (regionBox.prune()) {
                removeView(regionBox);
                this.mRegionBoxes.removeAt(i2);
                regionCount--;
                i2--;
            }
            i2++;
        }
        int cueCount = this.mCueBoxes.size();
        while (i < cueCount) {
            CueLayout cueBox = this.mCueBoxes.valueAt(i);
            if (!cueBox.isActive()) {
                removeView(cueBox);
                this.mCueBoxes.removeAt(i);
                cueCount--;
                i--;
            }
            i++;
        }
    }

    private void prepForPrune() {
        int regionCount = this.mRegionBoxes.size();
        for (int i = 0; i < regionCount; i++) {
            this.mRegionBoxes.valueAt(i).prepForPrune();
        }
        int cueCount = this.mCueBoxes.size();
        for (int i2 = 0; i2 < cueCount; i2++) {
            this.mCueBoxes.valueAt(i2).prepForPrune();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int regionCount = this.mRegionBoxes.size();
        for (int i = 0; i < regionCount; i++) {
            this.mRegionBoxes.valueAt(i).measureForParent(widthMeasureSpec, heightMeasureSpec);
        }
        int cueCount = this.mCueBoxes.size();
        for (int i2 = 0; i2 < cueCount; i2++) {
            this.mCueBoxes.valueAt(i2).measureForParent(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int viewportWidth = r - l;
        int viewportHeight = b - t;
        setCaptionStyle(this.mCaptionStyle, this.mManager.getFontScale() * LINE_HEIGHT_RATIO * ((float) viewportHeight));
        int regionCount = this.mRegionBoxes.size();
        for (int i = 0; i < regionCount; i++) {
            layoutRegion(viewportWidth, viewportHeight, this.mRegionBoxes.valueAt(i));
        }
        int cueCount = this.mCueBoxes.size();
        for (int i2 = 0; i2 < cueCount; i2++) {
            layoutCue(viewportWidth, viewportHeight, this.mCueBoxes.valueAt(i2));
        }
    }

    private void layoutRegion(int viewportWidth, int viewportHeight, RegionLayout regionBox) {
        TextTrackRegion region = regionBox.getRegion();
        int regionHeight = regionBox.getMeasuredHeight();
        int regionWidth = regionBox.getMeasuredWidth();
        int left = (int) ((((float) (viewportWidth - regionWidth)) * region.mViewportAnchorPointX) / 100.0f);
        int top = (int) ((((float) (viewportHeight - regionHeight)) * region.mViewportAnchorPointY) / 100.0f);
        regionBox.layout(left, top, left + regionWidth, top + regionHeight);
    }

    private void layoutCue(int viewportWidth, int viewportHeight, CueLayout cueBox) {
        int xPosition;
        int top;
        CueLayout cueLayout = cueBox;
        TextTrackCue cue = cueBox.getCue();
        int direction = getLayoutDirection();
        int absAlignment = resolveCueAlignment(direction, cue.mAlignment);
        boolean cueSnapToLines = cue.mSnapToLines;
        int size = (cueBox.getMeasuredWidth() * 100) / viewportWidth;
        switch (absAlignment) {
            case 203:
                xPosition = cue.mTextPosition;
                break;
            case 204:
                xPosition = cue.mTextPosition - size;
                break;
            default:
                xPosition = cue.mTextPosition - (size / 2);
                break;
        }
        if (direction == 1) {
            xPosition = 100 - xPosition;
        }
        if (cueSnapToLines) {
            int paddingLeft = (getPaddingLeft() * 100) / viewportWidth;
            int paddingRight = (getPaddingRight() * 100) / viewportWidth;
            if (xPosition < paddingLeft && xPosition + size > paddingLeft) {
                xPosition += paddingLeft;
                size -= paddingLeft;
            }
            float rightEdge = (float) (100 - paddingRight);
            if (((float) xPosition) < rightEdge && ((float) (xPosition + size)) > rightEdge) {
                size -= paddingRight;
            }
        }
        int left = (xPosition * viewportWidth) / 100;
        int width = (size * viewportWidth) / 100;
        int yPosition = calculateLinePosition(cueLayout);
        int height = cueBox.getMeasuredHeight();
        if (yPosition < 0) {
            top = viewportHeight + (yPosition * height);
        } else {
            top = ((viewportHeight - height) * yPosition) / 100;
        }
        cueLayout.layout(left, top, left + width, top + height);
    }

    private int calculateLinePosition(CueLayout cueBox) {
        TextTrackCue cue = cueBox.getCue();
        Integer linePosition = cue.mLinePosition;
        boolean snapToLines = cue.mSnapToLines;
        boolean autoPosition = linePosition == null;
        if (!snapToLines && !autoPosition && (linePosition.intValue() < 0 || linePosition.intValue() > 100)) {
            return 100;
        }
        if (!autoPosition) {
            return linePosition.intValue();
        }
        if (!snapToLines) {
            return 100;
        }
        return -(cueBox.mOrder + 1);
    }

    /* access modifiers changed from: private */
    public static int resolveCueAlignment(int layoutDirection, int alignment) {
        switch (alignment) {
            case 201:
                if (layoutDirection == 0) {
                    return 203;
                }
                return 204;
            case 202:
                if (layoutDirection == 0) {
                    return 204;
                }
                return 203;
            default:
                return alignment;
        }
    }

    /* compiled from: WebVttRenderer */
    private static class RegionLayout extends LinearLayout {
        private CaptioningManager.CaptionStyle mCaptionStyle;
        private float mFontSize;
        private final TextTrackRegion mRegion;
        private final ArrayList<CueLayout> mRegionCueBoxes = new ArrayList<>();

        public RegionLayout(Context context, TextTrackRegion region, CaptioningManager.CaptionStyle captionStyle, float fontSize) {
            super(context);
            this.mRegion = region;
            this.mCaptionStyle = captionStyle;
            this.mFontSize = fontSize;
            setOrientation(1);
            setBackgroundColor(captionStyle.windowColor);
        }

        public void setCaptionStyle(CaptioningManager.CaptionStyle captionStyle, float fontSize) {
            this.mCaptionStyle = captionStyle;
            this.mFontSize = fontSize;
            int cueCount = this.mRegionCueBoxes.size();
            for (int i = 0; i < cueCount; i++) {
                this.mRegionCueBoxes.get(i).setCaptionStyle(captionStyle, fontSize);
            }
            setBackgroundColor(captionStyle.windowColor);
        }

        public void measureForParent(int widthMeasureSpec, int heightMeasureSpec) {
            TextTrackRegion region = this.mRegion;
            measure(View.MeasureSpec.makeMeasureSpec((((int) region.mWidth) * View.MeasureSpec.getSize(widthMeasureSpec)) / 100, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(heightMeasureSpec), Integer.MIN_VALUE));
        }

        public void prepForPrune() {
            int cueCount = this.mRegionCueBoxes.size();
            for (int i = 0; i < cueCount; i++) {
                this.mRegionCueBoxes.get(i).prepForPrune();
            }
        }

        public void put(TextTrackCue cue) {
            int cueCount = this.mRegionCueBoxes.size();
            for (int i = 0; i < cueCount; i++) {
                CueLayout cueBox = this.mRegionCueBoxes.get(i);
                if (cueBox.getCue() == cue) {
                    cueBox.update();
                    return;
                }
            }
            CueLayout cueBox2 = new CueLayout(getContext(), cue, this.mCaptionStyle, this.mFontSize);
            this.mRegionCueBoxes.add(cueBox2);
            addView((View) cueBox2, -2, -2);
            if (getChildCount() > this.mRegion.mLines) {
                removeViewAt(0);
            }
        }

        public boolean prune() {
            int cueCount = this.mRegionCueBoxes.size();
            int i = 0;
            while (i < cueCount) {
                CueLayout cueBox = this.mRegionCueBoxes.get(i);
                if (!cueBox.isActive()) {
                    this.mRegionCueBoxes.remove(i);
                    removeView(cueBox);
                    cueCount--;
                    i--;
                }
                i++;
            }
            return this.mRegionCueBoxes.isEmpty();
        }

        public TextTrackRegion getRegion() {
            return this.mRegion;
        }
    }

    /* compiled from: WebVttRenderer */
    private static class CueLayout extends LinearLayout {
        private boolean mActive;
        private CaptioningManager.CaptionStyle mCaptionStyle;
        public final TextTrackCue mCue;
        private float mFontSize;
        /* access modifiers changed from: private */
        public int mOrder;

        public CueLayout(Context context, TextTrackCue cue, CaptioningManager.CaptionStyle captionStyle, float fontSize) {
            super(context);
            this.mCue = cue;
            this.mCaptionStyle = captionStyle;
            this.mFontSize = fontSize;
            int i = 0;
            int i2 = 1;
            boolean horizontal = cue.mWritingDirection == 100;
            setOrientation(horizontal ? 1 : i);
            switch (cue.mAlignment) {
                case 200:
                    setGravity(!horizontal ? 16 : i2);
                    break;
                case 201:
                    setGravity(8388611);
                    break;
                case 202:
                    setGravity(8388613);
                    break;
                case 203:
                    setGravity(3);
                    break;
                case 204:
                    setGravity(5);
                    break;
            }
            update();
        }

        public void setCaptionStyle(CaptioningManager.CaptionStyle style, float fontSize) {
            this.mCaptionStyle = style;
            this.mFontSize = fontSize;
            int n = getChildCount();
            for (int i = 0; i < n; i++) {
                View child = getChildAt(i);
                if (child instanceof SpanLayout) {
                    ((SpanLayout) child).setCaptionStyle(style, fontSize);
                }
            }
        }

        public void prepForPrune() {
            this.mActive = false;
        }

        public void update() {
            Layout.Alignment alignment;
            this.mActive = true;
            removeAllViews();
            switch (WebVttRenderingWidget.resolveCueAlignment(getLayoutDirection(), this.mCue.mAlignment)) {
                case 203:
                    alignment = Layout.Alignment.ALIGN_LEFT;
                    break;
                case 204:
                    alignment = Layout.Alignment.ALIGN_RIGHT;
                    break;
                default:
                    alignment = Layout.Alignment.ALIGN_CENTER;
                    break;
            }
            CaptioningManager.CaptionStyle captionStyle = this.mCaptionStyle;
            float fontSize = this.mFontSize;
            for (TextTrackCueSpan[] spanLayout : this.mCue.mLines) {
                SpanLayout lineBox = new SpanLayout(getContext(), spanLayout);
                lineBox.setAlignment(alignment);
                lineBox.setCaptionStyle(captionStyle, fontSize);
                addView((View) lineBox, -2, -2);
            }
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        public void measureForParent(int widthMeasureSpec, int heightMeasureSpec) {
            int maximumSize;
            TextTrackCue cue = this.mCue;
            int specWidth = View.MeasureSpec.getSize(widthMeasureSpec);
            int specHeight = View.MeasureSpec.getSize(heightMeasureSpec);
            int absAlignment = WebVttRenderingWidget.resolveCueAlignment(getLayoutDirection(), cue.mAlignment);
            if (absAlignment != 200) {
                switch (absAlignment) {
                    case 203:
                        maximumSize = 100 - cue.mTextPosition;
                        break;
                    case 204:
                        maximumSize = cue.mTextPosition;
                        break;
                    default:
                        maximumSize = 0;
                        break;
                }
            } else if (cue.mTextPosition <= 50) {
                maximumSize = cue.mTextPosition * 2;
            } else {
                maximumSize = (100 - cue.mTextPosition) * 2;
            }
            measure(View.MeasureSpec.makeMeasureSpec((Math.min(cue.mSize, maximumSize) * specWidth) / 100, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(specHeight, Integer.MIN_VALUE));
        }

        public void setOrder(int order) {
            this.mOrder = order;
        }

        public boolean isActive() {
            return this.mActive;
        }

        public TextTrackCue getCue() {
            return this.mCue;
        }
    }

    /* compiled from: WebVttRenderer */
    private static class SpanLayout extends SubtitleView {
        private final SpannableStringBuilder mBuilder = new SpannableStringBuilder();
        private final TextTrackCueSpan[] mSpans;

        public SpanLayout(Context context, TextTrackCueSpan[] spans) {
            super(context);
            this.mSpans = spans;
            update();
        }

        public void update() {
            SpannableStringBuilder builder = this.mBuilder;
            TextTrackCueSpan[] spans = this.mSpans;
            builder.clear();
            builder.clearSpans();
            int spanCount = spans.length;
            for (int i = 0; i < spanCount; i++) {
                if (spans[i].mEnabled) {
                    builder.append((CharSequence) spans[i].mText);
                }
            }
            setText((CharSequence) builder);
        }

        public void setCaptionStyle(CaptioningManager.CaptionStyle captionStyle, float fontSize) {
            setBackgroundColor(captionStyle.backgroundColor);
            setForegroundColor(captionStyle.foregroundColor);
            setEdgeColor(captionStyle.edgeColor);
            setEdgeType(captionStyle.edgeType);
            setTypeface(captionStyle.getTypeface());
            setTextSize(fontSize);
        }
    }
}
