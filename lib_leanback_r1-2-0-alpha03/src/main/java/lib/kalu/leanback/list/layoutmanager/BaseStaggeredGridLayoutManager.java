package lib.kalu.leanback.list.layoutmanager;

import android.content.Context;
import android.util.AttributeSet;

public class BaseStaggeredGridLayoutManager extends androidx.recyclerview.widget.StaggeredGridLayoutManager {

    public BaseStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BaseStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return false;
    }
}
