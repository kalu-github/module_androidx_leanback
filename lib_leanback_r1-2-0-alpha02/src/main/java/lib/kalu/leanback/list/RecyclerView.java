package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

public class RecyclerView extends androidx.recyclerview.widget.RecyclerView {
    public RecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private final void init() {
        setAnimation(null);
        setItemAnimator(null);
        setAnimationCacheEnabled(false);
        setNestedScrollingEnabled(false);
        setHasFixedSize(true);
        setFocusableInTouchMode(false);
    }

    public final int getSpanCount() {
        try {
            return ((GridLayoutManager) getLayoutManager()).getSpanCount();
        } catch (Exception e) {
            return 1;
        }
    }

    public final int getItemCount() {
        try {
            return getAdapter().getItemCount();
        } catch (Exception e) {
            return 0;
        }
    }

    public final int findFocusPosition() {
        try {
            View focusedView = getFocusedChild();
            return getChildAdapterPosition(focusedView);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public final void smoothScrollToPosition(int position) {
        try {
            super.smoothScrollToPosition(position);
        } catch (Exception e) {
        }
    }

    public final void requestFocusChild(int position, @IdRes int id) {
        try {
            ViewHolder viewHolder = findViewHolderForAdapterPosition(position);
            viewHolder.itemView.findViewById(id).requestFocus();
        } catch (Exception e) {
        }
    }

    public final void performClickChild(int position, @IdRes int id) {
        try {
            ViewHolder viewHolder = findViewHolderForAdapterPosition(position);
            viewHolder.itemView.findViewById(id).performClick();
        } catch (Exception e) {
        }
    }
}
