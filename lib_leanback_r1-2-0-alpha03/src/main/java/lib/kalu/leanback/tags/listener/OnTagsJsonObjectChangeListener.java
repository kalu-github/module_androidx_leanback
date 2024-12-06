package lib.kalu.leanback.tags.listener;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.Map;

@Keep
public interface OnTagsJsonObjectChangeListener {

    void onChange(@NonNull int row, @NonNull int column, @NonNull Map<String, JSONObject> data);
}
