package lib.kalu.leanback.clazz;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;

import lib.kalu.leanback.util.LeanBackUtil;
import lib.kalu.leanback.util.ViewUtil;

/**
 * 垂直
 */
public final class VerticalClassLayout extends BaseScrollView implements ClassLayoutImpl {

    public VerticalClassLayout(Context context) {
        super(context);
    }

    public VerticalClassLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalClassLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VerticalClassLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOrientation(RadioGroup radioGroup) {
        try {
            radioGroup.setOrientation(LinearLayout.VERTICAL);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        LeanBackUtil.log("VerticalClassLayout => onFocusChanged => gainFocus = " + gainFocus);
        if (gainFocus) {
            setCheckedRadioButton(true, false);
        } else {
            setCheckedRadioButton(false, false);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // up
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            int itemCount = getItemCount();
            int checkedIndex = getCheckedIndex();
            if (itemCount > 0 && checkedIndex > 0) {
                int next = checkedIndex - 1;
                setCheckedRadioButton(next, true, true);
                scrollNext(View.FOCUS_UP, next);
                return true;
            } else {
                View nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_UP);
                if (null == nextFocus) {
                    return true;
                } else {
                    clearFocus();
                }
            }
        }
        // down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            int itemCount = getItemCount();
            int checkedIndex = getCheckedIndex();
            if (itemCount > 0 && checkedIndex < itemCount) {
                int next = checkedIndex + 1;
                setCheckedRadioButton(next, true, true);
                scrollNext(View.FOCUS_DOWN, next);
                return true;
            } else {
                View nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_DOWN);
                if (null == nextFocus) {
                    return true;
                } else {
                    clearFocus();
                }
            }
        }
        // right
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            View nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_RIGHT);
            if (null == nextFocus) {
                return true;
            } else {
                clearFocus();
            }
        }
        // left
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            View nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_LEFT);
            if (null == nextFocus) {
                return true;
            } else {
                clearFocus();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void update(@NonNull List<? extends ClassBean> data) {
        ClassLayoutImpl.super.update(data, 0, false, mItemMargin, mItemHeight, mTextSize, true);
    }

    public void update(@NonNull List<? extends ClassBean> data, int checkedIndex) {
        ClassLayoutImpl.super.update(data, checkedIndex, false, mItemMargin, mItemHeight, mTextSize, true);
    }

    @Override
    public void update(@NonNull List<? extends ClassBean> data, @NonNull int chechedIndex, @NonNull boolean chechedIndexHasFocus, @NonNull int itemMargin, @NonNull int itemHeight, @NonNull int textSize, @NonNull boolean callListener) {

        if (null != data) {
            for (ClassBean o : data) {
                if (null == o)
                    continue;
                o.setTextColor(mTextColor);
                o.setTextColorFocus(mTextColorFocus);
                o.setTextColorChecked(mTextColorChecked);
                o.setBackgroundResource(mBackgroundResource);
                o.setBackgroundResourceChecked(mBackgroundResourceChecked);
                o.setBackgroundResourceFocus(mBackgroundResourceFocus);
            }
        }
        ClassLayoutImpl.super.update(data, chechedIndex, chechedIndexHasFocus, itemMargin, itemHeight, textSize, callListener);
    }
}
