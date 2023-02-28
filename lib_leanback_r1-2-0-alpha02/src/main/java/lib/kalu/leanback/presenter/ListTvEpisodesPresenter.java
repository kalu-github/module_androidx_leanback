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
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lib.kalu.leanback.list.RecyclerViewHorizontal;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvEpisodesPresenter<T extends ListTvEpisodesPresenter.ItemBean> extends Presenter implements ListTvPresenterImpl {

    private final LinkedHashMap<T, List<T>> mData = new LinkedHashMap<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            LinearLayout viewGroup = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.lb_list_tv_episodes, parent, false);
            initHead(context, viewGroup, R.id.lb_list_tv_episodes_head);
            initAdapter1(context, viewGroup);
            initAdapter2(context, viewGroup);
            return new ViewHolder(viewGroup);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => onCreateViewHolder => " + e.getMessage(), e);
            return null;
        }
    }

    private final void initAdapter1(@NonNull Context context, @NonNull LinearLayout viewGroup) {
        try {
            // 1
            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
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
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickEpisode(viewGroup, v);
                    }
                });
                item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        callEpisode(v, hasFocus, false);
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
                                // 区间 - 显示第一个
                                int checkedIndexRange = getCheckedIndexRange();
                                if (checkedIndexRange > 0) {
                                    // 剧集 - 显示第一个
                                    int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
                                    if (checkedIndexEpisode <= 0) {
                                        // 0
                                        cleanFocusChildOfEpisodeChecked(viewGroup);
                                        // 1
                                        int newCheckedIndexRange = checkedIndexRange - 1;
                                        updateRangeIndexChecked(newCheckedIndexRange);
                                        // 2
                                        int newCheckedIndexEpisode = num - 1;
                                        updateEpisodeIndexChecked(newCheckedIndexRange, newCheckedIndexEpisode);
                                        // 3
                                        updateEpisodeAdapter(context, viewGroup);
                                        // 4
                                        requestFocusChildOfEpisodeChecked(viewGroup);
                                        return true;
                                    } else {
                                        int newCheckedIndexEpisode = checkedIndexEpisode - 1;
                                        updateEpisodeIndexChecked(checkedIndexRange, newCheckedIndexEpisode);
                                    }
                                } else {
                                    int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
                                    if (checkedIndexEpisode <= 0)
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
                                // 区间 - 是否显示最后一个
                                int rangeLength = getRangeLength();
                                int checkedIndexRange = getCheckedIndexRange();
                                if (checkedIndexRange + 1 < rangeLength) {
                                    // 剧集 - 是否显示最后一个
                                    int size = getIndexOfEpisodeLength(checkedIndexRange);
                                    int episodeIndexOfChild = findEpisodeIndexOfChild(v);
                                    if (episodeIndexOfChild + 1 >= size) {
                                        // 0
                                        cleanFocusChildOfEpisodeChecked(viewGroup);
                                        // 1
                                        int newCheckedIndexRange = checkedIndexRange + 1;
                                        updateRangeIndexChecked(newCheckedIndexRange);
                                        // 2
                                        int newCheckedIndexEpisode = 0;
                                        updateEpisodeIndexChecked(newCheckedIndexRange, newCheckedIndexEpisode);
                                        // 3
                                        updateEpisodeAdapter(context, viewGroup);
                                        // 4
                                        requestFocusChildOfEpisodeChecked(viewGroup);
                                        return true;
                                    } else {
                                        int newCheckedIndexEpisode = episodeIndexOfChild + 1;
                                        updateEpisodeIndexChecked(checkedIndexRange, newCheckedIndexEpisode);
                                    }
                                } else {
                                    int size = getIndexOfEpisodeLength(checkedIndexRange);
                                    int episodeIndexOfChild = findEpisodeIndexOfChild(v);
                                    if (episodeIndexOfChild + 1 >= size)
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
                            requestFocusChildOfRangeChecked(viewGroup);
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

    private final void initAdapter2(@NonNull Context context, @NonNull LinearLayout viewGroup) {
        try {
            RecyclerView recyclerView = viewGroup.findViewById(R.id.lb_list_tv_episodes_ranges);
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
                            item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    try {
                                        int position = holder.getAbsoluteAdapterPosition();
                                        if (position < 0)
                                            throw new Exception("position error: " + position);
                                        callRange(v, position, hasFocus, false);
                                    } catch (Exception e) {
                                        LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter2 => onCreateViewHolder => onFocusChange => " + e.getMessage(), e);
                                    }
                                }
                            });
                            item.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View v, int keyCode, KeyEvent event) {
                                    LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter2 => onKey => action = " + event.getAction() + ", code = " + event.getKeyCode());

                                    // up
                                    if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                                        // 1
                                        cleanFocusChildOfRangeChecked(v);
                                        // 2
                                        requestFocusChildOfEpisodeChecked(viewGroup);
                                        return true;
                                    }
                                    // left
                                    else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                                        int checkedIndexRange = getCheckedIndexRange();
                                        LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter2 => onKey => left => checkedIndexRange = " + checkedIndexRange);
                                        if (checkedIndexRange > 0) {
                                            // 1
                                            int newCheckedIndexRange = checkedIndexRange - 1;
                                            updateRangeIndexChecked(newCheckedIndexRange);
                                            // 2
                                            updateEpisodeAdapter(context, viewGroup);
                                        }
                                    }
                                    // right
                                    else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                                        int checkedIndexRange = getCheckedIndexRange();
                                        int rangeLength = getRangeLength();
                                        LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter2 => onKey => right => checkedIndexRange = " + checkedIndexRange + ", rangeLength = " + rangeLength);
                                        if (checkedIndexRange + 1 < rangeLength) {
                                            // 1
                                            int newCheckedIndexRange = checkedIndexRange + 1;
                                            updateRangeIndexChecked(newCheckedIndexRange);
                                            // 2
                                            updateEpisodeAdapter(context, viewGroup);
                                        }
                                    }
                                    return false;
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
                            T t = getIndexOfRangeData(position);
                            boolean isPlaying = isPlayingIndexOfRange(position);
                            boolean isChecked = isCheckedIndexOfEpisode(position);
                            onBindViewHolderRange(holder.itemView.getContext(), holder.itemView, t, position, false, isPlaying, isChecked);
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter2 => onBindViewHolder => " + e.getMessage(), e);
                        }
                    }

                    @Override
                    public int getItemCount() {
                        return getRangeLength();
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter2 => " + e.getMessage(), e);
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
                key.setPlaying(i == 0);
                key.setChecked(i == 0);

                // map-value
                ArrayList<T> value = new ArrayList<>();
                for (int m = start - 1; m <= (end - 1); m++) {
                    T t = list.get(m);
                    if (null == t)
                        continue;
                    t.setPlaying(i == 0 && m == 0);
                    t.setChecked(i == 0 && m == 0);
                    value.add(t);
                }

                // map
                mData.put(key, value);
            }

        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => onBindViewHolder => " + e.getMessage(), e);
        }

        // log
        for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
            T t = entry.getKey();
            if (null != t) {
                LeanBackUtil.log("ListTvEpisodesPresenter => onBindViewHolder => " + t.toString());
            }
            List<T> value = entry.getValue();
            if (null != t) {
                LeanBackUtil.log("ListTvEpisodesPresenter => onBindViewHolder => " + value.toString());
            }
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
        updateEpisodeAdapter(viewHolder.view.getContext(), (LinearLayout) viewHolder.view);

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

    protected void onClickEpisode(@NonNull Context context, @NonNull View v, @NonNull T item, @NonNull int position) {
    }

    protected void onBindViewHolderEpisode(@NonNull Context context, @NonNull View v, @NonNull T item, @NonNull int position, boolean hasFocus, boolean isPlaying, boolean isChecked) {
    }

    protected void onBindViewHolderRange(@NonNull Context context, @NonNull View v, @NonNull T item, @NonNull int position, boolean hasFocus, boolean isPlaying, boolean isChecked) {
    }

    @LayoutRes
    protected abstract int initRangeLayout();

    /**********/

    @Keep
    public static class ItemBean {

        public ItemBean() {
        }

        private int start;
        private int end;
        private boolean checked = false; // 是否正在选中
        private boolean playing = false; // 是否正在播放

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public boolean isPlaying() {
            return playing;
        }

        public void setPlaying(boolean playing) {
            this.playing = playing;
        }

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

    /**********/

    private final List<T> getIndexOfEpisodeData(int position) throws Exception {
        try {
            int index = 0;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (index == position)
                    return entry.getValue();
                index += 1;
            }
            throw new Exception("not find error: " + index);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => getIndexOfEpisodeData => " + e.getMessage(), e);
            throw e;
        }
    }

    private final int getIndexOfEpisodeLength(int index) {
        try {
            List<T> list = getIndexOfEpisodeData(index);
            return list.size();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => getIndexOfEpisodeLength => " + e.getMessage(), e);
            return -1;
        }
    }

    private final int getCheckedIndexEpisode() {
        int checkedIndexRange = getCheckedIndexRange();
        return getCheckedIndexEpisode(checkedIndexRange);
    }

    private final int getCheckedIndexEpisode(int checkedIndexRange) {
        try {
            if (checkedIndexRange == -1)
                throw new Exception("indexRange error: " + checkedIndexRange);
            List<T> list = getIndexOfEpisodeData(checkedIndexRange);
            if (null == list)
                throw new Exception("list error: null");
            int size = list.size();
            if (size <= 0)
                throw new Exception("size error: " + size);
            int index = -1;
            for (int i = 0; i < size; i++) {
                T t = list.get(i);
                if (null == t)
                    continue;
                index += 1;
                boolean checked = t.isChecked();
                if (checked)
                    break;
            }
            if (index < 0)
                throw new Exception("index error: " + size);
            LeanBackUtil.log("ListTvEpisodesPresenter => getCheckedIndexEpisode => index = " + index);
            return index;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => getCheckedIndexEpisode => " + e.getMessage(), e);
            return -1;
        }
    }

    private final int getPlayingIndexEpisode() {
        try {
            int indexRange = getCheckedIndexRange();
            if (indexRange == -1)
                throw new Exception("indexRange error: " + indexRange);
            List<T> list = getIndexOfEpisodeData(indexRange);
            if (null == list)
                throw new Exception("list error: null");
            int size = list.size();
            if (size <= 0)
                throw new Exception("size error: " + size);
            int index = -1;
            for (int i = 0; i < size; i++) {
                T t = list.get(i);
                if (null == t)
                    continue;
                index += 1;
                boolean playing = t.isPlaying();
                if (playing)
                    break;
            }
            if (index < 0)
                throw new Exception("index error: " + size);
            LeanBackUtil.log("ListTvEpisodesPresenter => getPlayingIndexEpisode => index = " + index);
            return index;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => getPlayingIndexEpisode => " + e.getMessage(), e);
            return -1;
        }
    }

    private final int getCheckedIndexRange() {
        try {
            int index = -1;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (null == entry)
                    continue;
                T t = entry.getKey();
                if (null == t)
                    continue;
                index += 1;
                boolean checked = t.isChecked();
                if (checked)
                    break;
            }
            if (index < 0)
                throw new Exception("index error: " + index);
            LeanBackUtil.log("ListTvEpisodesPresenter => getCheckedIndexRange => index = " + index);
            return index;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => getCheckedIndexRange => " + e.getMessage(), e);
            return -1;
        }
    }

    private final int getPlayingIndexRange() {
        try {
            int index = -1;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (null == entry)
                    continue;
                T t = entry.getKey();
                if (null == t)
                    continue;
                index += 1;
                boolean playing = t.isPlaying();
                if (playing)
                    break;
            }
            LeanBackUtil.log("ListTvEpisodesPresenter => getPlayingIndexRange => index = " + index);
            return index;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => getPlayingIndexRange => " + e.getMessage(), e);
            return -1;
        }
    }

    private final int findEpisodeIndexOfChild(View v) {
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
            return findEpisodeIndexOfChild((View) parent);
        }
        return index;
    }

    private final boolean isPlayingIndexOfEpisode(int index) {
        int playingIndexEpisode = getPlayingIndexEpisode();
        int checkedIndexRange = getCheckedIndexRange();
        int playingIndexRange = getPlayingIndexRange();
        LeanBackUtil.log("ListTvEpisodesPresenter => isPlayingIndexOfEpisode => index = " + index);
        LeanBackUtil.log("ListTvEpisodesPresenter => isPlayingIndexOfEpisode => playingIndexEpisode = " + playingIndexEpisode);
        LeanBackUtil.log("ListTvEpisodesPresenter => isPlayingIndexOfEpisode => checkedIndexRange = " + checkedIndexRange);
        LeanBackUtil.log("ListTvEpisodesPresenter => isPlayingIndexOfEpisode => playingIndexRange = " + playingIndexRange);
        return index == playingIndexEpisode && checkedIndexRange == playingIndexRange;
    }

    private final boolean isCheckedIndexOfEpisode(int index) {
        int checkedIndexEpisode = getCheckedIndexEpisode();
        int checkedIndexRange = getCheckedIndexRange();
        int playingIndexRange = getPlayingIndexRange();
        LeanBackUtil.log("ListTvEpisodesPresenter => isCheckedIndexOfEpisode => index = " + index);
        LeanBackUtil.log("ListTvEpisodesPresenter => isCheckedIndexOfEpisode => checkedIndexEpisode = " + checkedIndexEpisode);
        LeanBackUtil.log("ListTvEpisodesPresenter => isCheckedIndexOfEpisode => checkedIndexRange = " + checkedIndexRange);
        LeanBackUtil.log("ListTvEpisodesPresenter => isCheckedIndexOfEpisode => playingIndexRange = " + playingIndexRange);
        return index == checkedIndexEpisode && checkedIndexRange == playingIndexRange;
    }

    private final boolean isPlayingIndexOfRange(int index) {
        int playingIndexRange = getPlayingIndexRange();
        return index == playingIndexRange;
    }

    private final int getRangeLength() {
        try {
            return mData.size();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => getRangeLength => " + e.getMessage(), e);
            return -1;
        }
    }

    private T getIndexOfRangeData(int position) {
        try {
            int index = 0;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (index == position) {
                    return entry.getKey();
                }
                index += 1;
            }
            throw new Exception("position error: " + position);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => getIndexOfRangeData => " + e.getMessage(), e);
            throw null;
        }
    }

    private final void callEpisode(View v, boolean hasFocus, boolean isClick) {
        try {
            int index;
            if (isClick) {
                index = getPlayingIndexEpisode();
            } else {
                index = findEpisodeIndexOfChild(v);
            }
            int checkedIndexRange = getCheckedIndexRange();
            List<T> list = getIndexOfEpisodeData(checkedIndexRange);
            T t = list.get(index);

            boolean isChecked;
            if (hasFocus) {
                isChecked = true;
            } else {
                isChecked = isCheckedIndexOfEpisode(index);
            }
            boolean isPlaying;
            if (isClick) {
                isPlaying = true;
            } else {
                isPlaying = isPlayingIndexOfEpisode(index);
            }
            View child = ((ViewGroup) v.getParent()).getChildAt(index);
            onBindViewHolderEpisode(child.getContext(), child, t, index, hasFocus, isPlaying, isChecked);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => callEpisode => " + e.getMessage(), e);
        }
    }

    private final void callRange(@NonNull View v, int index, boolean hasFocus, boolean isClick) {
        try {
//            RecyclerViewHorizontal recyclerView = ((ViewGroup) v.getParent().getParent()).findViewById(R.id.lb_list_tv_episodes_ranges);
//            recyclerView.scrollToPosition(index);
//            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(index);
            boolean isChecked;
            if (hasFocus) {
                isChecked = true;
            } else {
                isChecked = isCheckedIndexOfEpisode(index);
            }
            boolean isPlaying;
            if (isClick) {
                isPlaying = true;
            } else {
                isPlaying = isPlayingIndexOfRange(index);
            }
            T t = getIndexOfRangeData(index);
            onBindViewHolderRange(v.getContext(), v, t, index, hasFocus, isPlaying, isChecked);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => callRange => " + e.getMessage(), e);
        }
    }

    private final void updateRangeIndexChecked(int position) {
        try {
            int index = -1;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (null == entry)
                    continue;
                T t = entry.getKey();
                if (null == t)
                    continue;
                index += 1;
                t.setChecked(index == position);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => updateRangeIndexChecked => " + e.getMessage(), e);
        }
    }

    private final void updateEpisodeIndexPlayingAndChecked(int index, int position) {
        try {
            List<T> list = getIndexOfEpisodeData(index);
            if (null == list)
                throw new Exception("list error: null");
            int size = list.size();
            if (size <= 0)
                throw new Exception("size error: " + size);
            for (int i = 0; i < size; i++) {
                T t = list.get(i);
                if (null == t)
                    continue;
                t.setChecked(position == i);
                t.setPlaying(position == i);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeIndexPlayingAndChecked => " + e.getMessage(), e);
        }
    }

//    private final void updateEpisodeIndexChecked(int position) {
//        int checkedIndexRange = getCheckedIndexRange();
//        updateEpisodeIndexChecked(checkedIndexRange, position);
//    }

    private final void updateEpisodeIndexChecked(int checkedIndexRange, int position) {
        try {
            List<T> list = getIndexOfEpisodeData(checkedIndexRange);
            if (null == list)
                throw new Exception("list error: null");
            int size = list.size();
            if (size <= 0)
                throw new Exception("size error: " + size);
            for (int i = 0; i < size; i++) {
                T t = list.get(i);
                if (null == t)
                    continue;
                t.setChecked(position == i);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeIndexChecked => " + e.getMessage(), e);
        }
    }

    private final void updateEpisodeClickIndex(int position) {
        // checkRange
        int checkedIndexRange = getCheckedIndexRange();
        // playingRange
        int playingIndexRange = getPlayingIndexRange();
        if (checkedIndexRange != playingIndexRange) {
            updateRangeIndexPlayingAndChecked(null, checkedIndexRange);
        }
        // checkEpisode
        updateEpisodeIndexPlayingAndChecked(checkedIndexRange, position);
    }

    private final void clickEpisode(@NonNull LinearLayout viewGroup, View v) {

        int indexOfEpisode = findEpisodeIndexOfChild(v);
        boolean playingIndexOfEpisode = isPlayingIndexOfEpisode(indexOfEpisode);
        if (playingIndexOfEpisode)
            return;

        // 1. 还原之前正在playing, range
        // TODO: 2023/2/28
        resetRangeIndexPlayingAndChecked(viewGroup);
        // 2. 还原之前正在playing, episode
        callEpisode(v, false, true);
        // 3. 更新index
        updateEpisodeClickIndex(indexOfEpisode);
        int checkedIndexRange = getCheckedIndexRange();
        updateRangeIndexPlayingAndChecked(viewGroup, checkedIndexRange);
        // 4. 更新正在playing, episode
        callEpisode(v, false, false);
        // 5. listener
        try {
            List<T> list = getIndexOfEpisodeData(checkedIndexRange);
            T t = list.get(indexOfEpisode);
            onClickEpisode(v.getContext(), v, t, indexOfEpisode);
        } catch (Exception e) {
        }
    }

    private final void updateRangeIndexPlayingAndChecked(@NonNull LinearLayout viewGroup, int position) {
        try {
            int index = 0;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (null == entry)
                    continue;
                T t = entry.getKey();
                if (null == t)
                    continue;
                t.setChecked(position == index);
                t.setPlaying(position == index);
                index += 1;
            }
            RecyclerViewHorizontal recyclerView = viewGroup.findViewById(R.id.lb_list_tv_episodes_ranges);
            if (null == recyclerView)
                throw new Exception("recyclerView error: null");
            recyclerView.getAdapter().notifyItemRangeChanged(position, 1);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => updateRangeIndexPlayingAndChecked => " + e.getMessage(), e);
        }
    }

    private void resetRangeIndexPlayingAndChecked(LinearLayout viewGroup) {
        try {
            RecyclerViewHorizontal recyclerView = viewGroup.findViewById(R.id.lb_list_tv_episodes_ranges);
            if (null == recyclerView)
                throw new Exception("recyclerView error: null");
            for (int i = 0; i <= 1; i++) {
                int index;
                if (i == 0) {
                    index = getCheckedIndexRange();
                } else {
                    index = getPlayingIndexRange();
                }
                T t = getIndexOfRangeData(index);
                if (null == t)
                    continue;
                t.setChecked(false);
                t.setPlaying(false);
                recyclerView.getAdapter().notifyItemRangeChanged(index, 1);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => resetRangeIndexPlayingAndChecked => " + e.getMessage(), e);
        }
    }

    private final void updateEpisodeAdapter(Context context, LinearLayout viewGroup) {
        try {
            int checkedIndexRange = getCheckedIndexRange();
            List<T> list = getIndexOfEpisodeData(checkedIndexRange);
            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
            LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeAdapter => layout = " + layout);
            int max = layout.getChildCount();
            int num = list.size();
            LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeAdapter => checkedIndexRange = " + checkedIndexRange);
            LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeAdapter => max = " + max);
            LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeAdapter => num = " + num);
            for (int i = 0; i < max; i++) {
                View child = layout.getChildAt(i);
                LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeAdapter => i = " + i + ", child = " + child);
                if (null == child)
                    continue;
                child.setVisibility(i + 1 <= num ? View.VISIBLE : View.INVISIBLE);
                if (i + 1 > num)
                    continue;
                T t = list.get(i);
                LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeAdapter => " + t.toString());
                if (null == t)
                    continue;
                boolean isPlaying = isPlayingIndexOfEpisode(i);
                boolean isChecked = isCheckedIndexOfEpisode(i);
                onBindViewHolderEpisode(context, child, t, i, false, isPlaying, isChecked);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => updateEpisodeAdapter => " + e.getMessage(), e);
        }
    }

    private final void requestFocusChildOfRangeChecked(@NonNull LinearLayout viewGroup) {
        try {
            int checkedIndexRange = getCheckedIndexRange();
            RecyclerViewHorizontal recyclerView = viewGroup.findViewById(R.id.lb_list_tv_episodes_ranges);
            recyclerView.scrollToPosition(checkedIndexRange);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(checkedIndexRange);
            viewHolder.itemView.requestFocus();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => requestFocusChildOfRangeChecked => " + e.getMessage(), e);
        }
    }

    private final void cleanFocusChildOfEpisodeChecked(@NonNull LinearLayout viewGroup) {
        try {
            int checkedIndexEpisode = getCheckedIndexEpisode();
            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
            View childAt = layout.getChildAt(checkedIndexEpisode);
            childAt.clearFocus();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => cleanFocusChildOfEpisodeChecked => " + e.getMessage(), e);
        }
    }

    private final void cleanFocusChildOfRangeChecked(@NonNull View view) {
        try {
            view.clearFocus();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => cleanFocusChildOfRangeChecked => " + e.getMessage(), e);
        }
    }

    private final void requestFocusChildOfEpisodeChecked(@NonNull LinearLayout viewGroup) {
        try {
            int checkedIndexEpisode = getCheckedIndexEpisode();
            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
            View childAt = layout.getChildAt(checkedIndexEpisode);
            childAt.requestFocus();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => requestFocusChildOfEpisodeChecked => " + e.getMessage(), e);
        }
    }

    /**************/

    public final void requestFocusChildOfEpisodeChecked(BaseGridView viewGroup) {
        try {
            int checkedIndexEpisode = getCheckedIndexEpisode();
            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
            View childAt = layout.getChildAt(checkedIndexEpisode);
            childAt.requestFocus();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => requestFocusChildOfEpisodeChecked => " + e.getMessage(), e);
        }
    }

//    public void startPosition(Context context, BaseGridView viewGroup, int index) {
//
//        int key = 0;
//        int position = -1;
//        int start = 0;
//        for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
//            List<T> value = entry.getValue();
//            if (null == value)
//                continue;
//            int size = value.size();
//            if (size <= 0)
//                continue;
//            int end = start + size;
//            if (index >= start && index < end) {
//                position = index - start;
//                break;
//            }
//            key += 1;
//        }
//
//        if (key != -1) {
//            updateRangeCheckedIndex(key);
//            updateRangePlayingIndex();
//            try {
//                RecyclerViewHorizontal recyclerView = viewGroup.findViewById(R.id.lb_list_tv_episodes_ranges);
//                recyclerView.scrollToPosition(mCheckedIndexRange);
//                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(mCheckedIndexRange);
//                T t = getCheckedRangeData();
//                onFocusChangeRange(context, viewHolder.itemView, t, mCheckedIndexRange, false, true);
//            } catch (Exception e) {
//            }
//        }
//        if (position != -1) {
//            updateEpisodeCheckedIndex(position);
//            updateEpisodePlayingIndex(position);
//            try {
//                LinearLayout linearLayout = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
//                View child = linearLayout.getChildAt(position);
//                List<T> list = getCheckedEpisodeData();
//                T t = list.get(index);
//                onClickEpisode(child.getContext(), child, t, index);
//            } catch (Exception e) {
//            }
//        }
//    }
}
