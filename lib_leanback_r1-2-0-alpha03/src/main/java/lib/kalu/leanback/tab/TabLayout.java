package lib.kalu.leanback.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.leanback.R;

import org.json.JSONObject;

import java.util.List;

import lib.kalu.leanback.tab.listener.OnTabCheckedListener;
import lib.kalu.leanback.tab.model.TabModel;
import lib.kalu.leanback.util.LeanBackUtil;

/**
 * TabLayout for TV
 */
@SuppressLint("NewApi")
public class TabLayout extends HorizontalScrollView {

    private float mScale = 1f;
    private int mMargin = 0;
    private int mTextUnderlineColor = Color.TRANSPARENT;
    private int mTextUnderlineWidth = 0;
    private int mTextUnderlineHeight = 0;
    private int mTextSize = 10;
    private int mTextPadding = 0;
    private int mImageWidthMax = 0;
    private int mImageWidthMin = 0;
    private int mImageHeight = 0;
    private int mImagePadding = 0;

    public TabLayout(Context context) {
        super(context);
        init(null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

//        int repeatCount = event.getRepeatCount();
//        if (repeatCount > 0)
//            return true;
        //  LeanBackUtil.log("TabLayout => dispatchKeyEvent => action = " + event.getAction() + ", keyCode = " + event.getKeyCode());

//        // action_up => keycode_dpad_down
//        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
//
//            return true;
//        }
//        // action_up => keycode_dpad_up
//        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
//
//            return true;
//        }
//        // action_up => keycode_dpad_left
//        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
//            requestTab(View.FOCUS_LEFT);
//            return true;
//        }
//        // action_up => keycode_dpad_right
//        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
//            requestTab(View.FOCUS_RIGHT);
//            return true;
//        }
        // action_up => keycode_dpad_left
        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
//            LeanBackUtil.log("TabLayout => dispatchKeyEvent => action = ACTION_UP, keyCode = KEYCODE_DPAD_LEFT, repeat = " + event.getRepeatCount());
            callListenerChecked();
        }
        // action_down => keycode_dpad_left
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
//            LeanBackUtil.log("TabLayout => dispatchKeyEvent => action = ACTION_DOWN, keyCode = KEYCODE_DPAD_LEFT, repeat = " + event.getRepeatCount());
            try {
                int index = getCheckedIndex();
                if (index <= 0)
                    throw new Exception();
                int next = findNextPosition(View.FOCUS_LEFT, index);
                boolean scrollRequest = scrollRequest(View.FOCUS_LEFT, index, next, false);
                if (!scrollRequest)
                    throw new Exception();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        // action_up => keycode_dpad_right
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
//            LeanBackUtil.log("TabLayout => dispatchKeyEvent => action = ACTION_UP, keyCode = KEYCODE_DPAD_RIGHT, repeat = " + event.getRepeatCount());
            callListenerChecked();
        }
        // action_down => keycode_dpad_right
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
//            LeanBackUtil.log("TabLayout => dispatchKeyEvent => action = ACTION_DOWN, keyCode = KEYCODE_DPAD_RIGHT, repeat = " + event.getRepeatCount());
            try {
                int index = getCheckedIndex();
                int itemCount = getItemCount();
                if (index + 1 >= itemCount)
                    throw new Exception();
                int next = findNextPosition(View.FOCUS_RIGHT, index);
                boolean scrollRequest = scrollRequest(View.FOCUS_RIGHT, index, next, false);
                if (!scrollRequest)
                    throw new Exception();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        // action_down => keycode_dpad_up
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
        }
        // down action_down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        return super.onTouchEvent(ev);
        return false;
    }

    @Override
    public void clearChildFocus(View child) {
        super.clearChildFocus(child);
        // LeanBackUtil.log("TabLayout => clearChildFocus");
    }

    @Override
    public void clearFocus() {
        super.clearFocus();
        // LeanBackUtil.log("TabLayout => clearFocus");
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        //  LeanBackUtil.log("TabLayout => requestFocus => direction = " + direction);
        if (direction == FOCUS_DOWN) {
            focusedInto(View.FOCUS_DOWN);
        } else if (direction == FOCUS_UP) {
            focusedInto(View.FOCUS_UP);
        } else if (direction == FOCUS_LEFT) {
            focusedInto(View.FOCUS_LEFT);
        } else if (direction == FOCUS_RIGHT) {
            focusedInto(View.FOCUS_RIGHT);
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        //  LeanBackUtil.log("TabLayout => onFocusChanged => gainFocus = " + gainFocus + ", direction = " + direction);
        if (!gainFocus) {
            focusedOut(0x8888);
        }
    }

    private void init(@Nullable AttributeSet attrs) {
        // 1
        setFocusable(true);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        setSmoothScrollingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNestedScrollingEnabled(true);
        }
        setHorizontalScrollBarEnabled(false);
        setWillNotDraw(true);
        // 2
        addView(new TabLinearLayout(getContext()), 0);
        // 3
        TypedArray attributes = null;
        try {
            attributes = getContext().obtainStyledAttributes(attrs, R.styleable.TabLayout);
            mScale = attributes.getFloat(R.styleable.TabLayout_tab_scale, 1);
            mMargin = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tab_margin, 0);
            mTextPadding = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tab_text_padding, 0);
            mTextUnderlineColor = attributes.getColor(R.styleable.TabLayout_tab_text_underline_color, Color.TRANSPARENT);
            mTextUnderlineWidth = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tab_text_underline_width, 0);
            mTextUnderlineHeight = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tab_text_underline_height, 0);
            mTextSize = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tab_text_size, 10);
            mImageWidthMin = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tab_image_width_min, 0);
            mImageWidthMax = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tab_image_width_max, 0);
            mImageHeight = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tab_image_height, 0);
            mImagePadding = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tab_image_padding, 0);
        } catch (Exception e) {
        }

        if (null != attributes) {
            attributes.recycle();
        }
    }

    public final <T extends TabModel> void update(@NonNull List<T> list) {
        update(list, 0);
    }

    public final <T extends TabModel> void update(@NonNull List<T> list, int position) {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("error: childCount != 1");
            // 1
            addItems(list);
            // 2
            scrollRequest(0x9999, position, position, true);
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => update => " + e.getMessage());
        }
    }


    public final int getCheckedIndex() {
        try {
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            return ((TabLinearLayout) getChildAt(0)).getCheckedIndex();
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => getCheckedIndex => " + e.getMessage());
            return -1;
        }
    }

    public final JSONObject getCheckedItemJsonObject() {
        try {
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            return ((TabLinearLayout) getChildAt(0)).getCheckedItemJsonObject();
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => getCheckedItemJsonObject => " + e.getMessage());
            return null;
        }
    }

    public final int getCheckedItemId() {
        try {
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            return ((TabLinearLayout) getChildAt(0)).getCheckedItemId();
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => getCheckedItemId => " + e.getMessage());
            return Integer.MIN_VALUE;
        }
    }

    private boolean focusedOut(int direction) {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("childCount is not 1");

            int index = getCheckedIndex();
            ((TabLinearLayout) getChildAt(0)).checkedItem(index);

            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => focusedOut => " + e.getMessage());
            return false;
        }
    }

    private boolean focusedInto(int direction) {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("childCount is not 1");

            int index = getCheckedIndex();
            ((TabLinearLayout) getChildAt(0)).focusedItem(index);
            callListenerChecked();
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => focusedInto => " + e.getMessage());
            return false;
        }
    }

    public final int getItemCount() {
        try {
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            return ((TabLinearLayout) getChildAt(0)).getChildCount();
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => getItemCount => " + e.getMessage());
            return -1;
        }
    }


    private int findNextPosition(int direction, int position) {
        try {
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            int next = -1;
            if (direction == View.FOCUS_LEFT) {
                if (position > 0) {
                    next = position - 1;
                }
            } else if (direction == View.FOCUS_RIGHT) {
                int itemCount = getItemCount();
                if (position + 1 <= itemCount) {
                    next = position + 1;
                }
            } else if (direction == -1) {
                next = 0;
            }
            if (next == -1) throw new Exception("next is -1");
            return next;
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => findNextPosition => " + e.getMessage());
            return -1;
        }
    }

    private boolean scrollRequest(int direction, int position, int next, boolean init) {
        try {
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");

            if (direction == View.FOCUS_RIGHT) {
                int itemRight = ((TabLinearLayout) getChildAt(0)).getItemRight(next);
                int scrollX = getScrollX();
                int width = getWidth() - getPaddingLeft() - getPaddingRight();

                // 不可见/部分不可见
                if (itemRight - scrollX > width) {
                    int x = itemRight - scrollX - width;
                    scrollBy(x, 0);
                }

                if (position != next) {
                    ((TabLinearLayout) getChildAt(0)).clearItem(position);
                }
                ((TabLinearLayout) getChildAt(0)).focusedItem(next);
            } else if (direction == View.FOCUS_LEFT) {

                int scrollX = getScrollX();
                int itemLeft = ((TabLinearLayout) getChildAt(0)).getItemLeft(next);

                if (itemLeft < scrollX) {
                    scrollTo(itemLeft, 0);
                }

                if (position != next) {
                    ((TabLinearLayout) getChildAt(0)).clearItem(position);
                }
                ((TabLinearLayout) getChildAt(0)).focusedItem(next);
            } else if (direction == 0x9999) {
                if (position != next) {
                    ((TabLinearLayout) getChildAt(0)).clearItem(position);
                }
                if (init) {
                    ((TabLinearLayout) getChildAt(0)).checkedItem(next);
                } else {
                    ((TabLinearLayout) getChildAt(0)).focusedItem(next);
                }
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => scrollRequest => " + e.getMessage());
            return false;
        }
    }

    public final void startAnim(boolean hasFocus) {
        setScaleX(hasFocus ? mScale : 1f);
        setScaleY(hasFocus ? mScale : 1f);
    }

    public final boolean scrollLeft() {
        try {
            int itemCount = getItemCount();
            if (itemCount <= 0)
                throw new Exception("itemCount is :" + itemCount);
            int index = getCheckedIndex();
            if (index <= 0)
                throw new Exception("index is :" + index);
            int next = index - 1;
            boolean scrollRequest = scrollRequest(View.FOCUS_LEFT, index, next, false);
            callListenerChecked();
            return scrollRequest;
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => scrollLeft => " + e.getMessage());
            return false;
        }
    }


    public final boolean scrollRight() {
        try {
            int itemCount = getItemCount();
            if (itemCount <= 0)
                throw new Exception("itemCount error: " + itemCount);
            int index = getCheckedIndex();
            if (index < 0 || index + 1 > itemCount)
                throw new Exception("index error: " + index);
            int next = index + 1;
            boolean scrollRequest = scrollRequest(View.FOCUS_RIGHT, index, next, false);
            callListenerChecked();
            return scrollRequest;
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => scrollRight => " + e.getMessage());
            return false;
        }
    }

    public final boolean scrollToIndex(int newCheckedIndex) {
        try {
            int itemCount = getItemCount();
            if (itemCount <= 0)
                throw new Exception("itemCount error: " + itemCount);
            if (newCheckedIndex < 0 || newCheckedIndex + 1 > itemCount)
                throw new Exception("error: newCheckedIndex < 0 || newCheckedIndex + 1 > itemCount");
            int checkedIndex = getCheckedIndex();
            if (newCheckedIndex == checkedIndex)
                throw new Exception("error: newCheckedIndex == checkedIndex");
            boolean scrollRequest = scrollRequest(0x9999, checkedIndex, newCheckedIndex, false);
            callListenerChecked();
            return scrollRequest;
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => scrollToIndex => " + e.getMessage());
            return false;
        }
    }

    private void removeAllItem() {
        try {
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            ((TabLinearLayout) getChildAt(0)).removeAllViews();
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => removeAllItem => " + e.getMessage());
        }
    }

    private <T extends TabModel> void addItems(@NonNull List<T> list) {
        try {
            if (null == list || list.size() <= 0)
                throw new Exception("list is null");
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("childCount is not 1");
            removeAllItem();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                T t = list.get(i);
                if (null == t)
                    continue;
                // 图片
                if (t.isImg()) {
                    TabImageView view = new TabImageView(getContext(), t);
                    view.setTag(R.id.tab_item_json_object, t.getJsonObject());
                    view.setWidthMin(mImageWidthMin);
                    view.setWidthMax(mImageWidthMax);
                    view.setHeight(mImageHeight);
                    view.setPadding(mImagePadding, 0, mImagePadding, 0);
                    int height = getHeight() - getPaddingTop() - getPaddingBottom();
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, height);
                    if (i + 1 != size) {
                        layoutParams.rightMargin = mMargin;
                    }
                    view.setLayoutParams(layoutParams);
                    ((TabLinearLayout) getChildAt(0)).addView(view);
                }
                // 文字
                else {
                    TabTextView view = new TabTextView(getContext(), t);
                    view.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
                    view.setUnderlineColor(mTextUnderlineColor);
                    view.setUnderlineWidth(mTextUnderlineWidth);
                    view.setUnderlineHeight(mTextUnderlineHeight);
                    view.setPadding(mTextPadding, 0, mTextPadding, 0);
                    int height = getHeight() - getPaddingTop() - getPaddingBottom();
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, height);
                    if (i + 1 != size) {
                        layoutParams.rightMargin = mMargin;
                    }
                    view.setLayoutParams(layoutParams);
                    ((TabLinearLayout) getChildAt(0)).addView(view);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => addItems => " + e.getMessage());
        }
    }

    private void callListenerChecked() {
        try {
            if (null != mOnTabCheckedListener) {
                Object tag = getTag(R.id.tab_flag);
                int checkedIndex = getCheckedIndex();
                if (null == tag) {
                    setTag(R.id.tab_flag, checkedIndex);
                    mOnTabCheckedListener.onChecked(checkedIndex);
                } else if ((int) tag != checkedIndex) {
                    setTag(R.id.tab_flag, checkedIndex);
                    mOnTabCheckedListener.onChecked(checkedIndex);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => callListenerChecked => Exception => " + e.getMessage());
        }
    }

    /********/

    public OnTabCheckedListener mOnTabCheckedListener;

    public final void setOnTabCheckedListener(@NonNull OnTabCheckedListener listener) {
        this.mOnTabCheckedListener = listener;
    }

    /********/
}