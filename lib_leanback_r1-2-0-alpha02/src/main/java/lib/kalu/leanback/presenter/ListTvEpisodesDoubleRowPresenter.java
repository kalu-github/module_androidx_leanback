package lib.kalu.leanback.presenter;

import android.content.Context;
import android.view.FocusFinder;
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
            viewGroup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        ((ViewGroup) view).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                    } else {
                        ((ViewGroup) view).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    }
                }
            });
            viewGroup.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    // up-in
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        requestFocusCheckedRange(view, true);
                        return true;
                    }
                    // down-in
                    else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        requestFocusCheckedEpisode(view, true);
                        return true;
                    }
                    return false;
                }
            });
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

    private final void updateEpisode(@NonNull View viewGroup,
                                     @NonNull int rangeIndex,
                                     @NonNull int checkedIndex,
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
            int length = list.size();
            if (length <= 0)
                throw new Exception("length error: " + length);
            if (checkedIndex < 0) {
                for (T t : list) {
                    if (null == t)
                        continue;
                    if (t.isPlaying()) {
                        checkedIndex = list.indexOf(t);
                    }
                }
                if (checkedIndex < 0) {
                    checkedIndex = 0;
                }
            }
            for (int i = 0; i < episodeChildCount; i++) {
                View child = episodeGroup.getChildAt(i);
                if (null == child)
                    continue;
                child.setTag(R.id.lb_presenter_episode, null);
                child.setVisibility(i >= length ? View.INVISIBLE : View.VISIBLE);
                if (i >= length)
                    continue;
                T t = list.get(i);
                if (null == t)
                    continue;
                t.setFocus(isFromUser && i == checkedIndex);
                t.setChecked(i == checkedIndex);
                child.setVisibility(View.VISIBLE);
                child.setTag(R.id.lb_presenter_episode, t);
                onBindHolderEpisode(child.getContext(), child, t, i);
                if (isFromUser && i == checkedIndex) {
                    onClickEpisode(child.getContext(), child, t, i, false);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => updateEpisode => " + e.getMessage());
        }
    }

    private final void updateRange(@NonNull View viewGroup,
                                   @NonNull int startIndex,
                                   @NonNull int checkedIndex,
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
                if (direction == View.FOCUS_RIGHT) {
                    t.setFocus(i + 1 == rangeChildCount);
                    t.setChecked(i + 1 == rangeChildCount);
                } else if (direction == View.FOCUS_LEFT) {
                    t.setFocus(i == 0);
                    t.setChecked(i == 0);
                } else if (direction == -2) {
                    t.setChecked(rangeIndex == checkedIndex);
                }
                child.setTag(R.id.lb_presenter_range, t);
                onBindHolderRange(child.getContext(), child, t, i);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => updateRange => " + e.getMessage(), e);
        }
    }

    private final void swapRange(@NonNull View viewGroup,
                                 @NonNull int oldCheckedIndex,
                                 @NonNull int newCheckedIndex) {
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
                if (null == child || child.getVisibility() != View.VISIBLE)
                    continue;
                T t = (T) child.getTag(R.id.lb_presenter_range);
                if (null == t)
                    continue;
                if (t.getRangeIndex() == oldCheckedIndex) {
                    t.setChecked(false);
                    onBindHolderRange(child.getContext(), child, t, oldCheckedIndex);
                } else if (t.getRangeIndex() == newCheckedIndex) {
                    t.setChecked(true);
                    onBindHolderRange(child.getContext(), child, t, oldCheckedIndex);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => updateRange => " + e.getMessage(), e);
        }
    }

    private final void swapEpisode(@NonNull View viewGroup,
                                   @NonNull int oldCheckedIndex,
                                   @NonNull int newCheckedIndex) {
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
                if (null == child || child.getVisibility() != View.VISIBLE)
                    continue;
                T t = (T) child.getTag(R.id.lb_presenter_episode);
                if (null == t)
                    continue;
                if (t.getEpisodeIndex() == oldCheckedIndex) {
                    t.setChecked(false);
                    onBindHolderEpisode(child.getContext(), child, t, oldCheckedIndex);
                } else if (t.getEpisodeIndex() == newCheckedIndex) {
                    t.setChecked(true);
                    onBindHolderEpisode(child.getContext(), child, t, oldCheckedIndex);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => swapEpisode => " + e.getMessage(), e);
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

    private final void setPlayingEpisode(@NonNull View viewGroup,
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
            t.setPlaying(true);
            t.setChecked(true);
            child.setTag(R.id.lb_presenter_episode_playing, t);
            onBindHolderEpisode(child.getContext(), child, t, indexOfChild);
            onClickEpisode(child.getContext(), child, t, indexOfChild, true);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => setPlayingEpisode => " + e.getMessage(), e);
        }
    }

    private final void setPlayingRange(@NonNull View viewGroup,
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
            int rangeLength = getRangeLength();
            if (rangeLength < 0)
                throw new Exception("rangeLength error: " + rangeLength);
            if (checkedIndex >= rangeLength)
                throw new Exception("checkedIndex error: " + checkedIndex + ", rangeLength = " + rangeLength);
            int rangeChildCount = rangeGroup.getChildCount();
            if (rangeChildCount <= 0)
                throw new Exception("rangeChildCount error: " + rangeChildCount);
            for (int i = 0; i < rangeChildCount; i++) {
                View child = rangeGroup.getChildAt(i);
                if (null == child || child.getVisibility() != View.VISIBLE)
                    throw new Exception("child error: null");
                T t = (T) child.getTag(R.id.lb_presenter_range);
                if (null == t)
                    throw new Exception("t error: null");
                if (t.getRangeIndex() == checkedIndex) {
                    t.setPlaying(true);
                    t.setChecked(true);
                    onBindHolderRange(child.getContext(), child, t, i);
                    return;
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => setPlayingRange => " + e.getMessage());
        }
    }

    private final void requestFocusCheckedRange(@NonNull View viewGroup,
                                                @NonNull boolean isDescendant) {
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
            int checkedIndexRange = getRangeCheckedIndexOfChild(viewGroup);
            if (checkedIndexRange < 0)
                throw new Exception("checkedIndexRange error: " + checkedIndexRange);
            View child = rangeGroup.getChildAt(checkedIndexRange);
            if (null == child)
                throw new Exception("child error: null");
            child.requestFocus();
            if (!isDescendant)
                throw new Exception("isDescendant warning: false");
            viewGroup.setFocusable(false);
            ((ViewGroup) viewGroup).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => requestFocusCheckedRange => " + e.getMessage());
        }
    }

    private final void requestFocusCheckedEpisode(@NonNull View viewGroup,
                                                  @NonNull boolean isDescendant) {
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
            int indexChildOf;
            int checkedIndexEpisode = getEpisodeCheckedIndexOfChild(viewGroup);
            if (checkedIndexEpisode >= 0) {
                indexChildOf = checkedIndexEpisode;
            } else {
                int playingIndexEpisode = getEpisodePlayingIndexOfChild(viewGroup);
                if (playingIndexEpisode >= 0) {
                    indexChildOf = playingIndexEpisode;
                } else {
                    indexChildOf = 0;
                }
            }
            View child = episodeGroup.getChildAt(indexChildOf);
            if (null == child)
                throw new Exception("child error: null");
            child.requestFocus();
            if (!isDescendant)
                throw new Exception("isDescendant warning: false");
            viewGroup.setFocusable(false);
            ((ViewGroup) viewGroup).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => requestFocusCheckedEpisode => " + e.getMessage());
        }
    }

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
                LayoutInflater.from(context).inflate(initRangeLayout(), rangeGroup, true);
                View child = rangeGroup.getChildAt(i);
                if (null != child.getLayoutParams()) {
                    ((LinearLayout.LayoutParams) child.getLayoutParams()).weight = 1;
                    ((LinearLayout.LayoutParams) child.getLayoutParams()).bottomMargin = 0;
                    ((LinearLayout.LayoutParams) child.getLayoutParams()).topMargin = 0;
                    if (i == 0) {
                        ((LinearLayout.LayoutParams) child.getLayoutParams()).leftMargin = 0;
                        ((LinearLayout.LayoutParams) child.getLayoutParams()).rightMargin = rangeMargin;
                    } else if (i + 1 >= rangeNum) {
                        ((LinearLayout.LayoutParams) child.getLayoutParams()).leftMargin = rangeMargin;
                        ((LinearLayout.LayoutParams) child.getLayoutParams()).rightMargin = 0;
                    } else {
                        ((LinearLayout.LayoutParams) child.getLayoutParams()).leftMargin = rangeMargin;
                        ((LinearLayout.LayoutParams) child.getLayoutParams()).rightMargin = rangeMargin;
                    }
                }
                child.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (hasFocus) {
                            T t = (T) view.getTag(R.id.lb_presenter_range);
                            if (null != t) {
                                t.setFocus(true);
                                int indexOfChild = rangeGroup.indexOfChild(view);
                                onBindHolderRange(view.getContext(), view, t, indexOfChild);
                            }
                        } else {
                            T t = (T) view.getTag(R.id.lb_presenter_range);
                            if (null != t) {
                                t.setFocus(false);
                                int indexOfChild = rangeGroup.indexOfChild(view);
                                onBindHolderRange(view.getContext(), view, t, indexOfChild);
                            }
                        }
                    }
                });
                child.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                        // left
                        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            dispatchEventRange(view, View.FOCUS_LEFT);
                        }
                        // right
                        else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            dispatchEventRange(view, View.FOCUS_RIGHT);
                        }
                        // up
                        else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            requestFocusCheckedEpisode(viewGroup, false);
                            return true;
                        }
                        // down
                        else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            requestFocus(view, View.FOCUS_DOWN);
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

    private final ViewGroup findDecorView(View view) {
        try {
            View parent = (View) view.getParent();
            if (null == parent) {
                return (ViewGroup) view;
            } else {
                return findDecorView(parent);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => findDecorView => " + e.getMessage());
            return (ViewGroup) view;
        }
    }

    private final void requestFocus(@NonNull View focusView,
                                    @NonNull int direction) {

        try {
            ViewGroup decodeView = findDecorView(focusView);
            if (null == decodeView)
                throw new Exception("decodeView error: null");
            View nextFocus = FocusFinder.getInstance().findNextFocus(decodeView, focusView, direction);
            if (null == nextFocus)
                throw new Exception("nextFocus error: null");
            ViewGroup tvGroup = (ViewGroup) focusView.getParent().getParent();
            if (null == tvGroup)
                throw new Exception("tvGroup error: null");
            tvGroup.setFocusable(true);
            tvGroup.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            nextFocus.requestFocus();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => requestFocus => " + e.getMessage());
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
            int episodeNum = initEpisodeNum();
            int episodePadding = initEpisodePadding(context);
            int episodeMargin = episodePadding * (episodeNum - 1) / episodeNum;
            ((LinearLayout) episodeGroup).setWeightSum(episodeNum);
            // 3
            for (int i = 0; i < episodeNum; i++) {
                LayoutInflater.from(context).inflate(initEpisodeLayout(), episodeGroup, true);
                View child = episodeGroup.getChildAt(i);
                if (null != child.getLayoutParams()) {
                    ((LinearLayout.LayoutParams) child.getLayoutParams()).weight = 1;
                    ((LinearLayout.LayoutParams) child.getLayoutParams()).bottomMargin = 0;
                    ((LinearLayout.LayoutParams) child.getLayoutParams()).topMargin = 0;
                    if (i == 0) {
                        ((LinearLayout.LayoutParams) child.getLayoutParams()).leftMargin = 0;
                        ((LinearLayout.LayoutParams) child.getLayoutParams()).rightMargin = episodeMargin;
                    } else if (i + 1 >= episodeNum) {
                        ((LinearLayout.LayoutParams) child.getLayoutParams()).leftMargin = episodeMargin;
                        ((LinearLayout.LayoutParams) child.getLayoutParams()).rightMargin = 0;
                    } else {
                        ((LinearLayout.LayoutParams) child.getLayoutParams()).leftMargin = episodeMargin;
                        ((LinearLayout.LayoutParams) child.getLayoutParams()).rightMargin = episodeMargin;
                    }
                }
                child.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (hasFocus) {
                            T t = (T) view.getTag(R.id.lb_presenter_episode);
                            if (null != t) {
                                int indexOfChild = episodeGroup.indexOfChild(child);
                                t.setFocus(true);
                                onBindHolderEpisode(view.getContext(), view, t, indexOfChild);
                            }
                        } else {
                            T t = (T) view.getTag(R.id.lb_presenter_episode);
                            if (null != t) {
                                int indexOfChild = episodeGroup.indexOfChild(child);
                                t.setFocus(false);
                                onBindHolderEpisode(view.getContext(), view, t, indexOfChild);
                            }
                        }
                    }
                });
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            T t1 = (T) view.getTag(R.id.lb_presenter_episode);
                            if (null == t1)
                                throw new Exception("t1 error: null");
                            if (t1.isPlaying())
                                throw new Exception("t1 warning: playing");
                            for (int m = 0; m < episodeNum; m++) {
                                View child = episodeGroup.getChildAt(m);
                                if (null == child)
                                    continue;
                                T t2 = (T) child.getTag(R.id.lb_presenter_episode_playing);
                                if (null == t2)
                                    continue;
                                if (t2.isPlaying()) {
                                    t2.setPlaying(false);
                                    child.setTag(R.id.lb_presenter_episode_playing, null);
                                    if (t1.getRangeIndex() == t2.getRangeIndex()) {
                                        onBindHolderEpisode(child.getContext(), child, t2, m);
                                    }
                                    break;
                                }
                            }
                        } catch (Exception e) {
                        }
                        try {
                            T t = (T) view.getTag(R.id.lb_presenter_episode);
                            if (null != t && !t.isPlaying()) {
                                int indexOfChild = episodeGroup.indexOfChild(child);
                                t.setPlaying(true);
                                view.setTag(R.id.lb_presenter_episode_playing, t);
                                onBindHolderEpisode(view.getContext(), view, t, indexOfChild);
                                onClickEpisode(view.getContext(), view, t, indexOfChild, true);
                            }
                        } catch (Exception e) {
                        }
                    }
                });
                child.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                        // left
                        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            dispatchEventEpisode(view, View.FOCUS_LEFT);
                        }
                        // right
                        else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            dispatchEventEpisode(view, View.FOCUS_RIGHT);
                        }
                        // up
                        else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            requestFocus(view, View.FOCUS_UP);
                            return true;
                        }
                        // down
                        else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            requestFocusCheckedRange(viewGroup, false);
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

    private final int getEpisodeCheckedIndexOfChild(@NonNull View viewGroup) {
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
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getEpisodeCheckedIndexOfChild => " + e.getMessage());
            return -1;
        }
    }

    private final int getEpisodePlayingIndexOfChild(@NonNull View viewGroup) {
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
                if (t.isPlaying()) {
                    return i;
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getEpisodePlayingIndexOfChild => " + e.getMessage());
            return -1;
        }
    }

    private final int getRangeCheckedIndexOfChild(@NonNull View viewGroup) {
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
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getRangeCheckedIndexOfChild => " + e.getMessage());
            return -1;
        }
    }

    private final boolean dispatchEventEpisode(@NonNull View focusView,
                                               @NonNull int direction) {
        try {
            if (null == focusView)
                throw new Exception("focusView error: null");
            ViewGroup episodeGroup = (ViewGroup) focusView.getParent();
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
            int indexOfChild = episodeGroup.indexOfChild(focusView);
            if (indexOfChild < 0)
                throw new Exception("indexOfChild error: " + indexOfChild);
            View child = episodeGroup.getChildAt(indexOfChild);
            if (null == child)
                throw new Exception("child error: null");
            T t = (T) child.getTag(R.id.lb_presenter_episode);
            if (null == t)
                throw new Exception("t error: null");
            int rangeLength = getRangeLength();
            if (rangeLength < 0)
                throw new Exception("rangeLength warning: " + rangeLength);
            int episodeLength = getEpisodeLength();
            if (episodeLength < 0)
                throw new Exception("episodeLength warning: " + episodeLength);
            int episodeIndex = t.getEpisodeIndex();
            if (episodeIndex < 0)
                throw new Exception("episodeIndex warning: " + episodeIndex);
            // left-move
            if (direction == View.FOCUS_LEFT && indexOfChild > 0) {
                swapEpisode((View) episodeGroup.getParent(), episodeIndex, episodeIndex - 1);
            }
            // left-update
            else if (direction == View.FOCUS_LEFT && indexOfChild <= 0 && episodeIndex >= episodeNum) {
                T curFirstRangeT = getCurFirstRangeT((View) episodeGroup.getParent());
                if (null == curFirstRangeT)
                    throw new Exception("curFirstRangeT error: null");
                int curFirstRangeIndex = curFirstRangeT.getRangeIndex();
                int rangeIndex = t.getRangeIndex();
                int nextCheckRangeIndex = rangeIndex - 1;
                // 不够
                if (nextCheckRangeIndex >= curFirstRangeIndex) {
                    swapRange((View) episodeGroup.getParent(), rangeIndex, nextCheckRangeIndex);
                    updateEpisode((View) episodeGroup.getParent(), nextCheckRangeIndex, episodeNum - 1, true);
                }
                // 够了
                else {
                    updateRange((View) episodeGroup.getParent(), nextCheckRangeIndex, nextCheckRangeIndex, -2);
                    updateEpisode((View) episodeGroup.getParent(), nextCheckRangeIndex, episodeNum - 1, true);
                }
                // nextFocus
                episodeGroup.post(new Runnable() {
                    @Override
                    public void run() {
                        View nextFocusView = episodeGroup.getChildAt(episodeNum - 1);
                        nextFocusView.requestFocus();
                    }
                });
            }
            // right-move
            else if (direction == View.FOCUS_RIGHT && indexOfChild + 1 < episodeChildCount) {
                swapEpisode((View) episodeGroup.getParent(), episodeIndex, episodeIndex + 1);
            }
            // right-update
            else if (direction == View.FOCUS_RIGHT && indexOfChild + 1 >= episodeChildCount && episodeIndex + 1 < episodeLength) {
                T curLastRangeT = getCurLastRangeT((View) episodeGroup.getParent());
                if (null == curLastRangeT)
                    throw new Exception("curLastRangeT error: null");
                int curLastRangeIndex = curLastRangeT.getRangeIndex();
                int rangeIndex = t.getRangeIndex();
                int nextCheckRangeIndex = rangeIndex + 1;
                // 不够
                if (nextCheckRangeIndex <= curLastRangeIndex) {
                    swapRange((View) episodeGroup.getParent(), rangeIndex, nextCheckRangeIndex);
                    updateEpisode((View) episodeGroup.getParent(), nextCheckRangeIndex, 0, true);
                }
                // 够了
                else {
                    int startRangeIndex = nextCheckRangeIndex - initRangeNum() + 1;
                    updateRange((View) episodeGroup.getParent(), startRangeIndex, nextCheckRangeIndex, -2);
                    updateEpisode((View) episodeGroup.getParent(), nextCheckRangeIndex, 0, true);
                }
                // nextFocus
                episodeGroup.post(new Runnable() {
                    @Override
                    public void run() {
                        View nextFocusView = episodeGroup.getChildAt(0);
                        nextFocusView.requestFocus();
                    }
                });
            }
            return false;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => dispatchEventEpisode => " + e.getMessage());
            return true;
        }
    }

    private T getCurLastRangeT(View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup rangeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(2);
            int rangeChildCount = rangeGroup.getChildCount();
            for (int i = rangeChildCount - 1; i >= 0; i--) {
                View child = rangeGroup.getChildAt(i);
                if (null == child)
                    continue;
                if (child.getVisibility() != View.VISIBLE)
                    continue;
                T t = (T) child.getTag(R.id.lb_presenter_range);
                if (null == t)
                    continue;
                return t;
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getCurLastRangeT => " + e.getMessage());
            return null;
        }
    }

    private T getCurFirstRangeT(View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            int childCount = ((ViewGroup) viewGroup).getChildCount();
            if (childCount != 3)
                throw new Exception("childCount error: " + childCount);
            ViewGroup rangeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(2);
            int rangeChildCount = rangeGroup.getChildCount();
            for (int i = 0 - 1; i < rangeChildCount; i++) {
                View child = rangeGroup.getChildAt(i);
                if (null == child)
                    continue;
                if (child.getVisibility() != View.VISIBLE)
                    continue;
                T t = (T) child.getTag(R.id.lb_presenter_range);
                if (null == t)
                    continue;
                return t;
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => getCurFirstRangeT => " + e.getMessage());
            return null;
        }
    }

    private final boolean dispatchEventRange(@NonNull View focusView,
                                             @NonNull int direction) {

        try {
            if (null == focusView)
                throw new Exception("focusView error: null");
            ViewGroup rangeGroup = (ViewGroup) focusView.getParent();
            if (null == rangeGroup)
                throw new Exception("rangeGroup error: null");
            if (!(rangeGroup instanceof LinearLayout))
                throw new Exception("rangeGroup error: not instanceof LinearLayout");
            int rangeChildCount = rangeGroup.getChildCount();
            if (rangeChildCount <= 0)
                throw new Exception("rangeChildCount error: " + rangeChildCount);
            int indexOfChild = rangeGroup.indexOfChild(focusView);
            if (indexOfChild < 0)
                throw new Exception("indexOfChild error: " + indexOfChild);
            View child = rangeGroup.getChildAt(indexOfChild);
            if (null == child)
                throw new Exception("child error: null");
            T t = (T) child.getTag(R.id.lb_presenter_range);
            if (null == t)
                throw new Exception("t error: null");
            int rangeIndex = t.getRangeIndex();
            if (rangeIndex < 0)
                throw new Exception("rangeIndex warning: " + rangeIndex);
            int rangeLength = getRangeLength();
            if (rangeLength < 0)
                throw new Exception("rangeLength warning: " + rangeLength);
            // left-update
            if (direction == View.FOCUS_LEFT && rangeIndex > 0 && indexOfChild <= 0) {
                int startRangeIndex = rangeIndex - 1;
                updateRange((View) rangeGroup.getParent(), startRangeIndex, -1, direction);
                updateEpisode((View) rangeGroup.getParent(), startRangeIndex, 0, false);
            }
            // left-move
            else if (direction == View.FOCUS_LEFT && rangeIndex > 0) {
                int nextCheckedRangeIndex = rangeIndex - 1;
                swapRange((View) rangeGroup.getParent(), rangeIndex, nextCheckedRangeIndex);
                updateEpisode((View) rangeGroup.getParent(), nextCheckedRangeIndex, -1, false);
            }
            // right-update
            else if (direction == View.FOCUS_RIGHT && rangeIndex + 1 < rangeLength && indexOfChild + 1 >= rangeChildCount) {
                int checkedRangeIndex = rangeIndex + 1;
                int startRangeIndex = checkedRangeIndex - initRangeNum() + 1;
                updateRange((View) rangeGroup.getParent(), startRangeIndex, -1, direction);
                updateEpisode((View) rangeGroup.getParent(), checkedRangeIndex, 0, false);
            }
            // right-move
            else if (direction == View.FOCUS_RIGHT && rangeIndex + 1 < rangeLength) {
                int nextCheckedRangeIndex = rangeIndex + 1;
                swapRange((View) rangeGroup.getParent(), rangeIndex, nextCheckedRangeIndex);
                updateEpisode((View) rangeGroup.getParent(), nextCheckedRangeIndex, -1, false);
            }
            return false;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => dispatchEventRange => " + e.getMessage());
            return true;
        }
    }

    /*********************/

    public final boolean isPlayingPositionLast(View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            if (viewGroup.getId() != R.id.module_leanback_lep_root)
                throw new Exception("viewGroup.getId error: not R.id.module_leanback_lep_root");
            if (!(viewGroup instanceof LinearLayout))
                throw new Exception("viewGroup error: not instanceof LinearLayout");
            int indexOfChild = getEpisodePlayingIndexOfChild(viewGroup);
            if (indexOfChild < 0)
                throw new Exception("indexOfChild error: " + indexOfChild);
            ViewGroup episodeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(1);
            if (null == episodeGroup)
                throw new Exception("episodeGroup error: null");
            View child = episodeGroup.getChildAt(indexOfChild);
            if (null == child)
                throw new Exception("child error: null");
            T t = (T) child.getTag(R.id.lb_presenter_episode_playing);
            if (null == t)
                throw new Exception("t error: null");
            int episodeIndex = t.getEpisodeIndex();
            if (episodeIndex < 0)
                throw new Exception("episodeIndex error: " + episodeIndex);
            int episodeMax = t.getEpisodeMax();
            if (episodeMax < 0)
                throw new Exception("episodeMax error: " + episodeMax);
            return episodeIndex + 1 >= episodeMax;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => isPlayingPositionLast => " + e.getMessage());
            return true;
        }
    }

    public final int getPlayingPosition(View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            if (viewGroup.getId() != R.id.module_leanback_lep_root)
                throw new Exception("viewGroup.getId error: not R.id.module_leanback_lep_root");
            if (!(viewGroup instanceof LinearLayout))
                throw new Exception("viewGroup error: not instanceof LinearLayout");
            int indexOfChild = getEpisodePlayingIndexOfChild(viewGroup);
            if (indexOfChild < 0)
                throw new Exception("indexOfChild error: " + indexOfChild);
            ViewGroup episodeGroup = (ViewGroup) ((ViewGroup) viewGroup).getChildAt(1);
            if (null == episodeGroup)
                throw new Exception("episodeGroup error: null");
            View child = episodeGroup.getChildAt(indexOfChild);
            if (null == child)
                throw new Exception("child error: null");
            T t = (T) child.getTag(R.id.lb_presenter_episode_playing);
            if (null == t)
                throw new Exception("t error: null");
            int episodeIndex = t.getEpisodeIndex();
            if (episodeIndex < 0)
                throw new Exception("episodeIndex error: " + episodeIndex);
            return episodeIndex;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => getPlayingPosition => " + e.getMessage());
            return -1;
        }
    }

    public final int getPlayingPositionNext(View viewGroup) {
        try {
            boolean isPlayingPositionLast = isPlayingPositionLast(viewGroup);
            if (isPlayingPositionLast)
                throw new Exception("isPlayingPositionLast warning: true");
            int playingPosition = getPlayingPosition(viewGroup);
            if (playingPosition < 0)
                throw new Exception("playingPosition error: " + playingPosition);
            return playingPosition + 1;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => getPlayingPositionNext => " + e.getMessage());
            return -1;
        }
    }

    public final void checkedPlayingPosition(@NonNull View viewGroup,
                                             @NonNull int checkedEpisodePosition) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            if (viewGroup.getId() != R.id.module_leanback_lep_root)
                throw new Exception("viewGroup.getId error: not R.id.module_leanback_lep_root");
            if (checkedEpisodePosition < 0)
                throw new Exception("checkedEpisodePosition error: " + checkedEpisodePosition);
            int episodeLength = getEpisodeLength();
            if (checkedEpisodePosition + 1 >= episodeLength)
                throw new Exception("checkedEpisodePosition error: " + checkedEpisodePosition + ", episodeLength = " + episodeLength);
            int episodeNum = initEpisodeNum();
            int checkedEpisodeIndex = checkedEpisodePosition % episodeNum;
            int checkedRangeIndex = checkedEpisodePosition / episodeNum;
            int startRangeIndex;
            int rangeNum = initRangeNum();
            if (checkedRangeIndex + 1 <= rangeNum) {
                startRangeIndex = 0;
            } else {
                int rangeLength = getRangeLength();
                T t = getDataIndexOfRange(rangeLength - 1);
                int rangeMax = t.getRangeMax();
                if (rangeMax - checkedRangeIndex < rangeNum) {
                    startRangeIndex = rangeMax - rangeNum;
                } else {
                    startRangeIndex = checkedRangeIndex;
                }
            }
            ViewGroup rootGroup = viewGroup.findViewById(R.id.module_leanback_lep_root);
            if (null == rootGroup)
                throw new Exception("rootGroup error: null");
            if (!(rootGroup instanceof LinearLayout))
                throw new Exception("rootGroup error: not instanceof LinearLayout");
            findCleanFocusCheckedRange(rootGroup);
            findCleanFocusCheckedEpisode(rootGroup);
            updateRange(viewGroup, startRangeIndex, checkedRangeIndex, -1);
            updateEpisode(viewGroup, checkedRangeIndex, checkedEpisodeIndex, false);
            setPlayingRange(viewGroup, checkedRangeIndex);
            setPlayingEpisode(viewGroup, checkedEpisodeIndex);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => checkedPlayingPosition => " + e.getMessage());
        }
    }

    public final boolean checkedPlayingPositionNext(@NonNull View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            if (viewGroup.getId() != R.id.module_leanback_lep_root)
                throw new Exception("viewGroup.getId error: not R.id.module_leanback_lep_root");
            int episodeNextPosition = getPlayingPositionNext(viewGroup);
            if (episodeNextPosition < 0)
                throw new Exception("episodeNextPosition error: " + episodeNextPosition);
            checkedPlayingPosition(viewGroup, episodeNextPosition);
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesDoubleRowPresenter => checkedPlayingPositionNext => " + e.getMessage());
            return false;
        }
    }

    /**************/
}
