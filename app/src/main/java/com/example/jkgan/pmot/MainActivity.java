package com.example.jkgan.pmot;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button scannerButton;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        token = getIntent().getStringExtra("TOKEN");
        TextView txtToken = (TextView) findViewById(R.id.textViewToken);
        txtToken.setText(token);

        ((MyApplication)this.getApplication()).setToken(token);

        scannerButton = (Button) findViewById(R.id.buttonScanner);
        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QRcodeScanner.class);
                startActivity(intent);
            }
        });
    }
}
