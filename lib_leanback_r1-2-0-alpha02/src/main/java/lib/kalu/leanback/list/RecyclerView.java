package lib.kalu.leanback.list;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.R;
import androidx.recyclerview.widget.GridLayoutManager;

public class RecyclerView extends androidx.recyclerview.widget.RecyclerView {
    public RecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setAnimation(null);
        setItemAnimator(null);
        setAnimationCacheEnabled(false);
        setNestedScrollingEnabled(false);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int keyCode = event.getKeyCode();
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                View nextFocusView;
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    // 通过findNextFocus获取下一个需要得到焦点的view
                    nextFocusView = findLeft();
                } else {
                    // 通过findNextFocus获取下一个需要得到焦点的view
                    nextFocusView = findRight();
                }
                // 如果获取失败（也就是说需要交给系统来处理焦点， 消耗掉事件，不让系统处理， 并让先前获取焦点的view获取焦点）
                if (null != nextFocusView) {
                    nextFocusView.requestFocus();
                    return true;
                }
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                View nextFocusView;
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    // 通过findNextFocus获取下一个需要得到焦点的view
                    nextFocusView = findUp();
                } else {
                    // 通过findNextFocus获取下一个需要得到焦点的view
                    nextFocusView = findDown();
                }
                // 如果获取失败（也就是说需要交给系统来处理焦点， 消耗掉事件，不让系统处理， 并让先前获取焦点的view获取焦点）
                if (null != nextFocusView) {
                    nextFocusView.requestFocus();
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private View findLeft() {
        View focusedView = getFocusedChild();  // 获取当前获得焦点的view
        return FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_LEFT);
    }

    private View findRight() {
        View focusedView = getFocusedChild();  // 获取当前获得焦点的view
        return FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_RIGHT);
    }

    private View findDown() {
        View focusedView = getFocusedChild();  // 获取当前获得焦点的view
        View nextFocusView = FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_DOWN);

        if (null == nextFocusView) {
            int span = 1;
            LayoutManager layoutManager = getLayoutManager();
            if (null != layoutManager && layoutManager instanceof GridLayoutManager) {
                span = ((GridLayoutManager) layoutManager).getSpanCount();
            }

            int position = getChildAdapterPosition(focusedView);
            int index = (position + 1);
            int count = getLayoutManager().getItemCount();
            int max = count % span;
            if (max <= 0) {
                max = span;
            }
            if (count - index > max) {
                scrollToPosition(position + span);
                SystemClock.sleep(10);
                nextFocusView = FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_DOWN);
            }
        }

        return nextFocusView;
    }

    private View findUp() {
        View focusedView = getFocusedChild();  // 获取当前获得焦点的view
        View nextFocusView = FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_UP);

        if (null == nextFocusView) {
            int span = 1;
            LayoutManager layoutManager = getLayoutManager();
            if (null != layoutManager && layoutManager instanceof GridLayoutManager) {
                span = ((GridLayoutManager) layoutManager).getSpanCount();
            }

            int position = getChildAdapterPosition(focusedView);
            int index = (position + 1);
            if (index > span) {
                scrollToPosition(position - span);
                SystemClock.sleep(10);
                nextFocusView = FocusFinder.getInstance().findNextFocus(this, focusedView, View.FOCUS_UP);
            }
        }

        return nextFocusView;
    }

    @Override
    public void scrollToPosition(int position) {
        smoothScrollToPosition(position);
    }

    @Override
    public void smoothScrollToPosition(int position) {
        try {
            super.smoothScrollToPosition(position);
        } catch (Exception e) {
        }
    }

    public void requestFocusChild(int position, @IdRes int id) {
        try {
            ViewHolder viewHolder = findViewHolderForAdapterPosition(position);
            viewHolder.itemView.findViewById(id).requestFocus();
        } catch (Exception e) {
        }
    }

    public void performClickChild(int position, @IdRes int id) {
        try {
            ViewHolder viewHolder = findViewHolderForAdapterPosition(position);
            viewHolder.itemView.findViewById(id).performClick();
        } catch (Exception e) {
        }
    }
}
