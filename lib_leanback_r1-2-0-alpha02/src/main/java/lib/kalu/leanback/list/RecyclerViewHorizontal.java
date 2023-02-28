package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.leanback.util.LeanBackUtil;

public class RecyclerViewHorizontal extends RecyclerView {

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
    public boolean dispatchKeyEvent(KeyEvent event) {
        // left or right
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            boolean reqFocus = reqFocus(KeyEvent.KEYCODE_DPAD_LEFT);
            if (reqFocus) {
                return true;
            } else {
                int position = findFocusPosition();
                int itemCount = getItemCount();
                boolean scrollFocus = scrollFocus(KeyEvent.KEYCODE_DPAD_LEFT, position, itemCount);
                if (scrollFocus) {
                    boolean reqFocus1 = reqFocus(KeyEvent.KEYCODE_DPAD_LEFT);
                    if (reqFocus1) {
                        return true;
                    }
                }
            }
        }
        // right
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            boolean reqFocus = reqFocus(KeyEvent.KEYCODE_DPAD_RIGHT);
            if (reqFocus) {
                return true;
            } else {
                int position = findFocusPosition();
                int itemCount = getItemCount();
                boolean scrollFocus = scrollFocus(KeyEvent.KEYCODE_DPAD_RIGHT, position, itemCount);
                if (scrollFocus) {
                    boolean reqFocus1 = reqFocus(KeyEvent.KEYCODE_DPAD_RIGHT);
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
        // left
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_LEFT);
        }
        // right
        else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_RIGHT);
        }
        if (null != nextFocus) {
            nextFocus.requestFocus();
            return true;
        }
        return false;
    }

    private final boolean scrollFocus(int keyCode, int position, int itemCount) {
        // left
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (position <= 0)
                return false;
            int next = position - 1;
            LeanBackUtil.log("RecyclerViewHorizontal => scrollFocus => left => position = " + position + ", next = " + next + ", itemCount = " + itemCount);
            while (true) {
                ViewHolder viewHolder = findViewHolderForAdapterPosition(next);
                LeanBackUtil.log("RecyclerViewHorizontal => scrollFocus => left => viewHolder = " + viewHolder);
                if (null != viewHolder) {
                    View itemView = viewHolder.itemView;
                    if (null != itemView) {
                        int width = itemView.getWidth();
                        scrollBy(-width, 0);
                    }
                    return true;
                }
                scrollBy(-1, 0);
            }
        }
        // right
        else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            int next = position + 1;
            if (next >= itemCount)
                return false;
            LeanBackUtil.log("RecyclerViewHorizontal => scrollFocus => right => position = " + position + ", next = " + next + ", itemCount = " + itemCount);
            while (true) {
                ViewHolder viewHolder = findViewHolderForAdapterPosition(next);
                LeanBackUtil.log("RecyclerViewHorizontal => scrollFocus => right => viewHolder = " + viewHolder);
                if (null != viewHolder) {
                    View itemView = viewHolder.itemView;
                    if (null != itemView) {
                        int width = itemView.getWidth();
                        scrollBy(width, 0);
                    }
                    return true;
                }
                scrollBy(1, 0);
            }
        }
        return false;
    }

    @Override
    public void scrollToPosition(int position) {
//        super.scrollToPosition(position);

        while (true) {
            try {
                if (position < 0)
                    throw new Exception("position error: " + position);
                int itemCount = getAdapter().getItemCount();
                if (position + 1 >= itemCount)
                    throw new Exception("itemCount error: " + itemCount);
                View focusedChild = getFocusedChild();
                int fromPosition = findFocusedChildFromPosition(focusedChild);
                if (fromPosition == position) {
                    focusedChild.requestFocus();
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

    private int findFocusedChildFromPosition(View focusedChild) throws Exception {
        while (true) {
            if (null == focusedChild)
                throw new Exception("focusedChild error: null");
            ViewParent parent = focusedChild.getParent();
            if (parent instanceof RecyclerViewHorizontal) {
                return getChildAdapterPosition(focusedChild);
            } else {
                return findFocusedChildFromPosition((View) parent);
            }
        }
    }
}
