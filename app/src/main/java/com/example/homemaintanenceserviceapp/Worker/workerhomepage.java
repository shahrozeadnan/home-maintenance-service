package com.example.homemaintanenceserviceapp.Worker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.homemaintanenceserviceapp.MainPage.Aboutus;
import com.example.homemaintanenceserviceapp.Adapters.usershowAdapter;
import com.example.homemaintanenceserviceapp.MainPage.MainActivity;
import com.example.homemaintanenceserviceapp.Model.LocationTracking;
import com.example.homemaintanenceserviceapp.R;
import com.example.homemaintanenceserviceapp.databinding.ActivityWorkerhomepageBinding;
import com.example.homemaintanenceserviceapp.MainPage.help;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;


public class workerhomepage extends Activity {
    ActivityWorkerhomepageBinding binding;
    String name;
    usershowAdapter usershowAdapter;
    ArrayList<LocationTracking> list;
    String myUID;
    FirebaseAuth Auth;
    FirebaseUser User;
    DatabaseReference DataRef;

    DrawerLayout Drawer;
    NavigationView Navigation;
    ActionBarDrawerToggle drawerToggle;
    View headerView;
    ImageView photo;
    String email,userId;
    String WorkerName;
    String WorkerEmail;
    String Workerphoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkerhomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Auth=FirebaseAuth.getInstance();
        User=Auth.getCurrentUser();
        Drawer=findViewById(R.id.Drawer_layout_worker);
        Navigation=findViewById(R.id.nav_view_worker);
        headerView = Navigation.getHeaderView(0);

        photo = headerView.findViewById(R.id.nav_header_imageView);
        TextView userNameTextView = headerView.findViewById(R.id.UserName);
        TextView userEmailTextView = headerView.findViewById(R.id.UserEmail);
        if (User != null) {
            // Get the user ID
            email=User.getEmail();
            userId=User.getUid();
        }
        DataRef = FirebaseDatabase.getInstance().getReference().child("Workers").child(userId);
        DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Workerphoto=dataSnapshot.child("image").getValue(String.class);
                    WorkerEmail = dataSnapshot.child("email").getValue(String.class);
                    WorkerName = dataSnapshot.child("name").getValue(String.class);

                    Picasso.get()
                            .load(Workerphoto)
                            .into(photo);
                    userNameTextView.setText(WorkerName);
                    userEmailTextView.setText(WorkerEmail);
                    // Do something with the retrieved data
                } else {
                    // User with the specified userId does not exist
                    Toast.makeText(workerhomepage.this,"UserId does not exist",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        });


        drawerToggle=new ActionBarDrawerToggle(this,Drawer,R.string.open,R.string.close);
        Drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Navigation.setNavigationItemSelectedListener(item -> {
            switch(item.getItemId())
            {
                case R.id.home:
                    Intent intent2=new Intent(workerhomepage.this, workerhomepage.class);
                    startActivity(intent2);
                    finish();
                    break;
                case R.id.ViewProfile:
                    Intent intent=new Intent(workerhomepage.this, WorkerViewProfile.class);
                    startActivity(intent);
                    break;
                case R.id.EditProfile:
                    Intent intent1=new Intent(workerhomepage.this, WorkerEditProfile.class);
                    startActivity(intent1);
                    finish();
                    break;
                case R.id.Logout:
                    Auth.signOut();
                    sendUserToNextActivity();
                    break;
                case R.id.DeleteAccount:
                    User.delete().addOnCompleteListener(task -> {
                                DataRef.removeValue().addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(workerhomepage.this, "Account Successfully Deleted", Toast.LENGTH_SHORT).show();
                                                sendUserToNextActivity();
                                                finish();
                                            }
                                            else
                                            {
                                                Log.e("error", String.valueOf(task.getException()));
                                            }
                                        }
                                ).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Failed to delete user
                                        Toast.makeText(workerhomepage.this, "Failed to delete Worker", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                    );
                    break;
                case R.id.Aboutus:
                    Intent intent3=new Intent(workerhomepage.this, Aboutus.class);
                    startActivity(intent3);
                    break;
                case R.id.Help:
                    Intent intent4=new Intent(workerhomepage.this, help.class);
                    startActivity(intent4);
                    break;
            }
            return false;
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Workers")
                .child(myUID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    name = snapshot.child("name").getValue(String.class);
                    binding.name.setText(name);
                    ref.removeEventListener(this);
                }
                else {
                    Toast.makeText(workerhomepage.this,"error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });


        setAdapters();
        clickListeners();

    }

    private void setAdapters() {
        binding.rvAllRequests.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        usershowAdapter = new usershowAdapter(this, list,myUID);
        binding.rvAllRequests.setAdapter(usershowAdapter);

    }

    private void clickListeners() {

        binding.bGoOnline.setOnClickListener(view ->
        {
            checkLocationPermission();

        });
        binding.bGoOffline.setOnClickListener(view -> {
            setVisibility(2);
            deleteAvailability();
        });
    }

    private void deleteAvailability() {
        FirebaseDatabase.getInstance().getReference()
                .child("AvailableWorkers")
                .child(myUID)
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
                .child("Workers")
                .child(myUID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String workerName = snapshot.child("name").getValue(String.class);
                    String phoneNumber = snapshot.child("phonenum").getValue(String.class);
                    String type="";
                    if(Objects.equals(snapshot.child("plumbing").getValue(boolean.class), true))
                    {
                        type="plumbing";
                    }
                    else if(Objects.equals(snapshot.child("paint").getValue(boolean.class), true))
                    {
                        type="paint";
                    }
                    else if(Objects.equals(snapshot.child("electrical").getValue(boolean.class), true))
                    {
                        type="electrical";
                    }
                    else if(Objects.equals(snapshot.child("carpentry").getValue(boolean.class), true))
                    {
                        type="carpentry";
                    }
                    String image = snapshot.child("image").getValue(String.class);
                    getMyLocation(workerName, phoneNumber, image,type);
                    ref.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }

    private void getMyLocation(String workerName, String phoneNumber, String image, String Type) {
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
                        locationTracking.setWorkerName(workerName);
                        locationTracking.setPhoneNumber(phoneNumber);
                        locationTracking.setImage(image);
                        locationTracking.setType(Type);
                        locationTracking.setUID(myUID);
                        FirebaseDatabase.getInstance().getReference()
                                .child("AvailableWorkers")
                                .child(myUID)
                                .setValue(locationTracking);
                        Log.i("asdfadsfas", "ok2");
                    }
                });
    }

    private void setVisibility(int val) {
        if (val == 1) {
            binding.bGoOnline.setVisibility(View.GONE);
            binding.bGoOffline.setVisibility(View.VISIBLE);
            binding.progressCircular.setVisibility(View.VISIBLE);

        } else if (val == 2) {
            binding.bGoOnline.setVisibility(View.VISIBLE);
            binding.bGoOffline.setVisibility(View.GONE);
            binding.progressCircular.setVisibility(View.GONE);
            binding.rvAllRequests.setVisibility(View.GONE);
        }

    }
    private void findUsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Requests").child(myUID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        LocationTracking locationTracking = dataSnapshot.getValue(LocationTracking.class);
                        list.add(locationTracking);
                        binding.progressCircular.setVisibility(View.GONE);
                        binding.rvAllRequests.setVisibility(View.VISIBLE);
                        usershowAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        deleteAvailability();
    }
    private void sendUserToNextActivity()
    {
        Intent intent=new Intent(workerhomepage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}


/*public class workerhomepage extends AppCompatActivity {
    FirebaseAuth Auth;
    FirebaseUser User;
    DatabaseReference DataRef;

    DrawerLayout Drawer;
    NavigationView Navigation;
    ActionBarDrawerToggle drawerToggle;
    View headerView;
    ImageView photo;
    String email,userId;
    String WorkerName;
    String WorkerEmail;
    String Workerphoto;

    ActivityWorkerhomepageBinding binding;
    usershowAdapter usershowAdapter;
    ArrayList<LocationTracking> list;
    String myUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkerhomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Auth=FirebaseAuth.getInstance();
        User=Auth.getCurrentUser();
        Drawer=findViewById(R.id.Drawer_layout_worker);
        Navigation=findViewById(R.id.nav_view_worker);
        headerView = Navigation.getHeaderView(0);

        photo = headerView.findViewById(R.id.nav_header_imageView);
        TextView userNameTextView = headerView.findViewById(R.id.UserName);
        TextView userEmailTextView = headerView.findViewById(R.id.UserEmail);

        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Toast.makeText(this, myUID, Toast.LENGTH_SHORT).show();

        setAdapters();
        clickListeners();


        if (User != null) {
            // Get the user ID
            email=User.getEmail();
            userId=User.getUid();
        }
        DataRef = FirebaseDatabase.getInstance().getReference().child("Workers").child(userId);
        DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Workerphoto=dataSnapshot.child("image").getValue(String.class);
                    WorkerEmail = dataSnapshot.child("email").getValue(String.class);
                    WorkerName = dataSnapshot.child("name").getValue(String.class);

                    Picasso.get()
                            .load(Workerphoto)
                            .into(photo);
                    userNameTextView.setText(WorkerName);
                    userEmailTextView.setText(WorkerEmail);
                    // Do something with the retrieved data
                } else {
                    // User with the specified userId does not exist
                    Toast.makeText(workerhomepage.this,"UserId does not exist",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        });


        drawerToggle=new ActionBarDrawerToggle(this,Drawer,R.string.open,R.string.close);
        Drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Navigation.setNavigationItemSelectedListener(item -> {
            switch(item.getItemId())
            {
                case R.id.home:
                    Intent intent2=new Intent(workerhomepage.this, workerhomepage.class);
                    startActivity(intent2);
                    finish();
                    break;
                case R.id.ViewProfile:
                    Intent intent=new Intent(workerhomepage.this, WorkerViewProfile.class);
                    startActivity(intent);
                    break;
                case R.id.EditProfile:
                    Intent intent1=new Intent(workerhomepage.this, WorkerEditProfile.class);
                    startActivity(intent1);
                    finish();
                    break;
                case R.id.Logout:
                    Auth.signOut();
                    sendUserToNextActivity();
                    break;
                case R.id.DeleteAccount:
                    User.delete().addOnCompleteListener(task -> {
                        DataRef.removeValue().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(workerhomepage.this, "Account Successfully Deleted", Toast.LENGTH_SHORT).show();
                                    sendUserToNextActivity();
                                    finish();
                                }
                                else
                                {
                                    Log.e("error", String.valueOf(task.getException()));
                                }
                            }
                        ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to delete user
                                Toast.makeText(workerhomepage.this, "Failed to delete Worker", Toast.LENGTH_SHORT).show();
                            }
                        });
                            }
                    );
                    break;
                case R.id.Aboutus:
                    Intent intent3=new Intent(workerhomepage.this, Aboutus.class);
                    startActivity(intent3);
                    break;
                case R.id.Help:
                    Intent intent4=new Intent(workerhomepage.this, help.class);
                    startActivity(intent4);
                    break;
            }
            return false;
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d("TAG", "Fetching FCM registration token failed", task.getException());
                        return;
                    } String token = task.getResult();
                });

    }
    private void setAdapters() {
        binding.rvAllRequests.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        usershowAdapter = new usershowAdapter(this, list,myUID);
        binding.rvAllRequests.setAdapter(usershowAdapter);

    }

    private void clickListeners() {

        binding.bGoOnline.setOnClickListener(view ->
        {
            checkLocationPermission();

        } );
        binding.bGoOffline.setOnClickListener(view -> {
            setVisibility(2);
            deleteAvailability();
        });
    }

    private void deleteAvailability() {
        FirebaseDatabase.getInstance().getReference()
                .child("AvailableWorkers")
                .child(myUID)
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
                .child("Workers")
                .child(myUID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String workerName = snapshot.child("name").getValue(String.class);
                    String phoneNumber = snapshot.child("phonenum").getValue(String.class);
                    String type="";
                    if(Objects.equals(snapshot.child("plumbing").getValue(boolean.class), true))
                    {
                        type="plumbing";
                    }
                    else if(Objects.equals(snapshot.child("paint").getValue(boolean.class), true))
                    {
                        type="paint";
                    }
                    else if(Objects.equals(snapshot.child("electrical").getValue(boolean.class), true))
                    {
                        type="electrical";
                    }
                    else if(Objects.equals(snapshot.child("carpentry").getValue(boolean.class), true))
                    {
                        type="carpentry";
                    }
                    String image=snapshot.child("image").getValue(String.class);
                    getMyLocation(workerName, phoneNumber,image,type);
                    ref.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }
    private void getMyLocation(String workerName, String phoneNumber,String image, String Type) {
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
                        locationTracking.setWorkerName(workerName);
                        locationTracking.setPhoneNumber(phoneNumber);
                        locationTracking.setType(Type);
                        locationTracking.setImage(image);
                        locationTracking.setUID(myUID);
                        FirebaseDatabase.getInstance().getReference()
                                .child("AvailableWorkers")
                                .child(myUID)
                                .setValue(locationTracking);
                        Log.i("asdfadsfas", "ok2");
                       *//* try {
                            Thread.sleep(2000);
                            locationTracking.setWorkerName("sdfsdf");
                            locationTracking.setPhoneNumber("9799869");
                            locationTracking.setUID(myUID);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("AvailableWorkers")
                                    .child(myUID + "1")
                                    .setValue(locationTracking);
                            Log.i("asdfadsfas", "ok3");

                            Thread.sleep(2000);
                            locationTracking.setWorkerName("ncvmbnkjdfhkgjf");
                            locationTracking.setPhoneNumber("57647657");
                            locationTracking.setUID("adajiljqdjd99");
                            FirebaseDatabase.getInstance().getReference()
                                    .child("AvailableWorkers")
                                    .child(myUID + "9")
                                    .setValue(locationTracking);
                            Thread.sleep(2000);
                            locationTracking.setWorkerName("trurthrth");
                            locationTracking.setPhoneNumber("12341253");
                            locationTracking.setUID("adajiljqdjd90");
                            FirebaseDatabase.getInstance().getReference()
                                    .child("AvailableWorkers")
                                    .child(myUID + "8")
                                    .setValue(locationTracking);


                        } catch (InterruptedException e) {
                            Log.i("asdfadsfas", "okex" + e);

                        }*//*
                    }
                });
    }

    private void setVisibility(int val) {
        if (val == 1) {
            binding.bGoOnline.setVisibility(View.GONE);
            binding.bGoOffline.setVisibility(View.VISIBLE);
            binding.progressCircular.setVisibility(View.VISIBLE);

        }
        else if(val==2) {
            binding.bGoOnline.setVisibility(View.VISIBLE);
            binding.bGoOffline.setVisibility(View.GONE);
            binding.progressCircular.setVisibility(View.GONE);
            binding.rvAllRequests.setVisibility(View.GONE);
        }

    }
    private void findUsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Requests");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    LocationTracking locationTracking = dataSnapshot.getValue(LocationTracking.class);
                    list.add(locationTracking);
                    if(list!=null)
                    {
                        binding.progressCircular.setVisibility(View.GONE);

                    }
                    usershowAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendUserToNextActivity()
    {
        Intent intent=new Intent(workerhomepage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}*/
