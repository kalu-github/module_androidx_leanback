package lib.kalu.leanback.clazz;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.leanback.R;

abstract class BaseScrollView extends ScrollView {

    @Dimension
    int mItemMargin;
    @Dimension
    int mItemHeight;
    @Dimension
    int mTextSize;
    @DrawableRes
    int mBackgroundResource;
    @DrawableRes
    int mBackgroundResourceChecked;
    @DrawableRes
    int mBackgroundResourceFocus;
    @ColorInt
    int mTextColor;
    @ColorInt
    int mTextColorChecked;
    @ColorInt
    int mTextColorFocus;

    public BaseScrollView(Context context) {
        super(context);
        init(context, null);
    }

    public BaseScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BaseScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setFocusable(false);
        setOrientation(radioGroup);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        radioGroup.setLayoutParams(layoutParams);
        addView(radioGroup);
    }

    private void init(@NonNull Context context, @NonNull AttributeSet attrs) {

        // 1
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        setFillViewport(true);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);

        // 2
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClassLayout);
            mBackgroundResource = typedArray.getResourceId(R.styleable.ClassLayout_cl_item_background_resource, 0);
            mBackgroundResourceChecked = typedArray.getResourceId(R.styleable.ClassLayout_cl_item_background_resource_checked, 0);
            mBackgroundResourceFocus = typedArray.getResourceId(R.styleable.ClassLayout_cl_item_background_resource_focus, 0);
            mTextColor = typedArray.getColor(R.styleable.ClassLayout_cl_item_text_color, Color.WHITE);
            mTextColorChecked = typedArray.getColor(R.styleable.ClassLayout_cl_item_text_color_checked, Color.RED);
            mTextColorFocus = typedArray.getColor(R.styleable.ClassLayout_cl_item_text_color_focus, Color.BLACK);
            mTextSize = typedArray.getDimensionPixelOffset(R.styleable.ClassLayout_cl_item_text_size, 20);
            mItemMargin = typedArray.getDimensionPixelOffset(R.styleable.ClassLayout_cl_item_margin, 0);
            mItemHeight = typedArray.getDimensionPixelOffset(R.styleable.ClassLayout_cl_item_height, 100);
        } catch (Exception e) {
        }
        if (null != typedArray) {
            typedArray.recycle();
        }
    }

    public abstract void setOrientation(RadioGroup radioGroup);
}
