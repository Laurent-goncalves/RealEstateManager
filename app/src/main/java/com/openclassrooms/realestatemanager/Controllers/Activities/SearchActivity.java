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
import com.openclassrooms.realestatemanager.Utils.UtilsBaseActivity;
import butterknife.ButterKnife;


public class SearchActivity extends BaseActivity {

    private static final String MODE_SEARCH = "mode_search";
    private final static String GRAY_COLOR = "GRAY";

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

        //set device mode
        UtilsBaseActivity.setModeDevice(this);

        if(savedInstanceState!=null){ // restore data
            SaveAndRestoreDataActivity.RestoreDataActivity(savedInstanceState,this);

            if(modeDevice.equals(MODE_TABLET)){ // in tablet mode, we restore listPropertiesFragment
                listPropertiesFragment = (ListPropertiesFragment) getFragmentManager().findFragmentByTag(LIST_FRAG);

                if(listPropertiesFragment==null){
                    configureAndShowListPropertiesFragment(modeSelected);
                }
            }

            switch (fragmentDisplayed) {
                case EDIT_FRAG:
                    editFragment = (EditFragment) getFragmentManager().findFragmentByTag(EDIT_FRAG);
                    break;
                case DISPLAY_FRAG:
                    displayFragment = (DisplayFragment) getFragmentManager().findFragmentByTag(DISPLAY_FRAG);
                    break;
                case SEARCH_FRAG:
                    Utils.colorFragmentList(GRAY_COLOR,modeDevice,fragmentDisplayed,this);
                    searchFragment = (SearchFragment) getFragmentManager().findFragmentByTag(SEARCH_FRAG);
                    searchFragment.setSearchQuery(searchQuery);
                    break;
            }

        } else { // display init configuration
            idProperty=-1;
            Utils.colorFragmentList(GRAY_COLOR,modeDevice,SEARCH_FRAG,this);
            configureAndShowSearchFragment();
        }
    }

    public void configureAndShowSearchFragment(){

        fragmentDisplayed = SEARCH_FRAG;
        searchFragment = new SearchFragment();

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
            UtilsBaseActivity.askForConfirmationToLeaveEditMode(this,MODE_SEARCH,modeDevice,fragmentDisplayed, idProperty);
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
}
