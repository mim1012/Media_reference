package com.movies.player.boiler.general;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import com.movies.player.db.StealthDBHelper;
import com.movies.player.model.DestItem;
import com.movies.player.utils.Call;
import com.movies.player.utils.DBUtils;
import com.movies.player.utils.Helper;
import com.movies.player.utils.LogUtils;
import com.movies.player.utils.SharedData;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class BoilerRunnable implements Runnable {
    Call call;
    Context context;
    String mLogText;

    public BoilerRunnable(Call call, Context context) {
        this.call = call;
        this.context = context;
    }

    @Override // java.lang.Runnable
    public void run() {
        long currentTimeMillis = System.currentTimeMillis();
        this.mLogText = "";
        processBoiler();
        String str = this.mLogText + "\t--소요시간--: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        this.mLogText = str;
        LogUtils.uploadLog(str);
    }

    private void processBoiler() {
        DestItem destItem;
        DestItem destItem2;
        double distanceTo;
        try {
            this.mLogText += "일반모드 콜분석 시작: " + this.call.mDistance + "," + this.call.mDest + "\n";
            int callDistance = Helper.getCallDistance(this.call);
            if (SharedData.nCallDistance != 0 && callDistance > SharedData.nCallDistance) {
                if (SharedData.bAutoDeny && this.call.mDenyCtrl != null && this.call.mDenyCtrl.isClickable()) {
                    Helper.delegateButtonClick(this.call.mDenyCtrl);
                }
                this.mLogText += "\t: 손님거리 체크 실패: 거절(설정: " + SharedData.nCallDistance + "m)\n";
                return;
            }
            if (SharedData.aPreferredExclusionPlaceList != null && SharedData.aPreferredExclusionPlaceList.size() >= 1) {
                for (int i = 0; i < SharedData.aPreferredExclusionPlaceList.size(); i++) {
                    String str = SharedData.aPreferredExclusionPlaceList.get(i);
                    if (this.call.mDest.contains(str)) {
                        this.mLogText += "\t: 우선제외지 -> 콜:" + this.call.mDest + ", 지명:" + str + "\n";
                        return;
                    }
                }
            }
            if (SharedData.aPreferredAcceptPlaceList != null && SharedData.aPreferredAcceptPlaceList.size() >= 1) {
                for (int i2 = 0; i2 < SharedData.aPreferredAcceptPlaceList.size(); i2++) {
                    String str2 = SharedData.aPreferredAcceptPlaceList.get(i2);
                    if (this.call.mDest.contains(str2)) {
                        this.mLogText += "\t: 우선선호지 -> 콜:" + this.call.mDest + ", 지명:" + str2 + " 분석 성공: 접수\n";
                        Helper.delegateButtonClick(this.call.mAcceptCtrl);
                        return;
                    }
                }
            }
            int i3 = SharedData.nWorkMode;
            if (i3 == 256) {
                if (SharedData.sAllDestDongCodes.isEmpty()) {
                    return;
                }
                this.mLogText += "\t: 도착지 체크 시작\n";
                DestItem analyzeDestination = Helper.analyzeDestination(this.call);
                if (analyzeDestination != null && analyzeDestination.sDongName != null && (analyzeDestination.bRoad || analyzeDestination.sSigunguName != null)) {
                    this.mLogText += "\t: 주소분석 성공: 시도구명=" + analyzeDestination.sSigunguName + ", 동(도로)이름=" + analyzeDestination.sDongName + ", 도로명주소인가=" + analyzeDestination.bRoad + "\n";
                    ArrayList<Integer> arrayList = new ArrayList();
                    if (analyzeDestination.bRoad) {
                        this.mLogText += DBUtils.findHjdongCodeListByDestInfo(this.context, arrayList, analyzeDestination);
                    } else {
                        this.mLogText += DBUtils.findHjdongCodeListByHjdongName(this.context, arrayList, analyzeDestination);
                        if (arrayList.isEmpty()) {
                            this.mLogText += DBUtils.findHjdongCodeListByBjdongName(this.context, arrayList, analyzeDestination);
                        }
                    }
                    for (Integer num : arrayList) {
                        int intValue = num.intValue();
                        if (SharedData.sAllDestDongCodes.contains(String.valueOf(intValue))) {
                            this.mLogText += "\t: 도착지체크 성공 : " + intValue + " : 콜수락버튼 클릭됨\n";
                            Helper.delegateButtonClick(this.call.mAcceptCtrl);
                            return;
                        }
                    }
                    if (SharedData.bAutoDeny && this.call.mDenyCtrl != null && this.call.mDenyCtrl.isClickable()) {
                        Helper.delegateButtonClick(this.call.mDenyCtrl);
                    }
                    this.mLogText += "\t: 도착지체크 실패: 거절\n";
                    return;
                }
                if (SharedData.bAutoDeny && this.call.mDenyCtrl != null && this.call.mDenyCtrl.isClickable()) {
                    Helper.delegateButtonClick(this.call.mDenyCtrl);
                }
                this.mLogText += "\t: 주소분석 실패: 거절\n";
            } else if (i3 == 512) {
                if (SharedData.sAllExceptDongCodes.isEmpty()) {
                    return;
                }
                this.mLogText += "\t: 제외지 체크 시작\n";
                DestItem analyzeDestination2 = Helper.analyzeDestination(this.call);
                if (analyzeDestination2 != null && analyzeDestination2.sDongName != null && (analyzeDestination2.bRoad || analyzeDestination2.sSigunguName != null)) {
                    this.mLogText += "\t: 주소분석 성공: 시도구명=" + analyzeDestination2.sSigunguName + ", 동(도로)이름=" + analyzeDestination2.sDongName + ", 도로명주소인가=" + analyzeDestination2.bRoad + "\n";
                    ArrayList arrayList2 = new ArrayList();
                    if (analyzeDestination2.bRoad) {
                        this.mLogText += DBUtils.findHjdongCodeListByDestInfo(this.context, arrayList2, analyzeDestination2);
                    } else {
                        this.mLogText += DBUtils.findHjdongCodeListByHjdongName(this.context, arrayList2, analyzeDestination2);
                        if (arrayList2.isEmpty()) {
                            this.mLogText += DBUtils.findHjdongCodeListByBjdongName(this.context, arrayList2, analyzeDestination2);
                        }
                    }
                    for (int i4 = 0; !arrayList2.isEmpty() && i4 < arrayList2.size(); i4++) {
                        int intValue2 = ((Integer) arrayList2.get(i4)).intValue();
                        if (SharedData.sAllExceptDongCodes.contains(String.valueOf(intValue2))) {
                            if (SharedData.bAutoDeny && this.call.mDenyCtrl != null && this.call.mDenyCtrl.isClickable()) {
                                Helper.delegateButtonClick(this.call.mDenyCtrl);
                            }
                            this.mLogText += "\t: 제외지리스트에 있음 : " + intValue2 + " 거절\n";
                            return;
                        }
                    }
                    Helper.delegateButtonClick(this.call.mAcceptCtrl);
                    this.mLogText += "\t: 제외지리스트에 없음 : 접수\n";
                    return;
                }
                this.mLogText += "\t: 도착지주소 분석 실패: 거절\n";
            } else if (i3 == 768) {
                Helper.delegateButtonClick(this.call.mAcceptCtrl);
                this.mLogText += "\t: 전체모드: 접수\n";
            } else if (i3 == 1024) {
                this.mLogText += "\t" + this.call.mOrigin + " -> " + this.call.mDest + ": 거리모드 체크 시작\n";
                String[] split = this.call.mDest.split(" ");
                int i5 = 0;
                while (true) {
                    destItem = null;
                    if (i5 >= split.length) {
                        destItem2 = null;
                        break;
                    }
                    String str3 = split[i5];
                    if (str3 == null || str3.length() <= 0 || !"읍면동가".contains(str3.substring(str3.length() - 1))) {
                        i5++;
                    } else {
                        destItem2 = new DestItem();
                        destItem2.sDongName = str3;
                        if (i5 > 0) {
                            destItem2.sSigunguName = split[i5 - 1];
                        }
                        destItem2.bRoad = false;
                    }
                }
                String[] split2 = this.call.mOrigin.split(" ");
                int i6 = 0;
                while (true) {
                    if (i6 >= split2.length) {
                        break;
                    }
                    String str4 = split2[i6];
                    if (str4 == null || str4.length() <= 0 || !"읍면동가".contains(str4.substring(str4.length() - 1))) {
                        i6++;
                    } else {
                        destItem = new DestItem();
                        destItem.sDongName = str4;
                        if (i6 > 0) {
                            destItem.sSigunguName = split2[i6 - 1];
                        } else if (i6 == 0) {
                            destItem.sSigunguName = destItem2.sSigunguName;
                        }
                        destItem.bRoad = false;
                    }
                }
                if (destItem != null && destItem2 != null) {
                    SQLiteDatabase readableDatabase = new StealthDBHelper(this.context).getReadableDatabase();
                    Cursor rawQuery = readableDatabase.rawQuery("SELECT address_hjdongs.x, address_hjdongs.y FROM address_sigungus, address_hjdongs WHERE address_sigungus.sigungu_code = address_hjdongs.sigungu_code AND address_sigungus.sigungu_name LIKE ? AND address_hjdongs.hjdong_name = ?", new String[]{"%" + destItem.sSigunguName + "%", destItem.sDongName});
                    double d = 0.0d;
                    double d2 = 0.0d;
                    while (rawQuery.moveToNext()) {
                        d = Double.parseDouble(rawQuery.getString(0));
                        d2 = Double.parseDouble(rawQuery.getString(1));
                    }
                    rawQuery.close();
                    Cursor rawQuery2 = readableDatabase.rawQuery("SELECT address_hjdongs.x, address_hjdongs.y FROM address_sigungus, address_hjdongs WHERE address_sigungus.sigungu_code = address_hjdongs.sigungu_code AND address_sigungus.sigungu_name LIKE ? AND address_hjdongs.hjdong_name = ?", new String[]{"%" + destItem2.sSigunguName + "%", destItem2.sDongName});
                    double d3 = 0.0d;
                    double d4 = 0.0d;
                    while (rawQuery2.moveToNext()) {
                        d3 = Double.parseDouble(rawQuery2.getString(0));
                        d4 = Double.parseDouble(rawQuery2.getString(1));
                    }
                    rawQuery2.close();
                    double d5 = d;
                    int i7 = (d5 > 0.0d ? 1 : (d5 == 0.0d ? 0 : -1));
                    double d6 = d2;
                    if (i7 != 0 && d6 != 0.0d && d3 != 0.0d && d4 != 0.0d) {
                        Location location = new Location("origin");
                        Location location2 = new Location("dest");
                        location.setLatitude(d5);
                        location.setLongitude(d6);
                        location2.setLatitude(d3);
                        location2.setLongitude(d4);
                        if (location.distanceTo(location2) * 0.001d >= SharedData.nLongDistance) {
                            Helper.delegateButtonClick(this.call.mAcceptCtrl);
                            this.mLogText += "\t" + this.call.mDest + "(거리: " + String.valueOf(distanceTo) + "km, 설정: " + String.valueOf(SharedData.nLongDistance) + "): 거리모드: 접수\n";
                            return;
                        }
                        if (SharedData.bAutoDeny) {
                            Helper.delegateButtonClick(this.call.mDenyCtrl);
                        }
                        this.mLogText += "\t" + this.call.mDest + "(거리: " + String.valueOf(distanceTo) + "km, 설정: " + String.valueOf(SharedData.nLongDistance) + "): 거리모드: 거절\n";
                        return;
                    }
                    if (SharedData.bAutoDeny && this.call.mDenyCtrl != null && this.call.mDenyCtrl.isClickable()) {
                        Helper.delegateButtonClick(this.call.mDenyCtrl);
                    }
                    if (i7 == 0 || d6 == 0.0d) {
                        this.mLogText += "\t" + this.call.mOrigin + " -> " + this.call.mDest + ": 디비에 위경도값이 없는 출발위치니까 거절\n";
                    }
                    if (d3 == 0.0d || d4 == 0.0d) {
                        this.mLogText += "\t" + this.call.mOrigin + " -> " + this.call.mDest + ": 디비에 위경도값이 없는 도착위치니까 거절\n";
                        return;
                    }
                    return;
                }
                if (SharedData.bAutoDeny && this.call.mDenyCtrl != null && this.call.mDenyCtrl.isClickable()) {
                    Helper.delegateButtonClick(this.call.mDenyCtrl);
                }
                this.mLogText += "\t" + this.call.mOrigin + " -> " + this.call.mDest + ": [출발/도착]위치분석실패되었음\n";
            }
        } catch (Exception e) {
            this.mLogText += "일반모드 콜잡이오류: " + e.getMessage() + "\n";
        }
    }
}
