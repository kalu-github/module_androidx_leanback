package lib.kalu.leanback.tags.model;


import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Keep;

import org.json.JSONObject;

import java.io.Serializable;

@Keep
public class TagBean implements Serializable {

    private int id;
    private String name;
    private JSONObject jsonObject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

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
