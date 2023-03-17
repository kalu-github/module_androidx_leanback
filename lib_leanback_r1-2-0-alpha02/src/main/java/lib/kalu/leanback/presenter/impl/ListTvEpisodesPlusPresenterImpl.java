package lib.kalu.leanback.presenter.impl;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.annotation.Keep;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lib.kalu.leanback.presenter.bean.TvEpisodesPlusItemBean;
import lib.kalu.leanback.util.LeanBackUtil;

public interface ListTvEpisodesPlusPresenterImpl<T extends TvEpisodesPlusItemBean> extends ListTvPresenterImpl {

    LinkedHashMap<TvEpisodesPlusItemBean, List<TvEpisodesPlusItemBean>> mData = new LinkedHashMap<>();

    /**********/

    @LayoutRes
    int initEpisodeLayout();

    @LayoutRes
    int initRangeLayout();

    /************/

    default int initRangeNum() {
        return 5;
    }

    default int initRangePadding(@NonNull Context context) {
        return 0;
    }

    default int initRangeMarginTop(@NonNull Context context) {
        return 0;
    }

    default int initEpisodeNum() {
        return 10;
    }

    default int initEpisodePadding(@NonNull Context context) {
        return 0;
    }

    default int initEpisodeMarginBottom(@NonNull Context context) {
        return 0;
    }

    /********************/

    default int getEpisodeLength() {
        try {
            int length = 0;
            for (Map.Entry<TvEpisodesPlusItemBean, List<TvEpisodesPlusItemBean>> entry : mData.entrySet()) {
                List<TvEpisodesPlusItemBean> value = entry.getValue();
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

    default int getRangeLength() {
        try {
            return mData.size();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getRangeLength => " + e.getMessage());
            return 0;
        }
    }

    /*********************/

    default <T extends TvEpisodesPlusItemBean> void formatData(Object item) {
        try {
            mData.clear();
            List<T> list = (List<T>) item;
            int size = list.size();
            int episodeNum = initEpisodeNum();
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
                key.setFocus(false);
                key.setPlaying(false);
                key.setChecked(false);

                // map-value
                ArrayList<T> value = new ArrayList<>();
                for (int m = start - 1; m <= (end - 1); m++) {
                    T t = list.get(m);
                    if (null == t) continue;
                    t.setFocus(false);
                    t.setPlaying(false);
                    t.setChecked(false);
                    value.add(t);
                }

                // map
                mData.put((TvEpisodesPlusItemBean) key, (List<TvEpisodesPlusItemBean>) value);
            }

        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => formatData => " + e.getMessage());
        }
    }

    default T getDataIndexOfRange(int position) {
        try {
            int index = 0;
            for (Map.Entry<TvEpisodesPlusItemBean, List<TvEpisodesPlusItemBean>> entry : mData.entrySet()) {
                if (index == position) {
                    return (T) entry.getKey();
                }
                index += 1;
            }
            throw new Exception("position error: " + position);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getDataIndexOfRange => " + e.getMessage());
            throw null;
        }
    }

    default List<T> getDataIndexOfEpisode(int position) {
        try {
            int index = 0;
            for (Map.Entry<TvEpisodesPlusItemBean, List<TvEpisodesPlusItemBean>> entry : mData.entrySet()) {
                if (index == position)
                    return (List<T>) entry.getValue();
                index += 1;
            }
            throw new Exception("not find error: " + index);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getDataIndexOfEpisode => " + e.getMessage());
            return null;
        }
    }

    default int getPlayingIndexOfRange() {
        try {
            int index = -1;
            for (Map.Entry<TvEpisodesPlusItemBean, List<TvEpisodesPlusItemBean>> entry : mData.entrySet()) {
                if (null == entry)
                    continue;
                TvEpisodesPlusItemBean t = entry.getKey();
                if (null == t)
                    continue;
                index += 1;
                if (t.isPlaying()) {
                    return index;
                }
            }
            throw new Exception("index error: " + index);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getPlayingIndexOfRange => " + e.getMessage());
            return -1;
        }
    }

    default int getCheckedIndexOfRange() {
        try {
            int index = -1;
            for (Map.Entry<TvEpisodesPlusItemBean, List<TvEpisodesPlusItemBean>> entry : mData.entrySet()) {
                if (null == entry)
                    continue;
                TvEpisodesPlusItemBean t = entry.getKey();
                if (null == t)
                    continue;
                index += 1;
                if (t.isChecked()) {
                    return index;
                }
            }
            throw new Exception("index error: " + index);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getCheckedIndexOfRange => " + e.getMessage());
            return -1;
        }
    }

    default int getPlayingIndexOfEpisode(int playingIndexRange) {
        try {
            if (playingIndexRange < 0)
                throw new Exception("playingIndexRange error: " + playingIndexRange);
            List<T> list = getDataIndexOfEpisode(playingIndexRange);
            if (null == list)
                throw new Exception("list error: null");
            int size = list.size();
            if (size <= 0) throw new Exception("size error: " + size);
            for (int i = 0; i < size; i++) {
                T t = list.get(i);
                if (null == t)
                    continue;
                if (t.isPlaying())
                    return i;
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getPlayingIndexOfEpisode => " + e.getMessage());
            return -1;
        }
    }

    default int getCheckedIndexOfEpisode(int checkedIndexRange) {
        try {
            if (checkedIndexRange < 0)
                throw new Exception("checkedIndexRange error: " + checkedIndexRange);
            List<T> list = getDataIndexOfEpisode(checkedIndexRange);
            if (null == list)
                throw new Exception("list error: null");
            int size = list.size();
            if (size <= 0) throw new Exception("size error: " + size);
            for (int i = 0; i < size; i++) {
                T t = list.get(i);
                if (null == t)
                    continue;
                if (t.isPlaying())
                    return i;
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => getCheckedIndexOfEpisode => " + e.getMessage());
            return -1;
        }
    }

    default void updateRangeData(@NonNull ViewGroup viewGroup, @IdRes int groupId) {
        try {
            LinearLayout layoutGroup = viewGroup.findViewById(groupId);
            if (null == layoutGroup)
                throw new Exception("layoutGroup error: null");
            int playingIndexOfRange = getPlayingIndexOfRange();
            if (playingIndexOfRange < 0)
                throw new Exception("playingIndexOfRange error: " + playingIndexOfRange);
            int count = layoutGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View v = layoutGroup.getChildAt(i);
                if (null == v)
                    continue;
                try {
                    T t = getDataIndexOfRange(playingIndexOfRange + i);
                    if (null == t)
                        throw new Exception();
                    v.setVisibility(View.VISIBLE);
                    v.setTag(R.id.lb_listtvepisodespluspresenterimpl_data, t);
                    onBindHolderRange(v.getContext(), v, t, i);
                } catch (Exception e) {
                    v.setVisibility(View.GONE);
                    v.setTag(R.id.lb_listtvepisodespluspresenterimpl_data, null);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => updateRangeData => " + e.getMessage());
        }
    }

    default void updateEepisodesData(@NonNull ViewGroup viewGroup, @IdRes int groupId) {
        try {
            LinearLayout layoutGroup = viewGroup.findViewById(groupId);
            if (null == layoutGroup)
                throw new Exception("layoutGroup error: null");
            int playingIndexOfRange = getPlayingIndexOfRange();
            if (playingIndexOfRange < 0)
                throw new Exception("playingIndexOfRange error: " + playingIndexOfRange);
            int count = layoutGroup.getChildCount();
            List<T> list = getDataIndexOfEpisode(playingIndexOfRange);
            for (int i = 0; i < count; i++) {
                View v = layoutGroup.getChildAt(i);
                if (null == v)
                    continue;
                try {
                    T t = list.get(i);
                    if (null == t)
                        throw new Exception();
                    v.setVisibility(View.VISIBLE);
                    v.setTag(R.id.lb_listtvepisodespluspresenterimpl_data, t);
                    onBindHolderRange(v.getContext(), v, t, i);
                } catch (Exception e) {
                    v.setVisibility(View.GONE);
                    v.setTag(R.id.lb_listtvepisodespluspresenterimpl_data, null);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => updateEepisodesData => " + e.getMessage());
        }
    }

    default void checkedPosition(@NonNull ViewGroup viewGroup,
                                 int episodePosition,
                                 @IdRes int episodeGroupId,
                                 @IdRes int rangeGroupId) {
        try {
            // 1
            resetEpisodeLayoutStatus(viewGroup, episodeGroupId);
            // 2
            resetRangeLayoutStatus(viewGroup, rangeGroupId);
            // 3
            checkedRangeLayoutStatus(viewGroup, rangeGroupId, episodePosition);
            // 4
            checkedRangeLayoutStatus(viewGroup, episodeGroupId, episodePosition);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => checkedPosition => " + e.getMessage(), e);
        }
    }

    default void resetRangeLayoutStatus(@NonNull ViewGroup viewGroup,
                                        @IdRes int rangeGroupId) {
        try {
            ViewGroup groupLayout = viewGroup.findViewById(rangeGroupId);
            if (null == groupLayout)
                throw new Exception("groupLayout error: null");
            int count = groupLayout.getChildCount();
            if (count <= 0)
                throw new Exception("count error: " + count);
            for (int i = 0; i < count; i++) {
                View v = groupLayout.getChildAt(i);
                if (null == v)
                    continue;
                T t = (T) v.getTag(R.id.lb_listtvepisodespluspresenterimpl_data);
                if (null == t)
                    continue;
                if (t.isPlaying() || t.isChecked() || t.isFocus()) {
                    t.setChecked(false);
                    t.setPlaying(false);
                    t.setFocus(false);
                    onBindHolderRange(v.getContext(), v, t, i);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => resetRangeLayoutStatus => " + e.getMessage(), e);
        }
    }

    default void checkedRangeLayoutStatus(@NonNull ViewGroup viewGroup,
                                          @IdRes int rangeGroupId,
                                          int episodePosition) {
        try {
            ViewGroup groupLayout = viewGroup.findViewById(rangeGroupId);
            if (null == groupLayout)
                throw new Exception("groupLayout error: null");
            int count = groupLayout.getChildCount();
            if (count <= 0)
                throw new Exception("count error: " + count);
            int episodeNum = initEpisodeNum();
            int checkedIndexRange = episodePosition / episodeNum;
            int rangeNum = initRangeNum();
            int endRange;
            if (checkedIndexRange >= rangeNum) {
                endRange = checkedIndexRange;
            } else {
                endRange = rangeNum - 1;
            }
            int startRange = endRange - 9;
            for (int i = 0; i < count; i++) {
                View v = groupLayout.getChildAt(i);
                if (null == v)
                    continue;
                int rangeIndex = startRange + i;
                T t = getDataIndexOfRange(rangeIndex);
                if (null == t)
                    continue;
                t.setFocus(false);
                t.setChecked(rangeIndex == checkedIndexRange);
                t.setPlaying(rangeIndex == checkedIndexRange);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => checkedRangeLayoutStatus => " + e.getMessage(), e);
        }
    }

    default void checkedEpisodeLayoutStatus(@NonNull ViewGroup viewGroup,
                                            @IdRes int rangeGroupId,
                                            int episodePosition) {
        try {
            ViewGroup groupLayout = viewGroup.findViewById(rangeGroupId);
            if (null == groupLayout)
                throw new Exception("groupLayout error: null");
            int count = groupLayout.getChildCount();
            if (count <= 0)
                throw new Exception("count error: " + count);
            int episodeNum = initEpisodeNum();
            int checkedIndexRange = episodePosition / episodeNum;
            int episodeIndex = episodePosition % episodeNum;
            List<T> list = getDataIndexOfEpisode(checkedIndexRange);
            for (int i = 0; i < count; i++) {
                View v = groupLayout.getChildAt(i);
                if (null == v)
                    continue;
                T t = list.get(i);
                if (null == t)
                    continue;
                t.setFocus(false);
                t.setChecked(i == episodeIndex);
                t.setPlaying(i == checkedIndexRange);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => checkedEpisodeLayoutStatus => " + e.getMessage(), e);
        }
    }

    default void resetEpisodeLayoutStatus(@NonNull ViewGroup viewGroup,
                                          @IdRes int episodeGroupId) {
        try {
            ViewGroup groupLayout = viewGroup.findViewById(episodeGroupId);
            if (null == groupLayout)
                throw new Exception("groupLayout error: null");
            int count = groupLayout.getChildCount();
            if (count <= 0)
                throw new Exception("count error: " + count);
            for (int i = 0; i < count; i++) {
                View v = groupLayout.getChildAt(i);
                if (null == v)
                    continue;
                T t = (T) v.getTag(R.id.lb_listtvepisodespluspresenterimpl_data);
                if (null == t)
                    continue;
                if (t.isPlaying() || t.isChecked() || t.isFocus()) {
                    t.setChecked(false);
                    t.setPlaying(false);
                    t.setFocus(false);
                    onBindHolderRange(v.getContext(), v, t, i);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => resetEpisodeLayoutStatus => " + e.getMessage(), e);
        }
    }

//    default void resetMapDataStatus() {
//        try {
//            for (Map.Entry<TvEpisodesPlusItemBean, List<TvEpisodesPlusItemBean>> entry : mData.entrySet()) {
//                if (null == entry) continue;
//                TvEpisodesPlusItemBean t1 = entry.getKey();
//                if (null == t1)
//                    continue;
//                t1.setFocus(false);
//                t1.setChecked(false);
//                t1.setPlaying(false);
//                List<TvEpisodesPlusItemBean> list = entry.getValue();
//                if (null == list)
//                    continue;
//                for (TvEpisodesPlusItemBean t2 : list) {
//                    t2.setFocus(false);
//                    t2.setChecked(false);
//                    t2.setPlaying(false);
//                }
//            }
//        } catch (Exception e) {
//            LeanBackUtil.log("ListTvEpisodesPlusPresenter => resetMapDataStatus => " + e.getMessage(), e);
//        }
//    }

    /*********************/

    @Keep
    default void initLayoutRange(@NonNull Context context, @NonNull ViewGroup viewGroup, @IdRes int groupId) {
        try {
            // 1
            LinearLayout layoutGroup = viewGroup.findViewById(groupId);
            if (null == layoutGroup)
                throw new Exception("layoutGroup error: null");
            layoutGroup.removeAllViews();
            if (null != layoutGroup.getLayoutParams()) {
                int rangeMarginTop = initRangeMarginTop(context);
                ((LinearLayout.LayoutParams) layoutGroup.getLayoutParams()).leftMargin = 0;
                ((LinearLayout.LayoutParams) layoutGroup.getLayoutParams()).rightMargin = 0;
                ((LinearLayout.LayoutParams) layoutGroup.getLayoutParams()).topMargin = rangeMarginTop;
                ((LinearLayout.LayoutParams) layoutGroup.getLayoutParams()).bottomMargin = 0;
            }
            // 2
            int rangeNum = initRangeNum();
            int rangePadding = initRangePadding(context);
            int rangeMargin = rangePadding * (rangeNum - 1) / rangeNum;
            layoutGroup.setWeightSum(rangeNum);
            // 3
            for (int i = 0; i < rangeNum; i++) {
                View item = LayoutInflater.from(context).inflate(initRangeLayout(), layoutGroup, false);
                layoutGroup.addView(item);
                if (null != item.getLayoutParams()) {
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).weight = 1;
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).bottomMargin = 0;
                    ((LinearLayout.LayoutParams) item.getLayoutParams()).topMargin = 0;
                    if (i == 0) {
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).leftMargin = 0;
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).rightMargin = rangeMargin;
                    } else if (i + 1 >= rangeNum) {
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).leftMargin = rangeMargin;
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).rightMargin = 0;
                    } else {
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).leftMargin = rangeMargin;
                        ((LinearLayout.LayoutParams) item.getLayoutParams()).rightMargin = rangeMargin;
                    }
                }
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LeanBackUtil.log("ListTvEpisodesPresenterImpl => onClick => v = " + v);
                        try {
                            int position = layoutGroup.indexOfChild(v);
                            T t = (T) v.getTag(R.id.lb_listtvepisodespluspresenterimpl_data);
                            onClickRange(v.getContext(), v, t, position, true);
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvEpisodesPresenterImpl => onClick => " + e.getMessage());
                        }
                    }
                });
                item.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        LeanBackUtil.log("ListTvEpisodesPresenterImpl => onKey => action = " + event.getAction() + ", code = " + keyCode);
                        // left
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        }
                        // right
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        }
                        // down
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        }
                        // up
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        }
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => initLayoutRange => " + e.getMessage());
        }
    }

    @Keep
    default void initLayoutEpisode(@NonNull Context context, @NonNull ViewGroup viewGroup, @IdRes int groupId) {
        try {
            // 1
            LinearLayout layoutGroup = viewGroup.findViewById(groupId);
            if (null == layoutGroup)
                throw new Exception("layoutGroup error: null");
            layoutGroup.removeAllViews();
            if (null != layoutGroup.getLayoutParams()) {
                int episodeMarginBottom = initEpisodeMarginBottom(context);
                ((LinearLayout.LayoutParams) layoutGroup.getLayoutParams()).leftMargin = 0;
                ((LinearLayout.LayoutParams) layoutGroup.getLayoutParams()).rightMargin = 0;
                ((LinearLayout.LayoutParams) layoutGroup.getLayoutParams()).topMargin = 0;
                ((LinearLayout.LayoutParams) layoutGroup.getLayoutParams()).bottomMargin = episodeMarginBottom;
            }
            // 2
            int episodeNum = initEpisodeNum();
            int episodePadding = initEpisodePadding(context);
            int episodeMargin = episodePadding * (episodeNum - 1) / episodeNum;
            layoutGroup.setWeightSum(episodeNum);
            // 3
            for (int i = 0; i < episodeNum; i++) {
                View item = LayoutInflater.from(context).inflate(initEpisodeLayout(), layoutGroup, false);
                layoutGroup.addView(item);
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
                        LeanBackUtil.log("ListTvEpisodesPresenterImpl => onClick => v = " + v);
                        try {
                            int position = layoutGroup.indexOfChild(v);
                            T t = (T) v.getTag(R.id.lb_listtvepisodespluspresenterimpl_data);
                            onClickEpisode(v.getContext(), v, t, position, true);
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvEpisodesPresenterImpl => onClick => " + e.getMessage());
                        }
                    }
                });
                item.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        LeanBackUtil.log("ListTvEpisodesPresenterImpl => onKey => action = " + event.getAction() + ", code = " + keyCode);
                        // left
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        }
                        // right
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        }
                        // down
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        }
                        // up
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        }
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => initLayoutEpisode => " + e.getMessage());
        }
    }

    /*********************/

    default void onBindHolderEpisode(@NonNull Context context, @NonNull View v, @NonNull T item, @NonNull int position) {
    }

    default void onBindHolderRange(@NonNull Context context, @NonNull View v, @NonNull T item, @NonNull int position) {
    }

    default void onClickEpisode(@NonNull Context context, @NonNull View v, @NonNull T item, @NonNull int position, boolean isFromUser) {
    }

    default void onClickRange(@NonNull Context context, @NonNull View v, @NonNull T item, @NonNull int position, boolean isFromUser) {
    }

    /*********************/
}
