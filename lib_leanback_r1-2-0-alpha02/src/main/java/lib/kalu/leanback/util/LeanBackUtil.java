package lib.kalu.leanback.util;

import android.util.Log;

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
            Log.e(tag, message);
        } else {
            Log.e(tag, message, throwable);
        }
    }
}
