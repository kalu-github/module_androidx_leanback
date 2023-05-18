package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getRepeatCount() > 0)
            return true;

        // up
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            try {
                LayoutManager layoutManager = getLayoutManager();
                if (null == layoutManager)
                    throw new Exception("layoutManager error: null");
                int focusPosition = findFocusedChildPosition();
                if (focusPosition < 0)
                    throw new Exception("focusPosition error: " + focusPosition);
                View focusedChild = getFocusedChild();
                if (null == focusedChild)
                    throw new Exception("focusedChild error: null");
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_UP);
                if (null != nextFocus)
                    throw new Exception("nextFocus warning: " + nextFocus);
                int height = focusedChild.getHeight();
                if (height < 0)
                    throw new Exception("height error: " + height);
                scrollBy(0, -height);
                View nextFocusNews = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_UP);
                if (null == nextFocusNews)
                    throw new Exception("nextFocusNews error: null");
                nextFocusNews.requestFocus();
                return true;
            } catch (Exception e) {
                LeanBackUtil.log("RecyclerViewVertical => dispatchKeyEvent => up => " + e.getMessage());
            }
        }
        // down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            try {
                LayoutManager layoutManager = getLayoutManager();
                if (null == layoutManager)
                    throw new Exception("layoutManager error: null");
                Adapter adapter = getAdapter();
                if (null == adapter)
                    throw new Exception("adapter error: null");
                int itemCount = adapter.getItemCount();
                if (itemCount < 0)
                    throw new Exception("itemCount error: " + itemCount);
                int focusPosition = findFocusedChildPosition();
                if (focusPosition < 0)
                    throw new Exception("focusPosition error: " + focusPosition);
                if (focusPosition + 1 >= itemCount)
                    throw new Exception("focusPosition warning: " + focusPosition + ", itemCount = " + itemCount);
                View focusedChild = getFocusedChild();
                if (null == focusedChild)
                    throw new Exception("focusedChild error: null");
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_DOWN);
                if (null != nextFocus)
                    throw new Exception("nextFocus warning: " + nextFocus);
                int height = focusedChild.getHeight();
                if (height < 0)
                    throw new Exception("height error: " + height);
                scrollBy(0, height);
                View nextFocusNews = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_DOWN);
                if (null == nextFocusNews)
                    throw new Exception("nextFocusNews error: null");
                nextFocusNews.requestFocus();
                return true;
            } catch (Exception e) {
                LeanBackUtil.log("RecyclerViewVertical => dispatchKeyEvent => down => " + e.getMessage());
            }
        }
        return super.dispatchKeyEvent(event);
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
}