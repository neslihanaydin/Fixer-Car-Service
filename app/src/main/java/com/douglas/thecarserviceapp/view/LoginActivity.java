package com.douglas.thecarserviceapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.User;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText edTxtEmail, edTxtPassword;
    TextView txtRegLink;
    DatabaseHelper dbHelper;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        loadUI();
        loadEvents();
    }

    public void loadUI() {
        btnLogin = findViewById(R.id.btnLogin);
        edTxtEmail = findViewById(R.id.editEmail);
        edTxtPassword = findViewById(R.id.editPassword);
        txtRegLink = findViewById(R.id.txtRegisterLink);

        // AUTO LOGIN
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        email = preferences.getString("email", "");
        password = preferences.getString("password", "");

        if (!email.equals("") && !password.equals("")) {
            dbHelper = new DatabaseHelper(getApplicationContext());
            User user = dbHelper.getUserByEmail(email);
            AppManager.instance.setUser(user);
            startActivity(new Intent(LoginActivity.this, BookAnAppointment.class));
            finish();
        }
    }

    public void loadEvents() {
        // Login event
        btnLogin.setOnClickListener(v -> {
            String email = this.edTxtEmail.getText().toString().trim();
            String password = this.edTxtPassword.getText().toString().trim();
            dbHelper = new DatabaseHelper(getApplicationContext());

            if (email.isEmpty()) {
                this.edTxtEmail.setError("Email is required!");
                this.edTxtEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                this.edTxtPassword.setError("Password is required!");
                this.edTxtPassword.requestFocus();
                return;
            }

            // Check if the email field is a valid email format
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                this.edTxtEmail.setError("Please enter a valid email address!");
                this.edTxtEmail.requestFocus();
                return;
            }

            // Check if the email and password match the registered ones
            if(dbHelper.checkUserCredentials(email, password)) {
                User user = dbHelper.getUserByEmail(email);
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", user.getEmail());
                editor.putString("password", user.getPassword());
                editor.apply();
                AppManager.instance.setUser(user); //Singleton class to hold logged user for whole app life cycle
                startActivity(new Intent(LoginActivity.this, BookAnAppointment.class));
                Toast.makeText(getApplicationContext(), "Welcome " + user.getUserType(), Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Invalid email or password!", Toast.LENGTH_LONG).show();
            }
        });

        // Registration event
        txtRegLink.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)));
    }
}