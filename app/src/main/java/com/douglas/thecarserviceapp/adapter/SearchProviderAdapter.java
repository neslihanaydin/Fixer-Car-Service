package com.douglas.thecarserviceapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.User;
import com.douglas.thecarserviceapp.view.BookAppointment;
import com.douglas.thecarserviceapp.view.CreateAppointment;

import java.util.ArrayList;
import java.util.List;

public class SearchProviderAdapter extends RecyclerView.Adapter<SearchProviderAdapter.ViewHolder> {

    private List<User> providers;
    DatabaseHelper dbHelper;

    public SearchProviderAdapter(Context context, List<User> serviceProviders) {
        dbHelper = new DatabaseHelper(context);
        this.providers = serviceProviders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_search_provider, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User selectedProvider = providers.get(position);
        holder.textName.setText(selectedProvider.getFirstName() + " " + selectedProvider.getLastName());
        holder.textAddress.setText(selectedProvider.getAddress());
        holder.textPhoneNumber.setText(selectedProvider.getPhoneNumber());
        holder.cardProvider.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), BookAppointment.class);
            intent.putExtra("PROVIDER_ID", selectedProvider.getUserId());
            /*intent.putExtra("PROVIDER_FNAME", selectedProvider.getFirstName());
            intent.putExtra("PROVIDER_LNAME", selectedProvider.getLastName());
            intent.putExtra("PROVIDER_PHONE", selectedProvider.getPhoneNumber());
            intent.putExtra("PROVIDER_ADDRESS", selectedProvider.getAddress());*/
            v.getContext().startActivity(intent);
        });
    }

    public void setFilteredList(List<User> filteredProviders) {
        providers = filteredProviders;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return providers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textName;
        TextView textAddress;
        TextView textPhoneNumber;
        LinearLayout cardProvider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.txtProviderName);
            textPhoneNumber = itemView.findViewById(R.id.txtProviderPhone);
            textAddress = itemView.findViewById(R.id.txtProviderAddress);
            cardProvider = itemView.findViewById(R.id.cardProvider);
        }
    }
}
