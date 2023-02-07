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

import lib.kalu.leanback.util.LeanBackUtil;

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
            mPressNumRight = 0;
            LeanBackUtil.log("PageView => dispatchKeyEvent => left => mPressNumLeft = " + mPressNumLeft + ", mPressNumRight = " + mPressNumRight);
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
        // right
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            mPressNumLeft = 0;
            LeanBackUtil.log("PageView => dispatchKeyEvent => right => mPressNumLeft = " + mPressNumLeft + ", mPressNumRight = " + mPressNumRight);
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

    private int mPressNumLeft = 0;
    private int mPressNumRight = 0;
    private OnPageChangeListener mListener;

    public void setOnPageChangeListener(OnPageChangeListener l) {
        mListener = l;
    }
}
