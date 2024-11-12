package com.example.campingmaster;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.campingmaster.api.RetrofitClient;
import com.example.campingmaster.api.RetrofitService;
import com.example.campingmaster.api.gocamping.dto.CampingSiteDto;
import com.example.campingmaster.api.googlemap.MapHandler;
import com.example.campingmaster.utils.PermissionHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class CampingSiteDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView nameTextView;
    private TextView addressTextView;
    private TextView descriptionTextView;
    private ImageView siteImageView;
    private GoogleMap mMap;
    private MapHandler mapHandler;
    private FusedLocationProviderClient mFusedLocationClient;
    private PermissionHelper permissionHelper;
    private CampingSiteDto campingSite;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camping_site_detail);

        initializeComponents();
        setupMapView(savedInstanceState);
        setupViews();
        loadCampingSiteData();
    }

    private void initializeComponents() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        permissionHelper = new PermissionHelper(this);

        if (!permissionHelper.checkPermissions()) {
            permissionHelper.requestPermissions();
        }
    }

    private void setupMapView(Bundle savedInstanceState) {
        mapView = findViewById(R.id.detail_map);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }
    }

    private void setupViews() {
        nameTextView = findViewById(R.id.nameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        siteImageView = findViewById(R.id.siteImageView);
    }

    private void loadCampingSiteData() {
        campingSite = (CampingSiteDto) getIntent().getSerializableExtra("campingSite");
        if (campingSite != null) {
            updateUI();
        }
    }

    private void updateUI() {
        nameTextView.setText(getValueOrDefault(campingSite.getName(), "Name not available"));
        addressTextView.setText(getValueOrDefault(campingSite.getAddress(), "Address not available"));
        descriptionTextView.setText(getValueOrDefault(campingSite.getDescription(), "Description not available"));

        if (campingSite.getImgUrl() != null) {
            loadImageFromUrl(campingSite.getImgUrl(), siteImageView);
        } else {
            siteImageView.setImageResource(R.drawable.logo_108x82);
        }
    }

    private String getValueOrDefault(String value, String defaultValue) {
        return value != null ? value : defaultValue;
    }

    private void loadImageFromUrl(String imgUrl, ImageView imageView) {
        Glide.with(this)
                .load(imgUrl)
                .placeholder(R.drawable.logo_108x82)
                .error(R.drawable.logo_108x82)
                .into(imageView);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap == null) {
            Log.e("MapError", "GoogleMap is null");
            return;
        }

        // MapHandler 생성 및 맵 설정
        mapHandler = new MapHandler(this, mMap, mFusedLocationClient);
        mapHandler.onMapReady();

        // 캠핑장 마커 추가
        if (campingSite != null) {
            mapHandler.addCampingSiteMarker(campingSite);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.handlePermissionsResult(requestCode, permissions, grantResults);

        // 권한이 승인되었으면 위치 업데이트 시작
        if (permissionHelper.checkPermissions()) {
            mapHandler.startLocationUpdates(new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                        Location location = locationResult.getLocations().get(0);
                        mapHandler.setCurrentLocation(location, "Current Location", "Your current location");
                    }
                }
            });
        } else {
            // 권한이 승인되지 않으면 처리
            Log.e("Permissions", "Location permission not granted.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }
}
