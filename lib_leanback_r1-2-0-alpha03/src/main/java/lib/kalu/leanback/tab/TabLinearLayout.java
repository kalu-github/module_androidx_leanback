package lib.kalu.leanback.tab;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import lib.kalu.leanback.util.LeanBackUtil;

final class TabLinearLayout extends LinearLayout {
    TabLinearLayout(Context context) {
        super(context);
        init();
    }

    @Override
    public void setBackground(Drawable background) {
    }

    private void init() {
        setFocusable(false);
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    public int getCheckedIndex() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (null == view) continue;
                if (view instanceof TabTextView) {
                    boolean focus = ((TabTextView) view).isFocus();
                    if (focus) {
                        return i;
                    } else {
                        boolean checked = ((TabTextView) view).isChecked();
                        if (checked) {
                            return i;
                        }
                    }
                } else if (view instanceof TabImageView) {
                    boolean focus = ((TabImageView) view).isFocus();
                    if (focus) {
                        return i;
                    } else {
                        boolean checked = ((TabImageView) view).isChecked();
                        if (checked) {
                            return i;
                        }
                    }
                }
            }
            throw new Exception("not find checked index");
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => getCheckedIndex => " + e.getMessage());
            return -1;
        }
    }

    protected boolean requestChild(int index) {

        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            if (index < 0 || index + 1 > childCount)
                throw new Exception("index error: " + index);
            View view2 = getChildAt(index);
            if (null != view2) {
                if (view2 instanceof TabTextView) {
                    view2.setEnabled(true);
                    view2.setSelected(true);
                } else if (view2 instanceof TabImageView) {
                    view2.setEnabled(true);
                    view2.setSelected(true);
                }
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => focusItem => " + e.getMessage());
            return false;
        }
    }

    protected boolean checkChild(int index) {

        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            if (index < 0 || index + 1 > childCount)
                throw new Exception("index error: " + index);
            View view1 = getChildAt(index);
            if (null != view1) {
                if (view1 instanceof TabTextView) {
                    view1.setEnabled(false);
                    view1.setSelected(true);
                } else if (view1 instanceof TabImageView) {
                    view1.setEnabled(false);
                    view1.setSelected(true);
                }
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => checkItem => " + e.getMessage());
            return false;
        }
    }

    protected boolean resetChild(int index) {

        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            if (index < 0 || index + 1 > childCount)
                throw new Exception("index error: " + index);
            View view1 = getChildAt(index);
            if (null != view1) {
                if (view1 instanceof TabTextView) {
                    view1.setEnabled(false);
                    view1.setSelected(false);
                } else if (view1 instanceof TabImageView) {
                    view1.setEnabled(false);
                    view1.setSelected(false);
                }
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => resetItem => " + e.getMessage());
            return false;
        }

    }

    protected boolean scrollTo(int position) {
        try {
            int childCount = getChildCount();
            if (childCount <= 0) throw new Exception("childCount <= 0");
            if (position < 0 || position + 1 >= childCount)
                throw new Exception("position error: " + position);
            View view = getChildAt(position);
            if (null == view) throw new Exception("view is null");
            view.setFocusable(true);
            view.setEnabled(true);
            view.setSelected(true);
            view.requestFocus();
            view.setFocusable(false);
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => checkItemVisable => " + e.getMessage());
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
