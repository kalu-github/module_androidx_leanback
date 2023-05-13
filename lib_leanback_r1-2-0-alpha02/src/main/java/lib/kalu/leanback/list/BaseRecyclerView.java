package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.R;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

class BaseRecyclerView extends androidx.recyclerview.widget.RecyclerView {
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
        setFocusable(false);
        setFocusableInTouchMode(false);
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
}
