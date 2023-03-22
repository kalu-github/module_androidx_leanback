package lib.kalu.leanback.presenter.impl;

import android.content.Context;
import android.view.FocusFinder;
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
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => getEpisodeLength => " + e.getMessage());
            return -1;
        }
    }

    default int getRangeLength() {
        try {
            return mData.size();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => getRangeLength => " + e.getMessage());
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
            int rangeMax;
            if (size % episodeNum == 0) {
                rangeMax = size / episodeNum;
            } else {
                rangeMax = size / episodeNum + 1;
            }
            for (int i = 0; i < size; i += episodeNum) {

                // map-key
                Class<T> cls = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                T key = cls.newInstance();
                int start = (i / episodeNum) * episodeNum + 1;
                int end = start + (episodeNum - 1);
                if (end >= size) {
                    end = size;
                }
                key.setRangeStart(start);
                key.setRangeEnd(end);
                key.setRangeIndex(start / episodeNum);
                key.setRangeMax(rangeMax);
                key.setFocus(false);
                key.setPlaying(false);
                key.setChecked(false);

                // map-value
                LinkedList<T> value = new LinkedList<>();
                for (int m = start - 1; m <= (end - 1); m++) {
                    T t = list.get(m);
                    if (null == t)
                        continue;
                    t.setEpisodeIndex(m);
                    t.setEpisodeMax(size);
                    t.setFocus(false);
                    t.setPlaying(false);
                    t.setChecked(false);
                    value.add(t);
                }

                // map
                mData.put(key, (List<TvEpisodesPlusItemBean>) value);
            }

        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => formatData => " + e.getMessage());
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => getDataIndexOfRange => " + e.getMessage());
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => getDataIndexOfEpisode => " + e.getMessage());
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => getPlayingIndexOfRange => " + e.getMessage());
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => getCheckedIndexOfRange => " + e.getMessage());
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => getPlayingIndexOfEpisode => " + e.getMessage());
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => getCheckedIndexOfEpisode => " + e.getMessage());
            return -1;
        }
    }

    default void setDataRange(@NonNull ViewGroup viewGroup,
                              @IdRes int groupId,
                              @NonNull int startIndex,
                              @NonNull int checkedIndex) {
        try {
            if (checkedIndex < 0)
                throw new Exception("checkedIndex error: " + checkedIndex);
            if (startIndex < 0)
                throw new Exception("startIndex error: " + startIndex);
            LinearLayout layoutGroup = viewGroup.findViewById(groupId);
            if (null == layoutGroup)
                throw new Exception("layoutGroup error: null");
            int count = layoutGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View v = layoutGroup.getChildAt(i);
                if (null == v)
                    continue;
                int real = startIndex + i;
                T t = getDataIndexOfRange(real);
                t.setFocus(real == checkedIndex);
                t.setChecked(real == checkedIndex);
                v.setVisibility(null == t ? View.GONE : View.VISIBLE);
                v.setTag(R.id.lb_presenter_range, t);
                if (null == t)
                    continue;
                onBindHolderRange(v.getContext(), v, t, i);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => setDataRange => " + e.getMessage());
        }
    }

    default void setDataEepisode(@NonNull ViewGroup viewGroup, @IdRes int groupId, int checkedIndexRange) {
        try {
            if (checkedIndexRange < 0)
                throw new Exception("checkedIndexRange error: " + checkedIndexRange);
            LinearLayout layoutGroup = viewGroup.findViewById(groupId);
            if (null == layoutGroup)
                throw new Exception("layoutGroup error: null");
            int count = layoutGroup.getChildCount();
            List<T> list = getDataIndexOfEpisode(checkedIndexRange);
            for (int i = 0; i < count; i++) {
                View v = layoutGroup.getChildAt(i);
                if (null == v)
                    continue;
                try {
                    T t = list.get(i);
                    v.setTag(R.id.lb_presenter_episode, t);
                    v.setVisibility(View.VISIBLE);
                    onBindHolderEpisode(v.getContext(), v, t, i);
                } catch (Exception e) {
                    v.setTag(R.id.lb_presenter_episode, null);
                    v.setVisibility(View.INVISIBLE);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => updateEepisodesData => " + e.getMessage());
        }
    }

    default void cleanDataRange(@NonNull ViewGroup viewGroup,
                                @IdRes int groupId) {
        try {
            ViewGroup groupLayout = viewGroup.findViewById(groupId);
            if (null == groupLayout)
                throw new Exception("groupLayout error: null");
            int count = groupLayout.getChildCount();
            if (count <= 0)
                throw new Exception("count error: " + count);
            for (int i = 0; i < count; i++) {
                View v = groupLayout.getChildAt(i);
                if (null == v)
                    continue;
                T t = (T) v.getTag(R.id.lb_presenter_range);
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => cleanDataRange => " + e.getMessage(), e);
        }
    }

    default void cleanDataEpisode(@NonNull ViewGroup viewGroup,
                                  @IdRes int groupId) {
        try {
            ViewGroup groupLayout = viewGroup.findViewById(groupId);
            if (null == groupLayout)
                throw new Exception("groupLayout error: null");
            int count = groupLayout.getChildCount();
            if (count <= 0)
                throw new Exception("count error: " + count);
            for (int i = 0; i < count; i++) {
                View v = groupLayout.getChildAt(i);
                if (null == v)
                    continue;
                T t = (T) v.getTag(R.id.lb_presenter_episode);
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
            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => cleanDataEpisode => " + e.getMessage(), e);
        }
    }

//    default void checkedRangeLayoutStatus(@NonNull ViewGroup viewGroup,
//                                          @IdRes int rangeGroupId,
//                                          int episodePosition) {
//        try {
//            ViewGroup groupLayout = viewGroup.findViewById(rangeGroupId);
//            if (null == groupLayout)
//                throw new Exception("groupLayout error: null");
//            int count = groupLayout.getChildCount();
//            if (count <= 0)
//                throw new Exception("count error: " + count);
//            int episodeNum = initEpisodeNum();
//            int checkedIndexRange = episodePosition / episodeNum;
//            int rangeNum = initRangeNum();
//            int endRange;
//            if (checkedIndexRange >= rangeNum) {
//                endRange = checkedIndexRange;
//            } else {
//                endRange = rangeNum - 1;
//            }
//            int startRange = endRange - 9;
//            for (int i = 0; i < count; i++) {
//                View v = groupLayout.getChildAt(i);
//                if (null == v)
//                    continue;
//                int rangeIndex = startRange + i;
//                T t = getDataIndexOfRange(rangeIndex);
//                if (null == t)
//                    continue;
//                t.setFocus(false);
//                t.setChecked(rangeIndex == checkedIndexRange);
//                t.setPlaying(rangeIndex == checkedIndexRange);
//            }
//        } catch (Exception e) {
//            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => checkedRangeLayoutStatus => " + e.getMessage(), e);
//        }
//    }

//    default void checkedEpisodeLayoutStatus(@NonNull ViewGroup viewGroup,
//                                            @IdRes int rangeGroupId,
//                                            int episodePosition) {
//        try {
//            ViewGroup groupLayout = viewGroup.findViewById(rangeGroupId);
//            if (null == groupLayout)
//                throw new Exception("groupLayout error: null");
//            int count = groupLayout.getChildCount();
//            if (count <= 0)
//                throw new Exception("count error: " + count);
//            int episodeNum = initEpisodeNum();
//            int checkedIndexRange = episodePosition / episodeNum;
//            int episodeIndex = episodePosition % episodeNum;
//            List<T> list = getDataIndexOfEpisode(checkedIndexRange);
//            for (int i = 0; i < count; i++) {
//                View v = groupLayout.getChildAt(i);
//                if (null == v)
//                    continue;
//                T t = list.get(i);
//                if (null == t)
//                    continue;
//                t.setFocus(false);
//                t.setChecked(i == episodeIndex);
//                t.setPlaying(i == checkedIndexRange);
//            }
//        } catch (Exception e) {
//            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => checkedEpisodeLayoutStatus => " + e.getMessage(), e);
//        }
//    }

    default void refreshDataRange(@NonNull ViewGroup viewGroup,
                                  @NonNull int startRangeIndex,
                                  @NonNull int checkedRangeIndex,
                                  @IdRes int episodeGroupId,
                                  @IdRes int rangeGroupId) {
        try {
            View viewRange = viewGroup.findViewById(rangeGroupId);
            if (null == viewRange)
                throw new Exception("viewRange error: null");
            if (!(viewRange instanceof LinearLayout))
                throw new Exception("viewRange error: null");
            cleanDataRange(viewGroup, rangeGroupId);
            setDataRange(viewGroup, rangeGroupId, startRangeIndex, checkedRangeIndex);
            cleanDataEpisode(viewGroup, episodeGroupId);
            setDataEepisode(viewGroup, episodeGroupId, checkedRangeIndex);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenterImpl => refreshDataRange => " + e.getMessage(), e);
        }
    }

    default void requestFocusCheckedEpisode(ViewGroup viewGroup, int groupId) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            LinearLayout groupLayout = viewGroup.findViewById(groupId);
            if (null == groupLayout)
                throw new Exception("groupLayout error: null");
            int childCount = groupLayout.getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            for (int i = 0; i < childCount; i++) {
                View v = groupLayout.getChildAt(i);
                if (null == v)
                    continue;
                T t = (T) v.getTag(R.id.lb_presenter_episode);
                if (null == t)
                    continue;
                if (t.isChecked()) {
                    t.setFocus(true);
                    t.setChecked(true);
                    onBindHolderEpisode(v.getContext(), v, t, i);
                    return;
                }
            }
            View v = groupLayout.getChildAt(0);
            T t = (T) v.getTag(R.id.lb_presenter_episode);
            if (null == t)
                throw new Exception("t error: null");
            t.setFocus(true);
            t.setChecked(true);
            onBindHolderEpisode(v.getContext(), v, t, 0);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => requestFocusCheckedEpisode => " + e.getMessage());
        }
    }

    default void cleanFocusCheckedEpisode(ViewGroup viewGroup, int groupId) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            LinearLayout groupLayout = viewGroup.findViewById(groupId);
            if (null == groupLayout)
                throw new Exception("groupLayout error: null");
            int childCount = groupLayout.getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            for (int i = 0; i < childCount; i++) {
                View v = groupLayout.getChildAt(i);
                if (null == v)
                    continue;
                T t = (T) v.getTag(R.id.lb_presenter_episode);
                if (null == t)
                    continue;
                if (t.isFocus()) {
                    t.setFocus(false);
                    t.setChecked(true);
                    onBindHolderEpisode(v.getContext(), v, t, i);
                    return;
                }
            }
            View v = groupLayout.getChildAt(0);
            T t = (T) v.getTag(R.id.lb_presenter_episode);
            if (null == t)
                throw new Exception("t error: null");
            t.setFocus(true);
            t.setChecked(true);
            onBindHolderEpisode(v.getContext(), v, t, 0);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => cleanFocusCheckedEpisode => " + e.getMessage());
        }
    }

    default void requestFocusCheckedRange(ViewGroup viewGroup, int groupId) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            LinearLayout groupLayout = viewGroup.findViewById(groupId);
            if (null == groupLayout)
                throw new Exception("groupLayout error: null");
            int childCount = groupLayout.getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            for (int i = 0; i < childCount; i++) {
                View v = groupLayout.getChildAt(i);
                if (null == v)
                    continue;
                T t = (T) v.getTag(R.id.lb_presenter_range);
                if (null == t)
                    continue;
                if (t.isChecked()) {
                    t.setFocus(true);
                    t.setChecked(true);
                    onBindHolderRange(v.getContext(), v, t, i);
                    return;
                }
            }
            View v = groupLayout.getChildAt(0);
            T t = (T) v.getTag(R.id.lb_presenter_range);
            if (null == t)
                throw new Exception("t error: null");
            t.setFocus(true);
            t.setChecked(true);
            onBindHolderRange(v.getContext(), v, t, 0);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => requestFocusCheckedRange => " + e.getMessage());
        }
    }

    default void cleanFocusCheckedRange(ViewGroup viewGroup, int groupId) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            LinearLayout groupLayout = viewGroup.findViewById(groupId);
            if (null == groupLayout)
                throw new Exception("groupLayout error: null");
            int childCount = groupLayout.getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            for (int i = 0; i < childCount; i++) {
                View v = groupLayout.getChildAt(i);
                if (null == v)
                    continue;
                T t = (T) v.getTag(R.id.lb_presenter_range);
                if (null == t)
                    continue;
                if (t.isFocus()) {
                    t.setFocus(false);
                    t.setChecked(true);
                    onBindHolderRange(v.getContext(), v, t, i);
                    return;
                }
            }
            View v = groupLayout.getChildAt(0);
            T t = (T) v.getTag(R.id.lb_presenter_range);
            if (null == t)
                throw new Exception("t error: null");
            t.setFocus(false);
            t.setChecked(true);
            onBindHolderRange(v.getContext(), v, t, 0);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => cleanFocusCheckedRange => " + e.getMessage());
        }
    }

    /*********************/

    @Keep
    default void initLayoutRange(@NonNull Context context,
                                 @NonNull ViewGroup viewGroup,
                                 @IdRes int rangeGroupId,
                                 @IdRes int episodeGroupId) {
        try {
            // 1
            LinearLayout layoutGroup = viewGroup.findViewById(rangeGroupId);
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
                        try {
                            int indexOfChild = layoutGroup.indexOfChild(v);
                            if (indexOfChild < 0)
                                throw new Exception("indexOfChild error: " + indexOfChild);
                            T t = (T) v.getTag(R.id.lb_presenter_range);
                            if (null == t)
                                throw new Exception("t error: null");
                            onClickRange(v.getContext(), v, t, indexOfChild, true);
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
                            try {
                                T t = (T) v.getTag(R.id.lb_presenter_range);
                                if (null == t)
                                    throw new Exception("t error: null");
                                int rangeIndex = t.getRangeIndex();
                                LeanBackUtil.log("ListTvEpisodesPresenterImpl => onKey => left => rangeIndex = " + rangeIndex);
                                if (rangeIndex <= 0)
                                    throw new Exception("rangeIndex warning: " + rangeIndex);
                                int indexOfChild = layoutGroup.indexOfChild(v);
                                LeanBackUtil.log("ListTvEpisodesPresenterImpl => onKey => left => indexOfChild = " + indexOfChild);
                                if (indexOfChild < 0)
                                    throw new Exception("indexOfChild warning: " + indexOfChild);
                                int startIndex = rangeIndex - 1;
                                if (indexOfChild > 0) {
                                    // 1
                                    t.setFocus(false);
                                    t.setChecked(false);
                                    onBindHolderRange(v.getContext(), v, t, indexOfChild);
                                    // 2
                                    View nextFocus = FocusFinder.getInstance().findNextFocus(viewGroup, v, View.FOCUS_LEFT);
                                    T t1 = (T) nextFocus.getTag(R.id.lb_presenter_range);
                                    t1.setFocus(true);
                                    t1.setChecked(true);
                                    onBindHolderRange(nextFocus.getContext(), nextFocus, t1, indexOfChild - 1);
                                    // 3
                                    setDataEepisode(viewGroup, episodeGroupId, startIndex);
                                } else {
                                    LeanBackUtil.log("ListTvEpisodesPresenterImpl => onKey => left => startIndex = " + startIndex);
                                    refreshDataRange(viewGroup, startIndex, startIndex, episodeGroupId, rangeGroupId);
                                }
                            } catch (Exception e) {
                                LeanBackUtil.log("ListTvEpisodesPresenterImpl => onKey => left => " + e.getMessage());
                            }
                        }
                        // right
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            try {
                                T t = (T) v.getTag(R.id.lb_presenter_range);
                                if (null == t)
                                    throw new Exception("t error: null");
                                int rangeIndex = t.getRangeIndex();
                                int rangeMax = t.getRangeMax();
                                LeanBackUtil.log("ListTvEpisodesPresenterImpl => onKey => right => rangeIndex = " + rangeIndex + ", rangeMax = " + rangeMax);
                                if (rangeIndex + 1 >= rangeMax)
                                    throw new Exception("rangeIndex warning: " + rangeIndex);
                                int indexOfChild = layoutGroup.indexOfChild(v);
                                LeanBackUtil.log("ListTvEpisodesPresenterImpl => onKey => right => indexOfChild = " + indexOfChild);
                                if (indexOfChild < 0)
                                    throw new Exception("indexOfChild warning: " + indexOfChild);
                                int childCount = layoutGroup.getChildCount();
                                LeanBackUtil.log("ListTvEpisodesPresenterImpl => onKey => right => childCount = " + childCount);
                                if (childCount <= 0)
                                    throw new Exception("childCount warning: " + childCount);
                                int checkedIndex = rangeIndex + 1;
                                LeanBackUtil.log("ListTvEpisodesPresenterImpl => onKey => right => checkedIndex = " + checkedIndex);
                                if (indexOfChild + 1 < childCount) {
                                    // 1
                                    t.setFocus(false);
                                    t.setChecked(false);
                                    onBindHolderRange(v.getContext(), v, t, indexOfChild);
                                    // 2
                                    View nextFocus = FocusFinder.getInstance().findNextFocus(viewGroup, v, View.FOCUS_RIGHT);
                                    T t1 = (T) nextFocus.getTag(R.id.lb_presenter_range);
                                    t1.setFocus(true);
                                    t1.setChecked(true);
                                    onBindHolderRange(nextFocus.getContext(), nextFocus, t1, indexOfChild + 1);
                                    // 3
                                    setDataEepisode(viewGroup, episodeGroupId, checkedIndex);
                                } else {
                                    int startIndex = checkedIndex - (initRangeNum() - 1);
                                    LeanBackUtil.log("ListTvEpisodesPresenterImpl => onKey => right => startIndex = " + startIndex);
                                    refreshDataRange(viewGroup, startIndex, checkedIndex, episodeGroupId, rangeGroupId);
                                }
                            } catch (Exception e) {
                                LeanBackUtil.log("ListTvEpisodesPresenterImpl => onKey => right => " + e.getMessage());
                            }
                        }
                        // down1
                        else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            requestFocusCheckedRange(viewGroup, rangeGroupId);
                        }
                        // down2
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            cleanFocusCheckedRange(viewGroup, rangeGroupId);
                        }
                        // up
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            cleanFocusCheckedRange(viewGroup, rangeGroupId);
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
    default void initLayoutEpisode(@NonNull Context context,
                                   @NonNull ViewGroup viewGroup,
                                   @IdRes int rangeGroupId,
                                   @IdRes int episodeGroupId) {
        try {
            // 1
            LinearLayout layoutGroup = viewGroup.findViewById(episodeGroupId);
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
                        try {
                            int indexOfChild = layoutGroup.indexOfChild(v);
                            if (indexOfChild < 0)
                                throw new Exception("indexOfChild error: " + indexOfChild);
                            T t = (T) v.getTag(R.id.lb_presenter_episode);
                            if (null == t)
                                throw new Exception("t error: null");
                            onClickEpisode(v.getContext(), v, t, indexOfChild, true);
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
                        // down1
                        else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            requestFocusCheckedEpisode(viewGroup, episodeGroupId);
                        }
                        // down2
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            cleanFocusCheckedEpisode(viewGroup, episodeGroupId);
                        }
                        // up1
                        else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            requestFocusCheckedEpisode(viewGroup, episodeGroupId);
                        }
                        // up2
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            cleanFocusCheckedEpisode(viewGroup, episodeGroupId);
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
