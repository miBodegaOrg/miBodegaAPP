package com.mibodega.mystore.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mibodega.mystore.R;
import com.mibodega.mystore.views.signIn.SignInActivity;

public class FCMNotificationService extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        System.out.println("From: " + remoteMessage.getFrom());
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            System.out.println("Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        System.out.println("Message data payload: " + remoteMessage.getData().get("imagen"));
        //sendNotification2(remoteMessage.getFrom(),remoteMessage.getNotification().getBody());
        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),
                remoteMessage.getData().get("imagen") == null ? "" : remoteMessage.getData().get("imagen"));
    }
    private void sendNotification2(String from, String body) {
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                Toast.makeText(FCMNotificationService.this.getApplicationContext(), from +" ->"+body, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendNotification(String messageTitle,String messageBody, String imagen) {
        NotificationCompat.BigPictureStyle STYLE = null;

        Intent intent = new Intent(this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        String channelId = "fcm_default_channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.baseline_notifications_24)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        if (!imagen.equals("")){
            Bitmap imageBitmap = getBitmapFromUrl(imagen);
            STYLE = new NotificationCompat.BigPictureStyle()
                    .bigPicture(imageBitmap)
                    .bigLargeIcon((Bitmap) null); // Opcional, si deseas un ícono grande
            notificationBuilder.setStyle(STYLE);
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
    public Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            java.net.URL url = new java.net.URL(imageUrl);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            java.io.InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
