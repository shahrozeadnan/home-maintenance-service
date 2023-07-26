package com.example.homemaintanenceserviceapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homemaintanenceserviceapp.MainPage.MainActivity;
import com.example.homemaintanenceserviceapp.R;

public class AdminHomepage extends AppCompatActivity {

    Button ViewCustomers,ViewWorkers;
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);

        ViewCustomers=findViewById(R.id.Customersview);
        ViewWorkers=findViewById(R.id.WorkersView);
        logout=findViewById(R.id.logout);

        ViewCustomers.setOnClickListener(V->SendtoCustomerList());
        ViewWorkers.setOnClickListener(V->SendtoWorkerList());
        logout.setOnClickListener(V->setLogout());
    }
    void SendtoCustomerList()
    {
        Intent intent=new Intent(AdminHomepage.this, CustomerList.class);
        startActivity(intent);
    }
    void SendtoWorkerList()
    {
        Intent intent=new Intent(AdminHomepage.this, WorkerList.class);
        startActivity(intent);
    }
    void setLogout()
    {
        Intent intent=new Intent(AdminHomepage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    }
