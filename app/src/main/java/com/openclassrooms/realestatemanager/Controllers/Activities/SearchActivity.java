package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import com.openclassrooms.realestatemanager.Controllers.Fragments.DisplayFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.SearchFragment;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataActivity;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataSearchFragment;
import com.openclassrooms.realestatemanager.Utils.Utils;

import butterknife.ButterKnife;


public class SearchActivity extends BaseActivity {

    private static final String MODE_SEARCH = "mode_search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Assign views and variables
        ButterKnife.bind(this);
        modeSelected = MODE_SEARCH;

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

            switch (fragmentDisplayed) { // the
                case EDIT_FRAG:
                    editFragment = (EditFragment) getFragmentManager().findFragmentByTag(EDIT_FRAG);
                    break;
                case DISPLAY_FRAG:
                    displayFragment = (DisplayFragment) getFragmentManager().findFragmentByTag(DISPLAY_FRAG);
                    break;
                case SEARCH_FRAG:
                    searchFragment = (SearchFragment) getFragmentManager().findFragmentByTag(SEARCH_FRAG);
                    searchFragment.setSearchQuery(searchQuery);
                    break;
            }

        } else { // display init configuration
            idProperty=-1;
            configureAndShowSearchFragment();
        }
    }

    public void configureAndShowSearchFragment(){

        fragmentDisplayed = SEARCH_FRAG;
        searchFragment = new SearchFragment();
        Utils.colorFragmentList("GRAY",modeDevice,this);

        if(listPropertiesFragment!=null)
            listPropertiesFragment.setFragmentDisplayed(fragmentDisplayed);

        // Create bundle
        Bundle bundle = SaveAndRestoreDataSearchFragment.createBundleForSearchFragment(modeDevice, searchQuery);
        searchFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_position, searchFragment, SEARCH_FRAG);
        fragmentTransaction.commit();
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------  BACK BUTTON CONFIG ---------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed(){
        if(fragmentDisplayed.equals(EDIT_FRAG))
            askForConfirmationToLeaveEditMode(MODE_SEARCH, idProperty);
        else if(fragmentDisplayed.equals(DISPLAY_FRAG) && modeDevice.equals(MODE_PHONE))
            configureAndShowListPropertiesFragment(MODE_SEARCH);
        else
            returnToSearchCriteria();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SaveAndRestoreDataActivity.SaveDataActivity(MODE_SEARCH, fragmentDisplayed, idProperty, lastIdPropertyDisplayed, searchQuery, outState);
    }

    // --------------------------------------------------------------------------------------------------------
    // ------------------------------------ GETTER AND SETTERS ------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

}
