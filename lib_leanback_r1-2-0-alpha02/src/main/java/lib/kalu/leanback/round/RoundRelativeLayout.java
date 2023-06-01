package lib.kalu.leanback.round;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import lib.kalu.leanback.util.LeanBackUtil;

public class RoundRelativeLayout extends RelativeLayout implements RoundImpl {

    public RoundRelativeLayout(Context context) {
        super(context);
    }

    public RoundRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(context, attrs);
    }

    public RoundRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
    }

    @SuppressLint("NewApi")
    public RoundRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttributeSet(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int[] measureSpec = measureSpec(widthMeasureSpec, heightMeasureSpec);
            if (null == measureSpec)
                throw new Exception("measureSpec error: null");
            if (measureSpec.length != 2)
                throw new Exception("length error: != 2");
            super.onMeasure(measureSpec[0], measureSpec[1]);
        } catch (Exception e) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            LeanBackUtil.log("RoundRelativeLayout => onMeasure => " + e.getMessage());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        onSizeChanged(this, w, h);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        onFocusChanged(this, gainFocus);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        saveLayer(canvas);
        clipPath(canvas);
        super.dispatchDraw(canvas);
        drawPath(canvas);
        canvas.restore();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mClipBackground[0]) {
            canvas.save();
            canvas.clipPath(mClipPath[0]);
            super.draw(canvas);
            canvas.restore();
        } else {
            super.draw(canvas);
        }
    }
}
