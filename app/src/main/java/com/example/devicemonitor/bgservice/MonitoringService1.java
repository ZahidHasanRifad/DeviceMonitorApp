package com.example.devicemonitor.bgservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.devicemonitor.MainActivity;
import com.example.devicemonitor.R;

public class MonitoringService1 extends Service {


    private static final String CHANNEL_ID = "1";
    private final IBinder binder = new MonitoringBinder();
    String value = null;
    MediaPlayer player;

    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        MediaPlayer player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.setLooping(true);
        player.start();
        value = intent.getStringExtra("sendItem");
        return START_STICKY;
    }
*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }

    public class MonitoringBinder extends Binder{
        public MonitoringService1 getService(){
            return MonitoringService1.this;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        value = intent.getStringExtra("sendItem");
        player.setLooping(true);
        player.start();

        Intent intentnotification = new Intent(this, MainActivity.class);
        intentnotification.setFlags(intentnotification.FLAG_ACTIVITY_NEW_TASK | intentnotification.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentnotification, 0);
        createNotificationChannel();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Monitoring Notification")
                .setContentText(value)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        int notificationId = 101;
        notificationManagerCompat.notify(notificationId, notificationBuilder.build());
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        return super.onUnbind(intent);
    }

    public String getData(){
        return "value of sender "+value;
    }
}
