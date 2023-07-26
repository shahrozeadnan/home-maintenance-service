package com.example.homemaintanenceserviceapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemaintanenceserviceapp.Model.Worker;
import com.example.homemaintanenceserviceapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RegWorkersAdapter extends RecyclerView.Adapter<RegWorkersAdapter.RegWorkersViewHolder>{
    Context context;
    ArrayList<Worker> list;
    String n="Name: ";

    public RegWorkersAdapter(Context context,ArrayList<Worker>list) {
        this.context = context;
        this.list=list;
    }
    @NonNull
    @Override
    public RegWorkersAdapter.RegWorkersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.regworkersitem,parent, false);
        return new RegWorkersAdapter.RegWorkersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RegWorkersAdapter.RegWorkersViewHolder holder, int position) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(n);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannableStringBuilder.setSpan(boldSpan, 0, n.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.WName.setText(n +list.get(position).getName());
        holder.WPhonenum.setText("Phonenumber: "+list.get(position).getPhonenum());
        holder.WExperience.setText("Experience: "+list.get(position).getExperience());
        //holder.Waddress.setText(list.get(position).getAddress());
        String imageUrl =list.get(position).getImage();
        Picasso.get().load(imageUrl).into(holder.picture);
        String phoneNumber = list.get(position).getPhonenum();
        holder.Call.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        });

        holder.Message.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + phoneNumber));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public static class RegWorkersViewHolder extends RecyclerView.ViewHolder {
        TextView WName,WPhonenum,WExperience;
        Button Call,Message;
        ImageView picture;
        public RegWorkersViewHolder(@NonNull View itemView) {
            super(itemView);
            WName=itemView.findViewById(R.id.workername);
            WPhonenum=itemView.findViewById(R.id.workerphonenumber);
            WExperience=itemView.findViewById(R.id.workerExperience);
         //   Waddress=itemView.findViewById(R.id.wAddress);
            Call=itemView.findViewById(R.id.Call);
            Message=itemView.findViewById(R.id.Message);
            picture=itemView.findViewById(R.id.background);
        }
    }
}
