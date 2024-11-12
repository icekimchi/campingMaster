package com.example.campingmaster.api.googlemap;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import com.example.campingmaster.api.gocamping.dto.CampingSiteDto;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapHandler {
    private static final String TAG = "MapHandler";
    private static final int UPDATE_INTERVAL_MS = 1000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500;

    private Context context;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private Marker currentMarker;

    public MapHandler(Context context, GoogleMap map, FusedLocationProviderClient locationClient) {
        this.context = context;
        this.mMap = map;
        this.fusedLocationClient = locationClient;
    }

    public void onMapReady() {
        setDefaultMapSettings();

        // 위치 업데이트 시작
    }

    private void setDefaultMapSettings() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void startLocationUpdates(LocationCallback locationCallback) {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        if (location != null) {
            currentLocation = location;
            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            if (currentMarker != null) {
                currentMarker.remove();
            }

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(currentLatLng)
                    .title(markerTitle)
                    .snippet(markerSnippet);

            currentMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        }
    }

    public void addCampingSiteMarker(CampingSiteDto campingSite) {
        if (campingSite != null && campingSite.getMapX() != null && campingSite.getMapY() != null) {
            try {
                // 캠핑지 좌표 추출
                double longitude = Double.parseDouble(campingSite.getMapX()); //127.5112565
                double latitude = Double.parseDouble(campingSite.getMapY()); //37.7278127
                LatLng position = new LatLng(latitude, longitude);
                Log.e("MapHandler", position.toString());

                // 마커 옵션 설정
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(position)
                        .title(campingSite.getName())
                        .snippet(campingSite.getAddress());

                // 마커 추가
                Marker marker = mMap.addMarker(markerOptions);

                // 마커가 제대로 추가되었으면, 카메라를 해당 위치로 이동
                if (marker != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                }
            } catch (NumberFormatException e) {
                Log.e("MapHandler", "Invalid coordinates: " + campingSite.getMapX() + ", " + campingSite.getMapY());
            }
        } else {
            Log.e("MapHandler", "Invalid camping site data: " + campingSite);
        }
    }

    public String getCurrentAddress(LatLng latlng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
        } catch (IOException ioException) {
            return "지오코더 서비스 사용 불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.isEmpty()) {
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        return address.getAddressLine(0);
    }
}
