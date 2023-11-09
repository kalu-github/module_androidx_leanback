/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package androidx.leanback.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.R;

/**
 * A presenter that renders objects in a {@link VerticalGridView}.
 */
public class VerticalGridPresenter extends Presenter {
    private static final String TAG = "GridPresenter";
    private static final boolean DEBUG = false;

    class VerticalGridItemBridgeAdapter extends ItemBridgeAdapter {
        @Override
        protected void onCreate(ItemBridgeAdapter.ViewHolder viewHolder) {
        }

        @Override
        public void onBind(final ItemBridgeAdapter.ViewHolder itemViewHolder) {
            // Only when having an OnItemClickListener, we attach the OnClickListener.
        }

        @Override
        public void onUnbind(ItemBridgeAdapter.ViewHolder viewHolder) {
        }

        @Override
        public void onAttachedToWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
            viewHolder.itemView.setActivated(true);
        }
    }

    /**
     * ViewHolder for the VerticalGridPresenter.
     */
    public static class ViewHolder extends Presenter.ViewHolder {
        ItemBridgeAdapter mItemBridgeAdapter;
        final VerticalGridView mGridView;
        boolean mInitialized;

        public ViewHolder(@NonNull VerticalGridView view) {
            super(view);
            mGridView = view;
        }

        @NonNull
        public VerticalGridView getGridView() {
            return mGridView;
        }
    }

    private int mNumColumns = -1;
    private boolean mUseFocusDimmer;
    private boolean mKeepChildForeground = true;
    private boolean mRoundedCornersEnabled = true;

    /**
     * Constructs a VerticalGridPresenter with defaults.
     * enabled dimming on focus.
     */
    public VerticalGridPresenter() {
        this(true);
    }

    /**
     * Constructs a VerticalGridPresenter with the given parameters.
     *
     * @param useFocusDimmer determines if the FocusHighlighter will use the dimmer
     */
    public VerticalGridPresenter(boolean useFocusDimmer) {
        mUseFocusDimmer = useFocusDimmer;
    }

    /**
     * Sets the number of columns in the vertical grid.
     */
    public void setNumberOfColumns(int numColumns) {
        if (numColumns < 0) {
            throw new IllegalArgumentException("Invalid number of columns");
        }
        if (mNumColumns != numColumns) {
            mNumColumns = numColumns;
        }
    }

    /**
     * Returns the number of columns in the vertical grid.
     */
    public int getNumberOfColumns() {
        return mNumColumns;
    }

    /**
     * Enables or disabled rounded corners on children of this row.
     * Supported on Android SDK >= L.
     */
    public final void enableChildRoundedCorners(boolean enable) {
        mRoundedCornersEnabled = enable;
    }

    /**
     * Returns true if rounded corners are enabled for children of this row.
     */
    public final boolean areChildRoundedCornersEnabled() {
        return mRoundedCornersEnabled;
    }

    /**
     * Returns true if the focus dimmer is used for focus highlighting; false otherwise.
     */
    public final boolean isFocusDimmerUsed() {
        return mUseFocusDimmer;
    }

    @NonNull
    @Override
    public final ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        ViewHolder vh = createGridViewHolder(parent);
        vh.mInitialized = false;
        vh.mItemBridgeAdapter = new VerticalGridItemBridgeAdapter();
        initializeGridViewHolder(vh);
        if (!vh.mInitialized) {
            throw new RuntimeException("super.initializeGridViewHolder() must be called");
        }
        return vh;
    }

    /**
     * Subclass may override this to inflate a different layout.
     */
    @NonNull
    protected ViewHolder createGridViewHolder(@NonNull ViewGroup parent) {
        View root = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.lb_vertical_grid, parent, false);
        return new ViewHolder((VerticalGridView) root.findViewById(R.id.browse_grid));
    }

    /**
     * Called after a {@link VerticalGridPresenter.ViewHolder} is created.
     * Subclasses may override this method and start by calling
     * super.initializeGridViewHolder(ViewHolder).
     *
     * @param vh The ViewHolder to initialize for the vertical grid.
     */
    protected void initializeGridViewHolder(@NonNull ViewHolder vh) {
        if (mNumColumns == -1) {
            throw new IllegalStateException("Number of columns must be set");
        }
        if (DEBUG) Log.v(TAG, "mNumColumns " + mNumColumns);
        vh.getGridView().setNumColumns(mNumColumns);
        vh.mInitialized = true;
    }

    /**
     * Set if keeps foreground of child of this grid, the foreground will not
     * be used for overlay color.  Default value is true.
     *
     * @param keep   True if keep foreground of child of this grid.
     */
    public final void setKeepChildForeground(boolean keep) {
        mKeepChildForeground = keep;
    }

    /**
     * Returns true if keeps foreground of child of this grid, the foreground will not
     * be used for overlay color.  Default value is true.
     *
     * @return   True if keeps foreground of child of this grid.
     */
    public final boolean getKeepChildForeground() {
        return mKeepChildForeground;
    }

    @Override
    public void onBindViewHolder(@NonNull Presenter.ViewHolder viewHolder, @Nullable Object item) {
        if (DEBUG) Log.v(TAG, "onBindViewHolder " + item);
        ViewHolder vh = (ViewHolder) viewHolder;
        vh.mItemBridgeAdapter.setAdapter((ObjectAdapter) item);
        vh.getGridView().setAdapter(vh.mItemBridgeAdapter);
    }

    @Override
    public void onUnbindViewHolder(@NonNull Presenter.ViewHolder viewHolder) {
        if (DEBUG) Log.v(TAG, "onUnbindViewHolder");
        ViewHolder vh = (ViewHolder) viewHolder;
        vh.mItemBridgeAdapter.setAdapter(null);
        vh.getGridView().setAdapter(null);
    }

    /**
     * Changes the visibility of views.  The entrance transition will be run against the views that
     * change visibilities.  This method is called by the fragment, it should not be called
     * directly by the application.
     *
     * @param holder         The ViewHolder for the vertical grid.
     * @param afterEntrance  true if children of vertical grid participating in entrance transition
     *                       should be set to visible, false otherwise.
     */
    public void setEntranceTransitionState(@NonNull ViewHolder holder,
            boolean afterEntrance) {
        holder.mGridView.setChildrenVisibility(afterEntrance? View.VISIBLE : View.INVISIBLE);
    }
}
