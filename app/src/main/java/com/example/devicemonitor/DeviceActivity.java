package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

public class DeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
    }

    private String getDeviceInfo(){

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Brand "+Build.BRAND);
        stringBuffer.append("Manufacturer "+Build.MANUFACTURER);
        stringBuffer.append("Model "+Build.MODEL);
        stringBuffer.append("Product "+Build.PRODUCT);
        stringBuffer.append("Device "+Build.DEVICE);
        stringBuffer.append("Version Base_OS "+Build.VERSION.BASE_OS);
        stringBuffer.append("Security Patch "+Build.VERSION.SECURITY_PATCH);
        return stringBuffer.toString();
    }
}