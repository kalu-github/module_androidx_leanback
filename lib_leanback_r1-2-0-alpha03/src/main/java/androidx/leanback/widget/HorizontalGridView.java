/*   1:    */ package androidx.leanback.widget;
/*   2:    */ 
/*   3:    */ import android.annotation.SuppressLint;
/*   4:    */ import android.content.Context;
/*   5:    */ import android.content.res.TypedArray;
/*   6:    */ import android.graphics.Bitmap;
/*   7:    */ import android.graphics.Bitmap.Config;
/*   8:    */ import android.graphics.Canvas;
/*   9:    */ import android.graphics.LinearGradient;
/*  10:    */ import android.graphics.Paint;
/*  11:    */ import android.graphics.PorterDuff.Mode;
/*  12:    */ import android.graphics.PorterDuffXfermode;
/*  13:    */ import android.graphics.Rect;
/*  14:    */ import android.graphics.Shader.TileMode;
/*  15:    */ import android.util.AttributeSet;
/*  16:    */ import android.util.TypedValue;
/*  17:    */ import android.view.View;
/*  18:    */ import androidx.annotation.NonNull;
/*  19:    */ import androidx.annotation.Nullable;
/*  20:    */ import androidx.core.view.ViewCompat;
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
/*  49:    */ 
/*  50:    */ 
/*  51:    */ 
/*  52:    */ 
/*  53:    */ 
/*  54:    */ 
/*  55:    */ 
/*  56:    */ 
/*  57:    */ public class HorizontalGridView
/*  58:    */   extends BaseGridView
/*  59:    */ {
/*  60:    */   private boolean mFadingLowEdge;
/*  61:    */   private boolean mFadingHighEdge;
/*  62: 62 */   private Paint mTempPaint = new Paint();
/*  63:    */   private Bitmap mTempBitmapLow;
/*  64:    */   private LinearGradient mLowFadeShader;
/*  65:    */   private int mLowFadeShaderLength;
/*  66:    */   private int mLowFadeShaderOffset;
/*  67:    */   private Bitmap mTempBitmapHigh;
/*  68:    */   private LinearGradient mHighFadeShader;
/*  69:    */   private int mHighFadeShaderLength;
/*  70:    */   private int mHighFadeShaderOffset;
/*  71: 71 */   private final Rect mTempRect = new Rect();
/*  72:    */   
/*  73:    */   public HorizontalGridView(@NonNull Context context) {
/*  74: 74 */     this(context, null);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public HorizontalGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
/*  78: 78 */     this(context, attrs, 0);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public HorizontalGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle)
/*  82:    */   {
/*  83: 83 */     super(context, attrs, defStyle);
/*  84: 84 */     mLayoutManager.setOrientation(0);
/*  85: 85 */     initAttributes(context, attrs);
/*  86:    */   }
/*  87:    */   
/*  88:    */   @SuppressLint({"CustomViewStyleable"})
/*  89:    */   protected void initAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
/*  90: 90 */     initBaseGridViewAttributes(context, attrs);
/*  91: 91 */     TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.lbHorizontalGridView);
/*  92: 92 */     ViewCompat.saveAttributeDataForStyleable(this, context, R.styleable.lbHorizontalGridView, attrs, a, 0, 0);
/*  93:    */     
/*  94: 94 */     setRowHeight(a);
/*  95: 95 */     setNumRows(a.getInt(R.styleable.lbHorizontalGridView_numberOfRows, 1));
/*  96: 96 */     a.recycle();
/*  97: 97 */     updateLayerType();
/*  98: 98 */     mTempPaint = new Paint();
/*  99: 99 */     mTempPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
/* 100:    */   }
/* 101:    */   
/* 102:    */   void setRowHeight(TypedArray array) {
/* 103:103 */     TypedValue typedValue = array.peekValue(R.styleable.lbHorizontalGridView_rowHeight);
/* 104:104 */     if (typedValue != null) {
/* 105:105 */       int size = array.getLayoutDimension(R.styleable.lbHorizontalGridView_rowHeight, 0);
/* 106:106 */       setRowHeight(size);
/* 107:    */     }
/* 108:    */   }
/* 109:    */   
/* 110:    */ 
/* 111:    */ 
/* 112:    */   public void setNumRows(int numRows)
/* 113:    */   {
/* 114:114 */     mLayoutManager.setNumRows(numRows);
/* 115:115 */     requestLayout();
/* 116:    */   }
/* 117:    */   
/* 118:    */ 
/* 119:    */ 
/* 120:    */ 
/* 121:    */ 
/* 122:    */ 
/* 123:    */ 
/* 124:    */   public void setRowHeight(int height)
/* 125:    */   {
/* 126:126 */     mLayoutManager.setRowHeight(height);
/* 127:127 */     requestLayout();
/* 128:    */   }
/* 129:    */   
/* 130:    */ 
/* 131:    */ 
/* 132:    */ 
/* 133:    */   public final void setFadingLeftEdge(boolean fading)
/* 134:    */   {
/* 135:135 */     if (mFadingLowEdge != fading) {
/* 136:136 */       mFadingLowEdge = fading;
/* 137:137 */       if (!mFadingLowEdge) {
/* 138:138 */         mTempBitmapLow = null;
/* 139:    */       }
/* 140:140 */       invalidate();
/* 141:141 */       updateLayerType();
/* 142:    */     }
/* 143:    */   }
/* 144:    */   
/* 145:    */ 
/* 146:    */ 
/* 147:    */   @SuppressLint({"GetterSetterNames"})
/* 148:    */   public final boolean getFadingLeftEdge()
/* 149:    */   {
/* 150:150 */     return mFadingLowEdge;
/* 151:    */   }
/* 152:    */   
/* 153:    */ 
/* 154:    */ 
/* 155:    */   public final void setFadingLeftEdgeLength(int fadeLength)
/* 156:    */   {
/* 157:157 */     if (mLowFadeShaderLength != fadeLength) {
/* 158:158 */       mLowFadeShaderLength = fadeLength;
/* 159:159 */       if (mLowFadeShaderLength != 0) {
/* 160:160 */         mLowFadeShader = new LinearGradient(0.0F, 0.0F, mLowFadeShaderLength, 0.0F, 0, -16777216, Shader.TileMode.CLAMP);
/* 161:    */       }
/* 162:    */       else {
/* 163:163 */         mLowFadeShader = null;
/* 164:    */       }
/* 165:165 */       invalidate();
/* 166:    */     }
/* 167:    */   }
/* 168:    */   
/* 169:    */ 
/* 170:    */ 
/* 171:    */   public final int getFadingLeftEdgeLength()
/* 172:    */   {
/* 173:173 */     return mLowFadeShaderLength;
/* 174:    */   }
/* 175:    */   
/* 176:    */ 
/* 177:    */ 
/* 178:    */ 
/* 179:    */ 
/* 180:    */   public final void setFadingLeftEdgeOffset(int fadeOffset)
/* 181:    */   {
/* 182:182 */     if (mLowFadeShaderOffset != fadeOffset) {
/* 183:183 */       mLowFadeShaderOffset = fadeOffset;
/* 184:184 */       invalidate();
/* 185:    */     }
/* 186:    */   }
/* 187:    */   
/* 188:    */ 
/* 189:    */ 
/* 190:    */ 
/* 191:    */ 
/* 192:    */   public final int getFadingLeftEdgeOffset()
/* 193:    */   {
/* 194:194 */     return mLowFadeShaderOffset;
/* 195:    */   }
/* 196:    */   
/* 197:    */ 
/* 198:    */ 
/* 199:    */ 
/* 200:    */   public final void setFadingRightEdge(boolean fading)
/* 201:    */   {
/* 202:202 */     if (mFadingHighEdge != fading) {
/* 203:203 */       mFadingHighEdge = fading;
/* 204:204 */       if (!mFadingHighEdge) {
/* 205:205 */         mTempBitmapHigh = null;
/* 206:    */       }
/* 207:207 */       invalidate();
/* 208:208 */       updateLayerType();
/* 209:    */     }
/* 210:    */   }
/* 211:    */   
/* 212:    */ 
/* 213:    */ 
/* 214:    */   @SuppressLint({"GetterSetterNames"})
/* 215:    */   public final boolean getFadingRightEdge()
/* 216:    */   {
/* 217:217 */     return mFadingHighEdge;
/* 218:    */   }
/* 219:    */   
/* 220:    */ 
/* 221:    */ 
/* 222:    */   public final void setFadingRightEdgeLength(int fadeLength)
/* 223:    */   {
/* 224:224 */     if (mHighFadeShaderLength != fadeLength) {
/* 225:225 */       mHighFadeShaderLength = fadeLength;
/* 226:226 */       if (mHighFadeShaderLength != 0) {
/* 227:227 */         mHighFadeShader = new LinearGradient(0.0F, 0.0F, mHighFadeShaderLength, 0.0F, -16777216, 0, Shader.TileMode.CLAMP);
/* 228:    */       }
/* 229:    */       else {
/* 230:230 */         mHighFadeShader = null;
/* 231:    */       }
/* 232:232 */       invalidate();
/* 233:    */     }
/* 234:    */   }
/* 235:    */   
/* 236:    */ 
/* 237:    */ 
/* 238:    */   public final int getFadingRightEdgeLength()
/* 239:    */   {
/* 240:240 */     return mHighFadeShaderLength;
/* 241:    */   }
/* 242:    */   
/* 243:    */ 
/* 244:    */ 
/* 245:    */ 
/* 246:    */ 
/* 247:    */   public final void setFadingRightEdgeOffset(int fadeOffset)
/* 248:    */   {
/* 249:249 */     if (mHighFadeShaderOffset != fadeOffset) {
/* 250:250 */       mHighFadeShaderOffset = fadeOffset;
/* 251:251 */       invalidate();
/* 252:    */     }
/* 253:    */   }
/* 254:    */   
/* 255:    */ 
/* 256:    */ 
/* 257:    */ 
/* 258:    */ 
/* 259:    */   public final int getFadingRightEdgeOffset()
/* 260:    */   {
/* 261:261 */     return mHighFadeShaderOffset;
/* 262:    */   }
/* 263:    */   
/* 264:    */   private boolean needsFadingLowEdge() {
/* 265:265 */     if (!mFadingLowEdge) {
/* 266:266 */       return false;
/* 267:    */     }
/* 268:268 */     int c = getChildCount();
/* 269:269 */     for (int i = 0; i < c; i++) {
/* 270:270 */       View view = getChildAt(i);
/* 271:271 */       if (mLayoutManager.getOpticalLeft(view) < getPaddingLeft() - mLowFadeShaderOffset) {
/* 272:272 */         return true;
/* 273:    */       }
/* 274:    */     }
/* 275:275 */     return false;
/* 276:    */   }
/* 277:    */   
/* 278:    */   private boolean needsFadingHighEdge() {
/* 279:279 */     if (!mFadingHighEdge) {
/* 280:280 */       return false;
/* 281:    */     }
/* 282:282 */     int c = getChildCount();
/* 283:283 */     for (int i = c - 1; i >= 0; i--) {
/* 284:284 */       View view = getChildAt(i);
/* 285:    */       
/* 286:286 */       if (mLayoutManager.getOpticalRight(view) > getWidth() - getPaddingRight() + mHighFadeShaderOffset) {
/* 287:287 */         return true;
/* 288:    */       }
/* 289:    */     }
/* 290:290 */     return false;
/* 291:    */   }
/* 292:    */   
/* 293:    */   private Bitmap getTempBitmapLow() {
/* 294:294 */     if ((mTempBitmapLow == null) || 
/* 295:295 */       (mTempBitmapLow.getWidth() != mLowFadeShaderLength) || 
/* 296:296 */       (mTempBitmapLow.getHeight() != getHeight())) {
/* 297:297 */       mTempBitmapLow = Bitmap.createBitmap(mLowFadeShaderLength, getHeight(), Bitmap.Config.ARGB_8888);
/* 298:    */     }
/* 299:    */     
/* 300:300 */     return mTempBitmapLow;
/* 301:    */   }
/* 302:    */   
/* 303:    */   private Bitmap getTempBitmapHigh() {
/* 304:304 */     if ((mTempBitmapHigh == null) || 
/* 305:305 */       (mTempBitmapHigh.getWidth() != mHighFadeShaderLength) || 
/* 306:306 */       (mTempBitmapHigh.getHeight() != getHeight()))
/* 307:    */     {
/* 308:    */ 
/* 309:    */ 
/* 310:    */ 
/* 311:    */ 
/* 312:    */ 
/* 313:    */ 
/* 314:314 */       mTempBitmapHigh = Bitmap.createBitmap(mHighFadeShaderLength, getHeight(), Bitmap.Config.ARGB_8888);
/* 315:    */     }
/* 316:    */     
/* 317:    */ 
/* 318:318 */     return mTempBitmapHigh;
/* 319:    */   }
/* 320:    */   
/* 321:    */   public void draw(@NonNull Canvas canvas)
/* 322:    */   {
/* 323:323 */     boolean needsFadingLow = needsFadingLowEdge();
/* 324:324 */     boolean needsFadingHigh = needsFadingHighEdge();
/* 325:325 */     if (!needsFadingLow) {
/* 326:326 */       mTempBitmapLow = null;
/* 327:    */     }
/* 328:328 */     if (!needsFadingHigh) {
/* 329:329 */       mTempBitmapHigh = null;
/* 330:    */     }
/* 331:331 */     if ((!needsFadingLow) && (!needsFadingHigh)) {
/* 332:332 */       super.draw(canvas);
/* 333:333 */       return;
/* 334:    */     }
/* 335:    */     
/* 336:    */ 
/* 337:337 */     int lowEdge = mFadingLowEdge ? getPaddingLeft() - mLowFadeShaderOffset - mLowFadeShaderLength : 0;
/* 338:    */     
/* 339:339 */     int highEdge = mFadingHighEdge ? getWidth() - getPaddingRight() + mHighFadeShaderOffset + mHighFadeShaderLength : getWidth();
/* 340:    */     
/* 341:    */ 
/* 342:342 */     int save = canvas.save();
/* 343:343 */     canvas.clipRect(lowEdge + (mFadingLowEdge ? mLowFadeShaderLength : 0), 0, highEdge - (
/* 344:344 */       mFadingHighEdge ? mHighFadeShaderLength : 0), getHeight());
/* 345:345 */     super.draw(canvas);
/* 346:346 */     canvas.restoreToCount(save);
/* 347:    */     
/* 348:348 */     Canvas tmpCanvas = new Canvas();
/* 349:349 */     mTempRect.top = 0;
/* 350:350 */     mTempRect.bottom = getHeight();
/* 351:351 */     if ((needsFadingLow) && (mLowFadeShaderLength > 0)) {
/* 352:352 */       Bitmap tempBitmap = getTempBitmapLow();
/* 353:353 */       tempBitmap.eraseColor(0);
/* 354:354 */       tmpCanvas.setBitmap(tempBitmap);
/* 355:    */       
/* 356:356 */       int tmpSave = tmpCanvas.save();
/* 357:357 */       tmpCanvas.clipRect(0, 0, mLowFadeShaderLength, getHeight());
/* 358:358 */       tmpCanvas.translate(-lowEdge, 0.0F);
/* 359:359 */       super.draw(tmpCanvas);
/* 360:360 */       tmpCanvas.restoreToCount(tmpSave);
/* 361:    */       
/* 362:362 */       mTempPaint.setShader(mLowFadeShader);
/* 363:363 */       tmpCanvas.drawRect(0.0F, 0.0F, mLowFadeShaderLength, getHeight(), mTempPaint);
/* 364:    */       
/* 365:365 */       mTempRect.left = 0;
/* 366:366 */       mTempRect.right = mLowFadeShaderLength;
/* 367:367 */       canvas.translate(lowEdge, 0.0F);
/* 368:368 */       canvas.drawBitmap(tempBitmap, mTempRect, mTempRect, null);
/* 369:369 */       canvas.translate(-lowEdge, 0.0F);
/* 370:    */     }
/* 371:371 */     if ((needsFadingHigh) && (mHighFadeShaderLength > 0)) {
/* 372:372 */       Bitmap tempBitmap = getTempBitmapHigh();
/* 373:373 */       tempBitmap.eraseColor(0);
/* 374:374 */       tmpCanvas.setBitmap(tempBitmap);
/* 375:    */       
/* 376:376 */       int tmpSave = tmpCanvas.save();
/* 377:377 */       tmpCanvas.clipRect(0, 0, mHighFadeShaderLength, getHeight());
/* 378:378 */       tmpCanvas.translate(-(highEdge - mHighFadeShaderLength), 0.0F);
/* 379:379 */       super.draw(tmpCanvas);
/* 380:380 */       tmpCanvas.restoreToCount(tmpSave);
/* 381:    */       
/* 382:382 */       mTempPaint.setShader(mHighFadeShader);
/* 383:383 */       tmpCanvas.drawRect(0.0F, 0.0F, mHighFadeShaderLength, getHeight(), mTempPaint);
/* 384:    */       
/* 385:385 */       mTempRect.left = 0;
/* 386:386 */       mTempRect.right = mHighFadeShaderLength;
/* 387:387 */       canvas.translate(highEdge - mHighFadeShaderLength, 0.0F);
/* 388:388 */       canvas.drawBitmap(tempBitmap, mTempRect, mTempRect, null);
/* 389:389 */       canvas.translate(-(highEdge - mHighFadeShaderLength), 0.0F);
/* 390:    */     }
/* 391:    */   }
/* 392:    */   
/* 393:    */ 
/* 394:    */ 
/* 395:    */ 
/* 396:    */ 
/* 397:    */ 
/* 398:    */ 
/* 399:    */   private void updateLayerType()
/* 400:    */   {
/* 401:401 */     if ((mFadingLowEdge) || (mFadingHighEdge)) {
/* 402:402 */       setLayerType(2, null);
/* 403:403 */       setWillNotDraw(false);
/* 404:    */     } else {
/* 405:405 */       setLayerType(0, null);
/* 406:406 */       setWillNotDraw(true);
/* 407:    */     }
/* 408:    */   }
/* 409:    */ }


/* Location:           D:\Android\Huan_Component\androidx_aar_leanback\repo\classes.jar
 * Qualified Name:     androidx.leanback.widget.HorizontalGridView
 * JD-Core Version:    0.7.0.1
 */