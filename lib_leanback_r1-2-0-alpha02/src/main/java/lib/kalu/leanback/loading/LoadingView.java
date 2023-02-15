package lib.kalu.leanback.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.leanback.R;

@Keep
public class LoadingView extends View {

    private boolean mPause;
    private int mLoop = 0;
    private final Paint mPaint = new Paint();

    public LoadingView(Context context) {
        super(context);
        init(null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private int mCount = 8;
    private float mRadius = 0f;
    private float mRate = 0f;

    @ColorInt
    private int mColorCanvas = Color.TRANSPARENT;
    @ColorInt
    private int mColorBackground = Color.TRANSPARENT;
    @ColorInt
    private int mColorRound = Color.GRAY;

    private final void init(@Nullable AttributeSet attrs) {
//        setEnabled(true);

        TypedArray typedArray = null;

        try {
            typedArray = getContext().getApplicationContext().obtainStyledAttributes(attrs, R.styleable.LoadingView);
            mCount = typedArray.getInt(R.styleable.LoadingView_lv_count, 8);
            mRate = typedArray.getFloat(R.styleable.LoadingView_lv_rate, 0.9f);
            mRadius = typedArray.getDimension(R.styleable.LoadingView_lv_radius, 0f);
            mColorCanvas = typedArray.getColor(R.styleable.LoadingView_lv_color_canvas, Color.TRANSPARENT);
            mColorBackground = typedArray.getColor(R.styleable.LoadingView_lv_color_background, Color.TRANSPARENT);
            mColorRound = typedArray.getColor(R.styleable.LoadingView_lv_color_round, Color.GRAY);
        } catch (Exception e) {
        }

        if (null != typedArray) {
            typedArray.recycle();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            if (mPause) {
                mPause = false;
                invalidate();
            }
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            if (mPause) {
                mPause = false;
                invalidate();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        if (View.VISIBLE != getVisibility()) {
            mPause = true;
            return;
        }

        // 循环次数
        if (mLoop + 1 >= mCount) {
            mLoop = 0;
        }

        // 画笔
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(0f);
        mPaint.setFakeBoldText(true);

        float cx = getWidth() * 0.5f;
        float cy = getHeight() * 0.2f;
        float radius;
        if (mRadius == 0f) {
            radius = Math.max(cx, cy) / 6;
        } else {
            radius = mRadius;
        }
        float angle = 360 / mCount;

        // init
//        paint.setColor(Color.WHITE);
        canvas.drawColor(mColorCanvas);
//        canvas.drawColor(mColorCanvas, PorterDuff.Mode.CLEAR);
        mPaint.setColor(mColorBackground);
        canvas.drawCircle(cx, cy, Math.min(cx, cy), mPaint);

        // 椭圆
        int length = mLoop + mCount;
        for (int i = mLoop; i < length; i++) {
//            MediaLogUtil.log("MediaProgressBar => onDraw => i = " + i + ", mLoop = " + mLoop + ", mCount = " + mCount);
            if (i == mLoop) {
                if (mLoop != 0) {
                    canvas.save();
                    canvas.rotate(angle * (i % mCount), cx, cx);
                }
//                int color = Color.parseColor("#FFA7A7A7");
                mPaint.setColor(mColorRound);
            } else {
                try {
                    float r = ((mColorRound >> 16) & 0xff) / 255.0f;
                    float g = ((mColorRound >> 8) & 0xff) / 255.0f;
                    float b = ((mColorRound) & 0xff) / 255.0f;
//                    float a = ((mColorRound >> 24) & 0xff) / 255.0f;
                    int a = (255 / 11) * (i - mLoop);
                    int color = ((int) (a * 255.0f + 0.5f) << 24) |
                            ((int) (r * 255.0f + 0.5f) << 16) |
                            ((int) (g * 255.0f + 0.5f) << 8) |
                            (int) (b * 255.0f + 0.5f);
                    mPaint.setColor(color);
                } catch (Exception e) {
                }
            }
//            RectF rectF = new RectF(left, top, right, bottom);
//            canvas.drawRoundRect(rectF, rx, ry, paint);
//            MediaLogUtil.log("MediaProgressBar => onDraw => radius = " + radius + ", mRate = " + mRate);
            radius = radius * mRate;
            canvas.drawCircle(cx, cy, radius, mPaint);
            canvas.save();
            canvas.rotate(angle, cx, cx);
        }

        // delay
        mLoop = mLoop + 1;
        postInvalidateDelayed(120);
    }

    /*************/

    @Keep
    public final void setCount(@NonNull int count) {
        this.mCount = count;
    }

    @Keep
    public final void setRate(@NonNull float rate) {
        this.mRate = rate;
    }

    @Keep
    public final void setRadius(@DimenRes int resId) {
        try {
            float dimension = getResources().getDimension(resId);
            this.mRadius = dimension;
        } catch (Exception e) {
        }
    }
}
