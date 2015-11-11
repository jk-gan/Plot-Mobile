package com.example.jkgan.pmot;

import android.app.Application;

/**
 * Created by JKGan on 11/11/2015.
 */
public class MyApplication extends Application {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
