package lib.kalu.leanback.tags.listener;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.Map;

@Keep
public interface OnTagsChangeListener {

    /**
     * @param row    行
     * @param column 列
     * @param map    选中数据
     */
    void onChange(@NonNull int row, @NonNull int column, @NonNull Map<String, String> map);
}
