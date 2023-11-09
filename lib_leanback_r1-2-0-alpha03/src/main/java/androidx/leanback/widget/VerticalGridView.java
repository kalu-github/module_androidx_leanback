//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.leanback.widget.R.styleable;

public class VerticalGridView extends BaseGridView {
    public VerticalGridView(@NonNull Context context) {
        this(context, (AttributeSet)null);
    }

    public VerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mLayoutManager.setOrientation(1);
        this.initAttributes(context, attrs);
    }

    @SuppressLint({"CustomViewStyleable"})
    protected void initAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
        this.initBaseGridViewAttributes(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, styleable.lbVerticalGridView);
        ViewCompat.saveAttributeDataForStyleable(this, context, styleable.lbVerticalGridView, attrs, a, 0, 0);
        this.setColumnWidth(a);
        this.setNumColumns(a.getInt(styleable.lbVerticalGridView_numberOfColumns, 1));
        a.recycle();
    }

    void setColumnWidth(TypedArray array) {
        TypedValue typedValue = array.peekValue(styleable.lbVerticalGridView_columnWidth);
        if (typedValue != null) {
            int size = array.getLayoutDimension(styleable.lbVerticalGridView_columnWidth, 0);
            this.setColumnWidth(size);
        }

    }

    public void setNumColumns(int numColumns) {
        this.mLayoutManager.setNumRows(numColumns);
        this.requestLayout();
    }

    public void setColumnWidth(int width) {
        this.mLayoutManager.setRowHeight(width);
        this.requestLayout();
    }
}
