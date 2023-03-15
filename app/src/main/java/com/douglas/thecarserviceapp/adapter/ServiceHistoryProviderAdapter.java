package com.douglas.thecarserviceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.Appointment;

import java.text.DecimalFormat;
import java.util.List;

public class ServiceHistoryProviderAdapter extends RecyclerView.Adapter {
    ServiceHistoryProviderAdapter.ItemClickListener itemClickListener;
    LayoutInflater inflater;
    List<Appointment> wData;
    DatabaseHelper dbHelper;


    public ServiceHistoryProviderAdapter(Context context, List<Appointment> data, ServiceHistoryProviderAdapter.ItemClickListener itemClickListener){
        wData = data;
        this.itemClickListener = itemClickListener;
        inflater = LayoutInflater.from(context);
        dbHelper =  new DatabaseHelper(context);
    }

    Appointment getItem(int id){
        return wData.get(id);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recyclerview_service_history_provider, parent,false);
        ServiceHistoryProviderAdapter.ViewHolder viewHolder = new ServiceHistoryProviderAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int userId = wData.get(position).getUserId();
        int serviceId = wData.get(position).getServiceId();
        DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
        ((ServiceHistoryProviderAdapter.ViewHolder)holder).txtDateService.setText(wData.get(position).getDateTime());
        ((ServiceHistoryProviderAdapter.ViewHolder)holder).txtCustomer.setText(dbHelper.getUserName(userId));
        ((ServiceHistoryProviderAdapter.ViewHolder)holder).txtCustomerAddress.setText(dbHelper.getUserAddress(userId));
        ((ServiceHistoryProviderAdapter.ViewHolder)holder).txtComments.setText(wData.get(position).getComments());
        ((ServiceHistoryProviderAdapter.ViewHolder)holder).txtType.setText(wData.get(position).getType());
        ((ServiceHistoryProviderAdapter.ViewHolder)holder).txtTotal.setText("$" + decimalFormat.format(dbHelper.getServiceCost(serviceId)));

    }

    @Override
    public int getItemCount() {
        return wData.size();
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtDateService, txtCustomerAddress, txtCustomer, txtComments, txtType, txtTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDateService = itemView.findViewById(R.id.txtDateService);
            txtCustomer = itemView.findViewById(R.id.txtCustomer);
            txtCustomerAddress = itemView.findViewById(R.id.txtCustomerAddress);
            txtComments = itemView.findViewById(R.id.txtComments);
            txtType = itemView.findViewById(R.id.txtType);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            itemView.setOnClickListener(v -> {
                itemClickListener.onItemClick(v, getAdapterPosition());
            });
        }
    }
}