package com.openclassrooms.realestatemanager.Utils;

import android.os.Bundle;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.MapsActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.SearchActivity;
import com.openclassrooms.realestatemanager.Models.SearchQuery;


public class SaveAndRestoreDataActivity {

    private static final String BUNDLE_MODE_SELECTED = "bundle_mode_selected";
    private static final String BUNDLE_FRAG_DISPLAYED = "bundle_frag_displayed";
    private static final String BUNDLE_ID_PROPERTY_SELECTED = "bundle_id_property_selected";
    private static final String BUNDLE_LAST_ID_PROPERTY_DISPLAYED = "bundle_last_id_property_displayed";
    private static final String BUNDLE_CAMERA_TARGET_LAT = "bundle_camera_target_lat";
    private static final String BUNDLE_CAMERA_TARGET_LNG = "bundle_camera_target_lng";
    private static final String BUNDLE_SEARCH_QUERY = "bundle_search_query";

    // ----------------------------------- SAVE DATA

    public static void SaveDataActivity(String modeSelected, String fragmentDisplayed, int idProperty, int lastIdPropertyDisplayed, Bundle savedInstanceState) {
        savedInstanceState.putString(BUNDLE_MODE_SELECTED,modeSelected);
        savedInstanceState.putString(BUNDLE_FRAG_DISPLAYED,fragmentDisplayed);
        savedInstanceState.putInt(BUNDLE_ID_PROPERTY_SELECTED,idProperty);
        savedInstanceState.putInt(BUNDLE_LAST_ID_PROPERTY_DISPLAYED,lastIdPropertyDisplayed);
    }

    public static void SaveDataActivity(String modeSelected, String fragmentDisplayed, int idProperty, int lastIdPropertyDisplayed, SearchQuery searchQuery, Bundle savedInstanceState) {
        savedInstanceState.putString(BUNDLE_MODE_SELECTED,modeSelected);
        savedInstanceState.putString(BUNDLE_FRAG_DISPLAYED,fragmentDisplayed);
        savedInstanceState.putString(BUNDLE_SEARCH_QUERY,ConverterJSON.convertSearchQueryToJson(searchQuery));
        savedInstanceState.putInt(BUNDLE_ID_PROPERTY_SELECTED,idProperty);
        savedInstanceState.putInt(BUNDLE_LAST_ID_PROPERTY_DISPLAYED,lastIdPropertyDisplayed);
    }

    public static void SaveDataActivity(String modeSelected, String fragmentDisplayed, int idProperty, LatLng cameraTarget, int lastIdPropertyDisplayed, Bundle savedInstanceState) {
        savedInstanceState.putString(BUNDLE_MODE_SELECTED,modeSelected);
        savedInstanceState.putString(BUNDLE_FRAG_DISPLAYED,fragmentDisplayed);
        savedInstanceState.putInt(BUNDLE_ID_PROPERTY_SELECTED,idProperty);
        savedInstanceState.putInt(BUNDLE_LAST_ID_PROPERTY_DISPLAYED,lastIdPropertyDisplayed);

        if(cameraTarget==null){
            savedInstanceState.putDouble(BUNDLE_CAMERA_TARGET_LAT,0d);
            savedInstanceState.putDouble(BUNDLE_CAMERA_TARGET_LNG,0d);
        } else {
            savedInstanceState.putDouble(BUNDLE_CAMERA_TARGET_LAT,cameraTarget.latitude);
            savedInstanceState.putDouble(BUNDLE_CAMERA_TARGET_LNG,cameraTarget.longitude);
        }

    }

    // ----------------------------------- RESTORE DATA

    public static void RestoreDataActivity(Bundle savedInstanceState, MainActivity mainActivity){
        mainActivity.setCurrentPositionDisplayed(savedInstanceState.getInt(BUNDLE_ID_PROPERTY_SELECTED));
        mainActivity.setFragmentDisplayed(savedInstanceState.getString(BUNDLE_FRAG_DISPLAYED));
        mainActivity.setModeSelected(savedInstanceState.getString(BUNDLE_MODE_SELECTED));
        mainActivity.setLastIdPropertyDisplayed(savedInstanceState.getInt(BUNDLE_LAST_ID_PROPERTY_DISPLAYED));
    }

    public static void RestoreDataActivity(Bundle savedInstanceState, MapsActivity mapsActivity){

        LatLng cameraTarget = new LatLng(savedInstanceState.getDouble(BUNDLE_CAMERA_TARGET_LAT),
                                         savedInstanceState.getDouble(BUNDLE_CAMERA_TARGET_LNG));
        mapsActivity.setCameraTarget(cameraTarget);
        mapsActivity.setCurrentPositionDisplayed(savedInstanceState.getInt(BUNDLE_ID_PROPERTY_SELECTED));
        mapsActivity.setFragmentDisplayed(savedInstanceState.getString(BUNDLE_FRAG_DISPLAYED));
        mapsActivity.setModeSelected(savedInstanceState.getString(BUNDLE_MODE_SELECTED));
        mapsActivity.setLastIdPropertyDisplayed(savedInstanceState.getInt(BUNDLE_LAST_ID_PROPERTY_DISPLAYED));
    }

    public static void RestoreDataActivity(Bundle savedInstanceState, SearchActivity searchActivity){
        searchActivity.setLastIdPropertyDisplayed(savedInstanceState.getInt(BUNDLE_LAST_ID_PROPERTY_DISPLAYED));
        searchActivity.setCurrentPositionDisplayed(savedInstanceState.getInt(BUNDLE_ID_PROPERTY_SELECTED));
        searchActivity.setFragmentDisplayed(savedInstanceState.getString(BUNDLE_FRAG_DISPLAYED));
        searchActivity.setModeSelected(savedInstanceState.getString(BUNDLE_MODE_SELECTED));
        searchActivity.setSearchQuery(ConverterJSON.convertJsonToSearchQuery(savedInstanceState.getString(BUNDLE_SEARCH_QUERY)));
    }
}