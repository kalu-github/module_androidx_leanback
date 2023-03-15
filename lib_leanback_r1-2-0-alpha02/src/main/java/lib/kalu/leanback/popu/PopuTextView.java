package lib.kalu.leanback.popu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.leanback.R;

/**
 * https://blog.csdn.net/ansondroider/article/details/124790843?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-124790843-blog-101005004.pc_relevant_multi_platform_whitelistv4&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-124790843-blog-101005004.pc_relevant_multi_platform_whitelistv4&utm_relevant_index=2
 */
@SuppressLint("AppCompatCustomView")
public final class PopuTextView extends TextView {
    public PopuTextView(Context context) {
        super(context);
    }

    public PopuTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PopuTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PopuTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private final Rect mRect = new Rect();
    private final RectF mRectF = new RectF();
    private final Path mPath = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1
        canvas.getClipBounds(mRect);
        // 2
        mRectF.left = mRect.left;
        mRectF.right = mRect.right;
        mRectF.top = mRect.top;
        mRectF.bottom = mRect.bottom * 0.9f;
        // 3-popu
        Paint paint = getPaint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        int rx = getResources().getDimensionPixelOffset(R.dimen.module_poputextview_margin);
        canvas.drawRoundRect(mRectF, rx, rx, paint);
        // 4
        int centerX = mRect.centerX();
        int x1 = centerX - rx;
        int x2 = centerX + rx;
        int y1 = (int) mRectF.bottom;
        int y2 = mRect.bottom;
        mPath.reset();
        mPath.moveTo(x1, y1);
        mPath.lineTo(x2, y1);
        mPath.lineTo(centerX, y2);
        mPath.lineTo(x1, y1);
        mPath.close();
        canvas.drawPath(mPath, paint);
    }

//    @Override
//    public void setBackground(Drawable background) {
//    }
}
