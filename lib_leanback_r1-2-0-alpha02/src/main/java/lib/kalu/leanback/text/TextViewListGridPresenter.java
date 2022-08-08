package lib.kalu.leanback.text;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@SuppressLint("AppCompatCustomView")
public final class TextViewListGridPresenter extends TextView {
    public TextViewListGridPresenter(Context context) {
        super(context);
    }

    public TextViewListGridPresenter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewListGridPresenter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextViewListGridPresenter(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        setVisibility(null != text && text.length() > 0 ? View.VISIBLE : View.GONE);
    }
}