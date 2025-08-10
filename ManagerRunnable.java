package com.movies.player.boiler.list;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.movies.player.utils.Helper;
import com.movies.player.utils.LogUtils;
import com.movies.player.utils.SharedData;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
/* loaded from: classes.dex */
public class ManagerRunnable implements Runnable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Context mContext;
    private final AccessibilityNodeInfo mListWindowNode;

    public ManagerRunnable(AccessibilityNodeInfo accessibilityNodeInfo, Context context) {
        this.mListWindowNode = accessibilityNodeInfo;
        this.mContext = context;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            try {
                doWork();
                doScroll();
            } catch (Exception e) {
                LogUtils.uploadLog(e);
            }
        } finally {
            SharedData.bManagerThreadRunning = false;
        }
    }

    private void doWork() throws ExecutionException, InterruptedException {
        AccessibilityNodeInfo parent;
        ArrayList<Future> arrayList = new ArrayList();
        for (AccessibilityNodeInfo accessibilityNodeInfo : Helper.getNodeListByViewId(this.mListWindowNode, "tv_destination")) {
            AccessibilityNodeInfo parent2 = accessibilityNodeInfo.getParent();
            if (parent2 != null && (parent = parent2.getParent()) != null) {
                AccessibilityNodeInfo nodeByViewId = Helper.getNodeByViewId(parent, "tv_origin_label_distance");
                AccessibilityNodeInfo nodeByViewId2 = Helper.getNodeByViewId(parent, "ll_accept_btn");
                if (nodeByViewId != null && nodeByViewId2 != null) {
                    String str = nodeByViewId.getText().toString() + " : " + accessibilityNodeInfo.getText().toString();
                    if (!SharedData.aProcessedCallList.contains(str)) {
                        SharedData.addProcessedCallText(str);
                        arrayList.add(SharedData.tWorkThreadPool.submit(new BoilerCallable(accessibilityNodeInfo, nodeByViewId, nodeByViewId2, this.mContext)));
                    }
                }
            }
        }
        for (Future future : arrayList) {
            future.get();
        }
    }

    private void doScroll() {
        AccessibilityNodeInfo accessibilityNodeInfo = this.mListWindowNode;
        if (accessibilityNodeInfo != null && accessibilityNodeInfo.getActionList().contains(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD)) {
            int recycleHeight = Helper.getRecycleHeight(this.mListWindowNode);
            final Bundle bundle = new Bundle();
            bundle.putInt(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_MOVE_WINDOW_Y, recycleHeight);
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.movies.player.boiler.list.ManagerRunnable$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ManagerRunnable.this.m37lambda$doScroll$0$commoviesplayerboilerlistManagerRunnable(bundle);
                }
            });
            return;
        }
        this.mListWindowNode.recycle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$doScroll$0$com-movies-player-boiler-list-ManagerRunnable  reason: not valid java name */
    public /* synthetic */ void m37lambda$doScroll$0$commoviesplayerboilerlistManagerRunnable(Bundle bundle) {
        this.mListWindowNode.performAction(4096, bundle);
        this.mListWindowNode.recycle();
    }
}
