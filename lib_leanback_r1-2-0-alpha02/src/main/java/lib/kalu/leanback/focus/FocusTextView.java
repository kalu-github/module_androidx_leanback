package lib.kalu.leanback.focus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
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
    int mBgResNormal = -1;
    @DrawableRes
    int mBgResFocus = -1;
    @ColorInt
    int mBgColorNormal = -1;
    @ColorInt
    int mBgColorFocus = -1;
    @ColorInt
    int mTextColorNormal = -110;
    @ColorInt
    int mTextColorFocus = -110;

    private float mScale = 1.05f;
    private int mDuration = 100;

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
            mTextColorNormal = typedArray.getColor(R.styleable.FocusLayout_fl_text_color_normal, -1);
            mTextColorFocus = typedArray.getColor(R.styleable.FocusLayout_fl_text_color_focus, -1);
            mBgColorNormal = typedArray.getColor(R.styleable.FocusLayout_fl_bg_color_normal, -1);
            mBgColorFocus = typedArray.getColor(R.styleable.FocusLayout_fl_bg_color_focus, -1);
            mBgResNormal = typedArray.getResourceId(R.styleable.FocusLayout_fl_bg_resource_normal, -1);
            mBgResFocus = typedArray.getResourceId(R.styleable.FocusLayout_fl_bg_resource_focus, -1);
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
        refreshAnim(gainFocus);
    }

    protected final void refreshBackground(boolean gainFocus) {

        if (gainFocus) {
            if (mBgColorFocus != -1) {
                setBackgroundColor(mBgColorFocus);
            } else if (mBgResFocus != -1) {
                setBackgroundResource(mBgResFocus);
            }
        } else {
            if (mBgColorNormal != -1) {
                setBackgroundColor(mBgColorNormal);
            } else if (mBgResNormal != -1) {
                setBackgroundResource(mBgResNormal);
            }
        }
    }

    protected final void refreshTextColor(boolean gainFocus) {
        @ColorInt
        int color = gainFocus ? mTextColorFocus : mTextColorNormal;
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

    protected void refreshAnim(boolean gainFocus) {
        ViewCompat.animate(this).scaleX(gainFocus ? mScale : 1f).scaleY(gainFocus ? mScale : 1f).setDuration(mDuration).start();
    }
}
