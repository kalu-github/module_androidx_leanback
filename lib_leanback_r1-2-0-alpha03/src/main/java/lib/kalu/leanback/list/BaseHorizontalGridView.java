package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import lib.kalu.leanback.util.LeanBackUtil;

class BaseHorizontalGridView extends androidx.leanback.widget.HorizontalGridView {
    public BaseHorizontalGridView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseHorizontalGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseHorizontalGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
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
    }

    /*****************/

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        LeanBackUtil.log("BaseHorizontalGridView => onScrollStateChanged => state = " + state);
        switch (state) {
            case SCROLL_STATE_IDLE: //当屏幕停止滚动，加载图片
                try {
                    Glide.with(getContext()).resumeRequests();
                    LeanBackUtil.log("BaseHorizontalGridView => onScrollStateChanged => resumeRequests => succ");
                } catch (Exception e) {
                    LeanBackUtil.log("BaseHorizontalGridView => onScrollStateChanged => resumeRequests => fail");
                }
                break;
            case SCROLL_STATE_DRAGGING: //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
            case SCROLL_STATE_SETTLING: //由于用户的操作，屏幕产生惯性滑动，停止加载图片
            default:
                try {
                    Glide.with(getContext()).pauseRequests();
                    LeanBackUtil.log("BaseHorizontalGridView => onScrollStateChanged => pauseRequests => succ");
                } catch (Exception e) {
                    LeanBackUtil.log("BaseHorizontalGridView => onScrollStateChanged => pauseRequests => fail");
                }
                break;
        }
    }

    public final ViewHolder findViewHolderAtFirst(@NonNull Class<?> cls) {
        try {
            String simpleName = cls.getName();
            if (null == simpleName || simpleName.length() == 0)
                throw new Exception("simpleName error: " + simpleName);
            ItemBridgeAdapter itemBridgeAdapter = (ItemBridgeAdapter) getAdapter();
            if (null == itemBridgeAdapter)
                throw new Exception("itemBridgeAdapter error: null");
            ArrayObjectAdapter objectAdapter = (ArrayObjectAdapter) itemBridgeAdapter.getAdapter();
            if (null == objectAdapter)
                throw new Exception("objectAdapter error: null");
            int size = objectAdapter.size();
            if (size <= 0)
                throw new Exception("size error: " + size);
            for (int i = 0; i < size; i++) {
                Object o = objectAdapter.get(i);
                if (null == o)
                    continue;
                String oName = o.getClass().getName();
                if (!simpleName.equals(oName))
                    continue;
                ViewHolder forAdapterPosition = findViewHolderForAdapterPosition(i);
                if (null != forAdapterPosition) {
                    return forAdapterPosition;
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("BaseGridView => findViewHolderAtFirst => " + e.getMessage());
            return null;
        }
    }

    public final List<ViewHolder> findViewHolders(@NonNull Class<?> cls) {
        try {
            String simpleName = cls.getName();
            if (null == simpleName || simpleName.length() == 0)
                throw new Exception("simpleName error: " + simpleName);
            ItemBridgeAdapter itemBridgeAdapter = (ItemBridgeAdapter) getAdapter();
            if (null == itemBridgeAdapter)
                throw new Exception("itemBridgeAdapter error: null");
            ArrayObjectAdapter objectAdapter = (ArrayObjectAdapter) itemBridgeAdapter.getAdapter();
            if (null == objectAdapter)
                throw new Exception("objectAdapter error: null");
            int size = objectAdapter.size();
            if (size <= 0)
                throw new Exception("size error: " + size);
            ArrayList<ViewHolder> result = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Object o = objectAdapter.get(i);
                if (null == o)
                    continue;
                String oName = o.getClass().getName();
                if (!simpleName.equals(oName))
                    continue;
                ViewHolder forAdapterPosition = findViewHolderForAdapterPosition(i);
                if (null == forAdapterPosition)
                    continue;
                result.add(forAdapterPosition);
            }
            return result;
        } catch (Exception e) {
            LeanBackUtil.log("BaseGridView => findViewHolders => " + e.getMessage());
            return null;
        }
    }

    @Deprecated
    public final <T extends Presenter> T findPresenterFirst(Class<T> cls) {
        try {
            String simpleName = cls.getName();
            if (null == simpleName || simpleName.length() == 0)
                throw new Exception("simpleName error: " + simpleName);
            ItemBridgeAdapter itemBridgeAdapter = (ItemBridgeAdapter) getAdapter();
            if (null == itemBridgeAdapter)
                throw new Exception("itemBridgeAdapter error: null");
            ArrayObjectAdapter objectAdapter = (ArrayObjectAdapter) itemBridgeAdapter.getAdapter();
            if (null == objectAdapter)
                throw new Exception("objectAdapter error: null");
            Presenter[] presenters = objectAdapter.getPresenterSelector().getPresenters();
            if (null == presenters || presenters.length == 0)
                throw new Exception("presenters error: " + presenters);
            for (Presenter o : presenters) {
                if (null == o)
                    continue;
                if (simpleName.equals(o.getClass().getName())) {
                    return (T) o;
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("BaseGridView => findPresenterFirst => " + e.getMessage());
            return null;
        }
    }

    @Deprecated
    public final <T extends Presenter> List<T> findPresenterAll(Class<T> cls) {
        try {
            String simpleName = cls.getName();
            if (null == simpleName || simpleName.length() == 0)
                throw new Exception("simpleName error: " + simpleName);
            ItemBridgeAdapter itemBridgeAdapter = (ItemBridgeAdapter) getAdapter();
            if (null == itemBridgeAdapter)
                throw new Exception("itemBridgeAdapter error: null");
            ArrayObjectAdapter objectAdapter = (ArrayObjectAdapter) itemBridgeAdapter.getAdapter();
            if (null == objectAdapter)
                throw new Exception("objectAdapter error: null");
            Presenter[] presenters = objectAdapter.getPresenterSelector().getPresenters();
            if (null == presenters || presenters.length == 0)
                throw new Exception("presenters error: " + presenters);
            ArrayList<T> result = new ArrayList<>();
            for (Presenter o : presenters) {
                if (null == o)
                    continue;
                if (!simpleName.equals(o.getClass().getName()))
                    continue;
                result.add((T) o);
            }
            return result;
        } catch (Exception e) {
            LeanBackUtil.log("BaseGridView => findPresenterFirst => " + e.getMessage());
            return null;
        }
    }

    @Deprecated
    public final ViewHolder findViewHolderForAdapterObjectFirst(@NonNull Class<?> cls) {
        try {
            String simpleName = cls.getName();
            if (null == simpleName || simpleName.length() == 0)
                throw new Exception("simpleName error: " + simpleName);
            ItemBridgeAdapter itemBridgeAdapter = (ItemBridgeAdapter) getAdapter();
            if (null == itemBridgeAdapter)
                throw new Exception("itemBridgeAdapter error: null");
            ArrayObjectAdapter objectAdapter = (ArrayObjectAdapter) itemBridgeAdapter.getAdapter();
            if (null == objectAdapter)
                throw new Exception("objectAdapter error: null");
            int size = objectAdapter.size();
            if (size <= 0)
                throw new Exception("size error: " + size);
            for (int i = 0; i < size; i++) {
                Object o = objectAdapter.get(i);
                if (null == o)
                    continue;
                String oName = o.getClass().getName();
                if (!simpleName.equals(oName))
                    continue;
                ViewHolder forAdapterPosition = findViewHolderForAdapterPosition(i);
                if (null != forAdapterPosition) {
                    return forAdapterPosition;
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("BaseGridView => findViewHolderForAdapterObjectFirst => " + e.getMessage());
            return null;
        }
    }

    @Deprecated
    public final List<ViewHolder> findViewHolderForAdapterObjectAll(@NonNull Class<?> cls) {
        try {
            String simpleName = cls.getName();
            if (null == simpleName || simpleName.length() == 0)
                throw new Exception("simpleName error: " + simpleName);
            ItemBridgeAdapter itemBridgeAdapter = (ItemBridgeAdapter) getAdapter();
            if (null == itemBridgeAdapter)
                throw new Exception("itemBridgeAdapter error: null");
            ArrayObjectAdapter objectAdapter = (ArrayObjectAdapter) itemBridgeAdapter.getAdapter();
            if (null == objectAdapter)
                throw new Exception("objectAdapter error: null");
            int size = objectAdapter.size();
            if (size <= 0)
                throw new Exception("size error: " + size);
            ArrayList<ViewHolder> result = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Object o = objectAdapter.get(i);
                if (null == o)
                    continue;
                String oName = o.getClass().getName();
                if (!simpleName.equals(oName))
                    continue;
                ViewHolder forAdapterPosition = findViewHolderForAdapterPosition(i);
                if (null == forAdapterPosition)
                    continue;
                result.add(forAdapterPosition);
            }
            return result;
        } catch (Exception e) {
            LeanBackUtil.log("BaseGridView => findViewHolderForAdapterObjectAll => " + e.getMessage());
            return null;
        }
    }
}
