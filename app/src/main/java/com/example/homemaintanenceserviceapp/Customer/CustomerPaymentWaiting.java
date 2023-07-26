package com.example.homemaintanenceserviceapp.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.homemaintanenceserviceapp.R;
import com.example.homemaintanenceserviceapp.databinding.ActivityCustomerPaymentWaitingBinding;
import com.example.homemaintanenceserviceapp.databinding.ActivityCustomerwaitingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerPaymentWaiting extends AppCompatActivity {
    ActivityCustomerPaymentWaitingBinding binding;
    String myUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerPaymentWaitingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        setVisibility(1);
        getdata();
    }

    private void getdata() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Payment").child(myUID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String dID = getIntent().getStringExtra("dID");
                    startActivity(new Intent(CustomerPaymentWaiting.this, CustomerPayment.class)
                            .putExtra("dID", dID));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setVisibility(int val) {
        if (val == 1) {
            binding.progressCircular.setVisibility(View.VISIBLE);
            return;
        }
    }

}
