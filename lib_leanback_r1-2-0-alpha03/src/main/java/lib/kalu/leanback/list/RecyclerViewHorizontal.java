package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.leanback.util.LeanBackUtil;

public class RecyclerViewHorizontal extends BaseRecyclerView {

    public RecyclerViewHorizontal(@NonNull Context context) {
        super(context);
    }

    public RecyclerViewHorizontal(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewHorizontal(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void scrollToPosition(int position) {
//        super.scrollToPosition(position);
        scrollToPosition(position, true);
    }

    public void scrollToPosition(int position, boolean hasFocus) {

        while (true) {
            try {
                if (position < 0)
                    throw new Exception("position error: " + position);
                int itemCount = getAdapter().getItemCount();
                if (position + 1 >= itemCount)
                    throw new Exception("itemCount error: " + itemCount);
                View focusedChild = getFocusedChild();
                int fromPosition = findFocusedChildPosition();
                if (fromPosition == position) {
                    if (hasFocus) {
                        focusedChild.requestFocus();
                    }
                    break;
                }
                // right
                if (position > fromPosition) {
                    scrollBy(0, focusedChild.getWidth());
                }
                // left
                else {
                    scrollBy(0, -focusedChild.getWidth());
                }
            } catch (Exception e) {
                LeanBackUtil.log("RecyclerViewHorizontal => scrollToPosition => " + e.getMessage(), e);
                break;
            }
        }
    }

    @Override
    public void scrollFocusedChild(int direction) {
        try {
            if (direction != View.FOCUS_LEFT && direction != View.FOCUS_RIGHT)
                throw new Exception("direction error: " + direction);
            View focusedView = getFocusedChild();
            if (null == focusedView)
                throw new Exception("focusedView error: null");
            int measuredWidth = focusedView.getMeasuredWidth();
            scrollBy(0, direction == View.FOCUS_LEFT ? -measuredWidth : measuredWidth);
        } catch (Exception e) {
        }
    }

    @Override
    public void scrollTop(boolean hasFocus) {
        try {
            while (true) {
                View focusedChild = getFocusedChild();
                if (null == focusedChild)
                    throw new Exception();
                int focusPosition = findFocusedChildPosition();
                if (focusPosition <= 0) {
                    if (!hasFocus) {
                        focusedChild.clearFocus();
                    }
                    break;
                }
                scrollFocusedChild(View.FOCUS_LEFT);
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_LEFT);
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
            int itemCount = getAdapterItemCount();
            if (itemCount > 0) {
                scrollToPosition(0);
            }
        }
    }

    @Override
    public void scrollBottom(boolean hasFocus) {

        try {
            while (true) {
                View focusedChild = getFocusedChild();
                if (null == focusedChild)
                    throw new Exception();
                int focusPosition = findFocusedChildPosition();
                int adapterItemCount = getAdapterItemCount();
                if (focusPosition + 1 >= adapterItemCount) {
                    if (!hasFocus) {
                        focusedChild.clearFocus();
                    }
                    break;
                }
                scrollFocusedChild(View.FOCUS_RIGHT);
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_RIGHT);
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
            int itemCount = getAdapterItemCount();
            if (itemCount > 0) {
                scrollToPosition(itemCount - 1);
            }
        }
    }
}
