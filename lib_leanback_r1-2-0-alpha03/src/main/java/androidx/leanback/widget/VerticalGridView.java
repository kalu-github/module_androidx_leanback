/*  1:   */ package androidx.leanback.widget;
/*  2:   */ 
/*  3:   */ import android.annotation.SuppressLint;
/*  4:   */ import android.content.Context;
/*  5:   */ import android.content.res.TypedArray;
/*  6:   */ import android.util.AttributeSet;
/*  7:   */ import android.util.TypedValue;
/*  8:   */ import androidx.annotation.NonNull;
/*  9:   */ import androidx.annotation.Nullable;
/* 10:   */ import androidx.core.view.ViewCompat;
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
/* 27:   */ 
/* 28:   */ 
/* 29:   */ 
/* 30:   */ 
/* 31:   */ 
/* 32:   */ 
/* 33:   */ 
/* 34:   */ 
/* 35:   */ 
/* 36:   */ 
/* 37:   */ 
/* 38:   */ 
/* 39:   */ 
/* 40:   */ 
/* 41:   */ 
/* 42:   */ 
/* 43:   */ 
/* 44:   */ public class VerticalGridView
/* 45:   */   extends BaseGridView
/* 46:   */ {
/* 47:   */   public VerticalGridView(@NonNull Context context)
/* 48:   */   {
/* 49:49 */     this(context, null);
/* 50:   */   }
/* 51:   */   
/* 52:   */   public VerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
/* 53:53 */     this(context, attrs, 0);
/* 54:   */   }
/* 55:   */   
/* 56:   */   public VerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
/* 57:57 */     super(context, attrs, defStyle);
/* 58:58 */     mLayoutManager.setOrientation(1);
/* 59:59 */     initAttributes(context, attrs);
/* 60:   */   }
/* 61:   */   
/* 62:   */   @SuppressLint({"CustomViewStyleable"})
/* 63:   */   protected void initAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
/* 64:64 */     initBaseGridViewAttributes(context, attrs);
/* 65:65 */     TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.lbVerticalGridView);
/* 66:66 */     ViewCompat.saveAttributeDataForStyleable(this, context, R.styleable.lbVerticalGridView, attrs, a, 0, 0);
/* 67:   */     
/* 68:68 */     setColumnWidth(a);
/* 69:69 */     setNumColumns(a.getInt(R.styleable.lbVerticalGridView_numberOfColumns, 1));
/* 70:70 */     a.recycle();
/* 71:   */   }
/* 72:   */   
/* 73:   */   void setColumnWidth(TypedArray array) {
/* 74:74 */     TypedValue typedValue = array.peekValue(R.styleable.lbVerticalGridView_columnWidth);
/* 75:75 */     if (typedValue != null) {
/* 76:76 */       int size = array.getLayoutDimension(R.styleable.lbVerticalGridView_columnWidth, 0);
/* 77:77 */       setColumnWidth(size);
/* 78:   */     }
/* 79:   */   }
/* 80:   */   
/* 81:   */ 
/* 82:   */ 
/* 83:   */   public void setNumColumns(int numColumns)
/* 84:   */   {
/* 85:85 */     mLayoutManager.setNumRows(numColumns);
/* 86:86 */     requestLayout();
/* 87:   */   }
/* 88:   */   
/* 89:   */ 
/* 90:   */ 
/* 91:   */ 
/* 92:   */ 
/* 93:   */ 
/* 94:   */ 
/* 95:   */   public void setColumnWidth(int width)
/* 96:   */   {
/* 97:97 */     mLayoutManager.setRowHeight(width);
/* 98:98 */     requestLayout();
/* 99:   */   }
/* :0:   */ }


/* Location:           D:\Android\Huan_Component\androidx_aar_leanback\repo\classes.jar
 * Qualified Name:     androidx.leanback.widget.VerticalGridView
 * JD-Core Version:    0.7.0.1
 */