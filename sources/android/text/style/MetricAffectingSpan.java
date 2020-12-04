package android.text.style;

import android.text.TextPaint;

public abstract class MetricAffectingSpan extends CharacterStyle implements UpdateLayout {
    public abstract void updateMeasureState(TextPaint textPaint);

    public MetricAffectingSpan getUnderlying() {
        return this;
    }

    static class Passthrough extends MetricAffectingSpan {
        private MetricAffectingSpan mStyle;

        Passthrough(MetricAffectingSpan cs) {
            this.mStyle = cs;
        }

        public void updateDrawState(TextPaint tp) {
            this.mStyle.updateDrawState(tp);
        }

        public void updateMeasureState(TextPaint tp) {
            this.mStyle.updateMeasureState(tp);
        }

        public MetricAffectingSpan getUnderlying() {
            return this.mStyle.getUnderlying();
        }
    }
}
