package com.android.internal.widget.helper;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import com.android.internal.R;
import com.android.internal.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ItemTouchHelper extends RecyclerView.ItemDecoration implements RecyclerView.OnChildAttachStateChangeListener {
    static final int ACTION_MODE_DRAG_MASK = 16711680;
    private static final int ACTION_MODE_IDLE_MASK = 255;
    static final int ACTION_MODE_SWIPE_MASK = 65280;
    public static final int ACTION_STATE_DRAG = 2;
    public static final int ACTION_STATE_IDLE = 0;
    public static final int ACTION_STATE_SWIPE = 1;
    static final int ACTIVE_POINTER_ID_NONE = -1;
    public static final int ANIMATION_TYPE_DRAG = 8;
    public static final int ANIMATION_TYPE_SWIPE_CANCEL = 4;
    public static final int ANIMATION_TYPE_SWIPE_SUCCESS = 2;
    static final boolean DEBUG = false;
    static final int DIRECTION_FLAG_COUNT = 8;
    public static final int DOWN = 2;
    public static final int END = 32;
    public static final int LEFT = 4;
    private static final int PIXELS_PER_SECOND = 1000;
    public static final int RIGHT = 8;
    public static final int START = 16;
    static final String TAG = "ItemTouchHelper";
    public static final int UP = 1;
    int mActionState = 0;
    int mActivePointerId = -1;
    Callback mCallback;
    private RecyclerView.ChildDrawingOrderCallback mChildDrawingOrderCallback = null;
    private List<Integer> mDistances;
    private long mDragScrollStartTimeInMs;
    float mDx;
    float mDy;
    GestureDetector mGestureDetector;
    float mInitialTouchX;
    float mInitialTouchY;
    float mMaxSwipeVelocity;
    private final RecyclerView.OnItemTouchListener mOnItemTouchListener = new RecyclerView.OnItemTouchListener() {
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
            int index;
            RecoverAnimation animation;
            ItemTouchHelper.this.mGestureDetector.onTouchEvent(event);
            int action = event.getActionMasked();
            if (action == 0) {
                ItemTouchHelper.this.mActivePointerId = event.getPointerId(0);
                ItemTouchHelper.this.mInitialTouchX = event.getX();
                ItemTouchHelper.this.mInitialTouchY = event.getY();
                ItemTouchHelper.this.obtainVelocityTracker();
                if (ItemTouchHelper.this.mSelected == null && (animation = ItemTouchHelper.this.findAnimation(event)) != null) {
                    ItemTouchHelper.this.mInitialTouchX -= animation.mX;
                    ItemTouchHelper.this.mInitialTouchY -= animation.mY;
                    ItemTouchHelper.this.endRecoverAnimation(animation.mViewHolder, true);
                    if (ItemTouchHelper.this.mPendingCleanup.remove(animation.mViewHolder.itemView)) {
                        ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, animation.mViewHolder);
                    }
                    ItemTouchHelper.this.select(animation.mViewHolder, animation.mActionState);
                    ItemTouchHelper.this.updateDxDy(event, ItemTouchHelper.this.mSelectedFlags, 0);
                }
            } else if (action == 3 || action == 1) {
                ItemTouchHelper.this.mActivePointerId = -1;
                ItemTouchHelper.this.select((RecyclerView.ViewHolder) null, 0);
            } else if (ItemTouchHelper.this.mActivePointerId != -1 && (index = event.findPointerIndex(ItemTouchHelper.this.mActivePointerId)) >= 0) {
                ItemTouchHelper.this.checkSelectForSwipe(action, event, index);
            }
            if (ItemTouchHelper.this.mVelocityTracker != null) {
                ItemTouchHelper.this.mVelocityTracker.addMovement(event);
            }
            if (ItemTouchHelper.this.mSelected != null) {
                return true;
            }
            return false;
        }

        public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {
            ItemTouchHelper.this.mGestureDetector.onTouchEvent(event);
            if (ItemTouchHelper.this.mVelocityTracker != null) {
                ItemTouchHelper.this.mVelocityTracker.addMovement(event);
            }
            if (ItemTouchHelper.this.mActivePointerId != -1) {
                int action = event.getActionMasked();
                int activePointerIndex = event.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
                if (activePointerIndex >= 0) {
                    ItemTouchHelper.this.checkSelectForSwipe(action, event, activePointerIndex);
                }
                RecyclerView.ViewHolder viewHolder = ItemTouchHelper.this.mSelected;
                if (viewHolder != null) {
                    int newPointerIndex = 0;
                    if (action != 6) {
                        switch (action) {
                            case 1:
                                break;
                            case 2:
                                if (activePointerIndex >= 0) {
                                    ItemTouchHelper.this.updateDxDy(event, ItemTouchHelper.this.mSelectedFlags, activePointerIndex);
                                    ItemTouchHelper.this.moveIfNecessary(viewHolder);
                                    ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                                    ItemTouchHelper.this.mScrollRunnable.run();
                                    ItemTouchHelper.this.mRecyclerView.invalidate();
                                    return;
                                }
                                return;
                            case 3:
                                if (ItemTouchHelper.this.mVelocityTracker != null) {
                                    ItemTouchHelper.this.mVelocityTracker.clear();
                                    break;
                                }
                                break;
                            default:
                                return;
                        }
                        ItemTouchHelper.this.select((RecyclerView.ViewHolder) null, 0);
                        ItemTouchHelper.this.mActivePointerId = -1;
                        return;
                    }
                    int pointerIndex = event.getActionIndex();
                    if (event.getPointerId(pointerIndex) == ItemTouchHelper.this.mActivePointerId) {
                        if (pointerIndex == 0) {
                            newPointerIndex = 1;
                        }
                        ItemTouchHelper.this.mActivePointerId = event.getPointerId(newPointerIndex);
                        ItemTouchHelper.this.updateDxDy(event, ItemTouchHelper.this.mSelectedFlags, pointerIndex);
                    }
                }
            }
        }

        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            if (disallowIntercept) {
                ItemTouchHelper.this.select((RecyclerView.ViewHolder) null, 0);
            }
        }
    };
    View mOverdrawChild = null;
    int mOverdrawChildPosition = -1;
    final List<View> mPendingCleanup = new ArrayList();
    List<RecoverAnimation> mRecoverAnimations = new ArrayList();
    RecyclerView mRecyclerView;
    final Runnable mScrollRunnable = new Runnable() {
        public void run() {
            if (ItemTouchHelper.this.mSelected != null && ItemTouchHelper.this.scrollIfNecessary()) {
                if (ItemTouchHelper.this.mSelected != null) {
                    ItemTouchHelper.this.moveIfNecessary(ItemTouchHelper.this.mSelected);
                }
                ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                ItemTouchHelper.this.mRecyclerView.postOnAnimation(this);
            }
        }
    };
    RecyclerView.ViewHolder mSelected = null;
    int mSelectedFlags;
    float mSelectedStartX;
    float mSelectedStartY;
    private int mSlop;
    private List<RecyclerView.ViewHolder> mSwapTargets;
    float mSwipeEscapeVelocity;
    private final float[] mTmpPosition = new float[2];
    private Rect mTmpRect;
    VelocityTracker mVelocityTracker;

    public interface ViewDropHandler {
        void prepareForDrop(View view, View view2, int i, int i2);
    }

    public ItemTouchHelper(Callback callback) {
        this.mCallback = callback;
    }

    private static boolean hitTest(View child, float x, float y, float left, float top) {
        return x >= left && x <= ((float) child.getWidth()) + left && y >= top && y <= ((float) child.getHeight()) + top;
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        if (this.mRecyclerView != recyclerView) {
            if (this.mRecyclerView != null) {
                destroyCallbacks();
            }
            this.mRecyclerView = recyclerView;
            if (this.mRecyclerView != null) {
                Resources resources = recyclerView.getResources();
                this.mSwipeEscapeVelocity = resources.getDimension(R.dimen.item_touch_helper_swipe_escape_velocity);
                this.mMaxSwipeVelocity = resources.getDimension(R.dimen.item_touch_helper_swipe_escape_max_velocity);
                setupCallbacks();
            }
        }
    }

    private void setupCallbacks() {
        this.mSlop = ViewConfiguration.get(this.mRecyclerView.getContext()).getScaledTouchSlop();
        this.mRecyclerView.addItemDecoration(this);
        this.mRecyclerView.addOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.addOnChildAttachStateChangeListener(this);
        initGestureDetector();
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration(this);
        this.mRecyclerView.removeOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.removeOnChildAttachStateChangeListener(this);
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; i--) {
            this.mCallback.clearView(this.mRecyclerView, this.mRecoverAnimations.get(0).mViewHolder);
        }
        this.mRecoverAnimations.clear();
        this.mOverdrawChild = null;
        this.mOverdrawChildPosition = -1;
        releaseVelocityTracker();
    }

    private void initGestureDetector() {
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new GestureDetector(this.mRecyclerView.getContext(), (GestureDetector.OnGestureListener) new ItemTouchHelperGestureListener());
        }
    }

    private void getSelectedDxDy(float[] outPosition) {
        if ((this.mSelectedFlags & 12) != 0) {
            outPosition[0] = (this.mSelectedStartX + this.mDx) - ((float) this.mSelected.itemView.getLeft());
        } else {
            outPosition[0] = this.mSelected.itemView.getTranslationX();
        }
        if ((this.mSelectedFlags & 3) != 0) {
            outPosition[1] = (this.mSelectedStartY + this.mDy) - ((float) this.mSelected.itemView.getTop());
        } else {
            outPosition[1] = this.mSelected.itemView.getTranslationY();
        }
    }

    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        float dx = 0.0f;
        float dy = 0.0f;
        if (this.mSelected != null) {
            getSelectedDxDy(this.mTmpPosition);
            dx = this.mTmpPosition[0];
            dy = this.mTmpPosition[1];
        }
        this.mCallback.onDrawOver(c, parent, this.mSelected, this.mRecoverAnimations, this.mActionState, dx, dy);
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        this.mOverdrawChildPosition = -1;
        float dx = 0.0f;
        float dy = 0.0f;
        if (this.mSelected != null) {
            getSelectedDxDy(this.mTmpPosition);
            dx = this.mTmpPosition[0];
            dy = this.mTmpPosition[1];
        }
        this.mCallback.onDraw(c, parent, this.mSelected, this.mRecoverAnimations, this.mActionState, dx, dy);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x013f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void select(com.android.internal.widget.RecyclerView.ViewHolder r28, int r29) {
        /*
            r27 = this;
            r11 = r27
            r12 = r28
            r13 = r29
            com.android.internal.widget.RecyclerView$ViewHolder r0 = r11.mSelected
            if (r12 != r0) goto L_0x000f
            int r0 = r11.mActionState
            if (r13 != r0) goto L_0x000f
            return
        L_0x000f:
            r0 = -9223372036854775808
            r11.mDragScrollStartTimeInMs = r0
            int r14 = r11.mActionState
            r15 = 1
            r11.endRecoverAnimation(r12, r15)
            r11.mActionState = r13
            r10 = 2
            if (r13 != r10) goto L_0x0025
            android.view.View r0 = r12.itemView
            r11.mOverdrawChild = r0
            r27.addChildDrawingOrderCallback()
        L_0x0025:
            int r0 = r13 * 8
            r9 = 8
            int r0 = r0 + r9
            int r0 = r15 << r0
            int r16 = r0 + -1
            r17 = 0
            com.android.internal.widget.RecyclerView$ViewHolder r0 = r11.mSelected
            r8 = 0
            if (r0 == 0) goto L_0x00f4
            com.android.internal.widget.RecyclerView$ViewHolder r7 = r11.mSelected
            android.view.View r0 = r7.itemView
            android.view.ViewParent r0 = r0.getParent()
            if (r0 == 0) goto L_0x00db
            if (r14 != r10) goto L_0x0043
            r0 = r8
            goto L_0x0047
        L_0x0043:
            int r0 = r11.swipeIfNecessary(r7)
        L_0x0047:
            r6 = r0
            r27.releaseVelocityTracker()
            r0 = 4
            if (r6 == r0) goto L_0x0070
            if (r6 == r9) goto L_0x0070
            r1 = 16
            if (r6 == r1) goto L_0x0070
            r1 = 32
            if (r6 == r1) goto L_0x0070
            switch(r6) {
                case 1: goto L_0x0060;
                case 2: goto L_0x0060;
                default: goto L_0x005b;
            }
        L_0x005b:
            r1 = 0
            r2 = 0
        L_0x005d:
            r18 = r1
            goto L_0x0080
        L_0x0060:
            r1 = 0
            float r2 = r11.mDy
            float r2 = java.lang.Math.signum(r2)
            com.android.internal.widget.RecyclerView r3 = r11.mRecyclerView
            int r3 = r3.getHeight()
            float r3 = (float) r3
            float r2 = r2 * r3
            goto L_0x005d
        L_0x0070:
            r2 = 0
            float r1 = r11.mDx
            float r1 = java.lang.Math.signum(r1)
            com.android.internal.widget.RecyclerView r3 = r11.mRecyclerView
            int r3 = r3.getWidth()
            float r3 = (float) r3
            float r1 = r1 * r3
            goto L_0x005d
        L_0x0080:
            r19 = r2
            if (r14 != r10) goto L_0x0087
            r0 = 8
            goto L_0x008c
        L_0x0087:
            if (r6 <= 0) goto L_0x008b
            r0 = 2
            goto L_0x008c
        L_0x008b:
        L_0x008c:
            r5 = r0
            float[] r0 = r11.mTmpPosition
            r11.getSelectedDxDy(r0)
            float[] r0 = r11.mTmpPosition
            r20 = r0[r8]
            float[] r0 = r11.mTmpPosition
            r21 = r0[r15]
            com.android.internal.widget.helper.ItemTouchHelper$3 r22 = new com.android.internal.widget.helper.ItemTouchHelper$3
            r0 = r22
            r1 = r27
            r2 = r7
            r3 = r5
            r4 = r14
            r15 = r5
            r5 = r20
            r23 = r6
            r6 = r21
            r24 = r7
            r7 = r18
            r25 = r14
            r14 = r8
            r8 = r19
            r26 = r9
            r9 = r23
            r14 = r10
            r10 = r24
            r0.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10)
            com.android.internal.widget.helper.ItemTouchHelper$Callback r1 = r11.mCallback
            com.android.internal.widget.RecyclerView r2 = r11.mRecyclerView
            float r3 = r18 - r20
            float r4 = r19 - r21
            long r1 = r1.getAnimationDuration(r2, r15, r3, r4)
            r0.setDuration(r1)
            java.util.List<com.android.internal.widget.helper.ItemTouchHelper$RecoverAnimation> r3 = r11.mRecoverAnimations
            r3.add(r0)
            r0.start()
            r0 = 1
            r17 = r0
            r0 = r24
            goto L_0x00f0
        L_0x00db:
            r24 = r7
            r26 = r9
            r25 = r14
            r14 = r10
            r0 = r24
            android.view.View r1 = r0.itemView
            r11.removeChildDrawingOrderCallbackIfNecessary(r1)
            com.android.internal.widget.helper.ItemTouchHelper$Callback r1 = r11.mCallback
            com.android.internal.widget.RecyclerView r2 = r11.mRecyclerView
            r1.clearView(r2, r0)
        L_0x00f0:
            r1 = 0
            r11.mSelected = r1
            goto L_0x00f9
        L_0x00f4:
            r26 = r9
            r25 = r14
            r14 = r10
        L_0x00f9:
            if (r12 == 0) goto L_0x012b
            com.android.internal.widget.helper.ItemTouchHelper$Callback r0 = r11.mCallback
            com.android.internal.widget.RecyclerView r1 = r11.mRecyclerView
            int r0 = r0.getAbsoluteMovementFlags(r1, r12)
            r0 = r0 & r16
            int r1 = r11.mActionState
            int r1 = r1 * 8
            int r0 = r0 >> r1
            r11.mSelectedFlags = r0
            android.view.View r0 = r12.itemView
            int r0 = r0.getLeft()
            float r0 = (float) r0
            r11.mSelectedStartX = r0
            android.view.View r0 = r12.itemView
            int r0 = r0.getTop()
            float r0 = (float) r0
            r11.mSelectedStartY = r0
            r11.mSelected = r12
            if (r13 != r14) goto L_0x012b
            com.android.internal.widget.RecyclerView$ViewHolder r0 = r11.mSelected
            android.view.View r0 = r0.itemView
            r1 = 0
            r0.performHapticFeedback(r1)
            goto L_0x012c
        L_0x012b:
            r1 = 0
        L_0x012c:
            com.android.internal.widget.RecyclerView r0 = r11.mRecyclerView
            android.view.ViewParent r0 = r0.getParent()
            if (r0 == 0) goto L_0x013d
            com.android.internal.widget.RecyclerView$ViewHolder r2 = r11.mSelected
            if (r2 == 0) goto L_0x013a
            r1 = 1
        L_0x013a:
            r0.requestDisallowInterceptTouchEvent(r1)
        L_0x013d:
            if (r17 != 0) goto L_0x0148
            com.android.internal.widget.RecyclerView r1 = r11.mRecyclerView
            com.android.internal.widget.RecyclerView$LayoutManager r1 = r1.getLayoutManager()
            r1.requestSimpleAnimationsInNextLayout()
        L_0x0148:
            com.android.internal.widget.helper.ItemTouchHelper$Callback r1 = r11.mCallback
            com.android.internal.widget.RecyclerView$ViewHolder r2 = r11.mSelected
            int r3 = r11.mActionState
            r1.onSelectedChanged(r2, r3)
            com.android.internal.widget.RecyclerView r1 = r11.mRecyclerView
            r1.invalidate()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.helper.ItemTouchHelper.select(com.android.internal.widget.RecyclerView$ViewHolder, int):void");
    }

    /* access modifiers changed from: package-private */
    public void postDispatchSwipe(final RecoverAnimation anim, final int swipeDir) {
        this.mRecyclerView.post(new Runnable() {
            public void run() {
                if (ItemTouchHelper.this.mRecyclerView != null && ItemTouchHelper.this.mRecyclerView.isAttachedToWindow() && !anim.mOverridden && anim.mViewHolder.getAdapterPosition() != -1) {
                    RecyclerView.ItemAnimator animator = ItemTouchHelper.this.mRecyclerView.getItemAnimator();
                    if ((animator == null || !animator.isRunning((RecyclerView.ItemAnimator.ItemAnimatorFinishedListener) null)) && !ItemTouchHelper.this.hasRunningRecoverAnim()) {
                        ItemTouchHelper.this.mCallback.onSwiped(anim.mViewHolder, swipeDir);
                    } else {
                        ItemTouchHelper.this.mRecyclerView.post(this);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public boolean hasRunningRecoverAnim() {
        int size = this.mRecoverAnimations.size();
        for (int i = 0; i < size; i++) {
            if (!this.mRecoverAnimations.get(i).mEnded) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean scrollIfNecessary() {
        int bottomDiff;
        int rightDiff;
        if (this.mSelected == null) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            return false;
        }
        long now = System.currentTimeMillis();
        long scrollDuration = this.mDragScrollStartTimeInMs == Long.MIN_VALUE ? 0 : now - this.mDragScrollStartTimeInMs;
        RecyclerView.LayoutManager lm = this.mRecyclerView.getLayoutManager();
        if (this.mTmpRect == null) {
            this.mTmpRect = new Rect();
        }
        int scrollX = 0;
        int scrollY = 0;
        lm.calculateItemDecorationsForChild(this.mSelected.itemView, this.mTmpRect);
        if (lm.canScrollHorizontally()) {
            int curX = (int) (this.mSelectedStartX + this.mDx);
            int leftDiff = (curX - this.mTmpRect.left) - this.mRecyclerView.getPaddingLeft();
            if (this.mDx < 0.0f && leftDiff < 0) {
                scrollX = leftDiff;
            } else if (this.mDx > 0.0f && (rightDiff = ((this.mSelected.itemView.getWidth() + curX) + this.mTmpRect.right) - (this.mRecyclerView.getWidth() - this.mRecyclerView.getPaddingRight())) > 0) {
                scrollX = rightDiff;
            }
        }
        if (lm.canScrollVertically()) {
            int curY = (int) (this.mSelectedStartY + this.mDy);
            int topDiff = (curY - this.mTmpRect.top) - this.mRecyclerView.getPaddingTop();
            if (this.mDy < 0.0f && topDiff < 0) {
                scrollY = topDiff;
            } else if (this.mDy > 0.0f && (bottomDiff = ((this.mSelected.itemView.getHeight() + curY) + this.mTmpRect.bottom) - (this.mRecyclerView.getHeight() - this.mRecyclerView.getPaddingBottom())) > 0) {
                scrollY = bottomDiff;
            }
        }
        if (scrollX != 0) {
            scrollX = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getWidth(), scrollX, this.mRecyclerView.getWidth(), scrollDuration);
        }
        if (scrollY != 0) {
            scrollY = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getHeight(), scrollY, this.mRecyclerView.getHeight(), scrollDuration);
        }
        if (scrollX == 0 && scrollY == 0) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            return false;
        }
        if (this.mDragScrollStartTimeInMs == Long.MIN_VALUE) {
            this.mDragScrollStartTimeInMs = now;
        }
        this.mRecyclerView.scrollBy(scrollX, scrollY);
        return true;
    }

    private List<RecyclerView.ViewHolder> findSwapTargets(RecyclerView.ViewHolder viewHolder) {
        int left;
        int margin;
        RecyclerView.ViewHolder viewHolder2 = viewHolder;
        if (this.mSwapTargets == null) {
            this.mSwapTargets = new ArrayList();
            this.mDistances = new ArrayList();
        } else {
            this.mSwapTargets.clear();
            this.mDistances.clear();
        }
        int margin2 = this.mCallback.getBoundingBoxMargin();
        int j = Math.round(this.mSelectedStartX + this.mDx) - margin2;
        int top = Math.round(this.mSelectedStartY + this.mDy) - margin2;
        int right = viewHolder2.itemView.getWidth() + j + (margin2 * 2);
        int bottom = viewHolder2.itemView.getHeight() + top + (margin2 * 2);
        int centerX = (j + right) / 2;
        int centerY = (top + bottom) / 2;
        RecyclerView.LayoutManager lm = this.mRecyclerView.getLayoutManager();
        int childCount = lm.getChildCount();
        int i = 0;
        while (i < childCount) {
            View other = lm.getChildAt(i);
            if (other != viewHolder2.itemView) {
                if (other.getBottom() >= top && other.getTop() <= bottom && other.getRight() >= j) {
                    if (other.getLeft() <= right) {
                        RecyclerView.ViewHolder otherVh = this.mRecyclerView.getChildViewHolder(other);
                        if (this.mCallback.canDropOver(this.mRecyclerView, this.mSelected, otherVh)) {
                            int dx = Math.abs(centerX - ((other.getLeft() + other.getRight()) / 2));
                            int dy = Math.abs(centerY - ((other.getTop() + other.getBottom()) / 2));
                            int dist = (dx * dx) + (dy * dy);
                            int i2 = dx;
                            int cnt = this.mSwapTargets.size();
                            margin = margin2;
                            int pos = 0;
                            int pos2 = 0;
                            while (true) {
                                left = j;
                                int left2 = pos2;
                                if (left2 >= cnt) {
                                    break;
                                }
                                int cnt2 = cnt;
                                if (dist <= this.mDistances.get(left2).intValue()) {
                                    break;
                                }
                                pos++;
                                pos2 = left2 + 1;
                                j = left;
                                cnt = cnt2;
                            }
                            this.mSwapTargets.add(pos, otherVh);
                            this.mDistances.add(pos, Integer.valueOf(dist));
                            i++;
                            margin2 = margin;
                            j = left;
                            viewHolder2 = viewHolder;
                        }
                    }
                }
                margin = margin2;
                left = j;
                i++;
                margin2 = margin;
                j = left;
                viewHolder2 = viewHolder;
            }
            margin = margin2;
            left = j;
            i++;
            margin2 = margin;
            j = left;
            viewHolder2 = viewHolder;
        }
        int i3 = j;
        return this.mSwapTargets;
    }

    /* access modifiers changed from: package-private */
    public void moveIfNecessary(RecyclerView.ViewHolder viewHolder) {
        RecyclerView.ViewHolder viewHolder2 = viewHolder;
        if (!this.mRecyclerView.isLayoutRequested() && this.mActionState == 2) {
            float threshold = this.mCallback.getMoveThreshold(viewHolder2);
            int x = (int) (this.mSelectedStartX + this.mDx);
            int y = (int) (this.mSelectedStartY + this.mDy);
            if (((float) Math.abs(y - viewHolder2.itemView.getTop())) >= ((float) viewHolder2.itemView.getHeight()) * threshold || ((float) Math.abs(x - viewHolder2.itemView.getLeft())) >= ((float) viewHolder2.itemView.getWidth()) * threshold) {
                List<RecyclerView.ViewHolder> swapTargets = findSwapTargets(viewHolder);
                if (swapTargets.size() != 0) {
                    RecyclerView.ViewHolder target = this.mCallback.chooseDropTarget(viewHolder2, swapTargets, x, y);
                    if (target == null) {
                        this.mSwapTargets.clear();
                        this.mDistances.clear();
                        return;
                    }
                    int toPosition = target.getAdapterPosition();
                    int fromPosition = viewHolder.getAdapterPosition();
                    if (this.mCallback.onMove(this.mRecyclerView, viewHolder2, target)) {
                        this.mCallback.onMoved(this.mRecyclerView, viewHolder, fromPosition, target, toPosition, x, y);
                    }
                }
            }
        }
    }

    public void onChildViewAttachedToWindow(View view) {
    }

    public void onChildViewDetachedFromWindow(View view) {
        removeChildDrawingOrderCallbackIfNecessary(view);
        RecyclerView.ViewHolder holder = this.mRecyclerView.getChildViewHolder(view);
        if (holder != null) {
            if (this.mSelected == null || holder != this.mSelected) {
                endRecoverAnimation(holder, false);
                if (this.mPendingCleanup.remove(holder.itemView)) {
                    this.mCallback.clearView(this.mRecyclerView, holder);
                    return;
                }
                return;
            }
            select((RecyclerView.ViewHolder) null, 0);
        }
    }

    /* access modifiers changed from: package-private */
    public int endRecoverAnimation(RecyclerView.ViewHolder viewHolder, boolean override) {
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; i--) {
            RecoverAnimation anim = this.mRecoverAnimations.get(i);
            if (anim.mViewHolder == viewHolder) {
                anim.mOverridden |= override;
                if (!anim.mEnded) {
                    anim.cancel();
                }
                this.mRecoverAnimations.remove(i);
                return anim.mAnimationType;
            }
        }
        return 0;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.setEmpty();
    }

    /* access modifiers changed from: package-private */
    public void obtainVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
        }
        this.mVelocityTracker = VelocityTracker.obtain();
    }

    private void releaseVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private RecyclerView.ViewHolder findSwipedView(MotionEvent motionEvent) {
        View child;
        RecyclerView.LayoutManager lm = this.mRecyclerView.getLayoutManager();
        if (this.mActivePointerId == -1) {
            return null;
        }
        int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
        float dx = motionEvent.getX(pointerIndex) - this.mInitialTouchX;
        float dy = motionEvent.getY(pointerIndex) - this.mInitialTouchY;
        float absDx = Math.abs(dx);
        float absDy = Math.abs(dy);
        if (absDx < ((float) this.mSlop) && absDy < ((float) this.mSlop)) {
            return null;
        }
        if (absDx > absDy && lm.canScrollHorizontally()) {
            return null;
        }
        if ((absDy <= absDx || !lm.canScrollVertically()) && (child = findChildView(motionEvent)) != null) {
            return this.mRecyclerView.getChildViewHolder(child);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public boolean checkSelectForSwipe(int action, MotionEvent motionEvent, int pointerIndex) {
        RecyclerView.ViewHolder vh;
        int swipeFlags;
        MotionEvent motionEvent2 = motionEvent;
        if (this.mSelected != null) {
            int i = action;
        } else if (action != 2 || this.mActionState == 2 || !this.mCallback.isItemViewSwipeEnabled() || this.mRecyclerView.getScrollState() == 1 || (vh = findSwipedView(motionEvent2)) == null || (swipeFlags = (65280 & this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, vh)) >> 8) == 0) {
            return false;
        } else {
            float x = motionEvent.getX(pointerIndex);
            float y = motionEvent.getY(pointerIndex);
            float dx = x - this.mInitialTouchX;
            float dy = y - this.mInitialTouchY;
            float absDx = Math.abs(dx);
            float absDy = Math.abs(dy);
            if (absDx < ((float) this.mSlop) && absDy < ((float) this.mSlop)) {
                return false;
            }
            if (absDx > absDy) {
                if (dx < 0.0f && (swipeFlags & 4) == 0) {
                    return false;
                }
                if (dx > 0.0f && (swipeFlags & 8) == 0) {
                    return false;
                }
            } else if (dy < 0.0f && (swipeFlags & 1) == 0) {
                return false;
            } else {
                if (dy > 0.0f && (swipeFlags & 2) == 0) {
                    return false;
                }
            }
            this.mDy = 0.0f;
            this.mDx = 0.0f;
            this.mActivePointerId = motionEvent2.getPointerId(0);
            select(vh, 1);
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public View findChildView(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (this.mSelected != null) {
            View selectedView = this.mSelected.itemView;
            if (hitTest(selectedView, x, y, this.mSelectedStartX + this.mDx, this.mSelectedStartY + this.mDy)) {
                return selectedView;
            }
        }
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; i--) {
            RecoverAnimation anim = this.mRecoverAnimations.get(i);
            View view = anim.mViewHolder.itemView;
            if (hitTest(view, x, y, anim.mX, anim.mY)) {
                return view;
            }
        }
        return this.mRecyclerView.findChildViewUnder(x, y);
    }

    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        if (!this.mCallback.hasDragFlag(this.mRecyclerView, viewHolder)) {
            Log.e(TAG, "Start drag has been called but dragging is not enabled");
        } else if (viewHolder.itemView.getParent() != this.mRecyclerView) {
            Log.e(TAG, "Start drag has been called with a view holder which is not a child of the RecyclerView which is controlled by this ItemTouchHelper.");
        } else {
            obtainVelocityTracker();
            this.mDy = 0.0f;
            this.mDx = 0.0f;
            select(viewHolder, 2);
        }
    }

    public void startSwipe(RecyclerView.ViewHolder viewHolder) {
        if (!this.mCallback.hasSwipeFlag(this.mRecyclerView, viewHolder)) {
            Log.e(TAG, "Start swipe has been called but swiping is not enabled");
        } else if (viewHolder.itemView.getParent() != this.mRecyclerView) {
            Log.e(TAG, "Start swipe has been called with a view holder which is not a child of the RecyclerView controlled by this ItemTouchHelper.");
        } else {
            obtainVelocityTracker();
            this.mDy = 0.0f;
            this.mDx = 0.0f;
            select(viewHolder, 1);
        }
    }

    /* access modifiers changed from: package-private */
    public RecoverAnimation findAnimation(MotionEvent event) {
        if (this.mRecoverAnimations.isEmpty()) {
            return null;
        }
        View target = findChildView(event);
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; i--) {
            RecoverAnimation anim = this.mRecoverAnimations.get(i);
            if (anim.mViewHolder.itemView == target) {
                return anim;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void updateDxDy(MotionEvent ev, int directionFlags, int pointerIndex) {
        float x = ev.getX(pointerIndex);
        float y = ev.getY(pointerIndex);
        this.mDx = x - this.mInitialTouchX;
        this.mDy = y - this.mInitialTouchY;
        if ((directionFlags & 4) == 0) {
            this.mDx = Math.max(0.0f, this.mDx);
        }
        if ((directionFlags & 8) == 0) {
            this.mDx = Math.min(0.0f, this.mDx);
        }
        if ((directionFlags & 1) == 0) {
            this.mDy = Math.max(0.0f, this.mDy);
        }
        if ((directionFlags & 2) == 0) {
            this.mDy = Math.min(0.0f, this.mDy);
        }
    }

    private int swipeIfNecessary(RecyclerView.ViewHolder viewHolder) {
        if (this.mActionState == 2) {
            return 0;
        }
        int originalMovementFlags = this.mCallback.getMovementFlags(this.mRecyclerView, viewHolder);
        int flags = (this.mCallback.convertToAbsoluteDirection(originalMovementFlags, this.mRecyclerView.getLayoutDirection()) & 65280) >> 8;
        if (flags == 0) {
            return 0;
        }
        int originalFlags = (65280 & originalMovementFlags) >> 8;
        if (Math.abs(this.mDx) > Math.abs(this.mDy)) {
            int checkHorizontalSwipe = checkHorizontalSwipe(viewHolder, flags);
            int swipeDir = checkHorizontalSwipe;
            if (checkHorizontalSwipe <= 0) {
                int checkVerticalSwipe = checkVerticalSwipe(viewHolder, flags);
                int swipeDir2 = checkVerticalSwipe;
                if (checkVerticalSwipe > 0) {
                    return swipeDir2;
                }
            } else if ((originalFlags & swipeDir) == 0) {
                return Callback.convertToRelativeDirection(swipeDir, this.mRecyclerView.getLayoutDirection());
            } else {
                return swipeDir;
            }
        } else {
            int checkVerticalSwipe2 = checkVerticalSwipe(viewHolder, flags);
            int swipeDir3 = checkVerticalSwipe2;
            if (checkVerticalSwipe2 > 0) {
                return swipeDir3;
            }
            int checkHorizontalSwipe2 = checkHorizontalSwipe(viewHolder, flags);
            int swipeDir4 = checkHorizontalSwipe2;
            if (checkHorizontalSwipe2 > 0) {
                if ((originalFlags & swipeDir4) == 0) {
                    return Callback.convertToRelativeDirection(swipeDir4, this.mRecyclerView.getLayoutDirection());
                }
                return swipeDir4;
            }
        }
        return 0;
    }

    private int checkHorizontalSwipe(RecyclerView.ViewHolder viewHolder, int flags) {
        if ((flags & 12) == 0) {
            return 0;
        }
        int i = 4;
        int dirFlag = this.mDx > 0.0f ? 8 : 4;
        if (this.mVelocityTracker != null && this.mActivePointerId > -1) {
            this.mVelocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
            float xVelocity = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
            float yVelocity = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
            if (xVelocity > 0.0f) {
                i = 8;
            }
            int velDirFlag = i;
            float absXVelocity = Math.abs(xVelocity);
            if ((velDirFlag & flags) != 0 && dirFlag == velDirFlag && absXVelocity >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && absXVelocity > Math.abs(yVelocity)) {
                return velDirFlag;
            }
        }
        float threshold = ((float) this.mRecyclerView.getWidth()) * this.mCallback.getSwipeThreshold(viewHolder);
        if ((flags & dirFlag) == 0 || Math.abs(this.mDx) <= threshold) {
            return 0;
        }
        return dirFlag;
    }

    private int checkVerticalSwipe(RecyclerView.ViewHolder viewHolder, int flags) {
        if ((flags & 3) == 0) {
            return 0;
        }
        int i = 1;
        int dirFlag = this.mDy > 0.0f ? 2 : 1;
        if (this.mVelocityTracker != null && this.mActivePointerId > -1) {
            this.mVelocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
            float xVelocity = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
            float yVelocity = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
            if (yVelocity > 0.0f) {
                i = 2;
            }
            int velDirFlag = i;
            float absYVelocity = Math.abs(yVelocity);
            if ((velDirFlag & flags) != 0 && velDirFlag == dirFlag && absYVelocity >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && absYVelocity > Math.abs(xVelocity)) {
                return velDirFlag;
            }
        }
        float threshold = ((float) this.mRecyclerView.getHeight()) * this.mCallback.getSwipeThreshold(viewHolder);
        if ((flags & dirFlag) == 0 || Math.abs(this.mDy) <= threshold) {
            return 0;
        }
        return dirFlag;
    }

    private void addChildDrawingOrderCallback() {
        if (Build.VERSION.SDK_INT < 21) {
            if (this.mChildDrawingOrderCallback == null) {
                this.mChildDrawingOrderCallback = new RecyclerView.ChildDrawingOrderCallback() {
                    public int onGetChildDrawingOrder(int childCount, int i) {
                        if (ItemTouchHelper.this.mOverdrawChild == null) {
                            return i;
                        }
                        int childPosition = ItemTouchHelper.this.mOverdrawChildPosition;
                        if (childPosition == -1) {
                            childPosition = ItemTouchHelper.this.mRecyclerView.indexOfChild(ItemTouchHelper.this.mOverdrawChild);
                            ItemTouchHelper.this.mOverdrawChildPosition = childPosition;
                        }
                        if (i == childCount - 1) {
                            return childPosition;
                        }
                        return i < childPosition ? i : i + 1;
                    }
                };
            }
            this.mRecyclerView.setChildDrawingOrderCallback(this.mChildDrawingOrderCallback);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeChildDrawingOrderCallbackIfNecessary(View view) {
        if (view == this.mOverdrawChild) {
            this.mOverdrawChild = null;
            if (this.mChildDrawingOrderCallback != null) {
                this.mRecyclerView.setChildDrawingOrderCallback((RecyclerView.ChildDrawingOrderCallback) null);
            }
        }
    }

    public static abstract class Callback {
        private static final int ABS_HORIZONTAL_DIR_FLAGS = 789516;
        public static final int DEFAULT_DRAG_ANIMATION_DURATION = 200;
        public static final int DEFAULT_SWIPE_ANIMATION_DURATION = 250;
        private static final long DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS = 2000;
        static final int RELATIVE_DIR_FLAGS = 3158064;
        private static final Interpolator sDragScrollInterpolator = new Interpolator() {
            public float getInterpolation(float t) {
                return t * t * t * t * t;
            }
        };
        private static final Interpolator sDragViewScrollCapInterpolator = new Interpolator() {
            public float getInterpolation(float t) {
                float t2 = t - 1.0f;
                return (t2 * t2 * t2 * t2 * t2) + 1.0f;
            }
        };
        private static final ItemTouchUIUtil sUICallback = new ItemTouchUIUtilImpl();
        private int mCachedMaxScrollSpeed = -1;

        public abstract int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);

        public abstract boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2);

        public abstract void onSwiped(RecyclerView.ViewHolder viewHolder, int i);

        public static ItemTouchUIUtil getDefaultUIUtil() {
            return sUICallback;
        }

        public static int convertToRelativeDirection(int flags, int layoutDirection) {
            int masked = flags & ABS_HORIZONTAL_DIR_FLAGS;
            if (masked == 0) {
                return flags;
            }
            int flags2 = flags & (~masked);
            if (layoutDirection == 0) {
                return flags2 | (masked << 2);
            }
            return flags2 | ((masked << 1) & -789517) | ((ABS_HORIZONTAL_DIR_FLAGS & (masked << 1)) << 2);
        }

        public static int makeMovementFlags(int dragFlags, int swipeFlags) {
            return makeFlag(0, swipeFlags | dragFlags) | makeFlag(1, swipeFlags) | makeFlag(2, dragFlags);
        }

        public static int makeFlag(int actionState, int directions) {
            return directions << (actionState * 8);
        }

        public int convertToAbsoluteDirection(int flags, int layoutDirection) {
            int masked = flags & RELATIVE_DIR_FLAGS;
            if (masked == 0) {
                return flags;
            }
            int flags2 = flags & (~masked);
            if (layoutDirection == 0) {
                return flags2 | (masked >> 2);
            }
            return flags2 | ((masked >> 1) & -3158065) | ((RELATIVE_DIR_FLAGS & (masked >> 1)) >> 2);
        }

        /* access modifiers changed from: package-private */
        public final int getAbsoluteMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return convertToAbsoluteDirection(getMovementFlags(recyclerView, viewHolder), recyclerView.getLayoutDirection());
        }

        /* access modifiers changed from: package-private */
        public boolean hasDragFlag(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return (16711680 & getAbsoluteMovementFlags(recyclerView, viewHolder)) != 0;
        }

        /* access modifiers changed from: package-private */
        public boolean hasSwipeFlag(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return (65280 & getAbsoluteMovementFlags(recyclerView, viewHolder)) != 0;
        }

        public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
            return true;
        }

        public boolean isLongPressDragEnabled() {
            return true;
        }

        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        public int getBoundingBoxMargin() {
            return 0;
        }

        public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
            return 0.5f;
        }

        public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
            return 0.5f;
        }

        public float getSwipeEscapeVelocity(float defaultValue) {
            return defaultValue;
        }

        public float getSwipeVelocityThreshold(float defaultValue) {
            return defaultValue;
        }

        public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder selected, List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {
            int diff;
            int score;
            int diff2;
            int score2;
            int diff3;
            int score3;
            int diff4;
            int score4;
            RecyclerView.ViewHolder viewHolder = selected;
            int right = curX + viewHolder.itemView.getWidth();
            int bottom = curY + viewHolder.itemView.getHeight();
            RecyclerView.ViewHolder winner = null;
            int winnerScore = -1;
            int dx = curX - viewHolder.itemView.getLeft();
            int dy = curY - viewHolder.itemView.getTop();
            int targetsSize = dropTargets.size();
            for (int i = 0; i < targetsSize; i++) {
                RecyclerView.ViewHolder target = dropTargets.get(i);
                if (dx > 0 && (diff4 = target.itemView.getRight() - right) < 0 && target.itemView.getRight() > viewHolder.itemView.getRight() && (score4 = Math.abs(diff4)) > winnerScore) {
                    winnerScore = score4;
                    winner = target;
                }
                if (dx < 0 && (diff3 = target.itemView.getLeft() - curX) > 0 && target.itemView.getLeft() < viewHolder.itemView.getLeft() && (score3 = Math.abs(diff3)) > winnerScore) {
                    winnerScore = score3;
                    winner = target;
                }
                if (dy < 0 && (diff2 = target.itemView.getTop() - curY) > 0 && target.itemView.getTop() < viewHolder.itemView.getTop() && (score2 = Math.abs(diff2)) > winnerScore) {
                    winnerScore = score2;
                    winner = target;
                }
                if (dy > 0 && (diff = target.itemView.getBottom() - bottom) < 0 && target.itemView.getBottom() > viewHolder.itemView.getBottom() && (score = Math.abs(diff)) > winnerScore) {
                    winnerScore = score;
                    winner = target;
                }
            }
            List<RecyclerView.ViewHolder> list = dropTargets;
            return winner;
        }

        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (viewHolder != null) {
                sUICallback.onSelected(viewHolder.itemView);
            }
        }

        private int getMaxDragScroll(RecyclerView recyclerView) {
            if (this.mCachedMaxScrollSpeed == -1) {
                this.mCachedMaxScrollSpeed = recyclerView.getResources().getDimensionPixelSize(R.dimen.item_touch_helper_max_drag_scroll_per_frame);
            }
            return this.mCachedMaxScrollSpeed;
        }

        public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof ViewDropHandler) {
                ((ViewDropHandler) layoutManager).prepareForDrop(viewHolder.itemView, target.itemView, x, y);
                return;
            }
            if (layoutManager.canScrollHorizontally()) {
                if (layoutManager.getDecoratedLeft(target.itemView) <= recyclerView.getPaddingLeft()) {
                    recyclerView.scrollToPosition(toPos);
                }
                if (layoutManager.getDecoratedRight(target.itemView) >= recyclerView.getWidth() - recyclerView.getPaddingRight()) {
                    recyclerView.scrollToPosition(toPos);
                }
            }
            if (layoutManager.canScrollVertically() != 0) {
                if (layoutManager.getDecoratedTop(target.itemView) <= recyclerView.getPaddingTop()) {
                    recyclerView.scrollToPosition(toPos);
                }
                if (layoutManager.getDecoratedBottom(target.itemView) >= recyclerView.getHeight() - recyclerView.getPaddingBottom()) {
                    recyclerView.scrollToPosition(toPos);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.ViewHolder selected, List<RecoverAnimation> recoverAnimationList, int actionState, float dX, float dY) {
            Canvas canvas = c;
            int recoverAnimSize = recoverAnimationList.size();
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= recoverAnimSize) {
                    break;
                }
                RecoverAnimation anim = recoverAnimationList.get(i2);
                anim.update();
                int count = c.save();
                onChildDraw(c, parent, anim.mViewHolder, anim.mX, anim.mY, anim.mActionState, false);
                c.restoreToCount(count);
                i = i2 + 1;
            }
            List<RecoverAnimation> list = recoverAnimationList;
            if (selected != null) {
                int count2 = c.save();
                onChildDraw(c, parent, selected, dX, dY, actionState, true);
                c.restoreToCount(count2);
            }
        }

        /* access modifiers changed from: package-private */
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.ViewHolder selected, List<RecoverAnimation> recoverAnimationList, int actionState, float dX, float dY) {
            Canvas canvas = c;
            List<RecoverAnimation> list = recoverAnimationList;
            int recoverAnimSize = recoverAnimationList.size();
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= recoverAnimSize) {
                    break;
                }
                RecoverAnimation anim = list.get(i2);
                int count = c.save();
                onChildDrawOver(c, parent, anim.mViewHolder, anim.mX, anim.mY, anim.mActionState, false);
                c.restoreToCount(count);
                i = i2 + 1;
            }
            if (selected != null) {
                int count2 = c.save();
                onChildDrawOver(c, parent, selected, dX, dY, actionState, true);
                c.restoreToCount(count2);
            }
            boolean hasRunningAnimation = false;
            for (int i3 = recoverAnimSize - 1; i3 >= 0; i3--) {
                RecoverAnimation anim2 = list.get(i3);
                if (anim2.mEnded && !anim2.mIsPendingCleanup) {
                    list.remove(i3);
                } else if (!anim2.mEnded) {
                    hasRunningAnimation = true;
                }
            }
            if (hasRunningAnimation) {
                parent.invalidate();
            }
        }

        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            sUICallback.clearView(viewHolder.itemView);
        }

        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            sUICallback.onDraw(c, recyclerView, viewHolder.itemView, dX, dY, actionState, isCurrentlyActive);
        }

        public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            sUICallback.onDrawOver(c, recyclerView, viewHolder.itemView, dX, dY, actionState, isCurrentlyActive);
        }

        public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
            RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
            if (itemAnimator == null) {
                if (animationType == 8) {
                    return 200;
                }
                return 250;
            } else if (animationType == 8) {
                return itemAnimator.getMoveDuration();
            } else {
                return itemAnimator.getRemoveDuration();
            }
        }

        public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int viewSize, int viewSizeOutOfBounds, int totalSize, long msSinceStartScroll) {
            float timeRatio;
            int cappedScroll = (int) (((float) (((int) Math.signum((float) viewSizeOutOfBounds)) * getMaxDragScroll(recyclerView))) * sDragViewScrollCapInterpolator.getInterpolation(Math.min(1.0f, (((float) Math.abs(viewSizeOutOfBounds)) * 1.0f) / ((float) viewSize))));
            if (msSinceStartScroll > DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS) {
                timeRatio = 1.0f;
            } else {
                timeRatio = ((float) msSinceStartScroll) / 2000.0f;
            }
            int value = (int) (((float) cappedScroll) * sDragScrollInterpolator.getInterpolation(timeRatio));
            if (value == 0) {
                return viewSizeOutOfBounds > 0 ? 1 : -1;
            }
            return value;
        }
    }

    public static abstract class SimpleCallback extends Callback {
        private int mDefaultDragDirs;
        private int mDefaultSwipeDirs;

        public SimpleCallback(int dragDirs, int swipeDirs) {
            this.mDefaultSwipeDirs = swipeDirs;
            this.mDefaultDragDirs = dragDirs;
        }

        public void setDefaultSwipeDirs(int defaultSwipeDirs) {
            this.mDefaultSwipeDirs = defaultSwipeDirs;
        }

        public void setDefaultDragDirs(int defaultDragDirs) {
            this.mDefaultDragDirs = defaultDragDirs;
        }

        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return this.mDefaultSwipeDirs;
        }

        public int getDragDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return this.mDefaultDragDirs;
        }

        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(getDragDirs(recyclerView, viewHolder), getSwipeDirs(recyclerView, viewHolder));
        }
    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        ItemTouchHelperGestureListener() {
        }

        public boolean onDown(MotionEvent e) {
            return true;
        }

        public void onLongPress(MotionEvent e) {
            RecyclerView.ViewHolder vh;
            View child = ItemTouchHelper.this.findChildView(e);
            if (child != null && (vh = ItemTouchHelper.this.mRecyclerView.getChildViewHolder(child)) != null && ItemTouchHelper.this.mCallback.hasDragFlag(ItemTouchHelper.this.mRecyclerView, vh) && e.getPointerId(0) == ItemTouchHelper.this.mActivePointerId) {
                int index = e.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
                float x = e.getX(index);
                float y = e.getY(index);
                ItemTouchHelper.this.mInitialTouchX = x;
                ItemTouchHelper.this.mInitialTouchY = y;
                ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                ItemTouchHelper.this.mDy = 0.0f;
                itemTouchHelper.mDx = 0.0f;
                if (ItemTouchHelper.this.mCallback.isLongPressDragEnabled()) {
                    ItemTouchHelper.this.select(vh, 2);
                }
            }
        }
    }

    private class RecoverAnimation implements Animator.AnimatorListener {
        final int mActionState;
        final int mAnimationType;
        boolean mEnded = false;
        private float mFraction;
        public boolean mIsPendingCleanup;
        boolean mOverridden = false;
        final float mStartDx;
        final float mStartDy;
        final float mTargetX;
        final float mTargetY;
        private final ValueAnimator mValueAnimator;
        final RecyclerView.ViewHolder mViewHolder;
        float mX;
        float mY;

        RecoverAnimation(RecyclerView.ViewHolder viewHolder, int animationType, int actionState, float startDx, float startDy, float targetX, float targetY) {
            this.mActionState = actionState;
            this.mAnimationType = animationType;
            this.mViewHolder = viewHolder;
            this.mStartDx = startDx;
            this.mStartDy = startDy;
            this.mTargetX = targetX;
            this.mTargetY = targetY;
            this.mValueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(ItemTouchHelper.this) {
                public void onAnimationUpdate(ValueAnimator animation) {
                    RecoverAnimation.this.setFraction(animation.getAnimatedFraction());
                }
            });
            this.mValueAnimator.setTarget(viewHolder.itemView);
            this.mValueAnimator.addListener(this);
            setFraction(0.0f);
        }

        public void setDuration(long duration) {
            this.mValueAnimator.setDuration(duration);
        }

        public void start() {
            this.mViewHolder.setIsRecyclable(false);
            this.mValueAnimator.start();
        }

        public void cancel() {
            this.mValueAnimator.cancel();
        }

        public void setFraction(float fraction) {
            this.mFraction = fraction;
        }

        public void update() {
            if (this.mStartDx == this.mTargetX) {
                this.mX = this.mViewHolder.itemView.getTranslationX();
            } else {
                this.mX = this.mStartDx + (this.mFraction * (this.mTargetX - this.mStartDx));
            }
            if (this.mStartDy == this.mTargetY) {
                this.mY = this.mViewHolder.itemView.getTranslationY();
            } else {
                this.mY = this.mStartDy + (this.mFraction * (this.mTargetY - this.mStartDy));
            }
        }

        public void onAnimationStart(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
            if (!this.mEnded) {
                this.mViewHolder.setIsRecyclable(true);
            }
            this.mEnded = true;
        }

        public void onAnimationCancel(Animator animation) {
            setFraction(1.0f);
        }

        public void onAnimationRepeat(Animator animation) {
        }
    }
}
