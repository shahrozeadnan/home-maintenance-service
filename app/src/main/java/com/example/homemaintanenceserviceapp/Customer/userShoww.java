package com.example.homemaintanenceserviceapp.Customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;

import com.example.homemaintanenceserviceapp.Adapters.RegWorkersAdapter;
import com.example.homemaintanenceserviceapp.Adapters.workershowAdapter;
import com.example.homemaintanenceserviceapp.Model.LocationTracking;
import com.example.homemaintanenceserviceapp.Model.Worker;
import com.example.homemaintanenceserviceapp.R;
import com.example.homemaintanenceserviceapp.databinding.ActivityUserShowwBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class userShoww extends Activity {
    RegWorkersAdapter regWorkersAdapter;
    workershowAdapter workershowAdapter;
    ArrayList<LocationTracking> list;
    ArrayList<Worker> list2;
    String Type;
    Query query, cityquery;
    ActivityUserShowwBinding binding;
    String choosencity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserShowwBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar3.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        binding.toolbar3.setNavigationOnClickListener(view -> {
            Intent intent=new Intent(userShoww.this, homepage_user.class);
            startActivity(intent);
            finish();
        });
        clickListeners();
    }

    private void clickListeners() {
        binding.livelocation.setOnClickListener(view -> {
            setadapter();
            getAllLiveWorkers();
        });

        binding.registeredworkers.setOnClickListener(view -> {
            RegWorkerssetAdapter();
            useLater();
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Cities, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sp.setAdapter(adapter);

        binding.sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choosencity = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

     /*   binding.RadioGroupCity.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = findViewById(checkedId);
            choosencity = selectedRadioButton.getText().toString();
        });*/
    }

    private void getAllLiveWorkers() {
        list.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("AvailableWorkers");
        Intent intent = getIntent();
        Type = intent.getStringExtra("Type");

        switch (Type) {
            case "plumbing":
                query = ref.orderByChild("type").equalTo("plumbing");
                break;
            case "electrical":
                query = ref.orderByChild("type").equalTo("electrical");
                break;
            case "paint":
                query = ref.orderByChild("type").equalTo("paint");
                break;
            case "carpentry":
                query = ref.orderByChild("type").equalTo("carpentry");
                break;
        }


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LocationTracking locationTracking = dataSnapshot.getValue(LocationTracking.class);
                    list.add(locationTracking);

                }
                workershowAdapter.notifyDataSetChanged();
                if (list.isEmpty()) {
                    Toast.makeText(userShoww.this, "No Current live Workers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void useLater() {
        list2.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Workers");
        Intent intent = getIntent();
        Type = intent.getStringExtra("Type");

        switch (Type) {
            case "plumbing":
                query = ref.orderByChild("plumbing").equalTo(true);
                break;
            case "electrical":
                query = ref.orderByChild("electrical").equalTo(true);
                break;
            case "paint":
                query = ref.orderByChild("paint").equalTo(true);
                break;
            case "carpentry":
                query = ref.orderByChild("carpentry").equalTo(true);
                break;
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Worker worker = dataSnapshot.getValue(Worker.class);
                    assert worker != null;
                    if(worker.getCity().equals(choosencity)) {
                        list2.add(worker);

                    }
                    regWorkersAdapter.notifyDataSetChanged();
                }
                if (list2.isEmpty()) {
                    Toast.makeText(userShoww.this, "No Workers for service", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setadapter() {
        binding.WorkerShowList.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        workershowAdapter = new workershowAdapter(this, list);
        binding.WorkerShowList.setAdapter(workershowAdapter);
    }

    private void RegWorkerssetAdapter()
    {
        binding.WorkerShowList.setLayoutManager(new LinearLayoutManager(this));
        list2 = new ArrayList<>();
        regWorkersAdapter = new RegWorkersAdapter(this, list2);
        binding.WorkerShowList.setAdapter(regWorkersAdapter);
    }

}