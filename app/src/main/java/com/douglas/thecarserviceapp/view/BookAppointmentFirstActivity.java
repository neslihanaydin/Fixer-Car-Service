package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.BookAppointmentFirstAdapter;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.Appointment;
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
    List<Appointment> upcomingAppointments;
    TextView currentUser, toastText;

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

        if(user!=null) {
            currentUser = findViewById(R.id.txtCurrentUserName);
            currentUser.setText(user.getFirstName() + " " + user.getLastName());
            dbh = new DatabaseHelper(getApplicationContext());
            try{
                provider = dbh.getFavouriteProviders(user.getUserId());
                upcomingAppointments = dbh.getUpcomingAppointmentForCustomer(user.getUserId());
                if (upcomingAppointments.size() > 0) {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.fixer_toast, findViewById(R.id.custom_toast_layout));
                    toastText = layout.findViewById(R.id.text_toast);
                    toastText.setText("You have " + upcomingAppointments.size() + " upcoming appointments");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            RecyclerView recyclerviewFavProvider = findViewById(R.id.recyclerViewFavoriteProvider);
            recyclerviewFavProvider.setLayoutManager(new GridLayoutManager(this, 1));
            bookAppointmentFirstAdapter = new BookAppointmentFirstAdapter(this, provider, this);
            recyclerviewFavProvider.setAdapter(bookAppointmentFirstAdapter);

            Button buttonSearch;
            buttonSearch = findViewById(R.id.btnSearchProvider);
            buttonSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(BookAppointmentFirstActivity.this, SearchProvider.class));
                }
            });
        }
    }

    //Implement when customer click FavItems.
    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "You choose a provider", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(BookAppointmentFirstActivity.this, BookAppointment.class);
        intent.putExtra("PROVIDER_ID",provider.get(position).getUserId());
        startActivity(intent);
    }

    //relate with navigation bar
    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        NavigationActivity.closeDrawer(drawerLayout);
    }
}