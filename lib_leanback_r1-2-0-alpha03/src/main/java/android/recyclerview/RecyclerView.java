package android.recyclerview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 1001) {
                    Glide.with((Context) msg.obj).resumeRequests();
                } else {
                    Glide.with((Context) msg.obj).pauseRequests();
                }
            } catch (Exception e) {
                LeanBackUtil.log("RecyclerView => handleMessage => " + e.getMessage());
            }
        }
    };

    /**
     * SCROLL_STATE_IDLE: //当屏幕停止滚动，加载图片
     * SCROLL_STATE_DRAGGING: //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
     * SCROLL_STATE_SETTLING: //由于用户的操作，屏幕产生惯性滑动，停止加载图片
     */
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        try {
            Message message = Message.obtain();
            message.obj = getContext().getApplicationContext();
            if (state == SCROLL_STATE_IDLE) {
                message.what = 1001;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendMessageDelayed(message, 400);
            } else {
                message.what = 1002;
                mHandler.sendMessage(message);
            }
        } catch (Exception e) {
            LeanBackUtil.log("RecyclerView => onScrollStateChanged => " + e.getMessage());
        }
    }
}
