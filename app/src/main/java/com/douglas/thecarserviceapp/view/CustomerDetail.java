package com.douglas.thecarserviceapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.douglas.thecarserviceapp.R;
import com.douglas.thecarserviceapp.adapter.MainAdapter;
import com.douglas.thecarserviceapp.adapter.ProfileAdapter;
import com.douglas.thecarserviceapp.dbhelper.DatabaseHelper;
import com.douglas.thecarserviceapp.model.User;

public class CustomerDetail extends AppCompatActivity implements ProfileAdapter.ItemClickListener {

    RecyclerView menuRecyclerView;
    DrawerLayout drawerLayout;
    ImageView btMenu;
    ProfileAdapter profileAdapter;
    EditText edtNewPass, editConfPass;
    public static Button buttonSaveCus;
    public static Button buttonDelCus;
    RecyclerView recyclerViewEditFieldsCus;
    DatabaseHelper dbHelper;
    User customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        edtNewPass = findViewById(R.id.editTextNewPassCus);
        editConfPass = findViewById(R.id.editTextConfirmPassCus);
        buttonSaveCus = findViewById(R.id.saveCusButton);
        buttonDelCus = findViewById(R.id.deleteCusButton);

        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.menu_icon);
        menuRecyclerView = findViewById(R.id.recycler_view);


        //Change the page header
        FixerToolbar.setToolbar(this, "User Details", true, false);

        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuRecyclerView.setAdapter(new MainAdapter(this, NavigationActivity.items));
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Intent intent = getIntent();

        if(intent!=null){
            dbHelper = new DatabaseHelper(getApplicationContext());
            int userId = intent.getIntExtra("USER_ID", 0);
            customer = dbHelper.getUserById(userId);
            recyclerViewEditFieldsCus = findViewById(R.id.recyclerViewEditFieldsCus);
            recyclerViewEditFieldsCus.setLayoutManager(new LinearLayoutManager(this));
            profileAdapter = new ProfileAdapter(this,customer,this);
            recyclerViewEditFieldsCus.setAdapter(profileAdapter);
        }

        buttonSaveCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] fields = new String[ProfileAdapter.FIELD_COUNT];
                boolean isValid = true;
                for(int i = 0; i < ProfileAdapter.FIELD_COUNT; i++ ){
                    EditText editText = recyclerViewEditFieldsCus.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.editField);
                    if(TextUtils.isEmpty(editText.getText().toString())){
                        editText.setError("Invalid value");
                        isValid = false;
                        break;
                    } else {
                        fields[i] = editText.getText().toString();
                        isValid = true;
                    }
                }
                // Null check
                if(isValid) {
                    String firstName = fields[0];
                    String lastName = fields[1];
                    String address = fields[2];
                    String phone = fields[3];
                    //Update User Information on db
                    System.out.println(customer.getEmail());
                    User updatedUser = new User(firstName,lastName, address, phone, customer.getEmail());
                    long result = dbHelper.updateUserInfo(updatedUser);
                    if(edtNewPass.getText().toString().equals("") && result > 0){
                        Toast.makeText(CustomerDetail.this, "Customer profile updated", Toast.LENGTH_SHORT).show();
                    }
                }
                // Update password
                if(!edtNewPass.getText().toString().equals("") &&
                    !editConfPass.getText().toString().equals("")){
                    if((edtNewPass.getText().toString()).equals((editConfPass.getText().toString()))){
                        //update user password on session
                        customer.setPassword(edtNewPass.getText().toString());
                        //update user password on db
                        long result = dbHelper.updateUserPassword(customer);
                        if(result > 0){
                            Toast.makeText(CustomerDetail.this, "User profile updated", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(CustomerDetail.this, "Unexpected error at updating profile", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        edtNewPass.setError("Check the new password");
                        editConfPass.setError("Check the new password");
                    }
                }
                buttonSaveCus.setEnabled(false);

                CustomerDetail.buttonSaveCus.setBackgroundResource(R.drawable.rounded_background_gray_button);
                int buttonTextColor = ContextCompat.getColor(getApplicationContext(), R.color.fixer_white);
                CustomerDetail.buttonSaveCus.setTextColor(buttonTextColor);
                edtNewPass.setText(null);
                editConfPass.setText(null);
            }

        });

        editConfPass.addTextChangedListener(new TextWatcher() {
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

        buttonDelCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context activity = CustomerDetail.this;

                // BEGIN FIXER CUSTOM LOGOUT DIALOG BOX
                LayoutInflater inflater = LayoutInflater.from(activity);
                View customDialogView = inflater.inflate(R.layout.fixer_dialog_logout, null);
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setView(customDialogView);
                Dialog customDialog = builder.create();
                customDialog.setContentView(customDialogView);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(customDialog.getWindow().getAttributes());
                int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.8);
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                customDialog.getWindow().setAttributes(lp);
                customDialog.getWindow().setBackgroundDrawable(null);

                TextView title = customDialogView.findViewById(R.id.dialog_title);
                title.setText("Remove Customer");

                TextView message = customDialogView.findViewById(R.id.dialog_message);
                message.setText("Are you sure you want to remove the customer " + customer.getUserFirstandLastName() + "?");

                Button cancelButton = customDialogView.findViewById(R.id.dialog_cancel_button);
                cancelButton.setOnClickListener((View.OnClickListener) l -> customDialog.dismiss());

                Button yesButton = customDialogView.findViewById(R.id.dialog_yes_button);
                yesButton.setOnClickListener((View.OnClickListener) l -> {
                    dbHelper = new DatabaseHelper(getApplicationContext());
                    dbHelper.deleteUser(customer);
                    //recreate(); //Start activity again
                    startActivity(new Intent(CustomerDetail.this, SearchCustomer.class));
                    customDialog.dismiss();
                });
                customDialog.show();

                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetail.this);
                builder.setMessage("Are you sure you want to delete customer : " + customer.getUserFirstandLastName());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper = new DatabaseHelper(getApplicationContext());
                        dbHelper.deleteUser(customer);
                      //  recreate(); //Start activity again
                        dialog.dismiss();
                        startActivity(new Intent(CustomerDetail.this, SearchCustomer.class));
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();*/
            }
        });
    }

    private void checkRequiredFields() {
        boolean allFieldsFilled = true;
        if (TextUtils.isEmpty(edtNewPass.getText())){
            allFieldsFilled = false;
        }
        if (TextUtils.isEmpty(editConfPass.getText())){
            allFieldsFilled = false;
        }
        if (allFieldsFilled) {
            buttonSaveCus.setEnabled(true);
            CustomerDetail.buttonSaveCus.setBackgroundResource(R.drawable.button_enabled);
            int buttonTextColor = ContextCompat.getColor(getApplicationContext(), R.color.fixer_black);
            CustomerDetail.buttonSaveCus.setTextColor(buttonTextColor);
        } else {
            buttonSaveCus.setEnabled(false);
        }
    }


    @Override
    public void onItemClick(View view, int position) {

    }
}