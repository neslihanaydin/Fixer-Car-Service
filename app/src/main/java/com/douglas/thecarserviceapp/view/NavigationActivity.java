package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.model.User;
import com.douglas.thecarserviceapp.util.ItemDrawer;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity {
    //variable
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;
    static ArrayList<ItemDrawer> items = new ArrayList<>();
    MainAdapter adapter;
    private User user;
    TextView currentUser;

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.menu_icon);
        recyclerView = findViewById(R.id.recycler_view);
        currentUser = findViewById(R.id.txtCurrentUserName);

        user = AppManager.instance.user;
        items.clear();
        try{
            if(user != null) {
                currentUser.setText(user.getFirstName() + " " + user.getLastName());
                if(user.isCustomer()){
                    items.add(new ItemDrawer(R.drawable.icon_add, "Book an Appointment"));
                    items.add(new ItemDrawer(R.drawable.icon_search, "Search Service Provider"));
                    items.add(new ItemDrawer(R.drawable.icon_calendar, "View Appointments"));
                    items.add(new ItemDrawer(R.drawable.icon_history, "Service History"));
                    items.add(new ItemDrawer(R.drawable.icon_user, "Profile"));
                    items.add(new ItemDrawer(R.drawable.icon_logout, "Logout"));
                } else if (user.isProvider()) {
                    items.add(new ItemDrawer(R.drawable.icon_calendar, "View Appointments"));
                    items.add(new ItemDrawer(R.drawable.icon_add, "Create Customer"));
                    items.add(new ItemDrawer(R.drawable.icon_search, "Search Customers"));
                    items.add(new ItemDrawer(R.drawable.icon_history, "Service History"));
                    items.add(new ItemDrawer(R.drawable.icon_logout, "Logout"));
                } else{
                   throw new Exception(new NullPointerException());
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        adapter = new MainAdapter(this, items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        if(user != null){
            if(user.isCustomer()){
                startActivity(new Intent(NavigationActivity.this, BookAppointmentFirstActivity.class));
            } else if(user.isProvider()){
                startActivity(new Intent(NavigationActivity.this, ViewAppointments.class));
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}