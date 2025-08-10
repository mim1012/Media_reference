package com.movies.player.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.slcbxla.wkflshgies.R;
/* loaded from: classes.dex */
public class NotificationUtil extends ContextWrapper {
    private String id;
    private NotificationChannel mChannel;
    private Context mContext;
    private String name;

    public NotificationUtil(Context context) {
        super(context);
        this.mContext = context;
        this.id = context.getPackageName();
        this.name = context.getPackageName();
    }

    public Notification createNotification() {
        createChannel();
        Context applicationContext = getApplicationContext();
        RemoteViews remoteViews = new RemoteViews(getPackageName(), (int) R.layout.notification);
        remoteViews.setOnClickPendingIntent(R.id.notifiation_play_pause, PendingIntent.getBroadcast(this.mContext, 7776, new Intent(SharedData.ACTION_PLAY_PAUSE), 134217728));
        remoteViews.setOnClickPendingIntent(R.id.notification_music, PendingIntent.getBroadcast(this.mContext, 7777, new Intent(SharedData.ACTION_GO_HOME), 134217728));
        return new NotificationCompat.Builder(applicationContext, this.id).setSmallIcon(R.drawable.img_noti_pause_on).setOngoing(true).setCategory(NotificationCompat.CATEGORY_SERVICE).setAutoCancel(false).setCustomContentView(remoteViews).build();
    }

    private void createChannel() {
        if (this.mChannel != null) {
            return;
        }
        this.mChannel = new NotificationChannel(this.id, this.name, 4);
        NotificationManagerCompat.from(getApplicationContext()).createNotificationChannel(this.mChannel);
    }
}
