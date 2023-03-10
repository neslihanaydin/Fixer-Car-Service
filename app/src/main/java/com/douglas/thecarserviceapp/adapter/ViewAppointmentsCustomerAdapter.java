package com.douglas.thecarserviceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.Appointment;

import java.util.List;

public class ViewAppointmentsCustomerAdapter extends RecyclerView.Adapter {
    ItemClickListener itemClickListener;
    LayoutInflater inflater;
    List<Appointment> aData;
    DatabaseHelper dbHelper;

    public ViewAppointmentsCustomerAdapter(Context context, List<Appointment> data, ItemClickListener itemClickListener){
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

        View view = inflater.inflate(R.layout.recyclerview_viewappointments_customer, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int providerId = aData.get(position).getProviderId();
        int serviceId = aData.get(position).getServiceId();

        ((ViewHolder)holder).txDateAp.setText(aData.get(position).getDateTime());
        ((ViewHolder)holder).txProviderAp.setText(dbHelper.getUserName(providerId));
        ((ViewHolder)holder).txProviderDtlAp.setText(dbHelper.getUserAddress(providerId));
        ((ViewHolder)holder).txServicesAp.setText(dbHelper.getServiceType(serviceId));
        ((ViewHolder)holder).txTypeAp.setText(aData.get(position).getType());

    }

    @Override
    public int getItemCount() {
        return aData.size();
    }

    public interface ItemClickListener{
        void onItemClickCustomer(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txDateAp;
        TextView txProviderAp;
        TextView txProviderDtlAp;
        TextView txServicesAp;
        TextView txTypeAp;
        Button btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txDateAp = itemView.findViewById(R.id.txtDateAp);
            txProviderAp = itemView.findViewById(R.id.txtProviderAp);
            txProviderDtlAp = itemView.findViewById(R.id.txtProviderDtlAp);
            txServicesAp = itemView.findViewById(R.id.txtServicesAp);
            txTypeAp = itemView.findViewById(R.id.txtTypeAp);
            btnCancel = itemView.findViewById(R.id.btnCancelAp);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClickCustomer(v, getAdapterPosition());
                }
            });
        }
    }
}
