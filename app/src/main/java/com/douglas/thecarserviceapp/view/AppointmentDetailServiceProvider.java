package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.douglas.thecarserviceapp.R;

public class AppointmentDetailServiceProvider extends AppCompatActivity {

    TextView textViewDate, textViewCusName, textViewCusAdd, textViewServices, textViewType,
            textViewComment;
    TextView titleTextView;
    ImageView titleBarImage;

    Button buttonSave, buttonCancelAp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail_service_provider);

        titleBarImage = findViewById(R.id.title_bar_image);
        titleTextView= findViewById(R.id.title_bar_text);
        titleTextView.setText("View Appointments");
        titleBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonSave = findViewById(R.id.btnSave);
        buttonCancelAp = findViewById(R.id.btnCancelAppointment);
        textViewDate = findViewById(R.id.textViewDate);
        textViewCusName = findViewById(R.id.textViewCusName);
        textViewCusAdd = findViewById(R.id.textViewCusAdd);
        textViewServices = findViewById(R.id.textViewServices);
        textViewType = findViewById(R.id.textViewType);
        textViewComment = findViewById(R.id.textViewComment);

        Intent intent = getIntent();
        if(intent!=null){
            String aDate = intent.getStringExtra("DATE");
            String aCustomer = intent.getStringExtra("CUSTOMER");
            String aCusAdd = intent.getStringExtra("CUSTOMER_ADDRESS");
            String aServices = intent.getStringExtra("SERVICES");
            String aType = intent.getStringExtra("TYPE");
            String aComments = intent.getStringExtra("COMMENTS");
            String msg = "Customer information is:\n" +
                        aDate + aCustomer + aCusAdd + aServices + "\n" +
                        aType + aComments;
            System.out.println(msg);
            textViewDate.setText(aDate);
            textViewCusName.setText(aCustomer);
            textViewCusAdd.setText(aCusAdd);
            textViewServices.setText(aServices);
            textViewType.setText(aType);
            textViewComment.setText(aComments);
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update appointment informations in db
                //Then go back to the View Appointments screen
            }
        });

        buttonCancelAp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show Pop up which asks "are you sure?"
                //if yes then update appointment's status as "cancelled"
                //never delete a record
            }
        });



    }
}