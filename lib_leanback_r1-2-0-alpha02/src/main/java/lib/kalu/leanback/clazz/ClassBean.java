package lib.kalu.leanback.clazz;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import java.io.Serializable;

public class ClassBean implements Serializable {

    @DrawableRes
    private int drawableChecked = 0;
    @DrawableRes
    private int drawableHighlight = 0;
    @DrawableRes
    private int drawableNormal;
    private String text;
    private String code;
    private boolean checked;

    public String getText() {
        return text;
    }

    public String getCode() {
        return code;
    }

    public boolean isChecked() {
        return checked;
    }

    @DrawableRes
    public int getDrawableHighlight() {
        return drawableHighlight;
    }

    @DrawableRes
    public int getDrawableChecked() {
        return drawableChecked;
    }

    @DrawableRes
    public int getDrawableNormal() {
        return drawableNormal;
    }

    public void setDrawableChecked(int drawableChecked) {
        this.drawableChecked = drawableChecked;
    }

    public void setDrawableHighlight(int drawableHighlight) {
        this.drawableHighlight = drawableHighlight;
    }

    public void setDrawableNormal(int drawableNormal) {
        this.drawableNormal = drawableNormal;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
