package lib.kalu.leanback.recycler;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import lib.kalu.leanback.util.LeanBackUtil;

public class RecyclerView extends androidx.recyclerview.widget.RecyclerView {
    public RecyclerView(@NonNull Context context) {
        super(context);
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * SCROLL_STATE_IDLE: //当屏幕停止滚动，加载图片
     * SCROLL_STATE_DRAGGING: //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
     * SCROLL_STATE_SETTLING: //由于用户的操作，屏幕产生惯性滑动，停止加载图片
     */
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        try {
            if (state == SCROLL_STATE_IDLE) {
                Glide.with(getContext().getApplicationContext()).resumeRequests();
            } else {
                Glide.with(getContext().getApplicationContext()).pauseRequests();
            }
        } catch (Exception e) {
            LeanBackUtil.log("RecyclerView => onScrollStateChanged => " + e.getMessage());
        }
    }
}
