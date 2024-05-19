package com.example.campingmaster.utils;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class PermissionHelper {

    public static final int GPS_ENABLE_REQUEST_CODE = 2001;
    public static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private Fragment fragment;
    private View view;

    public PermissionHelper(Fragment fragment, View view) {
        this.fragment = fragment;
        this.view = view;
    }

    public boolean checkPermissions() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

        Log.d("PermissionHelper", "hasFineLocationPermission: " + (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED));
        Log.d("PermissionHelper", "hasCoarseLocationPermission: " + (hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED));

        return hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.requireActivity(), REQUIRED_PERMISSIONS[0])) {
            Snackbar.make(view, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(fragment.requireActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(fragment.requireActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) fragment.requireActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.requireActivity());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                fragment.startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public void handlePermissionsResult(int requestCode, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == REQUIRED_PERMISSIONS.length) {
            boolean checkResult = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    checkResult = false;
                    break;
                }
            }
            if (!checkResult) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.requireActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(fragment.requireActivity(), REQUIRED_PERMISSIONS[1])) {
                    Snackbar.make(view, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Snackbar.LENGTH_INDEFINITE)
                            .setAction("확인", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    fragment.requireActivity().finish();
                                }
                            }).show();
                } else {
                    Snackbar.make(view, "퍼미션이 거부되었습니다. 설정에서 퍼미션을 허용해야 합니다.", Snackbar.LENGTH_INDEFINITE)
                            .setAction("확인", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    fragment.requireActivity().finish();
                                }
                            }).show();
                }
            }
        }
    }
}
