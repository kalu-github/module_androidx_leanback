package lib.kalu.leanback.tab.model;

import androidx.annotation.Keep;

@Keep
public final class TabModelText extends TabModel {

    @Override
    int getType() {
//        setTextColorNormal(Color.BLACK);
//        setTextColorFocus(Color.RED);
//        setTextColorChecked(Color.BLUE);
//        setBackgroundResourceNormal(R.drawable.module_tablayout_ic_shape_background_normal);
//        setBackgroundResourceFocus(R.drawable.module_tablayout_ic_shape_background_focus);
//        setBackgroundResourceChecked(R.drawable.module_tablayout_ic_shape_background_checked);
        return TYPE_TXT;
    }
}
