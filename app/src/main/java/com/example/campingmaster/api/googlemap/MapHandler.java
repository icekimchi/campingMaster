package com.example.campingmaster.api.googlemap;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapHandler {
    private static final String TAG = "MAP";
    private GoogleMap mMap;
    private Marker currentMarker = null;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Context mContext;

    public MapHandler(Context context, GoogleMap googleMap, FusedLocationProviderClient fusedLocationClient) {
        this.mContext = context;
        this.mMap = googleMap;
        this.mFusedLocationClient = fusedLocationClient;
        setupLocationRequest();
    }

    private void setupLocationRequest() {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(500);
    }

    public void onMapReady() {
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMapClickListener(latLng -> Log.d(TAG, "onMapClick :"));
    }


    public void startLocationUpdates(LocationCallback locationCallback) {
        try {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException: Permission not granted.", e);
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

    public String getCurrentAddress(LatLng latlng) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
        } catch (IOException ioException) {
            Toast.makeText(mContext, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(mContext, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.isEmpty()) {
            Toast.makeText(mContext, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else {
            return addresses.get(0).getAddressLine(0);
        }
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
}
