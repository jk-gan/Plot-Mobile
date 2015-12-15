package com.example.jkgan.pmot;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.jkgan.pmot.person.User;

/**
 * Created by JKGan on 11/11/2015.
 */
public class MyApplication extends Application {

//    private final static String apiUrl = "http://10.0.2.2:3000/api/v1";
//    private final static String url = "http://10.0.2.2:3000";
    private final static String url = "http://169.254.141.36:3000";
    private final static String apiUrl = url + "/api/v1";

    private static User mUser;

    public static String getApiUrl() {
        return apiUrl;
    }

    public static String getUrl() {
        return url;
    }

    public static User getUser() {
        return mUser;
    }

    public static void setUser(User user) {
        mUser = user;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
