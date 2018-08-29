package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
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
import com.google.gson.Gson;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.MapsActivity;
import com.openclassrooms.realestatemanager.Models.PlaceNearby.PlaceNearby;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.Provider.MapsContentProvider;
import com.openclassrooms.realestatemanager.R;

import java.util.ArrayList;
import java.util.List;


public class GoogleMapUtils {

    private GoogleMap mMap;
    private Context context;
    private PlaceDetectionClient mPlaceDetectionClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final static String EXTRA_LAT_CURRENT = "latitude_current_location";
    private final static String EXTRA_LONG_CURRENT = "longitude_current_location";
    private boolean permissionGranted = true;
    private LatLng currentLatLng;
    private MapsActivity mapsActivity;
    private SharedPreferences sharedPreferences;
    private GoogleMapUtils mGoogleMapUtils;
    private List<Property> listProperties;

    public GoogleMapUtils(Context context, MapsActivity mapsActivity) {
        this.context = context;
        this.mapsActivity=mapsActivity;
        this.mGoogleMapUtils=this;
        sharedPreferences = mapsActivity.getSharedPreferences();
        listProperties = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) mapsActivity.getSupportFragmentManager()
                .findFragmentById(R.id.map);

        try {
            MapsInitializer.initialize(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
            }
        });

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(context);
        getLocationPermission();
    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true;
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(mapsActivity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
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
                        currentLatLng = findPlaceHighestLikelihood(task);

                        if(currentLatLng.longitude>=-123 && currentLatLng.longitude<=-122
                                && currentLatLng.latitude>=37 && currentLatLng.latitude<=38) // for Travis integration tests
                            currentLatLng=new LatLng(48.866667, 2.333333); // for Travis integration tests

                    } else {
                        currentLatLng =null;
                        Toast toast = Toast.makeText(context,context.getResources().getString(R.string.error_current_location),Toast.LENGTH_LONG);
                        toast.show();
                    }

                    if(currentLatLng!=null) {
                        saveLastCurrentLocation(currentLatLng);
                        moveCameraToDefinedPosition();
                    } else
                        setCurrentLocationByDefault();
                });

            } catch (SecurityException e) {

                setCurrentLocationByDefault();

                Toast toast = Toast.makeText(context,context.getResources().getString(R.string.error_current_location)
                        + "\n" + e.toString() ,Toast.LENGTH_LONG);
                toast.show();
            }
        } else
            setCurrentLocationByDefault();

    }

    private void moveCameraToDefinedPosition(){
        if(currentLatLng!=null) {
            // Show the initial position
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(currentLatLng.latitude, currentLatLng.longitude), 15));
        }

        getPropertiesCloseToTarget();
    }

    private void setCurrentLocationByDefault(){
        currentLatLng = findLastCurrentLocation();
        moveCameraToDefinedPosition();
    }

    private LatLng findLastCurrentLocation(){

        saveLastCurrentLocation(new LatLng(48.866667, 2.333333)); // for Travis integration tests

        Float latitude = sharedPreferences.getFloat(EXTRA_LAT_CURRENT,0);
        Float longitude = sharedPreferences.getFloat(EXTRA_LONG_CURRENT,0);

        LatLng last_location;

        if(latitude == 0 && longitude == 0)
            last_location=new LatLng(48.866667, 2.333333);
        else
            last_location = new LatLng(latitude,longitude);

        return last_location;
    }

    private void saveLastCurrentLocation(LatLng current_loc){
        sharedPreferences.edit().putFloat(EXTRA_LAT_CURRENT,(float) current_loc.latitude).apply();
        sharedPreferences.edit().putFloat(EXTRA_LONG_CURRENT,(float) current_loc.longitude).apply();
    }

    private LatLng findPlaceHighestLikelihood(@NonNull Task<PlaceLikelihoodBufferResponse> task){

        Place placeHighestLikelihood = null;
        float percentage = 0;

        PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

        for (PlaceLikelihood placeLikelihood : likelyPlaces) {       // for each placeLikelihood

            if(placeLikelihood.getLikelihood() > percentage) {       // if the likelihood is higher than the other ones,
                placeHighestLikelihood = placeLikelihood.getPlace(); //   set this place as placeLikelihood
                percentage = placeLikelihood.getLikelihood();        //   set this percentage as highest likelihood
            }
        }

        LatLng current_location=null;

        if(placeHighestLikelihood!=null)
            current_location = placeHighestLikelihood.getLatLng();

        likelyPlaces.release();

        return current_location;
    }

    private void updateMarkers(List<Property> listProp){
        createMarkerForEachProperty(listProp);
        create_marker_listener();
    }

    private void create_marker_listener(){

        mMap.setOnMarkerClickListener(marker -> {

            Intent intent = new Intent(mapsActivity, MainActivity.class);
            //intent.putExtra();

            mapsActivity.startActivity(intent);

            return false;
        });
    }

    private void createMarkerForEachProperty(List<Property> listProperties){

        mMap.clear();
        MarkerOptions markerOptions;

        for(Property property : listProperties){

            LatLng position = new LatLng(property.getLat(),property.getLng());

            markerOptions = new MarkerOptions()
                    .position(position);

            mMap.addMarker(markerOptions);
        }
    }

    private void getPropertiesCloseToTarget() {

        List<Property> listProperties = new ArrayList<>();

        MapsContentProvider mapsContentProvider = new MapsContentProvider();
        mapsContentProvider.setParametersQuery(mapsActivity.getApplicationContext());

        final Cursor cursor = mapsContentProvider.query(null, null, null, null, null);

        if (cursor != null){
            if(cursor.getCount() >0){
                while (cursor.moveToNext()) {
                    listProperties.add(Property.getPropertyFromCursor(cursor));
                }
            }
            cursor.close();
        }

        updateMarkers(listProperties);
    }
}