package com.example.homemaintanenceserviceapp.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemaintanenceserviceapp.Adapters.WorkerAdapter;
import com.example.homemaintanenceserviceapp.Model.Worker;
import com.example.homemaintanenceserviceapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WorkerList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference ref;
    WorkerAdapter workerAdapter;
    ArrayList<Worker>list;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_list);
        recyclerView=findViewById(R.id.WorkerList);
        toolbar=findViewById(R.id.toolbarworker);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent=new Intent(WorkerList.this, AdminHomepage.class);
            startActivity(intent);
            finish();});
        ref= FirebaseDatabase.getInstance().getReference("Workers");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        workerAdapter=new WorkerAdapter(this,list);
        recyclerView.setAdapter(workerAdapter);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Worker worker=dataSnapshot.getValue(Worker.class);
                    list.add(worker);
                }
                workerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}