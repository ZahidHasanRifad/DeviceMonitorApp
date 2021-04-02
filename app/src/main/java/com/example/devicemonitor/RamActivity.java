package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ram);

        TextView memInfo = findViewById(R.id.memInfo);
        memInfo.setText(getRamInfo());
    }

    private String getRamInfo() {
        StringBuffer sb = new StringBuffer();
        String path = "/proc/meminfo";
        //sb.append("abi: ").append(Build.BOARD).append("\n");
        if (new File(path).exists()) {
            try {
                BufferedReader br = new BufferedReader(
                        new FileReader(new File(path)));
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
        ////////////////
    }
}