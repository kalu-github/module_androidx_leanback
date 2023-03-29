package lib.kalu.leanback.presenter;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
            initLayoutEpisode(context, viewGroup);
            initLayoutRange(context, viewGroup);
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

    private final void cleanFocusCheckedRange(@NonNull View viewGroup,
                                              @NonNull int indexOfChild) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup rangeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(2);
            if (null == rangeGroup)
                throw new Exception("rangeGroup error: null");
            if (!(rangeGroup instanceof LinearLayout))
                throw new Exception("rangeGroup error: not instanceof LinearLayout");
            int rangeChildCount = rangeGroup.getChildCount();
            if (rangeChildCount <= 0)
                throw new Exception("rangeChildCount error: " + rangeChildCount);
            if (indexOfChild >= rangeChildCount)
                throw new Exception("indexOfChild error: " + indexOfChild + ", rangeChildCount = " + rangeChildCount);
            View child = rangeGroup.getChildAt(indexOfChild);
            if (null == child)
                throw new Exception("child error: null");
            T t = (T) child.getTag(R.id.lb_presenter_range);
            if (null == t)
                throw new Exception("t error: null");
            if (t.isPlaying() || t.isChecked() || t.isFocus()) {
                t.setChecked(false);
                t.setFocus(false);
                onBindHolderRange(child.getContext(), child, t, indexOfChild);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => cleanFocusCheckedRange => " + e.getMessage(), e);
        }
    }

    private final void findCleanFocusCheckedRange(@NonNull View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup rangeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(2);
            if (null == rangeGroup)
                throw new Exception("rangeGroup error: null");
            if (!(rangeGroup instanceof LinearLayout))
                throw new Exception("rangeGroup error: not instanceof LinearLayout");
            int rangeChildCount = rangeGroup.getChildCount();
            if (rangeChildCount <= 0)
                throw new Exception("rangeChildCount error: " + rangeChildCount);
            for (int i = 0; i < rangeChildCount; i++) {
                View child = rangeGroup.getChildAt(rangeChildCount);
                if (null == child)
                    continue;
                T t = (T) child.getTag(R.id.lb_presenter_range);
                if (null == t)
                    continue;
                if (t.isPlaying() || t.isChecked() || t.isFocus()) {
                    t.setChecked(false);
                    t.setFocus(false);
                    onBindHolderRange(child.getContext(), child, t, i);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => findCleanFocusCheckedRange => " + e.getMessage(), e);
        }
    }

    private final void requestFocusCheckedRange(@NonNull View viewGroup,
                                                @NonNull int indexOfChild) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup rangeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(2);
            if (null == rangeGroup)
                throw new Exception("rangeGroup error: null");
            if (!(rangeGroup instanceof LinearLayout))
                throw new Exception("rangeGroup error: not instanceof LinearLayout");
            int rangeChildCount = rangeGroup.getChildCount();
            if (rangeChildCount <= 0)
                throw new Exception("rangeChildCount error: " + rangeChildCount);
            if (indexOfChild >= rangeChildCount)
                throw new Exception("indexOfChild error: " + indexOfChild + ", rangeChildCount = " + rangeChildCount);
            View child = rangeGroup.getChildAt(indexOfChild);
            if (null == child)
                throw new Exception("child error: null");
            T t = (T) child.getTag(R.id.lb_presenter_range);
            if (null == t)
                throw new Exception("t error: null");
            child.requestFocus();
            t.setChecked(true);
            t.setFocus(true);
            onBindHolderRange(child.getContext(), child, t, indexOfChild);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => requestFocusCheckedRange => " + e.getMessage(), e);
        }
    }

    private final void refreshDataEpisode(@NonNull View viewGroup,
                                          @NonNull int rangeIndex,
                                          @NonNull int checkedIndex,
                                          @NonNull boolean isPlay,
                                          @NonNull boolean isFromUser) {
        try {
            if (rangeIndex < 0)
                throw new Exception("rangeIndex error: " + rangeIndex);
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup episodeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(1);
            if (null == episodeGroup)
                throw new Exception("episodeGroup error: null");
            if (!(episodeGroup instanceof LinearLayout))
                throw new Exception("episodeGroup error: not instanceof LinearLayout");
            int episodeChildCount = episodeGroup.getChildCount();
            if (episodeChildCount <= 0)
                throw new Exception("episodeChildCount error: " + episodeChildCount);
            List<T> list = getDataIndexOfEpisode(rangeIndex);
            if (null == list)
                throw new Exception("list error: null");
            int size = list.size();
            if (size <= 0)
                throw new Exception("size error: " + size);
            for (int i = 0; i < episodeChildCount; i++) {
                View child = episodeGroup.getChildAt(i);
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
                        t.setFocus(isFromUser && i == checkedIndex);
                        t.setPlaying(isPlay && i == checkedIndex);//
                        t.setChecked(i == checkedIndex);
                        child.setVisibility(View.VISIBLE);
                        child.setTag(R.id.lb_presenter_episode, t);
                        if (isFromUser && i == checkedIndex) {
                            onClickEpisode(child.getContext(), child, t, i, false);
                        }
                        onBindHolderEpisode(child.getContext(), child, t, i);
                    }
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => refreshDataEpisode => " + e.getMessage());
        }
    }

    private final void refreshDataRange(@NonNull View viewGroup,
                                        @NonNull int startIndex,
                                        @NonNull int checkedIndex) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup rangeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(2);
            if (null == rangeGroup)
                throw new Exception("rangeGroup error: null");
            if (!(rangeGroup instanceof LinearLayout))
                throw new Exception("rangeGroup error: not instanceof LinearLayout");
            int rangeChildCount = rangeGroup.getChildCount();
            if (rangeChildCount <= 0)
                throw new Exception("rangeChildCount error: " + rangeChildCount);
            int rangeLength = getRangeLength();
            if (rangeLength <= 0)
                throw new Exception("rangeLength error: " + rangeLength);
            for (int i = 0; i < rangeChildCount; i++) {
                View child = rangeGroup.getChildAt(i);
                if (null == child)
                    continue;
                child.setTag(R.id.lb_presenter_range, null);
                child.setVisibility(i >= rangeLength ? View.INVISIBLE : View.VISIBLE);
                if (i >= rangeLength)
                    continue;
                int rangeIndex = startIndex + i;
                T t = getDataIndexOfRange(rangeIndex);
                if (null == t)
                    continue;
                t.setFocus(false);
                t.setPlaying(false);
                t.setChecked(rangeIndex == checkedIndex);
                child.setTag(R.id.lb_presenter_range, t);
                onBindHolderRange(child.getContext(), child, t, i);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => refreshDataRange => " + e.getMessage(), e);
        }
    }


    private final void cleanFocusCheckedEpisode(@NonNull View viewGroup,
                                                @NonNull int indexOfChild) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup episodeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(1);
            if (null == episodeGroup)
                throw new Exception("episodeGroup error: null");
            if (!(episodeGroup instanceof LinearLayout))
                throw new Exception("episodeGroup error: not instanceof LinearLayout");
            int episodeChildCount = episodeGroup.getChildCount();
            if (episodeChildCount <= 0)
                throw new Exception("episodeChildCount error: " + episodeChildCount);
            if (indexOfChild >= episodeChildCount)
                throw new Exception("indexOfChild error: " + indexOfChild + ", episodeChildCount = " + episodeChildCount);
            View child = episodeGroup.getChildAt(indexOfChild);
            if (null == child)
                throw new Exception("child error: null");
            T t = (T) child.getTag(R.id.lb_presenter_episode);
            if (null == t)
                throw new Exception("t error: null");
            if (t.isPlaying() || t.isChecked() || t.isFocus()) {
                child.clearFocus();
                t.setChecked(false);
                t.setFocus(false);
                onBindHolderEpisode(child.getContext(), child, t, indexOfChild);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => cleanFocusCheckedEpisode => " + e.getMessage(), e);
        }
    }

    private final void findCleanFocusCheckedEpisode(@NonNull View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup episodeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(1);
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
                T t = (T) child.getTag(R.id.lb_presenter_episode);
                if (null == t)
                    continue;
                if (t.isPlaying() || t.isChecked() || t.isFocus()) {
                    t.setFocus(false);
                    t.setChecked(false);
                    onBindHolderEpisode(child.getContext(), child, t, i);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => cleanFocusCheckedEpisode => " + e.getMessage(), e);
        }
    }

    private final void requestFocusCheckedEpisode(@NonNull View viewGroup,
                                                  @NonNull int indexOfChild) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup episodeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(1);
            if (null == episodeGroup)
                throw new Exception("episodeGroup error: null");
            if (!(episodeGroup instanceof LinearLayout))
                throw new Exception("episodeGroup error: not instanceof LinearLayout");
            int episodeChildCount = episodeGroup.getChildCount();
            if (episodeChildCount <= 0)
                throw new Exception("episodeChildCount error: " + episodeChildCount);
            if (indexOfChild >= episodeChildCount)
                throw new Exception("indexOfChild error: " + indexOfChild + ", episodeChildCount = " + episodeChildCount);
            View child = episodeGroup.getChildAt(indexOfChild);
            if (null == child)
                throw new Exception("child error: null");
            T t = (T) child.getTag(R.id.lb_presenter_episode);
            if (null == t)
                throw new Exception("t error: null");
            child.requestFocus();
            t.setFocus(true);
            t.setChecked(true);
            onBindHolderEpisode(child.getContext(), child, t, indexOfChild);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => requestFocusCheckedEpisode => " + e.getMessage(), e);
        }
    }

    private final void requestFocusCheckedRange(View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup rangeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(2);
            if (null == rangeGroup)
                throw new Exception("rangeGroup error: null");
            if (!(rangeGroup instanceof LinearLayout))
                throw new Exception("rangeGroup error: not instanceof LinearLayout");
            int rangeChildCount = rangeGroup.getChildCount();
            if (rangeChildCount <= 0)
                throw new Exception("rangeChildCount error: " + rangeChildCount);
            for (int i = 0; i < rangeChildCount; i++) {
                View child = rangeGroup.getChildAt(i);
                if (null == child)
                    continue;
                T t = (T) child.getTag(R.id.lb_presenter_range);
                if (null == t)
                    continue;
                if (t.isChecked()) {
                    child.requestFocus();
                    t.setFocus(true);
                    t.setChecked(true);
                    onBindHolderRange(child.getContext(), child, t, i);
                    return;
                }
            }
            View child = rangeGroup.getChildAt(0);
            T t = (T) child.getTag(R.id.lb_presenter_range);
            if (null == t)
                throw new Exception("t error: null");
            child.requestFocus();
            t.setFocus(true);
            t.setChecked(true);
            onBindHolderRange(child.getContext(), child, t, 0);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => requestFocusCheckedRange => " + e.getMessage());
        }
    }

    private final void requestFocusCheckedEpisode(View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup episodeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(1);
            if (null == episodeGroup)
                throw new Exception("episodeGroup error: null");
            if (!(episodeGroup instanceof LinearLayout))
                throw new Exception("episodeGroup error: not instanceof LinearLayout");
            int episodeChildCount = episodeGroup.getChildCount();
            if (episodeChildCount <= 0)
                throw new Exception("episodeChildCount error: " + episodeChildCount);
            for (int i = 0; i < episodeChildCount; i++) {
                View child = episodeGroup.getChildAt(i);
                LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => requestFocusCheckedEpisode => i = " + i + ", child = " + child);
                if (null == child)
                    continue;
                T t = (T) child.getTag(R.id.lb_presenter_episode);
                LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => requestFocusCheckedEpisode => i = " + i + ", t = " + t);
                if (null == t)
                    continue;
                if (t.isChecked() || t.isPlaying()) {
                    t.setFocus(true);
                    t.setChecked(true);
                    onBindHolderEpisode(child.getContext(), child, t, i);
                    return;
                }
            }
            View child = episodeGroup.getChildAt(0);
            T t = (T) child.getTag(R.id.lb_presenter_episode);
            if (null == t)
                throw new Exception("t error: null");
            t.setFocus(true);
            t.setChecked(true);
            onBindHolderEpisode(child.getContext(), child, t, 0);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => requestFocusCheckedEpisode => " + e.getMessage());
        }
    }

    private final void cleanFocusCheckedEpisode(View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup episodeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(1);
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
                T t = (T) child.getTag(R.id.lb_presenter_episode);
                if (null == t)
                    continue;
                if (t.isFocus()) {
                    t.setFocus(false);
                    t.setChecked(true);
                    onBindHolderEpisode(child.getContext(), child, t, i);
                    return;
                }
            }
            View child = episodeGroup.getChildAt(0);
            T t = (T) child.getTag(R.id.lb_presenter_episode);
            if (null == t)
                throw new Exception("t error: null");
            t.setFocus(true);
            t.setChecked(true);
            onBindHolderEpisode(child.getContext(), child, t, 0);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => cleanFocusCheckedEpisode => " + e.getMessage());
        }
    }

    private final void cleanFocusCheckedRange(View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup rangeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(2);
            if (null == rangeGroup)
                throw new Exception("rangeGroup error: null");
            if (!(rangeGroup instanceof LinearLayout))
                throw new Exception("rangeGroup error: not instanceof LinearLayout");
            int rangeChildCount = rangeGroup.getChildCount();
            if (rangeChildCount <= 0)
                throw new Exception("rangeChildCount error: " + rangeChildCount);
            for (int i = 0; i < rangeChildCount; i++) {
                View child = rangeGroup.getChildAt(i);
                if (null == child)
                    continue;
                T t = (T) child.getTag(R.id.lb_presenter_range);
                if (null == t)
                    continue;
                if (t.isFocus()) {
                    t.setFocus(false);
                    t.setChecked(true);
                    onBindHolderRange(child.getContext(), child, t, i);
                    return;
                }
            }
            View child = rangeGroup.getChildAt(0);
            T t = (T) child.getTag(R.id.lb_presenter_range);
            if (null == t)
                throw new Exception("t error: null");
            t.setFocus(false);
            t.setChecked(true);
            onBindHolderRange(child.getContext(), child, t, 0);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => cleanFocusCheckedRange => " + e.getMessage());
        }
    }

    /*********************/

    @Keep
    private final void initLayoutRange(@NonNull Context context,
                                       @NonNull View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup rangeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(2);
            if (null == rangeGroup)
                throw new Exception("rangeGroup error: null");
            if (!(rangeGroup instanceof LinearLayout))
                throw new Exception("rangeGroup error: not instanceof LinearLayout");
            if (null != rangeGroup.getLayoutParams()) {
                int rangeMarginTop = initRangeMarginTop(context);
                ((LinearLayout.LayoutParams) rangeGroup.getLayoutParams()).leftMargin = 0;
                ((LinearLayout.LayoutParams) rangeGroup.getLayoutParams()).rightMargin = 0;
                ((LinearLayout.LayoutParams) rangeGroup.getLayoutParams()).topMargin = rangeMarginTop;
                ((LinearLayout.LayoutParams) rangeGroup.getLayoutParams()).bottomMargin = 0;
            }
            // 2
            int rangeNum = initRangeNum();
            int rangePadding = initRangePadding(context);
            int rangeMargin = rangePadding * (rangeNum - 1) / rangeNum;
            ((LinearLayout) rangeGroup).setWeightSum(rangeNum);
            // 3
            for (int i = 0; i < rangeNum; i++) {
                int rangeLayout = initRangeLayout();
                View item = LayoutInflater.from(context).inflate(rangeLayout, rangeGroup, false);
                rangeGroup.addView(item);
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
                            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => onClick => v = " + v);
                            int checkedIndexOfChild = findCheckedIndexRange(viewGroup);
                            if (checkedIndexOfChild < 0)
                                throw new Exception("checkedIndexOfChild error: " + checkedIndexOfChild);
                            int indexOfChild = rangeGroup.indexOfChild(v);
                            if (indexOfChild == checkedIndexOfChild)
                                throw new Exception("indexOfChild warning: " + indexOfChild + ", indexOfChild = " + indexOfChild);
                            // click
                            T t = (T) v.getTag(R.id.lb_presenter_range);
                            if (null == t)
                                throw new Exception("t error: null");
                            onClickRange(v.getContext(), v, t, indexOfChild, true);
                            // cur
                            cleanFocusCheckedRange(viewGroup, checkedIndexOfChild);
                            // next
                            requestFocusCheckedRange(viewGroup, indexOfChild);
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => onClick => " + e.getMessage());
                        }
                    }
                });
                item.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                        // down-leave
                        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => onKey => Range => down-leave =>");
                            cleanFocusCheckedRange(viewGroup);
                        }
                        // up-into
                        else if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => onKey => Range => up-into =>");
                            requestFocusCheckedRange(viewGroup);
                        }
                        // up-next
                        else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => onKey => Range => up-next =>");
                            cleanFocusCheckedRange(viewGroup);
                            requestFocusCheckedEpisode(viewGroup);
                        }
                        // left-next
                        else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => onKey => Range => left-next =>");
                            dispatchEventRange(viewGroup, view, View.FOCUS_LEFT);
                            return true;
                        }
                        // right-next
                        else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => onKey => Range => right-next =>");
                            dispatchEventRange(viewGroup, view, View.FOCUS_RIGHT);
                            return true;
                        }
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => initLayoutRange => " + e.getMessage());
        }
    }

    @Keep
    private final void initLayoutEpisode(@NonNull Context context,
                                         @NonNull View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup episodeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(1);
            if (null == episodeGroup)
                throw new Exception("episodeGroup error: null");
            if (!(episodeGroup instanceof LinearLayout))
                throw new Exception("episodeGroup error: not instanceof LinearLayout");
            if (null != episodeGroup.getLayoutParams()) {
                int episodeMarginBottom = initEpisodeMarginBottom(context);
                ((LinearLayout.LayoutParams) episodeGroup.getLayoutParams()).leftMargin = 0;
                ((LinearLayout.LayoutParams) episodeGroup.getLayoutParams()).rightMargin = 0;
                ((LinearLayout.LayoutParams) episodeGroup.getLayoutParams()).topMargin = 0;
                ((LinearLayout.LayoutParams) episodeGroup.getLayoutParams()).bottomMargin = episodeMarginBottom;
            }
            // 2
            int episodeNum = initEpisodeNum();
            int episodePadding = initEpisodePadding(context);
            int episodeMargin = episodePadding * (episodeNum - 1) / episodeNum;
            ((LinearLayout) episodeGroup).setWeightSum(episodeNum);
            // 3
            for (int i = 0; i < episodeNum; i++) {
                int episodeLayoutId = initEpisodeLayout();
                View item = LayoutInflater.from(context).inflate(episodeLayoutId, episodeGroup, false);
                episodeGroup.addView(item);
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
                    public void onClick(View view) {
                        try {
                            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => onClick => view = " + view);
                            // click
                            T t = (T) view.getTag(R.id.lb_presenter_episode);
                            if (null == t)
                                throw new Exception("t error: null");
                            if (t.isChecked())
                                throw new Exception("checked warning: 1");
                            int indexOfChild = episodeGroup.indexOfChild(view);
                            if (indexOfChild < 0)
                                throw new Exception("indexOfChild error: " + indexOfChild);
                            // cur
                            findCleanFocusCheckedEpisode(viewGroup);
                            // next
                            requestFocusCheckedEpisode(viewGroup, indexOfChild);
                            // click
                            onClickEpisode(view.getContext(), view, t, indexOfChild, true);
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => onClick => " + e.getMessage());
                        }
                    }
                });
                item.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                        // up-leave
                        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => onKey => Episode => up-leave =>");
                            cleanFocusCheckedEpisode(viewGroup);
                        }
                        // down-into
                        else if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => onKey => Episode => down-into =>");
                            requestFocusCheckedEpisode(viewGroup);
                        }
                        // down-next
                        else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => onKey => Episode => down-next =>");
                            cleanFocusCheckedEpisode(viewGroup);
                            requestFocusCheckedRange(viewGroup);
                            return true;
                        }
                        // left-next
                        else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => onKey => Episode => left-next =>");
                            dispatchEventEpisode(viewGroup,view, View.FOCUS_LEFT);
                            return true;
                        }
                        // right-next
                        else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => onKey => Episode => right-next =>");
                            dispatchEventEpisode(viewGroup, view, View.FOCUS_RIGHT);
                            return true;
                        }
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => initLayoutEpisode => " + e.getMessage());
        }
    }

    private final int findCheckedIndexEpisode(@NonNull View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup episodeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(1);
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
                T t = (T) child.getTag(R.id.lb_presenter_episode);
                if (null == t)
                    continue;
                if (t.isChecked()) {
                    return i;
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => findCheckedIndexEpisode => " + e.getMessage());
            return -1;
        }
    }

    private final int findCheckedIndexRange(@NonNull View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup rangeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(2);
            if (null == rangeGroup)
                throw new Exception("rangeGroup error: null");
            if (!(rangeGroup instanceof LinearLayout))
                throw new Exception("rangeGroup error: not instanceof LinearLayout");
            int rangeChildCount = rangeGroup.getChildCount();
            if (rangeChildCount <= 0)
                throw new Exception("rangeChildCount error: " + rangeChildCount);
            for (int i = 0; i < rangeChildCount; i++) {
                View child = rangeGroup.getChildAt(i);
                if (null == child)
                    continue;
                T t = (T) child.getTag(R.id.lb_presenter_range);
                if (null == t)
                    continue;
                if (t.isChecked()) {
                    return i;
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => findCheckedIndexRange => " + e.getMessage());
            return -1;
        }
    }

    private final boolean dispatchEventEpisode(@NonNull View viewGroup,
                                               @NonNull View focus,
                                               @NonNull int direction) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup episodeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(1);
            if (null == episodeGroup)
                throw new Exception("episodeGroup error: null");
            if (!(episodeGroup instanceof LinearLayout))
                throw new Exception("episodeGroup error: not instanceof LinearLayout");
            int episodeChildCount = episodeGroup.getChildCount();
            if (episodeChildCount <= 0)
                throw new Exception("episodeChildCount error: " + episodeChildCount);
            int episodeNum = initEpisodeNum();
            if (episodeNum < 0)
                throw new Exception("episodeNum error: " + episodeNum);
            int indexOfChild = episodeGroup.indexOfChild(focus);
            if (indexOfChild < 0)
                throw new Exception("indexOfChild error: " + indexOfChild);
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => dispatchEventEpisode => direction = " + direction);
            T t = (T) focus.getTag(R.id.lb_presenter_episode);
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => dispatchEventEpisode => t = " + t);
            if (null == t)
                throw new Exception("t error: null");
            int rangeLength = getRangeLength();
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => dispatchEventEpisode => rangeLength = " + rangeLength);
            if (rangeLength < 0)
                throw new Exception("rangeLength warning: " + rangeLength);
            int episodeLength = getEpisodeLength();
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => dispatchEventEpisode => episodeLength = " + episodeLength);
            if (episodeLength < 0)
                throw new Exception("episodeLength warning: " + episodeLength);
            int episodeIndex = t.getEpisodeIndex();
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => dispatchEventEpisode => episodeIndex = " + episodeIndex);
            if (episodeIndex < 0)
                throw new Exception("episodeIndex warning: " + episodeIndex);
            // left-move
            if (direction == View.FOCUS_LEFT && indexOfChild > 0) {
                // cur
                cleanFocusCheckedEpisode(viewGroup, indexOfChild);
                // next
                int nextIndexOfChild = indexOfChild - 1;
                requestFocusCheckedEpisode(viewGroup, nextIndexOfChild);
            }
            // left-refresh
            else if (direction == View.FOCUS_LEFT && episodeIndex >= episodeNum) {
                // cur1
                cleanFocusCheckedEpisode(viewGroup, indexOfChild);
                // cur2
                int rangeIndex = t.getRangeIndex();
                cleanFocusCheckedRange(viewGroup, rangeIndex);
                // refresh1
                int rangeMax = t.getRangeMax();
                int checkRangeIndex = rangeIndex - 1;
                int startRangeIndex;
                if (rangeMax <= rangeLength) {
                    startRangeIndex = 0;
                } else {
                    startRangeIndex = 0;
                }
                refreshDataRange(viewGroup, startRangeIndex, checkRangeIndex);
                // refresh2
                refreshDataEpisode(viewGroup, checkRangeIndex, episodeNum - 1, false, true);
            }
            // right-move
            else if (direction == View.FOCUS_RIGHT && indexOfChild + 1 < episodeChildCount) {
                // cur
                cleanFocusCheckedEpisode(viewGroup, indexOfChild);
                // next
                int nextIndexOfChild = indexOfChild + 1;
                requestFocusCheckedEpisode(viewGroup, nextIndexOfChild);
            }
            // right-refresh
            else if (direction == View.FOCUS_RIGHT && episodeIndex + 1 < episodeLength) {
                // cur1
                cleanFocusCheckedEpisode(viewGroup, indexOfChild);
                // cur2
                int rangeIndex = t.getRangeIndex();
                cleanFocusCheckedRange(viewGroup, rangeIndex);
                // refresh1
                int checkRangeIndex = rangeIndex + 1;
                int rangeMax = t.getRangeMax();
                int startRangeIndex;
                if (rangeMax <= rangeLength) {
                    startRangeIndex = 0;
                } else {
                    startRangeIndex = 0;
                }
                refreshDataRange(viewGroup, startRangeIndex, checkRangeIndex);
                // refresh2
                refreshDataEpisode(viewGroup, checkRangeIndex, 0, false, true);
            }
            return false;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => dispatchEventEpisode => " + e.getMessage());
            return true;
        }
    }

    private final boolean dispatchEventRange(@NonNull View viewGroup,
                                             @NonNull View focus,
                                             @NonNull int direction) {

        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup rangeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(2);
            if (null == rangeGroup)
                throw new Exception("rangeGroup error: null");
            if (!(rangeGroup instanceof LinearLayout))
                throw new Exception("rangeGroup error: not instanceof LinearLayout");
            int rangeChildCount = rangeGroup.getChildCount();
            if (rangeChildCount <= 0)
                throw new Exception("rangeChildCount error: " + rangeChildCount);
            int indexOfChild = rangeGroup.indexOfChild(focus);
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => dispatchEventRange => indexOfChild = " + indexOfChild);
            if (indexOfChild < 0)
                throw new Exception("indexOfChild error: " + indexOfChild);
            View child = rangeGroup.getChildAt(indexOfChild);
            T t = (T) child.getTag(R.id.lb_presenter_range);
            if (null == t)
                throw new Exception("t error: null");
            int rangeIndex = t.getRangeIndex();
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => dispatchEventRange => rangeIndex = " + rangeIndex);
            if (rangeIndex < 0)
                throw new Exception("rangeIndexOfChild warning: " + rangeIndex);
            // left-move
            if (direction == View.FOCUS_LEFT && indexOfChild > 0) {
                // cur
                cleanFocusCheckedRange(viewGroup, indexOfChild);
                // next1
                int nextIndexOfChild = indexOfChild - 1;
                requestFocusCheckedRange(viewGroup, nextIndexOfChild);
                // refresh
                refreshDataEpisode(viewGroup, nextIndexOfChild, 0, false, false);
            }
            // right-move
            else if (direction == View.FOCUS_RIGHT && indexOfChild + 1 < rangeChildCount) {
                // cur
                cleanFocusCheckedRange(viewGroup, indexOfChild);
                // next1
                int nextIndexOfChild = indexOfChild + 1;
                requestFocusCheckedRange(viewGroup, nextIndexOfChild);
                // refresh
                refreshDataEpisode(viewGroup, nextIndexOfChild, 0, false, false);
            }
            return false;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => dispatchEventRange => " + e.getMessage());
            return true;
        }
    }

    /*********************/

    public final boolean isPlayingEnd(ViewGroup viewGroup) {
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
            LeanBackUtil.log("ListTvEpisodesGridPresenter => isPlayingEnd => " + e.getMessage());
            return false;
        }
    }

    public final int getPlayingPositionNext(ViewGroup viewGroup) {
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
            LeanBackUtil.log("ListTvEpisodesGridPresenter => getPlayingPositionNext => " + e.getMessage());
            return -1;
        }
    }

    public final void startPlayingPosition(@NonNull ViewGroup viewGroup,
                                           @NonNull int checkedEpisodePosition) {
        try {
            if (checkedEpisodePosition < 0)
                throw new Exception("checkedEpisodePosition error: " + checkedEpisodePosition);
            int episodeLength = getEpisodeLength();
            if (checkedEpisodePosition + 1 >= episodeLength)
                throw new Exception("checkedEpisodePosition error: " + checkedEpisodePosition + ", episodeLength = " + episodeLength);
            int episodeNum = initEpisodeNum();
            int checkedEpisodeIndex = checkedEpisodePosition % episodeNum;
            int rangeCheckedIndex = checkedEpisodePosition / episodeNum;
            int rangeStartIndex;
            int rangeNum = initRangeNum();
            int rangeLength = getRangeLength();
            if (rangeLength > rangeNum) {
                if (rangeLength - rangeCheckedIndex < rangeNum) {
                    rangeStartIndex = rangeLength - rangeNum - 1;
                } else {
                    rangeStartIndex = rangeCheckedIndex;
                }
            } else {
                rangeStartIndex = 0;
            }
            ViewGroup rootGroup = viewGroup.findViewById(R.id.module_leanback_lep_root);
            if (null == rootGroup)
                throw new Exception("rootGroup error: null");
            if (!(rootGroup instanceof LinearLayout))
                throw new Exception("rootGroup error: not instanceof LinearLayout");
            findCleanFocusCheckedRange(rootGroup);
            findCleanFocusCheckedEpisode(rootGroup);
            refreshDataRange(viewGroup, rangeStartIndex, rangeCheckedIndex);
            refreshDataEpisode(viewGroup, rangeCheckedIndex, checkedEpisodeIndex, true, false);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => startPlayingPosition => " + e.getMessage());
        }
    }

    public final boolean startPlayingNext(@NonNull ViewGroup viewGroup) {
        try {
            int episodeNextPosition = getPlayingPositionNext(viewGroup);
            if (episodeNextPosition < 0)
                throw new Exception("episodeNextPosition error: " + episodeNextPosition);
            startPlayingPosition(viewGroup, episodeNextPosition);
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => startPlayingNext => " + e.getMessage());
            return false;
        }
    }

    /**************/

//    private final int getPlayingIndexOfRange() {
//        try {
//            int index = -1;
//            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
//                if (null == entry)
//                    continue;
//                TvEpisodesPlusItemBean t = entry.getKey();
//                if (null == t)
//                    continue;
//                index += 1;
//                if (t.isPlaying()) {
//                    return index;
//                }
//            }
//            throw new Exception("index error: " + index);
//        } catch (Exception e) {
//            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getPlayingIndexOfRange => " + e.getMessage());
//            return -1;
//        }
//    }
//
//    private final int getCheckedIndexOfRange() {
//        try {
//            int index = -1;
//            for (Map.Entry<T, List<T>> entry : mData.entrySet()) {
//                if (null == entry)
//                    continue;
//                TvEpisodesPlusItemBean t = entry.getKey();
//                if (null == t)
//                    continue;
//                index += 1;
//                if (t.isChecked()) {
//                    return index;
//                }
//            }
//            throw new Exception("index error: " + index);
//        } catch (Exception e) {
//            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getCheckedIndexOfRange => " + e.getMessage());
//            return -1;
//        }
//    }
//
//    private final int getPlayingIndexOfEpisode(int playingIndexRange) {
//        try {
//            if (playingIndexRange < 0)
//                throw new Exception("playingIndexRange error: " + playingIndexRange);
//            List<T> list = getDataIndexOfEpisode(playingIndexRange);
//            if (null == list)
//                throw new Exception("list error: null");
//            int size = list.size();
//            if (size <= 0) throw new Exception("size error: " + size);
//            for (int i = 0; i < size; i++) {
//                T t = list.get(i);
//                if (null == t)
//                    continue;
//                if (t.isPlaying())
//                    return i;
//            }
//            throw new Exception("not find");
//        } catch (Exception e) {
//            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getPlayingIndexOfEpisode => " + e.getMessage());
//            return -1;
//        }
//    }
//
//    private final int getCheckedIndexOfEpisode(int checkedIndexRange) {
//        try {
//            if (checkedIndexRange < 0)
//                throw new Exception("checkedIndexRange error: " + checkedIndexRange);
//            List<T> list = getDataIndexOfEpisode(checkedIndexRange);
//            if (null == list)
//                throw new Exception("list error: null");
//            int size = list.size();
//            if (size <= 0) throw new Exception("size error: " + size);
//            for (int i = 0; i < size; i++) {
//                T t = list.get(i);
//                if (null == t)
//                    continue;
//                if (t.isPlaying())
//                    return i;
//            }
//            throw new Exception("not find");
//        } catch (Exception e) {
//            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getCheckedIndexOfEpisode => " + e.getMessage());
//            return -1;
//        }
//    }
}
