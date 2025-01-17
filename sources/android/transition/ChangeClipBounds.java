package android.transition;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class ChangeClipBounds extends Transition {
    private static final String PROPNAME_BOUNDS = "android:clipBounds:bounds";
    private static final String PROPNAME_CLIP = "android:clipBounds:clip";
    private static final String TAG = "ChangeTransform";
    private static final String[] sTransitionProperties = {PROPNAME_CLIP};

    public ChangeClipBounds() {
    }

    public ChangeClipBounds(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    private void captureValues(TransitionValues values) {
        View view = values.view;
        if (view.getVisibility() != 8) {
            Rect clip = view.getClipBounds();
            values.values.put(PROPNAME_CLIP, clip);
            if (clip == null) {
                values.values.put(PROPNAME_BOUNDS, new Rect(0, 0, view.getWidth(), view.getHeight()));
            }
        }
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v7, resolved type: android.graphics.Rect} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v7, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: android.graphics.Rect} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.animation.Animator createAnimator(android.view.ViewGroup r10, android.transition.TransitionValues r11, android.transition.TransitionValues r12) {
        /*
            r9 = this;
            r0 = 0
            if (r11 == 0) goto L_0x0087
            if (r12 == 0) goto L_0x0087
            java.util.Map<java.lang.String, java.lang.Object> r1 = r11.values
            java.lang.String r2 = "android:clipBounds:clip"
            boolean r1 = r1.containsKey(r2)
            if (r1 == 0) goto L_0x0087
            java.util.Map<java.lang.String, java.lang.Object> r1 = r12.values
            java.lang.String r2 = "android:clipBounds:clip"
            boolean r1 = r1.containsKey(r2)
            if (r1 != 0) goto L_0x001a
            goto L_0x0087
        L_0x001a:
            java.util.Map<java.lang.String, java.lang.Object> r1 = r11.values
            java.lang.String r2 = "android:clipBounds:clip"
            java.lang.Object r1 = r1.get(r2)
            android.graphics.Rect r1 = (android.graphics.Rect) r1
            java.util.Map<java.lang.String, java.lang.Object> r2 = r12.values
            java.lang.String r3 = "android:clipBounds:clip"
            java.lang.Object r2 = r2.get(r3)
            android.graphics.Rect r2 = (android.graphics.Rect) r2
            r3 = 0
            r4 = 1
            if (r2 != 0) goto L_0x0034
            r5 = r4
            goto L_0x0035
        L_0x0034:
            r5 = r3
        L_0x0035:
            if (r1 != 0) goto L_0x003a
            if (r2 != 0) goto L_0x003a
            return r0
        L_0x003a:
            if (r1 != 0) goto L_0x0048
            java.util.Map<java.lang.String, java.lang.Object> r6 = r11.values
            java.lang.String r7 = "android:clipBounds:bounds"
            java.lang.Object r6 = r6.get(r7)
            r1 = r6
            android.graphics.Rect r1 = (android.graphics.Rect) r1
            goto L_0x0055
        L_0x0048:
            if (r2 != 0) goto L_0x0055
            java.util.Map<java.lang.String, java.lang.Object> r6 = r12.values
            java.lang.String r7 = "android:clipBounds:bounds"
            java.lang.Object r6 = r6.get(r7)
            r2 = r6
            android.graphics.Rect r2 = (android.graphics.Rect) r2
        L_0x0055:
            boolean r6 = r1.equals(r2)
            if (r6 == 0) goto L_0x005c
            return r0
        L_0x005c:
            android.view.View r0 = r12.view
            r0.setClipBounds(r1)
            android.animation.RectEvaluator r0 = new android.animation.RectEvaluator
            android.graphics.Rect r6 = new android.graphics.Rect
            r6.<init>()
            r0.<init>(r6)
            android.view.View r6 = r12.view
            java.lang.String r7 = "clipBounds"
            r8 = 2
            java.lang.Object[] r8 = new java.lang.Object[r8]
            r8[r3] = r1
            r8[r4] = r2
            android.animation.ObjectAnimator r3 = android.animation.ObjectAnimator.ofObject((java.lang.Object) r6, (java.lang.String) r7, (android.animation.TypeEvaluator) r0, (java.lang.Object[]) r8)
            if (r5 == 0) goto L_0x0086
            android.view.View r4 = r12.view
            android.transition.ChangeClipBounds$1 r6 = new android.transition.ChangeClipBounds$1
            r6.<init>(r4)
            r3.addListener(r6)
        L_0x0086:
            return r3
        L_0x0087:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.transition.ChangeClipBounds.createAnimator(android.view.ViewGroup, android.transition.TransitionValues, android.transition.TransitionValues):android.animation.Animator");
    }
}
