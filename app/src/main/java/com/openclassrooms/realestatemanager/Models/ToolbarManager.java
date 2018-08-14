package com.openclassrooms.realestatemanager.Models;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.R;

import butterknife.OnClick;

public class ToolbarManager {

    private MainActivity mainActivity;
    private TextView title_toolbar;
    private Context context;
    private ImageButton hamburger;
    private ImageButton addButton;
    private ImageButton editButton;
    private ImageButton searchButton;

    public ToolbarManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        context = mainActivity.getApplicationContext();
    }

    // ---------------------------------------------------------------------------------
    // -----------------     CONFIGURATION OF TOOLBAR  ---------------------------------
    // ---------------------------------------------------------------------------------

    public void configure_toolbar(){

        Toolbar toolbar = mainActivity.findViewById(R.id.activity_main_toolbar);
        mainActivity.setSupportActionBar(toolbar);

        //Assign and edit toolbar title
        title_toolbar = toolbar.findViewById(R.id.title_toolbar);
        title_toolbar.setText(context.getResources().getString(R.string.app_name));

        // Assign buttons from toolbar
        hamburger = toolbar.findViewById(R.id.button_hamburger);
        addButton = toolbar.findViewById(R.id.add_property_button);
        editButton = toolbar.findViewById(R.id.edit_property_button);
        searchButton = toolbar.findViewById(R.id.search_property_button);

        // Create onClickListener for buttons
        configureOnClickListener();
    }

    private void configureOnClickListener(){

        addButton.setOnClickListener(v -> {
            mainActivity.changeToEditMode(-1); // new property to add to BDD
        });

        editButton.setOnClickListener(v -> {
            int propertyId = mainActivity.getCurrentPropertyDisplayed();
            mainActivity.changeToEditMode(propertyId); // edit property currently displayed
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void removeIconsToolbar(){
        addButton.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
    }

    public void addIconsToolbar(){
        addButton.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.VISIBLE);
    }
}
