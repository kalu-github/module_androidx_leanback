//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.collection.CircularIntArray;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.CollectionInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class GridLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "GridLayoutManager";
    static final boolean DEBUG = false;
    static final boolean TRACE = false;
    static final int DEFAULT_MAX_PENDING_MOVES = 10;
    float mSmoothScrollSpeedFactor;
    int mMaxPendingMoves;
    static final int MIN_MS_SMOOTH_SCROLL_MAIN_SCREEN = 30;
    BaseGridView mBaseGridView;
    int mOrientation;
    private OrientationHelper mOrientationHelper;
    private int mSaveContextLevel;
    RecyclerView.State mState;
    int mPositionDeltaInPreLayout;
    int mExtraLayoutSpaceInPreLayout;
    final SparseIntArray mPositionToRowInPostLayout;
    int[] mDisappearingPositions;
    AudioManager mAudioManager;
    RecyclerView.Recycler mRecycler;
    private static final Rect sTempRect = new Rect();
    static final int PF_STAGE_MASK = 3;
    static final int PF_STAGE_LAYOUT = 1;
    static final int PF_STAGE_SCROLL = 2;
    static final int PF_FAST_RELAYOUT = 4;
    static final int PF_FAST_RELAYOUT_UPDATED_SELECTED_POSITION = 8;
    static final int PF_IN_LAYOUT_SEARCH_FOCUS = 16;
    static final int PF_IN_SELECTION = 32;
    static final int PF_SLIDING = 64;
    static final int PF_LAYOUT_EATEN_IN_SLIDING = 128;
    static final int PF_FORCE_FULL_LAYOUT = 256;
    static final int PF_LAYOUT_ENABLED = 512;
    static final int PF_ROW_SECONDARY_SIZE_REFRESH = 1024;
    static final int PF_FOCUS_OUT_FRONT = 2048;
    static final int PF_FOCUS_OUT_BACK = 4096;
    static final int PF_FOCUS_OUT_MASKS = 6144;
    static final int PF_FOCUS_OUT_SIDE_START = 8192;
    static final int PF_FOCUS_OUT_SIDE_END = 16384;
    static final int PF_FOCUS_OUT_SIDE_MASKS = 24576;
    static final int PF_FOCUS_SEARCH_DISABLED = 32768;
    static final int PF_PRUNE_CHILD = 65536;
    static final int PF_SCROLL_ENABLED = 131072;
    static final int PF_REVERSE_FLOW_PRIMARY = 262144;
    static final int PF_REVERSE_FLOW_SECONDARY = 524288;
    static final int PF_REVERSE_FLOW_MASK = 786432;
    int mFlag;
    private OnChildSelectedListener mChildSelectedListener;
    private ArrayList<OnChildViewHolderSelectedListener> mChildViewHolderSelectedListeners;
    @VisibleForTesting
    ArrayList<BaseGridView.OnLayoutCompletedListener> mOnLayoutCompletedListeners;
    OnChildLaidOutListener mChildLaidOutListener;
    int mFocusPosition;
    int mSubFocusPosition;
    GridLinearSmoothScroller mCurrentSmoothScroller;
    PendingMoveSmoothScroller mPendingMoveSmoothScroller;
    private int mFocusPositionOffset;
    private int mPrimaryScrollExtra;
    int mChildVisibility;
    int mScrollOffsetSecondary;
    private int mRowSizeSecondaryRequested;
    private int mFixedRowSizeSecondary;
    private int[] mRowSizeSecondary;
    private int mMaxSizeSecondary;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private int mSpacingPrimary;
    private int mSpacingSecondary;
    private int mGravity;
    int mNumRows;
    private int mNumRowsRequested;
    Grid mGrid;
    private int mFocusScrollStrategy;
    final WindowAlignment mWindowAlignment;
    private final ItemAlignment mItemAlignment;
    private int mSizePrimary;
    private int mExtraLayoutSpace;
    static int[] sTwoInts = new int[2];
    private final int[] mMeasuredDimension;
    final ViewsStateBundle mChildrenStates;
    private FacetProviderAdapter mFacetProviderAdapter;
    private final Runnable mRequestLayoutRunnable;
    private final Grid.Provider mGridProvider;
    private static final int PREV_ITEM = 0;
    private static final int NEXT_ITEM = 1;
    private static final int PREV_ROW = 2;
    private static final int NEXT_ROW = 3;

    String getTag() {
        return "GridLayoutManager:" + this.mBaseGridView.getId();
    }

    public GridLayoutManager() {
        this((BaseGridView)null);
    }

    @SuppressLint({"WrongConstant"})
    GridLayoutManager(@Nullable BaseGridView baseGridView) {
        this.mSmoothScrollSpeedFactor = 1.0F;
        this.mMaxPendingMoves = 10;
        this.mOrientation = 0;
        this.mOrientationHelper = OrientationHelper.createHorizontalHelper(this);
        this.mPositionToRowInPostLayout = new SparseIntArray();
        this.mFlag = 221696;
        this.mChildSelectedListener = null;
        this.mChildViewHolderSelectedListeners = null;
        this.mOnLayoutCompletedListeners = null;
        this.mChildLaidOutListener = null;
        this.mFocusPosition = -1;
        this.mSubFocusPosition = 0;
        this.mFocusPositionOffset = 0;
        this.mGravity = 8388659;
        this.mNumRowsRequested = 1;
        this.mFocusScrollStrategy = 0;
        this.mWindowAlignment = new WindowAlignment();
        this.mItemAlignment = new ItemAlignment();
        this.mMeasuredDimension = new int[2];
        this.mChildrenStates = new ViewsStateBundle();
        this.mRequestLayoutRunnable = new Runnable() {
            public void run() {
                GridLayoutManager.this.requestLayout();
            }
        };
        this.mGridProvider = new Grid.Provider() {
            public int getMinIndex() {
                return GridLayoutManager.this.mPositionDeltaInPreLayout;
            }

            public int getCount() {
                return GridLayoutManager.this.mState.getItemCount() + GridLayoutManager.this.mPositionDeltaInPreLayout;
            }

            public int createItem(int index, boolean append, Object[] item, boolean disappearingItem) {
                View v = GridLayoutManager.this.getViewForPosition(index - GridLayoutManager.this.mPositionDeltaInPreLayout);
                LayoutParams lp = (LayoutParams)v.getLayoutParams();
                if (!lp.isItemRemoved()) {
                    if (disappearingItem) {
                        if (append) {
                            GridLayoutManager.this.addDisappearingView(v);
                        } else {
                            GridLayoutManager.this.addDisappearingView(v, 0);
                        }
                    } else if (append) {
                        GridLayoutManager.this.addView(v);
                    } else {
                        GridLayoutManager.this.addView(v, 0);
                    }

                    if (GridLayoutManager.this.mChildVisibility != -1) {
                        v.setVisibility(GridLayoutManager.this.mChildVisibility);
                    }

                    if (GridLayoutManager.this.mPendingMoveSmoothScroller != null) {
                        GridLayoutManager.this.mPendingMoveSmoothScroller.consumePendingMovesBeforeLayout();
                    }

                    int subindex = GridLayoutManager.this.getSubPositionByView(v, v.findFocus());
                    if ((GridLayoutManager.this.mFlag & 3) != 1) {
                        if (index == GridLayoutManager.this.mFocusPosition && subindex == GridLayoutManager.this.mSubFocusPosition && GridLayoutManager.this.mPendingMoveSmoothScroller == null) {
                            GridLayoutManager.this.dispatchChildSelected();
                        }
                    } else if ((GridLayoutManager.this.mFlag & 4) == 0) {
                        if ((GridLayoutManager.this.mFlag & 16) == 0 && index == GridLayoutManager.this.mFocusPosition && subindex == GridLayoutManager.this.mSubFocusPosition) {
                            GridLayoutManager.this.dispatchChildSelected();
                        } else if ((GridLayoutManager.this.mFlag & 16) != 0 && index >= GridLayoutManager.this.mFocusPosition && v.hasFocusable()) {
                            GridLayoutManager.this.mFocusPosition = index;
                            GridLayoutManager.this.mSubFocusPosition = subindex;
                            GridLayoutManager var10000 = GridLayoutManager.this;
                            var10000.mFlag &= -17;
                            GridLayoutManager.this.dispatchChildSelected();
                        }
                    }

                    GridLayoutManager.this.measureChild(v);
                }

                item[0] = v;
                return GridLayoutManager.this.mOrientation == 0 ? GridLayoutManager.this.getDecoratedMeasuredWidthWithMargin(v) : GridLayoutManager.this.getDecoratedMeasuredHeightWithMargin(v);
            }

            public void addItem(Object item, int index, int length, int rowIndex, int edge) {
                View v = (View)item;
                if (edge == Integer.MIN_VALUE || edge == Integer.MAX_VALUE) {
                    edge = !GridLayoutManager.this.mGrid.isReversedFlow() ? GridLayoutManager.this.mWindowAlignment.mainAxis().getPaddingMin() : GridLayoutManager.this.mWindowAlignment.mainAxis().getSize() - GridLayoutManager.this.mWindowAlignment.mainAxis().getPaddingMax();
                }

                boolean edgeIsMin = !GridLayoutManager.this.mGrid.isReversedFlow();
                int start;
                int end;
                if (edgeIsMin) {
                    start = edge;
                    end = edge + length;
                } else {
                    start = edge - length;
                    end = edge;
                }

                int startSecondary = GridLayoutManager.this.getRowStartSecondary(rowIndex) + GridLayoutManager.this.mWindowAlignment.secondAxis().getPaddingMin() - GridLayoutManager.this.mScrollOffsetSecondary;
                GridLayoutManager.this.mChildrenStates.loadView(v, index);
                GridLayoutManager.this.layoutChild(rowIndex, v, start, end, startSecondary);
                if (!GridLayoutManager.this.mState.isPreLayout()) {
                    GridLayoutManager.this.updateScrollLimits();
                }

                if ((GridLayoutManager.this.mFlag & 3) != 1 && GridLayoutManager.this.mPendingMoveSmoothScroller != null) {
                    GridLayoutManager.this.mPendingMoveSmoothScroller.consumePendingMovesAfterLayout();
                }

                if (GridLayoutManager.this.mChildLaidOutListener != null) {
                    RecyclerView.ViewHolder vh = GridLayoutManager.this.mBaseGridView.getChildViewHolder(v);
                    GridLayoutManager.this.mChildLaidOutListener.onChildLaidOut(GridLayoutManager.this.mBaseGridView, v, index, vh == null ? -1L : vh.getItemId());
                }

            }

            public void removeItem(int index) {
                View v = GridLayoutManager.this.findViewByPosition(index - GridLayoutManager.this.mPositionDeltaInPreLayout);
                if ((GridLayoutManager.this.mFlag & 3) == 1) {
                    GridLayoutManager.this.detachAndScrapView(v, GridLayoutManager.this.mRecycler);
                } else {
                    GridLayoutManager.this.removeAndRecycleView(v, GridLayoutManager.this.mRecycler);
                }

            }

            public int getEdge(int index) {
                View v = GridLayoutManager.this.findViewByPosition(index - GridLayoutManager.this.mPositionDeltaInPreLayout);
                return (GridLayoutManager.this.mFlag & 262144) != 0 ? GridLayoutManager.this.getViewMax(v) : GridLayoutManager.this.getViewMin(v);
            }

            public int getSize(int index) {
                return GridLayoutManager.this.getViewPrimarySize(GridLayoutManager.this.findViewByPosition(index - GridLayoutManager.this.mPositionDeltaInPreLayout));
            }
        };
        this.mBaseGridView = baseGridView;
        this.mChildVisibility = -1;
        this.setItemPrefetchEnabled(false);
    }

    void setGridView(BaseGridView baseGridView) {
        this.mBaseGridView = baseGridView;
        this.mGrid = null;
    }

    public void setOrientation(int orientation) {
        if (orientation == 0 || orientation == 1) {
            this.mOrientation = orientation;
            this.mOrientationHelper = OrientationHelper.createOrientationHelper(this, this.mOrientation);
            this.mWindowAlignment.setOrientation(orientation);
            this.mItemAlignment.setOrientation(orientation);
            this.mFlag |= 256;
        }
    }

    public void setFocusOutAllowed(boolean throughFront, boolean throughBack) {
        this.mFlag = this.mFlag & -6145 | (throughFront ? 2048 : 0) | (throughBack ? 4096 : 0);
    }

    void onRtlPropertiesChanged(int layoutDirection) {
        int flags;
        if (this.mOrientation == 0) {
            flags = layoutDirection == 1 ? 262144 : 0;
        } else {
            flags = layoutDirection == 1 ? 524288 : 0;
        }

        if ((this.mFlag & 786432) != flags) {
            this.mFlag = this.mFlag & -786433 | flags;
            this.mFlag |= 256;
            this.mWindowAlignment.horizontal.setReversedFlow(layoutDirection == 1);
        }
    }

    int getFocusScrollStrategy() {
        return this.mFocusScrollStrategy;
    }

    void setFocusScrollStrategy(int focusScrollStrategy) {
        this.mFocusScrollStrategy = focusScrollStrategy;
    }

    void setWindowAlignment(int windowAlignment) {
        this.mWindowAlignment.mainAxis().setWindowAlignment(windowAlignment);
    }

    int getWindowAlignment() {
        return this.mWindowAlignment.mainAxis().getWindowAlignment();
    }

    void setWindowAlignmentOffset(int alignmentOffset) {
        this.mWindowAlignment.mainAxis().setWindowAlignmentOffset(alignmentOffset);
    }

    int getWindowAlignmentOffset() {
        return this.mWindowAlignment.mainAxis().getWindowAlignmentOffset();
    }

    void setWindowAlignmentOffsetPercent(float offsetPercent) {
        this.mWindowAlignment.mainAxis().setWindowAlignmentOffsetPercent(offsetPercent);
    }

    float getWindowAlignmentOffsetPercent() {
        return this.mWindowAlignment.mainAxis().getWindowAlignmentOffsetPercent();
    }

    void setItemAlignmentOffset(int alignmentOffset) {
        this.mItemAlignment.mainAxis().setItemAlignmentOffset(alignmentOffset);
        this.updateChildAlignments();
    }

    int getItemAlignmentOffset() {
        return this.mItemAlignment.mainAxis().getItemAlignmentOffset();
    }

    void setItemAlignmentOffsetWithPadding(boolean withPadding) {
        this.mItemAlignment.mainAxis().setItemAlignmentOffsetWithPadding(withPadding);
        this.updateChildAlignments();
    }

    boolean isItemAlignmentOffsetWithPadding() {
        return this.mItemAlignment.mainAxis().isItemAlignmentOffsetWithPadding();
    }

    void setItemAlignmentOffsetPercent(float offsetPercent) {
        this.mItemAlignment.mainAxis().setItemAlignmentOffsetPercent(offsetPercent);
        this.updateChildAlignments();
    }

    float getItemAlignmentOffsetPercent() {
        return this.mItemAlignment.mainAxis().getItemAlignmentOffsetPercent();
    }

    void setItemAlignmentViewId(int viewId) {
        this.mItemAlignment.mainAxis().setItemAlignmentViewId(viewId);
        this.updateChildAlignments();
    }

    int getItemAlignmentViewId() {
        return this.mItemAlignment.mainAxis().getItemAlignmentViewId();
    }

    void setFocusOutSideAllowed(boolean throughStart, boolean throughEnd) {
        this.mFlag = this.mFlag & -24577 | (throughStart ? 8192 : 0) | (throughEnd ? 16384 : 0);
    }

    void setNumRows(int numRows) {
        if (numRows < 0) {
            throw new IllegalArgumentException();
        } else {
            this.mNumRowsRequested = numRows;
        }
    }

    void setRowHeight(int height) {
        if (height < 0 && height != -2) {
            throw new IllegalArgumentException("Invalid row height: " + height);
        } else {
            this.mRowSizeSecondaryRequested = height;
        }
    }

    void setItemSpacing(int space) {
        this.mVerticalSpacing = this.mHorizontalSpacing = space;
        this.mSpacingPrimary = this.mSpacingSecondary = space;
    }

    void setVerticalSpacing(int space) {
        if (this.mOrientation == 1) {
            this.mSpacingPrimary = this.mVerticalSpacing = space;
        } else {
            this.mSpacingSecondary = this.mVerticalSpacing = space;
        }

    }

    void setHorizontalSpacing(int space) {
        if (this.mOrientation == 0) {
            this.mSpacingPrimary = this.mHorizontalSpacing = space;
        } else {
            this.mSpacingSecondary = this.mHorizontalSpacing = space;
        }

    }

    int getVerticalSpacing() {
        return this.mVerticalSpacing;
    }

    int getHorizontalSpacing() {
        return this.mHorizontalSpacing;
    }

    void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    boolean hasDoneFirstLayout() {
        return this.mGrid != null;
    }

    void setOnChildSelectedListener(OnChildSelectedListener listener) {
        this.mChildSelectedListener = listener;
    }

    void setOnChildViewHolderSelectedListener(OnChildViewHolderSelectedListener listener) {
        if (listener == null) {
            this.mChildViewHolderSelectedListeners = null;
        } else {
            if (this.mChildViewHolderSelectedListeners == null) {
                this.mChildViewHolderSelectedListeners = new ArrayList();
            } else {
                this.mChildViewHolderSelectedListeners.clear();
            }

            this.mChildViewHolderSelectedListeners.add(listener);
        }
    }

    void addOnChildViewHolderSelectedListener(OnChildViewHolderSelectedListener listener) {
        if (this.mChildViewHolderSelectedListeners == null) {
            this.mChildViewHolderSelectedListeners = new ArrayList();
        }

        this.mChildViewHolderSelectedListeners.add(listener);
    }

    void removeOnChildViewHolderSelectedListener(OnChildViewHolderSelectedListener listener) {
        if (this.mChildViewHolderSelectedListeners != null) {
            this.mChildViewHolderSelectedListeners.remove(listener);
        }

    }

    boolean hasOnChildViewHolderSelectedListener() {
        return this.mChildViewHolderSelectedListeners != null && this.mChildViewHolderSelectedListeners.size() > 0;
    }

    void fireOnChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
        if (this.mChildViewHolderSelectedListeners != null) {
            for(int i = this.mChildViewHolderSelectedListeners.size() - 1; i >= 0; --i) {
                ((OnChildViewHolderSelectedListener)this.mChildViewHolderSelectedListeners.get(i)).onChildViewHolderSelected(parent, child, position, subposition);
            }

        }
    }

    void fireOnChildViewHolderSelectedAndPositioned(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
        if (this.mChildViewHolderSelectedListeners != null) {
            for(int i = this.mChildViewHolderSelectedListeners.size() - 1; i >= 0; --i) {
                ((OnChildViewHolderSelectedListener)this.mChildViewHolderSelectedListeners.get(i)).onChildViewHolderSelectedAndPositioned(parent, child, position, subposition);
            }

        }
    }

    void addOnLayoutCompletedListener(BaseGridView.OnLayoutCompletedListener listener) {
        if (this.mOnLayoutCompletedListeners == null) {
            this.mOnLayoutCompletedListeners = new ArrayList();
        }

        this.mOnLayoutCompletedListeners.add(listener);
    }

    void removeOnLayoutCompletedListener(BaseGridView.OnLayoutCompletedListener listener) {
        if (this.mOnLayoutCompletedListeners != null) {
            this.mOnLayoutCompletedListeners.remove(listener);
        }

    }

    void setOnChildLaidOutListener(OnChildLaidOutListener listener) {
        this.mChildLaidOutListener = listener;
    }

    private int getAdapterPositionByView(View view) {
        if (view == null) {
            return -1;
        } else {
            LayoutParams params = (LayoutParams)view.getLayoutParams();
            return params != null && !params.isItemRemoved() ? params.getAbsoluteAdapterPosition() : -1;
        }
    }

    int getSubPositionByView(View view, View childView) {
        if (view != null && childView != null) {
            LayoutParams lp = (LayoutParams)view.getLayoutParams();
            ItemAlignmentFacet facet = lp.getItemAlignmentFacet();
            if (facet != null) {
                ItemAlignmentFacet.ItemAlignmentDef[] defs = facet.getAlignmentDefs();
                if (defs.length > 1) {
                    for(; childView != view; childView = (View)childView.getParent()) {
                        int id = childView.getId();
                        if (id != -1) {
                            for(int i = 1; i < defs.length; ++i) {
                                if (defs[i].getItemAlignmentFocusViewId() == id) {
                                    return i;
                                }
                            }
                        }
                    }
                }
            }

            return 0;
        } else {
            return 0;
        }
    }

    private int getAdapterPositionByIndex(int index) {
        return this.getAdapterPositionByView(this.getChildAt(index));
    }

    void dispatchChildSelected() {
        if (this.mChildSelectedListener != null || this.hasOnChildViewHolderSelectedListener()) {
            View view = this.mFocusPosition == -1 ? null : this.findViewByPosition(this.mFocusPosition);
            if (view != null) {
                RecyclerView.ViewHolder vh = this.mBaseGridView.getChildViewHolder(view);
                if (this.mChildSelectedListener != null) {
                    this.mChildSelectedListener.onChildSelected(this.mBaseGridView, view, this.mFocusPosition, vh == null ? -1L : vh.getItemId());
                }

                this.fireOnChildViewHolderSelected(this.mBaseGridView, vh, this.mFocusPosition, this.mSubFocusPosition);
            } else {
                if (this.mChildSelectedListener != null) {
                    this.mChildSelectedListener.onChildSelected(this.mBaseGridView, (View)null, -1, -1L);
                }

                this.fireOnChildViewHolderSelected(this.mBaseGridView, (RecyclerView.ViewHolder)null, -1, 0);
            }

            if ((this.mFlag & 3) != 1 && !this.mBaseGridView.isLayoutRequested()) {
                int childCount = this.getChildCount();

                for(int i = 0; i < childCount; ++i) {
                    if (this.getChildAt(i).isLayoutRequested()) {
                        this.forceRequestLayout();
                        break;
                    }
                }
            }

        }
    }

    void dispatchChildSelectedAndPositioned() {
        if (this.hasOnChildViewHolderSelectedListener()) {
            View view = this.mFocusPosition == -1 ? null : this.findViewByPosition(this.mFocusPosition);
            if (view != null) {
                RecyclerView.ViewHolder vh = this.mBaseGridView.getChildViewHolder(view);
                this.fireOnChildViewHolderSelectedAndPositioned(this.mBaseGridView, vh, this.mFocusPosition, this.mSubFocusPosition);
            } else {
                if (this.mChildSelectedListener != null) {
                    this.mChildSelectedListener.onChildSelected(this.mBaseGridView, (View)null, -1, -1L);
                }

                this.fireOnChildViewHolderSelectedAndPositioned(this.mBaseGridView, (RecyclerView.ViewHolder)null, -1, 0);
            }

        }
    }

    public boolean checkLayoutParams(@Nullable RecyclerView.LayoutParams lp) {
        return lp instanceof LayoutParams;
    }

    public boolean canScrollHorizontally() {
        return this.mOrientation == 0 || this.mNumRows > 1;
    }

    public boolean canScrollVertically() {
        return this.mOrientation == 1 || this.mNumRows > 1;
    }

    @NonNull
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @NonNull
    public RecyclerView.LayoutParams generateLayoutParams(@NonNull Context context, @NonNull AttributeSet attrs) {
        return new LayoutParams(context, attrs);
    }

    @NonNull
    public RecyclerView.LayoutParams generateLayoutParams(@NonNull ViewGroup.LayoutParams lp) {
        if (lp instanceof LayoutParams) {
            return new LayoutParams((LayoutParams)lp);
        } else if (lp instanceof RecyclerView.LayoutParams) {
            return new LayoutParams((RecyclerView.LayoutParams)lp);
        } else {
            return lp instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams)lp) : new LayoutParams(lp);
        }
    }

    View getViewForPosition(int position) {
        View v = this.mRecycler.getViewForPosition(position);
        LayoutParams lp = (LayoutParams)v.getLayoutParams();
        RecyclerView.ViewHolder vh = this.mBaseGridView.getChildViewHolder(v);
        lp.setItemAlignmentFacet((ItemAlignmentFacet)this.getFacet(vh, ItemAlignmentFacet.class));
        return v;
    }

    int getOpticalLeft(View v) {
        return ((LayoutParams)v.getLayoutParams()).getOpticalLeft(v);
    }

    int getOpticalRight(View v) {
        return ((LayoutParams)v.getLayoutParams()).getOpticalRight(v);
    }

    int getOpticalTop(View v) {
        return ((LayoutParams)v.getLayoutParams()).getOpticalTop(v);
    }

    int getOpticalBottom(View v) {
        return ((LayoutParams)v.getLayoutParams()).getOpticalBottom(v);
    }

    public int getDecoratedLeft(@NonNull View child) {
        return super.getDecoratedLeft(child) + ((LayoutParams)child.getLayoutParams()).mLeftInset;
    }

    public int getDecoratedTop(@NonNull View child) {
        return super.getDecoratedTop(child) + ((LayoutParams)child.getLayoutParams()).mTopInset;
    }

    public int getDecoratedRight(@NonNull View child) {
        return super.getDecoratedRight(child) - ((LayoutParams)child.getLayoutParams()).mRightInset;
    }

    public int getDecoratedBottom(@NonNull View child) {
        return super.getDecoratedBottom(child) - ((LayoutParams)child.getLayoutParams()).mBottomInset;
    }

    public void getDecoratedBoundsWithMargins(@NonNull View view, @NonNull Rect outBounds) {
        super.getDecoratedBoundsWithMargins(view, outBounds);
        LayoutParams params = (LayoutParams)view.getLayoutParams();
        outBounds.left += params.mLeftInset;
        outBounds.top += params.mTopInset;
        outBounds.right -= params.mRightInset;
        outBounds.bottom -= params.mBottomInset;
    }

    int getViewMin(View v) {
        return this.mOrientationHelper.getDecoratedStart(v);
    }

    int getViewMax(View v) {
        return this.mOrientationHelper.getDecoratedEnd(v);
    }

    int getViewPrimarySize(View view) {
        this.getDecoratedBoundsWithMargins(view, sTempRect);
        return this.mOrientation == 0 ? sTempRect.width() : sTempRect.height();
    }

    private int getViewCenter(View view) {
        return this.mOrientation == 0 ? this.getViewCenterX(view) : this.getViewCenterY(view);
    }

    private int getViewCenterSecondary(View view) {
        return this.mOrientation == 0 ? this.getViewCenterY(view) : this.getViewCenterX(view);
    }

    private int getViewCenterX(View v) {
        LayoutParams p = (LayoutParams)v.getLayoutParams();
        return p.getOpticalLeft(v) + p.getAlignX();
    }

    private int getViewCenterY(View v) {
        LayoutParams p = (LayoutParams)v.getLayoutParams();
        return p.getOpticalTop(v) + p.getAlignY();
    }

    AudioManager getAudioManager() {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager)this.mBaseGridView.getContext().getSystemService("audio");
        }

        return this.mAudioManager;
    }

    private void saveContext(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mSaveContextLevel == 0) {
            this.mRecycler = recycler;
            this.mState = state;
            this.mPositionDeltaInPreLayout = 0;
            this.mExtraLayoutSpaceInPreLayout = 0;
        }

        ++this.mSaveContextLevel;
    }

    private void leaveContext() {
        --this.mSaveContextLevel;
        if (this.mSaveContextLevel == 0) {
            this.mRecycler = null;
            this.mState = null;
            this.mPositionDeltaInPreLayout = 0;
            this.mExtraLayoutSpaceInPreLayout = 0;
        }

    }

    private boolean layoutInit() {
        int newItemCount = this.mState.getItemCount();
        if (newItemCount == 0) {
            this.mFocusPosition = -1;
            this.mSubFocusPosition = 0;
        } else if (this.mFocusPosition >= newItemCount) {
            this.mFocusPosition = newItemCount - 1;
            this.mSubFocusPosition = 0;
        } else if (this.mFocusPosition == -1 && newItemCount > 0) {
            this.mFocusPosition = 0;
            this.mSubFocusPosition = 0;
        }

        if (!this.mState.didStructureChange() && this.mGrid != null && this.mGrid.getFirstVisibleIndex() >= 0 && (this.mFlag & 256) == 0 && this.mGrid.getNumRows() == this.mNumRows) {
            this.updateScrollController();
            this.updateSecondaryScrollLimits();
            this.mGrid.setSpacing(this.mSpacingPrimary);
            return true;
        } else {
            this.mFlag &= -257;
            if (this.mGrid == null || this.mNumRows != this.mGrid.getNumRows() || (this.mFlag & 262144) != 0 != this.mGrid.isReversedFlow()) {
                this.mGrid = Grid.createGrid(this.mNumRows);
                this.mGrid.setProvider(this.mGridProvider);
                this.mGrid.setReversedFlow((this.mFlag & 262144) != 0);
            }

            this.initScrollController();
            this.updateSecondaryScrollLimits();
            this.mGrid.setSpacing(this.mSpacingPrimary);
            this.detachAndScrapAttachedViews(this.mRecycler);
            this.mGrid.resetVisibleIndex();
            this.mWindowAlignment.mainAxis().invalidateScrollMin();
            this.mWindowAlignment.mainAxis().invalidateScrollMax();
            return false;
        }
    }

    private int getRowSizeSecondary(int rowIndex) {
        if (this.mFixedRowSizeSecondary != 0) {
            return this.mFixedRowSizeSecondary;
        } else {
            return this.mRowSizeSecondary == null ? 0 : this.mRowSizeSecondary[rowIndex];
        }
    }

    int getRowStartSecondary(int rowIndex) {
        int start = 0;
        int i;
        if ((this.mFlag & 524288) != 0) {
            for(i = this.mNumRows - 1; i > rowIndex; --i) {
                start += this.getRowSizeSecondary(i) + this.mSpacingSecondary;
            }
        } else {
            for(i = 0; i < rowIndex; ++i) {
                start += this.getRowSizeSecondary(i) + this.mSpacingSecondary;
            }
        }

        return start;
    }

    private int getSizeSecondary() {
        int rightmostIndex = (this.mFlag & 524288) != 0 ? 0 : this.mNumRows - 1;
        return this.getRowStartSecondary(rightmostIndex) + this.getRowSizeSecondary(rightmostIndex);
    }

    int getDecoratedMeasuredWidthWithMargin(View v) {
        LayoutParams lp = (LayoutParams)v.getLayoutParams();
        return this.getDecoratedMeasuredWidth(v) + lp.leftMargin + lp.rightMargin;
    }

    int getDecoratedMeasuredHeightWithMargin(View v) {
        LayoutParams lp = (LayoutParams)v.getLayoutParams();
        return this.getDecoratedMeasuredHeight(v) + lp.topMargin + lp.bottomMargin;
    }

    private void measureScrapChild(int position, int widthSpec, int heightSpec, int[] measuredDimension) {
        View view = this.mRecycler.getViewForPosition(position);
        if (view != null) {
            LayoutParams p = (LayoutParams)view.getLayoutParams();
            this.calculateItemDecorationsForChild(view, sTempRect);
            int widthUsed = p.leftMargin + p.rightMargin + sTempRect.left + sTempRect.right;
            int heightUsed = p.topMargin + p.bottomMargin + sTempRect.top + sTempRect.bottom;
            int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, this.getPaddingLeft() + this.getPaddingRight() + widthUsed, p.width);
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, this.getPaddingTop() + this.getPaddingBottom() + heightUsed, p.height);
            view.measure(childWidthSpec, childHeightSpec);
            measuredDimension[0] = this.getDecoratedMeasuredWidthWithMargin(view);
            measuredDimension[1] = this.getDecoratedMeasuredHeightWithMargin(view);
            this.mRecycler.recycleView(view);
        }

    }

    private boolean processRowSizeSecondary(boolean measure) {
        if (this.mFixedRowSizeSecondary == 0 && this.mRowSizeSecondary != null) {
            CircularIntArray[] rows = this.mGrid == null ? null : this.mGrid.getItemPositionsInRows();
            boolean changed = false;
            int scrapeChildSize = -1;

            for(int rowIndex = 0; rowIndex < this.mNumRows; ++rowIndex) {
                CircularIntArray row = rows == null ? null : rows[rowIndex];
                int rowItemsPairCount = row == null ? 0 : row.size();
                int rowSize = -1;

                int itemCount;
                int position;
                int firstPos;
                int lastPos;
                for(itemCount = 0; itemCount < rowItemsPairCount; itemCount += 2) {
                    position = row.get(itemCount);
                    firstPos = row.get(itemCount + 1);

                    for(lastPos = position; lastPos <= firstPos; ++lastPos) {
                        View view = this.findViewByPosition(lastPos - this.mPositionDeltaInPreLayout);
                        if (view != null) {
                            if (measure) {
                                this.measureChild(view);
                            }

                            int secondarySize = this.mOrientation == 0 ? this.getDecoratedMeasuredHeightWithMargin(view) : this.getDecoratedMeasuredWidthWithMargin(view);
                            if (secondarySize > rowSize) {
                                rowSize = secondarySize;
                            }
                        }
                    }
                }

                itemCount = this.mState.getItemCount();
                if (!this.mBaseGridView.hasFixedSize() && measure && rowSize < 0 && itemCount > 0) {
                    if (scrapeChildSize < 0) {
                        position = this.mFocusPosition;
                        if (position < 0) {
                            position = 0;
                        } else if (position >= itemCount) {
                            position = itemCount - 1;
                        }

                        if (this.getChildCount() > 0) {
                            firstPos = this.mBaseGridView.getChildViewHolder(this.getChildAt(0)).getLayoutPosition();
                            lastPos = this.mBaseGridView.getChildViewHolder(this.getChildAt(this.getChildCount() - 1)).getLayoutPosition();
                            if (position >= firstPos && position <= lastPos) {
                                position = position - firstPos <= lastPos - position ? firstPos - 1 : lastPos + 1;
                                if (position < 0 && lastPos < itemCount - 1) {
                                    position = lastPos + 1;
                                } else if (position >= itemCount && firstPos > 0) {
                                    position = firstPos - 1;
                                }
                            }
                        }

                        if (position >= 0 && position < itemCount) {
                            this.measureScrapChild(position, MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0), this.mMeasuredDimension);
                            scrapeChildSize = this.mOrientation == 0 ? this.mMeasuredDimension[1] : this.mMeasuredDimension[0];
                        }
                    }

                    if (scrapeChildSize >= 0) {
                        rowSize = scrapeChildSize;
                    }
                }

                if (rowSize < 0) {
                    rowSize = 0;
                }

                if (this.mRowSizeSecondary[rowIndex] != rowSize) {
                    this.mRowSizeSecondary[rowIndex] = rowSize;
                    changed = true;
                }
            }

            return changed;
        } else {
            return false;
        }
    }

    private void updateRowSecondarySizeRefresh() {
        this.mFlag = this.mFlag & -1025 | (this.processRowSizeSecondary(false) ? 1024 : 0);
        if ((this.mFlag & 1024) != 0) {
            this.forceRequestLayout();
        }

    }

    private void forceRequestLayout() {
        ViewCompat.postOnAnimation(this.mBaseGridView, this.mRequestLayoutRunnable);
    }

    public void onMeasure(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state, int widthSpec, int heightSpec) {
        this.saveContext(recycler, state);
        int sizePrimary;
        int sizeSecondary;
        int modeSecondary;
        int paddingSecondary;
        if (this.mOrientation == 0) {
            sizePrimary = MeasureSpec.getSize(widthSpec);
            sizeSecondary = MeasureSpec.getSize(heightSpec);
            modeSecondary = MeasureSpec.getMode(heightSpec);
            paddingSecondary = this.getPaddingTop() + this.getPaddingBottom();
        } else {
            sizeSecondary = MeasureSpec.getSize(widthSpec);
            sizePrimary = MeasureSpec.getSize(heightSpec);
            modeSecondary = MeasureSpec.getMode(widthSpec);
            paddingSecondary = this.getPaddingLeft() + this.getPaddingRight();
        }

        this.mMaxSizeSecondary = sizeSecondary;
        int measuredSizeSecondary;
        if (this.mRowSizeSecondaryRequested == -2) {
            this.mNumRows = this.mNumRowsRequested == 0 ? 1 : this.mNumRowsRequested;
            this.mFixedRowSizeSecondary = 0;
            if (this.mRowSizeSecondary == null || this.mRowSizeSecondary.length != this.mNumRows) {
                this.mRowSizeSecondary = new int[this.mNumRows];
            }

            if (this.mState.isPreLayout()) {
                this.updatePositionDeltaInPreLayout();
            }

            this.processRowSizeSecondary(true);
            switch (modeSecondary) {
                case Integer.MIN_VALUE:
                    measuredSizeSecondary = Math.min(this.getSizeSecondary() + paddingSecondary, this.mMaxSizeSecondary);
                    break;
                case 0:
                    measuredSizeSecondary = this.getSizeSecondary() + paddingSecondary;
                    break;
                case 1073741824:
                    measuredSizeSecondary = this.mMaxSizeSecondary;
                    break;
                default:
                    throw new IllegalStateException("wrong spec");
            }
        } else {
            switch (modeSecondary) {
                case Integer.MIN_VALUE:
                case 1073741824:
                    if (this.mNumRowsRequested == 0 && this.mRowSizeSecondaryRequested == 0) {
                        this.mNumRows = 1;
                        this.mFixedRowSizeSecondary = sizeSecondary - paddingSecondary;
                    } else if (this.mNumRowsRequested == 0) {
                        this.mFixedRowSizeSecondary = this.mRowSizeSecondaryRequested;
                        this.mNumRows = (sizeSecondary + this.mSpacingSecondary) / (this.mRowSizeSecondaryRequested + this.mSpacingSecondary);
                    } else if (this.mRowSizeSecondaryRequested == 0) {
                        this.mNumRows = this.mNumRowsRequested;
                        this.mFixedRowSizeSecondary = (sizeSecondary - paddingSecondary - this.mSpacingSecondary * (this.mNumRows - 1)) / this.mNumRows;
                    } else {
                        this.mNumRows = this.mNumRowsRequested;
                        this.mFixedRowSizeSecondary = this.mRowSizeSecondaryRequested;
                    }

                    measuredSizeSecondary = sizeSecondary;
                    if (modeSecondary == Integer.MIN_VALUE) {
                        int childrenSize = this.mFixedRowSizeSecondary * this.mNumRows + this.mSpacingSecondary * (this.mNumRows - 1) + paddingSecondary;
                        if (childrenSize < sizeSecondary) {
                            measuredSizeSecondary = childrenSize;
                        }
                    }
                    break;
                case 0:
                    this.mFixedRowSizeSecondary = this.mRowSizeSecondaryRequested == 0 ? sizeSecondary - paddingSecondary : this.mRowSizeSecondaryRequested;
                    this.mNumRows = this.mNumRowsRequested == 0 ? 1 : this.mNumRowsRequested;
                    measuredSizeSecondary = this.mFixedRowSizeSecondary * this.mNumRows + this.mSpacingSecondary * (this.mNumRows - 1) + paddingSecondary;
                    break;
                default:
                    throw new IllegalStateException("wrong spec");
            }
        }

        if (this.mOrientation == 0) {
            this.setMeasuredDimension(sizePrimary, measuredSizeSecondary);
        } else {
            this.setMeasuredDimension(measuredSizeSecondary, sizePrimary);
        }

        this.leaveContext();
    }

    void measureChild(View child) {
        LayoutParams lp = (LayoutParams)child.getLayoutParams();
        this.calculateItemDecorationsForChild(child, sTempRect);
        int widthUsed = lp.leftMargin + lp.rightMargin + sTempRect.left + sTempRect.right;
        int heightUsed = lp.topMargin + lp.bottomMargin + sTempRect.top + sTempRect.bottom;
        int secondarySpec = this.mRowSizeSecondaryRequested == -2 ? MeasureSpec.makeMeasureSpec(0, 0) : MeasureSpec.makeMeasureSpec(this.mFixedRowSizeSecondary, 1073741824);
        int widthSpec;
        int heightSpec;
        if (this.mOrientation == 0) {
            widthSpec = ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, 0), widthUsed, lp.width);
            heightSpec = ViewGroup.getChildMeasureSpec(secondarySpec, heightUsed, lp.height);
        } else {
            heightSpec = ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, 0), heightUsed, lp.height);
            widthSpec = ViewGroup.getChildMeasureSpec(secondarySpec, widthUsed, lp.width);
        }

        child.measure(widthSpec, heightSpec);
    }

    <E> E getFacet(RecyclerView.ViewHolder vh, Class<? extends E> facetClass) {
        E facet = null;
        if (vh instanceof FacetProvider) {
            facet = ((FacetProvider)vh).getFacet(facetClass);
        }

        if (facet == null && this.mFacetProviderAdapter != null) {
            FacetProvider p = this.mFacetProviderAdapter.getFacetProvider(vh.getItemViewType());
            if (p != null) {
                facet = p.getFacet(facetClass);
            }
        }

        return facet;
    }

    void layoutChild(int rowIndex, View v, int start, int end, int startSecondary) {
        int sizeSecondary = this.mOrientation == 0 ? this.getDecoratedMeasuredHeightWithMargin(v) : this.getDecoratedMeasuredWidthWithMargin(v);
        if (this.mFixedRowSizeSecondary > 0) {
            sizeSecondary = Math.min(sizeSecondary, this.mFixedRowSizeSecondary);
        }

        int verticalGravity = this.mGravity & 112;
        int horizontalGravity = (this.mFlag & 786432) != 0 ? Gravity.getAbsoluteGravity(this.mGravity & 8388615, 1) : this.mGravity & 7;
        if ((this.mOrientation != 0 || verticalGravity != 48) && (this.mOrientation != 1 || horizontalGravity != 3)) {
            if ((this.mOrientation != 0 || verticalGravity != 80) && (this.mOrientation != 1 || horizontalGravity != 5)) {
                if (this.mOrientation == 0 && verticalGravity == 16 || this.mOrientation == 1 && horizontalGravity == 1) {
                    startSecondary += (this.getRowSizeSecondary(rowIndex) - sizeSecondary) / 2;
                }
            } else {
                startSecondary += this.getRowSizeSecondary(rowIndex) - sizeSecondary;
            }
        }

        int left;
        int top;
        int right;
        int bottom;
        if (this.mOrientation == 0) {
            left = start;
            top = startSecondary;
            right = end;
            bottom = startSecondary + sizeSecondary;
        } else {
            top = start;
            left = startSecondary;
            bottom = end;
            right = startSecondary + sizeSecondary;
        }

        LayoutParams params = (LayoutParams)v.getLayoutParams();
        this.layoutDecoratedWithMargins(v, left, top, right, bottom);
        super.getDecoratedBoundsWithMargins(v, sTempRect);
        params.setOpticalInsets(left - sTempRect.left, top - sTempRect.top, sTempRect.right - right, sTempRect.bottom - bottom);
        this.updateChildAlignments(v);
    }

    private void updateChildAlignments(View v) {
        LayoutParams p = (LayoutParams)v.getLayoutParams();
        if (p.getItemAlignmentFacet() == null) {
            p.setAlignX(this.mItemAlignment.horizontal.getAlignmentPosition(v));
            p.setAlignY(this.mItemAlignment.vertical.getAlignmentPosition(v));
        } else {
            p.calculateItemAlignments(this.mOrientation, v);
            if (this.mOrientation == 0) {
                p.setAlignY(this.mItemAlignment.vertical.getAlignmentPosition(v));
            } else {
                p.setAlignX(this.mItemAlignment.horizontal.getAlignmentPosition(v));
            }
        }

    }

    private void updateChildAlignments() {
        int i = 0;

        for(int c = this.getChildCount(); i < c; ++i) {
            this.updateChildAlignments(this.getChildAt(i));
        }

    }

    void setExtraLayoutSpace(int extraLayoutSpace) {
        if (this.mExtraLayoutSpace != extraLayoutSpace) {
            if (this.mExtraLayoutSpace < 0) {
                throw new IllegalArgumentException("ExtraLayoutSpace must >= 0");
            } else {
                this.mExtraLayoutSpace = extraLayoutSpace;
                this.requestLayout();
            }
        }
    }

    int getExtraLayoutSpace() {
        return this.mExtraLayoutSpace;
    }

    private void removeInvisibleViewsAtEnd() {
        if ((this.mFlag & 65600) == 65536) {
            this.mGrid.removeInvisibleItemsAtEnd(this.mFocusPosition, (this.mFlag & 262144) != 0 ? -this.mExtraLayoutSpace : this.mSizePrimary + this.mExtraLayoutSpace);
        }

    }

    private void removeInvisibleViewsAtFront() {
        if ((this.mFlag & 65600) == 65536) {
            this.mGrid.removeInvisibleItemsAtFront(this.mFocusPosition, (this.mFlag & 262144) != 0 ? this.mSizePrimary + this.mExtraLayoutSpace : -this.mExtraLayoutSpace);
        }

    }

    private boolean appendOneColumnVisibleItems() {
        return this.mGrid.appendOneColumnVisibleItems();
    }

    void slideIn() {
        if ((this.mFlag & 64) != 0) {
            this.mFlag &= -65;
            if (this.mFocusPosition >= 0) {
                this.scrollToSelection(this.mFocusPosition, this.mSubFocusPosition, true, this.mPrimaryScrollExtra);
            } else {
                this.mFlag &= -129;
                this.requestLayout();
            }

            if ((this.mFlag & 128) != 0) {
                this.mFlag &= -129;
                if (this.mBaseGridView.getScrollState() == 0 && !this.isSmoothScrolling()) {
                    this.requestLayout();
                } else {
                    this.mBaseGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            if (newState == 0) {
                                GridLayoutManager.this.mBaseGridView.removeOnScrollListener(this);
                                GridLayoutManager.this.requestLayout();
                            }

                        }
                    });
                }
            }
        }

    }

    int getSlideOutDistance() {
        int distance;
        int start;
        if (this.mOrientation == 1) {
            distance = -this.getHeight();
            if (this.getChildCount() > 0) {
                start = this.getChildAt(0).getTop();
                if (start < 0) {
                    distance += start;
                }
            }
        } else if ((this.mFlag & 262144) != 0) {
            distance = this.getWidth();
            if (this.getChildCount() > 0) {
                start = this.getChildAt(0).getRight();
                if (start > distance) {
                    distance = start;
                }
            }
        } else {
            distance = -this.getWidth();
            if (this.getChildCount() > 0) {
                start = this.getChildAt(0).getLeft();
                if (start < 0) {
                    distance += start;
                }
            }
        }

        return distance;
    }

    boolean isSlidingChildViews() {
        return (this.mFlag & 64) != 0;
    }

    void slideOut() {
        if ((this.mFlag & 64) == 0) {
            this.mFlag |= 64;
            if (this.getChildCount() != 0) {
                if (this.mOrientation == 1) {
                    this.mBaseGridView.smoothScrollBy(0, this.getSlideOutDistance(), new AccelerateDecelerateInterpolator());
                } else {
                    this.mBaseGridView.smoothScrollBy(this.getSlideOutDistance(), 0, new AccelerateDecelerateInterpolator());
                }

            }
        }
    }

    private boolean prependOneColumnVisibleItems() {
        return this.mGrid.prependOneColumnVisibleItems();
    }

    private void appendVisibleItems() {
        this.mGrid.appendVisibleItems((this.mFlag & 262144) != 0 ? -this.mExtraLayoutSpace - this.mExtraLayoutSpaceInPreLayout : this.mSizePrimary + this.mExtraLayoutSpace + this.mExtraLayoutSpaceInPreLayout);
    }

    private void prependVisibleItems() {
        this.mGrid.prependVisibleItems((this.mFlag & 262144) != 0 ? this.mSizePrimary + this.mExtraLayoutSpace + this.mExtraLayoutSpaceInPreLayout : -this.mExtraLayoutSpace - this.mExtraLayoutSpaceInPreLayout);
    }

    private void fastRelayout() {
        boolean invalidateAfter = false;
        int childCount = this.getChildCount();
        int position = this.mGrid.getFirstVisibleIndex();
        int index = 0;

        for(this.mFlag &= -9; index < childCount; ++position) {
            View view = this.getChildAt(index);
            if (position != this.getAdapterPositionByView(view)) {
                invalidateAfter = true;
                break;
            }

            Grid.Location location = this.mGrid.getLocation(position);
            if (location == null) {
                invalidateAfter = true;
                break;
            }

            int startSecondary = this.getRowStartSecondary(location.mRow) + this.mWindowAlignment.secondAxis().getPaddingMin() - this.mScrollOffsetSecondary;
            int start = this.getViewMin(view);
            int oldPrimarySize = this.getViewPrimarySize(view);
            LayoutParams lp = (LayoutParams)view.getLayoutParams();
            if (lp.viewNeedsUpdate()) {
                this.mFlag |= 8;
                this.detachAndScrapView(view, this.mRecycler);
                view = this.getViewForPosition(position);
                this.addView(view, index);
            }

            this.measureChild(view);
            int primarySize;
            int end;
            if (this.mOrientation == 0) {
                primarySize = this.getDecoratedMeasuredWidthWithMargin(view);
                end = start + primarySize;
            } else {
                primarySize = this.getDecoratedMeasuredHeightWithMargin(view);
                end = start + primarySize;
            }

            this.layoutChild(location.mRow, view, start, end, startSecondary);
            if (oldPrimarySize != primarySize) {
                invalidateAfter = true;
                break;
            }

            ++index;
        }

        if (invalidateAfter) {
            int savedLastPos = this.mGrid.getLastVisibleIndex();

            for(int i = childCount - 1; i >= index; --i) {
                View v = this.getChildAt(i);
                this.detachAndScrapView(v, this.mRecycler);
            }

            this.mGrid.invalidateItemsAfter(position);
            if ((this.mFlag & 65536) != 0) {
                this.appendVisibleItems();
                if (this.mFocusPosition >= 0 && this.mFocusPosition <= savedLastPos) {
                    while(this.mGrid.getLastVisibleIndex() < this.mFocusPosition) {
                        this.mGrid.appendOneColumnVisibleItems();
                    }
                }
            } else {
                while(this.mGrid.appendOneColumnVisibleItems() && this.mGrid.getLastVisibleIndex() < savedLastPos) {
                }
            }
        }

        this.updateScrollLimits();
        this.updateSecondaryScrollLimits();
    }

    public void removeAndRecycleAllViews(@NonNull RecyclerView.Recycler recycler) {
        for(int i = this.getChildCount() - 1; i >= 0; --i) {
            this.removeAndRecycleViewAt(i, recycler);
        }

    }

    private void focusToViewInLayout(boolean hadFocus, boolean alignToView, int extraDelta, int extraDeltaSecondary) {
        View focusView = this.findViewByPosition(this.mFocusPosition);
        if (focusView != null && alignToView) {
            this.scrollToView(focusView, false, extraDelta, extraDeltaSecondary);
        }

        if (focusView != null && hadFocus && !focusView.hasFocus()) {
            focusView.requestFocus();
        } else if (!hadFocus && !this.mBaseGridView.hasFocus()) {
            if (focusView != null && focusView.hasFocusable()) {
                this.mBaseGridView.focusableViewAvailable(focusView);
            } else {
                int i = 0;

                for(int count = this.getChildCount(); i < count; ++i) {
                    focusView = this.getChildAt(i);
                    if (focusView != null && focusView.hasFocusable()) {
                        this.mBaseGridView.focusableViewAvailable(focusView);
                        break;
                    }
                }
            }

            if (alignToView && focusView != null && focusView.hasFocus()) {
                this.scrollToView(focusView, false, extraDelta, extraDeltaSecondary);
            }
        }

    }

    public void onLayoutCompleted(@NonNull RecyclerView.State state) {
        if (this.mOnLayoutCompletedListeners != null) {
            for(int i = this.mOnLayoutCompletedListeners.size() - 1; i >= 0; --i) {
                ((BaseGridView.OnLayoutCompletedListener)this.mOnLayoutCompletedListeners.get(i)).onLayoutCompleted(state);
            }
        }

    }

    public boolean supportsPredictiveItemAnimations() {
        return true;
    }

    void updatePositionToRowMapInPostLayout() {
        this.mPositionToRowInPostLayout.clear();
        int childCount = this.getChildCount();

        for(int i = 0; i < childCount; ++i) {
            int position = this.mBaseGridView.getChildViewHolder(this.getChildAt(i)).getOldPosition();
            if (position >= 0) {
                Grid.Location loc = this.mGrid.getLocation(position);
                if (loc != null) {
                    this.mPositionToRowInPostLayout.put(position, loc.mRow);
                }
            }
        }

    }

    void fillScrapViewsInPostLayout() {
        List<RecyclerView.ViewHolder> scrapList = this.mRecycler.getScrapList();
        int scrapSize = scrapList.size();
        if (scrapSize != 0) {
            int totalItems;
            if (this.mDisappearingPositions == null || scrapSize > this.mDisappearingPositions.length) {
                for(totalItems = this.mDisappearingPositions == null ? 16 : this.mDisappearingPositions.length; totalItems < scrapSize; totalItems <<= 1) {
                }

                this.mDisappearingPositions = new int[totalItems];
            }

            totalItems = 0;

            for(int i = 0; i < scrapSize; ++i) {
                int pos = ((RecyclerView.ViewHolder)scrapList.get(i)).getAbsoluteAdapterPosition();
                if (pos >= 0) {
                    this.mDisappearingPositions[totalItems++] = pos;
                }
            }

            if (totalItems > 0) {
                Arrays.sort(this.mDisappearingPositions, 0, totalItems);
                this.mGrid.fillDisappearingItems(this.mDisappearingPositions, totalItems, this.mPositionToRowInPostLayout);
            }

            this.mPositionToRowInPostLayout.clear();
        }
    }

    void updatePositionDeltaInPreLayout() {
        if (this.getChildCount() > 0) {
            View view = this.getChildAt(0);
            LayoutParams lp = (LayoutParams)view.getLayoutParams();
            this.mPositionDeltaInPreLayout = this.mGrid.getFirstVisibleIndex() - lp.getViewLayoutPosition();
        } else {
            this.mPositionDeltaInPreLayout = 0;
        }

    }

    public void onLayoutChildren(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state) {
        if (this.mNumRows != 0) {
            int itemCount = state.getItemCount();
            if (itemCount >= 0) {
                if ((this.mFlag & 64) != 0 && this.getChildCount() > 0) {
                    this.mFlag |= 128;
                } else if ((this.mFlag & 512) == 0) {
                    this.discardLayoutInfo();
                    this.removeAndRecycleAllViews(recycler);
                } else {
                    this.mFlag = this.mFlag & -4 | 1;
                    this.saveContext(recycler, state);
                    int savedFocusPos;
                    int savedSubFocusPos;
                    int firstVisibleIndex;
                    int deltaSecondary;
                    if (state.isPreLayout()) {
                        this.updatePositionDeltaInPreLayout();
                        int childCount = this.getChildCount();
                        if (this.mGrid != null && childCount > 0) {
                            int minChangedEdge = Integer.MAX_VALUE;
                            savedFocusPos = Integer.MIN_VALUE;
                            savedSubFocusPos = this.mBaseGridView.getChildViewHolder(this.getChildAt(0)).getOldPosition();
                            int maxOldAdapterPosition = this.mBaseGridView.getChildViewHolder(this.getChildAt(childCount - 1)).getOldPosition();

                            for(firstVisibleIndex = 0; firstVisibleIndex < childCount; ++firstVisibleIndex) {
                                View view = this.getChildAt(firstVisibleIndex);
                                LayoutParams lp = (LayoutParams)view.getLayoutParams();
                                deltaSecondary = this.mBaseGridView.getChildAdapterPosition(view);
                                if (lp.isItemChanged() || lp.isItemRemoved() || view.isLayoutRequested() || !view.hasFocus() && this.mFocusPosition == lp.getAbsoluteAdapterPosition() || view.hasFocus() && this.mFocusPosition != lp.getAbsoluteAdapterPosition() || deltaSecondary < savedSubFocusPos || deltaSecondary > maxOldAdapterPosition) {
                                    minChangedEdge = Math.min(minChangedEdge, this.getViewMin(view));
                                    savedFocusPos = Math.max(savedFocusPos, this.getViewMax(view));
                                }
                            }

                            if (savedFocusPos > minChangedEdge) {
                                this.mExtraLayoutSpaceInPreLayout = savedFocusPos - minChangedEdge;
                            }

                            this.appendVisibleItems();
                            this.prependVisibleItems();
                        }

                        this.mFlag &= -4;
                        this.leaveContext();
                    } else {
                        if (state.willRunPredictiveAnimations()) {
                            this.updatePositionToRowMapInPostLayout();
                        }

                        boolean scrollToFocus = !this.isSmoothScrolling() && this.mFocusScrollStrategy == 0;
                        if (this.mFocusPosition != -1 && this.mFocusPositionOffset != Integer.MIN_VALUE) {
                            this.mFocusPosition += this.mFocusPositionOffset;
                            this.mSubFocusPosition = 0;
                        }

                        this.mFocusPositionOffset = 0;
                        View savedFocusView = this.findViewByPosition(this.mFocusPosition);
                        savedFocusPos = this.mFocusPosition;
                        savedSubFocusPos = this.mSubFocusPosition;
                        boolean hadFocus = this.mBaseGridView.hasFocus();
                        firstVisibleIndex = this.mGrid != null ? this.mGrid.getFirstVisibleIndex() : -1;
                        int lastVisibleIndex = this.mGrid != null ? this.mGrid.getLastVisibleIndex() : -1;
                        int deltaPrimary;
                        if (this.mOrientation == 0) {
                            deltaPrimary = state.getRemainingScrollHorizontal();
                            deltaSecondary = state.getRemainingScrollVertical();
                        } else {
                            deltaSecondary = state.getRemainingScrollHorizontal();
                            deltaPrimary = state.getRemainingScrollVertical();
                        }

                        int startFromPosition;
                        int endPos;
                        if (this.layoutInit()) {
                            this.mFlag |= 4;
                            this.mGrid.setStart(this.mFocusPosition);
                            this.fastRelayout();
                        } else {
                            this.mFlag &= -5;
                            this.mFlag = this.mFlag & -17 | (scrollToFocus ? 16 : 0);
                            if (!scrollToFocus || firstVisibleIndex >= 0 && this.mFocusPosition <= lastVisibleIndex && this.mFocusPosition >= firstVisibleIndex) {
                                startFromPosition = firstVisibleIndex;
                                endPos = lastVisibleIndex;
                            } else {
                                startFromPosition = endPos = this.mFocusPosition;
                            }

                            this.mGrid.setStart(startFromPosition);
                            if (endPos != -1) {
                                while(this.appendOneColumnVisibleItems() && this.findViewByPosition(endPos) == null) {
                                }
                            }
                        }

                        do {
                            do {
                                this.updateScrollLimits();
                                startFromPosition = this.mGrid.getFirstVisibleIndex();
                                endPos = this.mGrid.getLastVisibleIndex();
                                this.focusToViewInLayout(hadFocus, scrollToFocus, -deltaPrimary, -deltaSecondary);
                                this.appendVisibleItems();
                                this.prependVisibleItems();
                            } while(this.mGrid.getFirstVisibleIndex() != startFromPosition);
                        } while(this.mGrid.getLastVisibleIndex() != endPos);

                        this.removeInvisibleViewsAtFront();
                        this.removeInvisibleViewsAtEnd();
                        if (state.willRunPredictiveAnimations()) {
                            this.fillScrapViewsInPostLayout();
                        }

                        if ((this.mFlag & 1024) != 0) {
                            this.mFlag &= -1025;
                        } else {
                            this.updateRowSecondarySizeRefresh();
                        }

                        if ((this.mFlag & 4) != 0 && (this.mFocusPosition != savedFocusPos || this.mSubFocusPosition != savedSubFocusPos || this.findViewByPosition(this.mFocusPosition) != savedFocusView || (this.mFlag & 8) != 0)) {
                            this.dispatchChildSelected();
                        } else if ((this.mFlag & 20) == 16) {
                            this.dispatchChildSelected();
                        }

                        this.dispatchChildSelectedAndPositioned();
                        if ((this.mFlag & 64) != 0) {
                            this.scrollDirectionPrimary(this.getSlideOutDistance());
                        }

                        this.mFlag &= -4;
                        this.leaveContext();
                    }
                }
            }
        }
    }

    private void offsetChildrenSecondary(int increment) {
        int childCount = this.getChildCount();
        int i;
        if (this.mOrientation == 0) {
            for(i = 0; i < childCount; ++i) {
                this.getChildAt(i).offsetTopAndBottom(increment);
            }
        } else {
            for(i = 0; i < childCount; ++i) {
                this.getChildAt(i).offsetLeftAndRight(increment);
            }
        }

    }

    private void offsetChildrenPrimary(int increment) {
        int childCount = this.getChildCount();
        int i;
        if (this.mOrientation == 1) {
            for(i = 0; i < childCount; ++i) {
                this.getChildAt(i).offsetTopAndBottom(increment);
            }
        } else {
            for(i = 0; i < childCount; ++i) {
                this.getChildAt(i).offsetLeftAndRight(increment);
            }
        }

    }

    public int scrollHorizontallyBy(int dx, @NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state) {
        if ((this.mFlag & 512) != 0 && this.hasDoneFirstLayout()) {
            this.saveContext(recycler, state);
            this.mFlag = this.mFlag & -4 | 2;
            int result;
            if (this.mOrientation == 0) {
                result = this.scrollDirectionPrimary(dx);
            } else {
                result = this.scrollDirectionSecondary(dx);
            }

            this.leaveContext();
            this.mFlag &= -4;
            return result;
        } else {
            return 0;
        }
    }

    public int scrollVerticallyBy(int dy, @NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state) {
        if ((this.mFlag & 512) != 0 && this.hasDoneFirstLayout()) {
            this.mFlag = this.mFlag & -4 | 2;
            this.saveContext(recycler, state);
            int result;
            if (this.mOrientation == 1) {
                result = this.scrollDirectionPrimary(dy);
            } else {
                result = this.scrollDirectionSecondary(dy);
            }

            this.leaveContext();
            this.mFlag &= -4;
            return result;
        } else {
            return 0;
        }
    }

    private int scrollDirectionPrimary(int da) {
        int childCount;
        if ((this.mFlag & 64) == 0 && (this.mFlag & 3) != 1) {
            if (da > 0) {
                if (!this.mWindowAlignment.mainAxis().isMaxUnknown()) {
                    childCount = this.mWindowAlignment.mainAxis().getMaxScroll();
                    if (da > childCount) {
                        da = childCount;
                    }
                }
            } else if (da < 0 && !this.mWindowAlignment.mainAxis().isMinUnknown()) {
                childCount = this.mWindowAlignment.mainAxis().getMinScroll();
                if (da < childCount) {
                    da = childCount;
                }
            }
        }

        if (da == 0) {
            return 0;
        } else {
            this.offsetChildrenPrimary(-da);
            if ((this.mFlag & 3) == 1) {
                this.updateScrollLimits();
                return da;
            } else {
                label69: {
                    label68: {
                        childCount = this.getChildCount();
                        if ((this.mFlag & 262144) != 0) {
                            if (da > 0) {
                                break label68;
                            }
                        } else if (da < 0) {
                            break label68;
                        }

                        this.appendVisibleItems();
                        break label69;
                    }

                    this.prependVisibleItems();
                }

                boolean updated;
                label60: {
                    label59: {
                        updated = this.getChildCount() > childCount;
                        childCount = this.getChildCount();
                        if ((this.mFlag & 262144) != 0) {
                            if (da > 0) {
                                break label59;
                            }
                        } else if (da < 0) {
                            break label59;
                        }

                        this.removeInvisibleViewsAtFront();
                        break label60;
                    }

                    this.removeInvisibleViewsAtEnd();
                }

                updated |= this.getChildCount() < childCount;
                if (updated) {
                    this.updateRowSecondarySizeRefresh();
                }

                this.mBaseGridView.invalidate();
                this.updateScrollLimits();
                return da;
            }
        }
    }

    private int scrollDirectionSecondary(int dy) {
        if (dy == 0) {
            return 0;
        } else {
            this.offsetChildrenSecondary(-dy);
            this.mScrollOffsetSecondary += dy;
            this.updateSecondaryScrollLimits();
            this.mBaseGridView.invalidate();
            return dy;
        }
    }

    public void collectAdjacentPrefetchPositions(int dx, int dy, @NonNull RecyclerView.State state, @NonNull RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        try {
            this.saveContext((RecyclerView.Recycler)null, state);
            int da = this.mOrientation == 0 ? dx : dy;
            if (this.getChildCount() != 0 && da != 0) {
                int fromLimit = da < 0 ? -this.mExtraLayoutSpace : this.mSizePrimary + this.mExtraLayoutSpace;
                this.mGrid.collectAdjacentPrefetchPositions(fromLimit, da, layoutPrefetchRegistry);
                return;
            }
        } finally {
            this.leaveContext();
        }

    }

    public void collectInitialPrefetchPositions(int adapterItemCount, @NonNull RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int numToPrefetch = this.mBaseGridView.mInitialPrefetchItemCount;
        if (adapterItemCount != 0 && numToPrefetch != 0) {
            int initialPos = Math.max(0, Math.min(this.mFocusPosition - (numToPrefetch - 1) / 2, adapterItemCount - numToPrefetch));

            for(int i = initialPos; i < adapterItemCount && i < initialPos + numToPrefetch; ++i) {
                layoutPrefetchRegistry.addPosition(i, 0);
            }
        }

    }

    void updateScrollLimits() {
        if (this.mState.getItemCount() != 0) {
            int highVisiblePos;
            int lowVisiblePos;
            int highMaxPos;
            int lowMinPos;
            if ((this.mFlag & 262144) == 0) {
                highVisiblePos = this.mGrid.getLastVisibleIndex();
                highMaxPos = this.mState.getItemCount() - 1;
                lowVisiblePos = this.mGrid.getFirstVisibleIndex();
                lowMinPos = 0;
            } else {
                highVisiblePos = this.mGrid.getFirstVisibleIndex();
                highMaxPos = 0;
                lowVisiblePos = this.mGrid.getLastVisibleIndex();
                lowMinPos = this.mState.getItemCount() - 1;
            }

            if (highVisiblePos >= 0 && lowVisiblePos >= 0) {
                boolean highAvailable = highVisiblePos == highMaxPos;
                boolean lowAvailable = lowVisiblePos == lowMinPos;
                if (highAvailable || !this.mWindowAlignment.mainAxis().isMaxUnknown() || lowAvailable || !this.mWindowAlignment.mainAxis().isMinUnknown()) {
                    int maxEdge;
                    int maxViewCenter;
                    if (highAvailable) {
                        maxEdge = this.mGrid.findRowMax(true, sTwoInts);
                        View maxChild = this.findViewByPosition(sTwoInts[1]);
                        maxViewCenter = this.getViewCenter(maxChild);
                        LayoutParams lp = (LayoutParams)maxChild.getLayoutParams();
                        int[] multipleAligns = lp.getAlignMultiple();
                        if (multipleAligns != null && multipleAligns.length > 0) {
                            maxViewCenter += multipleAligns[multipleAligns.length - 1] - multipleAligns[0];
                        }
                    } else {
                        maxEdge = Integer.MAX_VALUE;
                        maxViewCenter = Integer.MAX_VALUE;
                    }

                    int minEdge;
                    int minViewCenter;
                    if (lowAvailable) {
                        minEdge = this.mGrid.findRowMin(false, sTwoInts);
                        View minChild = this.findViewByPosition(sTwoInts[1]);
                        minViewCenter = this.getViewCenter(minChild);
                    } else {
                        minEdge = Integer.MIN_VALUE;
                        minViewCenter = Integer.MIN_VALUE;
                    }

                    this.mWindowAlignment.mainAxis().updateMinMax(minEdge, maxEdge, minViewCenter, maxViewCenter);
                }
            }
        }
    }

    private void updateSecondaryScrollLimits() {
        WindowAlignment.Axis secondAxis = this.mWindowAlignment.secondAxis();
        int minEdge = secondAxis.getPaddingMin() - this.mScrollOffsetSecondary;
        int maxEdge = minEdge + this.getSizeSecondary();
        secondAxis.updateMinMax(minEdge, maxEdge, minEdge, maxEdge);
    }

    private void initScrollController() {
        this.mWindowAlignment.reset();
        this.mWindowAlignment.horizontal.setSize(this.getWidth());
        this.mWindowAlignment.vertical.setSize(this.getHeight());
        this.mWindowAlignment.horizontal.setPadding(this.getPaddingLeft(), this.getPaddingRight());
        this.mWindowAlignment.vertical.setPadding(this.getPaddingTop(), this.getPaddingBottom());
        this.mSizePrimary = this.mWindowAlignment.mainAxis().getSize();
        this.mScrollOffsetSecondary = 0;
    }

    private void updateScrollController() {
        this.mWindowAlignment.horizontal.setSize(this.getWidth());
        this.mWindowAlignment.vertical.setSize(this.getHeight());
        this.mWindowAlignment.horizontal.setPadding(this.getPaddingLeft(), this.getPaddingRight());
        this.mWindowAlignment.vertical.setPadding(this.getPaddingTop(), this.getPaddingBottom());
        this.mSizePrimary = this.mWindowAlignment.mainAxis().getSize();
    }

    public void scrollToPosition(int position) {
        this.setSelection(position, 0, false, 0);
    }

    public void smoothScrollToPosition(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state, int position) {
        this.setSelection(position, 0, true, 0);
    }

    void setSelection(int position, int primaryScrollExtra) {
        this.setSelection(position, 0, false, primaryScrollExtra);
    }

    void setSelectionSmooth(int position) {
        this.setSelection(position, 0, true, 0);
    }

    void setSelectionWithSub(int position, int subposition, int primaryScrollExtra) {
        this.setSelection(position, subposition, false, primaryScrollExtra);
    }

    void setSelectionSmoothWithSub(int position, int subposition) {
        this.setSelection(position, subposition, true, 0);
    }

    int getSelection() {
        return this.mFocusPosition;
    }

    int getSubSelection() {
        return this.mSubFocusPosition;
    }

    void setSelection(int position, int subposition, boolean smooth, int primaryScrollExtra) {
        if (this.mFocusPosition != position && position != -1 || subposition != this.mSubFocusPosition || primaryScrollExtra != this.mPrimaryScrollExtra) {
            this.scrollToSelection(position, subposition, smooth, primaryScrollExtra);
        }

    }

    void scrollToSelection(int position, int subposition, boolean smooth, int primaryScrollExtra) {
        this.mPrimaryScrollExtra = primaryScrollExtra;
        View view = this.findViewByPosition(position);
        boolean notSmoothScrolling = !this.isSmoothScrolling();
        if (notSmoothScrolling && !this.mBaseGridView.isLayoutRequested() && view != null && this.getAdapterPositionByView(view) == position) {
            this.mFlag |= 32;
            this.scrollToView(view, smooth);
            this.mFlag &= -33;
        } else {
            if ((this.mFlag & 512) == 0 || (this.mFlag & 64) != 0) {
                this.mFocusPosition = position;
                this.mSubFocusPosition = subposition;
                this.mFocusPositionOffset = Integer.MIN_VALUE;
                return;
            }

            if (smooth && !this.mBaseGridView.isLayoutRequested()) {
                this.mFocusPosition = position;
                this.mSubFocusPosition = subposition;
                this.mFocusPositionOffset = Integer.MIN_VALUE;
                if (!this.hasDoneFirstLayout()) {
                    Log.w(this.getTag(), "setSelectionSmooth should not be called before first layout pass");
                    return;
                }

                position = this.startPositionSmoothScroller(position);
                if (position != this.mFocusPosition) {
                    this.mFocusPosition = position;
                    this.mSubFocusPosition = 0;
                }
            } else {
                if (!notSmoothScrolling) {
                    this.skipSmoothScrollerOnStopInternal();
                    this.mBaseGridView.stopScroll();
                }

                if (!this.mBaseGridView.isLayoutRequested() && view != null && this.getAdapterPositionByView(view) == position) {
                    this.mFlag |= 32;
                    this.scrollToView(view, smooth);
                    this.mFlag &= -33;
                } else {
                    this.mFocusPosition = position;
                    this.mSubFocusPosition = subposition;
                    this.mFocusPositionOffset = Integer.MIN_VALUE;
                    this.mFlag |= 256;
                    this.requestLayout();
                }
            }
        }

    }

    int startPositionSmoothScroller(int position) {
        LinearSmoothScroller linearSmoothScroller = new GridLinearSmoothScroller() {
            public PointF computeScrollVectorForPosition(int targetPosition) {
                if (this.getChildCount() == 0) {
                    return null;
                } else {
                    int firstChildPos = GridLayoutManager.this.getPosition(GridLayoutManager.this.getChildAt(0));
                    boolean isStart = (GridLayoutManager.this.mFlag & 262144) != 0 ? targetPosition > firstChildPos : targetPosition < firstChildPos;
                    int direction = isStart ? -1 : 1;
                    return GridLayoutManager.this.mOrientation == 0 ? new PointF((float)direction, 0.0F) : new PointF(0.0F, (float)direction);
                }
            }
        };
        linearSmoothScroller.setTargetPosition(position);
        this.startSmoothScroll(linearSmoothScroller);
        return linearSmoothScroller.getTargetPosition();
    }

    void skipSmoothScrollerOnStopInternal() {
        if (this.mCurrentSmoothScroller != null) {
            this.mCurrentSmoothScroller.mSkipOnStopInternal = true;
        }

    }

    public void startSmoothScroll(@NonNull RecyclerView.SmoothScroller smoothScroller) {
        this.skipSmoothScrollerOnStopInternal();
        super.startSmoothScroll(smoothScroller);
        if (smoothScroller.isRunning() && smoothScroller instanceof GridLinearSmoothScroller) {
            this.mCurrentSmoothScroller = (GridLinearSmoothScroller)smoothScroller;
            if (this.mCurrentSmoothScroller instanceof PendingMoveSmoothScroller) {
                this.mPendingMoveSmoothScroller = (PendingMoveSmoothScroller)this.mCurrentSmoothScroller;
            } else {
                this.mPendingMoveSmoothScroller = null;
            }
        } else {
            this.mCurrentSmoothScroller = null;
            this.mPendingMoveSmoothScroller = null;
        }

    }

    void processPendingMovement(boolean forward) {
        if (forward) {
            if (this.hasCreatedLastItem()) {
                return;
            }
        } else if (this.hasCreatedFirstItem()) {
            return;
        }

        if (this.mPendingMoveSmoothScroller == null) {
            PendingMoveSmoothScroller linearSmoothScroller = new PendingMoveSmoothScroller(forward ? 1 : -1, this.mNumRows > 1);
            this.mFocusPositionOffset = 0;
            this.startSmoothScroll(linearSmoothScroller);
        } else if (forward) {
            this.mPendingMoveSmoothScroller.increasePendingMoves();
        } else {
            this.mPendingMoveSmoothScroller.decreasePendingMoves();
        }

        int soundEffect;
        if (this.mOrientation == 0) {
            boolean rtl = this.getLayoutDirection() == 1;
            if (rtl) {
                soundEffect = forward ? 3 : 4;
            } else {
                soundEffect = forward ? 4 : 3;
            }
        } else {
            soundEffect = forward ? 2 : 1;
        }

        this.getAudioManager().playSoundEffect(soundEffect);
    }

    public void onItemsAdded(@NonNull RecyclerView recyclerView, int positionStart, int itemCount) {
        if (this.mFocusPosition != -1 && this.mGrid != null && this.mGrid.getFirstVisibleIndex() >= 0 && this.mFocusPositionOffset != Integer.MIN_VALUE) {
            int pos = this.mFocusPosition + this.mFocusPositionOffset;
            if (positionStart <= pos) {
                this.mFocusPositionOffset += itemCount;
            }
        }

        this.mChildrenStates.clear();
    }

    public void onItemsChanged(@NonNull RecyclerView recyclerView) {
        this.mFocusPositionOffset = 0;
        this.mChildrenStates.clear();
    }

    public void onItemsRemoved(@NonNull RecyclerView recyclerView, int positionStart, int itemCount) {
        if (this.mFocusPosition != -1 && this.mGrid != null && this.mGrid.getFirstVisibleIndex() >= 0 && this.mFocusPositionOffset != Integer.MIN_VALUE) {
            int pos = this.mFocusPosition + this.mFocusPositionOffset;
            if (positionStart <= pos) {
                if (positionStart + itemCount > pos) {
                    this.mFocusPositionOffset += positionStart - pos;
                    this.mFocusPosition += this.mFocusPositionOffset;
                    this.mFocusPositionOffset = Integer.MIN_VALUE;
                } else {
                    this.mFocusPositionOffset -= itemCount;
                }
            }
        }

        this.mChildrenStates.clear();
    }

    public void onItemsMoved(@NonNull RecyclerView recyclerView, int fromPosition, int toPosition, int itemCount) {
        if (this.mFocusPosition != -1 && this.mFocusPositionOffset != Integer.MIN_VALUE) {
            int pos = this.mFocusPosition + this.mFocusPositionOffset;
            if (fromPosition <= pos && pos < fromPosition + itemCount) {
                this.mFocusPositionOffset += toPosition - fromPosition;
            } else if (fromPosition < pos && toPosition > pos - itemCount) {
                this.mFocusPositionOffset -= itemCount;
            } else if (fromPosition > pos && toPosition < pos) {
                this.mFocusPositionOffset += itemCount;
            }
        }

        this.mChildrenStates.clear();
    }

    public void onItemsUpdated(@NonNull RecyclerView recyclerView, int positionStart, int itemCount) {
        int i = positionStart;

        for(int end = positionStart + itemCount; i < end; ++i) {
            this.mChildrenStates.remove(i);
        }

    }

    public boolean onRequestChildFocus(@NonNull RecyclerView parent, @NonNull RecyclerView.State state, @NonNull View child, @Nullable View focused) {
        if ((this.mFlag & '耀') != 0) {
            return true;
        } else if (this.getAdapterPositionByView(child) == -1) {
            return true;
        } else {
            if ((this.mFlag & 35) == 0) {
                this.scrollToView(child, focused, true);
            }

            return true;
        }
    }

    public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent, @NonNull View child, @NonNull Rect rect, boolean immediate) {
        return false;
    }

    void getViewSelectedOffsets(View view, int[] offsets) {
        if (this.mOrientation == 0) {
            offsets[0] = this.getPrimaryAlignedScrollDistance(view);
            offsets[1] = this.getSecondaryScrollDistance(view);
        } else {
            offsets[1] = this.getPrimaryAlignedScrollDistance(view);
            offsets[0] = this.getSecondaryScrollDistance(view);
        }

    }

    private int getPrimaryAlignedScrollDistance(View view) {
        return this.mWindowAlignment.mainAxis().getScroll(this.getViewCenter(view));
    }

    private int getAdjustedPrimaryAlignedScrollDistance(int scrollPrimary, View view, View childView) {
        int subindex = this.getSubPositionByView(view, childView);
        if (subindex != 0) {
            LayoutParams lp = (LayoutParams)view.getLayoutParams();
            scrollPrimary += lp.getAlignMultiple()[subindex] - lp.getAlignMultiple()[0];
        }

        return scrollPrimary;
    }

    private int getSecondaryScrollDistance(View view) {
        int viewCenterSecondary = this.getViewCenterSecondary(view);
        return this.mWindowAlignment.secondAxis().getScroll(viewCenterSecondary);
    }

    void scrollToView(View view, boolean smooth) {
        this.scrollToView(view, view == null ? null : view.findFocus(), smooth);
    }

    void scrollToView(View view, boolean smooth, int extraDelta, int extraDeltaSecondary) {
        this.scrollToView(view, view == null ? null : view.findFocus(), smooth, extraDelta, extraDeltaSecondary);
    }

    private void scrollToView(View view, View childView, boolean smooth) {
        this.scrollToView(view, childView, smooth, 0, 0);
    }

    private void scrollToView(View view, View childView, boolean smooth, int extraDelta, int extraDeltaSecondary) {
        if ((this.mFlag & 64) == 0) {
            int newFocusPosition = this.getAdapterPositionByView(view);
            int newSubFocusPosition = this.getSubPositionByView(view, childView);
            if (newFocusPosition != this.mFocusPosition || newSubFocusPosition != this.mSubFocusPosition) {
                this.mFocusPosition = newFocusPosition;
                this.mSubFocusPosition = newSubFocusPosition;
                this.mFocusPositionOffset = 0;
                if ((this.mFlag & 3) != 1) {
                    this.dispatchChildSelected();
                }

                if (this.mBaseGridView.isChildrenDrawingOrderEnabledInternal()) {
                    this.mBaseGridView.invalidate();
                }
            }

            if (view != null) {
                if (!view.hasFocus() && this.mBaseGridView.hasFocus()) {
                    view.requestFocus();
                }

                if ((this.mFlag & 131072) != 0 || !smooth) {
                    if (this.getScrollPosition(view, childView, sTwoInts) || extraDelta != 0 || extraDeltaSecondary != 0) {
                        this.scrollGrid(sTwoInts[0] + extraDelta, sTwoInts[1] + extraDeltaSecondary, smooth);
                    }

                }
            }
        }
    }

    boolean getScrollPosition(View view, View childView, int[] deltas) {
        switch (this.mFocusScrollStrategy) {
            case 0:
            default:
                return this.getAlignedPosition(view, childView, deltas);
            case 1:
            case 2:
                return this.getNoneAlignedPosition(view, deltas);
        }
    }

    private boolean getNoneAlignedPosition(View view, int[] deltas) {
        int pos = this.getAdapterPositionByView(view);
        int viewMin = this.getViewMin(view);
        int viewMax = this.getViewMax(view);
        View firstView = null;
        View lastView = null;
        int paddingMin = this.mWindowAlignment.mainAxis().getPaddingMin();
        int clientSize = this.mWindowAlignment.mainAxis().getClientSize();
        int row = this.mGrid.getRowIndex(pos);
        CircularIntArray positions;
        if (viewMin < paddingMin) {
            firstView = view;
            if (this.mFocusScrollStrategy == 2) {
                while(this.prependOneColumnVisibleItems()) {
                    positions = this.mGrid.getItemPositionsInRows(this.mGrid.getFirstVisibleIndex(), pos)[row];
                    firstView = this.findViewByPosition(positions.get(0));
                    if (viewMax - this.getViewMin(firstView) > clientSize) {
                        if (positions.size() > 2) {
                            firstView = this.findViewByPosition(positions.get(2));
                        }
                        break;
                    }
                }
            }
        } else if (viewMax > clientSize + paddingMin) {
            if (this.mFocusScrollStrategy == 2) {
                firstView = view;

                do {
                    positions = this.mGrid.getItemPositionsInRows(pos, this.mGrid.getLastVisibleIndex())[row];
                    lastView = this.findViewByPosition(positions.get(positions.size() - 1));
                    if (this.getViewMax(lastView) - viewMin > clientSize) {
                        lastView = null;
                        break;
                    }
                } while(this.appendOneColumnVisibleItems());

                if (lastView != null) {
                    firstView = null;
                }
            } else {
                lastView = view;
            }
        }

        int scrollPrimary = 0;
        if (firstView != null) {
            scrollPrimary = this.getViewMin(firstView) - paddingMin;
        } else if (lastView != null) {
            scrollPrimary = this.getViewMax(lastView) - (paddingMin + clientSize);
        }

        View secondaryAlignedView;
        if (firstView != null) {
            secondaryAlignedView = firstView;
        } else if (lastView != null) {
            secondaryAlignedView = lastView;
        } else {
            secondaryAlignedView = view;
        }

        int scrollSecondary = this.getSecondaryScrollDistance(secondaryAlignedView);
        if (scrollPrimary == 0 && scrollSecondary == 0) {
            return false;
        } else {
            deltas[0] = scrollPrimary;
            deltas[1] = scrollSecondary;
            return true;
        }
    }

    private boolean getAlignedPosition(View view, View childView, int[] deltas) {
        int scrollPrimary = this.getPrimaryAlignedScrollDistance(view);
        if (childView != null) {
            scrollPrimary = this.getAdjustedPrimaryAlignedScrollDistance(scrollPrimary, view, childView);
        }

        int scrollSecondary = this.getSecondaryScrollDistance(view);
        scrollPrimary += this.mPrimaryScrollExtra;
        if (scrollPrimary == 0 && scrollSecondary == 0) {
            deltas[0] = 0;
            deltas[1] = 0;
            return false;
        } else {
            deltas[0] = scrollPrimary;
            deltas[1] = scrollSecondary;
            return true;
        }
    }

    private void scrollGrid(int scrollPrimary, int scrollSecondary, boolean smooth) {
        if ((this.mFlag & 3) == 1) {
            this.scrollDirectionPrimary(scrollPrimary);
            this.scrollDirectionSecondary(scrollSecondary);
        } else {
            int scrollX;
            int scrollY;
            if (this.mOrientation == 0) {
                scrollX = scrollPrimary;
                scrollY = scrollSecondary;
            } else {
                scrollX = scrollSecondary;
                scrollY = scrollPrimary;
            }

            if (smooth) {
                this.mBaseGridView.smoothScrollBy(scrollX, scrollY);
            } else {
                this.mBaseGridView.scrollBy(scrollX, scrollY);
                this.dispatchChildSelectedAndPositioned();
            }
        }

    }

    void setPruneChild(boolean pruneChild) {
        if ((this.mFlag & 65536) != 0 != pruneChild) {
            this.mFlag = this.mFlag & -65537 | (pruneChild ? 65536 : 0);
            if (pruneChild) {
                this.requestLayout();
            }
        }

    }

    boolean getPruneChild() {
        return (this.mFlag & 65536) != 0;
    }

    void setScrollEnabled(boolean scrollEnabled) {
        if ((this.mFlag & 131072) != 0 != scrollEnabled) {
            this.mFlag = this.mFlag & -131073 | (scrollEnabled ? 131072 : 0);
            if ((this.mFlag & 131072) != 0 && this.mFocusScrollStrategy == 0 && this.mFocusPosition != -1) {
                this.scrollToSelection(this.mFocusPosition, this.mSubFocusPosition, true, this.mPrimaryScrollExtra);
            }
        }

    }

    boolean isScrollEnabled() {
        return (this.mFlag & 131072) != 0;
    }

    private int findImmediateChildIndex(View view) {
        if (view != null && this.mBaseGridView != null && view != this.mBaseGridView) {
            view = this.findContainingItemView(view);
            if (view != null) {
                int i = 0;

                for(int count = this.getChildCount(); i < count; ++i) {
                    if (this.getChildAt(i) == view) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus) {
            int i = this.mFocusPosition;

            while(true) {
                View view = this.findViewByPosition(i);
                if (view == null) {
                    break;
                }

                if (view.getVisibility() == 0 && view.hasFocusable()) {
                    view.requestFocus();
                    break;
                }

                ++i;
            }
        }

    }

    void setFocusSearchDisabled(boolean disabled) {
        this.mFlag = this.mFlag & -32769 | (disabled ? '耀' : 0);
    }

    boolean isFocusSearchDisabled() {
        return (this.mFlag & '耀') != 0;
    }

    @Nullable
    public View onInterceptFocusSearch(@Nullable View focused, int direction) {
        if ((this.mFlag & '耀') != 0) {
            return focused;
        } else {
            FocusFinder ff = FocusFinder.getInstance();
            View result = null;
            int movement;
            if (direction != 2 && direction != 1) {
                result = ff.findNextFocus(this.mBaseGridView, focused, direction);
            } else {
                if (this.canScrollVertically()) {
                    movement = direction == 2 ? 130 : 33;
                    result = ff.findNextFocus(this.mBaseGridView, focused, movement);
                }

                if (this.canScrollHorizontally()) {
                    boolean rtl = this.getLayoutDirection() == 1;
                    int absDir = direction == 2 ^ rtl ? 66 : 17;
                    result = ff.findNextFocus(this.mBaseGridView, focused, absDir);
                }
            }

            if (result != null) {
                return result;
            } else if (this.mBaseGridView.getDescendantFocusability() == 393216) {
                return this.mBaseGridView.getParent().focusSearch(focused, direction);
            } else {
                movement = this.getMovement(direction);
                boolean isScroll = this.mBaseGridView.getScrollState() != 0;
                if (movement == 1) {
                    if (isScroll || (this.mFlag & 4096) == 0) {
                        result = focused;
                    }

                    if ((this.mFlag & 131072) != 0 && !this.hasCreatedLastItem()) {
                        this.processPendingMovement(true);
                        result = focused;
                    }
                } else if (movement == 0) {
                    if (isScroll || (this.mFlag & 2048) == 0) {
                        result = focused;
                    }

                    if ((this.mFlag & 131072) != 0 && !this.hasCreatedFirstItem()) {
                        this.processPendingMovement(false);
                        result = focused;
                    }
                } else if (movement == 3) {
                    if (isScroll || (this.mFlag & 16384) == 0) {
                        result = focused;
                    }
                } else if (movement == 2 && (isScroll || (this.mFlag & 8192) == 0)) {
                    result = focused;
                }

                if (result != null) {
                    return result;
                } else {
                    result = this.mBaseGridView.getParent().focusSearch(focused, direction);
                    if (result != null) {
                        return result;
                    } else {
                        return (View)(focused != null ? focused : this.mBaseGridView);
                    }
                }
            }
        }
    }

    boolean hasPreviousViewInSameRow(int pos) {
        if (this.mGrid != null && pos != -1 && this.mGrid.getFirstVisibleIndex() >= 0) {
            if (this.mGrid.getFirstVisibleIndex() > 0) {
                return true;
            } else {
                int focusedRow = this.mGrid.getLocation(pos).mRow;

                for(int i = this.getChildCount() - 1; i >= 0; --i) {
                    int position = this.getAdapterPositionByIndex(i);
                    Grid.Location loc = this.mGrid.getLocation(position);
                    if (loc != null && loc.mRow == focusedRow && position < pos) {
                        return true;
                    }
                }

                return false;
            }
        } else {
            return false;
        }
    }

    public boolean onAddFocusables(@NonNull RecyclerView recyclerView, @SuppressLint({"ConcreteCollection"}) @NonNull ArrayList<View> views, int direction, int focusableMode) {
        if ((this.mFlag & '耀') != 0) {
            return true;
        } else {
            int movement;
            int focusedIndex;
            int focusedPos;
            View view;
            if (recyclerView.hasFocus()) {
                if (this.mPendingMoveSmoothScroller != null) {
                    return true;
                }

                movement = this.getMovement(direction);
                view = recyclerView.findFocus();
                focusedIndex = this.findImmediateChildIndex(view);
                focusedPos = this.getAdapterPositionByIndex(focusedIndex);
                View immediateFocusedChild = focusedPos == -1 ? null : this.findViewByPosition(focusedPos);
                if (immediateFocusedChild != null) {
                    immediateFocusedChild.addFocusables(views, direction, focusableMode);
                }

                if (this.mGrid == null || this.getChildCount() == 0) {
                    return true;
                }

                if ((movement == 3 || movement == 2) && this.mGrid.getNumRows() <= 1) {
                    return true;
                }

                int focusedRow = this.mGrid != null && immediateFocusedChild != null ? this.mGrid.getLocation(focusedPos).mRow : -1;
                int focusableCount = views.size();
                int inc = movement != 1 && movement != 3 ? -1 : 1;
                int loop_end = inc > 0 ? this.getChildCount() - 1 : 0;
                int loop_start;
                if (focusedIndex == -1) {
                    loop_start = inc > 0 ? 0 : this.getChildCount() - 1;
                } else {
                    loop_start = focusedIndex + inc;
                }

                int i = loop_start;

                while(true) {
                    if (inc > 0) {
                        if (i > loop_end) {
                            break;
                        }
                    } else if (i < loop_end) {
                        break;
                    }

                    View child = this.getChildAt(i);
                    if (child.getVisibility() == 0 && child.hasFocusable()) {
                        if (immediateFocusedChild == null) {
                            child.addFocusables(views, direction, focusableMode);
                            if (views.size() > focusableCount) {
                                break;
                            }
                        } else {
                            int position = this.getAdapterPositionByIndex(i);
                            Grid.Location loc = this.mGrid.getLocation(position);
                            if (loc != null) {
                                if (movement == 1) {
                                    if (loc.mRow == focusedRow && position > focusedPos) {
                                        child.addFocusables(views, direction, focusableMode);
                                        if (views.size() > focusableCount) {
                                            break;
                                        }
                                    }
                                } else if (movement == 0) {
                                    if (loc.mRow == focusedRow && position < focusedPos) {
                                        child.addFocusables(views, direction, focusableMode);
                                        if (views.size() > focusableCount) {
                                            break;
                                        }
                                    }
                                } else if (movement == 3) {
                                    if (loc.mRow != focusedRow) {
                                        if (loc.mRow < focusedRow) {
                                            break;
                                        }

                                        child.addFocusables(views, direction, focusableMode);
                                    }
                                } else if (movement == 2 && loc.mRow != focusedRow) {
                                    if (loc.mRow > focusedRow) {
                                        break;
                                    }

                                    child.addFocusables(views, direction, focusableMode);
                                }
                            }
                        }
                    }

                    i += inc;
                }
            } else {
                movement = views.size();
                if (this.mFocusScrollStrategy == 0) {
                    view = this.findViewByPosition(this.mFocusPosition);
                    if (view != null) {
                        view.addFocusables(views, direction, focusableMode);
                    }
                } else {
                    int left = this.mWindowAlignment.mainAxis().getPaddingMin();
                    focusedIndex = this.mWindowAlignment.mainAxis().getClientSize() + left;
                    focusedPos = 0;

                    int count;
                    View child;
                    for(count = this.getChildCount(); focusedPos < count; ++focusedPos) {
                        child = this.getChildAt(focusedPos);
                        if (child.getVisibility() == 0 && this.getViewMin(child) >= left && this.getViewMax(child) <= focusedIndex) {
                            child.addFocusables(views, direction, focusableMode);
                        }
                    }

                    if (views.size() == movement) {
                        focusedPos = 0;

                        for(count = this.getChildCount(); focusedPos < count; ++focusedPos) {
                            child = this.getChildAt(focusedPos);
                            if (child.getVisibility() == 0) {
                                child.addFocusables(views, direction, focusableMode);
                            }
                        }
                    }
                }

                if (views.size() != movement) {
                    return true;
                }

                if (recyclerView.isFocusable()) {
                    views.add(recyclerView);
                }
            }

            return true;
        }
    }

    boolean hasCreatedLastItem() {
        int count = this.getItemCount();
        return count == 0 || this.mBaseGridView.findViewHolderForAdapterPosition(count - 1) != null;
    }

    boolean hasCreatedFirstItem() {
        int count = this.getItemCount();
        return count == 0 || this.mBaseGridView.findViewHolderForAdapterPosition(0) != null;
    }

    boolean isItemFullyVisible(int pos) {
        RecyclerView.ViewHolder vh = this.mBaseGridView.findViewHolderForAdapterPosition(pos);
        if (vh == null) {
            return false;
        } else {
            return vh.itemView.getLeft() >= 0 && vh.itemView.getRight() <= this.mBaseGridView.getWidth() && vh.itemView.getTop() >= 0 && vh.itemView.getBottom() <= this.mBaseGridView.getHeight();
        }
    }

    boolean canScrollTo(View view) {
        return view.getVisibility() == 0 && (!this.hasFocus() || view.hasFocusable());
    }

    boolean gridOnRequestFocusInDescendants(RecyclerView recyclerView, int direction, Rect previouslyFocusedRect) {
        switch (this.mFocusScrollStrategy) {
            case 0:
            default:
                return this.gridOnRequestFocusInDescendantsAligned(direction, previouslyFocusedRect);
            case 1:
            case 2:
                return this.gridOnRequestFocusInDescendantsUnaligned(direction, previouslyFocusedRect);
        }
    }

    private boolean gridOnRequestFocusInDescendantsAligned(int direction, Rect previouslyFocusedRect) {
        View view = this.findViewByPosition(this.mFocusPosition);
        if (view != null) {
            boolean result = view.requestFocus(direction, previouslyFocusedRect);
            if (!result) {
            }

            return result;
        } else {
            return false;
        }
    }

    private boolean gridOnRequestFocusInDescendantsUnaligned(int direction, Rect previouslyFocusedRect) {
        int count = this.getChildCount();
        int index;
        byte increment;
        int end;
        if ((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }

        int left = this.mWindowAlignment.mainAxis().getPaddingMin();
        int right = this.mWindowAlignment.mainAxis().getClientSize() + left;

        for(int i = index; i != end; i += increment) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == 0 && this.getViewMin(child) >= left && this.getViewMax(child) <= right && child.requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
        }

        return false;
    }

    private int getMovement(int direction) {
        int movement = 17;
        if (this.mOrientation == 0) {
            switch (direction) {
                case 17:
                    movement = (this.mFlag & 262144) == 0 ? 0 : 1;
                    break;
                case 33:
                    movement = 2;
                    break;
                case 66:
                    movement = (this.mFlag & 262144) == 0 ? 1 : 0;
                    break;
                case 130:
                    movement = 3;
            }
        } else if (this.mOrientation == 1) {
            switch (direction) {
                case 17:
                    movement = (this.mFlag & 524288) == 0 ? 2 : 3;
                    break;
                case 33:
                    movement = 0;
                    break;
                case 66:
                    movement = (this.mFlag & 524288) == 0 ? 3 : 2;
                    break;
                case 130:
                    movement = 1;
            }
        }

        return movement;
    }

    int getChildDrawingOrder(RecyclerView recyclerView, int childCount, int i) {
        View view = this.findViewByPosition(this.mFocusPosition);
        if (view == null) {
            return i;
        } else {
            int focusIndex = recyclerView.indexOfChild(view);
            if (i < focusIndex) {
                return i;
            } else {
                return i < childCount - 1 ? focusIndex + childCount - 1 - i : focusIndex;
            }
        }
    }

    public void onAdapterChanged(@Nullable RecyclerView.Adapter oldAdapter, @Nullable RecyclerView.Adapter newAdapter) {
        if (oldAdapter != null) {
            this.discardLayoutInfo();
            this.mFocusPosition = -1;
            this.mFocusPositionOffset = 0;
            this.mChildrenStates.clear();
        }

        if (newAdapter instanceof FacetProviderAdapter) {
            this.mFacetProviderAdapter = (FacetProviderAdapter)newAdapter;
        } else {
            this.mFacetProviderAdapter = null;
        }

        super.onAdapterChanged(oldAdapter, newAdapter);
    }

    private void discardLayoutInfo() {
        this.mGrid = null;
        this.mRowSizeSecondary = null;
        this.mFlag &= -1025;
    }

    void setLayoutEnabled(boolean layoutEnabled) {
        if ((this.mFlag & 512) != 0 != layoutEnabled) {
            this.mFlag = this.mFlag & -513 | (layoutEnabled ? 512 : 0);
            this.requestLayout();
        }

    }

    void setChildrenVisibility(int visibility) {
        this.mChildVisibility = visibility;
        if (this.mChildVisibility != -1) {
            int count = this.getChildCount();

            for(int i = 0; i < count; ++i) {
                this.getChildAt(i).setVisibility(this.mChildVisibility);
            }
        }

    }

    @NonNull
    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState();
        ss.mIndex = this.getSelection();
        Bundle bundle = this.mChildrenStates.saveAsBundle();
        int i = 0;

        for(int count = this.getChildCount(); i < count; ++i) {
            View view = this.getChildAt(i);
            int position = this.getAdapterPositionByView(view);
            if (position != -1) {
                bundle = this.mChildrenStates.saveOnScreenView(bundle, view, position);
            }
        }

        ss.mChildStates = bundle;
        return ss;
    }

    void onChildRecycled(RecyclerView.ViewHolder holder) {
        int position = holder.getAbsoluteAdapterPosition();
        if (position != -1) {
            this.mChildrenStates.saveOffscreenView(holder.itemView, position);
        }

    }

    public void onRestoreInstanceState(@Nullable Parcelable state) {
        if (state instanceof SavedState) {
            SavedState loadingState = (SavedState)state;
            this.mFocusPosition = loadingState.mIndex;
            this.mFocusPositionOffset = 0;
            this.mChildrenStates.loadFromBundle(loadingState.mChildStates);
            this.mFlag |= 256;
            this.requestLayout();
        }
    }

    public int getRowCountForAccessibility(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state) {
        return this.mOrientation == 0 && this.mGrid != null ? this.mGrid.getNumRows() : super.getRowCountForAccessibility(recycler, state);
    }

    public int getColumnCountForAccessibility(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state) {
        return this.mOrientation == 1 && this.mGrid != null ? this.mGrid.getNumRows() : super.getColumnCountForAccessibility(recycler, state);
    }

    public void onInitializeAccessibilityNodeInfoForItem(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state, @NonNull View host, @NonNull AccessibilityNodeInfoCompat info) {
        ViewGroup.LayoutParams lp = host.getLayoutParams();
        if (this.mGrid != null && lp instanceof LayoutParams) {
            LayoutParams glp = (LayoutParams)lp;
            int position = glp.getAbsoluteAdapterPosition();
            int rowIndex = position >= 0 ? this.mGrid.getRowIndex(position) : -1;
            if (rowIndex >= 0) {
                int guessSpanIndex = position / this.mGrid.getNumRows();
                if (this.mOrientation == 0) {
                    info.setCollectionItemInfo(CollectionItemInfoCompat.obtain(rowIndex, 1, guessSpanIndex, 1, false, false));
                } else {
                    info.setCollectionItemInfo(CollectionItemInfoCompat.obtain(guessSpanIndex, 1, rowIndex, 1, false, false));
                }

            }
        }
    }

    public boolean performAccessibilityAction(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state, int action, @Nullable Bundle args) {
        if (!this.isScrollEnabled()) {
            return true;
        } else {
            this.saveContext(recycler, state);
            int translatedAction = action;
            boolean reverseFlowPrimary = (this.mFlag & 262144) != 0;
            if (VERSION.SDK_INT >= 23) {
                if (this.mOrientation == 0) {
                    if (action == AccessibilityActionCompat.ACTION_SCROLL_LEFT.getId()) {
                        translatedAction = reverseFlowPrimary ? 4096 : 8192;
                    } else if (action == AccessibilityActionCompat.ACTION_SCROLL_RIGHT.getId()) {
                        translatedAction = reverseFlowPrimary ? 8192 : 4096;
                    }
                } else if (action == AccessibilityActionCompat.ACTION_SCROLL_UP.getId()) {
                    translatedAction = 8192;
                } else if (action == AccessibilityActionCompat.ACTION_SCROLL_DOWN.getId()) {
                    translatedAction = 4096;
                }
            }

            boolean scrollingReachedBeginning = this.mFocusPosition == 0 && translatedAction == 8192;
            boolean scrollingReachedEnd = this.mFocusPosition == state.getItemCount() - 1 && translatedAction == 4096;
            if (!scrollingReachedBeginning && !scrollingReachedEnd) {
                switch (translatedAction) {
                    case 4096:
                        this.processPendingMovement(true);
                        this.processSelectionMoves(false, 1);
                        break;
                    case 8192:
                        this.processPendingMovement(false);
                        this.processSelectionMoves(false, -1);
                }
            } else {
                this.sendTypeViewScrolledAccessibilityEvent();
            }

            this.leaveContext();
            return true;
        }
    }

    private void sendTypeViewScrolledAccessibilityEvent() {
        AccessibilityEvent event = AccessibilityEvent.obtain(4096);
        this.mBaseGridView.onInitializeAccessibilityEvent(event);
        this.mBaseGridView.requestSendAccessibilityEvent(this.mBaseGridView, event);
    }

    int processSelectionMoves(boolean preventScroll, int moves) {
        if (this.mGrid == null) {
            return moves;
        } else {
            int focusPosition = this.mFocusPosition;
            int focusedRow = focusPosition != -1 ? this.mGrid.getRowIndex(focusPosition) : -1;
            View newSelected = null;
            int i = 0;

            for(int count = this.getChildCount(); i < count && moves != 0; ++i) {
                int index = moves > 0 ? i : count - 1 - i;
                View child = this.getChildAt(index);
                if (this.canScrollTo(child)) {
                    int position = this.getAdapterPositionByIndex(index);
                    int rowIndex = this.mGrid.getRowIndex(position);
                    if (focusedRow == -1) {
                        focusPosition = position;
                        newSelected = child;
                        focusedRow = rowIndex;
                    } else if (rowIndex == focusedRow && (moves > 0 && position > focusPosition || moves < 0 && position < focusPosition)) {
                        focusPosition = position;
                        newSelected = child;
                        if (moves > 0) {
                            --moves;
                        } else {
                            ++moves;
                        }
                    }
                }
            }

            if (newSelected != null) {
                if (preventScroll) {
                    if (this.hasFocus()) {
                        this.mFlag |= 32;
                        newSelected.requestFocus();
                        this.mFlag &= -33;
                    }

                    this.mFocusPosition = focusPosition;
                    this.mSubFocusPosition = 0;
                } else {
                    this.scrollToView(newSelected, true);
                }
            }

            return moves;
        }
    }

    private void addA11yActionMovingBackward(AccessibilityNodeInfoCompat info, boolean reverseFlowPrimary) {
        if (VERSION.SDK_INT >= 23) {
            if (this.mOrientation == 0) {
                info.addAction(reverseFlowPrimary ? AccessibilityActionCompat.ACTION_SCROLL_RIGHT : AccessibilityActionCompat.ACTION_SCROLL_LEFT);
            } else {
                info.addAction(AccessibilityActionCompat.ACTION_SCROLL_UP);
            }
        } else {
            info.addAction(8192);
        }

        info.setScrollable(true);
    }

    private void addA11yActionMovingForward(AccessibilityNodeInfoCompat info, boolean reverseFlowPrimary) {
        if (VERSION.SDK_INT >= 23) {
            if (this.mOrientation == 0) {
                info.addAction(reverseFlowPrimary ? AccessibilityActionCompat.ACTION_SCROLL_LEFT : AccessibilityActionCompat.ACTION_SCROLL_RIGHT);
            } else {
                info.addAction(AccessibilityActionCompat.ACTION_SCROLL_DOWN);
            }
        } else {
            info.addAction(4096);
        }

        info.setScrollable(true);
    }

    public void onInitializeAccessibilityNodeInfo(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state, @NonNull AccessibilityNodeInfoCompat info) {
        this.saveContext(recycler, state);
        int count = state.getItemCount();
        boolean reverseFlowPrimary = (this.mFlag & 262144) != 0;
        if ((this.mFlag & 2048) == 0 || count > 1 && !this.isItemFullyVisible(0)) {
            this.addA11yActionMovingBackward(info, reverseFlowPrimary);
        }

        if ((this.mFlag & 4096) == 0 || count > 1 && !this.isItemFullyVisible(count - 1)) {
            this.addA11yActionMovingForward(info, reverseFlowPrimary);
        }

        AccessibilityNodeInfoCompat.CollectionInfoCompat collectionInfo = CollectionInfoCompat.obtain(this.getRowCountForAccessibility(recycler, state), this.getColumnCountForAccessibility(recycler, state), this.isLayoutHierarchical(recycler, state), this.getSelectionModeForAccessibility(recycler, state));
        info.setCollectionInfo(collectionInfo);
        info.setClassName(GridView.class.getName());
        this.leaveContext();
    }

    static final class LayoutParams extends RecyclerView.LayoutParams {
        int mLeftInset;
        int mTopInset;
        int mRightInset;
        int mBottomInset;
        private int mAlignX;
        private int mAlignY;
        private int[] mAlignMultiple;
        private ItemAlignmentFacet mAlignmentFacet;

        LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        LayoutParams(int width, int height) {
            super(width, height);
        }

        LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        LayoutParams(RecyclerView.LayoutParams source) {
            super(source);
        }

        LayoutParams(LayoutParams source) {
            super(source);
        }

        int getAlignX() {
            return this.mAlignX;
        }

        int getAlignY() {
            return this.mAlignY;
        }

        int getOpticalLeft(View view) {
            return view.getLeft() + this.mLeftInset;
        }

        int getOpticalTop(View view) {
            return view.getTop() + this.mTopInset;
        }

        int getOpticalRight(View view) {
            return view.getRight() - this.mRightInset;
        }

        int getOpticalBottom(View view) {
            return view.getBottom() - this.mBottomInset;
        }

        int getOpticalWidth(View view) {
            return view.getWidth() - this.mLeftInset - this.mRightInset;
        }

        int getOpticalHeight(View view) {
            return view.getHeight() - this.mTopInset - this.mBottomInset;
        }

        int getOpticalLeftInset() {
            return this.mLeftInset;
        }

        int getOpticalRightInset() {
            return this.mRightInset;
        }

        int getOpticalTopInset() {
            return this.mTopInset;
        }

        int getOpticalBottomInset() {
            return this.mBottomInset;
        }

        void setAlignX(int alignX) {
            this.mAlignX = alignX;
        }

        void setAlignY(int alignY) {
            this.mAlignY = alignY;
        }

        void setItemAlignmentFacet(ItemAlignmentFacet facet) {
            this.mAlignmentFacet = facet;
        }

        ItemAlignmentFacet getItemAlignmentFacet() {
            return this.mAlignmentFacet;
        }

        void calculateItemAlignments(int orientation, View view) {
            ItemAlignmentFacet.ItemAlignmentDef[] defs = this.mAlignmentFacet.getAlignmentDefs();
            if (this.mAlignMultiple == null || this.mAlignMultiple.length != defs.length) {
                this.mAlignMultiple = new int[defs.length];
            }

            for(int i = 0; i < defs.length; ++i) {
                this.mAlignMultiple[i] = ItemAlignmentFacetHelper.getAlignmentPosition(view, defs[i], orientation);
            }

            if (orientation == 0) {
                this.mAlignX = this.mAlignMultiple[0];
            } else {
                this.mAlignY = this.mAlignMultiple[0];
            }

        }

        int[] getAlignMultiple() {
            return this.mAlignMultiple;
        }

        void setOpticalInsets(int leftInset, int topInset, int rightInset, int bottomInset) {
            this.mLeftInset = leftInset;
            this.mTopInset = topInset;
            this.mRightInset = rightInset;
            this.mBottomInset = bottomInset;
        }
    }

    abstract class GridLinearSmoothScroller extends LinearSmoothScroller {
        boolean mSkipOnStopInternal;

        GridLinearSmoothScroller() {
            super(GridLayoutManager.this.mBaseGridView.getContext());
        }

        protected void onStop() {
            super.onStop();
            if (!this.mSkipOnStopInternal) {
                this.onStopInternal();
            }

            if (GridLayoutManager.this.mCurrentSmoothScroller == this) {
                GridLayoutManager.this.mCurrentSmoothScroller = null;
            }

            if (GridLayoutManager.this.mPendingMoveSmoothScroller == this) {
                GridLayoutManager.this.mPendingMoveSmoothScroller = null;
            }

        }

        protected void onStopInternal() {
            View targetView = this.findViewByPosition(this.getTargetPosition());
            if (targetView == null) {
                if (this.getTargetPosition() >= 0) {
                    GridLayoutManager.this.scrollToSelection(this.getTargetPosition(), 0, false, 0);
                }

            } else {
                if (GridLayoutManager.this.mFocusPosition != this.getTargetPosition()) {
                    GridLayoutManager.this.mFocusPosition = this.getTargetPosition();
                }

                if (GridLayoutManager.this.hasFocus()) {
                    GridLayoutManager var10000 = GridLayoutManager.this;
                    var10000.mFlag |= 32;
                    targetView.requestFocus();
                    var10000 = GridLayoutManager.this;
                    var10000.mFlag &= -33;
                }

                GridLayoutManager.this.dispatchChildSelected();
                GridLayoutManager.this.dispatchChildSelectedAndPositioned();
            }
        }

        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return super.calculateSpeedPerPixel(displayMetrics) * GridLayoutManager.this.mSmoothScrollSpeedFactor;
        }

        protected int calculateTimeForScrolling(int dx) {
            int ms = super.calculateTimeForScrolling(dx);
            if (GridLayoutManager.this.mWindowAlignment.mainAxis().getSize() > 0) {
                float minMs = 30.0F / (float)GridLayoutManager.this.mWindowAlignment.mainAxis().getSize() * (float)dx;
                if ((float)ms < minMs) {
                    ms = (int)minMs;
                }
            }

            return ms;
        }

        protected void onTargetFound(View targetView, RecyclerView.State state, RecyclerView.SmoothScroller.Action action) {
            if (GridLayoutManager.this.getScrollPosition(targetView, (View)null, GridLayoutManager.sTwoInts)) {
                int dx;
                int dy;
                if (GridLayoutManager.this.mOrientation == 0) {
                    dx = GridLayoutManager.sTwoInts[0];
                    dy = GridLayoutManager.sTwoInts[1];
                } else {
                    dx = GridLayoutManager.sTwoInts[1];
                    dy = GridLayoutManager.sTwoInts[0];
                }

                int distance = (int)Math.sqrt((double)(dx * dx + dy * dy));
                int time = this.calculateTimeForDeceleration(distance);
                action.update(dx, dy, time, this.mDecelerateInterpolator);
            }

        }
    }

    final class PendingMoveSmoothScroller extends GridLinearSmoothScroller {
        static final int TARGET_UNDEFINED = -2;
        private final boolean mStaggeredGrid;
        private int mPendingMoves;

        PendingMoveSmoothScroller(int initialPendingMoves, boolean staggeredGrid) {
            super();
            this.mPendingMoves = initialPendingMoves;
            this.mStaggeredGrid = staggeredGrid;
            this.setTargetPosition(-2);
        }

        void increasePendingMoves() {
            if (this.mPendingMoves < GridLayoutManager.this.mMaxPendingMoves) {
                ++this.mPendingMoves;
            }

        }

        void decreasePendingMoves() {
            if (this.mPendingMoves > -GridLayoutManager.this.mMaxPendingMoves) {
                --this.mPendingMoves;
            }

        }

        void consumePendingMovesBeforeLayout() {
            if (!this.mStaggeredGrid && this.mPendingMoves != 0) {
                View newSelected = null;
                int startPos = this.mPendingMoves > 0 ? GridLayoutManager.this.mFocusPosition + GridLayoutManager.this.mNumRows : GridLayoutManager.this.mFocusPosition - GridLayoutManager.this.mNumRows;

                for(int pos = startPos; this.mPendingMoves != 0; pos = this.mPendingMoves > 0 ? pos + GridLayoutManager.this.mNumRows : pos - GridLayoutManager.this.mNumRows) {
                    View v = this.findViewByPosition(pos);
                    if (v == null) {
                        break;
                    }

                    if (GridLayoutManager.this.canScrollTo(v)) {
                        newSelected = v;
                        GridLayoutManager.this.mFocusPosition = pos;
                        GridLayoutManager.this.mSubFocusPosition = 0;
                        if (this.mPendingMoves > 0) {
                            --this.mPendingMoves;
                        } else {
                            ++this.mPendingMoves;
                        }
                    }
                }

                if (newSelected != null && GridLayoutManager.this.hasFocus()) {
                    GridLayoutManager var10000 = GridLayoutManager.this;
                    var10000.mFlag |= 32;
                    newSelected.requestFocus();
                    var10000 = GridLayoutManager.this;
                    var10000.mFlag &= -33;
                }

            }
        }

        void consumePendingMovesAfterLayout() {
            if (this.mStaggeredGrid && this.mPendingMoves != 0) {
                this.mPendingMoves = GridLayoutManager.this.processSelectionMoves(true, this.mPendingMoves);
            }

            if (this.mPendingMoves == 0 || this.mPendingMoves > 0 && GridLayoutManager.this.hasCreatedLastItem() || this.mPendingMoves < 0 && GridLayoutManager.this.hasCreatedFirstItem()) {
                this.setTargetPosition(GridLayoutManager.this.mFocusPosition);
                this.stop();
            }

        }

        public PointF computeScrollVectorForPosition(int targetPosition) {
            if (this.mPendingMoves == 0) {
                return null;
            } else {
                byte var10000;
                label24: {
                    label23: {
                        if ((GridLayoutManager.this.mFlag & 262144) != 0) {
                            if (this.mPendingMoves > 0) {
                                break label23;
                            }
                        } else if (this.mPendingMoves < 0) {
                            break label23;
                        }

                        var10000 = 1;
                        break label24;
                    }

                    var10000 = -1;
                }

                int direction = var10000;
                return GridLayoutManager.this.mOrientation == 0 ? new PointF((float)direction, 0.0F) : new PointF(0.0F, (float)direction);
            }
        }

        protected void onStopInternal() {
            super.onStopInternal();
            this.mPendingMoves = 0;
            View v = this.findViewByPosition(this.getTargetPosition());
            if (v != null) {
                GridLayoutManager.this.scrollToView(v, true);
            }

        }
    }

    @SuppressLint({"BanParcelableUsage"})
    static final class SavedState implements Parcelable {
        int mIndex;
        Bundle mChildStates;
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(this.mIndex);
            out.writeBundle(this.mChildStates);
        }

        public int describeContents() {
            return 0;
        }

        SavedState(Parcel in) {
            this.mChildStates = Bundle.EMPTY;
            this.mIndex = in.readInt();
            this.mChildStates = in.readBundle(GridLayoutManager.class.getClassLoader());
        }

        SavedState() {
            this.mChildStates = Bundle.EMPTY;
        }
    }
}
