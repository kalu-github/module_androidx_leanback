package lib.kalu.leanback.round;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;

import androidx.core.view.ViewCompat;

import lib.kalu.leanback.util.LeanBackUtil;


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
public interface RoundImpl {

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
            LeanBackUtil.log("RoundImpl -> measureSpecWidth -> Exception -> " + e.getMessage());
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
            LeanBackUtil.log("RoundImpl -> measureSpecHeight -> Exception -> " + e.getMessage());
            return heightMeasureSpec;
        }
    }

    default void clipCornerTopLeft(Canvas canvas, Paint paint, int cornerWidth) {
        try {

            if (cornerWidth <= 0)
                throw new Exception("error: cornerWidth <= 0");

            int paddingLeft = ((View) this).getPaddingLeft();
            int paddingTop = ((View) this).getPaddingTop();

            int startX = 0;
            int startY = 0;

            int centerX = paddingLeft + cornerWidth;
            int centerY = 0;

            int endX = 0;
            int endY = paddingTop + cornerWidth;

            Path path = new Path();
            path.moveTo(startX, startY);
            path.lineTo(centerX, centerY);
            path.quadTo(startX, startY, endX, endY);
            path.lineTo(startX, startY);
            path.close();

            if (null == paint) {
                paint = new Paint();
            } else {
                paint.reset();
            }
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawPath(path, paint);
        } catch (Exception e) {
            LeanBackUtil.log("RoundImpl -> clipCornerTopLeft -> Exception -> " + e.getMessage());
        }
    }

    default void clipCornerTopRight(Canvas canvas, Paint paint, int cornerWidth) {
        try {

            if (cornerWidth <= 0)
                throw new Exception("error: cornerWidth <= 0");

            int paddingRight = ((View) this).getPaddingRight();
            int paddingTop = ((View) this).getPaddingTop();
            int width = ((View) this).getWidth();

            int startX = width;
            int startY = 0;

            int centerX = width;
            int centerY = paddingTop + cornerWidth;

            int endX = width - paddingRight - cornerWidth;
            int endY = 0;

            Path path = new Path();
            path.moveTo(startX, startY);
            path.lineTo(centerX, centerY);
            path.quadTo(startX, startY, endX, endY);
            path.lineTo(startX, startY);
            path.close();

            if (null == paint) {
                paint = new Paint();
            } else {
                paint.reset();
            }
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawPath(path, paint);
        } catch (Exception e) {
            LeanBackUtil.log("RoundImpl -> clipCornerTopRight -> Exception -> " + e.getMessage());
        }
    }

    default void clipCornerBottomLeft(Canvas canvas, Paint paint, int cornerWidth) {
        try {

            if (cornerWidth <= 0)
                throw new Exception("error: cornerWidth <= 0");

            int paddingLeft = ((View) this).getPaddingLeft();
            int paddingBottom = ((View) this).getPaddingBottom();
            int height = ((View) this).getHeight();

            int startX = 0;
            int startY = height;

            int centerX = 0;
            int centerY = height - paddingBottom - cornerWidth;

            int endX = paddingLeft + cornerWidth;
            int endY = height;

            Path path = new Path();
            path.moveTo(startX, startY);
            path.lineTo(centerX, centerY);
            path.quadTo(startX, startY, endX, endY);
            path.lineTo(startX, startY);
            path.close();

            if (null == paint) {
                paint = new Paint();
            } else {
                paint.reset();
            }
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawPath(path, paint);
        } catch (Exception e) {
            LeanBackUtil.log("RoundImpl -> clipCornerBottomLeft -> Exception -> " + e.getMessage());
        }
    }

    default void clipCornerBottomRight(Canvas canvas, Paint paint, int cornerWidth) {
        try {

            if (cornerWidth <= 0)
                throw new Exception("error: cornerWidth <= 0");

            int paddingRight = ((View) this).getPaddingRight();
            int paddingBottom = ((View) this).getPaddingBottom();
            int width = ((View) this).getWidth();
            int height = ((View) this).getHeight();

            int startX = width;
            int startY = height;

            int centerX = width - paddingRight - cornerWidth;
            int centerY = height;

            int endX = width;
            int endY = height - paddingBottom - cornerWidth;

            Path path = new Path();
            path.moveTo(startX, startY);
            path.lineTo(centerX, centerY);
            path.quadTo(startX, startY, endX, endY);
            path.lineTo(startX, startY);
            path.close();

            if (null == paint) {
                paint = new Paint();
            } else {
                paint.reset();
            }
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawPath(path, paint);
        } catch (Exception e) {
            LeanBackUtil.log("RoundImpl -> clipCornerBottomRight -> Exception -> " + e.getMessage());
        }
    }

    default void drawBorder(Canvas canvas, Paint paint, int strokeWidth, int strokeColor, int roundTopLeft, int roundTopRight, int roundBottomLeft, int roundBottomRight) {
        try {

            if (strokeWidth <= 0)
                throw new Exception("warning: strokeWidth <= 0");

            boolean focus = ((View) this).hasFocus();
            if (!focus)
                throw new Exception("warning: focus false");

            Path path = new Path();
            int width = ((View) this).getWidth();
            int height = ((View) this).getHeight();

            // move
            int move_x1 = strokeWidth / 2;
            int move_y1 = roundTopLeft;
            path.moveTo(move_x1, move_y1);

            // topLeft
            int top_left_x1 = 0;
            int top_left_y1 = 0;
            int top_left_x2 = roundTopLeft;
            int top_left_y2 = strokeWidth / 2;
            path.quadTo(top_left_x1, top_left_y1, top_left_x2, top_left_y2);

            // lineTo
            int line_x2 = width - roundTopRight;
            int line_y2 = strokeWidth / 2;
            path.lineTo(line_x2, line_y2);

            // topRight
            int top_right_x1 = width;
            int top_right_y1 = 0;
            int top_right_x2 = width - strokeWidth / 2;
            int top_right_y2 = roundTopRight;
            path.quadTo(top_right_x1, top_right_y1, top_right_x2, top_right_y2);

            // lineTo
            int line_x3 = width - strokeWidth / 2;
            int line_y3 = height - roundBottomRight;
            path.lineTo(line_x3, line_y3);

            // bottomRight
            int bottom_right_x1 = width;
            int bottom_right_y1 = height;
            int bottom_right_x2 = width - roundBottomRight;
            int bottom_right_y2 = height - strokeWidth / 2;
            path.quadTo(bottom_right_x1, bottom_right_y1, bottom_right_x2, bottom_right_y2);

            // lineTo
            int line_x4 = roundBottomLeft;
            int line_y4 = height - strokeWidth / 2;
            path.lineTo(line_x4, line_y4);

            // bottomLeft
            int bottom_left_x1 = 0;
            int bottom_left_y1 = height;
            int bottom_left_x2 = strokeWidth / 2;
            int bottom_left_y2 = height - roundBottomLeft;
            path.quadTo(bottom_left_x1, bottom_left_y1, bottom_left_x2, bottom_left_y2);

            // lineTo
            int line_x5 = strokeWidth / 2;
            int line_y5 = roundTopLeft;
            path.lineTo(line_x5, line_y5);

            // close
            path.close();

            if (null == paint) {
                paint = new Paint();
            } else {
                paint.reset();
            }
            paint.setAntiAlias(true);
            paint.setStrokeWidth(strokeWidth);
            paint.setColor(strokeColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setXfermode(null);
            canvas.drawPath(path, paint);
        } catch (Exception e) {
            LeanBackUtil.log("RoundImpl -> drawBorder -> Exception -> " + e.getMessage());
        }
    }

    default void focusAnimate(boolean gainFocus, float scale, int duration) {
        try {
            if (scale == 1f)
                throw new Exception("warning: scale == 1f");
            ViewCompat.animate((View) this).scaleX(gainFocus ? scale : 1f).scaleY(gainFocus ? scale : 1f).setDuration(duration).start();
        } catch (Exception e) {
            LeanBackUtil.log("RoundImpl -> focusAnimate -> Exception -> " + e.getMessage());
        }
    }
}
