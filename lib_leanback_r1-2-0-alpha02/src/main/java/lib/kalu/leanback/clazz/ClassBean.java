package lib.kalu.leanback.clazz;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class ClassBean implements Serializable {

    private String text;
    private String code;
    private boolean checked;
    private boolean focus;

    @DrawableRes
    private int leftDrawable = 0;
    @DrawableRes
    private int leftDrawableFocus = 0;
    @DrawableRes
    private int leftDrawableChecked = 0;
    @ColorInt
    private int textColor = Color.BLACK;
    @ColorInt
    private int textColorFocus = Color.WHITE;
    @ColorInt
    private int textColorChecked = Color.RED;
    @DrawableRes
    private int backgroundResource = 0;
    @DrawableRes
    private int backgroundResourceFocus = 0;
    @DrawableRes
    private int backgroundResourceChecked = 0;

    public void setLeftDrawable(int leftDrawable) {
        this.leftDrawable = leftDrawable;
    }

    public void setLeftDrawableFocus(int leftDrawableFocus) {
        this.leftDrawableFocus = leftDrawableFocus;
    }

    public void setLeftDrawableChecked(int leftDrawableChecked) {
        this.leftDrawableChecked = leftDrawableChecked;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextColorFocus(int textColorFocus) {
        this.textColorFocus = textColorFocus;
    }

    public void setTextColorChecked(int textColorChecked) {
        this.textColorChecked = textColorChecked;
    }

    public void setBackgroundResource(int backgroundResource) {
        this.backgroundResource = backgroundResource;
    }

    public void setBackgroundResourceFocus(int backgroundResourceFocus) {
        this.backgroundResourceFocus = backgroundResourceFocus;
    }

    public void setBackgroundResourceChecked(int backgroundResourceChecked) {
        this.backgroundResourceChecked = backgroundResourceChecked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    @ColorInt
    public int getTextColor() {
        if (focus) {
            return textColorFocus;
        } else if (checked) {
            return textColorChecked;
        } else {
            return textColor;
        }
    }

    @DrawableRes
    public int getBackgroundRecource() {
        if (focus) {
            return backgroundResourceFocus;
        } else if (checked) {
            return backgroundResourceChecked;
        } else {
            return backgroundResource;
        }
    }

    @DrawableRes
    public int getLeftDrawable() {
        if (focus) {
            return leftDrawableFocus;
        } else if (checked) {
            return leftDrawableChecked;
        } else {
            return leftDrawable;
        }
    }
}
