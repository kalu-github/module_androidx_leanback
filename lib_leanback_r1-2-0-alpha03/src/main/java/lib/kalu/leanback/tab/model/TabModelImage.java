package lib.kalu.leanback.tab.model;

import androidx.annotation.Keep;
import androidx.leanback.R;

@Keep
public final class TabModelImage extends TabModel {

    @Override
    int getType() {
//        setImagePlaceholderResource(R.drawable.module_tablayout_ic_shape_background_normal);
//        setBackgroundResourceNormal(R.drawable.module_tablayout_ic_shape_background_normal);
//        setBackgroundResourceFocus(R.drawable.module_tablayout_ic_shape_background_focus);
//        setBackgroundResourceChecked(R.drawable.module_tablayout_ic_shape_background_checked);
        return TYPE_IMG;
    }
}
