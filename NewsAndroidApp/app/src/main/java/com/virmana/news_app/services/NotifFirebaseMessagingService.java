package com.virmana.news_app.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.virmana.news_app.R;
import com.virmana.news_app.Provider.PrefManager;
import com.virmana.news_app.ui.Activities.CategoryActivity;
import com.virmana.news_app.ui.Activities.LoadActivity;
import com.virmana.news_app.ui.Activities.MainActivity;
import com.virmana.news_app.ui.Activities.UserActivity;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tamim on 26/10/2017.
 */

public class NotifFirebaseMessagingService  extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String type = remoteMessage.getData().get("type");
        String id = remoteMessage.getData().get("id");
        String title = remoteMessage.getData().get("title");
        String image = remoteMessage.getData().get("image");
        String icon = remoteMessage.getData().get("icon");
        String message = remoteMessage.getData().get("message");
        String language = remoteMessage.getData().get("language");
        PrefManager prf = new PrefManager(getApplicationContext());

        if (!prf.getString("notifications").equals("false")) {
            String default_language=prf.getString("LANGUAGE_DEFAULT");

            if (type.equals("post")) {
                if (language !=null){
                    Boolean shouldDisplay = false;
                    String[] languages =  language.split("_");
                    for (int i = 0; i < languages.length; i++) {
                        Log.v("MYOWN",default_language+ "-"+languages[i]);
                        if (default_language.equals(languages[i])){
                            shouldDisplay=true;
                        }
                    }
                    if (shouldDisplay) {
                        sendNotification(
                                id,
                                title,
                                image,
                                icon,
                                message
                        );
                    }
                }else{
                    sendNotification(
                            id,
                            title,
                            image,
                            icon,
                            message
                    );
                }
            }else if (type.equals("link")) {
                if (default_language.equals(language)) {
                    String link = remoteMessage.getData().get("link");
                    sendNotificationUrl(
                            id,
                            title,
                            image,
                            icon,
                            message,
                            link
                    );
                }
            }else if(type.equals("category")){

                if (default_language.equals(language)) {
                    String category_title = remoteMessage.getData().get("title_category");
                    String category_image = remoteMessage.getData().get("image_category");

                    sendNotificationCategory(
                            id,
                            title,
                            image,
                            icon,
                            message,
                            category_title,
                            category_image);
                }
            }else if(type.equals("user")){
                String name_user = remoteMessage.getData().get("name_user");
                String image_user = remoteMessage.getData().get("image_user");

                sendNotificationUser(
                        id,
                        title,
                        image,
                        icon,
                        message,
                        name_user,
                        image_user);
            }
        }
    }


    private void sendNotificationUser(
            String id,
            String title,
            String imageUri,
            String iconUrl,
            String message,
            String name_user,
            String image_user
    ) {

        Bitmap image = getBitmapfromUrl(imageUri);
        Bitmap icon = getBitmapfromUrl(iconUrl);
        Intent intent = new Intent(this, UserActivity.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));


        intent.putExtra("id", Integer.parseInt(id));
        intent.putExtra("name",name_user);
        intent.putExtra("image",image_user);
        intent.putExtra("from", "notification");




        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        int NOTIFICATION_ID = Integer.parseInt(id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_w)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);

        if (icon!=null){
            builder.setLargeIcon(icon);

        }else{
            builder.setLargeIcon(largeIcon);
        }
        if (image!=null){
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(LoadActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }
    private void sendNotificationCategory(
            String id,
            String title,
            String imageUri,
            String iconUrl,
            String message,
            String category_title,
            String category_image
    ) {


        Bitmap image = getBitmapfromUrl(imageUri);
        Bitmap icon = getBitmapfromUrl(iconUrl);
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));


        intent.putExtra("id", Integer.parseInt(id));
        intent.putExtra("title",category_title);
        intent.putExtra("image",category_image);
        intent.putExtra("from", "notification");




        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        int NOTIFICATION_ID = Integer.parseInt(id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_w)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);

        if (icon!=null){
            builder.setLargeIcon(icon);

        }else{
            builder.setLargeIcon(largeIcon);
        }
        if (image!=null){
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(LoadActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }

    private void sendNotificationUrl(
            String id,
            String title,
            String imageUri,
            String iconUrl,
            String message,
            String url
    ) {


        Bitmap image = getBitmapfromUrl(imageUri);
        Bitmap icon = getBitmapfromUrl(iconUrl);


        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse(url));
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        int NOTIFICATION_ID = Integer.parseInt(id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_w)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);

        if (icon!=null){
            builder.setLargeIcon(icon);

        }else{
            builder.setLargeIcon(largeIcon);
        }
        if (image!=null){
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());



    }


    private void sendNotification(
            String id,
            String title,
            String imageUri,
            String iconUrl,
            String message
    ){
        Bitmap image = getBitmapfromUrl(imageUri);
        Bitmap icon = getBitmapfromUrl(iconUrl);
        Intent intent = new Intent(this, LoadActivity.class);


        intent.putExtra("from","notification");
        intent.putExtra("id",Integer.parseInt(id));

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        int NOTIFICATION_ID = Integer.parseInt(id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_w)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);

        if (icon!=null){
            builder.setLargeIcon(icon);

        }else{
            builder.setLargeIcon(largeIcon);
        }
        if (image!=null){
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }




    /*
    *To get a Bitmap image from the URL received
    * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}