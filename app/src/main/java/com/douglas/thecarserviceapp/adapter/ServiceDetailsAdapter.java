package com.douglas.thecarserviceapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.Service;

import org.w3c.dom.Text;

import java.util.List;

public class ServiceDetailsAdapter extends RecyclerView.Adapter {

    ItemClickListener itemClickListener;
    LayoutInflater inflater;
    List<Service> sData;
    DatabaseHelper dbHelper;

    public ServiceDetailsAdapter(Context context, List<Service> data, ItemClickListener itemClickListener){
        sData = data;
        this.itemClickListener = itemClickListener;
        inflater = LayoutInflater.from(context);
        dbHelper =  new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_service_details, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).txtService.setText(sData.get(position).getType());
        ((ViewHolder)holder).edtCost.setText("$" + sData.get(position).getCost());
    }

    @Override
    public int getItemCount() {
        return sData.size();
    }
    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtService;
        TextView edtCost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtService = itemView.findViewById(R.id.txtServiceName);
            edtCost = itemView.findViewById(R.id.txtCost);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });

        }
    }
}
