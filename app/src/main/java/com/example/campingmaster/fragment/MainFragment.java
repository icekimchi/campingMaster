package com.example.campingmaster.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.campingmaster.CampSiteResultActivity;
import com.example.campingmaster.CampingSiteDetailActivity;
import com.example.campingmaster.R;
import com.example.campingmaster.adapter.CardViewAdapter;
import com.example.campingmaster.api.RetrofitClient;
import com.example.campingmaster.api.RetrofitService;
import com.example.campingmaster.api.gocamping.dto.CampingSiteDto;
import com.example.campingmaster.api.gocamping.dto.LocationSearchDto;
import com.example.campingmaster.api.googlemap.MapHandler;
import com.example.campingmaster.utils.PermissionHelper;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "googlemap_example";
    private GoogleMap mMap;
    private MapHandler mapHandler;
    private FusedLocationProviderClient mFusedLocationClient;
    private PermissionHelper permissionHelper;
    private RetrofitService service;
    private LocationCallback locationCallback;
    private SupportMapFragment mapFragment;
    private boolean isFirstLocationUpdate = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initializeComponents();
        setupMapFragment();
        setupCategoryButtons(view);
        return view;
    }

    private void initializeComponents() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        service = RetrofitClient.getClient().create(RetrofitService.class);
        permissionHelper = new PermissionHelper(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                    Location location = locationResult.getLocations().get(locationResult.getLocations().size() - 1);
                    updateLocationUI(location);
                }
            }
        };
    }

    private void setupMapFragment() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }
        mapFragment.getMapAsync(this);
    }

    private void updateLocationUI(Location location) {
        if (mapHandler != null && location != null) {
            LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
            String markerTitle = mapHandler.getCurrentAddress(currentPosition);
            String markerSnippet = "위도:" + location.getLatitude() + " 경도:" + location.getLongitude();

            if (isFirstLocationUpdate) {
                mapHandler.setCurrentLocation(location, markerTitle, markerSnippet);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));
                isFirstLocationUpdate = false;
            } else {
                mapHandler.setCurrentLocation(location, markerTitle, markerSnippet);
            }
        }
    }

    private void setupCategoryButtons(View view) {
        setupImageViewClick(view, R.id.cat_normal, "SELECT * FROM campsite WHERE category LIKE ?", "%일반야영장%");
        setupImageViewClick(view, R.id.cat_carvan, "SELECT * FROM campsite WHERE category LIKE ?", "%카라반%");
        setupImageViewClick(view, R.id.cat_glamping, "SELECT * FROM campsite WHERE category LIKE ?", "%글램핑%");
        setupImageViewClick(view, R.id.cat_sunrise, "SELECT * FROM campsite WHERE thema_envrn_cl LIKE ?", "%일출명소%");
        setupImageViewClick(view, R.id.cat_sunset, "SELECT * FROM campsite WHERE thema_envrn_cl LIKE ?", "%일몰명소%");
        setupImageViewClick(view, R.id.cat_spring, "SELECT * FROM campsite WHERE season LIKE ?", "%봄%");
        setupImageViewClick(view, R.id.cat_summer, "SELECT * FROM campsite WHERE season LIKE ?", "%여름%");
        setupImageViewClick(view, R.id.cat_animal, "SELECT * FROM campsite WHERE pet_allowed = ?", "반려동물");
        setupImageViewClick(view, R.id.cat_pool, "SELECT * FROM campsite WHERE thema_envrn_cl LIKE ?", "%수영장%");
        setupImageViewClick(view, R.id.cat_seoul, "SELECT * FROM campsite WHERE address LIKE ?", "%서울%");

        setupTextViewClick(view, R.id.key_spring, "SELECT * FROM campsite WHERE nearby_facilities LIKE ?", "%봄%");
        setupTextViewClick(view, R.id.key_summer, "SELECT * FROM campsite WHERE thema_envrn_cl LIKE ?", "%여름%");
        setupTextViewClick(view, R.id.key_fall, "SELECT * FROM campsite WHERE season LIKE ?", "%가을%");
        setupTextViewClick(view, R.id.key_winter, "SELECT * FROM campsite WHERE thema_envrn_cl LIKE ?", "%겨울%");
        setupTextViewClick(view, R.id.key_bbq, "SELECT * FROM campsite WHERE nearby_facilities LIKE ?", "%바베큐%");
        setupTextViewClick(view, R.id.key_baby, "SELECT * FROM campsite WHERE pet_allowed = ?", "#애견동반%");
        setupTextViewClick(view, R.id.key_tour, "SELECT * FROM campsite WHERE nearby_facilities LIKE ?", "%관광지주변%");
        setupTextViewClick(view, R.id.key_beach, "SELECT * FROM campsite WHERE location_category = ?", "해변");
        setupTextViewClick(view, R.id.key_deck, "SELECT * FROM campsite WHERE location_category LIKE ?", "%데크%");
        setupTextViewClick(view, R.id.key_fish, "SELECT * FROM campsite WHERE thema_envrn_cl LIKE ?", "%낚시%");
    }

    private void setupImageViewClick(View parentView, int imageViewId, String sqlQuery, String param) {
        ImageView imageView = parentView.findViewById(imageViewId);
        imageView.setOnClickListener(v -> launchResultActivity(sqlQuery, param));
    }

    private void setupTextViewClick(View parentView, int textViewId, String sqlQuery, String param) {
        TextView textView = parentView.findViewById(textViewId);
        textView.setOnClickListener(v -> launchResultActivity(sqlQuery, param));
    }

    private void launchResultActivity(String sqlQuery, String param) {
        Intent intent = new Intent(getActivity(), CampSiteResultActivity.class);
        intent.putExtra("sqlQuery", sqlQuery);
        intent.putExtra("param", param);
        intent.putExtra("keyword", param.replace("%", ""));
        startActivity(intent);
    }

    private void sendLocationToServer(LocationSearchDto data) {
        String mapX = data.getMapX();
        String mapY = data.getMapY();
        String radius = data.getRadius();
        service.searchByLocation(mapX, mapY, radius).enqueue(new Callback<List<CampingSiteDto>>() {
            @Override
            public void onResponse(Call<List<CampingSiteDto>> call, Response<List<CampingSiteDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CampingSiteDto> campingSites = response.body();
                    for(CampingSiteDto site: campingSites){
                        mapHandler.addCampingSitesMarker(site);
                    }
                }else {
                }
            }
            @Override
            public void onFailure(Call<List<CampingSiteDto>> call, Throwable t) {
                Log.e("CampingSites", "Response failed or empty body");
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady");
        mMap = googleMap;
        mapHandler = new MapHandler(requireContext(), mMap, mFusedLocationClient);
        mapHandler.onMapReady();

        mMap.setOnCameraIdleListener(() -> {
            LatLng center = mMap.getCameraPosition().target;
            LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

            loadCampingSitesByLocation(center, bounds);
        });

        mMap.setOnInfoWindowClickListener(marker -> {
            String siteName = (String) marker.getTag(); // 마커의 태그로 캠핑장 이름 가져옴
            if (siteName != null) {
                fetchSiteDetailAndNavigate(siteName);
            } else {
                Log.e("Map", "Marker tag is null");
            }
        });

        if (permissionHelper.checkPermissions()) {
            startLocationUpdates();
        } else {
            permissionHelper.requestPermissions();
        }
    }

    private void fetchSiteDetailAndNavigate(String siteName) {
        Log.d("CampingSiteDetail", "Fetching details for site: " + siteName);

        service.getSiteDetail(siteName).enqueue(new Callback<CampingSiteDto>() {
            @Override
            public void onResponse(Call<CampingSiteDto> call, Response<CampingSiteDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CampingSiteDto siteDetail = response.body();

                    // 다음 액티비티로 이동
                    Intent intent = new Intent(getActivity(), CampingSiteDetailActivity.class);
                    intent.putExtra("campingSite", siteDetail);
                    startActivity(intent);
                } else {
                    Log.e("CampingSiteDetail", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CampingSiteDto> call, Throwable t) {
                Log.e("CampingSiteDetail", "API call failed: " + t.getMessage());
            }
        });
    }

    private double calculateRadius(LatLngBounds bounds) {
        LatLng center = bounds.getCenter();

        LatLng northeast = bounds.northeast;

        float[] results = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                northeast.latitude, northeast.longitude,
                results);

        return results[0];
    }

    private void loadCampingSitesByLocation(LatLng center, LatLngBounds bounds) {
        String mapX = String.valueOf(center.longitude);
        String mapY = String.valueOf(center.latitude);

        double radius = calculateRadius(bounds);

        if (radius > 20000) {
            radius = 20000;
        }

        LocationSearchDto data = new LocationSearchDto(mapX, mapY, String.valueOf(radius));
        sendLocationToServer(data);
    }

    private void startLocationUpdates() {
        if (!permissionHelper.checkLocationServicesStatus()) {
            permissionHelper.showDialogForLocationServiceSetting();
            return;
        }

        if (permissionHelper.checkPermissions() && mapHandler != null) {
            mapHandler.startLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.handlePermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper.checkPermissions()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapHandler != null && permissionHelper.checkPermissions()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        if (mFusedLocationClient != null && locationCallback != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}