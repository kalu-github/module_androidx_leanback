package lib.kalu.leanback.plus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.R;

/**
 * description: 圆角文字
 * created by kalu on 2020-11-27
 */
@SuppressLint("AppCompatCustomView")
public class TextViewPlus extends TextView {

    private @FloatRange(from = 0f, to = Float.MAX_VALUE)
    float mDrawableLeftWidth = 0f;
    private @FloatRange(from = 0f, to = Float.MAX_VALUE)
    float mDrawableLeftHeight = 0f;
    private @FloatRange(from = 0f, to = Float.MAX_VALUE)
    float mDrawableTopWidth = 0f;
    private @FloatRange(from = 0f, to = Float.MAX_VALUE)
    float mDrawableTopHeight = 0f;
    private @FloatRange(from = 0f, to = Float.MAX_VALUE)
    float mDrawableRightWidth = 0f;
    private @FloatRange(from = 0f, to = Float.MAX_VALUE)
    float mDrawableRightHeight = 0f;
    private @FloatRange(from = 0f, to = Float.MAX_VALUE)
    float mDrawableBottomWidth = 0f;
    private @FloatRange(from = 0f, to = Float.MAX_VALUE)
    float mDrawableBottomHeight = 0f;

    public TextViewPlus(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public TextViewPlus(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextViewPlus(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {

        if (null == attrs)
            return;

        TypedArray typed = null;
        try {
            typed = context.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
            mDrawableLeftWidth = typed.getDimension(R.styleable.TextViewPlus_tvp_drawable_left_width, 0f);
            mDrawableLeftHeight = typed.getDimension(R.styleable.TextViewPlus_tvp_drawable_left_height, 0f);
            mDrawableTopWidth = typed.getDimension(R.styleable.TextViewPlus_tvp_drawable_top_width, 0f);
            mDrawableTopHeight = typed.getDimension(R.styleable.TextViewPlus_tvp_drawable_top_height, 0f);
            mDrawableRightWidth = typed.getDimension(R.styleable.TextViewPlus_tvp_drawable_right_width, 0f);
            mDrawableRightHeight = typed.getDimension(R.styleable.TextViewPlus_tvp_drawable_right_height, 0f);
            mDrawableBottomWidth = typed.getDimension(R.styleable.TextViewPlus_tvp_drawable_bottom_width, 0f);
            mDrawableBottomHeight = typed.getDimension(R.styleable.TextViewPlus_tvp_drawable_bottom_height, 0f);
        } catch (Exception e) {
        } finally {
            if (null != typed) {
                typed.recycle();
            }
        }
    }

    /**********************************************************************************************/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setDrawableBounds();
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        setDrawableBounds();
    }

    @Override
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int start, int top, int end, int bottom) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        setDrawableBounds();
    }

    @Override
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        setDrawableBounds();
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        setDrawableBounds();
    }

    private void setDrawableBounds() {

        // 左,上,右,下
        Drawable[] drawables = getCompoundDrawables();

        // DrawableLeft
        if (mDrawableLeftWidth > 0f && mDrawableLeftHeight > 0f && null != drawables[0] && null != drawables[0].getBounds() && drawables[0].getBounds().width() > 0f && drawables[0].getBounds().height() > 0f) {
            float left = drawables[0].getBounds().left;
            float top = drawables[0].getBounds().height() * 0.5f - mDrawableLeftHeight * 0.5f;
            float right = left + mDrawableLeftWidth;
            float bottom = drawables[0].getBounds().height() * 0.5f + mDrawableLeftHeight * 0.5f;
            drawables[0].setBounds((int) left, (int) top, (int) right, (int) bottom);
        }

        // 上
        if (mDrawableTopWidth > 0f && mDrawableTopHeight > 0f && null != drawables[1] && null != drawables[1].getBounds() && drawables[1].getBounds().width() > 0f && drawables[1].getBounds().height() > 0f) {
            float left = drawables[1].getBounds().left;
            float top = drawables[1].getBounds().height() * 0.5f - mDrawableTopHeight * 0.5f;
            float right = left + mDrawableTopWidth;
            float bottom = drawables[1].getBounds().height() * 0.5f + mDrawableTopHeight * 0.5f;
            drawables[1].setBounds((int) left, (int) top, (int) right, (int) bottom);
        }

        // 右
        if (mDrawableRightWidth > 0f && mDrawableRightHeight > 0f && null != drawables[2] && null != drawables[2].getBounds() && drawables[2].getBounds().width() > 0f && drawables[2].getBounds().height() > 0f) {
            float left = drawables[2].getBounds().left;
            float top = drawables[2].getBounds().height() * 0.5f - mDrawableRightHeight * 0.5f;
            float right = left + mDrawableRightWidth;
            float bottom = drawables[2].getBounds().height() * 0.5f + mDrawableRightHeight * 0.5f;
            drawables[2].setBounds((int) left, (int) top, (int) right, (int) bottom);
        }

        // 下
        if (mDrawableBottomWidth > 0f && mDrawableBottomHeight > 0f && null != drawables[3] && null != drawables[3].getBounds() && drawables[3].getBounds().width() > 0f && drawables[3].getBounds().height() > 0f) {
            float left = drawables[3].getBounds().left;
            float top = drawables[3].getBounds().height() * 0.5f - mDrawableBottomHeight * 0.5f;
            float right = left + mDrawableBottomWidth;
            float bottom = drawables[3].getBounds().height() * 0.5f + mDrawableBottomHeight * 0.5f;
            drawables[3].setBounds((int) left, (int) top, (int) right, (int) bottom);
        }

        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }
}
