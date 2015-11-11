package com.example.jkgan.pmot;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShopActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        TextView txtShopName = (TextView) findViewById(R.id.textViewShopName);
        txtShopName.setText(getIntent().getStringExtra("NAME"));
    }
}
