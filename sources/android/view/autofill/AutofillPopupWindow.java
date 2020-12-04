package android.view.autofill;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.RemoteException;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;
import com.android.internal.R;

public class AutofillPopupWindow extends PopupWindow {
    private static final String TAG = "AutofillPopupWindow";
    private boolean mFullScreen;
    private final View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener() {
        public void onViewAttachedToWindow(View v) {
        }

        public void onViewDetachedFromWindow(View v) {
            AutofillPopupWindow.this.dismiss();
        }
    };
    private WindowManager.LayoutParams mWindowLayoutParams;
    private final WindowPresenter mWindowPresenter;

    public AutofillPopupWindow(IAutofillWindowPresenter presenter) {
        this.mWindowPresenter = new WindowPresenter(presenter);
        setTouchModal(false);
        setOutsideTouchable(true);
        setInputMethodMode(2);
        setFocusable(true);
    }

    /* access modifiers changed from: protected */
    public boolean hasContentView() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean hasDecorView() {
        return true;
    }

    /* access modifiers changed from: protected */
    public WindowManager.LayoutParams getDecorViewLayoutParams() {
        return this.mWindowLayoutParams;
    }

    public void update(final View anchor, int offsetX, int offsetY, int width, int height, Rect virtualBounds) {
        int i;
        View actualAnchor;
        this.mFullScreen = width == -1;
        if (this.mFullScreen) {
            i = 2008;
        } else {
            i = 1005;
        }
        setWindowLayoutType(i);
        if (this.mFullScreen) {
            offsetX = 0;
            offsetY = 0;
            Point outPoint = new Point();
            anchor.getContext().getDisplay().getSize(outPoint);
            width = outPoint.x;
            if (height != -1) {
                offsetY = outPoint.y - height;
            }
            actualAnchor = anchor;
        } else if (virtualBounds != null) {
            final int[] mLocationOnScreen = {virtualBounds.left, virtualBounds.top};
            View r4 = new View(anchor.getContext()) {
                public void getLocationOnScreen(int[] location) {
                    location[0] = mLocationOnScreen[0];
                    location[1] = mLocationOnScreen[1];
                }

                public int getAccessibilityViewId() {
                    return anchor.getAccessibilityViewId();
                }

                public ViewTreeObserver getViewTreeObserver() {
                    return anchor.getViewTreeObserver();
                }

                public IBinder getApplicationWindowToken() {
                    return anchor.getApplicationWindowToken();
                }

                public View getRootView() {
                    return anchor.getRootView();
                }

                public int getLayoutDirection() {
                    return anchor.getLayoutDirection();
                }

                public void getWindowDisplayFrame(Rect outRect) {
                    anchor.getWindowDisplayFrame(outRect);
                }

                public void addOnAttachStateChangeListener(View.OnAttachStateChangeListener listener) {
                    anchor.addOnAttachStateChangeListener(listener);
                }

                public void removeOnAttachStateChangeListener(View.OnAttachStateChangeListener listener) {
                    anchor.removeOnAttachStateChangeListener(listener);
                }

                public boolean isAttachedToWindow() {
                    return anchor.isAttachedToWindow();
                }

                public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) {
                    return anchor.requestRectangleOnScreen(rectangle, immediate);
                }

                public IBinder getWindowToken() {
                    return anchor.getWindowToken();
                }
            };
            r4.setLeftTopRightBottom(virtualBounds.left, virtualBounds.top, virtualBounds.right, virtualBounds.bottom);
            r4.setScrollX(anchor.getScrollX());
            r4.setScrollY(anchor.getScrollY());
            anchor.setOnScrollChangeListener(new View.OnScrollChangeListener(mLocationOnScreen) {
                private final /* synthetic */ int[] f$0;

                {
                    this.f$0 = r1;
                }

                public final void onScrollChange(View view, int i, int i2, int i3, int i4) {
                    AutofillPopupWindow.lambda$update$0(this.f$0, view, i, i2, i3, i4);
                }
            });
            r4.setWillNotDraw(true);
            actualAnchor = r4;
        } else {
            actualAnchor = anchor;
        }
        if (!this.mFullScreen) {
            setAnimationStyle(-1);
        } else if (height == -1) {
            setAnimationStyle(0);
        } else {
            setAnimationStyle(R.style.AutofillHalfScreenAnimation);
        }
        if (!isShowing()) {
            setWidth(width);
            setHeight(height);
            showAsDropDown(actualAnchor, offsetX, offsetY);
            return;
        }
        update(actualAnchor, offsetX, offsetY, width, height);
    }

    static /* synthetic */ void lambda$update$0(int[] mLocationOnScreen, View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        mLocationOnScreen[0] = mLocationOnScreen[0] - (scrollX - oldScrollX);
        mLocationOnScreen[1] = mLocationOnScreen[1] - (scrollY - oldScrollY);
    }

    /* access modifiers changed from: protected */
    public void update(View anchor, WindowManager.LayoutParams params) {
        int layoutDirection;
        if (anchor != null) {
            layoutDirection = anchor.getLayoutDirection();
        } else {
            layoutDirection = 3;
        }
        this.mWindowPresenter.show(params, getTransitionEpicenter(), isLayoutInsetDecor(), layoutDirection);
    }

    /* access modifiers changed from: protected */
    public boolean findDropDownPosition(View anchor, WindowManager.LayoutParams outParams, int xOffset, int yOffset, int width, int height, int gravity, boolean allowScroll) {
        if (!this.mFullScreen) {
            return super.findDropDownPosition(anchor, outParams, xOffset, yOffset, width, height, gravity, allowScroll);
        }
        outParams.x = xOffset;
        outParams.y = yOffset;
        outParams.width = width;
        outParams.height = height;
        outParams.gravity = gravity;
        return false;
    }

    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (Helper.sVerbose) {
            Log.v(TAG, "showAsDropDown(): anchor=" + anchor + ", xoff=" + xoff + ", yoff=" + yoff + ", isShowing(): " + isShowing());
        }
        if (!isShowing()) {
            setShowing(true);
            setDropDown(true);
            attachToAnchor(anchor, xoff, yoff, gravity);
            WindowManager.LayoutParams p = createPopupLayoutParams(anchor.getWindowToken());
            this.mWindowLayoutParams = p;
            updateAboveAnchor(findDropDownPosition(anchor, p, xoff, yoff, p.width, p.height, gravity, getAllowScrollingAnchorParent()));
            p.accessibilityIdOfAnchor = (long) anchor.getAccessibilityViewId();
            p.packageName = anchor.getContext().getPackageName();
            this.mWindowPresenter.show(p, getTransitionEpicenter(), isLayoutInsetDecor(), anchor.getLayoutDirection());
        }
    }

    /* access modifiers changed from: protected */
    public void attachToAnchor(View anchor, int xoff, int yoff, int gravity) {
        super.attachToAnchor(anchor, xoff, yoff, gravity);
        anchor.addOnAttachStateChangeListener(this.mOnAttachStateChangeListener);
    }

    /* access modifiers changed from: protected */
    public void detachFromAnchor() {
        View anchor = getAnchor();
        if (anchor != null) {
            anchor.removeOnAttachStateChangeListener(this.mOnAttachStateChangeListener);
        }
        super.detachFromAnchor();
    }

    public void dismiss() {
        if (isShowing() && !isTransitioningToDismiss()) {
            setShowing(false);
            setTransitioningToDismiss(true);
            this.mWindowPresenter.hide(getTransitionEpicenter());
            detachFromAnchor();
            if (getOnDismissListener() != null) {
                getOnDismissListener().onDismiss();
            }
        }
    }

    public int getAnimationStyle() {
        throw new IllegalStateException("You can't call this!");
    }

    public Drawable getBackground() {
        throw new IllegalStateException("You can't call this!");
    }

    public View getContentView() {
        throw new IllegalStateException("You can't call this!");
    }

    public float getElevation() {
        throw new IllegalStateException("You can't call this!");
    }

    public Transition getEnterTransition() {
        throw new IllegalStateException("You can't call this!");
    }

    public Transition getExitTransition() {
        throw new IllegalStateException("You can't call this!");
    }

    public void setBackgroundDrawable(Drawable background) {
        throw new IllegalStateException("You can't call this!");
    }

    public void setContentView(View contentView) {
        if (contentView != null) {
            throw new IllegalStateException("You can't call this!");
        }
    }

    public void setElevation(float elevation) {
        throw new IllegalStateException("You can't call this!");
    }

    public void setEnterTransition(Transition enterTransition) {
        throw new IllegalStateException("You can't call this!");
    }

    public void setExitTransition(Transition exitTransition) {
        throw new IllegalStateException("You can't call this!");
    }

    public void setTouchInterceptor(View.OnTouchListener l) {
        throw new IllegalStateException("You can't call this!");
    }

    private class WindowPresenter {
        final IAutofillWindowPresenter mPresenter;

        WindowPresenter(IAutofillWindowPresenter presenter) {
            this.mPresenter = presenter;
        }

        /* access modifiers changed from: package-private */
        public void show(WindowManager.LayoutParams p, Rect transitionEpicenter, boolean fitsSystemWindows, int layoutDirection) {
            try {
                this.mPresenter.show(p, transitionEpicenter, fitsSystemWindows, layoutDirection);
            } catch (RemoteException e) {
                Log.w(AutofillPopupWindow.TAG, "Error showing fill window", e);
                e.rethrowFromSystemServer();
            }
        }

        /* access modifiers changed from: package-private */
        public void hide(Rect transitionEpicenter) {
            try {
                this.mPresenter.hide(transitionEpicenter);
            } catch (RemoteException e) {
                Log.w(AutofillPopupWindow.TAG, "Error hiding fill window", e);
                e.rethrowFromSystemServer();
            }
        }
    }
}
