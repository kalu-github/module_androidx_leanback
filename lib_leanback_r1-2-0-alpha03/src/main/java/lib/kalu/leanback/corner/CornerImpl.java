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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import lib.kalu.leanback.util.LeanBackUtil;

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

        LeanBackUtil.log("CornerImpl -> clipCornerDrawable -> corner = " + corner + ", cornerTopLeft = " + cornerTopLeft + ", cornerTopRight = " + cornerTopRight + ", cornerBottomRight = " + cornerBottomRight + ", cornerBottomLeft = " + cornerBottomLeft);

        try {

            if (null == drawable)
                throw new Exception("error: drawable null");

            Bitmap bm = null;
            if (drawable instanceof BitmapDrawable) {
                bm = ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable instanceof ColorDrawable) {
                int width = ((View) this).getWidth();
                LeanBackUtil.log("CornerImpl -> clipCornerDrawable -> width = " + width);
                if (width <= 0)
                    throw new Exception("error: width <= 0");
                int height = ((View) this).getHeight();
                LeanBackUtil.log("CornerImpl -> clipCornerDrawable -> height = " + height);
                if (height <= 0)
                    throw new Exception("error: height <= 0");
                bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bm);
                drawable.setBounds(0, 0, width, height);
                drawable.draw(canvas);
            }

            if (null == bm)
                throw new Exception("error: bm null");

            Bitmap bitmap = clipCornerBitmap(bm, paint, path, corner, cornerTopLeft, cornerTopRight, cornerBottomRight, cornerBottomLeft);
            if (null == bitmap)
                throw new Exception("error: bitmap null");
            return new BitmapDrawable(bitmap);
        } catch (Exception e) {
            LeanBackUtil.log("CornerImpl -> clipCornerDrawable -> Exception -> this = " + this + ", msg = " + e.getMessage());
            return null;
        }
    }

    default Bitmap clipCornerBitmap(Bitmap bm,
                                    Paint paint,
                                    Path path,
                                    int corner, int cornerTopLeft, int cornerTopRight, int cornerBottomRight, int cornerBottomLeft) {

        LeanBackUtil.log("CornerImpl -> clipCornerBitmap -> corner = " + corner + ", cornerTopLeft = " + cornerTopLeft + ", cornerTopRight = " + cornerTopRight + ", cornerBottomRight = " + cornerBottomRight + ", cornerBottomLeft = " + cornerBottomLeft);

        try {

            if (null == bm)
                throw new Exception("error: bm null");

            int width = bm.getWidth();
            if (width <= 0)
                throw new Exception("error: width <= 0");

            int height = bm.getHeight();
            if (height <= 0)
                throw new Exception("error: height <= 0");

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);

//            Canvas canvas = new Canvas(bitmap);


//            RectF rectF = new RectF(0, 0, width, height);
//            float radii1 = cornerTopLeft > 0 ? cornerTopLeft : corner;
//            float radii2 = cornerTopRight > 0 ? cornerTopRight : corner;
//            float radii3 = cornerBottomRight > 0 ? cornerBottomRight : corner;
//            float radii4 = cornerBottomLeft > 0 ? cornerBottomLeft : corner;
//            float[] radii = {radii1, radii1, radii2, radii2, radii3, radii3, radii4, radii4};
//
////            RectF rectf = new RectF(rect);
////
//            // 相当于清屏
//          //  canvas.drawARGB(0, 0, 0, 0);
////            //画圆角
////            canvas.drawRoundRect(rectf, 10, 10, paint);
//
//            paint.reset();
//            paint.setAntiAlias(true);
//
//            path.reset();
//            path.addRoundRect(rectF, radii, Path.Direction.CCW);
//            path.close();
//            canvas.drawPath(path, paint);
//
//            //            //设置矩形大小
//            Rect rect = new Rect(0, 0, width, height);
//            // 取两层绘制，显示上层
//            // 把原生的图片放到这个画布上，使之带有画布的效果
//            paint.reset();
//            paint.setAntiAlias(true);
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//            canvas.drawBitmap(((BitmapDrawable) drawable).getBitmap(), rect, rect, paint);


            Canvas canvas = new Canvas(bitmap);

            paint.reset();
            paint.setAntiAlias(true);

            //设置矩形大小
            Rect rect = new Rect(0, 0, width, height);

            // 相当于清屏
            canvas.drawARGB(0, 0, 0, 0);
            //画圆角
//            RectF rectf = new RectF(rect);
//            canvas.drawRoundRect(rectf, 40, 40, paint);

            RectF rectF = new RectF(rect);
//            float radii1 = 100;
//            float radii2 = 200;
//            float radii3 = 300;
//            float radii4 = 400;
            float radii1 = cornerTopLeft > 0 ? cornerTopLeft : corner;
            float radii2 = cornerTopRight > 0 ? cornerTopRight : corner;
            float radii3 = cornerBottomRight > 0 ? cornerBottomRight : corner;
            float radii4 = cornerBottomLeft > 0 ? cornerBottomLeft : corner;
            float[] radii = {radii1, radii1, radii2, radii2, radii3, radii3, radii4, radii4};

            path.reset();
            path.addRoundRect(rectF, radii, Path.Direction.CCW);
            path.close();
            canvas.drawPath(path, paint);

            // 取两层绘制，显示上层
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            // 把原生的图片放到这个画布上，使之带有画布的效果
            canvas.drawBitmap(bm, rect, rect, paint);
            return bitmap;
        } catch (Exception e) {
            LeanBackUtil.log("CornerImpl -> clipCornerBitmap -> Exception -> this = " + this + ", msg = " + e.getMessage());
            return null;
        }
    }

    default Drawable clipCornerGradientDrawable(Drawable background,
                                                int corner, int cornerTopLeft, int cornerTopRight, int cornerBottomRight, int cornerBottomLeft) {

        LeanBackUtil.log("CornerImpl -> clipCornerGradientDrawable -> corner = " + corner + ", cornerTopLeft = " + cornerTopLeft + ", cornerTopRight = " + cornerTopRight + ", cornerBottomRight = " + cornerBottomRight + ", cornerBottomLeft = " + cornerBottomLeft);
        try {

            if (null == background)
                throw new Exception("error: background null");

            GradientDrawable drawable = new GradientDrawable();

            // 设置形状为矩形
            drawable.setShape(GradientDrawable.RECTANGLE);

            // 设置圆角半径，单位是像素，这里设为20像素
            float radii1 = cornerTopLeft > 0 ? cornerTopLeft : corner;
            float radii2 = cornerTopRight > 0 ? cornerTopRight : corner;
            float radii3 = cornerBottomRight > 0 ? cornerBottomRight : corner;
            float radii4 = cornerBottomLeft > 0 ? cornerBottomLeft : corner;
            float[] radii = {radii1, radii1, radii2, radii2, radii3, radii3, radii4, radii4};
            drawable.setCornerRadii(radii);

            // 设置边框宽度和颜色，宽度为2像素，颜色为黑色
//                drawable.setStroke(2, 0xff000000);

            // 设置填充颜色，这里设为白色
            drawable.setColor(((ColorDrawable) background).getColor());

            return drawable;
        } catch (Exception e) {
            LeanBackUtil.log("CornerImpl -> clipCornerGradientDrawable -> Exception -> this = " + this + ", msg = " + e.getMessage());
            return null;
        }
    }
}
