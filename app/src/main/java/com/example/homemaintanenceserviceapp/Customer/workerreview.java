package com.example.homemaintanenceserviceapp.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homemaintanenceserviceapp.Model.Review;
import com.example.homemaintanenceserviceapp.Worker.workerhomepage;
import com.example.homemaintanenceserviceapp.databinding.ActivityWorkerreviewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class workerreview extends AppCompatActivity {
    ActivityWorkerreviewBinding binding;
    float ratevalue;
    String temp;
    String myUID;
    String rev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkerreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding.ratingBar.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            ratevalue = ratingBar.getRating();
            if (ratevalue <= 1 || ratevalue == 0) {
                binding.ratecount.setText("Bad " + ratevalue + "/5");

            } else if (ratevalue > 1 && ratevalue <= 2) {
                binding.ratecount.setText("Bad " + ratevalue + "/5");

            } else if (ratevalue > 2 && ratevalue <= 3) {
                binding.ratecount.setText("Average " + ratevalue + "/5");

            } else if (ratevalue > 3 && ratevalue <= 4) {
                binding.ratecount.setText("Good " + ratevalue + "/5");

            } else if (ratevalue > 4 && ratevalue <= 4.5) {
                binding.ratecount.setText("very Good " + ratevalue + "/5");

            } else if (ratevalue == 5) {
                binding.ratecount.setText("Excellent " + ratevalue + "/5");

            }

        });
        binding.submit.setOnClickListener(view ->
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss ");
            String currentDateandTime = sdf.format(new Date());
            temp = binding.ratecount.getText().toString();
            rev = binding.reviewbox.getText().toString();
            Review review = new Review();
            review.setRate(temp);
            review.setReviewdesc(rev);
            review.setDateandtime(currentDateandTime);
            String dID = getIntent().getStringExtra("dID");
            FirebaseDatabase.getInstance().getReference().child("Review")
                    .child(dID).child(myUID).setValue(review);


            Toast.makeText(workerreview.this,"Review Submitted.",Toast.LENGTH_LONG).show();

            Intent intent=new Intent(workerreview.this, homepage_user.class);
            startActivity(intent);
            finish();


        });




    }
}