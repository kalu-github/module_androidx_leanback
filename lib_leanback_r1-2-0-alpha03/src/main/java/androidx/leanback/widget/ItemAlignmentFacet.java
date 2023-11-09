//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package androidx.leanback.widget;

import androidx.annotation.NonNull;

public final class ItemAlignmentFacet {
    public static final float ITEM_ALIGN_OFFSET_PERCENT_DISABLED = -1.0F;
    private ItemAlignmentDef[] mAlignmentDefs = new ItemAlignmentDef[]{new ItemAlignmentDef()};

    public ItemAlignmentFacet() {
    }

    public boolean isMultiAlignment() {
        return this.mAlignmentDefs.length > 1;
    }

    public void setAlignmentDefs(@NonNull ItemAlignmentDef[] defs) {
        if (defs != null && defs.length >= 1) {
            this.mAlignmentDefs = defs;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @NonNull
    public ItemAlignmentDef[] getAlignmentDefs() {
        return this.mAlignmentDefs;
    }

    public static class ItemAlignmentDef {
        int mViewId = -1;
        int mFocusViewId = -1;
        int mOffset = 0;
        float mOffsetPercent = 50.0F;
        boolean mOffsetWithPadding = false;
        private boolean mAlignToBaseline;

        public ItemAlignmentDef() {
        }

        public final void setItemAlignmentOffset(int offset) {
            this.mOffset = offset;
        }

        public final int getItemAlignmentOffset() {
            return this.mOffset;
        }

        public final void setItemAlignmentOffsetWithPadding(boolean withPadding) {
            this.mOffsetWithPadding = withPadding;
        }

        public final boolean isItemAlignmentOffsetWithPadding() {
            return this.mOffsetWithPadding;
        }

        public final void setItemAlignmentOffsetPercent(float percent) {
            if ((percent < 0.0F || percent > 100.0F) && percent != -1.0F) {
                throw new IllegalArgumentException();
            } else {
                this.mOffsetPercent = percent;
            }
        }

        public final float getItemAlignmentOffsetPercent() {
            return this.mOffsetPercent;
        }

        public final void setItemAlignmentViewId(int viewId) {
            this.mViewId = viewId;
        }

        public final int getItemAlignmentViewId() {
            return this.mViewId;
        }

        public final void setItemAlignmentFocusViewId(int viewId) {
            this.mFocusViewId = viewId;
        }

        public final int getItemAlignmentFocusViewId() {
            return this.mFocusViewId != -1 ? this.mFocusViewId : this.mViewId;
        }

        public final void setAlignedToTextViewBaseline(boolean alignToBaseline) {
            this.mAlignToBaseline = alignToBaseline;
        }

        public boolean isAlignedToTextViewBaseLine() {
            return this.mAlignToBaseline;
        }
    }
}
