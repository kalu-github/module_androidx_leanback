package lib.kalu.leanback.round;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RoundFrameLayout extends FrameLayout {

    RoundHelper mRCHelper;

    public RoundFrameLayout(Context context) {
        super(context);
    }

    public RoundFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("NewApi")
    public RoundFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mRCHelper = new RoundHelper();
        this.mRCHelper.init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float rateW = mRCHelper.getRateW();
        float rateH = mRCHelper.getRateH();
        if (rateH <= 0 && rateW <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            try {
                int width;
                int height;
                int spec;
                if (rateW > 0) {
                    height = MeasureSpec.getSize(heightMeasureSpec);
                    width = (int) (height * rateW);
                    spec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                    super.onMeasure(spec, heightMeasureSpec);
                } else {
                    width = MeasureSpec.getSize(widthMeasureSpec);
                    height = (int) (width * rateH);
                    spec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                    super.onMeasure(widthMeasureSpec, spec);
                }
                setMeasuredDimension(width, height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRCHelper.onSizeChanged(this, w, h);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        mRCHelper.onFocusChanged(this, gainFocus);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        mRCHelper.saveLayer(canvas);
        mRCHelper.clipPath(canvas);
        super.dispatchDraw(canvas);
        mRCHelper.clipRound(canvas);
        canvas.restore();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mRCHelper.mClipBackground) {
            canvas.save();
            canvas.clipPath(mRCHelper.mClipPath);
            super.draw(canvas);
            canvas.restore();
        } else {
            super.draw(canvas);
        }
    }

    public void refreshRound(@NonNull float topLeft, @NonNull float topRight, @NonNull float bottomLeft, @NonNull float bottomRight) {
        mRCHelper.refreshRound(this, topLeft, topRight, bottomLeft, bottomRight);
    }

    public void resetRound() {
        mRCHelper.resetRound(this);
    }
}
