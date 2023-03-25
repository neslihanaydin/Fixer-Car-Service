package com.douglas.thecarserviceapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.util.ItemDrawer;
import com.douglas.thecarserviceapp.view.NavigationActivity;
import com.douglas.thecarserviceapp.view.LoginActivity;
import com.douglas.thecarserviceapp.view.Profile;
import com.douglas.thecarserviceapp.view.RegistrationActivity;
import com.douglas.thecarserviceapp.view.SearchCustomer;
import com.douglas.thecarserviceapp.view.SearchProvider;
import com.douglas.thecarserviceapp.view.ServiceHistory;
import com.douglas.thecarserviceapp.view.ViewAppointments;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    Activity activity;
    ArrayList<ItemDrawer> listItems;

    public MainAdapter(Activity activity, ArrayList<ItemDrawer> arrayList){
        this.activity = activity;
        listItems = arrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drawer_main, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //set text on textView
        holder.textView.setText(listItems.get(position).getTxt());
        //set icon
        holder.icon.setImageResource(listItems.get(position).getImgId());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if(AppManager.instance.user.isCustomer()){
                    switch(position){
                        case 0:
                            activity.startActivity(new Intent(activity, NavigationActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                        case 1:
                            activity.startActivity(new Intent(activity, SearchProvider.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                        case 2:
                            activity.startActivity(new Intent(activity, ViewAppointments.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                        case 3:
                            activity.startActivity(new Intent(activity, ServiceHistory.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                        case 4:
                            activity.startActivity(new Intent(activity, Profile.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                        case 5:
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("logout");
                            builder.setMessage("Are you sure to logout?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    activity.finishAffinity();
                                    AppManager.instance.setUser(null);
                                    SharedPreferences preferences = activity.getSharedPreferences("MyPrefs", activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    activity.startActivity(new Intent(activity, LoginActivity.class));
                                    //System.exit(0);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            //show dialog
                            builder.show();
                            break;
                    }
                } else if(AppManager.instance.user.isProvider()){
                    switch(position){
                        case 0:
                            activity.startActivity(new Intent(activity, ViewAppointments.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                        case 1:
                            Intent intent = new Intent(activity, RegistrationActivity.class);
                            intent.putExtra("WITH_PROVIDER", true);
                            activity.startActivity(intent
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                        case 2:
                            activity.startActivity(new Intent(activity, SearchCustomer.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                        case 3:
                            activity.startActivity(new Intent(activity, ServiceHistory.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                        case 4:
                            //TO DO LOGOUT, End user session in the AppManager
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("logout");
                            builder.setMessage("Are you sure to logout?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    activity.finishAffinity();
                                    AppManager.instance.setUser(null);
                                    SharedPreferences preferences = activity.getSharedPreferences("MyPrefs", activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    activity.startActivity(new Intent(activity, LoginActivity.class));
                                    // System.exit(0);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            //show dialog
                            builder.show();
                            break;
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        //return arraylist size
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_item_drawer);
            icon = itemView.findViewById(R.id.icon_item_drawer);
        }
    }
}

