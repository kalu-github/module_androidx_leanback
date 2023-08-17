package lib.kalu.leanback.presenter;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import lib.kalu.leanback.presenter.bean.TvPresenterRowBean;
import lib.kalu.leanback.presenter.impl.ListTvPresenterImpl;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvGridPresenter<T extends TvPresenterRowBean> extends Presenter implements ListTvPresenterImpl {

    private void putValue(@NonNull ViewHolder viewHolder, @NonNull List<T> list) {
        viewHolder.setObject(list);
    }

    private List<T> getValue(@NonNull ViewHolder viewHolder) {
        return (List<T>) viewHolder.getObject();
    }

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
            ViewHolder viewHolder = new ViewHolder(inflate);
            initAdapter(context, inflate, viewHolder);
            return viewHolder;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => onCreateViewHolder => " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        // datas
        formatData(viewHolder, item);
        // title
        updateTitle(getValue(viewHolder), viewHolder.view, R.id.module_leanback_lgp_title);
        // layoutManager
        initLayoutManager(viewHolder);
        // list
        updateAdapter(viewHolder.view);
    }

    private void updateAdapter(View view) {
        try {
            RecyclerView recyclerView = view.findViewById(R.id.module_leanback_lgp_list);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => updateAdapter => " + e.getMessage(), e);
        }
    }

    private void formatData(ViewHolder viewHolder, Object item) {
        try {
            int max = initMax();
            ArrayList<T> list = new ArrayList<>(max);
            if (max <= 0) {
                list.addAll((Collection<? extends T>) item);
            } else {
                List<T> collection = (List<T>) item;
                for (int i = 0; i < max; i++) {
                    T t = collection.get(i);
                    if (null == t)
                        continue;
                    list.add(t);
                }
            }
            putValue(viewHolder, list);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => formatData => " + e.getMessage());
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private void initLayoutManager(@NonNull ViewHolder viewHolder) {
        try {
            RecyclerView recyclerView = viewHolder.view.findViewById(R.id.module_leanback_lgp_list);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (null == layoutManager) {

                int col;
                int span = initSpan();
                int size = getValue(viewHolder).size();
                if (size <= span) {
                    col = span;
                } else {
                    col = Math.min(span, size);
                }

                GridLayoutManager manager = new GridLayoutManager(viewHolder.view.getContext(), col) {
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

    private void initAdapter(@NonNull Context context, @NonNull View inflate, @NonNull ViewHolder viewHolder) {
        try {
            RecyclerView recyclerView = inflate.findViewById(R.id.module_leanback_lgp_list);
            if (null == recyclerView)
                throw new Exception("recyclerView error: null");
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (null != adapter)
                throw new Exception("adapter error: null");
            recyclerView.setAdapter(new RecyclerView.Adapter() {

                private final List<T> mData = new ArrayList<>(initMax());

                private void checkData() {
                    try {
                        int size = mData.size();
                        if (size > 0)
                            throw new Exception();
                        List<T> value = getValue(viewHolder);
                        mData.clear();
                        mData.addAll(value);
                    } catch (Exception e) {
                    }
                }

                @NonNull
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    try {
                        onLife(context);
                        View view = LayoutInflater.from(context).inflate(initLayout(viewType), parent, false);
                        RecyclerView.ViewHolder itemHolder = new RecyclerView.ViewHolder(view) {
                        };
                        onCreateHolder(context, itemHolder, view, mData, viewType);
                        return itemHolder;
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
                    checkData();
                    T t = mData.get(position);
                    int i = initItemViewType(position, t);
                    return i != -1 ? i : super.getItemViewType(position);
                }

                @Override
                public int getItemCount() {
                    checkData();
                    return mData.size();
                }
            });
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

    private RecyclerView findRecyclerView(@NonNull View v, @NonNull boolean init) {

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

    private void checkedPosition(RecyclerView viewGroup, int index, int position) {
        try {
            RecyclerView.ViewHolder viewHolder = viewGroup.findViewHolderForAdapterPosition(index);
            RecyclerView recyclerView = viewHolder.itemView.findViewById(R.id.module_leanback_lgp_list);
            recyclerView.getAdapter().notifyItemRangeChanged(position, 1);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridPresenter => checkedPosition => " + e.getMessage(), e);
        }
    }

    protected void onCreateHolder(@NonNull Context context, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull View itemView, @NonNull List<T> list, @NonNull int viewType) {
    }

    protected void onBindHolder(@NonNull View view, @NonNull T item, @NonNull int position, @NonNull int viewType) {
    }

    @LayoutRes
    protected abstract int initLayout(int viewType);

    protected abstract int initSpan();
}
