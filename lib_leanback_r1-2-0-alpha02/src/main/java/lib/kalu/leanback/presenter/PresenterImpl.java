package lib.kalu.leanback.presenter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.leanback.widget.VerticalGridView;

public interface PresenterImpl {

    @ColorInt
    default int initBackgroundColor(@NonNull Context context) {
        return Color.TRANSPARENT;
    }

    default int initMagrinTop(@NonNull Context context) {
        return 0;
    }

    default int initMagrinBottom(@NonNull Context context) {
        return 0;
    }

    default void initStyle(@NonNull Context context, @NonNull View view, @NonNull ViewGroup parent) {
        int magrinTop = initMagrinTop(context);
        int magrinBottom = initMagrinBottom(context);
        if (magrinTop > 0 || magrinBottom > 0) {
            VerticalGridView.LayoutParams layoutParams = (VerticalGridView.LayoutParams) view.getLayoutParams();
            layoutParams.topMargin = magrinTop;
            layoutParams.bottomMargin = magrinBottom;
        }
        int backgroundColor = initBackgroundColor(context);
        if (backgroundColor != Color.TRANSPARENT) {
            view.setBackgroundColor(backgroundColor);
        }
    }
}
