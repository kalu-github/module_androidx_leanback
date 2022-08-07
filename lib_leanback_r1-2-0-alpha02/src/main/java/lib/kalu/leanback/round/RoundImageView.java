package lib.kalu.leanback.round;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
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

    private int mRadius = 0;
    public boolean mFocus = false;
    public float mScale = 1.05f;
    public int mDuration = 100;

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
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
        }
    }

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
        try {
            if (mRadius > 0) {
                RoundedBitmapDrawable news = RoundedBitmapDrawableFactory.create(getResources(), ((BitmapDrawable) drawable).getBitmap());
                news.setCornerRadius(mRadius);
                super.setImageDrawable(news);
            } else {
                super.setImageDrawable(drawable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
