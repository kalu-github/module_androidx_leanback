package lib.kalu.leanback.tab;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.leanback.R;

import org.json.JSONObject;

import lib.kalu.leanback.util.LeanBackUtil;

final class TabLinearLayout extends LinearLayout {
    TabLinearLayout(Context context) {
        super(context);
        setFocusable(false);
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    public void setBackground(Drawable background) {
    }

    public int getCheckedIndex() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (null == view) continue;
                if (view instanceof TabTextView) {
                    boolean checked = ((TabTextView) view).isSelected();
                    if (checked) {
                        return i;
                    }
                } else if (view instanceof TabImageView) {
                    boolean checked = ((TabImageView) view).isSelected();
                    if (checked) {
                        return i;
                    }
                }
            }
            throw new Exception("not find checked index");
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => getCheckedIndex => " + e.getMessage());
            return -1;
        }
    }

    public JSONObject getCheckedItemJsonObject() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (null == view) continue;
                if (view instanceof TabTextView) {
                    boolean checked = ((TabTextView) view).isSelected();
                    if (checked) {
                        Object tag = view.getTag(R.id.tab_item_json_object);
                        if (null != tag) {
                            return (JSONObject) tag;
                        }
                    }
                } else if (view instanceof TabImageView) {
                    boolean checked = ((TabImageView) view).isSelected();
                    if (checked) {
                        Object tag = view.getTag(R.id.tab_item_json_object);
                        if (null != tag) {
                            return (JSONObject) tag;
                        }
                    }
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => getCheckedItemJsonObject => " + e.getMessage());
            return null;
        }
    }

    public int getCheckedItemId() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (null == view) continue;
                if (view instanceof TabTextView) {
                    boolean checked = ((TabTextView) view).isSelected();
                    if (checked) {
                        Object tag = view.getTag(R.id.tab_item_id);
                        if (null != tag) {
                            return (int) tag;
                        }
                    }
                } else if (view instanceof TabImageView) {
                    boolean checked = ((TabImageView) view).isSelected();
                    if (checked) {
                        Object tag = view.getTag(R.id.tab_item_id);
                        if (null != tag) {
                            return (int) tag;
                        }
                    }
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => getCheckedItemId => " + e.getMessage());
            return Integer.MIN_VALUE;
        }
    }

    protected boolean focusedItem(int index) {

        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            if (index < 0 || index + 1 > childCount)
                throw new Exception("index error: " + index);
            View view = getChildAt(index);
            if (null == view)
                throw new Exception("error: view null");
            if (view instanceof TabTextView) {
                view.setHovered(true);
                view.setSelected(true);
                ((TabTextView) view).refreshUI();
            } else if (view instanceof TabImageView) {
                view.setHovered(true);
                view.setSelected(true);
                ((TabImageView) view).refreshUI();
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => focusedItem => " + e.getMessage());
            return false;
        }
    }

    protected boolean checkedItem(int index) {

        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            if (index < 0 || index + 1 > childCount)
                throw new Exception("index error: " + index);
            View view = getChildAt(index);
            if (null == view)
                throw new Exception("error: view null");
            if (view instanceof TabTextView) {
                view.setHovered(false);
                view.setSelected(true);
                ((TabTextView) view).refreshUI();
            } else if (view instanceof TabImageView) {
                view.setHovered(false);
                view.setSelected(true);
                ((TabImageView) view).refreshUI();
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => checkedItem => " + e.getMessage());
            return false;
        }
    }

    protected boolean clearItem(int index) {

        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            if (index < 0 || index + 1 > childCount)
                throw new Exception("index error: " + index);
            View view = getChildAt(index);
            if (null == view)
                throw new Exception("error: view null");
            if (view instanceof TabTextView) {
                view.setHovered(false);
                view.setSelected(false);
                ((TabTextView) view).refreshUI();
            } else if (view instanceof TabImageView) {
                view.setHovered(false);
                view.setSelected(false);
                ((TabImageView) view).refreshUI();
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => clearItem => " + e.getMessage());
            return false;
        }

    }

    protected int getItemgetMeasuredWidth(int position) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            if (position < 0 || position + 1 >= childCount)
                throw new Exception("position error: " + position);
            View view = getChildAt(position);
            if (null == view) throw new Exception("view is null");
            Rect rect = new Rect();
            view.getLocalVisibleRect(rect);
            return Math.abs(rect.right) - Math.abs(rect.left);
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => checkItemVisibility => " + e.getMessage());
            return 0;
        }
    }

    protected int getItemRectLeft(int position) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            if (position < 0 || position + 1 >= childCount)
                throw new Exception("position error: " + position);
            View view = getChildAt(position);
            if (null == view) throw new Exception("view is null");
            Rect rect = new Rect();
            view.getLocalVisibleRect(rect);
            return rect.left;
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => getItemRectLeft => " + e.getMessage());
            return 0;
        }
    }

    protected int getItemRectRight(int position) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            if (position < 0 || position + 1 >= childCount)
                throw new Exception("position error: " + position);
            View view = getChildAt(position);
            if (null == view) throw new Exception("view is null");
            Rect rect = new Rect();
            view.getLocalVisibleRect(rect);
            return rect.right;
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => getItemRectRight => " + e.getMessage());
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
            LeanBackUtil.log("TabLinearLayout => getItemgetLeft => " + e.getMessage());
            return 0;
        }
    }

    protected int getItemCount() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            return childCount;
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => getItemCount => " + e.getMessage());
            return 0;
        }
    }

    protected String getItemText(int position) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            if (position < 0 || position >= childCount)
                throw new Exception("position error: " + position);
            View view = getChildAt(position);
            if (null == view) throw new Exception("view is null");
            if (!(view instanceof TextView)) throw new Exception("view not TextView");
            return ((TextView) view).getText().toString();
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => getItemText => " + e.getMessage());
            return null;
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
            LeanBackUtil.log("TabLinearLayout => getItemRight => " + e.getMessage());
            return 0;
        }
    }

    protected int getItemWidth(int position) {

        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            if (position < 0 || position + 1 >= childCount)
                throw new Exception("position error: " + position);
            View view = getChildAt(position);
            return view.getWidth();
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => getItemWidth => " + e.getMessage());
            return 0;
        }
    }

    protected int getItemRange(int position) {
        int itemgetMeasuredWidth = getItemgetMeasuredWidth(position);
        int itemWidth = getItemWidth(position);
        return Math.abs(itemgetMeasuredWidth - itemWidth);
    }
}
