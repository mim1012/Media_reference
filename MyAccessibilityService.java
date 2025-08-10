package com.movies.player.services;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.movies.player.boiler.general.BoilerRunnable;
import com.movies.player.boiler.list.ManagerRunnable;
import com.movies.player.utils.Call;
import com.movies.player.utils.Constants;
import com.movies.player.utils.Helper;
import com.movies.player.utils.LogUtils;
import com.movies.player.utils.SharedData;
import java.util.List;
/* loaded from: classes.dex */
public class MyAccessibilityService extends AccessibilityService {
    private Call lastGeneralCall = null;

    @Override // android.accessibilityservice.AccessibilityService
    public void onInterrupt() {
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        try {
            AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
            if (rootInActiveWindow != null) {
                if (SharedData.bAuto && accessibilityEvent.getPackageName().toString().equals(Constants.TAXI_DRIVER_APP_PACKAGE)) {
                    processKakao(rootInActiveWindow);
                } else {
                    clearLastGeneralCall();
                }
                rootInActiveWindow.recycle();
            }
        } catch (Exception e) {
            LogUtils.uploadLog(e);
        }
    }

    private void processKakao(AccessibilityNodeInfo accessibilityNodeInfo) {
        Call kakaoGeneralCall;
        Call call;
        AccessibilityNodeInfo nodeByViewId;
        try {
            List<AccessibilityNodeInfo> nodeListByViewId = Helper.getNodeListByViewId(accessibilityNodeInfo, "v_arrow");
            List<AccessibilityNodeInfo> nodeListByViewId2 = Helper.getNodeListByViewId(accessibilityNodeInfo, "lv_call_list");
            if (!Helper.isValidNodes(nodeListByViewId) && !Helper.isValidNodes(nodeListByViewId2)) {
                clearLastGeneralCall();
            } else if (Helper.isValidNodes(accessibilityNodeInfo.findAccessibilityNodeInfosByText("배차가 완료된 콜입니다.")) && (nodeByViewId = Helper.getNodeByViewId(accessibilityNodeInfo, "tv_btn_delete_completed")) != null) {
                clearLastGeneralCall();
                Helper.delegateButtonClick(nodeByViewId);
            } else {
                if (Helper.isValidNodes(nodeListByViewId) && (kakaoGeneralCall = Helper.getKakaoGeneralCall(accessibilityNodeInfo)) != null && ((call = this.lastGeneralCall) == null || !Helper.equalGeneralCalls(call, kakaoGeneralCall))) {
                    setLastGeneralCall(kakaoGeneralCall);
                    SharedData.tWorkThreadPool.submit(new BoilerRunnable(kakaoGeneralCall, this));
                }
                if (Helper.isValidNodes(nodeListByViewId2)) {
                    clearLastGeneralCall();
                    if (SharedData.nWorkMode == 1024 || SharedData.bManagerThreadRunning) {
                        return;
                    }
                    SharedData.bManagerThreadRunning = true;
                    SharedData.tWorkThreadPool.submit(new ManagerRunnable(nodeListByViewId2.get(0), this));
                }
            }
        } catch (Exception e) {
            LogUtils.uploadLog(e);
        }
    }

    private void setLastGeneralCall(Call call) {
        this.lastGeneralCall = call;
    }

    private void clearLastGeneralCall() {
        this.lastGeneralCall = null;
    }
}
