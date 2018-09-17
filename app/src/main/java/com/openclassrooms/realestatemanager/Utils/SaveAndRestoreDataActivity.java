package com.openclassrooms.realestatemanager.Utils;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.MapsActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.SearchActivity;
import com.openclassrooms.realestatemanager.Models.Property;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SaveAndRestoreDataActivity {

    private static final String BUNDLE_MODE_SELECTED = "bundle_mode_selected";
    private static final String BUNDLE_FRAG_DISPLAYED = "bundle_frag_displayed";
    private static final String BUNDLE_LIST_PROPERTIES = "bundle_list_properties";
    private static final String BUNDLE_ID_PROPERTY_SELECTED = "bundle_id_property_selected";
    private static final String BUNDLE_CAMERA_TARGET_LAT = "bundle_camera_target_lat";
    private static final String BUNDLE_CAMERA_TARGET_LNG = "bundle_camera_target_lng";

    // ----------------------------------- SAVE DATA

    public static void SaveDataActivity(String modeSelected, String fragmentDisplayed, List<Property> listProperties, int idProperty, Bundle savedInstanceState) {
        savedInstanceState.putString(BUNDLE_MODE_SELECTED,modeSelected);
        savedInstanceState.putString(BUNDLE_FRAG_DISPLAYED,fragmentDisplayed);
        savedInstanceState.putInt(BUNDLE_ID_PROPERTY_SELECTED,idProperty);

        Gson gson = new Gson();
        String listPropertiesJson = gson.toJson(listProperties);
        savedInstanceState.putString(BUNDLE_LIST_PROPERTIES, listPropertiesJson);
    }

    public static void SaveDataActivity(String modeSelected, String fragmentDisplayed, List<Property> listProperties, int idProperty, LatLng cameraTarget, Bundle savedInstanceState) {
        savedInstanceState.putString(BUNDLE_MODE_SELECTED,modeSelected);
        savedInstanceState.putString(BUNDLE_FRAG_DISPLAYED,fragmentDisplayed);
        savedInstanceState.putInt(BUNDLE_ID_PROPERTY_SELECTED,idProperty);
        savedInstanceState.putDouble(BUNDLE_CAMERA_TARGET_LAT,cameraTarget.latitude);
        savedInstanceState.putDouble(BUNDLE_CAMERA_TARGET_LNG,cameraTarget.longitude);
        savedInstanceState.putString(BUNDLE_LIST_PROPERTIES, ConverterJSON.convertListPropertyToJson(listProperties));
    }

    // ----------------------------------- RESTORE DATA

    public static void RestoreDataActivity(Bundle savedInstanceState, MainActivity mainActivity){
        mainActivity.setCurrentPositionDisplayed(savedInstanceState.getInt(BUNDLE_ID_PROPERTY_SELECTED));
        mainActivity.setListProperties(ConverterJSON.convertJsonToListProperty(savedInstanceState.getString(BUNDLE_LIST_PROPERTIES)));
        mainActivity.setFragmentDisplayed(savedInstanceState.getString(BUNDLE_FRAG_DISPLAYED));
        mainActivity.setModeSelected(savedInstanceState.getString(BUNDLE_MODE_SELECTED));
    }

    public static void RestoreDataActivity(Bundle savedInstanceState, MapsActivity mapsActivity){

        LatLng cameraTarget = new LatLng(savedInstanceState.getDouble(BUNDLE_CAMERA_TARGET_LAT),
                                         savedInstanceState.getDouble(BUNDLE_CAMERA_TARGET_LNG));
        mapsActivity.setCameraTarget(cameraTarget);
        mapsActivity.setCurrentPositionDisplayed(savedInstanceState.getInt(BUNDLE_ID_PROPERTY_SELECTED));
        mapsActivity.setListProperties(ConverterJSON.convertJsonToListProperty(savedInstanceState.getString(BUNDLE_LIST_PROPERTIES)));
        mapsActivity.setFragmentDisplayed(savedInstanceState.getString(BUNDLE_FRAG_DISPLAYED));
        mapsActivity.setModeSelected(savedInstanceState.getString(BUNDLE_MODE_SELECTED));
    }

    public static void RestoreDataActivity(Bundle savedInstanceState, SearchActivity searchActivity){
        searchActivity.setCurrentPositionDisplayed(savedInstanceState.getInt(BUNDLE_ID_PROPERTY_SELECTED));
        searchActivity.setListProperties(ConverterJSON.convertJsonToListProperty(savedInstanceState.getString(BUNDLE_LIST_PROPERTIES)));
        searchActivity.setFragmentDisplayed(savedInstanceState.getString(BUNDLE_FRAG_DISPLAYED));
        searchActivity.setModeSelected(savedInstanceState.getString(BUNDLE_MODE_SELECTED));
    }
}