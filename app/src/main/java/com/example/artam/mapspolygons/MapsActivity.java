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
    SharedPreferences sPref = null;

    Marker marker = null;
    String myLocation;
    Set <String> markersSet;

    final String TAG = "my_logs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Activity was created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.i(TAG, "Activity was created");



    }


    @Override
    public void onMapLongClick(LatLng latLng) {

        EditText editT = (EditText) findViewById(R.id.editText);
        myLocation = editT.getText().toString();

        markersSet = new HashSet<>();
        markersSet.add(latLng.toString());

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(myLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        marker = mMap.addMarker(options);

        Toast.makeText(getApplicationContext(),
                "New marker added \n" + latLng.toString(), Toast.LENGTH_LONG)
                .show();
//http://stackoverflow.com/questions/22817902/android-g-maps-markers-how-to-store-the-icon-in-shared-preferences
        sPref.edit().putString("Lat",String.valueOf(latLng.latitude)).apply();
        sPref.edit().putString("Lng",String.valueOf(latLng.longitude)).apply();
            }

        public void storeMarkers() {
           /* sPref = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putStringSet("markers", markersSet);
            editor.apply();*/
            }

    public void extractMarkers() { //http://stackoverflow.com/questions/22814490/how-to-save-a-marker-onmapclick

        sPref = this.getSharedPreferences("LatLng", MODE_PRIVATE);
        //Check whether your preferences contains any values then we get those values
        if ((sPref.contains("Lat")) && (sPref.contains("Lng"))) {
            String lat = sPref.getString("Lat", "");
            String lng = sPref.getString("Lng", "");
            LatLng l = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

            mMap.addMarker(new MarkerOptions()
                    .position(l));
        }
    }

    private boolean checkReady() { //Google Maps Android Samples
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /** Called when the Clear button is clicked. */

    public void onClearMarkers(View view) { //Google Maps Android Samples

        if (!checkReady()) {
            return;
        }
        mMap.clear();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.i(TAG, "in onMapReady");
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
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
            @Override
            public boolean onMyLocationButtonClick() {
                if (isGPSEnabled(getApplicationContext())){
                    return false;
                } else Toast.makeText(getApplicationContext(),
                        "Please enable your GPS for detecting your current position!",
                        Toast.LENGTH_LONG).show();
                return true;
                                }
        });
    }

    public boolean isGPSEnabled (Context mContext){   //http://stackoverflow.com/questions/843675/how-do-i-find-out-if-the-gps-of-an-android-device-is-enabled
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
          }



