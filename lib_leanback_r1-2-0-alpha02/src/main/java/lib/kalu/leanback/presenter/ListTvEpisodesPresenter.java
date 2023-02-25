package lib.kalu.leanback.presenter;

import android.content.Context;
import android.graphics.Rect;
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
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvEpisodesPresenter<T extends ListTvEpisodesPresenter.ItemBean> extends Presenter implements ListTvPresenterImpl {

    private int mRangeCheckedIndex = 0;

    private int mEpisodeCheckedIndex = 0;
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
                item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            mEpisodeCheckedIndex = findEpisodeIndexOf(v);
                        }
                    }
                });
                item.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // left
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            try {
                                if (mEpisodeCheckedIndex <= 0) {
                                    if (mRangeCheckedIndex > 0) {
                                        mEpisodeCheckedIndex = num - 1;
                                        requestEpisodeChild(view);
                                        mRangeCheckedIndex -= 1;
                                        updateEpisodeAdapter(context, view);
                                    }
                                    return true;
                                }
                            } catch (Exception e) {
                                LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => onKey => left => " + e.getMessage());
                            }
                        }
                        // right
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            try {
                                int rangeCount = getRangeCount();
                                if (mEpisodeCheckedIndex + 1 >= num) {
                                    if (mRangeCheckedIndex + 1 < rangeCount) {
                                        mEpisodeCheckedIndex = 0;
                                        requestEpisodeChild(view);
                                        mRangeCheckedIndex += 1;
                                        updateEpisodeAdapter(context, view);
                                    }
                                    return true;
                                }
                            } catch (Exception e) {
                                LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => onKey => right => " + e.getMessage());
                            }
                        }
                        // down
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            mEpisodeCheckedIndex = findEpisodeIndexOf(v);
                            RecyclerView recyclerView = view.findViewById(R.id.lb_list_tv_episodes_ranges);
                            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(mRangeCheckedIndex);
                            viewHolder.itemView.requestFocus();
                            return true;
                        }
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => " + e.getMessage(), e);
        }
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
                            onCreateViewHolderRange(context, item);
                            RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(item) {
                            };
                            item.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View v, int keyCode, KeyEvent event) {
                                    // up
                                    if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                                        requestEpisodeChild(view);
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
                                            mRangeCheckedIndex = position;
                                            updateEpisodeAdapter(context, view);
                                        }
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
                            T t = getKeyIndex(position);
                            onBindViewHolderRange(context, holder.itemView, t, position);
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

    private void updateEpisodeAdapter(Context context, View view) {
        try {
            List<T> list = null;
            int index = 0;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (index == mRangeCheckedIndex) {
                    if (null != entry.getValue() && entry.getValue().size() > 0) {
                        list = entry.getValue();
                    }
                    break;
                }
                index += 1;
            }
            LinearLayout layout = view.findViewById(R.id.lb_list_tv_episodes_items);
            int count = layout.getChildCount();
            int size = list.size();
            for (int i = 0; i < count; i++) {
                View child = layout.getChildAt(i);
                if (null == child) continue;
                if (i + 1 >= size) {
                    child.setVisibility(View.INVISIBLE);
                    continue;
                }
                child.setVisibility(View.VISIBLE);
                T t = list.get(i);
                onBindViewHolderEpisode(context, child, t, i);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeAdapter => " + e.getMessage(), e);
            throw e;
        }
    }

    private void requestEpisodeChild(View view) {
        try {
            LinearLayout layout = view.findViewById(R.id.lb_list_tv_episodes_items);
            View childAt = layout.getChildAt(mEpisodeCheckedIndex);
            childAt.requestFocus();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => requestEpisodeChild => " + e.getMessage(), e);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

        // 数据
        try {

            if(null == item || !(item instanceof List))
                throw new Exception("date error: "+item);

            mData.clear();
            int episodeNum = initEpisodeNum();
            List<T> list = (List<T>) item;
            int size = list.size();
            for (int i = 0; i < size; i += episodeNum) {

                // map-key
                ItemBean key = new ItemBean();
                int start = (i / episodeNum) * episodeNum + 1;
                int end = start + (episodeNum-1);
                if (end >= size) {
                    end = size;
                }
                key.setRangeName(start + "-" + end);

                // map-value
                ArrayList<T> value = new ArrayList<>();
                if (end >= size) {
                    end -= 1;
                }
                for (int j = (start - 1); j <= end; j++) {
                    T t = list.get(j);
                    if (null == t) continue;
                    t.setHead("选集列表");
                    value.add(t);
                }

                // map
                mData.put((T) key, value);
            }

        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => onBindViewHolder => " + e.getMessage(), e);
        }

        // 标题
        try {
            String s = null;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (null == s || s.length() <= 0) {
                    T key = entry.getKey();
                    if (null != key) {
                        s = key.getHead();
                    }
                }
                if (null == s || s.length() <= 0) {
                    List<T> value = entry.getValue();
                    if (null != value && value.size() > 0) {
                        T t = value.get(0);
                        if (null != t) {
                            s = t.getHead();
                        }
                    }
                }
                break;
            }
            if (null != s && s.length() > 0) {
                TextView textView = viewHolder.view.findViewById(R.id.lb_list_tv_episodes_head);
                textView.setText(s);
                textView.setVisibility(View.VISIBLE);
            }
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

    private T getKeyIndex(int key) throws Exception {
        try {
            int index = 0;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (index == key) {
                    return entry.getKey();
                }
                index += 1;
            }
            throw new Exception("not find error: " + key);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => getItemKey => " + e.getMessage(), e);
            throw e;
        }
    }

    private List<T> getValueIndex(int key) throws Exception {
        try {
            int index = 0;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (index == key) {
                    return entry.getValue();
                }
                index += 1;
            }
            throw new Exception("not find error: " + key);
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

    @LayoutRes
    protected abstract int initRangeLayout();

    protected abstract void onCreateViewHolderRange(@NonNull Context context, @NonNull View view);

    protected abstract void onBindViewHolderRange(@NonNull Context context, @NonNull View view, @NonNull T item, @NonNull int position);

    protected abstract void onCreateViewHolderEpisode(@NonNull Context context, @NonNull View view, @NonNull T item, @NonNull int position);

    protected abstract void onBindViewHolderEpisode(@NonNull Context context, @NonNull View view, @NonNull T item, @NonNull int position);

    @Keep
    public static class ItemBean {

        private String head = null;
        private String rangeName;

        public String getRangeName() {
            return rangeName;
        }

        public void setRangeName(String rangeName) {
            this.rangeName = rangeName;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }
    }
}
