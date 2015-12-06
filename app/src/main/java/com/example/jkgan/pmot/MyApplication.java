package com.example.jkgan.pmot;

import android.app.Application;

import com.example.jkgan.pmot.person.User;

/**
 * Created by JKGan on 11/11/2015.
 */
public class MyApplication extends Application {

//        private final static String apiUrl = "http://10.0.2.2:3000/api/v1";
//    private final static String url = "http://10.0.2.2:3000";
    private final static String apiUrl = "http://192.168.1.8:3000/api/v1";
    private final static String url = "http://192.168.1.8:3000";
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
}
