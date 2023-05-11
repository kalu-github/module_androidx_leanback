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
        // up
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
                LeanBackUtil.log("RecyclerViewGrid => dispatchKeyEvent => up => " + e.getMessage());
            }
        }
        // down
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
                return true;
            } catch (Exception e) {
                LeanBackUtil.log("RecyclerViewGrid => dispatchKeyEvent => down => " + e.getMessage());
            }
        }
//        // left
//        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
//            try {
//                View focusedChild = getFocusedChild();
//                if (null == focusedChild)
//                    throw new Exception("focusedChild error: null");
//                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_LEFT);
//                if (null != nextFocus)
//                    throw new Exception("nextFocus warning: " + nextFocus);
//                LayoutManager layoutManager = getLayoutManager();
//                if (null == layoutManager)
//                    throw new Exception("layoutManager error: null");
//                if (!(layoutManager instanceof BaseGridLayoutManager))
//                    throw new Exception("layoutManager warning: not instanceof BaseGridLayoutManager");
//                ((BaseGridLayoutManager) layoutManager).setCanScrollVertically(false);
//            } catch (Exception e) {
//                LeanBackUtil.log("RecyclerViewGrid => dispatchKeyEvent => left => " + e.getMessage());
//            }
//        }
//        // right-iin
//        else if (event.getAction() == KeyEvent.ACTION_UP) {
//            switch (event.getKeyCode()) {
//                case KeyEvent.KEYCODE_DPAD_RIGHT:
//                case KeyEvent.KEYCODE_DPAD_LEFT:
//                case KeyEvent.KEYCODE_DPAD_UP:
//                case KeyEvent.KEYCODE_DPAD_DOWN:
//                    try {
//                        LayoutManager layoutManager = getLayoutManager();
//                        LeanBackUtil.log("RecyclerViewVertical => dispatchKeyEvent => into => layoutManager = " + layoutManager);
//                        if (null == layoutManager)
//                            throw new Exception("layoutManager error: null");
//                        if (!(layoutManager instanceof BaseGridLayoutManager))
//                            throw new Exception("layoutManager warning: not instanceof BaseGridLayoutManager");
//                        ((BaseGridLayoutManager) layoutManager).setCanScrollVertically(true);
//                    } catch (Exception e) {
//                        LeanBackUtil.log("RecyclerViewGrid => dispatchKeyEvent => into => " + e.getMessage());
//                    }
//                    break;
//            }
//        }
        return super.dispatchKeyEvent(event);
    }
}
