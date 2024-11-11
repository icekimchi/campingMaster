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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.campingmaster.CampSiteResultActivity;
import com.example.campingmaster.R;
import com.example.campingmaster.adapter.CardViewAdapter;
import com.example.campingmaster.api.RetrofitClient;
import com.example.campingmaster.api.RetrofitService;
import com.example.campingmaster.api.gocamping.dto.CampingSiteDto;
import com.example.campingmaster.api.googlemap.MapHandler;
import com.example.campingmaster.utils.PermissionHelper;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "googlemap_example";
    private GoogleMap mMap;
    private MapHandler mapHandler;
    private static final int UPDATE_INTERVAL_MS = 1000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500;
    private FusedLocationProviderClient mFusedLocationClient;
    private PermissionHelper permissionHelper;
    private RetrofitService service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_main, container, false);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        service = RetrofitClient.getClient().create(RetrofitService.class);
        permissionHelper = new PermissionHelper(this, view);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        return view;
    }

    private void setupImageViewClick(View parentView, int imageViewId, String sqlQuery, String param) {
        ImageView imageView = parentView.findViewById(imageViewId);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CampSiteResultActivity.class);
            intent.putExtra("sqlQuery", sqlQuery);
            intent.putExtra("param", param);
            startActivity(intent);
        });
    }

    private void setupTextViewClick(View parentView, int imageViewId, String sqlQuery, String param) {
        TextView textView = parentView.findViewById(imageViewId);
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CampSiteResultActivity.class);
            intent.putExtra("sqlQuery", sqlQuery);
            intent.putExtra("param", param);
            startActivity(intent);
        });
    }

    private void searchCategory(String sqlQuery, String param) {
        Map<String, Object> query = new HashMap<>();
        query.put("sqlQuery", sqlQuery);
        query.put("param", param);

        service.searchQuery(query).enqueue(new Callback<List<CampingSiteDto>>() {
            @Override
            public void onResponse(Call<List<CampingSiteDto>> call, Response<List<CampingSiteDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CampingSiteDto> campingSites = response.body();
                    CardViewAdapter adapter = new CardViewAdapter(campingSites);
                } else {
                    Toast.makeText(getContext(), "No data found for this category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CampingSiteDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady :");
        mMap = googleMap;
        mapHandler = new MapHandler(getContext(), mMap, mFusedLocationClient);
        if (mapHandler != null) {
            mapHandler.onMapReady();
            if (permissionHelper.checkPermissions()) {
                startLocationUpdates(); // 위치 업데이트 시작
            } else {
                permissionHelper.requestPermissions(); // 권한 요청
            }
        } else {
            Log.e(TAG, "Failed to initialize mapHandler.");
        }
    }

    private void startLocationUpdates() {
        if (mapHandler == null) {
            Log.e(TAG, "mapHandler is null. Cannot start location updates.");
            return;
        }

        if (!permissionHelper.checkLocationServicesStatus()) {
            permissionHelper.showDialogForLocationServiceSetting();
        } else if (permissionHelper.checkPermissions()) {
            mapHandler.startLocationUpdates(locationCallback);
        }
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            List<Location> locationList = locationResult.getLocations();
            if (!locationList.isEmpty()) {
                Location location = locationList.get(locationList.size() - 1);
                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                String markerTitle = mapHandler.getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + location.getLatitude() + " 경도:" + location.getLongitude();
                mapHandler.setCurrentLocation(location, markerTitle, markerSnippet);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (mapHandler != null && permissionHelper.checkPermissions()) {
            mapHandler.startLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}