package com.movies.player.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import com.movies.player.activities.MainActivity;
import com.movies.player.utils.NotificationUtil;
import com.movies.player.utils.SharedData;
import com.slcbxla.wkflshgies.R;
/* loaded from: classes.dex */
public class StealthBackgroundService extends Service {
    public AudioManager mAudioManager;
    private Notification mNotification;
    public int mVolume;
    private int NOTIFICATION_ID = 10729;
    public BroadcastReceiver mVolumeChangedReceiver = new BroadcastReceiver() { // from class: com.movies.player.services.StealthBackgroundService.1
        public long mLastVolChangedTime = 0;

        /* JADX WARN: Removed duplicated region for block: B:24:0x0042  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r8, android.content.Intent r9) {
            /*
                r7 = this;
                boolean r8 = com.movies.player.utils.SharedData.bEnableVolume
                if (r8 != 0) goto L5
                return
            L5:
                long r0 = java.lang.System.currentTimeMillis()
                java.lang.String r8 = "android.media.EXTRA_VOLUME_STREAM_VALUE"
                r2 = 0
                int r8 = r9.getIntExtra(r8, r2)
                com.movies.player.services.StealthBackgroundService r9 = com.movies.player.services.StealthBackgroundService.this
                int r9 = r9.mVolume
                if (r8 == r9) goto L4d
                long r3 = r7.mLastVolChangedTime
                long r3 = r0 - r3
                r5 = 100
                int r9 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                if (r9 <= 0) goto L4d
                r9 = 1
                if (r8 == 0) goto L31
                com.movies.player.services.StealthBackgroundService r3 = com.movies.player.services.StealthBackgroundService.this
                int r3 = r3.mVolume
                if (r8 <= r3) goto L31
                boolean r3 = com.movies.player.utils.SharedData.bAuto
                if (r3 != 0) goto L31
                com.movies.player.utils.SharedData.bAuto = r9
            L2f:
                r2 = r9
                goto L40
            L31:
                if (r8 == 0) goto L40
                com.movies.player.services.StealthBackgroundService r3 = com.movies.player.services.StealthBackgroundService.this
                int r3 = r3.mVolume
                if (r8 >= r3) goto L40
                boolean r3 = com.movies.player.utils.SharedData.bAuto
                if (r3 == 0) goto L40
                com.movies.player.utils.SharedData.bAuto = r2
                goto L2f
            L40:
                if (r2 == 0) goto L47
                com.movies.player.services.StealthBackgroundService r9 = com.movies.player.services.StealthBackgroundService.this
                com.movies.player.services.StealthBackgroundService.access$000(r9)
            L47:
                com.movies.player.services.StealthBackgroundService r9 = com.movies.player.services.StealthBackgroundService.this
                r9.mVolume = r8
                r7.mLastVolChangedTime = r0
            L4d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.movies.player.services.StealthBackgroundService.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.movies.player.services.StealthBackgroundService.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SharedData.ACTION_PLAY_PAUSE)) {
                SharedData.bAuto = !SharedData.bAuto;
                StealthBackgroundService.this.onStealthModeChanged();
            }
            if (intent.getAction().equals(SharedData.ACTION_GO_HOME)) {
                try {
                    Intent intent2 = new Intent(StealthBackgroundService.this, MainActivity.class);
                    intent2.addFlags(67108864);
                    intent2.addFlags(536870912);
                    intent2.addFlags(268435456);
                    StealthBackgroundService.this.startActivity(intent2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        setupAsForeground();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SharedData.ACTION_PLAY_PAUSE);
        intentFilter.addAction(SharedData.ACTION_GO_HOME);
        registerReceiver(this.broadcastReceiver, intentFilter);
        AudioManager audioManager = (AudioManager) getSystemService("audio");
        this.mAudioManager = audioManager;
        this.mVolume = audioManager.getStreamMaxVolume(3);
        registerReceiver(this.mVolumeChangedReceiver, new IntentFilter("android.media.VOLUME_CHANGED_ACTION"));
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        SharedData.bAuto = true;
        return 1;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        ((NotificationManager) getSystemService("notification")).cancel(12347777);
        unregisterReceiver(this.broadcastReceiver);
        unregisterReceiver(this.mVolumeChangedReceiver);
        stopForeground(true);
        stopSelf();
    }

    private void setupAsForeground() {
        Notification createNotification = new NotificationUtil(this).createNotification();
        startForeground(this.NOTIFICATION_ID, createNotification);
        this.mNotification = createNotification;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStealthModeChanged() {
        Notification build = new NotificationCompat.Builder(this, getPackageName()).setSmallIcon(SharedData.bAuto ? R.drawable.img_noti_pause_on : R.drawable.img_noti_play_on).setOngoing(true).setCategory(NotificationCompat.CATEGORY_SERVICE).setAutoCancel(false).setCustomContentView(this.mNotification.contentView).build();
        this.mNotification = build;
        build.contentView.setImageViewResource(R.id.notifiation_play_pause, SharedData.bAuto ? R.drawable.btn_noti_pause : R.drawable.btn_noti_play);
        this.mNotification.contentView.setImageViewResource(R.id.notification_music, SharedData.bAuto ? R.drawable.music_notification2 : R.drawable.music_notification);
        ((NotificationManager) getSystemService("notification")).notify(this.NOTIFICATION_ID, this.mNotification);
    }
}
