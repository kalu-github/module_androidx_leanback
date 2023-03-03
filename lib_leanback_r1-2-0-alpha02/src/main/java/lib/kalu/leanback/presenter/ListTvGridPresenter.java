package lib.kalu.leanback.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lib.kalu.leanback.presenter.bean.TvPresenterRowBean;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvGridPresenter<T extends TvPresenterRowBean> extends Presenter implements ListTvPresenterImpl {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            onLife(context);
            View view = LayoutInflater.from(context).inflate(R.layout.lb_list_tv_grid, parent, false);
            initTitleStyle(context, view, R.id.module_leanback_lgp_title);
            return new ViewHolder(view);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => onCreateViewHolder => " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

        // datas
        int col = -1;
        List<T> list = new ArrayList<>();
        try {
            List<T> t = (List<T>) item;
            int max = initMax();
            int span = initSpan();
            int size = t.size();
            int length = Math.min(max, size);
            if (size <= span) {
                col = span;
            } else {
                col = Math.min(span, size);
            }

            // 0
            for (int i = 0; i < length; i++) {
                T o = t.get(i);
                if (null == o)
                    continue;
                list.add(o);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => onBindViewHolder => " + e.getMessage(), e);
        }

        if (col == -1)
            return;

        // header
        setRowTitle(viewHolder.view, list);

        // list
        try {
            RecyclerView recyclerView = viewHolder.view.findViewById(R.id.module_leanback_lgp_list);
            Context context = recyclerView.getContext();
            setAdapter(context, recyclerView, col, list);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => onBindViewHolder => " + e.getMessage(), e);
        }
    }

    private final void setRowTitle(View view, List<T> data) {

        String rowTitle;
        try {
            rowTitle = initRowTitle(view.getContext());
        } catch (Exception e) {
            rowTitle = null;
        }
        try {
            if (null == rowTitle || rowTitle.length() <= 0) {
                T t = data.get(0);
                rowTitle = t.getRowTitle();
            }
        } catch (Exception e) {
        }

        try {
            TextView textView = view.findViewById(R.id.module_leanback_lgp_title);
            textView.setText(rowTitle);
            textView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesPresenter => setHead => " + e.getMessage(), e);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private final void setAdapter(@NonNull Context context, @NonNull RecyclerView recyclerView, @NonNull int col, @NonNull List<T> list) {
        try {

            // 1
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (null == layoutManager) {
                GridLayoutManager manager = new GridLayoutManager(context, col) {
                    @Override
                    public boolean canScrollHorizontally() {
                        return initScrollHorizontally();
                    }

                    @Override
                    public boolean canScrollVertically() {
                        return initScrollVertically();
                    }
                };
                manager.setOrientation(initOrientation());
                manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int spanSize = initSpanSize(position);
                        return spanSize <= 0 ? 1 : spanSize;
                    }
                });
                RecyclerView.ItemDecoration itemDecoration = initItemDecoration();
                if (null != itemDecoration) {
                    recyclerView.addItemDecoration(itemDecoration);
                }
                recyclerView.setLayoutManager(manager);
                recyclerView.setAnimation(null);
                recyclerView.setItemAnimator(null);
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
                            onCreateHolder(context, holder, view, list, viewType);
                            return holder;
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvGridPresenter => setAdapter => onCreateViewHolder => " + e.getMessage(), e);
                            return null;
                        }
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        try {
                            T t = list.get(position);
                            int viewType = holder.getItemViewType();
                            onBindHolder(holder.itemView, t, position, viewType);
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvGridPresenter => setAdapter => onBindViewHolder => " + e.getMessage(), e);
                        }
                    }

                    @Override
                    public int getItemViewType(int position) {
                        T t = list.get(position);
                        int i = initItemViewType(position, t);
                        return i != -1 ? i : super.getItemViewType(position);
                    }

                    @Override
                    public int getItemCount() {
                        return list.size();
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => setAdapter => " + e.getMessage(), e);
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

    protected final void requestFocus(@NonNull View v, @NonNull int position) {
        try {
            RecyclerView recyclerView = findRecyclerView(v, true);
            View viewByPosition = recyclerView.getLayoutManager().findViewByPosition(position);
            viewByPosition.requestFocus();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => requestFocus => " + e.getMessage(), e);
        }
    }

    protected final void notifyDataSetChanged(@NonNull View v) {
        try {
            RecyclerView recyclerView = findRecyclerView(v, true);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => notifyDataSetChanged => " + e.getMessage(), e);
        }
    }

    protected final void notifyItemRangeChanged(@NonNull View v, int start, int itemCount) {
        try {
            RecyclerView recyclerView = findRecyclerView(v, true);
            recyclerView.getAdapter().notifyItemRangeChanged(start, itemCount);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => notifyItemRangeChanged => " + e.getMessage(), e);
        }
    }

    private final RecyclerView findRecyclerView(@NonNull View v, @NonNull boolean init) {

        if (init) {
            View view = v.findViewById(R.id.module_leanback_lgp_list);
            if (view instanceof RecyclerView) {
                return (RecyclerView) view;
            } else {
                return findRecyclerView(v, false);
            }
        } else {
            ViewParent parent = v.getParent();
            if (null == parent) {
                return null;
            } else if (parent instanceof RecyclerView) {
                if (((RecyclerView) parent).getId() == R.id.module_leanback_lgp_list) {
                    return (RecyclerView) parent;
                } else {
                    return null;
                }
            } else {
                return findRecyclerView((View) parent, false);
            }
        }
    }

    protected boolean initScrollHorizontally() {
        return true;
    }

    protected boolean initScrollVertically() {
        return true;
    }

    protected int initOrientation() {
        return RecyclerView.VERTICAL;
    }

    protected int initItemViewType(int position, T t) {
        return -1;
    }

    protected int initSpanSize(int position) {
        return 1;
    }

    protected String initRowTitle(Context context) {
        return null;
    }

    protected RecyclerView.ItemDecoration initItemDecoration() {
        return null;
    }

    protected abstract void onCreateHolder(@NonNull Context context, @NonNull RecyclerView.ViewHolder holder, @NonNull View view, @NonNull List<T> datas, @NonNull int viewType);

    protected abstract void onBindHolder(@NonNull View view, @NonNull T item, @NonNull int position, @NonNull int viewType);

    @LayoutRes
    protected abstract int initLayout(int viewType);

    protected abstract int initSpan();

    protected abstract int initMax();
}
