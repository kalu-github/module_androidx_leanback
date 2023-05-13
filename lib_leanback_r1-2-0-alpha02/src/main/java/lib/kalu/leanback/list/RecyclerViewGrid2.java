package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.leanback.list.layoutmanager.BaseGridLayoutManager;
import lib.kalu.leanback.util.LeanBackUtil;

public class RecyclerViewGrid2 extends RecyclerViewGrid {

    public RecyclerViewGrid2(@NonNull Context context) {
        super(context);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }

    public RecyclerViewGrid2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }

    public RecyclerViewGrid2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    private void setScrollEnable(boolean enable) {
        try {
            LayoutManager layoutManager = getLayoutManager();
            if (null == layoutManager)
                throw new Exception("layoutManager error: null");
            if (!(layoutManager instanceof BaseGridLayoutManager))
                throw new Exception("layoutManager warning: not instanceof BaseGridLayoutManager");
            ((BaseGridLayoutManager) layoutManager).setCanScrollVertically(enable);
        } catch (Exception e) {
            LeanBackUtil.log("RecyclerViewGrid => setScrollEnable => " + e.getMessage());
        }
    }
}
