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
import android.widget.Toast;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.BookAppointmentFirstAdapter;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.User;

import java.util.List;

public class BookAppointmentFirstActivity extends AppCompatActivity implements BookAppointmentFirstAdapter.ItemClickListener{
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;
    BookAppointmentFirstAdapter bookAppointmentFirstAdapter;
    User user;
    DatabaseHelper dbh;
    List<User> provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment_first);

        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.menu_icon);
        recyclerView = findViewById(R.id.recycler_view);
        //When user login this will invoke favorite provider from database
        user = AppManager.instance.user;
        //Set toolbar name and visible menu
        FixerToolbar.setToolbar(this, "Book an Appointment", false, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainAdapter(this, NavigationActivity.items));
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        if(user!=null){
            dbh = new DatabaseHelper(getApplicationContext());
            try{
               provider = dbh.getFavouriteProviders(user.getUserId());
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            RecyclerView recyclerviewFavProvider = findViewById(R.id.recyclerViewFavoriteProvider);
            recyclerviewFavProvider.setLayoutManager(new GridLayoutManager(this, 1));
            bookAppointmentFirstAdapter = new BookAppointmentFirstAdapter(this, provider, this);
            recyclerviewFavProvider.setAdapter(bookAppointmentFirstAdapter);
        }
    }

    //Implememt when customer click FavItems.
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You choose a provider", Toast.LENGTH_SHORT).show();
    }

    //relate with navigation bar
    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        NavigationActivity.closeDrawer(drawerLayout);
    }
}