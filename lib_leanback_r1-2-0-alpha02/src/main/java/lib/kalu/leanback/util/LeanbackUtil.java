package lib.kalu.leanback.util;

import androidx.annotation.NonNull;

public final class LeanbackUtil {

    private static boolean mEnable = false;
    private static String mTAG = "LEANBACK_LOG";

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
            LeanbackUtil.log(tag, message);
        } else {
            LeanbackUtil.log(tag, message, throwable);
        }
    }
}
