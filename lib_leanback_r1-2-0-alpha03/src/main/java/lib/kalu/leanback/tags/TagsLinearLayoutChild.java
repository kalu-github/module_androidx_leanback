package lib.kalu.leanback.tags;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;

import org.json.JSONObject;

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
        setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
    }

    /*************/

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
            for (int i = 0; i < size; i++) {
                TagBean o = data.get(i);
                if (null == o)
                    continue;
                String text = o.getName();
                if (null == text || text.length() <= 0)
                    continue;
                o.setChecked(i == 0);
                o.setTextColor(textColor);
                o.setTextColorFocus(textColorFocus);
                o.setTextColorChecked(textColorChecked);
                o.setBackgroundResource(backgroundResource);
                o.setBackgroundResourceFocus(backgroundResourceFocus);
                o.setBackgroundResourceChecked(backgroundResourceChecked);
                TagsTextView view = new TagsTextView(getContext());
                view.setTag(R.id.lb_tagslayout_key, key);
                view.setTag(R.id.lb_tagslayout_data, o);
                view.setText(text);
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                view.setPadding(paddingLeft, 0, paddingRight, 0);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.leftMargin = i == 0 ? 0 : margin;
                view.setLayoutParams(layoutParams);
                // ui
                view.setTextColor(o.getTextColor(false));
                view.setBackgroundResource(o.getBackgroundRecource(false));
                // add
                addView(view);
            }
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => update => " + e.getMessage());
        }
    }

    /*****************/

    protected boolean setCheckedIndex(int checkedIndex, boolean hasFocus) {

        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (null == view)
                    continue;
                Object tag = view.getTag(R.id.lb_tagslayout_data);
                if (null == tag)
                    continue;
                if (!(tag instanceof TagBean))
                    continue;
                ((TagBean) tag).setChecked(i == checkedIndex);
                // ui
                ((TextView) view).setTextColor(((TagBean) tag).getTextColor(hasFocus));
                view.setBackgroundResource(((TagBean) tag).getBackgroundRecource(hasFocus));
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => setCheckedIndex => " + e.getMessage());
            return false;
        }
    }

    protected int getItemCount() {
        try {
            return getChildCount();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => getItemCount => " + e.getMessage());
            return 0;
        }
    }

    protected int getCheckedIndex() {

        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (null == child)
                    continue;
                Object tag = child.getTag(R.id.lb_tagslayout_data);
                if (null == tag)
                    continue;
                if (!(tag instanceof TagBean))
                    continue;
                if (((TagBean) tag).isChecked()) {
                    return i;
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => getCheckedIndex => " + e.getMessage());
            return -1;
        }
    }

    protected String getCheckedIndexKey() {
        try {
            int checkedIndex = getCheckedIndex();
            if (checkedIndex < 0)
                throw new Exception("checkedIndex error: " + checkedIndex);
            View view = getChildAt(checkedIndex);
            if (null == view)
                throw new Exception("view error: null");
            Object tag = view.getTag(R.id.lb_tagslayout_key);
            if (null == tag)
                throw new Exception("tag error: null");
            if (!(tag instanceof String))
                throw new Exception("tag error: " + tag);
            return (String) tag;
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => getCheckedIndexKey => " + e.getMessage());
            return null;
        }
    }

    protected JSONObject getCheckedIndexJsonObject() {
        try {
            int checkedIndex = getCheckedIndex();
            if (checkedIndex < 0)
                throw new Exception("checkedIndex error: " + checkedIndex);
            View view = getChildAt(checkedIndex);
            if (null == view)
                throw new Exception("view error: null");
            Object tag = view.getTag(R.id.lb_tagslayout_data);
            if (null == tag)
                throw new Exception("tag error: null");
            if (!(tag instanceof TagBean))
                throw new Exception("tag error: " + tag);
            return ((TagBean) tag).getJsonObject();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => getCheckedIndexJsonObject => " + e.getMessage());
            return null;
        }
    }

    protected String getCheckedIndexName() {
        try {
            int checkedIndex = getCheckedIndex();
            if (checkedIndex < 0)
                throw new Exception("checkedIndex error: " + checkedIndex);
            View view = getChildAt(checkedIndex);
            if (null == view)
                throw new Exception("view error: null");
            Object tag = view.getTag(R.id.lb_tagslayout_data);
            if (null == tag)
                throw new Exception("tag error: null");
            if (!(tag instanceof TagBean))
                throw new Exception("tag error: " + tag);
            return ((TagBean) tag).getName();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => getCheckedIndexName => " + e.getMessage());
            return null;
        }
    }

    protected int getCheckedIndexId() {
        try {
            int checkedIndex = getCheckedIndex();
            if (checkedIndex < 0)
                throw new Exception("checkedIndex error: " + checkedIndex);
            View view = getChildAt(checkedIndex);
            if (null == view)
                throw new Exception("view error: null");
            Object tag = view.getTag(R.id.lb_tagslayout_data);
            if (null == tag)
                throw new Exception("tag error: null");
            if (!(tag instanceof TagBean))
                throw new Exception("tag error: " + tag);
            return ((TagBean) tag).getId();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => getCheckedIndexCode => " + e.getMessage());
            return -1;
        }
    }

    protected int getItemRight(int position) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            if (position < 0 || position >= childCount)
                throw new Exception("position error: " + position);
            View view = getChildAt(position);
            if (null == view) throw new Exception("view is null");
            return view.getRight();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => getItemRight => " + e.getMessage());
            return 0;
        }
    }

    protected int getItemLeft(int position) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            if (position < 0 || position + 1 >= childCount)
                throw new Exception("position error: " + position);
            View view = getChildAt(position);
            if (null == view) throw new Exception("view is null");
            return view.getLeft();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLinearLayoutChild => getItemgetLeft => " + e.getMessage());
            return 0;
        }
    }
}
