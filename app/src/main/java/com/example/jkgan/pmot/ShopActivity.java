package com.example.jkgan.pmot;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.jkgan.pmot.Http.HttpRequest;

public class ShopActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Intent intent = getIntent();
        final String shopId = intent.getStringExtra("SHOP_ID");
        final String shopName = intent.getStringExtra("NAME");
        final String shopImage = intent.getStringExtra("IMAGE");
        final String shopAdress = intent.getStringExtra("ADDRESS");
        final String shopPhone = intent.getStringExtra("PHONE");
        final String shopDescription = intent.getStringExtra("DESCRIPTION");
        final boolean subscribed = intent.getBooleanExtra("SUBSCRIBED", false);

        ImageView imageView = (ImageView) findViewById(R.id.shopView);

        Glide.with(this).load(MyApplication.getUrl() + shopImage).into(imageView);

        TextView phone = (TextView) findViewById(R.id.phone);
        phone.setText(shopPhone);

        TextView address = (TextView) findViewById(R.id.address);
        address.setText(shopAdress);

        TextView description = (TextView) findViewById(R.id.description);
        description.setText(shopDescription);

        // Initializing Toolbar and setting it as the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(shopName);

//        TextView txtShopName = (TextView) findViewById(R.id.longWord);
//        txtShopName.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

        FloatingActionButton btnSubscribe = (FloatingActionButton) findViewById(R.id.btnSubscirbe);
        if(subscribed) {
            btnSubscribe.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation));
            btnSubscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication appState = ((MyApplication) getApplicationContext());
                    SubscribeASYNC subscribeTask = new SubscribeASYNC();
                    subscribeTask.execute(shopId, appState.getUser().getToken());
                }
            });
        } else {
            btnSubscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication appState = ((MyApplication) getApplicationContext());
                    SubscribeASYNC subscribeTask = new SubscribeASYNC();
                    subscribeTask.execute(shopId, appState.getUser().getToken());
                }
            });
        }
    }


    private class SubscribeASYNC extends AsyncTask<String, Void, JSONObject> {

        final ProgressDialog dialog = new ProgressDialog(ShopActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Subscribing...");
            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {

            final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("id", params[0]));
            parameters.add(new BasicNameValuePair("token", params[1]));
            String strURL = MyApplication.getApiUrl() + "/shops/subscribe";
            HttpRequest request = new HttpRequest();

            return request.makeHttpRequest(strURL, "POST", parameters);
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            String id = "";
            id = result.optString("id");


            if (!id.equals("")) {

                new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();
                                Snackbar.make(findViewById((R.id.shopLayout)), "Subscribed to the shop", Snackbar.LENGTH_LONG).show();

                            }
                        });
                    }
                }.start();

            } else {
                dialog.dismiss();
                Snackbar.make(findViewById((R.id.shopLayout)), "Something wrong", Snackbar.LENGTH_LONG).show();
            }
        }

    }
}
