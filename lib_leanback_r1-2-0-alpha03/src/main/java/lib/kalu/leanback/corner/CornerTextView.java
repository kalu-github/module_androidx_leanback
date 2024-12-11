package lib.kalu.leanback.corner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.leanback.R;

import lib.kalu.leanback.util.LeanBackUtil;

@SuppressLint("AppCompatCustomView")
public class CornerTextView extends TextView implements CornerImpl {

    private Path mPath;
    private Paint mPaint;

    private float mRateWidth;
    private float mRateHeight;

    private int mCorner;
    private int mCornerTopLeft;
    private int mCornerTopRight;
    private int mCornerBottomLeft;
    private int mCornerBottomRight;

    public CornerTextView(Context context) {
        super(context);
        init(context, null);
    }

    public CornerTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CornerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CornerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int specW = measureSpecWidth(widthMeasureSpec, heightMeasureSpec, mRateWidth);
            int specH = measureSpecHeight(widthMeasureSpec, heightMeasureSpec, mRateHeight);
            super.onMeasure(specW, specH);
        } catch (Exception e) {
            LeanBackUtil.log("CornerTextView -> onMeasure -> Exception -> " + e.getMessage(), e);
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        LeanBackUtil.log("CornerTextView -> setBackgroundResource -> resid = " + resid);
        super.setBackgroundResource(resid);
    }

    @Override
    public void setBackgroundColor(int color) {
        LeanBackUtil.log("CornerTextView -> setBackgroundColor -> color = " + color);
        super.setBackgroundColor(color);
    }

    @Override
    public void setBackground(Drawable background) {
        LeanBackUtil.log("CornerTextView -> setBackground -> background = " + background);
//        super.setBackground(background);
        try {
            if (null == background)
                throw new Exception("warning: background null");
            if (null == mPaint) {
                mPaint = new Paint();
            }
            if (null == mPath) {
                mPath = new Path();
            }
            Drawable cornerDrawable = clipCornerDrawable(background, mPaint, mPath, mCorner, mCornerTopLeft, mCornerTopRight, mCornerBottomRight, mCornerBottomLeft);
            super.setBackground(cornerDrawable);
        } catch (Exception e) {
            LeanBackUtil.log("CornerTextView -> setBackground -> Exception -> " + e.getMessage(), e);
            super.setBackground(background);
        }
    }

    private void init(@NonNull Context context, @NonNull AttributeSet attrs) {
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.CornerView);
            mCorner = typedArray.getDimensionPixelOffset(R.styleable.CornerView_cv_corner, 0);
            mCornerTopLeft = typedArray.getDimensionPixelOffset(R.styleable.CornerView_cv_corner_top_left, 0);
            mCornerTopRight = typedArray.getDimensionPixelOffset(R.styleable.CornerView_cv_corner_top_right, 0);
            mCornerBottomLeft = typedArray.getDimensionPixelOffset(R.styleable.CornerView_cv_corner_bottom_left, 0);
            mCornerBottomRight = typedArray.getDimensionPixelOffset(R.styleable.CornerView_cv_corner_bottom_right, 0);
            mRateWidth = typedArray.getFloat(R.styleable.CornerView_cv_rate_width, 0f);
            mRateHeight = typedArray.getFloat(R.styleable.CornerView_cv_rate_height, 0f);
        } catch (Exception e) {
            LeanBackUtil.log("CornerTextView -> init -> Exception -> " + e.getMessage(), e);
        }

        if (null != typedArray) {
            typedArray.recycle();
        }
    }
}
