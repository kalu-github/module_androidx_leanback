package lib.kalu.leanback.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class ListGridPresenter extends Presenter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            onLife(context);
            View view = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.lb_list_grid, parent, false);
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
            TextView textView = viewHolder.view.findViewById(R.id.module_leanback_lgp_header);
            onHeader(textView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // list
        try {
            RecyclerView recyclerView = viewHolder.view.findViewById(R.id.module_leanback_lgp_list);
            Context context = recyclerView.getContext();
            setAdapter(context, recyclerView, (List<Object>) item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private final void setAdapter(@NonNull Context context, @NonNull RecyclerView recyclerView, @NonNull List<Object> t) {
        try {
            int max = initMax();
            int span = initSpan();
            int size = t.size();
            int length = Math.min(max, size);
            int col = Math.min(span, size);
            ArrayList<Object> list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                Object o = t.get(i);
                if (null == o)
                    continue;
                list.add(o);
            }
            GridLayoutManager manager = new GridLayoutManager(context, col);
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
            recyclerView.setAdapter(new RecyclerView.Adapter() {
                @NonNull
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    try {
                        Context context = parent.getContext();
                        onLife(context);
                        View view = (View) LayoutInflater.from(context).inflate(initLayout(viewType), parent, false);
                        RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {
                        };
                        onHolder(context, holder, view, list);
                        return holder;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                    try {
                        onRefresh(holder.itemView, list, position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public int getItemViewType(int position) {
                    int i = initItemViewType(position);
                    return i != -1 ? i : super.getItemViewType(position);
                }

                @Override
                public int getItemCount() {
                    return list.size();
                }
            });
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

    protected void onHolder(@NonNull Context context, @NonNull RecyclerView.ViewHolder holder, @NonNull View view, @NonNull Object item) {
    }

    protected abstract void onRefresh(@NonNull View view, @NonNull Object item, @NonNull int position);

    protected void onHeader(@NonNull TextView textView) {
    }

    @LayoutRes
    protected abstract int initLayout(int viewType);

    protected abstract int initSpan();

    protected abstract int initMax();

    protected int initOrientation() {
        return RecyclerView.VERTICAL;
    }

    protected int initItemViewType(int position) {
        return -1;
    }

    protected int initSpanSize(int position) {
        return 1;
    }

    protected RecyclerView.ItemDecoration initItemDecoration() {
        return null;
    }
}
