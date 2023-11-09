package lib.kalu.leanback.radio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;
import androidx.leanback.R;

@SuppressLint("AppCompatCustomView")
public final class RadioButton extends android.widget.RadioButton {
    @ColorInt
    private int mUnderlineColor = Color.TRANSPARENT;
    private int mUnderlineWidth = 0;
    private int mUnderlineHeight = 0;

    public RadioButton(Context context) {
        super(context);
        init(null);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        Bitmap bitmap = null;
        setButtonDrawable(new BitmapDrawable(bitmap));

        TypedArray attributes = null;
        try {
            attributes = getContext().obtainStyledAttributes(attrs, R.styleable.RadioButton);
            mUnderlineColor = attributes.getColor(R.styleable.RadioButton_rb_text_underline_color, Color.TRANSPARENT);
            mUnderlineHeight = attributes.getDimensionPixelOffset(R.styleable.RadioButton_rb_text_underline_height, 0);
            mUnderlineWidth = attributes.getDimensionPixelOffset(R.styleable.RadioButton_rb_text_underline_width, 0);
        } catch (Exception e) {
        }

        if (null != attributes) {
            attributes.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mUnderlineColor == Color.TRANSPARENT)
            return;
        if (mUnderlineWidth <= 0 || mUnderlineHeight <= 0)
            return;
        boolean hasFocus = hasFocus();
        if (hasFocus)
            return;
        boolean checked = isChecked();
        if (!checked)
            return;

        // 驻留文字下划线
        try {
            TextPaint textPaint = getPaint();
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            int measureTextHeight = (int) (fontMetrics.bottom - fontMetrics.top);
            int paintColor = textPaint.getColor();
            int strokeWidth = (int) textPaint.getStrokeWidth();
            int width = getWidth();
            int height = getHeight();
            int startX;
            int stopX;
            if (mUnderlineWidth <= 0) {
                int measureTextWidth = (int) textPaint.measureText(String.valueOf(getText()));
                startX = width / 2 - measureTextWidth / 2;
                stopX = startX + measureTextWidth;
            } else {
                startX = width / 2 - mUnderlineWidth / 2;
                stopX = startX + mUnderlineWidth;
            }
            int startY = height / 2 + measureTextHeight / 2 + mUnderlineHeight / 2;
            if (startY >= height) {
                startY = height - mUnderlineHeight / 2;
            }
            int stopY = startY;
            textPaint.setStrokeJoin(Paint.Join.ROUND);
            textPaint.setStrokeCap(Paint.Cap.ROUND);
            textPaint.setAntiAlias(true);
            textPaint.setStrokeWidth(mUnderlineHeight);
            if (mUnderlineColor == Color.TRANSPARENT) {
                mUnderlineColor = paintColor;
            }
            textPaint.setColor(mUnderlineColor);
            canvas.drawLine(startX, startY, stopX, stopY, textPaint);
            textPaint.setColor(paintColor);
            textPaint.setStrokeWidth(strokeWidth);
        } catch (Exception e) {
        }
    }
}
