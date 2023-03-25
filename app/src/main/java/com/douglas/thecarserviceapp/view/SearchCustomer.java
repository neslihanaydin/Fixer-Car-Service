package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.adapter.SearchUserAdapter;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class SearchCustomer extends AppCompatActivity implements SearchUserAdapter.ItemClickListener{

    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;
    SearchUserAdapter searchUserAdapter;
    DatabaseHelper dbHelper;
    private List<User> userList;
    private SearchView searchView;
    RecyclerView recyclerSearchCustomer;
    List<User> filteredList = new ArrayList<>();


    @Override
    protected void onResume() {
        super.onResume();
        searchUserAdapter = new SearchUserAdapter(this, new ArrayList<User>(), this);
        recyclerSearchCustomer.setAdapter(searchUserAdapter);
        searchView.setQuery("", false);
        searchView.clearFocus();
        userList.clear();
        userList = dbHelper.getAllCustomers();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer);
        FixerToolbar.setToolbar(this, "Search Customers", true,true);
        //Drawer menu
        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.menu_icon);
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Change the page header
        // TO DO:when the buttonMenu is true, clicking is not working


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainAdapter(this, NavigationActivity.items));

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        EditText txtSearch = ((EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        txtSearch.setHint(getResources().getString(R.string.searchCustomer));
        txtSearch.setHintTextColor(getResources().getColor(R.color.fixer_gray));
        txtSearch.setTextColor(getResources().getColor(R.color.fixer_dark_blue));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        dbHelper = new DatabaseHelper(getApplicationContext());
        userList = dbHelper.getAllCustomers();
        recyclerSearchCustomer = findViewById(R.id.recyclerSearchCustomer);
        recyclerSearchCustomer.setLayoutManager(new LinearLayoutManager(this));
        searchUserAdapter = new SearchUserAdapter(this, new ArrayList<User>(), this);
        recyclerSearchCustomer.setAdapter(searchUserAdapter);

    }

    private void filterList(String text) {
        filteredList.clear();
        if(text.equals("")){
            searchUserAdapter.setFilteredList(filteredList);
        } else {
            for(User user : userList){
                if(user.getFirstName().toLowerCase().contains(text.toLowerCase())){
                    filteredList.add(user);
                }
            }
            if(!filteredList.isEmpty()){
                searchUserAdapter.setFilteredList(filteredList);
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
        if(filteredList.size() >= position && filteredList.size() != 0){
            Intent intent = new Intent(SearchCustomer.this, CustomerDetail.class);
            intent.putExtra("USER_ID",filteredList.get(position).getUserId());
            startActivity(intent);
        }

    }
}