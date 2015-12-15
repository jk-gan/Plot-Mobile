package com.example.jkgan.pmot;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.jkgan.pmot.person.User;
import com.example.jkgan.pmot.service.QuickstartPreferences;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by JKGan on 06/12/2015.
 */
public class SplashActivity extends AppCompatActivity {

    User user = new User();
    private static final String REGISTER_URL = MyApplication.getApiUrl() + "/users?token=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(MyApplication.isNetworkAvailable(this)) {
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this);
            boolean loggedIn = sharedPreferences
                    .getBoolean(LoginActivity.LOGGED_IN, false);

            String token = sharedPreferences
                    .getString(LoginActivity.TOKEN, "");
            if (loggedIn && token != "") {

                String url = REGISTER_URL + token;

                final OkHttpClient client = new OkHttpClient();

                final Request request = new Request.Builder()
                        .url(url)
                        .build();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Response response = null;
                        try {
                            response = client.newCall(request).execute();
                            JSONObject jObj = new JSONObject(response.body().string());
                            user.setToken(jObj.optString("token"));
                            JSONObject userData = jObj.getJSONObject("user");
                            user.setName(userData.optString("name"));
                            user.setEmail(userData.optString("email"));
                            MyApplication myApp = ((MyApplication) getApplicationContext());
                            myApp.setUser(user);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SplashActivity.this, FirstActivity.class);
                startActivity(intent);
            }
            finish();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Please check your internet connection.");

            alertDialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });

//            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    finish();
//                }
//            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}