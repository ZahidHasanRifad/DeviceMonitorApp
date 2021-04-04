package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.usage.StorageStats;
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
import android.os.UserHandle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.devicemonitor.bgservice.DataReceiverService;
import com.example.devicemonitor.bgservice.DataServerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    //private RecieveData recieveData;
    private DataServerService dataServerService;
    //private DataReceiverService dataReceiverService;
    boolean isSeviceBound = false;
    String deviceDataUrl = "http://192.168.0.109:8000/server/addDevice/";
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
        CardView storagebutton = findViewById(R.id.storagebutton);
        CardView batterybutton = findViewById(R.id.batterybutton);
        CardView devicebutton = findViewById(R.id.devicebutton);

        Switch monitorSwitch = findViewById(R.id.monitorswitch);

        Intent intentcpu = new Intent(this, CpuActivity.class);
        Intent intentmem = new Intent(this, RamActivity.class);
        Intent intentnet = new Intent(this, NetActivity.class);
        Intent intentprocess = new Intent(this, ProcessActivity.class);
        Intent intentstorage = new Intent(this, StorageActivity.class);
        Intent intentbattery = new Intent(this, BatteryActivity.class);
        Intent intentdevice = new Intent(this, DeviceActivity.class);


        //printApps(getUserInstalledApps());
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

        storagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentstorage);
            }
        });

        batterybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentbattery);
            }
        });

        devicebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentdevice);
            }
        });


        monitorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    JSONObject deviceData = new JSONObject();
                    //JSONArray deviceData = new JSONArray();
                    try {
                        deviceData.put("device_id", String.valueOf(generateDeviceId()));
                        deviceData.put("total_apps",getNumberOfTotalApp());
                        deviceData.put("registered_date", LocalDateTime.now());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    sendData(deviceDataUrl, deviceData);
                    //sendDeviceData(deviceData);
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
        intent.putExtra("camerapermission",isPermissionOn("android.permission.CAMERA"));
        intent.putExtra("microphonepermission",isPermissionOn("android.permission.RECORD_AUDIO"));
        intent.putExtra("storagepermissionRead",isPermissionOn("android.permission.READ_EXTERNAL_STORAGE"));
        intent.putExtra("storagepermissionWrite",isPermissionOn("android.permission.WRITE_EXTERNAL_STORAGE"));
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

    private JSONObject sendData(String url, JSONObject data){
        //String serverUrl = "";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JSONObject[] respnse = {new JSONObject()};
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                respnse[0] = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
        //requestQueue.start();
        return respnse[0];
    }

    private String sendDeviceData(JSONArray jsonArray){
        final String[] res = new String[1];

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, deviceDataUrl, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    res[0] = response.getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
        return res[0];
    }

    private int getNumberOfTotalApp(){
        int totalApp,tem;
        totalApp = getPackageManager().getInstalledApplications(PackageManager.MATCH_SYSTEM_ONLY).size();
        tem = getPackageManager().getInstalledApplications(0).size()-totalApp;
        //totalApp = getPackageManager().getInstalledPackages(0).size();
        return tem;
    }

    private List<ApplicationInfo> getUserInstalledAppsAsApplication(){
        List<ApplicationInfo> systemApplist = getPackageManager().getInstalledApplications(PackageManager.MATCH_SYSTEM_ONLY);
        List<ApplicationInfo> totalAppList = getPackageManager().getInstalledApplications(0);
        List<ApplicationInfo> userAppsList = new ArrayList<>();

        String[] apn = new String[totalAppList.size()];
        String[] spn = new String[systemApplist.size()];
        int i = 0, j = 0;
        for (ApplicationInfo all : totalAppList){
            apn[i] = all.packageName;
            i++;
        }
        for (ApplicationInfo sys : systemApplist){
            spn[j] = sys.packageName;
            j++;
        }
        //allPackage.addAll(sysPackage);
        List<String> l1 = new ArrayList<>(Arrays.asList(apn));
        List<String> l2 = new ArrayList<>(Arrays.asList(spn));
        Set<String> s1 = new HashSet<>(l1);
        Set<String> s2 = new HashSet<>(l2);
        s1.retainAll(s2);
        l1.removeAll(s1);
        l2.removeAll(s1);

        l1.addAll(l2);

        Set<String> user = new HashSet<>(l1);

        for (ApplicationInfo app : totalAppList){
            if (user.contains(app.packageName)){
                userAppsList.add(app);
            }
        }

        return userAppsList;
    }

    private List<PackageInfo> getUserInstalledAppsAsPackage(){
        List<String> appnames = new LinkedList<>();

        //PackageManager packageManager = getPackageManager();
//        List<ApplicationInfo> systemApplist = getPackageManager().getInstalledApplications(PackageManager.MATCH_SYSTEM_ONLY);
//        List<ApplicationInfo> totalAppList = getPackageManager().getInstalledApplications(0);
        List<PackageInfo> sysPackage = getPackageManager().getInstalledPackages(PackageManager.MATCH_SYSTEM_ONLY);
        List<PackageInfo> allPackage = getPackageManager().getInstalledPackages(0);
        List<PackageInfo> userAppsList = new ArrayList<>();
        String[] apn = new String[allPackage.size()];
        String[] spn = new String[sysPackage.size()];
        int i = 0, j = 0;
        for (PackageInfo all : allPackage){
            apn[i] = all.packageName;
            i++;
        }
        for (PackageInfo sys : sysPackage){
            spn[j] = sys.packageName;
            j++;
        }
        //allPackage.addAll(sysPackage);
        List<String> l1 = new ArrayList<>(Arrays.asList(apn));
        List<String> l2 = new ArrayList<>(Arrays.asList(spn));
        Set<String> s1 = new HashSet<>(l1);
        Set<String> s2 = new HashSet<>(l2);
        s1.retainAll(s2);
        l1.removeAll(s1);
        l2.removeAll(s1);

        l1.addAll(l2);

        Set<String> user = new HashSet<>(l1);

        for (PackageInfo app : allPackage){
            if (user.contains(app.packageName)){
                userAppsList.add(app);
            }
        }
//        System.out.println("////////////////////////////////////////////////////////");
        System.out.println(userAppsList.size());
        for (PackageInfo a : userAppsList){
            //System.out.println(a.packageName);
        }
        
        return userAppsList;
    }

    private void printApps(List<PackageInfo> apps){
        //System.out.println("Total Installed Apps "+ String.valueOf(apps.size()));
        for (PackageInfo app: apps){
            //System.out.println(app.flags);
        }

    }

    private String[] getAppsName(){
        String[] appsName = new String[getUserInstalledAppsAsPackage().size()];
        int i = 0;
        for (PackageInfo app : getUserInstalledAppsAsPackage()){
            appsName[i] = getAppName(app);
            i++;
        }
        return appsName;
    }

    private String getAppName(PackageInfo app){
        String name = app.packageName;
//        String[] packageName = app.processName.split(".");
//        name = packageName[packageName.length-1];
        return name;
    }

    private List<String> getAppGrantedPermission(String packageName){
        List<String> granted = new LinkedList<>();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            //System.out.println(packageInfo.packageName);
            if (packageInfo != null){
                for (int j = 0; j<packageInfo.requestedPermissions.length; j++){
                    if ((packageInfo.requestedPermissionsFlags[j] & PackageInfo.REQUESTED_PERMISSION_GRANTED)==PackageInfo.REQUESTED_PERMISSION_GRANTED){
                        granted.add(packageInfo.requestedPermissions[j]);
                        //System.out.println(packageInfo.requestedPermissions[j]);
                    }
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return granted;
    }

    private String[] getAppsGrantedTotalPermission(){
        List<PackageInfo> apps = getUserInstalledAppsAsPackage();
        String[] agtp = new String[apps.size()];
        int i = 0;
        for (PackageInfo app : apps){
                List<String> granted = getAppGrantedPermission(app.packageName);
                agtp[i] = getAppName(app)+":"+String.valueOf(granted.size());
            //System.out.println(app.processName);
                i++;
        }
        //packageManager.get
        return agtp;
    }

    private String[] isPermissionOn(String permission){
        List<PackageInfo> apps = getUserInstalledAppsAsPackage();
        String [] icp = new String[apps.size()];
        int i = 0;
        for (PackageInfo app : apps){
            List<String> granted = getAppGrantedPermission(app.packageName);
            if (granted.contains(permission)){
                icp[i] = getAppName(app)+":"+"1";
            }else icp[i] = getAppName(app)+":"+"0";
            i++;
        }
        return icp;
    }

    private String[] getStorageStats(){
        List<ApplicationInfo> apps = getUserInstalledAppsAsApplication();
        String[] ss = new String[apps.size()];
        // UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService("usagestats");
        StorageStatsManager storageStatsManager = (StorageStatsManager) getSystemService(STORAGE_STATS_SERVICE);
        int i = 0;
        for (ApplicationInfo app : apps){
            try {
                Long appStorage  = storageStatsManager.getTotalBytes(app.storageUuid);
//                try {
//                    StorageStats storageStats = storageStatsManager.queryStatsForPackage(app.storageUuid, app.packageName, UserHandle.getUserHandleForUid(app.uid));
//                    appStorage = storageStats.getAppBytes();
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }

                appStorage /= (1024 * 1024);
                //System.out.println(app.packageName + " " + String.valueOf(appStorage));
                ss[i] = app.packageName+":"+String.valueOf(appStorage);
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ss;
    }
}