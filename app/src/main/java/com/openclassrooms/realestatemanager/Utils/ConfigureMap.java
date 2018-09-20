package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

    public ConfigureMap(Context context, String modeDevice, MapsActivity mapsActivity) {
        this.context = context;
        this.modeDevice=modeDevice;
        this.mapsActivity=mapsActivity;
        restore = false;
        CameraMove = true;
        initialization();
        getLocationPermission();
    }

    private void initialization(){
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

        mapFragment.getMapAsync(this);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(context);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        cameraTarget = mapsActivity.getCameraTarget();
        mMap = googleMap;
        mMap.setOnCameraIdleListener(this);

        if(restore)
            getPropertiesCloseToTarget(cameraTarget);
    }

    // --------------------------------------------------------------------------------------------------------
    // ---------------------------------------- RESTORE STATE -------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    public void restoreData(LatLng cameraPosition){
        restore =true;
        CameraMove = true;
        initialization();
        fullListProperties = new ArrayList<>(UtilsGoogleMap.getAllProperties(mapsActivity.getApplicationContext()));
        listProperties = new ArrayList<>();
        currentLatLng = UtilsGoogleMap.findLastCurrentLocation(sharedPreferences);
        new RestoreDataMapFragment(cameraPosition,this);
    }

    // --------------------------------------------------------------------------------------------------------
    // ---------------------------------------- GET PROPERTIES ------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    public void launchMapConfiguration(){
        if(permissionGranted){ // if permission for geolocalization
            // get all properties not sold from the database
            fullListProperties = new ArrayList<>(UtilsGoogleMap.getAllProperties(mapsActivity.getApplicationContext()));
            // find current location (and search properties closed to current location)
            getCurrentLocation();
        }
    }

    public void getPropertiesCloseToTarget(LatLng cameraTarget) {
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
            getPropertiesCloseToTarget(cameraTarget);

            // configure markers on each property
            configureMapWithMarkers(currentLatLng);

            // If tablet mode, display list of properties
            if (modeDevice.equals(MODE_TABLET)) {
                mapsActivity.configureAndShowListPropertiesFragment(MODE_DISPLAY_MAPS, listProperties);
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
                        Toast toast = Toast.makeText(context,context.getResources().getString(R.string.error_current_location),Toast.LENGTH_LONG);
                        toast.show();
                    }

                    if(currentLatLng!=null) {
                        UtilsGoogleMap.saveLastCurrentLocation(sharedPreferences,currentLatLng);
                        finalizeMapConfiguration();
                    } else
                        finalizeMapConfiguration();
                });

            } catch (SecurityException e) {

                finalizeMapConfiguration();

                Toast toast = Toast.makeText(context,context.getResources().getString(R.string.error_current_location)
                        + "\n" + e.toString() ,Toast.LENGTH_LONG);
                toast.show();
            }
        } else
            finalizeMapConfiguration();

    }

    private void finalizeMapConfiguration(){
        // recover current location
        currentLatLng = UtilsGoogleMap.findLastCurrentLocation(sharedPreferences);

        // move camera to current location
        moveCameraToDefinedPosition(currentLatLng);

        // get properties close to current location
        getPropertiesCloseToTarget(currentLatLng);

        // configure markers on each property
        configureMapWithMarkers(currentLatLng);

        // If tablet mode, display list of properties
        if(modeDevice.equals(MODE_TABLET)){
            mapsActivity.configureAndShowListPropertiesFragment(MODE_DISPLAY_MAPS, listProperties);
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

    private void configureMapWithMarkers(LatLng cameraTarget){

        updateMarkers(listProperties);
        mapsActivity.setCameraTarget(cameraTarget);
        mapsActivity.setListProperties(listProperties);
        mapsActivity.getProgressBar().setVisibility(View.GONE);
    }

    private void updateMarkers(List<Property> listProp){
        createMarkerForEachProperty(listProp);
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

    private void createMarkerForEachProperty(List<Property> listProperties){

        listMarkers = new ArrayList<>();
        mMap.clear();
        MarkerOptions markerOptions;

        for(Property property : listProperties){

            LatLng position = new LatLng(property.getLat(),property.getLng());

            markerOptions = new MarkerOptions()
                    .position(position);

            Marker marker = mMap.addMarker(markerOptions);
            listMarkers.add(marker);

            marker.setTag(property.getId());
        }
    }

    public void centerToMarker(int idProp){

        if(listMarkers!=null) {
            for (Marker marker : listMarkers) {
                if (Objects.requireNonNull(marker.getTag()).toString().equals(String.valueOf(idProp)))
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                else
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
        }

        Property property = Utils.getPropertyFromList(idProp,listProperties);

        if(property!=null){
            LatLng position = new LatLng(property.getLat(),property.getLng());
            moveCameraToDefinedPosition(position);
        }
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
}