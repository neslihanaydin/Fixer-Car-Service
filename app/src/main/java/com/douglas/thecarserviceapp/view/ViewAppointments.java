package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.adapter.ServiceDetailsAdapter;
import com.douglas.thecarserviceapp.adapter.ViewAppointmentsCustomerAdapter;
import com.douglas.thecarserviceapp.adapter.ViewAppointmentsProviderAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.Appointment;
import com.douglas.thecarserviceapp.model.User;

import java.text.ParseException;
import java.util.List;

public class ViewAppointments extends AppCompatActivity implements ViewAppointmentsProviderAdapter.ItemClickListener, ViewAppointmentsCustomerAdapter.ItemClickListener{
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;
    ViewAppointmentsProviderAdapter viewAppointmentsProviderAdapter;
    ViewAppointmentsCustomerAdapter viewAppointmentsCustomerAdapter;
    User user;
    DatabaseHelper dbHelper;
    List<Appointment> appointments;
    TextView currentUser, toastText;
    RecyclerView recyclerViewApp;

    @Override
    protected void onResume() {
        super.onResume();
        if (user.isProvider()) {
            try {
                appointments = dbHelper.getUpcomingAppointmentForProvider(user.getUserId());
                viewAppointmentsProviderAdapter = new ViewAppointmentsProviderAdapter(this, appointments, this);
                recyclerViewApp.setAdapter(viewAppointmentsProviderAdapter);
            } catch (Exception e) {

            }
        } else {
            try {
                appointments = dbHelper.getAllAppointmentsForCustomer(user.getUserId());
                viewAppointmentsCustomerAdapter = new ViewAppointmentsCustomerAdapter(this, appointments, this);
                recyclerViewApp.setAdapter(viewAppointmentsCustomerAdapter);
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.menu_icon);
        recyclerView = findViewById(R.id.recycler_view);
        user = AppManager.instance.user;
        //Change the page header
        if(user!=null){
            if(user.isProvider()){
                FixerToolbar.setToolbar(this, "View Appointments", false, true);
            } else{
                FixerToolbar.setToolbar(this, "View Appointments", true, true);
            }
        }else{
            finish();
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainAdapter(this, NavigationActivity.items));

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        if(user!= null) {
            currentUser = findViewById(R.id.txtCurrentUserName);
            currentUser.setText(user.getFirstName() + " " + user.getLastName());
            if (user.isProvider()) {
                dbHelper = new DatabaseHelper(getApplicationContext());
                try {
                    appointments = dbHelper.getUpcomingAppointmentForProvider(user.getUserId());
                    if (appointments.size() > 0) {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.fixer_toast, findViewById(R.id.custom_toast_layout));
                        toastText = layout.findViewById(R.id.text_toast);
                        toastText.setText("You have " + appointments.size() + " upcoming appointments");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                recyclerViewApp = findViewById(R.id.recyclerViewAppointments);
                recyclerViewApp.setLayoutManager(new GridLayoutManager(this, 1));
                viewAppointmentsProviderAdapter = new ViewAppointmentsProviderAdapter(this, appointments, this);
                recyclerViewApp.setAdapter(viewAppointmentsProviderAdapter);
            } else if (user.isCustomer()) {
                dbHelper = new DatabaseHelper(getApplicationContext());
                try {
                    appointments = dbHelper.getUpcomingAppointmentForCustomer(user.getUserId());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                RecyclerView recyclerViewApp = findViewById(R.id.recyclerViewAppointments);
                recyclerViewApp.setLayoutManager(new GridLayoutManager(this, 1));
                viewAppointmentsCustomerAdapter = new ViewAppointmentsCustomerAdapter(this, appointments, this);
                recyclerViewApp.setAdapter(viewAppointmentsCustomerAdapter);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        NavigationActivity.closeDrawer(drawerLayout);
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this,"You clicked an appointment: " + (position + 1),Toast.LENGTH_LONG).show();

        if(user != null){
            Intent intent = new Intent(ViewAppointments.this, AppointmentDetailServiceProvider.class);
            intent.putExtra("APPOINTMENT_ID",appointments.get(position).getAppointmentId());
            intent.putExtra("CUSTOMER_ID",appointments.get(position).getUserId());
            intent.putExtra("DATE", appointments.get(position).getDateTime());
            intent.putExtra("CUSTOMER", dbHelper.getUserName(appointments.get(position).getUserId()));
            intent.putExtra("CUSTOMER_ADDRESS",dbHelper.getUserAddress(appointments.get(position).getUserId()));
            intent.putExtra("SERVICES",dbHelper.getServiceType(appointments.get(position).getServiceId()));
            intent.putExtra("TYPE", appointments.get(position).getType());
            intent.putExtra("COMMENTS",appointments.get(position).getComments());
            intent.putExtra("STATUS", appointments.get(position).getStatus());
            startActivity(intent);
        }
    }

    @Override
    public void onItemClickCustomer(View view, int position) {

        String appointmentDate = appointments.get(position).getDateTime();
        String providerName = dbHelper.getUserName(appointments.get(position).getProviderId());

        // BEGIN FIXER CUSTOM LOGOUT DIALOG BOX
        LayoutInflater inflater = LayoutInflater.from(this);
        View customDialogView = inflater.inflate(R.layout.fixer_dialog_logout, null);
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customDialogView);
        Dialog customDialog = builder.create();
        customDialog.setContentView(customDialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(customDialog.getWindow().getAttributes());
        int width = (int) (this.getResources().getDisplayMetrics().widthPixels * 0.8);
        lp.width = width;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        customDialog.getWindow().setAttributes(lp);
        customDialog.getWindow().setBackgroundDrawable(null);

        TextView title = customDialogView.findViewById(R.id.dialog_title);
        title.setText("Cancel Appointment");

        TextView message = customDialogView.findViewById(R.id.dialog_message);
        message.setText("Are you sure you want to cancel the appointment with " + providerName + " on " + appointmentDate + "?");

        Button cancelButton = customDialogView.findViewById(R.id.dialog_cancel_button);
        cancelButton.setOnClickListener((View.OnClickListener) l -> customDialog.dismiss());

        Button yesButton = customDialogView.findViewById(R.id.dialog_yes_button);
        yesButton.setOnClickListener((View.OnClickListener) l -> {
            dbHelper = new DatabaseHelper(getApplicationContext());
            dbHelper.cancelAppointment(appointments.get(position)); //cancel appointment
            recreate(); //Start activity again
            customDialog.dismiss();
        });
        customDialog.show();

        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to cancel your appointment with " + providerName + " on " + appointmentDate + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper = new DatabaseHelper(getApplicationContext());
                dbHelper.cancelAppointment(appointments.get(position)); //cancel appointment
                recreate(); //Start activity again
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();*/
    }
}