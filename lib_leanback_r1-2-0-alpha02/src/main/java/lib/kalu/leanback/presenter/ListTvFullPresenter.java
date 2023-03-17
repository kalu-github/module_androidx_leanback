package lib.kalu.leanback.presenter;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import lib.kalu.leanback.presenter.bean.TvPresenterRowBean;
import lib.kalu.leanback.presenter.impl.ListTvPresenterImpl;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvFullPresenter<T extends TvPresenterRowBean> extends Presenter implements ListTvPresenterImpl {

    private final LinkedList<T> mData = new LinkedList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            onLife(context);
            ViewGroup inflate = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.lb_list_tv_full, parent, false);
            setPadding(context, inflate);
            setBackgroundColor(context, inflate);
            setContentBackgroundColor(context, inflate, R.id.module_leanback_llf_list);
            setTitlePadding(context, inflate, R.id.module_leanback_llf_title);
            setTitleTextColor(context, inflate, R.id.module_leanback_llf_title);
            setTitleTextSize(context, inflate, R.id.module_leanback_llf_title);
            setTitleAssetTTF(context, inflate, R.id.module_leanback_llf_title);
            setTitleBackgroundColor(context, inflate, R.id.module_leanback_llf_title);
            initAdapter(context, inflate);
            return new ViewHolder(inflate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

        // data
        formatData(item);

        // header
        updateTitle(viewHolder.view);

        // list
        updateAdapter(viewHolder.view);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private final void updateAdapter(View view) {
        try {
            RecyclerView recyclerView = view.findViewById(R.id.module_leanback_llf_list);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvFullPresenter => updateAdapter => " + e.getMessage(), e);
        }
    }

    private final void formatData(Object item) {
        try {
            int size = mData.size();
            if (size > 0)
                throw new Exception("not empty");
            mData.clear();
            mData.addAll((Collection<? extends T>) item);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvFullPresenter => formatData => " + e.getMessage(), e);
        }
    }

    private final void updateTitle(View view) {

        String rowTitle;
        try {
            rowTitle = initRowTitle(view.getContext());
        } catch (Exception e) {
            rowTitle = null;
        }
        try {
            if (null == rowTitle || rowTitle.length() <= 0) {
                T t = mData.get(0);
                rowTitle = t.getRowTitle();
            }
        } catch (Exception e) {
        }

        try {
            TextView textView = view.findViewById(R.id.module_leanback_llf_title);
            textView.setText(rowTitle);
            textView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => updateTitle => " + e.getMessage(), e);
        }
    }

    private final void initAdapter(@NonNull Context context, @NonNull View inflate) {
        try {

            // 1
            RecyclerView recyclerView = inflate.findViewById(R.id.module_leanback_llf_list);

            int initMarginTop = initMarginTop(context);
            if (initMarginTop > 0) {
                if (null != recyclerView.getLayoutParams()) {
                    ((RelativeLayout.LayoutParams) recyclerView.getLayoutParams()).topMargin = initMarginTop;
                }
            }

            int initItemHeight = initItemHeight(context);
            if (initItemHeight > 0) {
                if (null != recyclerView.getLayoutParams()) {
                    ((RelativeLayout.LayoutParams) recyclerView.getLayoutParams()).height = initItemHeight;
                }
            }

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (null == layoutManager) {
                LinearLayoutManager manager = new LinearLayoutManager(context) {
                    @Override
                    public boolean canScrollHorizontally() {
                        int size = mData.size();
                        return ListTvFullPresenter.this.canScrollHorizontally(size);
                    }

                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        initItemOffsets(outRect, view, parent, state);
                    }
                });
                recyclerView.setLayoutManager(manager);
            }

            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (null != adapter) {
                adapter.notifyDataSetChanged();
            } else {
                recyclerView.setAdapter(new RecyclerView.Adapter() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        try {
                            Context context = parent.getContext();
                            onLife(context);
                            View view = LayoutInflater.from(context).inflate(initLayout(viewType), parent, false);
                            RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {
                            };
                            onCreateHolder(context, holder, view, mData);
                            return holder;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        try {
                            T t = mData.get(position);
                            int itemViewType = holder.getItemViewType();
                            onBindHolder(holder.itemView, t, position, itemViewType);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public int getItemViewType(int position) {
                        T t = mData.get(position);
                        return initItemViewType(position, t);
                    }

                    @Override
                    public int getItemCount() {
                        return mData.size();
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => initAdapter => " + e.getMessage(), e);
        }
    }

    protected void onLife(@NonNull Context context) {
        //            if (context instanceof AppCompatActivity) {
//                AppCompatActivity activity = (AppCompatActivity) context;
//                activity.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
//                    switch (event) {
//                        case ON_RESUME:
//                            break;
//                        case ON_PAUSE:
//                            break;
//                        case ON_STOP:
//                            break;
//                    }
//                });
//            }
    }

    protected int initMarginTop(Context context) {
        return 0;
    }

    protected int initItemHeight(Context context) {
        return 0;
    }

    protected abstract void onCreateHolder(@NonNull Context context, @NonNull RecyclerView.ViewHolder holder, @NonNull View view, @NonNull List<T> datas);

    protected abstract void onBindHolder(@NonNull View view, @NonNull T item, @NonNull int position, @NonNull int viewType);

    @LayoutRes
    protected abstract int initLayout(int viewType);

    protected int initItemViewType(int position, T t) {
        return 1;
    }

    protected boolean canScrollHorizontally(int count) {
        return true;
    }
}
