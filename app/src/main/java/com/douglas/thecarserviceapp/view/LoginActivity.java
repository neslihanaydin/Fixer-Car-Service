package com.douglas.thecarserviceapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.douglas.thecarserviceapp.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText email, password;
    TextView txtRegLink;

    String[] user = {"test@fixer.com", "123"};

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
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        txtRegLink = findViewById(R.id.txtRegisterLink);
    }

    public void loadEvents() {
        // Login event
        btnLogin.setOnClickListener(v -> {
            String email = this.email.getText().toString().trim();
            String password = this.password.getText().toString().trim();

            if (email.isEmpty()) {
                this.email.setError("Email is required!");
                this.email.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                this.password.setError("Password is required!");
                this.password.requestFocus();
                return;
            }

            // Check if the email field is a valid email format
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                this.email.setError("Please enter a valid email address!");
                this.email.requestFocus();
                return;
            }

            // Check if the email and password match the registered ones
            if (!email.equals(user[0]) || !password.equals(user[1])) {
                Toast.makeText(getApplicationContext(), "Invalid email or password!", Toast.LENGTH_LONG).show();
                return;
            }
        });

        // Registration event
        txtRegLink.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)));
    }
}