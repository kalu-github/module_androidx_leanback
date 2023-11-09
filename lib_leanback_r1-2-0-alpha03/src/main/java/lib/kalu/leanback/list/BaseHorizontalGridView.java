package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import lib.kalu.leanback.util.LeanBackUtil;

class BaseHorizontalGridView extends androidx.leanback.widget.HorizontalGridView {
    public BaseHorizontalGridView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseHorizontalGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseHorizontalGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setAnimation(null);
        setItemAnimator(null);
        setAnimationCacheEnabled(false);
        setNestedScrollingEnabled(false);
        setAnimateChildLayout(false);
        setHasFixedSize(true);
    }
}
