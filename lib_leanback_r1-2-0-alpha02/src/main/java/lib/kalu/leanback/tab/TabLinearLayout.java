package lib.kalu.leanback.tab;


import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Keep;

import lib.kalu.leanback.util.LeanBackUtil;

final class TabLinearLayout extends LinearLayout {
    TabLinearLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        setPadding(0, 0, 0, 0);
        setFocusable(false);
        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    public int getCheckedIndex() {
        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount <= 0");
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (null == view)
                    continue;
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

    protected boolean requestItem(int position, int next) {

        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount <= 0");
            if (position < 0 || position + 1 >= childCount)
                throw new Exception("position error: " + position);
            if (next < 0 || next + 1 >= childCount)
                throw new Exception("next error: " + next);
            View view1 = getChildAt(position);
            if (null != view1) {
                if (view1 instanceof TabTextView) {
                    ((TabTextView) view1).setEnabled(false);
                    ((TabTextView) view1).setSelected(false);
                } else if (view1 instanceof TabImageView) {
                    ((TabImageView) view1).setEnabled(false);
                    ((TabImageView) view1).setSelected(false);
                }
            }
            View view2 = getChildAt(next);
            if (null != view2) {
                if (view2 instanceof TabTextView) {
                    ((TabTextView) view2).setEnabled(true);
                    ((TabTextView) view2).setSelected(true);
                } else if (view2 instanceof TabImageView) {
                    ((TabImageView) view2).setEnabled(true);
                    ((TabImageView) view2).setSelected(true);
                }
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => requestNextFocus => " + e.getMessage());
            return false;
        }
    }

    protected boolean requestItem(int position) {

        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount <= 0");
            if (position < 0 || position + 1 >= childCount)
                throw new Exception("position error: " + position);
            View view2 = getChildAt(position);
            if (null != view2) {
                if (view2 instanceof TabTextView) {
                    ((TabTextView) view2).setEnabled(true);
                    ((TabTextView) view2).setSelected(true);
                } else if (view2 instanceof TabImageView) {
                    ((TabImageView) view2).setEnabled(true);
                    ((TabImageView) view2).setSelected(true);
                }
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => requestNextFocus => " + e.getMessage());
            return false;
        }
    }

    protected boolean checkItem(int position) {

        try {
            int childCount = getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount <= 0");
            if (position < 0 || position + 1 >= childCount)
                throw new Exception("position error: " + position);
            View view1 = getChildAt(position);
            if (null != view1) {
                if (view1 instanceof TabTextView) {
                    ((TabTextView) view1).setEnabled(false);
                    ((TabTextView) view1).setSelected(true);
                } else if (view1 instanceof TabImageView) {
                    ((TabImageView) view1).setEnabled(false);
                    ((TabImageView) view1).setSelected(true);
                }
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabLinearLayout => requestNextFocus => " + e.getMessage());
            return false;
        }

    }
}
