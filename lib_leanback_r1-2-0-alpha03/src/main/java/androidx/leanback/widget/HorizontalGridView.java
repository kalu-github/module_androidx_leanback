//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.leanback.widget.R.styleable;

public class HorizontalGridView extends BaseGridView {
    private boolean mFadingLowEdge;
    private boolean mFadingHighEdge;
    private Paint mTempPaint;
    private Bitmap mTempBitmapLow;
    private LinearGradient mLowFadeShader;
    private int mLowFadeShaderLength;
    private int mLowFadeShaderOffset;
    private Bitmap mTempBitmapHigh;
    private LinearGradient mHighFadeShader;
    private int mHighFadeShaderLength;
    private int mHighFadeShaderOffset;
    private final Rect mTempRect;

    public HorizontalGridView(@NonNull Context context) {
        this(context, (AttributeSet)null);
    }

    public HorizontalGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mTempPaint = new Paint();
        this.mTempRect = new Rect();
        this.mLayoutManager.setOrientation(0);
        this.initAttributes(context, attrs);
    }

    @SuppressLint({"CustomViewStyleable"})
    protected void initAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
        this.initBaseGridViewAttributes(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, styleable.lbHorizontalGridView);
        ViewCompat.saveAttributeDataForStyleable(this, context, styleable.lbHorizontalGridView, attrs, a, 0, 0);
        this.setRowHeight(a);
        this.setNumRows(a.getInt(styleable.lbHorizontalGridView_numberOfRows, 1));
        a.recycle();
        this.updateLayerType();
        this.mTempPaint = new Paint();
        this.mTempPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
    }

    void setRowHeight(TypedArray array) {
        TypedValue typedValue = array.peekValue(styleable.lbHorizontalGridView_rowHeight);
        if (typedValue != null) {
            int size = array.getLayoutDimension(styleable.lbHorizontalGridView_rowHeight, 0);
            this.setRowHeight(size);
        }

    }

    public void setNumRows(int numRows) {
        this.mLayoutManager.setNumRows(numRows);
        this.requestLayout();
    }

    public void setRowHeight(int height) {
        this.mLayoutManager.setRowHeight(height);
        this.requestLayout();
    }

    public final void setFadingLeftEdge(boolean fading) {
        if (this.mFadingLowEdge != fading) {
            this.mFadingLowEdge = fading;
            if (!this.mFadingLowEdge) {
                this.mTempBitmapLow = null;
            }

            this.invalidate();
            this.updateLayerType();
        }

    }

    @SuppressLint({"GetterSetterNames"})
    public final boolean getFadingLeftEdge() {
        return this.mFadingLowEdge;
    }

    public final void setFadingLeftEdgeLength(int fadeLength) {
        if (this.mLowFadeShaderLength != fadeLength) {
            this.mLowFadeShaderLength = fadeLength;
            if (this.mLowFadeShaderLength != 0) {
                this.mLowFadeShader = new LinearGradient(0.0F, 0.0F, (float)this.mLowFadeShaderLength, 0.0F, 0, -16777216, TileMode.CLAMP);
            } else {
                this.mLowFadeShader = null;
            }

            this.invalidate();
        }

    }

    public final int getFadingLeftEdgeLength() {
        return this.mLowFadeShaderLength;
    }

    public final void setFadingLeftEdgeOffset(int fadeOffset) {
        if (this.mLowFadeShaderOffset != fadeOffset) {
            this.mLowFadeShaderOffset = fadeOffset;
            this.invalidate();
        }

    }

    public final int getFadingLeftEdgeOffset() {
        return this.mLowFadeShaderOffset;
    }

    public final void setFadingRightEdge(boolean fading) {
        if (this.mFadingHighEdge != fading) {
            this.mFadingHighEdge = fading;
            if (!this.mFadingHighEdge) {
                this.mTempBitmapHigh = null;
            }

            this.invalidate();
            this.updateLayerType();
        }

    }

    @SuppressLint({"GetterSetterNames"})
    public final boolean getFadingRightEdge() {
        return this.mFadingHighEdge;
    }

    public final void setFadingRightEdgeLength(int fadeLength) {
        if (this.mHighFadeShaderLength != fadeLength) {
            this.mHighFadeShaderLength = fadeLength;
            if (this.mHighFadeShaderLength != 0) {
                this.mHighFadeShader = new LinearGradient(0.0F, 0.0F, (float)this.mHighFadeShaderLength, 0.0F, -16777216, 0, TileMode.CLAMP);
            } else {
                this.mHighFadeShader = null;
            }

            this.invalidate();
        }

    }

    public final int getFadingRightEdgeLength() {
        return this.mHighFadeShaderLength;
    }

    public final void setFadingRightEdgeOffset(int fadeOffset) {
        if (this.mHighFadeShaderOffset != fadeOffset) {
            this.mHighFadeShaderOffset = fadeOffset;
            this.invalidate();
        }

    }

    public final int getFadingRightEdgeOffset() {
        return this.mHighFadeShaderOffset;
    }

    private boolean needsFadingLowEdge() {
        if (!this.mFadingLowEdge) {
            return false;
        } else {
            int c = this.getChildCount();

            for(int i = 0; i < c; ++i) {
                View view = this.getChildAt(i);
                if (this.mLayoutManager.getOpticalLeft(view) < this.getPaddingLeft() - this.mLowFadeShaderOffset) {
                    return true;
                }
            }

            return false;
        }
    }

    private boolean needsFadingHighEdge() {
        if (!this.mFadingHighEdge) {
            return false;
        } else {
            int c = this.getChildCount();

            for(int i = c - 1; i >= 0; --i) {
                View view = this.getChildAt(i);
                if (this.mLayoutManager.getOpticalRight(view) > this.getWidth() - this.getPaddingRight() + this.mHighFadeShaderOffset) {
                    return true;
                }
            }

            return false;
        }
    }

    private Bitmap getTempBitmapLow() {
        if (this.mTempBitmapLow == null || this.mTempBitmapLow.getWidth() != this.mLowFadeShaderLength || this.mTempBitmapLow.getHeight() != this.getHeight()) {
            this.mTempBitmapLow = Bitmap.createBitmap(this.mLowFadeShaderLength, this.getHeight(), Config.ARGB_8888);
        }

        return this.mTempBitmapLow;
    }

    private Bitmap getTempBitmapHigh() {
        if (this.mTempBitmapHigh == null || this.mTempBitmapHigh.getWidth() != this.mHighFadeShaderLength || this.mTempBitmapHigh.getHeight() != this.getHeight()) {
            this.mTempBitmapHigh = Bitmap.createBitmap(this.mHighFadeShaderLength, this.getHeight(), Config.ARGB_8888);
        }

        return this.mTempBitmapHigh;
    }

    public void draw(@NonNull Canvas canvas) {
        boolean needsFadingLow = this.needsFadingLowEdge();
        boolean needsFadingHigh = this.needsFadingHighEdge();
        if (!needsFadingLow) {
            this.mTempBitmapLow = null;
        }

        if (!needsFadingHigh) {
            this.mTempBitmapHigh = null;
        }

        if (!needsFadingLow && !needsFadingHigh) {
            super.draw(canvas);
        } else {
            int lowEdge = this.mFadingLowEdge ? this.getPaddingLeft() - this.mLowFadeShaderOffset - this.mLowFadeShaderLength : 0;
            int highEdge = this.mFadingHighEdge ? this.getWidth() - this.getPaddingRight() + this.mHighFadeShaderOffset + this.mHighFadeShaderLength : this.getWidth();
            int save = canvas.save();
            canvas.clipRect(lowEdge + (this.mFadingLowEdge ? this.mLowFadeShaderLength : 0), 0, highEdge - (this.mFadingHighEdge ? this.mHighFadeShaderLength : 0), this.getHeight());
            super.draw(canvas);
            canvas.restoreToCount(save);
            Canvas tmpCanvas = new Canvas();
            this.mTempRect.top = 0;
            this.mTempRect.bottom = this.getHeight();
            Bitmap tempBitmap;
            int tmpSave;
            if (needsFadingLow && this.mLowFadeShaderLength > 0) {
                tempBitmap = this.getTempBitmapLow();
                tempBitmap.eraseColor(0);
                tmpCanvas.setBitmap(tempBitmap);
                tmpSave = tmpCanvas.save();
                tmpCanvas.clipRect(0, 0, this.mLowFadeShaderLength, this.getHeight());
                tmpCanvas.translate((float)(-lowEdge), 0.0F);
                super.draw(tmpCanvas);
                tmpCanvas.restoreToCount(tmpSave);
                this.mTempPaint.setShader(this.mLowFadeShader);
                tmpCanvas.drawRect(0.0F, 0.0F, (float)this.mLowFadeShaderLength, (float)this.getHeight(), this.mTempPaint);
                this.mTempRect.left = 0;
                this.mTempRect.right = this.mLowFadeShaderLength;
                canvas.translate((float)lowEdge, 0.0F);
                canvas.drawBitmap(tempBitmap, this.mTempRect, this.mTempRect, (Paint)null);
                canvas.translate((float)(-lowEdge), 0.0F);
            }

            if (needsFadingHigh && this.mHighFadeShaderLength > 0) {
                tempBitmap = this.getTempBitmapHigh();
                tempBitmap.eraseColor(0);
                tmpCanvas.setBitmap(tempBitmap);
                tmpSave = tmpCanvas.save();
                tmpCanvas.clipRect(0, 0, this.mHighFadeShaderLength, this.getHeight());
                tmpCanvas.translate((float)(-(highEdge - this.mHighFadeShaderLength)), 0.0F);
                super.draw(tmpCanvas);
                tmpCanvas.restoreToCount(tmpSave);
                this.mTempPaint.setShader(this.mHighFadeShader);
                tmpCanvas.drawRect(0.0F, 0.0F, (float)this.mHighFadeShaderLength, (float)this.getHeight(), this.mTempPaint);
                this.mTempRect.left = 0;
                this.mTempRect.right = this.mHighFadeShaderLength;
                canvas.translate((float)(highEdge - this.mHighFadeShaderLength), 0.0F);
                canvas.drawBitmap(tempBitmap, this.mTempRect, this.mTempRect, (Paint)null);
                canvas.translate((float)(-(highEdge - this.mHighFadeShaderLength)), 0.0F);
            }

        }
    }

    private void updateLayerType() {
        if (!this.mFadingLowEdge && !this.mFadingHighEdge) {
            this.setLayerType(0, (Paint)null);
            this.setWillNotDraw(true);
        } else {
            this.setLayerType(2, (Paint)null);
            this.setWillNotDraw(false);
        }

    }
}
