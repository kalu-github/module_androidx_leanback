//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package androidx.leanback.widget;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import androidx.collection.LruCache;
import java.util.Iterator;
import java.util.Map;

final class ViewsStateBundle {
    private static final int LIMIT_DEFAULT = 100;
    private static final int UNLIMITED = Integer.MAX_VALUE;
    private int mSavePolicy = 0;
    private int mLimitNumber = 100;
    private LruCache<String, SparseArray<Parcelable>> mChildStates;

    ViewsStateBundle() {
    }

    void clear() {
        if (this.mChildStates != null) {
            this.mChildStates.evictAll();
        }

    }

    void remove(int id) {
        if (this.mChildStates != null && this.mChildStates.size() != 0) {
            this.mChildStates.remove(getSaveStatesKey(id));
        }

    }

    Bundle saveAsBundle() {
        if (this.mChildStates != null && this.mChildStates.size() != 0) {
            Map<String, SparseArray<Parcelable>> snapshot = this.mChildStates.snapshot();
            Bundle bundle = new Bundle();
            Iterator<Map.Entry<String, SparseArray<Parcelable>>> i = snapshot.entrySet().iterator();

            while(i.hasNext()) {
                Map.Entry<String, SparseArray<Parcelable>> e = (Map.Entry)i.next();
                bundle.putSparseParcelableArray((String)e.getKey(), (SparseArray)e.getValue());
            }

            return bundle;
        } else {
            return null;
        }
    }

    void loadFromBundle(Bundle savedBundle) {
        if (this.mChildStates != null && savedBundle != null) {
            this.mChildStates.evictAll();
            Iterator<String> i = savedBundle.keySet().iterator();

            while(i.hasNext()) {
                String key = (String)i.next();
                this.mChildStates.put(key, savedBundle.getSparseParcelableArray(key));
            }
        }

    }

    int getSavePolicy() {
        return this.mSavePolicy;
    }

    int getLimitNumber() {
        return this.mLimitNumber;
    }

    void setSavePolicy(int savePolicy) {
        this.mSavePolicy = savePolicy;
        this.applyPolicyChanges();
    }

    void setLimitNumber(int limitNumber) {
        this.mLimitNumber = limitNumber;
        this.applyPolicyChanges();
    }

    void loadView(View view, int id) {
        if (this.mChildStates != null) {
            String key = getSaveStatesKey(id);
            SparseArray<Parcelable> container = (SparseArray)this.mChildStates.remove(key);
            if (container != null) {
                view.restoreHierarchyState(container);
            }
        }

    }

    Bundle saveOnScreenView(Bundle bundle, View view, int id) {
        if (this.mSavePolicy != 0) {
            String key = getSaveStatesKey(id);
            SparseArray<Parcelable> container = new SparseArray();
            view.saveHierarchyState(container);
            if (bundle == null) {
                bundle = new Bundle();
            }

            bundle.putSparseParcelableArray(key, container);
        }

        return bundle;
    }

    void saveOffscreenView(View view, int id) {
        switch (this.mSavePolicy) {
            case 1:
                this.remove(id);
                break;
            case 2:
            case 3:
                this.saveViewUnchecked(view, id);
        }

    }

    private void applyPolicyChanges() {
        if (this.mSavePolicy == 2) {
            if (this.mLimitNumber <= 0) {
                throw new IllegalArgumentException();
            }

            if (this.mChildStates == null || this.mChildStates.maxSize() != this.mLimitNumber) {
                this.mChildStates = new LruCache(this.mLimitNumber);
            }
        } else if (this.mSavePolicy != 3 && this.mSavePolicy != 1) {
            this.mChildStates = null;
        } else if (this.mChildStates == null || this.mChildStates.maxSize() != Integer.MAX_VALUE) {
            this.mChildStates = new LruCache(Integer.MAX_VALUE);
        }

    }

    private void saveViewUnchecked(View view, int id) {
        if (this.mChildStates != null) {
            String key = getSaveStatesKey(id);
            SparseArray<Parcelable> container = new SparseArray();
            view.saveHierarchyState(container);
            this.mChildStates.put(key, container);
        }

    }

    static String getSaveStatesKey(int id) {
        return Integer.toString(id);
    }
}
