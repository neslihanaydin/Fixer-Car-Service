package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.adapter.ProviderServicesAdapter;
import com.douglas.thecarserviceapp.adapter.ServiceDetailsAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.Appointment;
import com.douglas.thecarserviceapp.model.Service;
import com.douglas.thecarserviceapp.model.User;
import com.douglas.thecarserviceapp.util.Util;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookAppointment extends AppCompatActivity implements ProviderServicesAdapter.ItemClickListener {
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;
    ProviderServicesAdapter providerServicesAdapter;
    User user;
    DatabaseHelper dbHelper;
    TextView txtDate, currentUser;
    LinearLayout layoutDate;
    boolean timeSetStatus = false;
    LinearLayout linearLayoutProvider;
    LinearLayout linearLayoutCustomer;
    private List<Service> checkedServiceList = new ArrayList<>();
    public List<Service> serviceList;
    RadioButton rPickUp, rDropOff, rCancelled, rCompleted;
    EditText editTextComments;
    Button btnBookAppointment;
    LinearLayout statusOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.douglas.thecarserviceapp.R.layout.activity_book_appointment);
        dbHelper = new DatabaseHelper(getApplicationContext());
        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.menu_icon);
        recyclerView = findViewById(R.id.recycler_view);
        user = AppManager.instance.user;
        //Change the page header
        FixerToolbar.setToolbar(this, "Book an Appointment", true, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainAdapter(this, NavigationActivity.items));
        statusOptions = findViewById(R.id.statusOptions);
        linearLayoutCustomer = findViewById(R.id.linearLayoutCustomer);
        linearLayoutProvider = findViewById(R.id.linearLayoutProvider);
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        if(user!=null){
            currentUser = findViewById(R.id.txtCurrentUserName);
            currentUser.setText(user.getFirstName() + " " + user.getLastName());
            layoutDate = findViewById(R.id.layoutDatePicker);
            btnBookAppointment = findViewById(R.id.btnBookAppointment);
            txtDate = findViewById(R.id.txtDateField);
            rPickUp = findViewById(R.id.radioPickup);
            rDropOff = findViewById(R.id.radioDropOff);
            rCancelled = findViewById(R.id.radioCancelled);
            rCompleted = findViewById(R.id.radioCompleted);
            editTextComments = findViewById(R.id.editTextComments);

            if(user.isProvider()){
                int customerId = checkIntentForCustomerId();
                if(customerId != 0){
                    statusOptions.setVisibility(View.INVISIBLE);
                    loadCustomerInformation(customerId);
                    serviceList = dbHelper.getServicesByProviderId(user.getUserId());
                    int appointmentId = checkIntentForAppointmentId();
                    try {
                        Appointment appointment = dbHelper.getAppointmentByAppointmentId(appointmentId);

                        if(appointment.getType().equals("Pick up")){
                            rPickUp.setChecked(true);
                        }else {
                            rDropOff.setChecked(true);
                        }
                        String sType = dbHelper.getServiceType(appointment.getServiceId());
                        checkedServiceList.clear();
                        Service service1 = new Service(appointment.getServiceId(),sType, dbHelper.getServiceCost(appointment.getServiceId()),user.getUserId() );
                        checkedServiceList.add(service1);
                        RecyclerView recyclerViewApp = findViewById(R.id.recyclerViewServicesCosts);
                        recyclerViewApp.setLayoutManager(new GridLayoutManager(this, 1));
                        providerServicesAdapter = new ProviderServicesAdapter(this, serviceList, this, checkedServiceList);
                        recyclerViewApp.setAdapter(providerServicesAdapter);

                        String datePickerText = Util.DateTimeToStringForDatePicker(appointment.getDate(),appointment.getTime());
                       txtDate.setText(datePickerText);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    // set Listeners for the provider. First param : provider, Second param : customer
                    setOnClickListeners(user.getUserId(), customerId);
                }
            } else {
                int providerId = checkIntentForProviderId();
                if(providerId != 0){
                    statusOptions.setVisibility(View.INVISIBLE);
                    loadProviderInformation(providerId);
                    serviceList = dbHelper.getServicesByProviderId(providerId);
                    // set Listeners for the customer. First param : provider, Second param : customer
                    setOnClickListeners(providerId, user.getUserId());
                    RecyclerView recyclerViewApp = findViewById(R.id.recyclerViewServicesCosts);
                    recyclerViewApp.setLayoutManager(new GridLayoutManager(this, 1));
                    providerServicesAdapter = new ProviderServicesAdapter(this, serviceList, this, checkedServiceList);
                    recyclerViewApp.setAdapter(providerServicesAdapter);
                }
            }
        } else {
            finish();
        }
    }

    //First param : provider, Second param : customer
    private void setOnClickListeners(int providerId, int userId){
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                Calendar date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        BookAppointment.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth);
                                txtDate.setText(date.get(Calendar.DAY_OF_MONTH) + " / " + (date.get(Calendar.MONTH) + 1) + " / " + date.get(Calendar.YEAR));
                                timePicker();
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                if (!rPickUp.isChecked() && !rDropOff.isChecked()){
                    rPickUp.setError("!");
                    rDropOff.setError("!");
                    isValid = false;
                }
                if (checkedServiceList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Please choose at least one service", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }
                if(txtDate.getText().equals("-- / -- / ---- - 00:00")){
                    txtDate.setError("Please choose the date and time");
                    isValid = false;
                }
                if(isValid) {
                    int appointmentId = checkIntentForAppointmentId();
                    dbHelper.cancelAppointment(appointmentId);
                    for(int i = 0; i < checkedServiceList.size(); i++){
                        Appointment appointment = new Appointment();
                        appointment.setUserId(userId);
                        appointment.setProviderId(providerId);
                        appointment.setServiceId(checkedServiceList.get(i).getServiceId());
                        String dateHour =  txtDate.getText().toString();
                        String[] subDateHour = dateHour.split("-",2);
                        try {
                            appointment.setDate(trimDate(subDateHour[0]));
                            appointment.setTime(trimTime(subDateHour[1]));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if(rPickUp.isChecked()){
                            appointment.setType("Pick up");
                        } else if(rDropOff.isChecked()){
                            appointment.setType("Drop off");
                        }

                        if (rCancelled.isChecked()) {
                            String cancelled = rCancelled.getText().toString().toUpperCase();
                            appointment.setStatus(cancelled);
                        } else if (rCompleted.isChecked()) {
                            String completed = rCompleted.getText().toString().toUpperCase();
                            appointment.setStatus(completed);
                        }
                        appointment.setComments(editTextComments.getText().toString());
                        dbHelper.addAppointment(appointment);
                        startActivity(new Intent(BookAppointment.this, ViewAppointments.class));
                        finish();
                    }
                }
            }
        });

    }
    private java.sql.Date trimDate(String subDate) throws ParseException {
        String day;
        String month;
        String year;

        String[] dmy = subDate.split("/",3);
        dmy[0] = dmy[0].trim();
        dmy[1] = dmy[1].trim();
        dmy[2] = dmy[2].trim();

        day = dmy[0];
        month = dmy[1];
        year = dmy[2];

        Date date = Util.convertDate(year + "-" + month + "-" + day);

        return date;
    }

    private java.sql.Time trimTime(String subDate) throws ParseException {
        String hour;
        String minute;
        String[] hm = subDate.split(":",2);
        hm[0] = hm[0].trim();
        hm[1] = hm[1].trim();
        hour = hm[0];
        minute = hm[1];

        Time time = Util.convertTime(hour + ":" +minute);

        return time;
    }

    private void loadProviderInformation(int providerId){
        User provider = dbHelper.getUserById(providerId);
        linearLayoutCustomer.setVisibility(View.GONE);
        linearLayoutProvider.setVisibility(View.VISIBLE);
        TextView AppTxtProviderName = findViewById(R.id.AppTxtProviderName);
        TextView AppTxtPhone = findViewById(R.id.AppTxtPhone);
        TextView AppTxtAddress = findViewById(R.id.AppTxtAddress);
        AppTxtProviderName.setText(provider.getUserFirstandLastName());
        AppTxtPhone.setText(provider.getPhoneNumber());
        AppTxtAddress.setText(provider.getAddress());

    }

    private void loadCustomerInformation(int customerId){
        User customer = dbHelper.getUserById(customerId);
        linearLayoutProvider.setVisibility(View.GONE);
        linearLayoutCustomer.setVisibility(View.VISIBLE);
        TextView AppUserTxtName = findViewById(R.id.AppUserTxtName);
        TextView AppUserTxtPhone = findViewById(R.id.AppUserTxtPhone);
        TextView AppUserTxtAddress = findViewById(R.id.AppUserTxtAddress);
        AppUserTxtName.setText(customer.getUserFirstandLastName());
        AppUserTxtPhone.setText(customer.getPhoneNumber());
        AppUserTxtAddress.setText(customer.getAddress());
    }


    private int checkIntentForProviderId(){
        Intent intent = getIntent();
        int providerId = 0;
        if(intent != null){
            providerId = intent.getIntExtra("PROVIDER_ID",0);
        }
        return providerId;
    }

    private int checkIntentForCustomerId(){
        Intent intent = getIntent();
        int customerId = 0;
        if(intent != null){
            customerId = intent.getIntExtra("CUSTOMER_ID",0);
        }
        return customerId;
    }

    private int checkIntentForAppointmentId(){
        Intent intent = getIntent();
        int appointmentId = 0;
        if(intent != null){
            appointmentId = intent.getIntExtra("APPOINTMENT_ID",0);
        }
        return appointmentId;
    }

    private void timePicker(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        timeSetStatus = false;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format("%02d:%02d", hourOfDay, minute);
                String text = txtDate.getText().toString();
                txtDate.setText(text + " - " + time);
                timeSetStatus = true;
            }
        }, hour, minute, true);

        timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(!timeSetStatus){
                    String time = "12:00";
                    String text = txtDate.getText().toString();
                    txtDate.setText(text + " - " + time);
                }
            }
        });
        timePickerDialog.show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        NavigationActivity.closeDrawer(drawerLayout);
    }

    @Override
    public void onItemClick(View view, int position, boolean isChecked) {
        if(isChecked){
            checkedServiceList.add(serviceList.get(position));
        } else{
            checkedServiceList.remove(serviceList.get(position));
        }
    }
}