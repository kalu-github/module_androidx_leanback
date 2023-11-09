package lib.kalu.leanback.tab.listener;

import androidx.annotation.IntRange;
import androidx.annotation.Keep;

@Keep
public interface OnTabChangeListener {

    void onChecked(@IntRange(from = 0, to = Integer.MAX_VALUE) int position, @IntRange(from = 0, to = Integer.MAX_VALUE) int old);

    default void onRepeatUp(@IntRange(from = 0, to = Integer.MAX_VALUE) int position) {
    }

    default void onRepeatDown(@IntRange(from = 0, to = Integer.MAX_VALUE) int position) {
    }

    default void onRepeatLeft(@IntRange(from = 0, to = Integer.MAX_VALUE) int position) {
    }

    default void onRepeatRight(@IntRange(from = 0, to = Integer.MAX_VALUE) int position) {
    }

    default void onLeaveUp(@IntRange(from = 0, to = Integer.MAX_VALUE) int position) {
    }

    default void onLeaveDown(@IntRange(from = 0, to = Integer.MAX_VALUE) int position) {
    }

    default void onLeaveLeft(@IntRange(from = 0, to = Integer.MAX_VALUE) int position) {
    }

    default void onLeaveRight(@IntRange(from = 0, to = Integer.MAX_VALUE) int position) {
    }
}
