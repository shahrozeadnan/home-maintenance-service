package com.example.homemaintanenceserviceapp.Customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.homemaintanenceserviceapp.MainPage.Aboutus;
import com.example.homemaintanenceserviceapp.MainPage.MainActivity;
import com.example.homemaintanenceserviceapp.R;
import com.example.homemaintanenceserviceapp.MainPage.help;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class homepage_user extends Activity {
    Button Plumbing,Carpentry,Paint,Electrical;

    FirebaseAuth Auth;
    FirebaseUser User;
    DatabaseReference DataRef;

    DrawerLayout Drawer;
    NavigationView Navigation;
    ActionBarDrawerToggle drawerToggle;
    View headerView;

    String email,userId;
    String CustName;
    String CustEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_user);

        Auth=FirebaseAuth.getInstance();
        User=Auth.getCurrentUser();

        Plumbing=findViewById(R.id.plumbing);
        Carpentry=findViewById(R.id.carpentry);
        Paint=findViewById(R.id.paint);
        Electrical=findViewById(R.id.electrical);

        Drawer=findViewById(R.id.Drawer_layout);
        Navigation=findViewById(R.id.nav_view);
        headerView = Navigation.getHeaderView(0);

        TextView userNameTextView = headerView.findViewById(R.id.UserName);
        TextView userEmailTextView = headerView.findViewById(R.id.UserEmail);


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
                    CustEmail = dataSnapshot.child("email").getValue(String.class);
                    CustName = dataSnapshot.child("name").getValue(String.class);

                    userNameTextView.setText(CustName);
                    userEmailTextView.setText(CustEmail);
                    // Do something with the retrieved data
                } else {
                    // User with the specified userId does not exist
                    Toast.makeText(homepage_user.this,"UserId does not exist",Toast.LENGTH_LONG).show();
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
                    Intent intent2=new Intent(homepage_user.this, homepage_user.class);
                    startActivity(intent2);
                    finish();
                    break;
                case R.id.ViewProfile:
                    Intent intent=new Intent(homepage_user.this, customerviewprofile.class);
                    startActivity(intent);
                    break;
                case R.id.EditProfile:
                    Intent intent1=new Intent(homepage_user.this, CustomerEditProfile.class);
                    startActivity(intent1);
                    break;
                case R.id.Logout:
                    Auth.signOut();
                    sendUserToNextActivity();
                    break;
                case R.id.DeleteAccount:
                    User.delete().addOnCompleteListener(task -> {
                                DataRef.removeValue().addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(homepage_user.this, "Account Successfully Deleted", Toast.LENGTH_SHORT).show();
                                                sendUserToNextActivity();
                                                finish();
                                            } else {
                                                Log.e("error", String.valueOf(task.getException()));
                                            }
                                        }
                                ).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Failed to delete user
                                        Toast.makeText(homepage_user.this, "Failed to delete Customer", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                    );
                    break;
                case R.id.Aboutus:
                    Intent intent3=new Intent(homepage_user.this, Aboutus.class);
                    startActivity(intent3);
                    break;
                case R.id.Help:
                    Intent intent4=new Intent(homepage_user.this, help.class);
                    startActivity(intent4);
                    break;
            }
            return false;
        });


        Plumbing.setOnClickListener(view -> {
            Intent intent=new Intent(homepage_user.this, userShoww.class);
            intent.putExtra("Type","plumbing");
            startActivity(intent);
            finish();
        });

        Carpentry.setOnClickListener(view -> {
            Intent intent=new Intent(homepage_user.this,userShoww.class);
            intent.putExtra("Type","carpentry");
            startActivity(intent);
            finish();
        });
        Electrical.setOnClickListener(view -> {
            Intent intent=new Intent(homepage_user.this,userShoww.class);
            intent.putExtra("Type","electrical");
            startActivity(intent);
            finish();
        });
        Paint.setOnClickListener(view -> {
            Intent intent=new Intent(homepage_user.this,userShoww.class);
            intent.putExtra("Type","paint");
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        if(Drawer.isDrawerOpen(GravityCompat.START))
        {
            Drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void sendUserToNextActivity()
    {
        Intent intent=new Intent(homepage_user.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
