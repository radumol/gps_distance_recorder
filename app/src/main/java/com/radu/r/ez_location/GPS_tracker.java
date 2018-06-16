package com.radu.r.ez_location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class GPS_tracker implements LocationListener{
    Context context;

    public GPS_tracker(Context c){
        context = c;
    }

    public Location getLocation(){
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context,"Permission not granted",Toast.LENGTH_SHORT).show();
            return null;
        }

        boolean isGPSenabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSenabled){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,6000,10,this);
            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return l;
        }else{
            Toast.makeText(context, "Please enable GPS", Toast.LENGTH_LONG).show();
            return null;
        }

    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}