package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.openclassrooms.realestatemanager.Controllers.Activities.MapsActivity;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import java.util.ArrayList;
import java.util.List;



public class ConfigureMap implements GoogleMap.OnCameraIdleListener , OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private final static String MODE_TABLET = "mode_tablet";
    private static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    private final static String MAPS_FRAG = "fragment_maps";
    private GoogleMap mMap;
    private Context context;
    private PlaceDetectionClient mPlaceDetectionClient;
    private boolean permissionGranted;
    private LatLng currentLatLng;
    private MapsActivity mapsActivity;
    private SharedPreferences sharedPreferences;
    private List<Property> listProperties;
    private List<Property> fullListProperties;
    private String modeDevice;
    private List<Marker> listMarkers;
    private LatLngBounds cameraBounds;
    private Boolean restore;
    private boolean CameraMove;
    private Bundle savedInstantState;
    private int idProperty;

    public ConfigureMap(Context context, String modeDevice, MapsActivity mapsActivity, int idProperty) {
        this.context = context;
        this.modeDevice=modeDevice;
        this.mapsActivity=mapsActivity;
        this.idProperty=idProperty;
        CameraMove = true;
        getLocationPermission();
    }

    public ConfigureMap(Context context, String modeDevice, MapsActivity mapsActivity, int idProperty, Boolean restore, LatLngBounds cameraBounds) {
        this.context = context;
        this.modeDevice=modeDevice;
        this.mapsActivity=mapsActivity;
        this.idProperty=idProperty;
        this.restore = restore;
        this.cameraBounds=cameraBounds;
        CameraMove = false;
        initialization();
    }

    public void initialization(){
        sharedPreferences = mapsActivity.getSharedPreferences();
        SupportMapFragment mapFragment = (SupportMapFragment) mapsActivity.getSupportFragmentManager()
                .findFragmentById(R.id.map);

        try {
            MapsInitializer.initialize(context);
        } catch (Exception e) {
            String text = context.getResources().getString(R.string.error_map_init) + "\n" + e.toString();
            Toast toast = Toast.makeText(context,text,Toast.LENGTH_LONG);
            toast.show();
        }

        // get all properties not sold from the database
        fullListProperties = new ArrayList<>(UtilsGoogleMap.getAllProperties(mapsActivity.getApplicationContext()));

        // initialize map
        mapFragment.getMapAsync(this);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(context);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnCameraIdleListener(this);

        if(restore)
            finalizeMapConfiguration();
        else {
            if (permissionGranted) { // if permission for geolocalization
                getCurrentLocation(); // find current location (and search properties closed to current location)
            }
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // ---------------------------------------- GET PROPERTIES ------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    public void launchMapConfiguration(Boolean restore, LatLngBounds cameraBounds, Bundle savedInstantState){
        this.restore = restore;
        this.cameraBounds=cameraBounds;
        this.savedInstantState=savedInstantState;
        initialization();
    }

    public void getPropertiesCloseToTarget(LatLngBounds cameraBounds, List<Property> fullListProperties) {
        listProperties = new ArrayList<>(UtilsGoogleMap.filterListPropertiesByLocation(cameraBounds, fullListProperties));
    }

    @Override
    public void onCameraIdle() {

        cameraBounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        if(CameraMove) {

            // get properties close to cameraTarget
            getPropertiesCloseToTarget(cameraBounds, fullListProperties);

            // configure markers on each property
            configureMapWithMarkers(cameraBounds, idProperty);

            // If tablet mode, refresh list of properties
            if (modeDevice.equals(MODE_TABLET)) {

                if(mapsActivity.getListPropertiesFragment()==null)
                    mapsActivity.configureAndShowListPropertiesFragment(MODE_DISPLAY_MAPS);
                else {

                    Bundle bundle = SaveAndRestoreDataListPropertiesFrag.createBundleForListPropertiesFragment(idProperty,modeDevice,MODE_DISPLAY_MAPS,
                            mapsActivity.getFragmentDisplayed(),cameraBounds,null);

                    mapsActivity.getListPropertiesFragment().setListProperties(listProperties);
                    mapsActivity.refreshListPropertiesFragment(bundle,MODE_DISPLAY_MAPS);
                }
                mapsActivity.setFragmentDisplayed(MAPS_FRAG);
            }
        }
        CameraMove = true;
    }

    // --------------------------------------------------------------------------------------------------------
    // ---------------------------------- INITIAL MAP CONFIGURATION -------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private void getLocationPermission() {

        if(context!=null && mapsActivity!=null){
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(mapsActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                permissionGranted = false;
            }
        }
    }

    private void getCurrentLocation() {

        cameraBounds = null;
        currentLatLng = UtilsGoogleMap.findLastCurrentLocation(sharedPreferences);

        mPlaceDetectionClient = Places.getPlaceDetectionClient(mapsActivity);

        if (permissionGranted) {

            if(Utils.isInternetAvailable(context)){

                try {

                    final Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
                    placeResult.addOnCompleteListener(task -> {

                        // ----------------------------- CURRENT LOCATION FOUND -----------------------------
                        if (task.isSuccessful() && task.getResult() != null) {

                            // Recover the latitude and longitude of current location
                            currentLatLng = UtilsGoogleMap.findPlaceHighestLikelihood(task);

                            // Save currentLatLng in sharedpreferrences
                            UtilsGoogleMap.saveLastCurrentLocation(sharedPreferences,currentLatLng);

                            if(currentLatLng.longitude>=-123 && currentLatLng.longitude<=-122
                                    && currentLatLng.latitude>=37 && currentLatLng.latitude<=38) // for Travis integration tests
                                currentLatLng=new LatLng(48.866298, 2.383746); // for Travis integration tests

                        } else {

                            // ----------------------------- CURRENT LOCATION NOT FOUND -----------------------------

                            // Warn user
                            Toast.makeText(context,context.getResources().getString(R.string.error_current_location),Toast.LENGTH_LONG).show();
                        }

                        // Move camera to currentLatLng
                        moveCameraToCurrentPosition(currentLatLng);
                    });

                } catch (SecurityException e) {
                    Toast.makeText(context,context.getResources().getString(R.string.error_current_location)+ "\n" + e.toString() ,Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context,context.getResources().getString(R.string.error_internet),Toast.LENGTH_LONG).show();
                finalizeMapConfiguration();
            }
        } else
            UtilsBaseActivity.displayError(context,context.getResources().getString(R.string.give_permission));

    }

    public void moveCameraToCurrentPosition(LatLng currentLatLng){
        if(currentLatLng!=null) {
            // Show the initial position
            CameraMove = false;
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(currentLatLng.latitude, currentLatLng.longitude), 15));
        }

        // Finalize Map configuration
        new Handler().postDelayed(this::finalizeMapConfiguration,500);
    }

    // --------------------------------------------------------------------------------------------------------
    // ---------------------------------- FINALIZE MAP CONFIGURATION ------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    public void finalizeMapConfiguration(){

        if(cameraBounds==null)
            cameraBounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        // get properties close to current location
        getPropertiesCloseToTarget(cameraBounds, fullListProperties);

        // configure markers on each property
        if(listProperties!=null){
            if(listProperties.size()>0)
                configureMapWithMarkers(cameraBounds, idProperty);
        }

        // If tablet mode, display list of properties
        if(modeDevice.equals(MODE_TABLET)){
            if(savedInstantState==null && idProperty==-1) {
                mapsActivity.configureAndShowListPropertiesFragment(MODE_DISPLAY_MAPS);
                new Handler().postDelayed(() -> mapsActivity.getListPropertiesFragment().setFragmentDisplayed(MAPS_FRAG),500);
                mapsActivity.setFragmentDisplayed(MAPS_FRAG);
            } else {
                Bundle bundle = SaveAndRestoreDataListPropertiesFrag.createBundleForListPropertiesFragment(idProperty,modeDevice,MODE_DISPLAY_MAPS,
                        mapsActivity.getFragmentDisplayed(),cameraBounds,null);

                mapsActivity.getListPropertiesFragment().setListProperties(listProperties);
                mapsActivity.getListPropertiesFragment().setFragmentDisplayed(MAPS_FRAG);
                mapsActivity.refreshListPropertiesFragment(bundle,MODE_DISPLAY_MAPS);
                mapsActivity.setFragmentDisplayed(MAPS_FRAG);
            }
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------- MARKERS --------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private void configureMapWithMarkers(LatLngBounds cameraTarget, int idProperty){

        updateMarkers(listProperties, idProperty);
        mapsActivity.setCameraBounds(cameraTarget);
        mapsActivity.setListProperties(listProperties);
        mapsActivity.getProgressBar().setVisibility(View.GONE);
    }

    private void updateMarkers(List<Property> listProp, int idProperty){
        createMarkerForEachProperty(listProp, idProperty);
        createMarkerListener();
        selectMarker(idProperty);
    }

    private void createMarkerListener(){

        mMap.setOnMarkerClickListener(marker -> {

            CameraMove = false;
            int id = (int) marker.getTag();
            mapsActivity.changeToDisplayMode(id); // display property
            selectMarker(id); // change color of marker
            if (modeDevice.equals(MODE_TABLET)) { // in tablet mode, change property selected in the list

                Bundle bundle = SaveAndRestoreDataListPropertiesFrag.createBundleForListPropertiesFragment(id,modeDevice,MODE_DISPLAY_MAPS,
                        mapsActivity.getFragmentDisplayed(),cameraBounds,null);

                mapsActivity.refreshListPropertiesFragment(bundle,MODE_DISPLAY_MAPS);
            }

            return false;
        });
    }

    private void createMarkerForEachProperty(List<Property> listProperties, int idProperty){

        listMarkers = new ArrayList<>();
        mMap.clear();
        MarkerOptions markerOptions;

        for(Property property : listProperties){

            LatLng position = new LatLng(property.getLat(),property.getLng());

            markerOptions = new MarkerOptions()
                    .position(position);

            Marker marker = mMap.addMarker(markerOptions);
            listMarkers.add(marker);

            if(idProperty==property.getId()) // select the first position
                selectMarker(idProperty);

            marker.setTag(property.getId());
        }
    }

    public void selectMarker(int idProp){

        if(listMarkers!=null) {
            for (Marker marker : listMarkers) {
                if(marker!=null){
                    if(marker.getTag()!=null){
                        if (marker.getTag().toString().equals(String.valueOf(idProp)))
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        else
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }
                }
            }
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------- GETTERS AND SETTERS --------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    public GoogleMap getMap() {
        return mMap;
    }

    public void setMap(GoogleMap map) {
        mMap = map;
    }

    public List<Property> getListProperties() {
        return listProperties;
    }

    public void setIdProperty(int idProperty) {
        this.idProperty = idProperty;
    }
}