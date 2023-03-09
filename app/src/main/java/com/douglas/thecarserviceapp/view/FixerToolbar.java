package com.douglas.thecarserviceapp.view;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.douglas.thecarserviceapp.R;

public class FixerToolbar {
    static Toolbar toolbar;
    static TextView toolbarTitle;
    static ImageView backIcon, menuIcon;

    public static void setToolbar(AppCompatActivity activity, boolean buttonBack, boolean buttonMenu) {
        setToolbar(activity, null, buttonBack, buttonMenu);
    }

    public static void setToolbar(AppCompatActivity activity, String title, boolean buttonBack, boolean buttonMenu) {
        toolbar = activity.findViewById(R.id.fixer_toolbar);
        activity.setSupportActionBar(toolbar);
        toolbarTitle = activity.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(title);
        backIcon = activity.findViewById(R.id.back_arrow);
        menuIcon = activity.findViewById(R.id.menu_icon);

        if (title == null) {
            toolbarTitle.setVisibility(View.INVISIBLE);
        }

        if (buttonBack == false) {
            backIcon.setVisibility(View.INVISIBLE);
        }

        if (buttonMenu == false) {
            menuIcon.setVisibility(View.INVISIBLE);
        }
        createEvents(activity);
    }

    protected static void createEvents(AppCompatActivity activity) {
        backIcon.setOnClickListener(v -> {
            activity.onBackPressed();
        });
        menuIcon.setOnClickListener(v -> {

        });
    }
}
