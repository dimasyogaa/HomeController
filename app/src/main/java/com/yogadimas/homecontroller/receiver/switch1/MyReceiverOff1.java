package com.yogadimas.homecontroller.receiver.switch1;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;


import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.FirebaseDatabase;
import com.yogadimas.homecontroller.BuildConfig;
import com.yogadimas.homecontroller.activity.MainActivity;
import com.yogadimas.homecontroller.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

public class MyReceiverOff1 extends BroadcastReceiver {

    public static final String CHANNEL_ID = "Channel_11";
    public static final String CHANNEL_NAME = "Timer";
    public final static int ID_SWITCH_OFF_1 = 12;


    private static boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = context.getString(R.string.app_name);
        String message = context.getString(R.string.msg_notification_switch_off_1);
        showReminderNotification(context, title, message);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference(BuildConfig.RELAY1).setValue(0);

    }

    public void setTimerOff1(Context context, String time) {
        String TIME_FORMAT = "HH:mm";
        if (isDateInvalid(time, TIME_FORMAT)) return;
        String[] timeArray = time.split(":");

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, MyReceiverOff1.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_SWITCH_OFF_1, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

    }

    public void cancelTimer(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, MyReceiverOff1.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_SWITCH_OFF_1,
                intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);

        }

    }

    private void showReminderNotification(Context context, String title, String message) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService
                (Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, ID_SWITCH_OFF_1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setAutoCancel(true);
        builder.setColor(ContextCompat.getColor(context, android.R.color.transparent));
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }

        Notification notification = builder.build();
        notificationManager.notify(ID_SWITCH_OFF_1, notification);

    }


}