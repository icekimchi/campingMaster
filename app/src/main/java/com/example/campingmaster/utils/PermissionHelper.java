package com.example.campingmaster.utils;

import android.Manifest;
import android.app.Activity;
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
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private Context context;
    private Fragment fragment;

    public PermissionHelper(Context context) {
        this.context = context;
    }

    public PermissionHelper(Fragment fragment) {
        this.fragment = fragment;
        this.context = fragment.requireContext();
    }

    public boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions() {
        if (fragment != null) {
            fragment.requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        } else if (context instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) context,
                    REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", (dialog, id) -> {
            Intent callGPSSettingIntent =
                    new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            if (fragment != null) {
                fragment.startActivity(callGPSSettingIntent);
            } else {
                context.startActivity(callGPSSettingIntent);
            }
        });
        builder.setNegativeButton("취소", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }

    public void handlePermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == REQUIRED_PERMISSIONS.length) {
            boolean checkResult = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    checkResult = false;
                    break;
                }
            }

            if (!checkResult) {
                View rootView;
                if (fragment != null) {
                    rootView = fragment.requireView();
                } else {
                    rootView = ((Activity) context).findViewById(android.R.id.content);
                }

                if (shouldShowRequestPermissionRationale(REQUIRED_PERMISSIONS[0]) ||
                        shouldShowRequestPermissionRationale(REQUIRED_PERMISSIONS[1])) {
                    Snackbar.make(rootView,
                                    "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.",
                                    Snackbar.LENGTH_INDEFINITE)
                            .setAction("확인", view -> {
                                if (fragment != null) {
                                    fragment.requireActivity().finish();
                                } else {
                                    ((Activity) context).finish();
                                }
                            }).show();
                } else {
                    Snackbar.make(rootView,
                                    "퍼미션이 거부되었습니다. 설정에서 퍼미션을 허용해야 합니다.",
                                    Snackbar.LENGTH_INDEFINITE)
                            .setAction("확인", view -> {
                                if (fragment != null) {
                                    fragment.requireActivity().finish();
                                } else {
                                    ((Activity) context).finish();
                                }
                            }).show();
                }
            }
        }
    }

    private boolean shouldShowRequestPermissionRationale(String permission) {
        if (fragment != null) {
            return fragment.shouldShowRequestPermissionRationale(permission);
        } else {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission);
        }
    }
}