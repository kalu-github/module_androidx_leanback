package lib.kalu.leanback.presenter;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import lib.kalu.leanback.presenter.bean.TvPresenterRowBean;
import lib.kalu.leanback.presenter.impl.ListTvPresenterImpl;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvRowPresenter<T extends TvPresenterRowBean> extends Presenter implements ListTvPresenterImpl {

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
            ViewGroup inflate = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.lb_list_tv_row, parent, false);
            setPadding(context, inflate);
            setBackgroundColor(context, inflate);
            setContentBackgroundColor(context, inflate, R.id.module_leanback_llr_list);
            setTitlePadding(context, inflate, R.id.module_leanback_llr_title);
            setTitleTextColor(context, inflate, R.id.module_leanback_llr_title);
            setTitleTextSize(context, inflate, R.id.module_leanback_llr_title);
            setTitleAssetTTF(context, inflate, R.id.module_leanback_llr_title);
            setTitleBackgroundColor(context, inflate, R.id.module_leanback_llr_title);
            ViewHolder viewHolder = new ViewHolder(inflate);
            initAdapter(context, inflate, viewHolder);
            return viewHolder;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        // data
        formatData(viewHolder, item);
        // title
        updateTitle(getValue(viewHolder), viewHolder.view, R.id.module_leanback_llr_title);
        // list
        updateAdapter(viewHolder.view);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private void formatData(ViewHolder viewHolder, Object item) {
        try {
            putValue(viewHolder, (List<T>) item);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRowPresenter => formatData => " + e.getMessage(), e);
        }
    }

    private void updateAdapter(View view) {
        try {
            RecyclerView recyclerView = view.findViewById(R.id.module_leanback_llr_list);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRowPresenter => updateAdapter => " + e.getMessage(), e);
        }
    }

    private void initAdapter(@NonNull Context context, @NonNull View inflate, @NonNull ViewHolder viewHolder) {
        try {

            // 1
            RecyclerView recyclerView = inflate.findViewById(R.id.module_leanback_llr_list);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (null == layoutManager) {
                LinearLayoutManager manager = new LinearLayoutManager(context) {
                    @Override
                    public boolean canScrollHorizontally() {
                        int size = getValue(viewHolder).size();
                        return ListTvRowPresenter.this.canScrollHorizontally(size);
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

                    private final List<T> mData = new ArrayList<>();

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

                    private void cleanData() {
                        try {
                            mData.clear();
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
                        super.onViewDetachedFromWindow(holder);
                        cleanData();
                    }

                    @Override
                    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
                        super.onDetachedFromRecyclerView(recyclerView);
                        cleanData();
                    }

                    @Override
                    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
                        super.onViewAttachedToWindow(holder);
                        checkData();
                    }

                    @Override
                    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
                        super.onAttachedToRecyclerView(recyclerView);
                        checkData();
                    }

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
                        }
                    }

                    @Override
                    public int getItemViewType(int position) {
                        checkData();
                        T t = mData.get(position);
                        return initItemViewType(position, t);
                    }

                    @Override
                    public int getItemCount() {
                        checkData();
                        return mData.size();
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

    protected void onCreateHolder(@NonNull Context context, @NonNull RecyclerView.ViewHolder holder, @NonNull View view, @NonNull List<T> datas) {
    }

    protected void onBindHolder(@NonNull View view, @NonNull T item, @NonNull int position, @NonNull int viewType) {
    }

    @LayoutRes
    protected abstract int initLayout(int viewType);

    protected int initItemViewType(int position, T t) {
        return 1;
    }

    protected boolean canScrollHorizontally(int count) {
        return true;
    }
}
