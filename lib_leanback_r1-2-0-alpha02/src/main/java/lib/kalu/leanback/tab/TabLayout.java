package lib.kalu.leanback.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.leanback.R;

import java.util.List;

import lib.kalu.leanback.tab.listener.OnTabChangeListener;
import lib.kalu.leanback.tab.model.TabModel;
import lib.kalu.leanback.util.LeanBackUtil;

/**
 * TabLayout for TV
 */
public final class TabLayout extends HorizontalScrollView {

    private float mScale = 1f;
    private int mMargin = 0;
    private int mBackgroundColorsRadius = 0;

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

        int repeatCount = event.getRepeatCount();
        if (repeatCount > 0)
            return true;
//        LeanBackUtil.log("TabLayout => dispatchKeyEvent => action = " + event.getAction() + ", keyCode = " + event.getKeyCode());

        // action_up => keycode_dpad_down
        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            View focus = findFocus();
            if (null != focus && focus instanceof TabLayout) {
                requestTab(View.FOCUS_DOWN);
            }
            return true;
        }
        // action_up => keycode_dpad_up
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            View focus = findFocus();
            if (null != focus && focus instanceof TabLayout) {
                requestTab(View.FOCUS_UP);
            }
            return true;
        }
        // action_up => keycode_dpad_left
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            View focus = findFocus();
            if (null != focus && focus instanceof TabLayout) {
                requestTab(View.FOCUS_LEFT);
            }
            return true;
        }
        // action_up => keycode_dpad_right
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            View focus = findFocus();
            if (null != focus && focus instanceof TabLayout) {
                requestTab(View.FOCUS_RIGHT);
            }
            return true;
        }
        // action_down => keycode_dpad_left
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            try {
                int index = getCheckedIndex();
                if (index <= 0)
                    throw new Exception();
                int next = findNextPosition(View.FOCUS_LEFT, index);
                boolean scrollRequest = scrollRequest(View.FOCUS_LEFT, index, next);
                if (!scrollRequest)
                    throw new Exception();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        // action_down => keycode_dpad_right
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            try {
                int index = getCheckedIndex();
                int itemCount = getItemCount();
                if (index + 1 >= itemCount)
                    throw new Exception();
                int next = findNextPosition(View.FOCUS_RIGHT, index);
                boolean scrollRequest = scrollRequest(View.FOCUS_RIGHT, index, next);
                if (!scrollRequest)
                    throw new Exception();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        // action_down => keycode_dpad_up
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
//            View nextFocus = findNextFocus(View.FOCUS_UP);
//            if (null == nextFocus) {
//                return true;
//            } else {
//                checkedCurrentItem(View.FOCUS_UP);
//            }
        }
        // down action_down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
//            View nextFocus = findNextFocus(View.FOCUS_DOWN);
//            if (null == nextFocus) {
//                return true;
//            } else {
//                try {
//                    if (nextFocus instanceof androidx.recyclerview.widget.RecyclerView) {
//                        int itemCount = ((RecyclerView) nextFocus).getAdapter().getItemCount();
//                        if (itemCount <= 0) {
//                            return true;
//                        } else {
//                            throw new Exception();
//                        }
//                    }
//                    throw new Exception();
//                } catch (Exception e) {
//                    checkedCurrentItem(View.FOCUS_DOWN);
//                }
//            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        return super.onTouchEvent(ev);
        return false;
    }

    @Override
    public void setBackground(Drawable background) {
    }

    private void init(@Nullable AttributeSet attrs) {
        setFocusable(true);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        super.setBackground(new ColorDrawable(Color.TRANSPARENT));
        setSmoothScrollingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNestedScrollingEnabled(true);
        }
        setHorizontalScrollBarEnabled(false);
        setWillNotDraw(true);
        // 2
        TabLinearLayout root = new TabLinearLayout(getContext());
        addView(root, 0);
        TypedArray attributes = null;
        try {
            attributes = getContext().obtainStyledAttributes(attrs, R.styleable.TabLayout);
            mScale = attributes.getFloat(R.styleable.TabLayout_tab_scale, 1);
            mMargin = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tab_margin, 0);
            mBackgroundColorsRadius = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tab_background_colors_radius, 0);
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

    @Keep
    public <T extends TabModel> void update(@NonNull List<T> list) {
        update(list, 0);
    }

    @Keep
    public <T extends TabModel> void update(@NonNull List<T> list, int position) {
        try {
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            // 1
            addAllItem(list);
            // 2
            scrollRequest(0x9999, position, position);
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => update => " + e.getMessage());
        }
    }

    @Keep
    public int getCheckedIndex() {
        try {
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            return ((TabLinearLayout) getChildAt(0)).getCheckedIndex();
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => getCheckedIndex => " + e.getMessage());
            return -1;
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        LeanBackUtil.log("TabLayout => onFocusChanged => gainFocus = " + gainFocus + ", direction = " + direction);
        if (gainFocus) {
            requestTab(0x8888);
        } else {
            checkedTab(0x8888);
        }
    }

    @Keep
    private boolean requestTab(int direction) {
        try {
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            int index = getCheckedIndex();
            boolean requestChild = ((TabLinearLayout) getChildAt(0)).requestChild(index);
            if (!requestChild)
                throw new Exception("requestChild warning: false");
            if (null == mListener)
                throw new Exception("mListener warning: null");
            if (direction == View.FOCUS_UP) {
                mListener.onRepeatUp(index);
            } else if (direction == View.FOCUS_DOWN) {
                mListener.onRepeatDown(index);
            } else if (direction == View.FOCUS_LEFT) {
                mListener.onRepeatLeft(index);
            } else if (direction == View.FOCUS_RIGHT) {
                mListener.onRepeatRight(index);
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => requestTab => " + e.getMessage());
            return false;
        }
    }

    @Keep
    private boolean checkedTab(int direction) {
        try {
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            int index = getCheckedIndex();
            boolean checkChild = ((TabLinearLayout) getChildAt(0)).checkChild(index);
            if (!checkChild)
                throw new Exception("checkChild warning: false");
            if (null == mListener)
                throw new Exception("mListener warning: null");
            if (direction == View.FOCUS_UP) {
                mListener.onLeaveUp(index);
            } else if (direction == View.FOCUS_DOWN) {
                mListener.onLeaveDown(index);
            } else if (direction == View.FOCUS_LEFT) {
                mListener.onLeaveLeft(index);
            } else if (direction == View.FOCUS_RIGHT) {
                mListener.onLeaveRight(index);
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => checkedTab => " + e.getMessage());
            return false;
        }
    }

    @Keep
    public int getItemCount() {
        try {
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            return ((TabLinearLayout) getChildAt(0)).getChildCount();
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => getItemCount => " + e.getMessage());
            return -1;
        }
    }

//    private ViewGroup findDecorView(View view) {
//        try {
//            View parent = (View) view.getParent();
//            if (null == parent) {
//                return (ViewGroup) view;
//            } else {
//                return findDecorView(parent);
//            }
//        } catch (Exception e) {
//            LeanBackUtil.log("TabLayout => findDecorView => " + e.getMessage());
//            return (ViewGroup) view;
//        }
//    }

//    @Keep
//    public View findNextFocus(int direction) {
//        View nextFocus = null;
//        if (direction == View.FOCUS_LEFT) {
//            nextFocus = FocusFinder.getInstance().findNextFocus(findDecorView(this), this, View.FOCUS_LEFT);
//        } else if (direction == View.FOCUS_RIGHT) {
//            nextFocus = FocusFinder.getInstance().findNextFocus(findDecorView(this), this, View.FOCUS_RIGHT);
//        } else if (direction == View.FOCUS_UP) {
//            nextFocus = FocusFinder.getInstance().findNextFocus(findDecorView(this), this, View.FOCUS_UP);
//        } else if (direction == View.FOCUS_DOWN) {
//            nextFocus = FocusFinder.getInstance().findNextFocus(findDecorView(this), this, View.FOCUS_DOWN);
//        }
//        if (null != nextFocus && nextFocus instanceof TagsLayout) {
//            nextFocus = null;
//        }
//        return nextFocus;
//    }

    @Keep
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

    @Keep
    private boolean scrollRequest(int direction, int position, int next) {
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
                    ((TabLinearLayout) getChildAt(0)).resetChild(position);
                }
                ((TabLinearLayout) getChildAt(0)).requestChild(next);
                if (null != mListener) {
                    mListener.onChecked(next, position);
                }

            } else if (direction == View.FOCUS_LEFT) {

                int scrollX = getScrollX();
                int itemLeft = ((TabLinearLayout) getChildAt(0)).getItemLeft(next);

                if (itemLeft < scrollX) {
                    scrollTo(itemLeft, 0);
                }

                if (position != next) {
                    ((TabLinearLayout) getChildAt(0)).resetChild(position);
                }
                ((TabLinearLayout) getChildAt(0)).requestChild(next);
                if (null != mListener) {
                    mListener.onChecked(next, position);
                }
            } else if (direction == 0x9999) {
                if (position != next) {
                    ((TabLinearLayout) getChildAt(0)).resetChild(position);
                }
                ((TabLinearLayout) getChildAt(0)).requestChild(next);
                if (null != mListener) {
                    mListener.onChecked(next, position);
                }
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => scrollRequest => " + e.getMessage());
            return false;
        }
    }

    @Keep
    private int getContainerWidth() {
        try {
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            return ((TabLinearLayout) getChildAt(0)).getWidth();
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => getContainerWidth => " + e.getMessage());
            return 0;
        }
    }

    @Keep
    public void startAnim(boolean over) {
        if (mScale <= 1f) return;
        ViewCompat.animate(this).scaleX(over ? 1f : mScale).scaleY(over ? 1f : mScale).start();
    }

    @Keep
    public boolean scrollLeft() {
        try {
            int itemCount = getItemCount();
            if (itemCount <= 0)
                throw new Exception("itemCount is :" + itemCount);
            int index = getCheckedIndex();
            if (index <= 0)
                throw new Exception("index is :" + index);
            int next = index - 1;
            return scrollRequest(View.FOCUS_LEFT, index, next);
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => scrollLeft => " + e.getMessage());
            return false;
        }
    }

    @Keep
    public boolean scrollRight() {
        try {
            int itemCount = getItemCount();
            if (itemCount <= 0)
                throw new Exception("itemCount error: " + itemCount);
            int index = getCheckedIndex();
            if (index < 0 || index + 1 > itemCount)
                throw new Exception("index error: " + index);
            int next = index + 1;
            return scrollRequest(View.FOCUS_RIGHT, index, next);
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => scrollRight => " + e.getMessage());
            return false;
        }
    }


    @Keep
    public boolean checkedPosition(int position) {
        return checkedPosition(position, false);
    }

    @Keep
    public boolean checkedPosition(int position, boolean scrollTop) {
        try {
            int itemCount = getItemCount();
            if (itemCount <= 0)
                throw new Exception("itemCount error: " + itemCount);
            if (position < 0 || position + 1 > itemCount)
                throw new Exception("position error:" + position);
            if (scrollTop) {
                scrollTo(0, 0);
            }
            int index = getCheckedIndex();
            return scrollRequest(0x9999, index, position);
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => checkedPosition => " + e.getMessage());
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

    private <T extends TabModel> void addAllItem(@NonNull List<T> list) {
        try {
            if (null == list || list.size() <= 0) throw new Exception("list is null");
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            removeAllItem();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                T t = list.get(i);
                if (null == t) continue;
                if (t.isImg()) {
                    addImage(t, i, size);
                } else if (t.isTxt()) {
                    addText(t, i, size);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => addAllItem => " + e.getMessage());
        }
    }

    private <T extends TabModel> void addText(@NonNull T t, int index, int count) {
        try {
            if (null == t) throw new Exception("t error: " + t);
            String text = t.getText();
            if (null == text || text.length() == 0) throw new Exception("text error: " + text);
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            // 1
            TabTextView view = new TabTextView(getContext(), t);
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            view.setUnderlineColor(mTextUnderlineColor);
            view.setUnderlineWidth(mTextUnderlineWidth);
            view.setUnderlineHeight(mTextUnderlineHeight);
            view.setPadding(mTextPadding, 0, mTextPadding, 0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, getHeight());
            if (index + 1 != count) {
                layoutParams.rightMargin = mMargin;
            }
            view.setLayoutParams(layoutParams);
            // 2
            addItem(view);
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => addText => " + e.getMessage());
        }
    }

    private <T extends TabModel> void addImage(@NonNull T t, int index, int count) {

        try {
            if (null == t) throw new Exception("t error: " + t);
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            // 1
            TabImageView view = new TabImageView(getContext(), t);
            view.setWidthMin(mImageWidthMin);
            view.setWidthMax(mImageWidthMax);
            view.setHeight(mImageHeight);
            view.setPadding(mImagePadding, 0, mImagePadding, 0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, getHeight());
            if (index + 1 != count) {
                layoutParams.rightMargin = mMargin;
            }
            view.setLayoutParams(layoutParams);
            // 2
            addItem(view);
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => addImage => " + e.getMessage());
        }
    }

    private void addItem(@NonNull View view) {
        try {
            if (null == view) throw new Exception("view error: " + view);
            int childCount = getChildCount();
            if (childCount != 1) throw new Exception("childCount is not 1");
            ((TabLinearLayout) getChildAt(0)).addView(view);
        } catch (Exception e) {
            LeanBackUtil.log("TabLayout => addItem => " + e.getMessage());
        }
    }

    /************************************/

    public OnTabChangeListener mListener;

    public void setOnTabChangeListener(@NonNull OnTabChangeListener listener) {
        this.mListener = listener;
    }

    /********/
}