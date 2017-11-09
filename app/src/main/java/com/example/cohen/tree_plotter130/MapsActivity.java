package com.example.cohen.tree_plotter130;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;


import java.io.File;
import java.util.ArrayList;

class TrackThread extends Thread {
    private MapsActivity mapsActivity = new MapsActivity();


    @Override
    public void run() {

        }

}



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, Animation.AnimationListener{

    public GoogleMap mMap;
    LocationManager manager;
    private static final String ADD_PIN = "Add Pin";
    private static final String FILENAME = "route.gpx";
    private final static int MY_PERMISSIONS = 101;
    private static final String TAG = MapsActivity.class.getName();
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleApiClient googleApiClient;
    private double latitude;
    private double longitude;
    public ArrayList<Object> points;
    public ArrayList<String> lat;
    public ArrayList<String> lng;
    public ArrayList<String> pinLat;
    public ArrayList<String> pinLng;
    Polyline polyline;
    LatLng latLng;
    TrackThread t;
    Animation fade_out;
    Animation fade_in;
    String name;
    String owner;
    String mapType;
    String address;
    String date;
    SQLHelper sqlHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        locationRequest = new LocationRequest();
        locationRequest.setInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //Decelerations
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        points = new ArrayList<Object>();
        t = new TrackThread();
        final ImageButton imageButton = findViewById(R.id.Start_Stop);
        fade_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        lat = new ArrayList<>();
        lng = new ArrayList<>();
        pinLat = new ArrayList<>();
        pinLng = new ArrayList<>();
        sqlHelper = new SQLHelper(this);


        //Setting image values
        ImageView AddButton = new ImageView(this);
        AddButton.setImageResource(R.drawable.plus_green);

        ImageView BlueTag = new ImageView(this);
        BlueTag.setImageResource(R.drawable.tag_blue_circle);

        ImageView GreenTag = new ImageView(this);
        GreenTag.setImageResource(R.drawable.tag_green_circle);

        ImageView RedTag = new ImageView(this);
        RedTag.setImageResource(R.drawable.tag_red);

        ImageView GreyTag = new ImageView(this);
        GreyTag.setImageResource(R.drawable.tag_gray);

        fade_out.setAnimationListener(this);
        fade_in.setAnimationListener(this);

        Intent intent = this.getIntent();
        name = intent.getStringExtra("name");
        owner = intent.getStringExtra("owner");
        mapType = intent.getStringExtra("mapType");
        address = intent.getStringExtra("address");
        date = intent.getStringExtra("date");

    }


            //*******************************************************************************

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
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS);
                    }
                }

            }


            //*********************************************************************************

            //Requesting permission for location services
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                switch (requestCode) {
                    case MY_PERMISSIONS:
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                mMap.setMyLocationEnabled(true);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "This app cannot run successfully without your current location", Toast.LENGTH_LONG).show();
                            finish();

                        }
                        break;
                }


            }


            //*********************************************************************************


            @Override
            public void onConnected(@Nullable Bundle bundle) {
                requestLocationUpdates();
            }

            //*********************************************************************************


            private void requestLocationUpdates() {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                }
            }

            //*********************************************************************************


            @Override
            public void onConnectionSuspended(int i) {

            }


            //*********************************************************************************


            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }


            //*********************************************************************************


            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                latLng = new LatLng(latitude, longitude);


            }





            //*********************************************************************************


            @Override
            protected void onStart() {
                super.onStart();
                googleApiClient.connect();
            }


            //*********************************************************************************


            @Override
            protected void onStop() {
                super.onStop();
                googleApiClient.disconnect();
            }

            //*********************************************************************************


            @Override
            protected void onResume() {
                super.onResume();
                if (googleApiClient.isConnected()) {
                    requestLocationUpdates();
                }
            }

            //*********************************************************************************


            @Override
            protected void onPause() {
                super.onPause();
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            }

            //*********************************************************************************


            public void addMarker(View view) {
                String[] names = {"Green pin", "Red pin", "Yellow pin", "Blue pin"};
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = getLayoutInflater();
                View content = (View) layoutInflater.inflate(R.layout.dialog_layout_maps, null);
                alert.setView(content);
                alert.setTitle("Select a Pin");
                final ListView listView = (ListView) content.findViewById(R.id.lv);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
                listView.setAdapter(arrayAdapter);
                final Dialog dialog = alert.show();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i) {
                            case 0:
                                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                dialog.dismiss();
                                pinLat.add(String.valueOf(latitude));
                                pinLng.add(String.valueOf(longitude));
                                break;
                            case 1:
                                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                dialog.dismiss();
                                pinLat.add(String.valueOf(latitude));
                                pinLng.add(String.valueOf(longitude));
                                break;
                            case 2:
                                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                                dialog.dismiss();
                                pinLat.add(String.valueOf(latitude));
                                pinLng.add(String.valueOf(longitude));
                                break;
                            case 3:
                                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                dialog.dismiss();
                                pinLat.add(String.valueOf(latitude));
                                pinLng.add(String.valueOf(longitude));
                                break;
                        }
                    }

                });
            }

    public BitmapDescriptor getMarkerColour(String colour) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(colour), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);

    }

    public void startStop(View  view){
                view.startAnimation(fade_out);
                fadeInButtons();




    }

    public void pause(View view){
        view.startAnimation(fade_out);
        View view1 = findViewById(R.id.ReStart);
        view1.startAnimation(fade_in);
    }

    public void stop(View view){
        sqlHelper.insertData(owner, name, address, date, mapType, pinLat.toString(), pinLng.toString(), lat.toString(), lng.toString());
        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void reStart(View view){

    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void fadeInButtons(){
        View view = findViewById(R.id.stop);
        View view1 = findViewById(R.id.pause);
        view.setAnimation(fade_in);
        view1.setAnimation(fade_in);
        view.startAnimation(fade_in);
        view1.startAnimation(fade_in);
        view.setVisibility(View.VISIBLE);
        view1.setVisibility(View.VISIBLE);
    }
}