package lib.kalu.leanback.list.layoutmanager;


import android.content.Context;
import android.util.AttributeSet;

public class BaseGridLayoutManager extends androidx.recyclerview.widget.GridLayoutManager {

    public BaseGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

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

    @Override
    public boolean canScrollVertically() {
        return mCanScrollVertically;
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }
}
