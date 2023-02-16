package lib.kalu.leanback.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebSettings;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import lib.kalu.leanback.tab.model.TabModel;

@SuppressLint("AppCompatCustomView")
class TabTextView extends TextView {

    private boolean mUnderline = false;
    private int mUnderlineColor = Color.TRANSPARENT;
    private int mUnderlineHeight = 0;
    private int mUnderlineWidth = 0;

    private TabModel mTabModel;

    public TabTextView(@NonNull Context context, @NonNull TabModel data) {
        super(context);
        this.mTabModel = data;
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getPaint().setFakeBoldText(false);
        super.onDraw(canvas);

        // 驻留文字下划线
        if (mUnderline && mUnderlineHeight > 0f && String.valueOf(1).equals(getHint())) {
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        CharSequence text = getText();

        int left = getPaddingLeft();
        int right = getPaddingRight();
        int measureText = (int) getPaint().measureText(String.valueOf(text));
        int width = measureText;

        if (left == 0 || right == 0) {
            int size = measureText / text.length();
            width += size * 1.5;
        } else {
            width += left;
            width += right;
        }
        setMeasuredDimension(width, height);
    }

    private void init() {
        setEnabled(false);
        setSelected(false);
        setFocusable(false);
        setMaxLines(1);
        setLines(1);
        setMinEms(2);
        setGravity(Gravity.CENTER);
    }

    protected void setUnderline(boolean underline) {
        this.mUnderline = underline;
    }

    protected void setUnderlineColor(int color) {
        this.mUnderlineColor = color;
    }

    protected void setUnderlineWidth(int width) {
        this.mUnderlineWidth = width;
    }

    protected void setUnderlineHeight(int height) {
        this.mUnderlineHeight = height;
    }

    /*************************/


    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        refreshUI();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        refreshUI();
    }

    protected void setChecked(boolean v) {
        setSelected(v);
    }

    protected boolean isChecked() {
        return isSelected();
    }

    protected void setFocus(boolean v) {
        setEnabled(v);
    }

    protected boolean isFocus() {
        return hasFocus();
    }

    @Override
    public boolean hasFocus() {
        return isEnabled();
    }

    private void refreshUI() {
        boolean focus = isFocus();
        boolean checked = isChecked();
        refreshTextColor(focus, checked);
        refreshBackground(focus, checked);
    }

    // text-color 优先级 ：resource > color
    private void refreshTextColor(boolean focus, boolean checked) {

        try {
            @ColorRes
            int c1 = mTabModel.getTextColorResource(focus, checked);
            if (c1 != 0) {
                setTextColor(getResources().getColor(c1));
            } else {
                @ColorInt
                int c2 = mTabModel.getTextColor(focus, checked);
                if (c2 != 0) {
                    setTextColor(c2);
                }
            }
        } catch (Exception e) {
        }
    }

    // 优先级 ：net > path > assets > resource > color
    private void refreshBackground(boolean focus, boolean checked) {

        try {

            String s1 = mTabModel.getBackgroundImageUrl(focus, checked);
//            logE("updateImageBackground => url = " + s1);

            String s2 = mTabModel.getBackgroundImagePath(focus, checked);
//            logE("updateImageBackground => path = " + s2);

            String s3 = mTabModel.getBackgroundImageAssets(focus, checked);
//            logE("updateImageBackground => assets = " + s3);

            int i4 = mTabModel.getBackgroundResource(focus, checked);
//            logE("updateImageBackground => resource = " + i4);

            int i5 = mTabModel.getBackgroundColor(focus, checked);
//            logE("updateImageBackground => color = " + i5);

//        // 背景 => 渐变背景色
//        if (null != colors && colors.length >= 3) {
//            int[] color = stay ? colors[2] : (focus ? colors[1] : colors[0]);
//            logE("updateImageBackground[colors]=> color = " + Arrays.toString(color));
//            setBackgroundGradient(view, color, radius);
//        }

            if (null != s1 && s1.length() > 0) {
                TabUtil.loadImageUrl(this, s1, true);
            } else if (null != s2 && s2.length() > 0) {
                Drawable drawable = TabUtil.decodeDrawable(getContext(), s2, false);
                setBackground(drawable);
            } else if (null != s3 && s3.length() > 0) {
                Drawable drawable = TabUtil.decodeDrawable(getContext(), s2, true);
                setBackground(drawable);
            } else if (i4 != 0) {
                setBackgroundResource(i4);
            } else if (i5 != 0) {
                ColorDrawable drawable = new ColorDrawable(i5);
                setBackground(drawable);
            }
        } catch (Exception e) {
        }
    }
}