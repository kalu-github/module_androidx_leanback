package lib.kalu.leanback.corner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.leanback.R;

import lib.kalu.leanback.util.LeanBackUtil;

@SuppressLint("AppCompatCustomView")
public class CornerImageView extends ImageView implements CornerImpl {

    private Path mPath;
    private Paint mPaint;

    private float mRateWidth;
    private float mRateHeight;

    private int mCorner;
    private int mCornerTopLeft;
    private int mCornerTopRight;
    private int mCornerBottomLeft;
    private int mCornerBottomRight;

    public CornerImageView(Context context) {
        super(context);
        init(context, null);
    }

    public CornerImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CornerImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CornerImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
            LeanBackUtil.log("RoundImageView -> onMeasure -> Exception -> " + e.getMessage(), e);
        }
    }

    @Override
    public void setImageMatrix(Matrix matrix) {
        LeanBackUtil.log("CornerImageView -> setImageMatrix -> matrix = " + matrix);
        super.setImageMatrix(matrix);
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        LeanBackUtil.log("CornerImageView -> setImageURI -> uri = " + uri);
        super.setImageURI(uri);
    }

    @Override
    public void setImageResource(int resId) {
        LeanBackUtil.log("CornerImageView -> setImageResource -> resId = " + resId);
        try {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), resId);
            if (null == bm)
                throw new Exception("warning: bm null");
            setImageBitmap(bm);
        } catch (Exception e) {
            LeanBackUtil.log("CornerImageView -> setImageResource -> Exception -> " + e.getMessage());
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        LeanBackUtil.log("CornerImageView -> setImageBitmap -> bm = " + bm);
        try {
            if (null == bm)
                throw new Exception("warning: bm null");
            if (null == mPaint) {
                mPaint = new Paint();
            }
            if (null == mPath) {
                mPath = new Path();
            }
            Bitmap bitmap = clipCornerBitmap(bm, mPaint, mPath, mCorner, mCornerTopLeft, mCornerTopRight, mCornerBottomRight, mCornerBottomLeft);
            super.setImageBitmap(bitmap);
        } catch (Exception e) {
            LeanBackUtil.log("CornerImageView -> setImageBitmap -> Exception -> " + e.getMessage());
            super.setImageBitmap(bm);
        }
    }

    @Override
    public void setImageDrawable(@Nullable Drawable data) {
        LeanBackUtil.log("CornerImageView -> setImageDrawable -> drawable = " + data);
        super.setImageDrawable(data);

        try {
            Drawable drawable = getDrawable();
            if (null == drawable)
                throw new Exception("warning: drawable null");
            if (drawable instanceof LayerDrawable)
                throw new Exception("warning: drawable instanceof LayerDrawable");
            if (null == mPaint) {
                mPaint = new Paint();
            }
            if (null == mPath) {
                mPath = new Path();
            }
            Drawable cornerDrawable = clipCornerDrawable(drawable, mPaint, mPath, mCorner, mCornerTopLeft, mCornerTopRight, mCornerBottomRight, mCornerBottomLeft);
            super.setImageDrawable(cornerDrawable);
        } catch (Exception e) {
            LeanBackUtil.log("CornerImageView -> setImageDrawable -> Exception -> " + e.getMessage());
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        LeanBackUtil.log("CornerImageView -> setBackgroundResource -> resid = " + resid);
        super.setBackgroundResource(resid);
    }

    @Override
    public void setBackground(Drawable background) {
        LeanBackUtil.log("CornerImageView -> setBackground -> background = " + background);
        try {
            if (null == background)
                throw new Exception("warning: background null");
            if (background instanceof LayerDrawable)
                throw new Exception("warning: background instanceof LayerDrawable");
            if (null == mPaint) {
                mPaint = new Paint();
            }
            if (null == mPath) {
                mPath = new Path();
            }
            Drawable cornerDrawable = clipCornerDrawable(background, mPaint, mPath, mCorner, mCornerTopLeft, mCornerTopRight, mCornerBottomRight, mCornerBottomLeft);
            super.setBackground(cornerDrawable);
        } catch (Exception e) {
            LeanBackUtil.log("CornerImageView -> setBackground -> Exception -> " + e.getMessage());
            super.setBackground(background);
        }
    }

    private void init(@NonNull Context context, @NonNull AttributeSet attrs) {

        LeanBackUtil.log("CornerImageView -> init -> context = " + context + ", attrs = " + attrs);

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
            LeanBackUtil.log("CornerImageView -> init -> Exception -> " + e.getMessage(), e);
        }

        LeanBackUtil.log("CornerImageView -> init -> mCorner = " + mCorner + ", mCornerTopLeft = " + mCornerTopLeft + ", mCornerTopRight = " + mCornerTopRight + ", mCornerBottomLeft = " + mCornerBottomLeft + ", mCornerBottomLeft = " + mCornerBottomLeft);

        if (null != typedArray) {
            typedArray.recycle();
        }
    }
}
