package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.os.Bundle;
import android.widget.ScrollView;
import com.openclassrooms.realestatemanager.Controllers.Fragments.DisplayFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.SearchFragment;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataActivity;
import butterknife.ButterKnife;



public class SearchActivity extends BaseActivity {

    private ScrollView listFragLayout;
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

        if(modeDevice.equals(MODE_TABLET)) { // MODE TABLET
            listFragLayout = findViewById(R.id.fragment_list_layout);
            listFragLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorGrey));
        }

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
                    searchFragment= (SearchFragment) getFragmentManager().findFragmentByTag(SEARCH_FRAG);
                    searchFragment.launchSearchProperties();
                    break;
            }

        } else { // display init configuration
            modeSelected = MODE_SEARCH;
            idProperty=-1;
            configureAndShowSearchFragment();
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------  BACK BUTTON CONFIG ---------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed(){
        if(fragmentDisplayed.equals(EDIT_FRAG))
            configureAndShowDisplayFragment(modeSelected, idProperty);
        else if(fragmentDisplayed.equals(DISPLAY_FRAG) && modeDevice.equals(MODE_PHONE))
            configureAndShowListPropertiesFragment(modeSelected,listProperties);
        else
            returnToSearchCriteria();
    }

    // --------------------------------------------------------------------------------------------------------
    // ------------------------------------ GETTER AND SETTERS ------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    public ScrollView getListFragLayout() {
        return listFragLayout;
    }
}
