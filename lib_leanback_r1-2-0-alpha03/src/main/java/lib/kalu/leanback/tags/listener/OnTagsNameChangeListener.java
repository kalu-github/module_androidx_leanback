package lib.kalu.leanback.tags.listener;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.Map;

@Keep
public interface OnTagsNameChangeListener {

    void onChange(@NonNull int row, @NonNull int column, @NonNull Map<String, String> data);
}