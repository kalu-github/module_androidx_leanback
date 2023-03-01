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

public abstract class ListTvEpisodesPresenter3<T extends ListTvEpisodesPresenter3.ItemBean> extends Presenter implements ListTvPresenterImpl {

    private final LinkedHashMap<T, List<T>> mData = new LinkedHashMap<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            LinearLayout viewGroup = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.lb_list_tv_episodes2, parent, false);
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
            boolean[] isVertical = new boolean[]{false};
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
                        int index = findEpisodeIndexOfChild(v);
                        if (index >= 0) {
                            clickEpisode(viewGroup, v, index, isVertical[0]);
                            isVertical[0] = false;
                        }
                    }
                });
                item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        int index = findEpisodeIndexOfChild(v);
                        LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => onFocusChange => hasFocus = " + hasFocus + ", isVertical = " + isVertical[0]);
                        if (index >= 0) {
                            notifyEpisode(v, index, hasFocus, false, isVertical[0]);
                            isVertical[0] = false;
                        }
                    }
                });
                item.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => onKey => action = " + event.getAction() + ", code = " + keyCode);

                        // left
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            try {

                                int checkedIndexRange = getCheckedIndexRange();
                                int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
                                LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => onKey => left => checkedIndexEpisode = " + checkedIndexEpisode);

                                // 剧集 - 显示第一个
                                if (checkedIndexEpisode <= 0) {
                                    // 区间 - 显示第一个
                                    if (checkedIndexRange > 0) {
                                        // 1
                                        int newCheckedIndexRange = checkedIndexRange - 1;
                                        int newCheckedIndexEpisode = num - 1;
                                        resetCheckedIndex(newCheckedIndexRange, newCheckedIndexEpisode);
                                        // 2
                                        notifyEpisodeAdapter(context, viewGroup, checkedIndexEpisode, newCheckedIndexEpisode);
                                        // 3
                                        notifyRange(viewGroup, checkedIndexRange, false, false, false, false);
                                        notifyRange(viewGroup, newCheckedIndexRange, false, true, false, true);
                                    }
                                    return true;
                                }
                            } catch (Exception e) {
                                LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => onKey => left => " + e.getMessage());
                                return true;
                            }

                            try {
                                int checkedIndexRange = getCheckedIndexRange();
                                int checkedIndexEpisode = getCheckedIndexEpisode();
                                int newCheckedIndexEpisode = checkedIndexEpisode - 1;
                                resetCheckedIndex(checkedIndexRange, newCheckedIndexEpisode);
                            } catch (Exception e) {
                            }
                        }
                        // right
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            try {
                                // 剧集 - 是否显示最后一个
                                int checkedIndexRange = getCheckedIndexRange();
                                int size = getIndexOfEpisodeLength(checkedIndexRange);
                                int episodeIndexOfChild = findEpisodeIndexOfChild(v);
                                if (episodeIndexOfChild + 1 >= size) {
                                    // 区间 - 是否显示最后一个
                                    int rangeLength = getRangeLength();
                                    if (checkedIndexRange + 1 < rangeLength) {
                                        // 1
                                        int newCheckedIndexRange = checkedIndexRange + 1;
                                        int newCheckedIndexEpisode = 0;
                                        resetCheckedIndex(newCheckedIndexRange, newCheckedIndexEpisode);
                                        // 2
                                        notifyEpisodeAdapter(context, viewGroup, episodeIndexOfChild, newCheckedIndexEpisode);
                                        // 3
                                        notifyRange(viewGroup, checkedIndexRange, false, false, false, false);
                                        notifyRange(viewGroup, newCheckedIndexRange, false, true, false, true);
                                    }
                                    return true;
                                }
                            } catch (Exception e) {
                                LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter1 => onKey => right => " + e.getMessage());
                                return true;
                            }

                            try {
                                int checkedIndexRange = getCheckedIndexRange();
                                int checkedIndexEpisode = getCheckedIndexEpisode();
                                int newCheckedIndexEpisode = checkedIndexEpisode + 1;
                                resetCheckedIndex(checkedIndexRange, newCheckedIndexEpisode);
                            } catch (Exception e) {
                            }
                        }
                        // down
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            isVertical[0] = true;
                            focusDownEpisode(viewGroup);
                            return true;
                        }
                        // up
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            isVertical[0] = true;
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
//                            item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                                @Override
//                                public void onFocusChange(View v, boolean hasFocus) {
//                                    try {
//                                        int position = holder.getAbsoluteAdapterPosition();
//                                        LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter2 => onCreateViewHolder => onFocusChange => position = " + position + ", hasFocus = " + hasFocus + ", isVertical = " + isVertical[0]);
//                                        if (position >= 0) {
//                                            if (hasFocus && !isVertical[0]) {
//                                                resetCheckedIndex(position, 0);
//                                                notifyEpisodeAdapter(context, viewGroup, -1, -1);
//                                            }
//                                            notifyRange(viewGroup, position, hasFocus, false, isVertical[0], false);
//                                        }
//                                    } catch (Exception e) {
//                                        LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter2 => onCreateViewHolder => onFocusChange => " + e.getMessage(), e);
//                                    }
//                                }
//                            });
                            item.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View v, int keyCode, KeyEvent event) {
                                    LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter2 => onKey => action = " + event.getAction() + ", code = " + event.getKeyCode());
                                    // up
                                    if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                                        // 1
                                        int position = holder.getAbsoluteAdapterPosition();
                                        notifyRange(viewGroup, position, false, false, true, false);
                                        // 2
                                        focusUpRange(viewGroup);
                                        return true;
                                    }
                                    // down
                                    else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                                        int position = holder.getAbsoluteAdapterPosition();
                                        notifyRange(viewGroup, position, false, false, true, false);
                                    }
                                    // right
                                    else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                                        // 2
                                        int position = holder.getAbsoluteAdapterPosition();
                                        int newCheckedIndexRange = position + 1;
                                        resetCheckedIndex(newCheckedIndexRange, 0);
                                        // 3
                                        notifyEpisodeAdapter(context, viewGroup, -1, -1);
                                        // 4
                                        notifyRange(viewGroup, position, false, false, false, false);
                                        notifyRange(viewGroup, newCheckedIndexRange, true, false, false, false);
                                    }
                                    // left
                                    else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                                        // 2
                                        int position = holder.getAbsoluteAdapterPosition();
                                        int newCheckedIndexRange = position - 1;
                                        resetCheckedIndex(newCheckedIndexRange, 0);
                                        // 3
                                        notifyEpisodeAdapter(context, viewGroup, -1, -1);
                                        // 4
                                        notifyRange(viewGroup, position, false, false, false, false);
                                        notifyRange(viewGroup, newCheckedIndexRange, true, false, false, false);
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
                            boolean isChecked = isCheckedIndexOfRange(position);
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

            if (null == item || !(item instanceof List)) throw new Exception("date error: " + item);

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
                    if (null == t) continue;
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
//        for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
//            T t = entry.getKey();
//            if (null != t) {
//                LeanBackUtil.log("ListTvEpisodesPresenter => onBindViewHolder => " + t.toString());
//            }
//            List<T> value = entry.getValue();
//            if (null != t) {
//                LeanBackUtil.log("ListTvEpisodesPresenter => onBindViewHolder => " + value.toString());
//            }
//        }

        // head
        try {
            TextView textView = viewHolder.view.findViewById(R.id.lb_list_tv_episodes_head);
            textView.setText(initHead(viewHolder.view.getContext()));
            textView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => onBindViewHolder => " + e.getMessage(), e);
        }

        // 剧集
        notifyEpisodeAdapter(viewHolder.view.getContext(), (LinearLayout) viewHolder.view, -1, -1);

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
        return null;
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
                if (index == position) return entry.getValue();
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
            if (null == list) throw new Exception("list error: null");
            int size = list.size();
            if (size <= 0) throw new Exception("size error: " + size);
            int index = -1;
            for (int i = 0; i < size; i++) {
                T t = list.get(i);
                if (null == t) continue;
                index += 1;
                boolean checked = t.isChecked();
                if (checked) break;
            }
            if (index < 0) throw new Exception("index error: " + size);
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
            if (indexRange == -1) throw new Exception("indexRange error: " + indexRange);
            List<T> list = getIndexOfEpisodeData(indexRange);
            if (null == list) throw new Exception("list error: null");
            int size = list.size();
            if (size <= 0) throw new Exception("size error: " + size);
            int index = -1;
            for (int i = 0; i < size; i++) {
                T t = list.get(i);
                if (null == t) continue;
                index += 1;
                boolean playing = t.isPlaying();
                if (playing) break;
            }
            if (index < 0) throw new Exception("index error: " + size);
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
                if (null == entry) continue;
                T t = entry.getKey();
                if (null == t) continue;
                index += 1;
                boolean checked = t.isChecked();
                if (checked) break;
            }
            if (index < 0) throw new Exception("index error: " + index);
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
                if (null == entry) continue;
                T t = entry.getKey();
                if (null == t) continue;
                index += 1;
                boolean playing = t.isPlaying();
                if (playing) break;
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

    private final boolean isCheckedIndexOfRange(int index) {
        int checkedIndexRange = getCheckedIndexRange();
        LeanBackUtil.log("ListTvEpisodesPresenter => isCheckedIndexOfRange => checkedIndexRange = " + checkedIndexRange + ", index = " + index);
        return index == checkedIndexRange;
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

    private final void notifyEpisode(View v, int index, boolean hasFocus, boolean isClick, boolean isVertical) {
        try {
            LeanBackUtil.log("ListTvEpisodesPresenter => notifyEpisode => index = " + index + ", isVertical = " + isVertical);

            List<T> list = getIndexOfEpisodeData(index);
            T t = list.get(index);
            if (null == t) throw new Exception("t error: null");

            boolean isChecked;
            if (hasFocus || isVertical) {
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
            LeanBackUtil.log("ListTvEpisodesPresenter => notifyEpisode => " + e.getMessage(), e);
        }
    }

    private final void notifyRange(@NonNull LinearLayout viewGroup, int index, boolean hasFocus, boolean isClick, boolean isVertical, boolean isScroll) {
        try {
            boolean isPlaying;
            if (isClick) {
                isPlaying = true;
            } else {
                isPlaying = isPlayingIndexOfRange(index);
            }
            boolean isChecked;
            if (hasFocus || isVertical || isClick) {
                isChecked = true;
            } else {
                isChecked = false;
//                isChecked = isCheckedIndexOfRange(index);
            }
            LeanBackUtil.log("ListTvEpisodesPresenter => notifyRange => index = " + index + ", hasFocus" + hasFocus + ", isClick = " + isClick + ", isChecked = " + isChecked + ", isPlaying = " + isPlaying);
            RecyclerViewHorizontal recyclerView = viewGroup.findViewById(R.id.lb_list_tv_episodes_ranges);
            recyclerView.getAdapter().notifyItemRangeChanged(index, 1);
            if (isScroll) {
                recyclerView.scrollToPosition(index, false);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => notifyRange => " + e.getMessage(), e);
        }
    }

    private final void clickEpisode(@NonNull LinearLayout viewGroup, View v, int index, boolean isVertical) {

        boolean playingIndexOfEpisode = isPlayingIndexOfEpisode(index);
        if (playingIndexOfEpisode) return;

        int playingIndexEpisode = getPlayingIndexEpisode();
        int checkedIndexRange = getCheckedIndexRange();
        int playingIndexRange = getPlayingIndexRange();
        // 1.重置index
        resetPlayingIndex(checkedIndexRange, index);
        // 2. 还原之前正在playing, episode
        notifyEpisode(v, playingIndexEpisode, false, false, isVertical);
        notifyEpisode(v, index, true, true, isVertical);
        // 3. 还原之前正在playing, range
        notifyRange(viewGroup, playingIndexRange, false, false, isVertical, false);
        notifyRange(viewGroup, checkedIndexRange, false, true, isVertical, false);
        // 4. listener
        try {
            List<T> list = getIndexOfEpisodeData(checkedIndexRange);
            T t = list.get(index);
            onClickEpisode(v.getContext(), v, t, index);
        } catch (Exception e) {
        }
    }

    private final void resetPlayingIndex(int playingIndexRange, int playingIndexEpisode) {
        try {
            int index = -1;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                T t = entry.getKey();
                if (null == t) continue;
                index += 1;
                t.setChecked(index == playingIndexRange);
                t.setPlaying(index == playingIndexRange);
                List<T> value = entry.getValue();
                if (null == value) continue;
                int size = value.size();
                for (int i = 0; i < size; i++) {
                    T t1 = value.get(i);
                    if (null == t1) continue;
                    t1.setChecked(index == playingIndexRange && i == playingIndexEpisode);
                    t1.setPlaying(index == playingIndexRange && i == playingIndexEpisode);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => resetPlayingIndex => " + e.getMessage(), e);
        }
    }

    private final void resetCheckedIndex(int checkedIndexRange, int checkedIndexEpisode) {
        try {
            int index = -1;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                T t = entry.getKey();
                if (null == t) continue;
                index += 1;
                t.setChecked(index == checkedIndexRange);
                List<T> value = entry.getValue();
                if (null == value) continue;
                int size = value.size();
                for (int i = 0; i < size; i++) {
                    T t1 = value.get(i);
                    if (null == t1) continue;
                    t1.setChecked(index == checkedIndexRange && i == checkedIndexEpisode);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => resetPlayingIndex => " + e.getMessage(), e);
        }
    }

    private final void notifyEpisodeAdapter(Context context, LinearLayout viewGroup, int cleanFocusIndex, int requestFocusIndex) {
        try {
            int checkedIndexRange = getCheckedIndexRange();
            List<T> list = getIndexOfEpisodeData(checkedIndexRange);
            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
            LeanBackUtil.log("ListTvEpisodesPresenter => notifyEpisodeAdapter => layout = " + layout);
            int max = layout.getChildCount();
            int num = list.size();
            LeanBackUtil.log("ListTvEpisodesPresenter => notifyEpisodeAdapter => checkedIndexRange = " + checkedIndexRange);
            LeanBackUtil.log("ListTvEpisodesPresenter => notifyEpisodeAdapter => max = " + max);
            LeanBackUtil.log("ListTvEpisodesPresenter => notifyEpisodeAdapter => num = " + num);
            for (int i = 0; i < max; i++) {
                View child = layout.getChildAt(i);
                LeanBackUtil.log("ListTvEpisodesPresenter => notifyEpisodeAdapter => i = " + i + ", child = " + child);
                if (null == child) continue;
                child.setVisibility(i + 1 <= num ? View.VISIBLE : View.INVISIBLE);
                if (i + 1 > num) continue;
                T t = list.get(i);
                LeanBackUtil.log("ListTvEpisodesPresenter => notifyEpisodeAdapter => " + t.toString());
                if (null == t) continue;

                if (i == cleanFocusIndex) {
                    child.clearFocus();
                } else if (i == requestFocusIndex) {
                    child.requestFocus();
                } else {
                    boolean isPlaying = isPlayingIndexOfEpisode(i);
                    boolean isChecked = isCheckedIndexOfEpisode(i);
                    onBindViewHolderEpisode(context, child, t, i, false, isPlaying, isChecked);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => notifyEpisodeAdapter => " + e.getMessage(), e);
        }
    }

    private final void focusDownEpisode(@NonNull LinearLayout viewGroup) {
        try {
            int checkedIndexRange = getCheckedIndexRange();
            RecyclerViewHorizontal recyclerView = viewGroup.findViewById(R.id.lb_list_tv_episodes_ranges);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(checkedIndexRange);
            if (null != viewHolder) {
                viewHolder.itemView.requestFocus();
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => focusDownEpisode => " + e.getMessage(), e);
        }
    }

    private final void focusUpRange(@NonNull LinearLayout viewGroup) {
        try {
//            int index;
//            int checkedIndexRange = getCheckedIndexRange();
//            int playingIndexRange = getPlayingIndexRange();
//            if (checkedIndexRange == playingIndexRange && checkedIndexRange != -1) {
//                index = getPlayingIndexEpisode();
//            } else {
//                index = getCheckedIndexEpisode();
//            }
            int index = getPlayingIndexEpisode();
            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
            View childAt = layout.getChildAt(index);
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
