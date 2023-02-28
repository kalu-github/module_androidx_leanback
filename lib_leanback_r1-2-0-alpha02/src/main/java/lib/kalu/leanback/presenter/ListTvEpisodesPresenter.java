package lib.kalu.leanback.presenter;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lib.kalu.leanback.list.LeanBackVerticalGridView;
import lib.kalu.leanback.list.RecyclerViewHorizontal;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvEpisodesPresenter<T extends ListTvEpisodesPresenter.ItemBean> extends Presenter implements ListTvPresenterImpl {

    private int mPlayingIndexRange = 0;
    private int mPlayingIndexEpisode = 0;

    private int mCheckedIndexRange = 0;
    private int mCheckedIndexEpisode = 0;

    private final LinkedHashMap<T, List<T>> mData = new LinkedHashMap<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            View inflate = LayoutInflater.from(context).inflate(R.layout.lb_list_tv_episodes, parent, false);
            initHead(context, inflate, R.id.lb_list_tv_episodes_head);
            initAdapter1(context, inflate);
            initAdapter2(context, inflate);
            return new ViewHolder(inflate);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => onCreateViewHolder => " + e.getMessage(), e);
            return null;
        }
    }

    private final void initAdapter1(@NonNull Context context, @NonNull View view) {
        try {
            // 1
            LinearLayout layout = view.findViewById(R.id.lb_list_tv_episodes_items);
            layout.removeAllViews();
            if (null != layout.getLayoutParams()) {
                int episodeMarginBottom = initEpisodeMarginBottom(context);
                ((LinearLayout.LayoutParams) layout.getLayoutParams()).leftMargin = 0;
                ((LinearLayout.LayoutParams) layout.getLayoutParams()).rightMargin = 0;
                ((LinearLayout.LayoutParams) layout.getLayoutParams()).topMargin = 0;
                ((LinearLayout.LayoutParams) layout.getLayoutParams()).bottomMargin = episodeMarginBottom;
            }
            // 2
            int num = initEpisodeNum();
            layout.setWeightSum(num);
            // 3
            int episodePadding = initEpisodePadding(context);
            // 2 剧集
            for (int i = 0; i < num; i++) {
                View item = LayoutInflater.from(context).inflate(initEpisodeLayout(), layout, false);
                layout.addView(item);
                if (null != item.getLayoutParams()) {
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).weight = 1;
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).leftMargin = (i == 0 ? 0 : episodePadding);
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).rightMargin = (i + 1 == num ? 0 : episodePadding);
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).bottomMargin = 0;
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).topMargin = 0;
                }
//                onCreateViewHolderEpisode(context, item, i);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 1
                        callEpisodeItem(v, false, true);
                        // 2
                        int index = findEpisodeIndexOf(v);
                        updateEpisodePlayingIndex(index);
                        updateEpisodeCheckedIndex(index);
                        updateRangePlayingIndex();
                        // 4
                        try {
                            List<T> list = getCheckedEpisodeData();
                            T t = list.get(index);
                            onClickEpisode(v.getContext(), v, t, index);
                        } catch (Exception e) {
                        }
                    }
                });
                item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            int indexOf = findEpisodeIndexOf(v);
                            updateEpisodeCheckedIndex(indexOf);
                        }
                        callEpisodeItem(v, hasFocus, false);
                    }
                });
                item.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => onKey => action = " + event.getAction());
                        LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => onKey => code = " + keyCode);

                        // left
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            try {
                                // 剧集 - 是否显示第一个
                                boolean episodeFirst = isEpisodeFirst();
                                if (episodeFirst) {
                                    // 剧集区间 - 是否显示第一个
                                    boolean rangeFirst = isRangeFirst();
                                    if (!rangeFirst) {
                                        // 2
                                        updateEpisodeCheckedIndex(num - 1);
                                        // 3
                                        updateRangeCheckedIndex(mCheckedIndexRange - 1);
                                        // 4
                                        updateEpisodeAdapter(context, view);
                                        // 5
                                        requestFocusEpisodeCheckedItem(view);
                                    }
                                    return true;
                                }
                            } catch (Exception e) {
                                LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => onKey => left => " + e.getMessage());
                                return true;
                            }
                        }
                        // right
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            try {
                                // 剧集 - 是否显示最后一个
                                boolean episodeLast = isEpisodeLast();
                                if (episodeLast) {
                                    // 剧集区间 - 是否显示最后一个
                                    boolean rangeLast = isRangeLast();
                                    if (!rangeLast) {
                                        // 2
                                        updateEpisodeCheckedIndex(0);
                                        // 3
                                        updateRangeCheckedIndex(mCheckedIndexRange + 1);
                                        // 4
                                        updateEpisodeAdapter(context, view);
                                        // 5
                                        requestFocusEpisodeCheckedItem(view);
                                    }
                                    return true;
                                }
                            } catch (Exception e) {
                                LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => onKey => right => " + e.getMessage());
                                return true;
                            }
                        }
                        // down
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => onKey => down =>");
                            // 1
                            callEpisodeItem(v, false, false);
                            // 2
                            updateEpisodeCheckedIndex(mPlayingIndexEpisode);
                            // 3
                            RecyclerView recyclerView = view.findViewById(R.id.lb_list_tv_episodes_ranges);
                            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(mCheckedIndexRange);
                            viewHolder.itemView.requestFocus();
                            return true;
                        }
                        // up
//                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
//                            LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => onKey => up =>");
//                            callEpisodeItem(v, false, false);
//                        }
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => " + e.getMessage(), e);
        }
    }

    private void updateRangeCheckedIndex(int index) {
        mCheckedIndexRange = index;
    }

    private void updateEpisodeCheckedIndex(int index) {
        mCheckedIndexEpisode = index;
    }

    private void updateEpisodePlayingIndex(int index) {
        mPlayingIndexEpisode = index;
    }

    private void updateRangePlayingIndex() {
        mPlayingIndexRange = mCheckedIndexRange;
    }

    private int findEpisodeIndexOf(View v) {
        int index = -1;
        while (true) {
            if (null == v) break;
            ViewParent parent = v.getParent();
            if (null == parent) break;
            int id = ((ViewGroup) parent).getId();
            if (id == R.id.lb_list_tv_episodes_items) {
                index = ((ViewGroup) parent).indexOfChild(v);
                break;
            }
            return findEpisodeIndexOf((View) parent);
        }
        return index;
    }

    private final void initAdapter2(@NonNull Context context, @NonNull View view) {
        try {
            RecyclerView recyclerView = view.findViewById(R.id.lb_list_tv_episodes_ranges);
            if (null == recyclerView.getLayoutManager()) {
                LinearLayoutManager manager = new LinearLayoutManager(context);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(manager);
                int rangePadding = initRangePadding(context);
                int rangeMarginTop = initRangeMarginTop(context);
                if (rangePadding > 0 || rangeMarginTop > 0) {
                    recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                        @Override
                        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                            super.getItemOffsets(outRect, view, parent, state);
                            outRect.set(0, rangeMarginTop, rangePadding, 0);
                        }
                    });
                }
            }
            if (null == recyclerView.getAdapter()) {
                recyclerView.setAdapter(new RecyclerView.Adapter() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        try {
                            Context context = parent.getContext();
                            View item = LayoutInflater.from(context).inflate(initRangeLayout(), parent, false);
                            RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(item) {
                            };
                            item.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View v, int keyCode, KeyEvent event) {

                                    // up
                                    if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                                        requestFocusEpisodeCheckedItem(view);
                                        return true;
                                    }
                                    return false;
                                }
                            });
                            item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    if (hasFocus) {
                                        int position = holder.getAbsoluteAdapterPosition();
                                        if (position >= 0) {
                                            mCheckedIndexRange = position;
                                            updateEpisodeAdapter(context, view);
                                        }
                                    }
                                    try {
                                        T t = getCheckedRangeData();
                                        onFocusChangeRange(context, v, t, mCheckedIndexRange, hasFocus, mCheckedIndexRange == mPlayingIndexRange);
                                    } catch (Exception e) {
                                    }
                                }
                            });
                            return holder;
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter2 => onCreateViewHolder => " + e.getMessage(), e);
                            return null;
                        }
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        try {
                            T t = getRangeData(position);
                            onBindViewHolderRange(context, holder.itemView, t, position, position == mPlayingIndexRange);
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter2 => onBindViewHolder => " + e.getMessage(), e);
                        }
                    }

                    @Override
                    public int getItemCount() {
                        return getRangeCount();
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter2 => " + e.getMessage(), e);
        }
    }

    private boolean isEpisodeFirst() {
        try {
            return mCheckedIndexEpisode <= 0;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => isEpisodeFirst => " + e.getMessage(), e);
            throw e;
        }
    }

    private boolean isEpisodeLast() throws Exception {
        try {
            List<T> list = getCheckedEpisodeData();
            int size = list.size();
            return mCheckedIndexEpisode + 1 >= size;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => isEpisodeLast => " + e.getMessage(), e);
            throw e;
        }
    }

    private boolean isRangeFirst() {
        try {
            return mCheckedIndexRange <= 0;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => isRangeFirst => " + e.getMessage(), e);
            throw e;
        }
    }

    private boolean isRangeLast() throws Exception {
        try {
            int rangeCount = getRangeCount();
            return mCheckedIndexRange + 1 >= rangeCount;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => isRangeLast => " + e.getMessage(), e);
            throw e;
        }
    }

    private void updateEpisodeAdapter(Context context, View view) {
        try {
            List<T> list = null;
            int index = 0;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (index == mCheckedIndexRange) {
                    if (null != entry.getValue() && entry.getValue().size() > 0) {
                        list = entry.getValue();
                    }
                    break;
                }
                index += 1;
            }
            LinearLayout layout = view.findViewById(R.id.lb_list_tv_episodes_items);
            int max = layout.getChildCount();
            int num = list.size();
            for (int i = 0; i < max; i++) {
                View child = layout.getChildAt(i);
                if (null == child)
                    continue;
                child.setVisibility(i + 1 <= num ? View.VISIBLE : View.INVISIBLE);
                if (i + 1 > num)
                    continue;
                T t = list.get(i);
                if (null == t)
                    continue;
                LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeAdapter => i = " + i);
                LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeAdapter => mPlayingIndexEpisode = " + mPlayingIndexEpisode);
                LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeAdapter => mCheckedIndexRange = " + mCheckedIndexRange);
                LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeAdapter => mPlayingIndexRange = " + mPlayingIndexRange);
                onBindViewHolderEpisode(context, child, t, i, i == mPlayingIndexEpisode && mCheckedIndexRange == mPlayingIndexRange);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeAdapter => " + e.getMessage(), e);
            throw e;
        }
    }

    private void requestFocusEpisodeCheckedItem(View view) {
        try {
            List<T> list = getCheckedEpisodeData();
            int size = list.size();
            if (mCheckedIndexEpisode + 1 > size) {
                mCheckedIndexEpisode = size - 1;
            }
            LinearLayout layout = view.findViewById(R.id.lb_list_tv_episodes_items);
            View childAt = layout.getChildAt(mCheckedIndexEpisode);
            childAt.requestFocus();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => requestFocusEpisodeCheckedItem => " + e.getMessage(), e);
        }
    }

    private void callEpisodeItem(View v, boolean hasFocus, boolean isClick) {
        try {
            LeanBackUtil.log("ListTvEpisodesPresenter => callEpisodeItem => hasFocus = " + hasFocus + ", isClick = " + isClick);
            LeanBackUtil.log("ListTvEpisodesPresenter => callEpisodeItem => mCheckedIndexEpisode = " + mCheckedIndexEpisode);
            LeanBackUtil.log("ListTvEpisodesPresenter => callEpisodeItem => mPlayingIndexEpisode = " + mPlayingIndexEpisode);
            LeanBackUtil.log("ListTvEpisodesPresenter => callEpisodeItem => mCheckedIndexRange = " + mCheckedIndexRange);
            LeanBackUtil.log("ListTvEpisodesPresenter => callEpisodeItem => mPlayingIndexRange = " + mPlayingIndexRange);
            List<T> list = getCheckedEpisodeData();
            T t = list.get(isClick ? mPlayingIndexEpisode : mCheckedIndexEpisode);
            View child = ((ViewGroup) v.getParent()).getChildAt(isClick ? mPlayingIndexEpisode : mCheckedIndexEpisode);
            onFocusChangeEpisode(child.getContext(), child, t, isClick ? mPlayingIndexEpisode : mCheckedIndexEpisode, hasFocus, mCheckedIndexEpisode == mPlayingIndexEpisode && mCheckedIndexRange == mPlayingIndexRange);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => callEpisodeItem => " + e.getMessage(), e);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

        // 数据
        try {

            if (null == item || !(item instanceof List))
                throw new Exception("date error: " + item);

            mData.clear();
            int episodeNum = initEpisodeNum();
            List<T> list = (List<T>) item;
            int size = list.size();
            for (int i = 0; i < size; i += episodeNum) {

                // map-key
                Class<T> cls = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                T key = cls.newInstance();
                int start = (i / episodeNum) * episodeNum + 1;
                int end = start + (episodeNum - 1);
                if (end >= size) {
                    end = size;
                }
                key.setStart(start);
                key.setEnd(end);

                // map-value
                ArrayList<T> value = new ArrayList<>();
                for (int m = start - 1; m <= (end - 1); m++) {
                    T t = list.get(m);
                    if (null == t) continue;
                    value.add(t);
                }

                // map
                mData.put((T) key, value);
            }

        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => onBindViewHolder => " + e.getMessage(), e);
        }

        // head
        try {
            TextView textView = viewHolder.view.findViewById(R.id.lb_list_tv_episodes_head);
            textView.setText(initHead(viewHolder.view.getContext()));
            textView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => onBindViewHolder => " + e.getMessage(), e);
        }

        // 剧集
        updateEpisodeAdapter(viewHolder.view.getContext(), viewHolder.view);

        // 剧集区间
        try {
            RecyclerView recyclerView = viewHolder.view.findViewById(R.id.lb_list_tv_episodes_ranges);
            int size = mData.size();
            recyclerView.getAdapter().notifyItemRangeChanged(0, size);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => onBindViewHolder => " + e.getMessage(), e);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    protected String initHead(Context context) {
        return "test";
    }

    protected int initEpisodeNum() {
        return 10;
    }

    protected int initEpisodePadding(@NonNull Context context) {
        return 0;
    }

    protected int initEpisodeMarginBottom(@NonNull Context context) {
        return 0;
    }

    @LayoutRes
    protected abstract int initEpisodeLayout();

    protected int initRangePadding(@NonNull Context context) {
        return 0;
    }

    protected int initRangeMarginTop(@NonNull Context context) {
        return 0;
    }

    public T getCheckedRangeData() throws Exception {
        return getRangeData(mCheckedIndexRange);
    }

    private T getRangeData(int position) throws Exception {
        try {
            int index = 0;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (index == position) {
                    return entry.getKey();
                }
                index += 1;
            }
            throw new Exception("not find error: " + mCheckedIndexRange);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => getCheckedRangeData => " + e.getMessage(), e);
            throw e;
        }
    }

    public List<T> getCheckedEpisodeData() throws Exception {
        try {
            int index = 0;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (index == mCheckedIndexRange) {
                    return entry.getValue();
                }
                index += 1;
            }
            throw new Exception("not find error: " + mCheckedIndexRange);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => getItemKey => " + e.getMessage(), e);
            throw e;
        }
    }

    private int getRangeCount() {
        try {
            return mData.size();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => getItemCount => " + e.getMessage(), e);
            return -1;
        }
    }

    protected void onFocusChangeEpisode(@NonNull Context context, @NonNull View v, @NonNull T item, @NonNull int index, boolean hasFocus, boolean isPlayingIndex) {
    }

    protected void onClickEpisode(@NonNull Context context, @NonNull View v, @NonNull T item, @NonNull int position) {
    }

    protected void onFocusChangeRange(@NonNull Context context, @NonNull View v, @NonNull T item, @NonNull int position, boolean hasFocus, boolean isPlayingIndex) {
    }

    public void startPosition(Context context, BaseGridView viewGroup, int index) {

        int key = 0;
        int position = -1;
        int start = 0;
        for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
            List<T> value = entry.getValue();
            if (null == value)
                continue;
            int size = value.size();
            if (size <= 0)
                continue;
            int end = start + size;
            if (index >= start && index < end) {
                position = index - start;
                break;
            }
            key += 1;
        }

        if (key != -1) {
            updateRangeCheckedIndex(key);
            updateRangePlayingIndex();
            try {
                RecyclerViewHorizontal recyclerView = viewGroup.findViewById(R.id.lb_list_tv_episodes_ranges);
                recyclerView.scrollToPosition(mCheckedIndexEpisode);
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(mCheckedIndexRange);
                T t = getCheckedRangeData();
                onFocusChangeRange(context, viewHolder.itemView, t, mCheckedIndexRange, false, true);
            } catch (Exception e) {
            }
        }
        if (position != -1) {
            updateEpisodeCheckedIndex(position);
            updateEpisodePlayingIndex(position);
            try {
                LinearLayout linearLayout = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
                View child = linearLayout.getChildAt(position);
                List<T> list = getCheckedEpisodeData();
                T t = list.get(index);
                onClickEpisode(child.getContext(), child, t, index);
            } catch (Exception e) {
            }
        }
    }

    public void requestFocusEpisodePlayingItem(View view) {
        try {
            LinearLayout layout = view.findViewById(R.id.lb_list_tv_episodes_items);
            View childAt = layout.getChildAt(mPlayingIndexEpisode);
            childAt.requestFocus();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => requestFocusEpisodePlayingItem => " + e.getMessage(), e);
        }
    }

    protected void onBindViewHolderEpisode(@NonNull Context context, @NonNull View v, @NonNull T item, @NonNull int position, @NonNull boolean isPlayingIndex) {
    }

    protected void onBindViewHolderRange(@NonNull Context context, @NonNull View v, @NonNull T item, @NonNull int position, @NonNull boolean isPlayingIndex) {
    }

    @LayoutRes
    protected abstract int initRangeLayout();

    @Keep
    public static class ItemBean {

        public ItemBean() {
        }

        private int start;
        private int end;

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }
}
