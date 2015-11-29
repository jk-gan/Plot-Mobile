package com.example.jkgan.pmot;

/**
 * Created by JKGan on 11/11/2015.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class OneFragment extends Fragment{

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_one,container, false);

        FloatingActionButton scanButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(rootView.findViewById((R.id.coordinator)), "Something wrong", Snackbar.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), QRcodeScanner.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

}
