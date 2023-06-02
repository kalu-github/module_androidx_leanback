package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import lib.kalu.leanback.list.listener.OnBaseRecyclerViewChangeListener;
import lib.kalu.leanback.util.LeanBackUtil;

class BaseRecyclerView extends androidx.recyclerview.widget.RecyclerView {

    public BaseRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private final void init() {
        setAnimation(null);
        setItemAnimator(null);
        setAnimationCacheEnabled(false);
        setNestedScrollingEnabled(false);
        setHasFixedSize(true);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        try {
            super.onLayout(changed, l, t, r, b);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        try {
            return super.dispatchKeyEvent(event);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public final void smoothScrollToPosition(int position) {
        try {
            super.smoothScrollToPosition(position);
        } catch (Exception e) {
        }
    }

    public int getSpanCount() {
        try {
            return ((GridLayoutManager) getLayoutManager()).getSpanCount();
        } catch (Exception e) {
            return 1;
        }
    }

    public int findFocusedChildPosition() {
        try {
            View focusedView = getFocusedChild();
            if (null == focusedView)
                throw new Exception("focusedView error: null");
            while (true) {
                ViewParent parent = focusedView.getParent();
                if (parent instanceof BaseRecyclerView) {
                    int adapterPosition = getChildAdapterPosition(focusedView);
                    if (adapterPosition < 0)
                        throw new Exception("adapterPosition error: " + adapterPosition);
                    return adapterPosition;
                } else {
                    focusedView = (View) parent;
                }
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public int getAdapterItemCount() {
        try {
            Adapter adapter = getAdapter();
            if (null == adapter)
                throw new Exception("adapter error: null");
            return adapter.getItemCount();
        } catch (Exception e) {
            return 0;
        }
    }

    public void scrollFocusedChild(int direction) {
    }

    public void scrollTop(boolean hasFocus) {
    }

    public void scrollBottom(boolean hasFocus) {
    }

    public void leave(int direction) {
    }

    public void into(int direction) {
    }

    /******************/

    private OnBaseRecyclerViewChangeListener mOnBaseRecyclerViewChangeListener;

    public final void setOnBaseRecyclerViewChangeListener(@NonNull OnBaseRecyclerViewChangeListener listener) {
        this.mOnBaseRecyclerViewChangeListener = listener;
    }

    /********/

    protected void addLoadmoreListener(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            try {
                if (null == mOnBaseRecyclerViewChangeListener)
                    throw new Exception();
                int itemCount = getAdapterItemCount();
                if (itemCount <= 0)
                    throw new Exception();
                int focusedChildPosition = findFocusedChildPosition();
                if (focusedChildPosition < 0)
                    throw new Exception();
                int spanCount = getSpanCount();
                if (spanCount <= 1) {
                    if (focusedChildPosition + 1 >= itemCount) {
                        mOnBaseRecyclerViewChangeListener.onLoadmore();
                    }
                } else {
                    int value = focusedChildPosition % spanCount;
                    if (value == 0) {
                        if (itemCount - focusedChildPosition <= spanCount) {
                            mOnBaseRecyclerViewChangeListener.onLoadmore();
                        }
                    } else {
                        if (itemCount - focusedChildPosition <= value) {
                            mOnBaseRecyclerViewChangeListener.onLoadmore();
                        }
                    }
                }
            } catch (Exception e) {
                LeanBackUtil.log("BaseRecyclerView => addLoadmoreListener => " + e.getMessage());
            }
        }
    }

    protected void leaveScrollFocusedChild() {
        try {
            View focusedChild = getFocusedChild();
            if (null == focusedChild)
                throw new Exception();
            while (true) {
                ViewParent parent = focusedChild.getParent();
                if (parent instanceof RecyclerViewGrid)
                    break;
                focusedChild = (View) parent;
            }
            int position = getChildAdapterPosition(focusedChild);
            scrollToPosition(position);
        } catch (Exception e) {
            LeanBackUtil.log("BaseRecyclerView => leaveScrollFocusedChild => " + e.getMessage());
        }
    }
}
