package com.example.artam.mapspolygons;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.location.SettingInjectorService;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.example.artam.mapspolygons.R.id.add;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    int markersCount;
    final String TAG = "my_logs";

    SharedPreferences sPref = null;
    Marker marker = null;
    String myLocation, markersCountStr;
    LatLng position1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Activity was created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.i(TAG, "onMapLongClick");

        EditText editT = (EditText) findViewById(R.id.editText);
        myLocation = editT.getText().toString();

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(myLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        marker = mMap.addMarker(options);

        Toast.makeText(getApplicationContext(),
                "New marker added \n" + latLng.toString(), Toast.LENGTH_LONG)
                .show();

        markersCount++;
        markersCountStr = Integer.toString(markersCount);
        Toast.makeText(getApplicationContext(), markersCountStr, Toast.LENGTH_SHORT).show();

        // http://stackoverflow.com/questions/22814490/how-to-save-a-marker-onmapclick
        sPref.edit().putString("Lat" + Integer.toString((markersCount - 1)), Double.toString(latLng.latitude)).apply();
        sPref.edit().putString("Lng" + Integer.toString((markersCount - 1)), Double.toString(latLng.longitude)).apply();
        sPref.edit().putString("myLocation", myLocation).apply();
        sPref.edit().putInt("markersCount", markersCount).apply();
    }

    public void extractMarkers() { //http://stackoverflow.com/questions/35868807/saving-google-map-markers-into-sharedpreferences-in-android-studios

        Log.i(TAG, "extractMarkers");
        sPref = this.getSharedPreferences("location", 0);
        Log.i(TAG, "extractMarkers - getSharedPreferences");
        markersCount = sPref.getInt("markersCount", 0);
        Log.i(TAG, "extractMarkers - get markersCount");

        if (markersCount != 0) {
            Log.i(TAG, "extractMarkers");
            String lat = "";
            String lng = "";
            String myLocation = "";

            // Iterating through all the locations stored
            for (int i = 0; i < markersCount; i++) {
                Log.i(TAG, "extractMarkers - in for loop");
                // Getting the latitude of the i-th location
                lat = sPref.getString("Lat" + i, "0");
                Log.i(TAG, "extractMarkers - get lat");
                // Getting the longitude of the i-th location
                lng = sPref.getString("Lng" + i, "0");
                Log.i(TAG, "extractMarkers - get lng");

                myLocation = sPref.getString("myLocation", "0");

                Toast.makeText(this, "Location: " + myLocation + "\n " + "Latitude: " + lat + "\n "
                        + "Longitude: " + lng, Toast.LENGTH_LONG).show();

                double lat3 = Double.valueOf(lat);
                double lng3 = Double.valueOf(lng);

                position1 = new LatLng(lat3, lng3);

                mMap.addMarker(new MarkerOptions()
                        .position(position1)
                        .title(myLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                Log.i(TAG, "extractMarkers - I am done");
            }
        }
    }

    /**
     * Called when the Clear button is clicked.
     */

    public void onClearMarkers(View view) { //Google Maps Android Samples
        Log.i(TAG, "onClearMarkers");

        mMap.clear();
        sPref.edit().clear().apply();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.i(TAG, "onMapReady");
        mMap = googleMap;
        extractMarkers();
        mMap.setOnMapLongClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (isGPSEnabled(getApplicationContext())) {
                    return false;
                } else Toast.makeText(getApplicationContext(),
                        "Please enable your GPS for detecting your current position!",
                        Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    public boolean isGPSEnabled(Context mContext) {   //http://stackoverflow.com/questions/843675/how-do-i-find-out-if-the-gps-of-an-android-device-is-enabled
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}



