package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.adapter.ViewAppointmentsCustomerAdapter;
import com.douglas.thecarserviceapp.adapter.ViewAppointmentsProviderAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.Appointment;
import com.douglas.thecarserviceapp.model.User;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.util.List;

public class ViewAppointments extends AppCompatActivity implements  ViewAppointmentsProviderAdapter.ItemClickListener, ViewAppointmentsCustomerAdapter.ItemClickListener{
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;
    ViewAppointmentsProviderAdapter viewAppointmentsProviderAdapter;
    ViewAppointmentsCustomerAdapter viewAppointmentsCustomerAdapter;
    User user;
    DatabaseHelper dbHelper;


    //Test THEY SHOULD BE DONE WITH DB LATER
    Appointment app1 = new Appointment(1,1,4,4, Date.valueOf("2023-03-20"), Time.valueOf("10:30:00"),"Drop off","Use only Shell Plus oil for the engine");
    List<Appointment> appointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.menu_icon);
        recyclerView = findViewById(R.id.recycler_view);
        user = AppManager.instance.user;
        //Change the page header
        FixerToolbar.setToolbar(this, "View Appointments", true, true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainAdapter(this, BookAnAppointment.arrayList));

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        if(user!= null) {
            if (user.isProvider()) {
                dbHelper = new DatabaseHelper(getApplicationContext());
                //Get provider's appointments // TO DO: Check date later, and edit that only list upcoming appointments
                try {
                    appointments = dbHelper.getAllAppointmentsForProvider(user.getUserId());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                RecyclerView recyclerViewApp = findViewById(R.id.recyclerViewAppointments);
                recyclerViewApp.setLayoutManager(new GridLayoutManager(this, 1));
                viewAppointmentsProviderAdapter = new ViewAppointmentsProviderAdapter(this, appointments, this);
                recyclerViewApp.setAdapter(viewAppointmentsProviderAdapter);
            } else if (user.isCustomer()) {
                dbHelper = new DatabaseHelper(getApplicationContext());
                try {
                    appointments = dbHelper.getAllAppointmentsForCustomer(user.getUserId());
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
        BookAnAppointment.closeDrawer(drawerLayout);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this,"You clicked an appointment: " + (position + 1),Toast.LENGTH_LONG).show();

        if(user != null){
            Intent intent = new Intent(ViewAppointments.this, AppointmentDetailServiceProvider.class);
            intent.putExtra("DATE", appointments.get(position).getDateTime());
            intent.putExtra("CUSTOMER", dbHelper.getUserName(appointments.get(position).getUserId())); // TO DO: GET IT FROM DB
            intent.putExtra("CUSTOMER_ADDRESS",dbHelper.getUserAddress(appointments.get(position).getUserId()));
            intent.putExtra("SERVICES",dbHelper.getServiceType(appointments.get(position).getServiceId()));
            intent.putExtra("TYPE", appointments.get(position).getType());
            intent.putExtra("COMMENTS",appointments.get(position).getComments());
            startActivity(intent);
        }
    }

    @Override
    public void onItemClickCustomer(View view, int position) {

        String appointmentDate = appointments.get(position).getDateTime();
        String providerName = dbHelper.getUserName(appointments.get(position).getProviderId());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to cancel your appointment with " + providerName + " on " + appointmentDate + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update Appointment status as cancelled
                // refresh the page
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
        alert.show();
    }
}