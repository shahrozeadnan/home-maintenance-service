package com.example.homemaintanenceserviceapp.MainPage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homemaintanenceserviceapp.Admin.AdminHomepage;
import com.example.homemaintanenceserviceapp.Customer.Signup_user;
import com.example.homemaintanenceserviceapp.Customer.homepage_user;
import com.example.homemaintanenceserviceapp.Worker.PhoneNumVerification;
import com.example.homemaintanenceserviceapp.R;
import com.example.homemaintanenceserviceapp.Worker.workerhomepage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView customer, worker, forgotpassword;   //used to go to sign up page
    EditText email, password;
    Button login;
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";  //pattern used for email
    //String adminemail="support@homemaintenance.com";
    ProgressDialog progressDialog;   //to show progress
    RadioButton WorkerSelected, CustomerSelected,AdminSelected;
    FirebaseAuth Auth;
    FirebaseUser User;
    DatabaseReference DataRef;

    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        forgotpassword=findViewById(R.id.forgotpass);
        login = findViewById(R.id.LOGIN);
        progressDialog = new ProgressDialog(this);
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        customer = findViewById(R.id.customer_signup);
        worker = findViewById(R.id.worker_signup);

        WorkerSelected = findViewById(R.id.Worker);
        CustomerSelected = findViewById(R.id.Customer);
        //AdminSelected=findViewById(R.id.Admin);

        login.setOnClickListener(v -> performLogin());


        worker.setOnClickListener(view -> {
            Intent intent=new Intent(MainActivity.this, PhoneNumVerification.class);
            //Intent intent = new Intent(MainActivity.this, workerSignUp.class);
            startActivity(intent);
            finish();

        });

        customer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Signup_user.class);
            startActivity(intent);

        });

        forgotpassword.setOnClickListener(v->{
            Intent intent=new Intent(MainActivity.this, ForgotPassword.class);
            startActivity(intent);
        });

    }

    private void performLogin() {
        String Email,Password;
        Email = email.getText().toString();
        Password = password.getText().toString();

        if (!Email.matches(emailpattern)) {
            email.setError("Enter Correct Email");
        } else if (Password.isEmpty() || Password.length() < 6) {
            password.setError("Enter Correct password");
        } else {
            progressDialog.setMessage("Please wait while Logging in to Account.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            Auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
                String userid=Auth.getUid();
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    if (CustomerSelected.isChecked())
                    {
                        checkCustomer(userid);

                    }
                    else if (WorkerSelected.isChecked())
                    {
                        checkWorker(userid);
                    }
                 /*   else if(AdminSelected.isChecked())
                    {
                        Intent intent=new Intent(MainActivity.this, AdminHomepage.class);
                        startActivity(intent);
                    }*/

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void sendUserToCustomerHomepage() {
        Intent intent = new Intent(MainActivity.this, homepage_user.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void sendUserToWorkerHomepage() {
        Intent intent = new Intent(MainActivity.this, workerhomepage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void checkWorker(String userid)
    {

        DataRef = FirebaseDatabase.getInstance().getReference().child("Workers");
        query = DataRef.orderByKey().equalTo(userid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User exists, do something with the data
                    Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    sendUserToWorkerHomepage();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors that occur while searching the database
                Toast.makeText(MainActivity.this, "Error searching database: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void checkCustomer(String userid)
    {

        DataRef = FirebaseDatabase.getInstance().getReference().child("Customers");
        query = DataRef.orderByKey().equalTo(userid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User exists, do something with the data
                    Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    sendUserToCustomerHomepage();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors that occur while searching the database
                Toast.makeText(MainActivity.this, "Error searching database: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}