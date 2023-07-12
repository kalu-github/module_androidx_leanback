package lib.kalu.leanback.presenter;

import android.content.Context;
import android.graphics.Rect;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import lib.kalu.leanback.list.RecyclerViewVertical;
import lib.kalu.leanback.list.layoutmanager.BaseLinearLayoutManager;
import lib.kalu.leanback.presenter.bean.TvPresenterRowBean;
import lib.kalu.leanback.presenter.bean.TvRadioGroupItemBean;
import lib.kalu.leanback.presenter.impl.ListTvPresenterImpl;
import lib.kalu.leanback.radio.RadioGroupHorizontal;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvRadioGroupListPresenter<T extends TvRadioGroupItemBean> extends Presenter implements ListTvPresenterImpl {

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
            initBackground(context, inflate);
            initRadioGroup(context, inflate);
            initAdapter(context, inflate);
            return new ViewHolder(inflate);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => onCreateViewHolder => " + e.getMessage());
            return null;
        }
    }

    private void initBackground(Context context, View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            ViewGroup viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_contont);
            if (null == viewById)
                throw new Exception("viewById error: null");
            int childCount = viewById.getChildCount();
            if (childCount > 0)
                throw new Exception("childCount warning: " + childCount);
            LayoutInflater.from(context).inflate(initLayoutBackground(), viewById, true);
            onCreateHolderBackground(viewGroup, viewById);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => initBackground => " + e.getMessage());
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
            for (T t : list) {
                if (null == t)
                    continue;
                t.setChecked(false);
                mData.add(t);
            }
            mData.get(0).setChecked(true);
            recyclerView.getAdapter().notifyItemRangeInserted(0, mData.size());
            View viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_contont);
            playerPosition = 0;
            onBindHolderBackground(viewGroup, viewById, 0, mData.get(0), isFromUser, false);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => switchData => " + e.getMessage());
        }
    }

    private void initRadioGroup(Context context, View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            RadioGroup radioGroup = viewGroup.findViewById(R.id.module_leanback_lrgl_radio);
            if (null == radioGroup)
                throw new Exception("headGroup error: null");
            int childCount = radioGroup.getChildCount();
            if (childCount > 0)
                throw new Exception("childCount warning: " + childCount);
            int[] headMargin = initMarginRadioGroup(context);
            if (null != headMargin && headMargin.length == 4) {
                ViewGroup.LayoutParams layoutParams = radioGroup.getLayoutParams();
                if (null != layoutParams) {
                    ((RelativeLayout.LayoutParams) layoutParams).leftMargin = headMargin[0];
                    ((RelativeLayout.LayoutParams) layoutParams).topMargin = headMargin[1];
                    ((RelativeLayout.LayoutParams) layoutParams).rightMargin = headMargin[2];
                    ((RelativeLayout.LayoutParams) layoutParams).bottomMargin = headMargin[3];
                }
            }
            int[] headPadding = initPaddingRadioGroup(context);
            if (null != headPadding && headPadding.length == 4) {
                radioGroup.setPadding(headPadding[0], headPadding[1], headPadding[2], headPadding[3]);
            }
            LayoutInflater.from(context).inflate(initLayoutRadioGroup(), radioGroup, true);
            int count = radioGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View childAt = radioGroup.getChildAt(i);
                if (null == childAt)
                    continue;
                childAt.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                            int itemCount = getItemCount();
                            if (itemCount <= 0)
                                return true;
                        }
                        return false;
                    }
                });
            }
            onCreateHolderRadioGroup(viewGroup, radioGroup);
        } catch (
                Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => initRadioGroup => " + e.getMessage());
        }

    }

    private void initAdapter(Context context, View viewGroup) {
        try {
            RecyclerView recyclerView = viewGroup.findViewById(R.id.module_leanback_lrgl_list);
            if (null == recyclerView)
                throw new Exception("recyclerView error: null");
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    initItemOffsets(outRect, view, parent, state);
                }
            });
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
                BaseLinearLayoutManager manager = new BaseLinearLayoutManager(context) {
                };
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
                                        if (playerPosition != position) {
                                            onSwitchHolderItem(viewGroup, playerPosition, position);
                                            playerPosition = position;
                                            onBindHolderBackground(viewGroup, viewById, position, mData.get(position), true, false);
                                        } else {
                                            onBindHolderBackground(viewGroup, viewById, position, mData.get(position), true, true);
                                        }
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
                                        if (position == playerPosition) {
                                            if (!hasFocus && playerPosition > 0) {
                                                T t = mData.get(position);
                                                t.setChecked(false);
                                                onBindHolderItem(holder, position, t);
                                            }
                                            throw new Exception("playerPosition warning: " + playerPosition + ", hasFocus = " + hasFocus);
                                        } else {
                                            if (!hasFocus)
                                                throw new Exception("hasFocus warning: false");
                                            T t = mData.get(position);
                                            t.setChecked(true);
                                            onBindHolderItem(holder, position, t);
                                            onSwitchHolderItem(viewGroup, playerPosition, position);
                                            playerPosition = position;
                                            View viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_contont);
                                            onBindHolderBackground(viewGroup, viewById, position, mData.get(position), true, false);
                                        }
                                    } catch (Exception e) {
                                        LeanBackUtil.log("ListTvRadioGroupListPresenter => initAdapter => onFocusChange => " + e.getMessage());
                                    }
                                }
                            });

                            onCreateHolderItem(holder, mData);
                            holder.itemView.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                                    // action_down => keycode_dpad_left
                                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                                        int checkedIndex = getCheckedIndexFromRadioGroup(viewGroup);
                                        if (checkedIndex > 0) {
                                            setCheckedIndexFromRadioGroup(viewGroup, checkedIndex - 1);
                                            return true;
                                        }
                                    }
                                    // action_down => keycode_dpad_right
                                    else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                                        int checkedIndex = getCheckedIndexFromRadioGroup(viewGroup);
                                        int childCount = getChildCountFromRadioGroup(viewGroup);
                                        if (checkedIndex + 1 < childCount) {
                                            setCheckedIndexFromRadioGroup(viewGroup, checkedIndex + 1);
                                            return true;
                                        }
                                    }
                                    // action_down => keycode_dpad_down
                                    else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                                        try {
                                            int position = holder.getAbsoluteAdapterPosition();
                                            if (position < 0)
                                                throw new Exception("position error: " + position);
                                            if (position + 1 >= mData.size()) {
                                                onEnd(viewGroup);
                                                throw new Exception("position error: " + position + ", count error: " + mData.size());
                                            }
                                            T t = mData.get(position);
                                            t.setChecked(false);
                                            onBindHolderItem(holder, position, t);
                                        } catch (Exception e) {
                                            LeanBackUtil.log("ListTvRadioGroupListPresenter => initAdapter => onKey => " + e.getMessage());
                                        }
                                    }
                                    // action_down => keycode_dpad_up
                                    else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                                        try {
                                            int position = holder.getAbsoluteAdapterPosition();
                                            if (position < 0)
                                                throw new Exception("position error: " + position);
                                            if (position + 1 >= mData.size())
                                                throw new Exception("position error: " + position + ", count error: " + mData.size());
                                            T t = mData.get(position);
                                            t.setChecked(false);
                                            onBindHolderItem(holder, position, t);
                                        } catch (Exception e) {
                                            LeanBackUtil.log("ListTvRadioGroupListPresenter => initAdapter => onKey => " + e.getMessage());
                                        }
                                    }
                                    return false;
                                }
                            });
                            return holder;
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvRadioGroupListPresenter => initAdapter => onCreateViewHolder => " + e.getMessage(), e);
                            return null;
                        }
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                                 int position) {
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
    protected abstract int initLayoutBackground();

    @LayoutRes
    protected abstract int initLayoutRadioGroup();

    /**************/

    protected void onCreateHolderItem(@NonNull RecyclerView.ViewHolder holder, List<T> list) {
    }

    protected void onBindHolderItem(@NonNull RecyclerView.ViewHolder holder, int position, T t) {
    }

    protected void onCreateHolderBackground(@NonNull View rootGroup, @NonNull View contentGroup) {
    }

    protected void onBindHolderBackground(@NonNull View rootGroup, @NonNull View contentGroup, int position, T t, boolean isFromUser, boolean isPlaying) {
    }

    protected void onCreateHolderRadioGroup(@NonNull View rootGroup, @NonNull RadioGroup radioGroup) {
    }

    protected void onSwitchHolderItem(@NonNull View rootGroup, int oldPosition, int newPosition) {
    }

    protected void onEnd(@NonNull View rootGroup) {
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

    public boolean switchUp(@NonNull View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
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
            T t1 = mData.get(playerPosition);
            t1.setChecked(false);
            onBindHolderItem(viewHolder, playerPosition, t1);
            playerPosition = playerPosition - 1;
            RecyclerView.ViewHolder viewHolderCur = recyclerView.findViewHolderForAdapterPosition(playerPosition);
            T t2 = mData.get(playerPosition);
            t2.setChecked(true);
            onBindHolderItem(viewHolderCur, playerPosition, t2);
            View viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_contont);
            onBindHolderBackground(viewGroup, viewById, playerPosition, mData.get(playerPosition), true, false);
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => switchUp => " + e.getMessage());
            return false;
        }
    }

    public boolean switchDown(@NonNull View viewGroup) {
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
            T t1 = mData.get(playerPosition);
            t1.setChecked(false);
            onBindHolderItem(viewHolder, playerPosition, t1);
            playerPosition = playerPosition + 1;
            RecyclerView.ViewHolder viewHolderCur = recyclerView.findViewHolderForAdapterPosition(playerPosition);
            T t2 = mData.get(playerPosition);
            t2.setChecked(true);
            onBindHolderItem(viewHolderCur, playerPosition, t2);
            View viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_contont);
            onBindHolderBackground(viewGroup, viewById, playerPosition, mData.get(playerPosition), true, false);
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => switchDown => " + e.getMessage());
            return false;
        }
    }

    public boolean setBindHolderBackgroundForAdapterChecked(@NonNull View viewGroup) {
        try {
            RecyclerViewVertical recyclerView = viewGroup.findViewById(R.id.module_leanback_lrgl_list);
            if (null == recyclerView)
                throw new Exception("recyclerView error: null");
            int itemCount = recyclerView.getAdapterItemCount();
            if (itemCount <= 0)
                throw new Exception("itemCount error: " + itemCount);
            if (playerPosition + 1 > itemCount)
                throw new Exception("playerPosition error: " + playerPosition);
            View viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_contont);
            onBindHolderBackground(viewGroup, viewById, playerPosition, mData.get(playerPosition), true, false);
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => setBindHolderBackgroundForAdapterChecked => " + e.getMessage());
            return false;
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

    private int getCheckedIndexFromRadioGroup(@NonNull View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("parent error: null");
            if (viewGroup.getId() != R.id.module_leanback_lep_root)
                throw new Exception("id error: != R.id.module_leanback_lep_root");
            RadioGroupHorizontal radioGroup = viewGroup.findViewById(R.id.module_leanback_lrgl_radio);
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int childCount = radioGroup.getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            for (int i = 0; i < childCount; i++) {
                View childAt = radioGroup.getChildAt(i);
                if (null == childAt)
                    continue;
                if (!(childAt instanceof RadioButton))
                    continue;
                boolean checked = ((RadioButton) childAt).isChecked();
                if (checked)
                    return i;
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => getCheckedIndexFromRadioGroup => " + e.getMessage());
            return -1;
        }
    }

    private int getChildCountFromRadioGroup(@NonNull View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("parent error: null");
            if (viewGroup.getId() != R.id.module_leanback_lep_root)
                throw new Exception("id error: != R.id.module_leanback_lep_root");
            RadioGroupHorizontal radioGroup = viewGroup.findViewById(R.id.module_leanback_lrgl_radio);
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int childCount = radioGroup.getChildCount();
            return childCount;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => getChildCountFromRadioGroup => " + e.getMessage());
            return 0;
        }
    }

    private void setCheckedIndexFromRadioGroup(@NonNull View viewGroup, int index) {
        try {
            if (null == viewGroup)
                throw new Exception("parent error: null");
            if (viewGroup.getId() != R.id.module_leanback_lep_root)
                throw new Exception("id error: != R.id.module_leanback_lep_root");
            RadioGroupHorizontal radioGroup = viewGroup.findViewById(R.id.module_leanback_lrgl_radio);
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int childCount = radioGroup.getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount error: " + childCount);
            for (int i = 0; i < childCount; i++) {
                View childAt = radioGroup.getChildAt(i);
                if (null == childAt)
                    continue;
                if (!(childAt instanceof RadioButton))
                    continue;
                ((RadioButton) childAt).setChecked(i == index);
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => setCheckedIndexFromRadioGroup => " + e.getMessage());
        }
    }

    public T getDataForPlaying() {
        try {
            if (playerPosition < 0)
                throw new Exception("playerPosition error: " + playerPosition);
            int size = mData.size();
            if (playerPosition + 1 > size)
                throw new Exception("playerPosition error: " + playerPosition + ", size error: " + size);
            return getDataForAdapterPosition(playerPosition);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => getDataForPlaying => " + e.getMessage());
            return null;
        }
    }

    public T getDataForAdapterPosition(int index) {
        try {
            if (index < 0)
                throw new Exception("index error: " + index);
            int size = mData.size();
            if (index + 1 > size)
                throw new Exception("index error: " + index + ", size error: " + size);
            return mData.get(index);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => getDataFromPlaying => " + e.getMessage());
            return null;
        }
    }

    public final int getItemCount() {
        return mData.size();
    }
}