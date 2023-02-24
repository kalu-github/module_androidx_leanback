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
    private int textColorIntFocus;
    @ColorInt
    private int textColorIntSelect;
    @ColorInt
    private int textColorIntDetault;
    @ColorInt
    private int textColorResourceFocus;
    @ColorInt
    private int textColorResourceSelect;
    @ColorInt
    private int textColorResourceDetault;
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

    public int getTextColorIntFocus() {
        return textColorIntFocus;
    }

    public void setTextColorIntFocus(int textColorIntFocus) {
        this.textColorIntFocus = textColorIntFocus;
    }

    public int getTextColorIntSelect() {
        return textColorIntSelect;
    }

    public void setTextColorIntSelect(int textColorIntSelect) {
        this.textColorIntSelect = textColorIntSelect;
    }

    public int getTextColorIntDetault() {
        return textColorIntDetault;
    }

    public void setTextColorIntDetault(int textColorIntDetault) {
        this.textColorIntDetault = textColorIntDetault;
    }

    public int getTextColorResourceFocus() {
        return textColorResourceFocus;
    }

    public void setTextColorResourceFocus(int textColorResourceFocus) {
        this.textColorResourceFocus = textColorResourceFocus;
    }

    public int getTextColorResourceSelect() {
        return textColorResourceSelect;
    }

    public void setTextColorResourceSelect(int textColorResourceSelect) {
        this.textColorResourceSelect = textColorResourceSelect;
    }

    public int getTextColorResourceDetault() {
        return textColorResourceDetault;
    }

    public void setTextColorResourceDetault(int textColorResourceDetault) {
        this.textColorResourceDetault = textColorResourceDetault;
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
