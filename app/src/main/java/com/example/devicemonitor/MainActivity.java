package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ApplicationErrorReport;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView totalApp =findViewById(R.id.textview);
        totalApp.setText(String.valueOf(getNumberOfTotalApp()));

        CardView cpubutton = findViewById(R.id.cpubutton);
        CardView memorybutton = findViewById(R.id.membutton);
        CardView netbutton = findViewById(R.id.networkbutton);
        //CardView processbutton = findViewById(R.id.bgprocessbutton);

        Intent intentcpu = new Intent(this, CpuActivity.class);
        Intent intentmem = new Intent(this, RamActivity.class);
        Intent intentnet = new Intent(this, NetActivity.class);
        Intent intentprocess = new Intent(this, ProcessActivity.class);

        cpubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentcpu);
            }
        });

        memorybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentmem);
            }
        });

        netbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentnet);
            }
        });

        /*processbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentprocess);
            }
        });*/

    }
    private int getNumberOfTotalApp(){
        int totalApp;
        totalApp = getPackageManager().getInstalledApplications(0).size();
        return totalApp;
    }

    private int getNumberofApp(String s){
        int totalSystemApp = 0;
        int totalUserInstalledApp = 0;
        List<ApplicationInfo> applist = getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo app : applist){
            int flag = app.flags;

            if ((flag == ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) || (flag == ApplicationInfo.FLAG_SYSTEM)){
                totalSystemApp++;
            }else totalUserInstalledApp++;
        }
        if (s.equals("sys"))return totalSystemApp;
        else return totalUserInstalledApp;
    }

    private void getCpuUtilization(){

    }
}