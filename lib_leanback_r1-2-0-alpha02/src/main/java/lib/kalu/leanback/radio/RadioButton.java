package lib.kalu.leanback.radio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;

@SuppressLint("AppCompatCustomView")
public final class RadioButton extends android.widget.RadioButton {
    public RadioButton(Context context) {
        super(context);
        init();
    }

    public RadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Bitmap bitmap = null;
        setButtonDrawable(new BitmapDrawable(bitmap));
    }
}
