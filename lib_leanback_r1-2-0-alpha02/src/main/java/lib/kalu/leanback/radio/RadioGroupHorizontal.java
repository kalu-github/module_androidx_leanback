package lib.kalu.leanback.radio;


import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import lib.kalu.leanback.util.LeanBackUtil;

public final class RadioGroupHorizontal extends android.widget.RadioGroup {
    public RadioGroupHorizontal(Context context) {
        super(context);
        setFocusable(true);
        setOrientation(RadioGroupHorizontal.HORIZONTAL);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }

    public RadioGroupHorizontal(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setOrientation(RadioGroupHorizontal.HORIZONTAL);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        LeanBackUtil.log("RadioGroupHorizontal => onFocusChanged => ");
        try {
            if (!gainFocus)
                throw new Exception("gainFocus warning: false");
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (null == child)
                    continue;
                if (!(child instanceof RadioButton))
                    continue;
                if (((RadioButton) child).isChecked()) {
                    setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                    child.requestFocus();
                    setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    break;
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("RadioGroupHorizontal => onFocusChanged => " + e.getMessage());
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    checkedLeft();
                    requestFocusChecked();
                    return true;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    checkedRight();
                    requestFocusChecked();
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private int getCheckedIndex() {
        try {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (null == child)
                    continue;
                if (!(child instanceof RadioButton))
                    continue;
                if (((RadioButton) child).isChecked()) {
                    return i;
                }
            }
            throw new Exception();
        } catch (Exception e) {
            return -1;
        }
    }

    private void updateCheckedIndex(int index) {
        try {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (null == child)
                    continue;
                if (!(child instanceof RadioButton))
                    continue;
                ((RadioButton) child).setChecked(i == index);
            }
        } catch (Exception e) {
        }
    }

    private void checkedLeft() {
        try {
            int checkedIndex = getCheckedIndex();
            if (checkedIndex <= 0)
                throw new Exception();
            updateCheckedIndex(checkedIndex - 1);
        } catch (Exception e) {
        }
    }

    private void checkedRight() {
        try {
            int checkedIndex = getCheckedIndex();
            int count = getChildCount();
            if (checkedIndex + 1 > count)
                throw new Exception();
            updateCheckedIndex(checkedIndex + 1);
        } catch (Exception e) {
        }
    }

    private void requestFocusChecked() {
        try {
            int checkedIndex = getCheckedIndex();
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (null == child)
                    continue;
                if (i == checkedIndex) {
                    setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                    ((RadioButton) child).requestFocus();
                    setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    break;
                }
            }
        } catch (Exception e) {
        }
    }
}
