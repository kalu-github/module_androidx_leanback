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

    private Paint mPaint;
    private int mStrokeWidth;
    private int mStrokeColor;

    private int mCorner;
    private int mCornerTopLeft;
    private int mCornerTopRight;
    private int mCornerBottomLeft;
    private int mCornerBottomRight;

    private float mFocusScale;
    private int mFocusDuration;
    private float mRateWidth;
    private float mRateHeight;

    public RoundRelativeLayout2(Context context) {
        super(context);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        init(context, null);
    }

    public RoundRelativeLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        init(context, attrs);
    }

    public RoundRelativeLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        init(context, attrs);
    }

    @SuppressLint("NewApi")
    public RoundRelativeLayout2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        init(context, attrs);
    }

    public void init(@NonNull Context context, @NonNull AttributeSet attrs) {
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundView2);
//            mStrokeColor = typedArray.getColor(R.styleable.RoundView2_rv_stroke_width, Color.WHITE);
            mStrokeColor = Color.WHITE;
            mStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_stroke_width, 0);
            mCorner = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner, 0);
            mCornerTopLeft = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner_top_left, 0);
            mCornerTopRight = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner_top_right, 0);
            mCornerBottomLeft = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner_bottom_left, 0);
            mCornerBottomRight = typedArray.getDimensionPixelOffset(R.styleable.RoundView2_rv_corner_bottom_right, 0);
            mFocusScale = typedArray.getFloat(R.styleable.RoundView2_rv_focus_scale, 1f);
            mFocusDuration = typedArray.getInteger(R.styleable.RoundView2_rv_focus_duration, 100);
            mRateWidth = typedArray.getFloat(R.styleable.RoundView2_rv_rate_width, 0f);
            mRateHeight = typedArray.getFloat(R.styleable.RoundView2_rv_rate_height, 0f);
        } catch (Exception e) {
            LeanBackUtil.log("RoundRelativeLayout2 -> init -> Exception -> " + e.getMessage(), e);
        }

        if (null != typedArray) {
            typedArray.recycle();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int specW = measureSpecWidth(widthMeasureSpec, heightMeasureSpec, mRateWidth);
            int specH = measureSpecHeight(widthMeasureSpec, heightMeasureSpec, mRateHeight);
            super.onMeasure(specW, specH);
        } catch (Exception e) {
            LeanBackUtil.log("RoundRelativeLayout2 -> onMeasure -> Exception -> " + e.getMessage(), e);
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        focusAnimate(gainFocus, mFocusScale, mFocusDuration);
    }
}