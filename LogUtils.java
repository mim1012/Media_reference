package com.movies.player.utils;

import com.movies.player.BuildConfig;
import com.net.NetworkTask;
import java.util.Date;
import java.util.HashMap;
/* loaded from: classes.dex */
public class LogUtils {
    public static void uploadLog(Exception exc) {
        uploadLog(getErrorStackTrace(exc));
    }

    public static void uploadLog(String str) {
        SharedData.mLogContent += "\n" + DateUtils.parseStringMMddHHmmss(new Date()) + ": " + str;
        if (SharedData.mLogContent.length() > SharedData.MIN_UPLOAD_LOG_LENGTH) {
            uploadLog();
        }
    }

    public static void uploadLog() {
        try {
            if (SharedData.mLogContent.isEmpty()) {
                return;
            }
            String concat = ConvertUtil.decode(Constants.SERVICE_URL).concat("/api/mobile/logs/upload-log");
            HashMap hashMap = new HashMap();
            hashMap.put("appid", Constants.sAppId);
            hashMap.put("appver", BuildConfig.VERSION_NAME);
            hashMap.put("id", SharedData.sAndroidId);
            hashMap.put("number", SharedData.sPhoneNumber);
            hashMap.put("content", SharedData.mLogContent);
            new NetworkTask(concat, 4097, null).execute(hashMap);
            SharedData.mLogContent = "";
        } catch (Exception unused) {
        }
    }

    public static String getErrorStackTrace(Exception exc) {
        StringBuilder sb = new StringBuilder();
        sb.append("Exception: ").append(exc.toString()).append("\n");
        for (StackTraceElement stackTraceElement : exc.getStackTrace()) {
            sb.append("at ").append(stackTraceElement.toString()).append("\n");
        }
        return sb.toString();
    }
}
