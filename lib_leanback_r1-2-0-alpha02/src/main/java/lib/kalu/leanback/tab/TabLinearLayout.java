package lib.kalu.leanback.tab;


import android.content.Context;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

class TabLinearLayout extends LinearLayout {
    TabLinearLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        setPadding(0, 0, 0, 0);
        setFocusable(true);
        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }
}
