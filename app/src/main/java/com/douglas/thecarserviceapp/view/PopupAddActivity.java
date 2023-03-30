package com.douglas.thecarserviceapp.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.Service;

import java.util.ArrayList;
import java.util.List;

public class PopupAddActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    int serviceId = 0;
    String serviceCost = "";
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout
        setContentView(R.layout.activity_popup_add);

        spinner = findViewById(R.id.spinnerServices);
        EditText editTextCost = findViewById(R.id.editTextCostP);
        dbHelper = new DatabaseHelper(getApplicationContext());
        Button btnAddService = findViewById(R.id.btnAddService);


        List<String> unprovidedServices = dbHelper.getUnprovidedServicesForProvider(AppManager.instance.user.getUserId());
        if (unprovidedServices.size() == 0){
            unprovidedServices.add("Services");
            editTextCost.setEnabled(false);
            btnAddService.setEnabled(false);

        }
        // Spinner adapter with arraylist
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unprovidedServices);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editTextCost.getText())){
                    editTextCost.setError("!");
                }
                else{
                    String serviceType = spinner.getSelectedItem().toString();
                    long result = dbHelper.addServiceToProvider(serviceType,editTextCost.getText().toString(), AppManager.instance.user.getUserId());
                    if (result < 0){
                        System.out.println("New service added to the provider");
                    }else{
                        System.out.println("ERROR: SQLite on adding new service to the provider.");
                        finish();
                    }
                }
            }
        });

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Close the popup if user touch out of the screen
        finish();
        return true;
    }
}
