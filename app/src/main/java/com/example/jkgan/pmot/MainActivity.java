package com.example.jkgan.pmot;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtToken = (TextView) findViewById(R.id.textViewToken);

        txtToken.setText(getIntent().getStringExtra("TOKEN"));
    }
}
