package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import lib.kalu.leanback.list.listener.OnBaseRecyclerViewChangeListener;
import lib.kalu.leanback.util.LeanBackUtil;

class BaseRecyclerView extends androidx.recyclerview.widget.RecyclerView {

    /******************/

    private OnBaseRecyclerViewChangeListener mOnBaseRecyclerViewChangeListener;

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

    private void init() {
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

    public boolean fastScrollToPosition(int position) {
        return false;
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

    protected void fastScrollRange(int direction) {
        try {
            if (direction != View.FOCUS_UP && direction != View.FOCUS_DOWN)
                throw new Exception("direction error: " + direction);
            LayoutManager layoutManager = getLayoutManager();
            if (null == layoutManager)
                throw new Exception("layoutManager error: null");
            int firstPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            if (firstPosition < 0)
                throw new Exception("firstPosition error: " + firstPosition);
            ViewHolder viewHolder = findViewHolderForAdapterPosition(firstPosition);
            if (null == viewHolder)
                throw new Exception("viewHolder error: null");
            if (null == viewHolder.itemView)
                throw new Exception("viewHolder.itemView error: null");
            int measuredHeight = viewHolder.itemView.getMeasuredHeight();
            if (measuredHeight <= 0)
                throw new Exception("measuredHeight error: " + measuredHeight);
            scrollBy(0, direction == View.FOCUS_UP ? -measuredHeight : measuredHeight);
        } catch (Exception e) {
            LeanBackUtil.log("BaseRecyclerView => fastScrollChild => " + e.getMessage());
        }
    }

    /*****************/

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        LeanBackUtil.log("BaseRecyclerView => onScrollStateChanged => state = " + state);
        switch (state) {
            case SCROLL_STATE_IDLE: //当屏幕停止滚动，加载图片
                try {
                    Glide.with(getContext()).resumeRequests();
                    LeanBackUtil.log("BaseRecyclerView => onScrollStateChanged => resumeRequests => succ");
                } catch (Exception e) {
                    LeanBackUtil.log("BaseRecyclerView => onScrollStateChanged => resumeRequests => fail");
                }
                break;
            case SCROLL_STATE_DRAGGING: //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
            case SCROLL_STATE_SETTLING: //由于用户的操作，屏幕产生惯性滑动，停止加载图片
            default:
                try {
                    Glide.with(getContext()).pauseRequests();
                    LeanBackUtil.log("BaseRecyclerView => onScrollStateChanged => pauseRequests => succ");
                } catch (Exception e) {
                    LeanBackUtil.log("BaseRecyclerView => onScrollStateChanged => pauseRequests => fail");
                }
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        try {
//            int repeatCount = event.getRepeatCount();
//            LeanBackUtil.log("BaseRecyclerView => dispatchKeyEvent => repeatCount = " + repeatCount);
//            if (repeatCount <= 0) {
//                try {
//                    Glide.with(getContext()).resumeRequests();
//                    LeanBackUtil.log("BaseRecyclerView => dispatchKeyEvent => resumeRequests => succ");
//                } catch (Exception e) {
//                    LeanBackUtil.log("BaseRecyclerView => dispatchKeyEvent => resumeRequests => fail");
//                }
//            } else {
//                try {
//                    Glide.with(getContext()).pauseRequests();
//                    LeanBackUtil.log("BaseRecyclerView => dispatchKeyEvent => pauseRequests => succ");
//                } catch (Exception e) {
//                    LeanBackUtil.log("BaseRecyclerView => dispatchKeyEvent => pauseRequests => fail");
//                }
//            }
//        } catch (Exception e) {
//        }
        try {
            return super.dispatchKeyEvent(event);
        } catch (Exception e) {
            return false;
        }
    }
}
