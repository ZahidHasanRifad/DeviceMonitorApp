package com.example.devicemonitor.bgservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MonitoringService extends JobIntentService {

    /*public static final String ACTION = "com.example.devicemonitor.bgservice.MonitoringService.java";*/
    public MonitoringService(){
        super();
    }

    /*@Override
    public void onCreate() {
        super.onCreate();

    }*/

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        RecieveData recieveData = intent.getParcelableExtra("reciever");
        String value = intent.getStringExtra("sendItem");

        Bundle bundle = new Bundle();
        bundle.putString("resultValue", "result value passed in "+value);

        recieveData.send(Activity.RESULT_OK, bundle);
    }

    /*@Override
    protected void onHandleWork(@NonNull Intent intent) {
        String value = intent.getStringExtra("sendItem");
        Intent in = new Intent(Intent.ACTION_SENDTO);

        in.putExtra("resultCode", Activity.RESULT_OK);
        in.putExtra("resultValue", "Starting Background Service"+value);

        LocalBroadcastManager.getInstance(this).sendBroadcast(in);
    }*/
}
