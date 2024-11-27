package lib.kalu.leanback.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.recyclerview.widget.RecyclerView;

class BaseVerticalGridView extends androidx.leanback.widget.VerticalGridView {
    public BaseVerticalGridView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseVerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseVerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
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

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        try {
            if (!(adapter instanceof ItemBridgeAdapter))
                throw new Exception("error: adapter not ItemBridgeAdapter");
            super.setAdapter(adapter);
            ((ItemBridgeAdapter) adapter).setAdapterListener(new ItemBridgeAdapter.AdapterListener() {
                @Override
                public void onAttachedToWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
                    try {
                        View itemView = viewHolder.itemView;
                        if (null == itemView)
                            throw new Exception("error: itemView null");
                        ViewParent parent = itemView.getParent();
                        if (null == parent)
                            throw new Exception("error: parent null");
                        boolean assignableFrom = BaseGridView.class.isAssignableFrom(parent.getClass());
                        if (!assignableFrom)
                            throw new Exception("error: assignableFrom false");
                        RecyclerView.LayoutManager layoutManager = getLayoutManager();
                        int position = layoutManager.getPosition(itemView);
                        onViewHolderAttachedToWindow(viewHolder, position);
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onDetachedFromWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
                    try {
                        View itemView = viewHolder.itemView;
                        if (null == itemView)
                            throw new Exception("error: itemView null");
                        ViewParent parent = itemView.getParent();
                        if (null == parent)
                            throw new Exception("error: parent null");
                        boolean assignableFrom = BaseGridView.class.isAssignableFrom(parent.getClass());
                        if (!assignableFrom)
                            throw new Exception("error: assignableFrom false");
                        RecyclerView.LayoutManager layoutManager = getLayoutManager();
                        int position = layoutManager.getPosition(itemView);
                        onViewHolderDetachedFromWindow(viewHolder, position);
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onBind(ItemBridgeAdapter.ViewHolder viewHolder) {
                    try {
                        View itemView = viewHolder.itemView;
                        if (null == itemView)
                            throw new Exception("error: itemView null");
                        ViewParent parent = itemView.getParent();
                        if (null == parent)
                            throw new Exception("error: parent null");
                        boolean assignableFrom = BaseGridView.class.isAssignableFrom(parent.getClass());
                        if (!assignableFrom)
                            throw new Exception("error: assignableFrom false");
                        RecyclerView.LayoutManager layoutManager = getLayoutManager();
                        int position = layoutManager.getPosition(itemView);
                        onViewHolderBind(viewHolder, position);
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onUnbind(ItemBridgeAdapter.ViewHolder viewHolder) {
                    try {
                        View itemView = viewHolder.itemView;
                        if (null == itemView)
                            throw new Exception("error: itemView null");
                        ViewParent parent = itemView.getParent();
                        if (null == parent)
                            throw new Exception("error: parent null");
                        boolean assignableFrom = BaseGridView.class.isAssignableFrom(parent.getClass());
                        if (!assignableFrom)
                            throw new Exception("error: assignableFrom false");
                        RecyclerView.LayoutManager layoutManager = getLayoutManager();
                        int position = layoutManager.getPosition(itemView);
                        onViewHolderUnbind(viewHolder, position);
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    public void onViewHolderAttachedToWindow(ItemBridgeAdapter.ViewHolder viewHolder, int position) {
    }

    public void onViewHolderDetachedFromWindow(ItemBridgeAdapter.ViewHolder viewHolder, int position) {
    }

    public void onViewHolderBind(ItemBridgeAdapter.ViewHolder viewHolder, int position) {
    }

    public void onViewHolderUnbind(ItemBridgeAdapter.ViewHolder viewHolder, int position) {
    }
}
