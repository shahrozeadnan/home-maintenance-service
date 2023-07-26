package com.example.homemaintanenceserviceapp.Customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class customerviewprofile extends Activity {
    FirebaseUser User;
    DatabaseReference DataRef;
    String email;
    TextView CName,CCity,CPhone;
    TextView CEmail,CCnic;
    String userId;
    String CustName;
    String CustEmail;
    String CustCnic;
    String Custcity;
    String CustPhonenum;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_profile);
        toolbar=findViewById(R.id.toolbarviewcustomer);
        CName=findViewById(R.id.PersonName);
        CCity=findViewById(R.id.TextCity);
        CPhone=findViewById(R.id.TextPhone);
        CCnic=findViewById(R.id.TextCNIC);
        CEmail=findViewById(R.id.TextEmail);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent=new Intent(customerviewprofile.this, homepage_user.class);
            startActivity(intent);
            finish();
        });
        User = FirebaseAuth.getInstance().getCurrentUser();
        if (User != null) {
            // Get the user ID
           email=User.getEmail();
           userId=User.getUid();
        }
        DataRef = FirebaseDatabase.getInstance().getReference().child("Customers").child(userId);
        DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Custcity = dataSnapshot.child("city").getValue(String.class);
                    CustCnic = dataSnapshot.child("cnic").getValue(String.class);
                    CustEmail = dataSnapshot.child("email").getValue(String.class);
                    CustName = dataSnapshot.child("name").getValue(String.class);
                    CustPhonenum = dataSnapshot.child("phonenum").getValue(String.class);
                    CName.setText(CustName);
                    CPhone.setText(CustPhonenum);
                    CCnic.setText(CustCnic);
                    CCity.setText(Custcity);
                    CEmail.setText(CustEmail);
                    // Do something with the retrieved data
                } else {
                    // User with the specified userId does not exist
                    Toast.makeText(customerviewprofile.this,"UserId does not exist",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }
}