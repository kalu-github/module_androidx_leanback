package lib.kalu.leanback.tags.model;


import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class TagBean implements Serializable {

    private int code;
    private String text;
    private boolean checked;
    @ColorInt
    private int textColor;
    @ColorInt
    private int textColorFocus;
    @ColorInt
    private int textColorChecked;
    @DrawableRes
    private int backgroundResource;
    @DrawableRes
    private int backgroundResourceFocus;
    @DrawableRes
    private int backgroundResourceChecked;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    @ColorInt
    public int getTextColor(boolean hasFocus) {
        if (hasFocus && checked) {
            return textColorFocus;
        } else if (checked) {
            return textColorChecked;
        } else {
            return textColor;
        }
    }

    @DrawableRes
    public int getBackgroundRecource(boolean hasFocus) {
        if (hasFocus && checked) {
            return backgroundResourceFocus;
        } else if (checked) {
            return backgroundResourceChecked;
        } else {
            return backgroundResource;
        }
    }
}
