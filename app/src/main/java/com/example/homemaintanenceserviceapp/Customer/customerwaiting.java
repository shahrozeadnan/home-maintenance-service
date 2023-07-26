package com.example.homemaintanenceserviceapp.Customer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.homemaintanenceserviceapp.Model.LocationTracking;
import com.example.homemaintanenceserviceapp.R;
import com.example.homemaintanenceserviceapp.databinding.ActivityCustomerwaitingBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

public class customerwaiting extends AppCompatActivity {

    ActivityCustomerwaitingBinding binding;

    String myUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomerwaitingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar5.setNavigationIcon(R.drawable.baseline_arrow_back_24);

        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding.toolbar5.setNavigationOnClickListener(view -> {
            deleteAvailability();
            Intent intent = new Intent(customerwaiting.this, userShoww.class);
            startActivity(intent);
        });

        checkLocationPermission();

    }

    private void deleteAvailability() {
        FirebaseDatabase.getInstance().getReference()
                .child("Requests")
                .child(getIntent().getStringExtra("dID"))
                .removeValue();
    }

    private void checkLocationPermission() {
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        setVisibility(1);
                        postAvailability();
                        findUsers();
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

    private void postAvailability() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Customers")
                .child(myUID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String workerName = snapshot.child("name").getValue(String.class);
                    String phoneNumber = snapshot.child("phonenum").getValue(String.class);
                    getMyLocation(workerName, phoneNumber);
                    ref.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }

    private void findUsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("AcceptedRequest").child(myUID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ref.removeValue();
                    ref.removeEventListener(this);
                    String dID = getIntent().getStringExtra("dID");
                    startActivity(new Intent(customerwaiting.this, CustomerTracker.class)
                            .putExtra("dID", dID));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMyLocation(String customerName, String phoneNumber) {
        Log.i("asdfadsfas", "ok1top" + myUID);
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.i("asdfadsfas", "ok1" + myUID);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    Log.i("asdfadsfas", "ok" + location);
                    if (location != null) {
                        LocationTracking locationTracking = new LocationTracking();
                        locationTracking.setLongitude(location.getLongitude());
                        locationTracking.setLatitude(location.getLatitude());
                        locationTracking.setWorkerName(customerName);
                        locationTracking.setPhoneNumber(phoneNumber);
                        locationTracking.setUID(myUID);
                        String dID = getIntent().getStringExtra("dID");
                        FirebaseDatabase.getInstance().getReference()
                                .child("Requests")
                                .child(dID)
                                .child(myUID)
                                .setValue(locationTracking);
                        Log.i("asdfadsfas", "ok2");
                    }
                });
    }

    private void setVisibility(int val) {
        if (val == 1) {
            binding.progressCircular.setVisibility(View.VISIBLE);
            return;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        deleteAvailability();
    }
}