package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.widget.BaseGridView;

import lib.kalu.leanback.util.LeanBackUtil;

class BaseVerticalGridView extends androidx.leanback.widget.VerticalGridView {

    public BaseVerticalGridView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseVerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseVerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setAnimation(null);
        setItemAnimator(null);
        setAnimationCacheEnabled(false);
        setNestedScrollingEnabled(false);
        setAnimateChildLayout(false);
        setHasFixedSize(true);
    }


    private boolean hasFocusGridView = false;

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        LeanBackUtil.log("BaseVerticalGridView => dispatchKeyEvent => action = " + event.getAction() + ", keyCode = " + event.getKeyCode());
        // ACTION_UP KEYCODE_DPAD_DOWN
        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (!hasFocusGridView) {
                hasFocusGridView = true;
                onGridViewFocusInto();
            }
        }
        // ACTION_DOWN KEYCODE_DPAD_UP
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            if (hasFocusGridView) {
                int index = findFocusedChildByPositionAtGridView();
                if (index == 0) {
                    View focusedChild = getFocusedChild();
                    View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_UP);
                    if (null == nextFocus) {
                        hasFocusGridView = false;
                        onGridViewFocusOut();
                    }
                }
            }

            // fix bug: 焦点向上移动时, view还未滚动到正确位置
            try {
                View focus = findFocus();
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focus, View.FOCUS_UP);
                if (null == nextFocus) {
                    int positionAtGridView = findFocusedChildByPositionAtGridView();
                    if (positionAtGridView > 0) {
                        while (true) {
                            scrollBy(0, -10);
                            View view = FocusFinder.getInstance().findNextFocus(this, focus, View.FOCUS_UP);
                            if (null != view)
                                break;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public View getFocusedChild() {
        try {
            View focusedChild = findFocus();
            if (null == focusedChild)
                throw new Exception("focusedChild is null");
            //    LeanBackUtil.log("BaseVerticalGridView => findFocusChild => focusedChild = " + focusedChild);
            return focusedChild;
        } catch (Exception e) {
            LeanBackUtil.log("BaseVerticalGridView => findFocusChild => " + e.getMessage());
            return null;
        }
    }

    public final int findFocusedChildByPositionAtGridView() {
        try {
            View focusedChild = getFocusedChild();
            if (null == focusedChild)
                throw new Exception("focusedChild is null");
            int position = -1;
            while (true) {
                ViewParent parent = focusedChild.getParent();
                boolean assignableFrom = BaseGridView.class.isAssignableFrom(parent.getClass());
                if (assignableFrom) {
                    position = getChildAdapterPosition(focusedChild);
                    break;
                }
                focusedChild = (View) parent;
            }
            if (position == -1)
                throw new Exception("position error: " + position);
            return position;
        } catch (Exception e) {
            LeanBackUtil.log("BaseVerticalGridView => findFocusedChildByPositionAtGridView => " + e.getMessage());
            return -1;
        }
    }

    public final void scrollTop() {
        try {
            while (true) {
                // 1
                View focusedChild = getFocusedChild();
                if (null == focusedChild)
                    break;
                // 2
                scrollFocusedChild(View.FOCUS_UP);
                // 3
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_UP);
                if (null == nextFocus)
                    break;
                nextFocus.requestFocus();
            }
        } catch (Exception e) {
            LeanBackUtil.log("BaseVerticalGridView => scrollTop => " + e.getMessage());
        }
    }

    public final void scrollBottom() {
        try {
            while (true) {
                // 1
                View focusedChild = getFocusedChild();
                if (null == focusedChild)
                    break;
                // 2
                scrollFocusedChild(View.FOCUS_DOWN);
                // 3
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_DOWN);
                if (null == nextFocus)
                    break;
                nextFocus.requestFocus();
            }
        } catch (Exception e) {
            LeanBackUtil.log("BaseVerticalGridView => scrollBottom => " + e.getMessage());
        }
    }

    public final int findFocusedChildPosition() {
        try {
            View focusedView = getFocusedChild();
            if (null == focusedView)
                throw new Exception("focusedView error: null");
            while (true) {
                ViewParent parent = focusedView.getParent();
                if (parent instanceof LeanBackVerticalGridView) {
                    int adapterPosition = getChildAdapterPosition(focusedView);
                    if (adapterPosition < 0)
                        throw new Exception("adapterPosition error: " + adapterPosition);
                    return adapterPosition;
                } else {
                    focusedView = (View) parent;
                }
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public final int getAdapterItemCount() {
        try {
            Adapter adapter = getAdapter();
            if (null == adapter)
                throw new Exception("adapter error: null");
            return adapter.getItemCount();
        } catch (Exception e) {
            return 0;
        }
    }

    public final void scrollFocusedChild(int direction) {
        try {
            if (direction != View.FOCUS_UP && direction != View.FOCUS_DOWN)
                throw new Exception("direction error: " + direction);
            View focusedView = getFocusedChild();
            if (null == focusedView)
                throw new Exception("focusedView error: null");
            int measuredHeight = focusedView.getMeasuredHeight();
            scrollBy(0, direction == View.FOCUS_UP ? -measuredHeight : measuredHeight);
        } catch (Exception e) {
        }
    }

    /**************/

    protected void onGridViewFocusInto() {
    }

    protected void onGridViewFocusOut() {
    }
//
//    protected void onUp(int index) {
//    }
//
//    protected void onDown(int index) {
//    }
}
