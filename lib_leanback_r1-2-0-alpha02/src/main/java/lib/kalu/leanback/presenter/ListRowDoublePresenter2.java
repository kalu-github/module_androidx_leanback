//package lib.kalu.leanback.presenter;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.Keep;
//import androidx.annotation.LayoutRes;
//import androidx.annotation.NonNull;
//import androidx.leanback.R;
//import androidx.leanback.widget.Presenter;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public abstract class ListRowDoublePresenter2<T extends ListRowDoublePresenter2.ListRowDoubleBean> extends Presenter {
//
//    private final List<T> mDatas = new ArrayList();
//    private final List<T> mDataTop = new ArrayList<>();
//    private final List<T> mDataBottom = new ArrayList<>();
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent) {
//        try {
//            Context context = parent.getContext();
//            onLife(context);
//            View view = LayoutInflater.from(context).inflate(R.layout.lb_list_row_double, parent, false);
//            setAdapter(context, view);
//            return new ViewHolder(view);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
//
//        // data
//        try {
//            mDatas.clear();
//            mDataTop.clear();
//            mDataBottom.clear();
//            List<T> list = (List<T>) item;
//            int size = list.size();
//            for (int i = 0; i < size; i++) {
//                T t = list.get(i);
//                if (null == t)
//                    continue;
//                boolean top = t.isTop();
//                if (top) {
//                    mDatas.add(t);
//                } else {
//                    mDataBottom.add(t);
//                }
//            }
//
//            // bottom
//            boolean has = false;
//            int length = mDataBottom.size();
//            for (int i = 0; i < length; i++) {
//                T t = list.get(i);
//                if (null == t)
//                    continue;
//                boolean selected = t.isSelected();
//                if (selected) {
//                    has = true;
//                    break;
//                }
//                t.setSelected(i == 0);
//            }
//            if (!has && length > 0) {
//                T t = mDataBottom.get(0);
//                if (null != t) {
//                    t.setSelected(true);
//                }
//            }
//
//            refreshTemps();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // head
//        try {
//            String head = null;
//            int size = mDataTop.size();
//            for (int i = 0; i < size; i++) {
//                T t = mDataTop.get(i);
//                if (null == t) {
//                    continue;
//                }
//                String str = t.getHead();
//                if (null != str && str.length() > 0) {
//                    head = str;
//                    break;
//                }
//            }
//            if (null != head && head.length() > 0) {
//                TextView textView = viewHolder.view.findViewById(R.id.module_leanback_llrd_header);
//                textView.setText(head);
//                textView.setVisibility(View.VISIBLE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // list1
//        try {
//            RecyclerView recyclerView = viewHolder.view.findViewById(R.id.module_leanback_llrd_list1);
//            recyclerView.getAdapter().notifyDataSetChanged();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // list2
//        try {
//            RecyclerView recyclerView = viewHolder.view.findViewById(R.id.module_leanback_llrd_list2);
//            recyclerView.getAdapter().notifyDataSetChanged();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onUnbindViewHolder(ViewHolder viewHolder) {
//    }
//
//    private final void setAdapter(@NonNull Context context, @NonNull View root) {
//
//        // list1
//        try {
//            RecyclerView recyclerView = root.findViewById(R.id.module_leanback_llrd_list1);
//            if (null == recyclerView.getLayoutManager()) {
//                GridLayoutManager manager = new GridLayoutManager(context, initColumn()) {
//                    @Override
//                    public boolean canScrollVertically() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean canScrollHorizontally() {
//                        return false;
//                    }
//                };
//                manager.setOrientation(LinearLayoutManager.VERTICAL);
//                RecyclerView.ItemDecoration itemDecoration = initItemDecoration();
//                if (null != itemDecoration) {
//                    recyclerView.addItemDecoration(itemDecoration);
//                }
//                recyclerView.setLayoutManager(manager);
//            }
//            if (null == recyclerView.getAdapter()) {
//                recyclerView.setAdapter(new RecyclerView.Adapter() {
//                    @NonNull
//                    @Override
//                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        try {
//                            Context context = parent.getContext();
//                            onLife(context);
//                            View view = LayoutInflater.from(context).inflate(initLayout(viewType, true), parent, false);
//                            RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {
//                            };
//                            onCreateHolder(context, holder, view, mDataTop, true);
//                            return holder;
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            return null;
//                        }
//                    }
//
//                    @Override
//                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//                        try {
//                            T t = mDataTop.get(position);
//                            onBindHolder(holder.itemView, t, position, true);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public int getItemCount() {
//                        return mDataTop.size();
//                    }
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        // list2
//        try {
//            RecyclerView recyclerView = root.findViewById(R.id.module_leanback_llrd_list2);
//            if (null == recyclerView.getLayoutManager()) {
//                LinearLayoutManager manager = new LinearLayoutManager(context);
//                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//                RecyclerView.ItemDecoration itemDecoration = initItemDecoration();
//                if (null != itemDecoration) {
//                    recyclerView.addItemDecoration(itemDecoration);
//                }
//                recyclerView.setLayoutManager(manager);
//            }
//            if (null == recyclerView.getAdapter()) {
//                recyclerView.setAdapter(new RecyclerView.Adapter() {
//                    @NonNull
//                    @Override
//                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        try {
//                            Context context = parent.getContext();
//                            onLife(context);
//                            View view = LayoutInflater.from(context).inflate(initLayout(viewType, false), parent, false);
//                            RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {
//                            };
//                            onCreateHolder(context, holder, view, mDataBottom, false);
//                            return holder;
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            return null;
//                        }
//                    }
//
//                    @Override
//                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//                        try {
//                            T t = mDataBottom.get(position);
//                            onBindHolder(holder.itemView, t, position, false);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public int getItemCount() {
//                        return mDataBottom.size();
//                    }
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private final void refreshTemps() {
//
//        // 1
//        this.mDataTop.clear();
//
//        // 2
//        int column = initColumn();
//        if (column <= 0) {
//            mDataTop.addAll(mDatas);
//        } else {
//            int selected = getSelected();
//            int start = selected * column;
//            int count = start + column;
//            int size = mDatas.size();
//            if (count >= size) {
//                count = size;
//            }
//            Log.e("ListRowDoublePresenter", "refreshTemps => selected = " + selected + ", start = " + start + ", count = " + count + ", size = " + size);
//            for (int i = start; i < count; ++i) {
//                T t = this.mDatas.get(i);
//                if (null != t && t.isTop()) {
//                    this.mDataTop.add(t);
//                }
//            }
//        }
//
//        // top
//        boolean has = false;
//        int max = mDataTop.size();
//        for (int i = 0; i < max; i++) {
//            T t = mDataTop.get(i);
//            if (null == t)
//                continue;
//            boolean selected = t.isSelected();
//            if (selected) {
//                has = true;
//                break;
//            }
//        }
//        if (!has && max > 0) {
//            T t = mDataTop.get(0);
//            if (null != t) {
//                t.setSelected(true);
//            }
//        }
//    }
//
//    private final int getSelected() {
//        // 1
//        int position = 0;
//        try {
//            int size = mDataBottom.size();
//            for (int i = 0; i < size; i++) {
//                T t = mDataBottom.get(i);
//                if (null == t)
//                    continue;
//                boolean selected = t.isSelected();
//                if (selected) {
//                    position = i;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return position;
//    }
//
//    private final void refreshSelected(@NonNull int selected) {
//        int size = mDataBottom.size();
//        for (int i = 0; i < size; i++) {
//            T t = mDataBottom.get(i);
//            if (null == t)
//                continue;
//            t.setSelected(i == selected);
//        }
//    }
//
//    protected final void refreshBottom(@NonNull RecyclerView.ViewHolder viewHolder) {
//
//        // 1
//        int adapterPosition = viewHolder.getAbsoluteAdapterPosition();
//        int selected = getSelected();
//        Log.e("ListRowDoublePresenter", "refreshBottom => selected = " + selected + ", adapterPosition = " + adapterPosition);
//        if (adapterPosition == selected)
//            return;
//        refreshSelected(adapterPosition);
//
//        // 2
//        refreshTemps();
//
//        // 3
//        try {
//            ViewGroup viewGroup = (ViewGroup) viewHolder.itemView.getParent().getParent();
//            RecyclerView recyclerView = viewGroup.findViewById(R.id.module_leanback_llrd_list1);
//            recyclerView.getAdapter().notifyDataSetChanged();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
////    protected final void refreshTop(@NonNull RecyclerView.ViewHolder viewHolder) {
////
////        // 1
////        int adapterPosition = viewHolder.getAbsoluteAdapterPosition();
////        int selected = getSelected();
////        Log.e("ListRowDoublePresenter", "refresh => position = " + position + ", adapterPosition = " + adapterPosition);
////        if (adapterPosition == selected)
////            return;
////        refreshPosition(adapterPosition);
////
////        // 2
////        refreshTemps();
////
////        // 3
////        try {
////            ViewGroup viewGroup = (ViewGroup) viewHolder.itemView.getParent().getParent();
////            RecyclerView recyclerView = viewGroup.findViewById(R.id.module_leanback_llrd_list1);
////            recyclerView.getAdapter().notifyDataSetChanged();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//
//    protected final boolean scrollRightTop(@NonNull RecyclerView.ViewHolder viewHolder) {
//
//        // 1
//        int adapterPosition = viewHolder.getAbsoluteAdapterPosition();
//        int count = mDatas.size();
//        int column = initColumn();
//        int end = column - 1;
//        int selected = getSelected();
//        int position = adapterPosition + selected * column;
//        if (position >= count)
//            return false;
//
//        if (position == 0 || position % end != 0)
//            return false;
//
//        // 2
//        int next = selected + 1;
//        refreshSelected(next);
//
//        // 2
//        refreshTemps();
//
//        // 3
//        try {
//            RecyclerView recyclerView1 = (RecyclerView) viewHolder.itemView.getParent();
//            recyclerView1.getAdapter().notifyDataSetChanged();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // 4
//        try {
//            RecyclerView recyclerView2 = ((ViewGroup) viewHolder.itemView.getParent().getParent()).findViewById(R.id.module_leanback_llrd_list2);
//            recyclerView2.getAdapter().notifyDataSetChanged();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return true;
//    }
//
//
//    protected final void scrollRightBottom(@NonNull RecyclerView.ViewHolder viewHolder, boolean request) {
//
//        // 1
//        int adapterPosition = viewHolder.getAbsoluteAdapterPosition();
//        int position = adapterPosition + 1;
//        int size = mDataBottom.size();
//        if (position >= size)
//            return;
//
//        // 2
//        refreshSelected(position);
//
//        // 2
//        refreshTemps();
//
//        // 3
//        try {
//            RecyclerView recyclerView1 = (RecyclerView) viewHolder.itemView.getParent();
//            recyclerView1.getAdapter().notifyDataSetChanged();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // 4
//        try {
//            RecyclerView recyclerView2 = ((ViewGroup) viewHolder.itemView.getParent().getParent()).findViewById(R.id.module_leanback_llrd_list2);
//            recyclerView2.getAdapter().notifyDataSetChanged();
//            if (request) {
//                if (request) {
//                    recyclerView2.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            recyclerView2.scrollToPosition(position);
//                            recyclerView2.getLayoutManager().findViewByPosition(position).requestFocus();
//                        }
//                    }, 40);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected final boolean scrollLeftTop(@NonNull RecyclerView.ViewHolder viewHolder) {
//
//        // 1
//        int adapterPosition = viewHolder.getAbsoluteAdapterPosition();
//        int column = initColumn();
//        int selected = getSelected();
//        int position = adapterPosition + selected * column;
//        if (position == 0 || position % column != 0)
//            return false;
//
//        // 2
//        int next = selected - 1;
//        refreshSelected(next);
//
//        // 2
//        refreshTemps();
//
//        // 3
//        try {
//            RecyclerView recyclerView1 = (RecyclerView) viewHolder.itemView.getParent();
//            recyclerView1.getAdapter().notifyDataSetChanged();
//            recyclerView1.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    int index = column - 1;
//                    recyclerView1.scrollToPosition(index);
//                    recyclerView1.getLayoutManager().findViewByPosition(index).requestFocus();
//                }
//            }, 40);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // 4
//        try {
//            RecyclerView recyclerView2 = ((ViewGroup) viewHolder.itemView.getParent().getParent()).findViewById(R.id.module_leanback_llrd_list2);
//            recyclerView2.getAdapter().notifyDataSetChanged();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return true;
//    }
//
//    protected final void scrollLeftBottom(@NonNull RecyclerView.ViewHolder viewHolder, boolean request) {
//
//        // 1
//        int adapterPosition = viewHolder.getAbsoluteAdapterPosition();
//        if (adapterPosition <= 0)
//            return;
//
//        // 2
//        int position = adapterPosition - 1;
//        refreshSelected(position);
//
//        // 2
//        refreshTemps();
//
//        // 3
//        try {
//            RecyclerView recyclerView1 = (RecyclerView) viewHolder.itemView.getParent();
//            recyclerView1.getAdapter().notifyDataSetChanged();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // 4
//        try {
//            RecyclerView recyclerView2 = ((ViewGroup) viewHolder.itemView.getParent().getParent()).findViewById(R.id.module_leanback_llrd_list2);
//            recyclerView2.getAdapter().notifyDataSetChanged();
//            if (request) {
//                recyclerView2.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        recyclerView2.scrollToPosition(position);
//                        recyclerView2.getLayoutManager().findViewByPosition(position).requestFocus();
//                    }
//                }, 40);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected void onLife(@NonNull Context context) {
//    }
//
//    protected int initColumn() {
//        return 0;
//    }
//
//    protected RecyclerView.ItemDecoration initItemDecoration() {
//        return null;
//    }
//
//    protected abstract void onCreateHolder(@NonNull Context context, @NonNull RecyclerView.ViewHolder holder, @NonNull View view, @NonNull List<T> datas, @NonNull boolean isTop);
//
//    protected abstract void onBindHolder(@NonNull View view, @NonNull T item, @NonNull int position, @NonNull boolean isTop);
//
//    @LayoutRes
//    protected abstract int initLayout(@NonNull int viewType, @NonNull boolean isTop);
//
//    @Keep
//    public static class ListRowDoubleBean {
//        private boolean top = false;
//        private String head = null;
//        private boolean selected = false;
//
//        public boolean isTop() {
//            return top;
//        }
//
//        public void setTop(boolean top) {
//            this.top = top;
//        }
//
//        public String getHead() {
//            return head;
//        }
//
//        public void setHead(String head) {
//            this.head = head;
//        }
//
//        public boolean isSelected() {
//            return selected;
//        }
//
//        public void setSelected(boolean selected) {
//            this.selected = selected;
//        }
//    }
//}
