package lib.kalu.leanback.bold;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class BoldTextView extends TextView {
    public BoldTextView(Context context) {
        super(context);
        init();
    }

    public BoldTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoldTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public BoldTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        getPaint().setStrokeJoin(Paint.Join.ROUND);
        getPaint().setStrokeCap(Paint.Cap.ROUND);
        getPaint().setFakeBoldText(true);
    }
}
