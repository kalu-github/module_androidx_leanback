package lib.kalu.leanback.list;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GridView extends android.widget.GridView {
    public GridView(@NonNull Context context) {
        super(context);
        init();
    }

    public GridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setAnimation(null);
        setAnimationCacheEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNestedScrollingEnabled(false);
        }
        setFocusableInTouchMode(false);
    }
}
