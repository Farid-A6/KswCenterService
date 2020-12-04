package android.text.style;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public abstract class CharacterStyle {
    public abstract void updateDrawState(TextPaint textPaint);

    public static CharacterStyle wrap(CharacterStyle cs) {
        if (cs instanceof MetricAffectingSpan) {
            return new MetricAffectingSpan.Passthrough((MetricAffectingSpan) cs);
        }
        return new Passthrough(cs);
    }

    public CharacterStyle getUnderlying() {
        return this;
    }

    private static class Passthrough extends CharacterStyle {
        private CharacterStyle mStyle;

        public Passthrough(CharacterStyle cs) {
            this.mStyle = cs;
        }

        public void updateDrawState(TextPaint tp) {
            this.mStyle.updateDrawState(tp);
        }

        public CharacterStyle getUnderlying() {
            return this.mStyle.getUnderlying();
        }
    }
}
