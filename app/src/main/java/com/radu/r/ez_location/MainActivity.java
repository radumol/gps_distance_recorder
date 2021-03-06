package com.radu.r.ez_location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static double distanceRec = 0;
    private Location prevLocation;

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 12 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 8000; /* 2 sec */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startLocationUpdates();
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Request runtime permission
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 7123);
        }

        GPS_tracker g = new GPS_tracker(getApplicationContext());
        prevLocation = g.getLocation();

        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onLocationChanged(Location location) {
        // New location has now been determined

        //distanceRec = distanceRec + DistanceCalculator.distance(location.getLatitude(), location.getLongitude(), prevLocation.getLatitude(), prevLocation.getLongitude(), "K");
        double currentDistance = location.distanceTo(prevLocation); //in meters
        distanceRec = distanceRec + currentDistance;


        String msg1 = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude()) + ", Altitude:" + Double.toString(location.getAltitude()) + ", Has alt: " + location.hasAltitude() + ", Accuracy: " + location.getAccuracy()
                + ", Mockprovidor " + location.isFromMockProvider() + ", speed: " + location.getSpeed() + ", time: " + DateFormat.getDateInstance(DateFormat.SHORT).format(location.getTime());

        String msg = "\nAA" + "\nCurrent Distance: " + currentDistance +"\nDistance: " + distanceRec + "\nUpdated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude()) + "\nOld Location: " +
                Double.toString(prevLocation.getLatitude()) + "," +
                Double.toString(prevLocation.getLongitude());


        System.out.println(msg);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        prevLocation = location;
    }
}
