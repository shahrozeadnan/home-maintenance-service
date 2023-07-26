package com.example.homemaintanenceserviceapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemaintanenceserviceapp.Model.LocationTracking;
import com.example.homemaintanenceserviceapp.R;
import com.example.homemaintanenceserviceapp.Customer.customerwaiting;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class workershowAdapter extends RecyclerView.Adapter<workershowAdapter.workershowViewHolder>{

    Context context;
    ArrayList<LocationTracking> list;

    public workershowAdapter(Context context,ArrayList<LocationTracking>list) {
        this.context = context;
        this.list=list;
    }
    @NonNull
    @Override
    public workershowAdapter.workershowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.workershowitem,parent, false);
        return new workershowAdapter.workershowViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull workershowAdapter.workershowViewHolder holder, int position) {
        holder.WName.setText(list.get(position).getWorkerName());
        holder.WPhonenum.setText(list.get(position).getPhoneNumber());
        String imageUrl =list.get(position).getImage();
        Picasso.get().load(imageUrl).into(holder.picture);
    /*    String phoneNumber = list.get(position).getPhoneNumber();
        holder.Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                context.startActivity(intent);
            }
        });

        holder.Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + phoneNumber));
                context.startActivity(intent);
            }
        });*/
        holder.SendReq.setOnClickListener(view ->{
            context.startActivity(new Intent(context, customerwaiting.class)
                    .putExtra("dID", list.get(position).getUID()));
            //postAvailability(list.get(position).getUID())
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public static class workershowViewHolder extends RecyclerView.ViewHolder {
        TextView WName,WPhonenum;
        Button SendReq;
                //Call,Message;
        ImageView picture;
        public workershowViewHolder(@NonNull View itemView) {
            super(itemView);
            WName=itemView.findViewById(R.id.workername);
            WPhonenum=itemView.findViewById(R.id.workerphonenumber);
            SendReq=itemView.findViewById(R.id.sendrequest);
            picture=itemView.findViewById(R.id.background);
         /*   Call=itemView.findViewById(R.id.Call);
            Message=itemView.findViewById(R.id.Message);*/
        }
    }
}
