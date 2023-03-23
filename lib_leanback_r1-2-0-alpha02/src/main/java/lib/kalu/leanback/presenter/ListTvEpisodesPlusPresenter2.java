package lib.kalu.leanback.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;

import lib.kalu.leanback.presenter.bean.TvEpisodesPlusItemBean;
import lib.kalu.leanback.presenter.impl.ListTvEpisodesPlusPresenterImpl;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvEpisodesPlusPresenter2<T extends TvEpisodesPlusItemBean> extends Presenter implements ListTvEpisodesPlusPresenterImpl {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.lb_list_tv_episodes_plus2, parent, false);
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
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }
}
