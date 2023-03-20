package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
        /*
        User testProvider = dbHelper.getUserByEmail("bob.smith@example.com");
        User testCustomer = dbHelper.getUserByEmail("john.doe@example.com");

        AppManager.instance.setUser(testProvider); //Singleton class to hold logged user for whole app life cycle
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();

        //TEST FAVOURITE PROVIDERS FOR LOGGED USER, USE THIS PART IN BOOK AN APPOINTMENT SCREEN LATER
        List<User> favouriteProviders = dbHelper.getFavouriteProviders(testCustomer.getUserId());
        System.out.println("Favourite Providers:");
        for (int i = 0; i < favouriteProviders.size(); i++){
            System.out.println("\t" + favouriteProviders.get(i).getFirstName() + " " + favouriteProviders.get(i).getLastName());
        }

        //TEST USER SEARCH FOR PROVIDER, USE THIS PART IN SEARCH CUSTOMER SCREEN LATER
        List<User> usersList = dbHelper.getUsersByFirstAndLastName("B");
        System.out.println("Users which start with B");
        for (int i = 0; i < usersList.size(); i++){
            System.out.println("\t" + usersList.get(i).getFirstName() + " " + usersList.get(i).getLastName());
        }

        //TEST USER LOG IN
        if(dbHelper.checkUserCredentials(testCustomer.getEmail(),"123456") == false){
            System.out.println("Test Customer password is incorrect");
        }
        if(dbHelper.checkUserCredentials(testCustomer.getEmail(),"mypassword") == true){
            System.out.println("Test Customer password is correct");
        }
        */
    }
}