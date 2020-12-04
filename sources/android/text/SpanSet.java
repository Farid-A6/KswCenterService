package android.text;

import android.annotation.UnsupportedAppUsage;
import java.lang.reflect.Array;
import java.util.Arrays;

public class SpanSet<E> {
    private final Class<? extends E> classType;
    int numberOfSpans = 0;
    int[] spanEnds;
    int[] spanFlags;
    int[] spanStarts;
    @UnsupportedAppUsage
    E[] spans;

    SpanSet(Class<? extends E> type) {
        this.classType = type;
    }

    public void init(Spanned spanned, int start, int limit) {
        if (length > 0 && (this.spans == null || this.spans.length < length)) {
            this.spans = (Object[]) Array.newInstance(this.classType, length);
            this.spanStarts = new int[length];
            this.spanEnds = new int[length];
            this.spanFlags = new int[length];
        }
        int prevNumberOfSpans = this.numberOfSpans;
        this.numberOfSpans = 0;
        for (E span : spanned.getSpans(start, limit, this.classType)) {
            int spanStart = spanned.getSpanStart(span);
            int spanEnd = spanned.getSpanEnd(span);
            if (spanStart != spanEnd) {
                int spanFlag = spanned.getSpanFlags(span);
                this.spans[this.numberOfSpans] = span;
                this.spanStarts[this.numberOfSpans] = spanStart;
                this.spanEnds[this.numberOfSpans] = spanEnd;
                this.spanFlags[this.numberOfSpans] = spanFlag;
                this.numberOfSpans++;
            }
        }
        if (this.numberOfSpans < prevNumberOfSpans) {
            Arrays.fill(this.spans, this.numberOfSpans, prevNumberOfSpans, (Object) null);
        }
    }

    public boolean hasSpansIntersecting(int start, int end) {
        for (int i = 0; i < this.numberOfSpans; i++) {
            if (this.spanStarts[i] < end && this.spanEnds[i] > start) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public int getNextTransition(int start, int limit) {
        for (int i = 0; i < this.numberOfSpans; i++) {
            int spanStart = this.spanStarts[i];
            int spanEnd = this.spanEnds[i];
            if (spanStart > start && spanStart < limit) {
                limit = spanStart;
            }
            if (spanEnd > start && spanEnd < limit) {
                limit = spanEnd;
            }
        }
        return limit;
    }

    public void recycle() {
        if (this.spans != null) {
            Arrays.fill(this.spans, 0, this.numberOfSpans, (Object) null);
        }
    }
}
