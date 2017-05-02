package com.example.artam.mapspolygons;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.example.artam.mapspolygons.R.id.add;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    SharedPreferences sPref;

    Marker marker;
    String locality;
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

     //   LatLng markerPosition = marker.getPosition();

     //  double lat = latLng.latitude;
      // double lng = latLng.longitude;

     //Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());


        /*List<Address> geoList = null;
        try {
            geoList = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address address = geoList.get(0);
        String locality = address.getLocality();

        Log.i(TAG, "Address" + add);*/

        markersSet = new HashSet<>();
        markersSet.add(latLng.toString());

        //String lngString = String.valueOf(lng);
        //String latString = String.valueOf(lat);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(latLng.toString())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        marker = mMap.addMarker(options);

        Toast.makeText(getApplicationContext(),
                "New marker added \n" + latLng.toString(), Toast.LENGTH_LONG)
                .show();


            }





   /* public String getMarkerkLocation(Marker marker) {

        double lat = marker.getPosition().latitude;
        double lng = marker.getPosition().longitude;

        return lat, lng;


        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);

            Log.i(TAG, "Address" + add);

            return add;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }}
*/



    public void addMarkers() throws IOException {

        //  String markersLocation = marker.getPosition().toString();

    }

        public void storeMarkers() {

            sPref = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putStringSet("markers", markersSet);
            editor.apply();
        }

       /* marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(myLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));*/

    private boolean checkReady() { //Google Maps Android Samples
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /** Called when the Clear button is clicked. */
    public void onClearMap(View view) { //Google Maps Android Samples
        if (!checkReady()) {
            return;
        }
        mMap.clear();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "in onMapReady");
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);


        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
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


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public boolean isGPSEnabled (Context mContext){   //http://stackoverflow.com/questions/843675/how-do-i-find-out-if-the-gps-of-an-android-device-is-enabled
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    private void goToLocationZoom(double a, double b, float z){
        Log.i(TAG, "INgoToLocationZoom");
        LatLng latLng = new LatLng(a,b);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,z);
        mMap.moveCamera(cameraUpdate);
        Log.i(TAG, "CameraUpdated");


    }
    public void myGeoLocation(View view) throws IOException {
        Log.i(TAG, "inMyGeoLocation");
        EditText editText = (EditText) findViewById(R.id.editText);
        myLocation = editText.getText().toString();


        Geocoder geocoder = new Geocoder(this);
        try {

            List<Address> list = geocoder.getFromLocationName(myLocation, 1);
            Address address = list.get(0);
            locality = address.getLocality();

            double latitude = address.getLatitude();
            double longitude = address.getLongitude();

            goToLocationZoom(latitude,longitude,15);
            Log.i(TAG, "end of myGeoLocation");}
        catch (Exception E){
            Toast.makeText(getApplicationContext(), "Error: Incorrect place name", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error: Incorrect place name");
        }


       // Toast.makeText(this, locality, Toast.LENGTH_LONG).show();


    }


}
