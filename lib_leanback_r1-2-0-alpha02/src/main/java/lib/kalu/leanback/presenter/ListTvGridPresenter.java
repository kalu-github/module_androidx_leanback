package lib.kalu.leanback.presenter;

import android.content.Context;
import android.graphics.Rect;
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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import lib.kalu.leanback.presenter.bean.TvPresenterRowBean;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvGridPresenter<T extends TvPresenterRowBean> extends Presenter implements ListTvPresenterImpl {

    private final List<T> mData = new LinkedList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            onLife(context);
            ViewGroup inflate = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.lb_list_tv_grid, parent, false);
            setPadding(context, inflate);
            setBackgroundColor(context, inflate);
            setContentBackgroundColor(context, inflate, R.id.module_leanback_lgp_list);
            setTitlePadding(context, inflate, R.id.module_leanback_lgp_title);
            setTitleTextColor(context, inflate, R.id.module_leanback_lgp_title);
            setTitleTextSize(context, inflate, R.id.module_leanback_lgp_title);
            setTitleAssetTTF(context, inflate, R.id.module_leanback_lgp_title);
            setTitleBackgroundColor(context, inflate, R.id.module_leanback_lgp_title);
            initAdapter(context, inflate);
            return new ViewHolder(inflate);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => onCreateViewHolder => " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

        // datas
        formatData(item);

        // title
        updateTitle(mData, viewHolder.view, R.id.module_leanback_lgp_title);

        // layoutManager
        initLayoutManager(viewHolder.view);

        // list
        updateAdapter(viewHolder.view);
    }

    private final void updateAdapter(View view) {
        try {
            RecyclerView recyclerView = view.findViewById(R.id.module_leanback_lgp_list);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => updateAdapter => " + e.getMessage(), e);
        }
    }

    private final void formatData(Object item) {
        try {
            int size = mData.size();
            if (size > 0)
                throw new Exception("not empty");
            mData.clear();
            int max = initMax();
            if (max <= 0) {
                mData.addAll((Collection<? extends T>) item);
            } else {
                List<T> collection = (List<T>) item;
                for (int i = 0; i < max; i++) {
                    T t = collection.get(i);
                    if (null == t)
                        continue;
                    mData.add(t);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => formatData => " + e.getMessage(), e);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private final void initLayoutManager(@NonNull View inflate) {
        try {
            RecyclerView recyclerView = inflate.findViewById(R.id.module_leanback_lgp_list);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (null == layoutManager) {

                int col;
                int span = initSpan();
                int size = mData.size();
                if (size <= span) {
                    col = span;
                } else {
                    col = Math.min(span, size);
                }

                GridLayoutManager manager = new GridLayoutManager(inflate.getContext(), col) {
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
                recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        initItemOffsets(outRect, view, parent, state);
                    }
                });
                recyclerView.setLayoutManager(manager);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => initLayoutManager => " + e.getMessage(), e);
        }
    }

    private final void initAdapter(@NonNull Context context, @NonNull View inflate) {
        try {
            RecyclerView recyclerView = inflate.findViewById(R.id.module_leanback_lgp_list);
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (null == adapter) {
                recyclerView.setAdapter(new RecyclerView.Adapter() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        try {
                            onLife(context);
                            View view = LayoutInflater.from(context).inflate(initLayout(viewType), parent, false);
                            RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {
                            };
                            onCreateHolder(context, holder, view, mData, viewType);
                            return holder;
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvGridPresenter => setAdapter => onCreateViewHolder => " + e.getMessage(), e);
                            return null;
                        }
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        try {
                            T t = mData.get(position);
                            int viewType = holder.getItemViewType();
                            onBindHolder(holder.itemView, t, position, viewType);
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvGridPresenter => setAdapter => onBindViewHolder => " + e.getMessage(), e);
                        }
                    }

                    @Override
                    public int getItemViewType(int position) {
                        T t = mData.get(position);
                        int i = initItemViewType(position, t);
                        return i != -1 ? i : super.getItemViewType(position);
                    }

                    @Override
                    public int getItemCount() {
                        return mData.size();
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

    protected int initMax() {
        return 0;
    }

    protected abstract void onCreateHolder(@NonNull Context context, @NonNull RecyclerView.ViewHolder holder, @NonNull View view, @NonNull List<T> datas, @NonNull int viewType);

    protected abstract void onBindHolder(@NonNull View view, @NonNull T item, @NonNull int position, @NonNull int viewType);

    @LayoutRes
    protected abstract int initLayout(int viewType);

    protected abstract int initSpan();
}
