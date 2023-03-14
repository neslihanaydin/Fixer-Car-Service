package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.adapter.ProfileAdapter;
import com.douglas.thecarserviceapp.app.AppManager;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.User;

public class Profile extends AppCompatActivity implements ProfileAdapter.ItemClickListener {
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    ProfileAdapter profileAdapter;
    User user;
    EditText edtCurrPass, edtNewPass, edtConfPass;
    public static Button buttonSave;
    RecyclerView recyclerViewEditFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.menu_icon);
        recyclerView = findViewById(R.id.recycler_view);
        user = AppManager.instance.user;

        //Change the page header
        FixerToolbar.setToolbar(this, "Profile", true, true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainAdapter(this, BookAnAppointment.arrayList));

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        recyclerViewEditFields = findViewById(R.id.recyclerViewEditFields);
        recyclerViewEditFields.setLayoutManager(new GridLayoutManager(this,1));
        profileAdapter = new ProfileAdapter(this,user,this);
        recyclerViewEditFields.setAdapter(profileAdapter);

        dbHelper = new DatabaseHelper(getApplicationContext());
        buttonSave = findViewById(R.id.saveButton);
        edtCurrPass = findViewById(R.id.editTextCurPass);
        edtNewPass = findViewById(R.id.editTextNewPass);
        edtConfPass = findViewById(R.id.editTextConfirmPass);
        setListeners();

    }

    private void setListeners(){

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSaveButton();
            }
        });

        edtConfPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkRequiredFields();

            }
        });
    }

    private void checkRequiredFields() {
        boolean allFieldsFilled = true;
        if (TextUtils.isEmpty(edtCurrPass.getText())) {
            allFieldsFilled = false;
        }
        if (TextUtils.isEmpty(edtNewPass.getText())) {
            allFieldsFilled = false;
        }
        if (TextUtils.isEmpty(edtConfPass.getText())) {
            allFieldsFilled = false;
        }

        if (allFieldsFilled) {
            buttonSave.setEnabled(true);
            Profile.buttonSave.setBackgroundResource(R.drawable.button_enabled);
            int buttonTextColor = ContextCompat.getColor(getApplicationContext(), R.color.fixer_black);
            Profile.buttonSave.setTextColor(buttonTextColor);
        } else {
            buttonSave.setEnabled(false);
        }
    }

    private void onClickSaveButton(){

        String[] fields = new String[ProfileAdapter.FIELD_COUNT];
        boolean isValid = true;
        for (int i = 0; i < ProfileAdapter.FIELD_COUNT; i++) {
            //findViewHolderForAdapterPosition() is a method of the RecyclerView class
            // that is used to find the ViewHolder for a given position in the adapter.
            EditText editText = recyclerViewEditFields.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.editField);
            if(TextUtils.isEmpty(editText.getText().toString())){
                editText.setError("Invalid value");
                isValid = false;
                break;
            } else {
                fields[i] = editText.getText().toString();
                isValid = true;
            }
        }
        //Null check
        if(isValid){
            String firstName = fields[0];
            String lastName = fields[1];
            String address = fields[2];
            String phone = fields[3];
            //Update User Information on db
            System.out.println(user.getEmail());
            User updatedUser = new User(firstName,lastName, address, phone, user.getEmail());
            long result = dbHelper.updateUserInfo(updatedUser);
            if(edtCurrPass.getText().toString() == "" && result > 0){
                Toast.makeText(this, "User profile updated successfully", Toast.LENGTH_SHORT).show();
            }

        }
        //Update password
        if(!edtCurrPass.getText().toString().equals("") &&
                !edtNewPass.getText().toString().equals("") &&
                !edtConfPass.getText().toString().equals(""))  {
            //check password
            if(user.getPassword() == edtCurrPass.getText().toString()){
                edtCurrPass.setError("Password is incorrect!");
            } else {
                if((edtNewPass.getText().toString()).equals((edtConfPass.getText().toString()))){
                    //update user password on session
                    user.setPassword(edtNewPass.getText().toString());
                    //update user password on db
                    long result = dbHelper.updateUserPassword(user);
                    if(result > 0){
                        Toast.makeText(this, "User profile updated successfully", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Unexpected error at updating profile", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    edtNewPass.setError("Check the new password");
                    edtConfPass.setError("Check the new password");
                }
            }
        }
        buttonSave.setEnabled(false);
        Profile.buttonSave.setBackgroundResource(R.drawable.rounded_background_gray_button);
        int buttonTextColor = ContextCompat.getColor(getApplicationContext(), R.color.fixer_white);
        Profile.buttonSave.setTextColor(buttonTextColor);
        edtConfPass.setText(null);
        edtCurrPass.setText(null);
        edtNewPass.setText(null);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        BookAnAppointment.closeDrawer(drawerLayout);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}