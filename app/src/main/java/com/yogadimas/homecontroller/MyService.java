package com.yogadimas.homecontroller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.yogadimas.homecontroller.activity.TimerActivity;

public class MyService extends Service {
    public static final String CHANNEL_ID = "ForeverChannel";
    public static final String CHANNEL_NAME = "Timer";
private static final String TAG = MyService.class.getSimpleName();
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String title = this.getString(R.string.app_name);
        String message = this.getString(R.string.msg_notification_service);

        Intent notificationIntent = new Intent(this, TimerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
               0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setAutoCancel(false);
        builder.setColor(ContextCompat.getColor(this, android.R.color.transparent));
        builder.setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableVibration(true);

            builder.setChannelId(CHANNEL_ID);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(mChannel);
        }

        Notification notification = builder.build();
        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }





}