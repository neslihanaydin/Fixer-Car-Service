package com.douglas.thecarserviceapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import com.douglas.thecarserviceapp.view.ProviderServices;
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

        // BEGIN FIXER CUSTOM LOGOUT DIALOG BOX
        LayoutInflater inflater = LayoutInflater.from(activity);
        View customDialogView = inflater.inflate(R.layout.fixer_dialog_logout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(customDialogView);
        Dialog customDialog = builder.create();
        customDialog.setContentView(customDialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(customDialog.getWindow().getAttributes());
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.8);
        lp.width = width;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        customDialog.getWindow().setAttributes(lp);
        customDialog.getWindow().setBackgroundDrawable(null);

        TextView title = customDialogView.findViewById(R.id.dialog_title);
        title.setText("Logout");

        TextView message = customDialogView.findViewById(R.id.dialog_message);
        message.setText("Are you sure you want to logout?");
        // END

        holder.textView.setOnClickListener(view -> {
            int position1 = holder.getAdapterPosition();
            if(AppManager.instance.user.isCustomer()){
                switch(position1){
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
                        //BEGIN FIXER CUSTOM LOGOUT DIALOG BOX
                        Button cancelButton = customDialogView.findViewById(R.id.dialog_cancel_button);
                        cancelButton.setOnClickListener((View.OnClickListener) v -> customDialog.dismiss());

                        Button yesButton = customDialogView.findViewById(R.id.dialog_yes_button);
                        yesButton.setOnClickListener((View.OnClickListener) v -> {
                            activity.finishAffinity();
                            AppManager.instance.setUser(null);
                            SharedPreferences preferences = activity.getSharedPreferences("MyPrefs", activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();
                            activity.startActivity(new Intent(activity, LoginActivity.class));
                            customDialog.dismiss();
                        });
                        customDialog.show();
                        // END
                        break;
                        /*
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure to logout?");
                        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                            activity.finishAffinity();
                            AppManager.instance.setUser(null);
                            SharedPreferences preferences = activity.getSharedPreferences("MyPrefs", activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();
                            activity.startActivity(new Intent(activity, LoginActivity.class));
                            //System.exit(0);
                        });
                        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
                        //show dialog
                        builder.show();
                        break;*/
                }
            } else if(AppManager.instance.user.isProvider()){
                switch(position1){
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
                        activity.startActivity(new Intent(activity, ProviderServices.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;
                    case 5:
                        //TO DO LOGOUT, End user session in the AppManager
                        /*
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure to logout?");
                        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                            activity.finishAffinity();
                            AppManager.instance.setUser(null);
                            SharedPreferences preferences = activity.getSharedPreferences("MyPrefs", activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();
                            activity.startActivity(new Intent(activity, LoginActivity.class));
                            // System.exit(0);
                        });
                        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
                        //show dialog
                        builder.show();
                        break;*/

                        // BEGIN FIXER CUSTOM LOGOUT DIALOG BOX
                        Button cancelButton = customDialogView.findViewById(R.id.dialog_cancel_button);
                        cancelButton.setOnClickListener((View.OnClickListener) v -> customDialog.dismiss());

                        Button yesButton = customDialogView.findViewById(R.id.dialog_yes_button);
                        yesButton.setOnClickListener((View.OnClickListener) v -> {
                            activity.finishAffinity();
                            AppManager.instance.setUser(null);
                            SharedPreferences preferences = activity.getSharedPreferences("MyPrefs", activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();
                            activity.startActivity(new Intent(activity, LoginActivity.class));
                            customDialog.dismiss();
                        });
                        customDialog.show();
                        // END
                        break;
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

