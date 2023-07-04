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

        int repeatCount = event.getRepeatCount();
        if (repeatCount > 0)
            return true;
//        LeanBackUtil.log("RadioGroupHorizontal => dispatchKeyEvent => action = " + event.getAction() + ", keyCode = " + event.getKeyCode());

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    boolean checkedLeft = checkedLeft();
                    if (checkedLeft) {
                        requestFocusChecked();
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    boolean checkedRight = checkedRight();
                    if (checkedRight) {
                        requestFocusChecked();
                        return true;
                    }
                    break;
//                case KeyEvent.KEYCODE_DPAD_UP:
//                case KeyEvent.KEYCODE_DPAD_DOWN:
//                    return false;
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
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("RadioGroupHorizontal => getCheckedIndex => " + e.getMessage());
            return 0;
        }
    }

    private void updateCheckedIndex(int index) {
        try {
            int count = getChildCount();
            if (count <= 0)
                throw new Exception("count error: " + count);
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (null == child)
                    continue;
                if (!(child instanceof RadioButton))
                    continue;
                ((RadioButton) child).setChecked(i == index);
            }
        } catch (Exception e) {
            LeanBackUtil.log("RadioGroupHorizontal => updateCheckedIndex => " + e.getMessage());
        }
    }

    private boolean checkedLeft() {
        try {
            int checkedIndex = getCheckedIndex();
            if (checkedIndex <= 0)
                throw new Exception("checkedIndex error: " + checkedIndex);
            updateCheckedIndex(checkedIndex - 1);
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("RadioGroupHorizontal => checkedLeft => " + e.getMessage());
            return false;
        }
    }

    private boolean checkedRight() {
        try {
            int checkedIndex = getCheckedIndex();
            if (checkedIndex < 0)
                throw new Exception("checkedIndex error: " + checkedIndex);
            int count = getChildCount();
            if (count <= 0)
                throw new Exception("count error: " + count);
            if (checkedIndex + 1 >= count)
                throw new Exception("checkedIndex error: " + checkedIndex + "or count error: " + count);
            updateCheckedIndex(checkedIndex + 1);
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("RadioGroupHorizontal => checkedRight => " + e.getMessage());
            return false;
        }
    }

    private void requestFocusChecked() {
        try {
            int checkedIndex = getCheckedIndex();
            if (checkedIndex < 0)
                throw new Exception("checkedIndex error: " + checkedIndex);
            int count = getChildCount();
            if (count <= 0)
                throw new Exception("count error: " + count);
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (null == child)
                    continue;
                if (i == checkedIndex) {
                    setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                    child.requestFocus();
                    setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    break;
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("RadioGroupHorizontal => requestFocusChecked => " + e.getMessage());
        }
    }
}
