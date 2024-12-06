package lib.kalu.leanback.tags;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lib.kalu.leanback.tags.listener.OnTagsChangeListener;
import lib.kalu.leanback.tags.listener.OnTagsIdChangeListener;
import lib.kalu.leanback.tags.listener.OnTagsJsonObjectChangeListener;
import lib.kalu.leanback.tags.listener.OnTagsNameChangeListener;
import lib.kalu.leanback.tags.model.TagBean;
import lib.kalu.leanback.util.LeanBackUtil;


public final class TagsLayout extends LinearLayout {

    @DrawableRes
    int mBackgroundResource;
    @DrawableRes
    int mBackgroundResourceChecked;
    @DrawableRes
    int mBackgroundResourceFocus;
    @ColorInt
    int mTextColor;
    @ColorInt
    int mTextColorChecked;
    @ColorInt
    int mTextColorFocus;
    private int mTextSize = 0;
    private int mItemHeight = 0;
    private int mItemMargin = 0;
    private int mItemPaddingLeft = 0;
    private int mItemPaddingRight = 0;
    private int mUnderlineColor = 0;
    private int mUnderlineHeight = 0;
    private int mUnderlinePaddingLeft = 0;
    private int mUnderlinePaddingRight = 0;

    /*************************/


    public TagsLayout(Context context) {
        super(context);
        init(null);
    }

    public TagsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @SuppressLint("NewApi")
    public TagsLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        try {
            int checkedIndexRow = getCheckedIndexRow();
            if (checkedIndexRow < 0)
                throw new Exception("checkedIndexRow error: " + checkedIndexRow);
            int checkedIndex = getCheckedIndex(checkedIndexRow);
            setCheckedIndex(checkedIndexRow, checkedIndex, gainFocus);
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => onFocusChanged => " + e.getMessage());
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return dispatchKey(event) || super.dispatchKeyEvent(event);
    }

    private boolean dispatchKey(KeyEvent event) {
        try {
            View focus = findFocus();
            if (null == focus)
                throw new Exception("focus warning: null");
            if (!(focus instanceof TagsLayout))
                throw new Exception("focus warning: not instanceof TagsLayout");
            // action-down => left
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                int checkedIndexRow = getCheckedIndexRow();
                if (checkedIndexRow < 0)
                    throw new Exception("checkedIndexRow error: " + checkedIndexRow);
                int checkedIndex = getCheckedIndex(checkedIndexRow);
                if (checkedIndex > 0) {
                    int next = checkedIndex - 1;
                    setCheckedIndex(checkedIndexRow, next, true);
                    scrollNext(View.FOCUS_LEFT, checkedIndexRow, next);
                    callListener();
                    return true;
                }
            }
            // action-down => right
            else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                int checkedIndexRow = getCheckedIndexRow();
                if (checkedIndexRow < 0)
                    throw new Exception("checkedIndexRow error: " + checkedIndexRow);
                int checkedIndex = getCheckedIndex(checkedIndexRow);
                int itemCount = getItemCount(checkedIndexRow);
                if (checkedIndex + 1 < itemCount) {
                    int next = checkedIndex + 1;
                    setCheckedIndex(checkedIndexRow, next, true);
                    scrollNext(View.FOCUS_RIGHT, checkedIndexRow, next);
                    callListener();
                    return true;
                }
            }
            // action-down => up
            else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                int checkedIndexRow = getCheckedIndexRow();
                if (checkedIndexRow < 0)
                    throw new Exception("checkedIndexRow error: " + checkedIndexRow);
                if (checkedIndexRow > 0) {
                    int nextRow = checkedIndexRow - 1;
                    setTag(R.id.lb_tagslayout_row, nextRow);
                    int checkedIndexOlds = getCheckedIndex(checkedIndexRow);
                    setCheckedIndex(checkedIndexRow, checkedIndexOlds, false);
                    int checkedIndexNews = getCheckedIndex(nextRow);
                    setCheckedIndex(nextRow, checkedIndexNews, true);
                    return true;
                }
            }
            // action-down => down
            else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                int checkedIndexRow = getCheckedIndexRow();
                if (checkedIndexRow < 0)
                    throw new Exception("checkedIndexRow error: " + checkedIndexRow);
                int rowCount = getRowCount();
                if (checkedIndexRow + 1 < rowCount) {
                    int nextRow = checkedIndexRow + 1;
                    setTag(R.id.lb_tagslayout_row, nextRow);
                    int checkedIndexOlds = getCheckedIndex(checkedIndexRow);
                    setCheckedIndex(checkedIndexRow, checkedIndexOlds, false);
                    int checkedIndexNews = getCheckedIndex(nextRow);
                    setCheckedIndex(nextRow, checkedIndexNews, true);
                    return true;
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => dispatchKey => " + e.getMessage());
            return false;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mUnderlineColor != 0 && mUnderlineHeight > 0) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(mUnderlineColor);
            paint.setStrokeWidth(mUnderlineHeight);
            Rect rectF = new Rect();
            int width = getWidth();
            int height = getHeight();
            rectF.left = mUnderlinePaddingLeft;
            rectF.right = width - mUnderlinePaddingRight;
            rectF.top = height - mUnderlineHeight / 2;
            rectF.bottom = height;
            canvas.drawRect(rectF, paint);
        }
    }


    public void update(@NonNull Map<String, List<TagBean>> map) {

        if (null == map || map.size() == 0)
            return;

        for (String key : map.keySet()) {
            if (null == key || key.length() == 0)
                continue;
            List<TagBean> list = map.get(key);
            if (null == list || list.size() == 0)
                continue;
            // 1
            TagsHorizontalScrollView scrollView = new TagsHorizontalScrollView(getContext());
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
            scrollView.setLayoutParams(layoutParams);
            addView(scrollView);
            // 2
            scrollView.update(key, list, mTextSize, mItemMargin, mItemPaddingLeft, mItemPaddingRight, mTextColor, mTextColorFocus, mTextColorChecked, mBackgroundResource, mBackgroundResourceFocus, mBackgroundResourceChecked);
        }
    }

    /*************************/

    private String getCheckedIndexKey(int row) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            if (row >= childCount)
                throw new Exception("row error: " + row);
            TagsHorizontalScrollView tagsHorizontalScrollView = (TagsHorizontalScrollView) getChildAt(row);
            return tagsHorizontalScrollView.getCheckedIndexKey();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getCheckedIndexKey => " + e.getMessage());
            return null;
        }
    }

    private String getCheckedIndexName(int row) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            if (row >= childCount)
                throw new Exception("row error: " + row);
            TagsHorizontalScrollView tagsHorizontalScrollView = (TagsHorizontalScrollView) getChildAt(row);
            return tagsHorizontalScrollView.getCheckedIndexName();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getCheckedIndexName => " + e.getMessage());
            return null;
        }
    }

    private int getCheckedIndexId(int row) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            if (row >= childCount)
                throw new Exception("row error: " + row);
            TagsHorizontalScrollView tagsHorizontalScrollView = (TagsHorizontalScrollView) getChildAt(row);
            return tagsHorizontalScrollView.getCheckedIndexId();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getCheckedIndexId => " + e.getMessage());
            return -1;
        }
    }

    private int getCheckedIndex(int row) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            if (row >= childCount)
                throw new Exception("row error: " + row);
            TagsHorizontalScrollView tagsHorizontalScrollView = (TagsHorizontalScrollView) getChildAt(row);
            return tagsHorizontalScrollView.getCheckedIndex();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getCheckedIndex => " + e.getMessage());
            return -1;
        }
    }

    private int getItemCount(int row) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            if (row >= childCount)
                throw new Exception("row error: " + row);
            TagsHorizontalScrollView tagsHorizontalScrollView = (TagsHorizontalScrollView) getChildAt(row);
            return tagsHorizontalScrollView.getItemCount();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getItemCount => " + e.getMessage());
            return 0;
        }
    }

    private boolean setCheckedIndex(int row, int checkedIndex, boolean hasFocus) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            if (row >= childCount)
                throw new Exception("row error: " + row);
            TagsHorizontalScrollView tagsHorizontalScrollView = (TagsHorizontalScrollView) getChildAt(row);
            return tagsHorizontalScrollView.setCheckedIndex(checkedIndex, hasFocus);
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => setCheckedIndex => " + e.getMessage());
            return false;
        }
    }

    public int getRowCount() {
        try {
            return getChildCount();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getRowCount => " + e.getMessage());
            return 0;
        }
    }

    public int getCheckedIndexRow() {
        try {
            Object tag = getTag(R.id.lb_tagslayout_row);
            if (null == tag)
                throw new Exception("tag error: null");
            if (!(tag instanceof Integer))
                throw new Exception("tag error: " + tag);
            return (int) tag;
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getCheckedIndexRow => " + e.getMessage());
            return 0;
        }
    }

    private void scrollNext(int direction, int row, int col) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            if (row >= childCount)
                throw new Exception("error: childCount is " + childCount + ", row = " + row);
            ((TagsHorizontalScrollView) getChildAt(row)).scrollNext(direction, col);
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => scrollNext => " + e.getMessage());
        }
    }

    private void init(@Nullable AttributeSet attrs) {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);
        TypedArray attributes = null;
        try {
            attributes = getContext().obtainStyledAttributes(attrs, R.styleable.TagsLayout);
            mTextSize = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_text_size, 10);
            mItemHeight = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_item_height, 40);
            mItemMargin = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_item_margin, 0);
            mItemPaddingLeft = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_item_padding_left, 0);
            mItemPaddingRight = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_item_padding_right, 0);
            mUnderlineColor = attributes.getColor(R.styleable.TagsLayout_tag_underline_color, 0);
            mUnderlineHeight = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_underline_height, 0);
            mUnderlinePaddingLeft = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_underline_padding_left, 0);
            mUnderlinePaddingRight = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_underline_padding_right, 0);
            mTextColor = attributes.getColor(R.styleable.TagsLayout_tag_text_color, 0);
            mTextColorFocus = attributes.getColor(R.styleable.TagsLayout_tag_text_color_focus, 0);
            mTextColorChecked = attributes.getColor(R.styleable.TagsLayout_tag_text_color_checked, 0);
            mBackgroundResource = attributes.getResourceId(R.styleable.TagsLayout_tag_background_resource, 0);
            mBackgroundResourceFocus = attributes.getResourceId(R.styleable.TagsLayout_tag_background_resource_focus, 0);
            mBackgroundResourceChecked = attributes.getResourceId(R.styleable.TagsLayout_tag_background_resource_checked, 0);
        } catch (Exception e) {
        }
        if (null != attributes) {
            attributes.recycle();
        }
    }

    private void callListener() {

        try {
            int checkedIndexRow = getCheckedIndexRow();
            if (checkedIndexRow < 0)
                throw new Exception("checkedIndexRow error: " + checkedIndexRow);
            int checkedIndex = getCheckedIndex(checkedIndexRow);
            if (checkedIndex < 0)
                throw new Exception("checkedIndex error: " + checkedIndex);

            // mOnTagsChangeListener
            if (null != mOnTagsChangeListener) {
                mOnTagsChangeListener.onChange(checkedIndexRow, checkedIndex);
            }

            // mOnTagsIdChangeListener
            if (null != mOnTagsIdChangeListener) {
                Map<String, Integer> currentIds = getCurrentIds();
                if (null == currentIds)
                    throw new Exception("currentIds error: null");
                mOnTagsIdChangeListener.onChange(checkedIndexRow, checkedIndex, currentIds);
            }

            // mOnTagsNameChangeListener
            if (null != mOnTagsNameChangeListener) {
                Map<String, String> currentNames = getCurrentNames();
                if (null == currentNames)
                    throw new Exception("currentNames error: null");
                mOnTagsNameChangeListener.onChange(checkedIndexRow, checkedIndex, currentNames);
            }

            // mOnTagsJsonObjectChangeListener
            if (null != mOnTagsJsonObjectChangeListener) {
                Map<String, JSONObject> jsonObjects = getCurrentJsonObjects();
                if (null == jsonObjects)
                    throw new Exception("jsonObjects error: null");
                mOnTagsJsonObjectChangeListener.onChange(checkedIndexRow, checkedIndex, jsonObjects);
            }


        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getData => " + e.getMessage());
        }
    }

    /*********************/

    public Map<String, JSONObject> getCurrentJsonObjects() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            HashMap<String, JSONObject> map = new HashMap<>();
            for (int i = 0; i < childCount; i++) {
                TagsHorizontalScrollView tagsHorizontalScrollView = (TagsHorizontalScrollView) getChildAt(i);
                if (null == tagsHorizontalScrollView)
                    continue;
                String key = tagsHorizontalScrollView.getCheckedIndexKey();
                if (null == key)
                    continue;
                JSONObject jsonObject = tagsHorizontalScrollView.getCheckedIndexJsonObject();
                if (null == jsonObject)
                    continue;
                map.put(key, jsonObject);
            }
            return map;
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getCurrentJsonObjects => " + e.getMessage());
            return null;
        }
    }

    public Map<String, Integer> getCurrentIds() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            HashMap<String, Integer> map = new HashMap<>();
            for (int i = 0; i < childCount; i++) {
                TagsHorizontalScrollView tagsHorizontalScrollView = (TagsHorizontalScrollView) getChildAt(i);
                if (null == tagsHorizontalScrollView)
                    continue;
                String key = tagsHorizontalScrollView.getCheckedIndexKey();
                if (null == key)
                    continue;
                int checkedIndexId = tagsHorizontalScrollView.getCheckedIndexId();
                if (checkedIndexId == -1)
                    continue;
                map.put(key, checkedIndexId);
            }
            return map;
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getCurrentIds => " + e.getMessage());
            return null;
        }
    }

    public Map<String, String> getCurrentNames() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < childCount; i++) {
                TagsHorizontalScrollView tagsHorizontalScrollView = (TagsHorizontalScrollView) getChildAt(i);
                if (null == tagsHorizontalScrollView)
                    continue;
                String key = tagsHorizontalScrollView.getCheckedIndexKey();
                if (null == key)
                    continue;
                String checkedIndexName = tagsHorizontalScrollView.getCheckedIndexName();
                if (null == checkedIndexName)
                    continue;
                map.put(key, checkedIndexName);
            }
            return map;
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getCurrentNames => " + e.getMessage());
            return null;
        }
    }


    /***********/

    private OnTagsJsonObjectChangeListener mOnTagsJsonObjectChangeListener;

    public void setOnTagsChangeListener(@NonNull OnTagsJsonObjectChangeListener listener) {
        this.mOnTagsJsonObjectChangeListener = listener;
    }

    /***********/

    private OnTagsNameChangeListener mOnTagsNameChangeListener;

    public void setOnTagsNameChangeListener(@NonNull OnTagsNameChangeListener listener) {
        this.mOnTagsNameChangeListener = listener;
    }

    /***********/

    private OnTagsIdChangeListener mOnTagsIdChangeListener;

    public void setOnTagsIdChangeListener(@NonNull OnTagsIdChangeListener listener) {
        this.mOnTagsIdChangeListener = listener;
    }

    /***********/

    private OnTagsChangeListener mOnTagsChangeListener;

    public void setOnTagsChangeListener(@NonNull OnTagsChangeListener listener) {
        this.mOnTagsChangeListener = listener;
    }
}
