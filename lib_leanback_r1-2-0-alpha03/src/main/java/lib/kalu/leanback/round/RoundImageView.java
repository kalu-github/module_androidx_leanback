package lib.kalu.leanback.round;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.ViewCompat;
import androidx.leanback.R;

import lib.kalu.leanback.util.LeanBackUtil;

@SuppressLint("AppCompatCustomView")
public class RoundImageView extends ImageView implements RoundImpl {

    private Path mPath;
    private Paint mPaint;

    private int mStrokeWidth;
    private int mStrokeColor;

    private int mCorner;
    private int mCornerTopLeft;
    private int mCornerTopRight;
    private int mCornerBottomLeft;
    private int mCornerBottomRight;

    private boolean mClip;
    private float mScale;
    private float mRateWidth;
    private float mRateHeight;

    public RoundImageView(Context context) {
        super(context);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        init(context, null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        init(context, attrs);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        init(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画布裁剪
        if (mClip) {
            if (null == mPaint) {
                mPaint = new Paint();
            }
            if (null == mPath) {
                mPath = new Path();
            }
            clipRect(canvas, mPaint, mPath, mCorner, mCornerTopLeft, mCornerTopRight, mCornerBottomRight, mCornerBottomLeft);
        }
        // 获焦边框
        if (mStrokeWidth > 0f) {
            boolean hasFocus = hasFocus();
            if (hasFocus) {
                if (null == mPaint) {
                    mPaint = new Paint();
                }
                if (null == mPath) {
                    mPath = new Path();
                }
                drawBorder(canvas, mPaint, mPath, mStrokeWidth, mStrokeColor, mCorner, mCornerTopLeft, mCornerTopRight, mCornerBottomRight, mCornerBottomLeft);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int specW = measureSpecWidth(widthMeasureSpec, heightMeasureSpec, mRateWidth);
            int specH = measureSpecHeight(widthMeasureSpec, heightMeasureSpec, mRateHeight);
            super.onMeasure(specW, specH);
        } catch (Exception e) {
            LeanBackUtil.log("RoundImageView -> onMeasure -> Exception -> " + e.getMessage(), e);
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (mScale != 1f) {
            focusScale(gainFocus, mScale);
        }
    }

    private void init(@NonNull Context context, @NonNull AttributeSet attrs) {
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundView);
            mStrokeColor = typedArray.getColor(R.styleable.RoundView_rv_stroke_width, Color.WHITE);
            mStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.RoundView_rv_stroke_width, 0);
            mCorner = typedArray.getDimensionPixelOffset(R.styleable.RoundView_rv_corner, 0);
            mCornerTopLeft = typedArray.getDimensionPixelOffset(R.styleable.RoundView_rv_corner_top_left, 0);
            mCornerTopRight = typedArray.getDimensionPixelOffset(R.styleable.RoundView_rv_corner_top_right, 0);
            mCornerBottomLeft = typedArray.getDimensionPixelOffset(R.styleable.RoundView_rv_corner_bottom_left, 0);
            mCornerBottomRight = typedArray.getDimensionPixelOffset(R.styleable.RoundView_rv_corner_bottom_right, 0);
            mClip = typedArray.getBoolean(R.styleable.RoundView_rv_clip, false);
            mScale = typedArray.getFloat(R.styleable.RoundView_rv_scale, 1f);
            mRateWidth = typedArray.getFloat(R.styleable.RoundView_rv_rate_width, 0f);
            mRateHeight = typedArray.getFloat(R.styleable.RoundView_rv_rate_height, 0f);
        } catch (Exception e) {
            LeanBackUtil.log("RoundImageView -> init -> Exception -> " + e.getMessage(), e);
        }

        if (null != typedArray) {
            typedArray.recycle();
        }
    }
}