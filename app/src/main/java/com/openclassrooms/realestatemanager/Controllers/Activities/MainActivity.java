package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.os.Bundle;
import com.openclassrooms.realestatemanager.Controllers.Fragments.DisplayFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataActivity;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign views
        ButterKnife.bind(this);

        // Configure toolbar
        toolbarManager = new ToolbarManager(this);
        toolbarManager.configureNavigationDrawer(this);

        // Configure fragment
        configureFragment(savedInstanceState);
    }

    private void configureFragment(Bundle savedInstanceState){

        setModeDevice();

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
            modeSelected = MODE_DISPLAY;
            idProperty=-1;
            configureAndShowListPropertiesFragment(modeSelected,null);
        }
    }

    @Override
    public void onBackPressed(){
        if(fragmentDisplayed.equals(EDIT_FRAG))
            configureAndShowDisplayFragment(modeSelected, idProperty);
        else if(modeDevice.equals(MODE_PHONE))
            configureAndShowListPropertiesFragment(MODE_DISPLAY,null);
    }
}


        /*PropertyDatabase db = PropertyDatabase.getInstance(getApplicationContext());

        db.imageDao().deleteAllImage();
        db.propertyDao().deleteAllProperties();*/

                    /**/