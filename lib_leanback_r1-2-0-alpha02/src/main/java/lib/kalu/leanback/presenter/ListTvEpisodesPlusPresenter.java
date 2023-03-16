package lib.kalu.leanback.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lib.kalu.leanback.presenter.bean.TvEpisodesPlusItemBean;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvEpisodesPlusPresenter<T extends TvEpisodesPlusItemBean> extends Presenter implements ListTvPresenterImpl {

    private final LinkedHashMap<T, List<T>> mData = new LinkedHashMap<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            LinearLayout inflate = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.lb_list_tv_episodes_plus, parent, false);
            setPadding(context, inflate);
            setBackgroundColor(context, inflate);
            setContentBackgroundColor(context, inflate, R.id.lb_list_tv_episodes_items);
            setContentBackgroundColor(context, inflate, R.id.lb_list_tv_episodes_ranges);
            setTitlePadding(context, inflate, R.id.lb_list_tv_episodes_title);
            setTitleTextColor(context, inflate, R.id.lb_list_tv_episodes_title);
            setTitleTextSize(context, inflate, R.id.lb_list_tv_episodes_title);
            setTitleAssetTTF(context, inflate, R.id.lb_list_tv_episodes_title);
            setTitleBackgroundColor(context, inflate, R.id.lb_list_tv_episodes_title);
            initLayoutEpisode(context, inflate);
            return new ViewHolder(inflate);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => onCreateViewHolder => " + e.getMessage());
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

        // 数据
        formatData(item);

        // log
//        try {
//            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
//                T t = entry.getKey();
//                if (null != t) {
//                    LeanBackUtil.log("ListTvEpisodesPlusPresenter => printMap => " + t.toString());
//                }
//                List<T> value = entry.getValue();
//                if (null != t) {
//                    LeanBackUtil.log("ListTvEpisodesPlusPresenter => printMap => " + value.toString());
//                }
//            }
//        } catch (Exception e) {
//        }

        // 标题
        updateTitle(viewHolder.view, R.id.lb_list_tv_episodes_title);

        // 区间
        initLayoutRange(viewHolder.view.getContext(), (LinearLayout) viewHolder.view);

        // 剧集
        notifyEpisodeAdapter(viewHolder.view.getContext(), (LinearLayout) viewHolder.view, -1, -1);
    }

    private final void setRowTitle(View view) {
        try {
            TextView textView = view.findViewById(R.id.lb_list_tv_episodes_title);
            textView.setText(initRowTitle(view.getContext()));
            textView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => setHead => " + e.getMessage());
        }
    }

    private final void formatData(Object item) {
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => formatMap => " + e.getMessage());
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

    protected void onClickEpisode(@NonNull Context context, @NonNull View v, @NonNull T item, @NonNull int position, boolean isFromUser) {
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
                    } else if (i + 1 >= episodeNum) {
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).leftMargin = episodeMargin;
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).rightMargin = 0;
                    } else {
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).leftMargin = episodeMargin;
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).rightMargin = episodeMargin;
                    }
                }
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = findEpisodeIndexOfChild(v);
                        if (index >= 0) {
                            callClickEpisode(viewGroup, v, index, true, false, false);
                        }
                    }
                });
                item.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        LeanBackUtil.log("ListTvEpisodesPlusPresenter => initLayoutEpisode => onKey => action = " + event.getAction() + ", code = " + keyCode);

                        // left
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            try {

                                int checkedIndexRange = getCheckedIndexRange();
                                int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
                                LeanBackUtil.log("ListTvEpisodesPlusPresenter => initLayoutEpisode => onKey => left => checkedIndexEpisode = " + checkedIndexEpisode);

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
                                LeanBackUtil.log("ListTvEpisodesPlusPresenter => initLayoutEpisode => onKey => left => " + e.getMessage());
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
                                notifyEpisode1(viewGroup, checkedIndexEpisode, false, false, false);
                                notifyEpisode1(viewGroup, newCheckedIndexEpisode, true, false, false);
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
                                LeanBackUtil.log("ListTvEpisodesPlusPresenter => initLayoutEpisode => onKey => right => " + e.getMessage());
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
                                notifyEpisode1(viewGroup, checkedIndexEpisode, false, false, false);
                                notifyEpisode1(viewGroup, newCheckedIndexEpisode, true, false, false);
                            } catch (Exception e) {
                            }
                        }
                        // down
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            focusDownEpisode(viewGroup);
                            return true;
                        }
                        // up
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            int episodeIndexOfChild = findEpisodeIndexOfChild(v);
                            notifyEpisode1(viewGroup, episodeIndexOfChild, false, false, true);
                        }
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => initLayoutEpisode => " + e.getMessage());
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => initLayoutRange => rangeLength = " + rangeLength + ", rangePadding = " + rangePadding);
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
                // listener
                item.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        LeanBackUtil.log("ListTvEpisodesPlusPresenter => initLayoutRange => onKey => action = " + event.getAction() + ", code = " + keyCode);
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
                            LeanBackUtil.log("ListTvEpisodesPlusPresenter => initLayoutRange => onKey => left => rangeIndexOfChild = " + rangeIndexOfChild);
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
                            LeanBackUtil.log("ListTvEpisodesPlusPresenter => initLayoutRange => onKey => up => rangeIndexOfChild = " + rangeIndexOfChild);
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => initLayoutRange => " + e.getMessage());
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getIndexOfEpisodeData => " + e.getMessage());
            throw e;
        }
    }

    private final int getIndexOfEpisodeLength(int index) {
        try {
            List<T> list = getIndexOfEpisodeData(index);
            return list.size();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getIndexOfEpisodeLength => " + e.getMessage());
            return -1;
        }
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
            for (int i = size - 1; i >= 0; i--) {
                T t = list.get(i);
                LeanBackUtil.log("ListTvEpisodesPlusPresenter => getCheckedIndexEpisode => checkedIndexRange = " + checkedIndexRange + ", i = " + i + ", data = " + t.toString());
                if (null == t) continue;
                boolean checked = t.isChecked();
                if (checked)
                    return i;
            }
            throw new Exception("not find getCheckedIndexEpisode: " + checkedIndexRange);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getCheckedIndexEpisode => " + e.getMessage());
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
                LeanBackUtil.log("ListTvEpisodesPlusPresenter => getPlayingIndexEpisode => playingIndexRange = " + playingIndexRange + ", i = " + i + ", data = " + t.toString());
                if (null == t)
                    continue;
                boolean playing = t.isPlaying();
                if (playing)
                    return i;
            }
            throw new Exception("not find getPlayingIndexEpisode: playingIndexRange = " + playingIndexRange);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getPlayingIndexEpisode => " + e.getMessage());
            return -1;
        }
    }

    private final int getCheckedIndexRange() {
        try {
            int index = -1;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (null == entry) continue;
                T t = entry.getKey();
                if (null == t)
                    continue;
                index += 1;
                boolean checked = t.isChecked();
                if (checked) {
                    LeanBackUtil.log("ListTvEpisodesPlusPresenter => getCheckedIndexRange => index = " + index);
                    return index;
                }
            }
            throw new Exception("index error: " + index);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getCheckedIndexRange => " + e.getMessage());
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
                if (playing) {
                    LeanBackUtil.log("ListTvEpisodesPlusPresenter => getPlayingIndexRange => index = " + index);
                    return index;
                }
            }
            throw new Exception("index error: " + index);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getPlayingIndexRange => " + e.getMessage());
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
        LeanBackUtil.log("ListTvEpisodesPlusPresenter => isPlayingIndexOfEpisode => index = " + index);
        LeanBackUtil.log("ListTvEpisodesPlusPresenter => isPlayingIndexOfEpisode => playingIndexEpisode = " + playingIndexEpisode);
        LeanBackUtil.log("ListTvEpisodesPlusPresenter => isPlayingIndexOfEpisode => checkedIndexRange = " + checkedIndexRange);
        LeanBackUtil.log("ListTvEpisodesPlusPresenter => isPlayingIndexOfEpisode => playingIndexRange = " + playingIndexRange);
        return index == playingIndexEpisode && checkedIndexRange == playingIndexRange;
    }

    private final boolean isCheckedAndPlayingIndexOfEpisode(int index) {
        int checkedIndexRange = getCheckedIndexRange();
        int playingIndexRange = getPlayingIndexRange();
        int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
        LeanBackUtil.log("ListTvEpisodesPlusPresenter => isCheckedAndPlayingIndexOfEpisode => index = " + index);
        LeanBackUtil.log("ListTvEpisodesPlusPresenter => isCheckedAndPlayingIndexOfEpisode => checkedIndexEpisode = " + checkedIndexEpisode);
        LeanBackUtil.log("ListTvEpisodesPlusPresenter => isCheckedAndPlayingIndexOfEpisode => checkedIndexRange = " + checkedIndexRange);
        LeanBackUtil.log("ListTvEpisodesPlusPresenter => isCheckedAndPlayingIndexOfEpisode => playingIndexRange = " + playingIndexRange);
        return index == checkedIndexEpisode && checkedIndexRange == playingIndexRange;
    }

    private final boolean isCheckedIndexOfEpisode(int index) {
        int checkedIndexRange = getCheckedIndexRange();
        int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
        LeanBackUtil.log("ListTvEpisodesPlusPresenter => isCheckedIndexOfEpisode => index = " + index);
        LeanBackUtil.log("ListTvEpisodesPlusPresenter => isCheckedIndexOfEpisode => checkedIndexRange = " + checkedIndexRange);
        return index == checkedIndexEpisode;
    }

    private final boolean isPlayingIndexOfRange(int index) {
        int playingIndexRange = getPlayingIndexRange();
        return index == playingIndexRange;
    }

    private final boolean isCheckedIndexOfRange(int index) {
        int checkedIndexRange = getCheckedIndexRange();
        LeanBackUtil.log("ListTvEpisodesPlusPresenter => isCheckedIndexOfRange => checkedIndexRange = " + checkedIndexRange + ", index = " + index);
        return index == checkedIndexRange;
    }

    private final int getEpisodeLength() {
        try {
            int length = 0;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                List<T> value = entry.getValue();
                if (null == value)
                    continue;
                int size = value.size();
                if (size <= 0)
                    continue;
                length += size;
            }
            if (length <= 0)
                throw new Exception("length error: " + length);
            return length;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getEpisodeLength => " + e.getMessage());
            return -1;
        }
    }

    private final int getRangeLength() {
        try {
            return mData.size();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getRangeLength => " + e.getMessage());
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getIndexOfRangeData => " + e.getMessage());
            throw null;
        }
    }

    private final void notifyEpisode1(ViewGroup viewGroup, int index, boolean hasFocus, boolean isClick, boolean isVertical) {
        try {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => notifyEpisode => index = " + index + ", isVertical = " + isVertical);

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
//            View child = ((ViewGroup) v.getParent()).getChildAt(index);
            LinearLayout layoutRoot = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
            View child = layoutRoot.getChildAt(index);
            onBindViewHolderEpisode(child.getContext(), child, t, index, hasFocus, isPlaying, isChecked);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => notifyEpisode => " + e.getMessage());
        }
    }

    private final void notifyRange(@NonNull ViewGroup viewGroup, int index, boolean hasFocus, boolean isClick, boolean isVertical, boolean isScroll) {
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => notifyRange => index = " + index + ", hasFocus" + hasFocus + ", isClick = " + isClick + ", isChecked = " + isChecked + ", isPlaying = " + isPlaying);

            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_ranges);
            View childAt = layout.getChildAt(index);
            T t = getIndexOfRangeData(index);
            onBindViewHolderRange(childAt.getContext(), childAt, t, index, hasFocus, isPlaying, isChecked);
            if (isScroll) {
//                HorizontalScrollView scrollView = (HorizontalScrollView) layout.getParent();
//                scrollView.scrollTo(0, childAt.getRight());
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => notifyRange => " + e.getMessage());
        }
    }

    private final void callClickEpisode(@NonNull LinearLayout viewGroup, View v, int index, boolean isFromUser, boolean isPlaying, boolean isNext) {

        LeanBackUtil.log("ListTvEpisodesPlusPresenter => callClickEpisode => isFromUser = " + isFromUser);
        LeanBackUtil.log("ListTvEpisodesPlusPresenter => callClickEpisode => isPlaying = " + isPlaying);
        LeanBackUtil.log("ListTvEpisodesPlusPresenter => callClickEpisode => isNext = " + isNext);
        // user click
        if (isFromUser) {
            boolean playingIndexOfEpisode = isPlayingIndexOfEpisode(index);
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => callClickEpisode => playingIndexOfEpisode = " + playingIndexOfEpisode);
            if (!playingIndexOfEpisode) {
                int checkedIndexRange = getCheckedIndexRange();
                int playingIndexRange = getPlayingIndexRange();
                int playingIndexEpisode = getPlayingIndexEpisode();
                int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);

                // 1.重置index
                resetPlayingIndex(checkedIndexRange, index);
                // 2. 还原之前正在playing, episode
                notifyEpisode1(viewGroup, checkedIndexEpisode, false, false, false);
                notifyEpisode1(viewGroup, playingIndexEpisode, false, false, false);
                notifyEpisode1(viewGroup, index, true, true, true);
                // 3. 还原之前正在playing, range
                notifyRange(viewGroup, playingIndexRange, false, false, false, false);
                notifyRange(viewGroup, checkedIndexRange, false, true, true, false);
                // 4. listener
                try {
                    List<T> list = getIndexOfEpisodeData(checkedIndexRange);
                    T t = list.get(index);
                    int episodeNum = initEpisodeNum();
                    int real = checkedIndexRange * episodeNum + index;
                    onClickEpisode(v.getContext(), v, t, real, isFromUser);
                } catch (Exception e) {
                    LeanBackUtil.log("ListTvEpisodesPlusPresenter => callClickEpisode => " + e.getMessage());
                }
            }
        }
        // init start
        else {

            int checkedIndexRange = getCheckedIndexRange();
            int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => callClickEpisode => checkedIndexRange = " + checkedIndexRange);
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => callClickEpisode => checkedIndexEpisode = " + checkedIndexEpisode);

            if (isPlaying) {
                try {
                    List<T> list = getIndexOfEpisodeData(checkedIndexRange);
                    T t = list.get(index);
                    int episodeNum = initEpisodeNum();
                    int real = checkedIndexRange * episodeNum + index;
                    onClickEpisode(v.getContext(), v, t, real, false);
                } catch (Exception e) {
                    LeanBackUtil.log("ListTvEpisodesPlusPresenter => callClickEpisode => " + e.getMessage());
                }

                if (isNext) {
                    notifyEpisode1(viewGroup, checkedIndexEpisode, false, true, true);
                    notifyRange(viewGroup, checkedIndexRange, false, true, true, false);
                }

            } else {
                notifyEpisode1(viewGroup, checkedIndexEpisode, false, true, true);
                notifyRange(viewGroup, checkedIndexRange, false, true, true, false);
            }
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => resetPlayingIndex => " + e.getMessage());
        }
    }

    private final void resetCheckedIndex(int checkedIndexRange, int checkedIndexEpisode) {
        try {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => resetCheckedIndex => checkedIndexRange = " + checkedIndexRange + ", checkedIndexEpisode = " + checkedIndexEpisode);
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
                    LeanBackUtil.log("ListTvEpisodesPlusPresenter => resetCheckedIndex => index = " + index + ", m = " + m);
                    t2.setChecked(index == checkedIndexRange && m == checkedIndexEpisode);
                    LeanBackUtil.log("ListTvEpisodesPlusPresenter => resetCheckedIndex => " + t2.toString());
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => resetPlayingIndex => " + e.getMessage());
        }
    }

    private final void notifyEpisodeAdapter(Context context, LinearLayout viewGroup, int cleanFocusIndex, int requestFocusIndex) {
        try {
            int checkedIndexRange = getCheckedIndexRange();
            if (checkedIndexRange == -1) {
                checkedIndexRange = 0;
            }
            List<T> list = getIndexOfEpisodeData(checkedIndexRange);
            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => notifyEpisodeAdapter => layout = " + layout);
            int max = layout.getChildCount();
            int num = list.size();
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => notifyEpisodeAdapter => checkedIndexRange = " + checkedIndexRange);
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => notifyEpisodeAdapter => max = " + max);
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => notifyEpisodeAdapter => num = " + num);
            for (int i = 0; i < max; i++) {
                View child = layout.getChildAt(i);
                LeanBackUtil.log("ListTvEpisodesPlusPresenter => notifyEpisodeAdapter => i = " + i + ", child = " + child);
                if (null == child) continue;
                child.setVisibility(i + 1 <= num ? View.VISIBLE : View.INVISIBLE);
                if (i + 1 > num) continue;
                T t = list.get(i);
                LeanBackUtil.log("ListTvEpisodesPlusPresenter => notifyEpisodeAdapter => " + t.toString());
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => notifyEpisodeAdapter => " + e.getMessage());
        }
    }

    private final void focusDownEpisode(@NonNull LinearLayout viewGroup) {
        try {
            int checkedIndexRange = getCheckedIndexRange();
            int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
            // 1
            notifyEpisode1(viewGroup, checkedIndexEpisode, false, false, true);
            // 2
            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_ranges);
            View childAt = layout.getChildAt(checkedIndexRange);
            childAt.requestFocus();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => focusDownEpisode => " + e.getMessage());
        }
    }

    private final void focusUpRange(@NonNull LinearLayout viewGroup) {
        try {
            int index;
            int checkedIndexRange = getCheckedIndexRange();
            int playingIndexRange = getPlayingIndexRange();
            int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
            int playingIndexEpisode = getPlayingIndexEpisode();
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => focusUpRange => checkedIndexRange = " + checkedIndexRange + ", playingIndexRange = " + playingIndexRange + ", checkedIndexEpisode = " + checkedIndexEpisode + ", playingIndexEpisode = " + playingIndexEpisode);
//            if (checkedIndexRange == playingIndexRange && playingIndexEpisode!= -1) {
//                index = playingIndexEpisode;
//            } else  {
            index = checkedIndexEpisode;
//            }
            LinearLayout layout = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
            View childAt = layout.getChildAt(index);
            childAt.requestFocus();
            notifyEpisode1(viewGroup, index, true, false, true);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => focusUpRange => " + e.getMessage());
        }
    }

    private final void startEpisodePosition(ViewGroup viewGroup, int position, boolean isPlaying, boolean isFromUser, boolean isNext) {
        try {

            if (position < 0)
                throw new Exception("position error: " + position);
            int episodeLength = getEpisodeLength();
            if (position >= episodeLength) {
                if (!isFromUser) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startEpisodePosition(viewGroup, position, isPlaying, isFromUser, isNext);
                        }
                    }, 100);
                }
                throw new Exception("position error, episodeLength: " + episodeLength);
            }
            // 1
            int oldPlayingIndexRange = getPlayingIndexRange();
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => startEpisodePosition => oldPlayingIndexRange = " + oldPlayingIndexRange);
            int oldCheckedIndexRange = getCheckedIndexRange();
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => startEpisodePosition => oldCheckedIndexRange = " + oldCheckedIndexRange);
            int oldPlayingIndexEpisode = getPlayingIndexEpisode();
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => startEpisodePosition => oldPlayingIndexEpisode = " + oldPlayingIndexEpisode);
            int oldCheckedIndexEpisode = getCheckedIndexEpisode(oldCheckedIndexRange);
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => startEpisodePosition => oldCheckedIndexEpisode = " + oldCheckedIndexEpisode);
            int episodeNum = initEpisodeNum();
            int newCheckedIndexRange = position / episodeNum;
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => startEpisodePosition => newCheckedIndexRange = " + newCheckedIndexRange);
            int newCheckedIndexEpisode = position % episodeNum;
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => startEpisodePosition => newCheckedIndexEpisode = " + newCheckedIndexEpisode);
            int index = -1;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (null == entry)
                    continue;
                T t = entry.getKey();
                if (null == t)
                    continue;
                index += 1;
                t.setChecked(index == newCheckedIndexRange);
                t.setPlaying(index == newCheckedIndexRange);

                List<T> value = entry.getValue();
                if (null == value)
                    continue;
                int size = value.size();
                if (size <= 0)
                    continue;
                for (int i = 0; i < size; i++) {
                    T t1 = value.get(i);
                    if (null == t1)
                        continue;
                    t1.setChecked(index == newCheckedIndexRange && i == newCheckedIndexEpisode);
                    t1.setPlaying(index == newCheckedIndexRange && i == newCheckedIndexEpisode);
                }
            }
            // 2
            notifyRange(viewGroup, oldPlayingIndexRange, false, false, false, false);
            notifyRange(viewGroup, oldCheckedIndexRange, false, false, false, false);
            notifyEpisode1(viewGroup, oldPlayingIndexEpisode, false, false, false);
            notifyEpisode1(viewGroup, oldCheckedIndexEpisode, false, false, false);
            // 3
            LinearLayout layoutRoot = viewGroup.findViewById(R.id.lb_list_tv_episodes_root);
            if (newCheckedIndexRange != oldCheckedIndexRange) {
                notifyEpisodeAdapter(layoutRoot.getContext(), layoutRoot, -1, -1);
            }
            // 4
            LinearLayout layoutEpisode = viewGroup.findViewById(R.id.lb_list_tv_episodes_items);
            View childAt = layoutEpisode.getChildAt(newCheckedIndexEpisode);
            callClickEpisode(layoutRoot, childAt, newCheckedIndexEpisode, isFromUser, isPlaying, isNext);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => startEpisodePosition => " + e.getMessage());
        }
    }

    /**************/

    public final boolean dispatchEventActionDown(BaseGridView viewGroup, int index) {
        try {
            RecyclerView.ViewHolder viewHolder = viewGroup.findViewHolderForAdapterPosition(index);
            LinearLayout layout = viewHolder.itemView.findViewById(R.id.lb_list_tv_episodes_items);

            int checkedIndexRange = getCheckedIndexRange();
            int checkedIndexEpisode = getCheckedIndexEpisode(checkedIndexRange);
            View childAt = layout.getChildAt(checkedIndexEpisode);
            childAt.requestFocus();

            notifyEpisode1(viewGroup, checkedIndexEpisode, true, false, true);
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => intoFromKeyEvent => " + e.getMessage());
            return false;
        }
    }

    public final void startEpisodePosition(ViewGroup viewGroup, int position) {
        startEpisodePosition(viewGroup, position, true, false, false);
    }

    public final void updateEpisodePosition(ViewGroup viewGroup, int position) {
        startEpisodePosition(viewGroup, position, false, false, false);
    }

    public final void startEpisodeNext(ViewGroup viewGroup) {
        try {
            boolean episodeEnd = isEpisodeEnd();
            if (episodeEnd)
                throw new Exception("episodeEnd error: true");
            // 1
            int position = getEpisodeCurrentPosition();
            int next = getEpisodeNextPosition();
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => startEpisodeNext => position = " + position);
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => startEpisodeNext => next = " + next);
            // 2
            startEpisodePosition(viewGroup, next, true, false, true);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => startEpisodeNext => " + e.getMessage());
        }
    }

    public final int getEpisodeCurrentPosition() {
        try {
            int playingIndexRange = getPlayingIndexRange();
            int playingIndexEpisode = getPlayingIndexEpisode();
            int episodeNum = initEpisodeNum();
            return episodeNum * playingIndexRange + playingIndexEpisode;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getEpisodeCurrentPosition => " + e.getMessage());
            return -1;
        }
    }

    public final int getEpisodeNextPosition() {
        try {
            int playingIndexRange = getPlayingIndexRange();
            int playingIndexEpisode = getPlayingIndexEpisode();
            int episodeNum = initEpisodeNum();
            return episodeNum * playingIndexRange + playingIndexEpisode + 1;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getEpisodeNextPosition => " + e.getMessage());
            return -1;
        }
    }

    public final boolean isEpisodeEnd() {
        try {
            int episodeLength = getEpisodeLength();
            int next = getEpisodeNextPosition();
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => isEpisodeEnd => episodeLength = " + episodeLength);
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => isEpisodeEnd => next = " + next);
            return next >= episodeLength;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => isEpisodeEnd => " + e.getMessage());
            return true;
        }
    }
}
