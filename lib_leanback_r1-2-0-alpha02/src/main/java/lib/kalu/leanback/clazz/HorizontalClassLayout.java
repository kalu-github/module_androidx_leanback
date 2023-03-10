package lib.kalu.leanback.clazz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
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
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.leanback.R;

import java.util.List;

/**
 * 水平
 */
public final class HorizontalClassLayout extends ScrollView {

    boolean mDispatchTop = false;
    boolean mDispatchBottom = false;
    boolean mDispatchLeft = false;
    boolean mDispatchRight = false;
    @Dimension
    int mItemMargin;
    @Dimension
    int mItemWidth;
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

    public HorizontalClassLayout(Context context) {
        super(context);
        init(context, null);
    }

    public HorizontalClassLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HorizontalClassLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HorizontalClassLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // LbLogUtil.log("HorizontalClassLayout", "dispatchKeyEvent => action = " + event.getAction() + ", code = " + event.getKeyCode());

        boolean hasFocus = hasFocus();
        if (!hasFocus) return false;

        // move => left
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            int count = getCount();
            int index = getCheckedIndex();
//            LbLogUtil.log("HorizontalClassLayout", "left => count = " + count + ", index = " + index);
            if (count > 0 && index == 0) {
                if (mDispatchLeft) {
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
        // move => right
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            int count = getCount();
            int index = getCheckedIndex();
//            LbLogUtil.log("HorizontalClassLayout", "down => count = " + count + ", index = " + index);
            if (count > 0 && index + 1 == count) {
                if (mDispatchRight) {
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
        // leave => up
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            if (mDispatchTop) {
                return true;
            } else {
                int index = getCheckedIndex();
                updateBackground(index, false, true);
                updateText(false);
                return false;
            }
        }
        // leave => down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (mDispatchBottom) {
                return true;
            } else {
                int index = getCheckedIndex();
                updateBackground(index, false, true);
                updateText(false);
                return false;
            }
        }
        // into => up
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            int count = getCount();
            if (count > 0) {
                int index = getCheckedIndex();
                updateBackground(index, true, false);
                updateText(true);
                return true;
            } else {
                return false;
            }
        }
        // into => down
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            int count = getCount();
            if (count > 0) {
                int index = getCheckedIndex();
                updateBackground(index, true, false);
                updateText(true);
                return true;
            } else {
                return false;
            }
        }
        // into => right
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            int count = getCount();
            if (count > 0) {
                int index = getCheckedIndex();
                updateBackground(index, true, false);
                updateText(true);
                return true;
            } else {
                return false;
            }
        }
        // into => left
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            int count = getCount();
            if (count > 0) {
                int index = getCheckedIndex();
                updateBackground(index, true, false);
                updateText(true);
                return true;
            } else {
                return false;
            }
        }
        // pass
        else {
            return false;
        }
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
            mItemWidth = typedArray.getDimensionPixelOffset(R.styleable.ClassLayout_cl_item_width, 100);
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
//        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        setFillViewport(true);
        // 2
        RadioGroup layout = new RadioGroup(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setFocusable(false);
        addView(layout);
    }

    private int getCount() {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new IllegalArgumentException("getCount => child num only one");
            RadioGroup radioGroup = (RadioGroup) getChildAt(0);
            if (null == radioGroup)
                throw new IllegalArgumentException("getCount => radioGroup null");
            int count = radioGroup.getChildCount();
            if (count <= 0) throw new IllegalArgumentException("getCount => child num is empty");
            return radioGroup.getChildCount();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getItemCount() {
        try {
            RadioGroup radioGroup = (RadioGroup) getChildAt(0);
            return radioGroup.getChildCount();
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
            if (null == radioGroup)
                throw new IllegalArgumentException("getCheckedIndex => radioGroup null");
            int count = radioGroup.getChildCount();
            if (count <= 0)
                throw new IllegalArgumentException("getCheckedIndex => child num is empty");
            for (int i = 0; i < count; i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if (null == radioButton) continue;
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
            if (null == radioGroup) return;
            int count = radioGroup.getChildCount();
            if (count <= 0)
                throw new IllegalArgumentException("getCheckedIndex => child num is empty");
            for (int i = 0; i < count; i++) {
//                LbLogUtil.log("HorizontalClassLayout", "setChecked => i = " + i + ", index = " + index);
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if (null == radioButton) continue;
                radioButton.setChecked(false);
            }
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(index);
            radioButton.setChecked(true);
            // listener
            call(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBackground(@NonNull int index, boolean highlight, boolean checked) {
//        LbLogUtil.log("HorizontalClassLayout", "updateBackground => index = " + index + ", highlight = " + highlight + ", checked = " + checked);

        int count = getChildCount();
        if (count != 1) return;

        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        if (null == radioGroup) return;

        int size = radioGroup.getChildCount();
        for (int i = 0; i < size; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (null == radioButton) continue;
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
    }

    private void call(int index) {
        if (null == mListener) return;
        int count = getChildCount();
        if (count != 1) return;
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        if (null == radioGroup)
            return;

        if (index != -1) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(index);
            if (null == radioButton)
                return;
            CharSequence text = radioButton.getText();
            CharSequence hint = radioButton.getHint();
            mListener.onChecked(index, String.valueOf(text), String.valueOf(hint));
        } else {
            int size = radioGroup.getChildCount();
            for (int i = 0; i < size; i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if (null == radioButton) continue;
                boolean checked = radioButton.isChecked();
                if (checked) {
                    CharSequence text = radioButton.getText();
                    CharSequence hint = radioButton.getHint();
                    mListener.onChecked(i, String.valueOf(text), String.valueOf(hint));
                    break;
                }
            }
        }
    }

    private void updateText(@NonNull boolean highlight) {

        int count = getChildCount();
        if (count != 1) return;

        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        if (null == radioGroup) return;
        int size = radioGroup.getChildCount();
        for (int i = 0; i < size; i++) {

            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (null == radioButton) continue;
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
//            LbLogUtil.log("HorizontalClassLayout", "updateTextW => str = " + str);
            if (null != str && str.length() > 0) {
                radioButton.setText(str);
            }
        }
    }

    @Nullable
    public final String getCheckedCode() {
        int count = getChildCount();
        if (count != 1) return null;
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        if (null == radioGroup) return null;
        String code = null;
        int size = radioGroup.getChildCount();
        for (int i = 0; i < size; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (null == radioButton) continue;
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
        if (count != 1) return null;
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        if (null == radioGroup) return null;
        String name = null;
        int size = radioGroup.getChildCount();
        for (int i = 0; i < size; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (null == radioButton) continue;
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
        if (null == radioGroup) return;
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
        int index = 0;
        Context context = getContext();
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        if (null == radioGroup) return;
        for (int i = 0; i < size; i++) {
            ClassBean api = list.get(i);
            if (null == api) continue;
            if (api.isChecked()) {
                index = i;
            }
            HorizontalClassLayout.ClassRadioButton view = new HorizontalClassLayout.ClassRadioButton(context);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(mItemWidth, RadioGroup.LayoutParams.MATCH_PARENT);
            if (i > 0 && mItemMargin > 0) {
                params.leftMargin = mItemMargin;
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
        call(-1);
    }

    public void highlight(@NonNull int index) {
        if (index < 0) return;
        int count = getChildCount();
        if (count != 1) return;
        RadioGroup radioGroup = (RadioGroup) getChildAt(0);
        if (null == radioGroup) return;
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
        if (null == radioGroup) return;
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

    public void leave() {
        int index = getCheckedIndex();
        leave(index);
    }

    public void leave(int index) {
        updateBackground(index, false, true);
        updateText(false);
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
        updateBackground(index, true, false);
        updateText(true);
        // listener
        if (call) {
            call(index);
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

        @Override
        public void setTextColor(int color) {
            try {
                super.setTextColor(color);
            } catch (Exception e) {
            }
        }

        @Override
        public void setTextColor(ColorStateList colors) {
            try {
                super.setTextColor(colors);
            } catch (Exception e) {
            }
        }
    }

    /*************/

    private OnCheckedChangeListener mListener;

    public void setOnCheckedChangeListener(@NonNull OnCheckedChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnCheckedChangeListener {
        void onChecked(@NonNull int index, @NonNull String name, @NonNull String code);
    }
}
