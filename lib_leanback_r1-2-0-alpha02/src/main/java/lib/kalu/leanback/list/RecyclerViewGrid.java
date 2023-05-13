package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.leanback.list.layoutmanager.BaseGridLayoutManager;
import lib.kalu.leanback.util.LeanBackUtil;

public class RecyclerViewGrid extends BaseRecyclerView {

    public RecyclerViewGrid(@NonNull Context context) {
        super(context);
    }

    public RecyclerViewGrid(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewGrid(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getRepeatCount() > 1)
            return true;

        // action_down => up
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            try {
                LayoutManager layoutManager = getLayoutManager();
                if (null == layoutManager)
                    throw new Exception("layoutManager error: null");
                int spanCount = getSpanCount();
                if (spanCount <= 1)
                    throw new Exception("spanCount error: " + spanCount);
                int focusPosition = findFocusedChildPosition();
                if (focusPosition < spanCount)
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
                leaveFromUp();
                LeanBackUtil.log("RecyclerViewGrid => dispatchKeyEvent => up-down => " + e.getMessage());
            }
        }
        // action_down => down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            try {
                LayoutManager layoutManager = getLayoutManager();
                if (null == layoutManager)
                    throw new Exception("layoutManager error: null");
                int spanCount = getSpanCount();
                if (spanCount <= 1)
                    throw new Exception("spanCount error: " + spanCount);
                Adapter adapter = getAdapter();
                if (null == adapter)
                    throw new Exception("adapter error: null");
                int itemCount = adapter.getItemCount();
                if (itemCount < 0)
                    throw new Exception("itemCount error: " + itemCount);
                int focusPosition = findFocusedChildPosition();
                if (focusPosition < 0)
                    throw new Exception("focusPosition error: " + focusPosition);
                if (itemCount - focusPosition <= spanCount)
                    throw new Exception("focusPosition warning: " + focusPosition + ", itemCount = " + itemCount + ", spanCount = " + spanCount);
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
                int focusedChildPosition = findFocusedChildPosition();
                setLastFocusChildPosition(focusedChildPosition);
                return true;
            } catch (Exception e) {
                leaveFromDown();
                LeanBackUtil.log("RecyclerViewGrid => dispatchKeyEvent => down-down => " + e.getMessage());
            }
        }
        // action_down => left
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {

            try {
                LayoutManager layoutManager = getLayoutManager();
                if (null == layoutManager)
                    throw new Exception("layoutManager error: null");
                int spanCount = getSpanCount();
                if (spanCount <= 1)
                    throw new Exception("spanCount error: " + spanCount);
                Adapter adapter = getAdapter();
                if (null == adapter)
                    throw new Exception("adapter error: null");
                int itemCount = adapter.getItemCount();
                if (itemCount < 0)
                    throw new Exception("itemCount error: " + itemCount);
                int focusPosition = findFocusedChildPosition();
                if (focusPosition < 0)
                    throw new Exception("focusPosition error: " + focusPosition);
                if (focusPosition % spanCount == 0)
                    throw new Exception("focusPosition warning: " + focusPosition + ", itemCount = " + itemCount + ", spanCount = " + spanCount);
                View focusedChild = getFocusedChild();
                if (null == focusedChild)
                    throw new Exception("focusedChild error: null");
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_LEFT);
                if (null == nextFocus)
                    throw new Exception("nextFocus warning: null");
                nextFocus.requestFocus();
                return true;
            } catch (Exception e) {
                leaveFromLeft();
                LeanBackUtil.log("RecyclerViewGrid => dispatchKeyEvent => left-down => " + e.getMessage());
            }
        }
        // action_down => right
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            try {
                LayoutManager layoutManager = getLayoutManager();
                if (null == layoutManager)
                    throw new Exception("layoutManager error: null");
                int spanCount = getSpanCount();
                if (spanCount <= 1)
                    throw new Exception("spanCount error: " + spanCount);
                Adapter adapter = getAdapter();
                if (null == adapter)
                    throw new Exception("adapter error: null");
                int itemCount = adapter.getItemCount();
                if (itemCount < 0)
                    throw new Exception("itemCount error: " + itemCount);
                int focusPosition = findFocusedChildPosition();
                if (focusPosition < 0)
                    throw new Exception("focusPosition error: " + focusPosition);
                if ((focusPosition % spanCount) == (spanCount - 1))
                    throw new Exception("focusPosition warning: " + focusPosition + ", itemCount = " + itemCount + ", spanCount = " + spanCount);
                View focusedChild = getFocusedChild();
                if (null == focusedChild)
                    throw new Exception("focusedChild error: null");
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_RIGHT);
                if (null == nextFocus)
                    throw new Exception("nextFocus warning: null");
                nextFocus.requestFocus();
                return true;
            } catch (Exception e) {
                leaveFromRight();
                LeanBackUtil.log("RecyclerViewGrid => dispatchKeyEvent => right-down => " + e.getMessage());
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void setScrollEnable(boolean enable) {
        try {
            LayoutManager layoutManager = getLayoutManager();
            if (null == layoutManager)
                throw new Exception("layoutManager error: null");
            if (!(layoutManager instanceof BaseGridLayoutManager))
                throw new Exception("layoutManager warning: not instanceof BaseGridLayoutManager");
            ((BaseGridLayoutManager) layoutManager).setCanScrollVertically(enable);
        } catch (Exception e) {
            LeanBackUtil.log("RecyclerViewGrid => setScrollEnable => " + e.getMessage());
        }
    }
}
