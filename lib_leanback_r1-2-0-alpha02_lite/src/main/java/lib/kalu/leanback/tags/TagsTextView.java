package lib.kalu.leanback.tags;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.leanback.R;

import lib.kalu.leanback.tags.model.TagBean;
import lib.kalu.leanback.util.LeanBackUtil;

@SuppressLint("AppCompatCustomView")
final class TagsTextView extends TextView {
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
        setFocusable(false);
        setGravity(Gravity.CENTER);
    }

    /*************************/

//    protected boolean isChecked() {
//        try {
//            Object tag = getTag(R.id.lb_tagslayout_data);
//            if (null == tag)
//                throw new Exception("tag error: null");
//            if (!(tag instanceof TagBean))
//                throw new Exception("tag error: " + tag);
//            return ((TagBean)tag).isChecked();
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    protected boolean isFocus() {
//        return hasFocus();
//    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return false;
    }

    @Override
    public void clearFocus() {
    }

//    protected void delFocus(boolean checked) {
//        setChecked(checked);
//        setFocus(false);
//        try {
//            if (checked) {
//                setTextColor(mTextColorChecked);
//                setBackgroundResource(mBackgroundResourceChecked);
//            } else {
//                setTextColor(mTextColorDefault);
//                setBackgroundResource(mBackgroundResourceDefault);
//            }
//        } catch (Exception e) {
//        }
//    }

//    protected boolean reqFocus(boolean callListener) {
//        setChecked(true);
//        setFocus(true);
//        try {
//            // 1
//            if (callListener) {
//                ((TagsLinearLayoutChild) getParent()).callListener(this);
//            }
//            // 2
//            setTextColor(mTextColorFocus);
//            setBackgroundResource(mBackgroundResourceFocus);
//            LeanBackUtil.log("TagsTextView => reqFocus => succ");
//            return true;
//        } catch (Exception e) {
//            LeanBackUtil.log("TagsTextView => reqFocus => " + e.getMessage());
//            return false;
//        }
//    }

//    protected void setText(@NonNull String text, boolean focus, boolean checked) {
//        super.setText(text);
//        setFocus(focus);
//        setChecked(checked);
//        if (focus) {
//            setTextColor(mTextColorFocus);
//        } else if (checked) {
//            setTextColor(mTextColorChecked);
//        } else {
//            setTextColor(mTextColorDefault);
//        }
//    }
}
