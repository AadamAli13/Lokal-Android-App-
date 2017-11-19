package com.example.aadam.lokalandroid.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.aadam.lokalandroid.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

import static java.lang.Math.cos;

/**
 * Main activity of program, shows a map with public events
 * @author Samer Alabi
 */
@SuppressWarnings({"FieldCanBeLocal", "ConstantConditions"})
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, View.OnClickListener,
        ChildEventListener, LocationListener {
    // Declare constants
    private final double LAT_DEGREE_TO_KM = 110.574;
    private final double LONG_DEGREE_TO_KM = 111.320;

    // Declare attributes
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private ImageButton locationButton;
    private DatabaseReference mainRef;
    private HashMap<String, Marker> markerHashMap;
    private LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Get the main reference from firebase
        mainRef = FirebaseDatabase.getInstance().getReference();

        // Create hash map
        markerHashMap = new HashMap<>();

        // Get Location Manager and check for GPS & Network location services
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Create map fragment
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create location button
        locationButton = findViewById(R.id.locationButton);
        locationButton.setOnClickListener(this);
    } // End of method

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Check the permissions being enabled
        if (requestCode == 0) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
                enableLocationServices();
            }
            else {
                Toast.makeText(this.getApplicationContext(), "Location services could not be enabled", Toast.LENGTH_LONG).show();
            } // End of if statement
        } // End of if statement
    } // End of if statement

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Store the map, set map listener, and turn off location button
        map = googleMap;
        map.setOnMapLongClickListener(this);
        map.getUiSettings().setMyLocationButtonEnabled(false);
    } // End of method

    @Override
    public void onMapLongClick(LatLng latLng) {

    } // End of method

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    } // End of method

    @Override
    public void onClick(View view) {
        // Check if location permission are enabled
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Enable location and location services
            map.setMyLocationEnabled(true);

            // Check if location is enabled
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                // Remove all markers on map
                for (String id : markerHashMap.keySet()) {
                    markerHashMap.remove(id).remove();
                } // End of for loop

                // Request location update
                Criteria criteria = new Criteria();
                lm.requestLocationUpdates(lm.getBestProvider(criteria, true), 1000, 1, this);
            }
            else {
                enableLocationServices();
            } // End of if statement
        }
        else {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } // End of if statement
    } // End of method

    /**
     * Builds an alert dialog for enabling location services
     */
    private void enableLocationServices() {
        // Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Services Not Active");
        builder.setMessage("Please enable Location Services and GPS");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                // Show location settings when the user acknowledges the alert dialog
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            } // End of method
        }); // End of method

        // Build dialog
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    } // End of method

    /**
     * Get data set from firebase
     * @param latLng is an object storing latitude and longitude
     */
    private void getDataSet(LatLng latLng) {
        // Get latitude and longitude in of a 6 km radius
        double latitudeRange = 6 / LAT_DEGREE_TO_KM;
        double longitudeRange = 6 / (LONG_DEGREE_TO_KM * cos(latLng.latitude));

        // Get the query
        Query queryForData = mainRef.child("detailed_event").orderByChild("lat~long")
                .startAt(Double.toString(latLng.latitude - latitudeRange) + "~" + Double.toString(latLng.longitude - longitudeRange))
                .endAt(Double.toString(latLng.latitude + latitudeRange) + "~" + Double.toString(latLng.longitude + longitudeRange));

        // Add listener to query
        queryForData.addChildEventListener(this);
    } // End of method

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        // Store data
        if (dataSnapshot.hasChild("eventName") && dataSnapshot.hasChild("lat~long")) {
            Toast.makeText(this, dataSnapshot.getKey(), Toast.LENGTH_LONG).show();
            String id = dataSnapshot.getKey();
            String title = dataSnapshot.child("eventName").getValue().toString();
            String[] latLngArr = dataSnapshot.child("lat~long").getValue().toString().split("~");
            LatLng latLng = new LatLng(Double.parseDouble(latLngArr[0]), Double.parseDouble(latLngArr[1]));

            // Add marker
            Marker marker = map.addMarker(new MarkerOptions().title(title).position(latLng));
            marker.setTag(id);
        } // End of if statement
    } // End of method

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        // Store data in attributes
        if (dataSnapshot.hasChild("eventName") && dataSnapshot.hasChild("lat~long")) {
            String id = dataSnapshot.getKey();
            String title = dataSnapshot.child("eventName").getValue().toString();
            String[] latLngArr = dataSnapshot.child("lat~long").getValue().toString().split("~");
            LatLng latLng = new LatLng(Double.parseDouble(latLngArr[0]), Double.parseDouble(latLngArr[1]));

            // Add marker to map and list
            Marker marker = map.addMarker(new MarkerOptions().title(title).position(latLng));
            marker.setTag(id);
            markerHashMap.put(id, marker);
        } // End of if statement
    } // End of method

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        // Store key in attribute
        String id = dataSnapshot.child("eventName").getKey();

        // Remove marker containing tag
        markerHashMap.remove(id).remove();
    } // End of method

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
    } // End of method

    @Override
    public void onCancelled(DatabaseError databaseError) {
        databaseError.getMessage();
    } // End of method

    @Override
    public void onLocationChanged(Location location) {
        // Move camera to position
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));

        // Get and store new data set
        getDataSet(new LatLng(location.getLatitude(), location.getLongitude()));

        // Remove updates
        lm.removeUpdates(this);
    } // End of method

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    } // End of method

    @Override
    public void onProviderEnabled(String s) {
    } // End of method

    @Override
    public void onProviderDisabled(String s) {
    } // End of method
} // End of class
