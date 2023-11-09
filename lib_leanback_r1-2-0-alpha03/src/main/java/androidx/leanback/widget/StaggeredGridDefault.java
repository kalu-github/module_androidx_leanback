//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package androidx.leanback.widget;

final class StaggeredGridDefault extends StaggeredGrid {
    StaggeredGridDefault() {
    }

    int getRowMax(int rowIndex) {
        if (this.mFirstVisibleIndex < 0) {
            return Integer.MIN_VALUE;
        } else {
            int edge;
            if (this.mReversedFlow) {
                edge = this.mProvider.getEdge(this.mFirstVisibleIndex);
                if (this.getLocation(this.mFirstVisibleIndex).mRow == rowIndex) {
                    return edge;
                }

                for(int i = this.mFirstVisibleIndex + 1; i <= this.getLastIndex(); ++i) {
                    StaggeredGrid.Location loc = this.getLocation(i);
                    edge += loc.mOffset;
                    if (loc.mRow == rowIndex) {
                        return edge;
                    }
                }
            } else {
                edge = this.mProvider.getEdge(this.mLastVisibleIndex);
                StaggeredGrid.Location loc = this.getLocation(this.mLastVisibleIndex);
                if (loc.mRow == rowIndex) {
                    return edge + loc.mSize;
                }

                for(int i = this.mLastVisibleIndex - 1; i >= this.getFirstIndex(); --i) {
                    edge -= loc.mOffset;
                    loc = this.getLocation(i);
                    if (loc.mRow == rowIndex) {
                        return edge + loc.mSize;
                    }
                }
            }

            return Integer.MIN_VALUE;
        }
    }

    int getRowMin(int rowIndex) {
        if (this.mFirstVisibleIndex < 0) {
            return Integer.MAX_VALUE;
        } else {
            int edge;
            if (this.mReversedFlow) {
                edge = this.mProvider.getEdge(this.mLastVisibleIndex);
                StaggeredGrid.Location loc = this.getLocation(this.mLastVisibleIndex);
                if (loc.mRow == rowIndex) {
                    return edge - loc.mSize;
                }

                for(int i = this.mLastVisibleIndex - 1; i >= this.getFirstIndex(); --i) {
                    edge -= loc.mOffset;
                    loc = this.getLocation(i);
                    if (loc.mRow == rowIndex) {
                        return edge - loc.mSize;
                    }
                }
            } else {
                edge = this.mProvider.getEdge(this.mFirstVisibleIndex);
                if (this.getLocation(this.mFirstVisibleIndex).mRow == rowIndex) {
                    return edge;
                }

                for(int i = this.mFirstVisibleIndex + 1; i <= this.getLastIndex(); ++i) {
                    StaggeredGrid.Location loc = this.getLocation(i);
                    edge += loc.mOffset;
                    if (loc.mRow == rowIndex) {
                        return edge;
                    }
                }
            }

            return Integer.MAX_VALUE;
        }
    }

    public int findRowMax(boolean findLarge, int indexLimit, int[] indices) {
        int edge = this.mProvider.getEdge(indexLimit);
        StaggeredGrid.Location loc = this.getLocation(indexLimit);
        int row = loc.mRow;
        int index = indexLimit;
        int visitedRows = 1;
        int visitRow = row;
        int value;
        int i;
        if (this.mReversedFlow) {
            value = edge;

            for(i = indexLimit + 1; visitedRows < this.mNumRows && i <= this.mLastVisibleIndex; ++i) {
                loc = this.getLocation(i);
                edge += loc.mOffset;
                if (loc.mRow != visitRow) {
                    visitRow = loc.mRow;
                    ++visitedRows;
                    if (findLarge) {
                        if (edge <= value) {
                            continue;
                        }
                    } else if (edge >= value) {
                        continue;
                    }

                    row = visitRow;
                    value = edge;
                    index = i;
                }
            }
        } else {
            value = edge + this.mProvider.getSize(indexLimit);

            for(i = indexLimit - 1; visitedRows < this.mNumRows && i >= this.mFirstVisibleIndex; --i) {
                edge -= loc.mOffset;
                loc = this.getLocation(i);
                if (loc.mRow != visitRow) {
                    visitRow = loc.mRow;
                    ++visitedRows;
                    int newValue = edge + this.mProvider.getSize(i);
                    if (findLarge) {
                        if (newValue <= value) {
                            continue;
                        }
                    } else if (newValue >= value) {
                        continue;
                    }

                    row = visitRow;
                    value = newValue;
                    index = i;
                }
            }
        }

        if (indices != null) {
            indices[0] = row;
            indices[1] = index;
        }

        return value;
    }

    public int findRowMin(boolean findLarge, int indexLimit, int[] indices) {
        int edge = this.mProvider.getEdge(indexLimit);
        StaggeredGrid.Location loc = this.getLocation(indexLimit);
        int row = loc.mRow;
        int index = indexLimit;
        int visitedRows = 1;
        int visitRow = row;
        int value;
        int i;
        if (this.mReversedFlow) {
            value = edge - this.mProvider.getSize(indexLimit);

            for(i = indexLimit - 1; visitedRows < this.mNumRows && i >= this.mFirstVisibleIndex; --i) {
                edge -= loc.mOffset;
                loc = this.getLocation(i);
                if (loc.mRow != visitRow) {
                    visitRow = loc.mRow;
                    ++visitedRows;
                    int newValue = edge - this.mProvider.getSize(i);
                    if (findLarge) {
                        if (newValue <= value) {
                            continue;
                        }
                    } else if (newValue >= value) {
                        continue;
                    }

                    value = newValue;
                    row = visitRow;
                    index = i;
                }
            }
        } else {
            value = edge;

            for(i = indexLimit + 1; visitedRows < this.mNumRows && i <= this.mLastVisibleIndex; ++i) {
                loc = this.getLocation(i);
                edge += loc.mOffset;
                if (loc.mRow != visitRow) {
                    visitRow = loc.mRow;
                    ++visitedRows;
                    if (findLarge) {
                        if (edge <= value) {
                            continue;
                        }
                    } else if (edge >= value) {
                        continue;
                    }

                    value = edge;
                    row = visitRow;
                    index = i;
                }
            }
        }

        if (indices != null) {
            indices[0] = row;
            indices[1] = index;
        }

        return value;
    }

    private int findRowEdgeLimitSearchIndex(boolean append) {
        boolean wrapped = false;
        int index;
        int row;
        if (append) {
            for(index = this.mLastVisibleIndex; index >= this.mFirstVisibleIndex; --index) {
                row = this.getLocation(index).mRow;
                if (row == 0) {
                    wrapped = true;
                } else if (wrapped && row == this.mNumRows - 1) {
                    return index;
                }
            }
        } else {
            for(index = this.mFirstVisibleIndex; index <= this.mLastVisibleIndex; ++index) {
                row = this.getLocation(index).mRow;
                if (row == this.mNumRows - 1) {
                    wrapped = true;
                } else if (wrapped && row == 0) {
                    return index;
                }
            }
        }

        return -1;
    }

    protected boolean appendVisibleItemsWithoutCache(int toLimit, boolean oneColumnMode) {
        int count = this.mProvider.getCount();
        int itemIndex;
        int rowIndex;
        int edgeLimit;
        boolean edgeLimitIsValid;
        int location;
        if (this.mLastVisibleIndex >= 0) {
            if (this.mLastVisibleIndex < this.getLastIndex()) {
                return false;
            }

            itemIndex = this.mLastVisibleIndex + 1;
            rowIndex = this.getLocation(this.mLastVisibleIndex).mRow;
            int edgeLimitSearchIndex = this.findRowEdgeLimitSearchIndex(true);
            if (edgeLimitSearchIndex < 0) {
                edgeLimit = Integer.MIN_VALUE;

                for(location = 0; location < this.mNumRows; ++location) {
                    edgeLimit = this.mReversedFlow ? this.getRowMin(location) : this.getRowMax(location);
                    if (edgeLimit != Integer.MIN_VALUE) {
                        break;
                    }
                }
            } else {
                edgeLimit = this.mReversedFlow ? this.findRowMin(false, edgeLimitSearchIndex, (int[])null) : this.findRowMax(true, edgeLimitSearchIndex, (int[])null);
            }

            label165: {
                if (this.mReversedFlow) {
                    if (this.getRowMin(rowIndex) > edgeLimit) {
                        break label165;
                    }
                } else if (this.getRowMax(rowIndex) < edgeLimit) {
                    break label165;
                }

                ++rowIndex;
                if (rowIndex == this.mNumRows) {
                    rowIndex = 0;
                    edgeLimit = this.mReversedFlow ? this.findRowMin(false, (int[])null) : this.findRowMax(true, (int[])null);
                }
            }

            edgeLimitIsValid = true;
        } else {
            itemIndex = this.mStartIndex != -1 ? this.mStartIndex : 0;
            rowIndex = (this.mLocations.size() > 0 ? this.getLocation(this.getLastIndex()).mRow + 1 : itemIndex) % this.mNumRows;
            edgeLimit = 0;
            edgeLimitIsValid = false;
        }

        boolean filledOne = false;

        while(true) {
            while(rowIndex >= this.mNumRows) {
                if (oneColumnMode) {
                    return filledOne;
                }

                edgeLimit = this.mReversedFlow ? this.findRowMin(false, (int[])null) : this.findRowMax(true, (int[])null);
                rowIndex = 0;
            }

            if (itemIndex == count || !oneColumnMode && this.checkAppendOverLimit(toLimit)) {
                return filledOne;
            }

            location = this.mReversedFlow ? this.getRowMin(rowIndex) : this.getRowMax(rowIndex);
            if (location != Integer.MAX_VALUE && location != Integer.MIN_VALUE) {
                location += this.mReversedFlow ? -this.mSpacing : this.mSpacing;
            } else if (rowIndex == 0) {
                location = this.mReversedFlow ? this.getRowMin(this.mNumRows - 1) : this.getRowMax(this.mNumRows - 1);
                if (location != Integer.MAX_VALUE && location != Integer.MIN_VALUE) {
                    location += this.mReversedFlow ? -this.mSpacing : this.mSpacing;
                }
            } else {
                location = this.mReversedFlow ? this.getRowMax(rowIndex - 1) : this.getRowMin(rowIndex - 1);
            }

            int size = this.appendVisibleItemToRow(itemIndex++, rowIndex, location);
            filledOne = true;
            if (!edgeLimitIsValid) {
                edgeLimitIsValid = true;
                edgeLimit = this.mReversedFlow ? this.getRowMin(rowIndex) : this.getRowMax(rowIndex);
            } else {
                while(true) {
                    if (this.mReversedFlow) {
                        if (location - size <= edgeLimit) {
                            break;
                        }
                    } else if (location + size >= edgeLimit) {
                        break;
                    }

                    if (itemIndex == count || !oneColumnMode && this.checkAppendOverLimit(toLimit)) {
                        return filledOne;
                    }

                    location += this.mReversedFlow ? -size - this.mSpacing : size + this.mSpacing;
                    size = this.appendVisibleItemToRow(itemIndex++, rowIndex, location);
                }
            }

            ++rowIndex;
        }
    }

    protected boolean prependVisibleItemsWithoutCache(int toLimit, boolean oneColumnMode) {
        int itemIndex;
        int rowIndex;
        int edgeLimit;
        boolean edgeLimitIsValid;
        int location;
        if (this.mFirstVisibleIndex >= 0) {
            if (this.mFirstVisibleIndex > this.getFirstIndex()) {
                return false;
            }

            itemIndex = this.mFirstVisibleIndex - 1;
            rowIndex = this.getLocation(this.mFirstVisibleIndex).mRow;
            int edgeLimitSearchIndex = this.findRowEdgeLimitSearchIndex(false);
            if (edgeLimitSearchIndex < 0) {
                --rowIndex;
                edgeLimit = Integer.MAX_VALUE;

                for(location = this.mNumRows - 1; location >= 0; --location) {
                    edgeLimit = this.mReversedFlow ? this.getRowMax(location) : this.getRowMin(location);
                    if (edgeLimit != Integer.MAX_VALUE) {
                        break;
                    }
                }
            } else {
                edgeLimit = this.mReversedFlow ? this.findRowMax(true, edgeLimitSearchIndex, (int[])null) : this.findRowMin(false, edgeLimitSearchIndex, (int[])null);
            }

            label165: {
                if (this.mReversedFlow) {
                    if (this.getRowMax(rowIndex) < edgeLimit) {
                        break label165;
                    }
                } else if (this.getRowMin(rowIndex) > edgeLimit) {
                    break label165;
                }

                --rowIndex;
                if (rowIndex < 0) {
                    rowIndex = this.mNumRows - 1;
                    edgeLimit = this.mReversedFlow ? this.findRowMax(true, (int[])null) : this.findRowMin(false, (int[])null);
                }
            }

            edgeLimitIsValid = true;
        } else {
            itemIndex = this.mStartIndex != -1 ? this.mStartIndex : 0;
            rowIndex = (this.mLocations.size() > 0 ? this.getLocation(this.getFirstIndex()).mRow + this.mNumRows - 1 : itemIndex) % this.mNumRows;
            edgeLimit = 0;
            edgeLimitIsValid = false;
        }

        boolean filledOne = false;

        while(true) {
            while(rowIndex < 0) {
                if (oneColumnMode) {
                    return filledOne;
                }

                edgeLimit = this.mReversedFlow ? this.findRowMax(true, (int[])null) : this.findRowMin(false, (int[])null);
                rowIndex = this.mNumRows - 1;
            }

            if (itemIndex < 0 || !oneColumnMode && this.checkPrependOverLimit(toLimit)) {
                return filledOne;
            }

            location = this.mReversedFlow ? this.getRowMax(rowIndex) : this.getRowMin(rowIndex);
            if (location != Integer.MAX_VALUE && location != Integer.MIN_VALUE) {
                location += this.mReversedFlow ? this.mSpacing : -this.mSpacing;
            } else if (rowIndex == this.mNumRows - 1) {
                location = this.mReversedFlow ? this.getRowMax(0) : this.getRowMin(0);
                if (location != Integer.MAX_VALUE && location != Integer.MIN_VALUE) {
                    location += this.mReversedFlow ? this.mSpacing : -this.mSpacing;
                }
            } else {
                location = this.mReversedFlow ? this.getRowMin(rowIndex + 1) : this.getRowMax(rowIndex + 1);
            }

            int size = this.prependVisibleItemToRow(itemIndex--, rowIndex, location);
            filledOne = true;
            if (!edgeLimitIsValid) {
                edgeLimitIsValid = true;
                edgeLimit = this.mReversedFlow ? this.getRowMax(rowIndex) : this.getRowMin(rowIndex);
            } else {
                while(true) {
                    if (this.mReversedFlow) {
                        if (location + size >= edgeLimit) {
                            break;
                        }
                    } else if (location - size <= edgeLimit) {
                        break;
                    }

                    if (itemIndex < 0 || !oneColumnMode && this.checkPrependOverLimit(toLimit)) {
                        return filledOne;
                    }

                    location += this.mReversedFlow ? size + this.mSpacing : -size - this.mSpacing;
                    size = this.prependVisibleItemToRow(itemIndex--, rowIndex, location);
                }
            }

            --rowIndex;
        }
    }
}
