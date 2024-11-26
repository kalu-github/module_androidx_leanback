package lib.kalu.leanback.clock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.R;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("AppCompatCustomView")
public class TextClock extends TextView {

    private String mFormat = null;
    private Handler mHandler = null;

    public TextClock(Context context) {
        super(context);
        init(null);
    }

    public TextClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TextClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @SuppressLint("NewApi")
    public TextClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (null != mFormat && mFormat.length() > 0) {
            if (null == mHandler) {
                mHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        if (msg.what == 1000) {
                            try {
                                SimpleDateFormat format = new SimpleDateFormat(mFormat);
                                String data = format.format(new Date());
                                TextClock.super.setText(data);
                            } catch (Exception e) {
                            }
                            loopNext();
                        }
                    }
                };
            }
            loopNext();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    private void init(@Nullable AttributeSet attrs) {
        TypedArray attributes = null;
        try {
            attributes = getContext().obtainStyledAttributes(attrs, R.styleable.TextClock);
            mFormat = attributes.getString(R.styleable.TextClock_tc_format);
        } catch (Exception e) {
        }

        if (null != attributes) {
            attributes.recycle();
        }
    }

    private void loopNext() {
        if (null != mHandler) {
            mHandler.sendEmptyMessageDelayed(1000, 1000);
        }
    }
}
