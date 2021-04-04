package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class DeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        TextView brand = findViewById(R.id.brandv);
        TextView manufact = findViewById(R.id.manufacturerv);
        TextView model = findViewById(R.id.modelv);
        TextView secpatch = findViewById(R.id.spv);
        TextView andver = findViewById(R.id.androidvvv);
        brand.setText(Build.BRAND);
        manufact.setText(Build.MANUFACTURER);
        model.setText(Build.MODEL);
        andver.setText("API Level "+String.valueOf(Build.VERSION.SDK_INT));
        secpatch.setText(Build.VERSION.SECURITY_PATCH);
        //TextView brand = findViewById(R.id.brandv);


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