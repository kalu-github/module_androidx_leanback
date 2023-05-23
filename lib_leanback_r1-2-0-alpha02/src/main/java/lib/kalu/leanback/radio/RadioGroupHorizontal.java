package lib.kalu.leanback.radio;


import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

public final class RadioGroupHorizontal extends android.widget.RadioGroup {
    public RadioGroupHorizontal(Context context) {
        super(context);
        setEnable(true);
        setFocusable(true);
        setOrientation(RadioGroupHorizontal.HORIZONTAL);
    }

    public RadioGroupHorizontal(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEnable(true);
        setFocusable(true);
        setOrientation(RadioGroupHorizontal.HORIZONTAL);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                case KeyEvent.KEYCODE_DPAD_UP:
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    int count = getChildCount();
                    for (int i = 0; i < count; i++) {
                        View child = getChildAt(i);
                        if (null == child)
                            continue;
                        if (!(child instanceof RadioButton))
                            continue;
                        if (((RadioButton) child).isChecked()) {
                            setEnable(false);
                            child.requestFocus();
                            break;
                        }
                    }
                    return true;
            }
        } else if (event.getAction() == KeyEvent.ACTION_DOWN) {
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
                    setEnable(true);
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
                setEnable(false);
                child.requestFocus();
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
                setEnable(false);
                child.requestFocus();
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

    private void setEnable(boolean status) {
        setDescendantFocusability(status ? ViewGroup.FOCUS_BLOCK_DESCENDANTS : ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }
}
