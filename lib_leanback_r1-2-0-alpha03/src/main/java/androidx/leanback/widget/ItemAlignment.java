/*  1:   */ package androidx.leanback.widget;
/*  2:   */ 
/*  3:   */ import android.view.View;
/*  4:   */ 
/*  5:   */ 
/*  6:   */ 
/*  7:   */ 
/*  8:   */ 
/*  9:   */ 
/* 10:   */ 
/* 11:   */ 
/* 12:   */ 
/* 13:   */ 
/* 14:   */ 
/* 15:   */ 
/* 16:   */ 
/* 17:   */ 
/* 18:   */ 
/* 19:   */ 
/* 20:   */ 
/* 21:   */ 
/* 22:   */ 
/* 23:   */ 
/* 24:   */ 
/* 25:   */ 
/* 26:   */ 
/* 27:   */ class ItemAlignment
/* 28:   */ {
/* 29:   */   static final class Axis
/* 30:   */     extends ItemAlignmentFacet.ItemAlignmentDef
/* 31:   */   {
/* 32:   */     private final int mOrientation;
/* 33:   */     
/* 34:   */     Axis(int orientation)
/* 35:   */     {
/* 36:36 */       mOrientation = orientation;
/* 37:   */     }
/* 38:   */     
/* 39:   */ 
/* 40:   */ 
/* 41:   */     public int getAlignmentPosition(View itemView)
/* 42:   */     {
/* 43:43 */       return ItemAlignmentFacetHelper.getAlignmentPosition(itemView, this, mOrientation);
/* 44:   */     }
/* 45:   */   }
/* 46:   */   
/* 47:47 */   private int mOrientation = 0;
/* 48:   */   
/* 49:49 */   public final Axis vertical = new Axis(1);
/* 50:   */   
/* 51:51 */   public final Axis horizontal = new Axis(0);
/* 52:   */   
/* 53:53 */   private Axis mMainAxis = horizontal;
/* 54:   */   
/* 55:55 */   private Axis mSecondAxis = vertical;
/* 56:   */   
/* 57:   */   public final Axis mainAxis() {
/* 58:58 */     return mMainAxis;
/* 59:   */   }
/* 60:   */   
/* 61:   */   public final Axis secondAxis() {
/* 62:62 */     return mSecondAxis;
/* 63:   */   }
/* 64:   */   
/* 65:   */   public final void setOrientation(int orientation) {
/* 66:66 */     mOrientation = orientation;
/* 67:67 */     if (mOrientation == 0) {
/* 68:68 */       mMainAxis = horizontal;
/* 69:69 */       mSecondAxis = vertical;
/* 70:   */     } else {
/* 71:71 */       mMainAxis = vertical;
/* 72:72 */       mSecondAxis = horizontal;
/* 73:   */     }
/* 74:   */   }
/* 75:   */   
/* 76:   */   public final int getOrientation() {
/* 77:77 */     return mOrientation;
/* 78:   */   }
/* 79:   */ }


/* Location:           D:\Android\Huan_Component\androidx_aar_leanback\repo\classes.jar
 * Qualified Name:     androidx.leanback.widget.ItemAlignment
 * JD-Core Version:    0.7.0.1
 */