package com.example.devicemonitor.bgservice;

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

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.devicemonitor.MainActivity;
import com.example.devicemonitor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class DataServerService extends Service {


    private static final String CHANNEL_ID = "1";
    private final IBinder binder = new DataServerBinder();
    String value = null;
    MediaPlayer player;
    String deviceDataUrl = "http://192.168.0.110:8000/server/addDevice";
    String appDataUrl = "http://192.168.0.110:8000/server/addAppData";
    String appPermissionStatsDataUrl = "http://192.168.0.110:8000/server/addAppPermissionStats";

    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        MediaPlayer player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.setLooping(true);
        player.start();
        value = intent.getStringExtra("sendItem");
        return START_STICKY;
    }
*/
    /*@Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }*/

    public class DataServerBinder extends Binder{
        public DataServerService getService(){
            return DataServerService.this;
        }
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(){
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
    }

    private JSONObject sendData(String url, JSONObject data){
        //String serverUrl = "";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JSONObject[] respnse = new JSONObject[1];
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                respnse[0] = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);
        return respnse[0];
    }

//    private void sendDeviceData(){
//        JSONObject deviceData = new JSONObject();
//        try {
//            deviceData.put("device_id","");
//            deviceData.put("total_apps","");
//            deviceData.put("registered_date","");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        sendData(deviceDataUrl,deviceData);
//    }
//
//    private void sendAppData(){
//        JSONObject appData = new JSONObject();
//        try {
//            appData.put("device_id","");
//            appData.put("app_name","");
//            appData.put("cpu_utilization","");
//            appData.put("ram_utilization","");
//            appData.put("storage_utilization","");
//            appData.put("net_utilization","");
//            appData.put("battery_utilization","");
//            appData.put("active_time","");
//            appData.put("data_update_time","");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        sendData(appDataUrl,appData);
//    }
//
//    private void sendAppPermissionStatsData(){
//        JSONObject appPermissionStatsData = new JSONObject();
//        try {
//            appPermissionStatsData.put("device_id","");
//            appPermissionStatsData.put("app_name","");
//            appPermissionStatsData.put("numof_granted_permissions","");
//            appPermissionStatsData.put("is_camera_prmsn_on","");
//            appPermissionStatsData.put("is_microphone_prmsn_on","");
//            appPermissionStatsData.put("is_storage_prmsn_on","");
//            appPermissionStatsData.put("data_update_time","");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        sendData(appPermissionStatsDataUrl,appPermissionStatsData);
//    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        value = intent.getStringExtra("sendItem");
        player.setLooping(true);
        player.start();
        String deviceId = intent.getStringExtra("deviceId");
        int totalApps = intent.getIntExtra("numapps",0);
        String[] appsName = intent.getStringArrayExtra("apps");
        String[] cpuses = intent.getStringArrayExtra("cpus");
        String[] rams = intent.getStringArrayExtra("rams");
        String[] nets = intent.getStringArrayExtra("nets");
        String[] storages = intent.getStringArrayExtra("storages");
        String[] batterys = intent.getStringArrayExtra("batterys");
        String[] activetimes = intent.getStringArrayExtra("activetimes");
        String[] totalpermisstions = intent.getStringArrayExtra("totalpermisstion");
        String[] camerapermissions = intent.getStringArrayExtra("camerapermission");
        String[] microphonepermissions = intent.getStringArrayExtra("microphonepermission");
        String[] storagepermissionReads = intent.getStringArrayExtra("storagepermissionRead");
        String[] storagepermissionWrites = intent.getStringArrayExtra("storagepermissionWrite");


        JSONObject deviceData = new JSONObject();
        try {
            deviceData.put("device_id",deviceId);
            deviceData.put("total_apps",totalApps);
            deviceData.put("registered_date", LocalDateTime.now());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendData(deviceDataUrl, deviceData);

        JSONObject appData = new JSONObject();
        try {
            appData.put("device", deviceData);
            appData.put("app_name",appsName);
            appData.put("cpu_utilization","");
            appData.put("ram_utilization","");
            appData.put("storage_utilization","");
            appData.put("net_utilization","");
            appData.put("battery_utilization","");
            appData.put("active_time","");
            appData.put("data_update_time",LocalDateTime.now());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //sendData(appDataUrl, appData);

        JSONObject appPermissionStatsData = new JSONObject();
        try {
            appPermissionStatsData.put("device", deviceData);
            appPermissionStatsData.put("app_name","");
            appPermissionStatsData.put("numof_granted_permissions","");
            appPermissionStatsData.put("is_camera_prmsn_on","");
            appPermissionStatsData.put("is_microphone_prmsn_on","");
            appPermissionStatsData.put("is_storage_prmsn_on","");
            appPermissionStatsData.put("data_update_time",LocalDateTime.now());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*Intent intentnotification = new Intent(this, MainActivity.class);
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
        notificationManagerCompat.notify(notificationId, notificationBuilder.build());*/
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
