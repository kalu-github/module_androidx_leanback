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

@SuppressLint("AppCompatCustomView")
public class RoundImageView extends ImageView {
    public boolean mFocus = false;
    public float mScale = 1.05f;
    public int mDuration = 100;
    private int mRadius = 0;

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(@NonNull Context context, @NonNull AttributeSet attrs) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
            mRadius = typedArray.getDimensionPixelOffset(R.styleable.RoundImageView_riv_corner, 0);
            mFocus = typedArray.getBoolean(R.styleable.RoundImageView_riv_focus, false);
            mScale = typedArray.getFloat(R.styleable.RoundImageView_riv_scale, 1.05f);
            mDuration = typedArray.getInteger(R.styleable.RoundImageView_riv_duration, 100);
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
        if (!mFocus)
            return;
        ViewCompat.animate(this).scaleX(gainFocus ? mScale : 1f).scaleY(gainFocus ? mScale : 1f).setDuration(mDuration).start();
    }

    @Override
    public void draw(Canvas canvas) {

        /**
         * .CLEAR	所绘制不会提交到画布上
         * PorterDuff.Mode.SRC	显示上层绘制图片
         * PorterDuff.Mode.DST	显示下层绘制图片
         * PorterDuff.Mode.SRC_OVER	正常绘制显示，上下层绘制叠盖
         * PorterDuff.Mode.DST_OVER	上下层都显示。下层居上显示
         * PorterDuff.Mode.SRC_IN	取两层绘制交集。显示上层
         * PorterDuff.Mode.DST_IN	取两层绘制交集。显示下层
         * PorterDuff.Mode.SRC_OUT	取上层绘制非交集部分
         * PorterDuff.Mode.DST_OUT	取下层绘制非交集部分
         * PorterDuff.Mode.SRC_ATOP	取下层非交集部分与上层交集部分
         * PorterDuff.Mode.DST_ATOP	取上层非交集部分与下层交集部分
         * PorterDuff.Mode.XOR	异或：去除两图层交集部分
         * PorterDuff.Mode.DARKEN	取两图层全部区域，交集部分颜色加深
         * PorterDuff.Mode.LIGHTEN	取两图层全部，点亮交集部分颜色
         * PorterDuff.Mode.MULTIPLY	取两图层交集部分叠加后颜色
         * PorterDuff.Mode.SCREEN	取两图层全部区域，交集部分变为透明色
         */

        // 下层
        super.draw(canvas);

        // 上层
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int radius = 20;
        int strokeWidth = 10;
        int width = getWidth();
        int height = getHeight();
        int left = -strokeWidth;
        int top = -strokeWidth;
        int right = width + strokeWidth;
        int bottom = height + strokeWidth;
        Path path = new Path();
        path.addRoundRect(new RectF(left, top, right, bottom), new float[]{radius, radius, radius, radius, radius, radius, radius, radius}, Path.Direction.CW);
//        canvas.clipPath(path);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth * 2 + paddingLeft * 4);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawPath(path, paint);

//        int radius = 40;
//
//        // 裁剪
//        Path path = new Path();
////        path.addRect(new RectF(10, 10, getWidth() - 10, getHeight() - 10), Path.Direction.CW);
//        path.addRoundRect(new RectF(left, top, right, bottom), radius, radius, Path.Direction.CW);
//        canvas.clipPath(path);

//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setColor(Color.TRANSPARENT);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(40);
//        paint.setPathEffect(new CornerPathEffect(40));
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
//        canvas.drawPath(path, paint);

//        Rect rect = new Rect();
//        rect.left = 0;
//        rect.top = 0;
//        rect.bottom = getHeight();
//        rect.right = getWidth();
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
//        mPaint.setColor(Color.RED);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(40);
//        canvas.drawRoundRect(rect, mPaint);
////        canvas.clipPath(mClipPath);

    }

    //    @Override
//    protected void onDraw(Canvas canvas) {
//        try {
//            super.onDraw(canvas);
//        } catch (Exception e) {
//        }
//    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        Path path = new Path();
//        int w = this.getWidth();
//        int h = this.getHeight();
//        float v = getResources().getDimension(R.dimen.dp_4);
//        float[] radii = {v, v, v, v, 0.0f, 0.0f, 0.0f, 0.0f};
//        path.addRoundRect(new RectF(0, 0, w, h), radii, Path.Direction.CW);
//        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
//        canvas.clipPath(path);
//        canvas.save();
//        canvas.drawColor(Color.TRANSPARENT);
//        super.onDraw(canvas);
//    }


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