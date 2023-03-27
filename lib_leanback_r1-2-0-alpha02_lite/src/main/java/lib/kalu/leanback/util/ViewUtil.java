package lib.kalu.leanback.util;

import android.app.Activity;
import android.content.Context;
import android.view.FocusFinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.view.ContextThemeWrapper;

public final class ViewUtil {

    public static Activity getActivity(Context context) {
        try {
            if (context instanceof Activity) {
                return (Activity) context;
            } else if (context instanceof ContextThemeWrapper) {
                return getActivity(((ContextThemeWrapper) context).getBaseContext());
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static Window getWindow(Context context) {
        try {
            return getActivity(context).getWindow();
        } catch (Exception e) {
            return null;
        }
    }

    public static ViewGroup getRootView(Context context) {
        try {
            return (ViewGroup) getWindow(context).getDecorView().getRootView();
        } catch (Exception e) {
            return null;
        }
    }

    public static View findNextFocus(Context context, View focusedView, int direction) {
        try {
            ViewGroup rootView = getRootView(context);
            View nextFocus = FocusFinder.getInstance().findNextFocus(rootView, focusedView, direction);
            int visibility = nextFocus.getVisibility();
            if (visibility == View.VISIBLE) {
                return nextFocus;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
