package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import lib.kalu.leanback.util.LeanBackUtil;

public class RecyclerViewVertical extends RecyclerView {

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
            boolean reqFocus = reqFocus(KeyEvent.KEYCODE_DPAD_UP);
            if (reqFocus) {
                return true;
            } else {
                int position = findFocusPosition();
                int itemCount = getItemCount();
                boolean scrollFocus = scrollFocus(KeyEvent.KEYCODE_DPAD_UP, position, itemCount);
                if (scrollFocus) {
                    boolean reqFocus1 = reqFocus(KeyEvent.KEYCODE_DPAD_UP);
                    if (reqFocus1) {
                        return true;
                    }
                }
            }
        }
        // down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            boolean reqFocus = reqFocus(KeyEvent.KEYCODE_DPAD_DOWN);
            if (reqFocus) {
                return true;
            } else {
                int position = findFocusPosition();
                int itemCount = getItemCount();
                boolean scrollFocus = scrollFocus(KeyEvent.KEYCODE_DPAD_DOWN, position, itemCount);
                if (scrollFocus) {
                    boolean reqFocus1 = reqFocus(KeyEvent.KEYCODE_DPAD_DOWN);
                    if (reqFocus1) {
                        return true;
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private final boolean reqFocus(int keyCode) {
        View focusedView = getFocusedChild();
        View nextFocus = null;
        // up
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_UP);
        }
        // down
        else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_DOWN);
        }
        if (null != nextFocus) {
            nextFocus.requestFocus();
            return true;
        }
        return false;
    }

    private final boolean scrollFocus(int keyCode, int position, int itemCount) {
        // up
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (position <= 0)
                return false;
            int next = position - 1;
            LeanBackUtil.log("RecyclerViewHorizontal => scrollFocus => up => position = " + position + ", next = " + next + ", itemCount = " + itemCount);
            while (true) {
                ViewHolder viewHolder = findViewHolderForAdapterPosition(next);
                LeanBackUtil.log("RecyclerViewHorizontal => scrollFocus => up => viewHolder = " + viewHolder);
                if (null != viewHolder) {
                    View itemView = viewHolder.itemView;
                    if (null != itemView) {
                        int height = itemView.getHeight();
                        scrollBy(0, -height);
                    }
                    return true;
                }
                scrollBy(0, -1);
            }
        }
        // down
        else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            int next = position + 1;
            if (next >= itemCount)
                return false;
            LeanBackUtil.log("RecyclerViewHorizontal => scrollFocus => down => position = " + position + ", next = " + next + ", itemCount = " + itemCount);
            while (true) {
                ViewHolder viewHolder = findViewHolderForAdapterPosition(next);
                LeanBackUtil.log("RecyclerViewHorizontal => scrollFocus => down => viewHolder = " + viewHolder);
                if (null != viewHolder) {
                    View itemView = viewHolder.itemView;
                    if (null != itemView) {
                        int height = itemView.getHeight();
                        scrollBy(0, height);
                    }
                    return true;
                }
                scrollBy(0, 1);
            }
        }
        return false;
    }
}