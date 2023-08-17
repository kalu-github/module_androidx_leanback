package lib.kalu.leanback.presenter;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import lib.kalu.leanback.presenter.bean.TvPresenterRowBean;
import lib.kalu.leanback.presenter.impl.ListTvPresenterImpl;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvRowHeadPresenter<T extends TvPresenterRowBean> extends Presenter implements ListTvPresenterImpl {

    private final HashMap<ViewHolder, List<T>> mSimpleArrayMap = new HashMap<>();

    private void putValue(@NonNull ViewHolder viewHolder, @NonNull List<T> list) {
        mSimpleArrayMap.put(viewHolder, list);
    }

    private List<T> getValue(@NonNull ViewHolder viewHolder) {
        return mSimpleArrayMap.get(viewHolder);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            onLife(context);
            ViewGroup inflate = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.lb_list_tv_row_head, parent, false);
            setPadding(context, inflate);
            setBackgroundColor(context, inflate);
            setContentBackgroundColor(context, inflate, R.id.module_leanback_llr_head_list);
            setTitlePadding(context, inflate, R.id.module_leanback_llr_head_title);
            setTitleTextColor(context, inflate, R.id.module_leanback_llr_head_title);
            setTitleTextSize(context, inflate, R.id.module_leanback_llr_head_title);
            setTitleAssetTTF(context, inflate, R.id.module_leanback_llr_head_title);
            setTitleBackgroundColor(context, inflate, R.id.module_leanback_llr_head_title);
            initHeadAdapter(context, inflate);
            ViewHolder viewHolder = new ViewHolder(inflate);
            initItemAdapter(context, inflate, viewHolder);
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
        updateTitle(getValue(viewHolder), viewHolder.view, R.id.module_leanback_llr_head_title);

        // list
        updateAdapter(viewHolder.view);

        // head
        onCreateHeadHolder(viewHolder.view.getContext(), viewHolder.view, getValue(viewHolder));
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private final void formatData(ViewHolder viewHolder, Object item) {
        try {
            List<T> value = getValue(viewHolder);
            if (null != value)
                throw new Exception("value warning: not null");
            putValue(viewHolder, (List<T>) item);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRowHeadPresenter => formatData => " + e.getMessage(), e);
        }
    }

    private final void updateAdapter(View view) {
        try {
            RecyclerView recyclerView = view.findViewById(R.id.module_leanback_llr_head_list);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRowHeadPresenter => updateAdapter => " + e.getMessage());
        }
    }

    private final void initHeadAdapter(@NonNull Context context, @NonNull View inflate) {
        try {
            RelativeLayout headLayout = inflate.findViewById(R.id.module_leanback_llr_head_content);
            int childCount = headLayout.getChildCount();
            if (childCount > 0)
                throw new Exception("constance child");
            View view = LayoutInflater.from(context).inflate(initContent(), headLayout, false);
            headLayout.removeAllViews();
            headLayout.addView(view);
            if (null != view.getLayoutParams()) {
                ((RelativeLayout.LayoutParams) view.getLayoutParams()).bottomMargin = initContentMarginBottom(context);
                ((RelativeLayout.LayoutParams) view.getLayoutParams()).leftMargin = 0;
                ((RelativeLayout.LayoutParams) view.getLayoutParams()).rightMargin = 0;
                ((RelativeLayout.LayoutParams) view.getLayoutParams()).topMargin = 0;
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRowHeadPresenter => initHeadAdapter => " + e.getMessage());
        }
    }

    private void initItemAdapter(@NonNull Context context, @NonNull View viewGroup, @NonNull ViewHolder viewHolder) {
        try {

            ViewGroup headLayout = viewGroup.findViewById(R.id.module_leanback_llr_head_content);
            RecyclerView recyclerView = viewGroup.findViewById(R.id.module_leanback_llr_head_list);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (null == layoutManager) {
                LinearLayoutManager manager = new LinearLayoutManager(context) {
                    @Override
                    public boolean canScrollHorizontally() {
                        int size = getValue(viewHolder).size();
                        return ListTvRowHeadPresenter.this.canScrollHorizontally(size);
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
                            View view = LayoutInflater.from(context).inflate(initLayout(), parent, false);
                            RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {
                            };
                            onCreateItemHolder(context, headLayout, view, getValue(viewHolder), holder);
                            return holder;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        try {
                            T t = getValue(viewHolder).get(position);
                            onBindItemHolder(holder.itemView.getContext(), headLayout, holder.itemView, t, position);
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public int getItemCount() {
                        return getValue(viewHolder).size();
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRowHeadPresenter => initItemAdapter => " + e.getMessage());
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

    protected void onCreateHeadHolder(@NonNull Context context, @NonNull View headView, @NonNull List<T> data) {
    }

    protected void onCreateItemHolder(@NonNull Context context, @NonNull View headView, @NonNull View itemView, @NonNull List<T> data, @NonNull RecyclerView.ViewHolder holder) {
    }

    protected void onBindItemHolder(@NonNull Context context, @NonNull View headView, @NonNull View itemView, @NonNull T item, @NonNull int position) {
    }

    @LayoutRes
    protected abstract int initLayout();

    @LayoutRes
    protected abstract int initContent();

    protected int initContentMarginBottom(Context context) {
        return 0;
    }

    protected boolean canScrollHorizontally(int count) {
        return true;
    }
}
