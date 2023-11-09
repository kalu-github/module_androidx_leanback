//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package androidx.leanback.widget;

import androidx.collection.CircularArray;
import androidx.collection.CircularIntArray;
import java.io.PrintWriter;

abstract class StaggeredGrid extends Grid {
    protected CircularArray<Location> mLocations = new CircularArray(64);
    protected int mFirstIndex = -1;
    protected Object mPendingItem;
    protected int mPendingItemSize;

    StaggeredGrid() {
    }

    public final int getFirstIndex() {
        return this.mFirstIndex;
    }

    public final int getLastIndex() {
        return this.mFirstIndex + this.mLocations.size() - 1;
    }

    public final int getSize() {
        return this.mLocations.size();
    }

    public final Location getLocation(int index) {
        int indexInArray = index - this.mFirstIndex;
        return indexInArray >= 0 && indexInArray < this.mLocations.size() ? (Location)this.mLocations.get(indexInArray) : null;
    }

    public final void debugPrint(PrintWriter pw) {
        int i = 0;

        for(int size = this.mLocations.size(); i < size; ++i) {
            Location loc = (Location)this.mLocations.get(i);
            pw.print("<" + (this.mFirstIndex + i) + "," + loc.mRow + ">");
            pw.print(" ");
            pw.println();
        }

    }

    protected final boolean prependVisibleItems(int toLimit, boolean oneColumnMode) {
        if (this.mProvider.getCount() == 0) {
            return false;
        } else if (!oneColumnMode && this.checkPrependOverLimit(toLimit)) {
            return false;
        } else {
            boolean var3;
            try {
                if (this.prependVisbleItemsWithCache(toLimit, oneColumnMode)) {
                    var3 = true;
                    return var3;
                }

                var3 = this.prependVisibleItemsWithoutCache(toLimit, oneColumnMode);
            } finally {
                this.mTmpItem[0] = null;
                this.mPendingItem = null;
            }

            return var3;
        }
    }

    protected final boolean prependVisbleItemsWithCache(int toLimit, boolean oneColumnMode) {
        if (this.mLocations.size() == 0) {
            return false;
        } else {
            int itemIndex;
            int edge;
            int offset;
            if (this.mFirstVisibleIndex >= 0) {
                edge = this.mProvider.getEdge(this.mFirstVisibleIndex);
                offset = this.getLocation(this.mFirstVisibleIndex).mOffset;
                itemIndex = this.mFirstVisibleIndex - 1;
            } else {
                edge = Integer.MAX_VALUE;
                offset = 0;
                itemIndex = this.mStartIndex != -1 ? this.mStartIndex : 0;
                if (itemIndex > this.getLastIndex() || itemIndex < this.getFirstIndex() - 1) {
                    this.mLocations.clear();
                    return false;
                }

                if (itemIndex < this.getFirstIndex()) {
                    return false;
                }
            }

            for(int firstIndex = Math.max(this.mProvider.getMinIndex(), this.mFirstIndex); itemIndex >= firstIndex; --itemIndex) {
                Location loc = this.getLocation(itemIndex);
                int rowIndex = loc.mRow;
                int size = this.mProvider.createItem(itemIndex, false, this.mTmpItem, false);
                if (size != loc.mSize) {
                    this.mLocations.removeFromStart(itemIndex + 1 - this.mFirstIndex);
                    this.mFirstIndex = this.mFirstVisibleIndex;
                    this.mPendingItem = this.mTmpItem[0];
                    this.mPendingItemSize = size;
                    return false;
                }

                this.mFirstVisibleIndex = itemIndex;
                if (this.mLastVisibleIndex < 0) {
                    this.mLastVisibleIndex = itemIndex;
                }

                this.mProvider.addItem(this.mTmpItem[0], itemIndex, size, rowIndex, edge - offset);
                if (!oneColumnMode && this.checkPrependOverLimit(toLimit)) {
                    return true;
                }

                edge = this.mProvider.getEdge(itemIndex);
                offset = loc.mOffset;
                if (rowIndex == 0 && oneColumnMode) {
                    return true;
                }
            }

            return false;
        }
    }

    private int calculateOffsetAfterLastItem(int row) {
        int cachedIndex = this.getLastIndex();

        boolean foundCachedItemInSameRow;
        for(foundCachedItemInSameRow = false; cachedIndex >= this.mFirstIndex; --cachedIndex) {
            Location loc = this.getLocation(cachedIndex);
            if (loc.mRow == row) {
                foundCachedItemInSameRow = true;
                break;
            }
        }

        if (!foundCachedItemInSameRow) {
            cachedIndex = this.getLastIndex();
        }

        int offset = this.isReversedFlow() ? -this.getLocation(cachedIndex).mSize - this.mSpacing : this.getLocation(cachedIndex).mSize + this.mSpacing;

        for(int i = cachedIndex + 1; i <= this.getLastIndex(); ++i) {
            offset -= this.getLocation(i).mOffset;
        }

        return offset;
    }

    protected abstract boolean prependVisibleItemsWithoutCache(int var1, boolean var2);

    protected final int prependVisibleItemToRow(int itemIndex, int rowIndex, int edge) {
        if (this.mFirstVisibleIndex < 0 || this.mFirstVisibleIndex == this.getFirstIndex() && this.mFirstVisibleIndex == itemIndex + 1) {
            Location oldFirstLoc = this.mFirstIndex >= 0 ? this.getLocation(this.mFirstIndex) : null;
            int oldFirstEdge = this.mProvider.getEdge(this.mFirstIndex);
            Location loc = new Location(rowIndex, 0, 0);
            this.mLocations.addFirst(loc);
            Object item;
            if (this.mPendingItem != null) {
                loc.mSize = this.mPendingItemSize;
                item = this.mPendingItem;
                this.mPendingItem = null;
            } else {
                loc.mSize = this.mProvider.createItem(itemIndex, false, this.mTmpItem, false);
                item = this.mTmpItem[0];
            }

            this.mFirstIndex = this.mFirstVisibleIndex = itemIndex;
            if (this.mLastVisibleIndex < 0) {
                this.mLastVisibleIndex = itemIndex;
            }

            int thisEdge = !this.mReversedFlow ? edge - loc.mSize : edge + loc.mSize;
            if (oldFirstLoc != null) {
                oldFirstLoc.mOffset = oldFirstEdge - thisEdge;
            }

            this.mProvider.addItem(item, itemIndex, loc.mSize, rowIndex, thisEdge);
            return loc.mSize;
        } else {
            throw new IllegalStateException();
        }
    }

    protected final boolean appendVisibleItems(int toLimit, boolean oneColumnMode) {
        if (this.mProvider.getCount() == 0) {
            return false;
        } else if (!oneColumnMode && this.checkAppendOverLimit(toLimit)) {
            return false;
        } else {
            boolean var3;
            try {
                if (!this.appendVisbleItemsWithCache(toLimit, oneColumnMode)) {
                    var3 = this.appendVisibleItemsWithoutCache(toLimit, oneColumnMode);
                    return var3;
                }

                var3 = true;
            } finally {
                this.mTmpItem[0] = null;
                this.mPendingItem = null;
            }

            return var3;
        }
    }

    protected final boolean appendVisbleItemsWithCache(int toLimit, boolean oneColumnMode) {
        if (this.mLocations.size() == 0) {
            return false;
        } else {
            int count = this.mProvider.getCount();
            int itemIndex;
            int edge;
            if (this.mLastVisibleIndex >= 0) {
                itemIndex = this.mLastVisibleIndex + 1;
                edge = this.mProvider.getEdge(this.mLastVisibleIndex);
            } else {
                edge = Integer.MAX_VALUE;
                itemIndex = this.mStartIndex != -1 ? this.mStartIndex : 0;
                if (itemIndex > this.getLastIndex() + 1 || itemIndex < this.getFirstIndex()) {
                    this.mLocations.clear();
                    return false;
                }

                if (itemIndex > this.getLastIndex()) {
                    return false;
                }
            }

            for(int lastIndex = this.getLastIndex(); itemIndex < count && itemIndex <= lastIndex; ++itemIndex) {
                Location loc = this.getLocation(itemIndex);
                if (edge != Integer.MAX_VALUE) {
                    edge += loc.mOffset;
                }

                int rowIndex = loc.mRow;
                int size = this.mProvider.createItem(itemIndex, true, this.mTmpItem, false);
                if (size != loc.mSize) {
                    loc.mSize = size;
                    this.mLocations.removeFromEnd(lastIndex - itemIndex);
                    lastIndex = itemIndex;
                }

                this.mLastVisibleIndex = itemIndex;
                if (this.mFirstVisibleIndex < 0) {
                    this.mFirstVisibleIndex = itemIndex;
                }

                this.mProvider.addItem(this.mTmpItem[0], itemIndex, size, rowIndex, edge);
                if (!oneColumnMode && this.checkAppendOverLimit(toLimit)) {
                    return true;
                }

                if (edge == Integer.MAX_VALUE) {
                    edge = this.mProvider.getEdge(itemIndex);
                }

                if (rowIndex == this.mNumRows - 1 && oneColumnMode) {
                    return true;
                }
            }

            return false;
        }
    }

    protected abstract boolean appendVisibleItemsWithoutCache(int var1, boolean var2);

    protected final int appendVisibleItemToRow(int itemIndex, int rowIndex, int location) {
        if (this.mLastVisibleIndex >= 0 && (this.mLastVisibleIndex != this.getLastIndex() || this.mLastVisibleIndex != itemIndex - 1)) {
            throw new IllegalStateException();
        } else {
            int offset;
            if (this.mLastVisibleIndex < 0) {
                if (this.mLocations.size() > 0 && itemIndex == this.getLastIndex() + 1) {
                    offset = this.calculateOffsetAfterLastItem(rowIndex);
                } else {
                    offset = 0;
                }
            } else {
                offset = location - this.mProvider.getEdge(this.mLastVisibleIndex);
            }

            Location loc = new Location(rowIndex, offset, 0);
            this.mLocations.addLast(loc);
            Object item;
            if (this.mPendingItem != null) {
                loc.mSize = this.mPendingItemSize;
                item = this.mPendingItem;
                this.mPendingItem = null;
            } else {
                loc.mSize = this.mProvider.createItem(itemIndex, true, this.mTmpItem, false);
                item = this.mTmpItem[0];
            }

            if (this.mLocations.size() == 1) {
                this.mFirstIndex = this.mFirstVisibleIndex = this.mLastVisibleIndex = itemIndex;
            } else if (this.mLastVisibleIndex < 0) {
                this.mFirstVisibleIndex = this.mLastVisibleIndex = itemIndex;
            } else {
                ++this.mLastVisibleIndex;
            }

            this.mProvider.addItem(item, itemIndex, loc.mSize, rowIndex, location);
            return loc.mSize;
        }
    }

    public final CircularIntArray[] getItemPositionsInRows(int startPos, int endPos) {
        int i;
        for(i = 0; i < this.mNumRows; ++i) {
            this.mTmpItemPositionsInRows[i].clear();
        }

        if (startPos >= 0) {
            for(i = startPos; i <= endPos; ++i) {
                CircularIntArray row = this.mTmpItemPositionsInRows[this.getLocation(i).mRow];
                if (row.size() > 0 && row.getLast() == i - 1) {
                    row.popLast();
                    row.addLast(i);
                } else {
                    row.addLast(i);
                    row.addLast(i);
                }
            }
        }

        return this.mTmpItemPositionsInRows;
    }

    public void invalidateItemsAfter(int index) {
        super.invalidateItemsAfter(index);
        this.mLocations.removeFromEnd(this.getLastIndex() - index + 1);
        if (this.mLocations.size() == 0) {
            this.mFirstIndex = -1;
        }

    }

    static class Location extends Grid.Location {
        int mOffset;
        int mSize;

        Location(int row, int offset, int size) {
            super(row);
            this.mOffset = offset;
            this.mSize = size;
        }
    }
}
