package com.example.homemaintanenceserviceapp.MainPage;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.homemaintanenceserviceapp.R;

public class Aboutus extends AppCompatActivity {

    ImageView carpentry,plumbing,electrical,paint;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        toolbar=findViewById(R.id.toolbaraboutus);
        carpentry=findViewById(R.id.imageView4);
        plumbing=findViewById(R.id.imageViewplumbing);
        electrical=findViewById(R.id.imageViewelectrical);
        paint=findViewById(R.id.imageViewpaint);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    carpentry.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.chosencarpentry));
    plumbing.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.chosenplumbing));
    electrical.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.chosenelectrical));
    paint.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.chosenpaint));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}