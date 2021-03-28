package com.example.devicemonitor.bgservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class RecieveData extends ResultReceiver {
    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */

    private Reciever reciever;

    public RecieveData(Handler handler) {
        super(handler);
    }

    public void setReciever(Reciever reciever){
        this.reciever = reciever;
    }

    public interface Reciever {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData){
        if (reciever!=null){
            reciever.onReceiveResult(resultCode,resultData);
        }
    }
}
