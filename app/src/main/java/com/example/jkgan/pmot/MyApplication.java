package com.example.jkgan.pmot;

import android.app.Application;

import person.User;

/**
 * Created by JKGan on 11/11/2015.
 */
public class MyApplication extends Application {

//    private final static String url = "http://10.0.2.2:3000/api/v1";
    private final static String url = "http://192.168.1.164:3000/api/v1";
    private User user;

    public static String getUrl() {
        return url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
