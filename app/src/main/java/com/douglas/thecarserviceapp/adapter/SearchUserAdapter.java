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
import com.douglas.thecarserviceapp.model.User;

import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter {

    ItemClickListener itemClickListener;
    public List<User> userList;
    LayoutInflater inflater;
    DatabaseHelper dbHelper;

    public SearchUserAdapter(Context context, List<User> data, ItemClickListener itemClickListener){
        userList = data;
        this.itemClickListener = itemClickListener;
        inflater = LayoutInflater.from(context);
        dbHelper = new DatabaseHelper(context);
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = inflater.inflate(R.layout.recyclerview_searchcustomers_widget, parent, false);
       ViewHolder viewHolder = new ViewHolder(view);
       return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ViewHolder)holder).txCustomer.setText(userList.get(position).getUserFirstandLastName());
        ((ViewHolder)holder).txPhone.setText(userList.get(position).getPhoneNumber());
        ((ViewHolder)holder).txAddress.setText(userList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setFilteredList(List<User> filteredList){
        this.userList = filteredList;
        notifyDataSetChanged();
    }
    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imLeft;
        ImageView imRight;
        TextView txCustomer;
        TextView txPhone;
        TextView txAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imLeft = itemView.findViewById(R.id.searchLeft);
            txCustomer = itemView.findViewById(R.id.txtUserName);
            txPhone = itemView.findViewById(R.id.txtPhone);
            txAddress = itemView.findViewById(R.id.txtAddress);
            imRight = itemView.findViewById(R.id.searchRight);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });

        }
    }
}
