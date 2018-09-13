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

        //configureAndShowEditFragment(0);
        configureAndShowListPropertiesFragment(MODE_DISPLAY, null);
    }

    @Override
    public void onBackPressed(){
        if(fragmentDisplayed.equals(EDIT_FRAG))
            configureAndShowDisplayFragment(modeSelected, idProperty);
        else if(modeDevice.equals(MODE_PHONE))
            configureAndShowListPropertiesFragment(MODE_DISPLAY, null);
    }
}



        /*PropertyDatabase db = PropertyDatabase.getInstance(getApplicationContext());

        db.imageDao().deleteAllImage();
        db.propertyDao().deleteAllProperties();*/