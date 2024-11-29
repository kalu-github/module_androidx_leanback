package lib.kalu.leanback.round;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.leanback.R;

import lib.kalu.leanback.util.LeanBackUtil;

public class RoundRelativeLayout2 extends RelativeLayout implements RoundImpl {

    public float mScale = 1.05f;
    public int mDuration = 100;

    private Paint mPaint;
    private int mStrokeWidth;
    private int mStrokeColor;

    private int mCorner;
    private int mCornerTopLeft;
    private int mCornerTopRight;
    private int mCornerBottomLeft;
    private int mCornerBottomRight;

    public RoundRelativeLayout2(Context context) {
        super(context);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        init(context, null);
    }

    public RoundRelativeLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        init(context, null);
    }

    public RoundRelativeLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        init(context, null);
    }

    @SuppressLint("NewApi")
    public RoundRelativeLayout2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        init(context, null);
    }

    public void init(@NonNull Context context, @NonNull AttributeSet attrs) {
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundView2);
//            mStrokeColor = typedArray.getColor(R.styleable.RoundView2_rv_stroke_width, Color.WHITE);
            mStrokeColor = Color.RED;
            mStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_stroke_width, 0);
            mCorner = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner, 0);
            mCornerTopLeft = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner_top_left, 0);
            mCornerTopRight = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner_top_right, 0);
            mCornerBottomLeft = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner_bottom_left, 0);
            mCornerBottomRight = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner_bottom_right, 0);
            mScale = typedArray.getFloat(R.styleable.RoundView2_rv_scale, 1.05f);
            mDuration = typedArray.getInteger(R.styleable.RoundView2_rv_duration, 100);
        } catch (Exception e) {
            LeanBackUtil.log("RoundImageView -> init -> Exception -> " + e.getMessage(), e);
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
        int corner1 = mCornerTopLeft > 0 ? mCornerTopLeft : mCorner;
        clipCornerTopLeft(canvas, mPaint, corner1);
        int corner2 = mCornerTopRight > 0 ? mCornerTopRight : mCorner;
        clipCornerTopRight(canvas, mPaint, corner2);
        int corner3 = mCornerBottomLeft > 0 ? mCornerBottomLeft : mCorner;
        clipCornerBottomLeft(canvas, mPaint, corner3);
        int corner4 = mCornerBottomRight > 0 ? mCornerBottomRight : mCorner;
        clipCornerBottomRight(canvas, mPaint, corner4);
        drawBorder(canvas, mPaint, mStrokeWidth, mStrokeColor, corner1, corner2, corner3, corner4);
    }

//    @Override
//    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
//        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
//        ViewCompat.animate(this).scaleX(gainFocus ? mScale : 1f).scaleY(gainFocus ? mScale : 1f).setDuration(mDuration).start();
//    }
}
