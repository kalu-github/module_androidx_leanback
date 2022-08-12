package lib.kalu.leanback.focus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.leanback.R;

@SuppressLint("AppCompatCustomView")
public class FocusTextView extends TextView {

    @DrawableRes
    int mBgNormal = -1;
    @DrawableRes
    int mBgFocus = -1;
    @ColorInt
    int mColorNormal = -110;
    @ColorInt
    int mColorFocus = -110;

    private float mScale = 1.05f;
    private int mDuration = 100;
    private TextUtils.TruncateAt mEllipsize = null;

    public FocusTextView(Context context) {
        super(context);
    }

    public FocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        refreshBackground(false);
    }

    public void init(@NonNull Context context, @NonNull AttributeSet attrs) {

        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.FocusLayout);
            mColorNormal = typedArray.getColor(R.styleable.FocusLayout_fl_color_normal, -110);
            mColorFocus = typedArray.getColor(R.styleable.FocusLayout_fl_color_focus, -110);
            mBgNormal = typedArray.getResourceId(R.styleable.FocusLayout_fl_bg_normal, -1);
            mBgFocus = typedArray.getResourceId(R.styleable.FocusLayout_fl_bg_focus, -1);
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
        // text
        refreshTextColor(gainFocus);
        // bg
        refreshBackground(gainFocus);
        // anim
        ViewCompat.animate(this).scaleX(gainFocus ? mScale : 1f).scaleY(gainFocus ? mScale : 1f).setDuration(mDuration).start();
    }

    private final void refreshBackground(boolean gainFocus) {
        @DrawableRes
        int res = gainFocus ? mBgFocus : mBgNormal;
        if (res == -1)
            return;
        try {
            setBackgroundResource(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final void refreshTextColor(boolean gainFocus) {
        if (null == mEllipsize) {
            mEllipsize = getEllipsize();
        }
        setEllipsize(gainFocus ? TextUtils.TruncateAt.MARQUEE : mEllipsize);
        @ColorInt
        int color = gainFocus ? mColorFocus : mColorNormal;
        if (color == -110) {
            return;
        }
        try {
            setTextColor(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isFocused() {
//        TextUtils.TruncateAt ellipsize = getEllipsize();
//        if (null != ellipsize && "marquee".equalsIgnoreCase(ellipsize.name())) {
//            return true;
//        } else {
        return true;
//        }
    }

    public void onFocusCall(boolean gainFocus) {
        // text
        refreshTextColor(gainFocus);
        // bg
        refreshBackground(gainFocus);
        // anim
        ViewCompat.animate(this).scaleX(gainFocus ? mScale : 1f).scaleY(gainFocus ? mScale : 1f).setDuration(mDuration).start();
    }
}
