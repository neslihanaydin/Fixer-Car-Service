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

public class BookAppointmentFirstAdapter extends RecyclerView.Adapter {
    ItemClickListener itemClickListener;
    LayoutInflater inflater;
    List<User> uData;
    DatabaseHelper dbh;

    public BookAppointmentFirstAdapter(Context context, List<User> data, ItemClickListener itemClickListener){
        uData = data;
        this.itemClickListener = itemClickListener;
        inflater = LayoutInflater.from(context);
        dbh = new DatabaseHelper(context);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this view connect between activity and layout
        View view = inflater.inflate(R.layout.reclerview_favorite_provider, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //this purpose is to change contents of all elements
        ((ViewHolder)holder).txtProvider.setText(uData.get(position).getUserFirstandLastName());
        ((ViewHolder)holder).txtPhone.setText(uData.get(position).getPhoneNumber());
        ((ViewHolder)holder).txtAddress.setText(uData.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return uData.size();
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgLeft;
        ImageView imgRight;
        TextView txtProvider;
        TextView txtPhone;
        TextView txtAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Connect elements from layout with viewholder
            imgLeft = itemView.findViewById(R.id.favImgLeft);
            imgRight = itemView.findViewById(R.id.imgRight);
            txtProvider = itemView.findViewById(R.id.favTxtProviderName);
            txtPhone = itemView.findViewById(R.id.favTxtPhone);
            txtAddress = itemView.findViewById(R.id.favTxtAddress);

            //itemView is a set of all elements of favorite provider
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
