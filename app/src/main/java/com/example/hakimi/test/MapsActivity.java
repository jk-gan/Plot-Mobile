package com.example.kimi.test;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;




import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final LatLng Malaysia = new LatLng(4.210484, 102.240143);
    private GoogleMap map;

    // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        Marker malaysia = map.addMarker(new MarkerOptions().position(Malaysia).title("Malaysia").snippet("Marker To Malaysia"));

        // zoom in the camera
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Malaysia, 15));

        // animate the zoom process
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        map.setMyLocationEnabled(true);

        Intent intent = new Intent( Intent.ACTION_VIEW,
                Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d" +
                        "&saddr=31.249351,121.45905&daddr=31.186371,121.489885&hl=zh&t=m&dirflg=d"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng malaysia = new LatLng(4.210484, 102.240143);
        map.addMarker(new MarkerOptions().position(malaysia).title("Marker in Malaysia"));
        map.moveCamera(CameraUpdateFactory.newLatLng(malaysia));


    }

}
