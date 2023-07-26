package com.example.homemaintanenceserviceapp.Worker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.homemaintanenceserviceapp.Customer.Signup_user;
import com.example.homemaintanenceserviceapp.MainPage.MainActivity;
import com.example.homemaintanenceserviceapp.Model.Worker;
import com.example.homemaintanenceserviceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class workerSignUp extends Activity {
  private static final int REQUEST_CODE_IMAGE = 1;
    ImageView AddImage;
    Uri imageuri;
    String url;
    String selectedcity;
    Toolbar toolbar;
    EditText name, Address, Experience, cnic, PhoneNumber;
    Spinner City;
    CheckBox plumbing;
    CheckBox carpentry;
    CheckBox paint;
    CheckBox electrical;
    String number;
    DatabaseReference dataref;
    String Name, address, experience, Cnic, phone;
    String city;
    EditText email, password, confirm_pass;
    Button Signup;
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";  //pattern used for email
    String cnicPattern = "^[0-9+]{5}-[0-9+]{7}-[0-9]{1}$";
    ProgressDialog progressDialog;   //to show progress
    FirebaseAuth Auth;
    String userid;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            Picasso.get().load(imageuri).into(AddImage);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_sign_up);

        name = findViewById(R.id.name);
        Address = findViewById(R.id.Address);
        City = findViewById(R.id.spinnercity);
        Experience = findViewById(R.id.Experience);
        cnic = findViewById(R.id.cnic);
        PhoneNumber = findViewById(R.id.PhoneNumber);
        email = findViewById(R.id.emailaddress);
        password = findViewById(R.id.pass);
        confirm_pass = findViewById(R.id.retypepass);
        plumbing = findViewById(R.id.Plumbing);
        carpentry = findViewById(R.id.Carpentry);
        electrical = findViewById(R.id.Electrical);
        paint = findViewById(R.id.Paint);
        AddImage=findViewById(R.id.Addphoto);
        toolbar=findViewById(R.id.toolbarworker);
        Signup = findViewById(R.id.Signup);

        Intent intentphone = getIntent();
        number=intentphone.getStringExtra("PhoneNum");
        PhoneNumber.setText(number);

        progressDialog = new ProgressDialog(this);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent=new Intent(workerSignUp.this, MainActivity.class);
            startActivity(intent);
            finish();
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
        Auth = FirebaseAuth.getInstance();

        dataref=FirebaseDatabase.getInstance().getReference().child("Workers");


        AddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }


        });


        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    insertworker();
                }
        });
    }

    private void insertworker() {
        boolean plumb, electic, painting, carpent;
        Name = name.getText().toString();
        address = Address.getText().toString();
        city = selectedcity;
        Cnic = cnic.getText().toString();
        experience = Experience.getText().toString();
        phone=PhoneNumber.getText().toString();
        plumb = plumbing.isChecked();
        carpent = carpentry.isChecked();
        painting = paint.isChecked();
        electic = electrical.isChecked();
        String Email=email.getText().toString();
        String Password=password.getText().toString();
        String Confirmpass=confirm_pass.getText().toString();
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
        else
        {
            if(Cnic.matches(cnicPattern))
            {
                progressDialog.setMessage("Please wait while account is being created");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                Auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(task -> {
                    userid = Auth.getCurrentUser().getUid();
                    SendVerificationEmail(Auth);
                    if(task.isSuccessful())
                    {
                        try {
                            if (imageuri != null) {
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("WorkerImages");
                                final StorageReference ImageReference = storageReference.child(userid);
                                ImageReference.putFile(imageuri)
                                        .addOnSuccessListener(taskSnapshot -> {
                                            ImageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                                url = uri.toString();
                                                Worker worker = new Worker(Name, address, city, experience, Cnic, Email,phone, plumb, carpent, painting, electic,url);
                                                FirebaseDatabase.getInstance().getReference().child("Workers").child(userid).setValue(worker).addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(workerSignUp.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                                                        // Database node created successfully
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(workerSignUp.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                                                        // Database node creation failed
                                                    }
                                                });
                                            });
                                        })
                                        .addOnFailureListener(e -> {
                                            progressDialog.dismiss();
                                            Toast.makeText(workerSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } catch (Exception e) {
                            Log.d("OKKKK", e.toString());
                            Toast.makeText(workerSignUp.this, "" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(workerSignUp.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                cnic.setError("Enter Correct CNIC");}
        }
        sendUserToNextActivity();
    }
    private void sendUserToNextActivity()
    {
        Intent intent=new Intent(workerSignUp.this, MainActivity.class);
        startActivity(intent);
    }
    private void SendVerificationEmail(FirebaseAuth mAuth)
    {
        DatabaseReference mDatabase;
//Send verification email
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(workerSignUp.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(workerSignUp.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
