package com.movies.player.utils;

import android.app.Activity;
import android.util.Log;
/* loaded from: classes.dex */
public class PermissionUtils {
    public static boolean checkPermissions(Activity activity, String[] strArr) {
        boolean z = true;
        for (String str : strArr) {
            if (activity.checkSelfPermission(str) != 0) {
                z = false;
            }
        }
        if (z) {
            return true;
        }
        Log.e("PermissionUtils", "권한 요청");
        activity.requestPermissions(strArr, 101);
        return false;
    }
}
