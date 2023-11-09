package lib.kalu.leanback.tags.model;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public final class TagResultBean implements Serializable {

    private int code;
    private String key;
    private String name;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
