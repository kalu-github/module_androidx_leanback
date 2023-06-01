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
                    if (scroll(View.FOCUS_LEFT)) {
                        return true;
                    } else {
                        break;
                    }
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (scroll(View.FOCUS_RIGHT)) {
                        return true;
                    } else {
                        break;
                    }
                case KeyEvent.KEYCODE_DPAD_UP:
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private boolean scroll(int direction) {
        // left
        if (direction == View.FOCUS_LEFT) {
            try {
                int nextCheckedIndex = -1;
                int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    if (null == child)
                        continue;
                    if (!(child instanceof RadioButton))
                        continue;
                    if (((RadioButton) child).isChecked()) {
                        if (i > 0) {
                            nextCheckedIndex = i - 1;
                            ((RadioButton) child).setChecked(false);
                        }
                    }
                }
                if (nextCheckedIndex < 0)
                    throw new Exception("nextCheckedIndex error: " + nextCheckedIndex);
                View child = getChildAt(nextCheckedIndex);
                ((RadioButton) child).setChecked(true);
                setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                child.requestFocus();
                setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                return true;
            } catch (Exception e) {
            }
        }
        // right
        if (direction == View.FOCUS_RIGHT) {
            try {
                int nextCheckedIndex = -1;
                int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    if (null == child)
                        continue;
                    if (!(child instanceof RadioButton))
                        continue;
                    if (((RadioButton) child).isChecked()) {
                        if (i + 1 < count) {
                            nextCheckedIndex = i + 1;
                            ((RadioButton) child).setChecked(false);
                        }
                    }
                }
                if (nextCheckedIndex < 0)
                    throw new Exception("nextCheckedIndex error: " + nextCheckedIndex);
                View child = getChildAt(nextCheckedIndex);
                ((RadioButton) child).setChecked(true);
                setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                child.requestFocus();
                setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }
}
