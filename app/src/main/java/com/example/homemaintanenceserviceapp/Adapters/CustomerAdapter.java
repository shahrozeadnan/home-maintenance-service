package com.example.homemaintanenceserviceapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemaintanenceserviceapp.Model.Customer;
import com.example.homemaintanenceserviceapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>{

Context context;
ArrayList<Customer> list;

    public CustomerAdapter(Context context,ArrayList<Customer>list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.customeritem,parent, false);
        return new CustomerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
         Customer customer=list.get(position);
         holder.CName.setText(customer.getName());
         holder.CEmail.setText(customer.getEmail());
         holder.CCnic.setText(customer.getCnic());
         holder.CCity.setText(customer.getCity());
         holder.CPhone.setText(customer.getPhonenum());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder{

        TextView CName,CEmail,CPhone,CCity,CCnic;
        Button delete;
        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            CName=itemView.findViewById(R.id.CustName);
            CCity=itemView.findViewById(R.id.Customercity);
            CCnic=itemView.findViewById(R.id.Customercnic);
            CEmail=itemView.findViewById(R.id.CustomerEmail);
            CPhone=itemView.findViewById(R.id.Customerphonenum);
        }
    }
}
