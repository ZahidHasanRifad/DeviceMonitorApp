package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devicemonitor.bgservice.MonitoringService;
import com.example.devicemonitor.bgservice.MonitoringService1;
import com.example.devicemonitor.bgservice.RecieveData;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private RecieveData recieveData;
    private MonitoringService1 monitoringService1;
    boolean monitoringBound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MonitoringService1.MonitoringBinder binder1 = (MonitoringService1.MonitoringBinder) service;
            monitoringService1 = binder1.getService();
            monitoringBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            monitoringBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setUpServiceReciever();

        TextView totalApp =findViewById(R.id.textview);
        totalApp.setText(String.valueOf(getNumberofApp("sys")));

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
                    if (monitoringBound) {
                        String data = monitoringService1.getData();
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
        Intent intent = new Intent(this, MonitoringService1.class);
        intent.putExtra("sendItem", "start monitoring");
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
        //startService(intent);
    }

    private void stopMonitoringService(){
        //Intent intent = new Intent(this, MonitoringService1.class);
        unbindService(connection);
        monitoringBound = false;
        //stopService(intent);
    }



    /*private void stopMonitoringService(){
        Intent intent = new Intent(this, MonitoringService.class);
        stopService(intent);
    }



    private void launchMonitoringService(){
        Intent intent = new Intent(this, MonitoringService.class);
        intent.putExtra("sendItem", "start monitoring");
        intent.putExtra("reciever", recieveData);
        startService(intent);
    }*/

    /*public void setUpServiceReciever(){
        recieveData = new RecieveData(new Handler());
        recieveData.setReciever(new RecieveData.Reciever() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == RESULT_OK){
                    String resultValue = resultData.getString("resultValue");
                    Toast.makeText(MainActivity.this, resultValue, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    // receive result from background service
    /*private BroadcastReceiver monitoringReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if(resultCode == Activity.RESULT_OK){
                String resultValue = intent.getStringExtra("resultValue");
                Toast.makeText(MainActivity.this, resultValue, Toast.LENGTH_SHORT).show();
            }
        }
    };*/

    /*@Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(monitoringReciever, new IntentFilter(MonitoringService.ACTION));
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(monitoringReciever, new IntentFilter(MonitoringService.ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(monitoringReciever);
    }
*/
    private int getNumberOfTotalApp(){
        int totalApp;
        //totalApp = getPackageManager().getInstalledApplications(0).size();
        totalApp = getPackageManager().getInstalledPackages(0).size();
        return totalApp;
    }

    private int getNumberofApp(String s){
        int totalSystemApp = 0;
        int totalUserInstalledApp = 0;
        List<ApplicationInfo> applist = getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo app : applist){
            int flag = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                flag = app.category;
                if (flag != ApplicationInfo.CATEGORY_UNDEFINED){
                    totalSystemApp++;
                }else totalUserInstalledApp++;
            }


        }
        if (s.equals("sys"))return totalSystemApp;
        else return totalUserInstalledApp;
    }

    private void getCpuUtilization(){

    }
}