package com.example.homemaintanenceserviceapp.Worker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;

import com.example.homemaintanenceserviceapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class WorkerViewProfile extends Activity {

    FirebaseUser User;
    DatabaseReference DataRef;
    String email;
    TextView WName,WCity,WPhone;
    TextView WEmail,WCnic;
    ImageView WImage;
    String userId;
    String WorkerName;
    String WorkerEmail;
    String WorkerCnic;
    String Workercity;
    String Workerphoto;
    String WorkerPhonenum;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_view_profile);
        WName=findViewById(R.id.NameText);
        WCity=findViewById(R.id.TextCity);
        WPhone=findViewById(R.id.TextPhone);
        WCnic=findViewById(R.id.TextCNIC);
        WEmail=findViewById(R.id.TextEmail);
        WImage=findViewById(R.id.ProfileImage);
        toolbar=findViewById(R.id.toolbarviewworker);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent=new Intent(WorkerViewProfile.this, workerhomepage.class);
            startActivity(intent);
            finish();

        });
        User = FirebaseAuth.getInstance().getCurrentUser();
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
                    Workercity = dataSnapshot.child("city").getValue(String.class);
                    WorkerCnic = dataSnapshot.child("cnic").getValue(String.class);
                    WorkerEmail = dataSnapshot.child("email").getValue(String.class);
                    WorkerName = dataSnapshot.child("name").getValue(String.class);
                    WorkerPhonenum = dataSnapshot.child("phonenum").getValue(String.class);
                    WName.setText(WorkerName);
                    WPhone.setText(WorkerPhonenum);
                    WCnic.setText(WorkerCnic);
                    WCity.setText(Workercity);
                    WEmail.setText(WorkerEmail);
                    Picasso.get().load(Workerphoto).into(WImage);
                    // Do something with the retrieved data
                } else {
                    // User with the specified userId does not exist
                    Toast.makeText(WorkerViewProfile.this,"UserId does not exist",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        });
//        Toolbar toolbar = findViewById(R.id.toolbar3);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(WorkerViewProfile.this,workerhomepage.class);
//                startActivity(intent);
//            }
//        });





    }

}