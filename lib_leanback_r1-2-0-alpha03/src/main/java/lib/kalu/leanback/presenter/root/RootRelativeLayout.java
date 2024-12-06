package lib.kalu.leanback.presenter.root;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.leanback.util.LeanBackUtil;

public final class RootRelativeLayout extends RelativeLayout {

    public RootRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (null != mListener) {
            if (visibility == View.VISIBLE) {
                mListener.onShow();
            } else {
                mListener.onHide();
            }
        }
        LeanBackUtil.log("RootLinearLayout => onVisibilityChanged => visibility = " + visibility + ", changedView = " + changedView);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        LeanBackUtil.log("RootLinearLayout => onWindowVisibilityChanged => visibility = " + visibility);
    }

    private OnVisibilityChangedListener mListener;

    public void setOnVisibilityChangedListener(OnVisibilityChangedListener listener) {
        this.mListener = listener;
    }

    public interface OnVisibilityChangedListener {
        void onShow();

        void onHide();
    }
}
