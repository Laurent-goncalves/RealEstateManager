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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.ToLongBiFunction;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends BaseActivity {

    private SharedPreferences sharedPreferences;
    @BindView(R.id.fragment_layout) ScrollView mScrollView;
    private SupportMapFragment mapFragment;
    private GoogleMapUtils googleMapUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);
        toolbarManager = new ToolbarManager(this);
        toolbarManager.configureNavigationDrawer(this);

        sharedPreferences = getSharedPreferences("MAPSPREFERRENCES",MODE_PRIVATE);

        googleMapUtils = new GoogleMapUtils(getApplicationContext(), this);
    }

    @Override
    public void configureAndShowDisplayFragment(int idProperty){

        // change icons toolbar
        toolbarManager.setIconsToolbarDisplayMode();

        displayFragment = new DisplayFragment();

        // create a bundle
        Bundle bundle = new Bundle();

        // Add the property Id to the bundle
        bundle.putInt(LAST_PROPERTY_SELECTED, idProperty);
        bundle.putBoolean(MODE_DISPLAY_MAPS,false);

        // configure and show the editFragment
        displayFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_position, displayFragment);
        fragmentTransaction.commit();
    }

    public void configureAndShowMap() {

    }

    public void changeToMapMode(){
        mScrollView.setVisibility(View.GONE);
        googleMapUtils = new GoogleMapUtils(getApplicationContext(), this);
    }

    public void changeToDetailMode(int idProperty){
        mScrollView.setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction().
                remove(getSupportFragmentManager().findFragmentById(R.id.map)).commit();

        configureAndShowDisplayFragment(idProperty);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }


}


