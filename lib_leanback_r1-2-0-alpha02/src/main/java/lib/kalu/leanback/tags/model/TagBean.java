package lib.kalu.leanback.tags.model;


import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class TagBean implements Serializable {

    private int id;
    private String text;
    @ColorInt
    private int textColorFocus;
    @ColorInt
    private int textColorSelect;
    @ColorInt
    private int textColorDetault;
    @DrawableRes
    private int backgroundResourceFocus;
    @DrawableRes
    private int backgroundResourceSelect;
    @DrawableRes
    private int backgroundResourceDefault;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextColorFocus() {
        return textColorFocus;
    }

    public void setTextColorFocus(int textColorFocus) {
        this.textColorFocus = textColorFocus;
    }

    public int getTextColorSelect() {
        return textColorSelect;
    }

    public void setTextColorSelect(int textColorSelect) {
        this.textColorSelect = textColorSelect;
    }

    public int getTextColorDetault() {
        return textColorDetault;
    }

    public void setTextColorDetault(int textColorDetault) {
        this.textColorDetault = textColorDetault;
    }

    public int getBackgroundResourceFocus() {
        return backgroundResourceFocus;
    }

    public void setBackgroundResourceFocus(int backgroundResourceFocus) {
        this.backgroundResourceFocus = backgroundResourceFocus;
    }

    public int getBackgroundResourceSelect() {
        return backgroundResourceSelect;
    }

    public void setBackgroundResourceSelect(int backgroundResourceSelect) {
        this.backgroundResourceSelect = backgroundResourceSelect;
    }

    public int getBackgroundResourceDefault() {
        return backgroundResourceDefault;
    }

    public void setBackgroundResourceDefault(int backgroundResourceDefault) {
        this.backgroundResourceDefault = backgroundResourceDefault;
    }
}
