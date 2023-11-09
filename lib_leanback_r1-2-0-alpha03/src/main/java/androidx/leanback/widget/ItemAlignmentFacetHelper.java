//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package androidx.leanback.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

class ItemAlignmentFacetHelper {
    private static final Rect sRect = new Rect();

    static int getAlignmentPosition(View itemView, ItemAlignmentFacet.ItemAlignmentDef facet, int orientation) {
        GridLayoutManager.LayoutParams p = (GridLayoutManager.LayoutParams)itemView.getLayoutParams();
        View view = itemView;
        if (facet.mViewId != 0) {
            view = itemView.findViewById(facet.mViewId);
            if (view == null) {
                view = itemView;
            }
        }

        int alignPos = facet.mOffset;
        if (orientation == 0) {
            if (itemView.getLayoutDirection() == 1) {
                alignPos = (view == itemView ? p.getOpticalWidth(view) : view.getWidth()) - alignPos;
                if (facet.mOffsetWithPadding) {
                    if (facet.mOffsetPercent == 0.0F) {
                        alignPos -= view.getPaddingRight();
                    } else if (facet.mOffsetPercent == 100.0F) {
                        alignPos += view.getPaddingLeft();
                    }
                }

                if (facet.mOffsetPercent != -1.0F) {
                    alignPos -= (int)((float)(view == itemView ? p.getOpticalWidth(view) : view.getWidth()) * facet.mOffsetPercent / 100.0F);
                }

                if (itemView != view) {
                    sRect.right = alignPos;
                    ((ViewGroup)itemView).offsetDescendantRectToMyCoords(view, sRect);
                    alignPos = sRect.right + p.getOpticalRightInset();
                }
            } else {
                if (facet.mOffsetWithPadding) {
                    if (facet.mOffsetPercent == 0.0F) {
                        alignPos += view.getPaddingLeft();
                    } else if (facet.mOffsetPercent == 100.0F) {
                        alignPos -= view.getPaddingRight();
                    }
                }

                if (facet.mOffsetPercent != -1.0F) {
                    alignPos += (int)((float)(view == itemView ? p.getOpticalWidth(view) : view.getWidth()) * facet.mOffsetPercent / 100.0F);
                }

                if (itemView != view) {
                    sRect.left = alignPos;
                    ((ViewGroup)itemView).offsetDescendantRectToMyCoords(view, sRect);
                    alignPos = sRect.left - p.getOpticalLeftInset();
                }
            }
        } else {
            if (facet.mOffsetWithPadding) {
                if (facet.mOffsetPercent == 0.0F) {
                    alignPos += view.getPaddingTop();
                } else if (facet.mOffsetPercent == 100.0F) {
                    alignPos -= view.getPaddingBottom();
                }
            }

            if (facet.mOffsetPercent != -1.0F) {
                alignPos += (int)((float)(view == itemView ? p.getOpticalHeight(view) : view.getHeight()) * facet.mOffsetPercent / 100.0F);
            }

            if (itemView != view) {
                sRect.top = alignPos;
                ((ViewGroup)itemView).offsetDescendantRectToMyCoords(view, sRect);
                alignPos = sRect.top - p.getOpticalTopInset();
            }

            if (facet.isAlignedToTextViewBaseLine()) {
                alignPos += view.getBaseline();
            }
        }

        return alignPos;
    }

    private ItemAlignmentFacetHelper() {
    }
}
