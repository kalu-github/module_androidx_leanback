package lib.kalu.leanback.presenter;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;

import java.util.LinkedList;
import java.util.List;

import lib.kalu.leanback.presenter.bean.TvEpisodesGridItemBean;
import lib.kalu.leanback.presenter.impl.ListTvPresenterImpl;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvTableScrollPresenter<T extends TvEpisodesGridItemBean> extends Presenter implements ListTvPresenterImpl {

    private int startPosition = 0;
    private final List<T> mData = new LinkedList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            ViewGroup inflate = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.lb_list_tv_table_scroll, parent, false);
            setPadding(context, inflate);
            setBackgroundColor(context, inflate);
            setContentBackgroundColor(context, inflate, R.id.module_leanback_lghp_content);
            setTitlePadding(context, inflate, R.id.module_leanback_lghp_title);
            setTitleTextColor(context, inflate, R.id.module_leanback_lghp_title);
            setTitleTextSize(context, inflate, R.id.module_leanback_lghp_title);
            setTitleAssetTTF(context, inflate, R.id.module_leanback_lghp_title);
            setTitleBackgroundColor(context, inflate, R.id.module_leanback_lghp_title);
            initContent(context, inflate);
            initIconLeft(context, inflate);
            initIconRight(context, inflate);
            return new ViewHolder(inflate);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridHorizontalPresenter => onCreateViewHolder => " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        // datas
        formatData(item);
        // title
        updateTitle(mData, viewHolder.view, R.id.module_leanback_lghp_title);
        // show
        updateData(viewHolder.view.getContext(), viewHolder.view, 0);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    protected abstract void onBindHolder(@NonNull Context context, @NonNull View view, @NonNull T item, @NonNull int position);

    protected abstract void onClickHolder(@NonNull Context context, @NonNull View view, @NonNull T item, @NonNull int checkedIndex, @NonNull int playingIndex, @NonNull boolean isFromUser);

    /**********/

    @LayoutRes
    protected abstract int initLayout();

    protected abstract int initColumn();

    protected abstract int initRow();

    protected int initSpaceVertical(Context context) {
        return 10;
    }

    protected int initSpaceHorizontal(Context context) {
        return 10;
    }

    /**********/

    @DrawableRes
    protected int initDrawableResIconLeft() {
        return 0;
    }

    @DrawableRes
    protected int initDrawableResIconRight() {
        return 0;
    }

    /**********/

    private void initContent(@NonNull Context context, @NonNull View viewGroup) {
        try {
            int column = initColumn();
            int row = initRow();
            if (column <= 0 || row <= 0)
                throw new Exception("column error: " + column + ", row error: " + row);
            ViewGroup viewContent = viewGroup.findViewById(R.id.module_leanback_lghp_content);
            if (null == viewContent)
                throw new Exception("viewContent error: null");
            viewContent.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    LeanBackUtil.log("ListTvGridHorizontalPresenter => initContent => onKey => action = " + keyEvent.getAction() + ", code = " + keyEvent.getKeyCode());
                    // action_up => keycode_dpad_down
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                        try {
                            int count = ((LinearLayout) view).getChildCount();
                            if (count <= 0)
                                throw new Exception("count error: " + count);
                            ViewGroup layoutHorizontal = (ViewGroup) ((LinearLayout) view).getChildAt(0);
                            if (null == layoutHorizontal)
                                throw new Exception("layoutHorizontal error: null");
                            int childCount = layoutHorizontal.getChildCount();
                            if (childCount <= 0)
                                throw new Exception("childCount error: " + childCount);
                            for (int m = 0; m < childCount; m++) {
                                View childAt = layoutHorizontal.getChildAt(m);
                                if (null == childAt)
                                    continue;
                                if (childAt instanceof Space)
                                    continue;
                                Object tag = childAt.getTag(R.id.lb_presenter_grid_horizontal_data);
                                if (null == tag)
                                    continue;
                                if (((T) tag).isChecked()) {
                                    ((LinearLayout) view).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                                    childAt.requestFocus();
                                    return true;
                                }
                            }
                            for (int m = 0; m < childCount; m++) {
                                View childAt = layoutHorizontal.getChildAt(m);
                                if (null == childAt)
                                    continue;
                                if (childAt instanceof Space)
                                    continue;
                                Object tag = childAt.getTag(R.id.lb_presenter_grid_horizontal_data);
                                if (null == tag)
                                    continue;
                                if (((T) tag).isPlaying()) {
                                    ((LinearLayout) view).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                                    childAt.requestFocus();
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvGridHorizontalPresenter => initContent => onKey => " + e.getMessage(), e);
                        }
                    }
                    // action_up => keycode_dpad_up
                    else if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                        try {
                            int count = ((LinearLayout) view).getChildCount();
                            if (count <= 0)
                                throw new Exception("count error: " + count);
                            ViewGroup layoutHorizontal = (ViewGroup) ((LinearLayout) view).getChildAt(count - 1);
                            if (null == layoutHorizontal)
                                throw new Exception("layoutHorizontal error: null");
                            int childCount = layoutHorizontal.getChildCount();
                            if (childCount <= 0)
                                throw new Exception("childCount error: " + childCount);
                            for (int m = 0; m < childCount; m++) {
                                View childAt = layoutHorizontal.getChildAt(m);
                                if (null == childAt)
                                    continue;
                                if (childAt instanceof Space)
                                    continue;
                                Object tag = childAt.getTag(R.id.lb_presenter_grid_horizontal_data);
                                if (null == tag)
                                    continue;
                                if (((T) tag).isChecked()) {
                                    ((LinearLayout) view).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                                    childAt.requestFocus();
                                    return true;
                                }
                            }
                            for (int m = 0; m < childCount; m++) {
                                View childAt = layoutHorizontal.getChildAt(m);
                                if (null == childAt)
                                    continue;
                                if (childAt instanceof Space)
                                    continue;
                                Object tag = childAt.getTag(R.id.lb_presenter_grid_horizontal_data);
                                if (null == tag)
                                    continue;
                                if (((T) tag).isPlaying()) {
                                    ((LinearLayout) view).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                                    childAt.requestFocus();
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvGridHorizontalPresenter => initContent => onKey => " + e.getMessage(), e);
                        }
                    }
                    return false;
                }
            });
            int childCount = viewContent.getChildCount();
            if (childCount > 0) {
                viewContent.removeAllViews();
            }
            for (int i = 0; i < row; i++) {
                LinearLayout layoutHorizontal = new LinearLayout(context);
                layoutHorizontal.setFocusable(false);
                LinearLayout.LayoutParams layoutGroupLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (i + 1 < row) {
                    layoutGroupLayoutParams.bottomMargin = initSpaceVertical(context);
                } else {
                    layoutGroupLayoutParams.bottomMargin = 0;
                }
                layoutHorizontal.setLayoutParams(layoutGroupLayoutParams);
                layoutHorizontal.setWeightSum(column);
                for (int n = 0; n < column; n++) {
                    LayoutInflater.from(context).inflate(initLayout(), layoutHorizontal, true);
                    if (i + 1 < column) {
                        Space space = new Space(context);
                        space.setFocusable(false);
                        space.setLayoutParams(new LinearLayout.LayoutParams(initSpaceHorizontal(context), LinearLayout.LayoutParams.MATCH_PARENT));
                        layoutHorizontal.addView(space);
                    }
                    int count = layoutHorizontal.getChildCount();
                    if (count <= 0)
                        continue;
                    View childAt = layoutHorizontal.getChildAt(count - 2);
                    if (null == childAt)
                        continue;
                    childAt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            try {
                                Object tag = v.getTag(R.id.lb_presenter_grid_horizontal_data);
                                if (null == tag)
                                    throw new Exception("tag error: null");
                                int episodeIndex = ((T) tag).getEpisodeIndex();
                                if (hasFocus) {
                                    resetChildChecked(viewGroup);
                                    ((T) tag).setFocus(true);
                                    onBindHolder(v.getContext(), v, ((T) tag), episodeIndex);
                                } else {
                                    ((T) tag).setFocus(false);
                                    onBindHolder(v.getContext(), v, ((T) tag), episodeIndex);
                                }
                            } catch (Exception e) {
                                LeanBackUtil.log("ListTvGridHorizontalPresenter => initContent => onFocusChange => " + e.getMessage());
                            }
                        }
                    });
                    childAt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Object tag = v.getTag(R.id.lb_presenter_grid_horizontal_data);
                                if (null == tag)
                                    throw new Exception("tag error: null");
                                boolean playing = ((T) tag).isPlaying();
                                int episodeIndex = ((T) tag).getEpisodeIndex();
                                if (playing) {
                                    onClickHolder(v.getContext(), v, ((T) tag), episodeIndex, episodeIndex, true);
                                } else {
                                    resetChild(viewGroup, v);
                                    ((T) tag).setPlaying(true);
                                    ((T) tag).setChecked(true);
                                    onBindHolder(v.getContext(), v, ((T) tag), episodeIndex);
                                    onClickHolder(v.getContext(), v, ((T) tag), episodeIndex, -1, true);
                                }
                            } catch (Exception e) {
                                LeanBackUtil.log("ListTvGridHorizontalPresenter => initContent => onClick => " + e.getMessage());
                            }
                        }
                    });
                    childAt.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
                            // action_down => keycode_dpad_left
                            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                                try {
                                    int indexOfChild = layoutHorizontal.indexOfChild(view);
                                    if (indexOfChild > 0)
                                        throw new Exception("indexOfChild warning: " + indexOfChild);
                                    if (startPosition <= 0)
                                        throw new Exception("startPosition warning: " + startPosition);
                                    int start = startPosition - row;
                                    updateData(context, viewGroup, start);
                                } catch (Exception e) {
                                    LeanBackUtil.log("ListTvGridHorizontalPresenter => initContent => onKey => " + e.getMessage());
                                }
                            }
                            // action_down => keycode_dpad_right
                            else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                                try {
                                    int indexOfChild = layoutHorizontal.indexOfChild(view);
                                    int max = 2 * column - 1;
                                    if (indexOfChild + 1 < max)
                                        throw new Exception("indexOfChild warning: " + indexOfChild);
                                    int last = startPosition + row * column;
                                    int size = mData.size();
                                    if (last + 1 >= size)
                                        throw new Exception("last warning: " + last + ", size = " + size);
                                    int start = startPosition + row;
                                    updateData(context, viewGroup, start);
                                } catch (Exception e) {
                                    LeanBackUtil.log("ListTvGridHorizontalPresenter => initContent => onKey => " + e.getMessage());
                                }
                            }
                            // action_down => keycode_dpad_up
                            else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                                try {
                                    Object tag = view.getTag(R.id.lb_presenter_grid_horizontal_data);
                                    if (null == tag)
                                        throw new Exception("tag error: null");
                                    int episodeIndex = ((T) tag).getEpisodeIndex();
                                    if (episodeIndex % row != 0)
                                        throw new Exception("episodeIndex warning: " + episodeIndex);
                                    ((T) tag).setChecked(true);
                                    onBindHolder(view.getContext(), view, ((T) tag), episodeIndex);
                                    viewContent.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                                    viewContent.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                                } catch (Exception e) {
                                    LeanBackUtil.log("ListTvGridHorizontalPresenter => initContent => onKey => " + e.getMessage());
                                }
                            }
                            // action_down => keycode_dpad_down
                            else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                                try {
                                    Object tag = view.getTag(R.id.lb_presenter_grid_horizontal_data);
                                    if (null == tag)
                                        throw new Exception("tag error: null");
                                    int episodeIndex = ((T) tag).getEpisodeIndex();
                                    int v = row - 1;
                                    if (episodeIndex % row != v)
                                        throw new Exception("episodeIndex warning: " + episodeIndex);
                                    ((T) tag).setChecked(true);
                                    onBindHolder(view.getContext(), view, ((T) tag), episodeIndex);
                                    viewContent.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                                } catch (Exception e) {
                                    LeanBackUtil.log("ListTvGridHorizontalPresenter => initContent => onKey => " + e.getMessage());
                                }
                            }
                            return false;
                        }
                    });
                }
                viewContent.addView(layoutHorizontal, i);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridHorizontalPresenter => initContent => " + e.getMessage(), e);
        }
    }

    private void initIconLeft(Context context, ViewGroup viewGroup) {
        try {
            int resIcon = initDrawableResIconLeft();
            if (resIcon == 0)
                throw new Exception("initDrawableResIconLeft error: " + initDrawableResIconLeft());
            ImageView imageView = viewGroup.findViewById(R.id.module_leanback_lghp_icon_left);
            if (null == imageView)
                throw new Exception("imageView error: null");
            imageView.setImageResource(resIcon);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridHorizontalPresenter => initIconLeft => " + e.getMessage(), e);
        }
    }

    private void initIconRight(Context context, ViewGroup viewGroup) {
        try {
            int resIcon = initDrawableResIconRight();
            if (resIcon == 0)
                throw new Exception("initDrawableResIconRight error: " + initDrawableResIconRight());
            ImageView imageView = viewGroup.findViewById(R.id.module_leanback_lghp_icon_right);
            if (null == imageView)
                throw new Exception("imageView error: null");
            imageView.setImageResource(resIcon);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridHorizontalPresenter => initIconRight => " + e.getMessage(), e);
        }
    }

    private void formatData(Object item) {
        try {
//            int size = mData.size();
//            if (size > 0)
//                throw new Exception("not empty");
            mData.clear();
            List<T> list = (List<T>) item;
            int max = list.size();
            for (int i = 0; i < max; i++) {
                T t = list.get(i);
                if (null == t)
                    continue;
                t.setEpisodeIndex(i);
                t.setEpisodeMax(max);
                t.setChecked(false);
                t.setPlaying(false);
                t.setFocus(false);
                mData.add(t);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridHorizontalPresenter => formatData => " + e.getMessage(), e);
        }
    }

    private void updateData(Context context, View viewGroup, int startPosition) {
        try {
            int column = initColumn();
            int row = initRow();
            if (column <= 0 || row <= 0)
                throw new Exception("column error: " + column + ", row error: " + row);
            ViewGroup layoutVertical = viewGroup.findViewById(R.id.module_leanback_lghp_content);
            if (null == layoutVertical)
                throw new Exception("viewContent error: null");
            this.startPosition = startPosition;
            for (int n = 0; n < row; n++) {
                View layoutHorizontal = layoutVertical.getChildAt(n);
                if (null == layoutHorizontal)
                    continue;
                if (!(layoutHorizontal instanceof LinearLayout))
                    continue;
                int mReal = 0;
                int columnReal = column * 2 - 1;
                for (int m = 0; m < columnReal; m++) {
                    View view = ((LinearLayout) layoutHorizontal).getChildAt(m);
                    if (null == view)
                        continue;
                    if (view instanceof Space)
                        continue;
                    int index;
                    if (n % 2 == 0) {
                        index = row * mReal + startPosition;
                    } else {
                        index = row * mReal + 1 + startPosition;
                    }
                    mReal += 1;
                    int size = mData.size();
                    if (index + 1 >= size) {
                        view.setTag(R.id.lb_presenter_grid_horizontal_data, null);
                        view.setVisibility(View.INVISIBLE);
                    } else {
                        view.setVisibility(View.VISIBLE);
                        T t = mData.get(index);
                        view.setTag(R.id.lb_presenter_grid_horizontal_data, t);
                        onBindHolder(context, view, t, index);
                    }
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridHorizontalPresenter => updateData => " + e.getMessage(), e);
        }
    }

//    private int getPlayingIndex() {
//        try {
//            for (T t : mData) {
//                if (null == t)
//                    continue;
//                if (t.isPlaying())
//                    return mData.indexOf(t);
//            }
//            throw new Exception("not find");
//        } catch (Exception e) {
//            LeanBackUtil.log("ListTvGridHorizontalPresenter => getPlayingIndex => " + e.getMessage());
//            return -1;
//        }
//    }

    private int getCheckedIndex() {
        try {
            for (T t : mData) {
                if (null == t)
                    continue;
                if (t.isChecked())
                    return mData.indexOf(t);
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridHorizontalPresenter => getCheckedIndex => " + e.getMessage());
            return -1;
        }
    }

    private void updatePlayingCheckedIndex(int index) {
        try {
            int size = mData.size();
            for (int i = 0; i < size; i++) {
                T t = mData.get(i);
                if (null == t)
                    continue;
                t.setPlaying(i == index);
                t.setChecked(i == index);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridHorizontalPresenter => updatePlayingCheckedIndex => " + e.getMessage());
        }
    }

    private void resetChild(View viewGroup, View view) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            ViewGroup layoutVertical = viewGroup.findViewById(R.id.module_leanback_lghp_content);
            if (null == layoutVertical)
                throw new Exception("layoutVertical error: null");
            if (!(layoutVertical instanceof LinearLayout))
                throw new Exception("layoutVertical error: not instanceof LinearLayout");
            int childCount = layoutVertical.getChildCount();
            for (int m = 0; m < childCount; m++) {
                View layoutHorizontal = layoutVertical.getChildAt(m);
                if (null == layoutHorizontal)
                    continue;
                if (!(layoutHorizontal instanceof LinearLayout))
                    continue;
                int count = ((LinearLayout) layoutHorizontal).getChildCount();
                for (int n = 0; n < count; n++) {
                    View childAt = ((LinearLayout) layoutHorizontal).getChildAt(n);
                    if (null == childAt)
                        continue;
                    if (childAt instanceof Space)
                        continue;
                    if (null != view && view == childAt)
                        continue;
                    Object tag = childAt.getTag(R.id.lb_presenter_grid_horizontal_data);
                    if (null == tag)
                        continue;
                    if (!((T) tag).isChecked() && !((T) tag).isPlaying())
                        continue;
                    ((T) tag).setChecked(false);
                    ((T) tag).setPlaying(false);
                    onBindHolder(childAt.getContext(), childAt, ((T) tag), ((T) tag).getEpisodeIndex());
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridHorizontalPresenter => resetChild => " + e.getMessage());
        }

        try {
            for (T t : mData) {
                if (null == t)
                    continue;
                t.setChecked(false);
                t.setPlaying(false);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridHorizontalPresenter => resetChild => " + e.getMessage());
        }
    }

    private void resetChildChecked(View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            ViewGroup layoutVertical = viewGroup.findViewById(R.id.module_leanback_lghp_content);
            if (null == layoutVertical)
                throw new Exception("layoutVertical error: null");
            if (!(layoutVertical instanceof LinearLayout))
                throw new Exception("layoutVertical error: not instanceof LinearLayout");
            int childCount = layoutVertical.getChildCount();
            for (int m = 0; m < childCount; m++) {
                View layoutHorizontal = layoutVertical.getChildAt(m);
                if (null == layoutHorizontal)
                    continue;
                if (!(layoutHorizontal instanceof LinearLayout))
                    continue;
                int count = ((LinearLayout) layoutHorizontal).getChildCount();
                for (int n = 0; n < count; n++) {
                    View childAt = ((LinearLayout) layoutHorizontal).getChildAt(n);
                    if (null == childAt)
                        continue;
                    if (childAt instanceof Space)
                        continue;
                    Object tag = childAt.getTag(R.id.lb_presenter_grid_horizontal_data);
                    if (null == tag)
                        continue;
                    if (!((T) tag).isChecked())
                        continue;
                    ((T) tag).setChecked(false);
                    onBindHolder(childAt.getContext(), childAt, ((T) tag), ((T) tag).getEpisodeIndex());
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridHorizontalPresenter => resetChildChecked => " + e.getMessage());
        }

        try {
            for (T t : mData) {
                if (null == t)
                    continue;
                t.setChecked(false);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridHorizontalPresenter => resetChildChecked => " + e.getMessage());
        }
    }

//    private void resetChild(View viewGroup, int index) {
//        try {
//            if (null == viewGroup)
//                throw new Exception("viewGroup error: null");
//            if (viewGroup.getId() != R.id.module_leanback_lghp_root)
//                throw new Exception("viewGroup.getId error: not R.id.module_leanback_lghp_root");
//            int row = initRow();
//            if (row <= 0)
//                throw new Exception("row error: " + row);
//            int column = initColumn();
//            if (column <= 0)
//                throw new Exception("column error: " + column);
//            ViewGroup layoutVertical = viewGroup.findViewById(R.id.module_leanback_lghp_content);
//            if (null == layoutVertical)
//                throw new Exception("layoutVertical error: null");
//            int childIndex = index % row;
//            LeanBackUtil.log("ListTvGridHorizontalPresenter => checkedPlayingPosition => childIndex = " + childIndex);
//            int childCount = layoutVertical.getChildCount();
//            if (childIndex + 1 > childCount)
//                throw new Exception("childIndex error: " + childIndex + ", childCount error: " + childCount);
//            ViewGroup layoutHorizontal = (ViewGroup) layoutVertical.getChildAt(childIndex);
//            if (null == layoutHorizontal)
//                throw new Exception("layoutHorizontal error: null");
//            int layoutHorizontalChildIndex;
//            if (index % 2 == 0) {
//                layoutHorizontalChildIndex = index / column;
//            } else {
//                layoutHorizontalChildIndex = (index - 1) / column;
//            }
//            LeanBackUtil.log("ListTvGridHorizontalPresenter => checkedPlayingPosition => layoutHorizontalChildIndex = " + layoutHorizontalChildIndex);
//            int layoutHorizontalChildCount = layoutHorizontal.getChildCount() / 2 + 1;
//            if (layoutHorizontalChildIndex + 1 > layoutHorizontalChildCount)
//                throw new Exception("layoutHorizontalChildIndex error: " + layoutHorizontalChildIndex + ", layoutHorizontalChildCount error: " + layoutHorizontalChildCount);
//            View childAt;
//            if (layoutHorizontalChildIndex == 0) {
//                childAt = layoutHorizontal.getChildAt(layoutHorizontalChildIndex);
//            } else {
//                childAt = layoutHorizontal.getChildAt(2 * layoutHorizontalChildIndex);
//            }
//            if (null == childAt)
//                throw new Exception("childAt error: null");
//            T t = mData.get(index);
//            t.setChecked(false);
//            t.setPlaying(false);
//            t.setFocus(false);
//            onBindHolder(childAt.getContext(), childAt, t, index);
//        } catch (Exception e) {
//            LeanBackUtil.log("ListTvGridHorizontalPresenter => resetChild => " + e.getMessage());
//        }
//    }

    /********************/

    public void checkedPlayingPosition(@NonNull View viewGroup, @NonNull int checkedPosition) {
        LeanBackUtil.log("ListTvGridHorizontalPresenter => checkedPlayingPosition => checkedPosition = " + checkedPosition);
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            if (viewGroup.getId() != R.id.module_leanback_lghp_root)
                throw new Exception("viewGroup.getId error: not R.id.module_leanback_lghp_root");
            int row = initRow();
            if (row <= 0)
                throw new Exception("row error: " + row);
            int column = initColumn();
            if (column <= 0)
                throw new Exception("column error: " + column);
            ViewGroup layoutVertical = viewGroup.findViewById(R.id.module_leanback_lghp_content);
            if (null == layoutVertical)
                throw new Exception("layoutVertical error: null");
            int childIndex = checkedPosition % row;
            LeanBackUtil.log("ListTvGridHorizontalPresenter => checkedPlayingPosition => childIndex = " + childIndex);
            int childCount = layoutVertical.getChildCount();
            if (childIndex + 1 > childCount)
                throw new Exception("childIndex error: " + childIndex + ", childCount error: " + childCount);
            ViewGroup layoutHorizontal = (ViewGroup) layoutVertical.getChildAt(childIndex);
            if (null == layoutHorizontal)
                throw new Exception("layoutHorizontal error: null");
            int layoutHorizontalChildIndex;
            if (checkedPosition % 2 == 0) {
                layoutHorizontalChildIndex = checkedPosition / column;
            } else {
                layoutHorizontalChildIndex = (checkedPosition - 1) / column;
            }
            LeanBackUtil.log("ListTvGridHorizontalPresenter => checkedPlayingPosition => layoutHorizontalChildIndex = " + layoutHorizontalChildIndex);
            int layoutHorizontalChildCount = layoutHorizontal.getChildCount() / 2 + 1;
            if (layoutHorizontalChildIndex + 1 > layoutHorizontalChildCount)
                throw new Exception("layoutHorizontalChildIndex error: " + layoutHorizontalChildIndex + ", layoutHorizontalChildCount error: " + layoutHorizontalChildCount);
            View childAt;
            if (layoutHorizontalChildIndex == 0) {
                childAt = layoutHorizontal.getChildAt(layoutHorizontalChildIndex);
            } else {
                childAt = layoutHorizontal.getChildAt(2 * layoutHorizontalChildIndex);
            }
            if (null == childAt)
                throw new Exception("childAt error: null");
            resetChild(viewGroup, null);
            updatePlayingCheckedIndex(checkedPosition);
            T t = mData.get(checkedPosition);
            childAt.setTag(R.id.lb_presenter_grid_horizontal_data, t);
            onBindHolder(childAt.getContext(), childAt, t, checkedPosition);
            onClickHolder(childAt.getContext(), childAt, t, checkedPosition, t.getEpisodeIndex(), false);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridHorizontalPresenter => checkedPlayingPosition => " + e.getMessage());
        }
    }
}