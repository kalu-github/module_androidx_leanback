package lib.kalu.leanback.presenter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class ListRowDoublePresenter<T extends ListRowDoublePresenter.ListRowDoubleBean> extends Presenter {

    private final List<T> mDatas = new ArrayList();
    private final List<T> mDataTop = new ArrayList<>();
    private final List<T> mDataBottom = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.lb_list_row_double, parent, false);
            setAdapterTop(context, view);
            setAdapterBottom(context, view);
            return new ViewHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

        // data
        try {
            mDataTop.clear();
            mDataBottom.clear();
            mDatas.clear();
            mDatas.addAll((List<T>) item);
            int size = mDatas.size();
            for (int i = 0; i < size; i++) {
                T t = mDatas.get(i);
                if (null == t)
                    continue;
                boolean top = t.isTop();
                if (!top) {
                    mDataBottom.add(t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // head
        try {
            String head = null;
            int size = mDatas.size();
            for (int i = 0; i < size; i++) {
                T t = mDatas.get(i);
                if (null == t) {
                    continue;
                }
                String str = t.getHead();
                if (null != str && str.length() > 0) {
                    head = str;
                    break;
                }
            }
            if (null != head && head.length() > 0) {
                TextView textView = viewHolder.view.findViewById(R.id.module_leanback_llrd_head);
                textView.setText(head);
                textView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // top
        try {
            LinearLayout layout = viewHolder.view.findViewById(R.id.module_leanback_llrd_top);
            refreshTemps(layout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // list
        try {
            RecyclerView recyclerView = viewHolder.view.findViewById(R.id.module_leanback_llrd_bottom);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private final void setAdapterTop(@NonNull Context context, @NonNull View view) {
        try {
            // 1
            LinearLayout layout = view.findViewById(R.id.module_leanback_llrd_top);
            layout.removeAllViews();
            int column = initColumn();
            layout.setWeightSum(column);

            // 2
            for (int i = 0; i < column; i++) {
                View child = LayoutInflater.from(context).inflate(initLayoutTop(), null);
                child.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // left
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            return scrollLeft(v);
                        }
                        // right
                        else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            return scrollRight(v);
                        }
                        // down-in
                        else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            focusTop(v);
                        }
                        // up-in
                        else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            focusTop(v);
                        }
                        // left-in
                        else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            focusTop(v);
                        }
                        // right-in
                        else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            focusTop(v);
                        }
                        return false;
                    }
                });
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
                layoutParams.weight = 1;
                if (i > 0) {
                    layoutParams.leftMargin = initDecorationTop(context);
                }
                child.setLayoutParams(layoutParams);
                onCreateTop(context, child, mDataTop, i);
                layout.addView(child);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final void setAdapterBottom(@NonNull Context context, @NonNull View view) {
        try {
            RecyclerView recyclerView = view.findViewById(R.id.module_leanback_llrd_bottom);
            if (null == recyclerView.getLayoutManager()) {
                LinearLayoutManager manager = new LinearLayoutManager(context);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                RecyclerView.ItemDecoration itemDecoration = initDecorationBottom();
                if (null != itemDecoration) {
                    recyclerView.addItemDecoration(itemDecoration);
                }
                recyclerView.setLayoutManager(manager);
            }
            if (null == recyclerView.getAdapter()) {
                recyclerView.setAdapter(new RecyclerView.Adapter() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        try {
                            Context context = parent.getContext();
                            View view = LayoutInflater.from(context).inflate(initLayoutBottom(), parent, false);
                            RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {
                            };
                            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    if (hasFocus) {
                                        refreshTop(holder);
                                    }
                                }
                            });
                            onCreateBottom(context, holder, view, mDataBottom, false);
                            return holder;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        try {
                            T t = mDataBottom.get(position);
                            onBindBottom(holder.itemView, t, position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public int getItemCount() {
                        return mDataBottom.size();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final void refreshTemps(@NonNull LinearLayout layout) {

        // 1
        this.mDataTop.clear();

        // 2
        int column = initColumn();
        if (column <= 0) {
            mDataTop.addAll(mDatas);
        } else {
            int selected = getSelected();
            int start = selected * column;
            int count = start + column;
            int size = mDatas.size();
            if (count >= size) {
                count = size;
            }
            Log.e("ListRowDoublePresenter", "refreshTemps => selected = " + selected + ", start = " + start + ", count = " + count + ", size = " + size);
            for (int i = start; i < count; ++i) {
                T t = this.mDatas.get(i);
                if (null != t && t.isTop()) {
                    this.mDataTop.add(t);
                }
            }
        }

        // top
        boolean has = false;
        int max = mDataTop.size();
        for (int i = 0; i < max; i++) {
            T t = mDataTop.get(i);
            if (null == t)
                continue;
            boolean selected = t.isSelected();
            if (selected) {
                has = true;
                break;
            }
        }
        if (!has && max > 0) {
            T t = mDataTop.get(0);
            if (null != t) {
                t.setSelected(true);
            }
        }

        try {
            int length = mDataTop.size();
            for (int i = 0; i < column; i++) {
                View child = layout.getChildAt(i);
                if (i + 1 > length) {
                    child.setVisibility(View.INVISIBLE);
                } else {
                    T t = mDataTop.get(i);
                    if (null == t)
                        continue;
                    child.setVisibility(View.VISIBLE);
                    onBindTop(child, t, i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final void refreshSelected(@NonNull int selected) {
        int size = mDataBottom.size();
        for (int i = 0; i < size; i++) {
            T t = mDataBottom.get(i);
            if (null == t)
                continue;
            t.setSelected(i == selected);
        }
    }

    private final int getSelected() {
        int index = 0;
        try {
            int size = mDataBottom.size();
            for (int i = 0; i < size; i++) {
                T t = mDataBottom.get(i);
                if (null == t)
                    continue;
                boolean selected = t.isSelected();
                if (selected) {
                    index = i;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return index;
    }

    private void refreshTop(@NonNull RecyclerView.ViewHolder viewHolder) {

        int adapterPosition = viewHolder.getAbsoluteAdapterPosition();
        int selected = getSelected();
        if (adapterPosition == selected)
            return;

        refreshSelected(adapterPosition);

        ViewGroup viewGroup = (ViewGroup) viewHolder.itemView.getParent().getParent();
        LinearLayout layout = viewGroup.findViewById(R.id.module_leanback_llrd_top);
        refreshTemps(layout);
    }

    private final boolean scrollRight(@NonNull View view) {

        int index = -1;
        try {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            index = viewGroup.indexOfChild(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.e("ListRowDoublePresenter", "scrollRight => index = " + index);
        if (index == -1)
            return false;

        // 1
        int count = mDatas.size();
        int column = initColumn();
        int end = column - 1;
        int selected = getSelected();
        int position = index + selected * column;
//        Log.e("ListRowDoublePresenter", "scrollRight => position = " + position + ", count = " + count + ", end = " + end);
        if (position >= count)
            return false;

        if (position == 0 || position % end != 0)
            return false;

        // 2
        int next = selected + 1;
        refreshSelected(next);

        // 2
        LinearLayout layout = (LinearLayout) view.getParent();
        refreshTemps(layout);

        // 4
        for (int i = 0; i < column; i++) {
            View child = layout.getChildAt(i);
            if (null != child && child.getVisibility() == View.VISIBLE) {
                child.requestFocus();
                break;
            }
        }

        // 3
        try {
            RecyclerView recyclerView = ((ViewGroup) layout.getParent()).findViewById(R.id.module_leanback_llrd_bottom);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private final void focusTop(@NonNull View view) {
//        Toast.makeText(view.getContext(), "focusTop", Toast.LENGTH_SHORT).show();
    }

    private final boolean scrollLeft(@NonNull View view) {

        int index = -1;
        try {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            index = viewGroup.indexOfChild(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (index == -1)
            return false;

        // 1
        int column = initColumn();
        int selected = getSelected();
        int position = index + selected * column;
        if (position == 0 || position % column != 0)
            return false;

        // 2
        int next = selected - 1;
        refreshSelected(next);

        // 2
        LinearLayout layout = (LinearLayout) view.getParent();
        refreshTemps(layout);

        // 4
        int start = column - 1;
        for (int i = start; i >= 0; i--) {
            View child = layout.getChildAt(i);
            if (null != child && child.getVisibility() == View.VISIBLE) {
                child.requestFocus();
                break;
            }
        }

        // 3
        try {
            RecyclerView recyclerView = ((ViewGroup) layout.getParent()).findViewById(R.id.module_leanback_llrd_bottom);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    protected int initColumn() {
        return 10;
    }

    protected int initDecorationTop(@NonNull Context context) {
        return 0;
    }

    protected RecyclerView.ItemDecoration initDecorationBottom() {
        return null;
    }

    protected abstract void onCreateBottom(@NonNull Context context, @NonNull RecyclerView.ViewHolder holder, @NonNull View view, @NonNull List<T> datas, @NonNull boolean isTop);

    protected abstract void onCreateTop(@NonNull Context context, @NonNull View view, @NonNull List<T> datas, @NonNull int position);

    protected abstract void onBindBottom(@NonNull View view, @NonNull T item, @NonNull int position);

    protected abstract void onBindTop(@NonNull View view, @NonNull T item, @NonNull int position);

    @LayoutRes
    protected abstract int initLayoutBottom();

    @LayoutRes
    protected abstract int initLayoutTop();

    @Keep
    public static class ListRowDoubleBean {
        private boolean top = false;
        private String head = null;
        private boolean selected = false;

        public boolean isTop() {
            return top;
        }

        public void setTop(boolean top) {
            this.top = top;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
