package com.android.internal.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import com.android.internal.R;

public class EpicenterTranslateClipReveal extends Visibility {
    private static final String PROPNAME_BOUNDS = "android:epicenterReveal:bounds";
    private static final String PROPNAME_CLIP = "android:epicenterReveal:clip";
    private static final String PROPNAME_TRANSLATE_X = "android:epicenterReveal:translateX";
    private static final String PROPNAME_TRANSLATE_Y = "android:epicenterReveal:translateY";
    private static final String PROPNAME_TRANSLATE_Z = "android:epicenterReveal:translateZ";
    private static final String PROPNAME_Z = "android:epicenterReveal:z";
    private final TimeInterpolator mInterpolatorX;
    private final TimeInterpolator mInterpolatorY;
    private final TimeInterpolator mInterpolatorZ;

    public EpicenterTranslateClipReveal() {
        this.mInterpolatorX = null;
        this.mInterpolatorY = null;
        this.mInterpolatorZ = null;
    }

    public EpicenterTranslateClipReveal(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EpicenterTranslateClipReveal, 0, 0);
        int interpolatorX = a.getResourceId(0, 0);
        if (interpolatorX != 0) {
            this.mInterpolatorX = AnimationUtils.loadInterpolator(context, interpolatorX);
        } else {
            this.mInterpolatorX = TransitionConstants.LINEAR_OUT_SLOW_IN;
        }
        int interpolatorY = a.getResourceId(1, 0);
        if (interpolatorY != 0) {
            this.mInterpolatorY = AnimationUtils.loadInterpolator(context, interpolatorY);
        } else {
            this.mInterpolatorY = TransitionConstants.FAST_OUT_SLOW_IN;
        }
        int interpolatorZ = a.getResourceId(2, 0);
        if (interpolatorZ != 0) {
            this.mInterpolatorZ = AnimationUtils.loadInterpolator(context, interpolatorZ);
        } else {
            this.mInterpolatorZ = TransitionConstants.FAST_OUT_SLOW_IN;
        }
        a.recycle();
    }

    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues values) {
        View view = values.view;
        if (view.getVisibility() != 8) {
            values.values.put(PROPNAME_BOUNDS, new Rect(0, 0, view.getWidth(), view.getHeight()));
            values.values.put(PROPNAME_TRANSLATE_X, Float.valueOf(view.getTranslationX()));
            values.values.put(PROPNAME_TRANSLATE_Y, Float.valueOf(view.getTranslationY()));
            values.values.put(PROPNAME_TRANSLATE_Z, Float.valueOf(view.getTranslationZ()));
            values.values.put(PROPNAME_Z, Float.valueOf(view.getZ()));
            values.values.put(PROPNAME_CLIP, view.getClipBounds());
        }
    }

    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        View view2 = view;
        TransitionValues transitionValues = endValues;
        if (transitionValues == null) {
            return null;
        }
        Rect endBounds = (Rect) transitionValues.values.get(PROPNAME_BOUNDS);
        Rect startBounds = getEpicenterOrCenter(endBounds);
        float startX = (float) (startBounds.centerX() - endBounds.centerX());
        float startY = (float) (startBounds.centerY() - endBounds.centerY());
        float startZ = 0.0f - ((Float) transitionValues.values.get(PROPNAME_Z)).floatValue();
        view2.setTranslationX(startX);
        view2.setTranslationY(startY);
        view2.setTranslationZ(startZ);
        float endX = ((Float) transitionValues.values.get(PROPNAME_TRANSLATE_X)).floatValue();
        float endY = ((Float) transitionValues.values.get(PROPNAME_TRANSLATE_Y)).floatValue();
        float endZ = ((Float) transitionValues.values.get(PROPNAME_TRANSLATE_Z)).floatValue();
        Rect endClip = getBestRect(transitionValues);
        Rect startClip = getEpicenterOrCenter(endClip);
        view2.setClipBounds(startClip);
        State startStateX = new State(startClip.left, startClip.right, startX);
        State endStateX = new State(endClip.left, endClip.right, endX);
        float f = endX;
        State startStateY = new State(startClip.top, startClip.bottom, startY);
        Rect rect = startClip;
        State endStateY = new State(endClip.top, endClip.bottom, endY);
        TimeInterpolator timeInterpolator = this.mInterpolatorX;
        float f2 = startX;
        TimeInterpolator timeInterpolator2 = this.mInterpolatorY;
        Rect rect2 = endClip;
        float f3 = startZ;
        float f4 = endY;
        TimeInterpolator timeInterpolator3 = timeInterpolator;
        float f5 = startZ;
        TimeInterpolator timeInterpolator4 = timeInterpolator3;
        float f6 = startY;
        return createRectAnimator(view, startStateX, startStateY, f3, endStateX, endStateY, endZ, endValues, timeInterpolator4, timeInterpolator2, this.mInterpolatorZ);
    }

    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        TransitionValues transitionValues = startValues;
        TransitionValues transitionValues2 = endValues;
        if (transitionValues == null) {
            return null;
        }
        Rect startBounds = (Rect) transitionValues2.values.get(PROPNAME_BOUNDS);
        Rect endBounds = getEpicenterOrCenter(startBounds);
        float endX = (float) (endBounds.centerX() - startBounds.centerX());
        float endY = (float) (endBounds.centerY() - startBounds.centerY());
        float endZ = 0.0f - ((Float) transitionValues.values.get(PROPNAME_Z)).floatValue();
        float startX = ((Float) transitionValues2.values.get(PROPNAME_TRANSLATE_X)).floatValue();
        float startY = ((Float) transitionValues2.values.get(PROPNAME_TRANSLATE_Y)).floatValue();
        float startZ = ((Float) transitionValues2.values.get(PROPNAME_TRANSLATE_Z)).floatValue();
        Rect startClip = getBestRect(transitionValues);
        Rect endClip = getEpicenterOrCenter(startClip);
        view.setClipBounds(startClip);
        State startStateX = new State(startClip.left, startClip.right, startX);
        State endStateX = new State(endClip.left, endClip.right, endX);
        State startStateY = new State(startClip.top, startClip.bottom, startY);
        State endStateY = new State(endClip.top, endClip.bottom, endY);
        TimeInterpolator timeInterpolator = this.mInterpolatorX;
        float f = endX;
        TimeInterpolator timeInterpolator2 = this.mInterpolatorY;
        Rect rect = endClip;
        Rect rect2 = startClip;
        float f2 = startY;
        float f3 = startX;
        TimeInterpolator timeInterpolator3 = timeInterpolator;
        float f4 = endY;
        return createRectAnimator(view, startStateX, startStateY, startZ, endStateX, endStateY, endZ, endValues, timeInterpolator3, timeInterpolator2, this.mInterpolatorZ);
    }

    private Rect getEpicenterOrCenter(Rect bestRect) {
        Rect epicenter = getEpicenter();
        if (epicenter != null) {
            return epicenter;
        }
        int centerX = bestRect.centerX();
        int centerY = bestRect.centerY();
        return new Rect(centerX, centerY, centerX, centerY);
    }

    private Rect getBestRect(TransitionValues values) {
        Rect clipRect = (Rect) values.values.get(PROPNAME_CLIP);
        if (clipRect == null) {
            return (Rect) values.values.get(PROPNAME_BOUNDS);
        }
        return clipRect;
    }

    private static Animator createRectAnimator(View view, State startX, State startY, float startZ, State endX, State endY, float endZ, TransitionValues endValues, TimeInterpolator interpolatorX, TimeInterpolator interpolatorY, TimeInterpolator interpolatorZ) {
        final View view2 = view;
        TimeInterpolator timeInterpolator = interpolatorX;
        TimeInterpolator timeInterpolator2 = interpolatorY;
        TimeInterpolator timeInterpolator3 = interpolatorZ;
        StateEvaluator evaluator = new StateEvaluator();
        ObjectAnimator animZ = ObjectAnimator.ofFloat(view2, View.TRANSLATION_Z, startZ, endZ);
        if (timeInterpolator3 != null) {
            animZ.setInterpolator(timeInterpolator3);
        }
        ObjectAnimator animX = ObjectAnimator.ofObject(view2, new StateProperty(StateProperty.TARGET_X), evaluator, (V[]) new State[]{startX, endX});
        if (timeInterpolator != null) {
            animX.setInterpolator(timeInterpolator);
        }
        ObjectAnimator animY = ObjectAnimator.ofObject(view2, new StateProperty('y'), evaluator, (V[]) new State[]{startY, endY});
        if (timeInterpolator2 != null) {
            animY.setInterpolator(timeInterpolator2);
        }
        final Rect terminalClip = (Rect) endValues.values.get(PROPNAME_CLIP);
        AnimatorListenerAdapter animatorListener = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                View.this.setClipBounds(terminalClip);
            }
        };
        Rect rect = terminalClip;
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(animX, animY, animZ);
        animSet.addListener(animatorListener);
        return animSet;
    }

    private static class State {
        int lower;
        float trans;
        int upper;

        public State() {
        }

        public State(int lower2, int upper2, float trans2) {
            this.lower = lower2;
            this.upper = upper2;
            this.trans = trans2;
        }
    }

    private static class StateEvaluator implements TypeEvaluator<State> {
        private final State mTemp;

        private StateEvaluator() {
            this.mTemp = new State();
        }

        public State evaluate(float fraction, State startValue, State endValue) {
            this.mTemp.upper = startValue.upper + ((int) (((float) (endValue.upper - startValue.upper)) * fraction));
            this.mTemp.lower = startValue.lower + ((int) (((float) (endValue.lower - startValue.lower)) * fraction));
            this.mTemp.trans = startValue.trans + ((float) ((int) ((endValue.trans - startValue.trans) * fraction)));
            return this.mTemp;
        }
    }

    private static class StateProperty extends Property<View, State> {
        public static final char TARGET_X = 'x';
        public static final char TARGET_Y = 'y';
        private final int mTargetDimension;
        private final Rect mTempRect = new Rect();
        private final State mTempState = new State();

        public StateProperty(char targetDimension) {
            super(State.class, "state_" + targetDimension);
            this.mTargetDimension = targetDimension;
        }

        public State get(View object) {
            Rect tempRect = this.mTempRect;
            if (!object.getClipBounds(tempRect)) {
                tempRect.setEmpty();
            }
            State tempState = this.mTempState;
            if (this.mTargetDimension == 120) {
                tempState.trans = object.getTranslationX();
                tempState.lower = tempRect.left + ((int) tempState.trans);
                tempState.upper = tempRect.right + ((int) tempState.trans);
            } else {
                tempState.trans = object.getTranslationY();
                tempState.lower = tempRect.top + ((int) tempState.trans);
                tempState.upper = tempRect.bottom + ((int) tempState.trans);
            }
            return tempState;
        }

        public void set(View object, State value) {
            Rect tempRect = this.mTempRect;
            if (object.getClipBounds(tempRect)) {
                if (this.mTargetDimension == 120) {
                    tempRect.left = value.lower - ((int) value.trans);
                    tempRect.right = value.upper - ((int) value.trans);
                } else {
                    tempRect.top = value.lower - ((int) value.trans);
                    tempRect.bottom = value.upper - ((int) value.trans);
                }
                object.setClipBounds(tempRect);
            }
            if (this.mTargetDimension == 120) {
                object.setTranslationX(value.trans);
            } else {
                object.setTranslationY(value.trans);
            }
        }
    }
}
