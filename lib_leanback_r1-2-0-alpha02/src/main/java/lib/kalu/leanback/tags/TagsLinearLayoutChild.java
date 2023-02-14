package lib.kalu.leanback.tags;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.util.List;

import lib.kalu.leanback.tags.model.TagBean;
import lib.kalu.leanback.util.LeanBackUtil;

final class TagsLinearLayoutChild extends LinearLayout {

    public TagsLinearLayoutChild(Context context) {
        super(context);
        init();
    }

    private void init() {
        setFocusable(false);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    /*************/

    protected void update(@NonNull String key, @NonNull List<TagBean> list, @NonNull int textSize, @NonNull int paddingLeft, @NonNull int paddingRight) {

        if (null == key || key.length() == 0 || null == list || list.size() == 0)
            return;

        LeanBackUtil.log("TagsLinearLayoutChild", "**********************");
        int size = list.size();
        for (int i = 0; i < size; i++) {

            TagBean temp = list.get(i);
            if (null == temp)
                continue;

            String initText = temp.getText();
            LeanBackUtil.log("TagsLinearLayoutChild", "update => initText = " + initText);
            if (null == initText || initText.length() == 0)
                continue;

            TagsTextView child = new TagsTextView(getContext());
            child.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

            child.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            child.setPadding(paddingLeft, 0, paddingRight, 0);

            child.setTextColorDefault(temp.getTextColorDetault());
            child.setTextColorSelect(temp.getTextColorSelect());
            child.setTextColorFocus(temp.getTextColorFocus());

            child.setBackgroundResourceDefault(temp.getBackgroundResourceDefault());
            child.setBackgroundResourceSelect(temp.getBackgroundResourceSelect());
            child.setBackgroundResourceFocus(temp.getBackgroundResourceFocus());

            child.setSelected(false);
            child.setText(initText, false, i == 0);
            child.setHint(key);
            addView(child);
        }
        LeanBackUtil.log("TagsLinearLayoutChild", "**********************");
    }

    protected void callback(@NonNull TagsTextView view) {
        try {
            int index = indexOfChild(view);
            ((TagsHorizontalScrollView) getParent()).callback(index);
        } catch (Exception e) {
        }
    }

    /*****************/

    protected int findIndex() {

        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("error : childCount is " + childCount);
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (null == child)
                    continue;
                boolean focus = ((TagsTextView) child).isFocus();
                if (focus) {
                    return i;
                } else {
                    boolean checked = ((TagsTextView) child).isChecked();
                    if (checked) {
                        return i;
                    }
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => findIndex => " + e.getMessage());
            return -1;
        }
    }

    protected String[] getData() {
        try {
            int index = findIndex();
            TagsTextView child = (TagsTextView) getChildAt(index);
            CharSequence hint = child.getHint();
            CharSequence text = child.getText();
            return new String[]{String.valueOf(hint), String.valueOf(text)};
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => getData => " + e.getMessage());
            return null;
        }
    }

    protected boolean requestLastItem() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            ((TagsTextView) getChildAt(childCount - 1)).reqFocus();
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => requestLastItem => " + e.getMessage());
            return false;
        }
    }

    protected boolean focusFirst() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            ((TagsTextView) getChildAt(0)).reqFocus();
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => focusFirst => " + e.getMessage());
            return false;
        }
    }

    protected int findFocusItemIndex() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            for (int i = 0; i < childCount; i++) {
                boolean focus = ((TagsTextView) getChildAt(i)).isFocus();
                if (focus) {
                    return i;
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => findFocusItemIndex => " + e.getMessage());
            return -1;
        }
    }

    protected int findCheckedItemIndex() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            for (int i = 0; i < childCount; i++) {
                boolean checked = ((TagsTextView) getChildAt(i)).isChecked();
                if (checked) {
                    return i;
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => findCheckedItemIndex => " + e.getMessage());
            return 0;
        }
    }

    protected boolean isFocusItemIndexLast() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            boolean focus = ((TagsTextView) getChildAt(childCount - 1)).isFocus();
            if (focus) {
                return true;
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => isFocusItemIndexLast => " + e.getMessage());
            return false;
        }
    }

    protected boolean isFocusItemIndexFirst() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            boolean focus = ((TagsTextView) getChildAt(0)).isFocus();
            if (focus) {
                return true;
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => isFocusItemIndexFirst => " + e.getMessage());
            return false;
        }
    }

    protected boolean reqFocus(int index, boolean auto) {
        try {
            LeanBackUtil.log("TagsLinearLayoutChild => reqFocus => index = " + index + ", auto = " + auto);
            int childCount = getChildCount();
            LeanBackUtil.log("TagsLinearLayoutChild => reqFocus => childCount = " + childCount);
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            if (auto) {
                if (index + 1 > childCount) {
                    index = childCount - 1;
                }
                ((TagsTextView) getChildAt(index)).reqFocus();
            } else {
                if (index + 1 > childCount)
                    throw new Exception("error: childCount is " + childCount + ", index = " + index);
                ((TagsTextView) getChildAt(index)).reqFocus();
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => reqFocus => " + e.getMessage());
            return false;
        }
    }

    protected boolean delFocus(int index, boolean checked) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            if (index + 1 > childCount)
                throw new Exception("error: childCount is " + childCount + ", index = " + index);
            ((TagsTextView) getChildAt(index)).delFocus(checked);
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => delFocus => " + e.getMessage());
            return false;
        }
    }
}
