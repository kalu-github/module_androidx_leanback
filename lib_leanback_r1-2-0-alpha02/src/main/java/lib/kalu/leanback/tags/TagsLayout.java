package lib.kalu.leanback.tags;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.R;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lib.kalu.leanback.tags.listener.OnTagsChangeListener;
import lib.kalu.leanback.tags.model.TagBean;
import lib.kalu.leanback.util.LeanBackUtil;
import lib.kalu.leanback.util.ViewUtil;

@Keep
public final class TagsLayout extends LinearLayout {

    private int mItemHeight = 0;
    private int mItemPaddingTop = 0;
    private int mItemPaddingBottom = 0;

    private int mTextSize = 0;
    private int mTextPaddingLeft = 0;
    private int mTextPaddingRight = 0;

    private int mUnderlinePaddingLeft = 0;
    private int mUnderlinePaddingRight = 0;
    private int mUnderlineHeight = 0;
    @ColorInt
    private int mUnderlineColor = Color.TRANSPARENT;

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

    private int findCheckedItemIndex(int row) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            if (row + 1 > childCount)
                throw new Exception("error: childCount is " + childCount + ", row = " + row);
            return ((TagsHorizontalScrollView) getChildAt(row)).findCheckedItemIndex();
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => findCheckedItemIndex => " + e.getMessage());
            return 0;
        }
    }

    private int[] findFocusItemIndex() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            for (int i = 0; i < childCount; i++) {
                int index = ((TagsHorizontalScrollView) getChildAt(i)).findFocusItemIndex();
                if (index != -1) {
                    return new int[]{i, index};
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => findFocusItemIndex => " + e.getMessage());
            return null;
        }
    }

    private boolean isFocusItemIndexLast(int row) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            if (row + 1 >= childCount)
                throw new Exception("error: childCount is " + childCount + ", row = " + row);
            boolean last = ((TagsHorizontalScrollView) getChildAt(row)).isFocusItemIndexLast();
            if (last) {
                return true;
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => isFocusItemIndexLast => " + e.getMessage());
            return false;
        }
    }

    private boolean isFocusItemIndexFirst(int row) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            if (row + 1 > childCount)
                throw new Exception("error: childCount is " + childCount + ", row = " + row);
            boolean last = ((TagsHorizontalScrollView) getChildAt(row)).isFocusItemIndexFirst();
            if (last) {
                return true;
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => isFocusItemIndexFirst => " + e.getMessage());
            return false;
        }
    }

    private boolean delFocus(int row, int col, boolean checked) {
        try {
            LeanBackUtil.log("TagsLayout => delFocus => row = " + row + ", col = " + col + ", checked = " + checked);
            int childCount = getChildCount();
            LeanBackUtil.log("TagsLayout => delFocus => childCount = " + childCount);
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            if (row + 1 > childCount)
                throw new Exception("error: childCount is " + childCount + ", row = " + row);
            return ((TagsHorizontalScrollView) getChildAt(row)).delFocus(col, checked);
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => delFocus => " + e.getMessage());
            return false;
        }
    }

    private boolean reqFocus(int row, int col, boolean auto) {
        try {
            LeanBackUtil.log("TagsLayout => reqFocus => row = " + row + ", col = " + col + ", auto = " + auto);
            int childCount = getChildCount();
            LeanBackUtil.log("TagsLayout => reqFocus => childCount = " + childCount);
            if (childCount <= 0)
                throw new Exception("error: childCount is " + childCount);
            if (row + 1 > childCount)
                throw new Exception("error: childCount is " + childCount + ", row = " + row);
            return ((TagsHorizontalScrollView) getChildAt(row)).reqFocus(col, auto);
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => reqFocus => " + e.getMessage());
            return false;
        }
    }

    private void clear() {
        int[] ints = findFocusItemIndex();
        if (null != ints) {
            int row = ints[0];
            int before = ints[1];
            delFocus(row, before, true);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // error
        if (event.getRepeatCount() > 0) {
            return true;
        }
        // left action_up
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            View focus = findFocus();
            LeanBackUtil.log("TagsLayout => dispatchKeyEvent => left_action_up => focus = " + focus);
            if (null != focus && focus instanceof TagsLayout) {
                int[] ints = findFocusItemIndex();
                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => left_action_up => ints = " + Arrays.toString(ints));
                if (null == ints) {
                    int childCount = getChildCount();
                    if (childCount > 0) {
                        reqFocus(0, 0, true);
                        return true;
                    }
                }
            }
        }
        // left action_down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            View focus = findFocus();
            LeanBackUtil.log("TagsLayout => dispatchKeyEvent => left_action_down => focus = " + focus);
            if (null != focus && focus instanceof TagsLayout) {
                int[] ints = findFocusItemIndex();
                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => left_action_down => ints = " + Arrays.toString(ints));
                if (null != ints) {
                    int row = ints[0];
                    boolean first = isFocusItemIndexFirst(row);
                    LeanBackUtil.log("TagsLayout => dispatchKeyEvent => left_action_down => first = " + first);
                    if (first) {
                        View nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_LEFT);
                        LeanBackUtil.log("TagsLayout => dispatchKeyEvent => left_action_down => nextFocus = " + nextFocus);
                        if (null == nextFocus) {
                            return true;
                        } else if (nextFocus instanceof ViewGroup) {
                            try {
                                int childCount1 = ((ViewGroup) nextFocus).getChildCount();
                                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => left_action_down => childCount1 = " + childCount1);
                                if (childCount1 <= 0) {
                                    return true;
                                }
                            } catch (Exception e) {
                                return true;
                            }
                        } else {
                            boolean hasFocusable = nextFocus.hasFocusable();
                            LeanBackUtil.log("TagsLayout => dispatchKeyEvent => left_action_down => hasFocusable = " + hasFocusable);
                            if (hasFocusable) {
                                return true;
                            }
                        }

                        int before = ints[1];
                        delFocus(row, before, true);

                    } else {
                        int before = ints[1];
                        int next = before - 1;
                        delFocus(row, before, false);
                        reqFocus(row, next, false);
                        return true;
                    }
                } else {
                    boolean reqFocus = reqFocus(0, 0, false);
                    LeanBackUtil.log("TagsLayout => dispatchKeyEvent => left_action_down => reqFocus = " + reqFocus);
                }
            }
        }
        // right action_up
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            View focus = findFocus();
            LeanBackUtil.log("TagsLayout => dispatchKeyEvent => right_action_up => focus = " + focus);
            if (null != focus && focus instanceof TagsLayout) {
                int[] ints = findFocusItemIndex();
                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => right_action_up => ints = " + Arrays.toString(ints));
                if (null == ints) {
                    int childCount = getChildCount();
                    if (childCount > 0) {
                        reqFocus(0, 0, true);
                        return true;
                    }
                }
            }
        }
        // right action_down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            View focus = findFocus();
            LeanBackUtil.log("TagsLayout => dispatchKeyEvent => right_action_down => focus = " + focus);
            if (null != focus && focus instanceof TagsLayout) {
                int[] ints = findFocusItemIndex();
                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => right_action_down => ints = " + Arrays.toString(ints));
                if (null != ints) {
                    int row = ints[0];
                    boolean last = isFocusItemIndexLast(row);
                    LeanBackUtil.log("TagsLayout => dispatchKeyEvent => right_action_down => last = " + last);
                    if (last) {
                        View nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_RIGHT);
                        LeanBackUtil.log("TagsLayout => dispatchKeyEvent => right_action_down => nextFocus = " + nextFocus);
                        if (null == nextFocus) {
                            return true;
                        } else if (nextFocus instanceof ViewGroup) {
                            try {
                                int childCount1 = ((ViewGroup) nextFocus).getChildCount();
                                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => right_action_down => childCount1 = " + childCount1);
                                if (childCount1 <= 0) {
                                    return true;
                                }
                            } catch (Exception e) {
                                return true;
                            }
                        } else {
                            boolean hasFocusable = nextFocus.hasFocusable();
                            LeanBackUtil.log("TagsLayout => dispatchKeyEvent => right_action_down => hasFocusable = " + hasFocusable);
                            if (hasFocusable) {
                                return true;
                            }
                        }

                        int before = ints[1];
                        delFocus(row, before, true);

                    } else {
                        int before = ints[1];
                        int next = before + 1;
                        delFocus(row, before, false);
                        reqFocus(row, next, false);
                        return true;
                    }
                } else {
                    boolean reqFocus = reqFocus(0, 0, false);
                    LeanBackUtil.log("TagsLayout => dispatchKeyEvent => right_action_down => reqFocus = " + reqFocus);
                }
            }
        }
        // up action_up
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            View focus = findFocus();
            LeanBackUtil.log("TagsLayout => dispatchKeyEvent => up_action_up => focus = " + focus);
            if (null != focus && focus instanceof TagsLayout) {
                int[] ints = findFocusItemIndex();
                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => up_action_up => ints = " + Arrays.toString(ints));
                if (null == ints) {
                    int childCount = getChildCount();
                    if (childCount > 0) {
                        int row = childCount - 1;
                        int colUp = findCheckedItemIndex(row);
                        reqFocus(row, colUp, true);
                        return true;
                    }
                }
            }
        }
        // up action_down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            View focus = findFocus();
            LeanBackUtil.log("TagsLayout => dispatchKeyEvent => up_action_down => focus = " + focus);
            if (null != focus && focus instanceof TagsLayout) {
                int[] ints = findFocusItemIndex();
                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => up_action_down => ints = " + Arrays.toString(ints));
                if (null != ints) {
                    int row = ints[0];
                    if (row <= 0) {

                        View nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_UP);
                        LeanBackUtil.log("TagsLayout => dispatchKeyEvent => up_action_down => nextFocus = " + nextFocus);
                        if (null == nextFocus) {
                            return true;
                        } else if (nextFocus instanceof ViewGroup) {
                            try {
                                int childCount1 = ((ViewGroup) nextFocus).getChildCount();
                                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => up_action_down => childCount1 = " + childCount1);
                                if (childCount1 <= 0) {
                                    return true;
                                }
                            } catch (Exception e) {
                                return true;
                            }
                        } else {
                            boolean hasFocusable = nextFocus.hasFocusable();
                            LeanBackUtil.log("TagsLayout => dispatchKeyEvent => up_action_down => hasFocusable = " + hasFocusable);
                            if (hasFocusable) {
                                return true;
                            }
                        }

                        int before = ints[1];
                        delFocus(row, before, true);

                    } else {
                        int col = ints[1];
                        delFocus(row, col, true);
                        int rowDown = row - 1;
                        int colDown = findCheckedItemIndex(rowDown);
                        reqFocus(rowDown, colDown, true);
                        return true;
                    }
                } else {
                    View nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_UP);
                    LeanBackUtil.log("TagsLayout => dispatchKeyEvent => up_action_down => nextFocus = " + nextFocus);
                    if (null == nextFocus) {
                        return true;
                    }
                }
            }
        }
        // down action_up
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            View focus = findFocus();
            LeanBackUtil.log("TagsLayout => dispatchKeyEvent => down_action_up => focus = " + focus);
            if (null != focus && focus instanceof TagsLayout) {
                int[] ints = findFocusItemIndex();
                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => down_action_up => ints = " + Arrays.toString(ints));
                if (null == ints) {
                    int childCount = getChildCount();
                    if (childCount > 0) {
                        int rowDown = 0;
                        int colDown = findCheckedItemIndex(rowDown);
                        reqFocus(rowDown, colDown, true);
                        return true;
                    }
                }
            }
        }
        // down action_down
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            View focus = findFocus();
            LeanBackUtil.log("TagsLayout => dispatchKeyEvent => down_action_down => focus = " + focus);
            if (null != focus && focus instanceof TagsLayout) {
                int[] ints = findFocusItemIndex();
                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => down_action_down => ints = " + Arrays.toString(ints));
                if (null != ints) {
                    int row = ints[0];
                    int childCount = getChildCount();
                    LeanBackUtil.log("TagsLayout => dispatchKeyEvent => down_action_down => childCount = " + childCount);
                    if (row + 1 >= childCount) {

                        View nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_DOWN);
                        LeanBackUtil.log("TagsLayout => dispatchKeyEvent => down_action_down => nextFocus = " + nextFocus);
                        if (null == nextFocus) {
                            return true;
                        } else if (nextFocus instanceof ViewGroup) {
                            try {
                                int childCount1 = ((ViewGroup) nextFocus).getChildCount();
                                LeanBackUtil.log("TagsLayout => dispatchKeyEvent => down_action_down => childCount1 = " + childCount1);
                                if (childCount1 <= 0) {
                                    return true;
                                }
                            } catch (Exception e) {
                                return true;
                            }
                        } else {
                            boolean hasFocusable = nextFocus.hasFocusable();
                            LeanBackUtil.log("TagsLayout => dispatchKeyEvent => down_action_down => hasFocusable = " + hasFocusable);
                            if (hasFocusable) {
                                return true;
                            }
                        }

                        int before = ints[1];
                        delFocus(row, before, true);

                    } else {
                        int col = ints[1];
                        delFocus(row, col, true);
                        int rowUp = row + 1;
                        int colUp = findCheckedItemIndex(rowUp);
                        reqFocus(rowUp, colUp, true);
                        return true;
                    }
                } else {
                    View nextFocus = ViewUtil.findNextFocus(getContext(), this, View.FOCUS_DOWN);
                    LeanBackUtil.log("TagsLayout => dispatchKeyEvent => down_action_down => nextFocus = " + nextFocus);
                    if (null == nextFocus) {
                        return true;
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        TagsUtil.logE("dispatchDraw =>");
        if (mUnderlineColor != Color.TRANSPARENT && mUnderlineHeight > 0) {
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
            TagsUtil.logE("dispatchDraw[drawRect] => left = " + rectF.left + ", top = " + rectF.top + ", right = " + rectF.right + ", bottom = " + rectF.bottom + ", height = " + height + ", width = " + width + ", lineHeight = " + mUnderlineHeight + ", lineColor = " + mUnderlineColor);
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        TagsUtil.logE("drawChild => child = " + child);
        return super.drawChild(canvas, child, drawingTime);
    }

    private final void init(@Nullable AttributeSet attrs) {
        TypedArray attributes = null;
        try {
            attributes = getContext().obtainStyledAttributes(attrs, R.styleable.TagsLayout);
            mItemHeight = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_item_height, 0);
            mItemPaddingTop = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_item_padding_top, 0);
            mItemPaddingBottom = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_item_padding_bottom, 0);
            mTextSize = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_text_size, 0);
            mTextPaddingLeft = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_text_padding_left, 0);
            mTextPaddingRight = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_text_padding_right, 0);
            mUnderlineHeight = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_underline_height, 0);
            mUnderlinePaddingLeft = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_underline_padding_left, 0);
            mUnderlinePaddingRight = attributes.getDimensionPixelOffset(R.styleable.TagsLayout_tag_underline_padding_right, 0);
            mUnderlineColor = attributes.getColor(R.styleable.TagsLayout_tag_underline_color, Color.TRANSPARENT);
        } catch (Exception e) {
        }
        if (null != attributes) {
            attributes.recycle();
        }
        if (mTextSize <= 0) {
            mTextSize = getResources().getDimensionPixelOffset(R.dimen.module_tagslayout_text_size_default);
        }
        if (mItemHeight <= 0) {
            mItemHeight = getResources().getDimensionPixelOffset(R.dimen.module_tagslayout_item_height_default);
        }
        if (mTextPaddingLeft <= 0) {
            mTextPaddingLeft = getResources().getDimensionPixelOffset(R.dimen.module_tagslayout_text_padding_left_default);
        }
        if (mTextPaddingRight <= 0) {
            mTextPaddingRight = getResources().getDimensionPixelOffset(R.dimen.module_tagslayout_text_padding_right_default);
        }
        if (mItemPaddingTop <= 0) {
            mItemPaddingTop = getResources().getDimensionPixelOffset(R.dimen.module_tagslayout_item_padding_top_default);
        }
        if (mItemPaddingBottom <= 0) {
            mItemPaddingBottom = getResources().getDimensionPixelOffset(R.dimen.module_tagslayout_item_padding_bottom_default);
        }

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    /*************/

    private final void add(@NonNull String key, @NonNull List<TagBean> list) {
        TagsHorizontalScrollView child = new TagsHorizontalScrollView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
        child.setPadding(0, mItemPaddingTop, 0, mItemPaddingBottom);
        child.setLayoutParams(params);

        child.update(key, list, mTextSize, mTextPaddingLeft, mTextPaddingRight);
        addView(child);
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
            add(key, list);
        }
    }

    @Keep
    public Map<String, String> getData() {

        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount is " + childCount);
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < childCount; i++) {
                TagsHorizontalScrollView layout = (TagsHorizontalScrollView) getChildAt(i);
                LeanBackUtil.log("TagsLayout => getData => i = " + i + ", layout = " + layout);
                if (null == layout)
                    continue;
                String[] strings = layout.getData();
                LeanBackUtil.log("TagsLayout => getData => strings = " + Arrays.toString(strings));
                if (null == strings)
                    continue;
                if (strings.length != 2)
                    continue;
                map.put(strings[0], strings[1]);
            }
            return map;
        } catch (Exception e) {
            LeanBackUtil.log("TagsLayout => getData => " + e.getMessage());
            return null;
        }
    }

    protected void callback(@NonNull int row, @NonNull TagsHorizontalScrollView view) {
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
}
