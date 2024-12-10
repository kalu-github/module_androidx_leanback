package lib.kalu.leanback.square;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import lib.kalu.leanback.util.LeanBackUtil;

@SuppressLint("AppCompatCustomView")
public class SquareImageView extends ImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int modeW = MeasureSpec.getMode(widthMeasureSpec);
            int modeH = MeasureSpec.getMode(heightMeasureSpec);
            if (modeW == MeasureSpec.EXACTLY) {
                int size = View.MeasureSpec.getSize(widthMeasureSpec);
                int spec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
                super.onMeasure(spec, spec);
            } else if (modeH == MeasureSpec.EXACTLY) {
                int size = View.MeasureSpec.getSize(heightMeasureSpec);
                int spec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
                super.onMeasure(spec, spec);
            } else {
                throw new Exception("error: not find");
            }
        } catch (Exception e) {
            LeanBackUtil.log("SquareImageView -> Exception -> " + e.getMessage());
        }
    }
}
