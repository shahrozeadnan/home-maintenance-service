package com.example.homemaintanenceserviceapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemaintanenceserviceapp.Model.AcceptedRequest;
import com.example.homemaintanenceserviceapp.Model.LocationTracking;
import com.example.homemaintanenceserviceapp.R;
import com.example.homemaintanenceserviceapp.Worker.tracker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class usershowAdapter extends RecyclerView.Adapter<usershowAdapter.Viewholder>{
    Context context;
    usershowAdapter usershowAdapter;
    String WID;
    ArrayList<LocationTracking> list;
    String myUID;

    public usershowAdapter(Context context, ArrayList<LocationTracking> list,String WID) {
        this.context = context;
        this.list = list;
        this.WID=WID;
    }

    @NonNull
    @Override
    public usershowAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.liveusershowing,parent, false);
        return new usershowAdapter.Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull usershowAdapter.Viewholder holder, int position) {
        holder.customername.setText(list.get(position).getWorkerName());
        holder.customerphonenum.setText(list.get(position).getPhoneNumber());
        String imageUrl =list.get(position).getImage();
        double lat= list.get(position).getLatitude();
        double lon=list.get(position).getLongitude();
        Picasso.get().load(imageUrl).into(holder.picture);
        holder.accept.setOnClickListener(view -> {
            /*myUID= FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference()
                    .child("AvailableWorkers")
                    .child(myUID)
                    .removeValue();*/
            AcceptedRequest acceptedRequest=new AcceptedRequest();
            acceptedRequest.setCID(list.get(position).getUID());
            acceptedRequest.setWID(WID);
            Bundle bundle=new Bundle();
            bundle.putString("cID",list.get(position).getUID());
            bundle.putDouble("lat",lat);
            bundle.putDouble("lon",lon);

            FirebaseDatabase.getInstance().getReference().child("AcceptedRequest")
                    .child(list.get(position).getUID())
                    .setValue(acceptedRequest);
            Intent intent = new Intent(context, tracker.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
            //acceptRequest(list.get(position));
        });
        holder.reject.setOnClickListener(view -> {
            list.remove(position);
            notifyItemRemoved(position);
            FirebaseDatabase.getInstance().getReference().child("Requests").child(WID).
                    removeValue();


        });

    }

    private void acceptRequest(LocationTracking locationTracking) {
        AcceptedRequest acceptedRequest=new AcceptedRequest();
        acceptedRequest.setCID(locationTracking.getUID());
        acceptedRequest.setWID(WID);
        FirebaseDatabase.getInstance().getReference().child("AcceptedRequest")
                .child(locationTracking.getUID())
                .setValue(acceptedRequest);
        context.startActivity(new Intent(context, tracker.class)
                .putExtra("cID", locationTracking.getUID()));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView customername,customerphonenum;
        Button accept,reject;
        ImageView picture;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            customername=itemView.findViewById(R.id.username);
            customerphonenum=itemView.findViewById(R.id.userphonenumber);
            accept=itemView.findViewById(R.id.Accept);
            reject=itemView.findViewById(R.id.Reject);
            picture=itemView.findViewById(R.id.background);
        }
    }

}
