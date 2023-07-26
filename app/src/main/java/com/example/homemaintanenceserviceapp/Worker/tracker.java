package com.example.homemaintanenceserviceapp.Worker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.homemaintanenceserviceapp.Model.LocationUpdate;
import com.example.homemaintanenceserviceapp.R;
import com.example.homemaintanenceserviceapp.databinding.ActivityTrackerBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Timer;
import java.util.TimerTask;

public class tracker extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String myUID;
    private Marker marker;
    LatLng myPos;
    private ActivityTrackerBinding binding;
    SupportMapFragment mapFragment;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myUID= FirebaseAuth.getInstance().getCurrentUser().getUid();



        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.workerTracker);
        getLocationPermission();
        binding.reached.setOnClickListener(view -> {
            Bundle bundle = getIntent().getExtras();
            String cID=bundle.getString("cID");
            update2();
            finish();
            Intent intent=new Intent(tracker.this, payment.class).putExtra("cID",cID);
            startActivity(intent);

        });
    }

    private void update2() {
        String cid= getIntent().getStringExtra("cID");
        FirebaseDatabase.getInstance().getReference().child("RealTimePositions")
                .child(cid).child("reached").setValue(true);
        timer.cancel();
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        getMyLocation();
        setMarker();
    }
    private void setMarker() {
        Bundle bundle = getIntent().getExtras();
        double lat=bundle.getDouble("lat");
        double lon=bundle.getDouble("lon");

        marker=mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Destination"));

marker.showInfoWindow();

    }
    private void getLocationPermission() {
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        mapFragment.getMapAsync(tracker.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        //Show SnackBar and prompt to settings
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }
    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                myPos = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPos, 12));
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                trackRider();
            }
        });
    }
    private void trackRider() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateLocation());
            }
        }, 5000, 6000);

    }
    private void updateLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation().addOnSuccessListener(this,location -> {
            if (location != null) {
                LocationUpdate locationUpdate = new LocationUpdate();
                locationUpdate.setReached(false);
                locationUpdate.setLat(location.getLatitude());
                locationUpdate.setLng(location.getLongitude());
                String cid= getIntent().getStringExtra("cID");
                FirebaseDatabase.getInstance().getReference().child("RealTimePositions")
                        .child(cid)
                        .setValue(locationUpdate);
            }
        });
    }

}