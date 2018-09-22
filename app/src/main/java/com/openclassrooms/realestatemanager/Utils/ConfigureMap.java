package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.openclassrooms.realestatemanager.Controllers.Activities.MapsActivity;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    private LatLng cameraTarget;
    private SupportMapFragment mapFragment;
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

    public void initialization(){
        sharedPreferences = mapsActivity.getSharedPreferences();
        mapFragment = (SupportMapFragment) mapsActivity.getSupportFragmentManager()
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
            finalizeMapConfiguration(cameraTarget);
        else {
            if (permissionGranted) { // if permission for geolocalization
                getCurrentLocation(); // find current location (and search properties closed to current location)
            }
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // ---------------------------------------- GET PROPERTIES ------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    public void launchMapConfiguration(Boolean restore, LatLng cameraTarget, Bundle savedInstantState){
        this.restore = restore;
        this.cameraTarget=cameraTarget;
        this.savedInstantState=savedInstantState;
        initialization();
    }

    public void getPropertiesCloseToTarget(LatLng cameraTarget, List<Property> fullListProperties) {
        if(cameraTarget!=null){
            if(cameraTarget.latitude!=0 && cameraTarget.longitude!=0){
                currentLatLng = cameraTarget;
            }
        }

        listProperties = new ArrayList<>(UtilsGoogleMap.filterListPropertiesByLocation(cameraTarget, fullListProperties));
    }

    @Override
    public void onCameraIdle() {
        if(CameraMove) {
            cameraTarget = mMap.getCameraPosition().target;
            mapsActivity.setCameraTarget(cameraTarget);

            // get properties close to cameraTarget
            getPropertiesCloseToTarget(cameraTarget, fullListProperties);

            // configure markers on each property
            configureMapWithMarkers(currentLatLng, idProperty);

            // If tablet mode, display list of properties
            if (modeDevice.equals(MODE_TABLET)) {

                if(mapsActivity.getListPropertiesFragment()==null)
                    mapsActivity.configureAndShowListPropertiesFragment(MODE_DISPLAY_MAPS);
                else {

                    mapsActivity.getListPropertiesFragment().setListProperties(listProperties);
                    int position = Utils.getIndexPropertyFromList(idProperty,listProperties);
                    if(position==-1)
                        position=0;

                    mapsActivity.getListPropertiesFragment().configureListProperties(position);
                }

                mapsActivity.setFragmentDisplayed(MAPS_FRAG);
            }
        }
        CameraMove = true;
    }

    // --------------------------------------------------------------------------------------------------------
    // ------------------------------------ GET CURRENT LOCATION ----------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private void getLocationPermission() {

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

    private void getCurrentLocation() {

        mPlaceDetectionClient = Places.getPlaceDetectionClient(mapsActivity);

        if (permissionGranted) {

            if(Utils.isInternetAvailable(context)){

                try {

                    final Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
                    placeResult.addOnCompleteListener(task -> {

                        if (task.isSuccessful() && task.getResult() != null) {

                            // Recover the latitude and longitude of current location
                            currentLatLng = UtilsGoogleMap.findPlaceHighestLikelihood(task);

                            if(currentLatLng.longitude>=-123 && currentLatLng.longitude<=-122
                                    && currentLatLng.latitude>=37 && currentLatLng.latitude<=38) // for Travis integration tests
                                currentLatLng=new LatLng(48.866298, 2.383746); // for Travis integration tests

                        } else {
                            currentLatLng =null;
                            Toast.makeText(context,context.getResources().getString(R.string.error_current_location),Toast.LENGTH_LONG).show();
                        }

                        if(currentLatLng!=null) {
                            UtilsGoogleMap.saveLastCurrentLocation(sharedPreferences,currentLatLng);
                            finalizeMapConfiguration(currentLatLng);
                        } else {
                            finalizeMapConfiguration(null);
                        }
                    });

                } catch (SecurityException e) {

                    finalizeMapConfiguration(null);

                    Toast toast = Toast.makeText(context,context.getResources().getString(R.string.error_current_location)
                            + "\n" + e.toString() ,Toast.LENGTH_LONG);
                    toast.show();
                }

            } else {
                Toast.makeText(context,context.getResources().getString(R.string.error_internet),Toast.LENGTH_LONG).show();
                finalizeMapConfiguration(null);
            }
        } else
            finalizeMapConfiguration(null);

    }

    public void finalizeMapConfiguration(LatLng cameraPosition){

        // recover current location
        if(cameraPosition ==null)
            currentLatLng = UtilsGoogleMap.findLastCurrentLocation(sharedPreferences);
        else
            currentLatLng = cameraPosition;

        // move camera to current location
        moveCameraToDefinedPosition(currentLatLng);

        // get properties close to current location
        getPropertiesCloseToTarget(currentLatLng, fullListProperties);

        // configure markers on each property
        if(listProperties!=null){
            if(listProperties.size()>0)
                configureMapWithMarkers(currentLatLng, idProperty);
        }

        // If tablet mode, display list of properties
        if(modeDevice.equals(MODE_TABLET)){
            if(savedInstantState==null) {
                mapsActivity.configureAndShowListPropertiesFragment(MODE_DISPLAY_MAPS);
                mapsActivity.setFragmentDisplayed(MAPS_FRAG);
            }
        }
    }

    public void moveCameraToDefinedPosition(LatLng currentLatLng){
        if(currentLatLng!=null) {
            // Show the initial position
            CameraMove = false;
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(currentLatLng.latitude, currentLatLng.longitude), 15));
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------- MARKERS --------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private void configureMapWithMarkers(LatLng cameraTarget, int idProperty){

        updateMarkers(listProperties, idProperty);
        mapsActivity.setCameraTarget(cameraTarget);
        mapsActivity.setListProperties(listProperties);
        mapsActivity.getProgressBar().setVisibility(View.GONE);
    }

    private void updateMarkers(List<Property> listProp, int idProperty){
        createMarkerForEachProperty(listProp, idProperty);
        createMarkerListener();
    }

    private void createMarkerListener(){

        mMap.setOnMarkerClickListener(marker -> {

            int id = (int) marker.getTag();
            centerToMarker(id);
            mapsActivity.changeToDisplayMode(id);

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
                selectMarker(marker, true);

            marker.setTag(property.getId());
        }
    }

    public void centerToMarker(int idProp){

        if(listMarkers!=null) {
            for (Marker marker : listMarkers) {
                if (Objects.requireNonNull(marker.getTag()).toString().equals(String.valueOf(idProp)))
                    selectMarker(marker,true);
                else
                    selectMarker(marker,false);
            }
        }

        Property property = Utils.getPropertyFromList(idProp,listProperties);

        if(property!=null){
            LatLng position = new LatLng(property.getLat(),property.getLng());
            moveCameraToDefinedPosition(position);
        }
    }

    private void selectMarker(Marker marker, Boolean select){
        if(select)
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        else
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------- GETTERS AND SETTERS --------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    public GoogleMap getMap() {
        return mMap;
    }

    public SupportMapFragment getMapFragment() {
        return mapFragment;
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