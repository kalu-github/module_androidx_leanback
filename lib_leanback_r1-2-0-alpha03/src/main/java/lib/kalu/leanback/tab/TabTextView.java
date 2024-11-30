package lib.kalu.leanback.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.concurrent.Executors;

import lib.kalu.leanback.tab.model.TabModel;
import lib.kalu.leanback.tab.ninepatch.NinePatchChunk;
import lib.kalu.leanback.util.LeanBackUtil;

@SuppressLint("AppCompatCustomView")
class TabTextView extends TextView {

    private int mUnderlineColor = Color.TRANSPARENT;
    private int mUnderlineHeight = 0;
    private int mUnderlineWidth = 0;

    private TabModel mTabModel;
    private String downloadUrl = null;

    public TabTextView(@NonNull Context context, @NonNull TabModel data) {
        super(context);
        init(data);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getPaint().setFakeBoldText(false);
        super.onDraw(canvas);

        if (mUnderlineHeight <= 0f)
            return;

        boolean hasFocus = isHovered();
        if (hasFocus)
            return;

        boolean isChecked = isSelected();
        if (!isChecked)
            return;

        // 驻留文字下划线
        try {
            TextPaint textPaint = getPaint();
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            int measureTextHeight = (int) (fontMetrics.bottom - fontMetrics.top);
            int paintColor = textPaint.getColor();
            int strokeWidth = (int) textPaint.getStrokeWidth();
            int width = getWidth();
            int height = getHeight();
            int startX;
            int stopX;
            if (mUnderlineWidth <= 0) {
                int measureTextWidth = (int) textPaint.measureText(String.valueOf(getText()));
                startX = width / 2 - measureTextWidth / 2;
                stopX = startX + measureTextWidth;
            } else {
                startX = width / 2 - mUnderlineWidth / 2;
                stopX = startX + mUnderlineWidth;
            }
            int startY = height / 2 + measureTextHeight / 2 + mUnderlineHeight / 2;
            if (startY >= height) {
                startY = height - mUnderlineHeight / 2;
            }
            int stopY = startY;
            textPaint.setStrokeJoin(Paint.Join.ROUND);
            textPaint.setStrokeCap(Paint.Cap.ROUND);
            textPaint.setAntiAlias(true);
            textPaint.setStrokeWidth(mUnderlineHeight);
            if (mUnderlineColor == Color.TRANSPARENT) {
                mUnderlineColor = paintColor;
            }
            textPaint.setColor(mUnderlineColor);
            canvas.drawLine(startX, startY, stopX, stopY, textPaint);
            textPaint.setColor(paintColor);
            textPaint.setStrokeWidth(strokeWidth);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        CharSequence text = getText();

        int left = getPaddingLeft();
        int right = getPaddingRight();
        int measureText = (int) getPaint().measureText(String.valueOf(text));
        int width = measureText;

        if (left == 0 || right == 0) {
            int size = measureText / text.length();
            width += size * 1.5;
        } else {
            width += left;
            width += right;
        }
        setMeasuredDimension(width, height);
    }

    private void init(@NonNull TabModel data) {
        this.mTabModel = data;
        setSelected(false); // 选中
        setHovered(false); // 获焦
        setMaxLines(1);
        setLines(1);
        setMinEms(2);
        setGravity(Gravity.CENTER);
        refreshText();
        refreshTextColor(false, false);
        refreshBackground(false, false);
    }

    protected void setUnderlineColor(int color) {
        this.mUnderlineColor = color;
    }

    protected void setUnderlineWidth(int width) {
        this.mUnderlineWidth = width;
    }

    protected void setUnderlineHeight(int height) {
        this.mUnderlineHeight = height;
    }


    /*************************/

    public void refreshUI() {
        boolean hasFocus = isHovered();
        boolean isChecked = isSelected();
        refreshText();
        refreshTextColor(hasFocus, isChecked);
        refreshBackground(hasFocus, isChecked);
    }

    private void refreshText() {
        try {
            String text = mTabModel.getText();
            if (null != text && text.length() >= 0) {
                setText(text);
            }
        } catch (Exception e) {
            LeanBackUtil.log("TabTextView => refreshText => " + e.getMessage());
        }
    }

    // text-color 优先级 ：resource > color
    private void refreshTextColor(boolean focus, boolean checked) {

        try {
            @ColorRes int c1 = mTabModel.getTextColorResource(focus, checked);
            if (c1 != 0) {
                setTextColor(getResources().getColor(c1));
            } else {
                @ColorInt int c2 = mTabModel.getTextColor(focus, checked);
                if (c2 != 0) {
                    setTextColor(c2);
                }
            }
        } catch (Exception e) {
            LeanBackUtil.log("TabTextView => refreshTextColor => " + e.getMessage());
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
                show(s1);
            } else if (null != s2 && s2.length() > 0) {
                Drawable drawable = decodeDrawable(getContext(), s2, false);
                setBackgroundDrawable(drawable);
            } else if (null != s3 && s3.length() > 0) {
                Drawable drawable = decodeDrawable(getContext(), s2, true);
                setBackgroundDrawable(drawable);
            } else if (i4 != 0) {
                setBackgroundResource(i4);
            } else if (i5 != 0) {
                ColorDrawable drawable = new ColorDrawable(i5);
                setBackgroundDrawable(drawable);
            }
        } catch (Exception e) {
            LeanBackUtil.log("TabTextView => refreshBackground => " + e.getMessage());
        }
    }

    private void show(String url) {

        boolean checkPath = checkPath(url);
        // file
        if (checkPath) {
            String path = getPath(url);
            Drawable drawable = decodeDrawable(getContext(), path, false);
            setBackgroundDrawable(drawable);
        }
        // download
        else {
            download(url);
        }
    }

    private void download(@NonNull String url) {

        try {
            if (null == url || url.length() == 0)
                throw new Exception("url error: " + url);
            downloadUrl = url;
            Executors.newSingleThreadExecutor().shutdownNow();
            Executors.newSingleThreadExecutor().submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 1
                        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(input);
                        input.close();
                        // 2
                        String path = getPath(url);
                        File file = new File(path);
                        if (file.exists()) {
                            file.delete();
                        }
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
                        out.flush();
                        out.close();
                        bitmap.recycle();
                        // 3
                        if (!url.equals(downloadUrl))
                            throw new Exception("url warning: change url");
                        post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!url.equals(downloadUrl))
                                        throw new Exception("url warning: change url");
                                    Drawable drawable = decodeDrawable(getContext(), path, false);
                                    setBackgroundDrawable(drawable);
                                } catch (Exception e) {
                                    downloadUrl = null;
                                    LeanBackUtil.log("TabTextView => download => " + e.getMessage());
                                }
                            }
                        });
                    } catch (Exception e) {
                        downloadUrl = null;
                        LeanBackUtil.log("TabTextView => download => " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            LeanBackUtil.log("TabTextView => download => " + e.getMessage());
        }
    }

    private String getPath(@NonNull String url) {
        try {
            if (null == url || url.length() <= 0) throw new Exception("url is null");
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(url.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                builder.append(temp);
            }
            String s = builder.toString();
            File dir = getContext().getFilesDir();
            String path = dir.getAbsolutePath();
            File file = new File(path, "tab@img@");
            if (file.exists()) {
                file.delete();
            }
            file.mkdirs();
            return file.getAbsolutePath() + File.separator + s;
        } catch (Exception e) {
            LeanBackUtil.log("TabTextView => getPath => " + e.getMessage());
            return null;
        }
    }

    private boolean checkPath(@NonNull String url) {
        try {
            if (null == url || url.length() <= 0) throw new Exception("url is null");
            String name = getPath(url);
            File file = new File(name);
            if (!file.exists()) throw new Exception("url not find from sdcard");
            return true;
        } catch (Exception e) {
            LeanBackUtil.log("TabTextView => checkPath => " + e.getMessage());
            return false;
        }
    }

    private Drawable decodeDrawable(@NonNull Context context, @NonNull String absolutePath, boolean isAssets) {

        Drawable drawable = null;
        InputStream inputStream = null;

        try {

            if (null != absolutePath && absolutePath.length() > 0) {

                if (isAssets) {
                    inputStream = context.getResources().getAssets().open(absolutePath);
                } else {
                    inputStream = new FileInputStream(absolutePath);
                }

                // .9
                if (absolutePath.endsWith(".9.png")) {
                    drawable = NinePatchChunk.create9PatchDrawable(context, inputStream, null);
                }
                // not .9
                else {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    drawable = new BitmapDrawable(context.getResources(), bitmap);
                }
            }
        } catch (Exception e) {
//            logE("decodeDrawable[exception] => " + e.getMessage());
        }

        try {
            if (null != inputStream) {
                inputStream.close();
            }
        } catch (Exception e) {
        }
        return drawable;
    }
}