package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class ProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        TextView totalbgapp = findViewById(R.id.totalbgproc);
        TextView bgapps = findViewById(R.id.bgapps);

        totalbgapp.setText(String.valueOf(getBgTotalApps()));
        bgapps.setText(getBgAppInfo());

    }

    private int getBgTotalApps(){

        //ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();
        return runningAppProcessInfo.size();
        //return am.getRunningServices(100).size();
    }

    private String getBgAppInfo(){

        StringBuffer sb = new StringBuffer();
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo1 : runningAppProcessInfo){
            sb.append(runningAppProcessInfo1.processName);
        }
        return sb.toString();
    }
}