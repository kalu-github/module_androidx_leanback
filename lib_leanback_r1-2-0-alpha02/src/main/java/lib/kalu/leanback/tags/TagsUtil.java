package lib.kalu.leanback.tags;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.leanback.util.LeanBackUtil;

class TagsUtil {

    private static final String TAG = "module_tagslayout";

    public static final void logE(@NonNull String message) {
        logE(message, null);
    }

    public static final void logE(@NonNull String message, @Nullable Throwable tr) {

        if (null == message || message.length() == 0)
            return;

        if (null == tr) {
            LeanBackUtil.log(TAG, message);
        } else {
            LeanBackUtil.log(TAG, message, tr);
        }
    }
}
