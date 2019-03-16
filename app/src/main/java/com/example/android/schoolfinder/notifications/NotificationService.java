package com.example.android.schoolfinder.notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.schoolfinder.BuildConfig;
import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.example.android.schoolfinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Map;

//import com.example.android.schoolfinder.normalUsers.HomeActivity;

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = NotificationService.class.getSimpleName();
    private Config config = new Config();
    public Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            sendNotification(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public NotificationService() {
        super();
//        FirebaseInstanceId.getInstance()
//                .
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "onMessageRecieved() ----------------------- " + remoteMessage.toString());
        if (remoteMessage.getData() != null)
            getImage(remoteMessage);
    }

    /**
     * To send notification
     *
     * @param bitmap
     */
    private void sendNotification(Bitmap bitmap) {


        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(bitmap);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

//        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("School Finder Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(config.title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(config.content)
//                .setContentIntent(pendingIntent)
                .setStyle(style)
                .setLargeIcon(bitmap)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);


        if (notificationManager != null) {
            notificationManager.notify(1, notificationBuilder.build());
        }


    }

    /**
     * Get the image from the notification
     *
     * @param remoteMessage
     */
    private void getImage(final RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        config.title = data.get("title");
        config.content = data.get("content");
        config.imageUrl = data.get("imageUrl");
//        Config.gameUrl = data.get("gameUrl");
        //Create thread to fetch image from notification
        if (remoteMessage.getData() != null) {

            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Get image from data Notification
                    if (config.imageUrl != null && !config.imageUrl.isEmpty())
                        Picasso.get()
                                .load(config.imageUrl)
                                .into(target);
                }
            });
        }
    }

    @Override
    public void onNewToken(String s) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(BuildConfig.FLAVOR.equals("normalUsers") ? FirebaseConstants.NORMAL_USERS_NODE
                        : FirebaseConstants.SCHOOLS_USERS_NODE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseConstants.TOKEN)
                .setValue(s);
        Log.e(TAG, "onNewToken() ----------------------- " + s);
        super.onNewToken(s);
//        FirebaseMessaging.getInstance().subscribeToTopic()
    }
}
