package lib.kalu.leanback.presenter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.leanback.widget.VerticalGridView;

public interface ListTvPresenterImpl {

    @ColorInt
    default int initHeadBackgroundColor(@NonNull Context context) {
        return Color.TRANSPARENT;
    }

    @Dimension
    default int initHeadPadding(@NonNull Context context) {
        return 0;
    }

    @Dimension
    default int initHeadTextSize(@NonNull Context context) {
        return 0;
    }

    default String initHeadAssetTTF(@NonNull Context context) {
        return null;
    }


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

    default void initTitle(@NonNull Context context, @NonNull View view, @IdRes int headId) {
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

        // head
        View viewById = view.findViewById(headId);
        if (null != viewById) {
            int initHeadBackgroundColor = initHeadBackgroundColor(context);
            if (initHeadBackgroundColor != Color.TRANSPARENT) {
                viewById.setBackgroundColor(initHeadBackgroundColor);
            }
            int initHeadPadding = initHeadPadding(context);
            if (initHeadPadding > 0) {
                viewById.setPadding(0, 0, 0, initHeadPadding);
            }
            int initHeadTextSize = initHeadTextSize(context);
            if (initHeadTextSize > 0) {
                ((TextView) viewById).setTextSize(TypedValue.COMPLEX_UNIT_PX, initHeadTextSize);
            }
            String initHeadAssetTTF = initHeadAssetTTF(context);
            if (null != initHeadAssetTTF && initHeadAssetTTF.length() > 0) {
                try {
                    ((TextView) viewById).setTypeface(Typeface.createFromAsset(context.getAssets(), initHeadAssetTTF));
                } catch (Exception e) {
                }
            }
        }
    }
}
