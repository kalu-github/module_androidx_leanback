package lib.kalu.leanback.util;

import androidx.annotation.NonNull;

public final class LeanBackUtil {

    private static boolean mEnable = false;
    private static String mTAG = "LEAN_BACK_LOG";

    public static boolean isLogger() {
        return mEnable;
    }

    public static void setLogger(boolean enable) {
        mEnable = enable;
    }

    public static void log(@NonNull String message) {
        log(mTAG, message, null);
    }

    public static void log(@NonNull String tag, @NonNull String message) {
        log(tag, message, null);
    }

    public static void log(@NonNull String tag, @NonNull String message, @NonNull Throwable throwable) {

        if (!mEnable)
            return;

        if (null == throwable) {
            LeanBackUtil.log(tag, message);
        } else {
            LeanBackUtil.log(tag, message, throwable);
        }
    }
}
