package lib.kalu.leanback.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.leanback.R;

import lib.kalu.leanback.util.LeanBackUtil;

interface RoundImpl {
    float[] mRadii = new float[8];   // top-left, top-right, bottom-right, bottom-left
    float[] mRadiiTemp = new float[8];   // top-left, top-right, bottom-right, bottom-left
    Path[] mClipPath = new Path[]{null};                 // 剪裁区域路径
    Boolean[] mClipBackground = new Boolean[]{false};        // 是否剪裁背景
    Boolean[] mClipCircle = new Boolean[]{false};       // 是否剪裁背景
    Boolean[] mClip = new Boolean[]{false};
    Float[] mScale = new Float[]{1.05F};
    Integer[] mDuration = new Integer[]{100};
    Region[] mAreaRegion = new Region[]{null};             // 内容区域
    RectF[] mLayer = new RectF[]{null};                   // 画布图层大小
    Paint[] mPaint = new Paint[]{null}; // 画笔

    Float[] mRateWidth = new Float[]{0F};
    Float[] mRateHeight = new Float[]{0F};

    default void initAttributeSet(@NonNull Context context, @NonNull AttributeSet attrs) {

        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundLayout);
            mClip[0] = typedArray.getBoolean(R.styleable.RoundLayout_rl_clip_path, false);
            mClipCircle[0] = typedArray.getBoolean(R.styleable.RoundLayout_rl_clip_circle, false);
            mClipBackground[0] = typedArray.getBoolean(R.styleable.RoundLayout_rl_clip_background, false);
            mScale[0] = typedArray.getFloat(R.styleable.RoundLayout_rl_scale, 1.05f);
            mDuration[0] = typedArray.getInt(R.styleable.RoundLayout_rl_duration, 100);
            int topLeft = typedArray.getDimensionPixelOffset(R.styleable.RoundLayout_rl_corner_top_left, 0);
            int topRight = typedArray.getDimensionPixelOffset(R.styleable.RoundLayout_rl_corner_top_right, 0);
            int bottomLeft = typedArray.getDimensionPixelSize(R.styleable.RoundLayout_rl_corner_bottom_left, 0);
            int bottomRight = typedArray.getDimensionPixelSize(R.styleable.RoundLayout_rl_corner_bottom_right, 0);
            mRadii[0] = topLeft;
            mRadii[1] = topLeft;
            mRadii[2] = topRight;
            mRadii[3] = topRight;
            mRadii[4] = bottomRight;
            mRadii[5] = bottomRight;
            mRadii[6] = bottomLeft;
            mRadii[7] = bottomLeft;
            float w = typedArray.getFloat(R.styleable.RoundLayout_rl_rate_width, 0);
            float h = typedArray.getFloat(R.styleable.RoundLayout_rl_rate_height, 0);
            mRateHeight[0] = h;
            mRateWidth[0] = w > 0 && h > 0 ? 0 : w;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != typedArray) {
            typedArray.recycle();
        }

        mLayer[0] = new RectF();
        mClipPath[0] = new Path();
        mAreaRegion[0] = new Region();
        mPaint[0] = new Paint();
        mPaint[0].setColor(Color.WHITE);
        mPaint[0].setAntiAlias(true);
        mPaint[0].setFilterBitmap(true);
    }

    default void onSizeChanged(View view, int w, int h) {
        mLayer[0].set(0, 0, w, h);
        refreshRegion(view, false);
    }

    default void refreshRegion(@NonNull View view, @NonNull boolean temp) {
        int w = (int) mLayer[0].width();
        int h = (int) mLayer[0].height();
        RectF areas = new RectF();
//        areas.left = 0;
//        areas.top = 0;
//        areas.right = w;
//        areas.bottom = h;
        areas.left = view.getPaddingLeft();
        areas.top = view.getPaddingTop();
        areas.right = w - view.getPaddingRight();
        areas.bottom = h - view.getPaddingBottom();
        mClipPath[0].reset();
        if (mClipCircle[0]) {
            float d = areas.width() >= areas.height() ? areas.height() : areas.width();
            float r = d / 2;
            PointF center = new PointF(w / 2, h / 2);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                mClipPath[0].addCircle(center.x, center.y, r, Path.Direction.CW);
                mClipPath[0].moveTo(0, 0);  // 通过空操作让Path区域占满画布
                mClipPath[0].moveTo(w, h);
            } else {
                float y = h / 2 - r;
                mClipPath[0].moveTo(areas.left, y);
                mClipPath[0].addCircle(center.x, y + r, r, Path.Direction.CW);
            }
        } else {
            if (temp) {
                mClipPath[0].addRoundRect(areas, mRadiiTemp, Path.Direction.CW);
            } else {
                mClipPath[0].addRoundRect(areas, mRadii, Path.Direction.CW);
            }
        }
        Region clip = new Region((int) areas.left, (int) areas.top,
                (int) areas.right, (int) areas.bottom);
        mAreaRegion[0].setPath(mClipPath[0], clip);
    }

    default void saveLayer(Canvas canvas) {
        if (!mClip[0]) {
            canvas.saveLayer(mLayer[0], null, Canvas.ALL_SAVE_FLAG);
        }
    }

    default void clipPath(Canvas canvas) {
        if (mClip[0]) {
            canvas.save();
            canvas.clipPath(mClipPath[0]);
        }
    }

    default void drawPath(Canvas canvas) {

        if (!mClip[0]) {
            mPaint[0].setColor(Color.WHITE);
            mPaint[0].setStyle(Paint.Style.FILL);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                mPaint[0].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                canvas.drawPath(mClipPath[0], mPaint[0]);
            } else {  //android 9.0
                mPaint[0].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                final Path path = new Path();
                path.addRect(0, 0, (int) mLayer[0].width(), (int) mLayer[0].height(), Path.Direction.CW);
                path.op(mClipPath[0], Path.Op.DIFFERENCE);
                canvas.drawPath(path, mPaint[0]);
            }
        }

//        // 边框
//        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mPaint.setStrokeWidth(80);
//        mPaint.setColor(focus ? mStrokeColorFocus : mStrokeColor);
//        RectF rectF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
////        canvas.drawRoundRect(rectF, 10, 10, mPaint);
//        canvas.drawLine(0, canvas.getWidth(), 40, 40, mPaint);
    }

    default void onFocusChanged(@NonNull View view, boolean gainFocus) {
        if (mScale[0] < 1F)
            return;
        ViewCompat.animate(view).scaleX(gainFocus ? mScale[0] : 1f).scaleY(gainFocus ? mScale[0] : 1f).setDuration(mDuration[0]).start();
    }

    default float getRateW() {
        return mRateWidth[0];
    }

    default float getRateH() {
        return mRateHeight[0];
    }

    default void refreshRound(@NonNull float topLeft, @NonNull float topRight, @NonNull float bottomLeft, @NonNull float bottomRight) {
        mRadiiTemp[0] = topLeft;
        mRadiiTemp[1] = topLeft;
        mRadiiTemp[2] = topRight;
        mRadiiTemp[3] = topRight;
        mRadiiTemp[4] = bottomRight;
        mRadiiTemp[5] = bottomRight;
        mRadiiTemp[6] = bottomLeft;
        mRadiiTemp[7] = bottomLeft;
        refreshRegion((View) this, true);
        ((View) this).invalidate();
    }

    default void resetRound() {
        mRadiiTemp[0] = 0;
        mRadiiTemp[1] = 0;
        mRadiiTemp[2] = 0;
        mRadiiTemp[3] = 0;
        mRadiiTemp[4] = 0;
        mRadiiTemp[5] = 0;
        mRadiiTemp[6] = 0;
        mRadiiTemp[7] = 0;
        refreshRegion((View) this, false);
        ((View) this).invalidate();
    }

    default void setRadius(int topLeft, int topRight, int bottomLeft, int bottomRight) {
        mRadii[0] = topLeft;
        mRadii[1] = topLeft;
        mRadii[2] = topRight;
        mRadii[3] = topRight;
        mRadii[4] = bottomRight;
        mRadii[5] = bottomRight;
        mRadii[6] = bottomLeft;
        mRadii[7] = bottomLeft;
    }

    default void setScale(float v) {
        mScale[0] = v;
    }

    default int[] measureSpec(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            if (mRateWidth[0] > 0) {
                int h = View.MeasureSpec.getSize(heightMeasureSpec);
                int w = (int) (h * mRateWidth[0]);
                int specW = View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY);
                return new int[]{specW, heightMeasureSpec};
            } else if (mRateHeight[0] > 0) {
                int w = View.MeasureSpec.getSize(widthMeasureSpec);
                int h = (int) (w * mRateHeight[0]);
                int specH = View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.EXACTLY);
                return new int[]{widthMeasureSpec, specH};
            } else {
                throw new Exception("not find");
            }
        } catch (Exception e) {
            LeanBackUtil.log("RoundHelper => measureSpec => " + e.getMessage());
            return null;
        }
    }
}