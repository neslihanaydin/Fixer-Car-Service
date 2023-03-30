package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;

public class AppointmentDetailServiceProvider extends AppCompatActivity {

    TextView textViewDate, textViewCusName, textViewCusAdd, textViewServices, textViewType,
            textViewComment;
    ImageView imgBtnEdit;

    Button buttonCancelAp;
    int customerId;
    int appointmentId;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail_service_provider);

        //Change the page header
        FixerToolbar.setToolbar(this, "Appointment Details", true, false);

        buttonCancelAp = findViewById(R.id.btnCancelAppo);
        textViewDate = findViewById(R.id.textViewDate);
        textViewCusName = findViewById(R.id.textViewCusName);
        textViewCusAdd = findViewById(R.id.textViewCusAdd);
        textViewServices = findViewById(R.id.textViewServices);
        textViewType = findViewById(R.id.textViewType);
        textViewComment = findViewById(R.id.textViewComment);
        imgBtnEdit = findViewById(R.id.ImgBtnEdit);

        Intent intent = getIntent();
        if(intent!=null){
            appointmentId = intent.getIntExtra("APPOINTMENT_ID",0);
            customerId = intent.getIntExtra("CUSTOMER_ID",0);
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

        buttonCancelAp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DatabaseHelper(getApplicationContext());
                dbHelper.cancelAppointment(appointmentId);
                startActivity(new Intent(AppointmentDetailServiceProvider.this, ViewAppointments.class));
                finish();
            }
        });

        imgBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AppointmentDetailServiceProvider.this, BookAppointment.class);
                intent1.putExtra("CUSTOMER_ID",customerId );
                intent1.putExtra("APPOINTMENT_ID",appointmentId);
                startActivity(intent1);
            }
        });



    }
}