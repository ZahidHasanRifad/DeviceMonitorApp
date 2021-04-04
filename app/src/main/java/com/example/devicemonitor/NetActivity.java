package com.example.devicemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class NetActivity extends AppCompatActivity {

    static private LinkedList<InetAddress> inetAddresses = new LinkedList<InetAddress>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);

        TextView status = findViewById(R.id.statusv);
        TextView nettypetxt = findViewById(R.id.nettypet);
        TextView nettypevalue = findViewById(R.id.nettypev);
        TextView iptxt = findViewById(R.id.ipt);
        TextView ipvalue = findViewById(R.id.ipv);

        if (isNetworkAvailable(this)){
            status.setText("CONNECTED");
            nettypetxt.setText("Network Type");
            nettypevalue.setText(getNetType(connectivityManager));
            iptxt.setText("IP Address");
            ipvalue.setText(getWifiIPAddress(wifiMgr));
        }else status.setText("NOT CONNECTED");

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

    private boolean getStatus(ConnectivityManager connectivityManager){
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo.isAvailable())
            return true;
        else return false;
    }

    private String getNetType(ConnectivityManager connectivityManager){
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo.getTypeName();
    }

    public String getWifiIPAddress(WifiManager wifiMgr) {

        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return  Formatter.formatIpAddress(ip);
    }

    public static String getMobileIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        return  addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

}