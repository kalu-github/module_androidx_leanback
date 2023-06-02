package lib.kalu.leanback.page;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

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
        boolean dispatchKeyEvent = super.dispatchKeyEvent(event);
        // action_down => keycode_dpad_left
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            mPressNumRight = 0;
            if (!dispatchKeyEvent) {
                View focus = findFocus();
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, focus, View.FOCUS_LEFT);
                if (null != focus && null == nextFocus) {
                    if (mPressNumLeft >= 1) {
                        if (null != mListener) {
                            mPressNumLeft = 0;
                            mPressNumRight = 0;
                            mListener.onLeft();
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
                    if (mPressNumRight >= 1) {
                        if (null != mListener) {
                            mPressNumLeft = 0;
                            mPressNumRight = 0;
                            mListener.onRight();
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

    private ObjectAnimator mObjectAnimator;

    private final void shakeAnim() {

        if (null != mObjectAnimator) {
            mObjectAnimator.removeAllListeners();
            mObjectAnimator.cancel();
            mObjectAnimator = null;
        }

        View focus = findFocus();
        if (null == focus)
            return;

        mObjectAnimator = ObjectAnimator.ofFloat(focus, "translationX", -4F, 0F, 4F, 0F);
        mObjectAnimator.setDuration(40);
        mObjectAnimator.setRepeatCount(1);
        mObjectAnimator.setInterpolator(new CycleInterpolator(2));
        mObjectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                focus.requestFocus();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                focus.requestFocus();
            }

            @Override
            public void onAnimationPause(Animator animation) {
                focus.requestFocus();
            }
        });
        mObjectAnimator.start();
    }

    private int mPressNumLeft = 0;
    private int mPressNumRight = 0;
    private OnPageChangeListener mListener;

    public void setOnPageChangeListener(OnPageChangeListener l) {
        mListener = l;
    }
}
