package lib.kalu.leanback.tab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lib.kalu.leanback.tab.ninepatch.NinePatchChunk;
import lib.kalu.leanback.util.LeanBackUtil;

/**
 * utils
 */
class TabUtil {

    // 最多可能会先后下载3张图片
    private static final ExecutorService ES = Executors.newFixedThreadPool(3);

//    public static final <T extends TabModel> void updateImageUI(@NonNull ImageView view, @NonNull T t, @NonNull float radius, boolean focus, boolean stay) {
//
//        if (null == t || null == view)
//            return;
//
//        updateImageSrc(view, t, focus, stay);
//        updateBackground(view, t, radius, focus, stay);
//    }
//
//    private static final <T extends TabModel> void updateImageSrc(@NonNull ImageView view, @NonNull T t, boolean focus, boolean checked) {
//
//        if (null == t || null == view)
//            return;
//
//        int placeholder = t.getImagePlaceholderResource();
//        if (placeholder != 0) {
//            try {
//                view.setImageResource(placeholder);
//            } catch (Exception e) {
//            }
//        }
//
//        String s;
//        // focus
//        if (focus) {
//            s = t.getImageUrlFocus();
//
//        }
//        // checked
//        else if (checked) {
//            s = t.getImageUrlChecked();
//        }
//        // normal
//        else {
//            s = t.getImageUrlNormal();
//        }
//        if (null != s && s.length() > 0) {
//            loadImageUrl(view, s, false);
//        }
//    }
//
//    private static final <T extends TabModel> void updateBackground(@NonNull View view, @NonNull T t, @NonNull float radius, boolean focus, boolean checked) {
//
//        if (null == t || null == view)
//            return;
//
//        logE("updateImageBackground => ************************");
//
//        // 优先级 ：net > path > assets > resource > color
//
//        String s1 = t.getBackgroundImageUrl(focus, checked);
//        logE("updateImageBackground => url = " + s1);
//
//        String s2 = t.getBackgroundImagePath(focus, checked);
//        logE("updateImageBackground => path = " + s2);
//
//        String s3 = t.getBackgroundImageAssets(focus, checked);
//        logE("updateImageBackground => assets = " + s3);
//
//        int i4 = t.getBackgroundResource(focus, checked);
//        logE("updateImageBackground => resource = " + i4);
//
//        int i5 = t.getBackgroundColor(focus, checked);
//        logE("updateImageBackground => color = " + i5);
//
////        // 背景 => 渐变背景色
////        if (null != colors && colors.length >= 3) {
////            int[] color = stay ? colors[2] : (focus ? colors[1] : colors[0]);
////            logE("updateImageBackground[colors]=> color = " + Arrays.toString(color));
////            setBackgroundGradient(view, color, radius);
////        }
//
//        if (null != s1 && s1.length() > 0) {
//            loadImageUrl(view, s1, true);
//        } else if (null != s2 && s2.length() > 0) {
//            setBackgroundFile(view, s2, true);
//        } else if (null != s3 && s3.length() > 0) {
//            setBackgroundAssets(view, s3, true);
//        } else if (i4 != 0) {
//            setBackgroundResource(view, i4, true);
//        } else if (i5 != 0) {
//            ColorDrawable drawable = new ColorDrawable(i5);
//            setBackgroundDrawable(view, drawable, true);
//        }
//        logE("updateImageBackground => ************************");
//    }

//    /**
//     * @param view
//     * @param t
//     * @param radius
//     * @param focus  焦点
//     * @param stay   驻留
//     * @param <T>
//     */
//    public static final <T extends TabModel> void updateTextUI(@NonNull TabTextView view, @NonNull T t, @NonNull float radius, boolean focus, boolean stay) {
//
//        if (null == t || null == view)
//            return;
//
//        String text = t.getText();
//        if (null != text && text.length() > 0) {
//            view.setText(text);
//        }
//
//        updateTextColor(view, t, focus, stay);
//        updateBackground(view, t, radius, focus, stay);
//    }

//    private static final <T extends TabModel> void updateTextColor(@NonNull TabTextView view, @NonNull T t, boolean focus, boolean checked) {
//
//        if (null == t || null == view)
//            return;
//
//        // 优先级 ：resource > color
//        @ColorRes
//        int c1 = t.getTextColorResource(focus, checked);
//        @ColorInt
//        int c2 = t.getTextColor(focus, checked);
//        if (c1 != 0) {
//            view.refreshTextColorResource(c1);
//        } else if (c2 != 0) {
//            view.refreshTextColor(c2);
//        }
//    }

//    public final static void loadImageUrl(@NonNull final View view, @NonNull final String imageUrl, final boolean isBackground) {
//
//        if (null == imageUrl || imageUrl.length() == 0 || !imageUrl.startsWith("http"))
//            return;
//
//        // 加密缓存文件名
//        try {
//            MessageDigest digest = MessageDigest.getInstance("MD5");
//            byte[] bytes = digest.digest(imageUrl.getBytes());
//            StringBuilder builder = new StringBuilder();
//            for (byte b : bytes) {
//                String temp = Integer.toHexString(b & 0xff);
//                if (temp.length() == 1) {
//                    temp = "0" + temp;
//                }
//                builder.append(temp);
//            }
//
//            Context context = view.getContext();
//            File filesDir = context.getFilesDir();
//            if (!filesDir.exists()) {
//                filesDir.mkdir();
//            }
//
//            File tempDir = new File(filesDir, "temp_tl");
//            if (!tempDir.exists()) {
//                tempDir.mkdir();
//            }
//
//            String fileName = builder.toString();
//            File tempFile = new File(tempDir, fileName);
//            String path = tempFile.getAbsolutePath();
//
//            // 缓存
//            if (tempFile.exists()) {
//                setBackgroundFile(view, path, isBackground);
//            }
//            // 下载
//            else {
//                downloadImage(view, path, imageUrl, isBackground);
//            }
//
//        } catch (Exception e) {
//        }
//    }

//    /**
//     * 分线程下载
//     *
//     * @param view
//     * @param url
//     * @param isBackground
//     */
//    private final static void downloadImage(@NonNull final View view, @NonNull final String filePath, @NonNull final String url, final boolean isBackground) {
//
//        ES.submit(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                    // 1. 下载
//                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
//                    connection.setDoInput(true);
//                    connection.connect();
//                    InputStream input = connection.getInputStream();
//                    Bitmap bitmap = BitmapFactory.decodeStream(input);
//                    input.close();
//
//                    // 2.保存
//                    File temp = new File(filePath);
//                    if (temp.exists()) {
//                        temp.delete();
//                    }
//                    temp.createNewFile();
//                    FileOutputStream out = new FileOutputStream(temp);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
//                    out.flush();
//                    out.close();
//                    bitmap.recycle();
//
//                    // 3.主线程更新
//                    setBackgroundFile(view, filePath, isBackground);
//                } catch (Exception e) {
//                }
//            }
//        });
//    }

//    private final static void setBackgroundAssets(@NonNull final View view, @NonNull final String path, final boolean isBackground) {
//        try {
//            Drawable drawable = decodeDrawable(view, path, true);
//            setBackgroundDrawable(view, drawable, isBackground);
//        } catch (Exception e) {
//        }
//    }

//    private final static void setBackgroundFile(@NonNull final View view, @NonNull final String path, final boolean isBackground) {
//        try {
//            if (Looper.myLooper() != Looper.getMainLooper()) {
//                view.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Drawable drawable = decodeDrawable(view, path, false);
//                        setBackgroundDrawable(view, drawable, isBackground);
//                    }
//                });
//            } else {
//                Drawable drawable = decodeDrawable(view, path, false);
//                setBackgroundDrawable(view, drawable, isBackground);
//            }
//        } catch (Exception e) {
//        }
//    }

    public final static Drawable decodeDrawable(@NonNull Context context, @NonNull String absolutePath, boolean isAssets) {

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

    /*********************************/

    public final static void setBackgroundGradient(@NonNull View view, @NonNull final int[] colors, float radius) {

        if (null == colors || colors.length == 0)
            return;

        try {
            GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
            drawable.setCornerRadius(radius);
            view.setBackground(drawable);
        } catch (Exception e) {
        }
    }

//    public final static void setBackgroundResource(@NonNull final View view, @NonNull final int resId, final boolean isBackground) {
//        try {
////            String resourceName = view.getResources().getResourceName(resId);
////            logE("loadImageResource => resourceName = " + resourceName);
//
//            // img1
//            if (null != view && view instanceof ImageView && isBackground) {
//                ImageView imageView = (ImageView) view;
//                imageView.setBackground(null);
//                imageView.setBackgroundResource(resId);
//            }
//            // img2
//            else if (null != view && view instanceof ImageView) {
//                ImageView imageView = (ImageView) view;
//                imageView.setImageDrawable(null);
//                imageView.setImageResource(resId);
//                logE("setBackgroundResource => status = succ, view = " + view);
//            }
//            // view
//            else if (null != view) {
//                view.setBackground(null);
//                view.setBackgroundResource(resId);
//                logE("setBackgroundResource => status = succ, view = " + view);
//            }
//            // fail
//            else {
//                logE("setBackgroundResource => status = fail, view = " + view);
//            }
//        } catch (Exception e) {
//            logE("setBackgroundResource => " + e.getMessage());
//        }
//    }
//
//    private final static void setBackgroundDrawable(@NonNull View view, @NonNull Drawable drawable, final boolean isBackground) {
//        try {
//            // img1
//            if (null != view && view instanceof ImageView && isBackground) {
//                ImageView imageView = (ImageView) view;
//                imageView.setBackground(null);
//                imageView.setBackground(drawable);
//                logE("setBackgroundDrawable => status = succ, view = " + view);
//            }
//            // img2
//            else if (null != view && view instanceof ImageView) {
//                ImageView imageView = (ImageView) view;
//                imageView.setImageDrawable(null);
//                imageView.setImageDrawable(drawable);
//                logE("setBackgroundDrawable => status = succ, view = " + view);
//            }
//            // view
//            else if (null != view) {
//                view.setBackground(null);
//                view.setBackground(drawable);
//                logE("setBackgroundDrawable => status = succ, view = " + view);
//            }
//            // fail
//            else {
//                logE("setBackgroundDrawable => status = fail, view = " + view);
//            }
//        } catch (Exception e) {
//            logE("setBackgroundDrawable => " + e.getMessage());
//        }
//    }
}
