package com.douglas.thecarserviceapp.view;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.douglas.thecarserviceapp.R;

public class RegistrationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        FixerToolbar.setToolbar(this, true, false);
    }
}