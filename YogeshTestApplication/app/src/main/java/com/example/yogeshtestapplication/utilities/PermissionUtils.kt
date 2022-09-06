package com.example.yogeshtestapplication.utilities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.yogeshtestapplication.R

class PermissionUtils {
    object PermissionUtils {
        fun asktAccessFineLocationPermission(activity: AppCompatActivity, requestId: Int) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                requestId
            )
        }

        fun checkAccessFineLocationGranted(context: Context): Boolean {
            return ContextCompat
                .checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        }

        fun isLocationEnabled(context: Context): Boolean {
            val gfgLocationManager: LocationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return gfgLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || gfgLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }

        fun showGPSNotEnabledDialog(context: Context) {
            AlertDialog.Builder(context)
                .setTitle("test")
                .setMessage("test2")
                .setCancelable(false)
                .setPositiveButton("test3") { _, _ ->
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .show()
        }
    }

}