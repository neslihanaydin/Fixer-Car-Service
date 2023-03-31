package com.douglas.thecarserviceapp.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.app.AppManager;
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
                Context activity = PopupActivity.this;

                // BEGIN FIXER CUSTOM LOGOUT DIALOG BOX
                LayoutInflater inflater = LayoutInflater.from(activity);
                View customDialogView = inflater.inflate(R.layout.fixer_dialog_logout, null);
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setView(customDialogView);
                Dialog customDialog = builder.create();
                customDialog.setContentView(customDialogView);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(customDialog.getWindow().getAttributes());
                int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.8);
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                customDialog.getWindow().setAttributes(lp);
                customDialog.getWindow().setBackgroundDrawable(null);

                TextView title = customDialogView.findViewById(R.id.dialog_title);
                title.setText("Remove Service");

                TextView message = customDialogView.findViewById(R.id.dialog_message);
                message.setText("Are you sure you want to remove the service?");

                Button cancelButton = customDialogView.findViewById(R.id.dialog_cancel_button);
                cancelButton.setOnClickListener((View.OnClickListener) l -> customDialog.dismiss());

                Button yesButton = customDialogView.findViewById(R.id.dialog_yes_button);
                yesButton.setOnClickListener((View.OnClickListener) l -> {
                    dbHelper = new DatabaseHelper(getApplicationContext());
                    dbHelper.removeServiceForProvider(serviceId);
                    finish();
                    customDialog.dismiss();
                });
                customDialog.show();

                /*
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
                alert.show();*/
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
