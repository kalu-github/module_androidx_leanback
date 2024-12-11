package lib.kalu.leanback.corner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public interface CornerImpl {

    default int measureSpecWidth(int widthMeasureSpec, int heightMeasureSpec, float rateWidth) {
        try {
//            if (widthMeasureSpec != View.MeasureSpec.UNSPECIFIED)
//                throw new Exception("warning: widthMeasureSpec not View.MeasureSpec.UNSPECIFIED");
            if (rateWidth <= 0)
                throw new Exception("warning: rateWidth<=0");
            int height = View.MeasureSpec.getSize(heightMeasureSpec);
            int width = (int) (height * rateWidth);
            return View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        } catch (Exception e) {
//            LeanBackUtil.log("CornerImpl -> measureSpecWidth -> Exception -> this = " + this + ", msg = " + e.getMessage());
            return widthMeasureSpec;
        }
    }

    default int measureSpecHeight(int widthMeasureSpec, int heightMeasureSpec, float rateHeight) {
        try {
//            if (heightMeasureSpec != View.MeasureSpec.UNSPECIFIED)
//                throw new Exception("warning: heightMeasureSpec not View.MeasureSpec.UNSPECIFIED");
            if (rateHeight <= 0)
                throw new Exception("warning: rateHeight<=0");
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * rateHeight);
            return View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        } catch (Exception e) {
//            LeanBackUtil.log("CornerImpl -> measureSpecHeight -> Exception -> this = " + this + ", msg = " + e.getMessage());
            return heightMeasureSpec;
        }
    }

    default Drawable clipCornerDrawable(Drawable drawable,
                                        Paint paint,
                                        Path path,
                                        int corner, int cornerTopLeft, int cornerTopRight, int cornerBottomRight, int cornerBottomLeft) {
        try {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            Canvas canvas = new Canvas(bitmap);


            RectF rectF = new RectF(0, 0, width, height);
            float radii1 = cornerTopLeft > 0 ? cornerTopLeft : corner;
            float radii2 = cornerTopRight > 0 ? cornerTopRight : corner;
            float radii3 = cornerBottomRight > 0 ? cornerBottomRight : corner;
            float radii4 = cornerBottomLeft > 0 ? cornerBottomLeft : corner;
            float[] radii = {radii1, radii1, radii2, radii2, radii3, radii3, radii4, radii4};

            path.reset();
            path.addRoundRect(rectF, radii, Path.Direction.CCW);
            path.close();

//            //设置矩形大小
            Rect rect = new Rect(0, 0, width, height);
//            RectF rectf = new RectF(rect);
//
            // 相当于清屏
            canvas.drawARGB(0, 0, 0, 0);
//            //画圆角
//            canvas.drawRoundRect(rectf, 10, 10, paint);

            canvas.drawPath(path, paint);

            // 取两层绘制，显示上层
            paint.reset();
            paint.setAntiAlias(true);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            // 把原生的图片放到这个画布上，使之带有画布的效果
            canvas.drawBitmap(((BitmapDrawable) drawable).getBitmap(), rect, rect, paint);

            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);

            if (null != bitmap) {
                bitmap.recycle();
            }
            return bitmapDrawable;
        } catch (Exception e) {
            return null;
        }
    }
}
