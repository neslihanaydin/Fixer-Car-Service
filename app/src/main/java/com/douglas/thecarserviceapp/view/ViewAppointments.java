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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.adapter.ViewAppointmentsAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.model.Appointment;
import com.douglas.thecarserviceapp.model.AppointmentWidget;
import com.douglas.thecarserviceapp.model.User;

import java.sql.Date;
import java.sql.Time;

public class ViewAppointments extends AppCompatActivity implements  ViewAppointmentsAdapter.ItemClickListener{
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;
    ViewAppointmentsAdapter viewAppointmentsAdapter;
    User user;

    //Test THEY SHOULD BE DONE WITH DB LATER
    Appointment app1 = new Appointment(1,2,1,1000, Date.valueOf("2023-03-20"), Time.valueOf("10:30:00"));
    AppointmentWidget apw= new AppointmentWidget(R.drawable.directions_car,app1.getDate().toString() + " " + app1.getTime().toString(),"Jane Smith","Pick up",R.drawable.icon_arrow_circle_right);
    AppointmentWidget[] appointmentWidgets = {apw,apw,apw};

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

        RecyclerView recyclerViewApp = findViewById(R.id.recyclerViewAppointments);
        recyclerViewApp.setLayoutManager(new GridLayoutManager(this,1));
        viewAppointmentsAdapter = new ViewAppointmentsAdapter(this, appointmentWidgets, this);
        recyclerViewApp.setAdapter(viewAppointmentsAdapter);

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
            switch(position){
                case 0:
                    Intent intent = new Intent(ViewAppointments.this, AppointmentDetailServiceProvider.class);
                    //TO DO this information should be done by database
                    intent.putExtra("DATE", app1.getDate().toString());
                    intent.putExtra("CUSTOMER", apw.getCustomerName()); // TO DO: GET IT FROM DB
                    intent.putExtra("CUSTOMER_ADDRESS","+1 236 152 6035\nSurrey, BC, V2T 1D5");
                    intent.putExtra("SERVICES","Oil change");
                    intent.putExtra("TYPE", apw.getDropOrPick());
                    intent.putExtra("COMMENTS","Use only Shell Plus");
                    startActivity(intent);
                    break;
            }
        }







    }
}