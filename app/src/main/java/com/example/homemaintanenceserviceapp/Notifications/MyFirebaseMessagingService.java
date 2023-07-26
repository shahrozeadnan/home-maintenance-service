package com.example.homemaintanenceserviceapp.Notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.homemaintanenceserviceapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

@SuppressLint("NewApi")
public class MyFirebaseMessagingService extends FirebaseMessagingService {
        @Override
        public void onMessageReceived(@NonNull RemoteMessage message) {
            super.onMessageReceived(message);
            String title = message.getNotification().getTitle();
            String body = message.getNotification().getBody();
            String channelID = "MESSAGE";
            NotificationChannel channel = new NotificationChannel(
                    channelID,
                    "notification", NotificationManager.IMPORTANCE_HIGH);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(this, channelID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.notification)
                    .setAutoCancel(true);


            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1002, notification.build());

        }
    }
    /*@Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        getFirebaseMessage(message.getNotification().getTitle(),message.getNotification().getBody());
    }
    public void getFirebaseMessage(String title,String msg)
    {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"myFirebasechannel")
                .setSmallIcon(R.drawable.notification).setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);
        managerCompat.notify(101,builder.build());

    }*/
