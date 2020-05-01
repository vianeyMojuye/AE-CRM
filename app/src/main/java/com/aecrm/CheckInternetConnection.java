package com.aecrm;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternetConnection {

    private Context context;

    public CheckInternetConnection(Context context)
    {
        this.context = context;
    }

    public  boolean checkInternet()
    {
        //Create object for ConnectivityManager class which returns network related info
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //If connectivity object is not null
        if(connectivity!=null){
            //Get network info - Mobile internet access
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo info1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (info != null) {
                //Look for whether device is currently connected to WIFI network
                if (info.isConnected() || info1.isConnected()) {
                    return true;
                }
            }

        }

       return false;


    }
}
