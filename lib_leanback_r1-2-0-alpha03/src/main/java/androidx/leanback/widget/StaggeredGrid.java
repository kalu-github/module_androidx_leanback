/*   1:    */ package androidx.leanback.widget;
/*   2:    */ 
/*   3:    */ import androidx.collection.CircularArray;
/*   4:    */ import androidx.collection.CircularIntArray;
/*   5:    */ import java.io.PrintWriter;
/*   6:    */ 
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
/*  26:    */ 
/*  27:    */ 
/*  28:    */ 
/*  29:    */ 
/*  30:    */ 
/*  31:    */ 
/*  32:    */ 
/*  33:    */ 
/*  34:    */ 
/*  35:    */ 
/*  36:    */ 
/*  37:    */ 
/*  38:    */ 
/*  39:    */ 
/*  40:    */ 
/*  41:    */ 
/*  42:    */ 
/*  43:    */ 
/*  44:    */ 
/*  45:    */ 
/*  46:    */ 
/*  47:    */ 
/*  48:    */ 
/*  49:    */ abstract class StaggeredGrid
/*  50:    */   extends Grid
/*  51:    */ {
/*  52:    */   static class Location
/*  53:    */     extends Grid.Location
/*  54:    */   {
/*  55:    */     int mOffset;
/*  56:    */     int mSize;
/*  57:    */     
/*  58:    */     Location(int row, int offset, int size)
/*  59:    */     {
/*  60: 60 */       super();
/*  61: 61 */       mOffset = offset;
/*  62: 62 */       mSize = size;
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66: 66 */   protected CircularArray<Location> mLocations = new CircularArray(64);
/*  67:    */   
/*  68:    */ 
/*  69:    */ 
/*  70: 70 */   protected int mFirstIndex = -1;
/*  71:    */   
/*  72:    */ 
/*  73:    */   protected Object mPendingItem;
/*  74:    */   
/*  75:    */   protected int mPendingItemSize;
/*  76:    */   
/*  77:    */ 
/*  78:    */   public final int getFirstIndex()
/*  79:    */   {
/*  80: 80 */     return mFirstIndex;
/*  81:    */   }
/*  82:    */   
/*  83:    */ 
/*  84:    */ 
/*  85:    */ 
/*  86:    */   public final int getLastIndex()
/*  87:    */   {
/*  88: 88 */     return mFirstIndex + mLocations.size() - 1;
/*  89:    */   }
/*  90:    */   
/*  91:    */ 
/*  92:    */ 
/*  93:    */   public final int getSize()
/*  94:    */   {
/*  95: 95 */     return mLocations.size();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public final Location getLocation(int index)
/*  99:    */   {
/* 100:100 */     int indexInArray = index - mFirstIndex;
/* 101:101 */     if ((indexInArray < 0) || (indexInArray >= mLocations.size())) {
/* 102:102 */       return null;
/* 103:    */     }
/* 104:104 */     return (Location)mLocations.get(indexInArray);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public final void debugPrint(PrintWriter pw)
/* 108:    */   {
/* 109:109 */     int i = 0; for (int size = mLocations.size(); i < size; i++) {
/* 110:110 */       Location loc = (Location)mLocations.get(i);
/* 111:111 */       pw.print("<" + (mFirstIndex + i) + "," + mRow + ">");
/* 112:112 */       pw.print(" ");
/* 113:113 */       pw.println();
/* 114:    */     }
/* 115:    */   }
/* 116:    */   
/* 117:    */   protected final boolean prependVisibleItems(int toLimit, boolean oneColumnMode)
/* 118:    */   {
/* 119:119 */     if (mProvider.getCount() == 0) {
/* 120:120 */       return false;
/* 121:    */     }
/* 122:122 */     if ((!oneColumnMode) && (checkPrependOverLimit(toLimit)))
/* 123:123 */       return false;
/* 124:    */     try {
/* 125:    */       boolean bool;
/* 126:126 */       if (prependVisbleItemsWithCache(toLimit, oneColumnMode)) {
/* 127:127 */         return true;
/* 128:    */       }
/* 129:129 */       return prependVisibleItemsWithoutCache(toLimit, oneColumnMode);
/* 130:    */     } finally {
/* 131:131 */       mTmpItem[0] = null;
/* 132:132 */       mPendingItem = null;
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */ 
/* 137:    */ 
/* 138:    */ 
/* 139:    */   protected final boolean prependVisbleItemsWithCache(int toLimit, boolean oneColumnMode)
/* 140:    */   {
/* 141:141 */     if (mLocations.size() == 0)
/* 142:142 */       return false;
/* 143:    */     int itemIndex;
/* 144:    */     int edge;
/* 145:    */     int offset;
/* 146:    */     int itemIndex;
/* 147:147 */     if (mFirstVisibleIndex >= 0)
/* 148:    */     {
/* 149:149 */       int edge = mProvider.getEdge(mFirstVisibleIndex);
/* 150:150 */       int offset = getLocationmFirstVisibleIndex).mOffset;
/* 151:151 */       itemIndex = mFirstVisibleIndex - 1;
/* 152:    */     }
/* 153:    */     else {
/* 154:154 */       edge = 2147483647;
/* 155:155 */       offset = 0;
/* 156:156 */       itemIndex = mStartIndex != -1 ? mStartIndex : 0;
/* 157:157 */       if ((itemIndex > getLastIndex()) || (itemIndex < getFirstIndex() - 1))
/* 158:    */       {
/* 159:159 */         mLocations.clear();
/* 160:160 */         return false; }
/* 161:161 */       if (itemIndex < getFirstIndex())
/* 162:    */       {
/* 163:163 */         return false;
/* 164:    */       }
/* 165:    */     }
/* 166:166 */     int firstIndex = Math.max(mProvider.getMinIndex(), mFirstIndex);
/* 167:167 */     for (; itemIndex >= firstIndex; itemIndex--) {
/* 168:168 */       Location loc = getLocation(itemIndex);
/* 169:169 */       int rowIndex = mRow;
/* 170:170 */       int size = mProvider.createItem(itemIndex, false, mTmpItem, false);
/* 171:171 */       if (size != mSize) {
/* 172:172 */         mLocations.removeFromStart(itemIndex + 1 - mFirstIndex);
/* 173:173 */         mFirstIndex = mFirstVisibleIndex;
/* 174:    */         
/* 175:175 */         mPendingItem = mTmpItem[0];
/* 176:176 */         mPendingItemSize = size;
/* 177:177 */         return false;
/* 178:    */       }
/* 179:179 */       mFirstVisibleIndex = itemIndex;
/* 180:180 */       if (mLastVisibleIndex < 0) {
/* 181:181 */         mLastVisibleIndex = itemIndex;
/* 182:    */       }
/* 183:183 */       mProvider.addItem(mTmpItem[0], itemIndex, size, rowIndex, edge - offset);
/* 184:184 */       if ((!oneColumnMode) && (checkPrependOverLimit(toLimit))) {
/* 185:185 */         return true;
/* 186:    */       }
/* 187:187 */       edge = mProvider.getEdge(itemIndex);
/* 188:188 */       offset = mOffset;
/* 189:    */       
/* 190:190 */       if ((rowIndex == 0) && 
/* 191:191 */         (oneColumnMode)) {
/* 192:192 */         return true;
/* 193:    */       }
/* 194:    */     }
/* 195:    */     
/* 196:196 */     return false;
/* 197:    */   }
/* 198:    */   
/* 199:    */ 
/* 200:    */ 
/* 201:    */ 
/* 202:    */   private int calculateOffsetAfterLastItem(int row)
/* 203:    */   {
/* 204:204 */     int cachedIndex = getLastIndex();
/* 205:205 */     boolean foundCachedItemInSameRow = false;
/* 206:206 */     while (cachedIndex >= mFirstIndex) {
/* 207:207 */       Location loc = getLocation(cachedIndex);
/* 208:208 */       if (mRow == row) {
/* 209:209 */         foundCachedItemInSameRow = true;
/* 210:210 */         break;
/* 211:    */       }
/* 212:212 */       cachedIndex--;
/* 213:    */     }
/* 214:214 */     if (!foundCachedItemInSameRow) {
/* 215:215 */       cachedIndex = getLastIndex();
/* 216:    */     }
/* 217:    */     
/* 218:    */ 
/* 219:    */ 
/* 220:    */ 
/* 221:221 */     int offset = isReversedFlow() ? -getLocationmSize - mSpacing : getLocationmSize + mSpacing;
/* 222:222 */     for (int i = cachedIndex + 1; i <= getLastIndex(); i++) {
/* 223:223 */       offset -= getLocationmOffset;
/* 224:    */     }
/* 225:225 */     return offset;
/* 226:    */   }
/* 227:    */   
/* 228:    */ 
/* 229:    */ 
/* 230:    */ 
/* 231:    */ 
/* 232:    */   protected abstract boolean prependVisibleItemsWithoutCache(int paramInt, boolean paramBoolean);
/* 233:    */   
/* 234:    */ 
/* 235:    */ 
/* 236:    */ 
/* 237:    */ 
/* 238:    */   protected final int prependVisibleItemToRow(int itemIndex, int rowIndex, int edge)
/* 239:    */   {
/* 240:240 */     if ((mFirstVisibleIndex >= 0) && (
/* 241:241 */       (mFirstVisibleIndex != getFirstIndex()) || (mFirstVisibleIndex != itemIndex + 1)))
/* 242:    */     {
/* 243:243 */       throw new IllegalStateException();
/* 244:    */     }
/* 245:    */     
/* 246:246 */     Location oldFirstLoc = mFirstIndex >= 0 ? getLocation(mFirstIndex) : null;
/* 247:247 */     int oldFirstEdge = mProvider.getEdge(mFirstIndex);
/* 248:248 */     Location loc = new Location(rowIndex, 0, 0);
/* 249:249 */     mLocations.addFirst(loc);
/* 250:    */     Object item;
/* 251:251 */     if (mPendingItem != null) {
/* 252:252 */       mSize = mPendingItemSize;
/* 253:253 */       Object item = mPendingItem;
/* 254:254 */       mPendingItem = null;
/* 255:    */     } else {
/* 256:256 */       mSize = mProvider.createItem(itemIndex, false, mTmpItem, false);
/* 257:257 */       item = mTmpItem[0];
/* 258:    */     }
/* 259:259 */     mFirstIndex = (this.mFirstVisibleIndex = itemIndex);
/* 260:260 */     if (mLastVisibleIndex < 0) {
/* 261:261 */       mLastVisibleIndex = itemIndex;
/* 262:    */     }
/* 263:263 */     int thisEdge = !mReversedFlow ? edge - mSize : edge + mSize;
/* 264:264 */     if (oldFirstLoc != null) {
/* 265:265 */       mOffset = (oldFirstEdge - thisEdge);
/* 266:    */     }
/* 267:267 */     mProvider.addItem(item, itemIndex, mSize, rowIndex, thisEdge);
/* 268:268 */     return mSize;
/* 269:    */   }
/* 270:    */   
/* 271:    */   protected final boolean appendVisibleItems(int toLimit, boolean oneColumnMode)
/* 272:    */   {
/* 273:273 */     if (mProvider.getCount() == 0) {
/* 274:274 */       return false;
/* 275:    */     }
/* 276:276 */     if ((!oneColumnMode) && (checkAppendOverLimit(toLimit)))
/* 277:277 */       return false;
/* 278:    */     try {
/* 279:    */       boolean bool;
/* 280:280 */       if (appendVisbleItemsWithCache(toLimit, oneColumnMode)) {
/* 281:281 */         return true;
/* 282:    */       }
/* 283:283 */       return appendVisibleItemsWithoutCache(toLimit, oneColumnMode);
/* 284:    */     } finally {
/* 285:285 */       mTmpItem[0] = null;
/* 286:286 */       mPendingItem = null;
/* 287:    */     }
/* 288:    */   }
/* 289:    */   
/* 290:    */ 
/* 291:    */ 
/* 292:    */ 
/* 293:    */ 
/* 294:    */   protected final boolean appendVisbleItemsWithCache(int toLimit, boolean oneColumnMode)
/* 295:    */   {
/* 296:296 */     if (mLocations.size() == 0) {
/* 297:297 */       return false;
/* 298:    */     }
/* 299:299 */     int count = mProvider.getCount();
/* 300:    */     int edge;
/* 301:    */     int edge;
/* 302:302 */     int itemIndex; if (mLastVisibleIndex >= 0)
/* 303:    */     {
/* 304:304 */       int itemIndex = mLastVisibleIndex + 1;
/* 305:305 */       edge = mProvider.getEdge(mLastVisibleIndex);
/* 306:    */     }
/* 307:    */     else {
/* 308:308 */       edge = 2147483647;
/* 309:309 */       itemIndex = mStartIndex != -1 ? mStartIndex : 0;
/* 310:310 */       if ((itemIndex > getLastIndex() + 1) || (itemIndex < getFirstIndex()))
/* 311:    */       {
/* 312:312 */         mLocations.clear();
/* 313:313 */         return false; }
/* 314:314 */       if (itemIndex > getLastIndex())
/* 315:    */       {
/* 316:316 */         return false;
/* 317:    */       }
/* 318:    */     }
/* 319:319 */     int lastIndex = getLastIndex();
/* 320:320 */     for (; (itemIndex < count) && (itemIndex <= lastIndex); itemIndex++) {
/* 321:321 */       Location loc = getLocation(itemIndex);
/* 322:322 */       if (edge != 2147483647) {
/* 323:323 */         edge += mOffset;
/* 324:    */       }
/* 325:325 */       int rowIndex = mRow;
/* 326:326 */       int size = mProvider.createItem(itemIndex, true, mTmpItem, false);
/* 327:327 */       if (size != mSize) {
/* 328:328 */         mSize = size;
/* 329:329 */         mLocations.removeFromEnd(lastIndex - itemIndex);
/* 330:330 */         lastIndex = itemIndex;
/* 331:    */       }
/* 332:332 */       mLastVisibleIndex = itemIndex;
/* 333:333 */       if (mFirstVisibleIndex < 0) {
/* 334:334 */         mFirstVisibleIndex = itemIndex;
/* 335:    */       }
/* 336:336 */       mProvider.addItem(mTmpItem[0], itemIndex, size, rowIndex, edge);
/* 337:337 */       if ((!oneColumnMode) && (checkAppendOverLimit(toLimit))) {
/* 338:338 */         return true;
/* 339:    */       }
/* 340:340 */       if (edge == 2147483647) {
/* 341:341 */         edge = mProvider.getEdge(itemIndex);
/* 342:    */       }
/* 343:    */       
/* 344:344 */       if ((rowIndex == mNumRows - 1) && 
/* 345:345 */         (oneColumnMode)) {
/* 346:346 */         return true;
/* 347:    */       }
/* 348:    */     }
/* 349:    */     
/* 350:350 */     return false;
/* 351:    */   }
/* 352:    */   
/* 353:    */ 
/* 354:    */ 
/* 355:    */ 
/* 356:    */ 
/* 357:    */   protected abstract boolean appendVisibleItemsWithoutCache(int paramInt, boolean paramBoolean);
/* 358:    */   
/* 359:    */ 
/* 360:    */ 
/* 361:    */ 
/* 362:    */ 
/* 363:    */   protected final int appendVisibleItemToRow(int itemIndex, int rowIndex, int location)
/* 364:    */   {
/* 365:365 */     if ((mLastVisibleIndex >= 0) && (
/* 366:366 */       (mLastVisibleIndex != getLastIndex()) || (mLastVisibleIndex != itemIndex - 1)))
/* 367:    */     {
/* 368:368 */       throw new IllegalStateException(); }
/* 369:    */     int offset;
/* 370:    */     int offset;
/* 371:371 */     if (mLastVisibleIndex < 0)
/* 372:    */     {
/* 373:    */       int offset;
/* 374:374 */       if ((mLocations.size() > 0) && (itemIndex == getLastIndex() + 1)) {
/* 375:375 */         offset = calculateOffsetAfterLastItem(rowIndex);
/* 376:    */       } else {
/* 377:377 */         offset = 0;
/* 378:    */       }
/* 379:    */     } else {
/* 380:380 */       offset = location - mProvider.getEdge(mLastVisibleIndex);
/* 381:    */     }
/* 382:382 */     Location loc = new Location(rowIndex, offset, 0);
/* 383:383 */     mLocations.addLast(loc);
/* 384:    */     Object item;
/* 385:385 */     if (mPendingItem != null) {
/* 386:386 */       mSize = mPendingItemSize;
/* 387:387 */       Object item = mPendingItem;
/* 388:388 */       mPendingItem = null;
/* 389:    */     } else {
/* 390:390 */       mSize = mProvider.createItem(itemIndex, true, mTmpItem, false);
/* 391:391 */       item = mTmpItem[0];
/* 392:    */     }
/* 393:393 */     if (mLocations.size() == 1) {
/* 394:394 */       mFirstIndex = (this.mFirstVisibleIndex = this.mLastVisibleIndex = itemIndex);
/* 395:    */     }
/* 396:396 */     else if (mLastVisibleIndex < 0) {
/* 397:397 */       mFirstVisibleIndex = (this.mLastVisibleIndex = itemIndex);
/* 398:    */     } else {
/* 399:399 */       mLastVisibleIndex += 1;
/* 400:    */     }
/* 401:    */     
/* 402:402 */     mProvider.addItem(item, itemIndex, mSize, rowIndex, location);
/* 403:403 */     return mSize;
/* 404:    */   }
/* 405:    */   
/* 406:    */   public final CircularIntArray[] getItemPositionsInRows(int startPos, int endPos)
/* 407:    */   {
/* 408:408 */     for (int i = 0; i < mNumRows; i++) {
/* 409:409 */       mTmpItemPositionsInRows[i].clear();
/* 410:    */     }
/* 411:411 */     if (startPos >= 0) {
/* 412:412 */       for (int i = startPos; i <= endPos; i++) {
/* 413:413 */         CircularIntArray row = mTmpItemPositionsInRows[getLocationmRow];
/* 414:414 */         if ((row.size() > 0) && (row.getLast() == i - 1))
/* 415:    */         {
/* 416:416 */           row.popLast();
/* 417:417 */           row.addLast(i);
/* 418:    */         }
/* 419:    */         else {
/* 420:420 */           row.addLast(i);
/* 421:421 */           row.addLast(i);
/* 422:    */         }
/* 423:    */       }
/* 424:    */     }
/* 425:425 */     return mTmpItemPositionsInRows;
/* 426:    */   }
/* 427:    */   
/* 428:    */   public void invalidateItemsAfter(int index)
/* 429:    */   {
/* 430:430 */     super.invalidateItemsAfter(index);
/* 431:431 */     mLocations.removeFromEnd(getLastIndex() - index + 1);
/* 432:432 */     if (mLocations.size() == 0) {
/* 433:433 */       mFirstIndex = -1;
/* 434:    */     }
/* 435:    */   }
/* 436:    */ }


/* Location:           D:\Android\Huan_Component\androidx_aar_leanback\repo\classes.jar
 * Qualified Name:     androidx.leanback.widget.StaggeredGrid
 * JD-Core Version:    0.7.0.1
 */