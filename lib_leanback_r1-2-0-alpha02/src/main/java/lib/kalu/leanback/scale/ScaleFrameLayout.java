package lib.kalu.leanback.scale;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.leanback.R;

public class ScaleFrameLayout extends FrameLayout {

    public float mScale = 1.05f;
    public int mDuration = 100;

    public ScaleFrameLayout(Context context) {
        super(context);
    }

    public ScaleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ScaleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScaleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    public void init(@NonNull Context context, @NonNull AttributeSet attrs) {

        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.FocusLayout);
            mScale = typedArray.getFloat(R.styleable.FocusLayout_fl_scale, 1.05f);
            mDuration = typedArray.getInteger(R.styleable.FocusLayout_fl_duration, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != typedArray) {
            typedArray.recycle();
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        ViewCompat.animate(this).scaleX(gainFocus ? mScale : 1f).scaleY(gainFocus ? mScale : 1f).setDuration(mDuration).start();
    }
}
