package lib.kalu.leanback.list.layoutmanager;


import android.content.Context;
import android.util.AttributeSet;

public class AutoMeasureNoGridLayoutManager extends androidx.recyclerview.widget.GridLayoutManager {

    public AutoMeasureNoGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AutoMeasureNoGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public AutoMeasureNoGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return false;
    }
}
