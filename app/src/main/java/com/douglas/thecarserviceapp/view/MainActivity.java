package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);

        User testProvider = dbHelper.getUserByEmail("bob.smith@example.com");

        AppManager.instance.setUser(testProvider); //Singleton class to hold logged user for whole app life cycle
       // dbHelper.addUser(providerUser);
      //  providerUser.setLastName("TURPCU");
      //  dbHelper.updateUser(providerUser);
        startActivity(new Intent(MainActivity.this, BookAnAppointment.class));
        finish();

    }
}