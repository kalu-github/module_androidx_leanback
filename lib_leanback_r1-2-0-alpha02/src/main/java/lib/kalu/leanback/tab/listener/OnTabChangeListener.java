package lib.kalu.leanback.tab.listener;

import androidx.annotation.IntRange;
import androidx.annotation.Keep;

@Keep
public interface OnTabChangeListener {

    void onChecked(@IntRange(from = 0, to = Integer.MAX_VALUE) int position, @IntRange(from = 0, to = Integer.MAX_VALUE) int old);

    void onRepeat(@IntRange(from = 0, to = Integer.MAX_VALUE) int position);

    void onLeave(@IntRange(from = 0, to = Integer.MAX_VALUE) int position);
}
