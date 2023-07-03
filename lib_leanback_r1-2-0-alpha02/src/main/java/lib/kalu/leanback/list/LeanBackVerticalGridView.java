package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import lib.kalu.leanback.util.LeanBackUtil;

public class LeanBackVerticalGridView extends BaseLeanBackGridViewVertical {
    public LeanBackVerticalGridView(@NonNull Context context) {
        super(context);
    }

    public LeanBackVerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LeanBackVerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    public final void scrollUp(int row) {
//        while (true) {
//            View focusedChild = getFocusedChild();
//            if (null == focusedChild)
//                break;
//            int height = focusedChild.getHeight();
//            scrollBy(0, -height);
//            View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_UP);
//            if (null == nextFocus)
//                break;
//            while (true) {
//                ViewParent parent = nextFocus.getParent();
//                if (parent instanceof LeanBackVerticalGridView) {
//                    break;
//                }
//                nextFocus = (View) parent;
//            }
//            if (null == nextFocus)
//                break;
//            nextFocus.requestFocus();
//            int position = getChildAdapterPosition(nextFocus);
//            if (position == row)
//                break;
//        }
//    }
//
//    public final void scrollDown(int row) {
//        while (true) {
//            View focusedChild = getFocusedChild();
//            if (null == focusedChild)
//                break;
//            int height = focusedChild.getHeight();
//            scrollBy(0, height);
//            View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_UP);
//            if (null == nextFocus)
//                break;
//            while (true) {
//                ViewParent parent = nextFocus.getParent();
//                if (parent instanceof LeanBackVerticalGridView) {
//                    break;
//                }
//                nextFocus = (View) parent;
//            }
//            if (null == nextFocus)
//                break;
//            nextFocus.requestFocus();
//            int position = getChildAdapterPosition(nextFocus);
//            if (position == row)
//                break;
//        }
//    }

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

    public int getAdapterItemCount() {
        try {
            Adapter adapter = getAdapter();
            if (null == adapter)
                throw new Exception("adapter error: null");
            return adapter.getItemCount();
        } catch (Exception e) {
            return 0;
        }
    }

    public void scrollFocusedChild(int direction) {
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

//    public void scrollTopCompat() {
//        try {
//            View focusedChild = getFocusedChild();
//            if (null == focusedChild)
//                throw new Exception("focusedChild error: null");
//            int paddingTop = getPaddingTop();
//            scrollBy(0, -paddingTop);
//            scrollBy(0, -Integer.MAX_VALUE);
//        } catch (Exception e) {
//            LeanBackUtil.log("LeanBackVerticalGridView => scrollTopCompat => " + e.getMessage());
//        }
//    }

    public void scrollTop(boolean hasFocus) {
        try {
            while (true) {
                View focusedChild = getFocusedChild();
                if (null == focusedChild)
                    throw new Exception("focusedChild error: null");
                int focusPosition = findFocusedChildPosition();
                if (focusPosition <= 0) {
                    if (!hasFocus) {
                        focusedChild.clearFocus();
                    }
                    break;
                }
                scrollFocusedChild(View.FOCUS_UP);
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_UP);
                if (null == nextFocus)
                    continue;
                while (true) {
                    if (null == nextFocus)
                        break;
                    if (nextFocus.isFocusable())
                        break;
                    nextFocus = FocusFinder.getInstance().findNextFocus(this, nextFocus, View.FOCUS_UP);
                }
                nextFocus.requestFocus();
            }
        } catch (Exception e) {
            LeanBackUtil.log("LeanBackVerticalGridView => scrollTop => " + e.getMessage());
            int itemCount = getAdapterItemCount();
            if (itemCount > 0) {
                scrollToPosition(0);
            }
        }
    }

//    public void scrollBottomCompat() {
//        try {
//            View focusedChild = getFocusedChild();
//            if (null == focusedChild)
//                throw new Exception("focusedChild error: null");
//            int paddingBottom = getPaddingBottom();
//            scrollBy(0, paddingBottom);
//            scrollBy(0, Integer.MAX_VALUE);
//        } catch (Exception e) {
//            LeanBackUtil.log("LeanBackVerticalGridView => scrollBottomCompat => " + e.getMessage());
//        }
//    }

    public void scrollBottom(boolean hasFocus) {

        try {
            while (true) {
                View focusedChild = getFocusedChild();
                if (null == focusedChild)
                    throw new Exception("focusedChild error: null");
                int focusPosition = findFocusedChildPosition();
                int adapterItemCount = getAdapterItemCount();
                if (focusPosition + 1 >= adapterItemCount) {
                    if (!hasFocus) {
                        focusedChild.clearFocus();
                    }
                    break;
                }
                scrollFocusedChild(View.FOCUS_DOWN);
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_DOWN);
                if (null == nextFocus)
                    continue;
                while (true) {
                    if (null == nextFocus)
                        break;
                    if (nextFocus.isFocusable())
                        break;
                    nextFocus = FocusFinder.getInstance().findNextFocus(this, nextFocus, View.FOCUS_UP);
                }
                nextFocus.requestFocus();
            }
        } catch (Exception e) {
            LeanBackUtil.log("LeanBackVerticalGridView => scrollBottom => " + e.getMessage());
            int itemCount = getAdapterItemCount();
            if (itemCount > 0) {
                scrollToPosition(itemCount - 1);
            }
        }
    }
}
