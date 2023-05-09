package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class BaseLeanBackGridViewHorizontal extends androidx.leanback.widget.HorizontalGridView {
    public BaseLeanBackGridViewHorizontal(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseLeanBackGridViewHorizontal(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseLeanBackGridViewHorizontal(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
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
        setFocusableInTouchMode(false);
    }
}
