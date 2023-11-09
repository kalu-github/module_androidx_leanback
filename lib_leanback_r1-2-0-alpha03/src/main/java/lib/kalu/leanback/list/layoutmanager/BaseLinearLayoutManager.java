package lib.kalu.leanback.list.layoutmanager;

import android.content.Context;
import android.util.AttributeSet;

public class BaseLinearLayoutManager extends androidx.recyclerview.widget.LinearLayoutManager {
    public BaseLinearLayoutManager(Context context) {
        super(context);
    }

    public BaseLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public BaseLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return false;
    }
}
