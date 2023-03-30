package lib.kalu.leanback.clazz;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.R;

import java.util.List;

import lib.kalu.leanback.util.LeanBackUtil;

interface ClassLayoutImpl {

    /*************/

    OnCheckedChangeListener[] mListener = new OnCheckedChangeListener[1];

    default void setOnCheckedChangeListener(@NonNull OnCheckedChangeListener listener) {
        this.mListener[0] = null;
        this.mListener[0] = listener;
    }

    /*************/

    default RadioGroup getRadioGroup(boolean checkCount) {
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
            if (checkCount) {
                int childCount = ((RadioGroup) childAt).getChildCount();
                if (childCount <= 0)
                    throw new Exception("childCount error: " + childCount);
            }
            return (RadioGroup) childAt;
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => getRadioGroup => " + e.getMessage());
            return null;
        }
    }

    default RadioButton getRadioButton(int checkedIndex) {
        try {
            RadioGroup radioGroup = getRadioGroup(true);
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

    default void callListener(boolean isFromUser) {
        try {
            if (null == mListener[0])
                throw new Exception("mListener error: null");
            RadioGroup radioGroup = getRadioGroup(true);
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int itemCount = getItemCount();
            if (itemCount <= 0)
                throw new Exception("itemCount error: " + itemCount);
            for (int i = 0; i < itemCount; i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if (null == radioButton)
                    continue;
                Object tag = radioButton.getTag(R.id.lb_classlayout_data);
                if (null == tag || !(tag instanceof ClassBean))
                    continue;
                if (((ClassBean) tag).isChecked()) {
                    mListener[0].onChecked(isFromUser, i, ((ClassBean) tag).getText(), ((ClassBean) tag).getCode());
                    break;
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => callListener => " + e.getMessage());
        }
    }

    default int getItemCount() {
        try {
            RadioGroup radioGroup = getRadioGroup(true);
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int count = radioGroup.getChildCount();
            if (count <= 0)
                throw new Exception("count error: " + count);
            return count;
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => getItemCount => " + e.getMessage());
            return 0;
        }
    }

    default int getCheckedIndex() {
        try {
            RadioGroup radioGroup = getRadioGroup(true);
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int itemCount = getItemCount();
            for (int i = 0; i < itemCount; i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if (null == radioButton)
                    continue;
                Object tag = radioButton.getTag(R.id.lb_classlayout_data);
                if (null == tag || !(tag instanceof ClassBean))
                    continue;
                if (((ClassBean) tag).isChecked())
                    return i;
            }
            throw new Exception("error");
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => getCheckedIndex => " + e.getMessage());
            return -1;
        }
    }


    default void setCheckedRadioButton(@NonNull boolean hasFocus,
                                       @NonNull boolean isFromUser,
                                       @NonNull boolean callListener) {
        int checkedIndex = getCheckedIndex();
        setCheckedRadioButton(checkedIndex, hasFocus, isFromUser, callListener);
    }

    default void setCheckedRadioButton(@NonNull int checkedIndex,
                                       @NonNull boolean hasFocus,
                                       @NonNull boolean isFromUser,
                                       @NonNull boolean callListener) {
        try {
            LeanBackUtil.log("ClassLayoutImpl => setCheckedIndex => checkedIndex = " + checkedIndex + ", hasFocus = " + hasFocus + ", callListener = " + callListener);
            RadioGroup radioGroup = getRadioGroup(true);
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int itemCount = getItemCount();
            if (checkedIndex >= itemCount)
                throw new Exception("checkedIndex error: " + checkedIndex + ", itemCount = " + itemCount);
            for (int i = 0; i < itemCount; i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if (null == radioButton)
                    continue;
                Object tag = radioButton.getTag(R.id.lb_classlayout_data);
                if (null == tag || !(tag instanceof ClassBean))
                    continue;
                LeanBackUtil.log("ClassLayoutImpl => setCheckedIndex => i = " + i + ", tag = " + tag.toString());
                ((ClassBean) tag).setChecked(checkedIndex == i);
                LeanBackUtil.log("ClassLayoutImpl => setCheckedIndex => i = " + i + ", tag = " + tag.toString());
                // ui
                radioButton.setText(((ClassBean) tag).getTextSpannableString(((View) this).getContext(), hasFocus));
                radioButton.setTextColor(((ClassBean) tag).getTextColor(hasFocus));
                radioButton.setBackgroundResource(((ClassBean) tag).getBackgroundRecource(hasFocus));
            }
            if (!callListener)
                throw new Exception("not callListener");
            callListener(isFromUser);
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => setCheckedIndex => " + e.getMessage());
        }
    }

    default CharSequence getCheckedCode() {
        try {
            RadioGroup radioGroup = getRadioGroup(true);
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int itemCount = getItemCount();
            for (int i = 0; i < itemCount; i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if (null == radioButton)
                    continue;
                Object tag = radioButton.getTag(R.id.lb_classlayout_data);
                if (null == tag || !(tag instanceof ClassBean))
                    continue;
                if (((ClassBean) tag).isChecked())
                    return ((ClassBean) tag).getCode();
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => getCheckedCode => " + e.getMessage());
            return null;
        }
    }

    @Nullable
    default String getCheckedName() {
        try {
            RadioGroup radioGroup = getRadioGroup(true);
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int itemCount = getItemCount();
            for (int i = 0; i < itemCount; i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if (null == radioButton)
                    continue;
                Object tag = radioButton.getTag(R.id.lb_classlayout_data);
                if (null == tag || !(tag instanceof ClassBean))
                    continue;
                if (((ClassBean) tag).isChecked())
                    return ((ClassBean) tag).getText();
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => getCheckedName => " + e.getMessage());
            return null;
        }
    }

    default void setRadioButtonText(@NonNull int position, @NonNull String s) {
        try {
            RadioGroup radioGroup = getRadioGroup(true);
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int itemCount = getItemCount();
            if (position >= itemCount)
                throw new Exception("position error: " + position + ", itemCount = " + itemCount);
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(position);
            radioButton.setText(s);
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => setRadioButtonText => " + e.getMessage());
        }
    }

    default void update(@NonNull int index, String name) {
        try {
            RadioButton radioButton = getRadioButton(index);
            if (null == radioButton)
                throw new Exception("radioButton error: null");
            radioButton.setText(name);
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => update => " + e.getMessage());
        }
    }

    default void update(@NonNull List<? extends ClassBean> data,
                        @NonNull int chechedIndex,
                        @NonNull int itemMargin,
                        @NonNull int itemWidth,
                        @NonNull int itemHeight,
                        @NonNull int textSize,
                        @NonNull int orientation,
                        @ColorInt int textColor,
                        @ColorInt int textColorFocus,
                        @ColorInt int textColorChecked,
                        @DrawableRes int backgroundResource,
                        @DrawableRes int backgroundResourceFocus,
                        @DrawableRes int backgroundResourceChecked,
                        @NonNull boolean callListener) {

        try {
            if (null == data)
                throw new Exception("data error: " + data);
            int size = data.size();
            if (size <= 0)
                throw new Exception("size error: " + size);
            if (chechedIndex >= size)
                throw new Exception("chechedIndex error: " + chechedIndex + ", size = " + size);
            RadioGroup radioGroup = getRadioGroup(false);
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            for (int i = 0; i < size; i++) {
                ClassBean o = data.get(i);
                if (null == o)
                    continue;
                o.setChecked(i == chechedIndex);
                o.setTextColor(textColor);
                o.setTextColorFocus(textColorFocus);
                o.setTextColorChecked(textColorChecked);
                o.setBackgroundResource(backgroundResource);
                o.setBackgroundResourceChecked(backgroundResourceChecked);
                o.setBackgroundResourceFocus(backgroundResourceFocus);
//                LeanBackUtil.log("ClassLayoutImpl => update => i = " + i + ", o = " + o);
            }
            for (int i = 0; i < size; i++) {
                ClassBean o = data.get(i);
                if (null == o)
                    continue;
                ClassRadioButton radioButton = new ClassRadioButton(((View) this).getContext());
                RadioGroup.LayoutParams layoutParams;
                if (orientation == 1) {
                    layoutParams = new RadioGroup.LayoutParams(itemWidth, RadioGroup.LayoutParams.MATCH_PARENT);
                } else {
                    layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, itemHeight);
                }
                if (i > 0 && itemMargin > 0) {
                    if (orientation == 1) {
                        layoutParams.leftMargin = itemMargin;
                    } else {
                        layoutParams.topMargin = itemMargin;
                    }
                }
                radioButton.setLayoutParams(layoutParams);
                radioButton.setTag(R.id.lb_classlayout_data, o);
                radioButton.setSingleLine();
                radioButton.setFocusable(false);
                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                radioButton.setGravity(Gravity.CENTER);
                radioButton.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
                radioGroup.addView(radioButton);
                // ui
                radioButton.setText((o.getTextSpannableString(((View) this).getContext(), false)));
                radioButton.setTextColor(o.getTextColor(false));
                radioButton.setBackgroundResource(o.getBackgroundRecource(false));
            }
            if (!callListener)
                throw new Exception("not callListener");
            callListener(false);
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => update => " + e.getMessage());
        }
    }

    /**************/

    default int getItemLeft(int position) {
        try {
            RadioButton radioButton = getRadioButton(position);
            if (null == radioButton)
                throw new Exception("radioButton error: null");
            return radioButton.getLeft();
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => getItemLeft => " + e.getMessage());
            return 0;
        }
    }

    default int getItemRight(int position) {
        try {
            RadioButton radioButton = getRadioButton(position);
            if (null == radioButton)
                throw new Exception("radioButton error: null");
            return radioButton.getRight();
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => getItemRight => " + e.getMessage());
            return 0;
        }
    }

    default int getItemBottom(int position) {
        try {
            RadioButton radioButton = getRadioButton(position);
            if (null == radioButton)
                throw new Exception("radioButton error: null");
            return radioButton.getBottom();
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => getItemBottom => " + e.getMessage());
            return 0;
        }
    }

    default int getItemTop(int position) {
        try {
            RadioButton radioButton = getRadioButton(position);
            if (null == radioButton)
                throw new Exception("radioButton error: null");
            return radioButton.getTop();
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => getItemTop => " + e.getMessage());
            return 0;
        }
    }

    default void scrollNext(int direction, int next) {
        try {
            RadioGroup radioGroup = getRadioGroup(false);
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            // down
            if (direction == View.FOCUS_DOWN) {
                int itemBottom = getItemBottom(next);
                int scrollY = ((View) this).getScrollY();
                int height = ((View) this).getHeight() - ((View) this).getPaddingTop() - ((View) this).getPaddingBottom();
                if (itemBottom > height) {
                    int y = itemBottom - scrollY - height;
                    ((View) this).scrollBy(0, y);
                }
            }
            // up
            else if (direction == View.FOCUS_UP) {
                int scrollY = ((View) this).getScrollY();
                int itemTop = getItemTop(next);
                if (itemTop < scrollY) {
                    ((View) this).scrollTo(0, itemTop);
                }
            }
            // right
            else if (direction == View.FOCUS_RIGHT) {
                int itemRight = getItemRight(next);
                int scrollX = ((View) this).getScrollX();
                int width = ((View) this).getWidth() - ((View) this).getPaddingLeft() - ((View) this).getPaddingRight();

                // 不可见/部分不可见
                if (itemRight > width) {
                    int x = itemRight - scrollX - width;
                    ((View) this).scrollBy(x, 0);
                }
            }
            // left
            else if (direction == View.FOCUS_LEFT) {

                int scrollX = ((View) this).getScrollX();
                int itemLeft = getItemLeft(next);
                if (itemLeft < scrollX) {
                    ((View) this).scrollTo(itemLeft, 0);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ClassLayoutImpl => scrollNext => " + e.getMessage());
        }
    }
}