package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.douglas.thecarserviceapp.R;

public class CreateAppointment extends AppCompatActivity {
    TextView txtProviderName, txtProviderPhone, txtProviderAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);
        FixerToolbar.setToolbar(this, true, false);
        txtProviderName = findViewById(R.id.txtProviderName);
        txtProviderPhone = findViewById(R.id.txtProviderPhone);
        txtProviderAddress = findViewById(R.id.txtProviderAddress);
        Intent intent = getIntent();
        txtProviderName.setText(intent.getStringExtra("PROVIDER_FNAME") + " " + intent.getStringExtra("PROVIDER_LNAME"));
        txtProviderPhone.setText(intent.getStringExtra("PROVIDER_PHONE"));
        txtProviderAddress.setText(intent.getStringExtra("PROVIDER_ADDRESS"));
    }
}