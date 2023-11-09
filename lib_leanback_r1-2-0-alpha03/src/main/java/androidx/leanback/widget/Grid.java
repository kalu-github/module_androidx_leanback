//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package androidx.leanback.widget;

import android.util.SparseIntArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.CircularIntArray;
import androidx.recyclerview.widget.RecyclerView;
import java.io.PrintWriter;
import java.util.Arrays;

abstract class Grid {
    static final int START_DEFAULT = -1;
    Object[] mTmpItem = new Object[1];
    protected Provider mProvider;
    protected boolean mReversedFlow;
    protected int mSpacing;
    protected int mNumRows;
    protected int mFirstVisibleIndex = -1;
    protected int mLastVisibleIndex = -1;
    protected CircularIntArray[] mTmpItemPositionsInRows;
    protected int mStartIndex = -1;

    Grid() {
    }

    public static Grid createGrid(int rows) {
        Object grid;
        if (rows == 1) {
            grid = new SingleRow();
        } else {
            grid = new StaggeredGridDefault();
            ((Grid)grid).setNumRows(rows);
        }

        return (Grid)grid;
    }

    public final void setSpacing(int spacing) {
        this.mSpacing = spacing;
    }

    public final void setReversedFlow(boolean reversedFlow) {
        this.mReversedFlow = reversedFlow;
    }

    public boolean isReversedFlow() {
        return this.mReversedFlow;
    }

    public void setProvider(Provider provider) {
        this.mProvider = provider;
    }

    public void setStart(int startIndex) {
        this.mStartIndex = startIndex;
    }

    public int getNumRows() {
        return this.mNumRows;
    }

    void setNumRows(int numRows) {
        if (numRows <= 0) {
            throw new IllegalArgumentException();
        } else if (this.mNumRows != numRows) {
            this.mNumRows = numRows;
            this.mTmpItemPositionsInRows = new CircularIntArray[this.mNumRows];

            for(int i = 0; i < this.mNumRows; ++i) {
                this.mTmpItemPositionsInRows[i] = new CircularIntArray();
            }

        }
    }

    public final int getFirstVisibleIndex() {
        return this.mFirstVisibleIndex;
    }

    public final int getLastVisibleIndex() {
        return this.mLastVisibleIndex;
    }

    public void resetVisibleIndex() {
        this.mFirstVisibleIndex = this.mLastVisibleIndex = -1;
    }

    public void invalidateItemsAfter(int index) {
        if (index >= 0) {
            if (this.mLastVisibleIndex >= 0) {
                if (this.mLastVisibleIndex >= index) {
                    this.mLastVisibleIndex = index - 1;
                }

                this.resetVisibleIndexIfEmpty();
                if (this.getFirstVisibleIndex() < 0) {
                    this.setStart(index);
                }

            }
        }
    }

    public final int getRowIndex(int index) {
        Location location = this.getLocation(index);
        return location == null ? -1 : location.mRow;
    }

    public abstract Location getLocation(int var1);

    public final int findRowMin(boolean findLarge, @Nullable int[] indices) {
        return this.findRowMin(findLarge, this.mReversedFlow ? this.mLastVisibleIndex : this.mFirstVisibleIndex, indices);
    }

    protected abstract int findRowMin(boolean var1, int var2, int[] var3);

    public final int findRowMax(boolean findLarge, @Nullable int[] indices) {
        return this.findRowMax(findLarge, this.mReversedFlow ? this.mFirstVisibleIndex : this.mLastVisibleIndex, indices);
    }

    protected abstract int findRowMax(boolean var1, int var2, int[] var3);

    protected final boolean checkAppendOverLimit(int toLimit) {
        if (this.mLastVisibleIndex < 0) {
            return false;
        } else {
            return this.mReversedFlow ? this.findRowMin(true, (int[])null) <= toLimit + this.mSpacing : this.findRowMax(false, (int[])null) >= toLimit - this.mSpacing;
        }
    }

    protected final boolean checkPrependOverLimit(int toLimit) {
        if (this.mLastVisibleIndex < 0) {
            return false;
        } else {
            return this.mReversedFlow ? this.findRowMax(false, (int[])null) >= toLimit - this.mSpacing : this.findRowMin(true, (int[])null) <= toLimit + this.mSpacing;
        }
    }

    public abstract CircularIntArray[] getItemPositionsInRows(int var1, int var2);

    public final CircularIntArray[] getItemPositionsInRows() {
        return this.getItemPositionsInRows(this.getFirstVisibleIndex(), this.getLastVisibleIndex());
    }

    public final boolean prependOneColumnVisibleItems() {
        return this.prependVisibleItems(this.mReversedFlow ? Integer.MIN_VALUE : Integer.MAX_VALUE, true);
    }

    public final void prependVisibleItems(int toLimit) {
        this.prependVisibleItems(toLimit, false);
    }

    protected abstract boolean prependVisibleItems(int var1, boolean var2);

    public boolean appendOneColumnVisibleItems() {
        return this.appendVisibleItems(this.mReversedFlow ? Integer.MAX_VALUE : Integer.MIN_VALUE, true);
    }

    public final void appendVisibleItems(int toLimit) {
        this.appendVisibleItems(toLimit, false);
    }

    protected abstract boolean appendVisibleItems(int var1, boolean var2);

    public void removeInvisibleItemsAtEnd(int aboveIndex, int toLimit) {
        while(true) {
            if (this.mLastVisibleIndex >= this.mFirstVisibleIndex && this.mLastVisibleIndex > aboveIndex) {
                boolean offEnd = !this.mReversedFlow ? this.mProvider.getEdge(this.mLastVisibleIndex) >= toLimit : this.mProvider.getEdge(this.mLastVisibleIndex) <= toLimit;
                if (offEnd) {
                    this.mProvider.removeItem(this.mLastVisibleIndex);
                    --this.mLastVisibleIndex;
                    continue;
                }
            }

            this.resetVisibleIndexIfEmpty();
            return;
        }
    }

    public void removeInvisibleItemsAtFront(int belowIndex, int toLimit) {
        while(true) {
            if (this.mLastVisibleIndex >= this.mFirstVisibleIndex && this.mFirstVisibleIndex < belowIndex) {
                int size = this.mProvider.getSize(this.mFirstVisibleIndex);
                boolean offFront = !this.mReversedFlow ? this.mProvider.getEdge(this.mFirstVisibleIndex) + size <= toLimit : this.mProvider.getEdge(this.mFirstVisibleIndex) - size >= toLimit;
                if (offFront) {
                    this.mProvider.removeItem(this.mFirstVisibleIndex);
                    ++this.mFirstVisibleIndex;
                    continue;
                }
            }

            this.resetVisibleIndexIfEmpty();
            return;
        }
    }

    private void resetVisibleIndexIfEmpty() {
        if (this.mLastVisibleIndex < this.mFirstVisibleIndex) {
            this.resetVisibleIndex();
        }

    }

    public void fillDisappearingItems(int[] positions, int positionsLength, SparseIntArray positionToRow) {
        int lastPos = this.getLastVisibleIndex();
        int resultSearchLast = lastPos >= 0 ? Arrays.binarySearch(positions, 0, positionsLength, lastPos) : 0;
        int firstPos;
        int edge;
        int firstDisappearingIndex;
        int edge;
        int i;
        int disappearingIndex;
        if (resultSearchLast < 0) {
            firstPos = -resultSearchLast - 1;
            if (this.mReversedFlow) {
                edge = this.mProvider.getEdge(lastPos) - this.mProvider.getSize(lastPos) - this.mSpacing;
            } else {
                edge = this.mProvider.getEdge(lastPos) + this.mProvider.getSize(lastPos) + this.mSpacing;
            }

            for(firstDisappearingIndex = firstPos; firstDisappearingIndex < positionsLength; ++firstDisappearingIndex) {
                edge = positions[firstDisappearingIndex];
                i = positionToRow.get(edge);
                if (i < 0) {
                    i = 0;
                }

                disappearingIndex = this.mProvider.createItem(edge, true, this.mTmpItem, true);
                this.mProvider.addItem(this.mTmpItem[0], edge, disappearingIndex, i, edge);
                if (this.mReversedFlow) {
                    edge = edge - disappearingIndex - this.mSpacing;
                } else {
                    edge = edge + disappearingIndex + this.mSpacing;
                }
            }
        }

        firstPos = this.getFirstVisibleIndex();
        edge = firstPos >= 0 ? Arrays.binarySearch(positions, 0, positionsLength, firstPos) : 0;
        if (edge < 0) {
            firstDisappearingIndex = -edge - 2;
            if (this.mReversedFlow) {
                edge = this.mProvider.getEdge(firstPos);
            } else {
                edge = this.mProvider.getEdge(firstPos);
            }

            for(i = firstDisappearingIndex; i >= 0; --i) {
                disappearingIndex = positions[i];
                int disappearingRow = positionToRow.get(disappearingIndex);
                if (disappearingRow < 0) {
                    disappearingRow = 0;
                }

                int size = this.mProvider.createItem(disappearingIndex, false, this.mTmpItem, true);
                if (this.mReversedFlow) {
                    edge = edge + this.mSpacing + size;
                } else {
                    edge = edge - this.mSpacing - size;
                }

                this.mProvider.addItem(this.mTmpItem[0], disappearingIndex, size, disappearingRow, edge);
            }
        }

    }

    public void collectAdjacentPrefetchPositions(int fromLimit, int da, @NonNull RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
    }

    public abstract void debugPrint(PrintWriter var1);

    interface Provider {
        int getCount();

        int getMinIndex();

        int createItem(int var1, boolean var2, Object[] var3, boolean var4);

        void addItem(Object var1, int var2, int var3, int var4, int var5);

        void removeItem(int var1);

        int getEdge(int var1);

        int getSize(int var1);
    }

    static class Location {
        int mRow;

        Location(int row) {
            this.mRow = row;
        }
    }
}
