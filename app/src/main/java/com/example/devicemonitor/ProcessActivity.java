package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ProcessActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        TextView total = findViewById(R.id.totalappv);
        TextView sys = findViewById(R.id.totalsysv);
        TextView user = findViewById(R.id.userappsv);
        total.setText(String.valueOf(getPackageManager().getInstalledApplications(0).size()));
        sys.setText(String.valueOf(getPackageManager().getInstalledApplications(PackageManager.MATCH_SYSTEM_ONLY).size()));
        user.setText(String.valueOf(getNumberOfTotalApp()));
        //bgapps.setText(getBgAppInfo());


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

    private List<PackageInfo> getenabledPackags(){
        List<PackageInfo> packageInfos = getUserInstalledAppsAsPackage();
        List<PackageInfo> en = new ArrayList<>();
        for (PackageInfo app : packageInfos){
            if (app.applicationInfo.enabled){
                en.add(app);
                System.out.println(app.packageName);
            }
        }
        return en;
    }

    private int getBgTotalApps(){

        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();
        return runningAppProcessInfo.size();
        //return am.getRunningServices(100).size();
    }

    /*private String getBgAppInfo(){

        StringBuffer sb = new StringBuffer();
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //PackageInfo packageInfo = ActivityManager.RunningAppProcessInfo.IMPORTANCE_CACHED;
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();
        //List<ActivityManager.RunningTaskInfo> runningAppProcessInfo = am.getRunningTasks(10);
        String[] pkgList = new String[50];
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo1 : runningAppProcessInfo){
            sb.append(runningAppProcessInfo1.toString());
            pkgList = runningAppProcessInfo1.pkgList;

        }
        for (int i =0; i<pkgList.length; i++){

        }
        return sb.toString();
    }*/
}