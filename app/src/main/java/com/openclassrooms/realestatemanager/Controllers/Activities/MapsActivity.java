package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Fragments.DisplayFragment;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.Provider.MapsContentProvider;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.GoogleMapUtils;
import com.openclassrooms.realestatemanager.Utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.ToLongBiFunction;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends BaseActivity {

    private static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    private SharedPreferences sharedPreferences;
    private ScrollView displayLayout;
    @BindView(R.id.fragment_maps_layout) FrameLayout mapsLayout;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    private SupportMapFragment mapFragment;
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

        sharedPreferences = getSharedPreferences("MAPSPREFERRENCES",MODE_PRIVATE);

        if(modeDevice.equals(MODE_TABLET)) { // MODE TABLET
            displayLayout = findViewById(R.id.fragment_layout);
            displayLayout.setVisibility(View.GONE);
        }

        configureAndShowMap();
    }

    @Override
    public void configureAndShowDisplayFragment(String modeSelected, int idProperty){

        // change icons toolbar
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

        // Select property in the list
        getListPropertiesFragment().refreshListProperties(Utils.getIndexPropertyFromList(property,listProperties));

        configureAndShowDisplayFragment(MODE_DISPLAY_MAPS, idProperty);

        displayLayout.setVisibility(View.VISIBLE);
        mapsLayout.setVisibility(View.GONE);
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


