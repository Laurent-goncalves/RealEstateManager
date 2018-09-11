package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.os.Bundle;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        modeSelected = MODE_DISPLAY;

        // Configure toolbar
        setModeDevice();
        toolbarManager = new ToolbarManager(this);
        toolbarManager.configureNavigationDrawer(this);

        idProperty=-1;

        configureAndShowListPropertiesFragment(MODE_DISPLAY, null);
    }
}

//android:configChanges="keyboardHidden|orientation"



        /*if(getIntent()!=null){
            Bundle bundle = getIntent().getExtras();
            if(bundle!=null) {
                if (bundle.getInt(EXTRA_PROPERTY_ID, -1) != -1) {
                    configureAndShowDisplayFragment(bundle.getInt(EXTRA_PROPERTY_ID, -1));
                }
            } else {
                /*Intent intent = new Intent(this,MapsActivity.class);
                startActivity(intent);
                configureAndShowListPropertiesFragment(MODE_DISPLAY, null);
            }
        } else {
            Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);

        }*/

// Show ListPropertiesFragment
// configureAndShowListPropertiesFragment(MODE_DISPLAY, null);