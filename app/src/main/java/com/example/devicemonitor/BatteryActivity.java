package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;

public class BatteryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
    }

    private String getBatteryInfo(){

        StringBuffer stringBuffer = new StringBuffer();

        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        if (batteryManager.isCharging()){
            stringBuffer.append("Status: Charging");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                stringBuffer.append("Time Remaining to Full Charge: "+ batteryManager.computeChargeTimeRemaining());
            }
        }

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            stringBuffer.append(String.valueOf(powerManager.getCurrentThermalStatus()));
        }

        return stringBuffer.toString();
    }
}