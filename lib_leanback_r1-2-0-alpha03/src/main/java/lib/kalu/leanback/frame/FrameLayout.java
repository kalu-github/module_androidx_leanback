package lib.kalu.leanback.frame;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import lib.kalu.leanback.page.OnPageChangeListener;
import lib.kalu.leanback.util.LeanBackUtil;

public class FrameLayout extends android.widget.FrameLayout {
    private int mPressNumLeft = 0;
    private int mPressNumRight = 0;

    public FrameLayout(@NonNull Context context) {
        super(context);
    }

    public FrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        // ACTION_DOWN KEYCODE_DPAD_LEFT
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            try {
                mPressNumRight = 0;
                View focus = findFocus();
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focus, View.FOCUS_LEFT);
                if (null != nextFocus)
                    throw new Exception();
                focus.clearAnimation();
                if (mPressNumLeft >= 1) {
                    mPressNumLeft = 0;
                    mPressNumRight = 0;
                    onOutLeft();
                    return true;
                } else {
                    mPressNumLeft++;
                    shakeAnim();
                }
            } catch (Exception e) {
            }
        }
        // ACTION_DOWN KEYCODE_DPAD_RIGHT
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            try {
                mPressNumLeft = 0;
                View focus = findFocus();
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focus, View.FOCUS_RIGHT);
                if (null != focus && null == nextFocus) {
                    focus.clearAnimation();
                    if (mPressNumRight >= 1) {
                        mPressNumLeft = 0;
                        mPressNumRight = 0;
                        onOutRight();
                        return true;
                    } else {
                        mPressNumRight++;
                        shakeAnim();
                    }
                }
            } catch (Exception e) {
            }
        }
        // ACTION_DOWN KEYCODE_DPAD_UP
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            mPressNumLeft = 0;
            mPressNumRight = 0;
        }
        // ACTION_DOWN KEYCODE_DPAD_DOWN
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            mPressNumLeft = 0;
            mPressNumRight = 0;
        }
        return super.dispatchKeyEvent(event);
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

    protected final void onOutLeft() {
    }

    protected final void onOutRight() {
    }

    protected final void onOutTop() {
    }

    protected final void onOutBottom() {
    }
}
