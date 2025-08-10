package com.movies.player.boiler.list;

import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;
import com.movies.player.model.DestItem;
import com.movies.player.utils.Call;
import com.movies.player.utils.DBUtils;
import com.movies.player.utils.Helper;
import com.movies.player.utils.LogUtils;
import com.movies.player.utils.SharedData;
import java.util.ArrayList;
import java.util.concurrent.Callable;
/* loaded from: classes.dex */
public class BoilerCallable implements Callable {
    Call call;
    Context context;
    String mLogText;

    public BoilerCallable(AccessibilityNodeInfo accessibilityNodeInfo, AccessibilityNodeInfo accessibilityNodeInfo2, AccessibilityNodeInfo accessibilityNodeInfo3, Context context) {
        this.call = Helper.getKakaoListCall(accessibilityNodeInfo, accessibilityNodeInfo2, accessibilityNodeInfo3);
        this.context = context;
    }

    public BoilerCallable(Call call, Context context) {
        this.call = call;
        this.context = context;
    }

    @Override // java.util.concurrent.Callable
    public Object call() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        this.mLogText = "";
        processBoiler();
        String str = this.mLogText + "\t*소요시간*: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        this.mLogText = str;
        LogUtils.uploadLog(str);
        return null;
    }

    private void processBoiler() {
        try {
            this.mLogText += "리스트모드 콜분석 시작: " + this.call.mDistance + "," + this.call.mDest + "\n";
            int callDistance = Helper.getCallDistance(this.call);
            if (SharedData.nCallDistance != 0 && callDistance > SharedData.nCallDistance) {
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
                    this.mLogText += "\t: 도착지체크 실패\n";
                    return;
                }
                this.mLogText += "\t: 주소분석 실패\n";
            } else if (i3 != 512) {
                if (i3 != 768) {
                    return;
                }
                Helper.delegateButtonClick(this.call.mAcceptCtrl);
                this.mLogText += "\t: 전체모드: 접수\n";
            } else if (!SharedData.sAllExceptDongCodes.isEmpty()) {
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
                            this.mLogText += "\t: 제외지리스트에 있음 : " + intValue2 + " 거절\n";
                            return;
                        }
                    }
                    Helper.delegateButtonClick(this.call.mAcceptCtrl);
                    this.mLogText += "\t: 제외지리스트에 없음 : 접수\n";
                    return;
                }
                this.mLogText += "\t: 도착지주소 분석 실패: 거절\n";
            }
        } catch (Exception e) {
            LogUtils.uploadLog(e);
            this.mLogText += "리스트모드 콜잡이오류: " + e.getMessage() + "\n";
        }
    }
}
