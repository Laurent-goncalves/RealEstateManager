package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Fragments.DisplayFragment;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.GoogleMapUtils;
import com.openclassrooms.realestatemanager.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MapsActivity extends BaseActivity {

    private static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    private static final String SHAREDPREFERENCES = "MAPSPREFERRENCES";
    private SharedPreferences sharedPreferences;
    private ScrollView displayLayout;
    @BindView(R.id.fragment_maps_layout) FrameLayout mapsLayout;
    @BindView(R.id.fragment_layout) ScrollView listPropMap;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    private GoogleMapUtils googleMapUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);

        modeSelected = MODE_DISPLAY_MAPS;

        setModeDevice();
        toolbarManager = new ToolbarManager(this);
        toolbarManager.configureNavigationDrawer(this);

        sharedPreferences = getSharedPreferences(SHAREDPREFERENCES,MODE_PRIVATE);
        displayLayout = findViewById(R.id.fragment_layout);
        displayLayout.setVisibility(View.GONE);

        configureAndShowMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configureAndShowMap();
                } else {
                    displayError(getApplicationContext().getResources().getString(R.string.give_permission));
                }
                break;
            }
        }
    }

    @Override
    public void configureAndShowDisplayFragment(String modeSelected, int idProperty){

        // change icons toolbar
        if(toolbarManager!=null)
            toolbarManager.setIconsToolbarDisplayMode(modeSelected, modeDevice);

        displayFragment = new DisplayFragment();

        // create a bundle
        Bundle bundle = new Bundle();

        // Add the property Id to the bundle
        bundle.putInt(LAST_PROPERTY_SELECTED, idProperty);
        bundle.putString(BUNDLE_DEVICE, modeDevice);
        bundle.putString(BUNDLE_MODE_SELECTED, modeSelected);

        // configure and show the editFragment
        displayFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_position, displayFragment);
        fragmentTransaction.commit();
    }

    public void configureAndShowMap() {
        toolbarManager.setIconsToolbarMapsMode();
        progressBar.setVisibility(View.VISIBLE);
        googleMapUtils = new GoogleMapUtils(getApplicationContext(), modeDevice,this);
    }

    public void changeToMapMode(int idProperty){

        this.idProperty = idProperty;

        toolbarManager.setIconsToolbarMapsMode();

        Property property = Utils.getPropertyFromList(idProperty,listProperties);

        // Select property in the list
        if(modeDevice.equals(MODE_TABLET))
            getListPropertiesFragment().refreshListProperties(Utils.getIndexPropertyFromList(property,listProperties));

        // move camera to property location
        if(property!=null){
            LatLng position = new LatLng(property.getLat(),property.getLng());
            googleMapUtils.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
        }

        displayLayout.setVisibility(View.GONE);
        mapsLayout.setVisibility(View.VISIBLE);
    }

    public void changeToDisplayMode(int idProperty){

        this.idProperty = idProperty;

        toolbarManager.setIconsToolbarDisplayMode(modeSelected,modeDevice);

        Property property = Utils.getPropertyFromList(idProperty,listProperties);

        // Select property in the list (mode Tablet)
        if(modeDevice.equals(MODE_TABLET))
            getListPropertiesFragment().refreshListProperties(Utils.getIndexPropertyFromList(property,listProperties));

        configureAndShowDisplayFragment(MODE_DISPLAY_MAPS, idProperty);

        displayLayout.setVisibility(View.VISIBLE);
        mapsLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

        if(fragmentDisplayed.equals(EDIT_FRAG))
            configureAndShowDisplayFragment(modeSelected, idProperty);
        else
            configureAndShowMap();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public GoogleMapUtils getGoogleMapUtils() {
        return googleMapUtils;
    }
}


