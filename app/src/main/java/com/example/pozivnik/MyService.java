package com.example.pozivnik;

import static com.example.pozivnik.R.drawable;
import static com.example.pozivnik.R.string;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    public MyService() {
    }

    private static final int NOTIF_ID = 1;
    String channelId = "some_channel_id";
    CharSequence channelName = "Pokaži delovanje";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground();
        }else{
            startNoForeground();
        }
    }
    public void startForeground(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);

            Intent notificationIntent = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                    channelId) // don't forget create a notification channel first
                    .setOngoing(true)
                    .setSmallIcon(drawable.ic_launcher_icon)
                    .setContentTitle(getString(string.main_app_name))
                    .setContentText(getString(string.main_app_name) + " "+ getString(string.runningBackground))
                    .setContentIntent(pendingIntent)
                    .build());
        }
    }
    public void startNoForeground(){
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(drawable.ic_launcher_icon)
                .setContentText(getString(string.main_app_name) + " "+ getString(string.runningBackground))
                .setContentIntent(pendingIntent).build();

        startForeground(NOTIF_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.getIntExtra("calling-activity",0) != 100){
            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startIntent.putExtra("calling-activity", 112);
            String number = intent.getStringExtra("number");
            String message = intent.getStringExtra("message");

            SharedPreferences preferences = this.getSharedPreferences("MyPrefs",0);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("number",number);
            editor.putString("message", message);
            editor.apply();
            startActivity(startIntent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }
}
