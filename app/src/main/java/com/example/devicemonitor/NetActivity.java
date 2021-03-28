package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;

public class NetActivity extends AppCompatActivity {

    static private LinkedList<InetAddress> inetAddresses = new LinkedList<InetAddress>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);

        TextView netInfo = findViewById(R.id.netInfo);
        //load();
        netInfo.setText(getIP());
    }

    private String getnetInfo(){
        if (isNetworkAvailable(this)) return "net available";
        else return "No network available";

    }

    public static boolean isNetworkAvailable(Activity mActivity) {
        Context context = mActivity.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static void load() {
        inetAddresses.clear();
        try {
            Enumeration<NetworkInterface> networkInterfaceEnum = NetworkInterface.getNetworkInterfaces();

            while(networkInterfaceEnum.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresseEnum = networkInterfaceEnum.nextElement().getInetAddresses();

                while(inetAddresseEnum.hasMoreElements()) {
                    inetAddresses.add(inetAddresseEnum.nextElement());
                }
            }
        }
        catch(IOException e) {
            Log.d("MyNetworkInterfaces", e.toString());
        }
    }

    public static boolean contains(InetAddress addr) {
        return inetAddresses.contains(addr);
    }

    private String getIP(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        Network network = connectivityManager.getActiveNetwork();
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo.getState().toString();
        //return networkInfo.getTypeName();
        

    }
}