package com.douglas.thecarserviceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.Appointment;

import java.text.DecimalFormat;
import java.util.List;

public class ServiceHistoryCustomerAdapter extends RecyclerView.Adapter {
    ServiceHistoryCustomerAdapter.ItemClickListener itemClickListener;
    LayoutInflater inflater;
    List<Appointment> aData;
    DatabaseHelper dbHelper;

    public ServiceHistoryCustomerAdapter(Context context, List<Appointment> data, ServiceHistoryCustomerAdapter.ItemClickListener itemClickListener) {
        aData = data;
        this.itemClickListener = itemClickListener;
        inflater = LayoutInflater.from(context);
        dbHelper =  new DatabaseHelper(context);
    }

    Appointment getItem(int id){
        return aData.get(id);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recyclerview_service_history_customer, parent,false);
        ServiceHistoryCustomerAdapter.ViewHolder viewHolder = new ServiceHistoryCustomerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int providerId = aData.get(position).getProviderId();
        int serviceId = aData.get(position).getServiceId();
        DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
        ((ServiceHistoryCustomerAdapter.ViewHolder)holder).txtDateService.setText(aData.get(position).getDateTime());
        ((ServiceHistoryCustomerAdapter.ViewHolder)holder).txtProvider.setText(dbHelper.getUserName(providerId));
        ((ServiceHistoryCustomerAdapter.ViewHolder)holder).txtProviderAddress.setText(dbHelper.getUserAddress(providerId));
        ((ServiceHistoryCustomerAdapter.ViewHolder)holder).txtServices.setText(dbHelper.getServiceType(serviceId));
        ((ServiceHistoryCustomerAdapter.ViewHolder)holder).txtTotal.setText("$" + decimalFormat.format(dbHelper.getServiceCost(serviceId)));

    }

    @Override
    public int getItemCount() {
        return aData.size();
    }

    public interface ItemClickListener {
        void onItemClickCustomer(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDateService, txtProvider, txtProviderAddress, txtServices, txtTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDateService = itemView.findViewById(R.id.txtDateService);
            txtProvider = itemView.findViewById(R.id.txtProvider);
            txtProviderAddress = itemView.findViewById(R.id.txtProviderAddress);
            txtServices = itemView.findViewById(R.id.txtServices);
            txtTotal = itemView.findViewById(R.id.txtTotal);
        }
    }
}