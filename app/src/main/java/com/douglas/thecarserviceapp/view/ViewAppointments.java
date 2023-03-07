package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.adapter.ViewAppointmentsAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.Appointment;
import com.douglas.thecarserviceapp.model.User;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class ViewAppointments extends AppCompatActivity implements  ViewAppointmentsAdapter.ItemClickListener{
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;
    ViewAppointmentsAdapter viewAppointmentsAdapter;
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
        btMenu = findViewById(R.id.bt_menu);
        recyclerView = findViewById(R.id.recycler_view);
        user = AppManager.instance.user;
        //Change the page header
        TextView headerTextView = findViewById(R.id.toolBarTextView);
        headerTextView.setText("View Appointments");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainAdapter(this, BookAnAppointment.arrayList));

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        if(user!= null && user.isProvider()){
            dbHelper =  new DatabaseHelper(getApplicationContext());
            //Get provider's appointments // TO DO: Check date later, and edit that only list upcoming appointments
            appointments = dbHelper.getAllAppointmentsForProvider(user.getUserId());
            RecyclerView recyclerViewApp = findViewById(R.id.recyclerViewAppointments);
            recyclerViewApp.setLayoutManager(new GridLayoutManager(this,1));
            viewAppointmentsAdapter = new ViewAppointmentsAdapter(this, appointments, this);
            recyclerViewApp.setAdapter(viewAppointmentsAdapter);
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
}