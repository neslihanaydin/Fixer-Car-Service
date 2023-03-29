package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.adapter.ServiceHistoryCustomerAdapter;
import com.douglas.thecarserviceapp.adapter.ServiceHistoryProviderAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.Appointment;
import com.douglas.thecarserviceapp.model.User;

import java.text.ParseException;
import java.util.List;

public class ServiceHistory extends AppCompatActivity implements ServiceHistoryCustomerAdapter.ItemClickListener, ServiceHistoryProviderAdapter.ItemClickListener {
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;
    ServiceHistoryProviderAdapter serviceHistoryProviderAdapter;
    ServiceHistoryCustomerAdapter serviceHistoryCustomerAdapter;
    User user;
    DatabaseHelper dbHelper;
    List<Appointment> appointments;
    TextView currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_history);

        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.menu_icon);
        recyclerView = findViewById(R.id.recycler_view);
        user = AppManager.instance.user;
        //Change the page header
        FixerToolbar.setToolbar(this, "Service History", true, true);

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
                    appointments = dbHelper.getServiceHistoryForProvider(user.getUserId());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                RecyclerView recyclerViewApp = findViewById(R.id.recyclerServiceHistory);
                recyclerViewApp.setLayoutManager(new GridLayoutManager(this, 1));
                serviceHistoryProviderAdapter = new ServiceHistoryProviderAdapter(this, appointments, this);
                recyclerViewApp.setAdapter(serviceHistoryProviderAdapter);
            } else if (user.isCustomer()) {
                dbHelper = new DatabaseHelper(getApplicationContext());
                try {
                    appointments = dbHelper.getServiceHistoryForUser(user.getUserId());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                RecyclerView recyclerViewApp = findViewById(R.id.recyclerServiceHistory);
                recyclerViewApp.setLayoutManager(new GridLayoutManager(this, 1));
                serviceHistoryCustomerAdapter = new ServiceHistoryCustomerAdapter(this, appointments, this);
                recyclerViewApp.setAdapter(serviceHistoryCustomerAdapter);
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
    }

    @Override
    public void onItemClickCustomer(View view, int position) {
    }
}