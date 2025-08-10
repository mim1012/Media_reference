package com.movies.player.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.movies.player.db.StealthDBHelper;
import com.movies.player.utils.ConvertUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
/* loaded from: classes.dex */
public class ExceptDongCollection {
    public static volatile ExceptDongCollection mInstance;
    public Context mContext;
    public Map<Integer, ArrayList<Integer>> mExceptMap;
    public ArrayList<Integer> mExceptSidoList;
    public Map<Integer, String> mAllSidoMap = new TreeMap();
    public Map<Integer, SigunguItem> mAllSigunguMap = new TreeMap();
    public Map<Integer, DongItem> mAllDongMap = new TreeMap();
    public Map<Integer, String> mExceptSigunguMap = new TreeMap();

    public static ExceptDongCollection getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ExceptDongCollection.class) {
                if (mInstance == null) {
                    mInstance = new ExceptDongCollection(context);
                }
            }
        }
        return mInstance;
    }

    public void clear() {
        synchronized (ExceptDongCollection.class) {
            if (mInstance != null) {
                this.mContext = null;
                mInstance = null;
                this.mAllSidoMap.clear();
                this.mAllSigunguMap.clear();
                this.mAllDongMap.clear();
                this.mExceptSidoList.clear();
                this.mExceptSigunguMap.clear();
                this.mExceptMap.clear();
            }
        }
    }

    public ExceptDongCollection(Context context) {
        this.mExceptSidoList = new ArrayList<>();
        this.mExceptMap = new TreeMap();
        this.mContext = context;
        SQLiteDatabase readableDatabase = new StealthDBHelper(this.mContext).getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM address_sidos", null);
        while (rawQuery.moveToNext()) {
            int i = rawQuery.getInt(0);
            String string = rawQuery.getString(1);
            if (!this.mAllSidoMap.containsKey(Integer.valueOf(i))) {
                this.mAllSidoMap.put(Integer.valueOf(i), string);
            }
        }
        rawQuery.close();
        Cursor rawQuery2 = readableDatabase.rawQuery("SELECT * FROM address_sigungus", null);
        while (rawQuery2.moveToNext()) {
            String string2 = rawQuery2.getString(1);
            int i2 = rawQuery2.getInt(2);
            String string3 = rawQuery2.getString(3);
            if (!this.mAllSigunguMap.containsKey(Integer.valueOf(i2))) {
                SigunguItem sigunguItem = new SigunguItem();
                sigunguItem.nSigunguCode = i2;
                sigunguItem.sSidoName = string2;
                sigunguItem.sSigunguName = string3;
                this.mAllSigunguMap.put(Integer.valueOf(i2), sigunguItem);
            }
        }
        rawQuery2.close();
        Cursor rawQuery3 = readableDatabase.rawQuery("SELECT * FROM address_hjdongs", null);
        while (rawQuery3.moveToNext()) {
            int i3 = rawQuery3.getInt(1);
            int i4 = rawQuery3.getInt(2);
            String string4 = rawQuery3.getString(3);
            if (!this.mAllDongMap.containsKey(Integer.valueOf(i4)) && this.mAllSigunguMap.containsKey(Integer.valueOf(i3))) {
                SigunguItem sigunguItem2 = this.mAllSigunguMap.get(Integer.valueOf(i3));
                DongItem dongItem = new DongItem();
                dongItem.nDongCode = i4;
                dongItem.sDongName = string4;
                dongItem.sSidoName = sigunguItem2.sSidoName;
                dongItem.sSigunguName = sigunguItem2.sSigunguName;
                this.mAllDongMap.put(Integer.valueOf(i4), dongItem);
            }
        }
        rawQuery3.close();
        readableDatabase.close();
        try {
            String string5 = this.mContext.getSharedPreferences("dong_setting", 0).getString("except_sido", "");
            if (!string5.isEmpty()) {
                this.mExceptSidoList = ConvertUtil.stringToIntegerList(string5);
            }
        } catch (Exception unused) {
        }
        refreshExceptSigunguMap();
        this.mExceptMap = loadAutoExceptDongSettingMap("AUTOEXCEPT0_");
    }

    public String getAllExceptDongNames() {
        Map<Integer, ArrayList<Integer>> map = this.mExceptMap;
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("");
        Iterator<Integer> it = this.mExceptMap.keySet().iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            if (!this.mExceptSigunguMap.containsKey(Integer.valueOf(intValue))) {
                it.remove();
            } else {
                ArrayList<Integer> arrayList = this.mExceptMap.get(Integer.valueOf(intValue));
                if (arrayList == null) {
                    sb.append(this.mExceptSigunguMap.get(Integer.valueOf(intValue)).split(" ")[1] + ",");
                } else {
                    for (int i = 0; i < arrayList.size(); i++) {
                        sb.append(this.mAllDongMap.get(arrayList.get(i)).sDongName + ",");
                    }
                }
            }
        }
        return sb.toString();
    }

    public void saveAutoExceptDongSettingMap(String str, Map<Integer, ArrayList<Integer>> map) {
        try {
            SharedPreferences.Editor edit = this.mContext.getSharedPreferences("dong_setting", 0).edit();
            edit.putString(str, ConvertUtil.integerMapToString(map));
            edit.apply();
        } catch (Exception unused) {
        }
    }

    public Map<Integer, ArrayList<Integer>> loadAutoExceptDongSettingMap(String str) {
        TreeMap treeMap = new TreeMap();
        try {
            String string = this.mContext.getSharedPreferences("dong_setting", 0).getString(str, "");
            return string.isEmpty() ? treeMap : ConvertUtil.stringToIntegerMap(string);
        } catch (Exception unused) {
            return treeMap;
        }
    }

    public synchronized Map<Integer, ArrayList<Integer>> loadExceptMap() {
        return this.mExceptMap;
    }

    public synchronized void setExceptMap(Map<Integer, ArrayList<Integer>> map) {
        this.mExceptMap = new TreeMap(map);
    }

    public void saveExceptMap() {
        saveAutoExceptDongSettingMap("AUTOEXCEPT0_", this.mExceptMap);
    }

    public String getAllExceptDongCodes() {
        Map<Integer, ArrayList<Integer>> map = this.mExceptMap;
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("");
        Iterator<Integer> it = this.mExceptMap.keySet().iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            if (!this.mExceptSigunguMap.containsKey(Integer.valueOf(intValue))) {
                it.remove();
            } else {
                ArrayList<Integer> arrayList = this.mExceptMap.get(Integer.valueOf(intValue));
                if (arrayList == null) {
                    for (Integer num : this.mAllDongMap.keySet()) {
                        DongItem dongItem = this.mAllDongMap.get(Integer.valueOf(num.intValue()));
                        if (dongItem.nDongCode / 1000 == intValue) {
                            sb.append(dongItem.nDongCode + ",");
                        }
                    }
                } else {
                    for (int i = 0; i < arrayList.size(); i++) {
                        sb.append(this.mAllDongMap.get(arrayList.get(i)).nDongCode + ",");
                    }
                }
            }
        }
        return sb.toString();
    }

    public Map<Integer, String> getAllDongNamesFromSigunguCode(int i) {
        TreeMap treeMap = new TreeMap();
        for (Integer num : this.mAllDongMap.keySet()) {
            DongItem dongItem = this.mAllDongMap.get(Integer.valueOf(num.intValue()));
            if (i == dongItem.nDongCode / 1000 && !treeMap.containsKey(Integer.valueOf(dongItem.nDongCode))) {
                treeMap.put(Integer.valueOf(dongItem.nDongCode), dongItem.sDongName);
            }
        }
        return treeMap;
    }

    public void refreshExceptSigunguMap() {
        this.mExceptSigunguMap.clear();
        for (Integer num : this.mAllSigunguMap.keySet()) {
            SigunguItem sigunguItem = this.mAllSigunguMap.get(Integer.valueOf(num.intValue()));
            int i = sigunguItem.nSigunguCode / 1000;
            if (this.mExceptSidoList.isEmpty() || this.mExceptSidoList.contains(Integer.valueOf(i))) {
                if (!this.mExceptSigunguMap.containsKey(Integer.valueOf(sigunguItem.nSigunguCode))) {
                    this.mExceptSigunguMap.put(Integer.valueOf(sigunguItem.nSigunguCode), sigunguItem.sSidoName + " " + sigunguItem.sSigunguName);
                }
            }
        }
    }
}
