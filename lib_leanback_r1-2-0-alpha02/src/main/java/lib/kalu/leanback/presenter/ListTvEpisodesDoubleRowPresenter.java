package lib.kalu.leanback.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import lib.kalu.leanback.presenter.bean.TvEpisodesPlusItemBean;
import lib.kalu.leanback.presenter.impl.ListTvEpisodesDoubleLinearLayoutPresenterImpl;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvEpisodesDoubleRowPresenter<T extends TvEpisodesPlusItemBean> extends Presenter implements ListTvEpisodesDoubleLinearLayoutPresenterImpl {

    private final LinkedHashMap<T, LinkedList<T>> mData = new LinkedHashMap<>();

    @Override
    public final Map<T, LinkedList<T>> getData() {
        return mData;
    }

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

    @LayoutRes
    public abstract int initEpisodeLayout();

    @LayoutRes
    public abstract int initRangeLayout();

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
}
