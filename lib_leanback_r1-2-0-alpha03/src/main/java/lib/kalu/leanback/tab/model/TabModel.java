package lib.kalu.leanback.tab.model;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.io.File;
import java.io.Serializable;

@Keep
public abstract class TabModel implements Serializable {

    public int TYPE_TXT = 1001;
    public int TYPE_IMG = 1002;

    abstract int getType();

    public boolean isTxt() {
        return getType() == TYPE_TXT;
    }

    public boolean isImg() {
        return getType() == TYPE_IMG;
    }

    String text;

    @ColorInt
    int textColorNormal;

    @ColorInt
    int textColorFocus;

    @ColorInt
    int textColorChecked;

    @ColorRes
    int textColorResourceNormal;

    @ColorRes
    int textColorResourceFocus;

    @ColorRes
    int textColorResourceChecked;

    @DrawableRes
    int backgroundResourceNormal;

    @DrawableRes
    int backgroundResourceFocus;

    @DrawableRes
    int backgroundResourceChecked;

    @ColorInt
    int backgroundColorNormal;

    @ColorInt
    int backgroundColorFocus;

    @ColorInt
    int backgroundColorChecked;

    @Nullable
    String backgroundImageUrlNormal;

    @Nullable
    String backgroundImageUrlFocus;

    @Nullable
    String backgroundImageUrlChecked;

    @Nullable
    String backgroundImagePathNormal;

    @Nullable
    String backgroundImagePathFocus;

    @Nullable
    String backgroundImagePathChecked;

    @Nullable
    String backgroundImageAssetsNormal;

    @Nullable
    String backgroundImageAssetsFocus;

    @Nullable
    String backgroundImageAssetsChecked;

    @Nullable
    String imagePathNormal;

    @Nullable
    String imagePathFocus;

    @Nullable
    String imagePathChecked;

    @Nullable
    String imageUrlNormal;

    @Nullable
    String imageUrlFocus;

    @Nullable
    String imageUrlChecked;

    @DrawableRes
    int imagePlaceholderResource;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextColorNormal() {
        return textColorNormal;
    }

    public void setTextColorNormal(@ColorInt int textColorNormal) {
        this.textColorNormal = textColorNormal;
    }

    public int getTextColorFocus() {
        return textColorFocus;
    }

    public void setTextColorFocus(@ColorInt int textColorFocus) {
        this.textColorFocus = textColorFocus;
    }

    public int getTextColorChecked() {
        return textColorChecked;
    }

    public void setTextColorChecked(@ColorInt int textColorChecked) {
        this.textColorChecked = textColorChecked;
    }

    public int getTextColorResourceNormal() {
        return textColorResourceNormal;
    }

    public void setTextColorResourceNormal(int textColorResourceNormal) {
        this.textColorResourceNormal = textColorResourceNormal;
    }

    public int getTextColorResourceFocus() {
        return textColorResourceFocus;
    }

    public void setTextColorResourceFocus(int textColorResourceFocus) {
        this.textColorResourceFocus = textColorResourceFocus;
    }

    public int getTextColorResourceChecked() {
        return textColorResourceChecked;
    }

    public void setTextColorResourceChecked(int textColorResourceChecked) {
        this.textColorResourceChecked = textColorResourceChecked;
    }

    public int getBackgroundResourceNormal() {
        return backgroundResourceNormal;
    }

    public void setBackgroundResourceNormal(int backgroundResourceNormal) {
        this.backgroundResourceNormal = backgroundResourceNormal;
    }

    public int getBackgroundResourceFocus() {
        return backgroundResourceFocus;
    }

    public void setBackgroundResourceFocus(int backgroundResourceFocus) {
        this.backgroundResourceFocus = backgroundResourceFocus;
    }

    public int getBackgroundResourceChecked() {
        return backgroundResourceChecked;
    }

    public void setBackgroundResourceChecked(int backgroundResourceChecked) {
        this.backgroundResourceChecked = backgroundResourceChecked;
    }

    public int getBackgroundColorNormal() {
        return backgroundColorNormal;
    }

    public void setBackgroundColorNormal(@ColorInt int backgroundColorNormal) {
        this.backgroundColorNormal = backgroundColorNormal;
    }

    public int getBackgroundColorFocus() {
        return backgroundColorFocus;
    }

    public void setBackgroundColorFocus(@ColorInt int backgroundColorFocus) {
        this.backgroundColorFocus = backgroundColorFocus;
    }

    public int getBackgroundColorChecked() {
        return backgroundColorChecked;
    }

    public void setBackgroundColorChecked(@ColorInt int backgroundColorChecked) {
        this.backgroundColorChecked = backgroundColorChecked;
    }

    @Nullable
    public String getBackgroundImageUrlNormal() {
        return backgroundImageUrlNormal;
    }

    public void setBackgroundImageUrlNormal(@Nullable String backgroundImageUrlNormal) {
        this.backgroundImageUrlNormal = backgroundImageUrlNormal;
    }

    @Nullable
    public String getBackgroundImageUrlFocus() {
        return backgroundImageUrlFocus;
    }

    public void setBackgroundImageUrlFocus(@Nullable String backgroundImageUrlFocus) {
        this.backgroundImageUrlFocus = backgroundImageUrlFocus;
    }

    @Nullable
    public String getBackgroundImageUrlChecked() {
        return backgroundImageUrlChecked;
    }

    public void setBackgroundImageUrlChecked(@Nullable String backgroundImageUrlChecked) {
        this.backgroundImageUrlChecked = backgroundImageUrlChecked;
    }

    @Nullable
    public String getBackgroundImagePathNormal() {
        try {
            File file = new File(backgroundImagePathNormal);
            if (!file.exists()) {
                throw new Exception();
            }
            return backgroundImagePathNormal;
        } catch (Exception e) {
            return null;
        }
    }

    public void setBackgroundImagePathNormal(@Nullable String backgroundImagePathNormal) {
        this.backgroundImagePathNormal = backgroundImagePathNormal;
    }

    @Nullable
    public String getBackgroundImagePathFocus() {
        try {
            File file = new File(backgroundImagePathFocus);
            if (!file.exists()) {
                throw new Exception();
            }
            return backgroundImagePathFocus;
        } catch (Exception e) {
            return null;
        }
    }

    public void setBackgroundImagePathFocus(@Nullable String backgroundImagePathFocus) {
        this.backgroundImagePathFocus = backgroundImagePathFocus;
    }

    @Nullable
    public String getBackgroundImagePathChecked() {
        try {
            File file = new File(backgroundImagePathChecked);
            if (!file.exists()) {
                throw new Exception();
            }
            return backgroundImagePathChecked;
        } catch (Exception e) {
            return null;
        }
    }

    public void setBackgroundImagePathChecked(@Nullable String backgroundImagePathChecked) {
        this.backgroundImagePathChecked = backgroundImagePathChecked;
    }

    @Nullable
    public String getBackgroundImageAssetsNormal() {
        return backgroundImageAssetsNormal;
    }

    public void setBackgroundImageAssetsNormal(@Nullable String backgroundImageAssetsNormal) {
        this.backgroundImageAssetsNormal = backgroundImageAssetsNormal;
    }

    @Nullable
    public String getBackgroundImageAssetsFocus() {
        return backgroundImageAssetsFocus;
    }

    public void setBackgroundImageAssetsFocus(@Nullable String backgroundImageAssetsFocus) {
        this.backgroundImageAssetsFocus = backgroundImageAssetsFocus;
    }

    @Nullable
    public String getBackgroundImageAssetsChecked() {
        return backgroundImageAssetsChecked;
    }

    public void setBackgroundImageAssetsChecked(@Nullable String backgroundImageAssetsChecked) {
        this.backgroundImageAssetsChecked = backgroundImageAssetsChecked;
    }

    @Nullable
    public String getImageUrlNormal() {
        return imageUrlNormal;
    }

    public void setImageUrlNormal(@Nullable String imageUrlNormal) {
        this.imageUrlNormal = imageUrlNormal;
    }

    @Nullable
    public String getImageUrlFocus() {
        return imageUrlFocus;
    }

    public void setImageUrlFocus(@Nullable String imageUrlFocus) {
        this.imageUrlFocus = imageUrlFocus;
    }

    @Nullable
    public String getImageUrlChecked() {
        return imageUrlChecked;
    }

    public void setImageUrlChecked(@Nullable String imageUrlChecked) {
        this.imageUrlChecked = imageUrlChecked;
    }

    @Nullable
    public String getImagePathNormal() {
        return imagePathNormal;
    }

    public void setImagePathNormal(@Nullable String imagePathNormal) {
        this.imagePathNormal = imagePathNormal;
    }

    @Nullable
    public String getImagePathFocus() {
        return imagePathFocus;
    }

    public void setImagePathFocus(@Nullable String imagePathFocus) {
        this.imagePathFocus = imagePathFocus;
    }

    @Nullable
    public String getImagePathChecked() {
        return imagePathChecked;
    }

    public void setImagePathChecked(@Nullable String imagePathChecked) {
        this.imagePathChecked = imagePathChecked;
    }

    public int getImagePlaceholderResource() {
        return imagePlaceholderResource;
    }

    public void setImagePlaceholderResource(int imagePlaceholderResource) {
        this.imagePlaceholderResource = imagePlaceholderResource;
    }

    /********************/

    public String getBackgroundImageUrl(boolean focus, boolean checked) {
        if (focus) {
            return getBackgroundImageUrlFocus();
        } else if (checked) {
            return getBackgroundImageUrlChecked();
        } else {
            return getBackgroundImageUrlNormal();
        }
    }

    public String getBackgroundImagePath(boolean focus, boolean checked) {
        if (focus) {
            return getBackgroundImagePathFocus();
        } else if (checked) {
            return getBackgroundImagePathChecked();
        } else {
            return getBackgroundImagePathNormal();
        }
    }

    public String getBackgroundImageAssets(boolean focus, boolean checked) {
        if (focus) {
            return getBackgroundImageAssetsFocus();
        } else if (checked) {
            return getBackgroundImageAssetsChecked();
        } else {
            return getBackgroundImageAssetsNormal();
        }
    }

    @DrawableRes
    public int getBackgroundResource(boolean focus, boolean checked) {
        if (focus) {
            return getBackgroundResourceFocus();
        } else if (checked) {
            return getBackgroundResourceChecked();
        } else {
            return getBackgroundResourceNormal();
        }
    }

    @ColorInt
    public int getBackgroundColor(boolean focus, boolean checked) {
        if (focus) {
            return getBackgroundColorFocus();
        } else if (checked) {
            return getBackgroundColorChecked();
        } else {
            return getBackgroundColorNormal();
        }
    }

    @ColorInt
    public int getTextColor(boolean focus, boolean checked) {
        if (focus) {
            return getTextColorFocus();
        } else if (checked) {
            return getTextColorChecked();
        } else {
            return getTextColorNormal();
        }
    }

    @ColorRes
    public int getTextColorResource(boolean focus, boolean checked) {
        if (focus) {
            return getTextColorResourceFocus();
        } else if (checked) {
            return getTextColorResourceChecked();
        } else {
            return getTextColorResourceNormal();
        }
    }

    @NonNull
    public String getImageUrl(boolean focus, boolean checked) {
        if (focus) {
            return getImageUrlFocus();
        } else if (checked) {
            return getImageUrlChecked();
        } else {
            return getImageUrlNormal();
        }
    }

    @NonNull
    public String getImagePath(boolean focus, boolean checked) {
        if (focus) {
            return getImagePathFocus();
        } else if (checked) {
            return getImagePathChecked();
        } else {
            return getImagePathNormal();
        }
    }
}
