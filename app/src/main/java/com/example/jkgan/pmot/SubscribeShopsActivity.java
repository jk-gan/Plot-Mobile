package com.example.jkgan.pmot;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class SubscribeShopsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayoutManager lLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_shops);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        List<ItemObject> rowListItem = getAllItemList();
        lLayout = new LinearLayoutManager(SubscribeShopsActivity.this);

        RecyclerView rView = (RecyclerView)findViewById(R.id.recycler_view);
        rView.setLayoutManager(lLayout);

        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(SubscribeShopsActivity.this, rowListItem);
        rView.setAdapter(rcAdapter);
    }

    private List<ItemObject> getAllItemList(){

        List<ItemObject> allItems = new ArrayList<ItemObject>();
        allItems.add(new ItemObject("Peter James", "Vildansvagen 19, Lund Sweden", R.drawable.profile));
        allItems.add(new ItemObject("Henry Jacobs", "3 Villa Crescent London, UK", R.drawable.profile));
        allItems.add(new ItemObject("Bola Olumide", "Victoria Island Lagos, Nigeria", R.drawable.profile));
        allItems.add(new ItemObject("Chidi Johnson", "New Heaven Enugu, Nigeria", R.drawable.profile));
        allItems.add(new ItemObject("DeGordio Puritio", "Italion Gata, Padova, Italy", R.drawable.profile));
        allItems.add(new ItemObject("Gary Cook", "San Fransisco, United States", R.drawable.profile));
        allItems.add(new ItemObject("Edith Helen", "Queens Crescent, New Zealand", R.drawable.profile));
        allItems.add(new ItemObject("Kingston Dude", "Ivory Lane, Abuja, Nigeria", R.drawable.profile));
        allItems.add(new ItemObject("Edwin Bent", "Johnson Road, Port Harcourt, Nigeria", R.drawable.profile));
        allItems.add(new ItemObject("Grace Praise", "Federal Quarters, Abuja Nigeria", R.drawable.profile));

        return allItems;
    }



}
