package lib.kalu.leanback.clazz;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import lib.kalu.leanback.util.LeanBackUtil;

interface ClassLayoutImpl {

    /*************/

    OnCheckedChangeListener[] mListener = new OnCheckedChangeListener[1];

    default void setOnCheckedChangeListener(@NonNull OnCheckedChangeListener listener) {
        this.mListener[0] = null;
        this.mListener[0] = listener;
    }

    interface OnCheckedChangeListener {
        void onChecked(@NonNull int index, @NonNull String name, @NonNull String code);
    }

    /*************/

    default int getCheckedIndex() {
        try {
            if (!(this instanceof ScrollView))
                throw new Exception("not instanceof ScrollView");
            int scrollChildCount = ((ScrollView) this).getChildCount();
            if (scrollChildCount != 1)
                throw new IllegalArgumentException("scrollChildCount error: " + scrollChildCount);
            int index = -1;
            RadioGroup radioGroup = (RadioGroup) ((ScrollView) this).getChildAt(0);
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
            LeanBackUtil.log("ClassLayoutImpl => getCheckedIndex => " + e.getMessage());
            return -1;
        }
    }


    default void requestFocus(boolean callListener,
                              @ColorInt int textColor,
                              @ColorInt int backgroundColor,
                              @DrawableRes int backgroundResource) {
        try {
            int checkedIndex = getCheckedIndex();
            if (checkedIndex < 0)
                throw new Exception("checkedIndex error: " + checkedIndex);
            requestFocus(checkedIndex, callListener, textColor, backgroundColor, backgroundResource);
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => requestFocus => " + e.getMessage());
        }
    }

    default void requestFocus(int checkedIndex,
                              boolean callListener,
                              @ColorInt int textColor,
                              @ColorInt int backgroundColor,
                              @DrawableRes int backgroundResource) {
        try {
            if (checkedIndex < 0)
                throw new Exception("checkedIndex error: " + checkedIndex);
            RadioButton radioButton = getRadioButton(checkedIndex);
            if (null == radioButton)
                throw new Exception("radioButton error: null");
            ((ViewGroup) this).setFocusable(true);
            ((ViewGroup) this).requestFocus();
            updateBackground(checkedIndex, textColor, backgroundColor, backgroundResource);
            updateText(true);
            if (!callListener)
                throw new Exception("not callListener");
            callListener();
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => requestFocus => " + e.getMessage());
        }
    }

    default void clearFocus(@ColorInt int textColor,
                            @ColorInt int backgroundColor,
                            @DrawableRes int backgroundResource) {
        try {
            int checkedIndex = getCheckedIndex();
            if (checkedIndex < 0)
                throw new Exception("checkedIndex error: " + checkedIndex);
            updateBackground(checkedIndex, textColor, backgroundColor, backgroundResource);
            updateText(false);
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => clearFocus => " + e.getMessage());
        }
    }

    default RadioGroup getRadioGroup() {
        try {
            if (!(this instanceof ScrollView))
                throw new Exception("not instanceof ScrollView");
            int scrollChildCount = ((ScrollView) this).getChildCount();
            if (scrollChildCount != 1)
                throw new IllegalArgumentException("scrollChildCount error: " + scrollChildCount);
            View childAt = ((ScrollView) this).getChildAt(0);
            if (null == childAt)
                throw new Exception("childAt error: null");
            if (!(childAt instanceof RadioGroup))
                throw new Exception("childAt error: " + childAt);
            return (RadioGroup) childAt;
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => getRadioGroup => " + e.getMessage());
            return null;
        }
    }

    default RadioButton getRadioButton(int checkedIndex) {
        try {
            RadioGroup radioGroup = getRadioGroup();
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int radioChildCount = radioGroup.getChildCount();
            if (radioChildCount < 0)
                throw new IllegalArgumentException("radioChildCount error: " + radioChildCount);
            if (checkedIndex >= radioChildCount)
                throw new Exception("checkedIndex error: " + checkedIndex);
            return (RadioButton) radioGroup.getChildAt(checkedIndex);
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => getRadioButton => " + e.getMessage());
            return null;
        }
    }

    default void updateBackground(@NonNull int checkedIndex,
                                  @ColorInt int textColor,
                                  @ColorInt int backgroundColor,
                                  @DrawableRes int backgroundResource) {

        try {
            RadioGroup radioGroup = getRadioGroup();
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int size = radioGroup.getChildCount();
            for (int i = 0; i < size; i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if (backgroundResource != -1) {
                    radioButton.setBackgroundResource(backgroundResource);
                } else {
                    radioButton.setBackgroundColor(backgroundColor);
                }
                radioButton.setTextColor(textColor);
            }

            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(checkedIndex);
            radioButton.setTextColor(textColor);
            if (backgroundResource != -1) {
                radioButton.setBackgroundResource(backgroundResource);
            } else {
                radioButton.setBackgroundColor(backgroundColor);
            }

            int scrollY = ((ViewGroup) this).getScrollY();
            int top = radioButton.getTop();
            ((ViewGroup) this).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            radioButton.requestFocus();
            radioButton.clearFocus();
            ((ViewGroup) this).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            if (scrollY > top) {
                ((ViewGroup) this).scrollTo(0, top);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => updateBackground => " + e.getMessage());
        }
    }

    default void updateText(@NonNull boolean highlight) {

        try {
            RadioGroup radioGroup = getRadioGroup();
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int size = radioGroup.getChildCount();
            for (int i = 0; i < size; i++) {

                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                // 1=>normal
                // 2=>highlight
                // 3=>checked
                Object tag = radioButton.getTag();
                if (null == tag || !(tag instanceof ClassBean))
                    continue;

                boolean checked = radioButton.isChecked();

                CharSequence str;
                Context context = ((View) this).getContext();
                if (highlight && checked) {
                    str = ClassUtil.textHighlight(context, (ClassBean) tag);
                } else if (checked) {
                    str = ClassUtil.textChecked(context, (ClassBean) tag);
                } else {
                    str = ClassUtil.textNormal(context, (ClassBean) tag);
                }
                if (null == str || str.length() <= 0)
                    throw new Exception("str error: " + str);
                radioButton.setText(str);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => updateText => " + e.getMessage());
        }
    }

    default void callListener() {
        try {
            if (null == mListener[0])
                throw new Exception("mListener error: null");
            RadioGroup radioGroup = getRadioGroup();
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int childCount = radioGroup.getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            for (int i = 0; i < childCount; i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                boolean checked = radioButton.isChecked();
                if (checked) {
                    CharSequence text = radioButton.getText();
                    CharSequence hint = radioButton.getHint();
                    mListener[0].onChecked(i, String.valueOf(text), String.valueOf(hint));
                    break;
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => callListener => " + e.getMessage());
        }
    }
}
