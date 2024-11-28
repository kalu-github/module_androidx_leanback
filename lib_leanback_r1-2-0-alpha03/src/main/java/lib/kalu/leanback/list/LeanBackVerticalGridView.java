package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.leanback.util.LeanBackUtil;

public class LeanBackVerticalGridView extends BaseVerticalGridView {
    public LeanBackVerticalGridView(@NonNull Context context) {
        super(context);
    }

    public LeanBackVerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LeanBackVerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    public final void scrollUp(int row) {
//        while (true) {
//            View focusedChild = getFocusedChild();
//            if (null == focusedChild)
//                break;
//            int height = focusedChild.getHeight();
//            scrollBy(0, -height);
//            View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_UP);
//            if (null == nextFocus)
//                break;
//            while (true) {
//                ViewParent parent = nextFocus.getParent();
//                if (parent instanceof LeanBackVerticalGridView) {
//                    break;
//                }
//                nextFocus = (View) parent;
//            }
//            if (null == nextFocus)
//                break;
//            nextFocus.requestFocus();
//            int position = getChildAdapterPosition(nextFocus);
//            if (position == row)
//                break;
//        }
//    }
//
//    public final void scrollDown(int row) {
//        while (true) {
//            View focusedChild = getFocusedChild();
//            if (null == focusedChild)
//                break;
//            int height = focusedChild.getHeight();
//            scrollBy(0, height);
//            View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_UP);
//            if (null == nextFocus)
//                break;
//            while (true) {
//                ViewParent parent = nextFocus.getParent();
//                if (parent instanceof LeanBackVerticalGridView) {
//                    break;
//                }
//                nextFocus = (View) parent;
//            }
//            if (null == nextFocus)
//                break;
//            nextFocus.requestFocus();
//            int position = getChildAdapterPosition(nextFocus);
//            if (position == row)
//                break;
//        }
//    }

//    public void scrollTopCompat() {
//        try {
//            View focusedChild = getFocusedChild();
//            if (null == focusedChild)
//                throw new Exception("focusedChild error: null");
//            int paddingTop = getPaddingTop();
//            scrollBy(0, -paddingTop);
//            scrollBy(0, -Integer.MAX_VALUE);
//        } catch (Exception e) {
//            LeanBackUtil.log("LeanBackVerticalGridView => scrollTopCompat => " + e.getMessage());
//        }
//    }

//    public void scrollBottomCompat() {
//        try {
//            View focusedChild = getFocusedChild();
//            if (null == focusedChild)
//                throw new Exception("focusedChild error: null");
//            int paddingBottom = getPaddingBottom();
//            scrollBy(0, paddingBottom);
//            scrollBy(0, Integer.MAX_VALUE);
//        } catch (Exception e) {
//            LeanBackUtil.log("LeanBackVerticalGridView => scrollBottomCompat => " + e.getMessage());
//        }
//    }
}
