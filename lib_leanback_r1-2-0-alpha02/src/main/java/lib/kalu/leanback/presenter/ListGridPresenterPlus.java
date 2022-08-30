package lib.kalu.leanback.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class ListGridPresenterPlus<T> extends Presenter {

    private int index = 0;
    private final List<T> mDatas = new ArrayList<>();
    private final List<T> mSubs = new ArrayList<>();
    private final List<T> mTemps = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            onLife(context);
            View view = LayoutInflater.from(context).inflate(R.layout.lb_list_grid, parent, false);
            return new ViewHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

        // add
        try {
            List<T>[] tem = (List<T>[]) item;
            mDatas.clear();
            mDatas.addAll(tem[0]);
            mSubs.clear();
            mSubs.addAll(tem[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // header
        try {
            TextView textView = viewHolder.view.findViewById(R.id.module_leanback_lgp_header);
            onBindHeader(textView, (List<T>) mDatas);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // list
        try {
            RecyclerView recyclerView = viewHolder.view.findViewById(R.id.module_leanback_lgp_list);
            Context context = recyclerView.getContext();
            setAdapter(context, recyclerView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private final void setAdapter(@NonNull Context context, @NonNull RecyclerView recyclerView) {


        try {

            // 1
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (null == layoutManager) {

                refreshTemps();
                int span = initSpan();
                int size = mTemps.size();
                int col;
                if (size <= span) {
                    col = span;
                } else {
                    col = Math.min(span, size);
                }

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
                        T t = mTemps.get(position);
                        int spanSize = initSpanSize(t, position);
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
            if (null == adapter) {
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
                            onCreateHolder(context, holder, view, mTemps, viewType);
                            return holder;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        try {
                            T t1 = mTemps.get(position);
                            int viewType = holder.getItemViewType();
                            onBindHolder(holder.itemView, t1, position, viewType);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public int getItemViewType(int position) {
                        T t = mTemps.get(position);
                        int i = initItemViewType(t, position);
                        return i != -1 ? i : super.getItemViewType(position);
                    }

                    @Override
                    public int getItemCount() {
                        return mTemps.size();
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

    protected boolean initScrollHorizontally() {
        return true;
    }

    protected boolean initScrollVertically() {
        return true;
    }

    protected int initOrientation() {
        return RecyclerView.VERTICAL;
    }

    private final void refreshTemps() {
        mTemps.clear();
        // 数据
        int span = initSpan();
        int start = index * span;
        int count = start + 10;
        for (int i = start; i < count; i++) {
            T t = mDatas.get(i);
            if (null == t)
                continue;
            mTemps.add(t);
        }
        mTemps.addAll(mSubs);
    }

    protected final void right() {
        index++;
        int span = initSpan();
        int size = index * span;
        int count = mDatas.size();
        if (size >= count)
            return;
        refreshTemps();
    }

    protected final void left() {
        index--;
        if (index <= 0)
            return;
        refreshTemps();
    }

    protected int initItemViewType(@NonNull T t, int position) {
        return -1;
    }

    protected int initSpanSize(@NonNull T t, int position) {
        return 1;
    }

    protected RecyclerView.ItemDecoration initItemDecoration() {
        return null;
    }

    protected void onBindHeader(@NonNull TextView textView, @NonNull List<T> t) {
    }

    protected abstract void onCreateHolder(@NonNull Context context, @NonNull RecyclerView.ViewHolder holder, @NonNull View view, @NonNull List<T> datas, @NonNull int viewType);

    protected abstract void onBindHolder(@NonNull View view, @NonNull T item, @NonNull int position, @NonNull int viewType);

    @LayoutRes
    protected abstract int initLayout(int viewType);

    protected abstract int initSpan();

    @SuppressLint("AppCompatCustomView")
    public static final class TextViewListGridPresenter extends TextView {
        public TextViewListGridPresenter(Context context) {
            super(context);
        }

        public TextViewListGridPresenter(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public TextViewListGridPresenter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public TextViewListGridPresenter(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void setText(CharSequence text, BufferType type) {
            super.setText(text, type);
            setVisibility(null != text && text.length() > 0 ? View.VISIBLE : View.GONE);
        }
    }
}
