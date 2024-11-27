package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.recyclerview.widget.RecyclerView;

import lib.kalu.leanback.util.LeanBackUtil;

class BaseVerticalGridView extends androidx.leanback.widget.VerticalGridView {
    public BaseVerticalGridView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseVerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseVerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setAnimation(null);
        setItemAnimator(null);
        setAnimationCacheEnabled(false);
        setNestedScrollingEnabled(false);
        setAnimateChildLayout(false);
        setHasFixedSize(true);
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        // ACTION_UP KEYCODE_DPAD_DOWN
        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            int focusedChildAtPosition = getFocusedChildAtPosition();
            if (focusedChildAtPosition == 0) {
                onFocusInto();
            }
            if (focusedChildAtPosition >= 0) {
                onDown(focusedChildAtPosition);
            }
        }
        // ACTION_UP KEYCODE_DPAD_UP
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            int focusedChildAtPosition = getFocusedChildAtPosition();
            if (focusedChildAtPosition == 0) {
                onFocusOut();
            }
        }
        // ACTION_UP KEYCODE_DPAD_UP
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            int focusedChildAtPosition = getFocusedChildAtPosition();
            if (focusedChildAtPosition >= 0) {
                onUp(focusedChildAtPosition);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public View getFocusedChild() {
        try {
            View focusedChild = findFocus();
            if (null == focusedChild)
                throw new Exception("focusedChild is null");
            return focusedChild;
        } catch (Exception e) {
            LeanBackUtil.log("BaseVerticalGridView => findFocusChild => " + e.getMessage());
            return null;
        }
    }

    protected final int getFocusedChildAtPosition() {
        try {
            View focusedChild = getFocusedChild();
            if (null == focusedChild)
                throw new Exception("focusedChild is null");
            int position = -1;
            while (true) {
                ViewParent parent = focusedChild.getParent();
                if (parent instanceof BaseVerticalGridView) {
                    position = getChildAdapterPosition(focusedChild);
                    break;
                }
                focusedChild = (View) parent;
            }
            if (position == -1)
                throw new Exception("position error: " + position);
            return position;
        } catch (Exception e) {
            LeanBackUtil.log("BaseVerticalGridView => getFocusedChildAtPosition => " + e.getMessage());
            return -1;
        }
    }

    protected void onFocusInto() {
    }

    protected void onFocusOut() {
    }

    protected void onUp(int index) {
    }

    protected void onDown(int index) {
    }
}
