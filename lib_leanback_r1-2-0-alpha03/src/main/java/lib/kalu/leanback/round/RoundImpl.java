package lib.kalu.leanback.round;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.View;

import lib.kalu.leanback.util.LeanBackUtil;

public interface RoundImpl {

    /**
     * PorterDuff.Mode.CLEAR	所绘制不会提交到画布上
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
    default void clipPath(Canvas canvas, Paint paint, float[] radii, int strokeWidth) {
        try {
            int paddingLeft = ((View) this).getPaddingLeft();
            int paddingRight = ((View) this).getPaddingRight();
            int paddingTop = ((View) this).getPaddingTop();
            int paddingBottom = ((View) this).getPaddingBottom();
            int paddingMin = Math.min(paddingLeft, paddingRight);
            paddingMin = Math.min(paddingMin, paddingTop);
            paddingMin = Math.min(paddingMin, paddingBottom);

            int width = ((View) this).getWidth();
            int height = ((View) this).getHeight();
            int left = -strokeWidth / 2;
            int top = -strokeWidth / 2;
            int right = width + strokeWidth / 2;
            int bottom = height + strokeWidth / 2;
            Path path = new Path();
            path.addRoundRect(new RectF(left, top, right, bottom), radii, Path.Direction.CW);
//        canvas.clipPath(path);

            paint.reset();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(strokeWidth + paddingMin * 2);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawPath(path, paint);
        } catch (Exception e) {
            LeanBackUtil.log("RoundImpl -> clipPath -> Exception -> " + e.getMessage());
        }
    }

    default void drawFocus(Canvas canvas, Paint paint, float[] radii, int strokeWidth) {
        try {
            boolean hasFocus = ((View) this).hasFocus();
            if (!hasFocus)
                throw new Exception("warning: hasFocus false");

            int width = ((View) this).getWidth();
            int height = ((View) this).getHeight();
            int left = strokeWidth / 2;
            int top = strokeWidth / 2;
            int right = width - strokeWidth / 2;
            int bottom = height - strokeWidth / 2;
            Path path = new Path();
            path.addRoundRect(new RectF(left, top, right, bottom), radii, Path.Direction.CW);
//        canvas.clipPath(path);

            paint.reset();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(strokeWidth);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setXfermode(null);
            canvas.drawPath(path, paint);
        } catch (Exception e) {
            LeanBackUtil.log("RoundImpl -> drawFocus -> Exception -> " + e.getMessage());
        }
    }
}
