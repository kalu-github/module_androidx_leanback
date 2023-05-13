package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class BaseLeanBackGridViewVertical extends androidx.leanback.widget.VerticalGridView {
    public BaseLeanBackGridViewVertical(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseLeanBackGridViewVertical(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseLeanBackGridViewVertical(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private final void init() {
        setAnimation(null);
        setItemAnimator(null);
        setAnimationCacheEnabled(false);
        setNestedScrollingEnabled(false);
        setAnimateChildLayout(false);
        setHasFixedSize(true);
    }
}
