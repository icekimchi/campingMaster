package com.example.campingmaster.fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campingmaster.CampSiteResultActivity;
import com.example.campingmaster.R;
import com.example.campingmaster.adapter.CardViewAdapter;
import com.example.campingmaster.api.RetrofitClient;
import com.example.campingmaster.api.RetrofitService;
import com.example.campingmaster.api.gocamping.dto.CampingSiteDto;
import com.example.campingmaster.utils.PermissionHelper;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "googlemap_example";
    private GoogleMap mMap;
    private Marker currentMarker = null;
    private static final int UPDATE_INTERVAL_MS = 1000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location mCurrentLocation;
    private LatLng currentPosition;
    private View mLayout;
    private PermissionHelper permissionHelper;
    private RetrofitService service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_main, container, false);
        mLayout = view.findViewById(R.id.map);
        service = RetrofitClient.getClient().create(RetrofitService.class);
        permissionHelper = new PermissionHelper(this, mLayout);

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupImageViewClick(view, R.id.cat_normal, "SELECT * FROM campsite WHERE category LIKE ?", "%일반야영장%");
        setupImageViewClick(view, R.id.cat_carvan, "SELECT * FROM campsite WHERE category LIKE ?", "%카라반%");
        setupImageViewClick(view, R.id.cat_glamping, "SELECT * FROM campsite WHERE category LIKE ?", "%글램핑%");
        setupImageViewClick(view, R.id.cat_sunrise, "SELECT * FROM campsite WHERE thema_envrn_cl LIKE ?", "%일출%");
        setupImageViewClick(view, R.id.cat_sunset, "SELECT * FROM campsite WHERE thema_envrn_cl LIKE ?", "%일몰%");
        setupImageViewClick(view, R.id.cat_spring, "SELECT * FROM campsite WHERE season LIKE ?", "%봄%");
        setupImageViewClick(view, R.id.cat_summer, "SELECT * FROM campsite WHERE season LIKE ?", "%여름%");
        setupImageViewClick(view, R.id.cat_animal, "SELECT * FROM campsite WHERE pet_allowed = ?", "1");
        setupImageViewClick(view, R.id.cat_pool, "SELECT * FROM campsite WHERE thema_envrn_cl LIKE ?", "%물놀이%");
        setupImageViewClick(view, R.id.cat_seoul, "SELECT * FROM campsite WHERE address LIKE ?", "%서울%");

        setupTextViewClick(view, R.id.key_spring, "SELECT * FROM campsite WHERE nearby_facilities LIKE ?", "%수상레저%");
        setupTextViewClick(view, R.id.key_fall, "SELECT * FROM campsite WHERE thema_envrn_cl LIKE ?", "%가을단풍명소%");
        setupTextViewClick(view, R.id.key_winter, "SELECT * FROM campsite WHERE season LIKE ?", "%겨울%");
        setupTextViewClick(view, R.id.key_bbq, "SELECT * FROM campsite WHERE thema_envrn_cl LIKE ?", "%액티비티%");
        setupTextViewClick(view, R.id.key_play, "SELECT * FROM campsite WHERE nearby_facilities LIKE ?", "%놀이시설%");
        setupTextViewClick(view, R.id.key_baby, "SELECT * FROM campsite WHERE pet_allowed = ?", "1");
        setupTextViewClick(view, R.id.key_tour, "SELECT * FROM campsite WHERE nearby_facilities LIKE ?", "%농어촌체험%");
        setupTextViewClick(view, R.id.key_beach, "SELECT * FROM campsite WHERE location_category = ?", "해변");
        setupTextViewClick(view, R.id.key_deck, "SELECT * FROM campsite WHERE location_category LIKE ?", "%숲%");
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
        setDefaultLocation();

        if (permissionHelper.checkPermissions()) {
            Log.d(TAG, "Have Permissions :");
            startLocationUpdates();
        } else {
            Log.d(TAG, "Don't have Permissions :");
            permissionHelper.requestPermissions();
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMapClickListener(latLng -> Log.d(TAG, "onMapClick :"));
    }

    private void startLocationUpdates() {
        if (!permissionHelper.checkLocationServicesStatus()) {
            permissionHelper.showDialogForLocationServiceSetting();
        } else if (permissionHelper.checkPermissions()) {
            try {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                if (checkPermission()) mMap.setMyLocationEnabled(true);
            } catch (SecurityException e) {
                Log.e(TAG, "SecurityException: Permission not granted.", e);
            }
        }
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            List<Location> locationList = locationResult.getLocations();
            if (!locationList.isEmpty()) {
                Location location = locationList.get(locationList.size() - 1);
                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + location.getLatitude() + " 경도:" + location.getLongitude();
                setCurrentLocation(location, markerTitle, markerSnippet);
                mCurrentLocation = location;
            }
        }
    };

    public String getCurrentAddress(LatLng latlng) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
        } catch (IOException ioException) {
            Toast.makeText(getContext(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(getContext(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.isEmpty()) {
            Toast.makeText(getContext(), "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else {
            return addresses.get(0).getAddressLine(0);
        }
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        if (currentMarker != null) currentMarker.remove();
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        currentMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
    }

    public void setDefaultLocation() {
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";

        if (currentMarker != null) currentMarker.remove();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15));
    }

    private boolean checkPermission() {
        return permissionHelper.checkPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.handlePermissionsResult(requestCode, grantResults);
        if (requestCode == PermissionHelper.PERMISSIONS_REQUEST_CODE) {
            if (checkPermission()) {
                startLocationUpdates();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionHelper.GPS_ENABLE_REQUEST_CODE) {
            if (permissionHelper.checkLocationServicesStatus() && permissionHelper.checkPermissions()) {
                startLocationUpdates();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (checkPermission()) {
            try {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                if (mMap != null) mMap.setMyLocationEnabled(true);
            } catch (SecurityException e) {
                Log.e(TAG, "SecurityException: Permission not granted.", e);
            }
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
