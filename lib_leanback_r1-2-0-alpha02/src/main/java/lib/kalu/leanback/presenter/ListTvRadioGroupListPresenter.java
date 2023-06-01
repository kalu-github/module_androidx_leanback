package lib.kalu.leanback.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import lib.kalu.leanback.list.RecyclerViewVertical;
import lib.kalu.leanback.list.layoutmanager.BaseLinearLayoutManager;
import lib.kalu.leanback.presenter.bean.TvPresenterRowBean;
import lib.kalu.leanback.presenter.impl.ListTvPresenterImpl;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvRadioGroupListPresenter<T extends TvPresenterRowBean> extends Presenter implements ListTvPresenterImpl {

    private final List<List<T>> mCollection = new LinkedList<>();
    private final List<T> mData = new LinkedList<>();
    private int playerPosition = -1;

    /**************/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            ViewGroup inflate = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.lb_list_tv_radio_group_list, parent, false);
            setPadding(context, inflate);
            setBackgroundColor(context, inflate);
            setContentBackgroundColor(context, inflate, R.id.module_leanback_lrgl_list);
            setTitlePadding(context, inflate, R.id.module_leanback_lrgl_title);
            setTitleTextColor(context, inflate, R.id.module_leanback_lrgl_title);
            setTitleTextSize(context, inflate, R.id.module_leanback_lrgl_title);
            setTitleAssetTTF(context, inflate, R.id.module_leanback_lrgl_title);
            setTitleBackgroundColor(context, inflate, R.id.module_leanback_lrgl_title);
            initContent(context, inflate);
            initRadioGroup(context, inflate);
            initAdapter(context, inflate);
            return new ViewHolder(inflate);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => onCreateViewHolder => " + e.getMessage());
            return null;
        }
    }

    private void initContent(Context context, View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            ViewGroup viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_contont);
            if (null == viewById)
                throw new Exception("viewById error: null");
            int childCount = viewById.getChildCount();
            if (childCount > 0)
                throw new Exception("childCount warning: " + childCount);
            LayoutInflater.from(context).inflate(initLayoutContent(), viewById, true);
            onCreateHolderContent(viewGroup, viewById);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => initContent => " + e.getMessage());
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        // data
        try {
            if (null == item)
                throw new Exception("item error: null");
            if (!(item instanceof List))
                throw new Exception("item error: not instanceof List[]");
            List<List<T>> datas = (List<List<T>>) item;
            int length = datas.size();
            if (length <= 0)
                throw new Exception("length error: " + length);
            for (int i = 0; i < length; i++) {
                List<T> list = new LinkedList<>();
                list.addAll(datas.get(i));
                mCollection.add(list);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => onBindViewHolder => " + e.getMessage());
        }

        // 2
        switchData(viewHolder.view, 0, false);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    /**************/

    private void switchData(View viewGroup, int position, boolean isFromUser) {
        // update
        try {
            if (position < 0)
                throw new Exception("checkedIndex error: " + position);
            if (null == mCollection)
                throw new Exception("mMap error: null");
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            RecyclerViewVertical recyclerView = viewGroup.findViewById(R.id.module_leanback_lrgl_list);
            if (null == recyclerView)
                throw new Exception("recyclerView error: null");
            int size1 = mData.size();
            mData.clear();
            playerPosition = -1;
            recyclerView.getAdapter().notifyItemRangeRemoved(0, size1);
            int size = mCollection.size();
            if (position + 1 > size)
                throw new Exception("size error: " + size);
            List<T> list = mCollection.get(position);
            if (null == list || list.size() <= 0)
                throw new Exception("list error: " + list);
            mData.addAll(list);
            recyclerView.getAdapter().notifyItemRangeChanged(0, mData.size());
            View viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_contont);
            playerPosition = 0;
            onBindHolderContent(viewGroup, viewById, 0, mData.get(0), isFromUser);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => switchData => " + e.getMessage());
        }
    }

    private final void initRadioGroup(Context context, View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            ViewGroup headGroup = viewGroup.findViewById(R.id.module_leanback_lrgl_radio);
            if (null == headGroup)
                throw new Exception("headGroup error: null");
            int childCount = headGroup.getChildCount();
            if (childCount > 0)
                throw new Exception("childCount warning: " + childCount);
            int[] headMargin = initMarginRadioGroup(context);
            if (null != headMargin && headMargin.length == 4) {
                ViewGroup.LayoutParams layoutParams = headGroup.getLayoutParams();
                if (null != layoutParams) {
                    ((RelativeLayout.LayoutParams) layoutParams).leftMargin = headMargin[0];
                    ((RelativeLayout.LayoutParams) layoutParams).topMargin = headMargin[1];
                    ((RelativeLayout.LayoutParams) layoutParams).rightMargin = headMargin[2];
                    ((RelativeLayout.LayoutParams) layoutParams).bottomMargin = headMargin[3];
                }
            }
            int[] headPadding = initPaddingRadioGroup(context);
            if (null != headPadding && headPadding.length == 4) {
                headGroup.setPadding(headPadding[0], headPadding[1], headPadding[2], headPadding[3]);
            }
            LayoutInflater.from(context).inflate(initLayoutRadioGroup(), headGroup, true);
            onCreateHolderRadioGroup(viewGroup, headGroup);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => initRadioGroup => " + e.getMessage());
        }
    }

    private final void initAdapter(Context context, View viewGroup) {
        try {
            RecyclerView recyclerView = viewGroup.findViewById(R.id.module_leanback_lrgl_list);
            if (null == recyclerView)
                throw new Exception("recyclerView error: null");
            int[] itemMargin = initMarginItem(context);
            if (null != itemMargin && itemMargin.length == 4) {
                ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                if (null != layoutParams) {
                    ((RelativeLayout.LayoutParams) layoutParams).leftMargin = itemMargin[0];
                    ((RelativeLayout.LayoutParams) layoutParams).topMargin = itemMargin[1];
                    ((RelativeLayout.LayoutParams) layoutParams).rightMargin = itemMargin[2];
                    ((RelativeLayout.LayoutParams) layoutParams).bottomMargin = itemMargin[3];
                }
            }
            int[] itemPadding = initPaddingItem(context);
            if (null != itemPadding && itemPadding.length == 4) {
                recyclerView.setPadding(itemPadding[0], itemPadding[1], itemPadding[2], itemPadding[3]);
            }
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (null == layoutManager) {
                BaseLinearLayoutManager manager = new BaseLinearLayoutManager(context);
                manager.setOrientation(BaseLinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(manager);
            }
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (null == adapter) {
                recyclerView.setAdapter(new RecyclerView.Adapter() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        try {
                            View view = LayoutInflater.from(parent.getContext()).inflate(initLayoutItem(), parent, false);
                            RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {
                            };
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        int position = holder.getAbsoluteAdapterPosition();
                                        if (position < 0)
                                            throw new Exception("position error: " + position);
                                        View viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_contont);
                                        playerPosition = position;
                                        onBindHolderContent(viewGroup, viewById, position, mData.get(position), true);
                                    } catch (Exception e) {
                                        LeanBackUtil.log("ListTvRadioGroupListPresenter => initAdapter => onClick => " + e.getMessage());
                                    }
                                }
                            });
                            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    try {
                                        int position = holder.getAbsoluteAdapterPosition();
                                        if (playerPosition == -1 && position <= 0)
                                            throw new Exception("position error: " + position);
                                        if (position == playerPosition)
                                            throw new Exception("playerPosition warning: " + playerPosition);
                                        if (!hasFocus)
                                            throw new Exception("hasFocus warning: false");
                                        playerPosition = position;
                                        View viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_contont);
                                        onBindHolderContent(viewGroup, viewById, position, mData.get(position), true);
                                    } catch (Exception e) {
                                        LeanBackUtil.log("ListTvRadioGroupListPresenter => initAdapter => onFocusChange => " + e.getMessage());
                                    }
                                }
                            });
                            onCreateHolderItem(holder, mData);
                            return holder;
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvRadioGroupListPresenter => initAdapter => onCreateViewHolder => " + e.getMessage(), e);
                            return null;
                        }
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        try {
                            if (position < 0)
                                throw new Exception("position error: " + position);
                            T t = mData.get(position);
                            if (null == t)
                                throw new Exception("t error: null");
                            onBindHolderItem(holder, position, t);
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvRadioGroupListPresenter => initAdapter => onBindViewHolder => " + e.getMessage(), e);
                        }
                    }

                    @Override
                    public int getItemCount() {
                        return mData.size();
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => initAdapter => " + e.getMessage(), e);
        }
    }

    /**************/

    @LayoutRes
    protected abstract int initLayoutItem();

    @LayoutRes
    protected abstract int initLayoutContent();

    @LayoutRes
    protected abstract int initLayoutRadioGroup();

    /**************/

    protected void onCreateHolderItem(@NonNull RecyclerView.ViewHolder holder, List<T> list) {
    }

    protected void onBindHolderItem(@NonNull RecyclerView.ViewHolder holder, int position, T t) {
    }

    protected void onCreateHolderContent(@NonNull View rootGroup, @NonNull View contentGroup) {
    }

    protected void onBindHolderContent(@NonNull View rootGroup, @NonNull View contentGroup, int position, T t, boolean isFromUser) {
    }

    protected void onCreateHolderRadioGroup(@NonNull View rootGroup, @NonNull View headGroup) {
    }

    /**************/

    protected int[] initPaddingRadioGroup(@NonNull Context context) {
        return null;
    }

    protected int[] initMarginRadioGroup(@NonNull Context context) {
        return null;
    }

    protected int[] initMarginItem(@NonNull Context context) {
        return null;
    }

    protected int[] initPaddingItem(@NonNull Context context) {
        return null;
    }

    /**************/

    public void startFull(View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            ViewGroup viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_radio);
            if (null == viewById)
                throw new Exception("viewById error: null");
            viewById.setVisibility(View.GONE);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => startFull => " + e.getMessage());
        }
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            ViewGroup viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_list);
            if (null == viewById)
                throw new Exception("viewById error: null");
            viewById.setVisibility(View.GONE);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => startFull => " + e.getMessage());
        }
    }

    public void clearFull(View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            ViewGroup viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_radio);
            if (null == viewById)
                throw new Exception("viewById error: null");
            viewById.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => clearFull => " + e.getMessage());
        }
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            ViewGroup viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_list);
            if (null == viewById)
                throw new Exception("viewById error: null");
            viewById.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => clearFull => " + e.getMessage());
        }
    }

    public void switchPlayerUp(@NonNull View viewGroup) {
        try {
            RecyclerViewVertical recyclerView = viewGroup.findViewById(R.id.module_leanback_lrgl_list);
            if (null == recyclerView)
                throw new Exception("recyclerView error: null");
            int itemCount = recyclerView.getAdapterItemCount();
            if (itemCount <= 0)
                throw new Exception("itemCount error: " + itemCount);
            if (playerPosition <= 0)
                throw new Exception("up error: not next");
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(playerPosition);
            int measuredHeight = viewHolder.itemView.getMeasuredHeight();
            recyclerView.scrollBy(0, -measuredHeight);
            View viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_contont);
            playerPosition = playerPosition - 1;
            onBindHolderContent(viewGroup, viewById, playerPosition, mData.get(playerPosition), true);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => switchPlayerUp => " + e.getMessage());
        }
    }

    public void switchPlayerDown(@NonNull View viewGroup) {
        try {
            RecyclerViewVertical recyclerView = viewGroup.findViewById(R.id.module_leanback_lrgl_list);
            if (null == recyclerView)
                throw new Exception("recyclerView error: null");
            int itemCount = recyclerView.getAdapterItemCount();
            if (itemCount <= 0)
                throw new Exception("itemCount error: " + itemCount);
            if (playerPosition + 1 >= itemCount)
                throw new Exception("down error: not next");
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(playerPosition);
            int measuredHeight = viewHolder.itemView.getMeasuredHeight();
            recyclerView.scrollBy(0, measuredHeight);
            View viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_contont);
            playerPosition = playerPosition + 1;
            onBindHolderContent(viewGroup, viewById, playerPosition, mData.get(playerPosition), true);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => switchPlayerDown => " + e.getMessage());
        }
    }

    public void switchData(View viewGroup, int position) {
        switchData(viewGroup, position, true);
    }

    public View findViewForAdapterPosition(View viewGroup, int position) {
        try {
            RecyclerViewVertical recyclerView = viewGroup.findViewById(R.id.module_leanback_lrgl_list);
            if (null == recyclerView)
                throw new Exception("recyclerView error: null");
            int itemCount = recyclerView.getAdapterItemCount();
            if (itemCount <= 0)
                throw new Exception("itemCount error: " + itemCount);
            if (position + 1 > itemCount)
                throw new Exception("down error: not next");
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
            if (null == viewHolder)
                throw new Exception("viewHolder error: null");
            if (null == viewHolder.itemView)
                throw new Exception("viewHolder.itemView error: null");
            return viewHolder.itemView;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => getViewItemForPosition => " + e.getMessage());
            return null;
        }
    }
}