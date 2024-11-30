package lib.kalu.leanback.tab.listener;

import androidx.annotation.Keep;

@Keep
public interface OnTabChangeListener {

    default void onRepeat(int direction, int index) {
    }

    default void onLeave(int direction, int index) {
    }
}
