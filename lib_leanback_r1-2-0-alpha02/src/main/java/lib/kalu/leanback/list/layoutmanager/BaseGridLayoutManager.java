package lib.kalu.leanback.list.layoutmanager;


import android.content.Context;
import android.util.AttributeSet;

public class BaseGridLayoutManager extends androidx.recyclerview.widget.GridLayoutManager {

    public BaseGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public BaseGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return false;
    }

    private boolean mCanScrollVertically = true;

    public void setCanScrollVertically(boolean mCanScrollVertically) {
        this.mCanScrollVertically = mCanScrollVertically;
    }

//    @Override
//    public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent, @NonNull View child, @NonNull Rect rect, boolean immediate) {
//        return canScrollVertically() && super.requestChildRectangleOnScreen(parent, child, rect, immediate);
//    }
//
//    @Override
//    public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent, @NonNull View child, @NonNull Rect rect, boolean immediate, boolean focusedChildVisible) {
//        return canScrollVertically() && super.requestChildRectangleOnScreen(parent, child, rect, immediate, focusedChildVisible);
//    }

    @Override
    public boolean canScrollVertically() {
        return this.mCanScrollVertically;
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }
}
