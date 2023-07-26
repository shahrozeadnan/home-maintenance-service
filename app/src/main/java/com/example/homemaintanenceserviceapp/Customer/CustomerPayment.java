package com.example.homemaintanenceserviceapp.Customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import com.example.homemaintanenceserviceapp.databinding.ActivityCustomerPaymentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerPayment extends AppCompatActivity {
    ActivityCustomerPaymentBinding binding;
    String myUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getammount();
        binding.homepage.setOnClickListener(view -> {
            Intent intent = new Intent(CustomerPayment.this, userShoww.class);
            startActivity(intent);
        });


        binding.paymentcustomer.setOnClickListener(view -> Toast.makeText(CustomerPayment.this, "Paid", Toast.LENGTH_SHORT).show());
        binding.review.setOnClickListener(view -> givereview());
    }

    private void givereview() {
        String dID = getIntent().getStringExtra("dID");
        startActivity(new Intent(CustomerPayment.this, workerreview.class).putExtra("dID", dID));
    }

    private void getammount() {
        //String dID=getIntent().getStringExtra("dID");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Payment")
                .child(myUID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String pay = snapshot.child("pay").getValue(String.class);
                    String desc=snapshot.child("desc").getValue(String.class);
                    binding.ammount.setText(pay);
                    binding.Description.setText(desc);

                    ref.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }
}