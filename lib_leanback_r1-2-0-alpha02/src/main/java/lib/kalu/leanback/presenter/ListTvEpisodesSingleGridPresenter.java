package lib.kalu.leanback.presenter;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

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

public abstract class ListTvEpisodesSingleGridPresenter<T extends TvEpisodesGridItemBean> extends Presenter implements ListTvPresenterImpl {

    private final List<T> mData = new LinkedList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        try {
            Context context = parent.getContext();
            onLife(context);
            ViewGroup inflate = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.lb_list_tv_episodes_single_grid, parent, false);
            setPadding(context, inflate);
            setBackgroundColor(context, inflate);
            setContentBackgroundColor(context, inflate, R.id.module_leanback_legp_list);
            setTitlePadding(context, inflate, R.id.module_leanback_legp_title);
            setTitleTextColor(context, inflate, R.id.module_leanback_legp_title);
            setTitleTextSize(context, inflate, R.id.module_leanback_legp_title);
            setTitleAssetTTF(context, inflate, R.id.module_leanback_legp_title);
            setTitleBackgroundColor(context, inflate, R.id.module_leanback_legp_title);
            initAdapter(context, inflate);
            return new ViewHolder(inflate);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => onCreateViewHolder => " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

        // datas
        formatData(item);

        // title
        updateTitle(mData, viewHolder.view, R.id.module_leanback_legp_title);

        // layoutManager
        initLayoutManager(viewHolder.view);

        // list
        updateAdapter(viewHolder.view);
    }

    private final void updateAdapter(View view) {
        try {
            RecyclerView recyclerView = view.findViewById(R.id.module_leanback_legp_list);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => updateAdapter => " + e.getMessage(), e);
        }
    }

    private final void formatData(Object item) {
        try {
            int size = mData.size();
            if (size > 0)
                throw new Exception("not empty");
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
            LeanBackUtil.log("ListTvEpisodesGridPresenter => formatData => " + e.getMessage(), e);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    private final void initLayoutManager(@NonNull View inflate) {
        try {
            RecyclerView recyclerView = inflate.findViewById(R.id.module_leanback_legp_list);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (null == layoutManager) {

                int col;
                int span = 2;
                int size = mData.size();
                if (size <= span) {
                    col = span;
                } else {
                    col = Math.min(span, size);
                }

                GridLayoutManager manager = new GridLayoutManager(inflate.getContext(), col) {
                    @Override
                    public boolean canScrollHorizontally() {
                        return initScrollHorizontally();
                    }

                    @Override
                    public boolean canScrollVertically() {
                        return initScrollVertically();
                    }
                };
                manager.setOrientation(RecyclerView.HORIZONTAL);
                recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        initItemOffsets(outRect, view, parent, state);
                    }
                });
                recyclerView.setLayoutManager(manager);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => initLayoutManager => " + e.getMessage(), e);
        }
    }

    private final void initAdapter(@NonNull Context context, @NonNull View inflate) {
        try {
            RecyclerView recyclerView = inflate.findViewById(R.id.module_leanback_legp_list);
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (null == adapter) {
                recyclerView.setAdapter(new RecyclerView.Adapter() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        try {
                            onLife(context);
                            View view = LayoutInflater.from(context).inflate(initLayout(viewType), parent, false);
                            RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {
                            };
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    try {
                                        int checkedPosition = holder.getAbsoluteAdapterPosition();
                                        if (checkedPosition < 0)
                                            throw new Exception("position error: " + checkedPosition);
                                        // 1
                                        int playingIndex = -1;
                                        for (T t : mData) {
                                            if (null == t)
                                                continue;
                                            boolean playing = t.isPlaying();
                                            if (playing) {
                                                t.setChecked(false);
                                                t.setPlaying(false);
                                                playingIndex = mData.indexOf(t);
                                                recyclerView.getAdapter().notifyItemRangeChanged(playingIndex, 1);
                                                break;
                                            }
                                        }

                                        // 2
                                        T t = mData.get(checkedPosition);
                                        t.setChecked(true);
                                        t.setPlaying(true);
                                        onBindHolder(v.getContext(), v, t, checkedPosition);
                                        onClickHolder(v.getContext(), v, t, checkedPosition, playingIndex, true);
                                    } catch (Exception e) {
                                        LeanBackUtil.log("ListTvEpisodesGridPresenter => onClick => " + e.getMessage());
                                    }
                                }
                            });
                            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    int position = holder.getAbsoluteAdapterPosition();
                                    LeanBackUtil.log("ListTvEpisodesGridPresenter => onFocusChange => position = " + position + ", hasFocus = " + hasFocus);
                                    if (position >= 0) {
                                        T t = mData.get(position);
                                        if (hasFocus) {
                                            t.setChecked(true);
                                        } else {
                                            Object tag = v.getTag(R.id.lb_presenter_episodes_grid);
                                            if (null != tag) {
                                                v.setTag(R.id.lb_presenter_episodes_grid, null);
                                            } else {
                                                t.setChecked(false);
                                            }
                                        }
                                        t.setFocus(hasFocus);
                                        onBindHolder(v.getContext(), v, t, position);
                                    }
                                }
                            });
                            view.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View v, int keyCode, KeyEvent event) {
                                    LeanBackUtil.log("ListTvEpisodesGridPresenter => onKey => aition = " + event.getAction() + ", keyCode = " + keyCode);
                                    // up-leave
                                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                        int cur = holder.getAbsoluteAdapterPosition();
                                        LeanBackUtil.log("ListTvEpisodesGridPresenter => onKey => up-leave => cur = " + cur);
                                        if (cur >= 0 && cur % 2 == 0) {
                                            v.setTag(R.id.lb_presenter_episodes_grid, true);
                                            T t = mData.get(cur);
                                            t.setChecked(true);
                                            t.setFocus(false);
                                        }
                                    }
                                    // up-into up-move
                                    else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_UP) {

                                        int adapterPosition = holder.getAbsoluteAdapterPosition();
                                        // into
                                        if (adapterPosition % 2 == 1) {
                                            LeanBackUtil.log("ListTvEpisodesGridPresenter => onKey => up-into => adapterPosition = " + adapterPosition);

                                            int checkedPosition = -1;
                                            for (T t : mData) {
                                                if (null == t)
                                                    continue;
                                                if (t.isChecked()) {
                                                    checkedPosition = mData.indexOf(t);
                                                }
                                            }

                                            if (checkedPosition >= 0) {
                                                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(checkedPosition);
                                                LeanBackUtil.log("ListTvEpisodesGridPresenter => onKey => up-into => viewHolder = " + viewHolder);
                                                if (null != viewHolder) {
                                                    viewHolder.itemView.requestFocus();
                                                }
                                            }

                                        }
                                        // move
                                        else {
                                            LeanBackUtil.log("ListTvEpisodesGridPresenter => onKey => up-move => adapterPosition = " + adapterPosition);
                                        }
                                        return true;
                                    }
                                    // down-leave
                                    else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                                        int cur = holder.getAbsoluteAdapterPosition();
                                        LeanBackUtil.log("ListTvEpisodesGridPresenter => onKey => down-leave => cur = " + cur);
                                        if (cur >= 0 && cur % 2 == 1) {
                                            T t = mData.get(cur);
                                            t.setChecked(true);
                                            t.setFocus(false);
                                            v.setTag(R.id.lb_presenter_episodes_grid, true);
                                        }
                                    }
                                    // down-into down-move
                                    else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

                                        int adapterPosition = holder.getAbsoluteAdapterPosition();
                                        // into
                                        if (adapterPosition % 2 == 0) {
                                            LeanBackUtil.log("ListTvEpisodesGridPresenter => onKey => down-into => adapterPosition = " + adapterPosition);
                                            int checkedPosition = -1;
                                            for (T t : mData) {
                                                if (null == t)
                                                    continue;
                                                if (t.isChecked()) {
                                                    checkedPosition = mData.indexOf(t);
                                                }
                                            }
                                            LeanBackUtil.log("ListTvEpisodesGridPresenter => onKey => down-into => checkedPosition = " + checkedPosition);

                                            if (checkedPosition >= 0) {
                                                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(checkedPosition);
                                                LeanBackUtil.log("ListTvEpisodesGridPresenter => onKey => down-into => viewHolder = " + viewHolder);
                                                if (null != viewHolder) {
                                                    viewHolder.itemView.requestFocus();
                                                }
                                            }
                                        }
                                        // move
                                        else {
                                            LeanBackUtil.log("ListTvEpisodesGridPresenter => onKey => down-move => adapterPosition = " + adapterPosition);
                                        }
                                        return true;
                                    }
                                    return false;
                                }
                            });
                            return holder;
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvEpisodesGridPresenter => setAdapter => onCreateViewHolder => " + e.getMessage(), e);
                            return null;
                        }
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        try {
                            T t = mData.get(position);
                            onBindHolder(holder.itemView.getContext(), holder.itemView, t, position);
                        } catch (Exception e) {
                            LeanBackUtil.log("ListTvEpisodesGridPresenter => setAdapter => onBindViewHolder => " + e.getMessage(), e);
                        }
                    }

                    @Override
                    public int getItemCount() {
                        return mData.size();
                    }
                });
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => setAdapter => " + e.getMessage(), e);
        }

    }

    protected void onLife(@NonNull Context context) {
    }

    protected final void requestFocus(@NonNull View v, @NonNull int position) {
        try {
            RecyclerView recyclerView = findRecyclerView(v, true);
            View viewByPosition = recyclerView.getLayoutManager().findViewByPosition(position);
            viewByPosition.requestFocus();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => requestFocus => " + e.getMessage(), e);
        }
    }

    protected final void notifyDataSetChanged(@NonNull View v) {
        try {
            RecyclerView recyclerView = findRecyclerView(v, true);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => notifyDataSetChanged => " + e.getMessage(), e);
        }
    }

    protected final void notifyItemRangeChanged(@NonNull View v, int start, int itemCount) {
        try {
            RecyclerView recyclerView = findRecyclerView(v, true);
            recyclerView.getAdapter().notifyItemRangeChanged(start, itemCount);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => notifyItemRangeChanged => " + e.getMessage(), e);
        }
    }

    private final RecyclerView findRecyclerView(@NonNull View v, @NonNull boolean init) {

        if (init) {
            View view = v.findViewById(R.id.module_leanback_legp_list);
            if (view instanceof RecyclerView) {
                return (RecyclerView) view;
            } else {
                return findRecyclerView(v, false);
            }
        } else {
            ViewParent parent = v.getParent();
            if (null == parent) {
                return null;
            } else if (parent instanceof RecyclerView) {
                if (((RecyclerView) parent).getId() == R.id.module_leanback_legp_list) {
                    return (RecyclerView) parent;
                } else {
                    return null;
                }
            } else {
                return findRecyclerView((View) parent, false);
            }
        }
    }

    protected boolean initScrollHorizontally() {
        return true;
    }

    protected boolean initScrollVertically() {
        return true;
    }

    protected abstract void onBindHolder(@NonNull Context context, @NonNull View view, @NonNull T item, @NonNull int position);

    protected void onClickHolder(@NonNull Context context, @NonNull View v, @NonNull T item, @NonNull int checkedIndex, @NonNull int playingIndex, boolean isFromUser) {
    }

    @LayoutRes
    protected abstract int initLayout(int viewType);

    /*********************/

    public final boolean isPlayingPositionLast() {
        try {
            int playingIndex = -1;
            for (T t : mData) {
                if (null == t)
                    continue;
                if (t.isPlaying()) {
                    playingIndex = mData.indexOf(t);
                    break;
                }
            }
            if (playingIndex < 0)
                throw new Exception("playingIndex error: " + playingIndex);
            int count = mData.size();
            int nextIndex = playingIndex + 1;
            return nextIndex >= count;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => isPlayingPositionLast => " + e.getMessage());
            return true;
        }
    }

    public final int getPlayingPosition() {
        try {
            int playingIndex = -1;
            for (T t : mData) {
                if (null == t)
                    continue;
                if (t.isPlaying()) {
                    playingIndex = mData.indexOf(t);
                    break;
                }
            }
            if (playingIndex < 0)
                throw new Exception("playingIndex error: " + playingIndex);
            return playingIndex;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => getPlayingPosition => " + e.getMessage());
            return -1;
        }
    }

    public final int getPlayingPositionNext() {
        try {
            boolean isPlayingPositionLast = isPlayingPositionLast();
            if (isPlayingPositionLast)
                throw new Exception("isPlayingPositionLast warning: true");
            int playingPosition = getPlayingPosition();
            if (playingPosition < 0)
                throw new Exception("playingPosition error: " + playingPosition);
            return playingPosition + 1;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => getPlayingPositionNext => " + e.getMessage());
            return -1;
        }
    }

    public final void checkedPlayingPosition(@NonNull View viewGroup,
                                             @NonNull int checkedPosition) {

        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            if (viewGroup.getId() != R.id.module_leanback_legp_root)
                throw new Exception("viewGroup.getId error: not R.id.module_leanback_legp_root");
            int playingIndex = -1;
            int chechedIndex = -1;
            // 1
            int size = mData.size();
            for (int i = 0; i < size; i++) {
                T t = mData.get(i);
                if (null == t)
                    continue;
                if (chechedIndex == -1 && t.isChecked()) {
                    chechedIndex = mData.indexOf(t);
                }
                if (playingIndex == -1 && t.isPlaying()) {
                    playingIndex = mData.indexOf(t);
                }
                t.setPlaying(i == checkedPosition);
                t.setChecked(i == checkedPosition);
            }
            // 2
            RecyclerView recyclerView = viewGroup.findViewById(R.id.module_leanback_legp_list);
            if (chechedIndex != -1) {
                recyclerView.getAdapter().notifyItemRangeChanged(chechedIndex, 1);
            }
            if (playingIndex != -1) {
                recyclerView.getAdapter().notifyItemRangeChanged(playingIndex, 1);
            }
            // 3
            RecyclerView.ViewHolder viewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(checkedPosition);
            if (null == viewHolderForAdapterPosition) {
                recyclerView.scrollToPosition(checkedPosition);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkedPlayingPosition(viewGroup, checkedPosition);
                    }
                }, 50);
            } else {
                T t = mData.get(checkedPosition);
                View v = viewHolderForAdapterPosition.itemView;
                onBindHolder(v.getContext(), v, t, checkedPosition);
                onClickHolder(v.getContext(), v, t, checkedPosition, playingIndex, false);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => checkedPlayingPosition => " + e.getMessage(), e);
        }
    }

    public final void checkedPlayingPositionNext(View viewGroup) {

        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            if (viewGroup.getId() != R.id.module_leanback_legp_root)
                throw new Exception("viewGroup.getId error: not R.id.module_leanback_legp_root");
            int chechedIndex = -1;
            int playingIndex = -1;
            // 1
            int size = mData.size();
            for (int i = 0; i < size; i++) {
                if (chechedIndex != -1 && playingIndex != -1)
                    break;
                T t = mData.get(i);
                if (null == t)
                    continue;
                if (t.isChecked() && t.isPlaying()) {
                    playingIndex = i;
                    break;
                } else if (chechedIndex == -1 && t.isChecked()) {
                    chechedIndex = i;
                } else if (playingIndex == -1 && t.isPlaying()) {
                    playingIndex = i;
                }
            }
            if (playingIndex == -1)
                throw new Exception("playingIndex error: " + playingIndex);
            int nextCheckedPosition = playingIndex + 1;
            if (nextCheckedPosition >= size)
                throw new Exception("nextCheckedPosition error: " + nextCheckedPosition);
            for (int i = 0; i < size; i++) {
                T t = mData.get(i);
                if (null == t)
                    continue;
                t.setPlaying(i == nextCheckedPosition);
                t.setChecked(i == nextCheckedPosition);
            }
            // 2
            RecyclerView recyclerView = viewGroup.findViewById(R.id.module_leanback_legp_list);
            if (chechedIndex != -1) {
                recyclerView.getAdapter().notifyItemRangeChanged(chechedIndex, 1);
            }
            if (playingIndex != -1) {
                recyclerView.getAdapter().notifyItemRangeChanged(playingIndex, 1);
            }
            // 3
            RecyclerView.ViewHolder viewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(nextCheckedPosition);
            if (null == viewHolderForAdapterPosition) {
                recyclerView.scrollToPosition(nextCheckedPosition);
                int finalNextChechedPosition = nextCheckedPosition;
                int finalPlayingIndex = playingIndex;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        T t = mData.get(finalNextChechedPosition);
                        View v = viewHolderForAdapterPosition.itemView;
                        onBindHolder(v.getContext(), v, t, finalNextChechedPosition);
                        onClickHolder(v.getContext(), v, t, finalNextChechedPosition, finalPlayingIndex, false);
                    }
                }, 50);
            } else {
                T t = mData.get(nextCheckedPosition);
                View v = viewHolderForAdapterPosition.itemView;
                onBindHolder(v.getContext(), v, t, nextCheckedPosition);
                onClickHolder(v.getContext(), v, t, nextCheckedPosition, playingIndex, false);
            }
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => checkedPlayingPositionNext => " + e.getMessage(), e);
        }
    }

    public final boolean dispatchKeyEventCheckedPosition(View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            if (viewGroup.getId() != R.id.module_leanback_legp_root)
                throw new Exception("viewGroup.getId error: not R.id.module_leanback_legp_root");
            int checkedPosition = -1;
            for (T t : mData) {
                if (null == t)
                    continue;
                if (t.isChecked()) {
                    checkedPosition = mData.indexOf(t);
                }
            }
            if (checkedPosition < 0)
                throw new Exception("checkedPosition error: " + checkedPosition);
            RecyclerView recyclerView = viewGroup.findViewById(R.id.module_leanback_legp_list);
            if (null == recyclerView)
                throw new Exception("recyclerView error: " + viewGroup);

            RecyclerView.ViewHolder viewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(checkedPosition);
            if (null == viewHolderForAdapterPosition) {
                recyclerView.scrollToPosition(checkedPosition);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dispatchKeyEventCheckedPosition(viewGroup);
                    }
                }, 50);
            } else {
                viewHolderForAdapterPosition.itemView.requestFocus();
            }
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => dispatchKeyEventCheckedPosition => " + e.getMessage());
            return false;
        }
    }

    public final boolean checkedPlayingPositionRepeat(@NonNull View viewGroup) {
        try {
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            if (viewGroup.getId() != R.id.module_leanback_legp_root)
                throw new Exception("viewGroup.getId error: not R.id.module_leanback_legp_root");
            int playingPosition = getPlayingPosition();
            if (playingPosition < 0)
                throw new Exception("playingPosition error: " + playingPosition);
            checkedPlayingPosition(viewGroup, playingPosition);
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("ListTvEpisodesGridPresenter => checkedPlayingPositionRepeat => " + e.getMessage());
            return false;
        }
    }
}
