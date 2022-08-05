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
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.leanback.R;

public final class RoundHelper {
    public float[] mRadii = new float[8];   // top-left, top-right, bottom-right, bottom-left
    public Path mClipPath;                 // 剪裁区域路径
    public boolean mClipBackground;        // 是否剪裁背景
    public boolean mClipCircle;        // 是否剪裁背景
    public boolean mFocus = false;
    public float mScale = 1.05f;
    public int mDuration = 100;
    public Region mAreaRegion;             // 内容区域
    public RectF mLayer;                   // 画布图层大小
    private Paint mPaint; // 画笔

    public void init(@NonNull Context context, @NonNull AttributeSet attrs) {

        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundApi);
            mClipCircle = typedArray.getBoolean(R.styleable.RoundApi_ra_clip_circle, false);
            mClipBackground = typedArray.getBoolean(R.styleable.RoundApi_ra_clip_background, false);
            mFocus = typedArray.getBoolean(R.styleable.RoundApi_ra_focus, false);
            mScale = typedArray.getFloat(R.styleable.RoundApi_ra_scale, 1.05f);
            mDuration = typedArray.getInt(R.styleable.RoundApi_ra_duration, 100);
            int topLeft = typedArray.getDimensionPixelOffset(R.styleable.RoundApi_ra_corner_top_left, 0);
            int topRight = typedArray.getDimensionPixelOffset(R.styleable.RoundApi_ra_corner_top_right, 0);
            int bottomLeft = typedArray.getDimensionPixelSize(R.styleable.RoundApi_ra_corner_bottom_left, 0);
            int bottomRight = typedArray.getDimensionPixelSize(R.styleable.RoundApi_ra_corner_bottom_right, 0);
            mRadii[0] = topLeft;
            mRadii[1] = topLeft;
            mRadii[2] = topRight;
            mRadii[3] = topRight;
            mRadii[4] = bottomRight;
            mRadii[5] = bottomRight;
            mRadii[6] = bottomLeft;
            mRadii[7] = bottomLeft;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != typedArray) {
            typedArray.recycle();
        }

        mLayer = new RectF();
        mClipPath = new Path();
        mAreaRegion = new Region();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
    }

    public void onSizeChanged(View view, int w, int h) {
        mLayer.set(0, 0, w, h);
        refreshRegion(view);
    }

    public void refreshRegion(View view) {
        int w = (int) mLayer.width();
        int h = (int) mLayer.height();
        RectF areas = new RectF();
//        areas.left = 0;
//        areas.top = 0;
//        areas.right = w;
//        areas.bottom = h;
        areas.left = view.getPaddingLeft();
        areas.top = view.getPaddingTop();
        areas.right = w - view.getPaddingRight();
        areas.bottom = h - view.getPaddingBottom();
        mClipPath.reset();
        if (mClipCircle) {
            float d = areas.width() >= areas.height() ? areas.height() : areas.width();
            float r = d / 2;
            PointF center = new PointF(w / 2, h / 2);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                mClipPath.addCircle(center.x, center.y, r, Path.Direction.CW);
                mClipPath.moveTo(0, 0);  // 通过空操作让Path区域占满画布
                mClipPath.moveTo(w, h);
            } else {
                float y = h / 2 - r;
                mClipPath.moveTo(areas.left, y);
                mClipPath.addCircle(center.x, y + r, r, Path.Direction.CW);
            }
        } else {
            mClipPath.addRoundRect(areas, mRadii, Path.Direction.CW);
        }
        Region clip = new Region((int) areas.left, (int) areas.top,
                (int) areas.right, (int) areas.bottom);
        mAreaRegion.setPath(mClipPath, clip);
    }

    public void onClipDraw(Canvas canvas) {

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawPath(mClipPath, mPaint);
        } else {  //android 9.0
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            final Path path = new Path();
            path.addRect(0, 0, (int) mLayer.width(), (int) mLayer.height(), Path.Direction.CW);
            path.op(mClipPath, Path.Op.DIFFERENCE);
            canvas.drawPath(path, mPaint);
        }

//        // 边框
//        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mPaint.setStrokeWidth(80);
//        mPaint.setColor(focus ? mStrokeColorFocus : mStrokeColor);
//        RectF rectF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
////        canvas.drawRoundRect(rectF, 10, 10, mPaint);
//        canvas.drawLine(0, canvas.getWidth(), 40, 40, mPaint);
    }

    protected void onFocusChanged(@NonNull View view, boolean gainFocus) {
        if (!mFocus)
            return;
        ViewCompat.animate(view).scaleX(gainFocus ? mScale : 1f).scaleY(gainFocus ? mScale : 1f).setDuration(mDuration).start();
    }
}
