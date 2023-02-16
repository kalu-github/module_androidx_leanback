package lib.kalu.leanback.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.leanback.tab.model.TabModel;

@SuppressLint("AppCompatCustomView")
class TabImageView extends ImageView {

    private int mHeight = 0;
    private int mWidthMax = 0;
    private int mWidthMin = 0;
    private TabModel mTabModel;

    public TabImageView(@NonNull Context context, @NonNull TabModel data) {
        super(context);
        this.mTabModel = data;
        init();
    }

    private void init() {
        setEnabled(false);
        setSelected(false);
        setFocusable(false);
        setScaleType(ScaleType.FIT_CENTER);
        setPadding(0, 0, 0, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = MeasureSpec.getSize(heightMeasureSpec);

        try {
            Drawable drawable = getDrawable();
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            TabUtil.logE("TabImageView => intrinsicWidth = " + intrinsicWidth + ", intrinsicHeight =" + intrinsicHeight);
            width = height * intrinsicWidth / intrinsicHeight;
        } catch (Exception e) {
        }

        if (mWidthMin > 0 && width < mWidthMin) {
            width = mWidthMin;
        } else if (mWidthMax > 0 && width > mWidthMax) {
            width = mWidthMax;
        }

        TabUtil.logE("TabImageView => width = " + width + ", height =" + height);
        setMeasuredDimension(width, height);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

//        int width = getWidth();
//        int height = getHeight();
//
//        // 原始区域
//        Paint paint1 = new Paint();
//        paint1.setColor(Color.RED);
//        Rect rect1 = new Rect(0, 0, width, height);
//        canvas.drawRect(rect1, paint1);
//
//        // 目标区域
//        Paint paint2 = new Paint();
//        paint1.setColor(Color.BLACK);
//        Rect rect2 = new Rect(width / 10, height / 10, width - width / 10, height - height / 10);
//        canvas.drawRect(rect2, paint2);

        super.onDraw(canvas);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {

        try {
//
//            // 1
//            float rectW = getWidth();
//            float rectH = getHeight();
//
//            // 2
//            float imgW = drawable.getIntrinsicWidth();
//            float imgH = drawable.getIntrinsicHeight();
//            if (imgW > rectW) {
//                imgW = rectW;
//                rectH = (rectH / rectW) * imgW;
//            } else if (imgH > rectH) {
//                imgH = rectH;
//                imgW = (rectW / rectH) * imgH;
//            }
//
//            // 3
//            float bitmapH = rectH / 10 * 8;
//            float bitmapW = (imgW / imgH) * bitmapH;
////            if (imgW > rectW) {
////                bitmapW = rectW / 10 * 8;
////                bitmapH = (imgH / imgW) * bitmapW;
////            } else if (imgH > rectH) {
////                bitmapH = rectH / 10 * 8;
////                bitmapW = (imgW / imgH) * bitmapH;
////            } else {
////                bitmapH = rectH / 10 * 8;
////                bitmapW = (imgW / imgH) * bitmapH;
////            }
//
//            // 4
//            Bitmap bitmap = Bitmap.createBitmap((int) rectW, (int) rectH, Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(bitmap);
////            TabUtil.logE("IMGsetImageDrawable => paddingLeft = " + paddingLeft + ", paddingRight =" + paddingRight);
////            TabUtil.logE("IMGsetImageDrawable => imgW = " + imgW + ", imgH =" + imgH);
////            TabUtil.logE("IMGsetImageDrawable => tabWidth = " + tabWidth + ", tabHeight =" + tabHeight);
////            TabUtil.logE("IMGsetImageDrawable => canvasWidth = " + canvasWidth + ", canvasHeight =" + canvasHeight);
//            Rect src = new Rect((int) ((rectW - imgW) / 2), (int) ((rectH - imgH) / 2), (int) imgW, (int) imgH);
//            RectF dst = new RectF((rectW - bitmapW) / 2, (rectH - bitmapH) / 2, bitmapW, bitmapH);
//            Bitmap temp = ((BitmapDrawable) drawable).getBitmap();
//            canvas.drawBitmap(temp, src, dst, null);
//            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
//            if (null != temp) {
//                temp.recycle();
//            }
//            super.setImageDrawable(bitmapDrawable);
            super.setImageDrawable(drawable);
        } catch (Exception e) {
        }
    }

    protected void setHeight(int height) {
        this.mHeight = height;
    }

    protected void setWidthMin(int min) {
        this.mWidthMin = min;
    }

    protected void setWidthMax(int max) {
        this.mWidthMin = max;
    }

    protected void setChecked(boolean v) {
        setSelected(v);
    }

    protected boolean isChecked() {
        return isSelected();
    }

    protected void setFocus(boolean v) {
        setEnabled(v);
    }

    protected boolean isFocus() {
        return hasFocus();
    }

    @Override
    public boolean hasFocus() {
        return isEnabled();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        refreshUI();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        refreshUI();
    }

    private void refreshUI() {
        boolean focus = isFocus();
        boolean checked = isChecked();
        refreshDrawable(focus, checked);
        refreshBackground(focus, checked);
    }

    // text-color 优先级 ：resource > color
    private void refreshDrawable(boolean focus, boolean checked) {

        try {
            int placeholder = mTabModel.getImagePlaceholderResource();
            if (placeholder != 0) {
                setImageResource(placeholder);
            }
        } catch (Exception e) {
        }

        try {

            String s;
            // focus
            if (focus) {
                s = mTabModel.getImageUrlFocus();

            }
            // checked
            else if (checked) {
                s = mTabModel.getImageUrlChecked();
            }
            // normal
            else {
                s = t.getImageUrlNormal();
            }
            if (null != s && s.length() > 0) {
                loadImageUrl(view, s, false);
            }
        } catch (Exception e) {
        }
    }

    // 优先级 ：net > path > assets > resource > color
    private void refreshBackground(boolean focus, boolean checked) {

        try {

            String s1 = mTabModel.getBackgroundImageUrl(focus, checked);
//            logE("updateImageBackground => url = " + s1);

            String s2 = mTabModel.getBackgroundImagePath(focus, checked);
//            logE("updateImageBackground => path = " + s2);

            String s3 = mTabModel.getBackgroundImageAssets(focus, checked);
//            logE("updateImageBackground => assets = " + s3);

            int i4 = mTabModel.getBackgroundResource(focus, checked);
//            logE("updateImageBackground => resource = " + i4);

            int i5 = mTabModel.getBackgroundColor(focus, checked);
//            logE("updateImageBackground => color = " + i5);

//        // 背景 => 渐变背景色
//        if (null != colors && colors.length >= 3) {
//            int[] color = stay ? colors[2] : (focus ? colors[1] : colors[0]);
//            logE("updateImageBackground[colors]=> color = " + Arrays.toString(color));
//            setBackgroundGradient(view, color, radius);
//        }

            if (null != s1 && s1.length() > 0) {
                TabUtil.loadImageUrl(this, s1, true);
            } else if (null != s2 && s2.length() > 0) {
                Drawable drawable = TabUtil.decodeDrawable(getContext(), s2, false);
                setBackground(drawable);
            } else if (null != s3 && s3.length() > 0) {
                Drawable drawable = TabUtil.decodeDrawable(getContext(), s2, true);
                setBackground(drawable);
            } else if (i4 != 0) {
                setBackgroundResource(i4);
            } else if (i5 != 0) {
                ColorDrawable drawable = new ColorDrawable(i5);
                setBackground(drawable);
            }
        } catch (Exception e) {
        }
    }
}