package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.adapter.SearchUserAdapter;
import com.douglas.thecarserviceapp.adapter.ServiceDetailsAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.Service;
import com.douglas.thecarserviceapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class ProviderServices extends AppCompatActivity implements ServiceDetailsAdapter.ItemClickListener {

    //Variables For Navigation Layout
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;

    //Variables for adapter and recycler view
    ServiceDetailsAdapter serviceDetailsAdapter;
    RecyclerView recyclerServiceDetails;
    List<Service> serviceList;

    //General logged user
    User user;

    //Database Helper
    DatabaseHelper dbHelper;
    Button btnAddNewService;
    @Override
    protected void onResume() {
        super.onResume();
        serviceList = dbHelper.getServicesByProviderId(user.getUserId());
        serviceDetailsAdapter = new ServiceDetailsAdapter(this, serviceList, this);
        recyclerServiceDetails.setAdapter(serviceDetailsAdapter);
        checkProvidedServices();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_services);
        loadUI();
        checkProvidedServices();

    }

    private void checkProvidedServices(){
        List<String> unprovidedServices = dbHelper.getUnprovidedServicesForProvider(AppManager.instance.user.getUserId());
        if (unprovidedServices.size() == 0){
            unprovidedServices.add("Services");
            btnAddNewService.setBackgroundResource(R.drawable.rounded_background_gray_button);
            btnAddNewService.setTextColor(getResources().getColor(R.color.fixer_white));
            btnAddNewService.setEnabled(false);
        } else {
            btnAddNewService.setBackgroundResource(R.drawable.button_enabled);
            btnAddNewService.setTextColor(getResources().getColor(R.color.fixer_dark_blue));
            btnAddNewService.setEnabled(true);
        }
    }
    private void loadUI(){
        user = AppManager.instance.user;
        if(user != null){
            loadNavigationToolBar();
            dbHelper = new DatabaseHelper(getApplicationContext());
            recyclerServiceDetails = findViewById(R.id.recyclerServiceDetails);
            recyclerServiceDetails.setLayoutManager(new GridLayoutManager(this, 1));
            serviceList = dbHelper.getServicesByProviderId(user.getUserId());
            serviceDetailsAdapter = new ServiceDetailsAdapter(this,serviceList,this);
            recyclerServiceDetails.setAdapter(serviceDetailsAdapter);
            btnAddNewService = findViewById(R.id.btnServices);
            btnAddNewService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ProviderServices.this, PopupAddActivity.class));
                }
            });

        }

    }

    private void loadNavigationToolBar(){
        //Set the toolbar
        FixerToolbar.setToolbar(this, "Service Details", true,true);

        //Navigation drawer menu
        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.menu_icon);
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //Navigation items
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainAdapter(this, NavigationActivity.items));

    }
    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        NavigationActivity.closeDrawer(drawerLayout);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(ProviderServices.this, PopupActivity.class);
        intent.putExtra("SERVICE_ID",serviceList.get(position).getServiceId());
        intent.putExtra("SERVICE_TYPE",serviceList.get(position).getType());
        intent.putExtra("SERVICE_COST", String.valueOf(serviceList.get(position).getCost()));
        startActivity(intent);
    }



}