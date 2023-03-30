package com.douglas.thecarserviceapp.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;

public class PopupActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    int serviceId = 0;
    String serviceCost = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout
        setContentView(R.layout.activity_popup);

        EditText editTextService = findViewById(R.id.editTextService);
        EditText editTextCost = findViewById(R.id.editTextCost);

        Intent intent = getIntent();
        if(intent != null){
            serviceId = intent.getIntExtra("SERVICE_ID",0);
            String serviceType = intent.getStringExtra("SERVICE_TYPE");
            serviceCost = intent.getStringExtra("SERVICE_COST");
            editTextService.setText(serviceType);
            editTextService.setEnabled(false);
            editTextCost.setText(serviceCost);
        }

        Button btnPopup = findViewById(R.id.btnServices);
        btnPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DatabaseHelper(getApplicationContext());
                if(TextUtils.isEmpty(editTextCost.getText())){
                    editTextCost.setError("!");
                }
                else{
                    long result = dbHelper.updateServiceCost(serviceId,editTextCost.getText().toString());
                    if (result < 0){
                        Toast.makeText(PopupActivity.this, "Failed to update service cost", Toast.LENGTH_SHORT).show();
                    }else{
                        finish();
                    }
                }
            }
        });

        Button btnRemoveService = findViewById(R.id.btnRemoveService);
        btnRemoveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PopupActivity.this);
                builder.setMessage("Are you sure you want to remove your service ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper = new DatabaseHelper(getApplicationContext());
                        dbHelper.removeServiceForProvider(serviceId);
                        finish();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
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
