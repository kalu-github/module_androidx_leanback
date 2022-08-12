package lib.kalu.leanback.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class ListRowPresenter<T> extends Presenter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            onLife(context);
            View view = LayoutInflater.from(context).inflate(R.layout.lb_list_row_news, parent, false);
            return new ViewHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        // header
        try {
            TextView textView = viewHolder.view.findViewById(R.id.module_leanback_llr_header);
            onBindHeader(textView, (List<T>) item);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // list
        try {
            RecyclerView recyclerView = viewHolder.view.findViewById(R.id.module_leanback_llr_list);
            Context context = recyclerView.getContext();
            setAdapter(context, recyclerView, (List<T>) item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private final void setAdapter(@NonNull Context context, @NonNull RecyclerView recyclerView, @NonNull List<T> t) {
        try {

            // 1
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (null == layoutManager) {
                LinearLayoutManager manager = new LinearLayoutManager(context);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                RecyclerView.ItemDecoration itemDecoration = initItemDecoration();
                if (null != itemDecoration) {
                    recyclerView.addItemDecoration(itemDecoration);
                }
                recyclerView.setLayoutManager(manager);
            }

            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (null == adapter) {
                int length = t.size();
                ArrayList<T> list = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    T o = t.get(i);
                    if (null == o)
                        continue;
                    list.add(o);
                }
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
                            T t1 = list.get(position);
                            onBindHolder(holder.itemView, t1, position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    protected RecyclerView.ItemDecoration initItemDecoration() {
        return null;
    }

    protected void onBindHeader(@NonNull TextView textView, @NonNull List<T> t) {
    }

    protected abstract void onCreateHolder(@NonNull Context context, @NonNull RecyclerView.ViewHolder holder, @NonNull View view, @NonNull List<T> datas);

    protected abstract void onBindHolder(@NonNull View view, @NonNull T item, @NonNull int position);

    @LayoutRes
    protected abstract int initLayout(int viewType);

    @SuppressLint("AppCompatCustomView")
    public static final class TextViewListRowPresenter extends TextView {
        public TextViewListRowPresenter(Context context) {
            super(context);
        }

        public TextViewListRowPresenter(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public TextViewListRowPresenter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public TextViewListRowPresenter(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void setText(CharSequence text, BufferType type) {
            super.setText(text, type);
            setVisibility(null != text && text.length() > 0 ? View.VISIBLE : View.GONE);
        }
    }
}
