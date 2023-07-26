package com.example.homemaintanenceserviceapp.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import com.example.homemaintanenceserviceapp.R;

public class HMSNotification {

    private static final String CHANNEL_ID = "6969";
    private static final String CHANNEL_NAME = "com.example.homemaintanenceserviceapp.Notifications.HMS";
    private static final String CHANNEL_DESCRIPTION = "Worker Reached at Location.";
    public static void createNotificationChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(CHANNEL_DESCRIPTION);
        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
    public static void showNotification(Context context, String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(54, builder.build());
    }
}