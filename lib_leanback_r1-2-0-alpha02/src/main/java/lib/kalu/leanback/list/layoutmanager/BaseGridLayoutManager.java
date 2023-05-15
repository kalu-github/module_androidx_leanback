package lib.kalu.leanback.list.layoutmanager;


import android.content.Context;

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
}
