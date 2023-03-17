package lib.kalu.leanback.presenter.impl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.leanback.R;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import lib.kalu.leanback.presenter.bean.TvEpisodesPlusItemBean;
import lib.kalu.leanback.presenter.bean.TvPresenterRowBean;
import lib.kalu.leanback.util.LeanBackUtil;

public interface ListTvPresenterImpl<T extends TvPresenterRowBean> {

    default void initItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
    }

    default String initRowTitle(Context context) {
        return null;
    }

    default int initPaddingLeft(@NonNull Context context) {
        return 0;
    }

    default int initPaddingRight(@NonNull Context context) {
        return 0;
    }

    default int initPaddingTop(@NonNull Context context) {
        return 0;
    }

    default int initPaddingBottom(@NonNull Context context) {
        return 0;
    }

    default int initTitlePaddingTop(@NonNull Context context) {
        return 0;
    }

    default int initTitlePaddingBottom(@NonNull Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.lb_common_title_padding_bottom);
    }

    default int initTitlePaddingLeft(@NonNull Context context) {
        return 0;
    }

    default int initTitlePaddingRight(@NonNull Context context) {
        return 0;
    }

    @ColorInt
    default int initTitleBackgroundColor(@NonNull Context context) {
        return Color.TRANSPARENT;
    }

    @ColorInt
    default int initTitleTextColor(@NonNull Context context) {
        return Color.WHITE;
    }

    default int initTitleTextSize(@NonNull Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.lb_common_title_text_size);
    }

    default String initTitleAssetTTF(@NonNull Context context) {
        return null;
    }

    @ColorInt
    default int initBackgroundColor(@NonNull Context context) {
        return Color.TRANSPARENT;
    }

    @ColorInt
    default int initContentBackgroundColor(@NonNull Context context) {
        return Color.TRANSPARENT;
    }

    default void setBackgroundColor(@NonNull Context context, @NonNull ViewGroup viewGroup) {
        try {
            int backgroundColor = initBackgroundColor(context);
            viewGroup.setBackgroundColor(backgroundColor);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvPresenterImpl => setBackgroundColor => " + e.getMessage());
        }
    }

    default void setContentBackgroundColor(@NonNull Context context, @NonNull ViewGroup viewGroup, @IdRes int id) {
        try {
            View view = viewGroup.findViewById(id);
            if (null == view)
                throw new Exception("not find");
            int contentBackgroundColor = initContentBackgroundColor(context);
            view.setBackgroundColor(contentBackgroundColor);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvPresenterImpl => setContentBackgroundColor => " + e.getMessage());
        }
    }

    default void setPadding(@NonNull Context context, @NonNull ViewGroup viewGroup) {
        try {
            int paddingTop = initPaddingTop(context);
            int paddingBottom = initPaddingBottom(context);
            int paddingLeft = initPaddingLeft(context);
            int paddingRight = initPaddingRight(context);
            viewGroup.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvPresenterImpl => setPadding => " + e.getMessage());
        }
    }

    default void setTitleBackgroundColor(@NonNull Context context, @NonNull ViewGroup viewGroup, @IdRes int id) {
        try {
            View view = viewGroup.findViewById(id);
            if (null == view)
                throw new Exception("not find");
            int titleBackgroundColor = initTitleBackgroundColor(context);
            view.setBackgroundColor(titleBackgroundColor);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvPresenterImpl => setTitleBackgroundColor => " + e.getMessage());
        }
    }

    default void setTitlePadding(@NonNull Context context, @NonNull ViewGroup viewGroup, @IdRes int id) {
        try {
            View view = viewGroup.findViewById(id);
            if (null == view)
                throw new Exception("not find");
            int paddingTop = initTitlePaddingTop(context);
            int paddingBottom = initTitlePaddingBottom(context);
            int paddingLeft = initTitlePaddingLeft(context);
            int paddingRight = initTitlePaddingRight(context);
            view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvPresenterImpl => setTitlePadding => " + e.getMessage());
        }
    }

    default void setTitleTextSize(@NonNull Context context, @NonNull ViewGroup viewGroup, @IdRes int id) {
        try {
            View view = viewGroup.findViewById(id);
            if (null == view)
                throw new Exception("not find");
            int titleTextSize = initTitleTextSize(context);
            if (titleTextSize <= 0)
                throw new Exception("titleTextSize <=0");
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvPresenterImpl => setTitleTextSize => " + e.getMessage());
        }
    }

    default void setTitleTextColor(@NonNull Context context, @NonNull ViewGroup viewGroup, @IdRes int id) {
        try {
            View view = viewGroup.findViewById(id);
            if (null == view)
                throw new Exception("not find");
            int titleTextColor = initTitleTextColor(context);
            ((TextView) view).setTextColor(titleTextColor);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvPresenterImpl => setTitleTextColor => " + e.getMessage());
        }
    }

    default void setTitleAssetTTF(@NonNull Context context, @NonNull ViewGroup viewGroup, @IdRes int id) {
        try {
            View view = viewGroup.findViewById(id);
            if (null == view)
                throw new Exception("not find");
            String titleAssetTTF = initTitleAssetTTF(context);
            if (null == titleAssetTTF || titleAssetTTF.length() <= 0)
                throw new Exception("titleAssetTTF is null");
            ((TextView) view).setTypeface(Typeface.createFromAsset(context.getAssets(), titleAssetTTF));
        } catch (Exception e) {
            LeanBackUtil.log("ListTvPresenterImpl => setTitleAssetTTF => " + e.getMessage());
        }
    }

    default void updateTitle(List<T> data, View view, @IdRes int id) {

        try {
            String rowTitle = initRowTitle(view.getContext());
            if (null == rowTitle || rowTitle.length() <= 0) {
                T t = data.get(0);
                rowTitle = t.getRowTitle();
            }
            if (null == rowTitle || rowTitle.length() <= 0)
                throw new Exception("rowTitle is null");
            TextView textView = view.findViewById(id);
            textView.setText(rowTitle);
            textView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvPresenterImpl => updateTitle => " + e.getMessage());
            TextView textView = view.findViewById(id);
            textView.setVisibility(View.GONE);
        }
    }

    default void updateTitle(View view, @IdRes int id) {

        try {
            String rowTitle = initRowTitle(view.getContext());
            if (null == rowTitle || rowTitle.length() <= 0)
                throw new Exception("rowTitle is null");
            TextView textView = view.findViewById(id);
            textView.setText(rowTitle);
            textView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LeanBackUtil.log("ListTvPresenterImpl => updateTitle => " + e.getMessage());
            TextView textView = view.findViewById(id);
            textView.setVisibility(View.GONE);
        }
    }

//    default void setItemMargin(View view, int left, int top, int right, int bottom) {
//
//        try {
//            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
//            if (null == layoutParams)
//                throw new Exception("layoutParams is null");
//            layoutParams.leftMargin = left;
//            layoutParams.topMargin = top;
//            layoutParams.rightMargin = right;
//            layoutParams.bottomMargin = bottom;
//        } catch (Exception e) {
//            LeanBackUtil.log("ListTvPresenterImpl => setItemMargin => " + e.getMessage());
//        }
//    }
}
