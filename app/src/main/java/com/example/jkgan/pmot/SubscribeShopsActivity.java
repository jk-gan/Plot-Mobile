package com.example.jkgan.pmot;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.example.jkgan.pmot.Http.HttpRequest;
import com.example.jkgan.pmot.Shops.Shop;

public class SubscribeShopsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
//    private LinearLayoutManager lLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_shops);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ShopsListASYNC loginTask = new ShopsListASYNC();
        loginTask.execute(MyApplication.getUser().getToken());

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });




//        List<Shop> rowListItem = getAllItemList();
//        lLayout = new LinearLayoutManager(SubscribeShopsActivity.this);
//
//        RecyclerView rView = (RecyclerView)findViewById(R.id.recycler_view);
//        rView.setLayoutManager(lLayout);

//        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(SubscribeShopsActivity.this, rowListItem);
//        rView.setAdapter(rcAdapter);
    }

    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ShopsListASYNC loginTask = new ShopsListASYNC();
                loginTask.execute(MyApplication.getUser().getToken());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

//
//    private List<Shop> getAllItemList(){
//
//        List<Shop> allItems = new ArrayList<Shop>();
//        allItems.add(new Shop("Peter James", "Vildansvagen 19, Lund Sweden", R.drawable.profile));
//        allItems.add(new Shop("Henry Jacobs", "3 Villa Crescent London, UK", R.drawable.profile));
//        allItems.add(new Shop("Bola Olumide", "Victoria Island Lagos, Nigeria", R.drawable.profile));
//        allItems.add(new Shop("Chidi Johnson", "New Heaven Enugu, Nigeria", R.drawable.profile));
//        allItems.add(new Shop("DeGordio Puritio", "Italion Gata, Padova, Italy", R.drawable.profile));
//        allItems.add(new Shop("Gary Cook", "San Fransisco, United States", R.drawable.profile));
//        allItems.add(new Shop("Edith Helen", "Queens Crescent, New Zealand", R.drawable.profile));
//        allItems.add(new Shop("Kingston Dude", "Ivory Lane, Abuja, Nigeria", R.drawable.profile));
//        allItems.add(new Shop("Edwin Bent", "Johnson Road, Port Harcourt, Nigeria", R.drawable.profile));
//        allItems.add(new Shop("Grace Praise", "Federal Quarters, Abuja Nigeria", R.drawable.profile));
//
//        return allItems;
//    }

    private class ShopsListASYNC extends AsyncTask<String, Void, JSONObject> {

        final ProgressDialog dialog = new ProgressDialog(SubscribeShopsActivity.this);

        @Override
        protected void onPreExecute() {
//            dialog.setMessage("Loading List...");
//            dialog.setCancelable(false);
//            dialog.setInverseBackgroundForced(false);
//            dialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {

            final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("token", params[0]));
            String strURL = MyApplication.getApiUrl() + "/shops/subscribe";
            HttpRequest request = new HttpRequest();

            return request.makeHttpRequest(strURL, "GET", parameters);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            PmotDB db = new PmotDB(getApplicationContext());
            JSONArray jsonArr = null;
            try {
                List<Shop> allItems = new ArrayList<Shop>();
                jsonArr = result.getJSONArray("Shops");
                URLEncoder.encode(jsonArr.toString(), "UTF8");
                JSONObject jsnObj2 = null;
                if(jsonArr != null) {
                    int length = jsonArr.length();
                    for(int i = 0; i < length; i++) {
                        jsnObj2 = jsonArr.getJSONObject(i);
                        allItems.add(new Shop(jsnObj2.optString("name"), jsnObj2.optString("address"), jsnObj2.optString("id"), jsnObj2.getJSONObject("image").getJSONObject("medium").optString("url"), jsnObj2.getJSONObject("image").getJSONObject("small").optString("url"), jsnObj2.optString("phone"), new String(jsnObj2.optString("description").getBytes(), "UTF-8")));
                        db.fnRunSQL("INSERT INTO shops (id, name, address, phone, description) VALUES ("+jsnObj2.optInt("id")+", \""+jsnObj2.optString("name")+"\", \""+jsnObj2.optString("address")+"\", \""+jsnObj2.optString("phone")+"\", \""+jsnObj2.optString("description")+"\");",
                                getApplicationContext());

                        System.out.println("INSERT INTO shops (id, name, address, phone, description) VALUES ("+jsnObj2.optInt("id")+", \""+jsnObj2.optString("name")+"\", \""+jsnObj2.optString("address")+"\", \""+jsnObj2.optString("phone")+"\", \""+jsnObj2.optString("description")+"");
                    }

                    SQLiteDatabase sqLiteDatabase;
                    sqLiteDatabase = openOrCreateDatabase("db_Pmot", MODE_PRIVATE, null);
                    Cursor resultSet = sqLiteDatabase.rawQuery("Select * from shops;", null);


                    if (resultSet.moveToFirst()){
                        do{

                            String name = resultSet.getString(resultSet.getColumnIndex("name"));
                            System.out.println("====DDDD=================" + name);


                        }while (resultSet.moveToNext());
                    }


                    LinearLayoutManager lLayout = new LinearLayoutManager(SubscribeShopsActivity.this);
                    lLayout.setOrientation(LinearLayoutManager.VERTICAL);

                    RecyclerView rView = (RecyclerView)findViewById(R.id.recycler_view);
                    rView.setLayoutManager(lLayout);

                    RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(SubscribeShopsActivity.this, allItems);
                    rView.setAdapter(rcAdapter);



//                    dialog.dismiss();
                } else {
                    Toast toast;
                    toast = Toast.makeText(getApplicationContext(), "Empty json array", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }


}
