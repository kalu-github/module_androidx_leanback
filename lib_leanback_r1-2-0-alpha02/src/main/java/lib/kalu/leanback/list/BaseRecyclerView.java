package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.R;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lib.kalu.leanback.util.LeanBackUtil;

class BaseRecyclerView extends androidx.recyclerview.widget.RecyclerView {

    private boolean mBlockDescendants = false;

    public BaseRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private final void init() {
        setAnimation(null);
        setItemAnimator(null);
        setAnimationCacheEnabled(false);
        setNestedScrollingEnabled(false);
        setHasFixedSize(true);

        try {
            int descendantFocusability = getDescendantFocusability();
            if (descendantFocusability != ViewGroup.FOCUS_BLOCK_DESCENDANTS)
                throw new Exception();
            mBlockDescendants = true;
        } catch (Exception e) {
            mBlockDescendants = false;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        // into
        try {
            if (!mBlockDescendants)
                throw new Exception();
            View focus = findFocus();
            LeanBackUtil.log("BaseRecyclerView => dispatchKeyEvent => focus = "+focus);
            // action_up => up => from down
            if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                int focusChildPosition = getLastFocusChildPosition();
                ViewHolder viewHolder = findViewHolderForAdapterPosition(focusChildPosition);
                if (null == viewHolder || null == viewHolder.itemView)
                    throw new Exception();
                viewHolder.itemView.requestFocus();
                intoFromDown();
                return true;
            }
            // action_up => down => from up
            else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                int focusChildPosition = getLastFocusChildPosition();
                ViewHolder viewHolder = findViewHolderForAdapterPosition(focusChildPosition);
                if (null == viewHolder || null == viewHolder.itemView)
                    throw new Exception();
                viewHolder.itemView.requestFocus();
                intoFromUp();
                return true;
            }
            // action_up => up => from left
            else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                int focusChildPosition = getLastFocusChildPosition();
                ViewHolder viewHolder = findViewHolderForAdapterPosition(focusChildPosition);
                if (null == viewHolder || null == viewHolder.itemView)
                    throw new Exception();
                viewHolder.itemView.requestFocus();
                intoFromLeft();
                return true;
            }
            // action_up => up => from right
            else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                int focusChildPosition = getLastFocusChildPosition();
                ViewHolder viewHolder = findViewHolderForAdapterPosition(focusChildPosition);
                if (null == viewHolder || null == viewHolder.itemView)
                    throw new Exception();
                viewHolder.itemView.requestFocus();
                intoFromLeft();
                return true;
            }
        } catch (Exception e) {
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        try {
            if (null == adapter)
                throw new Exception("adapter error: null");
            super.setAdapter(adapter);
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            });
        } catch (Exception e) {
        }
    }

    public void setLastFocusChildPosition(int position) {
        try {
            setTag(R.id.lb_baserecyclerview_last_focus_child_position, position);
        } catch (Exception e) {
        }
    }

    public int getLastFocusChildPosition() {
        try {
            Object tag = getTag(R.id.lb_baserecyclerview_last_focus_child_position);
            if (null == tag)
                throw new Exception();
            return (int) tag;
        } catch (Exception e) {
            return findCurrentFirstVisableChildPosition();
        }
    }

    private int findCurrentFirstVisableChildPosition() {
        try {
            int itemCount = getAdapterItemCount();
            if (itemCount <= 0)
                throw new Exception();
            for (int i = 0; i < itemCount; i++) {
                ViewHolder viewHolder = findViewHolderForAdapterPosition(i);
                if (null == viewHolder)
                    continue;
                int visibility = viewHolder.itemView.getVisibility();
                if (visibility != View.VISIBLE)
                    continue;
                return i;
            }
            throw new Exception();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public final void smoothScrollToPosition(int position) {
        try {
            super.smoothScrollToPosition(position);
        } catch (Exception e) {
        }
    }

    public int getSpanCount() {
        try {
            return ((GridLayoutManager) getLayoutManager()).getSpanCount();
        } catch (Exception e) {
            return 1;
        }
    }

    public void requestFocusChild(int position, @IdRes int id) {
        try {
            ViewHolder viewHolder = findViewHolderForAdapterPosition(position);
            viewHolder.itemView.findViewById(id).requestFocus();
        } catch (Exception e) {
        }
    }

    public void performClickChild(int position, @IdRes int id) {
        try {
            ViewHolder viewHolder = findViewHolderForAdapterPosition(position);
            viewHolder.itemView.findViewById(id).performClick();
        } catch (Exception e) {
        }
    }

    public int findFocusedChildPosition() {
        try {
            View focusedView = getFocusedChild();
            if (null == focusedView)
                throw new Exception("focusedView error: null");
            while (true) {
                ViewParent parent = focusedView.getParent();
                if (parent instanceof BaseRecyclerView) {
                    int adapterPosition = getChildAdapterPosition(focusedView);
                    if (adapterPosition < 0)
                        throw new Exception("adapterPosition error: " + adapterPosition);
                    return adapterPosition;
                } else {
                    focusedView = (View) parent;
                }
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public int getAdapterItemCount() {
        try {
            Adapter adapter = getAdapter();
            if (null == adapter)
                throw new Exception("adapter error: null");
            return adapter.getItemCount();
        } catch (Exception e) {
            return 0;
        }
    }

    public void scrollFocusedChild(int direction) {
    }

    public void scrollTop(boolean hasFocus) {
    }

    public void scrollBottom(boolean hasFocus) {
    }

    public void leaveFromLeft() {
        try {
            if (!mBlockDescendants)
                throw new Exception();
            setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        } catch (Exception e) {
        }
    }

    public void leaveFromRight() {
        try {
            if (!mBlockDescendants)
                throw new Exception();
            setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        } catch (Exception e) {
        }
    }

    public void leaveFromUp() {
        try {
            if (!mBlockDescendants)
                throw new Exception();
            setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        } catch (Exception e) {
        }
    }

    public void leaveFromDown() {
        try {
            if (!mBlockDescendants)
                throw new Exception();
            setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        } catch (Exception e) {
        }
    }

    public void intoFromLeft() {
        try {
            if (!mBlockDescendants)
                throw new Exception();
            setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        } catch (Exception e) {
        }
    }

    public void intoFromRight() {
        try {
            if (!mBlockDescendants)
                throw new Exception();
            setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        } catch (Exception e) {
        }
    }

    public void intoFromUp() {
        try {
            if (!mBlockDescendants)
                throw new Exception();
            setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        } catch (Exception e) {
        }
    }

    public void intoFromDown() {
        try {
            if (!mBlockDescendants)
                throw new Exception();
            setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        } catch (Exception e) {
        }
    }
}
