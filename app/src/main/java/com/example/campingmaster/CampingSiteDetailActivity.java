package com.example.campingmaster;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.example.campingmaster.api.gocamping.dto.CampingSiteDto;
import com.example.campingmaster.api.googlemap.MapHandler;
import com.example.campingmaster.utils.PermissionHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

public class CampingSiteDetailActivity extends ToolBarActivity implements OnMapReadyCallback {
    private TextView nameTextView;
    private TextView addressTextView;
    private TextView descriptionTextView;
    private TextView detailTextView;
    private ImageView siteImageView;
    private TextView urlTextView;
    private TextView reserveText;
    private TextView categoryText;
    private GoogleMap mMap;
    private MapHandler mapHandler;
    private FusedLocationProviderClient mFusedLocationClient;
    private PermissionHelper permissionHelper;
    private CampingSiteDto campingSite;
    private MapView mapView;
    private String siteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camping_site_detail);

        // 상태 바만 투명하게 설정
        Window window = getWindow();
        // 상태 바만 투명하게 설정하고 내비게이션 바는 그대로 유지
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        Intent intent = getIntent();
        siteName = intent.getStringExtra("citeName");
        setupToolbar(siteName); // 툴바 설정

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
        detailTextView = findViewById(R.id.detailText);
        urlTextView = findViewById(R.id.homepageText);
        reserveText = findViewById(R.id.reserveText);
        categoryText = findViewById(R.id.categoryText);
    }

    private void loadCampingSiteData() {
        campingSite = (CampingSiteDto) getIntent().getSerializableExtra("campingSite");
        if (campingSite != null) {
            updateUI();
        }
    }

    private void updateUI() {
        setTextOrHide(nameTextView, campingSite.getName(), "Name not available");
        setTextOrHide(addressTextView, campingSite.getAddress(), "Address not available");
        setTextOrHide(descriptionTextView, campingSite.getDescription(), "Description not available");
        setTextOrHide(detailTextView, campingSite.getFeatureNm(), "");
        setTextOrHide(categoryText, campingSite.getCategory(), "");

        // Set homepage URL as clickable
        setUrlOrHide(urlTextView, campingSite.getHomepageUrl(), "Visit Homepage");

        // Set reservation URL as clickable
        setUrlOrHide(reserveText, campingSite.getReserveUrl(), "Reserve Now");

        // Load image if available, else set default image
        if (campingSite.getImgUrl() != null) {
            loadImageFromUrl(campingSite.getImgUrl(), siteImageView);
        } else {
            siteImageView.setImageResource(R.drawable.logo_108x82);
        }
    }

    private void setTextOrHide(TextView textView, String text, String defaultText) {
        if (text == null || text.isEmpty()) {
            textView.setVisibility(View.GONE); // Hide if no text
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }

    private void setUrlOrHide(TextView textView, String url, String displayText) {
        if (url == null || url.isEmpty()) {
            textView.setVisibility(View.GONE); // Hide if no URL
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(url);
            textView.setTextColor(getResources().getColor(R.color.blue)); // Set text color to look like a link
            textView.setOnClickListener(v -> openLink(url));
        }
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
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

        mapHandler = new MapHandler(this, mMap, mFusedLocationClient);
        mapHandler.onMapReady();

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
