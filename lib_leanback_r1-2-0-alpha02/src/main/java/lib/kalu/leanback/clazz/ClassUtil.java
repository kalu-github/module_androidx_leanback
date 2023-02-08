package lib.kalu.leanback.clazz;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;

class ClassUtil {

    @NonNull
    public static CharSequence textHighlight(@NonNull Context context, @NonNull ClassBean data) {
        try {
            int id = data.getDrawableHighlight();
            if (id == 0)
                throw new IllegalArgumentException("not exist drawableHighlight");
            SpannableString ss = new SpannableString("@@ " + data.getText());
            Drawable d = context.getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            ss.setSpan(span, 0, "@@".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            return ss;
        } catch (Exception e) {
            return data.getText();
        }
    }

    @NonNull
    public static CharSequence textChecked(@NonNull Context context, @NonNull ClassBean data) {
        try {
            int id = data.getDrawableChecked();
            if (id == 0)
                throw new IllegalArgumentException("not exist drawableChecked");
            SpannableString ss = new SpannableString("@@ " + data.getText());
            Drawable d = context.getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            ss.setSpan(span, 0, "@@".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            return ss;
        } catch (Exception e) {
            return data.getText();
        }
    }

    @NonNull
    public static CharSequence textNormal(@NonNull Context context, @NonNull ClassBean data) {
        try {
            int id = data.getDrawableNormal();
            if (id == 0)
                throw new IllegalArgumentException("not exist drawableNormal");
            SpannableString ss = new SpannableString("@@ " + data.getText());
            Drawable d = context.getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            ss.setSpan(span, 0, "@@".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            return ss;
        } catch (Exception e) {
            return data.getText();
        }
    }
}
