package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

        if (event.getRepeatCount() > 0)
            return true;
//        LeanBackUtil.log("RecyclerViewGrid => dispatchKeyEvent => action = " + event.getAction() + ", keyCode = " + event.getKeyCode());

        addLoadmoreListener(event);

        // action_down => keycode_dpad_up
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
                if (null == nextFocus) {
                    int height = focusedChild.getHeight();
                    if (height < 0)
                        throw new Exception("height error: " + height);
                    scrollBy(0, -height);
                }
            } catch (Exception e) {
                leave(ViewGroup.FOCUS_UP);
                LeanBackUtil.log("RecyclerViewGrid => dispatchKeyEvent => up-down => " + e.getMessage());
            }
        }
        // action_down => keycode_dpad_down
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
                int min = Math.min(itemCount % spanCount, spanCount);
                if (itemCount - focusPosition <= min)
                    throw new Exception("focusPosition warning: " + focusPosition + ", itemCount = " + itemCount + ", spanCount = " + spanCount + ", min = " + min);
                View focusedChild = getFocusedChild();
                if (null == focusedChild)
                    throw new Exception("focusedChild error: null");
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_DOWN);
                if (null == nextFocus) {
                    int height = focusedChild.getHeight();
                    if (height < 0)
                        throw new Exception("height error: " + height);
                    scrollBy(0, height);
                }
            } catch (Exception e) {
                leave(ViewGroup.FOCUS_DOWN);
                LeanBackUtil.log("RecyclerViewGrid => dispatchKeyEvent => down-down => " + e.getMessage());
            }
        }
        // action_down => keycode_dpad_left
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
                leave(ViewGroup.FOCUS_LEFT);
                leaveScrollFocusedChild();
                LeanBackUtil.log("RecyclerViewGrid => dispatchKeyEvent => left-down => " + e.getMessage());
            }
        }
        // action_down => keycode_dpad_right
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
                leave(ViewGroup.FOCUS_RIGHT);
                leaveScrollFocusedChild();
                LeanBackUtil.log("RecyclerViewGrid => dispatchKeyEvent => right-down => " + e.getMessage());
            }
        }
        return super.dispatchKeyEvent(event);
    }
}