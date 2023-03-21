package lib.kalu.leanback.tags;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;

import lib.kalu.leanback.util.LeanBackUtil;

@SuppressLint("AppCompatCustomView")
final class TagsTextView extends TextView {
    @ColorInt
    private int mTextColorFocus = Color.WHITE;
    @ColorInt
    private int mTextColorChecked = Color.RED;
    @ColorInt
    private int mTextColorDefault = Color.GRAY;

    @DrawableRes
    private int mBackgroundResourceFocus = R.drawable.module_tagslayout_ic_shape_background_focus;
    @DrawableRes
    private int mBackgroundResourceChecked = R.drawable.module_tagslayout_ic_shape_background_checked;
    @DrawableRes
    private int mBackgroundResourceDefault = R.drawable.module_tagslayout_ic_shape_background_normal;

    public TagsTextView(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        CharSequence text = getText();
        if (null == text) {
            text = "";
        }
        int width = (int) getPaint().measureText(String.valueOf(text));
        if (width > 0) {
            width += getPaddingLeft();
            width += getPaddingRight();
        }
        setMeasuredDimension(width, height);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        setEnabled(false);
        setSelected(false);
        setFocusable(false);
        setGravity(Gravity.CENTER);
        setBackgroundColor(Color.TRANSPARENT);
    }

    /*************************/

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

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return false;
    }

    @Override
    public void clearFocus() {
    }

    protected void delFocus(boolean checked) {
        setChecked(checked);
        setFocus(false);
        try {
            if (checked) {
                setTextColor(mTextColorChecked);
                setBackgroundResource(mBackgroundResourceChecked);
            } else {
                setTextColor(mTextColorDefault);
                setBackgroundResource(mBackgroundResourceDefault);
            }
        } catch (Exception e) {
        }
    }

    protected boolean reqFocus(boolean callListener) {
        setChecked(true);
        setFocus(true);
        try {
            // 1
            if (callListener) {
                ((TagsLinearLayoutChild) getParent()).callListener(this);
            }
            // 2
            setTextColor(mTextColorFocus);
            setBackgroundResource(mBackgroundResourceFocus);
            LeanBackUtil.log("TagsTextView => reqFocus => succ");
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TagsTextView => reqFocus => " + e.getMessage());
            return false;
        }
    }

    protected void setText(@NonNull String text, boolean focus, boolean checked) {
        super.setText(text);
        setFocus(focus);
        setChecked(checked);
        if (focus) {
            setTextColor(mTextColorFocus);
        } else if (checked) {
            setTextColor(mTextColorChecked);
        } else {
            setTextColor(mTextColorDefault);
        }
    }

    //    public void hightlight() {
////        setSelected(true);
//        setTextColor(mTextColorChecked);
//        setBackgroundResource(mBackgroundResourceChecked);
//    }
//
//    protected boolean isHightlight() {
//        return hasFocus() || isSelected();
//    }
//
//
    public void setTextColorFocus(int mTextColorFocus) {
        this.mTextColorFocus = mTextColorFocus;
    }

    public void setTextColorSelect(int mTextColorChecked) {
        this.mTextColorChecked = mTextColorChecked;
    }

    public void setTextColorDefault(int mTextColorDefault) {
        this.mTextColorDefault = mTextColorDefault;
    }

    public void setBackgroundResourceFocus(int mBackgroundResourceFocus) {
        this.mBackgroundResourceFocus = mBackgroundResourceFocus;
    }

    public void setBackgroundResourceSelect(int mBackgroundResourceChecked) {
        this.mBackgroundResourceChecked = mBackgroundResourceChecked;
    }

    public void setBackgroundResourceDefault(int mBackgroundResourceDefault) {
        this.mBackgroundResourceDefault = mBackgroundResourceDefault;
    }
}
