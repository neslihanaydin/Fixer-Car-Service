package com.douglas.thecarserviceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.User;
import com.douglas.thecarserviceapp.view.CustomerDetail;
import com.douglas.thecarserviceapp.view.Profile;

public class ProfileAdapter extends RecyclerView.Adapter {

    ItemClickListener itemClickListener;
    LayoutInflater inflater;
    User user;
    DatabaseHelper dbHelper;
    public static final int FIELD_COUNT = 5;

    public ProfileAdapter(Context context, User user,ItemClickListener itemClickListener)
    {
        this.user = user;
        this.itemClickListener = itemClickListener;
        inflater = LayoutInflater.from(context);
        dbHelper = new DatabaseHelper(context);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.edit_field_with_icon, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (position){
            case 0:
                ((ViewHolder)holder).editField.setText(user.getFirstName());
                break;
            case 1:
                ((ViewHolder)holder).editField.setText(user.getLastName());
                break;
            case 2:
                ((ViewHolder)holder).editField.setText(user.getAddress());
                break;
            case 3:
                ((ViewHolder)holder).editField.setText(user.getPhoneNumber());
                break;
            case 4:
                ((ViewHolder)holder).editField.setText(user.getEmail());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return FIELD_COUNT;
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        EditText editField;
        ImageButton editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editField = itemView.findViewById(R.id.edTxtSearchByCity);
            editField.setEnabled(false);
            editButton = itemView.findViewById(R.id.btnSearchByCity);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editField.setEnabled(true);
                    itemClickListener.onItemClick(v, getAdapterPosition());
                    int buttonTextColor = ContextCompat.getColor(itemView.getContext(), R.color.fixer_black);
                    if(Profile.buttonSave != null){
                        Profile.buttonSave.setEnabled(true);
                        Profile.buttonSave.setBackgroundResource(R.drawable.button_enabled);
                        Profile.buttonSave.setTextColor(buttonTextColor);
                    }
                    if(CustomerDetail.buttonSaveCus != null){
                        CustomerDetail.buttonSaveCus.setEnabled(true);
                        CustomerDetail.buttonSaveCus.setBackgroundResource(R.drawable.button_enabled);
                        CustomerDetail.buttonSaveCus.setTextColor(buttonTextColor);
                    }

                }
            });
        }
    }
}
