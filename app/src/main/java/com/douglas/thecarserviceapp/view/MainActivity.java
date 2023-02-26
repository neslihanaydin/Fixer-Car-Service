package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User providerUser = new User(1,"Neslihan","Turpcu",
                "New Westminster 2021","1123334321",
                "neslihanturpcu@mail.com","84hfy!shr3", User.UserType.PROVIDER);

        User customerUser = new User(2,"Jane","Smith",
                "New Westminster 2021","1333234421",
                "janesmith@mail.com","8t53ga!shr3", User.UserType.CUSTOMER);

        System.out.println("Testing user type: " + providerUser.getFirstName());
        System.out.println("Is provider " + providerUser.isProvider());
        System.out.println("Is customer " + providerUser.isCustomer());

        AppManager.instance.setUser(providerUser); //Singleton class to hold logged user for whole app life cycle

    }
}