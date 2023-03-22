package com.douglas.thecarserviceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.Appointment;
import com.douglas.thecarserviceapp.model.Service;

import java.util.List;

public class ProviderServicesAdapter extends RecyclerView.Adapter{

    ItemClickListener itemClickListener;
    LayoutInflater inflater;
    List<Service> sData;
    DatabaseHelper dbHelper;

    public ProviderServicesAdapter(Context context, List<Service> data, ItemClickListener itemClickListener){
        sData = data;
        this.itemClickListener = itemClickListener;
        inflater = LayoutInflater.from(context);
        dbHelper =  new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_services_costs, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).txtServiceRc.setText(sData.get(position).getType());
        ((ViewHolder)holder).txtCostRc.setText("$" + sData.get(position).getCost());
    }

    @Override
    public int getItemCount() {
        return sData.size();
    }
    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtServiceRc;
        TextView txtCostRc;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtServiceRc = itemView.findViewById(R.id.txtServiceRc);
            txtCostRc = itemView.findViewById(R.id.txtPriceRc);
            checkBox = itemView.findViewById(R.id.checkRc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
