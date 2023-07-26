package com.example.homemaintanenceserviceapp.Adapters;

import android.content.Context;
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

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.WorkerViewHolder>{

    Context context;
    ArrayList<Worker> list;

    public WorkerAdapter(Context context, ArrayList<Worker>list) {
        this.context = context;
        this.list=list;
    }
    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.workeritem,parent, false);
        return new WorkerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerViewHolder holder, int position) {
        Worker worker=list.get(position);
        holder.WName.setText(worker.getName());
        holder.WAddress.setText(worker.getAddress());
        holder.WExp.setText(worker.getExperience());
        holder.WCnic.setText(worker.getCnic());
        holder.WCity.setText(worker.getCity());
        holder.WPhonenum.setText(worker.getPhonenum());
        if(worker.isPaint())
        {
            holder.Wpaint.setVisibility(View.VISIBLE);
            holder.Wpaint.setText("Painter");
        }
        else if(worker.isPlumbing())
        {
            holder.Wplumbing.setVisibility(View.VISIBLE);
            holder.Wplumbing.setText("Plumber");
        }
        else if(worker.isElectrical())
        {
            holder.Welectrical.setVisibility(View.VISIBLE);
            holder.Welectrical.setText("Electrician");
        }
        else if(worker.isCarpentry())
        {
            holder.Wcarpentry.setVisibility(View.VISIBLE);
            holder.Wcarpentry.setText("Carpenter");
        }
        String imageUrl = worker.getImage();
        Picasso.get().load(imageUrl).into(holder.Wimage);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public static class WorkerViewHolder extends RecyclerView.ViewHolder {
        TextView WName,WAddress,WCity,WExp,WCnic,WPhonenum,Welectrical,Wplumbing,Wcarpentry,Wpaint;
        ImageView Wimage;
        public WorkerViewHolder(@NonNull View itemView) {
            super(itemView);
            WName=itemView.findViewById(R.id.WorkerName);
            WAddress=itemView.findViewById(R.id.WorkerAddress);
            WCity=itemView.findViewById(R.id.WorkerCity);
            WExp=itemView.findViewById(R.id.WorkerExperience);
            WCnic=itemView.findViewById(R.id.WorkerCnic);
            WPhonenum=itemView.findViewById(R.id.Workerphonenum);
            Welectrical=itemView.findViewById(R.id.Welectrical);
            Wplumbing=itemView.findViewById(R.id.Wplumbing);
            Wcarpentry=itemView.findViewById(R.id.Wcarpentry);
            Wpaint=itemView.findViewById(R.id.Wpaint);
           Wimage=itemView.findViewById(R.id.WorkerImage);
        }
    }
}