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
    public boolean mFocus = false;
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
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (!mFocus)
            return;
        ViewCompat.animate(this).scaleX(gainFocus ? mScale : 1f).scaleY(gainFocus ? mScale : 1f).setDuration(mDuration).start();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
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

    @Override
    public ScaleType getScaleType() {
        return ScaleType.FIT_XY;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(ScaleType.FIT_XY);
    }

    @Override
    public void setImageResource(int resId) {
        try {
            Drawable drawable = getResources().getDrawable(resId);
            setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
//        try {
//            if (mRadius > 0) {
//                RoundedBitmapDrawable news = RoundedBitmapDrawableFactory.create(getResources(), ((BitmapDrawable) drawable).getBitmap());
//                news.setCornerRadius(mRadius);
//                super.setImageDrawable(news);
//            } else {
//                super.setImageDrawable(drawable);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        super.setImageDrawable(drawable);
    }
//
//    @Override
//    public void setImageBitmap(Bitmap bm) {
//        try {
//            RoundedBitmapDrawable drawableNews = RoundedBitmapDrawableFactory.create(getResources(), bm);
//            float scale = getResources().getDisplayMetrics().density;
//            float v = 8 * scale + 0.5f;
//            drawableNews.setCornerRadius(v);
//            super.setImageDrawable(drawableNews);
//        } catch (Exception e) {
////            super.setImageBitmap(bm);
//        }
//    }
}