package lib.kalu.leanback.list;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import lib.kalu.leanback.util.LeanBackUtil;

public class ListView extends android.widget.ListView {
    public ListView(@NonNull Context context) {
        super(context);
        init();
    }

    public ListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
