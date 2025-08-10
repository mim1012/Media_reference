package com.movies.player.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.movies.player.db.StealthDBHelper;
import com.movies.player.model.DestDongCollection;
import com.movies.player.model.DestItem;
import com.movies.player.model.SigunguItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class DBUtils {
    public static int findSigunguCodeByName(Context context, String str) {
        SQLiteDatabase readableDatabase = new StealthDBHelper(context).getReadableDatabase();
        int i = 0;
        if (str != null) {
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM address_sigungus WHERE sigungu_name='" + str + "'", null);
            while (rawQuery.moveToNext()) {
                i = rawQuery.getInt(2);
            }
            rawQuery.close();
        }
        readableDatabase.close();
        return i;
    }

    public static ArrayList<Integer> findSigunguCodeListByName(Context context, String str) {
        SQLiteDatabase readableDatabase = new StealthDBHelper(context).getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<>();
        if (str != null) {
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM address_sigungus WHERE sigungu_name='" + str + "'", null);
            while (rawQuery.moveToNext()) {
                arrayList.add(Integer.valueOf(rawQuery.getInt(2)));
            }
            rawQuery.close();
        }
        readableDatabase.close();
        return arrayList;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0073, code lost:
        r3 = "\t: 행정동 주소찾기 성공 : " + r2.sSidoName + " " + r2.sSigunguName + " " + r5 + ":" + r4 + "\n";
        r9.add(java.lang.Integer.valueOf(r4));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String findHjdongCodeListByHjdongName(android.content.Context r8, java.util.List<java.lang.Integer> r9, com.movies.player.model.DestItem r10) {
        /*
            com.movies.player.model.DestDongCollection r0 = com.movies.player.model.DestDongCollection.getInstance(r8)
            com.movies.player.db.StealthDBHelper r1 = new com.movies.player.db.StealthDBHelper
            r1.<init>(r8)
            android.database.sqlite.SQLiteDatabase r8 = r1.getReadableDatabase()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "SELECT * FROM address_hjdongs WHERE hjdong_name='"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = r10.sDongName
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = "'"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r2 = 0
            android.database.Cursor r1 = r8.rawQuery(r1, r2)
        L2d:
            boolean r2 = r1.moveToNext()
            java.lang.String r3 = ""
            if (r2 == 0) goto Lb7
            r2 = 1
            int r2 = r1.getInt(r2)
            r4 = 2
            int r4 = r1.getInt(r4)
            r5 = 3
            java.lang.String r5 = r1.getString(r5)
            java.util.Map<java.lang.Integer, com.movies.player.model.SigunguItem> r6 = r0.mAllSigunguMap
            java.lang.Integer r7 = java.lang.Integer.valueOf(r2)
            boolean r6 = r6.containsKey(r7)
            if (r6 == 0) goto L2d
            java.util.Map<java.lang.Integer, com.movies.player.model.SigunguItem> r6 = r0.mAllSigunguMap
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            java.lang.Object r2 = r6.get(r2)
            com.movies.player.model.SigunguItem r2 = (com.movies.player.model.SigunguItem) r2
            if (r2 != 0) goto L5f
            goto L2d
        L5f:
            java.lang.String r6 = r2.sSidoName
            java.lang.String r7 = r10.sSigunguName
            boolean r6 = r6.contains(r7)
            if (r6 != 0) goto L73
            java.lang.String r6 = r2.sSigunguName
            java.lang.String r7 = r10.sSigunguName
            boolean r6 = r6.contains(r7)
            if (r6 == 0) goto L2d
        L73:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.StringBuilder r10 = r10.append(r3)
            java.lang.String r0 = "\t: 행정동 주소찾기 성공 : "
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r0 = r2.sSidoName
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r0 = " "
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r2 = r2.sSigunguName
            java.lang.StringBuilder r10 = r10.append(r2)
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.StringBuilder r10 = r10.append(r5)
            java.lang.String r0 = ":"
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.StringBuilder r10 = r10.append(r4)
            java.lang.String r0 = "\n"
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r3 = r10.toString()
            java.lang.Integer r10 = java.lang.Integer.valueOf(r4)
            r9.add(r10)
        Lb7:
            r1.close()
            r8.close()
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.movies.player.utils.DBUtils.findHjdongCodeListByHjdongName(android.content.Context, java.util.List, com.movies.player.model.DestItem):java.lang.String");
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0073, code lost:
        r3 = "\t: 법정동 주소찾기 성공 : " + r2.sSidoName + " " + r2.sSigunguName + " " + r4 + ":" + r5 + "\n";
        r9.add(java.lang.Integer.valueOf(r5));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String findHjdongCodeListByBjdongName(android.content.Context r8, java.util.List<java.lang.Integer> r9, com.movies.player.model.DestItem r10) {
        /*
            com.movies.player.model.DestDongCollection r0 = com.movies.player.model.DestDongCollection.getInstance(r8)
            com.movies.player.db.StealthDBHelper r1 = new com.movies.player.db.StealthDBHelper
            r1.<init>(r8)
            android.database.sqlite.SQLiteDatabase r8 = r1.getReadableDatabase()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "SELECT * FROM address_bjdongs WHERE bjdong_name='"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = r10.sDongName
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = "'"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r2 = 0
            android.database.Cursor r1 = r8.rawQuery(r1, r2)
        L2d:
            boolean r2 = r1.moveToNext()
            java.lang.String r3 = ""
            if (r2 == 0) goto Lb7
            r2 = 1
            int r2 = r1.getInt(r2)
            r4 = 3
            java.lang.String r4 = r1.getString(r4)
            r5 = 4
            int r5 = r1.getInt(r5)
            java.util.Map<java.lang.Integer, com.movies.player.model.SigunguItem> r6 = r0.mAllSigunguMap
            java.lang.Integer r7 = java.lang.Integer.valueOf(r2)
            boolean r6 = r6.containsKey(r7)
            if (r6 == 0) goto L2d
            java.util.Map<java.lang.Integer, com.movies.player.model.SigunguItem> r6 = r0.mAllSigunguMap
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            java.lang.Object r2 = r6.get(r2)
            com.movies.player.model.SigunguItem r2 = (com.movies.player.model.SigunguItem) r2
            if (r2 != 0) goto L5f
            goto L2d
        L5f:
            java.lang.String r6 = r2.sSidoName
            java.lang.String r7 = r10.sSigunguName
            boolean r6 = r6.contains(r7)
            if (r6 != 0) goto L73
            java.lang.String r6 = r2.sSigunguName
            java.lang.String r7 = r10.sSigunguName
            boolean r6 = r6.contains(r7)
            if (r6 == 0) goto L2d
        L73:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.StringBuilder r10 = r10.append(r3)
            java.lang.String r0 = "\t: 법정동 주소찾기 성공 : "
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r0 = r2.sSidoName
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r0 = " "
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r2 = r2.sSigunguName
            java.lang.StringBuilder r10 = r10.append(r2)
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.StringBuilder r10 = r10.append(r4)
            java.lang.String r0 = ":"
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.StringBuilder r10 = r10.append(r5)
            java.lang.String r0 = "\n"
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r3 = r10.toString()
            java.lang.Integer r10 = java.lang.Integer.valueOf(r5)
            r9.add(r10)
        Lb7:
            r1.close()
            r8.close()
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.movies.player.utils.DBUtils.findHjdongCodeListByBjdongName(android.content.Context, java.util.List, com.movies.player.model.DestItem):java.lang.String");
    }

    public static String findHjdongCodeListByDestInfo(Context context, List<Integer> list, DestItem destItem) {
        SigunguItem sigunguItem;
        int intValue;
        DestDongCollection destDongCollection = DestDongCollection.getInstance(context);
        SQLiteDatabase readableDatabase = new StealthDBHelper(context).getReadableDatabase();
        ArrayList<Integer> findSigunguCodeListByName = findSigunguCodeListByName(context, destItem.sSigunguName);
        String str = ("\t: 시군구 찾기 성공 : 코드=" + ConvertUtil.integerArrayToString(findSigunguCodeListByName) + ", 이름=" + destItem.sSigunguName + "\n") + "\t: 법정동 찾기 : ";
        ArrayList arrayList = new ArrayList();
        if (findSigunguCodeListByName.size() > 0) {
            Iterator<Integer> it = findSigunguCodeListByName.iterator();
            while (it.hasNext()) {
                Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM address_roads WHERE " + (it.next().intValue() != 0 ? "sigungu_code=" + intValue + " AND " : "") + "road_name='" + destItem.sDongName + "'", null);
                while (rawQuery.moveToNext()) {
                    int i = rawQuery.getInt(3);
                    if (!arrayList.contains(Integer.valueOf(i))) {
                        arrayList.add(Integer.valueOf(i));
                        str = str + i + ", ";
                    }
                }
                rawQuery.close();
            }
        } else {
            Cursor rawQuery2 = readableDatabase.rawQuery("SELECT * FROM address_roads WHERE road_name='" + destItem.sDongName + "'", null);
            while (rawQuery2.moveToNext()) {
                int i2 = rawQuery2.getInt(3);
                if (!arrayList.contains(Integer.valueOf(i2))) {
                    arrayList.add(Integer.valueOf(i2));
                    str = str + i2 + ", ";
                }
            }
            rawQuery2.close();
        }
        String str2 = (str + "\n") + "\t: 행정동 찾기 : ";
        if (arrayList.isEmpty()) {
            return str2;
        }
        StringBuilder sb = new StringBuilder("SELECT * FROM address_bjdongs WHERE bjdong_code IN (");
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            int intValue2 = ((Integer) arrayList.get(i3)).intValue();
            if (i3 != arrayList.size() - 1) {
                sb.append("'").append(intValue2).append("', ");
            } else {
                sb.append("'").append(intValue2).append("')");
            }
        }
        Cursor rawQuery3 = readableDatabase.rawQuery(sb.toString(), null);
        while (rawQuery3.moveToNext()) {
            int i4 = rawQuery3.getInt(1);
            int i5 = rawQuery3.getInt(4);
            if (!list.contains(Integer.valueOf(i5)) && (destItem.sSigunguName == null || (destDongCollection.mAllSigunguMap.containsKey(Integer.valueOf(i4)) && (sigunguItem = destDongCollection.mAllSigunguMap.get(Integer.valueOf(i4))) != null && (sigunguItem.sSidoName.contains(destItem.sSigunguName) || sigunguItem.sSigunguName.contains(destItem.sSigunguName))))) {
                str2 = str2 + i5 + ", ";
                list.add(Integer.valueOf(i5));
            }
        }
        String str3 = str2 + "\n";
        rawQuery3.close();
        readableDatabase.close();
        return str3;
    }
}
