package lib.kalu.leanback.clazz;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.leanback.R;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lib.kalu.leanback.util.LeanBackUtil;
import lib.kalu.leanback.util.ViewUtil;

public final class ClassScrollView extends ScrollView implements ClassLayoutImpl {

    @IntRange(from = 1, to = 2)
    int mOrientation = 1; // 默认horizontal
    @Dimension
    int mItemMargin;
    @Dimension
    int mItemHeight;
    @Dimension
    int mItemWidth;
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

    public ClassScrollView(Context context) {
        super(context);
        init(context, null);
    }

    public ClassScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClassScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ClassScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LeanBackUtil.log("BaseScrollView => onFinishInflate => mOrientation = " + mOrientation);
        // 1
        setFillViewport(true);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        // 2
        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setFocusable(false);
        radioGroup.setOrientation(mOrientation == 1 ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        radioGroup.setLayoutParams(layoutParams);
        removeAllViews();
        addView(radioGroup);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        LeanBackUtil.log("BaseScrollView => onFocusChanged => gainFocus = " + gainFocus);
        setCheckedRadioButton(gainFocus, false, false);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        View focus = findFocus();
        if (null != focus && focus instanceof ClassScrollView && mOrientation == 1){
            return dispatchEventHorizontal(event);
        } else if (null != focus && focus instanceof ClassScrollView && mOrientation == 2) {
            return dispatchEventVertical(event);
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    private boolean dispatchEventHorizontal(KeyEvent event) {
        // left
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            int itemCount = getItemCount();
            int checkedIndex = getCheckedIndex();
            if (itemCount > 0 && checkedIndex > 0) {
                int next = checkedIndex - 1;
                setCheckedRadioButton(next, true, true, true);
                scrollNext(View.FOCUS_LEFT, next);
                return true;
            }
        }
        // right
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            int itemCount = getItemCount();
            int checkedIndex = getCheckedIndex();
            if (itemCount > 0 && checkedIndex < itemCount) {
                int next = checkedIndex + 1;
                setCheckedRadioButton(next, true, true, true);
                scrollNext(View.FOCUS_RIGHT, next);
                return true;
            }
        }
        return checkNextFocus(event) || super.dispatchKeyEvent(event);
    }

    private boolean dispatchEventVertical(KeyEvent event) {
        // up
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            int itemCount = getItemCount();
            int checkedIndex = getCheckedIndex();
            if (itemCount > 0 && checkedIndex > 0) {
                int next = checkedIndex - 1;
                setCheckedRadioButton(next, true, true, true);
                scrollNext(View.FOCUS_UP, next);
                return true;
            }
        }
        // down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            int itemCount = getItemCount();
            int checkedIndex = getCheckedIndex();
            if (itemCount > 0 && checkedIndex < itemCount) {
                int next = checkedIndex + 1;
                setCheckedRadioButton(next, true, true, true);
                scrollNext(View.FOCUS_DOWN, next);
                return true;
            }
        }
        return checkNextFocus(event) || super.dispatchKeyEvent(event);
    }

    private boolean checkNextFocus(@NonNull KeyEvent event) {
        try {

            View nextFocus = null;
            boolean checkNext = false;

            // left
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                checkNext = true;
                nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_LEFT);
            }
            // right
            else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                checkNext = true;
                nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_RIGHT);
            }
            // up
            else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                checkNext = true;
                nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_UP);
            }
            // down
            else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                checkNext = true;
                nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_DOWN);
            }

            LeanBackUtil.log("BaseScrollView => checkNextFocus => checkNext = " + checkNext + ", nextFocus = " + nextFocus);

            if (checkNext && null == nextFocus) {
                return true;
            } else if (checkNext && nextFocus instanceof RecyclerView) {
                androidx.recyclerview.widget.RecyclerView.Adapter adapter = ((RecyclerView) nextFocus).getAdapter();
                LeanBackUtil.log("BaseScrollView => checkNextFocus => adapter = " + adapter);
                if (null == adapter) {
                    return true;
                } else {
                    int itemCount = adapter.getItemCount();
                    LeanBackUtil.log("BaseScrollView => checkNextFocus => itemCount = " + itemCount);
                    if (itemCount <= 0) {
                        return true;
                    }
                }
            }
            throw new Exception("check error");
        } catch (Exception e) {
            LeanBackUtil.log("BaseScrollView => checkNextFocus => " + e.getMessage());
            return false;
        }
    }

    private void init(@NonNull Context context, @NonNull AttributeSet attrs) {
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
            mItemWidth = typedArray.getDimensionPixelOffset(R.styleable.ClassLayout_cl_item_width, 100);
            mOrientation = typedArray.getInt(R.styleable.ClassLayout_cl_orientation, 1);
        } catch (Exception e) {
        }
        if (null != typedArray) {
            typedArray.recycle();
        }
    }

    public void update(@NonNull List<? extends ClassBean> data) {
        update(data, 0, mItemMargin, mItemWidth, mItemHeight, mTextSize, mOrientation, mTextColor, mTextColorFocus, mTextColorChecked, mBackgroundResource, mBackgroundResourceFocus, mBackgroundResourceChecked, true);
    }

    public void update(@NonNull List<? extends ClassBean> data, int checkedIndex) {
        update(data, checkedIndex, mItemMargin, mItemWidth, mItemHeight, mTextSize, mOrientation, mTextColor, mTextColorFocus, mTextColorChecked, mBackgroundResource, mBackgroundResourceFocus, mBackgroundResourceChecked, true);
    }
}
