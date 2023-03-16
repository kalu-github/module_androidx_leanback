package lib.kalu.leanback.tags;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.List;

import lib.kalu.leanback.tags.model.TagBean;
import lib.kalu.leanback.util.LeanBackUtil;

@Keep
final class TagsHorizontalScrollView extends HorizontalScrollView {
    public TagsHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setFocusable(false);
        setFillViewport(true);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
    }

    /********************/

    protected void update(@NonNull String key, @NonNull List<TagBean> list, @NonNull int textSize, @NonNull int paddingLeft, @NonNull int paddingRight) {

        if (null == key || key.length() == 0 || null == list || list.size() == 0)
            return;
        TagsLinearLayoutChild layout = new TagsLinearLayoutChild(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);

        layout.update(key, list, textSize, paddingLeft, paddingRight);
        addView(layout);
    }

    protected void callback(@NonNull int index) {
        try {
            ((TagsLayout) getParent()).callback(index, this);
        } catch (Exception e) {
        }
    }

    protected String[] getData() {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("error: childCount is " + childCount);
            return ((TagsLinearLayoutChild) getChildAt(0)).getData();
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => getData => " + e.getMessage());
            return null;
        }
    }

//    protected boolean requestLastItem() {
//        try {
//            int childCount = getChildCount();
//            if (childCount != 1)
//                throw new Exception("error: childCount is " + childCount);
//            return ((TagsLinearLayoutChild) getChildAt(0)).requestLastItem();
//        } catch (Exception e) {
//            LeanBackUtil.log("TagsHorizontalScrollView => requestLastItem => " + e.getMessage());
//            return false;
//        }
//    }

    protected int findFocusItemIndex() {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("error: childCount is " + childCount);
            return ((TagsLinearLayoutChild) getChildAt(0)).findFocusItemIndex();
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => findFocusItemIndex => " + e.getMessage());
            return -1;
        }
    }

    protected int findCheckedItemIndex() {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("error: childCount is " + childCount);
            return ((TagsLinearLayoutChild) getChildAt(0)).findCheckedItemIndex();
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => findCheckedItemIndex => " + e.getMessage());
            return 0;
        }
    }

    protected boolean isFocusItemIndexLast() {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("error: childCount is " + childCount);
            return ((TagsLinearLayoutChild) getChildAt(0)).isFocusItemIndexLast();
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => isFocusItemIndexLast => " + e.getMessage());
            return false;
        }
    }

    protected boolean isFocusItemIndexFirst() {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("error: childCount is " + childCount);
            return ((TagsLinearLayoutChild) getChildAt(0)).isFocusItemIndexFirst();
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => isFocusItemIndexFirst => " + e.getMessage());
            return false;
        }
    }

    protected boolean reqFocus(int index, boolean auto) {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("error: childCount is " + childCount);
            boolean reqFocus = ((TagsLinearLayoutChild) getChildAt(0)).reqFocus(index, auto);
            if (reqFocus) {

            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => reqFocus => " + e.getMessage());
            return false;
        }
    }

    protected void scrollNext(int direction, int next) {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("childCount is not 1");

            if (direction == View.FOCUS_RIGHT) {
                int itemRight = ((TagsLinearLayoutChild) getChildAt(0)).getItemRight(next);
                int scrollX = getScrollX();
                int width = getWidth() - getPaddingLeft() - getPaddingRight();
                LeanBackUtil.log("TagsHorizontalScrollView => scrollNext => right => width = " + width + ", scrollX = " + scrollX + ", itemRight = " + itemRight);

                // 不可见/部分不可见
                if (itemRight > width) {
                    int x = itemRight - scrollX - width;
                    scrollBy(x, 0);
                }

            } else if (direction == View.FOCUS_LEFT) {

                int scrollX = getScrollX();
                int itemLeft = ((TagsLinearLayoutChild) getChildAt(0)).getItemLeft(next);
                if (itemLeft < scrollX) {
                    scrollTo(itemLeft, 0);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => scrollNext => " + e.getMessage());
        }
    }

    protected boolean delFocus(int index, boolean checked) {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("error: childCount is " + childCount);
            return ((TagsLinearLayoutChild) getChildAt(0)).delFocus(index, checked);
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => delFocus => " + e.getMessage());
            return false;
        }
    }
}
