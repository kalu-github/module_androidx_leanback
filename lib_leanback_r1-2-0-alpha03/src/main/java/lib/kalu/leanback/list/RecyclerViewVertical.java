package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import lib.kalu.leanback.util.LeanBackUtil;

public class RecyclerViewVertical extends BaseRecyclerView {

    public RecyclerViewVertical(@NonNull Context context) {
        super(context);
    }

    public RecyclerViewVertical(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewVertical(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
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
            int itemCount = getAdapterItemCount();
            if (itemCount > 0) {
                scrollToPosition(itemCount - 1);
            }
        }
    }

    @Override
    public boolean fastScrollToPosition(@NonNull int checkedPosition) {

        try {
            while (true) {
                LayoutManager layoutManager = getLayoutManager();
                if (null == layoutManager)
                    throw new Exception("layoutManager error: null");
                int firstPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                if (firstPosition < 0)
                    throw new Exception("firstPosition error: " + firstPosition);
                int lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                if (lastPosition < 0)
                    throw new Exception("lastPosition error: " + lastPosition);
                if (checkedPosition < firstPosition) {
                    fastScrollRange(View.FOCUS_UP);
                } else if (checkedPosition > lastPosition) {
                    fastScrollRange(View.FOCUS_DOWN);
                }
                ViewHolder viewHolder = findViewHolderForAdapterPosition(checkedPosition);
                if (null == viewHolder)
                    continue;
                if (null == viewHolder.itemView)
                    continue;
                viewHolder.itemView.requestFocus();
                break;
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("RecyclerViewVertical => fastScrollToPosition => " + e.getMessage());
            return false;
        }
    }
}