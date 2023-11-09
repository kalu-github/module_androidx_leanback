/*   1:    */ package androidx.leanback.widget;
/*   2:    */ 
/*   3:    */ import androidx.annotation.NonNull;
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
/*  42:    */ public final class ItemAlignmentFacet
/*  43:    */ {
/*  44:    */   public static final float ITEM_ALIGN_OFFSET_PERCENT_DISABLED = -1.0F;
/*  45:    */   
/*  46:    */   public static class ItemAlignmentDef
/*  47:    */   {
/*  48: 48 */     int mViewId = -1;
/*  49: 49 */     int mFocusViewId = -1;
/*  50: 50 */     int mOffset = 0;
/*  51: 51 */     float mOffsetPercent = 50.0F;
/*  52: 52 */     boolean mOffsetWithPadding = false;
/*  53:    */     
/*  54:    */ 
/*  55:    */ 
/*  56:    */     private boolean mAlignToBaseline;
/*  57:    */     
/*  58:    */ 
/*  59:    */ 
/*  60:    */     public final void setItemAlignmentOffset(int offset)
/*  61:    */     {
/*  62: 62 */       mOffset = offset;
/*  63:    */     }
/*  64:    */     
/*  65:    */ 
/*  66:    */ 
/*  67:    */ 
/*  68:    */ 
/*  69:    */ 
/*  70:    */ 
/*  71:    */     public final int getItemAlignmentOffset()
/*  72:    */     {
/*  73: 73 */       return mOffset;
/*  74:    */     }
/*  75:    */     
/*  76:    */ 
/*  77:    */ 
/*  78:    */ 
/*  79:    */ 
/*  80:    */ 
/*  81:    */ 
/*  82:    */ 
/*  83:    */ 
/*  84:    */ 
/*  85:    */     public final void setItemAlignmentOffsetWithPadding(boolean withPadding)
/*  86:    */     {
/*  87: 87 */       mOffsetWithPadding = withPadding;
/*  88:    */     }
/*  89:    */     
/*  90:    */ 
/*  91:    */ 
/*  92:    */ 
/*  93:    */ 
/*  94:    */ 
/*  95:    */ 
/*  96:    */ 
/*  97:    */ 
/*  98:    */ 
/*  99:    */     public final boolean isItemAlignmentOffsetWithPadding()
/* 100:    */     {
/* 101:101 */       return mOffsetWithPadding;
/* 102:    */     }
/* 103:    */     
/* 104:    */ 
/* 105:    */ 
/* 106:    */ 
/* 107:    */ 
/* 108:    */     public final void setItemAlignmentOffsetPercent(float percent)
/* 109:    */     {
/* 110:110 */       if (((percent < 0.0F) || (percent > 100.0F)) && (percent != -1.0F))
/* 111:    */       {
/* 112:112 */         throw new IllegalArgumentException();
/* 113:    */       }
/* 114:114 */       mOffsetPercent = percent;
/* 115:    */     }
/* 116:    */     
/* 117:    */ 
/* 118:    */ 
/* 119:    */ 
/* 120:    */ 
/* 121:    */     public final float getItemAlignmentOffsetPercent()
/* 122:    */     {
/* 123:123 */       return mOffsetPercent;
/* 124:    */     }
/* 125:    */     
/* 126:    */ 
/* 127:    */ 
/* 128:    */ 
/* 129:    */ 
/* 130:    */ 
/* 131:    */ 
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:    */     public final void setItemAlignmentViewId(int viewId)
/* 137:    */     {
/* 138:138 */       mViewId = viewId;
/* 139:    */     }
/* 140:    */     
/* 141:    */ 
/* 142:    */ 
/* 143:    */ 
/* 144:    */ 
/* 145:    */ 
/* 146:    */ 
/* 147:    */ 
/* 148:    */ 
/* 149:    */ 
/* 150:    */     public final int getItemAlignmentViewId()
/* 151:    */     {
/* 152:152 */       return mViewId;
/* 153:    */     }
/* 154:    */     
/* 155:    */ 
/* 156:    */ 
/* 157:    */ 
/* 158:    */ 
/* 159:    */ 
/* 160:    */     public final void setItemAlignmentFocusViewId(int viewId)
/* 161:    */     {
/* 162:162 */       mFocusViewId = viewId;
/* 163:    */     }
/* 164:    */     
/* 165:    */ 
/* 166:    */ 
/* 167:    */ 
/* 168:    */     public final int getItemAlignmentFocusViewId()
/* 169:    */     {
/* 170:170 */       return mFocusViewId != -1 ? mFocusViewId : mViewId;
/* 171:    */     }
/* 172:    */     
/* 173:    */ 
/* 174:    */ 
/* 175:    */ 
/* 176:    */ 
/* 177:    */ 
/* 178:    */     public final void setAlignedToTextViewBaseline(boolean alignToBaseline)
/* 179:    */     {
/* 180:180 */       mAlignToBaseline = alignToBaseline;
/* 181:    */     }
/* 182:    */     
/* 183:    */ 
/* 184:    */ 
/* 185:    */     public boolean isAlignedToTextViewBaseLine()
/* 186:    */     {
/* 187:187 */       return mAlignToBaseline;
/* 188:    */     }
/* 189:    */   }
/* 190:    */   
/* 191:191 */   private ItemAlignmentDef[] mAlignmentDefs = { new ItemAlignmentDef() };
/* 192:    */   
/* 193:    */   public boolean isMultiAlignment() {
/* 194:194 */     return mAlignmentDefs.length > 1;
/* 195:    */   }
/* 196:    */   
/* 197:    */ 
/* 198:    */ 
/* 199:    */ 
/* 200:    */   public void setAlignmentDefs(@NonNull ItemAlignmentDef[] defs)
/* 201:    */   {
/* 202:202 */     if ((defs == null) || (defs.length < 1)) {
/* 203:203 */       throw new IllegalArgumentException();
/* 204:    */     }
/* 205:205 */     mAlignmentDefs = defs;
/* 206:    */   }
/* 207:    */   
/* 208:    */ 
/* 209:    */ 
/* 210:    */ 
/* 211:    */   @NonNull
/* 212:    */   public ItemAlignmentDef[] getAlignmentDefs()
/* 213:    */   {
/* 214:214 */     return mAlignmentDefs;
/* 215:    */   }
/* 216:    */ }


/* Location:           D:\Android\Huan_Component\androidx_aar_leanback\repo\classes.jar
 * Qualified Name:     androidx.leanback.widget.ItemAlignmentFacet
 * JD-Core Version:    0.7.0.1
 */