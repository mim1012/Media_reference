package com.movies.player.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public class SharedData {
    public static int MIN_UPLOAD_LOG_LENGTH = 300;
    public static boolean bManagerThreadRunning = false;
    public static String mLogContent = "";
    public static int nPrepareMode;
    public static int nWorkMode;
    public static ExecutorService tWorkThreadPool;
    public static CopyOnWriteArrayList<String> aProcessedCallList = new CopyOnWriteArrayList<>();
    public static String sPhoneNumber = "";
    public static String sAndroidId = "";
    public static Date dtExpDate = null;
    public static String sDbFileVersionCode = "1.0.0";
    public static String sNewDbFileVersionCode = "1.0.0";
    public static String sDbFileDownloadUrl = "";
    public static boolean bAuto = false;
    public static int nCallDistance = 0;
    public static int nLongDistance = 0;
    public static boolean bAutoDeny = false;
    public static boolean bEnableVolume = false;
    public static String sPreferredExclusionPlaces = "";
    public static ArrayList<String> aPreferredExclusionPlaceList = new ArrayList<>();
    public static String sPreferredAcceptPlaces = "";
    public static ArrayList<String> aPreferredAcceptPlaceList = new ArrayList<>();
    public static int bThreadNumber = 0;
    public static String sAllDestDongCodes = "";
    public static String sAllExceptDongCodes = "";
    public static String ACTION_PLAY_PAUSE = "action_play_pause";
    public static String ACTION_GO_HOME = "action_go_home";

    public static void loadConfig(Context context) {
        tWorkThreadPool = Executors.newFixedThreadPool(30);
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref", 0);
        sDbFileVersionCode = sharedPreferences.getString("DB_VERSION", "1.0.0");
        nCallDistance = sharedPreferences.getInt("callkm", 0);
        nLongDistance = sharedPreferences.getInt("longkm", 0);
        bAutoDeny = sharedPreferences.getBoolean("autodeny", false);
        bEnableVolume = sharedPreferences.getBoolean("enableVolume", false);
        sPreferredExclusionPlaces = FileUtils.loadPreferredExclusionPlacesFileToText();
        sPreferredAcceptPlaces = FileUtils.loadPreferredAcceptPlacesFileToText();
        aPreferredExclusionPlaceList = parseStr2Array(sPreferredExclusionPlaces);
        aPreferredAcceptPlaceList = parseStr2Array(sPreferredAcceptPlaces);
    }

    public static void saveConfig(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences("pref", 0).edit();
        edit.putString("DB_VERSION", sDbFileVersionCode);
        edit.putInt("callkm", nCallDistance);
        edit.putInt("longkm", nLongDistance);
        edit.putBoolean("autodeny", bAutoDeny);
        edit.putBoolean("enableVolume", bEnableVolume);
        edit.apply();
        FileUtils.savePreferredExclusionPlacesToFile(sPreferredExclusionPlaces);
        FileUtils.savePreferredAcceptPlacesToFile(sPreferredAcceptPlaces);
    }

    public static ArrayList<String> parseStr2Array(String str) {
        String[] split;
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            for (String str2 : str.replaceAll(" ", "").split("[\n,]")) {
                if (!str2.equals("")) {
                    arrayList.add(str2);
                }
            }
        } catch (Exception unused) {
        }
        return arrayList;
    }

    public static String parseArray2Str(ArrayList<String> arrayList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            try {
                sb.append(arrayList.get(i)).append(", ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void addProcessedCallText(String str) {
        if (aProcessedCallList.size() >= 100) {
            aProcessedCallList.remove(0);
        }
        aProcessedCallList.add(str);
    }
}
