package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.usage.StorageStatsManager;
import android.os.Bundle;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.widget.TextView;

import java.io.File;

public class StorageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        File dirs[];
        dirs = ContextCompat.getExternalFilesDirs(this,null);

        //StorageStatsManager storageStatsManager = (StorageStatsManager) getSystemService(STORAGE_STATS_SERVICE);
        //StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
        StatFs statFs = new StatFs(dirs[0].getPath());
        Long blockSize = statFs.getBlockSizeLong();
        Long totalBlock = statFs.getBlockCountLong();
        Long internalMemorySize = blockSize*totalBlock;

        Long availableBlock = statFs.getAvailableBlocksLong();
        Long availableMemorySize = blockSize*availableBlock;

        Long usedMemorySize = internalMemorySize - availableMemorySize;

        TextView total = findViewById(R.id.totalsizev);
        TextView used = findViewById(R.id.usedspacev);
        TextView avail = findViewById(R.id.availspacev);

        total.setText(String.valueOf(internalMemorySize/(1024*1024*1024))+" GB");
        used.setText(String.valueOf(usedMemorySize/(1024*1024*1024))+" GB");
        avail.setText(String.valueOf(availableMemorySize/(1024*1024*1024))+" GB");
    }


}