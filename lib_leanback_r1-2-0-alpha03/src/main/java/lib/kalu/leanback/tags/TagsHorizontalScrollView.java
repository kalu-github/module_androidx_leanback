package lib.kalu.leanback.tags;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
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

    protected void update(@NonNull String key,
                          @NonNull List<TagBean> data,
                          @NonNull int textSize,
                          @NonNull int margin,
                          @NonNull int paddingLeft,
                          @NonNull int paddingRight,
                          @ColorInt int textColor,
                          @ColorInt int textColorFocus,
                          @ColorInt int textColorChecked,
                          @DrawableRes int backgroundResource,
                          @DrawableRes int backgroundResourceFocus,
                          @DrawableRes int backgroundResourceChecked) {

        try {
            if (null == key || key.length() <= 0)
                throw new Exception("key error: " + key);
            if (null == data)
                throw new Exception("data error: null");
            int size = data.size();
            if (size <= 0)
                throw new Exception("size error: " + size);
            int childCount = getChildCount();
            if (childCount <= 0) {
                TagsLinearLayoutChild layout = new TagsLinearLayoutChild(getContext());
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layout.setLayoutParams(params);
                addView(layout);
            }
            TagsLinearLayoutChild tagsLinearLayoutChild = (TagsLinearLayoutChild) getChildAt(0);
            tagsLinearLayoutChild.update(key, data, textSize, margin, paddingLeft, paddingRight, textColor, textColorFocus, textColorChecked, backgroundResource, backgroundResourceFocus, backgroundResourceChecked);
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => update => " + e.getMessage());
        }
    }

    protected String getCheckedIndexKey() {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("childCount error: " + childCount);
            TagsLinearLayoutChild tagsLinearLayoutChild = (TagsLinearLayoutChild) getChildAt(0);
            return tagsLinearLayoutChild.getCheckedIndexKey();
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => getCheckedIndexKey => " + e.getMessage());
            return null;
        }
    }

    protected String getCheckedIndexName() {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("childCount error: " + childCount);
            TagsLinearLayoutChild tagsLinearLayoutChild = (TagsLinearLayoutChild) getChildAt(0);
            return tagsLinearLayoutChild.getCheckedIndexName();
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => getCheckedIndexName => " + e.getMessage());
            return null;
        }
    }

    protected int getCheckedIndexCode() {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("childCount error: " + childCount);
            TagsLinearLayoutChild tagsLinearLayoutChild = (TagsLinearLayoutChild) getChildAt(0);
            return tagsLinearLayoutChild.getCheckedIndexCode();
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => getCheckedIndexCode => " + e.getMessage());
            return -1;
        }
    }

    protected int getItemCount() {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("childCount error: " + childCount);
            TagsLinearLayoutChild tagsLinearLayoutChild = (TagsLinearLayoutChild) getChildAt(0);
            return tagsLinearLayoutChild.getItemCount();
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => getItemCount => " + e.getMessage());
            return 0;
        }
    }

    protected int getCheckedIndex() {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("childCount error: " + childCount);
            TagsLinearLayoutChild tagsLinearLayoutChild = (TagsLinearLayoutChild) getChildAt(0);
            return tagsLinearLayoutChild.getCheckedIndex();
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => getCheckedIndex => " + e.getMessage());
            return -1;
        }
    }

    protected boolean setCheckedIndex(int checkedIndex, boolean hasFocus) {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("childCount error: " + childCount);
            TagsLinearLayoutChild tagsLinearLayoutChild = (TagsLinearLayoutChild) getChildAt(0);
            return tagsLinearLayoutChild.setCheckedIndex(checkedIndex, hasFocus);
        } catch (Exception e) {
            LeanBackUtil.log("TagsHorizontalScrollView => setCheckedIndex => " + e.getMessage());
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
}
