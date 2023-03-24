package lib.kalu.leanback.list.layoutmanager;

import android.content.Context;
import android.util.AttributeSet;

public class AutoMeasureNoLinearLayoutManager extends androidx.recyclerview.widget.LinearLayoutManager {
    public AutoMeasureNoLinearLayoutManager(Context context) {
        super(context);
    }

    public AutoMeasureNoLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public AutoMeasureNoLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return false;
    }
}
