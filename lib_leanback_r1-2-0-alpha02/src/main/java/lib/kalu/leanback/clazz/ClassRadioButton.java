package lib.kalu.leanback.clazz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RadioButton;

import androidx.annotation.RequiresApi;

@SuppressLint("AppCompatCustomView")
class ClassRadioButton extends RadioButton {

    public ClassRadioButton(Context context) {
        super(context);
        init();
    }

    public ClassRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClassRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ClassRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setSingleLine();
        setFocusable(false);
//        setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        setGravity(Gravity.CENTER);
        setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
