package com.example.homemaintanenceserviceapp.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.homemaintanenceserviceapp.Customer.CustomerPayment;
import com.example.homemaintanenceserviceapp.Model.LocationUpdate;
import com.example.homemaintanenceserviceapp.Notifications.HMSNotification;
import com.example.homemaintanenceserviceapp.R;
import com.example.homemaintanenceserviceapp.databinding.ActivityCustomerTrackerBinding;
import com.example.homemaintanenceserviceapp.databinding.ActivityUserShowwBinding;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class CustomerTracker extends AppCompatActivity implements OnMapReadyCallback {
    ActivityCustomerTrackerBinding binding;
    LatLng myPos;

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Marker workerMarker; // Add this line

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setMap();
        HMSNotification.createNotificationChannel(this);
    }

    private void setMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.custTracker);
        checkLocation();
    }

    private void checkLocation() {
        Dexter.withContext(getApplicationContext())
                .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        mapFragment.getMapAsync(CustomerTracker.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void getLocationUserCoordinates() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {

                        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
                    }
                });
    }

    private void getLocationUpdate() {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("RealTimePositions")
                .child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    LocationUpdate locationUpdate = snapshot.getValue(LocationUpdate.class);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationUpdate.getLat(), locationUpdate.getLng()), 12));

                    if (workerMarker != null) {
                        workerMarker.remove();
                    }

                    // Add custom marker icon
                    BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.baseline_directions_bike_24));

                    workerMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(locationUpdate.getLat(), locationUpdate.getLng()))
                            .title("Worker Location")
                            .icon(markerIcon));
                    workerMarker.showInfoWindow();

                    mMap.getUiSettings().setMyLocationButtonEnabled(false);

                    if (locationUpdate.isReached()) {
                        HMSNotification.showNotification(CustomerTracker.this, "Home Maintenance App", "Worker has arrived.");
                        ref.removeEventListener(this);
                        ref.removeValue();
                        finish();
                        String dID = getIntent().getStringExtra("dID");
                        startActivity(new Intent(CustomerTracker.this, CustomerPaymentWaiting.class).putExtra("dID", dID));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(this, "Map Ready", Toast.LENGTH_SHORT).show();
        getLocationUserCoordinates();
        getLocationUpdate();
    }

    // Method to convert vector drawable to Bitmap
    private Bitmap getBitmapFromVectorDrawable(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}