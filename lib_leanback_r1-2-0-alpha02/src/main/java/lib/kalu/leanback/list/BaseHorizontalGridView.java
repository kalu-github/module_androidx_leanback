package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

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

    /*****************/

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        LeanBackUtil.log("BaseHorizontalGridView => onScrollStateChanged => state = " + state);
        switch (state) {
            case SCROLL_STATE_IDLE: //当屏幕停止滚动，加载图片
                try {
                    Glide.with(getContext()).resumeRequests();
                    LeanBackUtil.log("BaseHorizontalGridView => onScrollStateChanged => resumeRequests => succ");
                } catch (Exception e) {
                    LeanBackUtil.log("BaseHorizontalGridView => onScrollStateChanged => resumeRequests => fail");
                }
                break;
            case SCROLL_STATE_DRAGGING: //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
            case SCROLL_STATE_SETTLING: //由于用户的操作，屏幕产生惯性滑动，停止加载图片
            default:
                try {
                    Glide.with(getContext()).pauseRequests();
                    LeanBackUtil.log("BaseHorizontalGridView => onScrollStateChanged => pauseRequests => succ");
                } catch (Exception e) {
                    LeanBackUtil.log("BaseHorizontalGridView => onScrollStateChanged => pauseRequests => fail");
                }
                break;
        }
    }
}
