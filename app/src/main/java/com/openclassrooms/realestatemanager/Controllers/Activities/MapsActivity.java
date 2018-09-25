package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import com.google.android.gms.maps.model.LatLngBounds;
import com.openclassrooms.realestatemanager.Controllers.Fragments.DisplayFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Models.MapsActivityListener;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.ConfigureMap;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataActivity;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.Utils.UtilsBaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MapsActivity extends BaseActivity implements MapsActivityListener {

    @BindView(R.id.fragment_maps_layout) FrameLayout mapsLayout;
    @BindView(R.id.fragment_layout) ScrollView listPropMap;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    private final static String MAPS_FRAG = "fragment_maps";
    private static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    private static final String SHAREDPREFERENCES = "MAPSPREFERRENCES";
    private SharedPreferences sharedPreferences;
    private ScrollView displayLayout;
    private ConfigureMap mConfigureMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Assign views
        ButterKnife.bind(this);
        displayLayout = findViewById(R.id.fragment_layout);
        modeSelected = MODE_DISPLAY_MAPS;

        // Configure toolbar
        toolbarManager = new ToolbarManager(this);
        toolbarManager.configureNavigationDrawer(this);

        // Configure fragment
        configureFragment(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SaveAndRestoreDataActivity.SaveDataActivity(modeSelected,fragmentDisplayed,idProperty,cameraBounds,lastIdPropertyDisplayed,outState);
    }

    // --------------------------------------------------------------------------------------------------------
    // ------------------------------------ CONFIGURE VIEWS ---------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private void configureFragment(Bundle savedInstanceState){

        // Get sharedpreferences and set mode device
        sharedPreferences = getSharedPreferences(SHAREDPREFERENCES,MODE_PRIVATE);
        UtilsBaseActivity.setModeDevice(this);

        if(savedInstanceState!=null){

            // restore data (after rotation)
            SaveAndRestoreDataActivity.RestoreDataActivity(savedInstanceState,this);

            // restore fragments
            if(modeDevice.equals(MODE_TABLET)){
                listPropertiesFragment = (ListPropertiesFragment) getFragmentManager().findFragmentByTag(LIST_FRAG);
            }

            switch (fragmentDisplayed) {
                case EDIT_FRAG:
                    displayDetails();
                    editFragment = (EditFragment) getFragmentManager().findFragmentByTag(EDIT_FRAG);
                    break;
                case DISPLAY_FRAG:
                    displayDetails();
                    displayFragment = (DisplayFragment) getFragmentManager().findFragmentByTag(DISPLAY_FRAG);
                    break;
                case MAPS_FRAG:

                    mConfigureMap = new ConfigureMap(getApplicationContext(), modeDevice,this, idProperty,true,cameraBounds);

                    // Define fragmentDisplayed and send it to listPropertiesFragment if exists (tablet mode)
                    if(listPropertiesFragment!=null)
                        listPropertiesFragment.setFragmentDisplayed(fragmentDisplayed);

                    // hide display and show map layout
                    new Handler().postDelayed(this::displayMap, 500);
                    break;
            }

        } else { // display init configuration
            fragmentDisplayed = MAPS_FRAG;
            idProperty=-1;
            configureAndShowMap();

            // configure buttons add and edit in toolbar
            getToolbarManager().setIconsToolbarMapsMode();
        }
    }

    @Override
    public void configureAndShowDisplayFragment(String modeSelected, int idProperty){

        // change icons toolbar
        if(toolbarManager!=null)
            toolbarManager.setIconsToolbarDisplayMode(MODE_DISPLAY_MAPS, modeDevice);

        displayFragment = new DisplayFragment();
        fragmentDisplayed = DISPLAY_FRAG;
        lastIdPropertyDisplayed = idProperty;

        if(listPropertiesFragment!=null)
            listPropertiesFragment.setFragmentDisplayed(fragmentDisplayed);

        // create a bundle
        Bundle bundle = new Bundle();

        // Add the property Id to the bundle
        bundle.putInt(LAST_PROPERTY_SELECTED, idProperty);
        bundle.putString(BUNDLE_DEVICE, modeDevice);
        bundle.putString(BUNDLE_MODE_SELECTED, MODE_DISPLAY_MAPS);

        // configure and show the editFragment
        displayFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_position, displayFragment);
        fragmentTransaction.commit();

        displayDetails(); // hide map and show display layout
    }

    public void configureAndShowMap() {

        displayMap(); // hide display and show map layout

        // Define fragmentDisplayed and send it to listPropertiesFragment if exists (tablet mode)
        fragmentDisplayed = MAPS_FRAG;
        if(listPropertiesFragment!=null)
            listPropertiesFragment.setFragmentDisplayed(fragmentDisplayed);

        // update icons in toolbar
        toolbarManager.setIconsToolbarMapsMode();
        progressBar.setVisibility(View.VISIBLE); // open progressBar

        // Launch map configuration
        mConfigureMap = new ConfigureMap(getApplicationContext(), modeDevice,this, idProperty);
        mConfigureMap.setIdProperty(idProperty);
        mConfigureMap.launchMapConfiguration(false, cameraBounds, null);
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------- SWITCH MODE ----------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    public void changeToDisplayMode(int idProperty){

        this.idProperty = idProperty;
        this.lastIdPropertyDisplayed = idProperty;
        fragmentDisplayed = DISPLAY_FRAG;

        // Change icons in toolbar
        toolbarManager.setIconsToolbarDisplayMode(MODE_DISPLAY_MAPS,modeDevice);

        // in Tablet mode, update the list of properties
        Property property = Utils.getPropertyFromList(idProperty,listProperties);
        int position = Utils.getIndexPropertyFromList(property,listProperties);

        if(modeDevice.equals(MODE_TABLET))
            getListPropertiesFragment().configureListProperties(position);

        // show the property details
        configureAndShowDisplayFragment(MODE_DISPLAY_MAPS, idProperty);
    }

    public void displayMap(){
        fragmentDisplayed = MAPS_FRAG;
        toolbarManager.setIconsToolbarMapsMode();
        mapsLayout.setVisibility(View.VISIBLE);
        displayLayout.setVisibility(View.GONE);
    }

    private void displayDetails(){
        mapsLayout.setVisibility(View.GONE);
        displayLayout.setVisibility(View.VISIBLE);
    }

    // --------------------------------------------------------------------------------------------------------
    // ---------------------------- PERMISSIONS AND BACK BUTTON CONFIG ----------------------------------------
    // --------------------------------------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchMapsActivity();
                    finish();
                } else {
                    UtilsBaseActivity.displayError(getApplicationContext(),getApplicationContext().getResources().getString(R.string.give_permission));
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(fragmentDisplayed!=null){
            switch (fragmentDisplayed) {
                case EDIT_FRAG:
                    UtilsBaseActivity.askForConfirmationToLeaveEditMode(this, MODE_DISPLAY_MAPS, modeDevice, fragmentDisplayed, idProperty);
                    break;
                case MAPS_FRAG:
                    launchMainActivity();
                    stopActivity();
                    break;
                default:
                    mConfigureMap = new ConfigureMap(getApplicationContext(), modeDevice,this, idProperty,true, cameraBounds);
                    displayMap();
                    break;
            }
        } else
            configureAndShowMap();
    }

    // --------------------------------------------------------------------------------------------------------
    // ------------------------------------ GETTER AND SETTERS ------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public ConfigureMap getConfigureMap() {
        return mConfigureMap;
    }

    public void setConfigureMap(ConfigureMap configureMap) {
        mConfigureMap = configureMap;
    }

    public void setCameraBounds(LatLngBounds cameraBounds) {
        this.cameraBounds = cameraBounds;
    }

    public MapsActivity getMapsActivity(){
        return this;
    }


}