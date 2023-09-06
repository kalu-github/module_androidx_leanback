package lib.kalu.leanback.presenter;

import android.content.Context;
import android.graphics.Rect;
import android.view.KeyEvent;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import lib.kalu.leanback.presenter.bean.TvPresenterRowBean;
import lib.kalu.leanback.presenter.impl.ListTvPresenterImpl;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvRowHeadPresenter<T extends TvPresenterRowBean> extends Presenter implements ListTvPresenterImpl {

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
        try {
            List<T> list = getValue(viewHolder);
            onCreateHeadHolder(viewHolder.view.getContext(), viewHolder.view, list);
        } catch (Exception e) {
        }
        try {
            List<T> list = getValue(viewHolder);
            T t = list.get(0);
            onBindHeadHolder(viewHolder.view.getContext(), viewHolder.view, 0, t);
        } catch (Exception e) {
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private void formatData(ViewHolder viewHolder, Object item) {
        try {
            putValue(viewHolder, (List<T>) item);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRowHeadPresenter => formatData => " + e.getMessage(), e);
        }
    }

    private void updateAdapter(View view) {
        try {
            RecyclerView recyclerView = view.findViewById(R.id.module_leanback_llr_head_list);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRowHeadPresenter => updateAdapter => " + e.getMessage());
        }
    }

    private void initHeadAdapter(@NonNull Context context, @NonNull View inflate) {
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
                            View view = LayoutInflater.from(context).inflate(initLayout(), parent, false);
                            view.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                                    LeanBackUtil.log("ListTvRowHeadPresenter => initItemAdapter => setOnKeyListener => action = " + keyEvent.getAction() + ", keyCode = " + keyCode);

                                    // action_down => keycode_dpad_left
                                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                                        try {
                                            int position = recyclerView.getChildAdapterPosition(view);
                                            T t = mData.get(position);
                                            if (position <= 0) {
                                                onUnCheckedItemHolder(view.getContext(), headLayout, view, t, position, keyCode);
                                            } else {
                                                onBindHeadHolder(view.getContext(), headLayout, position - 1, t);
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                    // action_down => keycode_dpad_right
                                    else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                                        try {
                                            int position = recyclerView.getChildAdapterPosition(view);
                                            if (position <= 0)
                                                throw new Exception();
                                            int size = mData.size();
                                            T t = mData.get(position);
                                            if (position + 1 >= size) {
                                                onUnCheckedItemHolder(view.getContext(), headLayout, view, t, position, keyCode);
                                            } else {
                                                onBindHeadHolder(view.getContext(), headLayout, position + 1, t);
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                    // action_down => keycode_dpad_down
                                    else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                                        try {
                                            int position = recyclerView.getChildAdapterPosition(view);
                                            if (position < 0)
                                                throw new Exception();
                                            T t = mData.get(position);
                                            onUnCheckedItemHolder(view.getContext(), headLayout, view, t, position, keyCode);
                                        } catch (Exception e) {
                                        }
                                    }
                                    // action_down => keycode_dpad_up
                                    else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                        try {
                                            int position = recyclerView.getChildAdapterPosition(view);
                                            if (position < 0)
                                                throw new Exception();
                                            T t = mData.get(position);
                                            onUnCheckedItemHolder(view.getContext(), headLayout, view, t, position, keyCode);
                                        } catch (Exception e) {
                                        }
                                    }
                                    // action_up => keycode_dpad_down
                                    else if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                                        try {
                                            int position = recyclerView.getChildAdapterPosition(view);
                                            if (position < 0)
                                                throw new Exception();
                                            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && position == 0)
                                                throw new Exception();
                                            int size = mData.size();
                                            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && position + 1 > size)
                                                throw new Exception();
                                            T t = mData.get(position);
                                            onBindHeadHolder(view.getContext(), headLayout, position, t);
                                        } catch (Exception e) {
                                        }
                                    }
                                    // action_up => keycode_dpad_up
                                    else if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                        try {
                                            int position = recyclerView.getChildAdapterPosition(view);
                                            if (position < 0)
                                                throw new Exception();
                                            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && position == 0)
                                                throw new Exception();
                                            int size = mData.size();
                                            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && position + 1 > size)
                                                throw new Exception();
                                            T t = mData.get(position);
                                            onBindHeadHolder(view.getContext(), headLayout, position, t);
                                        } catch (Exception e) {
                                        }
                                    }
                                    return false;
                                }
                            });
                            RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {
                            };
                            onCreateItemHolder(context, headLayout, view, mData, holder);
                            return holder;
                        } catch (Exception e) {
                            return null;
                        }
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        try {
                            T t = mData.get(position);
                            onBindItemHolder(holder.itemView.getContext(), headLayout, holder.itemView, t, position);
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public int getItemCount() {
                        checkData();
                        return mData.size();
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

    protected void onBindHeadHolder(@NonNull Context context, @NonNull View headView, int position, T data) {
    }

    protected void onCreateItemHolder(@NonNull Context context, @NonNull View headView, @NonNull View itemView, @NonNull List<T> data, @NonNull RecyclerView.ViewHolder holder) {
    }

    protected void onBindItemHolder(@NonNull Context context, @NonNull View headView, @NonNull View itemView, @NonNull T item, @NonNull int position) {
    }

    protected void onUnCheckedItemHolder(@NonNull Context context, @NonNull View headView, @NonNull View itemView, @NonNull T item, @NonNull int position, @NonNull int keyCode) {
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
