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
import com.douglas.thecarserviceapp.model.AppointmentWidget;

public class ViewAppointmentsAdapter extends RecyclerView.Adapter {
    ItemClickListener itemClickListener;
    AppointmentWidget[] wData;
    LayoutInflater inflater;

    public ViewAppointmentsAdapter(Context context, AppointmentWidget[] data, ItemClickListener itemClickListener){
        wData = data;
        this.itemClickListener = itemClickListener;
        inflater = LayoutInflater.from(context);
    }

    AppointmentWidget getItem(int id){
        return wData[id];
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recyclerview_viewappointments_provider, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ViewHolder)holder).imLeft.setImageResource(wData[position].getwImg());
        ((ViewHolder)holder).txDate.setText(wData[position].getTime());
        ((ViewHolder)holder).txCustomer.setText(wData[position].getCustomerName());
        ((ViewHolder)holder).txOpt.setText(wData[position].getDropOrPick());
        ((ViewHolder)holder).imRight.setImageResource(wData[position].getBtnDetail());

    }

    @Override
    public int getItemCount() {
        return wData.length;
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imLeft;
        ImageView imRight;
        TextView txDate;
        TextView txCustomer;
        TextView txOpt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imLeft = itemView.findViewById(R.id.imgLeft);
            txDate = itemView.findViewById(R.id.txtDate);
            txCustomer = itemView.findViewById(R.id.txtCustomer);
            txOpt = itemView.findViewById(R.id.txtOption);
            imRight = itemView.findViewById(R.id.imgRight);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
