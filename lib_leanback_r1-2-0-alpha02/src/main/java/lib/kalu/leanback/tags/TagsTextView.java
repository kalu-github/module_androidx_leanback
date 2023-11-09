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

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return false;
    }

    @Override
    public void clearFocus() {
    }
}
