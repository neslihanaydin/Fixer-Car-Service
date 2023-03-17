package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.model.User;

import java.util.ArrayList;

public class BookAppointment extends AppCompatActivity {
    //variable
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;
    static ArrayList<String> arrayList = new ArrayList<>();
    MainAdapter adapter;
    private User user;

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        FixerToolbar.setToolbar(this, "Book an Appointment", false, true);

        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.menu_icon);
        recyclerView = findViewById(R.id.recycler_view);

        user = AppManager.instance.user;
        arrayList.clear();
        try{
            if(user != null) {
                if(user.isCustomer()){
                    arrayList.add("Book an Appointment");
                    arrayList.add("Search Service Provider");
                    arrayList.add("View Appointments");
                    arrayList.add("Service History");
                    arrayList.add("Profile");
                    arrayList.add("Logout");
                } else if (user.isProvider()) {
                    arrayList.add("View Appointments");
                    arrayList.add("Create Customer");
                    arrayList.add("Search Customers");
                    arrayList.add("Service History");
                    arrayList.add("Logout");
                } else{
                   throw new Exception(new NullPointerException());
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        adapter = new MainAdapter(this, arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}