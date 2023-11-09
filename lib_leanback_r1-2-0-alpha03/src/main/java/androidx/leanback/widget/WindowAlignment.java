/*   1:    */ package androidx.leanback.widget;
/*   2:    */ 
/*   3:    */ 
/*   4:    */ 
/*   5:    */ 
/*   6:    */ 
/*   7:    */ 
/*   8:    */ 
/*   9:    */ 
/*  10:    */ final class WindowAlignment
/*  11:    */ {
/*  12:    */   static final class Axis
/*  13:    */   {
/*  14:    */     private static final int PF_KEYLINE_OVER_LOW_EDGE = 1;
/*  15:    */     
/*  16:    */ 
/*  17:    */ 
/*  18:    */ 
/*  19:    */ 
/*  20:    */ 
/*  21:    */ 
/*  22:    */     private static final int PF_KEYLINE_OVER_HIGH_EDGE = 2;
/*  23:    */     
/*  24:    */ 
/*  25:    */ 
/*  26:    */ 
/*  27:    */ 
/*  28:    */ 
/*  29:    */ 
/*  30:    */     private int mMaxEdge;
/*  31:    */     
/*  32:    */ 
/*  33:    */ 
/*  34:    */ 
/*  35:    */ 
/*  36:    */ 
/*  37:    */     private int mMinEdge;
/*  38:    */     
/*  39:    */ 
/*  40:    */ 
/*  41:    */ 
/*  42:    */ 
/*  43:    */ 
/*  44:    */     private int mMaxScroll;
/*  45:    */     
/*  46:    */ 
/*  47:    */ 
/*  48:    */ 
/*  49:    */ 
/*  50:    */ 
/*  51:    */     private int mMinScroll;
/*  52:    */     
/*  53:    */ 
/*  54:    */ 
/*  55:    */ 
/*  56:    */ 
/*  57:    */ 
/*  58: 58 */     private int mPreferredKeyLine = 2;
/*  59:    */     
/*  60: 60 */     private int mWindowAlignment = 3;
/*  61:    */     
/*  62: 62 */     private int mWindowAlignmentOffset = 0;
/*  63:    */     
/*  64: 64 */     private float mWindowAlignmentOffsetPercent = 50.0F;
/*  65:    */     
/*  66:    */ 
/*  67:    */     private int mSize;
/*  68:    */     
/*  69:    */ 
/*  70:    */     private int mPaddingMin;
/*  71:    */     
/*  72:    */ 
/*  73:    */     private int mPaddingMax;
/*  74:    */     
/*  75:    */ 
/*  76:    */     private boolean mReversedFlow;
/*  77:    */     
/*  78:    */ 
/*  79:    */     Axis(String name)
/*  80:    */     {
/*  81: 81 */       reset();
/*  82:    */     }
/*  83:    */     
/*  84:    */     public int getWindowAlignment() {
/*  85: 85 */       return mWindowAlignment;
/*  86:    */     }
/*  87:    */     
/*  88:    */     public void setWindowAlignment(int windowAlignment) {
/*  89: 89 */       mWindowAlignment = windowAlignment;
/*  90:    */     }
/*  91:    */     
/*  92:    */ 
/*  93:    */     void setPreferKeylineOverLowEdge(boolean keylineOverLowEdge)
/*  94:    */     {
/*  95: 95 */       mPreferredKeyLine = (keylineOverLowEdge ? mPreferredKeyLine | 0x1 : mPreferredKeyLine & 0xFFFFFFFE);
/*  96:    */     }
/*  97:    */     
/*  98:    */ 
/*  99:    */     void setPreferKeylineOverHighEdge(boolean keylineOverHighEdge)
/* 100:    */     {
/* 101:101 */       mPreferredKeyLine = (keylineOverHighEdge ? mPreferredKeyLine | 0x2 : mPreferredKeyLine & 0xFFFFFFFD);
/* 102:    */     }
/* 103:    */     
/* 104:    */     boolean isPreferKeylineOverHighEdge() {
/* 105:105 */       return (mPreferredKeyLine & 0x2) != 0;
/* 106:    */     }
/* 107:    */     
/* 108:    */     boolean isPreferKeylineOverLowEdge() {
/* 109:109 */       return (mPreferredKeyLine & 0x1) != 0;
/* 110:    */     }
/* 111:    */     
/* 112:    */     public int getWindowAlignmentOffset() {
/* 113:113 */       return mWindowAlignmentOffset;
/* 114:    */     }
/* 115:    */     
/* 116:    */     public void setWindowAlignmentOffset(int offset) {
/* 117:117 */       mWindowAlignmentOffset = offset;
/* 118:    */     }
/* 119:    */     
/* 120:    */     public void setWindowAlignmentOffsetPercent(float percent) {
/* 121:121 */       if (((percent < 0.0F) || (percent > 100.0F)) && (percent != -1.0F))
/* 122:    */       {
/* 123:123 */         throw new IllegalArgumentException();
/* 124:    */       }
/* 125:125 */       mWindowAlignmentOffsetPercent = percent;
/* 126:    */     }
/* 127:    */     
/* 128:    */     public float getWindowAlignmentOffsetPercent() {
/* 129:129 */       return mWindowAlignmentOffsetPercent;
/* 130:    */     }
/* 131:    */     
/* 132:    */ 
/* 133:    */ 
/* 134:    */     public int getMinScroll()
/* 135:    */     {
/* 136:136 */       return mMinScroll;
/* 137:    */     }
/* 138:    */     
/* 139:    */     public void invalidateScrollMin() {
/* 140:140 */       mMinEdge = -2147483648;
/* 141:141 */       mMinScroll = -2147483648;
/* 142:    */     }
/* 143:    */     
/* 144:    */ 
/* 145:    */ 
/* 146:    */     public int getMaxScroll()
/* 147:    */     {
/* 148:148 */       return mMaxScroll;
/* 149:    */     }
/* 150:    */     
/* 151:    */     public void invalidateScrollMax() {
/* 152:152 */       mMaxEdge = 2147483647;
/* 153:153 */       mMaxScroll = 2147483647;
/* 154:    */     }
/* 155:    */     
/* 156:    */     void reset() {
/* 157:157 */       mMinEdge = -2147483648;
/* 158:158 */       mMaxEdge = 2147483647;
/* 159:    */     }
/* 160:    */     
/* 161:    */     public boolean isMinUnknown() {
/* 162:162 */       return mMinEdge == -2147483648;
/* 163:    */     }
/* 164:    */     
/* 165:    */     public boolean isMaxUnknown() {
/* 166:166 */       return mMaxEdge == 2147483647;
/* 167:    */     }
/* 168:    */     
/* 169:    */     public void setSize(int size) {
/* 170:170 */       mSize = size;
/* 171:    */     }
/* 172:    */     
/* 173:    */     public int getSize() {
/* 174:174 */       return mSize;
/* 175:    */     }
/* 176:    */     
/* 177:    */     public void setPadding(int paddingMin, int paddingMax) {
/* 178:178 */       mPaddingMin = paddingMin;
/* 179:179 */       mPaddingMax = paddingMax;
/* 180:    */     }
/* 181:    */     
/* 182:    */     public int getPaddingMin() {
/* 183:183 */       return mPaddingMin;
/* 184:    */     }
/* 185:    */     
/* 186:    */     public int getPaddingMax() {
/* 187:187 */       return mPaddingMax;
/* 188:    */     }
/* 189:    */     
/* 190:    */     public int getClientSize() {
/* 191:191 */       return mSize - mPaddingMin - mPaddingMax;
/* 192:    */     }
/* 193:    */     
/* 194:    */     int calculateKeyline() {
/* 195:    */       int keyLine;
/* 196:196 */       if (!mReversedFlow) { int keyLine;
/* 197:197 */         int keyLine; if (mWindowAlignmentOffset >= 0) {
/* 198:198 */           keyLine = mWindowAlignmentOffset;
/* 199:    */         } else {
/* 200:200 */           keyLine = mSize + mWindowAlignmentOffset;
/* 201:    */         }
/* 202:202 */         if (mWindowAlignmentOffsetPercent != -1.0F)
/* 203:203 */           keyLine += (int)(mSize * mWindowAlignmentOffsetPercent / 100.0F);
/* 204:    */       } else {
/* 205:    */         int keyLine;
/* 206:206 */         if (mWindowAlignmentOffset >= 0) {
/* 207:207 */           keyLine = mSize - mWindowAlignmentOffset;
/* 208:    */         } else {
/* 209:209 */           keyLine = -mWindowAlignmentOffset;
/* 210:    */         }
/* 211:211 */         if (mWindowAlignmentOffsetPercent != -1.0F) {
/* 212:212 */           keyLine -= (int)(mSize * mWindowAlignmentOffsetPercent / 100.0F);
/* 213:    */         }
/* 214:    */       }
/* 215:215 */       return keyLine;
/* 216:    */     }
/* 217:    */     
/* 218:    */ 
/* 219:    */ 
/* 220:    */     int calculateScrollToKeyLine(int viewCenterPosition, int keyLine)
/* 221:    */     {
/* 222:222 */       return viewCenterPosition - keyLine;
/* 223:    */     }
/* 224:    */     
/* 225:    */ 
/* 226:    */ 
/* 227:    */ 
/* 228:    */     public void updateMinMax(int minEdge, int maxEdge, int minChildViewCenter, int maxChildViewCenter)
/* 229:    */     {
/* 230:230 */       mMinEdge = minEdge;
/* 231:231 */       mMaxEdge = maxEdge;
/* 232:232 */       int clientSize = getClientSize();
/* 233:233 */       int keyLine = calculateKeyline();
/* 234:234 */       boolean isMinUnknown = isMinUnknown();
/* 235:235 */       boolean isMaxUnknown = isMaxUnknown();
/* 236:236 */       if (!isMinUnknown) {
/* 237:237 */         if (!mReversedFlow ? (mWindowAlignment & 0x1) != 0 : (mWindowAlignment & 0x2) != 0)
/* 238:    */         {
/* 239:    */ 
/* 240:240 */           mMinScroll = (mMinEdge - mPaddingMin);
/* 241:    */         }
/* 242:    */         else {
/* 243:243 */           mMinScroll = calculateScrollToKeyLine(minChildViewCenter, keyLine);
/* 244:    */         }
/* 245:    */       }
/* 246:246 */       if (!isMaxUnknown) {
/* 247:247 */         if (!mReversedFlow ? (mWindowAlignment & 0x2) != 0 : (mWindowAlignment & 0x1) != 0)
/* 248:    */         {
/* 249:    */ 
/* 250:250 */           mMaxScroll = (mMaxEdge - mPaddingMin - clientSize);
/* 251:    */         }
/* 252:    */         else {
/* 253:253 */           mMaxScroll = calculateScrollToKeyLine(maxChildViewCenter, keyLine);
/* 254:    */         }
/* 255:    */       }
/* 256:256 */       if ((!isMaxUnknown) && (!isMinUnknown)) {
/* 257:257 */         if (!mReversedFlow) {
/* 258:258 */           if ((mWindowAlignment & 0x1) != 0) {
/* 259:259 */             if (isPreferKeylineOverLowEdge())
/* 260:    */             {
/* 261:    */ 
/* 262:262 */               mMinScroll = Math.min(mMinScroll, 
/* 263:263 */                 calculateScrollToKeyLine(maxChildViewCenter, keyLine));
/* 264:    */             }
/* 265:    */             
/* 266:266 */             mMaxScroll = Math.max(mMinScroll, mMaxScroll);
/* 267:267 */           } else if ((mWindowAlignment & 0x2) != 0) {
/* 268:268 */             if (isPreferKeylineOverHighEdge())
/* 269:    */             {
/* 270:    */ 
/* 271:271 */               mMaxScroll = Math.max(mMaxScroll, 
/* 272:272 */                 calculateScrollToKeyLine(minChildViewCenter, keyLine));
/* 273:    */             }
/* 274:    */             
/* 275:275 */             mMinScroll = Math.min(mMinScroll, mMaxScroll);
/* 276:    */           }
/* 277:    */         }
/* 278:278 */         else if ((mWindowAlignment & 0x1) != 0) {
/* 279:279 */           if (isPreferKeylineOverLowEdge())
/* 280:    */           {
/* 281:    */ 
/* 282:282 */             mMaxScroll = Math.max(mMaxScroll, 
/* 283:283 */               calculateScrollToKeyLine(minChildViewCenter, keyLine));
/* 284:    */           }
/* 285:    */           
/* 286:286 */           mMinScroll = Math.min(mMinScroll, mMaxScroll);
/* 287:287 */         } else if ((mWindowAlignment & 0x2) != 0) {
/* 288:288 */           if (isPreferKeylineOverHighEdge())
/* 289:    */           {
/* 290:    */ 
/* 291:291 */             mMinScroll = Math.min(mMinScroll, 
/* 292:292 */               calculateScrollToKeyLine(maxChildViewCenter, keyLine));
/* 293:    */           }
/* 294:    */           
/* 295:295 */           mMaxScroll = Math.max(mMinScroll, mMaxScroll);
/* 296:    */         }
/* 297:    */       }
/* 298:    */     }
/* 299:    */     
/* 300:    */ 
/* 301:    */ 
/* 302:    */ 
/* 303:    */ 
/* 304:    */ 
/* 305:    */     public int getScroll(int viewCenter)
/* 306:    */     {
/* 307:307 */       int size = getSize();
/* 308:308 */       int keyLine = calculateKeyline();
/* 309:309 */       boolean isMinUnknown = isMinUnknown();
/* 310:310 */       boolean isMaxUnknown = isMaxUnknown();
/* 311:311 */       if (!isMinUnknown) {
/* 312:312 */         int keyLineToMinEdge = keyLine - mPaddingMin;
/* 313:313 */         if ((!mReversedFlow ? (mWindowAlignment & 0x1) != 0 : (mWindowAlignment & 0x2) != 0) && (viewCenter - mMinEdge <= keyLineToMinEdge))
/* 314:    */         {
/* 315:    */ 
/* 316:    */ 
/* 317:317 */           int alignToMin = mMinEdge - mPaddingMin;
/* 318:    */           
/* 319:319 */           if ((!isMaxUnknown) && (alignToMin > mMaxScroll)) {
/* 320:320 */             alignToMin = mMaxScroll;
/* 321:    */           }
/* 322:322 */           return alignToMin;
/* 323:    */         }
/* 324:    */       }
/* 325:325 */       if (!isMaxUnknown) {
/* 326:326 */         int keyLineToMaxEdge = size - keyLine - mPaddingMax;
/* 327:327 */         if ((!mReversedFlow ? (mWindowAlignment & 0x2) != 0 : (mWindowAlignment & 0x1) != 0) && (mMaxEdge - viewCenter <= keyLineToMaxEdge))
/* 328:    */         {
/* 329:    */ 
/* 330:    */ 
/* 331:331 */           int alignToMax = mMaxEdge - (size - mPaddingMax);
/* 332:    */           
/* 333:333 */           if ((!isMinUnknown) && (alignToMax < mMinScroll)) {
/* 334:334 */             alignToMax = mMinScroll;
/* 335:    */           }
/* 336:336 */           return alignToMax;
/* 337:    */         }
/* 338:    */       }
/* 339:    */       
/* 340:340 */       return calculateScrollToKeyLine(viewCenter, keyLine);
/* 341:    */     }
/* 342:    */     
/* 343:    */     public void setReversedFlow(boolean reversedFlow) {
/* 344:344 */       mReversedFlow = reversedFlow;
/* 345:    */     }
/* 346:    */     
/* 347:    */     public String toString()
/* 348:    */     {
/* 349:349 */       return " min:" + mMinEdge + " " + mMinScroll + " max:" + mMaxEdge + " " + mMaxScroll;
/* 350:    */     }
/* 351:    */   }
/* 352:    */   
/* 353:    */ 
/* 354:354 */   private int mOrientation = 0;
/* 355:    */   
/* 356:356 */   public final Axis vertical = new Axis("vertical");
/* 357:    */   
/* 358:358 */   public final Axis horizontal = new Axis("horizontal");
/* 359:    */   
/* 360:360 */   private Axis mMainAxis = horizontal;
/* 361:    */   
/* 362:362 */   private Axis mSecondAxis = vertical;
/* 363:    */   
/* 364:    */   public Axis mainAxis() {
/* 365:365 */     return mMainAxis;
/* 366:    */   }
/* 367:    */   
/* 368:    */   public Axis secondAxis() {
/* 369:369 */     return mSecondAxis;
/* 370:    */   }
/* 371:    */   
/* 372:    */   public void setOrientation(int orientation) {
/* 373:373 */     mOrientation = orientation;
/* 374:374 */     if (mOrientation == 0) {
/* 375:375 */       mMainAxis = horizontal;
/* 376:376 */       mSecondAxis = vertical;
/* 377:    */     } else {
/* 378:378 */       mMainAxis = vertical;
/* 379:379 */       mSecondAxis = horizontal;
/* 380:    */     }
/* 381:    */   }
/* 382:    */   
/* 383:    */   public int getOrientation() {
/* 384:384 */     return mOrientation;
/* 385:    */   }
/* 386:    */   
/* 387:    */   public void reset() {
/* 388:388 */     mainAxis().reset();
/* 389:    */   }
/* 390:    */   
/* 391:    */   public String toString()
/* 392:    */   {
/* 393:393 */     return "horizontal=" + horizontal + "; vertical=" + vertical;
/* 394:    */   }
/* 395:    */ }


/* Location:           D:\Android\Huan_Component\androidx_aar_leanback\repo\classes.jar
 * Qualified Name:     androidx.leanback.widget.WindowAlignment
 * JD-Core Version:    0.7.0.1
 */