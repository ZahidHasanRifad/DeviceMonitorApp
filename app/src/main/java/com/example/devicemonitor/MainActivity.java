package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.usage.StorageStatsManager;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devicemonitor.bgservice.DataReceiverService;
import com.example.devicemonitor.bgservice.DataServerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //private RecieveData recieveData;
    private DataServerService dataServerService;
    //private DataReceiverService dataReceiverService;
    boolean isSeviceBound = false;
    //boolean dataReceiverBound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DataServerService.DataServerBinder binder = (DataServerService.DataServerBinder) service;
            dataServerService = binder.getService();
            isSeviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isSeviceBound = false;
        }
    };

    /*private ServiceConnection dataReceiverConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DataReceiverService.DataReceiverBinder binder = (DataReceiverService.DataReceiverBinder) service;
            dataReceiverService = binder.getService();
            dataReceiverBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            dataReceiverBound = false;
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setUpServiceReciever();

        TextView totalApp =findViewById(R.id.textview);
        //totalApp.setText(String.valueOf(getNumberofApp("sys")));
        totalApp.setText(String.valueOf(getNumberOfTotalApp()));

        CardView cpubutton = findViewById(R.id.cpubutton);
        CardView memorybutton = findViewById(R.id.membutton);
        CardView netbutton = findViewById(R.id.networkbutton);
        CardView processbutton = findViewById(R.id.appsbutton);

        Switch monitorSwitch = findViewById(R.id.monitorswitch);

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

        processbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentprocess);
            }
        });

        monitorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    launchMonitoringService();
                    if (isSeviceBound) {
                        String data = dataServerService.getData();
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    stopMonitoringService();
                    Toast.makeText(MainActivity.this, "Monitoring Service Stopped", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void launchMonitoringService(){
        Intent intent = new Intent(this, DataServerService.class);
        intent.putExtra("deviceId", generateDeviceId());
        intent.putExtra("numapps", getNumberOfTotalApp());
        intent.putExtra("apps",getAppsName()); // value is installed applist name only
        intent.putExtra("cpus","");//value is a applist
        intent.putExtra("rams","");//value is a applist
        intent.putExtra("nets","");//value is a applist
        intent.putExtra("storages",getStorageStats());//value is a applist
        intent.putExtra("batterys","");
        intent.putExtra("activetimes","");
        intent.putExtra("totalpermisstion",getAppsGrantedTotalPermission());
        intent.putExtra("camerapermission",isPermissionOn("CAMERA"));
        intent.putExtra("microphonepermission",isPermissionOn("RECORD_AUDIO"));
        intent.putExtra("storagepermissionRead",isPermissionOn("READ_EXTERNAL_STORAGE"));
        intent.putExtra("storagepermissionWrite",isPermissionOn("WRITE_EXTERNAL_STORAGE"));
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
        //startService(intent);
    }

    private void stopMonitoringService(){
        //Intent intent = new Intent(this, MonitoringService1.class);
        unbindService(connection);
        isSeviceBound = false;

        //stopService(intent);
    }

    private String generateDeviceId(){
        String deviceId = null;
        Random random = new Random();
        int id= random.nextInt(999999);
        deviceId = String.valueOf(id);
        return deviceId;
    }


    private int getNumberOfTotalApp(){
        int totalApp,tem;
        totalApp = getPackageManager().getInstalledApplications(PackageManager.MATCH_SYSTEM_ONLY).size();
        tem = getPackageManager().getInstalledApplications(0).size()-totalApp;
        //totalApp = getPackageManager().getInstalledPackages(0).size();
        return tem;
    }

    private List<ApplicationInfo> getUserInstalledApps(){
        List<String> appnames = new LinkedList<>();

        //PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> systemApplist = getPackageManager().getInstalledApplications(PackageManager.MATCH_SYSTEM_ONLY);
        List<ApplicationInfo> totalAppList = getPackageManager().getInstalledApplications(0);
        List<ApplicationInfo> userAppsList = new LinkedList<>();
        for (ApplicationInfo app : systemApplist){
            if (!totalAppList.contains(app)){
                userAppsList.add(app);
            }
        }
        return userAppsList;
    }

    private String[] getAppsName(){
        String[] appsName = new String[getUserInstalledApps().size()];
        int i = 0;
        for (ApplicationInfo app : getUserInstalledApps()){
            appsName[i] = app.name;
            i++;
        }
        return appsName;
    }

    private List<String> getAppGrantedPermission(String packageName){
        List<String> granted = new LinkedList<>();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            for (int j = 0; j<packageInfo.requestedPermissions.length; j++){
                if ((packageInfo.requestedPermissionsFlags[j] & PackageInfo.REQUESTED_PERMISSION_GRANTED)==PackageInfo.REQUESTED_PERMISSION_GRANTED){
                    granted.add(packageInfo.requestedPermissions[j]);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return granted;
    }

    private String[] getAppsGrantedTotalPermission(){
        List<ApplicationInfo> applicationInfos = getUserInstalledApps();
        String[] agtp = new String[applicationInfos.size()];
        int i = 0;
        for (ApplicationInfo app : applicationInfos){
                List<String> granted = getAppGrantedPermission(app.packageName);
                agtp[i] = app.name+":"+String.valueOf(granted.size());
                i++;
        }
        //packageManager.get
        return agtp;
    }

    private String[] isPermissionOn(String permission){
        List<ApplicationInfo> apps = getUserInstalledApps();
        String [] icp = new String[apps.size()];
        int i = 0;
        for (ApplicationInfo app : apps){
            List<String> granted = getAppGrantedPermission(app.packageName);
            if (granted.contains(permission)){
                icp[i] = app.name+":"+"1";
            }else icp[i] = app.name+":"+"0";
            i++;
        }
        return icp;
    }

    private String[] getStorageStats(){
        List<ApplicationInfo> apps = getUserInstalledApps();
        String[] ss = new String[apps.size()];
        // UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService("usagestats");
        StorageStatsManager storageStatsManager = (StorageStatsManager) getSystemService(STORAGE_STATS_SERVICE);
        int i = 0;
        for (ApplicationInfo app : apps){
            try {
                Long appStorage = storageStatsManager.getTotalBytes(app.storageUuid);
                appStorage = appStorage/(1024*1024);
                ss[i] = app.name+":"+String.valueOf(appStorage);
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ss;
    }

}