package com.example.campingmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        setContentView(R.layout.fragment_main);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;

        LatLng seoul = new LatLng(37.56, 126.97);

        MarkerOptions options = new MarkerOptions();
        options.position(seoul)
                .title("서울")
                .snippet("한국의 수도");
        map.addMarker(options);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 10));
    }
}