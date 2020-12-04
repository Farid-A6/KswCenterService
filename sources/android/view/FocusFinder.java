package android.view;

import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.view.FocusFinder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class FocusFinder {
    private static final ThreadLocal<FocusFinder> tlFocusFinder = new ThreadLocal<FocusFinder>() {
        /* access modifiers changed from: protected */
        public FocusFinder initialValue() {
            return new FocusFinder();
        }
    };
    final Rect mBestCandidateRect;
    private final FocusSorter mFocusSorter;
    final Rect mFocusedRect;
    final Rect mOtherRect;
    private final ArrayList<View> mTempList;
    private final UserSpecifiedFocusComparator mUserSpecifiedClusterComparator;
    private final UserSpecifiedFocusComparator mUserSpecifiedFocusComparator;

    public static FocusFinder getInstance() {
        return tlFocusFinder.get();
    }

    static /* synthetic */ View lambda$new$0(View r, View v) {
        if (isValidId(v.getNextFocusForwardId())) {
            return v.findUserSetNextFocus(r, 2);
        }
        return null;
    }

    static /* synthetic */ View lambda$new$1(View r, View v) {
        if (isValidId(v.getNextClusterForwardId())) {
            return v.findUserSetNextKeyboardNavigationCluster(r, 2);
        }
        return null;
    }

    private FocusFinder() {
        this.mFocusedRect = new Rect();
        this.mOtherRect = new Rect();
        this.mBestCandidateRect = new Rect();
        this.mUserSpecifiedFocusComparator = new UserSpecifiedFocusComparator($$Lambda$FocusFinder$Pgx6IETuqCkrhJYdiBes48tolG4.INSTANCE);
        this.mUserSpecifiedClusterComparator = new UserSpecifiedFocusComparator($$Lambda$FocusFinder$P8rLvOJhymJH5ALAgUjGaM5gxKA.INSTANCE);
        this.mFocusSorter = new FocusSorter();
        this.mTempList = new ArrayList<>();
    }

    public final View findNextFocus(ViewGroup root, View focused, int direction) {
        return findNextFocus(root, focused, (Rect) null, direction);
    }

    public View findNextFocusFromRect(ViewGroup root, Rect focusedRect, int direction) {
        this.mFocusedRect.set(focusedRect);
        return findNextFocus(root, (View) null, this.mFocusedRect, direction);
    }

    /* JADX INFO: finally extract failed */
    private View findNextFocus(ViewGroup root, View focused, Rect focusedRect, int direction) {
        View next = null;
        ViewGroup effectiveRoot = getEffectiveRoot(root, focused);
        if (focused != null) {
            next = findNextUserSpecifiedFocus(effectiveRoot, focused, direction);
        }
        if (next != null) {
            return next;
        }
        ArrayList<View> focusables = this.mTempList;
        try {
            focusables.clear();
            effectiveRoot.addFocusables(focusables, direction);
            if (!focusables.isEmpty()) {
                next = findNextFocus(effectiveRoot, focused, focusedRect, direction, focusables);
            }
            focusables.clear();
            return next;
        } catch (Throwable th) {
            focusables.clear();
            throw th;
        }
    }

    private ViewGroup getEffectiveRoot(ViewGroup root, View focused) {
        if (focused == null || focused == root) {
            return root;
        }
        ViewGroup effective = null;
        ViewParent nextParent = focused.getParent();
        while (nextParent != root) {
            ViewGroup vg = (ViewGroup) nextParent;
            if (vg.getTouchscreenBlocksFocus() && focused.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN) && vg.isKeyboardNavigationCluster()) {
                effective = vg;
            }
            nextParent = nextParent.getParent();
            if (!(nextParent instanceof ViewGroup)) {
                return root;
            }
        }
        return effective != null ? effective : root;
    }

    /* JADX INFO: finally extract failed */
    public View findNextKeyboardNavigationCluster(View root, View currentCluster, int direction) {
        View next = null;
        if (currentCluster != null && (next = findNextUserSpecifiedKeyboardNavigationCluster(root, currentCluster, direction)) != null) {
            return next;
        }
        ArrayList<View> clusters = this.mTempList;
        try {
            clusters.clear();
            root.addKeyboardNavigationClusters(clusters, direction);
            if (!clusters.isEmpty()) {
                next = findNextKeyboardNavigationCluster(root, currentCluster, clusters, direction);
            }
            clusters.clear();
            return next;
        } catch (Throwable th) {
            clusters.clear();
            throw th;
        }
    }

    private View findNextUserSpecifiedKeyboardNavigationCluster(View root, View currentCluster, int direction) {
        View userSetNextCluster = currentCluster.findUserSetNextKeyboardNavigationCluster(root, direction);
        if (userSetNextCluster == null || !userSetNextCluster.hasFocusable()) {
            return null;
        }
        return userSetNextCluster;
    }

    private View findNextUserSpecifiedFocus(ViewGroup root, View focused, int direction) {
        View userSetNextFocus = focused.findUserSetNextFocus(root, direction);
        View cycleCheck = userSetNextFocus;
        boolean cycleStep = true;
        while (userSetNextFocus != null) {
            if (userSetNextFocus.isFocusable() && userSetNextFocus.getVisibility() == 0 && (!userSetNextFocus.isInTouchMode() || userSetNextFocus.isFocusableInTouchMode())) {
                return userSetNextFocus;
            }
            userSetNextFocus = userSetNextFocus.findUserSetNextFocus(root, direction);
            boolean z = !cycleStep;
            cycleStep = z;
            if (z && (cycleCheck = cycleCheck.findUserSetNextFocus(root, direction)) == userSetNextFocus) {
                return null;
            }
        }
        return null;
    }

    private View findNextFocus(ViewGroup root, View focused, Rect focusedRect, int direction, ArrayList<View> focusables) {
        Rect focusedRect2;
        Rect focusedRect3;
        ViewGroup viewGroup = root;
        int i = direction;
        if (focused != null) {
            if (focusedRect == null) {
                focusedRect3 = this.mFocusedRect;
            } else {
                focusedRect3 = focusedRect;
            }
            focused.getFocusedRect(focusedRect3);
            root.offsetDescendantRectToMyCoords(focused, focusedRect3);
        } else if (focusedRect == null) {
            focusedRect3 = this.mFocusedRect;
            if (i != 17 && i != 33) {
                if (i != 66 && i != 130) {
                    switch (i) {
                        case 1:
                            if (!root.isLayoutRtl()) {
                                setFocusBottomRight(root, focusedRect3);
                                break;
                            } else {
                                setFocusTopLeft(root, focusedRect3);
                                break;
                            }
                        case 2:
                            if (!root.isLayoutRtl()) {
                                setFocusTopLeft(root, focusedRect3);
                                break;
                            } else {
                                setFocusBottomRight(root, focusedRect3);
                                break;
                            }
                    }
                } else {
                    setFocusTopLeft(root, focusedRect3);
                }
            } else {
                setFocusBottomRight(root, focusedRect3);
            }
        } else {
            focusedRect2 = focusedRect;
            if (i != 17 || i == 33 || i == 66 || i == 130) {
                return findNextFocusInAbsoluteDirection(focusables, root, focused, focusedRect2, direction);
            }
            switch (i) {
                case 1:
                case 2:
                    return findNextFocusInRelativeDirection(focusables, root, focused, focusedRect2, direction);
                default:
                    throw new IllegalArgumentException("Unknown direction: " + direction);
            }
        }
        focusedRect2 = focusedRect3;
        if (i != 17) {
        }
        return findNextFocusInAbsoluteDirection(focusables, root, focused, focusedRect2, direction);
    }

    /* JADX INFO: finally extract failed */
    private View findNextKeyboardNavigationCluster(View root, View currentCluster, List<View> clusters, int direction) {
        try {
            this.mUserSpecifiedClusterComparator.setFocusables(clusters, root);
            Collections.sort(clusters, this.mUserSpecifiedClusterComparator);
            this.mUserSpecifiedClusterComparator.recycle();
            int count = clusters.size();
            if (!(direction == 17 || direction == 33)) {
                if (!(direction == 66 || direction == 130)) {
                    switch (direction) {
                        case 1:
                            break;
                        case 2:
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown direction: " + direction);
                    }
                }
                return getNextKeyboardNavigationCluster(root, currentCluster, clusters, count);
            }
            return getPreviousKeyboardNavigationCluster(root, currentCluster, clusters, count);
        } catch (Throwable th) {
            this.mUserSpecifiedClusterComparator.recycle();
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    private View findNextFocusInRelativeDirection(ArrayList<View> focusables, ViewGroup root, View focused, Rect focusedRect, int direction) {
        try {
            this.mUserSpecifiedFocusComparator.setFocusables(focusables, root);
            Collections.sort(focusables, this.mUserSpecifiedFocusComparator);
            this.mUserSpecifiedFocusComparator.recycle();
            int count = focusables.size();
            switch (direction) {
                case 1:
                    return getPreviousFocusable(focused, focusables, count);
                case 2:
                    return getNextFocusable(focused, focusables, count);
                default:
                    return focusables.get(count - 1);
            }
        } catch (Throwable th) {
            this.mUserSpecifiedFocusComparator.recycle();
            throw th;
        }
    }

    private void setFocusBottomRight(ViewGroup root, Rect focusedRect) {
        int rootBottom = root.getScrollY() + root.getHeight();
        int rootRight = root.getScrollX() + root.getWidth();
        focusedRect.set(rootRight, rootBottom, rootRight, rootBottom);
    }

    private void setFocusTopLeft(ViewGroup root, Rect focusedRect) {
        int rootTop = root.getScrollY();
        int rootLeft = root.getScrollX();
        focusedRect.set(rootLeft, rootTop, rootLeft, rootTop);
    }

    /* access modifiers changed from: package-private */
    public View findNextFocusInAbsoluteDirection(ArrayList<View> focusables, ViewGroup root, View focused, Rect focusedRect, int direction) {
        this.mBestCandidateRect.set(focusedRect);
        if (direction == 17) {
            this.mBestCandidateRect.offset(focusedRect.width() + 1, 0);
        } else if (direction == 33) {
            this.mBestCandidateRect.offset(0, focusedRect.height() + 1);
        } else if (direction == 66) {
            this.mBestCandidateRect.offset(-(focusedRect.width() + 1), 0);
        } else if (direction == 130) {
            this.mBestCandidateRect.offset(0, -(focusedRect.height() + 1));
        }
        View closest = null;
        int numFocusables = focusables.size();
        for (int i = 0; i < numFocusables; i++) {
            View focusable = focusables.get(i);
            if (!(focusable == focused || focusable == root)) {
                focusable.getFocusedRect(this.mOtherRect);
                root.offsetDescendantRectToMyCoords(focusable, this.mOtherRect);
                if (isBetterCandidate(direction, focusedRect, this.mOtherRect, this.mBestCandidateRect)) {
                    this.mBestCandidateRect.set(this.mOtherRect);
                    closest = focusable;
                }
            }
        }
        return closest;
    }

    private static View getNextFocusable(View focused, ArrayList<View> focusables, int count) {
        int position;
        if (focused != null && (position = focusables.lastIndexOf(focused)) >= 0 && position + 1 < count) {
            return focusables.get(position + 1);
        }
        if (focusables.isEmpty() == 0) {
            return focusables.get(0);
        }
        return null;
    }

    private static View getPreviousFocusable(View focused, ArrayList<View> focusables, int count) {
        int position;
        if (focused != null && (position = focusables.indexOf(focused)) > 0) {
            return focusables.get(position - 1);
        }
        if (focusables.isEmpty() == 0) {
            return focusables.get(count - 1);
        }
        return null;
    }

    private static View getNextKeyboardNavigationCluster(View root, View currentCluster, List<View> clusters, int count) {
        if (currentCluster == null) {
            return clusters.get(0);
        }
        int position = clusters.lastIndexOf(currentCluster);
        if (position < 0 || position + 1 >= count) {
            return root;
        }
        return clusters.get(position + 1);
    }

    private static View getPreviousKeyboardNavigationCluster(View root, View currentCluster, List<View> clusters, int count) {
        if (currentCluster == null) {
            return clusters.get(count - 1);
        }
        int position = clusters.indexOf(currentCluster);
        if (position > 0) {
            return clusters.get(position - 1);
        }
        return root;
    }

    /* access modifiers changed from: package-private */
    public boolean isBetterCandidate(int direction, Rect source, Rect rect1, Rect rect2) {
        if (!isCandidate(source, rect1, direction)) {
            return false;
        }
        if (!isCandidate(source, rect2, direction) || beamBeats(direction, source, rect1, rect2)) {
            return true;
        }
        if (!beamBeats(direction, source, rect2, rect1) && getWeightedDistanceFor((long) majorAxisDistance(direction, source, rect1), (long) minorAxisDistance(direction, source, rect1)) < getWeightedDistanceFor((long) majorAxisDistance(direction, source, rect2), (long) minorAxisDistance(direction, source, rect2))) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean beamBeats(int direction, Rect source, Rect rect1, Rect rect2) {
        boolean rect1InSrcBeam = beamsOverlap(direction, source, rect1);
        if (beamsOverlap(direction, source, rect2) || !rect1InSrcBeam) {
            return false;
        }
        if (!isToDirectionOf(direction, source, rect2) || direction == 17 || direction == 66) {
            return true;
        }
        if (majorAxisDistance(direction, source, rect1) < majorAxisDistanceToFarEdge(direction, source, rect2)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public long getWeightedDistanceFor(long majorAxisDistance, long minorAxisDistance) {
        return (13 * majorAxisDistance * majorAxisDistance) + (minorAxisDistance * minorAxisDistance);
    }

    /* access modifiers changed from: package-private */
    public boolean isCandidate(Rect srcRect, Rect destRect, int direction) {
        if (direction != 17) {
            if (direction != 33) {
                if (direction != 66) {
                    if (direction == 130) {
                        return (srcRect.top < destRect.top || srcRect.bottom <= destRect.top) && srcRect.bottom < destRect.bottom;
                    }
                    throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                } else if ((srcRect.left < destRect.left || srcRect.right <= destRect.left) && srcRect.right < destRect.right) {
                    return true;
                } else {
                    return false;
                }
            } else if ((srcRect.bottom > destRect.bottom || srcRect.top >= destRect.bottom) && srcRect.top > destRect.top) {
                return true;
            } else {
                return false;
            }
        } else if ((srcRect.right > destRect.right || srcRect.left >= destRect.right) && srcRect.left > destRect.left) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean beamsOverlap(int direction, Rect rect1, Rect rect2) {
        if (direction != 17) {
            if (direction != 33) {
                if (direction != 66) {
                    if (direction != 130) {
                        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    }
                }
            }
            if (rect2.right <= rect1.left || rect2.left >= rect1.right) {
                return false;
            }
            return true;
        }
        if (rect2.bottom <= rect1.top || rect2.top >= rect1.bottom) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isToDirectionOf(int direction, Rect src, Rect dest) {
        if (direction != 17) {
            if (direction != 33) {
                if (direction != 66) {
                    if (direction != 130) {
                        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    } else if (src.bottom <= dest.top) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (src.right <= dest.left) {
                    return true;
                } else {
                    return false;
                }
            } else if (src.top >= dest.bottom) {
                return true;
            } else {
                return false;
            }
        } else if (src.left >= dest.right) {
            return true;
        } else {
            return false;
        }
    }

    static int majorAxisDistance(int direction, Rect source, Rect dest) {
        return Math.max(0, majorAxisDistanceRaw(direction, source, dest));
    }

    static int majorAxisDistanceRaw(int direction, Rect source, Rect dest) {
        if (direction == 17) {
            return source.left - dest.right;
        }
        if (direction == 33) {
            return source.top - dest.bottom;
        }
        if (direction == 66) {
            return dest.left - source.right;
        }
        if (direction == 130) {
            return dest.top - source.bottom;
        }
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }

    static int majorAxisDistanceToFarEdge(int direction, Rect source, Rect dest) {
        return Math.max(1, majorAxisDistanceToFarEdgeRaw(direction, source, dest));
    }

    static int majorAxisDistanceToFarEdgeRaw(int direction, Rect source, Rect dest) {
        if (direction == 17) {
            return source.left - dest.left;
        }
        if (direction == 33) {
            return source.top - dest.top;
        }
        if (direction == 66) {
            return dest.right - source.right;
        }
        if (direction == 130) {
            return dest.bottom - source.bottom;
        }
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }

    static int minorAxisDistance(int direction, Rect source, Rect dest) {
        if (direction != 17) {
            if (direction != 33) {
                if (direction != 66) {
                    if (direction != 130) {
                        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    }
                }
            }
            return Math.abs((source.left + (source.width() / 2)) - (dest.left + (dest.width() / 2)));
        }
        return Math.abs((source.top + (source.height() / 2)) - (dest.top + (dest.height() / 2)));
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.view.View findNearestTouchable(android.view.ViewGroup r19, int r20, int r21, int r22, int[] r23) {
        /*
            r18 = this;
            r0 = r18
            r1 = r19
            r2 = r20
            r3 = r21
            r4 = r22
            java.util.ArrayList r5 = r19.getTouchables()
            r6 = 2147483647(0x7fffffff, float:NaN)
            r7 = 0
            int r8 = r5.size()
            android.content.Context r9 = r1.mContext
            android.view.ViewConfiguration r9 = android.view.ViewConfiguration.get(r9)
            int r9 = r9.getScaledEdgeSlop()
            android.graphics.Rect r10 = new android.graphics.Rect
            r10.<init>()
            android.graphics.Rect r11 = r0.mOtherRect
            r13 = r6
            r6 = 0
        L_0x0029:
            if (r6 >= r8) goto L_0x00af
            java.lang.Object r14 = r5.get(r6)
            android.view.View r14 = (android.view.View) r14
            r14.getDrawingRect(r11)
            r12 = 1
            r1.offsetRectBetweenParentAndChild(r14, r11, r12, r12)
            boolean r16 = r0.isTouchCandidate(r2, r3, r11, r4)
            if (r16 != 0) goto L_0x003f
            goto L_0x0092
        L_0x003f:
            r16 = 2147483647(0x7fffffff, float:NaN)
            r12 = 33
            r0 = 17
            if (r4 == r0) goto L_0x0064
            if (r4 == r12) goto L_0x005b
            r12 = 66
            if (r4 == r12) goto L_0x0058
            r12 = 130(0x82, float:1.82E-43)
            if (r4 == r12) goto L_0x0055
        L_0x0052:
            r12 = r16
            goto L_0x006d
        L_0x0055:
            int r12 = r11.top
            goto L_0x006d
        L_0x0058:
            int r12 = r11.left
            goto L_0x006d
        L_0x005b:
            int r12 = r11.bottom
            int r12 = r3 - r12
            r17 = 1
            int r16 = r12 + 1
            goto L_0x0052
        L_0x0064:
            r17 = 1
            int r12 = r11.right
            int r12 = r2 - r12
            int r16 = r12 + 1
            goto L_0x0052
        L_0x006d:
            if (r12 >= r9) goto L_0x0092
            if (r7 == 0) goto L_0x007f
            boolean r16 = r10.contains(r11)
            if (r16 != 0) goto L_0x007f
            boolean r16 = r11.contains(r10)
            if (r16 != 0) goto L_0x0092
            if (r12 >= r13) goto L_0x0092
        L_0x007f:
            r13 = r12
            r7 = r14
            r10.set(r11)
            if (r4 == r0) goto L_0x00a2
            r0 = 33
            if (r4 == r0) goto L_0x009c
            r0 = 66
            if (r4 == r0) goto L_0x0098
            r0 = 130(0x82, float:1.82E-43)
            if (r4 == r0) goto L_0x0094
        L_0x0092:
            r15 = 0
            goto L_0x00a7
        L_0x0094:
            r0 = 1
            r23[r0] = r12
            goto L_0x0092
        L_0x0098:
            r15 = 0
            r23[r15] = r12
            goto L_0x00a7
        L_0x009c:
            r0 = 1
            r15 = 0
            int r1 = -r12
            r23[r0] = r1
            goto L_0x00a7
        L_0x00a2:
            r15 = 0
            int r0 = -r12
            r23[r15] = r0
        L_0x00a7:
            int r6 = r6 + 1
            r0 = r18
            r1 = r19
            goto L_0x0029
        L_0x00af:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.FocusFinder.findNearestTouchable(android.view.ViewGroup, int, int, int, int[]):android.view.View");
    }

    private boolean isTouchCandidate(int x, int y, Rect destRect, int direction) {
        if (direction != 17) {
            if (direction != 33) {
                if (direction != 66) {
                    if (direction != 130) {
                        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    } else if (destRect.top < y || destRect.left > x || x > destRect.right) {
                        return false;
                    } else {
                        return true;
                    }
                } else if (destRect.left < x || destRect.top > y || y > destRect.bottom) {
                    return false;
                } else {
                    return true;
                }
            } else if (destRect.top > y || destRect.left > x || x > destRect.right) {
                return false;
            } else {
                return true;
            }
        } else if (destRect.left > x || destRect.top > y || y > destRect.bottom) {
            return false;
        } else {
            return true;
        }
    }

    private static final boolean isValidId(int id) {
        return (id == 0 || id == -1) ? false : true;
    }

    static final class FocusSorter {
        private int mLastPoolRect;
        private HashMap<View, Rect> mRectByView = null;
        private ArrayList<Rect> mRectPool = new ArrayList<>();
        private int mRtlMult;
        private Comparator<View> mSidesComparator = new Comparator() {
            public final int compare(Object obj, Object obj2) {
                return FocusFinder.FocusSorter.lambda$new$1(FocusFinder.FocusSorter.this, (View) obj, (View) obj2);
            }
        };
        private Comparator<View> mTopsComparator = new Comparator() {
            public final int compare(Object obj, Object obj2) {
                return FocusFinder.FocusSorter.lambda$new$0(FocusFinder.FocusSorter.this, (View) obj, (View) obj2);
            }
        };

        FocusSorter() {
        }

        public static /* synthetic */ int lambda$new$0(FocusSorter focusSorter, View first, View second) {
            if (first == second) {
                return 0;
            }
            Rect firstRect = focusSorter.mRectByView.get(first);
            Rect secondRect = focusSorter.mRectByView.get(second);
            int result = firstRect.top - secondRect.top;
            if (result == 0) {
                return firstRect.bottom - secondRect.bottom;
            }
            return result;
        }

        public static /* synthetic */ int lambda$new$1(FocusSorter focusSorter, View first, View second) {
            if (first == second) {
                return 0;
            }
            Rect firstRect = focusSorter.mRectByView.get(first);
            Rect secondRect = focusSorter.mRectByView.get(second);
            int result = firstRect.left - secondRect.left;
            if (result == 0) {
                return firstRect.right - secondRect.right;
            }
            return focusSorter.mRtlMult * result;
        }

        public void sort(View[] views, int start, int end, ViewGroup root, boolean isRtl) {
            int count = end - start;
            if (count >= 2) {
                if (this.mRectByView == null) {
                    this.mRectByView = new HashMap<>();
                }
                this.mRtlMult = isRtl ? -1 : 1;
                for (int i = this.mRectPool.size(); i < count; i++) {
                    this.mRectPool.add(new Rect());
                }
                for (int i2 = start; i2 < end; i2++) {
                    ArrayList<Rect> arrayList = this.mRectPool;
                    int i3 = this.mLastPoolRect;
                    this.mLastPoolRect = i3 + 1;
                    Rect next = arrayList.get(i3);
                    views[i2].getDrawingRect(next);
                    root.offsetDescendantRectToMyCoords(views[i2], next);
                    this.mRectByView.put(views[i2], next);
                }
                Arrays.sort(views, start, count, this.mTopsComparator);
                int sweepBottom = this.mRectByView.get(views[start]).bottom;
                int rowStart = start;
                int sweepIdx = start + 1;
                while (sweepIdx < end) {
                    Rect currRect = this.mRectByView.get(views[sweepIdx]);
                    if (currRect.top >= sweepBottom) {
                        if (sweepIdx - rowStart > 1) {
                            Arrays.sort(views, rowStart, sweepIdx, this.mSidesComparator);
                        }
                        sweepBottom = currRect.bottom;
                        rowStart = sweepIdx;
                    } else {
                        sweepBottom = Math.max(sweepBottom, currRect.bottom);
                    }
                    sweepIdx++;
                }
                if (sweepIdx - rowStart > 1) {
                    Arrays.sort(views, rowStart, sweepIdx, this.mSidesComparator);
                }
                this.mLastPoolRect = 0;
                this.mRectByView.clear();
            }
        }
    }

    public static void sort(View[] views, int start, int end, ViewGroup root, boolean isRtl) {
        getInstance().mFocusSorter.sort(views, start, end, root, isRtl);
    }

    private static final class UserSpecifiedFocusComparator implements Comparator<View> {
        private final ArrayMap<View, View> mHeadsOfChains = new ArrayMap<>();
        private final ArraySet<View> mIsConnectedTo = new ArraySet<>();
        private final ArrayMap<View, View> mNextFoci = new ArrayMap<>();
        private final NextFocusGetter mNextFocusGetter;
        private final ArrayMap<View, Integer> mOriginalOrdinal = new ArrayMap<>();
        private View mRoot;

        public interface NextFocusGetter {
            View get(View view, View view2);
        }

        UserSpecifiedFocusComparator(NextFocusGetter nextFocusGetter) {
            this.mNextFocusGetter = nextFocusGetter;
        }

        public void recycle() {
            this.mRoot = null;
            this.mHeadsOfChains.clear();
            this.mIsConnectedTo.clear();
            this.mOriginalOrdinal.clear();
            this.mNextFoci.clear();
        }

        public void setFocusables(List<View> focusables, View root) {
            this.mRoot = root;
            for (int i = 0; i < focusables.size(); i++) {
                this.mOriginalOrdinal.put(focusables.get(i), Integer.valueOf(i));
            }
            for (int i2 = focusables.size() - 1; i2 >= 0; i2--) {
                View view = focusables.get(i2);
                View next = this.mNextFocusGetter.get(this.mRoot, view);
                if (next != null && this.mOriginalOrdinal.containsKey(next)) {
                    this.mNextFoci.put(view, next);
                    this.mIsConnectedTo.add(next);
                }
            }
            for (int i3 = focusables.size() - 1; i3 >= 0; i3--) {
                View view2 = focusables.get(i3);
                if (this.mNextFoci.get(view2) != null && !this.mIsConnectedTo.contains(view2)) {
                    setHeadOfChain(view2);
                }
            }
        }

        private void setHeadOfChain(View view) {
            View head = view;
            while (view != null) {
                View otherHead = this.mHeadsOfChains.get(view);
                if (otherHead != null) {
                    if (otherHead != head) {
                        view = head;
                        head = otherHead;
                    } else {
                        return;
                    }
                }
                this.mHeadsOfChains.put(view, head);
                view = this.mNextFoci.get(view);
            }
        }

        public int compare(View first, View second) {
            if (first == second) {
                return 0;
            }
            View firstHead = this.mHeadsOfChains.get(first);
            View secondHead = this.mHeadsOfChains.get(second);
            if (firstHead != secondHead || firstHead == null) {
                boolean involvesChain = false;
                if (firstHead != null) {
                    first = firstHead;
                    involvesChain = true;
                }
                if (secondHead != null) {
                    second = secondHead;
                    involvesChain = true;
                }
                if (!involvesChain) {
                    return 0;
                }
                if (this.mOriginalOrdinal.get(first).intValue() < this.mOriginalOrdinal.get(second).intValue()) {
                    return -1;
                }
                return 1;
            } else if (first == firstHead) {
                return -1;
            } else {
                return (second == firstHead || this.mNextFoci.get(first) == null) ? 1 : -1;
            }
        }
    }
}
