package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.Iterator;

public class ViewOverlay {
    OverlayViewGroup mOverlayViewGroup;

    ViewOverlay(Context context, View hostView) {
        this.mOverlayViewGroup = new OverlayViewGroup(context, hostView);
    }

    /* access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public ViewGroup getOverlayView() {
        return this.mOverlayViewGroup;
    }

    public void add(Drawable drawable) {
        this.mOverlayViewGroup.add(drawable);
    }

    public void remove(Drawable drawable) {
        this.mOverlayViewGroup.remove(drawable);
    }

    public void clear() {
        this.mOverlayViewGroup.clear();
    }

    /* access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public boolean isEmpty() {
        return this.mOverlayViewGroup.isEmpty();
    }

    static class OverlayViewGroup extends ViewGroup {
        ArrayList<Drawable> mDrawables = null;
        final View mHostView;

        OverlayViewGroup(Context context, View hostView) {
            super(context);
            this.mHostView = hostView;
            this.mAttachInfo = this.mHostView.mAttachInfo;
            this.mRight = hostView.getWidth();
            this.mBottom = hostView.getHeight();
            this.mRenderNode.setLeftTopRightBottom(0, 0, this.mRight, this.mBottom);
        }

        public void add(Drawable drawable) {
            if (drawable != null) {
                if (this.mDrawables == null) {
                    this.mDrawables = new ArrayList<>();
                }
                if (!this.mDrawables.contains(drawable)) {
                    this.mDrawables.add(drawable);
                    invalidate(drawable.getBounds());
                    drawable.setCallback(this);
                    return;
                }
                return;
            }
            throw new IllegalArgumentException("drawable must be non-null");
        }

        public void remove(Drawable drawable) {
            if (drawable == null) {
                throw new IllegalArgumentException("drawable must be non-null");
            } else if (this.mDrawables != null) {
                this.mDrawables.remove(drawable);
                invalidate(drawable.getBounds());
                drawable.setCallback((Drawable.Callback) null);
            }
        }

        /* access modifiers changed from: protected */
        public boolean verifyDrawable(Drawable who) {
            return super.verifyDrawable(who) || (this.mDrawables != null && this.mDrawables.contains(who));
        }

        public void add(View child) {
            if (child != null) {
                if (child.getParent() instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) child.getParent();
                    if (!(parent == this.mHostView || parent.getParent() == null || parent.mAttachInfo == null)) {
                        int[] parentLocation = new int[2];
                        int[] hostViewLocation = new int[2];
                        parent.getLocationOnScreen(parentLocation);
                        this.mHostView.getLocationOnScreen(hostViewLocation);
                        child.offsetLeftAndRight(parentLocation[0] - hostViewLocation[0]);
                        child.offsetTopAndBottom(parentLocation[1] - hostViewLocation[1]);
                    }
                    parent.removeView(child);
                    if (parent.getLayoutTransition() != null) {
                        parent.getLayoutTransition().cancel(3);
                    }
                    if (child.getParent() != null) {
                        child.mParent = null;
                    }
                }
                super.addView(child);
                return;
            }
            throw new IllegalArgumentException("view must be non-null");
        }

        public void remove(View view) {
            if (view != null) {
                super.removeView(view);
                return;
            }
            throw new IllegalArgumentException("view must be non-null");
        }

        public void clear() {
            removeAllViews();
            if (this.mDrawables != null) {
                Iterator<Drawable> it = this.mDrawables.iterator();
                while (it.hasNext()) {
                    it.next().setCallback((Drawable.Callback) null);
                }
                this.mDrawables.clear();
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isEmpty() {
            if (getChildCount() != 0) {
                return false;
            }
            if (this.mDrawables == null || this.mDrawables.size() == 0) {
                return true;
            }
            return false;
        }

        public void invalidateDrawable(Drawable drawable) {
            invalidate(drawable.getBounds());
        }

        /* access modifiers changed from: protected */
        public void dispatchDraw(Canvas canvas) {
            canvas.insertReorderBarrier();
            super.dispatchDraw(canvas);
            canvas.insertInorderBarrier();
            int numDrawables = this.mDrawables == null ? 0 : this.mDrawables.size();
            for (int i = 0; i < numDrawables; i++) {
                this.mDrawables.get(i).draw(canvas);
            }
        }

        /* access modifiers changed from: protected */
        public void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        public void invalidate(Rect dirty) {
            super.invalidate(dirty);
            if (this.mHostView != null) {
                this.mHostView.invalidate(dirty);
            }
        }

        public void invalidate(int l, int t, int r, int b) {
            super.invalidate(l, t, r, b);
            if (this.mHostView != null) {
                this.mHostView.invalidate(l, t, r, b);
            }
        }

        public void invalidate() {
            super.invalidate();
            if (this.mHostView != null) {
                this.mHostView.invalidate();
            }
        }

        public void invalidate(boolean invalidateCache) {
            super.invalidate(invalidateCache);
            if (this.mHostView != null) {
                this.mHostView.invalidate(invalidateCache);
            }
        }

        /* access modifiers changed from: package-private */
        public void invalidateViewProperty(boolean invalidateParent, boolean forceRedraw) {
            super.invalidateViewProperty(invalidateParent, forceRedraw);
            if (this.mHostView != null) {
                this.mHostView.invalidateViewProperty(invalidateParent, forceRedraw);
            }
        }

        /* access modifiers changed from: protected */
        public void invalidateParentCaches() {
            super.invalidateParentCaches();
            if (this.mHostView != null) {
                this.mHostView.invalidateParentCaches();
            }
        }

        /* access modifiers changed from: protected */
        public void invalidateParentIfNeeded() {
            super.invalidateParentIfNeeded();
            if (this.mHostView != null) {
                this.mHostView.invalidateParentIfNeeded();
            }
        }

        public void onDescendantInvalidated(View child, View target) {
            if (this.mHostView == null) {
                return;
            }
            if (this.mHostView instanceof ViewGroup) {
                ((ViewGroup) this.mHostView).onDescendantInvalidated(this.mHostView, target);
                super.onDescendantInvalidated(child, target);
                return;
            }
            invalidate();
        }

        public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
            if (this.mHostView == null) {
                return null;
            }
            dirty.offset(location[0], location[1]);
            if (this.mHostView instanceof ViewGroup) {
                location[0] = 0;
                location[1] = 0;
                super.invalidateChildInParent(location, dirty);
                return ((ViewGroup) this.mHostView).invalidateChildInParent(location, dirty);
            }
            invalidate(dirty);
            return null;
        }
    }
}
