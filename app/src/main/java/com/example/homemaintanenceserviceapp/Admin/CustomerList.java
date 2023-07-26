package com.example.homemaintanenceserviceapp.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemaintanenceserviceapp.Adapters.CustomerAdapter;
import com.example.homemaintanenceserviceapp.Model.Customer;
import com.example.homemaintanenceserviceapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference ref;
    CustomerAdapter customerAdapter;
    ArrayList<Customer>list;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        recyclerView=findViewById(R.id.CustomerList);
        toolbar=findViewById(R.id.toolbarcustomer);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent=new Intent(CustomerList.this, AdminHomepage.class);
            startActivity(intent);
            finish();});
        ref= FirebaseDatabase.getInstance().getReference("Customers");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        customerAdapter=new CustomerAdapter(this,list);
        recyclerView.setAdapter(customerAdapter);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Customer customer=dataSnapshot.getValue(Customer.class);
                    list.add(customer);
                }
                customerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}