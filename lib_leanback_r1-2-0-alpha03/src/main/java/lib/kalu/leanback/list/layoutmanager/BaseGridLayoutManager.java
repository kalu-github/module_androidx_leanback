package lib.kalu.leanback.list.layoutmanager;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseGridLayoutManager extends androidx.recyclerview.widget.GridLayoutManager {

    public BaseGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public BaseGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return false;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        }catch (Exception e){
        }
    }

    @Override
    public void onMeasure(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state, int widthSpec, int heightSpec) {
        try {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        }catch (Exception e){
        }
    }
}
