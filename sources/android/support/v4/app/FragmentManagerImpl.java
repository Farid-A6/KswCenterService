package android.support.v4.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.SettingsStringUtil;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.ArraySet;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: FragmentManager */
final class FragmentManagerImpl extends FragmentManager implements LayoutInflater.Factory2 {
    static final Interpolator ACCELERATE_CUBIC = new AccelerateInterpolator(1.5f);
    static final Interpolator ACCELERATE_QUINT = new AccelerateInterpolator(2.5f);
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
    static final Interpolator DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    static Field sAnimationListenerField = null;
    SparseArray<Fragment> mActive;
    final ArrayList<Fragment> mAdded = new ArrayList<>();
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState = 0;
    boolean mDestroyed;
    Runnable mExecCommit = new Runnable() {
        public void run() {
            FragmentManagerImpl.this.execPendingActions();
        }
    };
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    private final CopyOnWriteArrayList<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks = new CopyOnWriteArrayList<>();
    boolean mNeedMenuInvalidate;
    int mNextFragmentIndex = 0;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<OpGenerator> mPendingActions;
    ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    FragmentManagerNonConfig mSavedNonConfig;
    SparseArray<Parcelable> mStateArray = null;
    Bundle mStateBundle = null;
    boolean mStateSaved;
    ArrayList<Fragment> mTmpAddedFragments;
    ArrayList<Boolean> mTmpIsPop;
    ArrayList<BackStackRecord> mTmpRecords;

    /* compiled from: FragmentManager */
    interface OpGenerator {
        boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2);
    }

    FragmentManagerImpl() {
    }

    static boolean modifiesAlpha(AnimationOrAnimator anim) {
        if (anim.animation instanceof AlphaAnimation) {
            return true;
        }
        if (!(anim.animation instanceof AnimationSet)) {
            return modifiesAlpha(anim.animator);
        }
        List<Animation> anims = ((AnimationSet) anim.animation).getAnimations();
        for (int i = 0; i < anims.size(); i++) {
            if (anims.get(i) instanceof AlphaAnimation) {
                return true;
            }
        }
        return false;
    }

    static boolean modifiesAlpha(Animator anim) {
        if (anim == null) {
            return false;
        }
        if (anim instanceof ValueAnimator) {
            PropertyValuesHolder[] values = ((ValueAnimator) anim).getValues();
            for (PropertyValuesHolder propertyName : values) {
                if ("alpha".equals(propertyName.getPropertyName())) {
                    return true;
                }
            }
        } else if (anim instanceof AnimatorSet) {
            List<Animator> animList = ((AnimatorSet) anim).getChildAnimations();
            for (int i = 0; i < animList.size(); i++) {
                if (modifiesAlpha(animList.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean shouldRunOnHWLayer(View v, AnimationOrAnimator anim) {
        if (v == null || anim == null || Build.VERSION.SDK_INT < 19 || v.getLayerType() != 0 || !ViewCompat.hasOverlappingRendering(v) || !modifiesAlpha(anim)) {
            return false;
        }
        return true;
    }

    private void throwException(RuntimeException ex) {
        Log.e(TAG, ex.getMessage());
        Log.e(TAG, "Activity state:");
        PrintWriter pw = new PrintWriter(new LogWriter(TAG));
        if (this.mHost != null) {
            try {
                this.mHost.onDump("  ", (FileDescriptor) null, pw, new String[0]);
            } catch (Exception e) {
                Log.e(TAG, "Failed dumping state", e);
            }
        } else {
            try {
                dump("  ", (FileDescriptor) null, pw, new String[0]);
            } catch (Exception e2) {
                Log.e(TAG, "Failed dumping state", e2);
            }
        }
        throw ex;
    }

    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    public boolean executePendingTransactions() {
        boolean updates = execPendingActions();
        forcePostponedTransactions();
        return updates;
    }

    public void popBackStack() {
        enqueueAction(new PopBackStackState((String) null, -1, 0), false);
    }

    public boolean popBackStackImmediate() {
        checkStateLoss();
        return popBackStackImmediate((String) null, -1, 0);
    }

    public void popBackStack(String name, int flags) {
        enqueueAction(new PopBackStackState(name, -1, flags), false);
    }

    public boolean popBackStackImmediate(String name, int flags) {
        checkStateLoss();
        return popBackStackImmediate(name, -1, flags);
    }

    public void popBackStack(int id, int flags) {
        if (id >= 0) {
            enqueueAction(new PopBackStackState((String) null, id, flags), false);
            return;
        }
        throw new IllegalArgumentException("Bad id: " + id);
    }

    public boolean popBackStackImmediate(int id, int flags) {
        checkStateLoss();
        execPendingActions();
        if (id >= 0) {
            return popBackStackImmediate((String) null, id, flags);
        }
        throw new IllegalArgumentException("Bad id: " + id);
    }

    private boolean popBackStackImmediate(String name, int id, int flags) {
        FragmentManager childManager;
        execPendingActions();
        ensureExecReady(true);
        if (this.mPrimaryNav != null && id < 0 && name == null && (childManager = this.mPrimaryNav.peekChildFragmentManager()) != null && childManager.popBackStackImmediate()) {
            return true;
        }
        boolean executePop = popBackStackState(this.mTmpRecords, this.mTmpIsPop, name, id, flags);
        if (executePop) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                cleanupExec();
            }
        }
        doPendingDeferredStart();
        burpActive();
        return executePop;
    }

    public int getBackStackEntryCount() {
        if (this.mBackStack != null) {
            return this.mBackStack.size();
        }
        return 0;
    }

    public FragmentManager.BackStackEntry getBackStackEntryAt(int index) {
        return this.mBackStack.get(index);
    }

    public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList<>();
        }
        this.mBackStackChangeListeners.add(listener);
    }

    public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners != null) {
            this.mBackStackChangeListeners.remove(listener);
        }
    }

    public void putFragment(Bundle bundle, String key, Fragment fragment) {
        if (fragment.mIndex < 0) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        bundle.putInt(key, fragment.mIndex);
    }

    public Fragment getFragment(Bundle bundle, String key) {
        int index = bundle.getInt(key, -1);
        if (index == -1) {
            return null;
        }
        Fragment f = this.mActive.get(index);
        if (f == null) {
            throwException(new IllegalStateException("Fragment no longer exists for key " + key + ": index " + index));
        }
        return f;
    }

    public List<Fragment> getFragments() {
        List<Fragment> list;
        if (this.mAdded.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        synchronized (this.mAdded) {
            list = (List) this.mAdded.clone();
        }
        return list;
    }

    /* access modifiers changed from: package-private */
    public List<Fragment> getActiveFragments() {
        if (this.mActive == null) {
            return null;
        }
        int count = this.mActive.size();
        ArrayList<Fragment> fragments = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            fragments.add(this.mActive.valueAt(i));
        }
        return fragments;
    }

    /* access modifiers changed from: package-private */
    public int getActiveFragmentCount() {
        if (this.mActive == null) {
            return 0;
        }
        return this.mActive.size();
    }

    public Fragment.SavedState saveFragmentInstanceState(Fragment fragment) {
        Bundle result;
        if (fragment.mIndex < 0) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        if (fragment.mState <= 0 || (result = saveFragmentBasicState(fragment)) == null) {
            return null;
        }
        return new Fragment.SavedState(result);
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        if (this.mParent != null) {
            DebugUtils.buildShortClassTag(this.mParent, sb);
        } else {
            DebugUtils.buildShortClassTag(this.mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        int N;
        int N2;
        int N3;
        int N4;
        int N5;
        String innerPrefix = prefix + "    ";
        if (this.mActive != null && (N5 = this.mActive.size()) > 0) {
            writer.print(prefix);
            writer.print("Active Fragments in ");
            writer.print(Integer.toHexString(System.identityHashCode(this)));
            writer.println(SettingsStringUtil.DELIMITER);
            for (int i = 0; i < N5; i++) {
                Fragment f = this.mActive.valueAt(i);
                writer.print(prefix);
                writer.print("  #");
                writer.print(i);
                writer.print(": ");
                writer.println(f);
                if (f != null) {
                    f.dump(innerPrefix, fd, writer, args);
                }
            }
        }
        int N6 = this.mAdded.size();
        if (N6 > 0) {
            writer.print(prefix);
            writer.println("Added Fragments:");
            for (int i2 = 0; i2 < N6; i2++) {
                writer.print(prefix);
                writer.print("  #");
                writer.print(i2);
                writer.print(": ");
                writer.println(this.mAdded.get(i2).toString());
            }
        }
        if (this.mCreatedMenus != null && (N4 = this.mCreatedMenus.size()) > 0) {
            writer.print(prefix);
            writer.println("Fragments Created Menus:");
            for (int i3 = 0; i3 < N4; i3++) {
                writer.print(prefix);
                writer.print("  #");
                writer.print(i3);
                writer.print(": ");
                writer.println(this.mCreatedMenus.get(i3).toString());
            }
        }
        if (this.mBackStack != null && (N3 = this.mBackStack.size()) > 0) {
            writer.print(prefix);
            writer.println("Back Stack:");
            for (int i4 = 0; i4 < N3; i4++) {
                BackStackRecord bs = this.mBackStack.get(i4);
                writer.print(prefix);
                writer.print("  #");
                writer.print(i4);
                writer.print(": ");
                writer.println(bs.toString());
                bs.dump(innerPrefix, fd, writer, args);
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null && (N2 = this.mBackStackIndices.size()) > 0) {
                writer.print(prefix);
                writer.println("Back Stack Indices:");
                for (int i5 = 0; i5 < N2; i5++) {
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i5);
                    writer.print(": ");
                    writer.println(this.mBackStackIndices.get(i5));
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                writer.print(prefix);
                writer.print("mAvailBackStackIndices: ");
                writer.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
        }
        if (this.mPendingActions != null && (N = this.mPendingActions.size()) > 0) {
            writer.print(prefix);
            writer.println("Pending Actions:");
            for (int i6 = 0; i6 < N; i6++) {
                writer.print(prefix);
                writer.print("  #");
                writer.print(i6);
                writer.print(": ");
                writer.println(this.mPendingActions.get(i6));
            }
        }
        writer.print(prefix);
        writer.println("FragmentManager misc state:");
        writer.print(prefix);
        writer.print("  mHost=");
        writer.println(this.mHost);
        writer.print(prefix);
        writer.print("  mContainer=");
        writer.println(this.mContainer);
        if (this.mParent != null) {
            writer.print(prefix);
            writer.print("  mParent=");
            writer.println(this.mParent);
        }
        writer.print(prefix);
        writer.print("  mCurState=");
        writer.print(this.mCurState);
        writer.print(" mStateSaved=");
        writer.print(this.mStateSaved);
        writer.print(" mDestroyed=");
        writer.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            writer.print(prefix);
            writer.print("  mNeedMenuInvalidate=");
            writer.println(this.mNeedMenuInvalidate);
        }
        if (this.mNoTransactionsBecause != null) {
            writer.print(prefix);
            writer.print("  mNoTransactionsBecause=");
            writer.println(this.mNoTransactionsBecause);
        }
    }

    static AnimationOrAnimator makeOpenCloseAnimation(Context context, float startScale, float endScale, float startAlpha, float endAlpha) {
        AnimationSet set = new AnimationSet(false);
        ScaleAnimation scale = new ScaleAnimation(startScale, endScale, startScale, endScale, 1, 0.5f, 1, 0.5f);
        scale.setInterpolator(DECELERATE_QUINT);
        scale.setDuration(220);
        set.addAnimation(scale);
        AlphaAnimation alpha = new AlphaAnimation(startAlpha, endAlpha);
        alpha.setInterpolator(DECELERATE_CUBIC);
        alpha.setDuration(220);
        set.addAnimation(alpha);
        return new AnimationOrAnimator((Animation) set);
    }

    static AnimationOrAnimator makeFadeAnimation(Context context, float start, float end) {
        AlphaAnimation anim = new AlphaAnimation(start, end);
        anim.setInterpolator(DECELERATE_CUBIC);
        anim.setDuration(220);
        return new AnimationOrAnimator((Animation) anim);
    }

    /* access modifiers changed from: package-private */
    public AnimationOrAnimator loadAnimation(Fragment fragment, int transit, boolean enter, int transitionStyle) {
        int styleIndex;
        int nextAnim = fragment.getNextAnim();
        Animation animation = fragment.onCreateAnimation(transit, enter, nextAnim);
        if (animation != null) {
            return new AnimationOrAnimator(animation);
        }
        Animator animator = fragment.onCreateAnimator(transit, enter, nextAnim);
        if (animator != null) {
            return new AnimationOrAnimator(animator);
        }
        if (nextAnim != 0) {
            boolean isAnim = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(nextAnim));
            boolean successfulLoad = false;
            if (isAnim) {
                try {
                    Animation animation2 = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                    if (animation2 != null) {
                        return new AnimationOrAnimator(animation2);
                    }
                    successfulLoad = true;
                } catch (Resources.NotFoundException e) {
                    throw e;
                } catch (RuntimeException e2) {
                }
            }
            if (!successfulLoad) {
                try {
                    Animator animator2 = AnimatorInflater.loadAnimator(this.mHost.getContext(), nextAnim);
                    if (animator2 != null) {
                        return new AnimationOrAnimator(animator2);
                    }
                } catch (RuntimeException e3) {
                    if (!isAnim) {
                        Animation animation3 = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                        if (animation3 != null) {
                            return new AnimationOrAnimator(animation3);
                        }
                    } else {
                        throw e3;
                    }
                }
            }
        }
        if (transit == 0 || (styleIndex = transitToStyleIndex(transit, enter)) < 0) {
            return null;
        }
        switch (styleIndex) {
            case 1:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.125f, 1.0f, 0.0f, 1.0f);
            case 2:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 0.975f, 1.0f, 0.0f);
            case 3:
                return makeOpenCloseAnimation(this.mHost.getContext(), 0.975f, 1.0f, 0.0f, 1.0f);
            case 4:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 1.075f, 1.0f, 0.0f);
            case 5:
                return makeFadeAnimation(this.mHost.getContext(), 0.0f, 1.0f);
            case 6:
                return makeFadeAnimation(this.mHost.getContext(), 1.0f, 0.0f);
            default:
                if (transitionStyle == 0 && this.mHost.onHasWindowAnimations()) {
                    transitionStyle = this.mHost.onGetWindowAnimations();
                }
                return transitionStyle == 0 ? null : null;
        }
    }

    public void performPendingDeferredStart(Fragment f) {
        if (!f.mDeferStart) {
            return;
        }
        if (this.mExecutingActions) {
            this.mHavePendingDeferredStart = true;
            return;
        }
        f.mDeferStart = false;
        moveToState(f, this.mCurState, 0, 0, false);
    }

    private static void setHWLayerAnimListenerIfAlpha(View v, AnimationOrAnimator anim) {
        if (v != null && anim != null && shouldRunOnHWLayer(v, anim)) {
            if (anim.animator != null) {
                anim.animator.addListener(new AnimatorOnHWLayerIfNeededListener(v));
                return;
            }
            Animation.AnimationListener originalListener = getAnimationListener(anim.animation);
            v.setLayerType(2, (Paint) null);
            anim.animation.setAnimationListener(new AnimateOnHWLayerIfNeededListener(v, originalListener));
        }
    }

    private static Animation.AnimationListener getAnimationListener(Animation animation) {
        try {
            if (sAnimationListenerField == null) {
                sAnimationListenerField = Animation.class.getDeclaredField("mListener");
                sAnimationListenerField.setAccessible(true);
            }
            return (Animation.AnimationListener) sAnimationListenerField.get(animation);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "No field with the name mListener is found in Animation class", e);
            return null;
        } catch (IllegalAccessException e2) {
            Log.e(TAG, "Cannot access Animation's mListener field", e2);
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isStateAtLeast(int state) {
        return this.mCurState >= state;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:143:0x0323, code lost:
        if (r0 >= 4) goto L_0x0346;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:145:0x0327, code lost:
        if (DEBUG == false) goto L_0x0340;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x0329, code lost:
        android.util.Log.v(TAG, "movefrom STARTED: " + r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x0340, code lost:
        r16.performStop();
        dispatchOnFragmentStopped(r8, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:148:0x0346, code lost:
        if (r0 >= 3) goto L_0x0366;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:150:0x034a, code lost:
        if (DEBUG == false) goto L_0x0363;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x034c, code lost:
        android.util.Log.v(TAG, "movefrom STOPPED: " + r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:152:0x0363, code lost:
        r16.performReallyStop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:153:0x0366, code lost:
        if (r0 >= 2) goto L_0x03f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:155:0x036a, code lost:
        if (DEBUG == false) goto L_0x0383;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:156:0x036c, code lost:
        android.util.Log.v(TAG, "movefrom ACTIVITY_CREATED: " + r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:158:0x0385, code lost:
        if (r8.mView == null) goto L_0x0396;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:160:0x038d, code lost:
        if (r7.mHost.onShouldSaveFragmentState(r8) == false) goto L_0x0396;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x0391, code lost:
        if (r8.mSavedViewState != null) goto L_0x0396;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:163:0x0393, code lost:
        saveFragmentViewState(r16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:164:0x0396, code lost:
        r16.performDestroyView();
        dispatchOnFragmentViewDestroyed(r8, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:165:0x039e, code lost:
        if (r8.mView == null) goto L_0x03e4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x03a2, code lost:
        if (r8.mContainer == null) goto L_0x03e4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:168:0x03a4, code lost:
        r8.mView.clearAnimation();
        r8.mContainer.endViewTransition(r8.mView);
        r1 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:169:0x03b4, code lost:
        if (r7.mCurState <= 0) goto L_0x03d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:171:0x03b8, code lost:
        if (r7.mDestroyed != false) goto L_0x03d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:173:0x03c0, code lost:
        if (r8.mView.getVisibility() != 0) goto L_0x03d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:0x03c6, code lost:
        if (r8.mPostponedAlpha < 0.0f) goto L_0x03d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:0x03c8, code lost:
        r1 = loadAnimation(r8, r18, false, r19);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:177:0x03d1, code lost:
        r2 = r18;
        r4 = r19;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:178:0x03d5, code lost:
        r8.mPostponedAlpha = 0.0f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:179:0x03d7, code lost:
        if (r1 == null) goto L_0x03dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:180:0x03d9, code lost:
        animateRemoveFragment(r8, r1, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:181:0x03dc, code lost:
        r8.mContainer.removeView(r8.mView);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:182:0x03e4, code lost:
        r2 = r18;
        r4 = r19;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:183:0x03e8, code lost:
        r8.mContainer = null;
        r8.mView = null;
        r8.mInnerView = null;
        r8.mInLayout = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:185:0x03f5, code lost:
        if (r0 >= 1) goto L_0x046c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:187:0x03f9, code lost:
        if (r7.mDestroyed == false) goto L_0x041c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:189:0x03ff, code lost:
        if (r16.getAnimatingAway() == null) goto L_0x040c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:190:0x0401, code lost:
        r1 = r16.getAnimatingAway();
        r8.setAnimatingAway((android.view.View) null);
        r1.clearAnimation();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:192:0x0410, code lost:
        if (r16.getAnimator() == null) goto L_0x041c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:193:0x0412, code lost:
        r1 = r16.getAnimator();
        r8.setAnimator((android.animation.Animator) null);
        r1.cancel();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:195:0x0420, code lost:
        if (r16.getAnimatingAway() != null) goto L_0x0468;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:197:0x0426, code lost:
        if (r16.getAnimator() == null) goto L_0x0429;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:199:0x042b, code lost:
        if (DEBUG == false) goto L_0x0444;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:200:0x042d, code lost:
        android.util.Log.v(TAG, "movefrom CREATED: " + r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:202:0x0446, code lost:
        if (r8.mRetaining != false) goto L_0x044f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:203:0x0448, code lost:
        r16.performDestroy();
        dispatchOnFragmentDestroyed(r8, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:204:0x044f, code lost:
        r8.mState = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:205:0x0451, code lost:
        r16.performDetach();
        dispatchOnFragmentDetached(r8, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:206:0x0457, code lost:
        if (r20 != false) goto L_0x046c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:208:0x045b, code lost:
        if (r8.mRetaining != false) goto L_0x0461;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:209:0x045d, code lost:
        makeInactive(r16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:210:0x0461, code lost:
        r8.mHost = null;
        r8.mParentFragment = null;
        r8.mFragmentManager = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:211:0x0468, code lost:
        r8.setStateAfterAnimating(r0);
        r0 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x019f, code lost:
        r1 = r0;
        ensureInflatedFragmentView(r16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x01a3, code lost:
        if (r1 <= 1) goto L_0x02a0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x01a7, code lost:
        if (DEBUG == false) goto L_0x01c0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x01a9, code lost:
        android.util.Log.v(TAG, "moveto ACTIVITY_CREATED: " + r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x01c2, code lost:
        if (r8.mFromLayout != false) goto L_0x028b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x01c4, code lost:
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x01c7, code lost:
        if (r8.mContainerId == 0) goto L_0x023b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x01cc, code lost:
        if (r8.mContainerId != -1) goto L_0x01ec;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x01ce, code lost:
        throwException(new java.lang.IllegalArgumentException("Cannot create fragment " + r8 + " for a container view with no id"));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x01ec, code lost:
        r2 = (android.view.ViewGroup) r7.mContainer.onFindViewById(r8.mContainerId);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x01f6, code lost:
        if (r2 != null) goto L_0x023a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x01fa, code lost:
        if (r8.mRestored != false) goto L_0x023a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:?, code lost:
        r0 = r16.getResources().getResourceName(r8.mContainerId);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x0208, code lost:
        r0 = "unknown";
     */
    /* JADX WARNING: Removed duplicated region for block: B:214:0x0470  */
    /* JADX WARNING: Removed duplicated region for block: B:216:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void moveToState(android.support.v4.app.Fragment r16, int r17, int r18, int r19, boolean r20) {
        /*
            r15 = this;
            r7 = r15
            r8 = r16
            boolean r0 = r8.mAdded
            r9 = 1
            if (r0 == 0) goto L_0x0010
            boolean r0 = r8.mDetached
            if (r0 == 0) goto L_0x000d
            goto L_0x0010
        L_0x000d:
            r0 = r17
            goto L_0x0015
        L_0x0010:
            r0 = r17
            if (r0 <= r9) goto L_0x0015
            r0 = 1
        L_0x0015:
            boolean r1 = r8.mRemoving
            if (r1 == 0) goto L_0x002b
            int r1 = r8.mState
            if (r0 <= r1) goto L_0x002b
            int r1 = r8.mState
            if (r1 != 0) goto L_0x0029
            boolean r1 = r16.isInBackStack()
            if (r1 == 0) goto L_0x0029
            r0 = 1
            goto L_0x002b
        L_0x0029:
            int r0 = r8.mState
        L_0x002b:
            boolean r1 = r8.mDeferStart
            r10 = 4
            r11 = 3
            if (r1 == 0) goto L_0x0038
            int r1 = r8.mState
            if (r1 >= r10) goto L_0x0038
            if (r0 <= r11) goto L_0x0038
            r0 = 3
        L_0x0038:
            int r1 = r8.mState
            r12 = 2
            r13 = 0
            r14 = 0
            if (r1 > r0) goto L_0x02f5
            boolean r1 = r8.mFromLayout
            if (r1 == 0) goto L_0x0048
            boolean r1 = r8.mInLayout
            if (r1 != 0) goto L_0x0048
            return
        L_0x0048:
            android.view.View r1 = r16.getAnimatingAway()
            if (r1 != 0) goto L_0x0054
            android.animation.Animator r1 = r16.getAnimator()
            if (r1 == 0) goto L_0x0067
        L_0x0054:
            r8.setAnimatingAway(r13)
            r8.setAnimator(r13)
            int r3 = r16.getStateAfterAnimating()
            r4 = 0
            r5 = 0
            r6 = 1
            r1 = r15
            r2 = r16
            r1.moveToState(r2, r3, r4, r5, r6)
        L_0x0067:
            int r1 = r8.mState
            switch(r1) {
                case 0: goto L_0x006e;
                case 1: goto L_0x019f;
                case 2: goto L_0x02a1;
                case 3: goto L_0x02a5;
                case 4: goto L_0x02c8;
                default: goto L_0x006c;
            }
        L_0x006c:
            goto L_0x02ef
        L_0x006e:
            if (r0 <= 0) goto L_0x019f
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x008b
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "moveto CREATED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x008b:
            android.os.Bundle r1 = r8.mSavedFragmentState
            if (r1 == 0) goto L_0x00d3
            android.os.Bundle r1 = r8.mSavedFragmentState
            android.support.v4.app.FragmentHostCallback r2 = r7.mHost
            android.content.Context r2 = r2.getContext()
            java.lang.ClassLoader r2 = r2.getClassLoader()
            r1.setClassLoader(r2)
            android.os.Bundle r1 = r8.mSavedFragmentState
            java.lang.String r2 = "android:view_state"
            android.util.SparseArray r1 = r1.getSparseParcelableArray(r2)
            r8.mSavedViewState = r1
            android.os.Bundle r1 = r8.mSavedFragmentState
            java.lang.String r2 = "android:target_state"
            android.support.v4.app.Fragment r1 = r15.getFragment(r1, r2)
            r8.mTarget = r1
            android.support.v4.app.Fragment r1 = r8.mTarget
            if (r1 == 0) goto L_0x00c0
            android.os.Bundle r1 = r8.mSavedFragmentState
            java.lang.String r2 = "android:target_req_state"
            int r1 = r1.getInt(r2, r14)
            r8.mTargetRequestCode = r1
        L_0x00c0:
            android.os.Bundle r1 = r8.mSavedFragmentState
            java.lang.String r2 = "android:user_visible_hint"
            boolean r1 = r1.getBoolean(r2, r9)
            r8.mUserVisibleHint = r1
            boolean r1 = r8.mUserVisibleHint
            if (r1 != 0) goto L_0x00d3
            r8.mDeferStart = r9
            if (r0 <= r11) goto L_0x00d3
            r0 = 3
        L_0x00d3:
            android.support.v4.app.FragmentHostCallback r1 = r7.mHost
            r8.mHost = r1
            android.support.v4.app.Fragment r1 = r7.mParent
            r8.mParentFragment = r1
            android.support.v4.app.Fragment r1 = r7.mParent
            if (r1 == 0) goto L_0x00e4
            android.support.v4.app.Fragment r1 = r7.mParent
            android.support.v4.app.FragmentManagerImpl r1 = r1.mChildFragmentManager
            goto L_0x00ea
        L_0x00e4:
            android.support.v4.app.FragmentHostCallback r1 = r7.mHost
            android.support.v4.app.FragmentManagerImpl r1 = r1.getFragmentManagerImpl()
        L_0x00ea:
            r8.mFragmentManager = r1
            android.support.v4.app.Fragment r1 = r8.mTarget
            if (r1 == 0) goto L_0x0135
            android.util.SparseArray<android.support.v4.app.Fragment> r1 = r7.mActive
            android.support.v4.app.Fragment r2 = r8.mTarget
            int r2 = r2.mIndex
            java.lang.Object r1 = r1.get(r2)
            android.support.v4.app.Fragment r2 = r8.mTarget
            if (r1 != r2) goto L_0x010f
            android.support.v4.app.Fragment r1 = r8.mTarget
            int r1 = r1.mState
            if (r1 >= r9) goto L_0x0135
            android.support.v4.app.Fragment r2 = r8.mTarget
            r3 = 1
            r4 = 0
            r5 = 0
            r6 = 1
            r1 = r15
            r1.moveToState(r2, r3, r4, r5, r6)
            goto L_0x0135
        L_0x010f:
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Fragment "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r3 = " declared target fragment "
            r2.append(r3)
            android.support.v4.app.Fragment r3 = r8.mTarget
            r2.append(r3)
            java.lang.String r3 = " that does not belong to this FragmentManager!"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L_0x0135:
            android.support.v4.app.FragmentHostCallback r1 = r7.mHost
            android.content.Context r1 = r1.getContext()
            r15.dispatchOnFragmentPreAttached(r8, r1, r14)
            r8.mCalled = r14
            android.support.v4.app.FragmentHostCallback r1 = r7.mHost
            android.content.Context r1 = r1.getContext()
            r8.onAttach((android.content.Context) r1)
            boolean r1 = r8.mCalled
            if (r1 == 0) goto L_0x0183
            android.support.v4.app.Fragment r1 = r8.mParentFragment
            if (r1 != 0) goto L_0x0157
            android.support.v4.app.FragmentHostCallback r1 = r7.mHost
            r1.onAttachFragment(r8)
            goto L_0x015c
        L_0x0157:
            android.support.v4.app.Fragment r1 = r8.mParentFragment
            r1.onAttachFragment(r8)
        L_0x015c:
            android.support.v4.app.FragmentHostCallback r1 = r7.mHost
            android.content.Context r1 = r1.getContext()
            r15.dispatchOnFragmentAttached(r8, r1, r14)
            boolean r1 = r8.mIsCreated
            if (r1 != 0) goto L_0x0179
            android.os.Bundle r1 = r8.mSavedFragmentState
            r15.dispatchOnFragmentPreCreated(r8, r1, r14)
            android.os.Bundle r1 = r8.mSavedFragmentState
            r8.performCreate(r1)
            android.os.Bundle r1 = r8.mSavedFragmentState
            r15.dispatchOnFragmentCreated(r8, r1, r14)
            goto L_0x0180
        L_0x0179:
            android.os.Bundle r1 = r8.mSavedFragmentState
            r8.restoreChildFragmentState(r1)
            r8.mState = r9
        L_0x0180:
            r8.mRetaining = r14
            goto L_0x019f
        L_0x0183:
            android.support.v4.app.SuperNotCalledException r1 = new android.support.v4.app.SuperNotCalledException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Fragment "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r3 = " did not call through to super.onAttach()"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L_0x019f:
            r1 = r0
            r15.ensureInflatedFragmentView(r16)
            if (r1 <= r9) goto L_0x02a0
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x01c0
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "moveto ACTIVITY_CREATED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r0, r2)
        L_0x01c0:
            boolean r0 = r8.mFromLayout
            if (r0 != 0) goto L_0x028b
            r0 = 0
            int r2 = r8.mContainerId
            if (r2 == 0) goto L_0x023b
            int r2 = r8.mContainerId
            r3 = -1
            if (r2 != r3) goto L_0x01ec
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Cannot create fragment "
            r3.append(r4)
            r3.append(r8)
            java.lang.String r4 = " for a container view with no id"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            r15.throwException(r2)
        L_0x01ec:
            android.support.v4.app.FragmentContainer r2 = r7.mContainer
            int r3 = r8.mContainerId
            android.view.View r2 = r2.onFindViewById(r3)
            android.view.ViewGroup r2 = (android.view.ViewGroup) r2
            if (r2 != 0) goto L_0x023a
            boolean r0 = r8.mRestored
            if (r0 != 0) goto L_0x023a
            android.content.res.Resources r0 = r16.getResources()     // Catch:{ NotFoundException -> 0x0207 }
            int r3 = r8.mContainerId     // Catch:{ NotFoundException -> 0x0207 }
            java.lang.String r0 = r0.getResourceName(r3)     // Catch:{ NotFoundException -> 0x0207 }
            goto L_0x020b
        L_0x0207:
            r0 = move-exception
            java.lang.String r0 = "unknown"
        L_0x020b:
            java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "No view found for id 0x"
            r4.append(r5)
            int r5 = r8.mContainerId
            java.lang.String r5 = java.lang.Integer.toHexString(r5)
            r4.append(r5)
            java.lang.String r5 = " ("
            r4.append(r5)
            r4.append(r0)
            java.lang.String r5 = ") for fragment "
            r4.append(r5)
            r4.append(r8)
            java.lang.String r4 = r4.toString()
            r3.<init>(r4)
            r15.throwException(r3)
        L_0x023a:
            r0 = r2
        L_0x023b:
            r8.mContainer = r0
            android.os.Bundle r2 = r8.mSavedFragmentState
            android.view.LayoutInflater r2 = r8.performGetLayoutInflater(r2)
            android.os.Bundle r3 = r8.mSavedFragmentState
            android.view.View r2 = r8.performCreateView(r2, r0, r3)
            r8.mView = r2
            android.view.View r2 = r8.mView
            if (r2 == 0) goto L_0x0289
            android.view.View r2 = r8.mView
            r8.mInnerView = r2
            android.view.View r2 = r8.mView
            r2.setSaveFromParentEnabled(r14)
            if (r0 == 0) goto L_0x025f
            android.view.View r2 = r8.mView
            r0.addView(r2)
        L_0x025f:
            boolean r2 = r8.mHidden
            if (r2 == 0) goto L_0x026a
            android.view.View r2 = r8.mView
            r3 = 8
            r2.setVisibility(r3)
        L_0x026a:
            android.view.View r2 = r8.mView
            android.os.Bundle r3 = r8.mSavedFragmentState
            r8.onViewCreated(r2, r3)
            android.view.View r2 = r8.mView
            android.os.Bundle r3 = r8.mSavedFragmentState
            r15.dispatchOnFragmentViewCreated(r8, r2, r3, r14)
            android.view.View r2 = r8.mView
            int r2 = r2.getVisibility()
            if (r2 != 0) goto L_0x0285
            android.view.ViewGroup r2 = r8.mContainer
            if (r2 == 0) goto L_0x0285
            goto L_0x0286
        L_0x0285:
            r9 = r14
        L_0x0286:
            r8.mIsNewlyAdded = r9
            goto L_0x028b
        L_0x0289:
            r8.mInnerView = r13
        L_0x028b:
            android.os.Bundle r0 = r8.mSavedFragmentState
            r8.performActivityCreated(r0)
            android.os.Bundle r0 = r8.mSavedFragmentState
            r15.dispatchOnFragmentActivityCreated(r8, r0, r14)
            android.view.View r0 = r8.mView
            if (r0 == 0) goto L_0x029e
            android.os.Bundle r0 = r8.mSavedFragmentState
            r8.restoreViewState(r0)
        L_0x029e:
            r8.mSavedFragmentState = r13
        L_0x02a0:
            r0 = r1
        L_0x02a1:
            if (r0 <= r12) goto L_0x02a5
            r8.mState = r11
        L_0x02a5:
            if (r0 <= r11) goto L_0x02c8
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x02c2
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "moveto STARTED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x02c2:
            r16.performStart()
            r15.dispatchOnFragmentStarted(r8, r14)
        L_0x02c8:
            if (r0 <= r10) goto L_0x02ef
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x02e5
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "moveto RESUMED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x02e5:
            r16.performResume()
            r15.dispatchOnFragmentResumed(r8, r14)
            r8.mSavedFragmentState = r13
            r8.mSavedViewState = r13
        L_0x02ef:
            r2 = r18
            r4 = r19
            goto L_0x046c
        L_0x02f5:
            int r1 = r8.mState
            if (r1 <= r0) goto L_0x02ef
            int r1 = r8.mState
            switch(r1) {
                case 1: goto L_0x03f1;
                case 2: goto L_0x0366;
                case 3: goto L_0x0346;
                case 4: goto L_0x0323;
                case 5: goto L_0x02ff;
                default: goto L_0x02fe;
            }
        L_0x02fe:
            goto L_0x02ef
        L_0x02ff:
            r1 = 5
            if (r0 >= r1) goto L_0x0323
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x031d
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "movefrom RESUMED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x031d:
            r16.performPause()
            r15.dispatchOnFragmentPaused(r8, r14)
        L_0x0323:
            if (r0 >= r10) goto L_0x0346
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0340
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "movefrom STARTED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x0340:
            r16.performStop()
            r15.dispatchOnFragmentStopped(r8, r14)
        L_0x0346:
            if (r0 >= r11) goto L_0x0366
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0363
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "movefrom STOPPED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x0363:
            r16.performReallyStop()
        L_0x0366:
            if (r0 >= r12) goto L_0x03f1
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0383
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "movefrom ACTIVITY_CREATED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x0383:
            android.view.View r1 = r8.mView
            if (r1 == 0) goto L_0x0396
            android.support.v4.app.FragmentHostCallback r1 = r7.mHost
            boolean r1 = r1.onShouldSaveFragmentState(r8)
            if (r1 == 0) goto L_0x0396
            android.util.SparseArray<android.os.Parcelable> r1 = r8.mSavedViewState
            if (r1 != 0) goto L_0x0396
            r15.saveFragmentViewState(r16)
        L_0x0396:
            r16.performDestroyView()
            r15.dispatchOnFragmentViewDestroyed(r8, r14)
            android.view.View r1 = r8.mView
            if (r1 == 0) goto L_0x03e4
            android.view.ViewGroup r1 = r8.mContainer
            if (r1 == 0) goto L_0x03e4
            android.view.View r1 = r8.mView
            r1.clearAnimation()
            android.view.ViewGroup r1 = r8.mContainer
            android.view.View r2 = r8.mView
            r1.endViewTransition(r2)
            r1 = 0
            int r2 = r7.mCurState
            r3 = 0
            if (r2 <= 0) goto L_0x03d1
            boolean r2 = r7.mDestroyed
            if (r2 != 0) goto L_0x03d1
            android.view.View r2 = r8.mView
            int r2 = r2.getVisibility()
            if (r2 != 0) goto L_0x03d1
            float r2 = r8.mPostponedAlpha
            int r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r2 < 0) goto L_0x03d1
            r2 = r18
            r4 = r19
            android.support.v4.app.FragmentManagerImpl$AnimationOrAnimator r1 = r15.loadAnimation(r8, r2, r14, r4)
            goto L_0x03d5
        L_0x03d1:
            r2 = r18
            r4 = r19
        L_0x03d5:
            r8.mPostponedAlpha = r3
            if (r1 == 0) goto L_0x03dc
            r15.animateRemoveFragment(r8, r1, r0)
        L_0x03dc:
            android.view.ViewGroup r3 = r8.mContainer
            android.view.View r5 = r8.mView
            r3.removeView(r5)
            goto L_0x03e8
        L_0x03e4:
            r2 = r18
            r4 = r19
        L_0x03e8:
            r8.mContainer = r13
            r8.mView = r13
            r8.mInnerView = r13
            r8.mInLayout = r14
            goto L_0x03f5
        L_0x03f1:
            r2 = r18
            r4 = r19
        L_0x03f5:
            if (r0 >= r9) goto L_0x046c
            boolean r1 = r7.mDestroyed
            if (r1 == 0) goto L_0x041c
            android.view.View r1 = r16.getAnimatingAway()
            if (r1 == 0) goto L_0x040c
            android.view.View r1 = r16.getAnimatingAway()
            r8.setAnimatingAway(r13)
            r1.clearAnimation()
            goto L_0x041c
        L_0x040c:
            android.animation.Animator r1 = r16.getAnimator()
            if (r1 == 0) goto L_0x041c
            android.animation.Animator r1 = r16.getAnimator()
            r8.setAnimator(r13)
            r1.cancel()
        L_0x041c:
            android.view.View r1 = r16.getAnimatingAway()
            if (r1 != 0) goto L_0x0468
            android.animation.Animator r1 = r16.getAnimator()
            if (r1 == 0) goto L_0x0429
            goto L_0x0468
        L_0x0429:
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0444
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "movefrom CREATED: "
            r3.append(r5)
            r3.append(r8)
            java.lang.String r3 = r3.toString()
            android.util.Log.v(r1, r3)
        L_0x0444:
            boolean r1 = r8.mRetaining
            if (r1 != 0) goto L_0x044f
            r16.performDestroy()
            r15.dispatchOnFragmentDestroyed(r8, r14)
            goto L_0x0451
        L_0x044f:
            r8.mState = r14
        L_0x0451:
            r16.performDetach()
            r15.dispatchOnFragmentDetached(r8, r14)
            if (r20 != 0) goto L_0x046c
            boolean r3 = r8.mRetaining
            if (r3 != 0) goto L_0x0461
            r15.makeInactive(r16)
            goto L_0x046c
        L_0x0461:
            r8.mHost = r13
            r8.mParentFragment = r13
            r8.mFragmentManager = r13
            goto L_0x046c
        L_0x0468:
            r8.setStateAfterAnimating(r0)
            r0 = 1
        L_0x046c:
            int r3 = r8.mState
            if (r3 == r0) goto L_0x04a0
            java.lang.String r3 = "FragmentManager"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "moveToState: Fragment state for "
            r5.append(r6)
            r5.append(r8)
            java.lang.String r6 = " not updated inline; "
            r5.append(r6)
            java.lang.String r6 = "expected state "
            r5.append(r6)
            r5.append(r0)
            java.lang.String r6 = " found "
            r5.append(r6)
            int r6 = r8.mState
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            android.util.Log.w((java.lang.String) r3, (java.lang.String) r5)
            r8.mState = r0
        L_0x04a0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.moveToState(android.support.v4.app.Fragment, int, int, int, boolean):void");
    }

    private void animateRemoveFragment(@NonNull final Fragment fragment, @NonNull AnimationOrAnimator anim, int newState) {
        final View viewToAnimate = fragment.mView;
        fragment.setStateAfterAnimating(newState);
        if (anim.animation != null) {
            Animation animation = anim.animation;
            fragment.setAnimatingAway(fragment.mView);
            animation.setAnimationListener(new AnimationListenerWrapper(getAnimationListener(animation)) {
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    if (fragment.getAnimatingAway() != null) {
                        fragment.setAnimatingAway((View) null);
                        FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                    }
                }
            });
            setHWLayerAnimListenerIfAlpha(viewToAnimate, anim);
            fragment.mView.startAnimation(animation);
            return;
        }
        Animator animator = anim.animator;
        fragment.setAnimator(anim.animator);
        final ViewGroup container = fragment.mContainer;
        if (container != null) {
            container.startViewTransition(viewToAnimate);
        }
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                if (container != null) {
                    container.endViewTransition(viewToAnimate);
                }
                if (fragment.getAnimator() != null) {
                    fragment.setAnimator((Animator) null);
                    FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                }
            }
        });
        animator.setTarget(fragment.mView);
        setHWLayerAnimListenerIfAlpha(fragment.mView, anim);
        animator.start();
    }

    /* access modifiers changed from: package-private */
    public void moveToState(Fragment f) {
        moveToState(f, this.mCurState, 0, 0, false);
    }

    /* access modifiers changed from: package-private */
    public void ensureInflatedFragmentView(Fragment f) {
        if (f.mFromLayout && !f.mPerformedCreateView) {
            f.mView = f.performCreateView(f.performGetLayoutInflater(f.mSavedFragmentState), (ViewGroup) null, f.mSavedFragmentState);
            if (f.mView != null) {
                f.mInnerView = f.mView;
                f.mView.setSaveFromParentEnabled(false);
                if (f.mHidden) {
                    f.mView.setVisibility(8);
                }
                f.onViewCreated(f.mView, f.mSavedFragmentState);
                dispatchOnFragmentViewCreated(f, f.mView, f.mSavedFragmentState, false);
                return;
            }
            f.mInnerView = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void completeShowHideFragment(final Fragment fragment) {
        if (fragment.mView != null) {
            AnimationOrAnimator anim = loadAnimation(fragment, fragment.getNextTransition(), !fragment.mHidden, fragment.getNextTransitionStyle());
            if (anim == null || anim.animator == null) {
                if (anim != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, anim);
                    fragment.mView.startAnimation(anim.animation);
                    anim.animation.start();
                }
                fragment.mView.setVisibility((!fragment.mHidden || fragment.isHideReplaced()) ? 0 : 8);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            } else {
                anim.animator.setTarget(fragment.mView);
                if (!fragment.mHidden) {
                    fragment.mView.setVisibility(0);
                } else if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                } else {
                    final ViewGroup container = fragment.mContainer;
                    final View animatingView = fragment.mView;
                    container.startViewTransition(animatingView);
                    anim.animator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            container.endViewTransition(animatingView);
                            animation.removeListener(this);
                            if (fragment.mView != null) {
                                fragment.mView.setVisibility(8);
                            }
                        }
                    });
                }
                setHWLayerAnimListenerIfAlpha(fragment.mView, anim);
                anim.animator.start();
            }
        }
        if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0033, code lost:
        r4 = r1.mView;
        r5 = r11.mContainer;
        r6 = r5.indexOfChild(r4);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void moveFragmentToExpectedState(android.support.v4.app.Fragment r11) {
        /*
            r10 = this;
            if (r11 != 0) goto L_0x0003
            return
        L_0x0003:
            int r0 = r10.mCurState
            boolean r1 = r11.mRemoving
            r2 = 0
            r3 = 1
            if (r1 == 0) goto L_0x001a
            boolean r1 = r11.isInBackStack()
            if (r1 == 0) goto L_0x0016
            int r0 = java.lang.Math.min(r0, r3)
            goto L_0x001a
        L_0x0016:
            int r0 = java.lang.Math.min(r0, r2)
        L_0x001a:
            int r7 = r11.getNextTransition()
            int r8 = r11.getNextTransitionStyle()
            r9 = 0
            r4 = r10
            r5 = r11
            r6 = r0
            r4.moveToState(r5, r6, r7, r8, r9)
            android.view.View r1 = r11.mView
            if (r1 == 0) goto L_0x0090
            android.support.v4.app.Fragment r1 = r10.findFragmentUnder(r11)
            if (r1 == 0) goto L_0x004b
            android.view.View r4 = r1.mView
            android.view.ViewGroup r5 = r11.mContainer
            int r6 = r5.indexOfChild(r4)
            android.view.View r7 = r11.mView
            int r7 = r5.indexOfChild(r7)
            if (r7 >= r6) goto L_0x004b
            r5.removeViewAt(r7)
            android.view.View r8 = r11.mView
            r5.addView((android.view.View) r8, (int) r6)
        L_0x004b:
            boolean r4 = r11.mIsNewlyAdded
            if (r4 == 0) goto L_0x0090
            android.view.ViewGroup r4 = r11.mContainer
            if (r4 == 0) goto L_0x0090
            float r4 = r11.mPostponedAlpha
            r5 = 0
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 <= 0) goto L_0x0061
            android.view.View r4 = r11.mView
            float r6 = r11.mPostponedAlpha
            r4.setAlpha(r6)
        L_0x0061:
            r11.mPostponedAlpha = r5
            r11.mIsNewlyAdded = r2
            int r2 = r11.getNextTransition()
            int r4 = r11.getNextTransitionStyle()
            android.support.v4.app.FragmentManagerImpl$AnimationOrAnimator r2 = r10.loadAnimation(r11, r2, r3, r4)
            if (r2 == 0) goto L_0x0090
            android.view.View r3 = r11.mView
            setHWLayerAnimListenerIfAlpha(r3, r2)
            android.view.animation.Animation r3 = r2.animation
            if (r3 == 0) goto L_0x0084
            android.view.View r3 = r11.mView
            android.view.animation.Animation r4 = r2.animation
            r3.startAnimation(r4)
            goto L_0x0090
        L_0x0084:
            android.animation.Animator r3 = r2.animator
            android.view.View r4 = r11.mView
            r3.setTarget(r4)
            android.animation.Animator r3 = r2.animator
            r3.start()
        L_0x0090:
            boolean r1 = r11.mHiddenChanged
            if (r1 == 0) goto L_0x0097
            r10.completeShowHideFragment(r11)
        L_0x0097:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.moveFragmentToExpectedState(android.support.v4.app.Fragment):void");
    }

    /* access modifiers changed from: package-private */
    public void moveToState(int newState, boolean always) {
        if (this.mHost == null && newState != 0) {
            throw new IllegalStateException("No activity");
        } else if (always || newState != this.mCurState) {
            this.mCurState = newState;
            if (this.mActive != null) {
                int numAdded = this.mAdded.size();
                boolean loadersRunning = false;
                for (int i = 0; i < numAdded; i++) {
                    Fragment f = this.mAdded.get(i);
                    moveFragmentToExpectedState(f);
                    if (f.mLoaderManager != null) {
                        loadersRunning |= f.mLoaderManager.hasRunningLoaders();
                    }
                }
                int numActive = this.mActive.size();
                boolean loadersRunning2 = loadersRunning;
                for (int i2 = 0; i2 < numActive; i2++) {
                    Fragment f2 = this.mActive.valueAt(i2);
                    if (f2 != null && ((f2.mRemoving || f2.mDetached) && !f2.mIsNewlyAdded)) {
                        moveFragmentToExpectedState(f2);
                        if (f2.mLoaderManager != null) {
                            loadersRunning2 |= f2.mLoaderManager.hasRunningLoaders();
                        }
                    }
                }
                if (!loadersRunning2) {
                    startPendingDeferredFragments();
                }
                if (this.mNeedMenuInvalidate && this.mHost != null && this.mCurState == 5) {
                    this.mHost.onSupportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = false;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void startPendingDeferredFragments() {
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); i++) {
                Fragment f = this.mActive.valueAt(i);
                if (f != null) {
                    performPendingDeferredStart(f);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void makeActive(Fragment f) {
        if (f.mIndex < 0) {
            int i = this.mNextFragmentIndex;
            this.mNextFragmentIndex = i + 1;
            f.setIndex(i, this.mParent);
            if (this.mActive == null) {
                this.mActive = new SparseArray<>();
            }
            this.mActive.put(f.mIndex, f);
            if (DEBUG) {
                Log.v(TAG, "Allocated fragment index " + f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void makeInactive(Fragment f) {
        if (f.mIndex >= 0) {
            if (DEBUG) {
                Log.v(TAG, "Freeing fragment index " + f);
            }
            this.mActive.put(f.mIndex, null);
            this.mHost.inactivateFragment(f.mWho);
            f.initState();
        }
    }

    public void addFragment(Fragment fragment, boolean moveToStateNow) {
        if (DEBUG) {
            Log.v(TAG, "add: " + fragment);
        }
        makeActive(fragment);
        if (fragment.mDetached) {
            return;
        }
        if (!this.mAdded.contains(fragment)) {
            synchronized (this.mAdded) {
                this.mAdded.add(fragment);
            }
            fragment.mAdded = true;
            fragment.mRemoving = false;
            if (fragment.mView == null) {
                fragment.mHiddenChanged = false;
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            if (moveToStateNow) {
                moveToState(fragment);
                return;
            }
            return;
        }
        throw new IllegalStateException("Fragment already added: " + fragment);
    }

    public void removeFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
        }
        boolean inactive = !fragment.isInBackStack();
        if (!fragment.mDetached || inactive) {
            synchronized (this.mAdded) {
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
        }
    }

    public void hideFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "hide: " + fragment);
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged = true ^ fragment.mHiddenChanged;
        }
    }

    public void showFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "show: " + fragment);
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged = !fragment.mHiddenChanged;
        }
    }

    public void detachFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "detach: " + fragment);
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (DEBUG) {
                    Log.v(TAG, "remove from detach: " + fragment);
                }
                synchronized (this.mAdded) {
                    this.mAdded.remove(fragment);
                }
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                fragment.mAdded = false;
            }
        }
    }

    public void attachFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "attach: " + fragment);
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (fragment.mAdded) {
                return;
            }
            if (!this.mAdded.contains(fragment)) {
                if (DEBUG) {
                    Log.v(TAG, "add from attach: " + fragment);
                }
                synchronized (this.mAdded) {
                    this.mAdded.add(fragment);
                }
                fragment.mAdded = true;
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                    return;
                }
                return;
            }
            throw new IllegalStateException("Fragment already added: " + fragment);
        }
    }

    public Fragment findFragmentById(int id) {
        for (int i = this.mAdded.size() - 1; i >= 0; i--) {
            Fragment f = this.mAdded.get(i);
            if (f != null && f.mFragmentId == id) {
                return f;
            }
        }
        if (this.mActive == null) {
            return null;
        }
        for (int i2 = this.mActive.size() - 1; i2 >= 0; i2--) {
            Fragment f2 = this.mActive.valueAt(i2);
            if (f2 != null && f2.mFragmentId == id) {
                return f2;
            }
        }
        return null;
    }

    public Fragment findFragmentByTag(String tag) {
        if (tag != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; i--) {
                Fragment f = this.mAdded.get(i);
                if (f != null && tag.equals(f.mTag)) {
                    return f;
                }
            }
        }
        if (this.mActive == null || tag == null) {
            return null;
        }
        for (int i2 = this.mActive.size() - 1; i2 >= 0; i2--) {
            Fragment f2 = this.mActive.valueAt(i2);
            if (f2 != null && tag.equals(f2.mTag)) {
                return f2;
            }
        }
        return null;
    }

    public Fragment findFragmentByWho(String who) {
        if (this.mActive == null || who == null) {
            return null;
        }
        for (int i = this.mActive.size() - 1; i >= 0; i--) {
            Fragment f = this.mActive.valueAt(i);
            if (f != null) {
                Fragment findFragmentByWho = f.findFragmentByWho(who);
                Fragment f2 = findFragmentByWho;
                if (findFragmentByWho != null) {
                    return f2;
                }
            }
        }
        return null;
    }

    private void checkStateLoss() {
        if (this.mStateSaved) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        } else if (this.mNoTransactionsBecause != null) {
            throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause);
        }
    }

    public boolean isStateSaved() {
        return this.mStateSaved;
    }

    public void enqueueAction(OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss) {
            checkStateLoss();
        }
        synchronized (this) {
            if (!this.mDestroyed) {
                if (this.mHost != null) {
                    if (this.mPendingActions == null) {
                        this.mPendingActions = new ArrayList<>();
                    }
                    this.mPendingActions.add(action);
                    scheduleCommit();
                    return;
                }
            }
            if (!allowStateLoss) {
                throw new IllegalStateException("Activity has been destroyed");
            }
        }
    }

    /* access modifiers changed from: private */
    public void scheduleCommit() {
        synchronized (this) {
            boolean pendingReady = false;
            boolean postponeReady = this.mPostponedTransactions != null && !this.mPostponedTransactions.isEmpty();
            if (this.mPendingActions != null && this.mPendingActions.size() == 1) {
                pendingReady = true;
            }
            if (postponeReady || pendingReady) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
            }
        }
    }

    public int allocBackStackIndex(BackStackRecord bse) {
        synchronized (this) {
            if (this.mAvailBackStackIndices != null) {
                if (this.mAvailBackStackIndices.size() > 0) {
                    int index = this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1).intValue();
                    if (DEBUG) {
                        Log.v(TAG, "Adding back stack index " + index + " with " + bse);
                    }
                    this.mBackStackIndices.set(index, bse);
                    return index;
                }
            }
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<>();
            }
            int index2 = this.mBackStackIndices.size();
            if (DEBUG) {
                Log.v(TAG, "Setting back stack index " + index2 + " to " + bse);
            }
            this.mBackStackIndices.add(bse);
            return index2;
        }
    }

    public void setBackStackIndex(int index, BackStackRecord bse) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<>();
            }
            int N = this.mBackStackIndices.size();
            if (index < N) {
                if (DEBUG) {
                    Log.v(TAG, "Setting back stack index " + index + " to " + bse);
                }
                this.mBackStackIndices.set(index, bse);
            } else {
                while (N < index) {
                    this.mBackStackIndices.add((Object) null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList<>();
                    }
                    if (DEBUG) {
                        Log.v(TAG, "Adding available back stack index " + N);
                    }
                    this.mAvailBackStackIndices.add(Integer.valueOf(N));
                    N++;
                }
                if (DEBUG) {
                    Log.v(TAG, "Adding back stack index " + index + " with " + bse);
                }
                this.mBackStackIndices.add(bse);
            }
        }
    }

    public void freeBackStackIndex(int index) {
        synchronized (this) {
            this.mBackStackIndices.set(index, (Object) null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList<>();
            }
            if (DEBUG) {
                Log.v(TAG, "Freeing back stack index " + index);
            }
            this.mAvailBackStackIndices.add(Integer.valueOf(index));
        }
    }

    private void ensureExecReady(boolean allowStateLoss) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        } else if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
            if (!allowStateLoss) {
                checkStateLoss();
            }
            if (this.mTmpRecords == null) {
                this.mTmpRecords = new ArrayList<>();
                this.mTmpIsPop = new ArrayList<>();
            }
            this.mExecutingActions = true;
            try {
                executePostponedTransaction((ArrayList<BackStackRecord>) null, (ArrayList<Boolean>) null);
            } finally {
                this.mExecutingActions = false;
            }
        } else {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
    }

    public void execSingleAction(OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss || (this.mHost != null && !this.mDestroyed)) {
            ensureExecReady(allowStateLoss);
            if (action.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
                this.mExecutingActions = true;
                try {
                    removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                } finally {
                    cleanupExec();
                }
            }
            doPendingDeferredStart();
            burpActive();
        }
    }

    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    /* JADX INFO: finally extract failed */
    public boolean execPendingActions() {
        ensureExecReady(true);
        boolean didSomething = false;
        while (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                cleanupExec();
                didSomething = true;
            } catch (Throwable th) {
                cleanupExec();
                throw th;
            }
        }
        doPendingDeferredStart();
        burpActive();
        return didSomething;
    }

    private void executePostponedTransaction(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
        int index;
        int numPostponed = this.mPostponedTransactions == null ? 0 : this.mPostponedTransactions.size();
        int i = 0;
        while (i < numPostponed) {
            StartEnterTransitionListener listener = this.mPostponedTransactions.get(i);
            if (records != null && !listener.mIsBack && (index = records.indexOf(listener.mRecord)) != -1 && isRecordPop.get(index).booleanValue()) {
                listener.cancelTransaction();
            } else if (listener.isReady() != 0 || (records != null && listener.mRecord.interactsWith(records, 0, records.size()))) {
                this.mPostponedTransactions.remove(i);
                i--;
                numPostponed--;
                if (records != null && !listener.mIsBack) {
                    int indexOf = records.indexOf(listener.mRecord);
                    int index2 = indexOf;
                    if (indexOf != -1 && isRecordPop.get(index2).booleanValue()) {
                        listener.cancelTransaction();
                    }
                }
                listener.completeTransaction();
            }
            i++;
        }
    }

    private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
        if (records != null && !records.isEmpty()) {
            if (isRecordPop == null || records.size() != isRecordPop.size()) {
                throw new IllegalStateException("Internal error with the back stack records");
            }
            executePostponedTransaction(records, isRecordPop);
            int numRecords = records.size();
            int startIndex = 0;
            int recordNum = 0;
            while (recordNum < numRecords) {
                if (!records.get(recordNum).mReorderingAllowed) {
                    if (startIndex != recordNum) {
                        executeOpsTogether(records, isRecordPop, startIndex, recordNum);
                    }
                    int reorderingEnd = recordNum + 1;
                    if (isRecordPop.get(recordNum).booleanValue()) {
                        while (reorderingEnd < numRecords && isRecordPop.get(reorderingEnd).booleanValue() && !records.get(reorderingEnd).mReorderingAllowed) {
                            reorderingEnd++;
                        }
                    }
                    executeOpsTogether(records, isRecordPop, recordNum, reorderingEnd);
                    startIndex = reorderingEnd;
                    recordNum = reorderingEnd - 1;
                }
                recordNum++;
            }
            if (startIndex != numRecords) {
                executeOpsTogether(records, isRecordPop, startIndex, numRecords);
            }
        }
    }

    private void executeOpsTogether(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        Fragment trackAddedFragmentsInPop;
        ArrayList<BackStackRecord> arrayList = records;
        ArrayList<Boolean> arrayList2 = isRecordPop;
        int i = startIndex;
        int i2 = endIndex;
        boolean allowReordering = arrayList.get(i).mReorderingAllowed;
        if (this.mTmpAddedFragments == null) {
            this.mTmpAddedFragments = new ArrayList<>();
        } else {
            this.mTmpAddedFragments.clear();
        }
        this.mTmpAddedFragments.addAll(this.mAdded);
        boolean addToBackStack = false;
        Fragment oldPrimaryNav = getPrimaryNavigationFragment();
        int recordNum = i;
        while (true) {
            boolean z = true;
            if (recordNum >= i2) {
                break;
            }
            BackStackRecord record = arrayList.get(recordNum);
            if (!arrayList2.get(recordNum).booleanValue()) {
                trackAddedFragmentsInPop = record.expandOps(this.mTmpAddedFragments, oldPrimaryNav);
            } else {
                trackAddedFragmentsInPop = record.trackAddedFragmentsInPop(this.mTmpAddedFragments, oldPrimaryNav);
            }
            oldPrimaryNav = trackAddedFragmentsInPop;
            if (!addToBackStack && !record.mAddToBackStack) {
                z = false;
            }
            addToBackStack = z;
            recordNum++;
        }
        this.mTmpAddedFragments.clear();
        if (!allowReordering) {
            FragmentTransition.startTransitions(this, records, isRecordPop, startIndex, endIndex, false);
        }
        executeOps(records, isRecordPop, startIndex, endIndex);
        int postponeIndex = endIndex;
        if (allowReordering) {
            ArraySet<Fragment> addedFragments = new ArraySet<>();
            addAddedFragments(addedFragments);
            int postponeIndex2 = postponePostponableTransactions(records, isRecordPop, startIndex, endIndex, addedFragments);
            makeRemovedFragmentsInvisible(addedFragments);
            postponeIndex = postponeIndex2;
        }
        if (postponeIndex != i && allowReordering) {
            FragmentTransition.startTransitions(this, records, isRecordPop, startIndex, postponeIndex, true);
            moveToState(this.mCurState, true);
        }
        for (int recordNum2 = i; recordNum2 < i2; recordNum2++) {
            BackStackRecord record2 = arrayList.get(recordNum2);
            if (arrayList2.get(recordNum2).booleanValue() && record2.mIndex >= 0) {
                freeBackStackIndex(record2.mIndex);
                record2.mIndex = -1;
            }
            record2.runOnCommitRunnables();
        }
        if (addToBackStack) {
            reportBackStackChanged();
        }
    }

    private void makeRemovedFragmentsInvisible(ArraySet<Fragment> fragments) {
        int numAdded = fragments.size();
        for (int i = 0; i < numAdded; i++) {
            Fragment fragment = fragments.valueAt(i);
            if (!fragment.mAdded) {
                View view = fragment.getView();
                fragment.mPostponedAlpha = view.getAlpha();
                view.setAlpha(0.0f);
            }
        }
    }

    private int postponePostponableTransactions(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex, ArraySet<Fragment> added) {
        int postponeIndex = endIndex;
        for (int i = endIndex - 1; i >= startIndex; i--) {
            BackStackRecord record = records.get(i);
            boolean isPop = isRecordPop.get(i).booleanValue();
            if (record.isPostponed() && !record.interactsWith(records, i + 1, endIndex)) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList<>();
                }
                StartEnterTransitionListener listener = new StartEnterTransitionListener(record, isPop);
                this.mPostponedTransactions.add(listener);
                record.setOnStartPostponedListener(listener);
                if (isPop) {
                    record.executeOps();
                } else {
                    record.executePopOps(false);
                }
                postponeIndex--;
                if (i != postponeIndex) {
                    records.remove(i);
                    records.add(postponeIndex, record);
                }
                addAddedFragments(added);
            }
        }
        return postponeIndex;
    }

    /* access modifiers changed from: private */
    public void completeExecute(BackStackRecord record, boolean isPop, boolean runTransitions, boolean moveToState) {
        if (isPop) {
            record.executePopOps(moveToState);
        } else {
            record.executeOps();
        }
        ArrayList<BackStackRecord> records = new ArrayList<>(1);
        ArrayList arrayList = new ArrayList(1);
        records.add(record);
        arrayList.add(Boolean.valueOf(isPop));
        if (runTransitions) {
            FragmentTransition.startTransitions(this, records, arrayList, 0, 1, true);
        }
        if (moveToState) {
            moveToState(this.mCurState, true);
        }
        if (this.mActive != null) {
            int numActive = this.mActive.size();
            for (int i = 0; i < numActive; i++) {
                Fragment fragment = this.mActive.valueAt(i);
                if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && record.interactsWith(fragment.mContainerId)) {
                    if (fragment.mPostponedAlpha > 0.0f) {
                        fragment.mView.setAlpha(fragment.mPostponedAlpha);
                    }
                    if (moveToState) {
                        fragment.mPostponedAlpha = 0.0f;
                    } else {
                        fragment.mPostponedAlpha = -1.0f;
                        fragment.mIsNewlyAdded = false;
                    }
                }
            }
        }
    }

    private Fragment findFragmentUnder(Fragment f) {
        ViewGroup container = f.mContainer;
        View view = f.mView;
        if (container == null || view == null) {
            return null;
        }
        for (int i = this.mAdded.indexOf(f) - 1; i >= 0; i--) {
            Fragment underFragment = this.mAdded.get(i);
            if (underFragment.mContainer == container && underFragment.mView != null) {
                return underFragment;
            }
        }
        return null;
    }

    private static void executeOps(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            BackStackRecord record = records.get(i);
            boolean moveToState = true;
            if (isRecordPop.get(i).booleanValue()) {
                record.bumpBackStackNesting(-1);
                if (i != endIndex - 1) {
                    moveToState = false;
                }
                record.executePopOps(moveToState);
            } else {
                record.bumpBackStackNesting(1);
                record.executeOps();
            }
        }
    }

    private void addAddedFragments(ArraySet<Fragment> added) {
        if (this.mCurState >= 1) {
            int state = Math.min(this.mCurState, 4);
            int numAdded = this.mAdded.size();
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < numAdded) {
                    Fragment fragment = this.mAdded.get(i2);
                    if (fragment.mState < state) {
                        moveToState(fragment, state, fragment.getNextAnim(), fragment.getNextTransition(), false);
                        if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded) {
                            added.add(fragment);
                        }
                    }
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                this.mPostponedTransactions.remove(0).completeTransaction();
            }
        }
    }

    private void endAnimatingAwayFragments() {
        int numFragments = this.mActive == null ? 0 : this.mActive.size();
        for (int i = 0; i < numFragments; i++) {
            Fragment fragment = this.mActive.valueAt(i);
            if (fragment != null) {
                if (fragment.getAnimatingAway() != null) {
                    int stateAfterAnimating = fragment.getStateAfterAnimating();
                    View animatingAway = fragment.getAnimatingAway();
                    fragment.setAnimatingAway((View) null);
                    Animation animation = animatingAway.getAnimation();
                    if (animation != null) {
                        animation.cancel();
                        animatingAway.clearAnimation();
                    }
                    moveToState(fragment, stateAfterAnimating, 0, 0, false);
                } else if (fragment.getAnimator() != null) {
                    fragment.getAnimator().end();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003c, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean generateOpsForPendingActions(java.util.ArrayList<android.support.v4.app.BackStackRecord> r5, java.util.ArrayList<java.lang.Boolean> r6) {
        /*
            r4 = this;
            r0 = 0
            monitor-enter(r4)
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r1 = r4.mPendingActions     // Catch:{ all -> 0x003d }
            r2 = 0
            if (r1 == 0) goto L_0x003b
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r1 = r4.mPendingActions     // Catch:{ all -> 0x003d }
            int r1 = r1.size()     // Catch:{ all -> 0x003d }
            if (r1 != 0) goto L_0x0010
            goto L_0x003b
        L_0x0010:
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r1 = r4.mPendingActions     // Catch:{ all -> 0x003d }
            int r1 = r1.size()     // Catch:{ all -> 0x003d }
        L_0x0017:
            if (r2 >= r1) goto L_0x0029
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r3 = r4.mPendingActions     // Catch:{ all -> 0x003d }
            java.lang.Object r3 = r3.get(r2)     // Catch:{ all -> 0x003d }
            android.support.v4.app.FragmentManagerImpl$OpGenerator r3 = (android.support.v4.app.FragmentManagerImpl.OpGenerator) r3     // Catch:{ all -> 0x003d }
            boolean r3 = r3.generateOps(r5, r6)     // Catch:{ all -> 0x003d }
            r0 = r0 | r3
            int r2 = r2 + 1
            goto L_0x0017
        L_0x0029:
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r2 = r4.mPendingActions     // Catch:{ all -> 0x003d }
            r2.clear()     // Catch:{ all -> 0x003d }
            android.support.v4.app.FragmentHostCallback r2 = r4.mHost     // Catch:{ all -> 0x003d }
            android.os.Handler r2 = r2.getHandler()     // Catch:{ all -> 0x003d }
            java.lang.Runnable r3 = r4.mExecCommit     // Catch:{ all -> 0x003d }
            r2.removeCallbacks(r3)     // Catch:{ all -> 0x003d }
            monitor-exit(r4)     // Catch:{ all -> 0x003d }
            return r0
        L_0x003b:
            monitor-exit(r4)     // Catch:{ all -> 0x003d }
            return r2
        L_0x003d:
            r1 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x003d }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.generateOpsForPendingActions(java.util.ArrayList, java.util.ArrayList):boolean");
    }

    /* access modifiers changed from: package-private */
    public void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            boolean loadersRunning = false;
            for (int i = 0; i < this.mActive.size(); i++) {
                Fragment f = this.mActive.valueAt(i);
                if (!(f == null || f.mLoaderManager == null)) {
                    loadersRunning |= f.mLoaderManager.hasRunningLoaders();
                }
            }
            if (!loadersRunning) {
                this.mHavePendingDeferredStart = false;
                startPendingDeferredFragments();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); i++) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void addBackStackState(BackStackRecord state) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList<>();
        }
        this.mBackStack.add(state);
    }

    /* access modifiers changed from: package-private */
    public boolean popBackStackState(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, String name, int id, int flags) {
        if (this.mBackStack == null) {
            return false;
        }
        if (name == null && id < 0 && (flags & 1) == 0) {
            int last = this.mBackStack.size() - 1;
            if (last < 0) {
                return false;
            }
            records.add(this.mBackStack.remove(last));
            isRecordPop.add(true);
        } else {
            int index = -1;
            if (name != null || id >= 0) {
                int index2 = this.mBackStack.size() - 1;
                while (index >= 0) {
                    BackStackRecord bss = this.mBackStack.get(index);
                    if ((name != null && name.equals(bss.getName())) || (id >= 0 && id == bss.mIndex)) {
                        break;
                    }
                    index2 = index - 1;
                }
                if (index < 0) {
                    return false;
                }
                if ((flags & 1) != 0) {
                    index--;
                    while (index >= 0) {
                        BackStackRecord bss2 = this.mBackStack.get(index);
                        if ((name == null || !name.equals(bss2.getName())) && (id < 0 || id != bss2.mIndex)) {
                            break;
                        }
                        index--;
                    }
                }
            }
            if (index == this.mBackStack.size() - 1) {
                return false;
            }
            for (int i = this.mBackStack.size() - 1; i > index; i--) {
                records.add(this.mBackStack.remove(i));
                isRecordPop.add(true);
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public FragmentManagerNonConfig retainNonConfig() {
        setRetaining(this.mSavedNonConfig);
        return this.mSavedNonConfig;
    }

    private static void setRetaining(FragmentManagerNonConfig nonConfig) {
        if (nonConfig != null) {
            List<Fragment> fragments = nonConfig.getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    fragment.mRetaining = true;
                }
            }
            List<FragmentManagerNonConfig> children = nonConfig.getChildNonConfigs();
            if (children != null) {
                for (FragmentManagerNonConfig child : children) {
                    setRetaining(child);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void saveNonConfig() {
        FragmentManagerNonConfig child;
        ArrayList<Fragment> fragments = null;
        ArrayList<FragmentManagerNonConfig> fragments2 = null;
        if (this.mActive != null) {
            ArrayList<FragmentManagerNonConfig> childFragments = null;
            ArrayList<Fragment> fragments3 = null;
            for (int i = 0; i < this.mActive.size(); i++) {
                Fragment f = this.mActive.valueAt(i);
                if (f != null) {
                    if (f.mRetainInstance) {
                        if (fragments3 == null) {
                            fragments3 = new ArrayList<>();
                        }
                        fragments3.add(f);
                        f.mTargetIndex = f.mTarget != null ? f.mTarget.mIndex : -1;
                        if (DEBUG) {
                            Log.v(TAG, "retainNonConfig: keeping retained " + f);
                        }
                    }
                    if (f.mChildFragmentManager != null) {
                        f.mChildFragmentManager.saveNonConfig();
                        child = f.mChildFragmentManager.mSavedNonConfig;
                    } else {
                        child = f.mChildNonConfig;
                    }
                    if (childFragments == null && child != null) {
                        childFragments = new ArrayList<>(this.mActive.size());
                        for (int j = 0; j < i; j++) {
                            childFragments.add((Object) null);
                        }
                    }
                    if (childFragments != null) {
                        childFragments.add(child);
                    }
                }
            }
            fragments = fragments3;
            fragments2 = childFragments;
        }
        if (fragments == null && fragments2 == null) {
            this.mSavedNonConfig = null;
        } else {
            this.mSavedNonConfig = new FragmentManagerNonConfig(fragments, fragments2);
        }
    }

    /* access modifiers changed from: package-private */
    public void saveFragmentViewState(Fragment f) {
        if (f.mInnerView != null) {
            if (this.mStateArray == null) {
                this.mStateArray = new SparseArray<>();
            } else {
                this.mStateArray.clear();
            }
            f.mInnerView.saveHierarchyState(this.mStateArray);
            if (this.mStateArray.size() > 0) {
                f.mSavedViewState = this.mStateArray;
                this.mStateArray = null;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Bundle saveFragmentBasicState(Fragment f) {
        Bundle result = null;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        f.performSaveInstanceState(this.mStateBundle);
        dispatchOnFragmentSaveInstanceState(f, this.mStateBundle, false);
        if (!this.mStateBundle.isEmpty()) {
            result = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (f.mView != null) {
            saveFragmentViewState(f);
        }
        if (f.mSavedViewState != null) {
            if (result == null) {
                result = new Bundle();
            }
            result.putSparseParcelableArray(VIEW_STATE_TAG, f.mSavedViewState);
        }
        if (!f.mUserVisibleHint) {
            if (result == null) {
                result = new Bundle();
            }
            result.putBoolean(USER_VISIBLE_HINT_TAG, f.mUserVisibleHint);
        }
        return result;
    }

    /* access modifiers changed from: package-private */
    public Parcelable saveAllState() {
        int N;
        forcePostponedTransactions();
        endAnimatingAwayFragments();
        execPendingActions();
        this.mStateSaved = true;
        this.mSavedNonConfig = null;
        if (this.mActive == null || this.mActive.size() <= 0) {
            return null;
        }
        int N2 = this.mActive.size();
        FragmentState[] active = new FragmentState[N2];
        boolean haveFragments = false;
        for (int i = 0; i < N2; i++) {
            Fragment f = this.mActive.valueAt(i);
            if (f != null) {
                if (f.mIndex < 0) {
                    throwException(new IllegalStateException("Failure saving state: active " + f + " has cleared index: " + f.mIndex));
                }
                haveFragments = true;
                FragmentState fs = new FragmentState(f);
                active[i] = fs;
                if (f.mState <= 0 || fs.mSavedFragmentState != null) {
                    fs.mSavedFragmentState = f.mSavedFragmentState;
                } else {
                    fs.mSavedFragmentState = saveFragmentBasicState(f);
                    if (f.mTarget != null) {
                        if (f.mTarget.mIndex < 0) {
                            throwException(new IllegalStateException("Failure saving state: " + f + " has target not in fragment manager: " + f.mTarget));
                        }
                        if (fs.mSavedFragmentState == null) {
                            fs.mSavedFragmentState = new Bundle();
                        }
                        putFragment(fs.mSavedFragmentState, TARGET_STATE_TAG, f.mTarget);
                        if (f.mTargetRequestCode != 0) {
                            fs.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, f.mTargetRequestCode);
                        }
                    }
                }
                if (DEBUG) {
                    Log.v(TAG, "Saved state of " + f + ": " + fs.mSavedFragmentState);
                }
            }
        }
        if (!haveFragments) {
            if (DEBUG) {
                Log.v(TAG, "saveAllState: no fragments!");
            }
            return null;
        }
        int[] added = null;
        BackStackState[] backStack = null;
        int N3 = this.mAdded.size();
        if (N3 > 0) {
            added = new int[N3];
            for (int i2 = 0; i2 < N3; i2++) {
                added[i2] = this.mAdded.get(i2).mIndex;
                if (added[i2] < 0) {
                    throwException(new IllegalStateException("Failure saving state: active " + this.mAdded.get(i2) + " has cleared index: " + added[i2]));
                }
                if (DEBUG) {
                    Log.v(TAG, "saveAllState: adding fragment #" + i2 + ": " + this.mAdded.get(i2));
                }
            }
        }
        if (this.mBackStack != null && (N = this.mBackStack.size()) > 0) {
            backStack = new BackStackState[N];
            for (int i3 = 0; i3 < N; i3++) {
                backStack[i3] = new BackStackState(this.mBackStack.get(i3));
                if (DEBUG) {
                    Log.v(TAG, "saveAllState: adding back stack #" + i3 + ": " + this.mBackStack.get(i3));
                }
            }
        }
        FragmentManagerState fms = new FragmentManagerState();
        fms.mActive = active;
        fms.mAdded = added;
        fms.mBackStack = backStack;
        if (this.mPrimaryNav != null) {
            fms.mPrimaryNavActiveIndex = this.mPrimaryNav.mIndex;
        }
        fms.mNextFragmentIndex = this.mNextFragmentIndex;
        saveNonConfig();
        return fms;
    }

    /* access modifiers changed from: package-private */
    public void restoreAllState(Parcelable state, FragmentManagerNonConfig nonConfig) {
        if (state != null) {
            FragmentManagerState fms = (FragmentManagerState) state;
            if (fms.mActive != null) {
                List<FragmentManagerNonConfig> childNonConfigs = null;
                if (nonConfig != null) {
                    List<Fragment> nonConfigFragments = nonConfig.getFragments();
                    childNonConfigs = nonConfig.getChildNonConfigs();
                    int count = nonConfigFragments != null ? nonConfigFragments.size() : 0;
                    for (int i = 0; i < count; i++) {
                        Fragment f = nonConfigFragments.get(i);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: re-attaching retained " + f);
                        }
                        int index = 0;
                        while (index < fms.mActive.length && fms.mActive[index].mIndex != f.mIndex) {
                            index++;
                        }
                        if (index == fms.mActive.length) {
                            throwException(new IllegalStateException("Could not find active fragment with index " + f.mIndex));
                        }
                        FragmentState fs = fms.mActive[index];
                        fs.mInstance = f;
                        f.mSavedViewState = null;
                        f.mBackStackNesting = 0;
                        f.mInLayout = false;
                        f.mAdded = false;
                        f.mTarget = null;
                        if (fs.mSavedFragmentState != null) {
                            fs.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                            f.mSavedViewState = fs.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
                            f.mSavedFragmentState = fs.mSavedFragmentState;
                        }
                    }
                }
                this.mActive = new SparseArray<>(fms.mActive.length);
                for (int i2 = 0; i2 < fms.mActive.length; i2++) {
                    FragmentState fs2 = fms.mActive[i2];
                    if (fs2 != null) {
                        FragmentManagerNonConfig childNonConfig = null;
                        if (childNonConfigs != null && i2 < childNonConfigs.size()) {
                            childNonConfig = childNonConfigs.get(i2);
                        }
                        Fragment f2 = fs2.instantiate(this.mHost, this.mContainer, this.mParent, childNonConfig);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: active #" + i2 + ": " + f2);
                        }
                        this.mActive.put(f2.mIndex, f2);
                        fs2.mInstance = null;
                    }
                }
                if (nonConfig != null) {
                    List<Fragment> nonConfigFragments2 = nonConfig.getFragments();
                    int count2 = nonConfigFragments2 != null ? nonConfigFragments2.size() : 0;
                    for (int i3 = 0; i3 < count2; i3++) {
                        Fragment f3 = nonConfigFragments2.get(i3);
                        if (f3.mTargetIndex >= 0) {
                            f3.mTarget = this.mActive.get(f3.mTargetIndex);
                            if (f3.mTarget == null) {
                                Log.w(TAG, "Re-attaching retained fragment " + f3 + " target no longer exists: " + f3.mTargetIndex);
                            }
                        }
                    }
                }
                this.mAdded.clear();
                if (fms.mAdded != null) {
                    int i4 = 0;
                    while (i4 < fms.mAdded.length) {
                        Fragment f4 = this.mActive.get(fms.mAdded[i4]);
                        if (f4 == null) {
                            throwException(new IllegalStateException("No instantiated fragment for index #" + fms.mAdded[i4]));
                        }
                        f4.mAdded = true;
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: added #" + i4 + ": " + f4);
                        }
                        if (!this.mAdded.contains(f4)) {
                            synchronized (this.mAdded) {
                                this.mAdded.add(f4);
                            }
                            i4++;
                        } else {
                            throw new IllegalStateException("Already added!");
                        }
                    }
                }
                if (fms.mBackStack != null) {
                    this.mBackStack = new ArrayList<>(fms.mBackStack.length);
                    for (int i5 = 0; i5 < fms.mBackStack.length; i5++) {
                        BackStackRecord bse = fms.mBackStack[i5].instantiate(this);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: back stack #" + i5 + " (index " + bse.mIndex + "): " + bse);
                            PrintWriter pw = new PrintWriter(new LogWriter(TAG));
                            bse.dump("  ", pw, false);
                            pw.close();
                        }
                        this.mBackStack.add(bse);
                        if (bse.mIndex >= 0) {
                            setBackStackIndex(bse.mIndex, bse);
                        }
                    }
                } else {
                    this.mBackStack = null;
                }
                if (fms.mPrimaryNavActiveIndex >= 0) {
                    this.mPrimaryNav = this.mActive.get(fms.mPrimaryNavActiveIndex);
                }
                this.mNextFragmentIndex = fms.mNextFragmentIndex;
            }
        }
    }

    private void burpActive() {
        if (this.mActive != null) {
            for (int i = this.mActive.size() - 1; i >= 0; i--) {
                if (this.mActive.valueAt(i) == null) {
                    this.mActive.delete(this.mActive.keyAt(i));
                }
            }
        }
    }

    public void attachController(FragmentHostCallback host, FragmentContainer container, Fragment parent) {
        if (this.mHost == null) {
            this.mHost = host;
            this.mContainer = container;
            this.mParent = parent;
            return;
        }
        throw new IllegalStateException("Already attached");
    }

    public void noteStateNotSaved() {
        this.mSavedNonConfig = null;
        this.mStateSaved = false;
        int addedCount = this.mAdded.size();
        for (int i = 0; i < addedCount; i++) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.noteStateNotSaved();
            }
        }
    }

    public void dispatchCreate() {
        this.mStateSaved = false;
        dispatchStateChange(1);
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        dispatchStateChange(2);
    }

    public void dispatchStart() {
        this.mStateSaved = false;
        dispatchStateChange(4);
    }

    public void dispatchResume() {
        this.mStateSaved = false;
        dispatchStateChange(5);
    }

    public void dispatchPause() {
        dispatchStateChange(4);
    }

    public void dispatchStop() {
        this.mStateSaved = true;
        dispatchStateChange(3);
    }

    public void dispatchReallyStop() {
        dispatchStateChange(2);
    }

    public void dispatchDestroyView() {
        dispatchStateChange(1);
    }

    public void dispatchDestroy() {
        this.mDestroyed = true;
        execPendingActions();
        dispatchStateChange(0);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
    }

    /* JADX INFO: finally extract failed */
    private void dispatchStateChange(int nextState) {
        try {
            this.mExecutingActions = true;
            moveToState(nextState, false);
            this.mExecutingActions = false;
            execPendingActions();
        } catch (Throwable th) {
            this.mExecutingActions = false;
            throw th;
        }
    }

    public void dispatchMultiWindowModeChanged(boolean isInMultiWindowMode) {
        for (int i = this.mAdded.size() - 1; i >= 0; i--) {
            Fragment f = this.mAdded.get(i);
            if (f != null) {
                f.performMultiWindowModeChanged(isInMultiWindowMode);
            }
        }
    }

    public void dispatchPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        for (int i = this.mAdded.size() - 1; i >= 0; i--) {
            Fragment f = this.mAdded.get(i);
            if (f != null) {
                f.performPictureInPictureModeChanged(isInPictureInPictureMode);
            }
        }
    }

    public void dispatchConfigurationChanged(Configuration newConfig) {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = this.mAdded.get(i);
            if (f != null) {
                f.performConfigurationChanged(newConfig);
            }
        }
    }

    public void dispatchLowMemory() {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = this.mAdded.get(i);
            if (f != null) {
                f.performLowMemory();
            }
        }
    }

    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int i = 0;
        ArrayList<Fragment> newMenus = null;
        boolean show = false;
        for (int i2 = 0; i2 < this.mAdded.size(); i2++) {
            Fragment f = this.mAdded.get(i2);
            if (f != null && f.performCreateOptionsMenu(menu, inflater)) {
                show = true;
                if (newMenus == null) {
                    newMenus = new ArrayList<>();
                }
                newMenus.add(f);
            }
        }
        if (this.mCreatedMenus != null) {
            while (true) {
                int i3 = i;
                if (i3 >= this.mCreatedMenus.size()) {
                    break;
                }
                Fragment f2 = this.mCreatedMenus.get(i3);
                if (newMenus == null || !newMenus.contains(f2)) {
                    f2.onDestroyOptionsMenu();
                }
                i = i3 + 1;
            }
        }
        this.mCreatedMenus = newMenus;
        return show;
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        boolean show = false;
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = this.mAdded.get(i);
            if (f != null && f.performPrepareOptionsMenu(menu)) {
                show = true;
            }
        }
        return show;
    }

    public boolean dispatchOptionsItemSelected(MenuItem item) {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = this.mAdded.get(i);
            if (f != null && f.performOptionsItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean dispatchContextItemSelected(MenuItem item) {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = this.mAdded.get(i);
            if (f != null && f.performContextItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = this.mAdded.get(i);
            if (f != null) {
                f.performOptionsMenuClosed(menu);
            }
        }
    }

    public void setPrimaryNavigationFragment(Fragment f) {
        if (f == null || (this.mActive.get(f.mIndex) == f && (f.mHost == null || f.getFragmentManager() == this))) {
            this.mPrimaryNav = f;
            return;
        }
        throw new IllegalArgumentException("Fragment " + f + " is not an active fragment of FragmentManager " + this);
    }

    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }

    public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks cb, boolean recursive) {
        this.mLifecycleCallbacks.add(new Pair(cb, Boolean.valueOf(recursive)));
    }

    public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks cb) {
        synchronized (this.mLifecycleCallbacks) {
            int i = 0;
            int N = this.mLifecycleCallbacks.size();
            while (true) {
                if (i >= N) {
                    break;
                } else if (this.mLifecycleCallbacks.get(i).first == cb) {
                    this.mLifecycleCallbacks.remove(i);
                    break;
                } else {
                    i++;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentPreAttached(Fragment f, Context context, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentPreAttached(f, context, true);
            }
        }
        Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> p = it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentManager.FragmentLifecycleCallbacks) p.first).onFragmentPreAttached(this, f, context);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentAttached(Fragment f, Context context, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentAttached(f, context, true);
            }
        }
        Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> p = it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentManager.FragmentLifecycleCallbacks) p.first).onFragmentAttached(this, f, context);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentPreCreated(Fragment f, Bundle savedInstanceState, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentPreCreated(f, savedInstanceState, true);
            }
        }
        Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> p = it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentManager.FragmentLifecycleCallbacks) p.first).onFragmentPreCreated(this, f, savedInstanceState);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentCreated(Fragment f, Bundle savedInstanceState, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentCreated(f, savedInstanceState, true);
            }
        }
        Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> p = it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentManager.FragmentLifecycleCallbacks) p.first).onFragmentCreated(this, f, savedInstanceState);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentActivityCreated(Fragment f, Bundle savedInstanceState, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentActivityCreated(f, savedInstanceState, true);
            }
        }
        Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> p = it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentManager.FragmentLifecycleCallbacks) p.first).onFragmentActivityCreated(this, f, savedInstanceState);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentViewCreated(Fragment f, View v, Bundle savedInstanceState, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentViewCreated(f, v, savedInstanceState, true);
            }
        }
        Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> p = it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentManager.FragmentLifecycleCallbacks) p.first).onFragmentViewCreated(this, f, v, savedInstanceState);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentStarted(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentStarted(f, true);
            }
        }
        Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> p = it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentManager.FragmentLifecycleCallbacks) p.first).onFragmentStarted(this, f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentResumed(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentResumed(f, true);
            }
        }
        Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> p = it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentManager.FragmentLifecycleCallbacks) p.first).onFragmentResumed(this, f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentPaused(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentPaused(f, true);
            }
        }
        Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> p = it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentManager.FragmentLifecycleCallbacks) p.first).onFragmentPaused(this, f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentStopped(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentStopped(f, true);
            }
        }
        Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> p = it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentManager.FragmentLifecycleCallbacks) p.first).onFragmentStopped(this, f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentSaveInstanceState(Fragment f, Bundle outState, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentSaveInstanceState(f, outState, true);
            }
        }
        Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> p = it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentManager.FragmentLifecycleCallbacks) p.first).onFragmentSaveInstanceState(this, f, outState);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentViewDestroyed(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentViewDestroyed(f, true);
            }
        }
        Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> p = it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentManager.FragmentLifecycleCallbacks) p.first).onFragmentViewDestroyed(this, f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentDestroyed(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentDestroyed(f, true);
            }
        }
        Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> p = it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentManager.FragmentLifecycleCallbacks) p.first).onFragmentDestroyed(this, f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnFragmentDetached(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentDetached(f, true);
            }
        }
        Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> p = it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentManager.FragmentLifecycleCallbacks) p.first).onFragmentDetached(this, f);
            }
        }
    }

    public static int reverseTransit(int transit) {
        if (transit == 4097) {
            return 8194;
        }
        if (transit == 4099) {
            return 4099;
        }
        if (transit != 8194) {
            return 0;
        }
        return 4097;
    }

    public static int transitToStyleIndex(int transit, boolean enter) {
        if (transit == 4097) {
            return enter ? 1 : 2;
        } else if (transit == 4099) {
            return enter ? 5 : 6;
        } else if (transit != 8194) {
            return -1;
        } else {
            return enter ? 3 : 4;
        }
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        Fragment fragment;
        Fragment fragment2;
        Context context2 = context;
        AttributeSet attributeSet = attrs;
        if (!"fragment".equals(name)) {
            return null;
        }
        String fname = attributeSet.getAttributeValue((String) null, "class");
        TypedArray a = context2.obtainStyledAttributes(attributeSet, FragmentTag.Fragment);
        int i = 0;
        if (fname == null) {
            fname = a.getString(0);
        }
        String fname2 = fname;
        int id = a.getResourceId(1, -1);
        String tag = a.getString(2);
        a.recycle();
        if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), fname2)) {
            return null;
        }
        if (parent != null) {
            i = parent.getId();
        }
        int containerId = i;
        if (containerId == -1 && id == -1 && tag == null) {
            throw new IllegalArgumentException(attrs.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + fname2);
        }
        Fragment fragment3 = id != -1 ? findFragmentById(id) : null;
        if (fragment3 == null && tag != null) {
            fragment3 = findFragmentByTag(tag);
        }
        if (fragment3 == null && containerId != -1) {
            fragment3 = findFragmentById(containerId);
        }
        if (DEBUG) {
            Log.v(TAG, "onCreateView: id=0x" + Integer.toHexString(id) + " fname=" + fname2 + " existing=" + fragment3);
        }
        if (fragment3 == null) {
            Fragment fragment4 = this.mContainer.instantiate(context2, fname2, (Bundle) null);
            fragment4.mFromLayout = true;
            fragment4.mFragmentId = id != 0 ? id : containerId;
            fragment4.mContainerId = containerId;
            fragment4.mTag = tag;
            fragment4.mInLayout = true;
            fragment4.mFragmentManager = this;
            fragment4.mHost = this.mHost;
            fragment4.onInflate(this.mHost.getContext(), attributeSet, fragment4.mSavedFragmentState);
            addFragment(fragment4, true);
            fragment = fragment4;
        } else if (!fragment3.mInLayout) {
            fragment3.mInLayout = true;
            fragment3.mHost = this.mHost;
            if (!fragment3.mRetaining) {
                fragment3.onInflate(this.mHost.getContext(), attributeSet, fragment3.mSavedFragmentState);
            }
            fragment = fragment3;
        } else {
            throw new IllegalArgumentException(attrs.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(id) + ", tag " + tag + ", or parent id 0x" + Integer.toHexString(containerId) + " with another fragment for " + fname2);
        }
        if (this.mCurState >= 1 || !fragment.mFromLayout) {
            fragment2 = fragment;
            moveToState(fragment2);
        } else {
            fragment2 = fragment;
            moveToState(fragment, 1, 0, 0, false);
        }
        if (fragment2.mView != null) {
            if (id != 0) {
                fragment2.mView.setId(id);
            }
            if (fragment2.mView.getTag() == null) {
                fragment2.mView.setTag(tag);
            }
            return fragment2.mView;
        }
        throw new IllegalStateException("Fragment " + fname2 + " did not create a view.");
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return onCreateView((View) null, name, context, attrs);
    }

    /* access modifiers changed from: package-private */
    public LayoutInflater.Factory2 getLayoutInflaterFactory() {
        return this;
    }

    /* compiled from: FragmentManager */
    static class FragmentTag {
        public static final int[] Fragment = {16842755, 16842960, 16842961};
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;

        FragmentTag() {
        }
    }

    /* compiled from: FragmentManager */
    private class PopBackStackState implements OpGenerator {
        final int mFlags;
        final int mId;
        final String mName;

        PopBackStackState(String name, int id, int flags) {
            this.mName = name;
            this.mId = id;
            this.mFlags = flags;
        }

        public boolean generateOps(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
            FragmentManager childManager;
            if (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null && (childManager = FragmentManagerImpl.this.mPrimaryNav.peekChildFragmentManager()) != null && childManager.popBackStackImmediate()) {
                return false;
            }
            return FragmentManagerImpl.this.popBackStackState(records, isRecordPop, this.mName, this.mId, this.mFlags);
        }
    }

    /* compiled from: FragmentManager */
    static class StartEnterTransitionListener implements Fragment.OnStartEnterTransitionListener {
        /* access modifiers changed from: private */
        public final boolean mIsBack;
        private int mNumPostponed;
        /* access modifiers changed from: private */
        public final BackStackRecord mRecord;

        StartEnterTransitionListener(BackStackRecord record, boolean isBack) {
            this.mIsBack = isBack;
            this.mRecord = record;
        }

        public void onStartEnterTransition() {
            this.mNumPostponed--;
            if (this.mNumPostponed == 0) {
                this.mRecord.mManager.scheduleCommit();
            }
        }

        public void startListening() {
            this.mNumPostponed++;
        }

        public boolean isReady() {
            return this.mNumPostponed == 0;
        }

        public void completeTransaction() {
            boolean z = false;
            boolean canceled = this.mNumPostponed > 0;
            FragmentManagerImpl manager = this.mRecord.mManager;
            int numAdded = manager.mAdded.size();
            for (int i = 0; i < numAdded; i++) {
                Fragment fragment = manager.mAdded.get(i);
                fragment.setOnStartEnterTransitionListener((Fragment.OnStartEnterTransitionListener) null);
                if (canceled && fragment.isPostponed()) {
                    fragment.startPostponedEnterTransition();
                }
            }
            FragmentManagerImpl fragmentManagerImpl = this.mRecord.mManager;
            BackStackRecord backStackRecord = this.mRecord;
            boolean z2 = this.mIsBack;
            if (!canceled) {
                z = true;
            }
            fragmentManagerImpl.completeExecute(backStackRecord, z2, z, true);
        }

        public void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }
    }

    /* compiled from: FragmentManager */
    private static class AnimationOrAnimator {
        public final Animation animation;
        public final Animator animator;

        private AnimationOrAnimator(Animation animation2) {
            this.animation = animation2;
            this.animator = null;
            if (animation2 == null) {
                throw new IllegalStateException("Animation cannot be null");
            }
        }

        private AnimationOrAnimator(Animator animator2) {
            this.animation = null;
            this.animator = animator2;
            if (animator2 == null) {
                throw new IllegalStateException("Animator cannot be null");
            }
        }
    }

    /* compiled from: FragmentManager */
    private static class AnimationListenerWrapper implements Animation.AnimationListener {
        private final Animation.AnimationListener mWrapped;

        private AnimationListenerWrapper(Animation.AnimationListener wrapped) {
            this.mWrapped = wrapped;
        }

        @CallSuper
        public void onAnimationStart(Animation animation) {
            if (this.mWrapped != null) {
                this.mWrapped.onAnimationStart(animation);
            }
        }

        @CallSuper
        public void onAnimationEnd(Animation animation) {
            if (this.mWrapped != null) {
                this.mWrapped.onAnimationEnd(animation);
            }
        }

        @CallSuper
        public void onAnimationRepeat(Animation animation) {
            if (this.mWrapped != null) {
                this.mWrapped.onAnimationRepeat(animation);
            }
        }
    }

    /* compiled from: FragmentManager */
    private static class AnimateOnHWLayerIfNeededListener extends AnimationListenerWrapper {
        View mView;

        AnimateOnHWLayerIfNeededListener(View v, Animation.AnimationListener listener) {
            super(listener);
            this.mView = v;
        }

        @CallSuper
        public void onAnimationEnd(Animation animation) {
            if (ViewCompat.isAttachedToWindow(this.mView) || Build.VERSION.SDK_INT >= 24) {
                this.mView.post(new Runnable() {
                    public void run() {
                        AnimateOnHWLayerIfNeededListener.this.mView.setLayerType(0, (Paint) null);
                    }
                });
            } else {
                this.mView.setLayerType(0, (Paint) null);
            }
            super.onAnimationEnd(animation);
        }
    }

    /* compiled from: FragmentManager */
    private static class AnimatorOnHWLayerIfNeededListener extends AnimatorListenerAdapter {
        View mView;

        AnimatorOnHWLayerIfNeededListener(View v) {
            this.mView = v;
        }

        public void onAnimationStart(Animator animation) {
            this.mView.setLayerType(2, (Paint) null);
        }

        public void onAnimationEnd(Animator animation) {
            this.mView.setLayerType(0, (Paint) null);
            animation.removeListener(this);
        }
    }
}
