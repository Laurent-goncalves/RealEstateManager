package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.os.Bundle;
import com.openclassrooms.realestatemanager.Controllers.Fragments.DisplayFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataActivity;
import com.openclassrooms.realestatemanager.Utils.UtilsBaseActivity;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign views
        ButterKnife.bind(this);
        modeSelected = MODE_DISPLAY;

        // Configure toolbar
        toolbarManager = new ToolbarManager(this);
        toolbarManager.configureNavigationDrawer(this);

        // Configure fragment
        configureFragment(savedInstanceState);
    }

    private void configureFragment(Bundle savedInstanceState){

        // set device mode
        UtilsBaseActivity.setModeDevice(this);

        if(savedInstanceState!=null){ // restore data
            SaveAndRestoreDataActivity.RestoreDataActivity(savedInstanceState,this);

            if(modeDevice.equals(MODE_TABLET)){ // in tablet mode, we restore listPropertiesFragment
                listPropertiesFragment = (ListPropertiesFragment) getFragmentManager().findFragmentByTag(LIST_FRAG);
            }

            switch (fragmentDisplayed) {
                case EDIT_FRAG:
                    editFragment = (EditFragment) getFragmentManager().findFragmentByTag(EDIT_FRAG);
                    break;
                case DISPLAY_FRAG:
                    displayFragment = (DisplayFragment) getFragmentManager().findFragmentByTag(DISPLAY_FRAG);
                    break;
            }

        } else { // display init configuration
            idProperty=-1;
            configureAndShowListPropertiesFragment(MODE_DISPLAY);
        }
    }

    @Override
    public void onBackPressed(){
        if(fragmentDisplayed.equals(EDIT_FRAG))
            UtilsBaseActivity.askForConfirmationToLeaveEditMode(this, MODE_DISPLAY,modeDevice,fragmentDisplayed, idProperty);
        else if(modeDevice.equals(MODE_PHONE))
            configureAndShowListPropertiesFragment(MODE_DISPLAY);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SaveAndRestoreDataActivity.SaveDataActivity(MODE_DISPLAY,fragmentDisplayed,idProperty,lastIdPropertyDisplayed, outState);
    }
}


        /*PropertyDatabase db = PropertyDatabase.getInstance(getApplicationContext());

        db.imageDao().deleteAllImage();
        db.propertyDao().deleteAllProperties();*/

                    /**/