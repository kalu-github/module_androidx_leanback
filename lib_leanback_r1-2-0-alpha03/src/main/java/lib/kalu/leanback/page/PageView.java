package lib.kalu.leanback.page;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import lib.kalu.leanback.util.LeanBackUtil;

public class PageView extends FrameLayout {
    private int mPressNumLeft = 0;
    private int mPressNumRight = 0;
    private OnPageChangeListener mListener;

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
        boolean dispatchKeyEvent = super.dispatchKeyEvent(event);
        // action_down => keycode_dpad_left
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            mPressNumRight = 0;
            if (!dispatchKeyEvent) {
                View focus = findFocus();
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focus, View.FOCUS_LEFT);
                if (null != focus && null == nextFocus) {
                    focus.clearAnimation();
                    if (mPressNumLeft >= 1) {
                        if (null != mListener) {
                            mPressNumLeft = 0;
                            mPressNumRight = 0;
                            mListener.onLeft();
                            return true;
                        }
                    } else {
                        mPressNumLeft++;
                        shakeAnim();
                    }
                }
            }
        }
        // action_down => keycode_dpad_right
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            mPressNumLeft = 0;
            if (!dispatchKeyEvent) {
                View focus = findFocus();
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focus, View.FOCUS_RIGHT);
                if (null != focus && null == nextFocus) {
                    focus.clearAnimation();
                    if (mPressNumRight >= 1) {
                        if (null != mListener) {
                            mPressNumLeft = 0;
                            mPressNumRight = 0;
                            mListener.onRight();
                            return true;
                        }
                    } else {
                        mPressNumRight++;
                        shakeAnim();
                    }
                }
            }
        }
        // action_down => keycode_dpad_up
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            mPressNumLeft = 0;
            mPressNumRight = 0;
        }
        // action_down => keycode_dpad_down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            mPressNumLeft = 0;
            mPressNumRight = 0;
        }
        return dispatchKeyEvent;
    }

    private void shakeAnim() {
        try {
            View focus = findFocus();
            if (null == focus)
                throw new Exception("focus error: null");
            Animation animation = new TranslateAnimation(-4F, 4F, 0, 0);
            CycleInterpolator cycleInterpolator = new CycleInterpolator(1);
            animation.setInterpolator(cycleInterpolator);
            animation.setDuration(40);
            focus.clearAnimation();
            focus.startAnimation(animation);
        } catch (Exception e) {
            LeanBackUtil.log("PageView => shakeAnim => " + e.getMessage());
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener l) {
        mListener = l;
    }
}
