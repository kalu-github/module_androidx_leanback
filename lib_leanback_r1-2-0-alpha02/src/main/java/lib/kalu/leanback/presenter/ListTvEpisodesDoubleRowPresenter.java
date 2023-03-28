package lib.kalu.leanback.presenter;

import android.content.Context;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.annotation.Keep;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lib.kalu.leanback.presenter.bean.TvEpisodesPlusItemBean;
import lib.kalu.leanback.presenter.impl.ListTvPresenterImpl;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvEpisodesDoubleRowPresenter<T extends TvEpisodesPlusItemBean> extends Presenter implements ListTvPresenterImpl {

    /***************************/

    private final Map<T, List<T>> mData = new LinkedHashMap<>();

    /***************************/

    @LayoutRes
    protected abstract int initEpisodeLayout();

    @LayoutRes
    protected abstract int initRangeLayout();

    @IntRange(from = 1, to = 1000)
    protected abstract int initRangeNum();

    @IntRange(from = 1, to = 10)
    protected abstract int initEpisodeNum();


    /***************************/

    protected int initRangePadding(@NonNull Context context) {
        return 0;
    }

    protected int initRangeMarginTop(@NonNull Context context) {
        return 0;
    }

    protected int initEpisodePadding(@NonNull Context context) {
        return 0;
    }

    protected int initEpisodeMarginBottom(@NonNull Context context) {
        return 0;
    }

    /***************************/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.lb_list_tv_episodes_double_row, parent, false);
            setPadding(context, viewGroup);
            setBackgroundColor(context, viewGroup);
            setContentBackgroundColor(context, viewGroup, R.id.module_leanback_lep_episodes);
            setContentBackgroundColor(context, viewGroup, R.id.module_leanback_lep_ranges);
            setTitlePadding(context, viewGroup, R.id.module_leanback_lep_title);
            setTitleTextColor(context, viewGroup, R.id.module_leanback_lep_title);
            setTitleTextSize(context, viewGroup, R.id.module_leanback_lep_title);
            setTitleAssetTTF(context, viewGroup, R.id.module_leanback_lep_title);
            setTitleBackgroundColor(context, viewGroup, R.id.module_leanback_lep_title);
            initLayoutEpisode(context, viewGroup, R.id.module_leanback_lep_ranges, R.id.module_leanback_lep_episodes);
            initLayoutRange(context, viewGroup, R.id.module_leanback_lep_ranges, R.id.module_leanback_lep_episodes);
            return new ViewHolder(viewGroup);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPlusPresenter => onCreateViewHolder => " + e.getMessage());
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        // 数据
        formatData(item);
        // 标题
        updateTitle(viewHolder.view, R.id.module_leanback_lep_title);
        // 检查
        checkViewItemCount(viewHolder.view);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    public void onBindHolderEpisode(@NonNull Context context, @NonNull View v, @NonNull T item,
                                    @NonNull int position) {
    }

    public void onBindHolderRange(@NonNull Context context, @NonNull View v, @NonNull T item,
                                  @NonNull int position) {

    }

    public void onClickEpisode(@NonNull Context context, @NonNull View v, @NonNull T item,
                               @NonNull int position, boolean isFromUser) {
    }

    public void onClickRange(@NonNull Context context, @NonNull View v, @NonNull T item,
                             @NonNull int position, boolean isFromUser) {
    }

    /************/

    private final void checkViewItemCount(View view) {
        try {
            if (null == view)
                throw new Exception("view error: null");
            ViewGroup rangeGroup = view.findViewById(R.id.module_leanback_lep_ranges);
            int rangeChildCount = rangeGroup.getChildCount();
            if (rangeChildCount <= 0)
                throw new Exception("rangeChildCount error: " + rangeChildCount);
            int size = mData.size();
            if (size >= rangeChildCount)
                throw new Exception("size warning: " + size + ", rangeChildCount = " + rangeChildCount);
            for (int i = 0; i < rangeChildCount; i++) {
                View child = rangeGroup.getChildAt(i);
                child.setVisibility(i >= size ? View.INVISIBLE : View.VISIBLE);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => checkViewItemCount => " + e.getMessage());
        }
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
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getEpisodeLength => " + e.getMessage());
            return -1;
        }
    }

    private final int getRangeLength() {
        try {
            return mData.size();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getRangeLength => " + e.getMessage());
            return 0;
        }
    }

    /*********************/

    private final void formatData(Object item) {
        try {
            int length = mData.size();
            if (length > 0)
                throw new Exception("length warning: " + length);
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
                    t.setRangeStart(start);
                    t.setRangeEnd(end);
                    t.setRangeIndex(start / episodeNum);
                    t.setRangeMax(rangeMax);
                    t.setEpisodeIndex(m);
                    t.setEpisodeMax(size);
                    t.setFocus(false);
                    t.setPlaying(false);
                    t.setChecked(false);
                    value.add(t);
                }

                // map
                mData.put(key, value);
            }

        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => formatData => " + e.getMessage());
        }
    }

    private final T getDataIndexOfRange(int position) {
        try {
            int index = 0;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (index == position) {
                    return (T) entry.getKey();
                }
                index += 1;
            }
            throw new Exception("position error: " + position);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getDataIndexOfRange => " + e.getMessage());
            throw null;
        }
    }

    private final List<T> getDataIndexOfEpisode(int position) {
        try {
            int index = 0;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
                if (index == position)
                    return entry.getValue();
                index += 1;
            }
            throw new Exception("not find error: " + index);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getDataIndexOfEpisode => " + e.getMessage());
            return null;
        }
    }

    private final int getPlayingIndexOfRange() {
        try {
            int index = -1;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
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
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getPlayingIndexOfRange => " + e.getMessage());
            return -1;
        }
    }

    private final int getCheckedIndexOfRange() {
        try {
            int index = -1;
            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
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
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getCheckedIndexOfRange => " + e.getMessage());
            return -1;
        }
    }

    private final int getPlayingIndexOfEpisode(int playingIndexRange) {
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
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getPlayingIndexOfEpisode => " + e.getMessage());
            return -1;
        }
    }

    private final int getCheckedIndexOfEpisode(int checkedIndexRange) {
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
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getCheckedIndexOfEpisode => " + e.getMessage());
            return -1;
        }
    }

    private final void setDataRange(@NonNull ViewGroup viewGroup,
                                    @IdRes int groupId,
                                    @NonNull int startIndex,
                                    @NonNull int checkedIndex) {
        try {
            if (checkedIndex < 0)
                throw new Exception("checkedIndex error: " + checkedIndex);
            if (startIndex < 0)
                throw new Exception("startIndex error: " + startIndex);
            LinearLayout rangeGroup = viewGroup.findViewById(groupId);
            if (null == rangeGroup)
                throw new Exception("layoutGroup error: null");
            int rangeChildCount = rangeGroup.getChildCount();
            int rangeLength = getRangeLength();
            for (int i = 0; i < rangeChildCount; i++) {
                View child = rangeGroup.getChildAt(i);
                if (null == child)
                    continue;
                child.setTag(R.id.lb_presenter_range, null);
                child.setVisibility(i >= rangeLength ? View.INVISIBLE : View.VISIBLE);
                if (i >= rangeChildCount)
                    continue;
                int rangeIndex = startIndex + i;
                T t = getDataIndexOfRange(rangeIndex);
                if (null == t)
                    continue;
                t.setFocus(child.hasFocus() && rangeIndex == checkedIndex);
                t.setChecked(rangeIndex == checkedIndex);
                child.setTag(R.id.lb_presenter_range, t);
                onBindHolderRange(child.getContext(), child, t, i);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => setDataRange => " + e.getMessage());
        }
    }

    private final void setDataEpisode(@NonNull ViewGroup viewGroup,
                                      @IdRes int groupId,
                                      @NonNull int checkedIndexRange,
                                      @NonNull int checkedIndexEspisode,
                                      @NonNull boolean hasFocus) {
        try {
            if (checkedIndexRange < 0)
                throw new Exception("checkedIndexRange error: " + checkedIndexRange);
            LinearLayout layoutGroup = viewGroup.findViewById(groupId);
            if (null == layoutGroup)
                throw new Exception("layoutGroup error: null");
            int count = layoutGroup.getChildCount();
            List<T> list = getDataIndexOfEpisode(checkedIndexRange);
            int size = list.size();
            for (int i = 0; i < count; i++) {
                View child = layoutGroup.getChildAt(i);
                if (null == child)
                    continue;
                if (i >= size) {
                    child.setVisibility(View.INVISIBLE);
                    child.setTag(R.id.lb_presenter_episode, null);
                } else {
                    T t = list.get(i);
                    if (null == t) {
                        child.setVisibility(View.INVISIBLE);
                        child.setTag(R.id.lb_presenter_episode, null);
                    } else {
                        child.setVisibility(View.VISIBLE);
                        child.setTag(R.id.lb_presenter_episode, t);
                        t.setFocus(hasFocus && i == checkedIndexEspisode);
                        t.setChecked(i == checkedIndexEspisode);
                        if (hasFocus && i == checkedIndexEspisode) {
                            child.post(new Runnable() {
                                @Override
                                public void run() {
                                    child.requestFocus();
                                }
                            });
                        }
                        if (i == checkedIndexEspisode) {
                            onClickEpisode(child.getContext(), child, t, i, false);
                        }
                        onBindHolderEpisode(child.getContext(), child, t, i);
                    }
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => setDataEpisode => " + e.getMessage());
        }
    }

    private final void cleanDataRange(@NonNull ViewGroup viewGroup,
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
                    t.setFocus(false);
                    onBindHolderRange(v.getContext(), v, t, i);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => cleanDataRange => " + e.getMessage(), e);
        }
    }

    private final void cleanDataEpisode(@NonNull ViewGroup viewGroup,
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
                    t.setFocus(false);
                    onBindHolderRange(v.getContext(), v, t, i);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => cleanDataEpisode => " + e.getMessage(), e);
        }
    }

    private final void refreshDataRange(@NonNull ViewGroup viewGroup,
                                        @NonNull int startRangeIndex,
                                        @NonNull int checkedRangeIndex,
                                        @IdRes int episodeGroupId,
                                        @IdRes int rangeGroupId,
                                        @NonNull boolean hasFocus) {
        try {
            View viewRange = viewGroup.findViewById(rangeGroupId);
            if (null == viewRange)
                throw new Exception("viewRange error: null");
            if (!(viewRange instanceof LinearLayout))
                throw new Exception("viewRange error: null");
            cleanDataRange(viewGroup, rangeGroupId);
            setDataRange(viewGroup, rangeGroupId, startRangeIndex, checkedRangeIndex);
            cleanDataEpisode(viewGroup, episodeGroupId);
            setDataEpisode(viewGroup, episodeGroupId, checkedRangeIndex, -1, false);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => refreshDataRange => " + e.getMessage(), e);
        }
    }

    private final void requestFocusEpisodeView(ViewGroup viewGroup, int groupId) {
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
                    v.requestFocus();
                    t.setFocus(true);
                    t.setChecked(true);
                    onBindHolderEpisode(v.getContext(), v, t, i);
                    return;
                }
            }
            for (int i = 0; i < childCount; i++) {
                View v = groupLayout.getChildAt(i);
                if (null == v)
                    continue;
                T t = (T) v.getTag(R.id.lb_presenter_episode);
                if (null == t)
                    continue;
                if (t.isPlaying()) {
                    v.requestFocus();
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
            v.requestFocus();
            t.setFocus(true);
            t.setChecked(true);
            onBindHolderEpisode(v.getContext(), v, t, 0);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => requestFocusCheckedEpisode => " + e.getMessage());
        }
    }

    private final void cleanFocusEpisodeView(ViewGroup viewGroup, int groupId) {
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
                    v.clearFocus();
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
            v.clearFocus();
            t.setFocus(true);
            t.setChecked(true);
            onBindHolderEpisode(v.getContext(), v, t, 0);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => cleanFocusCheckedEpisode => " + e.getMessage());
        }
    }

    private final void requestFocusRangeView(ViewGroup viewGroup, int groupId) {
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
                    v.requestFocus();
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
            v.requestFocus();
            t.setFocus(true);
            t.setChecked(true);
            onBindHolderRange(v.getContext(), v, t, 0);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => requestFocusCheckedRange => " + e.getMessage());
        }
    }

    private final void cleanFocusCheckedRange(ViewGroup viewGroup, int groupId) {
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
                    v.clearFocus();
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
            v.clearFocus();
            t.setFocus(false);
            t.setChecked(true);
            onBindHolderRange(v.getContext(), v, t, 0);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => cleanFocusCheckedRange => " + e.getMessage());
        }
    }

    /*********************/

    @Keep
    private final void initLayoutRange(@NonNull Context context,
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
                int rangeLayout = initRangeLayout();
                View item = LayoutInflater.from(context).inflate(rangeLayout, layoutGroup, false);
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
                        LeanBackUtil.log("ListTvEpisodesDoubleLinearLayoutPresenterImpl => onKey2 => action = " + event.getAction() + ", keyCode = " + keyCode);
                        // left
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            return dispatchEventRangeLeft(viewGroup, v, rangeGroupId, episodeGroupId);
                        }
                        // right
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            return dispatchEventRangeRight(viewGroup, v, rangeGroupId, episodeGroupId);
                        }
                        // down
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            cleanFocusCheckedRange(viewGroup, rangeGroupId);
                            return true;
                        }
                        // up
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            cleanFocusCheckedRange(viewGroup, rangeGroupId);
                            requestFocusEpisodeView(viewGroup, episodeGroupId);
                            return true;
                        }
                        // into-from-up
                        else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            requestFocusRangeView(viewGroup, rangeGroupId);
                            return true;
                        }
                        // up
                        else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            return true;
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
    private final void setDataEpisodePlaying(@NonNull View v) {
        try {
            if (null == v)
                throw new Exception("v error: null");
            ViewGroup episodeGroup = (ViewGroup) v.getParent();
            if (null == episodeGroup)
                throw new Exception("episodeGroup error: null");
            if (!(episodeGroup instanceof LinearLayout))
                throw new Exception("episodeGroup error: not instanceof LinearLayout");
            int indexOfChild = episodeGroup.indexOfChild(v);
            if (indexOfChild < 0)
                throw new Exception("indexOfChild error: " + indexOfChild);
            T t = (T) v.getTag(R.id.lb_presenter_episode);
            if (null == t)
                throw new Exception("t error: null");
            t.setPlaying(true);
            t.setChecked(true);
            v.setTag(R.id.lb_presenter_episode_playing, t);
            onBindHolderEpisode(v.getContext(), v, t, indexOfChild);
            onClickEpisode(v.getContext(), v, t, indexOfChild, true);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleLinearLayoutPresenterImpl => setDataEpisodePlaying => " + e.getMessage());
        }
    }

    @Keep
    private final void cleanDataEpisodePlaying(@NonNull ViewGroup viewGroup,
                                               @IdRes int episodeGroupId) {
        // olds
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            ViewGroup episodeGroup = viewGroup.findViewById(episodeGroupId);
            if (null == episodeGroup)
                throw new Exception("episodeGroup error: null");
            if (!(episodeGroup instanceof LinearLayout))
                throw new Exception("episodeGroup error: not instanceof LinearLayout");
            int episodeChildCount = episodeGroup.getChildCount();
            if (episodeChildCount <= 0)
                throw new Exception("episodeChildCount error: " + episodeChildCount);
            for (int i = 0; i < episodeChildCount; i++) {
                View child = episodeGroup.getChildAt(i);
                if (null == child)
                    continue;
                T t = (T) child.getTag(R.id.lb_presenter_episode_playing);
                if (null == t)
                    continue;
                t.setPlaying(false);
                t.setChecked(false);
                child.setTag(R.id.lb_presenter_episode_playing, null);
                onBindHolderEpisode(child.getContext(), child, t, i);
                break;
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleLinearLayoutPresenterImpl => cleanDataEpisodePlaying => " + e.getMessage());
        }
    }

    @Keep
    private final void initLayoutEpisode(@NonNull Context context,
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
                int episodeLayout = initEpisodeLayout();
                View item = LayoutInflater.from(context).inflate(episodeLayout, layoutGroup, false);
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
                        // olds
                        cleanDataEpisodePlaying(viewGroup, episodeGroupId);
                        // news
                        setDataEpisodePlaying(v);
                    }
                });
                item.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        LeanBackUtil.log("ListTvEpisodesDoubleLinearLayoutPresenterImpl => onKey1 => action = " + event.getAction() + ", keyCode = " + keyCode);
                        // left
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            return dispatchEventEpisodesLeft(viewGroup, v, rangeGroupId, episodeGroupId);
                        }
                        // right
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            return dispatchEventEpisodesRight(viewGroup, v, rangeGroupId, episodeGroupId);
                        }
                        // down
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            cleanFocusEpisodeView(viewGroup, episodeGroupId);
                            requestFocusRangeView(viewGroup, rangeGroupId);
                            return true;
                        }
                        // up
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            cleanFocusEpisodeView(viewGroup, episodeGroupId);
                            return true;
                        }
                        // into-from-down
                        else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            requestFocusEpisodeView(viewGroup, episodeGroupId);
                            return true;
                        }
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => initLayoutEpisode => " + e.getMessage());
        }
    }

    private final boolean dispatchEventRangeLeft(@NonNull ViewGroup viewGroup,
                                                 @NonNull View v,
                                                 @IdRes int rangeGroupId,
                                                 @IdRes int episodeGroupId) {

        try {
            if (null == v)
                throw new Exception("v error: null");
            ViewGroup rangeGroup = (ViewGroup) v.getParent();
            if (null == rangeGroup)
                throw new Exception("rangeGroup error: null");
            if (!(rangeGroup instanceof LinearLayout))
                throw new Exception("rangeGroup error: not instanceof LinearLayout");
            T t = (T) v.getTag(R.id.lb_presenter_range);
            if (null == t)
                throw new Exception("t error: null");
            int rangeIndex = t.getRangeIndex();
            if (rangeIndex <= 0)
                throw new Exception("rangeIndexOfChild warning: " + rangeIndex);
            int rangeIndexOfChild = rangeGroup.indexOfChild(v);
            if (rangeIndexOfChild < 0)
                throw new Exception("rangeIndexOfChild warning: " + rangeIndexOfChild);
            int rangeStartIndex = rangeIndex - 1;
            if (rangeIndexOfChild > 0) {
                // 1
                t.setFocus(false);
                t.setChecked(false);
                onBindHolderRange(v.getContext(), v, t, rangeIndexOfChild);
                // 2
                View nextFocus = FocusFinder.getInstance().findNextFocus(rangeGroup, v, View.FOCUS_LEFT);
                T t1 = (T) nextFocus.getTag(R.id.lb_presenter_range);
                t1.setFocus(true);
                t1.setChecked(true);
                onBindHolderRange(nextFocus.getContext(), nextFocus, t1, rangeIndexOfChild - 1);
                // 3
                setDataEpisode(viewGroup, episodeGroupId, rangeStartIndex, -1, false);
            } else {
                refreshDataRange(viewGroup, rangeStartIndex, rangeStartIndex, episodeGroupId, rangeGroupId, false);
            }
            return false;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => dispatchEventRangeLeft => " + e.getMessage());
            return true;
        }
    }

    private final boolean dispatchEventRangeRight(@NonNull ViewGroup viewGroup,
                                                  @NonNull View v,
                                                  @IdRes int rangeGroupId,
                                                  @IdRes int episodeGroupId) {

        try {
            if (null == v)
                throw new Exception("v error: null");
            ViewGroup rangeGroup = (ViewGroup) v.getParent();
            if (null == rangeGroup)
                throw new Exception("rangeGroup error: null");
            if (!(rangeGroup instanceof LinearLayout))
                throw new Exception("rangeGroup error: not instanceof LinearLayout");
            T t = (T) v.getTag(R.id.lb_presenter_range);
            if (null == t)
                throw new Exception("t error: null");
            int rangeIndex = t.getRangeIndex();
            int rangeMax = t.getRangeMax();
            if (rangeIndex + 1 >= rangeMax)
                throw new Exception("rangeIndex warning: " + rangeIndex);
            int indexOfChild = rangeGroup.indexOfChild(v);
            if (indexOfChild < 0)
                throw new Exception("indexOfChild warning: " + indexOfChild);
            int childCount = rangeGroup.getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount warning: " + childCount);
            int checkedIndex = rangeIndex + 1;
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
                setDataEpisode(viewGroup, episodeGroupId, checkedIndex, -1, false);
            } else {
                int startIndex = checkedIndex - (initRangeNum() - 1);
                refreshDataRange(viewGroup, startIndex, checkedIndex, episodeGroupId, rangeGroupId, false);
            }
            return false;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => dispatchEventRangeRight => " + e.getMessage());
            return true;
        }
    }

    private final boolean dispatchEventEpisodesLeft(@NonNull ViewGroup viewGroup,
                                                    @NonNull View v,
                                                    @IdRes int rangeGroupId,
                                                    @IdRes int episodeGroupId) {
        try {
            int episodeNum = initEpisodeNum();
            if (episodeNum < 0)
                throw new Exception("episodeNum error: " + episodeNum);
            if (null == v)
                throw new Exception("v error: null");
            ViewGroup episodeGroup = (ViewGroup) v.getParent();
            if (null == episodeGroup)
                throw new Exception("episodeGroup error: null");
            if (!(episodeGroup instanceof LinearLayout))
                throw new Exception("episodeGroup error: not instanceof LinearLayout");
            int episodeChildCount = episodeGroup.getChildCount();
            if (episodeChildCount <= 0)
                throw new Exception("episodeChildCount error: " + episodeChildCount);
            int episodeIndexOfChild = episodeGroup.indexOfChild(v);
            if (episodeIndexOfChild < 0)
                throw new Exception("episodeIndexOfChild warning: " + episodeIndexOfChild);
            T t = (T) v.getTag(R.id.lb_presenter_episode);
            if (null == t)
                throw new Exception("t error: null");
            int episodeIndex = t.getEpisodeIndex();
            if (episodeIndex <= 0)
                throw new Exception("episodeIndex warning: " + episodeIndex);
            if (episodeIndexOfChild > 0) {
                // 1
                t.setFocus(false);
                t.setChecked(false);
                onBindHolderEpisode(v.getContext(), v, t, episodeIndexOfChild);
                // 2
                int nextepisodeIndex = episodeIndexOfChild - 1;
                View vNext = episodeGroup.getChildAt(nextepisodeIndex);
                T tNext = (T) vNext.getTag(R.id.lb_presenter_episode);
                tNext.setFocus(true);
                tNext.setChecked(true);
                onBindHolderEpisode(vNext.getContext(), vNext, tNext, nextepisodeIndex);
            } else {
                int rangeIndex = t.getRangeIndex();
                if (rangeIndex < 0)
                    throw new Exception("rangeIndex error: " + rangeIndex);
                // 1
                cleanDataEpisode(viewGroup, episodeGroupId);
                // 2
                ViewGroup rangeGroup = viewGroup.findViewById(rangeGroupId);
                View vRangeFirst = rangeGroup.getChildAt(0);
                T tRangeFirst = (T) vRangeFirst.getTag(R.id.lb_presenter_range);
                int firstRangeIndex = tRangeFirst.getRangeIndex();
                int nextRangeIndex = rangeIndex - 1;
                if (nextRangeIndex <= firstRangeIndex) {
                    cleanDataRange(viewGroup, rangeGroupId);
                    setDataRange(viewGroup, rangeGroupId, nextRangeIndex, nextRangeIndex);
                    setDataEpisode(viewGroup, episodeGroupId, nextRangeIndex, episodeNum - 1, true);
                } else {
                    int rangeNum = initRangeNum();
                    for (int i = 0; i < rangeNum; i++) {
                        View child = rangeGroup.getChildAt(i);
                        if (null == child)
                            continue;
                        T o = (T) child.getTag(R.id.lb_presenter_range);
                        if (null == o)
                            continue;
                        int index = o.getRangeIndex();
                        if (index == rangeIndex || index == nextRangeIndex) {
                            o.setFocus(false);
                            o.setChecked(index == nextRangeIndex);
                            onBindHolderRange(child.getContext(), child, o, i);
                        }
                    }
                    setDataEpisode(viewGroup, episodeGroupId, nextRangeIndex, episodeNum - 1, true);
                }
//                requestFocusEpisodeViewIndex(viewGroup, episodeGroupId, episodeNum - 1);
            }
            return false;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => dispatchEventEpisodesLeft => " + e.getMessage());
            return true;
        }
    }

    private final boolean dispatchEventEpisodesRight(@NonNull ViewGroup viewGroup,
                                                     @NonNull View v,
                                                     @IdRes int rangeGroupId,
                                                     @IdRes int episodeGroupId) {
        try {
            if (null == v)
                throw new Exception("v error: null");
            ViewGroup episodeGroup = (ViewGroup) v.getParent();
            if (null == episodeGroup)
                throw new Exception("episodeGroup error: null");
            if (!(episodeGroup instanceof LinearLayout))
                throw new Exception("episodeGroup error: not instanceof LinearLayout");
            int episodeChildCount = episodeGroup.getChildCount();
            if (episodeChildCount <= 0)
                throw new Exception("episodeChildCount error: " + episodeChildCount);
            int episodeIndexOfChild = episodeGroup.indexOfChild(v);
            if (episodeIndexOfChild < 0)
                throw new Exception("episodeIndexOfChild warning: " + episodeIndexOfChild);
            T t = (T) v.getTag(R.id.lb_presenter_episode);
            if (null == t)
                throw new Exception("t error: null");
            int episodeMax = t.getEpisodeMax();
            if (episodeMax <= 0)
                throw new Exception("episodeMax error: " + episodeMax);
            int episodeIndex = t.getEpisodeIndex();
            if (episodeIndex + 1 >= episodeMax)
                throw new Exception("episodeIndex warning: " + episodeIndex + ", episodeMax = " + episodeMax);
            if (episodeIndexOfChild + 1 < episodeChildCount) {
                // 1
                t.setFocus(false);
                t.setChecked(false);
                onBindHolderEpisode(v.getContext(), v, t, episodeIndexOfChild);
                // 2
                int nextepisodeIndex = episodeIndexOfChild + 1;
                View nextFocus = episodeGroup.getChildAt(nextepisodeIndex);
                T nextT = (T) nextFocus.getTag(R.id.lb_presenter_episode);
                nextT.setFocus(true);
                nextT.setChecked(true);
                onBindHolderEpisode(nextFocus.getContext(), nextFocus, nextT, nextepisodeIndex);
            } else {
                int rangeIndex = t.getRangeIndex();
                if (rangeIndex < 0)
                    throw new Exception("rangeIndex error: " + rangeIndex);
                // 1
                cleanDataEpisode(viewGroup, episodeGroupId);
                // 2
                ViewGroup rangeGroup = viewGroup.findViewById(rangeGroupId);
                int rangeChildCount = rangeGroup.getChildCount();
                int rangeLength = getRangeLength();
                int realLastIndex = Math.min(rangeLength, rangeChildCount);
                View vRangeLast = rangeGroup.getChildAt(realLastIndex - 1);
                T tRangeLast = (T) vRangeLast.getTag(R.id.lb_presenter_range);
                int lastRangeIndex = tRangeLast.getRangeIndex();
                int nextRangeIndex = rangeIndex + 1;
                if (nextRangeIndex >= lastRangeIndex) {
                    int startRangeIndex;
                    if (rangeLength > rangeChildCount) {
                        startRangeIndex = nextRangeIndex - initRangeNum() + 1;
                    } else {
                        startRangeIndex = 0;
                    }
                    cleanDataRange(viewGroup, rangeGroupId);
                    setDataRange(viewGroup, rangeGroupId, startRangeIndex, nextRangeIndex);
                    setDataEpisode(viewGroup, episodeGroupId, nextRangeIndex, 0, true);
                } else {
                    int rangeNum = initRangeNum();
                    for (int i = 0; i < rangeNum; i++) {
                        View child = rangeGroup.getChildAt(i);
                        if (null == child)
                            continue;
                        T o = (T) child.getTag(R.id.lb_presenter_range);
                        if (null == o)
                            continue;
                        int index = o.getRangeIndex();
                        if (index == rangeIndex || index == nextRangeIndex) {
                            o.setFocus(false);
                            o.setChecked(index == nextRangeIndex);
                            onBindHolderRange(child.getContext(), child, o, i);
                        }
                    }
                    setDataEpisode(viewGroup, episodeGroupId, nextRangeIndex, 0, true);
                }
//                requestFocusEpisodeViewIndex(viewGroup, episodeGroupId, 0);
            }
            return false;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => dispatchEventEpisodesRight => " + e.getMessage());
            return true;
        }
    }

    /*********************/

    public final boolean isEpisodeEnd(ViewGroup viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            ViewGroup episodeGroup = viewGroup.findViewById(R.id.module_leanback_lep_episodes);
            if (null == episodeGroup)
                throw new Exception("episodeGroup error: null");
            int childCount = episodeGroup.getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            for (int i = 0; i < childCount; i++) {
                View child = episodeGroup.getChildAt(i);
                if (null == child)
                    continue;
                T t = (T) child.getTag(R.id.lb_presenter_episode_playing);
                if (null == t)
                    continue;
                int episodeIndex = t.getEpisodeIndex();
                if (episodeIndex < 0)
                    continue;
                int episodeMax = t.getEpisodeMax();
                if (episodeMax < 0)
                    continue;
                if (episodeIndex + 1 >= episodeMax)
                    return true;
            }
            throw new Exception("nit find");
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => isEpisodeEnd => " + e.getMessage());
            return false;
        }
    }

    public final int getEpisodeNextPosition(ViewGroup viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            ViewGroup episodeGroup = viewGroup.findViewById(R.id.module_leanback_lep_episodes);
            if (null == episodeGroup)
                throw new Exception("episodeGroup error: null");
            int childCount = episodeGroup.getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            for (int i = 0; i < childCount; i++) {
                View child = episodeGroup.getChildAt(i);
                if (null == child)
                    continue;
                T t = (T) child.getTag(R.id.lb_presenter_episode_playing);
                if (null == t)
                    continue;
                int episodeIndex = t.getEpisodeIndex();
                if (episodeIndex < 0)
                    continue;
                int episodeMax = t.getEpisodeMax();
                if (episodeMax < 0)
                    continue;
                int nextEpisodeIndex = episodeIndex + 1;
                if (nextEpisodeIndex < episodeMax)
                    return nextEpisodeIndex;
            }
            throw new Exception("nit find");
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => getEpisodeNextPosition => " + e.getMessage());
            return -1;
        }
    }

    public final void checkedPositionEpisode(@NonNull ViewGroup viewGroup,
                                             @NonNull int checkedEpisodePosition) {
        checkedPositionEpisode(viewGroup, checkedEpisodePosition, false);
    }

    public final void checkedPositionEpisode(@NonNull ViewGroup viewGroup,
                                             @NonNull int checkedEpisodePosition,
                                             @NonNull boolean hasFocus) {
        try {
            if (checkedEpisodePosition < 0)
                throw new Exception("checkedEpisodePosition error: " + checkedEpisodePosition);
            int episodeLength = getEpisodeLength();
            if (checkedEpisodePosition + 1 >= episodeLength)
                throw new Exception("checkedEpisodePosition error: " + checkedEpisodePosition + ", episodeLength = " + episodeLength);
            int episodeNum = initEpisodeNum();
            int checkedEpisodeIndex = checkedEpisodePosition % episodeNum;
            int rangeStartIndex = checkedEpisodePosition / episodeNum;
            int rangeNum = initRangeNum();
            int rangeLength = getRangeLength();
            if (rangeLength > rangeNum && rangeLength - rangeStartIndex < rangeNum) {
                rangeStartIndex = rangeLength - rangeNum - 1;
            }
            cleanDataRange(viewGroup, R.id.module_leanback_lep_ranges);
            cleanDataEpisode(viewGroup, R.id.module_leanback_lep_episodes);
            setDataRange(viewGroup, R.id.module_leanback_lep_ranges, rangeStartIndex, rangeStartIndex);
            setDataEpisode(viewGroup, R.id.module_leanback_lep_episodes, rangeStartIndex, checkedEpisodeIndex, hasFocus);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => checkedPositionEpisode => " + e.getMessage());
        }
    }

    public final boolean checkedPositionEpisodeNext(@NonNull ViewGroup viewGroup) {
        try {
            int episodeNextPosition = getEpisodeNextPosition(viewGroup);
            if (episodeNextPosition < 0)
                throw new Exception("episodeNextPosition error: " + episodeNextPosition);
            checkedPositionEpisode(viewGroup, episodeNextPosition);
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenterImpl => checkedPositionEpisodeNext => " + e.getMessage());
            return false;
        }
    }
}
