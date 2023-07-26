package com.example.homemaintanenceserviceapp.Customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.homemaintanenceserviceapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CustomerEditProfile extends Activity {
    FirebaseUser User;
    DatabaseReference DataRef;
    String email;
    EditText CName,CCity,CPhone;
    EditText CEmail,CCnic;
    String userId;
    String CustName;
    String CustEmail;
    String CustCnic;
    String Custcity;
    String CustPhonenum;
    AlertDialog.Builder builder;
    Button UpdateProfile;
   Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_edit_profile);

        builder = new AlertDialog.Builder(CustomerEditProfile.this);

        CName=findViewById(R.id.PersonName);
        CCity=findViewById(R.id.TextCity);
        CPhone=findViewById(R.id.TextPhone);
        UpdateProfile=findViewById(R.id.Updatebutton);
        toolbar=findViewById(R.id.EditProfileToolbar);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent=new Intent(CustomerEditProfile.this, homepage_user.class);
            startActivity(intent);
            finish();
        });
        User = FirebaseAuth.getInstance().getCurrentUser();
        if (User != null) {
            // Get the user ID
            userId=User.getUid();
        }
        DataRef = FirebaseDatabase.getInstance().getReference().child("Customers").child(userId);
        DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Custcity = dataSnapshot.child("city").getValue(String.class);
                    CustName = dataSnapshot.child("name").getValue(String.class);
                    CustPhonenum = dataSnapshot.child("phonenum").getValue(String.class);
                    CName.setText(CustName);
                    CPhone.setText(CustPhonenum);
                    CCity.setText(Custcity);
                    // Do something with the retrieved data
                } else {
                    // User with the specified userId does not exist
                    Toast.makeText(CustomerEditProfile.this,"UserId does not exist",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }


        });
        UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = CName.getText().toString();
                String city = CCity.getText().toString();
                String phone = CPhone.getText().toString();

                HashMap<String, Object> updateData = new HashMap<>();
                updateData.put("name", name);
                updateData.put("city", city);
                updateData.put("phonenum", phone);

                DataRef.updateChildren(updateData)
                        .addOnSuccessListener(aVoid -> {


                            // Show an AlertDialog with the message "Profile updated successfully"
                            builder.setMessage("Profile updated successfully")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent1=new Intent(CustomerEditProfile.this, homepage_user.class);
                                            startActivity(intent1);
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "Error updating profile: " + e.getMessage());
                                Toast.makeText(CustomerEditProfile.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}