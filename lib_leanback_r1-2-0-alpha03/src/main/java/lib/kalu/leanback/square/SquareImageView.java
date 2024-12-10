package lib.kalu.leanback.square;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.leanback.R;

import lib.kalu.leanback.util.LeanBackUtil;

@SuppressLint("AppCompatCustomView")
public class SquareImageView extends ImageView {

    private int mType = 0;

    public SquareImageView(Context context) {
        super(context);
        init(context, null);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            if (mType == 0)
                throw new Exception("warning: mType == 0");
            if (mType == 1) {
                int size = View.MeasureSpec.getSize(heightMeasureSpec);
                int spec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
                super.onMeasure(spec, spec);
            } else {
                int size = View.MeasureSpec.getSize(widthMeasureSpec);
                int spec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
                super.onMeasure(spec, spec);
            }
        } catch (Exception e) {
            LeanBackUtil.log("SquareImageView -> Exception -> " + e.getMessage());
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void init(@NonNull Context context, @NonNull AttributeSet attrs) {
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView);
            mType = typedArray.getInteger(R.styleable.SquareImageView_siv_type, 0);
        } catch (Exception e) {
            LeanBackUtil.log("SquareImageView -> init -> Exception -> " + e.getMessage(), e);
        }

        if (null != typedArray) {
            typedArray.recycle();
        }
    }
}
