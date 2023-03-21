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
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lib.kalu.leanback.list.RecyclerView;
import lib.kalu.leanback.tags.listener.OnTagsChangeListener;
import lib.kalu.leanback.tags.model.TagBean;
import lib.kalu.leanback.tags.model.TagResultBean;
import lib.kalu.leanback.util.LeanBackUtil;
import lib.kalu.leanback.util.ViewUtil;

@Keep
public final class TagsLayout extends LinearLayout {

    private int mTextSize = 0;
    private int mItemHeight = 0;
    private int mItemMargin = 0;
    private int mItemPaddingLeft = 0;
    private int mItemPaddingRight = 0;
    private int mUnderlineColor = 0;
    private int mUnderlineHeight = 0;
    private int mUnderlinePaddingLeft = 0;
    private int mUnderlinePaddingRight = 0;
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
        if (gainFocus) {
        } else {
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && null != findFocus() && findFocus() instanceof TagsLayout) {

            // left
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => left =>");
                int checkedIndexRow = getCheckedIndexRow();
                int checkedIndex = getCheckedIndex(checkedIndexRow);
                if (checkedIndex > 0) {
                    int next = checkedIndex - 1;
                    LeanBackUtil.log("TagsLayout => dispatchKeyEvent => left => next = " + next);
                    setCheckedIndex(checkedIndexRow, next, true);
                    scrollNext(View.FOCUS_LEFT, checkedIndexRow, next);
                    return true;
                }
            }
            // right
            else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => right =>");
                int checkedIndexRow = getCheckedIndexRow();
                int checkedIndex = getCheckedIndex(checkedIndexRow);
                int itemCount = getItemCount(checkedIndexRow);
                if (checkedIndex < itemCount) {
                    int next = checkedIndex + 1;
                    LeanBackUtil.log("TagsLayout => dispatchKeyEvent => right => next = " + next);
                    setCheckedIndex(checkedIndexRow, next, true);
                    scrollNext(View.FOCUS_RIGHT, checkedIndexRow, next);
                    return true;
                }
            }
            // up
            else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => up =>");
                int checkedIndexRow = getCheckedIndexRow();
                if (checkedIndexRow > 0) {
                    int nextRow = checkedIndexRow - 1;
                    setTag(R.id.lb_tagslayout_row, nextRow);
                    int checkedIndexOlds = getCheckedIndex(checkedIndexRow);
                    LeanBackUtil.log("TagsLayout => dispatchKeyEvent => up => curRow = " + checkedIndexRow + ", checkedIndexOlds = " + checkedIndexOlds);
                    setCheckedIndex(checkedIndexRow, checkedIndexOlds, false);
                    int checkedIndexNews = getCheckedIndex(nextRow);
                    LeanBackUtil.log("TagsLayout => dispatchKeyEvent => up => nextRow = " + nextRow + ", checkedIndexNews = " + checkedIndexNews);
                    setCheckedIndex(nextRow, checkedIndexNews, true);
                    return true;
                }
            }
            // down
            else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => down =>");
                int checkedIndexRow = getCheckedIndexRow();
                int rowCount = getRowCount();
                if (checkedIndexRow < rowCount) {
                    int nextRow = checkedIndexRow - 1;
                    setTag(R.id.lb_tagslayout_row, nextRow);
                    int checkedIndexOlds = getCheckedIndex(checkedIndexRow);
                    LeanBackUtil.log("TagsLayout => dispatchKeyEvent => down => curRow = " + checkedIndexRow + ", checkedIndexOlds = " + checkedIndexOlds);
                    setCheckedIndex(checkedIndexRow, checkedIndexOlds, false);
                    int checkedIndexNews = getCheckedIndex(nextRow);
                    LeanBackUtil.log("TagsLayout => dispatchKeyEvent => down => nextRow = " + nextRow + ", checkedIndexNews = " + checkedIndexNews);
                    setCheckedIndex(nextRow, checkedIndexNews, true);
                    return true;
                }
            }
            // other
            else {
                return checkNextFocus(event) || super.dispatchKeyEvent(event);
            }
        }
        return super.dispatchKeyEvent(event);
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

    @Keep
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

    @Keep
    public Map<String, String> getData() {

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
                if (null == key || key.length() <= 0)
                    continue;
                String name = tagsHorizontalScrollView.getCheckedIndexName();
                if (null == name || name.length() <= 0)
                    continue;
                map.put(key, name);
            }
            return map;
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getData => " + e.getMessage());
            return null;
        }
    }

    @Keep
    public List<TagResultBean> getData2() {

        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            LinkedList<TagResultBean> list = new LinkedList<>();
            for (int i = 0; i < childCount; i++) {
                TagsHorizontalScrollView tagsHorizontalScrollView = (TagsHorizontalScrollView) getChildAt(i);
                if (null == tagsHorizontalScrollView)
                    continue;
                String key = tagsHorizontalScrollView.getCheckedIndexKey();
                if (null == key || key.length() <= 0)
                    continue;
                String name = tagsHorizontalScrollView.getCheckedIndexName();
                if (null == name || name.length() <= 0)
                    continue;
                int code = tagsHorizontalScrollView.getCheckedIndexCode();
                TagResultBean bean = new TagResultBean();
                bean.setKey(key);
                bean.setName(name);
                bean.setCode(code);
                list.add(bean);
            }
            return list;
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getData => " + e.getMessage());
            return null;
        }
    }

    protected void callListener(@NonNull int row, @NonNull TagsHorizontalScrollView view) {
        if (null == onTagsChangeListener)
            return;
        int col;
        try {
            col = indexOfChild(view);
        } catch (Exception e) {
            col = -1;
        }
        onTagsChangeListener.onChange(row, col, getData());
    }

    /*************************/

    private OnTagsChangeListener onTagsChangeListener;

    public void setOnTagsChangeListener(@NonNull OnTagsChangeListener listener) {
        this.onTagsChangeListener = listener;
    }

    /*************************/

    private String getCheckedIndexKey(int row) {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
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
            if (childCount != 1)
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

    private int getCheckedIndexCode(int row) {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
                throw new Exception("childCount error: " + childCount);
            if (row >= childCount)
                throw new Exception("row error: " + row);
            TagsHorizontalScrollView tagsHorizontalScrollView = (TagsHorizontalScrollView) getChildAt(row);
            return tagsHorizontalScrollView.getCheckedIndexCode();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getCheckedIndexCode => " + e.getMessage());
            return -1;
        }
    }

    private int getCheckedIndex(int row) {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
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
            if (childCount != 1)
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

    private int getRowCount() {
        try {
            return getChildCount();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getRowCount => " + e.getMessage());
            return 0;
        }
    }

    private boolean setCheckedIndex(int row, int checkedIndex, boolean hasFocus) {
        try {
            int childCount = getChildCount();
            if (childCount != 1)
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

    private int getCheckedIndexRow() {
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
            LeanBackUtil.log("TagsLayout => scrollNext => row = " + row + ", col = " + col);
            int childCount = getChildCount();
            LeanBackUtil.log("TagsLayout => scrollNext => childCount = " + childCount);
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            if (row >= childCount)
                throw new Exception("error: childCount is " + childCount + ", row = " + row);
            ((TagsHorizontalScrollView) getChildAt(row)).scrollNext(direction, col);
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => scrollNext => " + e.getMessage());
        }
    }

    private boolean checkNextFocus(@NonNull KeyEvent event) {
        try {

            View nextFocus = null;
            boolean checkNext = false;

            // left
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                checkNext = true;
                nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_LEFT);
            }
            // right
            else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                checkNext = true;
                nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_RIGHT);
            }
            // up
            else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                checkNext = true;
                nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_UP);
            }
            // down
            else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                checkNext = true;
                nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_DOWN);
            }

            if (checkNext && null == nextFocus) {
                return true;
            } else if (checkNext && nextFocus instanceof RecyclerView) {
                androidx.recyclerview.widget.RecyclerView.Adapter adapter = ((RecyclerView) nextFocus).getAdapter();
                if (null == adapter) {
                    return true;
                } else {
                    int itemCount = adapter.getItemCount();
                    if (itemCount <= 0) {
                        return true;
                    }
                }
            }
            throw new Exception("check error");
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => checkNextFocus => " + e.getMessage());
            return false;
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
}
