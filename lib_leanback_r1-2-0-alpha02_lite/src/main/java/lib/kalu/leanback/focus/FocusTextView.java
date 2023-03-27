package lib.kalu.leanback.focus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.leanback.R;

import lib.kalu.leanback.util.LeanBackUtil;

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
        init(context, null);
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
        refreshTextColor(false);
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

        LeanBackUtil.log("FocusTextView", "init => mBgColorNormal = " + mBgColorNormal);
        LeanBackUtil.log("FocusTextView", "init => mBgColorFocus = " + mBgColorFocus);

        if (null != typedArray) {
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (mCorner > 0) {
//            if (null == mPath) {
//                mPath = new Path();
//            }
//
//            RectF areas = new RectF();
//            areas.left = 0;
//            areas.top = 0;
//            areas.right = getWidth();
//            areas.bottom = getHeight();
//
//            float[] floats = new float[8];
//            floats[0] = mCorner;
//            floats[1] = mCorner;
//            floats[2] = mCorner;
//            floats[3] = mCorner;
//            floats[4] = mCorner;
//            floats[5] = mCorner;
//            floats[6] = mCorner;
//            floats[7] = mCorner;
//
//            mPath.reset();
//            mPath.addRoundRect(areas, floats, Path.Direction.CW);
//
//
//            canvas.save();
//            canvas.clipPath(mPath);
//        }
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
        LeanBackUtil.log("FocusTextView", "refreshBackground => gainFocus = " + gainFocus);
        LeanBackUtil.log("FocusTextView", "refreshBackground => mBgColorFocus = " + mBgColorFocus);
        LeanBackUtil.log("FocusTextView", "refreshBackground => mBgColorNormal = " + mBgColorNormal);
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
