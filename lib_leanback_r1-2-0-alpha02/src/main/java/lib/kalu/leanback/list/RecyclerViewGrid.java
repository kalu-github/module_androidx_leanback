package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.leanback.util.LeanBackUtil;

public class RecyclerViewGrid extends RecyclerView {

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
        // left
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            boolean nextFocus = nextFocus(KeyEvent.KEYCODE_DPAD_LEFT);
            if (nextFocus) {
                return true;
            }
        }
        // right
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            boolean nextFocus = nextFocus(KeyEvent.KEYCODE_DPAD_RIGHT);
            if (nextFocus) {
                return true;
            }
        }
        // up
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            boolean nextFocus = nextFocus(KeyEvent.KEYCODE_DPAD_UP);
            if (nextFocus) {
                return true;
            }
        }
        // down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            boolean nextFocus = nextFocus(KeyEvent.KEYCODE_DPAD_DOWN);
            if (nextFocus) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private final boolean nextFocus(int keyCode) {
        // left
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            View focusedView = getFocusedChild();
            View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_LEFT);
            if (null != nextFocus) {
                nextFocus.requestFocus();
                return true;
            }
        }
        // right
        else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            View focusedView = getFocusedChild();
            View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_RIGHT);
            if (null != nextFocus) {
                nextFocus.requestFocus();
                return true;
            }
        }
        // up
        else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            int position = findFocusPosition();
            int spanCount = getSpanCount();
            // not 第一行
            if (position + 1 > spanCount) {
                boolean hasFocus = hasFocus(keyCode);
                if (hasFocus) {
                    boolean reqFocus = reqFocus(keyCode);
                    if (reqFocus) {
                        return true;
                    }
                } else {
                    int itemCount = getItemCount();
                    boolean scrollFocus = scrollFocus(keyCode, position, spanCount, itemCount);
                    if (scrollFocus) {
                        boolean reqFocus = reqFocus(keyCode);
                        if (reqFocus) {
                            return true;
                        }
                    }
                }
            }
        }
        // down
        else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            int position = findFocusPosition();
            int itemCount = getItemCount();
            int spanCount = getSpanCount();
            // 最后一行
            if (itemCount - position > spanCount) {
                boolean hasFocus = hasFocus(keyCode);
                if (hasFocus) {
                    boolean reqFocus = reqFocus(keyCode);
                    if (reqFocus) {
                        return true;
                    }
                } else {
                    boolean scrollFocus = scrollFocus(keyCode, position, spanCount, itemCount);
                    if (scrollFocus) {
                        boolean reqFocus = reqFocus(keyCode);
                        if (reqFocus) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private final boolean hasFocus(int keyCode) {
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
        // up
        else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_UP);
        }
        // down
        else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_DOWN);
        }
        if (null != nextFocus) {
            return true;
        }
        return false;
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
        // up
        else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
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

    private final boolean scrollFocus(int keyCode, int position, int spanCount, int itemCount) {

        // left
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
        }
        // right
        else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
        }
        // up
        else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            int next = position - spanCount;
            LeanBackUtil.log("RecyclerView => scrollFocus => up => position = " + position + ", next = " + next + ", spanCount = " + spanCount + ", itemCount = " + itemCount);
            while (true) {
                ViewHolder viewHolder = findViewHolderForAdapterPosition(next);
                LeanBackUtil.log("RecyclerView => scrollFocus => up => viewHolder = " + viewHolder);
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
            int next = position + spanCount;
            LeanBackUtil.log("RecyclerView => scrollFocus => down => position = " + position + ", next = " + next + ", spanCount = " + spanCount + ", itemCount = " + itemCount);
            while (true) {
                ViewHolder viewHolder = findViewHolderForAdapterPosition(next);
                LeanBackUtil.log("RecyclerView => scrollFocus => down => viewHolder = " + viewHolder);
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