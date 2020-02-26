package com.example.smartanimalrecognitionapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    public static final int MY_PERMISSION_REQUEST_LOCATION = 99;
    boolean doubleBackToExitPressedOnce = false;
    private StorageReference mStorageRef;

    public DatabaseReference myRefLat;
    public DatabaseReference myRefLon;
    private String latitude;
    private String longitude;

    private StorageReference storageReference;
    private StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //If location off, direct to the location settings window
        int off = 0;
        try {
            off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (off == 0) {
            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(onGPS);
        }

        checkLocationPermission();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        getLocation();

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);



        displayImages();

    }


    public void retrieveLatitude() {

//        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(MainActivity.this);


        // Read latitude from the database
        myRefLat = FirebaseDatabase.getInstance().getReference().child("Location").child("Latitude");
        myRefLat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    latitude = ds.getValue(String.class);
                    //#System.out.println("Latitude: " + latitude);

                }setLatitude(latitude);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
                System.out.println("Error: " + databaseError.getMessage());

            }
        });
    }


    public void setLatitude(String lati) {
        this.latitude = lati;
        //#System.out.println("hi there latitude, " + latitude);
        //Log.d(TAG, "hi there, " + yourNameVariable);
    }

    public void retrieveLongitude(){

//        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);


        // Read longitude from the database
        myRefLon = FirebaseDatabase.getInstance().getReference().child("Location").child("Longitude");
        myRefLon.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    longitude = ds.getValue(String.class);
                    //#System.out.println("Longitude: " + longitude);

                }setLongitude(longitude);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
                System.out.println("Error: " + databaseError.getMessage());

            }
        });

    }

    public void setLongitude(String longi) {
        this.longitude = longi;
        //#System.out.println("hi there longitude, " + longitude);
        //Log.d(TAG, "hi there, " + yourNameVariable);
    }

    public String getLatitude() {
        //retrieveLatitude();
        return latitude;
    }

    public String getLongitude() {
        //retrieveLongitude();
        return longitude;
    }

    public void getLocation() {
        retrieveLatitude();
        retrieveLongitude();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.floating_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.btnAbout:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                //go to about activity
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                finish();
                break;

            /*case R.id.btnExit:
                //Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();
                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                        .setTitle("Exit Smart Animal Recognition")
                        .setMessage("Are you sure you want to exit?")
                        // .setIcon(R.drawable.button_face_1)
                        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_LONG).show();
                                finish();
                                System.exit(0);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                myQuittingDialogBox.show();
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    //Check whether location permissions allow or not by default, if not request for location permissions
    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSION_REQUEST_LOCATION);
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSION_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_REQUEST_LOCATION);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_REQUEST_LOCATION);

            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                    } else if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    }
                } else {
                    //checkLocationPermission();
                }
                return;
            }
        }
    }





    @Override
    public void onMapReady(GoogleMap googleMap) {

        //getLocation();
        String latit = getLatitude();
        String longit = getLongitude();

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        //#System.out.println("wtfwtfwtfwtfwtf.................." + latit + " " + longit);

        if (latit != null && longit != null) {
            //#System.out.println("###################" + latit);
            //#System.out.println("###################" + longit);
            mMap = googleMap;

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            //checkLocationPermission();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            //#System.out.println("Starting values.............");
            Double lat = Double.valueOf(latit);
            Double lon = Double.valueOf(longit);
            //Double lat = Double.valueOf(latitude);
            //Double lon = Double.valueOf(longitude);
            //System.out.println("Lat: " + lat);
            //System.out.println("Lon: " + lon);
            //#System.out.println("Ending values.............");

            //LatLng loc1 = new LatLng(-1*lat, lon);
            LatLng loc1 = new LatLng(lat, lon);

            if (mMap != null) {

                //Instantiate Geocoder class
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {

                    //Set the snippet
                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoWindow(Marker arg0) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {

                            Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                            LinearLayout info = new LinearLayout(context);
                            info.setOrientation(LinearLayout.VERTICAL);

                            TextView title = new TextView(context);
                            title.setTextColor(Color.BLACK);
                            title.setGravity(Gravity.CENTER);
                            title.setTypeface(null, Typeface.BOLD);
                            title.setText(marker.getTitle());

                            TextView snippet = new TextView(context);
                            snippet.setTextColor(Color.GRAY);
                            snippet.setText(marker.getSnippet());

                            info.addView(title);
                            info.addView(snippet);

                            return info;
                        }
                    });

                    List<Address> addressList = geocoder.getFromLocation(Math.round(lat * 100.0) / 100.0, Math.round(lon * 10.0) / 10.0, 1);
                    String str =   "Latitude: " + addressList.get(0).getLatitude() + "\n" +
                                    "Longitude: " + addressList.get(0).getLongitude() + "\n" +
                                    "Location: " + addressList.get(0).getLocality();
                    mMap.addMarker(new MarkerOptions()
                            .position(loc1)
                            .snippet(str)
                            .title("Animal's Location")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc1, 10.2f));
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc1, 12));
            } else {
                System.out.println("Location is empty......");
            }
        }

    }


   /* @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, RecordVoiceActivity.class);
        startActivity(intent);
        finish();
    }


    @SuppressLint("SetTextI18n")
    public void displayImages() {

        storageReference = FirebaseStorage.getInstance().getReference();

        TextView textViewResult = (TextView) findViewById(R.id.textViewResult);

        String imageKey = "bat";

        switch(imageKey) {
            case "bat":
                ref = storageReference.child("bat.jpg");
                textViewResult.setText("The calling animal is a BAT");
                break;
            case "elephant":
                ref = storageReference.child("elephant.jpg");
                textViewResult.setText("The calling animal is an ELEPHANT");
                break;
            case "hornbill":
                ref = storageReference.child("hornbill.JPG");
                textViewResult.setText("The calling animal is a HORNBILL");
                break;
            case "junglefowl":
                ref = storageReference.child("junglefowl.jpg");
                textViewResult.setText("The calling animal is a JUNGLEFOWL");
                break;
            case "macaque":
                ref = storageReference.child("macaque.jpg");
                textViewResult.setText("The calling animal is an MACAQUE");
                break;
            case "myna":
                ref = storageReference.child("myna.jpg");
                textViewResult.setText("The calling animal is a MYNA");
                break;
            case "peafowl":
                ref = storageReference.child("peafowl.jpg");
                textViewResult.setText("The calling animal is a PEAFOWL");
                break;
            case "pig":
                ref = storageReference.child("pig.jpg");
                textViewResult.setText("The calling animal is an WILD BOAR");
                break;
            case "squirrel":
                ref = storageReference.child("squirrel.jpg");
                textViewResult.setText("The calling animal is a SQUIRREL");
                break;
            case "toad":
                ref = storageReference.child("toad.jpg");
                textViewResult.setText("The calling animal is a TOAD");
                break;
            default:
                ref = storageReference.child("error.jpg");
                textViewResult.setText("ERROR FROM THE DATABASE");
                break;
        }

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            ImageView image = (ImageView) findViewById(R.id.imageViewAnimal);
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                Picasso.with(MainActivity.this).load(url).fit().centerCrop().into(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }

}
