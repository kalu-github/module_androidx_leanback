//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package androidx.leanback.widget;

import android.view.View;

class ItemAlignment {
    private int mOrientation = 0;
    public final Axis vertical = new Axis(1);
    public final Axis horizontal = new Axis(0);
    private Axis mMainAxis;
    private Axis mSecondAxis;

    ItemAlignment() {
        this.mMainAxis = this.horizontal;
        this.mSecondAxis = this.vertical;
    }

    public final Axis mainAxis() {
        return this.mMainAxis;
    }

    public final Axis secondAxis() {
        return this.mSecondAxis;
    }

    public final void setOrientation(int orientation) {
        this.mOrientation = orientation;
        if (this.mOrientation == 0) {
            this.mMainAxis = this.horizontal;
            this.mSecondAxis = this.vertical;
        } else {
            this.mMainAxis = this.vertical;
            this.mSecondAxis = this.horizontal;
        }

    }

    public final int getOrientation() {
        return this.mOrientation;
    }

    static final class Axis extends ItemAlignmentFacet.ItemAlignmentDef {
        private final int mOrientation;

        Axis(int orientation) {
            this.mOrientation = orientation;
        }

        public int getAlignmentPosition(View itemView) {
            return ItemAlignmentFacetHelper.getAlignmentPosition(itemView, this, this.mOrientation);
        }
    }
}
