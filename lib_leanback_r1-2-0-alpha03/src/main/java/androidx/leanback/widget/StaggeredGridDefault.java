/*   1:    */ package androidx.leanback.widget;
/*   2:    */ 
/*   3:    */ import androidx.collection.CircularArray;
/*   4:    */ 
/*   5:    */ 
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
/*  26:    */ final class StaggeredGridDefault
/*  27:    */   extends StaggeredGrid
/*  28:    */ {
/*  29:    */   int getRowMax(int rowIndex)
/*  30:    */   {
/*  31: 31 */     if (mFirstVisibleIndex < 0) {
/*  32: 32 */       return -2147483648;
/*  33:    */     }
/*  34: 34 */     if (mReversedFlow) {
/*  35: 35 */       int edge = mProvider.getEdge(mFirstVisibleIndex);
/*  36: 36 */       if (getLocationmFirstVisibleIndex).mRow == rowIndex) {
/*  37: 37 */         return edge;
/*  38:    */       }
/*  39: 39 */       for (int i = mFirstVisibleIndex + 1; i <= getLastIndex(); i++) {
/*  40: 40 */         StaggeredGrid.Location loc = getLocation(i);
/*  41: 41 */         edge += mOffset;
/*  42: 42 */         if (mRow == rowIndex) {
/*  43: 43 */           return edge;
/*  44:    */         }
/*  45:    */       }
/*  46:    */     } else {
/*  47: 47 */       int edge = mProvider.getEdge(mLastVisibleIndex);
/*  48: 48 */       StaggeredGrid.Location loc = getLocation(mLastVisibleIndex);
/*  49: 49 */       if (mRow == rowIndex) {
/*  50: 50 */         return edge + mSize;
/*  51:    */       }
/*  52: 52 */       for (int i = mLastVisibleIndex - 1; i >= getFirstIndex(); i--) {
/*  53: 53 */         edge -= mOffset;
/*  54: 54 */         loc = getLocation(i);
/*  55: 55 */         if (mRow == rowIndex) {
/*  56: 56 */           return edge + mSize;
/*  57:    */         }
/*  58:    */       }
/*  59:    */     }
/*  60: 60 */     return -2147483648;
/*  61:    */   }
/*  62:    */   
/*  63:    */ 
/*  64:    */ 
/*  65:    */ 
/*  66:    */   int getRowMin(int rowIndex)
/*  67:    */   {
/*  68: 68 */     if (mFirstVisibleIndex < 0) {
/*  69: 69 */       return 2147483647;
/*  70:    */     }
/*  71: 71 */     if (mReversedFlow) {
/*  72: 72 */       int edge = mProvider.getEdge(mLastVisibleIndex);
/*  73: 73 */       StaggeredGrid.Location loc = getLocation(mLastVisibleIndex);
/*  74: 74 */       if (mRow == rowIndex) {
/*  75: 75 */         return edge - mSize;
/*  76:    */       }
/*  77: 77 */       for (int i = mLastVisibleIndex - 1; i >= getFirstIndex(); i--) {
/*  78: 78 */         edge -= mOffset;
/*  79: 79 */         loc = getLocation(i);
/*  80: 80 */         if (mRow == rowIndex) {
/*  81: 81 */           return edge - mSize;
/*  82:    */         }
/*  83:    */       }
/*  84:    */     } else {
/*  85: 85 */       int edge = mProvider.getEdge(mFirstVisibleIndex);
/*  86: 86 */       if (getLocationmFirstVisibleIndex).mRow == rowIndex) {
/*  87: 87 */         return edge;
/*  88:    */       }
/*  89: 89 */       for (int i = mFirstVisibleIndex + 1; i <= getLastIndex(); i++) {
/*  90: 90 */         StaggeredGrid.Location loc = getLocation(i);
/*  91: 91 */         edge += mOffset;
/*  92: 92 */         if (mRow == rowIndex) {
/*  93: 93 */           return edge;
/*  94:    */         }
/*  95:    */       }
/*  96:    */     }
/*  97: 97 */     return 2147483647;
/*  98:    */   }
/*  99:    */   
/* 100:    */ 
/* 101:    */ 
/* 102:    */ 
/* 103:    */ 
/* 104:    */ 
/* 105:    */   public int findRowMax(boolean findLarge, int indexLimit, int[] indices)
/* 106:    */   {
/* 107:107 */     int edge = mProvider.getEdge(indexLimit);
/* 108:108 */     StaggeredGrid.Location loc = getLocation(indexLimit);
/* 109:109 */     int row = mRow;
/* 110:110 */     int index = indexLimit;
/* 111:111 */     int visitedRows = 1;
/* 112:112 */     int visitRow = row;
/* 113:113 */     int value; if (mReversedFlow) {
/* 114:114 */       int value = edge;
/* 115:115 */       for (int i = indexLimit + 1; (visitedRows < mNumRows) && (i <= mLastVisibleIndex); i++) {
/* 116:116 */         loc = getLocation(i);
/* 117:117 */         edge += mOffset;
/* 118:118 */         if (mRow != visitRow) {
/* 119:119 */           visitRow = mRow;
/* 120:120 */           visitedRows++;
/* 121:121 */           if (findLarge ? edge > value : edge < value) {
/* 122:122 */             row = visitRow;
/* 123:123 */             value = edge;
/* 124:124 */             index = i;
/* 125:    */           }
/* 126:    */         }
/* 127:    */       }
/* 128:    */     } else {
/* 129:129 */       value = edge + mProvider.getSize(indexLimit);
/* 130:130 */       for (int i = indexLimit - 1; (visitedRows < mNumRows) && (i >= mFirstVisibleIndex); i--) {
/* 131:131 */         edge -= mOffset;
/* 132:132 */         loc = getLocation(i);
/* 133:133 */         if (mRow != visitRow) {
/* 134:134 */           visitRow = mRow;
/* 135:135 */           visitedRows++;
/* 136:136 */           int newValue = edge + mProvider.getSize(i);
/* 137:137 */           if (findLarge ? newValue > value : newValue < value) {
/* 138:138 */             row = visitRow;
/* 139:139 */             value = newValue;
/* 140:140 */             index = i;
/* 141:    */           }
/* 142:    */         }
/* 143:    */       }
/* 144:    */     }
/* 145:145 */     if (indices != null) {
/* 146:146 */       indices[0] = row;
/* 147:147 */       indices[1] = index;
/* 148:    */     }
/* 149:149 */     return value;
/* 150:    */   }
/* 151:    */   
/* 152:    */ 
/* 153:    */ 
/* 154:    */ 
/* 155:    */ 
/* 156:    */ 
/* 157:    */   public int findRowMin(boolean findLarge, int indexLimit, int[] indices)
/* 158:    */   {
/* 159:159 */     int edge = mProvider.getEdge(indexLimit);
/* 160:160 */     StaggeredGrid.Location loc = getLocation(indexLimit);
/* 161:161 */     int row = mRow;
/* 162:162 */     int index = indexLimit;
/* 163:163 */     int visitedRows = 1;
/* 164:164 */     int visitRow = row;
/* 165:165 */     int value; if (mReversedFlow) {
/* 166:166 */       int value = edge - mProvider.getSize(indexLimit);
/* 167:167 */       for (int i = indexLimit - 1; (visitedRows < mNumRows) && (i >= mFirstVisibleIndex); i--) {
/* 168:168 */         edge -= mOffset;
/* 169:169 */         loc = getLocation(i);
/* 170:170 */         if (mRow != visitRow) {
/* 171:171 */           visitRow = mRow;
/* 172:172 */           visitedRows++;
/* 173:173 */           int newValue = edge - mProvider.getSize(i);
/* 174:174 */           if (findLarge ? newValue > value : newValue < value) {
/* 175:175 */             value = newValue;
/* 176:176 */             row = visitRow;
/* 177:177 */             index = i;
/* 178:    */           }
/* 179:    */         }
/* 180:    */       }
/* 181:    */     } else {
/* 182:182 */       value = edge;
/* 183:183 */       for (int i = indexLimit + 1; (visitedRows < mNumRows) && (i <= mLastVisibleIndex); i++) {
/* 184:184 */         loc = getLocation(i);
/* 185:185 */         edge += mOffset;
/* 186:186 */         if (mRow != visitRow) {
/* 187:187 */           visitRow = mRow;
/* 188:188 */           visitedRows++;
/* 189:189 */           if (findLarge ? edge > value : edge < value) {
/* 190:190 */             value = edge;
/* 191:191 */             row = visitRow;
/* 192:192 */             index = i;
/* 193:    */           }
/* 194:    */         }
/* 195:    */       }
/* 196:    */     }
/* 197:197 */     if (indices != null) {
/* 198:198 */       indices[0] = row;
/* 199:199 */       indices[1] = index;
/* 200:    */     }
/* 201:201 */     return value;
/* 202:    */   }
/* 203:    */   
/* 204:    */   private int findRowEdgeLimitSearchIndex(boolean append) {
/* 205:205 */     boolean wrapped = false;
/* 206:206 */     if (append) {
/* 207:207 */       for (int index = mLastVisibleIndex; index >= mFirstVisibleIndex; index--) {
/* 208:208 */         int row = getLocationmRow;
/* 209:209 */         if (row == 0) {
/* 210:210 */           wrapped = true;
/* 211:211 */         } else if ((wrapped) && (row == mNumRows - 1)) {
/* 212:212 */           return index;
/* 213:    */         }
/* 214:    */       }
/* 215:    */     } else {
/* 216:216 */       for (int index = mFirstVisibleIndex; index <= mLastVisibleIndex; index++) {
/* 217:217 */         int row = getLocationmRow;
/* 218:218 */         if (row == mNumRows - 1) {
/* 219:219 */           wrapped = true;
/* 220:220 */         } else if ((wrapped) && (row == 0)) {
/* 221:221 */           return index;
/* 222:    */         }
/* 223:    */       }
/* 224:    */     }
/* 225:225 */     return -1;
/* 226:    */   }
/* 227:    */   
/* 228:    */   protected boolean appendVisibleItemsWithoutCache(int toLimit, boolean oneColumnMode)
/* 229:    */   {
/* 230:230 */     int count = mProvider.getCount();
/* 231:    */     boolean edgeLimitIsValid;
/* 232:    */     int itemIndex;
/* 233:    */     int rowIndex;
/* 234:    */     int edgeLimit;
/* 235:235 */     boolean edgeLimitIsValid; if (mLastVisibleIndex >= 0) {
/* 236:236 */       if (mLastVisibleIndex < getLastIndex())
/* 237:    */       {
/* 238:238 */         return false;
/* 239:    */       }
/* 240:240 */       int itemIndex = mLastVisibleIndex + 1;
/* 241:241 */       int rowIndex = getLocationmLastVisibleIndex).mRow;
/* 242:    */       
/* 243:243 */       int edgeLimitSearchIndex = findRowEdgeLimitSearchIndex(true);
/* 244:244 */       int edgeLimit; if (edgeLimitSearchIndex < 0)
/* 245:    */       {
/* 246:    */ 
/* 247:247 */         int edgeLimit = -2147483648;
/* 248:248 */         for (int i = 0; i < mNumRows; i++) {
/* 249:249 */           edgeLimit = mReversedFlow ? getRowMin(i) : getRowMax(i);
/* 250:250 */           if (edgeLimit != -2147483648) {
/* 251:    */             break;
/* 252:    */           }
/* 253:    */         }
/* 254:    */       }
/* 255:    */       else {
/* 256:256 */         edgeLimit = mReversedFlow ? findRowMin(false, edgeLimitSearchIndex, null) : findRowMax(true, edgeLimitSearchIndex, null);
/* 257:    */       }
/* 258:258 */       if (mReversedFlow ? getRowMin(rowIndex) <= edgeLimit : 
/* 259:259 */         getRowMax(rowIndex) >= edgeLimit)
/* 260:    */       {
/* 261:261 */         rowIndex += 1;
/* 262:262 */         if (rowIndex == mNumRows)
/* 263:    */         {
/* 264:264 */           rowIndex = 0;
/* 265:265 */           edgeLimit = mReversedFlow ? findRowMin(false, null) : findRowMax(true, null);
/* 266:    */         }
/* 267:    */       }
/* 268:268 */       edgeLimitIsValid = true;
/* 269:    */     } else {
/* 270:270 */       itemIndex = mStartIndex != -1 ? mStartIndex : 0;
/* 271:    */       
/* 272:272 */       rowIndex = (mLocations.size() > 0 ? getLocationgetLastIndex()mRow + 1 : itemIndex) % mNumRows;
/* 273:    */       
/* 274:274 */       edgeLimit = 0;
/* 275:275 */       edgeLimitIsValid = false;
/* 276:    */     }
/* 277:    */     
/* 278:278 */     boolean filledOne = false;
/* 279:    */     
/* 280:    */ 
/* 281:    */     for (;;)
/* 282:    */     {
/* 283:283 */       if (rowIndex < mNumRows)
/* 284:    */       {
/* 285:285 */         if ((itemIndex == count) || ((!oneColumnMode) && (checkAppendOverLimit(toLimit)))) {
/* 286:286 */           return filledOne;
/* 287:    */         }
/* 288:288 */         int location = mReversedFlow ? getRowMin(rowIndex) : getRowMax(rowIndex);
/* 289:289 */         if ((location == 2147483647) || (location == -2147483648))
/* 290:    */         {
/* 291:291 */           if (rowIndex == 0) {
/* 292:292 */             location = mReversedFlow ? getRowMin(mNumRows - 1) : getRowMax(mNumRows - 1);
/* 293:    */             
/* 294:294 */             if ((location != 2147483647) && (location != -2147483648)) {
/* 295:295 */               location += (mReversedFlow ? -mSpacing : mSpacing);
/* 296:    */             }
/* 297:    */           } else {
/* 298:298 */             location = mReversedFlow ? getRowMax(rowIndex - 1) : getRowMin(rowIndex - 1);
/* 299:    */           }
/* 300:    */         }
/* 301:    */         else {
/* 302:302 */           location += (mReversedFlow ? -mSpacing : mSpacing);
/* 303:    */         }
/* 304:304 */         int size = appendVisibleItemToRow(itemIndex++, rowIndex, location);
/* 305:305 */         filledOne = true;
/* 306:    */         
/* 307:    */ 
/* 308:308 */         if (edgeLimitIsValid) {
/* 309:309 */           while (mReversedFlow ? location - size > edgeLimit : location + size < edgeLimit)
/* 310:    */           {
/* 311:311 */             if ((itemIndex == count) || ((!oneColumnMode) && (checkAppendOverLimit(toLimit))))
/* 312:    */             {
/* 313:313 */               return filledOne;
/* 314:    */             }
/* 315:315 */             location += (mReversedFlow ? -size - mSpacing : size + mSpacing);
/* 316:316 */             size = appendVisibleItemToRow(itemIndex++, rowIndex, location);
/* 317:    */           }
/* 318:    */         }
/* 319:319 */         edgeLimitIsValid = true;
/* 320:320 */         edgeLimit = mReversedFlow ? getRowMin(rowIndex) : getRowMax(rowIndex);rowIndex++;
/* 321:    */       }
/* 322:    */       else
/* 323:    */       {
/* 324:323 */         if (oneColumnMode) {
/* 325:324 */           return filledOne;
/* 326:    */         }
/* 327:326 */         edgeLimit = mReversedFlow ? findRowMin(false, null) : findRowMax(true, null);
/* 328:    */         
/* 329:328 */         rowIndex = 0;
/* 330:    */       }
/* 331:    */     }
/* 332:    */   }
/* 333:    */   
/* 334:    */   protected boolean prependVisibleItemsWithoutCache(int toLimit, boolean oneColumnMode) { boolean edgeLimitIsValid;
/* 335:    */     int itemIndex;
/* 336:    */     int rowIndex;
/* 337:    */     int edgeLimit;
/* 338:    */     boolean edgeLimitIsValid;
/* 339:338 */     if (mFirstVisibleIndex >= 0) {
/* 340:339 */       if (mFirstVisibleIndex > getFirstIndex())
/* 341:    */       {
/* 342:341 */         return false;
/* 343:    */       }
/* 344:343 */       int itemIndex = mFirstVisibleIndex - 1;
/* 345:344 */       int rowIndex = getLocationmFirstVisibleIndex).mRow;
/* 346:    */       
/* 347:346 */       int edgeLimitSearchIndex = findRowEdgeLimitSearchIndex(false);
/* 348:347 */       int edgeLimit; if (edgeLimitSearchIndex < 0)
/* 349:    */       {
/* 350:    */ 
/* 351:350 */         rowIndex -= 1;
/* 352:351 */         int edgeLimit = 2147483647;
/* 353:352 */         for (int i = mNumRows - 1; i >= 0; i--) {
/* 354:353 */           edgeLimit = mReversedFlow ? getRowMax(i) : getRowMin(i);
/* 355:354 */           if (edgeLimit != 2147483647) {
/* 356:    */             break;
/* 357:    */           }
/* 358:    */         }
/* 359:    */       }
/* 360:    */       else {
/* 361:360 */         edgeLimit = mReversedFlow ? findRowMax(true, edgeLimitSearchIndex, null) : findRowMin(false, edgeLimitSearchIndex, null);
/* 362:    */       }
/* 363:362 */       if (mReversedFlow ? getRowMax(rowIndex) >= edgeLimit : 
/* 364:363 */         getRowMin(rowIndex) <= edgeLimit)
/* 365:    */       {
/* 366:365 */         rowIndex -= 1;
/* 367:366 */         if (rowIndex < 0)
/* 368:    */         {
/* 369:368 */           rowIndex = mNumRows - 1;
/* 370:    */           
/* 371:370 */           edgeLimit = mReversedFlow ? findRowMax(true, null) : findRowMin(false, null);
/* 372:    */         }
/* 373:    */       }
/* 374:373 */       edgeLimitIsValid = true;
/* 375:    */     } else {
/* 376:375 */       itemIndex = mStartIndex != -1 ? mStartIndex : 0;
/* 377:    */       
/* 378:    */ 
/* 379:378 */       rowIndex = (mLocations.size() > 0 ? getLocationgetFirstIndex()mRow + mNumRows - 1 : itemIndex) % mNumRows;
/* 380:379 */       edgeLimit = 0;
/* 381:380 */       edgeLimitIsValid = false;
/* 382:    */     }
/* 383:382 */     boolean filledOne = false;
/* 384:    */     
/* 385:    */ 
/* 386:    */     for (;;)
/* 387:    */     {
/* 388:387 */       if (rowIndex >= 0)
/* 389:    */       {
/* 390:389 */         if ((itemIndex < 0) || ((!oneColumnMode) && (checkPrependOverLimit(toLimit)))) {
/* 391:390 */           return filledOne;
/* 392:    */         }
/* 393:392 */         int location = mReversedFlow ? getRowMax(rowIndex) : getRowMin(rowIndex);
/* 394:393 */         if ((location == 2147483647) || (location == -2147483648))
/* 395:    */         {
/* 396:395 */           if (rowIndex == mNumRows - 1) {
/* 397:396 */             location = mReversedFlow ? getRowMax(0) : getRowMin(0);
/* 398:397 */             if ((location != 2147483647) && (location != -2147483648)) {
/* 399:398 */               location += (mReversedFlow ? mSpacing : -mSpacing);
/* 400:    */             }
/* 401:    */           } else {
/* 402:401 */             location = mReversedFlow ? getRowMin(rowIndex + 1) : getRowMax(rowIndex + 1);
/* 403:    */           }
/* 404:    */         }
/* 405:    */         else {
/* 406:405 */           location += (mReversedFlow ? mSpacing : -mSpacing);
/* 407:    */         }
/* 408:407 */         int size = prependVisibleItemToRow(itemIndex--, rowIndex, location);
/* 409:408 */         filledOne = true;
/* 410:    */         
/* 411:    */ 
/* 412:    */ 
/* 413:412 */         if (edgeLimitIsValid) {
/* 414:413 */           while (mReversedFlow ? location + size < edgeLimit : location - size > edgeLimit)
/* 415:    */           {
/* 416:415 */             if ((itemIndex < 0) || ((!oneColumnMode) && (checkPrependOverLimit(toLimit)))) {
/* 417:416 */               return filledOne;
/* 418:    */             }
/* 419:418 */             location += (mReversedFlow ? size + mSpacing : -size - mSpacing);
/* 420:419 */             size = prependVisibleItemToRow(itemIndex--, rowIndex, location);
/* 421:    */           }
/* 422:    */         }
/* 423:422 */         edgeLimitIsValid = true;
/* 424:423 */         edgeLimit = mReversedFlow ? getRowMax(rowIndex) : getRowMin(rowIndex);rowIndex--;
/* 425:    */       }
/* 426:    */       else
/* 427:    */       {
/* 428:426 */         if (oneColumnMode) {
/* 429:427 */           return filledOne;
/* 430:    */         }
/* 431:429 */         edgeLimit = mReversedFlow ? findRowMax(true, null) : findRowMin(false, null);
/* 432:    */         
/* 433:431 */         rowIndex = mNumRows - 1;
/* 434:    */       }
/* 435:    */     }
/* 436:    */   }
/* 437:    */ }


/* Location:           D:\Android\Huan_Component\androidx_aar_leanback\repo\classes.jar
 * Qualified Name:     androidx.leanback.widget.StaggeredGridDefault
 * JD-Core Version:    0.7.0.1
 */