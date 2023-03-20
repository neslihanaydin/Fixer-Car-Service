package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.SearchProviderAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class SearchProvider extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    SearchProviderAdapter searchProviderAdapter;
    User user;
    List<User> providers, filteredProviders = new ArrayList<>();
    SearchView searchView;
    @Override
    protected void onResume() {
        super.onResume();
        searchProviderAdapter = new SearchProviderAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(searchProviderAdapter);
        searchView.setQuery("", false);
        searchView.clearFocus();
        providers.clear();
        providers = dbHelper.getAllProviders();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_provider);
        loadUI();
    }

    protected void loadUI() {
        FixerToolbar.setToolbar(this, "Search Service Provider", true, false);
        recyclerView = findViewById(R.id.recycler_view_provider_list);
        dbHelper = new DatabaseHelper(getApplicationContext());
        user = AppManager.instance.user;
        searchView = findViewById(R.id.searchProvider);
        searchView.clearFocus();
        EditText txtSearch = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        txtSearch.setHint(getResources().getString(R.string.searchByCity));
        txtSearch.setHintTextColor(getResources().getColor(R.color.fixer_gray));
        txtSearch.setTextColor(getResources().getColor(R.color.fixer_dark_blue));
        txtSearch.setTextSize(16);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });
        providers = dbHelper.getAllProviders();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SearchProviderAdapter(this, providers));
        searchProviderAdapter = new SearchProviderAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(searchProviderAdapter);
    }

    protected void search(String text) {
        filteredProviders.clear();
        if(text.equals("")){
            searchProviderAdapter.setFilteredList(filteredProviders);
        } else {
            for(User user : providers){
                if(user.getAddress().toLowerCase().contains(text.toLowerCase())){
                    filteredProviders.add(user);
                }
            }
            if(!filteredProviders.isEmpty()){
                searchProviderAdapter.setFilteredList(filteredProviders);
            }
        }
    }
}