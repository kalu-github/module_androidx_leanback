package lib.kalu.leanback.presenter;

import android.content.Context;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
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
import lib.kalu.leanback.radio.RadioGroupHorizontal;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvRadioGroupListPresenter<T extends TvPresenterRowBean> extends Presenter implements ListTvPresenterImpl {

    private final List<T>[] mCollection = new LinkedList[initRadioGroupText().length];
    private final List<T> mData = new LinkedList<>();

    /**************/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        LeanBackUtil.log("ListTvRadioGroupListPresenter => onCreateViewHolder =>");
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
            View inflate = LayoutInflater.from(context).inflate(initLayoutContent(), viewById, true);
            onCreateHolderContent(inflate);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => initContent => " + e.getMessage());
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        LeanBackUtil.log("ListTvRadioGroupListPresenter => onBindViewHolder =>");
        // data
        try {
            if (null == item)
                throw new Exception("item error: null");
            if (!(item instanceof List[]))
                throw new Exception("item error: not instanceof List[]");
            List<T>[] datas = (List<T>[]) item;
            int length = datas.length;
            if (length <= 0)
                throw new Exception("length error: " + length);
            if (length < mCollection.length)
                throw new Exception("length error: < " + mCollection.length);
            for (int i = 0; i < mCollection.length; i++) {
                LinkedList<T> list = new LinkedList<>();
                list.addAll(datas[i]);
                mCollection[i] = list;
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => onBindViewHolder => " + e.getMessage());
        }

        // 2
        switchContent(viewHolder.view, false);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private void switchContent(View viewGroup, boolean isFromUser) {
        LeanBackUtil.log("ListTvRadioGroupListPresenter => switchContent =>");
        // update
        try {
            if (null == mCollection)
                throw new Exception("mMap error: null");
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            RecyclerViewVertical recyclerView = viewGroup.findViewById(R.id.module_leanback_lrgl_list);
            if (null == recyclerView)
                throw new Exception("recyclerView error: null");
            RadioGroupHorizontal radioGroup = viewGroup.findViewById(R.id.module_leanback_lrgl_radio);
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int checkedIndex = -1;
            int childCount = radioGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = radioGroup.getChildAt(i);
                if (null == child)
                    continue;
                boolean checked = ((RadioButton) child).isChecked();
                if (checked) {
                    checkedIndex = i;
                    break;
                }
            }
            if (checkedIndex < 0)
                throw new Exception("checkedIndex error: " + checkedIndex);
            List<T> list = mCollection[checkedIndex];
            if (null == list || list.size() <= 0)
                throw new Exception("list error: " + list);
            mData.clear();
            mData.addAll(list);
            recyclerView.getAdapter().notifyItemRangeChanged(0, mData.size());
            View viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_contont);
            onBindHolderContent(viewById, 0, mData.get(0), isFromUser);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => switchContent => " + e.getMessage());
        }
    }

    /**************/

    private final void initRadioGroup(Context context, View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            RadioGroupHorizontal radioGroup = viewGroup.findViewById(R.id.module_leanback_lrgl_radio);
            if (null == radioGroup)
                throw new Exception("radioGroup error: null");
            int childCount = radioGroup.getChildCount();
            if (childCount > 0)
                throw new Exception("childCount warning: " + childCount);
            String[] groupText = initRadioGroupText();
            if (null == groupText)
                throw new Exception("groupText error: null");
            int[] groupMargin = initRadioGroupMargin(context);
            if (null != groupMargin && groupMargin.length == 4) {
                ViewGroup.LayoutParams layoutParams = radioGroup.getLayoutParams();
                if (null != layoutParams) {
                    ((RelativeLayout.LayoutParams) layoutParams).height = initRadioGroupHeight(context);
                    ((RelativeLayout.LayoutParams) layoutParams).leftMargin = groupMargin[0];
                    ((RelativeLayout.LayoutParams) layoutParams).rightMargin = groupMargin[1];
                    ((RelativeLayout.LayoutParams) layoutParams).topMargin = groupMargin[2];
                    ((RelativeLayout.LayoutParams) layoutParams).bottomMargin = groupMargin[3];
                }
            }
            int[] groupPadding = initRadioGroupPadding(context);
            if (null != groupPadding && groupPadding.length == 4) {
                radioGroup.setPadding(groupPadding[0], groupPadding[1], groupPadding[2], groupPadding[3]);
            }
            int length = groupText.length;
            for (int i = 0; i < length; i++) {
                String s = groupText[i];
                LeanBackUtil.log("ListTvRadioGroupListPresenter => initRadioGroup => s = " + s + ", i = " + i);
                if (null == s || s.length() <= 0)
                    continue;
                RadioButton radioButton = new RadioButton(context);
                radioButton.setChecked(i == 0);
                radioButton.setText(s);
                radioButton.setFocusable(true);
                radioButton.setButtonDrawable(null);
                RadioGroup.LayoutParams layoutParams = new RadioGroupHorizontal.LayoutParams(RadioGroupHorizontal.LayoutParams.WRAP_CONTENT, initRadioGroupHeight(context));
                int[] margin = initRadioButtonMargin(context);
                if (null != margin && margin.length == 4) {
                    layoutParams.leftMargin = margin[0];
                    layoutParams.rightMargin = margin[1];
                    layoutParams.topMargin = margin[2];
                    layoutParams.bottomMargin = margin[3];
                }
                radioButton.setLayoutParams(layoutParams);
                int[] padding = initRadioButtonPadding(context);
                if (null != padding && padding.length == 4) {
                    radioButton.setPadding(padding[0], padding[1], padding[2], padding[3]);
                }
                int background = initRadioButtonBackground(context);
                if (background != 0) {
                    radioButton.setBackgroundResource(background);
                }
                int textSize = initRadioButtonTextSize(context);
                if (textSize != 0) {
                    radioButton.setTextSize(textSize, TypedValue.COMPLEX_UNIT_PX);
                }
                int textColor = initRadioButtonTextColor(context);
                if (textColor != 0) {
                    radioButton.setTextColor(textColor);
                }
                int count = radioGroup.getChildCount();
                LeanBackUtil.log("ListTvRadioGroupListPresenter => initRadioGroup => addView => count = " + count);
                radioGroup.addView(radioButton, count);
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switchContent(viewGroup, true);
                }
            });
        } catch (Exception e) {
            LeanBackUtil.log("ListTvRadioGroupListPresenter => initRadioGroup => " + e.getMessage());
        }
    }

    private final void initAdapter(Context context, View viewGroup) {
        try {
            RecyclerView recyclerView = viewGroup.findViewById(R.id.module_leanback_lrgl_list);
            if (null == recyclerView)
                throw new Exception("recyclerView error: null");
            int[] margin = initContentMargin(context);
            if (null != margin && margin.length == 4) {
                ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                if (null != layoutParams) {
                    ((RelativeLayout.LayoutParams) layoutParams).leftMargin = margin[0];
                    ((RelativeLayout.LayoutParams) layoutParams).rightMargin = margin[1];
                    ((RelativeLayout.LayoutParams) layoutParams).topMargin = margin[2];
                    ((RelativeLayout.LayoutParams) layoutParams).bottomMargin = margin[3];
                }
            }
            int[] padding = initContentPadding(context);
            if (null != padding && padding.length == 4) {
                recyclerView.setPadding(padding[0], padding[1], padding[2], padding[3]);
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
                            View view = LayoutInflater.from(context).inflate(initLayoutItem(), parent, false);
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
                                        onBindHolderContent(viewById, position, mData.get(position), true);
                                    } catch (Exception e) {
                                        LeanBackUtil.log("ListTvRadioGroupListPresenter => onClick => " + e.getMessage());
                                    }
                                }
                            });
                            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    try {
                                        int position = holder.getAbsoluteAdapterPosition();
                                        if (position < 0)
                                            throw new Exception("position error: " + position);
                                        if (hasFocus) {
                                            View viewById = viewGroup.findViewById(R.id.module_leanback_lrgl_contont);
                                            onBindHolderContent(viewById, position, mData.get(position), true);
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            });
                            view.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View v, int keyCode, KeyEvent event) {
                                    // up-leave
                                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                    }
                                    // up-into up-move
                                    else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                        return true;
                                    }
                                    // down-leave
                                    else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                                    }
                                    // down-into down-move
                                    else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                                        return true;
                                    }
                                    return false;
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

    protected abstract String[] initRadioGroupText();

    @LayoutRes
    protected abstract int initLayoutItem();

    @LayoutRes
    protected abstract int initLayoutContent();

    /**************/

    protected void onCreateHolderItem(@NonNull RecyclerView.ViewHolder holder, List<T> list) {
    }

    protected void onBindHolderItem(@NonNull RecyclerView.ViewHolder holder, int position, T t) {
    }

    protected void onCreateHolderContent(@NonNull View viewGroup) {
    }

    protected void onBindHolderContent(@NonNull View viewGroup, int position, T t, boolean isFromUser) {
    }

    /**************/


    protected int initRadioGroupHeight(@NonNull Context context) {
        return 40;
    }

    protected int[] initRadioGroupPadding(@NonNull Context context) {
        return new int[]{0, 0, 0, 0};
    }

    protected int[] initRadioGroupMargin(@NonNull Context context) {
        return new int[]{0, 0, 0, 0};
    }

    protected int initRadioButtonTextSize(@NonNull Context context) {
        return 100;
    }

    protected int[] initRadioButtonPadding(@NonNull Context context) {
        return new int[]{0, 0, 0, 0};
    }

    protected int[] initRadioButtonMargin(@NonNull Context context) {
        return new int[]{0, 0, 0, 0};
    }

    @ColorInt
    protected int initRadioButtonTextColor(@NonNull Context context) {
        return 0;
    }

    @DrawableRes
    protected int initRadioButtonBackground(@NonNull Context context) {
        return 0;
    }

    protected int[] initContentMargin(@NonNull Context context) {
        return new int[]{0, 0, 0, 0};
    }

    protected int[] initContentPadding(@NonNull Context context) {
        return new int[]{0, 0, 0, 0};
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
}