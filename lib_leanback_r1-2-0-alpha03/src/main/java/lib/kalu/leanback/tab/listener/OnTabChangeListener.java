package lib.kalu.leanback.tab.listener;

import androidx.annotation.IntRange;
import androidx.annotation.Keep;

@Keep
public interface OnTabChangeListener {

    void onChecked(int index);

    default void onUnChecked(int index) {
    }

    default void onRepeat(int direction, int index) {
    }

    default void onLeave(int direction, int index) {
    }
}
