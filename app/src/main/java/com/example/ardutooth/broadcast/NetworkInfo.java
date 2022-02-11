package com.example.ardutooth.broadcast;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkInfo {
    public static boolean isNotworkConnected(Context context)
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );
        android.net.NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }
}
