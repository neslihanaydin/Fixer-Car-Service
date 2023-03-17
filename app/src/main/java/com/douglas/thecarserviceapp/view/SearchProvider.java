package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.SearchProviderAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.User;

import java.util.List;

public class SearchProvider extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    SearchProviderAdapter searchProviderAdapter;
    User user;
    List<User> providers;
    EditText edSearchByCity;
    ImageView btnSearchByCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_provider);
        loadUI();
        createEvents();
    }

    protected void loadUI() {
        FixerToolbar.setToolbar(this, "Search Service Provider", true, false);
        edSearchByCity = findViewById(R.id.edTxtSearchByCity);
        btnSearchByCity = findViewById(R.id.btnSearchByCity);
        recyclerView = findViewById(R.id.recycler_view_provider_list);
        dbHelper = new DatabaseHelper(getApplicationContext());
        user = AppManager.instance.user;
    }

    protected void createEvents() {
        btnSearchByCity.setOnClickListener(v -> {
            if (user != null) {
                String city = edSearchByCity.getText().toString();
                providers = dbHelper.getProvidersByCity(city);
                if (providers.isEmpty()) {
                    Toast.makeText(this, "0 Results", Toast.LENGTH_SHORT).show();
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                    recyclerView.setAdapter(new SearchProviderAdapter(providers));
                }
            }
        });
    }
}