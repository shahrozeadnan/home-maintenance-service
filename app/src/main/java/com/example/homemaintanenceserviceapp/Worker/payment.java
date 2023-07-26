package com.example.homemaintanenceserviceapp.Worker;

import android.content.Intent;
import android.os.Bundle;

import com.example.homemaintanenceserviceapp.Model.Payment;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import com.example.homemaintanenceserviceapp.databinding.ActivityPaymentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class payment extends AppCompatActivity {
    ActivityPaymentBinding binding;
    String pay,desc;
    String myUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =   ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myUID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding.payment.setOnClickListener(view ->
        {
            paymentt();

        });

    }

    private void paymentt() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss ");
        String currentDateandTime = sdf.format(new Date());
        pay=binding.paymentbox.getText().toString();
        desc=binding.description.getText().toString();
        if(pay.isEmpty())
        {
            binding.paymentbox.setError("fill this");

        }
        else if(desc.isEmpty()) {
            binding.description.setError("fill this");


        }
        else {
            Payment payment = new Payment();
            payment.setPay(pay);
            payment.setDesc(desc);
            payment.setDateandtime(currentDateandTime);
            String cid = getIntent().getStringExtra("cID");
            FirebaseDatabase.getInstance().getReference().child("Payment")
                    .child(cid)
                    .setValue(payment);
            Toast.makeText(payment.this,"Total expenditure sent to customer",Toast.LENGTH_LONG).show();
            startActivity(new Intent(payment.this, workerhomepage.class));
        }
    }
}