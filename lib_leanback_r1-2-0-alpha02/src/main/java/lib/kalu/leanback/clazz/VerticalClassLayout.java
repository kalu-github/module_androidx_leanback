package lib.kalu.leanback.clazz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.leanback.R;

import java.util.List;

import lib.kalu.leanback.list.RecyclerView;
import lib.kalu.leanback.util.LeanBackUtil;
import lib.kalu.leanback.util.ViewUtil;

/**
 * 垂直
 */
public final class VerticalClassLayout extends ScrollView {

    boolean mDispatchTop = false;
    boolean mDispatchBottom = false;
    boolean mDispatchLeft = false;
    boolean mDispatchRight = false;
    @Dimension
    int mItemMargin;
    @Dimension
    int mItemHeight;
    @Dimension
    int mTextSize;
    @ColorInt
    int mBackgroundColor;
    @ColorInt
    int mBackgroundColorChecked;
    @ColorInt
    int mBackgroundColorHighlight;
    @DrawableRes
    int mBackgroundResource;
    @DrawableRes
    int mBackgroundResourceChecked;
    @DrawableRes
    int mBackgroundResourceHighlight;
    @ColorInt
    int mColor;
    @ColorInt
    int mColorChecked;
    @ColorInt
    int mColorHighlight;

    public VerticalClassLayout(Context context) {
        super(context);
        init(context, null);
    }

    public VerticalClassLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public VerticalClassLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VerticalClassLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        LbLogUtil.log("ClassLayout", "dispatchKeyEvent => action = " + event.getAction() + ", code = " + event.getKeyCode());
        // up
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            int count = getItemCount();
            if (count > 0) {
                int index = getCheckedIndex();
                if (index > 0) {
                    int next = index - 1;
                    setChecked(next);
                    updateBackground(true, false, next, true, false);
                    updateText(true);
                    return true;
                } else {
                    View focus = findFocus();
                    View nextFocus = ViewUtil.findNextFocus(getContext(), focus, View.FOCUS_UP);
                    if (null == nextFocus) return true;
                }
            }
            if (mDispatchTop) {
                return true;
            }
        }
        // down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            int count = getItemCount();
            if (count > 0) {
                int index = getCheckedIndex();
                if (index + 1 < count) {
                    int next = index + 1;
                    setChecked(next);
                    updateBackground(false, true, next, true, false);
                    updateText(true);
                    return true;
                } else {
                    View focus = findFocus();
                    View nextFocus = ViewUtil.findNextFocus(getContext(), focus, View.FOCUS_DOWN);
                    if (null == nextFocus) return true;
                }
            }
            if (mDispatchBottom) {
                return true;
            }
        }
        // right-in
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            View focus = findFocus();
            if (null != focus) {
                focusInto();
                return true;
            }
        }
        // right-out
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            View focus = findFocus();
            LeanBackUtil.log("VerticalClassLayout => dispatchKeyEvent => rightLeave => focus = " + focus);
            if (null != focus) {
                View nextFocus = ViewUtil.findNextFocus(getContext(), focus, View.FOCUS_RIGHT);
                LeanBackUtil.log("VerticalClassLayout => dispatchKeyEvent => rightLeave => nextFocus = " + nextFocus);
                if (null == nextFocus) {
                    return true;
                }
                // RecyclerView
                else if (nextFocus instanceof RecyclerView) {
                    try {
                        int count = ((RecyclerView) nextFocus).getAdapter().getItemCount();
                        LeanBackUtil.log("VerticalClassLayout => dispatchKeyEvent => rightLeave => count = " + count);
                        if (count <= 0) {
                            return true;
                        }
                    } catch (Exception e) {
                        return true;
                    }
                }
                // ViewGroup
                else if (nextFocus instanceof ViewGroup) {
                }
                // View
                else {
                    boolean hasFocusable = nextFocus.hasFocusable();
                    if (!hasFocusable) {
                        return true;
                    }
                }
                focusLeave();
            }
        }
        // left-in
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            View focus = findFocus();
            if (null != focus) {
                focusInto();
                return true;
            }
        }
        // left-leave
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            View focus = findFocus();
            LeanBackUtil.log("VerticalClassLayout => dispatchKeyEvent => leftLeave => focus = " + focus);
            if (null != focus) {
                View nextFocus = ViewUtil.findNextFocus(getContext(), focus, View.FOCUS_LEFT);
                LeanBackUtil.log("VerticalClassLayout => dispatchKeyEvent => leftLeave => nextFocus = " + nextFocus);
                if (null == nextFocus) {
                    return true;
                }
                focusLeave();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void init(@NonNull Context context, @NonNull AttributeSet attrs) {

        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClassLayout);
            mBackgroundResource = typedArray.getResourceId(R.styleable.ClassLayout_cl_item_background_resource, -1);
            mBackgroundResourceChecked = typedArray.getResourceId(R.styleable.ClassLayout_cl_item_background_resource_checked, -1);
            mBackgroundResourceHighlight = typedArray.getResourceId(R.styleable.ClassLayout_cl_item_background_resource_highlight, -1);
            mBackgroundColor = typedArray.getColor(R.styleable.ClassLayout_cl_item_background_color, Color.TRANSPARENT);
            mBackgroundColorChecked = typedArray.getColor(R.styleable.ClassLayout_cl_item_background_color_checked, Color.TRANSPARENT);
            mBackgroundColorHighlight = typedArray.getColor(R.styleable.ClassLayout_cl_item_background_color_highlight, Color.TRANSPARENT);
            mColor = typedArray.getColor(R.styleable.ClassLayout_cl_item_text_color, Color.WHITE);
            mColorChecked = typedArray.getColor(R.styleable.ClassLayout_cl_item_text_color_checked, Color.RED);
            mColorHighlight = typedArray.getColor(R.styleable.ClassLayout_cl_item_text_color_highlight, Color.BLACK);
            mTextSize = typedArray.getDimensionPixelOffset(R.styleable.ClassLayout_cl_item_text_size, 20);
            mItemMargin = typedArray.getDimensionPixelOffset(R.styleable.ClassLayout_cl_item_margin, 0);
            mItemHeight = typedArray.getDimensionPixelOffset(R.styleable.ClassLayout_cl_item_height, 100);
            mDispatchLeft = typedArray.getBoolean(R.styleable.ClassLayout_cl_dispatch_left, false);
            mDispatchRight = typedArray.getBoolean(R.styleable.ClassLayout_cl_dispatch_right, false);
            mDispatchTop = typedArray.getBoolean(R.styleable.ClassLayout_cl_dispatch_top, false);
            mDispatchBottom = typedArray.getBoolean(R.styleable.ClassLayout_cl_dispatch_bottom, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != typedArray) {
            typedArray.recycle();
        }

        // 1
        setFillViewport(true);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        // 2
        RadioGroup layout = new RadioGroup(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setFocusable(false);
        addView(layout);
    }

    private int getItemCount() {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new IllegalArgumentException("getCount => child num only one");
            RadioGroup radioGroup = (RadioGroup) getChildAt(0);
            int count = radioGroup.getChildCount();
            if (count <= 0) throw new IllegalArgumentException("getCount => child num is empty");
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getCheckedIndex() {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new IllegalArgumentException("getCheckedIndex => child num only one");
            int index = -1;
            RadioGroup radioGroup = (RadioGroup) getChildAt(0);
            int count = radioGroup.getChildCount();
            if (count <= 0)
                throw new IllegalArgumentException("getCheckedIndex => child num is empty");
            for (int i = 0; i < count; i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                boolean checked = radioButton.isChecked();
                if (checked) {
                    index = i;
                    break;
                }
            }
            return index;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void setChecked(@NonNull int index) {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new IllegalArgumentException("getCheckedIndex => child num only one");
            RadioGroup radioGroup = (RadioGroup) getChildAt(0);
            int count = radioGroup.getChildCount();
            if (count <= 0)
                throw new IllegalArgumentException("getCheckedIndex => child num is empty");
            for (int i = 0; i < count; i++) {
//                LbLogUtil.log("ClassLayout", "setChecked => i = " + i + ", index = " + index);
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setChecked(false);
            }
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(index);
            radioButton.setChecked(true);
            // listener
            call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBackground(@NonNull boolean up, @NonNull boolean down, @NonNull int index, boolean highlight, boolean checked) {

        int count = getChildCount();
        if (count != 1) return;

        try {
            RadioGroup radioGroup = (RadioGroup) getChildAt(0);
            int size = radioGroup.getChildCount();
            for (int i = 0; i < size; i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if (mBackgroundResource != -1) {
                    radioButton.setBackgroundResource(mBackgroundResource);
                } else {
                    radioButton.setBackgroundColor(mBackgroundColor);
                }
                radioButton.setTextColor(mColor);
            }

            @ColorInt int color = highlight ? mColorHighlight : (checked ? mColorChecked : mColor);
            @ColorInt int background = highlight ? mBackgroundColorHighlight : (checked ? mBackgroundColorChecked : mBackgroundColor);
            @DrawableRes int resource = highlight ? mBackgroundResourceHighlight : (checked ? mBackgroundResourceChecked : mBackgroundResource);
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(index);
            radioButton.setTextColor(color);
            if (resource != -1) {
                radioButton.setBackgroundResource(resource);
            } else {
                radioButton.setBackgroundColor(background);
            }

            // scroll
//        int top = radioButton.getTop();
//        scrollTo(0, top);
            int scrollY = getScrollY();
            int top = radioButton.getTop();
//        LbLogUtil.log("ClassLayout", "updateBackground => index = " + index + ", scrollY");
            setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            radioButton.requestFocus();
            radioButton.clearFocus();
            setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            if (scrollY > top) {
                scrollTo(0, top);
            }
        } catch (Exception e) {
        }
    }

    private void call() {
        if (null == mListener) return;
        int count = getChildCount();
        if (count != 1) return;
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        int size = radioGroup.getChildCount();
        for (int i = 0; i < size; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            boolean checked = radioButton.isChecked();
            if (checked) {
                CharSequence text = radioButton.getText();
                CharSequence hint = radioButton.getHint();
                mListener.onChecked(i, String.valueOf(text), String.valueOf(hint));
                break;
            }
        }
    }

    private void updateText(@NonNull boolean highlight) {

        int count = getChildCount();
        if (count != 1) return;

        try {
            RadioGroup radioGroup = (RadioGroup) getChildAt(0);
            int size = radioGroup.getChildCount();
            for (int i = 0; i < size; i++) {

                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                // 1=>normal
                // 2=>highlight
                // 3=>checked
                Object tag = radioButton.getTag();
                if (null == tag || !(tag instanceof ClassBean)) continue;

                boolean checked = radioButton.isChecked();

                CharSequence str;
                if (highlight && checked) {
                    str = ClassUtil.textHighlight(getContext(), (ClassBean) tag);
                } else if (checked) {
                    str = ClassUtil.textChecked(getContext(), (ClassBean) tag);
                } else {
                    str = ClassUtil.textNormal(getContext(), (ClassBean) tag);
                }
//            LbLogUtil.log("ClassLayout", "updateTextW => str = " + str);
                if (null != str && str.length() > 0) {
                    radioButton.setText(str);
                }
            }
        } catch (Exception e) {
        }
    }

    @Nullable
    public String getCheckedCode() {
        int count = getChildCount();
        if (count != 1) return null;
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        String code = null;
        int size = radioGroup.getChildCount();
        for (int i = 0; i < size; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            boolean checked = radioButton.isChecked();
            if (checked) {
                CharSequence hint = radioButton.getHint();
                code = String.valueOf(hint);
                break;
            }
        }
        return code;
    }

    @Nullable
    public String getCheckedName() {
        int count = getChildCount();
        if (count != 1) return null;
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        String name = null;
        int size = radioGroup.getChildCount();
        for (int i = 0; i < size; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            boolean checked = radioButton.isChecked();
            if (checked) {
                CharSequence text = radioButton.getText();
                name = String.valueOf(text);
                break;
            }
        }
        return name;
    }

    public void setText(@NonNull int index, @NonNull String str) {
        if (null == str || str.length() <= 0) return;
        if (index < 0) return;
        int count = getChildCount();
        if (count != 1) return;
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        int size = radioGroup.getChildCount();
        if (index + 1 > size) return;
        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(index);
        radioButton.setText(str);
    }

    public void update(@NonNull List<? extends ClassBean> list) {
        update(list, false);
    }

    public void update(@NonNull List<? extends ClassBean> list, boolean highlight) {

        if (null == list) return;
        int size = list.size();
        if (size <= 0) return;
        int count = getChildCount();
        if (count != 1) return;
        setFocusable(true);
        int index = 0;
        Context context = getContext();
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        for (int i = 0; i < size; i++) {
            ClassBean api = list.get(i);
            if (null == api) continue;
            if (api.isChecked()) {
                index = i;
            }
            ClassRadioButton view = new ClassRadioButton(context);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, mItemHeight);
            if (i > 0 && mItemMargin > 0) {
                params.topMargin = mItemMargin;
            }
            view.setLayoutParams(params);
            view.setChecked(false);
            view.setTag(api);
            view.setHint(api.getCode());
            if (mBackgroundResource != -1) {
                view.setBackgroundResource(mBackgroundResource);
            } else {
                view.setBackgroundColor(mBackgroundColor);
            }
            view.setTextColor(mColor);
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            radioGroup.addView(view);
        }

        // ui
        if (highlight) {
            highlight(index);
        } else {
            checked(index);
        }

        // listener
        call();
    }

    public void highlight(@NonNull int index) {
        if (index < 0) return;
        int count = getChildCount();
        if (count != 1) return;
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        int size = radioGroup.getChildCount();
        if (index + 1 > size) return;

        // reset
        for (int i = 0; i < size; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (null == radioButton) continue;
            if (mBackgroundResource != -1) {
                radioButton.setBackgroundResource(mBackgroundResource);
            } else {
                radioButton.setBackgroundColor(mBackgroundColor);
            }
            radioButton.setTextColor(mColor);
            radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        // background
        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(index);
        radioButton.setChecked(true);
        if (mBackgroundResource != -1) {
            radioButton.setBackgroundResource(mBackgroundResourceHighlight);
        } else {
            radioButton.setBackgroundColor(mBackgroundColorHighlight);
        }
        radioButton.setTextColor(mColorHighlight);

        // text
        updateText(true);
    }

    public void checked(@NonNull int index) {
        if (index < 0) return;
        int count = getChildCount();
        if (count != 1) return;
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        int size = radioGroup.getChildCount();
        if (index + 1 > size) return;

        // reset
        for (int i = 0; i < size; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (null == radioButton) continue;
            if (mBackgroundResource != -1) {
                radioButton.setBackgroundResource(mBackgroundResource);
            } else {
                radioButton.setBackgroundColor(mBackgroundColor);
            }
            radioButton.setTextColor(mColor);
            radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        // background
        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(index);
        radioButton.setChecked(true);
        if (mBackgroundResource != -1) {
            radioButton.setBackgroundResource(mBackgroundResourceChecked);
        } else {
            radioButton.setBackgroundColor(mBackgroundColorChecked);
        }
        radioButton.setTextColor(mColorChecked);

        // text
        updateText(false);
    }

    public void focusInto() {
        try {
            int index = getCheckedIndex();
            updateBackground(false, false, index, true, false);
            updateText(true);
        } catch (Exception e) {
        }
    }

    public void focusLeave() {
        try {
            int index = getCheckedIndex();
            updateBackground(false, false, index, false, true);
            updateText(false);
        } catch (Exception e) {
        }
    }


    public void requestFocus(boolean call) {
        int index = getCheckedIndex();
        requestFocus(index, call);
    }

    public void requestFocus(int index, boolean call) {
        RadioButton radioButton = getTab(index);
        if (null == radioButton) return;
        setFocusable(true);
        requestFocus();
        updateBackground(false, true, index, true, false);
        updateText(true);
        // listener
        if (call) {
            call();
        }
    }

    public RadioButton getTab(int index) {
        if (index < 0) return null;
        int count = getChildCount();
        if (count != 1) return null;
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        if (null == radioGroup) return null;
        int size = radioGroup.getChildCount();
        if (index + 1 > size) return null;
        return (RadioButton) radioGroup.getChildAt(index);
    }

    @SuppressLint("AppCompatCustomView")
    private final class ClassRadioButton extends RadioButton {

        public ClassRadioButton(Context context) {
            super(context);
            init();
        }

        public ClassRadioButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public ClassRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ClassRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            init();
        }

        private void init() {
            setSingleLine();
            setFocusable(false);
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            setGravity(Gravity.CENTER);
            setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    /*************/

    private OnCheckedChangeListener mListener;

    public final void setOnCheckedChangeListener(@NonNull OnCheckedChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnCheckedChangeListener {
        void onChecked(@NonNull int index, @NonNull String name, @NonNull String code);
    }

    /*************/
}
