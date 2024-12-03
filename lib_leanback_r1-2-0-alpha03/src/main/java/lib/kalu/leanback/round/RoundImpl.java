package lib.kalu.leanback.round;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.View;

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
//            LeanBackUtil.log("RoundImpl -> measureSpecWidth -> Exception -> this = " + this + ", msg = " + e.getMessage());
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
//            LeanBackUtil.log("RoundImpl -> measureSpecHeight -> Exception -> this = " + this + ", msg = " + e.getMessage());
            return heightMeasureSpec;
        }
    }

    default void clipRect(Canvas canvas, Path path, int roundTopLeft, int roundTopRight,
                          int roundBottomLeft, int roundBottomRight) {
        try {

            int width = ((View) this).getWidth();
            int height = ((View) this).getHeight();

            RectF rectF = new RectF(0, 0, width, height);
            float[] radii = {roundTopLeft, roundTopLeft, roundTopRight, roundTopRight, roundBottomRight, roundBottomRight, roundBottomLeft, roundBottomLeft};

            path.reset();
            path.addRoundRect(rectF, radii, Path.Direction.CCW);
            path.close();

            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvas.clipPath(path);
        } catch (Exception e) {
//            LeanBackUtil.log("RoundImpl -> clipRect -> Exception -> this = " + this + ", msg = " + e.getMessage());
        }
    }

    default void clipRect(Canvas canvas, Paint paint, Path path, int corner, int cornerTopLeft, int cornerTopRight, int cornerBottomRight, int cornerBottomLeft) {
        try {

            if (null == paint)
                throw new Exception("error: paint null");

            int paddingLeft = ((View) this).getPaddingLeft();
            int paddingRight = ((View) this).getPaddingRight();
            int paddingTop = ((View) this).getPaddingTop();
            int paddingBottom = ((View) this).getPaddingBottom();
            int width = ((View) this).getWidth();
            int height = ((View) this).getHeight();

            // top left
            int corner1 = cornerTopLeft > 0 ? cornerTopLeft : corner;
            if (corner1 > 0) {

                path.reset();
                int rx = paddingLeft + corner1;
                int ry = paddingTop + corner1;
                path.moveTo(rx, ry);
                // 画圆弧 stratAngle是只开始的角度，X轴正方向为0度，sweepAngle是持续的角度;
                float left = 0f;
                float top = 0f;
                float right = rx * 2f;
                float bottom = ry * 2f;
                path.addArc(new RectF(left, top, right, bottom), -180, 90);
                path.lineTo(0f, 0f);
                path.lineTo(0f, ry);
                path.close();

                paint.reset();
                paint.setAntiAlias(true);
                paint.setStrokeWidth(0f);
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                // CLEAR
                // DST_OUT
                // SRC_OUT
                // XOR
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawPath(path, paint);
            }

            // top right
            int corner2 = cornerTopRight > 0 ? cornerTopRight : corner;
            if (corner2 > 0) {

                path.reset();
                int rx = width - corner2 - paddingRight;
                int ry = paddingTop + corner2;
                path.moveTo(rx, ry);
                // 画圆弧 stratAngle是只开始的角度，X轴正方向为0度，sweepAngle是持续的角度;
                float left = width - corner2 * 2f - paddingRight * 2f;
                float top = 0f;
                float right = width;
                float bottom = ry * 2f;
                path.addArc(new RectF(left, top, right, bottom), -90, 90);
                path.lineTo(width, 0f);
                path.lineTo(rx, 0f);
                path.close();

                paint.reset();
                paint.setAntiAlias(true);
                paint.setStrokeWidth(0f);
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawPath(path, paint);
            }

            // bottom right
            int corner3 = cornerBottomRight > 0 ? cornerBottomRight : corner;
            if (corner3 > 0) {

                path.reset();
                int rx = width - corner3 - paddingRight;
                int ry = height - corner3 - paddingBottom;
                path.moveTo(rx, ry);
                // 画圆弧 stratAngle是只开始的角度，X轴正方向为0度，sweepAngle是持续的角度;
                float left = width - corner3 * 2f - paddingRight * 2f;
                float top = height - corner3 * 2f - paddingBottom * 2f;
                float right = width;
                float bottom = height;
                path.addArc(new RectF(left, top, right, bottom), 0, 90);
                path.lineTo(width, height);
                path.lineTo(width, ry);
                path.close();

                paint.reset();
                paint.setAntiAlias(true);
                paint.setStrokeWidth(0f);
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawPath(path, paint);
            }

            // bottom left
            int corner4 = cornerBottomLeft > 0 ? cornerBottomLeft : corner;
            if (corner4 > 0) {

                path.reset();
                int rx = corner4 + paddingLeft;
                int ry = height - corner4 - paddingBottom;
                path.moveTo(rx, ry);
                // 画圆弧 stratAngle是只开始的角度，X轴正方向为0度，sweepAngle是持续的角度;
                float left = 0f;
                float top = height - corner4 * 2f - paddingBottom * 2f;
                float right = rx * 2f;
                float bottom = height;
                path.addArc(new RectF(left, top, right, bottom), 90, 90);
                path.lineTo(0f, height);
                path.lineTo(rx, height);
                path.close();

                paint.reset();
                paint.setAntiAlias(true);
                paint.setStrokeWidth(0f);
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawPath(path, paint);
            }

        } catch (Exception e) {
//            LeanBackUtil.log("RoundImpl -> clipRect -> Exception -> this = " + this + ", msg = " + e.getMessage());
        }
    }

    default void drawBorder(Canvas canvas, Paint paint, Path path, int strokeWidth, int strokeColor,
                            int corner, int cornerTopLeft, int cornerTopRight, int cornerBottomRight, int cornerBottomLeft) {
        try {

            if (null == paint)
                throw new Exception("error: paint null");

            if (strokeWidth <= 0)
                throw new Exception("warning: strokeWidth <= 0");

            int roundTopLeft = cornerTopLeft > 0 ? cornerTopLeft : corner;
            int roundTopRight = cornerTopRight > 0 ? cornerTopRight : corner;
            int roundBottomRight = cornerBottomRight > 0 ? cornerBottomRight : corner;
            int roundBottomLeft = cornerBottomLeft > 0 ? cornerBottomLeft : corner;

            int width = ((View) this).getWidth();
            int height = ((View) this).getHeight();

//            Path path = new Path();
//
//            // moveTo
//            float move_x1 = roundTopLeft;
//            float move_y1 = roundTopLeft;
//            path.moveTo(move_x1, move_y1);
//
//            // top left
//            float left1 = strokeWidth * 0.5f;
//            float top1 = strokeWidth * 0.5f;
//            float right1 = roundTopLeft * 2f - strokeWidth * 0.5f;
//            float bottom1 = roundTopLeft * 2f - strokeWidth * 0.5f;
//            path.addArc(new RectF(left1, top1, right1, bottom1), -180, 90);
//
//            // lineTo
//            float line_x1 = width - roundTopRight + strokeWidth * 0.5f;
//            float line_y1 = strokeWidth * 0.5f;
//            path.lineTo(line_x1, line_y1);
//
//            // moveTo
//            float move_x2 = line_x1;
//            float move_y2 = roundTopRight;
//            path.moveTo(move_x2, move_y2);
//
//            // top right
//            float left2 = width - roundTopRight * 2f;
//            float top2 = strokeWidth * 0.5f;
//            float right2 = width - strokeWidth * 0.5f;
//            float bottom2 = roundTopRight * 2f - strokeWidth * 0.5f;
//            path.addArc(new RectF(left2, top2, right2, bottom2), -90, 90);
//
//            // moveTo
//            float move_x3 = width - strokeWidth * 0.5f;
//            float move_y3 = roundTopRight - strokeWidth * 0.5f;
//            path.moveTo(move_x3, move_y3);
//
//            // lineTo
//            float line_x3 = width - strokeWidth * 0.5f;
//            float line_y3 = height - roundBottomRight + strokeWidth * 0.5f;
//            path.lineTo(line_x3, line_y3);
//
//            // moveTo
//            float move_x4 = width - roundBottomRight;
//            float move_y4 = height - roundBottomRight;
//            path.moveTo(move_x4, move_y4);
//
//            // bottom right
//            float left3 = left2;
//            float top3 = height - roundBottomRight * 2f + strokeWidth * 0.5f;
//            float right3 = width - strokeWidth * 0.5f;
//            float bottom3 = height - strokeWidth * 0.5f;
//            path.addArc(new RectF(left3, top3, right3, bottom3), 0, 90);
//
//            // moveTo
//            float move_x5 = width - roundBottomRight + strokeWidth * 0.5f;
//            float move_y5 = height - strokeWidth * 0.5f;
//            path.moveTo(move_x5, move_y5);
//
//            // lineTo
//            float line_x4 = roundBottomLeft;
//            float line_y4 = height - strokeWidth * 0.5f;
//            path.lineTo(line_x4, line_y4);
//
//            // moveTo
//            float move_x6 = roundBottomLeft;
//            float move_y6 = height - roundBottomLeft + strokeWidth * 0.5f;
//            path.moveTo(move_x6, move_y6);
//
//            // bottom left
//            float left4 = strokeWidth * 0.5f;
//            float top4 = height - roundBottomLeft * 2f + strokeWidth * 0.5f;
//            float right4 = roundBottomLeft * 2f - strokeWidth * 0.5f;
//            float bottom4 = height - strokeWidth * 0.5f;
//            path.addArc(new RectF(left4, top4, right4, bottom4), 90, 90);
//
//            // moveTo
//            float move_x7 = strokeWidth * 0.5f;
//            float move_y7 = height - roundBottomLeft + strokeWidth * 0.5f;
//            path.moveTo(move_x7, move_y7);
//
//            // lineTo
//            float line_x5 = strokeWidth * 0.5f;
//            float line_y5 = roundTopLeft;
//            path.lineTo(line_x5, line_y5);
//
//            // close
//            path.close();

            RectF rectF = new RectF(strokeWidth * 0.5f, strokeWidth * 0.5f, width - strokeWidth * 0.5f, height - strokeWidth * 0.5f);
            float[] radii = {roundTopLeft, roundTopLeft, roundTopRight, roundTopRight, roundBottomRight, roundBottomRight, roundBottomLeft, roundBottomLeft};

            path.reset();
            path.addRoundRect(rectF, radii, Path.Direction.CCW);
            path.close();

            paint.reset();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(strokeWidth);
            paint.setColor(strokeColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setXfermode(null);
            canvas.drawPath(path, paint);
        } catch (Exception e) {
//            LeanBackUtil.log("RoundImpl -> drawBorder -> Exception -> this = " + this + ", msg = " + e.getMessage());
        }
    }

    default void focusScale(boolean gainFocus, float scale) {
        try {
            ((View) this).setScaleX(gainFocus ? scale : 1f);
            ((View) this).setScaleY(gainFocus ? scale : 1f);
        } catch (Exception e) {
//            LeanBackUtil.log("RoundImpl -> focusScale -> Exception -> this = " + this + ", msg = " + e.getMessage());
        }
    }
}
