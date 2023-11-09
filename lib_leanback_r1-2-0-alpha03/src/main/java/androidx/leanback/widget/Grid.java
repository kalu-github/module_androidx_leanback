/*   1:    */ package androidx.leanback.widget;
/*   2:    */ 
/*   3:    */ import android.util.SparseIntArray;
/*   4:    */ import androidx.annotation.NonNull;
/*   5:    */ import androidx.annotation.Nullable;
/*   6:    */ import androidx.collection.CircularIntArray;
/*   7:    */ import androidx.recyclerview.widget.RecyclerView.LayoutManager.LayoutPrefetchRegistry;
/*   8:    */ import java.io.PrintWriter;
/*   9:    */ import java.util.Arrays;
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
/*  47:    */ abstract class Grid
/*  48:    */ {
/*  49:    */   static final int START_DEFAULT = -1;
/*  50: 50 */   Object[] mTmpItem = new Object[1];
/*  51:    */   
/*  52:    */ 
/*  53:    */ 
/*  54:    */ 
/*  55:    */ 
/*  56:    */   protected Provider mProvider;
/*  57:    */   
/*  58:    */ 
/*  59:    */ 
/*  60:    */ 
/*  61:    */   protected boolean mReversedFlow;
/*  62:    */   
/*  63:    */ 
/*  64:    */ 
/*  65:    */ 
/*  66:    */   protected int mSpacing;
/*  67:    */   
/*  68:    */ 
/*  69:    */ 
/*  70:    */ 
/*  71:    */   protected int mNumRows;
/*  72:    */   
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76:    */ 
/*  77:    */   static abstract interface Provider
/*  78:    */   {
/*  79:    */     public abstract int getCount();
/*  80:    */     
/*  81:    */ 
/*  82:    */ 
/*  83:    */ 
/*  84:    */ 
/*  85:    */     public abstract int getMinIndex();
/*  86:    */     
/*  87:    */ 
/*  88:    */ 
/*  89:    */ 
/*  90:    */ 
/*  91:    */     public abstract int createItem(int paramInt, boolean paramBoolean1, Object[] paramArrayOfObject, boolean paramBoolean2);
/*  92:    */     
/*  93:    */ 
/*  94:    */ 
/*  95:    */ 
/*  96:    */ 
/*  97:    */     public abstract void addItem(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*  98:    */     
/*  99:    */ 
/* 100:    */ 
/* 101:    */ 
/* 102:    */ 
/* 103:    */     public abstract void removeItem(int paramInt);
/* 104:    */     
/* 105:    */ 
/* 106:    */ 
/* 107:    */ 
/* 108:    */ 
/* 109:    */     public abstract int getEdge(int paramInt);
/* 110:    */     
/* 111:    */ 
/* 112:    */ 
/* 113:    */ 
/* 114:    */ 
/* 115:    */     public abstract int getSize(int paramInt);
/* 116:    */   }
/* 117:    */   
/* 118:    */ 
/* 119:    */ 
/* 120:    */ 
/* 121:    */ 
/* 122:    */   static class Location
/* 123:    */   {
/* 124:    */     int mRow;
/* 125:    */     
/* 126:    */ 
/* 127:    */ 
/* 128:    */ 
/* 129:    */ 
/* 130:    */     Location(int row)
/* 131:    */     {
/* 132:132 */       mRow = row;
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */ 
/* 137:    */ 
/* 138:    */ 
/* 139:    */ 
/* 140:140 */   protected int mFirstVisibleIndex = -1;
/* 141:141 */   protected int mLastVisibleIndex = -1;
/* 142:    */   
/* 143:    */ 
/* 144:    */   protected CircularIntArray[] mTmpItemPositionsInRows;
/* 145:    */   
/* 146:146 */   protected int mStartIndex = -1;
/* 147:    */   
/* 148:    */ 
/* 149:    */   public static Grid createGrid(int rows)
/* 150:    */   {
/* 151:    */     Grid grid;
/* 152:    */     Grid grid;
/* 153:153 */     if (rows == 1) {
/* 154:154 */       grid = new SingleRow();
/* 155:    */     }
/* 156:    */     else {
/* 157:157 */       grid = new StaggeredGridDefault();
/* 158:158 */       grid.setNumRows(rows);
/* 159:    */     }
/* 160:160 */     return grid;
/* 161:    */   }
/* 162:    */   
/* 163:    */ 
/* 164:    */ 
/* 165:    */   public final void setSpacing(int spacing)
/* 166:    */   {
/* 167:167 */     mSpacing = spacing;
/* 168:    */   }
/* 169:    */   
/* 170:    */ 
/* 171:    */ 
/* 172:    */   public final void setReversedFlow(boolean reversedFlow)
/* 173:    */   {
/* 174:174 */     mReversedFlow = reversedFlow;
/* 175:    */   }
/* 176:    */   
/* 177:    */ 
/* 178:    */ 
/* 179:    */   public boolean isReversedFlow()
/* 180:    */   {
/* 181:181 */     return mReversedFlow;
/* 182:    */   }
/* 183:    */   
/* 184:    */ 
/* 185:    */ 
/* 186:    */ 
/* 187:    */ 
/* 188:    */   public void setProvider(Provider provider)
/* 189:    */   {
/* 190:190 */     mProvider = provider;
/* 191:    */   }
/* 192:    */   
/* 193:    */ 
/* 194:    */ 
/* 195:    */ 
/* 196:    */ 
/* 197:    */   public void setStart(int startIndex)
/* 198:    */   {
/* 199:199 */     mStartIndex = startIndex;
/* 200:    */   }
/* 201:    */   
/* 202:    */ 
/* 203:    */ 
/* 204:    */   public int getNumRows()
/* 205:    */   {
/* 206:206 */     return mNumRows;
/* 207:    */   }
/* 208:    */   
/* 209:    */ 
/* 210:    */ 
/* 211:    */ 
/* 212:    */ 
/* 213:    */ 
/* 214:    */ 
/* 215:    */   void setNumRows(int numRows)
/* 216:    */   {
/* 217:217 */     if (numRows <= 0) {
/* 218:218 */       throw new IllegalArgumentException();
/* 219:    */     }
/* 220:220 */     if (mNumRows == numRows) {
/* 221:221 */       return;
/* 222:    */     }
/* 223:223 */     mNumRows = numRows;
/* 224:224 */     mTmpItemPositionsInRows = new CircularIntArray[mNumRows];
/* 225:225 */     for (int i = 0; i < mNumRows; i++) {
/* 226:226 */       mTmpItemPositionsInRows[i] = new CircularIntArray();
/* 227:    */     }
/* 228:    */   }
/* 229:    */   
/* 230:    */ 
/* 231:    */ 
/* 232:    */ 
/* 233:    */   public final int getFirstVisibleIndex()
/* 234:    */   {
/* 235:235 */     return mFirstVisibleIndex;
/* 236:    */   }
/* 237:    */   
/* 238:    */ 
/* 239:    */ 
/* 240:    */ 
/* 241:    */   public final int getLastVisibleIndex()
/* 242:    */   {
/* 243:243 */     return mLastVisibleIndex;
/* 244:    */   }
/* 245:    */   
/* 246:    */ 
/* 247:    */ 
/* 248:    */   public void resetVisibleIndex()
/* 249:    */   {
/* 250:250 */     mFirstVisibleIndex = (this.mLastVisibleIndex = -1);
/* 251:    */   }
/* 252:    */   
/* 253:    */ 
/* 254:    */ 
/* 255:    */ 
/* 256:    */ 
/* 257:    */ 
/* 258:    */   public void invalidateItemsAfter(int index)
/* 259:    */   {
/* 260:260 */     if (index < 0) {
/* 261:261 */       return;
/* 262:    */     }
/* 263:263 */     if (mLastVisibleIndex < 0) {
/* 264:264 */       return;
/* 265:    */     }
/* 266:266 */     if (mLastVisibleIndex >= index) {
/* 267:267 */       mLastVisibleIndex = (index - 1);
/* 268:    */     }
/* 269:269 */     resetVisibleIndexIfEmpty();
/* 270:270 */     if (getFirstVisibleIndex() < 0) {
/* 271:271 */       setStart(index);
/* 272:    */     }
/* 273:    */   }
/* 274:    */   
/* 275:    */ 
/* 276:    */ 
/* 277:    */   public final int getRowIndex(int index)
/* 278:    */   {
/* 279:279 */     Location location = getLocation(index);
/* 280:280 */     if (location == null) {
/* 281:281 */       return -1;
/* 282:    */     }
/* 283:283 */     return mRow;
/* 284:    */   }
/* 285:    */   
/* 286:    */ 
/* 287:    */ 
/* 288:    */ 
/* 289:    */   public abstract Location getLocation(int paramInt);
/* 290:    */   
/* 291:    */ 
/* 292:    */ 
/* 293:    */ 
/* 294:    */   public final int findRowMin(boolean findLarge, @Nullable int[] indices)
/* 295:    */   {
/* 296:296 */     return findRowMin(findLarge, mReversedFlow ? mLastVisibleIndex : mFirstVisibleIndex, indices);
/* 297:    */   }
/* 298:    */   
/* 299:    */ 
/* 300:    */ 
/* 301:    */ 
/* 302:    */ 
/* 303:    */ 
/* 304:    */   protected abstract int findRowMin(boolean paramBoolean, int paramInt, int[] paramArrayOfInt);
/* 305:    */   
/* 306:    */ 
/* 307:    */ 
/* 308:    */ 
/* 309:    */ 
/* 310:    */   public final int findRowMax(boolean findLarge, @Nullable int[] indices)
/* 311:    */   {
/* 312:312 */     return findRowMax(findLarge, mReversedFlow ? mFirstVisibleIndex : mLastVisibleIndex, indices);
/* 313:    */   }
/* 314:    */   
/* 315:    */ 
/* 316:    */ 
/* 317:    */ 
/* 318:    */ 
/* 319:    */   protected abstract int findRowMax(boolean paramBoolean, int paramInt, int[] paramArrayOfInt);
/* 320:    */   
/* 321:    */ 
/* 322:    */ 
/* 323:    */ 
/* 324:    */   protected final boolean checkAppendOverLimit(int toLimit)
/* 325:    */   {
/* 326:326 */     if (mLastVisibleIndex < 0) {
/* 327:327 */       return false;
/* 328:    */     }
/* 329:329 */     return findRowMin(true, null) <= toLimit + mSpacing;
/* 330:    */   }
/* 331:    */   
/* 332:    */ 
/* 333:    */ 
/* 334:    */ 
/* 335:    */   protected final boolean checkPrependOverLimit(int toLimit)
/* 336:    */   {
/* 337:337 */     if (mLastVisibleIndex < 0) {
/* 338:338 */       return false;
/* 339:    */     }
/* 340:340 */     return findRowMax(false, null) >= toLimit - mSpacing;
/* 341:    */   }
/* 342:    */   
/* 343:    */ 
/* 344:    */ 
/* 345:    */ 
/* 346:    */ 
/* 347:    */ 
/* 348:    */ 
/* 349:    */ 
/* 350:    */ 
/* 351:    */   public abstract CircularIntArray[] getItemPositionsInRows(int paramInt1, int paramInt2);
/* 352:    */   
/* 353:    */ 
/* 354:    */ 
/* 355:    */ 
/* 356:    */ 
/* 357:    */ 
/* 358:    */ 
/* 359:    */ 
/* 360:    */   public final CircularIntArray[] getItemPositionsInRows()
/* 361:    */   {
/* 362:362 */     return getItemPositionsInRows(getFirstVisibleIndex(), getLastVisibleIndex());
/* 363:    */   }
/* 364:    */   
/* 365:    */ 
/* 366:    */ 
/* 367:    */ 
/* 368:    */ 
/* 369:    */ 
/* 370:    */   public final boolean prependOneColumnVisibleItems()
/* 371:    */   {
/* 372:372 */     return prependVisibleItems(mReversedFlow ? -2147483648 : 2147483647, true);
/* 373:    */   }
/* 374:    */   
/* 375:    */ 
/* 376:    */ 
/* 377:    */ 
/* 378:    */   public final void prependVisibleItems(int toLimit)
/* 379:    */   {
/* 380:380 */     prependVisibleItems(toLimit, false);
/* 381:    */   }
/* 382:    */   
/* 383:    */ 
/* 384:    */ 
/* 385:    */ 
/* 386:    */ 
/* 387:    */ 
/* 388:    */ 
/* 389:    */ 
/* 390:    */   protected abstract boolean prependVisibleItems(int paramInt, boolean paramBoolean);
/* 391:    */   
/* 392:    */ 
/* 393:    */ 
/* 394:    */ 
/* 395:    */ 
/* 396:    */ 
/* 397:    */ 
/* 398:    */   public boolean appendOneColumnVisibleItems()
/* 399:    */   {
/* 400:400 */     return appendVisibleItems(mReversedFlow ? 2147483647 : -2147483648, true);
/* 401:    */   }
/* 402:    */   
/* 403:    */ 
/* 404:    */ 
/* 405:    */ 
/* 406:    */   public final void appendVisibleItems(int toLimit)
/* 407:    */   {
/* 408:408 */     appendVisibleItems(toLimit, false);
/* 409:    */   }
/* 410:    */   
/* 411:    */ 
/* 412:    */ 
/* 413:    */ 
/* 414:    */ 
/* 415:    */ 
/* 416:    */ 
/* 417:    */ 
/* 418:    */   protected abstract boolean appendVisibleItems(int paramInt, boolean paramBoolean);
/* 419:    */   
/* 420:    */ 
/* 421:    */ 
/* 422:    */ 
/* 423:    */ 
/* 424:    */ 
/* 425:    */ 
/* 426:    */   public void removeInvisibleItemsAtEnd(int aboveIndex, int toLimit)
/* 427:    */   {
/* 428:428 */     while ((mLastVisibleIndex >= mFirstVisibleIndex) && (mLastVisibleIndex > aboveIndex))
/* 429:    */     {
/* 430:430 */       boolean offEnd = mProvider.getEdge(mLastVisibleIndex) >= toLimit;
/* 431:431 */       if (!offEnd) break;
/* 432:432 */       mProvider.removeItem(mLastVisibleIndex);
/* 433:433 */       mLastVisibleIndex -= 1;
/* 434:    */     }
/* 435:    */     
/* 436:    */ 
/* 437:    */ 
/* 438:438 */     resetVisibleIndexIfEmpty();
/* 439:    */   }
/* 440:    */   
/* 441:    */ 
/* 442:    */ 
/* 443:    */ 
/* 444:    */ 
/* 445:    */ 
/* 446:    */   public void removeInvisibleItemsAtFront(int belowIndex, int toLimit)
/* 447:    */   {
/* 448:448 */     while ((mLastVisibleIndex >= mFirstVisibleIndex) && (mFirstVisibleIndex < belowIndex)) {
/* 449:449 */       int size = mProvider.getSize(mFirstVisibleIndex);
/* 450:    */       
/* 451:    */ 
/* 452:452 */       boolean offFront = mProvider.getEdge(mFirstVisibleIndex) + size <= toLimit;
/* 453:453 */       if (!offFront) break;
/* 454:454 */       mProvider.removeItem(mFirstVisibleIndex);
/* 455:455 */       mFirstVisibleIndex += 1;
/* 456:    */     }
/* 457:    */     
/* 458:    */ 
/* 459:    */ 
/* 460:460 */     resetVisibleIndexIfEmpty();
/* 461:    */   }
/* 462:    */   
/* 463:    */   private void resetVisibleIndexIfEmpty() {
/* 464:464 */     if (mLastVisibleIndex < mFirstVisibleIndex) {
/* 465:465 */       resetVisibleIndex();
/* 466:    */     }
/* 467:    */   }
/* 468:    */   
/* 469:    */ 
/* 470:    */ 
/* 471:    */ 
/* 472:    */ 
/* 473:    */ 
/* 474:    */ 
/* 475:    */ 
/* 476:    */ 
/* 477:    */ 
/* 478:    */   public void fillDisappearingItems(int[] positions, int positionsLength, SparseIntArray positionToRow)
/* 479:    */   {
/* 480:480 */     int lastPos = getLastVisibleIndex();
/* 481:    */     
/* 482:482 */     int resultSearchLast = lastPos >= 0 ? Arrays.binarySearch(positions, 0, positionsLength, lastPos) : 0;
/* 483:483 */     if (resultSearchLast < 0)
/* 484:    */     {
/* 485:485 */       int firstDisappearingIndex = -resultSearchLast - 1;
/* 486:    */       int edge;
/* 487:487 */       int edge; if (mReversedFlow) {
/* 488:488 */         edge = mProvider.getEdge(lastPos) - mProvider.getSize(lastPos) - mSpacing;
/* 489:    */       } else {
/* 490:490 */         edge = mProvider.getEdge(lastPos) + mProvider.getSize(lastPos) + mSpacing;
/* 491:    */       }
/* 492:492 */       for (int i = firstDisappearingIndex; i < positionsLength; i++) {
/* 493:493 */         int disappearingIndex = positions[i];
/* 494:494 */         int disappearingRow = positionToRow.get(disappearingIndex);
/* 495:495 */         if (disappearingRow < 0) {
/* 496:496 */           disappearingRow = 0;
/* 497:    */         }
/* 498:498 */         int size = mProvider.createItem(disappearingIndex, true, mTmpItem, true);
/* 499:499 */         mProvider.addItem(mTmpItem[0], disappearingIndex, size, disappearingRow, edge);
/* 500:500 */         if (mReversedFlow) {
/* 501:501 */           edge = edge - size - mSpacing;
/* 502:    */         } else {
/* 503:503 */           edge = edge + size + mSpacing;
/* 504:    */         }
/* 505:    */       }
/* 506:    */     }
/* 507:    */     
/* 508:508 */     int firstPos = getFirstVisibleIndex();
/* 509:    */     
/* 510:510 */     int resultSearchFirst = firstPos >= 0 ? Arrays.binarySearch(positions, 0, positionsLength, firstPos) : 0;
/* 511:511 */     if (resultSearchFirst < 0)
/* 512:    */     {
/* 513:513 */       int firstDisappearingIndex = -resultSearchFirst - 2;
/* 514:    */       int edge;
/* 515:515 */       int edge; if (mReversedFlow) {
/* 516:516 */         edge = mProvider.getEdge(firstPos);
/* 517:    */       } else {
/* 518:518 */         edge = mProvider.getEdge(firstPos);
/* 519:    */       }
/* 520:520 */       for (int i = firstDisappearingIndex; i >= 0; i--) {
/* 521:521 */         int disappearingIndex = positions[i];
/* 522:522 */         int disappearingRow = positionToRow.get(disappearingIndex);
/* 523:523 */         if (disappearingRow < 0) {
/* 524:524 */           disappearingRow = 0;
/* 525:    */         }
/* 526:526 */         int size = mProvider.createItem(disappearingIndex, false, mTmpItem, true);
/* 527:527 */         if (mReversedFlow) {
/* 528:528 */           edge = edge + mSpacing + size;
/* 529:    */         } else {
/* 530:530 */           edge = edge - mSpacing - size;
/* 531:    */         }
/* 532:532 */         mProvider.addItem(mTmpItem[0], disappearingIndex, size, disappearingRow, edge);
/* 533:    */       }
/* 534:    */     }
/* 535:    */   }
/* 536:    */   
/* 537:    */   public void collectAdjacentPrefetchPositions(int fromLimit, int da, @NonNull RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {}
/* 538:    */   
/* 539:    */   public abstract void debugPrint(PrintWriter paramPrintWriter);
/* 540:    */ }


/* Location:           D:\Android\Huan_Component\androidx_aar_leanback\repo\classes.jar
 * Qualified Name:     androidx.leanback.widget.Grid
 * JD-Core Version:    0.7.0.1
 */