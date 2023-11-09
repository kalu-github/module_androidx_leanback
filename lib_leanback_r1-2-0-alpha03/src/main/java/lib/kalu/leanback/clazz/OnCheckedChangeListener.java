package lib.kalu.leanback.clazz;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

@Keep
public interface OnCheckedChangeListener {

    void onChecked(@NonNull boolean isFromUser, @NonNull int index, @NonNull String name, @NonNull String code);
}
