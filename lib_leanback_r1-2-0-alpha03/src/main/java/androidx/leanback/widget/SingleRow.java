//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package androidx.leanback.widget;

import androidx.annotation.NonNull;
import androidx.collection.CircularIntArray;
import androidx.recyclerview.widget.RecyclerView;
import java.io.PrintWriter;

class SingleRow extends Grid {
    private final Grid.Location mTmpLocation = new Grid.Location(0);

    SingleRow() {
        this.setNumRows(1);
    }

    public final Grid.Location getLocation(int index) {
        return this.mTmpLocation;
    }

    public final void debugPrint(PrintWriter pw) {
        pw.print("SingleRow<");
        pw.print(this.mFirstVisibleIndex);
        pw.print(",");
        pw.print(this.mLastVisibleIndex);
        pw.print(">");
        pw.println();
    }

    int getStartIndexForAppend() {
        if (this.mLastVisibleIndex >= 0) {
            return this.mLastVisibleIndex + 1;
        } else {
            return this.mStartIndex != -1 ? Math.min(this.mStartIndex, this.mProvider.getCount() - 1) : 0;
        }
    }

    int getStartIndexForPrepend() {
        if (this.mFirstVisibleIndex >= 0) {
            return this.mFirstVisibleIndex - 1;
        } else {
            return this.mStartIndex != -1 ? Math.min(this.mStartIndex, this.mProvider.getCount() - 1) : this.mProvider.getCount() - 1;
        }
    }

    protected final boolean prependVisibleItems(int toLimit, boolean oneColumnMode) {
        if (this.mProvider.getCount() == 0) {
            return false;
        } else if (!oneColumnMode && this.checkPrependOverLimit(toLimit)) {
            return false;
        } else {
            boolean filledOne = false;
            int minIndex = this.mProvider.getMinIndex();

            for(int index = this.getStartIndexForPrepend(); index >= minIndex; --index) {
                int size = this.mProvider.createItem(index, false, this.mTmpItem, false);
                int edge;
                if (this.mFirstVisibleIndex >= 0 && this.mLastVisibleIndex >= 0) {
                    if (this.mReversedFlow) {
                        edge = this.mProvider.getEdge(index + 1) + this.mSpacing + size;
                    } else {
                        edge = this.mProvider.getEdge(index + 1) - this.mSpacing - size;
                    }

                    this.mFirstVisibleIndex = index;
                } else {
                    edge = this.mReversedFlow ? Integer.MIN_VALUE : Integer.MAX_VALUE;
                    this.mLastVisibleIndex = this.mFirstVisibleIndex = index;
                }

                this.mProvider.addItem(this.mTmpItem[0], index, size, 0, edge);
                filledOne = true;
                if (oneColumnMode || this.checkPrependOverLimit(toLimit)) {
                    break;
                }
            }

            return filledOne;
        }
    }

    protected final boolean appendVisibleItems(int toLimit, boolean oneColumnMode) {
        if (this.mProvider.getCount() == 0) {
            return false;
        } else if (!oneColumnMode && this.checkAppendOverLimit(toLimit)) {
            return false;
        } else {
            boolean filledOne = false;

            for(int index = this.getStartIndexForAppend(); index < this.mProvider.getCount(); ++index) {
                int size = this.mProvider.createItem(index, true, this.mTmpItem, false);
                int edge;
                if (this.mFirstVisibleIndex >= 0 && this.mLastVisibleIndex >= 0) {
                    if (this.mReversedFlow) {
                        edge = this.mProvider.getEdge(index - 1) - this.mProvider.getSize(index - 1) - this.mSpacing;
                    } else {
                        edge = this.mProvider.getEdge(index - 1) + this.mProvider.getSize(index - 1) + this.mSpacing;
                    }

                    this.mLastVisibleIndex = index;
                } else {
                    edge = this.mReversedFlow ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                    this.mLastVisibleIndex = this.mFirstVisibleIndex = index;
                }

                this.mProvider.addItem(this.mTmpItem[0], index, size, 0, edge);
                filledOne = true;
                if (oneColumnMode || this.checkAppendOverLimit(toLimit)) {
                    break;
                }
            }

            return filledOne;
        }
    }

    public void collectAdjacentPrefetchPositions(int fromLimit, int da, @NonNull RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int indexToPrefetch;
        int nearestEdge;
        int distance;
        label37: {
            label38: {
                if (this.mReversedFlow) {
                    if (da <= 0) {
                        break label38;
                    }
                } else if (da >= 0) {
                    break label38;
                }

                if (this.getFirstVisibleIndex() == 0) {
                    return;
                }

                indexToPrefetch = this.getStartIndexForPrepend();
                nearestEdge = this.mProvider.getEdge(this.mFirstVisibleIndex) + (this.mReversedFlow ? this.mSpacing : -this.mSpacing);
                break label37;
            }

            if (this.getLastVisibleIndex() == this.mProvider.getCount() - 1) {
                return;
            }

            indexToPrefetch = this.getStartIndexForAppend();
            distance = this.mProvider.getSize(this.mLastVisibleIndex) + this.mSpacing;
            nearestEdge = this.mProvider.getEdge(this.mLastVisibleIndex) + (this.mReversedFlow ? -distance : distance);
        }

        distance = Math.abs(nearestEdge - fromLimit);
        layoutPrefetchRegistry.addPosition(indexToPrefetch, distance);
    }

    public final CircularIntArray[] getItemPositionsInRows(int startPos, int endPos) {
        this.mTmpItemPositionsInRows[0].clear();
        this.mTmpItemPositionsInRows[0].addLast(startPos);
        this.mTmpItemPositionsInRows[0].addLast(endPos);
        return this.mTmpItemPositionsInRows;
    }

    protected final int findRowMin(boolean findLarge, int indexLimit, int[] indices) {
        if (indices != null) {
            indices[0] = 0;
            indices[1] = indexLimit;
        }

        return this.mReversedFlow ? this.mProvider.getEdge(indexLimit) - this.mProvider.getSize(indexLimit) : this.mProvider.getEdge(indexLimit);
    }

    protected final int findRowMax(boolean findLarge, int indexLimit, int[] indices) {
        if (indices != null) {
            indices[0] = 0;
            indices[1] = indexLimit;
        }

        return this.mReversedFlow ? this.mProvider.getEdge(indexLimit) : this.mProvider.getEdge(indexLimit) + this.mProvider.getSize(indexLimit);
    }
}
