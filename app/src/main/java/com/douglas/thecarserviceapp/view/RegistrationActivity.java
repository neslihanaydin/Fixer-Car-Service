package com.douglas.thecarserviceapp.view;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.User;

public class RegistrationActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;
    User user;
    DatabaseHelper dbHelper;

    EditText editFirstName;
    EditText editLastName;
    EditText editAddress;
    EditText editPhone;
    EditText editRegEmail;
    EditText editRegPass;
    EditText editRegPassConf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        FixerToolbar.setToolbar(this, "Service History", true, false);
        setRegistrationLayout();


    }

    private void setRegistrationLayout(){
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editAddress = findViewById(R.id.editAddress);
        editPhone = findViewById(R.id.editPhone);
        editRegEmail = findViewById(R.id.editRegEmail);
        editRegPass = findViewById(R.id.editRegPass);
        editRegPassConf = findViewById(R.id.editRegPassConfirm);
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkTextUtils()){
                    String fName = editFirstName.getText().toString();
                    String lName = editLastName.getText().toString();
                    String address = editAddress.getText().toString();
                    String phone = editPhone.getText().toString();
                    String email = editRegEmail.getText().toString();
                    String password = editRegPass.getText().toString();
                    User newUser = new User(fName,lName,address,phone,email,password, User.UserType.CUSTOMER);
                    dbHelper = new DatabaseHelper(getApplicationContext());
                    dbHelper.addUser(newUser);
                    if(checkIntent()){ // If provider has created the new customer
                        finish();
                    } else {
                        startActivity(new Intent(RegistrationActivity.this, NavigationActivity.class));
                        AppManager.instance.setUser(newUser);
                        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("email", newUser.getEmail());
                        editor.putString("password", newUser.getPassword());
                        editor.apply();
                        finish();
                    }
                }
            }
        });
    }

    private boolean checkTextUtils(){
        boolean isValid = true;
        String password, passwordConf;
        if(TextUtils.isEmpty(editFirstName.getText().toString())){
            editFirstName.setError("Invalid value");
            isValid = false;
        }
        if(TextUtils.isEmpty(editLastName.getText().toString())){
            editLastName.setError("Invalid value");
            isValid = false;
        }
        if(TextUtils.isEmpty(editAddress.getText().toString())){
            editAddress.setError("Invalid value");
            isValid = false;
        }
        if(TextUtils.isEmpty(editPhone.getText().toString())){
            editPhone.setError("Invalid value");
            isValid = false;
        }
        if(TextUtils.isEmpty(editRegEmail.getText().toString())){
            editRegEmail.setError("Invalid value");
            isValid = false;
        }
        if(TextUtils.isEmpty(editRegPass.getText().toString())){
            editRegPass.setError("Invalid value");
            isValid = false;
        }
        if(TextUtils.isEmpty(editRegPassConf.getText().toString())){
            editRegPassConf.setError("Invalid value");
            isValid = false;
        }
        if (isValid) {
            password = editRegPass.getText().toString();
            passwordConf = editRegPassConf.getText().toString();
            if(!password.equals(passwordConf)){
                Toast.makeText(getApplicationContext(),"Password and password confirmation are not same",Toast.LENGTH_SHORT).show();
            }
        }
        return isValid;

    }
    /*
    The checkIntent() method verifies whether the Registration screen received
    a new customer through the Create Customer activity called by the provider.
    If the method returns true, the application should not redirect the newly
    registered user to the home page. Instead, the application should remain
    on the provider screen.
     */
    private boolean checkIntent(){
        Intent intent = getIntent();
        boolean withProvider = false;
        if(intent != null){
            withProvider = intent.getBooleanExtra("WITH_PROVIDER", false);
        }
        return withProvider;
    }

}