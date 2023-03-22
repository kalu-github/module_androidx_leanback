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
        // up
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            try {
                LayoutManager layoutManager = getLayoutManager();
                if(null == layoutManager)
                    throw new Exception("layoutManager error: null");
                int focusPosition = findFocusPosition();
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
                LeanBackUtil.log("RecyclerViewVertical => dispatchKeyEvent => up => focusPosition = " + focusPosition + ", height = " + height);
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
                if(null == layoutManager)
                    throw new Exception("layoutManager error: null");
                Adapter adapter = getAdapter();
                if (null == adapter)
                    throw new Exception("adapter error: null");
                int itemCount = adapter.getItemCount();
                if (itemCount < 0)
                    throw new Exception("itemCount error: " + itemCount);
                int focusPosition = findFocusPosition();
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
                LeanBackUtil.log("RecyclerViewVertical => dispatchKeyEvent => down => focusPosition = " + focusPosition + ", itemCount = " + itemCount + ", height = " + height);
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
}