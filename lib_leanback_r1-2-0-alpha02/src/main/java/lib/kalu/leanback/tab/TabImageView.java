package lib.kalu.leanback.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
class TabImageView extends ImageView {

    private int mHeight = 0;
    private int mWidthMax = 0;
    private int mWidthMin = 0;

    TabImageView(@NonNull Context context) {
        super(context);
        init();
    }

    private final void init() {
        setFocusable(true);
        setScaleType(ScaleType.FIT_CENTER);
        setPadding(0, 0, 0, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = MeasureSpec.getSize(heightMeasureSpec);

        try {
            Drawable drawable = getDrawable();
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            TabUtil.logE("IMGonMeasure => intrinsicWidth = " + intrinsicWidth + ", intrinsicHeight =" + intrinsicHeight);
            width = height * intrinsicWidth / intrinsicHeight;
        } catch (Exception e) {
        }

        if (mWidthMin > 0 && width < mWidthMin) {
            width = mWidthMin;
        } else if (mWidthMax > 0 && width > mWidthMax) {
            width = mWidthMax;
        }

        TabUtil.logE("IMGonMeasure => width = " + width + ", height =" + height);
        setMeasuredDimension(width, height);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

//        int width = getWidth();
//        int height = getHeight();
//
//        // 原始区域
//        Paint paint1 = new Paint();
//        paint1.setColor(Color.RED);
//        Rect rect1 = new Rect(0, 0, width, height);
//        canvas.drawRect(rect1, paint1);
//
//        // 目标区域
//        Paint paint2 = new Paint();
//        paint1.setColor(Color.BLACK);
//        Rect rect2 = new Rect(width / 10, height / 10, width - width / 10, height - height / 10);
//        canvas.drawRect(rect2, paint2);

        super.onDraw(canvas);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {

        try {
//
//            // 1
//            float rectW = getWidth();
//            float rectH = getHeight();
//
//            // 2
//            float imgW = drawable.getIntrinsicWidth();
//            float imgH = drawable.getIntrinsicHeight();
//            if (imgW > rectW) {
//                imgW = rectW;
//                rectH = (rectH / rectW) * imgW;
//            } else if (imgH > rectH) {
//                imgH = rectH;
//                imgW = (rectW / rectH) * imgH;
//            }
//
//            // 3
//            float bitmapH = rectH / 10 * 8;
//            float bitmapW = (imgW / imgH) * bitmapH;
////            if (imgW > rectW) {
////                bitmapW = rectW / 10 * 8;
////                bitmapH = (imgH / imgW) * bitmapW;
////            } else if (imgH > rectH) {
////                bitmapH = rectH / 10 * 8;
////                bitmapW = (imgW / imgH) * bitmapH;
////            } else {
////                bitmapH = rectH / 10 * 8;
////                bitmapW = (imgW / imgH) * bitmapH;
////            }
//
//            // 4
//            Bitmap bitmap = Bitmap.createBitmap((int) rectW, (int) rectH, Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(bitmap);
////            TabUtil.logE("IMGsetImageDrawable => paddingLeft = " + paddingLeft + ", paddingRight =" + paddingRight);
////            TabUtil.logE("IMGsetImageDrawable => imgW = " + imgW + ", imgH =" + imgH);
////            TabUtil.logE("IMGsetImageDrawable => tabWidth = " + tabWidth + ", tabHeight =" + tabHeight);
////            TabUtil.logE("IMGsetImageDrawable => canvasWidth = " + canvasWidth + ", canvasHeight =" + canvasHeight);
//            Rect src = new Rect((int) ((rectW - imgW) / 2), (int) ((rectH - imgH) / 2), (int) imgW, (int) imgH);
//            RectF dst = new RectF((rectW - bitmapW) / 2, (rectH - bitmapH) / 2, bitmapW, bitmapH);
//            Bitmap temp = ((BitmapDrawable) drawable).getBitmap();
//            canvas.drawBitmap(temp, src, dst, null);
//            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
//            if (null != temp) {
//                temp.recycle();
//            }
//            super.setImageDrawable(bitmapDrawable);
            super.setImageDrawable(drawable);
        } catch (Exception e) {
        }
    }

    protected final void setHeight(int height) {
        this.mHeight = height;
    }

    protected final void setWidthMin(int min) {
        this.mWidthMin = min;
    }

    protected final void setWidthMax(int max) {
        this.mWidthMin = max;
    }

    /*************************/

    protected void refresh(boolean focus, boolean stay) {
    }
}