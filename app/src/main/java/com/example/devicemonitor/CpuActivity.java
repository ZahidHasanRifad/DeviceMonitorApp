package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.CpuUsageInfo;
import android.os.HardwarePropertiesManager;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CpuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);

        TextView cpuinfo = findViewById(R.id.cpuInfo);
        cpuinfo.setText(getCpuInfo());
        //cpuinfo.setText(getCpuUsageInfo());
    }

    private String getCpuInfo(){
        StringBuffer sb = new StringBuffer();
        sb.append("abi: ").append(Build.CPU_ABI).append("\n");
        //sb.append(Build.SUPPORTED_ABIS);
        if (new File("/proc/cpuinfo").exists()) {
            try {
                BufferedReader br = new BufferedReader(
                        new FileReader(new File("/proc/cpuinfo")));
                String aLine;
                while ((aLine = br.readLine()) != null) {
                    sb.append(aLine + "\n");
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private String getCpuUsageInfo(){

        StringBuffer stringBuffer = new StringBuffer();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            HardwarePropertiesManager hardwarePropertiesManager = (HardwarePropertiesManager) getSystemService(HARDWARE_PROPERTIES_SERVICE);
            CpuUsageInfo[] cpuUsageInfo = hardwarePropertiesManager.getCpuUsages();
            for (int i=0; i<cpuUsageInfo.length; i++){
                stringBuffer.append(String.valueOf(cpuUsageInfo[i].getActive()));
            }
        }
        return stringBuffer.toString();
    }
}