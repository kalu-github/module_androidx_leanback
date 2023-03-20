package lib.kalu.leanback.clazz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.RadioButton;

import androidx.annotation.RequiresApi;

@SuppressLint("AppCompatCustomView")
class ClassRadioButton extends RadioButton {
    public ClassRadioButton(Context context) {
        super(context);
    }

    public ClassRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClassRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ClassRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (text instanceof SpannableStringBuilder) {
            float measureText = getPaint().measureText(String.valueOf(text));
            int length = text.length();
            float v = measureText / length * 2;
            setPadding(0, 0, (int) v, 0);
        } else {
            setPadding(0, 0, 0, 0);
        }
    }
}
