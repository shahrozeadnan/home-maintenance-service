package com.example.homemaintanenceserviceapp.Customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.homemaintanenceserviceapp.MainPage.MainActivity;
import com.example.homemaintanenceserviceapp.Model.Customer;
import com.example.homemaintanenceserviceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup_user extends Activity {
    EditText email,password,confirm_pass,name, cnic, PhoneNumber;
    Button signup;
    String emailpattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";  //pattern used for email
    String cnicPattern = "^[0-9+]{5}-[0-9+]{7}-[0-9]{1}$";
    String Name, city, Cnic, phonenum,Email,Password, Confirmpass;
    ProgressDialog progressDialog;   //to show progress
    String userid;
    FirebaseAuth Auth;
    Toolbar toolbar;
    String selectedcity;
    Spinner City;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_user);

        name = findViewById(R.id.Name);
        City = findViewById(R.id.spinnerCity);
        cnic = findViewById(R.id.CNIC);
        PhoneNumber = findViewById(R.id.Phonenum);
        email= findViewById(R.id.EmailAddress);
        password= findViewById(R.id.Password);
        confirm_pass= findViewById(R.id.RetypePass);
        toolbar=findViewById(R.id.toolbaruser);
        signup= findViewById(R.id.signup);
        progressDialog=new ProgressDialog(this);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent=new Intent(Signup_user.this, MainActivity.class);
            startActivity(intent);

        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Cities, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        City.setAdapter(adapter);

        City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedcity=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Auth=FirebaseAuth.getInstance();
        signup.setOnClickListener(view -> PerformAuth());
    }
    private void PerformAuth()
    {
        Name = name.getText().toString();
        city =selectedcity;
        Cnic = cnic.getText().toString();
        phonenum = PhoneNumber.getText().toString();
        Email=email.getText().toString();
        Password=password.getText().toString();
        Confirmpass=confirm_pass.getText().toString();
        if(!Email.matches(emailpattern))
        {
            email.setError("Enter Correct Email");
        }
        else if(Password.isEmpty() || Password.length()<6)
        {
            password.setError("Enter Correct Password");
        }
        else if(!Password.equals(Confirmpass))
        {
            confirm_pass.setError("Password does not match the re-entered password");
        }
        else {
            if (Cnic.matches(cnicPattern)) {
                progressDialog.setMessage("Please wait while account is being created");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                Auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
                    userid = Auth.getCurrentUser().getUid();
                    SendVerificationEmail(Auth);
                    if (task.isSuccessful()) {

                        Customer customer = new Customer(Name, city, Cnic, phonenum, Email);

                        FirebaseDatabase.getInstance().getReference().child("Customers").child(userid).setValue(customer)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Signup_user.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                        // Database node created successfully
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(Signup_user.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                        // Database node creation failed
                                    }
                                });

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Signup_user.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                cnic.setError("Enter Correct CNIC");
            }
        }
        //Customer customer = new Customer(Name, city, Cnic, phonenum,Email);
        //FirebaseDatabase.getInstance().getReference().child("Customers").child(userid).setValue(customer);
        //Toast.makeText(Signup_user.this, "Account Created", Toast.LENGTH_SHORT).show();
        sendUserToNextActivity();
    }
    private void sendUserToNextActivity()
    {
        Intent intent=new Intent(Signup_user.this, MainActivity.class);
        startActivity(intent);
    }

    private void SendVerificationEmail(FirebaseAuth mAuth)
    {
//Send verification email
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Signup_user.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Signup_user.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
