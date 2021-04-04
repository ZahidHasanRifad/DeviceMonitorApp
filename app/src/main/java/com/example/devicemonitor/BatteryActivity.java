package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BatteryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);

        TextView status = findViewById(R.id.batterystatus);
        TextView remaining = findViewById(R.id.timeremainingv);
        TextView temperature = findViewById(R.id.temparaturev);
        List<String> bi = getBatteryInfo();

            status.setText(bi.get(0));
            remaining.setText(bi.get(1));
            temperature.setText(bi.get(2));



//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                battery.setText(getBatteryInfo());
//            }
//        }, 1000);


    }

    private List<String> getBatteryInfo(){

        //StringBuffer stringBuffer = new StringBuffer();
        //String [] bt = new String[3];
        List<String> btl = new ArrayList<>();
        String charg ="", time= "N/A", temp = "N/A";
        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        if (batteryManager.isCharging()){
            //stringBuffer.append("Status: Charging");
            //bt[0] = "CHARGING"
            charg = "CHARGING";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                //stringBuffer.append("Time Remaining to Full Charge: "+ batteryManager.computeChargeTimeRemaining());
                time = String.valueOf(batteryManager.computeChargeTimeRemaining());
            }
        }else charg = "NOT CHARGING";

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //stringBuffer.append("Temperature: "+String.valueOf(powerManager.getCurrentThermalStatus()));
            temp = String.valueOf(powerManager.getCurrentThermalStatus());
        }

        btl.add(charg);
        btl.add(time);
        btl.add(temp);

        return btl;
    }
}