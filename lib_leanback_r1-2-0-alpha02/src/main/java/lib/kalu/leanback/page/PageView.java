package lib.kalu.leanback.page;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.leanback.R;

public class PageView extends FrameLayout {
    public PageView(@NonNull Context context) {
        super(context);
    }

    public PageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // left
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            View focus = findFocus();
            View nextFocus = FocusFinder.getInstance().findNextFocus(this, focus, View.FOCUS_LEFT);
            if (null != focus && null == nextFocus) {
                shakeAnim();
                if (null != mListener) {
                    mListener.onLeft();
                }
            }
        }
        // right
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            View focus = findFocus();
            View nextFocus = FocusFinder.getInstance().findNextFocus(this, focus, View.FOCUS_RIGHT);
            if (null != focus && null == nextFocus) {
                shakeAnim();
                if (null != mListener) {
                    mListener.onRight();
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private final void shakeAnim() {
        View focus = findFocus();
        if (null == focus)
            return;
        Context context = getContext().getApplicationContext();
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.anim_shake);
        focus.startAnimation(shake);
    }

    private OnPageChangeListener mListener;

    public void setOnPageChangeListener(OnPageChangeListener l) {
        mListener = l;
    }
}
