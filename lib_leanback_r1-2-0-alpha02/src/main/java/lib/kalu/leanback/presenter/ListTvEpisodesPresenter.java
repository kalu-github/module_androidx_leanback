package lib.kalu.leanback.presenter;

import android.content.Context;
import android.graphics.Rect;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
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

import lib.kalu.leanback.presenter.bean.TvEpisodesItemBean;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvEpisodesPresenter<T extends TvEpisodesItemBean> extends Presenter implements ListTvPresenterImpl {

    private final LinkedHashMap<T, List<T>> mData = new LinkedHashMap<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            LinearLayout viewGroup = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.lb_list_tv_episodes, parent, false);
            initTitle(context, viewGroup, R.id.lb_list_tv_episodes_title);
            initLayoutEpisode(context, viewGroup);
            return new ViewHolder(viewGroup);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => onCreateViewHolder => " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

        // 数据
        formatMap(item);

        // log
        printMap();

        // head
        setRowTitle(viewHolder.view);

        // 剧集
        notifyEpisodeAdapter(viewHolder.view.getContext(), (LinearLayout) viewHolder.view, -1, -1);
        // 剧集区间
        initLayoutRange(viewHolder.view.getContext(), (LinearLayout) viewHolder.view);
    }

    private final void setRowTitle(View view) {
        try {
            TextView textView = view.findViewById(R.id.lb_list_tv_episodes_title);
            textView.setText(initRowTitle(view.getContext()));
            textView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => setHead => " + e.getMessage(), e);
        }
    }

    private final void formatMap(Object item) {
        try {
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
                key.setPlaying(false);
                key.setChecked(false);

                // map-value
                ArrayList<T> value = new ArrayList<>();
                for (int m = start - 1; m <= (end - 1); m++) {
                    T t = list.get(m);
                    if (null == t) continue;
                    t.setPlaying(false);
                    t.setChecked(false);
                    value.add(t);
                }

                // map
                mData.put(key, value);
            }

        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => formatMap => " + e.getMessage(), e);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    protected String initRowTitle(Context context) {
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
    private final void initLayoutEpisode(@NonNull Context context, @NonNull LinearLayout viewGroup) {
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
            int episodeNum = initEpisodeNum();
            int episodePadding = initEpisodePadding(context);
            int episodeMargin = episodePadding * (episodeNum - 1) / episodeNum;
            layout.setWeightSum(episodeNum);
            // 3 剧集
            for (int i = 0; i < episodeNum; i++) {
                View item = LayoutInflater.from(context).inflate(initEpisodeLayout(), layout, false);
                layout.addView(item);
                if (null != item.getLayoutParams()) {
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).weight = 1;
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).bottomMargin = 0;
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).topMargin = 0;
                    if (i == 0) {
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).leftMargin = 0;
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).rightMargin = episodeMargin;
                    } else if (i + 1 <= episodeNum) {
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).leftMargin = episodeMargin;
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).rightMargin = 0;
                    } else {
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).leftMargin = episodeMargin / 2;
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).rightMargin = episodeMargin / 2;
                    }
                }
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = findEpisodeIndexOfChild(v);
                        if (index >= 0) {
                            clickEpisode(viewGroup, v, index, true);
                        }
                    }
                });
                item.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        LeanBackUtil.log("ListTvEpisodesPresenter => initLayoutEpisode => onKey => action = " + event.getAction() + ", code = " + keyCode);

                        // left
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            try {

                                int checkedIndexRange = getCheckedIndexRange();
                                int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
                                LeanBackUtil.log("ListTvEpisodesPresenter => initLayoutEpisode => onKey => left => checkedIndexEpisode = " + checkedIndexEpisode);

                                // 剧集 - 显示第一个
                                if (checkedIndexEpisode <= 0) {
                                    // 区间 - 显示第一个
                                    if (checkedIndexRange > 0) {
                                        // 1
                                        int newCheckedIndexRange = checkedIndexRange - 1;
                                        int newCheckedIndexEpisode = episodeNum - 1;
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
                                LeanBackUtil.log("ListTvEpisodesPresenter => initLayoutEpisode => onKey => left => " + e.getMessage());
                                return true;
                            }

                            // 左移
                            try {
                                int checkedIndexRange = getCheckedIndexRange();
                                int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
                                int newCheckedIndexEpisode = checkedIndexEpisode - 1;
                                // 1
                                resetCheckedIndex(checkedIndexRange, newCheckedIndexEpisode);
                                // 2
                                notifyEpisode(v, checkedIndexEpisode, false, false, false);
                                notifyEpisode(v, newCheckedIndexEpisode, true, false, false);
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
                                LeanBackUtil.log("ListTvEpisodesPresenter => initLayoutEpisode => onKey => right => " + e.getMessage());
                                return true;
                            }

                            // 右移
                            try {
                                int checkedIndexRange = getCheckedIndexRange();
                                int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
                                int newCheckedIndexEpisode = checkedIndexEpisode + 1;
//                                // 1
                                resetCheckedIndex(checkedIndexRange, newCheckedIndexEpisode);
//                                // 2
                                notifyEpisode(v, checkedIndexEpisode, false, false, false);
                                notifyEpisode(v, newCheckedIndexEpisode, true, false, false);
                            } catch (Exception e) {
                            }
                        }
                        // down
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            focusDownEpisode(viewGroup, v);
                            return true;
                        }
                        // up
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            int episodeIndexOfChild = findEpisodeIndexOfChild(v);
                            notifyEpisode(v, episodeIndexOfChild, false, false, true);
                        }
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => initLayoutEpisode => " + e.getMessage(), e);
        }
    }

    private final void initLayoutRange(@NonNull Context context, @NonNull LinearLayout viewGroup) {
        try {
            // 1
            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_ranges);
            layout.removeAllViews();
            int rangeMarginTop = initRangeMarginTop(context);
            if (rangeMarginTop > 0 && null != layout.getLayoutParams()) {
                ((ScrollView.LayoutParams) layout.getLayoutParams()).topMargin = rangeMarginTop;
                ((ScrollView.LayoutParams) layout.getLayoutParams()).bottomMargin = 0;
                ((ScrollView.LayoutParams) layout.getLayoutParams()).leftMargin = 0;
                ((ScrollView.LayoutParams) layout.getLayoutParams()).rightMargin = 0;
            }
            // 2
            int rangeLength = getRangeLength();
            int rangePadding = initRangePadding(context);
            LeanBackUtil.log("ListTvEpisodesPresenter => initLayoutRange => rangeLength = " + rangeLength + ", rangePadding = " + rangePadding);
            for (int i = 0; i < rangeLength; i++) {
                View item = LayoutInflater.from(context).inflate(initRangeLayout(), layout, false);
                layout.addView(item);
                if (null != item.getLayoutParams()) {
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).leftMargin = 0;
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).rightMargin = rangePadding;
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).bottomMargin = 0;
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).topMargin = 0;
                }
                T t = getIndexOfRangeData(i);
                if (null != t) {
                    boolean isChecked = isCheckedIndexOfRange(i);
                    onBindViewHolderRange(context, item, t, i, false, false, isChecked);
                }

                item.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        return false;
                    }
                });
                item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {

                        }
                    }
                });
                item.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        LeanBackUtil.log("ListTvEpisodesPresenter => initLayoutRange => onKey => action = " + event.getAction() + ", code = " + keyCode);
                        // right
                        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            int rangeLength = getRangeLength();
                            int rangeIndexOfChild = findRangeIndexOfChild(v);
                            if (rangeIndexOfChild + 1 >= rangeLength)
                                return true;
                            // 1
                            int newCheckedIndexEpisode = 0;
                            int newRangeCheckedIndex = rangeIndexOfChild + 1;
                            resetCheckedIndex(newRangeCheckedIndex, newCheckedIndexEpisode);
                            // 2
                            notifyEpisodeAdapter(context, viewGroup, -1, -1);
                            // 3
                            notifyRange(viewGroup, rangeIndexOfChild, false, false, false, false);
                            notifyRange(viewGroup, newRangeCheckedIndex, true, false, false, false);
                        }
                        // left
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                            int rangeIndexOfChild = findRangeIndexOfChild(v);
                            LeanBackUtil.log("ListTvEpisodesPresenter => initLayoutRange => onKey => left => rangeIndexOfChild = " + rangeIndexOfChild);
                            if (rangeIndexOfChild <= 0)
                                return true;
                            // 1
                            int newCheckedIndexEpisode = 0;
                            int newRangeCheckedIndex = rangeIndexOfChild - 1;
                            resetCheckedIndex(newRangeCheckedIndex, newCheckedIndexEpisode);
                            // 2
                            notifyEpisodeAdapter(context, viewGroup, -1, -1);
                            // 3
                            notifyRange(viewGroup, rangeIndexOfChild, false, false, false, false);
                            notifyRange(viewGroup, newRangeCheckedIndex, true, false, false, false);
                        }
                        // up
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                            int rangeIndexOfChild = findRangeIndexOfChild(v);
                            LeanBackUtil.log("ListTvEpisodesPresenter => initLayoutRange => onKey => up => rangeIndexOfChild = " + rangeIndexOfChild);
                            // 1
                            notifyRange(viewGroup, rangeIndexOfChild, false, true, false, false);
                            // 2
                            focusUpRange(viewGroup);
                            return true;
                        }
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => initLayoutRange => " + e.getMessage(), e);
        }
    }

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

//    private final int getCheckedIndexEpisode() {
//        int checkedIndexRange = getCheckedIndexRange();
//        return getCheckedIndexEpisode(checkedIndexRange);
//    }

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
            for (int i = size - 1; i >= 0; i--) {
                T t = list.get(i);
                LeanBackUtil.log("ListTvEpisodesPresenter => getCheckedIndexEpisode => checkedIndexRange = " + checkedIndexRange + ", i = " + i + ", data = " + t.toString());
                if (null == t) continue;
                boolean checked = t.isChecked();
                if (checked)
                    return i;
            }
            throw new Exception("not find getCheckedIndexEpisode: " + checkedIndexRange);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => getCheckedIndexEpisode => " + e.getMessage(), e);
            return -1;
        }
    }

    private final int getPlayingIndexEpisode() {
        try {
            int playingIndexRange = getPlayingIndexRange();
            if (playingIndexRange == -1)
                throw new Exception("playingIndexRange error: " + playingIndexRange);
            List<T> list = getIndexOfEpisodeData(playingIndexRange);
            if (null == list) throw new Exception("list error: null");
            int size = list.size();
            if (size <= 0) throw new Exception("size error: " + size);
            for (int i = 0; i < size; i++) {
                T t = list.get(i);
                LeanBackUtil.log("ListTvEpisodesPresenter => getPlayingIndexEpisode => playingIndexRange = " + playingIndexRange + ", i = " + i + ", data = " + t.toString());
                if (null == t)
                    continue;
                boolean playing = t.isPlaying();
                if (playing)
                    return i;
            }
            throw new Exception("not find getPlayingIndexEpisode: playingIndexRange = " + playingIndexRange);
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

    private final int findRangeIndexOfChild(View v) {
        int index = -1;
        while (true) {
            if (null == v) break;
            ViewParent parent = v.getParent();
            if (null == parent) break;
            int id = ((ViewGroup) parent).getId();
            if (id == R.id.lb_list_tv_episodes_ranges) {
                index = ((ViewGroup) parent).indexOfChild(v);
                break;
            }
            return findRangeIndexOfChild((View) parent);
        }
        return index;
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

    private final boolean isCheckedAndPlayingIndexOfEpisode(int index) {
        int checkedIndexRange = getCheckedIndexRange();
        int playingIndexRange = getPlayingIndexRange();
        int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
        LeanBackUtil.log("ListTvEpisodesPresenter => isCheckedAndPlayingIndexOfEpisode => index = " + index);
        LeanBackUtil.log("ListTvEpisodesPresenter => isCheckedAndPlayingIndexOfEpisode => checkedIndexEpisode = " + checkedIndexEpisode);
        LeanBackUtil.log("ListTvEpisodesPresenter => isCheckedAndPlayingIndexOfEpisode => checkedIndexRange = " + checkedIndexRange);
        LeanBackUtil.log("ListTvEpisodesPresenter => isCheckedAndPlayingIndexOfEpisode => playingIndexRange = " + playingIndexRange);
        return index == checkedIndexEpisode && checkedIndexRange == playingIndexRange;
    }

    private final boolean isCheckedIndexOfEpisode(int index) {
        int checkedIndexRange = getCheckedIndexRange();
        int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
        LeanBackUtil.log("ListTvEpisodesPresenter => isCheckedIndexOfEpisode => index = " + index);
        LeanBackUtil.log("ListTvEpisodesPresenter => isCheckedIndexOfEpisode => checkedIndexRange = " + checkedIndexRange);
        return index == checkedIndexEpisode;
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

            int checkedIndexRange = getCheckedIndexRange();
            List<T> list = getIndexOfEpisodeData(checkedIndexRange);
            T t = list.get(index);
            if (null == t) throw new Exception("t error: null");

            boolean isChecked;
            if (hasFocus || isVertical) {
                isChecked = true;
            } else {
                isChecked = isCheckedAndPlayingIndexOfEpisode(index);
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

            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_ranges);
            View childAt = layout.getChildAt(index);
            T t = getIndexOfRangeData(index);
            onBindViewHolderRange(childAt.getContext(), childAt, t, index, hasFocus, isPlaying, isChecked);
            if (isScroll) {
//                HorizontalScrollView scrollView = (HorizontalScrollView) layout.getParent();
//                scrollView.scrollTo(0, childAt.getRight());
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => notifyRange => " + e.getMessage(), e);
        }
    }

    private final void clickEpisode(@NonNull LinearLayout viewGroup, View v, int index, boolean isVertical) {

        boolean playingIndexOfEpisode = isPlayingIndexOfEpisode(index);
        if (playingIndexOfEpisode)
            return;

        int checkedIndexRange = getCheckedIndexRange();
        int playingIndexRange = getPlayingIndexRange();

        int playingIndexEpisode = getPlayingIndexEpisode();
        int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);

        // 1.重置index
        resetPlayingIndex(checkedIndexRange, index);
        // 2. 还原之前正在playing, episode
        notifyEpisode(v, checkedIndexEpisode, false, false, false);
        notifyEpisode(v, playingIndexEpisode, false, false, false);
        notifyEpisode(v, index, true, true, true);
        // 3. 还原之前正在playing, range
        notifyRange(viewGroup, playingIndexRange, false, false, false, false);
        notifyRange(viewGroup, checkedIndexRange, false, true, true, false);
        // 4. listener
        try {
            List<T> list = getIndexOfEpisodeData(checkedIndexRange);
            T t = list.get(index);
            int episodeNum = initEpisodeNum();
            int real = checkedIndexRange * episodeNum + index;
            onClickEpisode(v.getContext(), v, t, real);
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
            LeanBackUtil.log("ListTvEpisodesPresenter => resetCheckedIndex => checkedIndexRange = " + checkedIndexRange + ", checkedIndexEpisode = " + checkedIndexEpisode);
            int index = -1;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                T t1 = entry.getKey();
                if (null == t1)
                    continue;
                index += 1;
                t1.setChecked(index == checkedIndexRange);
                List<T> value = entry.getValue();
                if (null == value)
                    continue;
                int size = value.size();
                for (int m = 0; m < size; m++) {
                    T t2 = value.get(m);
                    if (null == t2)
                        continue;
//                    boolean checked = t2.isChecked();
//                    if (checked)
//                        continue;
                    LeanBackUtil.log("ListTvEpisodesPresenter => resetCheckedIndex => index = " + index + ", m = " + m);
                    t2.setChecked(index == checkedIndexRange && m == checkedIndexEpisode);
                    LeanBackUtil.log("ListTvEpisodesPresenter => resetCheckedIndex => " + t2.toString());
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
                }
                boolean isPlaying = isPlayingIndexOfEpisode(i);
                boolean isChecked = isCheckedIndexOfEpisode(i);
                onBindViewHolderEpisode(context, child, t, i, i == requestFocusIndex, isPlaying, isChecked);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => notifyEpisodeAdapter => " + e.getMessage(), e);
        }
    }

//    private final void notifyRangeAdapter(Context context, LinearLayout viewGroup, boolean isInit) {
//
//    }

    private final void focusDownEpisode(@NonNull LinearLayout viewGroup, @NonNull View focusView) {
        try {
            int checkedIndexRange = getCheckedIndexRange();
            int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
            // 1
            notifyEpisode(focusView, checkedIndexEpisode, false, false, true);
            // 2
            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_ranges);
            View childAt = layout.getChildAt(checkedIndexRange);
            childAt.requestFocus();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => focusDownEpisode => " + e.getMessage(), e);
        }
    }

    private final void focusUpRange(@NonNull LinearLayout viewGroup) {
        try {
            int index;
            int checkedIndexRange = getCheckedIndexRange();
            int playingIndexRange = getPlayingIndexRange();
            int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
            int playingIndexEpisode = getPlayingIndexEpisode();
            LeanBackUtil.log("ListTvEpisodesPresenter => focusUpRange => checkedIndexRange = " + checkedIndexRange + ", playingIndexRange = " + playingIndexRange + ", checkedIndexEpisode = " + checkedIndexEpisode + ", playingIndexEpisode = " + playingIndexEpisode);
//            if (checkedIndexRange == playingIndexRange && playingIndexEpisode!= -1) {
//                index = playingIndexEpisode;
//            } else  {
            index = checkedIndexEpisode;
//            }
            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
            View childAt = layout.getChildAt(index);
            childAt.requestFocus();
            notifyEpisode(childAt, index, true, false, true);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => focusUpRange => " + e.getMessage(), e);
        }
    }

    private final void printMap() {
        try {
//            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
//                T t = entry.getKey();
//                if (null != t) {
//                    LeanBackUtil.log("ListTvEpisodesPresenter => printMap => " + t.toString());
//                }
//                List<T> value = entry.getValue();
//                if (null != t) {
//                    LeanBackUtil.log("ListTvEpisodesPresenter => printMap => " + value.toString());
//                }
//            }
        } catch (Exception e) {
        }
    }

    /**************/

    public final void requestFocusChildOfEpisodeChecked(BaseGridView viewGroup) {
        try {
            int checkedIndexRange = getCheckedIndexRange();
            int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
            // 1
            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
            View childAt = layout.getChildAt(checkedIndexEpisode);
            childAt.requestFocus();
            // 2
            notifyEpisode(childAt, checkedIndexEpisode, true, false, true);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => requestFocusChildOfEpisodeChecked => " + e.getMessage(), e);
        }
    }

    public final void startPosition(RecyclerView recyclerView, int position) {
        try {
            if (position >= 0) {
                // 1
                int episodeNum = initEpisodeNum();
                int newRangeCheckedIndex = position / episodeNum;
                int newCheckedIndexEpisode = position % episodeNum;
                resetCheckedIndex(newRangeCheckedIndex, newCheckedIndexEpisode);
                // 2
                LinearLayout viewGroup = recyclerView.findViewById(R.id.lb_list_tv_episodes_items);
                View childAt = viewGroup.getChildAt(newCheckedIndexEpisode);
                clickEpisode(viewGroup, childAt, newCheckedIndexEpisode, true);
            }

        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => formatMap => " + e.getMessage(), e);
        }
    }

    public final void startNext(RecyclerView recyclerView) {
        try {
            int checkedIndexRange = getCheckedIndexRange();
            int indexEpisode = getCheckedIndexEpisode(checkedIndexRange);
            LinearLayout viewGroup = recyclerView.findViewById(R.id.lb_list_tv_episodes_items);
            View childAt = viewGroup.getChildAt(indexEpisode + 1);
            childAt.performClick();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => startNext => " + e.getMessage(), e);
        }
    }
}
