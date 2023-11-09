/*   1:    */ package androidx.leanback.widget;
/*   2:    */ 
/*   3:    */ import android.graphics.Rect;
/*   4:    */ import android.view.View;
/*   5:    */ import android.view.ViewGroup;
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
/*  30:    */ class ItemAlignmentFacetHelper
/*  31:    */ {
/*  32: 32 */   private static final Rect sRect = new Rect();
/*  33:    */   
/*  34:    */ 
/*  35:    */ 
/*  36:    */ 
/*  37:    */   static int getAlignmentPosition(View itemView, ItemAlignmentFacet.ItemAlignmentDef facet, int orientation)
/*  38:    */   {
/*  39: 39 */     GridLayoutManager.LayoutParams p = (GridLayoutManager.LayoutParams)itemView.getLayoutParams();
/*  40: 40 */     View view = itemView;
/*  41: 41 */     if (mViewId != 0) {
/*  42: 42 */       view = itemView.findViewById(mViewId);
/*  43: 43 */       if (view == null) {
/*  44: 44 */         view = itemView;
/*  45:    */       }
/*  46:    */     }
/*  47: 47 */     int alignPos = mOffset;
/*  48: 48 */     if (orientation == 0) {
/*  49: 49 */       if (itemView.getLayoutDirection() == 1)
/*  50:    */       {
/*  51: 51 */         alignPos = (view == itemView ? p.getOpticalWidth(view) : view.getWidth()) - alignPos;
/*  52: 52 */         if (mOffsetWithPadding) {
/*  53: 53 */           if (mOffsetPercent == 0.0F) {
/*  54: 54 */             alignPos -= view.getPaddingRight();
/*  55: 55 */           } else if (mOffsetPercent == 100.0F) {
/*  56: 56 */             alignPos += view.getPaddingLeft();
/*  57:    */           }
/*  58:    */         }
/*  59: 59 */         if (mOffsetPercent != -1.0F)
/*  60:    */         {
/*  61: 61 */           alignPos = alignPos - (int)((view == itemView ? p.getOpticalWidth(view) : view.getWidth()) * mOffsetPercent / 100.0F);
/*  62:    */         }
/*  63: 63 */         if (itemView != view) {
/*  64: 64 */           sRectright = alignPos;
/*  65: 65 */           ((ViewGroup)itemView).offsetDescendantRectToMyCoords(view, sRect);
/*  66: 66 */           alignPos = sRectright + p.getOpticalRightInset();
/*  67:    */         }
/*  68:    */       } else {
/*  69: 69 */         if (mOffsetWithPadding) {
/*  70: 70 */           if (mOffsetPercent == 0.0F) {
/*  71: 71 */             alignPos += view.getPaddingLeft();
/*  72: 72 */           } else if (mOffsetPercent == 100.0F) {
/*  73: 73 */             alignPos -= view.getPaddingRight();
/*  74:    */           }
/*  75:    */         }
/*  76: 76 */         if (mOffsetPercent != -1.0F)
/*  77:    */         {
/*  78: 78 */           alignPos = alignPos + (int)((view == itemView ? p.getOpticalWidth(view) : view.getWidth()) * mOffsetPercent / 100.0F);
/*  79:    */         }
/*  80: 80 */         if (itemView != view) {
/*  81: 81 */           sRectleft = alignPos;
/*  82: 82 */           ((ViewGroup)itemView).offsetDescendantRectToMyCoords(view, sRect);
/*  83: 83 */           alignPos = sRectleft - p.getOpticalLeftInset();
/*  84:    */         }
/*  85:    */       }
/*  86:    */     } else {
/*  87: 87 */       if (mOffsetWithPadding) {
/*  88: 88 */         if (mOffsetPercent == 0.0F) {
/*  89: 89 */           alignPos += view.getPaddingTop();
/*  90: 90 */         } else if (mOffsetPercent == 100.0F) {
/*  91: 91 */           alignPos -= view.getPaddingBottom();
/*  92:    */         }
/*  93:    */       }
/*  94: 94 */       if (mOffsetPercent != -1.0F) {
/*  95: 95 */         alignPos += (int)((view == itemView ? p.getOpticalHeight(view) : view.getHeight()) * mOffsetPercent / 100.0F);
/*  96:    */       }
/*  97:    */       
/*  98: 98 */       if (itemView != view) {
/*  99: 99 */         sRecttop = alignPos;
/* 100:100 */         ((ViewGroup)itemView).offsetDescendantRectToMyCoords(view, sRect);
/* 101:101 */         alignPos = sRecttop - p.getOpticalTopInset();
/* 102:    */       }
/* 103:103 */       if (facet.isAlignedToTextViewBaseLine()) {
/* 104:104 */         alignPos += view.getBaseline();
/* 105:    */       }
/* 106:    */     }
/* 107:107 */     return alignPos;
/* 108:    */   }
/* 109:    */ }


/* Location:           D:\Android\Huan_Component\androidx_aar_leanback\repo\classes.jar
 * Qualified Name:     androidx.leanback.widget.ItemAlignmentFacetHelper
 * JD-Core Version:    0.7.0.1
 */