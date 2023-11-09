/*   1:    */ package androidx.leanback.widget;
/*   2:    */ 
/*   3:    */ import androidx.annotation.NonNull;
/*   4:    */ import androidx.collection.CircularIntArray;
/*   5:    */ import androidx.recyclerview.widget.RecyclerView.LayoutManager.LayoutPrefetchRegistry;
/*   6:    */ import java.io.PrintWriter;
/*   7:    */ 
/*   8:    */ 
/*   9:    */ 
/*  10:    */ 
/*  11:    */ 
/*  12:    */ 
/*  13:    */ 
/*  14:    */ 
/*  15:    */ 
/*  16:    */ 
/*  17:    */ 
/*  18:    */ 
/*  19:    */ 
/*  20:    */ 
/*  21:    */ 
/*  22:    */ 
/*  23:    */ 
/*  24:    */ 
/*  25:    */ 
/*  26:    */ class SingleRow
/*  27:    */   extends Grid
/*  28:    */ {
/*  29: 29 */   private final Grid.Location mTmpLocation = new Grid.Location(0);
/*  30:    */   
/*  31:    */   SingleRow() {
/*  32: 32 */     setNumRows(1);
/*  33:    */   }
/*  34:    */   
/*  35:    */ 
/*  36:    */   public final Grid.Location getLocation(int index)
/*  37:    */   {
/*  38: 38 */     return mTmpLocation;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public final void debugPrint(PrintWriter pw)
/*  42:    */   {
/*  43: 43 */     pw.print("SingleRow<");
/*  44: 44 */     pw.print(mFirstVisibleIndex);
/*  45: 45 */     pw.print(",");
/*  46: 46 */     pw.print(mLastVisibleIndex);
/*  47: 47 */     pw.print(">");
/*  48: 48 */     pw.println();
/*  49:    */   }
/*  50:    */   
/*  51:    */   int getStartIndexForAppend() {
/*  52: 52 */     if (mLastVisibleIndex >= 0)
/*  53: 53 */       return mLastVisibleIndex + 1;
/*  54: 54 */     if (mStartIndex != -1) {
/*  55: 55 */       return Math.min(mStartIndex, mProvider.getCount() - 1);
/*  56:    */     }
/*  57: 57 */     return 0;
/*  58:    */   }
/*  59:    */   
/*  60:    */   int getStartIndexForPrepend()
/*  61:    */   {
/*  62: 62 */     if (mFirstVisibleIndex >= 0)
/*  63: 63 */       return mFirstVisibleIndex - 1;
/*  64: 64 */     if (mStartIndex != -1) {
/*  65: 65 */       return Math.min(mStartIndex, mProvider.getCount() - 1);
/*  66:    */     }
/*  67: 67 */     return mProvider.getCount() - 1;
/*  68:    */   }
/*  69:    */   
/*  70:    */ 
/*  71:    */   protected final boolean prependVisibleItems(int toLimit, boolean oneColumnMode)
/*  72:    */   {
/*  73: 73 */     if (mProvider.getCount() == 0) {
/*  74: 74 */       return false;
/*  75:    */     }
/*  76: 76 */     if ((!oneColumnMode) && (checkPrependOverLimit(toLimit))) {
/*  77: 77 */       return false;
/*  78:    */     }
/*  79: 79 */     boolean filledOne = false;
/*  80: 80 */     int minIndex = mProvider.getMinIndex();
/*  81: 81 */     for (int index = getStartIndexForPrepend(); index >= minIndex; index--) {
/*  82: 82 */       int size = mProvider.createItem(index, false, mTmpItem, false);
/*  83:    */       int edge;
/*  84: 84 */       if ((mFirstVisibleIndex < 0) || (mLastVisibleIndex < 0)) {
/*  85: 85 */         int edge = mReversedFlow ? -2147483648 : 2147483647;
/*  86: 86 */         mLastVisibleIndex = (this.mFirstVisibleIndex = index);
/*  87:    */       } else { int edge;
/*  88: 88 */         if (mReversedFlow) {
/*  89: 89 */           edge = mProvider.getEdge(index + 1) + mSpacing + size;
/*  90:    */         } else {
/*  91: 91 */           edge = mProvider.getEdge(index + 1) - mSpacing - size;
/*  92:    */         }
/*  93: 93 */         mFirstVisibleIndex = index;
/*  94:    */       }
/*  95: 95 */       mProvider.addItem(mTmpItem[0], index, size, 0, edge);
/*  96: 96 */       filledOne = true;
/*  97: 97 */       if ((oneColumnMode) || (checkPrependOverLimit(toLimit))) {
/*  98:    */         break;
/*  99:    */       }
/* 100:    */     }
/* 101:101 */     return filledOne;
/* 102:    */   }
/* 103:    */   
/* 104:    */   protected final boolean appendVisibleItems(int toLimit, boolean oneColumnMode)
/* 105:    */   {
/* 106:106 */     if (mProvider.getCount() == 0) {
/* 107:107 */       return false;
/* 108:    */     }
/* 109:109 */     if ((!oneColumnMode) && (checkAppendOverLimit(toLimit)))
/* 110:    */     {
/* 111:111 */       return false;
/* 112:    */     }
/* 113:113 */     boolean filledOne = false;
/* 114:114 */     for (int index = getStartIndexForAppend(); index < mProvider.getCount(); index++) {
/* 115:115 */       int size = mProvider.createItem(index, true, mTmpItem, false);
/* 116:    */       int edge;
/* 117:117 */       if ((mFirstVisibleIndex < 0) || (mLastVisibleIndex < 0)) {
/* 118:118 */         int edge = mReversedFlow ? 2147483647 : -2147483648;
/* 119:119 */         mLastVisibleIndex = (this.mFirstVisibleIndex = index);
/* 120:    */       } else { int edge;
/* 121:121 */         if (mReversedFlow) {
/* 122:122 */           edge = mProvider.getEdge(index - 1) - mProvider.getSize(index - 1) - mSpacing;
/* 123:    */         } else {
/* 124:124 */           edge = mProvider.getEdge(index - 1) + mProvider.getSize(index - 1) + mSpacing;
/* 125:    */         }
/* 126:126 */         mLastVisibleIndex = index;
/* 127:    */       }
/* 128:128 */       mProvider.addItem(mTmpItem[0], index, size, 0, edge);
/* 129:129 */       filledOne = true;
/* 130:130 */       if ((oneColumnMode) || (checkAppendOverLimit(toLimit))) {
/* 131:    */         break;
/* 132:    */       }
/* 133:    */     }
/* 134:134 */     return filledOne;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public void collectAdjacentPrefetchPositions(int fromLimit, int da, @NonNull RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry)
/* 138:    */   {
/* 139:    */     int nearestEdge;
/* 140:    */     int indexToPrefetch;
/* 141:    */     int nearestEdge;
/* 142:142 */     if (mReversedFlow ? da > 0 : da < 0)
/* 143:    */     {
/* 144:144 */       if (getFirstVisibleIndex() == 0) {
/* 145:145 */         return;
/* 146:    */       }
/* 147:    */       
/* 148:148 */       int indexToPrefetch = getStartIndexForPrepend();
/* 149:    */       
/* 150:150 */       nearestEdge = mProvider.getEdge(mFirstVisibleIndex) + (mReversedFlow ? mSpacing : -mSpacing);
/* 151:    */     }
/* 152:    */     else {
/* 153:153 */       if (getLastVisibleIndex() == mProvider.getCount() - 1) {
/* 154:154 */         return;
/* 155:    */       }
/* 156:    */       
/* 157:157 */       indexToPrefetch = getStartIndexForAppend();
/* 158:158 */       int itemSizeWithSpace = mProvider.getSize(mLastVisibleIndex) + mSpacing;
/* 159:    */       
/* 160:160 */       nearestEdge = mProvider.getEdge(mLastVisibleIndex) + (mReversedFlow ? -itemSizeWithSpace : itemSizeWithSpace);
/* 161:    */     }
/* 162:    */     
/* 163:163 */     int distance = Math.abs(nearestEdge - fromLimit);
/* 164:164 */     layoutPrefetchRegistry.addPosition(indexToPrefetch, distance);
/* 165:    */   }
/* 166:    */   
/* 167:    */ 
/* 168:    */   public final CircularIntArray[] getItemPositionsInRows(int startPos, int endPos)
/* 169:    */   {
/* 170:170 */     mTmpItemPositionsInRows[0].clear();
/* 171:171 */     mTmpItemPositionsInRows[0].addLast(startPos);
/* 172:172 */     mTmpItemPositionsInRows[0].addLast(endPos);
/* 173:173 */     return mTmpItemPositionsInRows;
/* 174:    */   }
/* 175:    */   
/* 176:    */   protected final int findRowMin(boolean findLarge, int indexLimit, int[] indices)
/* 177:    */   {
/* 178:178 */     if (indices != null) {
/* 179:179 */       indices[0] = 0;
/* 180:180 */       indices[1] = indexLimit;
/* 181:    */     }
/* 182:182 */     return mReversedFlow ? mProvider.getEdge(indexLimit) - mProvider.getSize(indexLimit) : 
/* 183:183 */       mProvider.getEdge(indexLimit);
/* 184:    */   }
/* 185:    */   
/* 186:    */   protected final int findRowMax(boolean findLarge, int indexLimit, int[] indices)
/* 187:    */   {
/* 188:188 */     if (indices != null) {
/* 189:189 */       indices[0] = 0;
/* 190:190 */       indices[1] = indexLimit;
/* 191:    */     }
/* 192:192 */     return mReversedFlow ? mProvider.getEdge(indexLimit) : 
/* 193:193 */       mProvider.getEdge(indexLimit) + mProvider.getSize(indexLimit);
/* 194:    */   }
/* 195:    */ }


/* Location:           D:\Android\Huan_Component\androidx_aar_leanback\repo\classes.jar
 * Qualified Name:     androidx.leanback.widget.SingleRow
 * JD-Core Version:    0.7.0.1
 */