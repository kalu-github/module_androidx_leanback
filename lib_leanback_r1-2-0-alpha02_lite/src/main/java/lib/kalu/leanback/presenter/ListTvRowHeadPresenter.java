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
import java.util.LinkedList;

import lib.kalu.leanback.presenter.bean.TvPresenterRowBean;
import lib.kalu.leanback.presenter.impl.ListTvPresenterImpl;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvRowHeadPresenter<T extends TvPresenterRowBean> extends Presenter implements ListTvPresenterImpl {

    private final LinkedList<T> mData = new LinkedList<>();

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
            initContent(context, inflate);
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

        // title
        updateTitle(mData, viewHolder.view, R.id.module_leanback_llr_head_title);

        // list
        updateAdapter(viewHolder.view);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private final void formatData(Object item) {
        try {
            int size = mData.size();
            if (size > 0)
                throw new Exception("not empty");
            mData.clear();
            mData.addAll((Collection<? extends T>) item);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRowPresenter => formatData => " + e.getMessage(), e);
        }
    }

    private final void updateAdapter(View view) {
        try {
            RecyclerView recyclerView = view.findViewById(R.id.module_leanback_llr_head_list);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRowPresenter => updateAdapter => " + e.getMessage(), e);
        }
    }

    private final void initContent(@NonNull Context context, @NonNull View inflate) {
        try {
            RelativeLayout relativeLayout = inflate.findViewById(R.id.module_leanback_llr_head_content);
            int childCount = relativeLayout.getChildCount();
            if (childCount > 0)
                throw new Exception("constance child");
            View view = LayoutInflater.from(context).inflate(initContent(), relativeLayout, false);
            relativeLayout.removeAllViews();
            relativeLayout.addView(view);
            if (null != view.getLayoutParams()) {
                ((RelativeLayout.LayoutParams) view.getLayoutParams()).bottomMargin = initContentMarginBottom(context);
                ((RelativeLayout.LayoutParams) view.getLayoutParams()).leftMargin = 0;
                ((RelativeLayout.LayoutParams) view.getLayoutParams()).rightMargin = 0;
                ((RelativeLayout.LayoutParams) view.getLayoutParams()).topMargin = 0;
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRowPresenter => initContent => " + e.getMessage(), e);
        }
    }

    private final void initAdapter(@NonNull Context context, @NonNull ViewGroup viewGroup) {
        try {

            // 1
            RecyclerView recyclerView = viewGroup.findViewById(R.id.module_leanback_llr_head_list);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (null == layoutManager) {
                LinearLayoutManager manager = new LinearLayoutManager(context) {
                    @Override
                    public boolean canScrollHorizontally() {
                        int size = mData.size();
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
                            // onClick
                            try {
                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int position = holder.getAbsoluteAdapterPosition();
                                        if (position >= 0) {
                                            T t = mData.get(position);
                                            onClickItemHolder(v.getContext(), v, t, position);
                                        }
                                    }
                                });
                            } catch (Exception e) {
                            }
                            // onFocus
                            try {
                                view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                    @Override
                                    public void onFocusChange(View v, boolean b) {
                                        int position = holder.getAbsoluteAdapterPosition();
                                        if (position >= 0) {
                                            // 1
                                            T t = mData.get(position);
                                            onFocusItemHolder(v.getContext(), v, t, position, b);
                                            // 2
                                            if (b) {
                                                ViewGroup groupLayout = viewGroup.findViewById(R.id.module_leanback_llr_head_content);
                                                onBindHeadHolder(groupLayout.getContext(), groupLayout, t, position);
                                            }
                                        }
                                    }
                                });
                            } catch (Exception e) {
                            }
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
                            onBindItemHolder(holder.itemView.getContext(), holder.itemView, t, position);
                        } catch (Exception e) {
                        }
                        try {
                            if (position > 0)
                                throw new Exception();
                            ViewGroup groupLayout = viewGroup.findViewById(R.id.module_leanback_llr_head_content);
                            Object tag = groupLayout.getTag(R.id.module_leanback_llr_head_content);
                            if (null != tag)
                                throw new Exception();
                            groupLayout.setTag(R.id.module_leanback_llr_head_content, 1);
                            T t = mData.get(position);
                            onBindHeadHolder(groupLayout.getContext(), groupLayout, t, position);
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public int getItemCount() {
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

    protected abstract void onFocusItemHolder(@NonNull Context context, @NonNull View view, @NonNull T item, @NonNull int position, boolean hasFocus);

    protected abstract void onClickItemHolder(@NonNull Context context, @NonNull View view, @NonNull T item, @NonNull int position);

    protected abstract void onBindItemHolder(@NonNull Context context, @NonNull View view, @NonNull T item, @NonNull int position);

    protected abstract void onBindHeadHolder(@NonNull Context context, @NonNull View view, @NonNull T item, @NonNull int position);

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
