package lib.kalu.leanback.round;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.leanback.R;

public class RoundRelativeLayout2 extends RelativeLayout implements RoundImpl {

    public float mScale = 1.05f;
    public int mDuration = 100;
    private Paint mPaint;
    private float[] mRadii;
    private int mCorner;
    private int mCornerTopLeft;
    private int mCornerTopRight;
    private int mCornerBottomLeft;
    private int mCornerBottomRight;

    public RoundRelativeLayout2(Context context) {
        super(context);
    }

    public RoundRelativeLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundRelativeLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("NewApi")
    public RoundRelativeLayout2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(@NonNull Context context, @NonNull AttributeSet attrs) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundView2);
            mCorner = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner, 0);
            mCornerTopLeft = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner_top_left, 0);
            mCornerTopRight = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner_top_right, 0);
            mCornerBottomLeft = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner_bottom_left, 0);
            mCornerBottomRight = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner_bottom_right, 0);
            mScale = typedArray.getFloat(R.styleable.RoundView2_rv_scale, 1.05f);
            mDuration = typedArray.getInteger(R.styleable.RoundView2_rv_duration, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != typedArray) {
            typedArray.recycle();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (null == mPaint) {
            mPaint = new Paint();
        }
        if (null == mRadii) {
            // radii dx,dy 成对出现，控制上右下左，四个位置圆角
            if (mCorner > 0) {
                mRadii = new float[]{mCorner, mCorner, mCorner, mCorner, mCorner, mCorner, mCorner, mCorner};
            } else {
                mRadii = new float[]{mCornerTopLeft, mCornerTopLeft, mCornerTopRight, mCornerTopRight, mCornerBottomRight, mCornerBottomRight, mCornerBottomLeft, mCornerBottomLeft};
            }
        }
      //  clipPath(canvas, mPaint, mRadii, mClipStrokeWidth);
//        drawFocus(canvas, mPaint, mRadii, mFocusStrokeWidth);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        ViewCompat.animate(this).scaleX(gainFocus ? mScale : 1f).scaleY(gainFocus ? mScale : 1f).setDuration(mDuration).start();
    }
}
