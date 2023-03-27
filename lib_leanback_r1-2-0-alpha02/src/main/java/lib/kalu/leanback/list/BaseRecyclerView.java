package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

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

//    @Override
//    public void setLayoutManager(@Nullable LayoutManager layout) {
//        // StaggeredGridLayoutManager
//        if (layout instanceof StaggeredGridLayoutManager) {
//            LeanBackUtil.log("BaseRecyclerView => setLayoutManager[StaggeredGridLayoutManager] =>");
//            super.setLayoutManager(layout);
//        }
//        // GridLayoutManager
//        else if (layout instanceof GridLayoutManager) {
//            int orientation = ((GridLayoutManager) layout).getOrientation();
//            int spanCount = ((GridLayoutManager) layout).getSpanCount();
//            boolean reverseLayout = ((GridLayoutManager) layout).getReverseLayout();
//            LeanBackUtil.log("BaseRecyclerView => setLayoutManager[GridLayoutManager] => orientation = " + orientation + ", spanCount = " + spanCount + ", reverseLayout = " + reverseLayout);
//            GridLayoutManager.SpanSizeLookup spanSizeLookup = ((GridLayoutManager) layout).getSpanSizeLookup();
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), spanCount, orientation, reverseLayout) {
//                @Override
//                public boolean isAutoMeasureEnabled() {
//                    return false;
//                }
//            };
//            gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
//            super.setLayoutManager(gridLayoutManager);
//        }
//        // LinearLayoutManager
//        else {
//            int orientation = ((LinearLayoutManager) layout).getOrientation();
//            boolean reverseLayout = ((LinearLayoutManager) layout).getReverseLayout();
//            LeanBackUtil.log("BaseRecyclerView => setLayoutManager[LinearLayoutManager] => orientation = " + orientation + ", reverseLayout = " + reverseLayout);
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), orientation, reverseLayout) {
//                @Override
//                public boolean isAutoMeasureEnabled() {
//                    return false;
//                }
//            };
//            super.setLayoutManager(linearLayoutManager);
//        }
//    }

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
