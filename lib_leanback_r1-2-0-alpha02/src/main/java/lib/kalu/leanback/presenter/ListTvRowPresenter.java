package lib.kalu.leanback.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lib.kalu.leanback.presenter.bean.TvPresenterRowBean;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvRowPresenter<T extends TvPresenterRowBean> extends Presenter implements ListTvPresenterImpl {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            onLife(context);
            View view = LayoutInflater.from(context).inflate(R.layout.lb_list_tv_row, parent, false);
            initTitleStyle(context, view, R.id.module_leanback_llr_header);
            return new ViewHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

        // data
        List<T> list = new ArrayList<>();
        try {
            List<T> t = (List<T>) item;
            int size = t.size();
            list.clear();
            for (int i = 0; i < size; i++) {
                T o = t.get(i);
                if (null == o)
                    continue;
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // header
        setRowTitle(viewHolder.view, list);

        // list
        try {
            RecyclerView recyclerView = viewHolder.view.findViewById(R.id.module_leanback_llr_list);
            Context context = recyclerView.getContext();
            setAdapter(context, recyclerView, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final void setRowTitle(View view, List<T> data) {

        String rowTitle;
        try {
            rowTitle = data.get(0).getRowTitle();
        } catch (Exception e) {
            rowTitle = null;
        }
        try {
            if (null == rowTitle || rowTitle.length() <= 0) {
                rowTitle = initRowTitle(view.getContext());
            }
        } catch (Exception e) {
        }

        try {
            TextView textView = view.findViewById(R.id.module_leanback_llr_header);
            textView.setText(rowTitle);
            textView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => setHead => " + e.getMessage(), e);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private final void setAdapter(@NonNull Context context, @NonNull RecyclerView recyclerView, @NonNull List<T> list) {
        try {

            // 1
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (null == layoutManager) {
                LinearLayoutManager manager = new LinearLayoutManager(context) {
                    @Override
                    public boolean canScrollHorizontally() {
                        int size = list.size();
                        return ListTvRowPresenter.this.canScrollHorizontally(size);
                    }

                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                RecyclerView.ItemDecoration itemDecoration = initItemDecoration();
                if (null != itemDecoration) {
                    recyclerView.addItemDecoration(itemDecoration);
                }
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
                            onCreateHolder(context, holder, view, list);
                            return holder;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        try {
                            T t = list.get(position);
                            int itemViewType = holder.getItemViewType();
                            onBindHolder(holder.itemView, t, position, itemViewType);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public int getItemViewType(int position) {
                        T t = list.get(position);
                        return initItemViewType(position, t);
                    }

                    @Override
                    public int getItemCount() {
                        return list.size();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    protected String initRowTitle(Context context) {
        return null;
    }

    protected RecyclerView.ItemDecoration initItemDecoration() {
        return null;
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
