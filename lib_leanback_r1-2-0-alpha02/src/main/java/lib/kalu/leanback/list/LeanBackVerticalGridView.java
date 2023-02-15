package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class LeanBackVerticalGridView extends androidx.leanback.widget.VerticalGridView {

    public LeanBackVerticalGridView(@NonNull Context context) {
        super(context);
        init();
    }

    public LeanBackVerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LeanBackVerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setAnimation(null);
        setItemAnimator(null);
        setAnimationCacheEnabled(false);
        setNestedScrollingEnabled(false);
        setAnimateChildLayout(false);
        setHasFixedSize(true);
        setFocusableInTouchMode(false);
    }

    public final void scrollUp(int row) {
        while (true) {
            View focusedChild = getFocusedChild();
            if (null == focusedChild)
                break;
            int height = focusedChild.getHeight();
            scrollBy(0, -height);
            View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_UP);
            if (null == nextFocus)
                break;
            while (true) {
                ViewParent parent = nextFocus.getParent();
                if (parent instanceof LeanBackVerticalGridView) {
                    break;
                }
                nextFocus = (View) parent;
            }
            if (null == nextFocus)
                break;
            nextFocus.requestFocus();
            int position = getChildAdapterPosition(nextFocus);
            if (position == row)
                break;
        }
    }

    public final void scrollDown(int row) {
        while (true) {
            View focusedChild = getFocusedChild();
            if (null == focusedChild)
                break;
            int height = focusedChild.getHeight();
            scrollBy(0, height);
            View nextFocus = FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_UP);
            if (null == nextFocus)
                break;
            while (true) {
                ViewParent parent = nextFocus.getParent();
                if (parent instanceof LeanBackVerticalGridView) {
                    break;
                }
                nextFocus = (View) parent;
            }
            if (null == nextFocus)
                break;
            nextFocus.requestFocus();
            int position = getChildAdapterPosition(nextFocus);
            if (position == row)
                break;
        }
    }
}
