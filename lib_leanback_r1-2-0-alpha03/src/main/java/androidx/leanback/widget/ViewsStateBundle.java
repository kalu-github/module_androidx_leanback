/*   1:    */ package androidx.leanback.widget;
/*   2:    */ 
/*   3:    */ import android.os.Bundle;
/*   4:    */ import android.os.Parcelable;
/*   5:    */ import android.util.SparseArray;
/*   6:    */ import android.view.View;
/*   7:    */ import androidx.collection.LruCache;
/*   8:    */ import java.util.Iterator;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Map.Entry;
/*  11:    */ import java.util.Set;
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
/*  46:    */ final class ViewsStateBundle
/*  47:    */ {
/*  48:    */   private static final int LIMIT_DEFAULT = 100;
/*  49:    */   private static final int UNLIMITED = 2147483647;
/*  50:    */   private int mSavePolicy;
/*  51:    */   private int mLimitNumber;
/*  52:    */   private LruCache<String, SparseArray<Parcelable>> mChildStates;
/*  53:    */   
/*  54:    */   ViewsStateBundle()
/*  55:    */   {
/*  56: 56 */     mSavePolicy = 0;
/*  57: 57 */     mLimitNumber = 100;
/*  58:    */   }
/*  59:    */   
/*  60:    */   void clear() {
/*  61: 61 */     if (mChildStates != null) {
/*  62: 62 */       mChildStates.evictAll();
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   void remove(int id) {
/*  67: 67 */     if ((mChildStates != null) && (mChildStates.size() != 0)) {
/*  68: 68 */       mChildStates.remove(getSaveStatesKey(id));
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */ 
/*  73:    */ 
/*  74:    */   Bundle saveAsBundle()
/*  75:    */   {
/*  76: 76 */     if ((mChildStates == null) || (mChildStates.size() == 0)) {
/*  77: 77 */       return null;
/*  78:    */     }
/*  79: 79 */     Map<String, SparseArray<Parcelable>> snapshot = mChildStates.snapshot();
/*  80: 80 */     Bundle bundle = new Bundle();
/*  81:    */     
/*  82: 82 */     for (Iterator<Map.Entry<String, SparseArray<Parcelable>>> i = snapshot.entrySet().iterator(); i.hasNext();) {
/*  83: 83 */       Map.Entry<String, SparseArray<Parcelable>> e = (Map.Entry)i.next();
/*  84: 84 */       bundle.putSparseParcelableArray((String)e.getKey(), (SparseArray)e.getValue());
/*  85:    */     }
/*  86: 86 */     return bundle;
/*  87:    */   }
/*  88:    */   
/*  89:    */   void loadFromBundle(Bundle savedBundle) {
/*  90:    */     Iterator<String> i;
/*  91: 91 */     if ((mChildStates != null) && (savedBundle != null)) {
/*  92: 92 */       mChildStates.evictAll();
/*  93: 93 */       for (i = savedBundle.keySet().iterator(); i.hasNext();) {
/*  94: 94 */         String key = (String)i.next();
/*  95: 95 */         mChildStates.put(key, savedBundle.getSparseParcelableArray(key));
/*  96:    */       }
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */ 
/* 101:    */ 
/* 102:    */ 
/* 103:    */   int getSavePolicy()
/* 104:    */   {
/* 105:105 */     return mSavePolicy;
/* 106:    */   }
/* 107:    */   
/* 108:    */ 
/* 109:    */ 
/* 110:    */ 
/* 111:    */   int getLimitNumber()
/* 112:    */   {
/* 113:113 */     return mLimitNumber;
/* 114:    */   }
/* 115:    */   
/* 116:    */ 
/* 117:    */ 
/* 118:    */   void setSavePolicy(int savePolicy)
/* 119:    */   {
/* 120:120 */     mSavePolicy = savePolicy;
/* 121:121 */     applyPolicyChanges();
/* 122:    */   }
/* 123:    */   
/* 124:    */ 
/* 125:    */ 
/* 126:    */   void setLimitNumber(int limitNumber)
/* 127:    */   {
/* 128:128 */     mLimitNumber = limitNumber;
/* 129:129 */     applyPolicyChanges();
/* 130:    */   }
/* 131:    */   
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:    */ 
/* 137:    */   void loadView(View view, int id)
/* 138:    */   {
/* 139:139 */     if (mChildStates != null) {
/* 140:140 */       String key = getSaveStatesKey(id);
/* 141:    */       
/* 142:    */ 
/* 143:143 */       SparseArray<Parcelable> container = (SparseArray)mChildStates.remove(key);
/* 144:144 */       if (container != null) {
/* 145:145 */         view.restoreHierarchyState(container);
/* 146:    */       }
/* 147:    */     }
/* 148:    */   }
/* 149:    */   
/* 150:    */ 
/* 151:    */ 
/* 152:    */ 
/* 153:    */ 
/* 154:    */ 
/* 155:    */ 
/* 156:    */ 
/* 157:    */   Bundle saveOnScreenView(Bundle bundle, View view, int id)
/* 158:    */   {
/* 159:159 */     if (mSavePolicy != 0) {
/* 160:160 */       String key = getSaveStatesKey(id);
/* 161:161 */       SparseArray<Parcelable> container = new SparseArray();
/* 162:162 */       view.saveHierarchyState(container);
/* 163:163 */       if (bundle == null) {
/* 164:164 */         bundle = new Bundle();
/* 165:    */       }
/* 166:166 */       bundle.putSparseParcelableArray(key, container);
/* 167:    */     }
/* 168:168 */     return bundle;
/* 169:    */   }
/* 170:    */   
/* 171:    */ 
/* 172:    */ 
/* 173:    */ 
/* 174:    */ 
/* 175:    */ 
/* 176:    */   void saveOffscreenView(View view, int id)
/* 177:    */   {
/* 178:178 */     switch (mSavePolicy) {
/* 179:    */     case 2: 
/* 180:    */     case 3: 
/* 181:181 */       saveViewUnchecked(view, id);
/* 182:182 */       break;
/* 183:    */     case 1: 
/* 184:184 */       remove(id);
/* 185:185 */       break;
/* 186:    */     }
/* 187:    */     
/* 188:    */   }
/* 189:    */   
/* 190:    */   private void applyPolicyChanges()
/* 191:    */   {
/* 192:192 */     if (mSavePolicy == 2) {
/* 193:193 */       if (mLimitNumber <= 0) {
/* 194:194 */         throw new IllegalArgumentException();
/* 195:    */       }
/* 196:196 */       if ((mChildStates == null) || (mChildStates.maxSize() != mLimitNumber)) {
/* 197:197 */         mChildStates = new LruCache(mLimitNumber);
/* 198:    */       }
/* 199:199 */     } else if ((mSavePolicy == 3) || (mSavePolicy == 1)) {
/* 200:200 */       if ((mChildStates == null) || (mChildStates.maxSize() != 2147483647)) {
/* 201:201 */         mChildStates = new LruCache(2147483647);
/* 202:    */       }
/* 203:    */     } else {
/* 204:204 */       mChildStates = null;
/* 205:    */     }
/* 206:    */   }
/* 207:    */   
/* 208:    */ 
/* 209:    */ 
/* 210:    */ 
/* 211:    */ 
/* 212:    */ 
/* 213:    */   private void saveViewUnchecked(View view, int id)
/* 214:    */   {
/* 215:215 */     if (mChildStates != null) {
/* 216:216 */       String key = getSaveStatesKey(id);
/* 217:217 */       SparseArray<Parcelable> container = new SparseArray();
/* 218:218 */       view.saveHierarchyState(container);
/* 219:219 */       mChildStates.put(key, container);
/* 220:    */     }
/* 221:    */   }
/* 222:    */   
/* 223:    */   static String getSaveStatesKey(int id) {
/* 224:224 */     return Integer.toString(id);
/* 225:    */   }
/* 226:    */ }


/* Location:           D:\Android\Huan_Component\androidx_aar_leanback\repo\classes.jar
 * Qualified Name:     androidx.leanback.widget.ViewsStateBundle
 * JD-Core Version:    0.7.0.1
 */