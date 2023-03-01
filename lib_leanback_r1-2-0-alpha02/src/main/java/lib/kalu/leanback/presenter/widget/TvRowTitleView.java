package lib.kalu.leanback.presenter.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class TvRowTitleView extends TextView {
    public TvRowTitleView(Context context) {
        super(context);
    }

    public TvRowTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TvRowTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public TvRowTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        setVisibility(null != text && text.length() > 0 ? View.VISIBLE : View.GONE);
    }
}
