package com.example.barcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class GPSUtils {
    private Activity activity;

    public GPSUtils(Activity activity) {
        this.activity = activity;
    }

    public void statusCheck(View view) {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showSnackbar(view, " يجب تفعيل خدمة GPS.");
        }
    }

    private void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("تفعيل GPS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openLocationSettings();
                    }
                });

        snackbar.show();
    }

    private void openLocationSettings() {
        activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
}
