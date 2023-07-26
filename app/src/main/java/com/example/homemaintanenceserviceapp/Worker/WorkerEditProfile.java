package com.example.homemaintanenceserviceapp.Worker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.homemaintanenceserviceapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class WorkerEditProfile extends Activity {

    static int REQUEST_CODE_IMAGE = 1;

    FirebaseUser User;
    DatabaseReference DataRef;
    StorageReference StorageRef;
Toolbar toolbar;
    String email;
    EditText WName,WCity,WPhone,WExperience;
    CheckBox plumbing;
    CheckBox carpentry;
    CheckBox paint;
    CheckBox electrical;
    ImageView profileImage;

    Uri imageuri;
    AlertDialog.Builder builder;
    String userId;

    String WorkerName;
    String Workercity;
    String WorkerPhonenum;
    String WorkerExperience;
    String workerPhoto;
    String uriphoto;
    boolean plumb, electric, painting, carpent;

    Button UpdateProfile;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            Picasso.get().load(imageuri).into(profileImage);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_edit_profile);

        builder = new AlertDialog.Builder(WorkerEditProfile.this);

        WName=findViewById(R.id.WorkName);
        WCity=findViewById(R.id.WorkerTextCity);
        WPhone=findViewById(R.id.WorkerTextPhone);
        WExperience=findViewById(R.id.WorkerPersonExperience);
        profileImage=findViewById(R.id.WorkerProfileImage);

        plumbing=findViewById(R.id.Plumbing);
        carpentry=findViewById(R.id.Carpentry);
        paint=findViewById(R.id.Paint);
        electrical=findViewById(R.id.Electrical);
        UpdateProfile=findViewById(R.id.WorkerUpdatebutton);
        toolbar=findViewById(R.id.toolbareditworker);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent=new Intent(WorkerEditProfile.this, workerhomepage.class);
            startActivity(intent);
            finish();

        });
        User = FirebaseAuth.getInstance().getCurrentUser();
        if (User != null) {
            // Get the user ID
            userId=User.getUid();
        }
        DataRef = FirebaseDatabase.getInstance().getReference().child("Workers").child(userId);
        StorageRef = FirebaseStorage.getInstance().getReference().child("WorkerImages");

        DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    workerPhoto=dataSnapshot.child("image").getValue(String.class);
                    Workercity = dataSnapshot.child("city").getValue(String.class);
                    WorkerName = dataSnapshot.child("name").getValue(String.class);
                    WorkerPhonenum = dataSnapshot.child("phonenum").getValue(String.class);
                    WorkerExperience=dataSnapshot.child("experience").getValue(String.class);
                    plumb= Boolean.TRUE.equals(dataSnapshot.child("plumbing").getValue(boolean.class));
                    electric=Boolean.TRUE.equals(dataSnapshot.child("electrical").getValue(boolean.class));
                    painting=Boolean.TRUE.equals(dataSnapshot.child("paint").getValue(boolean.class));
                    carpent=Boolean.TRUE.equals(dataSnapshot.child("carpentry").getValue(boolean.class));

                    WName.setText(WorkerName);
                    WPhone.setText(WorkerPhonenum);
                    WCity.setText(Workercity);
                    WExperience.setText(WorkerExperience);
                    Picasso.get().load(workerPhoto).into(profileImage);
                    plumbing.setChecked(plumb);
                    carpentry.setChecked(carpent);
                    paint.setChecked(painting);
                    electrical.setChecked(electric);
                    // Do something with the retrieved data
                }
                else
                {
                    // User with the specified userId does not exist
                    Toast.makeText(WorkerEditProfile.this,"UserId does not exist",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }


        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }


        });
        UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = WName.getText().toString();
                String city = WCity.getText().toString();
                String phone = WPhone.getText().toString();
                String Experience=WExperience.getText().toString();
                boolean Carpentry=carpentry.isChecked();
                boolean Plumbing=plumbing.isChecked();
                boolean Electrical=electrical.isChecked();
                boolean Paint=paint.isChecked();

                HashMap<String, Object> updateData = new HashMap<>();

                if (imageuri != null) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("WorkerImages");
                    final StorageReference ImageReference = storageReference.child(userId);
                    ImageReference.putFile(imageuri)
                            .addOnSuccessListener(taskSnapshot -> {
                                        ImageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                                    uriphoto = uri.toString();
                                                    updateData.put("image",uriphoto);
                                            DataRef.updateChildren(updateData)
                                                    .addOnSuccessListener(aVoid -> {
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.d("TAG", "Error updating profile: " + e.getMessage());
                                                        Toast.makeText(WorkerEditProfile.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                                                    });
                                                }
                                        );
                                    }
                            );
                }

                updateData.put("name", name);
                updateData.put("city", city);
                updateData.put("phonenum", phone);
                updateData.put("experience",Experience);
                updateData.put("carpentry",Carpentry);
                updateData.put("plumbing",Plumbing);
                updateData.put("electrical",Electrical);
                updateData.put("paint",Paint);
                DataRef.updateChildren(updateData)
                        .addOnSuccessListener(aVoid -> {


                            // Show an AlertDialog with the message "Profile updated successfully"
                            builder.setMessage("Profile updated successfully")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent1=new Intent(WorkerEditProfile.this, workerhomepage.class);
                                            startActivity(intent1);
                                            finish();
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        })
                        .addOnFailureListener(e -> {
                            Log.d("TAG", "Error updating profile: " + e.getMessage());
                            Toast.makeText(WorkerEditProfile.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                        });
            }
});

    }

}