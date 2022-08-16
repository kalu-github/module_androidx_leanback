package lib.kalu.leanback.clazz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
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

/**
 * 垂直
 */
public final class ClassLayout extends ScrollView {

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

    public ClassLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ClassLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClassLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ClassLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("ClassLayout", "dispatchKeyEvent => action = " + event.getAction() + ", code = " + event.getKeyCode());
        // up
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            int count = getCount();
            int index = getCheckedIndex();
//            Log.e("ClassLayout", "up => count = " + count + ", index = " + index);
            if (count > 0 && index == 0) {
                if (mDispatchTop) {
                    return true;
                } else {
                    updateBackground(index, false, true);
                    updateText(false);
                    return false;
                }
            } else if (count > 0 && index > 0) {
                int next = index - 1;
                setChecked(next);
                updateBackground(next, true, false);
                updateText(true);
                return true;
            } else {
                return false;
            }
        }
        // down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            int count = getCount();
            int index = getCheckedIndex();
//            Log.e("ClassLayout", "down => count = " + count + ", index = " + index);
            if (count > 0 && index + 1 == count) {
                if (mDispatchBottom) {
                    return true;
                } else {
                    updateBackground(index, false, true);
                    updateText(false);
                    return false;
                }
            } else if (count > 0 && index >= 0) {
                int next = index + 1;
                setChecked(next);
                updateBackground(next, true, false);
                updateText(true);
                return true;
            } else {
                return false;
            }
        }
        // right
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            int index = getCheckedIndex();
            updateBackground(index, false, true);
            updateText(false);
            return false;
        }
        // left
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            int index = getCheckedIndex();
            updateBackground(index, true, false);
            updateText(true);
            return false;
        }
        // pass
        else {
            return false;
        }
    }

    private final void init(@NonNull Context context, @NonNull AttributeSet attrs) {

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
        // blocksDescendants：ViewGroup拦截，不让子 view获取焦点。
        // beforeDescendants：ViewGroup优先尝试（尝试的意思是，根据View或ViewGroup当前状态来判断是否能得到焦点，如是否可见，是否可获取焦点等等，在View的requestFocus方法的注释中提到，下同）获取焦点，若ViewGroup没拿到焦点，再遍历子 view（包括所有直接子 view和间接子 view），让子 view尝试获取焦点。
        // afterDescendants：先遍历子 view，让子 view尝试获取焦点，若所有子 view（包括所有直接子 view和间接子 view）都没拿到焦点，才让ViewGroup尝试获取焦点。
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        setClickable(false);
        setLongClickable(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setFillViewport(true);
        // 2
        RadioGroup layout = new RadioGroup(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setClickable(false);
        layout.setLongClickable(false);
        layout.setFocusable(false);
        layout.setFocusableInTouchMode(false);
        addView(layout);
    }

    private final int getCount() {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new IllegalArgumentException("getCount => child num only one");
            RadioGroup radioGroup = (RadioGroup) getChildAt(0);
            int count = radioGroup.getChildCount();
            if (count <= 0)
                throw new IllegalArgumentException("getCount => child num is empty");
            return radioGroup.getChildCount();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public final int getItemCount() {
        try {
            RadioGroup radioGroup = (RadioGroup) getChildAt(0);
            return radioGroup.getChildCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public final int getCheckedIndex() {
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

    private final void setChecked(@NonNull int index) {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new IllegalArgumentException("getCheckedIndex => child num only one");
            RadioGroup radioGroup = (RadioGroup) getChildAt(0);
            int count = radioGroup.getChildCount();
            if (count <= 0)
                throw new IllegalArgumentException("getCheckedIndex => child num is empty");
            for (int i = 0; i < count; i++) {
//                Log.e("ClassLayout", "setChecked => i = " + i + ", index = " + index);
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

    private final void updateBackground(@NonNull int index, boolean highlight, boolean checked) {

        int count = getChildCount();
        if (count != 1)
            return;

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

        @ColorInt
        int color = highlight ? mColorHighlight : (checked ? mColorChecked : mColor);
        @ColorInt
        int background = highlight ? mBackgroundColorHighlight : (checked ? mBackgroundColorChecked : mBackgroundColor);
        @DrawableRes
        int resource = highlight ? mBackgroundResourceHighlight : (checked ? mBackgroundResourceChecked : mBackgroundResource);
        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(index);
        radioButton.setTextColor(color);
        if (resource != -1) {
            radioButton.setBackgroundResource(resource);
        } else {
            radioButton.setBackgroundColor(background);
        }
    }

    private final void call() {
        if (null == mListener)
            return;
        int count = getChildCount();
        if (count != 1)
            return;
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

    private final void updateText(@NonNull boolean highlight) {

        int count = getChildCount();
        if (count != 1)
            return;

        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        int size = radioGroup.getChildCount();
        for (int i = 0; i < size; i++) {

            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            // 1=>normal
            // 2=>highlight
            // 3=>checked
            Object tag = radioButton.getTag();
            if (null == tag || !(tag instanceof ClassApi))
                continue;

            boolean checked = radioButton.isChecked();

            CharSequence str;
            if (highlight && checked) {
                str = ((ClassApi) tag).textHighlight(getContext());
            } else if (checked) {
                str = ((ClassApi) tag).textChecked(getContext());
            } else {
                str = ((ClassApi) tag).textNormal(getContext());
            }
//            Log.e("ClassLayout", "updateTextW => str = " + str);
            if (null != str && str.length() > 0) {
                radioButton.setText(str);
            }
        }
    }

    @Nullable
    public final String getCheckedCode() {
        int count = getChildCount();
        if (count != 1)
            return null;
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
    public final String getCheckedName() {
        int count = getChildCount();
        if (count != 1)
            return null;
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
        if (null == str || str.length() <= 0)
            return;
        if (index < 0)
            return;
        int count = getChildCount();
        if (count != 1)
            return;
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        int size = radioGroup.getChildCount();
        if (index + 1 > size)
            return;
        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(index);
        radioButton.setText(str);
    }

    public void update(@NonNull List<? extends ClassApi> list) {
        update(list, false);
    }

    public void update(@NonNull List<? extends ClassApi> list, boolean highlight) {

        if (null == list)
            return;
        int size = list.size();
        if (size <= 0)
            return;
        int count = getChildCount();
        if (count != 1)
            return;

        int index = 0;
        Context context = getContext();
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        for (int i = 0; i < size; i++) {
            ClassApi api = list.get(i);
            if (null == api)
                continue;
            if (api.checked()) {
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
            view.setHint(api.code());
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
        if (index < 0)
            return;
        int count = getChildCount();
        if (count != 1)
            return;
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        int size = radioGroup.getChildCount();
        if (index + 1 >= size)
            return;

        // reset
        for (int i = 0; i < size; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (null == radioButton)
                continue;
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
        if (index < 0)
            return;
        int count = getChildCount();
        if (count != 1)
            return;
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        int size = radioGroup.getChildCount();
        if (index + 1 >= size)
            return;

        // reset
        for (int i = 0; i < size; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (null == radioButton)
                continue;
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
            setClickable(false);
            setLongClickable(false);
            setFocusable(false);
            setFocusableInTouchMode(false);
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
