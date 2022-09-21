package lib.kalu.leanback.clazz;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

public interface ClassApi {

    @DrawableRes
    default int drawableHighlight() {
        return 0;
    }

    @DrawableRes
    default int drawableChecked() {
        return 0;
    }

    @DrawableRes
    default int drawableNormal() {
        return 0;
    }

    @NonNull
    default CharSequence textHighlight(@NonNull Context context) {
        try {
            int id = drawableHighlight();
            if (id == 0)
                throw new IllegalArgumentException("not exist drawableHighlight");
            SpannableString ss = new SpannableString("@@ " + text());
            Drawable d = context.getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            ss.setSpan(span, 0, "@@".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            return ss;
        } catch (Exception e) {
            return text();
        }
    }

    @NonNull
    default CharSequence textChecked(@NonNull Context context) {
        try {
            int id = drawableChecked();
            if (id == 0)
                throw new IllegalArgumentException("not exist drawableChecked");
            SpannableString ss = new SpannableString("@@ " + text());
            Drawable d = context.getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            ss.setSpan(span, 0, "@@".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            return ss;
        } catch (Exception e) {
            return text();
        }
    }

    @NonNull
    default CharSequence textNormal(@NonNull Context context) {
        try {
            int id = drawableNormal();
            if (id == 0)
                throw new IllegalArgumentException("not exist drawableNormal");
            SpannableString ss = new SpannableString("@@ " + text());
            Drawable d = context.getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            ss.setSpan(span, 0, "@@".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            return ss;
        } catch (Exception e) {
            return text();
        }
    }

    @NonNull
    CharSequence text();

    @NonNull
    String code();

    @NonNull
    boolean checked();
}
