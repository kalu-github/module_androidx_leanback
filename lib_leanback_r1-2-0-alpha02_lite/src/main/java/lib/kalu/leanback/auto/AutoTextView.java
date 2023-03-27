package lib.kalu.leanback.auto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import lib.kalu.leanback.focus.FocusTextView;

public class AutoTextView extends FocusTextView {
    public AutoTextView(Context context) {
        super(context);
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AutoTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        // super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }


    public void onFocusCall(boolean gainFocus) {
        // Ellipsize
        setMarqueeRepeatLimit(gainFocus ? Integer.MAX_VALUE : 0);
        setEllipsize(gainFocus ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
        // text
        refreshTextColor(gainFocus);
        // bg
        refreshBackground(gainFocus);
        // anim
        refreshAnim(gainFocus);
    }
}
