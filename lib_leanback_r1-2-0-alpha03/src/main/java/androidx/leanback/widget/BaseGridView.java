//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.leanback.widget.R.styleable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public abstract class BaseGridView extends RecyclerView {
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static final int FOCUS_SCROLL_ALIGNED = 0;
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static final int FOCUS_SCROLL_ITEM = 1;
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static final int FOCUS_SCROLL_PAGE = 2;
    public static final int WINDOW_ALIGN_LOW_EDGE = 1;
    public static final int WINDOW_ALIGN_HIGH_EDGE = 2;
    public static final int WINDOW_ALIGN_BOTH_EDGE = 3;
    public static final int WINDOW_ALIGN_NO_EDGE = 0;
    public static final float WINDOW_ALIGN_OFFSET_PERCENT_DISABLED = -1.0F;
    public static final float ITEM_ALIGN_OFFSET_PERCENT_DISABLED = -1.0F;
    public static final int SAVE_NO_CHILD = 0;
    public static final int SAVE_ON_SCREEN_CHILD = 1;
    public static final int SAVE_LIMITED_CHILD = 2;
    public static final int SAVE_ALL_CHILD = 3;
    private static final int PFLAG_RETAIN_FOCUS_FOR_CHILD = 1;
    GridLayoutManager mLayoutManager = new GridLayoutManager(this);
    private SmoothScrollByBehavior mSmoothScrollByBehavior;
    private boolean mAnimateChildLayout = true;
    private boolean mHasOverlappingRendering = true;
    private RecyclerView.ItemAnimator mSavedItemAnimator;
    private OnTouchInterceptListener mOnTouchInterceptListener;
    private OnMotionInterceptListener mOnMotionInterceptListener;
    private OnKeyInterceptListener mOnKeyInterceptListener;
    private OnUnhandledKeyListener mOnUnhandledKeyListener;
    int mInitialPrefetchItemCount = 4;
    private int mPrivateFlag;

    BaseGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setLayoutManager(this.mLayoutManager);
        this.setPreserveFocusAfterLayout(false);
        this.setDescendantFocusability(262144);
        this.setHasFixedSize(true);
        this.setChildrenDrawingOrderEnabled(true);
        this.setWillNotDraw(true);
        this.setOverScrollMode(2);
        ((SimpleItemAnimator)this.getItemAnimator()).setSupportsChangeAnimations(false);
        super.addRecyclerListener(new RecyclerView.RecyclerListener() {
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                BaseGridView.this.mLayoutManager.onChildRecycled(holder);
            }
        });
    }

    @SuppressLint({"CustomViewStyleable"})
    void initBaseGridViewAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, styleable.lbBaseGridView);
        boolean throughFront = a.getBoolean(styleable.lbBaseGridView_focusOutFront, false);
        boolean throughEnd = a.getBoolean(styleable.lbBaseGridView_focusOutEnd, false);
        this.mLayoutManager.setFocusOutAllowed(throughFront, throughEnd);
        boolean throughSideStart = a.getBoolean(styleable.lbBaseGridView_focusOutSideStart, true);
        boolean throughSideEnd = a.getBoolean(styleable.lbBaseGridView_focusOutSideEnd, true);
        this.mLayoutManager.setFocusOutSideAllowed(throughSideStart, throughSideEnd);
        this.mLayoutManager.setVerticalSpacing(a.getDimensionPixelSize(styleable.lbBaseGridView_android_verticalSpacing, a.getDimensionPixelSize(styleable.lbBaseGridView_verticalMargin, 0)));
        this.mLayoutManager.setHorizontalSpacing(a.getDimensionPixelSize(styleable.lbBaseGridView_android_horizontalSpacing, a.getDimensionPixelSize(styleable.lbBaseGridView_horizontalMargin, 0)));
        if (a.hasValue(styleable.lbBaseGridView_android_gravity)) {
            this.setGravity(a.getInt(styleable.lbBaseGridView_android_gravity, 0));
        }

        a.recycle();
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setFocusScrollStrategy(int scrollStrategy) {
        if (scrollStrategy != 0 && scrollStrategy != 1 && scrollStrategy != 2) {
            throw new IllegalArgumentException("Invalid scrollStrategy");
        } else {
            this.mLayoutManager.setFocusScrollStrategy(scrollStrategy);
            this.requestLayout();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public int getFocusScrollStrategy() {
        return this.mLayoutManager.getFocusScrollStrategy();
    }

    public void setWindowAlignment(int windowAlignment) {
        this.mLayoutManager.setWindowAlignment(windowAlignment);
        this.requestLayout();
    }

    public int getWindowAlignment() {
        return this.mLayoutManager.getWindowAlignment();
    }

    public void setWindowAlignmentPreferKeyLineOverLowEdge(boolean preferKeyLineOverLowEdge) {
        this.mLayoutManager.mWindowAlignment.mainAxis().setPreferKeylineOverLowEdge(preferKeyLineOverLowEdge);
        this.requestLayout();
    }

    public void setWindowAlignmentPreferKeyLineOverHighEdge(boolean preferKeyLineOverHighEdge) {
        this.mLayoutManager.mWindowAlignment.mainAxis().setPreferKeylineOverHighEdge(preferKeyLineOverHighEdge);
        this.requestLayout();
    }

    public boolean isWindowAlignmentPreferKeyLineOverLowEdge() {
        return this.mLayoutManager.mWindowAlignment.mainAxis().isPreferKeylineOverLowEdge();
    }

    public boolean isWindowAlignmentPreferKeyLineOverHighEdge() {
        return this.mLayoutManager.mWindowAlignment.mainAxis().isPreferKeylineOverHighEdge();
    }

    public void setWindowAlignmentOffset(int offset) {
        this.mLayoutManager.setWindowAlignmentOffset(offset);
        this.requestLayout();
    }

    public int getWindowAlignmentOffset() {
        return this.mLayoutManager.getWindowAlignmentOffset();
    }

    public void setWindowAlignmentOffsetPercent(float offsetPercent) {
        this.mLayoutManager.setWindowAlignmentOffsetPercent(offsetPercent);
        this.requestLayout();
    }

    public float getWindowAlignmentOffsetPercent() {
        return this.mLayoutManager.getWindowAlignmentOffsetPercent();
    }

    public void setItemAlignmentOffset(int offset) {
        this.mLayoutManager.setItemAlignmentOffset(offset);
        this.requestLayout();
    }

    public int getItemAlignmentOffset() {
        return this.mLayoutManager.getItemAlignmentOffset();
    }

    public void setItemAlignmentOffsetWithPadding(boolean withPadding) {
        this.mLayoutManager.setItemAlignmentOffsetWithPadding(withPadding);
        this.requestLayout();
    }

    public boolean isItemAlignmentOffsetWithPadding() {
        return this.mLayoutManager.isItemAlignmentOffsetWithPadding();
    }

    public void setItemAlignmentOffsetPercent(float offsetPercent) {
        this.mLayoutManager.setItemAlignmentOffsetPercent(offsetPercent);
        this.requestLayout();
    }

    public float getItemAlignmentOffsetPercent() {
        return this.mLayoutManager.getItemAlignmentOffsetPercent();
    }

    public void setItemAlignmentViewId(int viewId) {
        this.mLayoutManager.setItemAlignmentViewId(viewId);
    }

    public int getItemAlignmentViewId() {
        return this.mLayoutManager.getItemAlignmentViewId();
    }

    /** @deprecated */
    @Deprecated
    public void setItemMargin(int margin) {
        this.setItemSpacing(margin);
    }

    public void setItemSpacing(int spacing) {
        this.mLayoutManager.setItemSpacing(spacing);
        this.requestLayout();
    }

    /** @deprecated */
    @Deprecated
    public void setVerticalMargin(int margin) {
        this.setVerticalSpacing(margin);
    }

    /** @deprecated */
    @Deprecated
    public int getVerticalMargin() {
        return this.mLayoutManager.getVerticalSpacing();
    }

    /** @deprecated */
    @Deprecated
    public void setHorizontalMargin(int margin) {
        this.setHorizontalSpacing(margin);
    }

    /** @deprecated */
    @Deprecated
    public int getHorizontalMargin() {
        return this.mLayoutManager.getHorizontalSpacing();
    }

    public void setVerticalSpacing(int spacing) {
        this.mLayoutManager.setVerticalSpacing(spacing);
        this.requestLayout();
    }

    public int getVerticalSpacing() {
        return this.mLayoutManager.getVerticalSpacing();
    }

    public void setHorizontalSpacing(int spacing) {
        this.mLayoutManager.setHorizontalSpacing(spacing);
        this.requestLayout();
    }

    public int getHorizontalSpacing() {
        return this.mLayoutManager.getHorizontalSpacing();
    }

    public void setOnChildLaidOutListener(@Nullable OnChildLaidOutListener listener) {
        this.mLayoutManager.setOnChildLaidOutListener(listener);
    }

    @SuppressLint({"ReferencesDeprecated"})
    public void setOnChildSelectedListener(@Nullable OnChildSelectedListener listener) {
        this.mLayoutManager.setOnChildSelectedListener(listener);
    }

    public final void addOnLayoutCompletedListener(@NonNull OnLayoutCompletedListener listener) {
        this.mLayoutManager.addOnLayoutCompletedListener(listener);
    }

    public final void removeOnLayoutCompletedListener(@NonNull OnLayoutCompletedListener listener) {
        this.mLayoutManager.removeOnLayoutCompletedListener(listener);
    }

    public void setOnChildViewHolderSelectedListener(@Nullable OnChildViewHolderSelectedListener listener) {
        this.mLayoutManager.setOnChildViewHolderSelectedListener(listener);
    }

    public void addOnChildViewHolderSelectedListener(@NonNull OnChildViewHolderSelectedListener listener) {
        this.mLayoutManager.addOnChildViewHolderSelectedListener(listener);
    }

    public void removeOnChildViewHolderSelectedListener(@NonNull OnChildViewHolderSelectedListener listener) {
        this.mLayoutManager.removeOnChildViewHolderSelectedListener(listener);
    }

    public void setSelectedPosition(int position) {
        this.mLayoutManager.setSelection(position, 0);
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setSelectedPositionWithSub(int position, int subposition) {
        this.mLayoutManager.setSelectionWithSub(position, subposition, 0);
    }

    public void setSelectedPosition(int position, int scrollExtra) {
        this.mLayoutManager.setSelection(position, scrollExtra);
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setSelectedPositionWithSub(int position, int subposition, int scrollExtra) {
        this.mLayoutManager.setSelectionWithSub(position, subposition, scrollExtra);
    }

    public void setSelectedPositionSmooth(int position) {
        this.mLayoutManager.setSelectionSmooth(position);
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setSelectedPositionSmoothWithSub(int position, int subposition) {
        this.mLayoutManager.setSelectionSmoothWithSub(position, subposition);
    }

    public void setSelectedPositionSmooth(final int position, @Nullable final ViewHolderTask task) {
        if (task != null) {
            RecyclerView.ViewHolder vh = this.findViewHolderForPosition(position);
            if (vh != null && !this.hasPendingAdapterUpdates()) {
                task.run(vh);
            } else {
                this.addOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
                    public void onChildViewHolderSelected(@NonNull RecyclerView parent, RecyclerView.ViewHolder child, int selectedPosition, int subposition) {
                        if (selectedPosition == position) {
                            BaseGridView.this.removeOnChildViewHolderSelectedListener(this);
                            task.run(child);
                        }

                    }
                });
            }
        }

        this.setSelectedPositionSmooth(position);
    }

    public void setSelectedPosition(final int position, @Nullable final ViewHolderTask task) {
        if (task != null) {
            RecyclerView.ViewHolder vh = this.findViewHolderForPosition(position);
            if (vh != null && !this.hasPendingAdapterUpdates()) {
                task.run(vh);
            } else {
                this.addOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
                    public void onChildViewHolderSelectedAndPositioned(@NonNull RecyclerView parent, RecyclerView.ViewHolder child, int selectedPosition, int subposition) {
                        if (selectedPosition == position) {
                            BaseGridView.this.removeOnChildViewHolderSelectedListener(this);
                            task.run(child);
                        }

                    }
                });
            }
        }

        this.setSelectedPosition(position);
    }

    public int getSelectedPosition() {
        return this.mLayoutManager.getSelection();
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public int getSelectedSubPosition() {
        return this.mLayoutManager.getSubSelection();
    }

    public void setAnimateChildLayout(boolean animateChildLayout) {
        if (this.mAnimateChildLayout != animateChildLayout) {
            this.mAnimateChildLayout = animateChildLayout;
            if (!this.mAnimateChildLayout) {
                this.mSavedItemAnimator = this.getItemAnimator();
                super.setItemAnimator((RecyclerView.ItemAnimator)null);
            } else {
                super.setItemAnimator(this.mSavedItemAnimator);
            }
        }

    }

    public boolean isChildLayoutAnimated() {
        return this.mAnimateChildLayout;
    }

    public void setGravity(int gravity) {
        this.mLayoutManager.setGravity(gravity);
        this.requestLayout();
    }

    public void setLayoutManager(@Nullable RecyclerView.LayoutManager layout) {
        if (layout == null) {
            super.setLayoutManager((RecyclerView.LayoutManager)null);
            if (this.mLayoutManager != null) {
                this.mLayoutManager.setGridView((BaseGridView)null);
            }

            this.mLayoutManager = null;
        } else {
            this.mLayoutManager = (GridLayoutManager)layout;
            this.mLayoutManager.setGridView(this);
            super.setLayoutManager(layout);
        }
    }

    public boolean onRequestFocusInDescendants(int direction, @Nullable Rect previouslyFocusedRect) {
        return (this.mPrivateFlag & 1) == 1 ? false : this.mLayoutManager.gridOnRequestFocusInDescendants(this, direction, previouslyFocusedRect);
    }

    public void getViewSelectedOffsets(@NonNull View view, @NonNull int[] offsets) {
        this.mLayoutManager.getViewSelectedOffsets(view, offsets);
    }

    public int getChildDrawingOrder(int childCount, int i) {
        return this.mLayoutManager.getChildDrawingOrder(this, childCount, i);
    }

    final boolean isChildrenDrawingOrderEnabledInternal() {
        return this.isChildrenDrawingOrderEnabled();
    }

    @Nullable
    public View focusSearch(int direction) {
        if (this.isFocused()) {
            View view = this.mLayoutManager.findViewByPosition(this.mLayoutManager.getSelection());
            if (view != null) {
                return this.focusSearch(view, direction);
            }
        }

        return super.focusSearch(direction);
    }

    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        this.mLayoutManager.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public final void setFocusSearchDisabled(boolean disabled) {
        this.setDescendantFocusability(disabled ? 393216 : 262144);
        this.mLayoutManager.setFocusSearchDisabled(disabled);
    }

    public final boolean isFocusSearchDisabled() {
        return this.mLayoutManager.isFocusSearchDisabled();
    }

    public void setLayoutEnabled(boolean layoutEnabled) {
        this.mLayoutManager.setLayoutEnabled(layoutEnabled);
    }

    public void setChildrenVisibility(int visibility) {
        this.mLayoutManager.setChildrenVisibility(visibility);
    }

    public void setPruneChild(boolean pruneChild) {
        this.mLayoutManager.setPruneChild(pruneChild);
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        this.mLayoutManager.setScrollEnabled(scrollEnabled);
    }

    public boolean isScrollEnabled() {
        return this.mLayoutManager.isScrollEnabled();
    }

    public boolean hasPreviousViewInSameRow(int position) {
        return this.mLayoutManager.hasPreviousViewInSameRow(position);
    }

    public void setFocusDrawingOrderEnabled(boolean enabled) {
        super.setChildrenDrawingOrderEnabled(enabled);
    }

    public boolean isFocusDrawingOrderEnabled() {
        return super.isChildrenDrawingOrderEnabled();
    }

    public void setOnTouchInterceptListener(@Nullable OnTouchInterceptListener listener) {
        this.mOnTouchInterceptListener = listener;
    }

    public void setOnMotionInterceptListener(@Nullable OnMotionInterceptListener listener) {
        this.mOnMotionInterceptListener = listener;
    }

    public void setOnKeyInterceptListener(@Nullable OnKeyInterceptListener listener) {
        this.mOnKeyInterceptListener = listener;
    }

    public void setOnUnhandledKeyListener(@Nullable OnUnhandledKeyListener listener) {
        this.mOnUnhandledKeyListener = listener;
    }

    @Nullable
    public OnUnhandledKeyListener getOnUnhandledKeyListener() {
        return this.mOnUnhandledKeyListener;
    }

    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        if (this.mOnKeyInterceptListener != null && this.mOnKeyInterceptListener.onInterceptKeyEvent(event)) {
            return true;
        } else if (super.dispatchKeyEvent(event)) {
            return true;
        } else {
            return this.mOnUnhandledKeyListener != null && this.mOnUnhandledKeyListener.onUnhandledKey(event);
        }
    }

    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        return this.mOnTouchInterceptListener != null && this.mOnTouchInterceptListener.onInterceptTouchEvent(event) ? true : super.dispatchTouchEvent(event);
    }

    protected boolean dispatchGenericFocusedEvent(@NonNull MotionEvent event) {
        return this.mOnMotionInterceptListener != null && this.mOnMotionInterceptListener.onInterceptMotionEvent(event) ? true : super.dispatchGenericFocusedEvent(event);
    }

    public final int getSaveChildrenPolicy() {
        return this.mLayoutManager.mChildrenStates.getSavePolicy();
    }

    public final int getSaveChildrenLimitNumber() {
        return this.mLayoutManager.mChildrenStates.getLimitNumber();
    }

    public final void setSaveChildrenPolicy(int savePolicy) {
        this.mLayoutManager.mChildrenStates.setSavePolicy(savePolicy);
    }

    public final void setSaveChildrenLimitNumber(int limitNumber) {
        this.mLayoutManager.mChildrenStates.setLimitNumber(limitNumber);
    }

    public boolean hasOverlappingRendering() {
        return this.mHasOverlappingRendering;
    }

    public void setHasOverlappingRendering(boolean hasOverlapping) {
        this.mHasOverlappingRendering = hasOverlapping;
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        if (this.mLayoutManager != null) {
            this.mLayoutManager.onRtlPropertiesChanged(layoutDirection);
        }

    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setExtraLayoutSpace(int extraLayoutSpace) {
        this.mLayoutManager.setExtraLayoutSpace(extraLayoutSpace);
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public int getExtraLayoutSpace() {
        return this.mLayoutManager.getExtraLayoutSpace();
    }

    public void animateOut() {
        this.mLayoutManager.slideOut();
    }

    public void animateIn() {
        this.mLayoutManager.slideIn();
    }

    public void scrollToPosition(int position) {
        if (this.mLayoutManager.isSlidingChildViews()) {
            this.mLayoutManager.setSelectionWithSub(position, 0, 0);
        } else {
            super.scrollToPosition(position);
        }
    }

    public void smoothScrollToPosition(int position) {
        if (this.mLayoutManager.isSlidingChildViews()) {
            this.mLayoutManager.setSelectionWithSub(position, 0, 0);
        } else {
            super.smoothScrollToPosition(position);
        }
    }

    public final void setSmoothScrollByBehavior(@Nullable SmoothScrollByBehavior behavior) {
        this.mSmoothScrollByBehavior = behavior;
    }

    @Nullable
    public SmoothScrollByBehavior getSmoothScrollByBehavior() {
        return this.mSmoothScrollByBehavior;
    }

    public void smoothScrollBy(int dx, int dy) {
        if (this.mSmoothScrollByBehavior != null) {
            this.smoothScrollBy(dx, dy, this.mSmoothScrollByBehavior.configSmoothScrollByInterpolator(dx, dy), this.mSmoothScrollByBehavior.configSmoothScrollByDuration(dx, dy));
        } else {
            this.smoothScrollBy(dx, dy, (Interpolator)null, Integer.MIN_VALUE);
        }

    }

    public void smoothScrollBy(int dx, int dy, @Nullable Interpolator interpolator) {
        if (this.mSmoothScrollByBehavior != null) {
            this.smoothScrollBy(dx, dy, interpolator, this.mSmoothScrollByBehavior.configSmoothScrollByDuration(dx, dy));
        } else {
            this.smoothScrollBy(dx, dy, interpolator, Integer.MIN_VALUE);
        }

    }

    public final void setSmoothScrollSpeedFactor(float smoothScrollSpeedFactor) {
        this.mLayoutManager.mSmoothScrollSpeedFactor = smoothScrollSpeedFactor;
    }

    public final float getSmoothScrollSpeedFactor() {
        return this.mLayoutManager.mSmoothScrollSpeedFactor;
    }

    public final void setSmoothScrollMaxPendingMoves(int maxPendingMoves) {
        this.mLayoutManager.mMaxPendingMoves = maxPendingMoves;
    }

    public final int getSmoothScrollMaxPendingMoves() {
        return this.mLayoutManager.mMaxPendingMoves;
    }

    public void setInitialPrefetchItemCount(int itemCount) {
        this.mInitialPrefetchItemCount = itemCount;
    }

    public int getInitialPrefetchItemCount() {
        return this.mInitialPrefetchItemCount;
    }

    public void removeView(@NonNull View view) {
        boolean retainFocusForChild = view.hasFocus() && this.isFocusable();
        if (retainFocusForChild) {
            this.mPrivateFlag |= 1;
            this.requestFocus();
        }

        super.removeView(view);
        if (retainFocusForChild) {
            this.mPrivateFlag ^= -2;
        }

    }

    public void removeViewAt(int index) {
        boolean retainFocusForChild = this.getChildAt(index).hasFocus();
        if (retainFocusForChild) {
            this.mPrivateFlag |= 1;
            this.requestFocus();
        }

        super.removeViewAt(index);
        if (retainFocusForChild) {
            this.mPrivateFlag ^= -2;
        }

    }

    public interface OnLayoutCompletedListener {
        void onLayoutCompleted(@NonNull RecyclerView.State var1);
    }

    public interface OnTouchInterceptListener {
        boolean onInterceptTouchEvent(@NonNull MotionEvent var1);
    }

    public interface OnMotionInterceptListener {
        boolean onInterceptMotionEvent(@NonNull MotionEvent var1);
    }

    public interface OnKeyInterceptListener {
        boolean onInterceptKeyEvent(@NonNull KeyEvent var1);
    }

    public interface OnUnhandledKeyListener {
        boolean onUnhandledKey(@NonNull KeyEvent var1);
    }

    public interface SmoothScrollByBehavior {
        int configSmoothScrollByDuration(int var1, int var2);

        @Nullable
        Interpolator configSmoothScrollByInterpolator(int var1, int var2);
    }
}
