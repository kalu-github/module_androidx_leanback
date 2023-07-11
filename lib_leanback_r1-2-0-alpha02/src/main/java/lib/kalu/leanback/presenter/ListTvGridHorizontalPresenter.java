package lib.kalu.leanback.presenter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import lib.kalu.leanback.presenter.bean.TvEpisodesGridItemBean;
import lib.kalu.leanback.presenter.impl.ListTvPresenterImpl;
import lib.kalu.leanback.util.LeanBackUtil;

public abstract class ListTvGridHorizontalPresenter<T extends TvEpisodesGridItemBean> extends Presenter implements ListTvPresenterImpl {

    private int startPosition = 0;
    private final List<T> mData = new LinkedList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            ViewGroup inflate = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.lb_list_tv_grid_horizontal, parent, false);
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
            int childCount = viewContent.getChildCount();
            if (childCount > 0) {
                viewContent.removeAllViews();
            }
            for (int i = 0; i < row; i++) {
                LinearLayout layoutGroup = new LinearLayout(context);
                LinearLayout.LayoutParams layoutGroupLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (i + 1 < row) {
                    layoutGroupLayoutParams.bottomMargin = initSpaceVertical(context);
                } else {
                    layoutGroupLayoutParams.bottomMargin = 0;
                }
                layoutGroup.setLayoutParams(layoutGroupLayoutParams);
                layoutGroup.setWeightSum(column);
                for (int n = 0; n < column; n++) {
                    LayoutInflater.from(context).inflate(initLayout(), layoutGroup, true);
                    if (i + 1 < column) {
                        Space space = new Space(context);
                        space.setFocusable(false);
                        space.setLayoutParams(new LinearLayout.LayoutParams(initSpaceHorizontal(context), LinearLayout.LayoutParams.MATCH_PARENT));
                        layoutGroup.addView(space);
                    }
                    int count = layoutGroup.getChildCount();
                    if (count <= 0)
                        continue;
                    View childAt = layoutGroup.getChildAt(count - 2);
                    if (null == childAt)
                        continue;
                    childAt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean hasFocus) {
                        }
                    });
                    childAt.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
                            // action_down => keycode_dpad_left
                            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                                try {
                                    int indexOfChild = layoutGroup.indexOfChild(view);
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
                                    int indexOfChild = layoutGroup.indexOfChild(view);
                                    int max = 2 * column - 1;
                                    LeanBackUtil.log("ListTvGridHorizontalPresenter => initContent => onKey => indexOfChild = " + indexOfChild + ", max = " + max);
                                    if (indexOfChild + 1 < max)
                                        throw new Exception("indexOfChild warning: " + indexOfChild);
                                    int last = startPosition + row * column;
                                    int size = mData.size();
                                    if (last >= size)
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
                                    int indexOfChild = layoutGroup.indexOfChild(view);
                                    if (indexOfChild < 0)
                                        throw new Exception("indexOfChild error: " + indexOfChild);

                                } catch (Exception e) {
                                    LeanBackUtil.log("ListTvGridHorizontalPresenter => initContent => onKey => " + e.getMessage(), e);
                                }
                            }
                            // action_down => keycode_dpad_down
                            else if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                                try {
                                    int indexOfChild = layoutGroup.indexOfChild(view);
                                    if (indexOfChild < 0)
                                        throw new Exception("indexOfChild error: " + indexOfChild);

                                } catch (Exception e) {
                                    LeanBackUtil.log("ListTvGridHorizontalPresenter => initContent => onKey => " + e.getMessage(), e);
                                }
                            }
                            return false;
                        }
                    });
                }
                viewContent.addView(layoutGroup, i);
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
                        view.setVisibility(View.INVISIBLE);
                    } else {
                        view.setVisibility(View.VISIBLE);
                        T t = mData.get(index);
                        onBindHolder(context, view, t, index);
                    }
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvGridHorizontalPresenter => updateData => " + e.getMessage(), e);
        }
    }

    /********************/

    public void checkedPlayingPosition(@NonNull View viewGroup, @NonNull int checkedPosition) {

    }
}
