package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Activities.MapsActivity;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.Models.SearchQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SaveAndRestoreDataListPropertiesFrag {

    private static final String BUNDLE_SEARCH_QUERY = "bundle_search_query";
    private static final String BUNDLE_LIST_PROPERTIES = "bundle_list_properties";
    private static final String BUNDLE_FRAG_DISPLAYED = "bundle_frag_displayed";
    private static final String BUNDLE_ITEM_LIST_SELECTED = "bundle_item_selected_in_the_list";
    private final static String BUNDLE_DEVICE = "bundle_device";
    private final static String MODE_TABLET = "mode_tablet";
    private final static String MODE_PHONE = "mode_phone";
    private static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    private static final String MODE_SEARCH = "mode_search";
    private static final String MODE_DISPLAY = "mode_display";
    private static final String BUNDLE_MODE_SELECTED = "bundle_mode_selected";
    private final static String DISPLAY_FRAG = "fragment_display";
    private static final String BUNDLE_CAMERA_TARGET_LAT = "bundle_camera_target_lat";
    private static final String BUNDLE_CAMERA_TARGET_LNG = "bundle_camera_target_lng";


    // ----------------------------------- CREATE BUNDLE

    public static Bundle createBundleForListPropertiesFragment(int itemSelected, String modeDevice, String modeSelected, String fragDisplayed, LatLng cameraTarget, SearchQuery searchQuery){

        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_ITEM_LIST_SELECTED, itemSelected);
        bundle.putString(BUNDLE_DEVICE, modeDevice);
        bundle.putString(BUNDLE_FRAG_DISPLAYED, fragDisplayed);
        bundle.putString(BUNDLE_SEARCH_QUERY, ConverterJSON.convertSearchQueryToJson(searchQuery));

        if(cameraTarget!=null) {
            bundle.putDouble(BUNDLE_CAMERA_TARGET_LAT, cameraTarget.latitude);
            bundle.putDouble(BUNDLE_CAMERA_TARGET_LNG, cameraTarget.longitude);
        }

        switch (modeSelected) {
            case MODE_DISPLAY:
                bundle.putString(BUNDLE_MODE_SELECTED, MODE_DISPLAY);
                break;
            case MODE_SEARCH: {
                bundle.putString(BUNDLE_MODE_SELECTED, MODE_SEARCH);
                break;
            }
            case MODE_DISPLAY_MAPS: {
                bundle.putString(BUNDLE_MODE_SELECTED, MODE_DISPLAY_MAPS);
                break;
            }
        }

        return bundle;
    }

    // ----------------------------------- SAVE DATA

    public static void saveDatas(Bundle outState, int itemSelected, String modeSelected, String fragmentDisplayed, String modeDevice){
        outState.putInt(BUNDLE_ITEM_LIST_SELECTED, itemSelected);
        outState.putString(BUNDLE_MODE_SELECTED, modeSelected);
        outState.putString(BUNDLE_FRAG_DISPLAYED, fragmentDisplayed);
        outState.putString(BUNDLE_DEVICE, modeDevice);
    }

    public static void saveDatas(Bundle outState, int itemSelected, String modeSelected, String fragmentDisplayed, String modeDevice, SearchQuery searchQuery){
        outState.putInt(BUNDLE_ITEM_LIST_SELECTED, itemSelected);
        outState.putString(BUNDLE_MODE_SELECTED, modeSelected);
        outState.putString(BUNDLE_FRAG_DISPLAYED, fragmentDisplayed);
        outState.putString(BUNDLE_DEVICE, modeDevice);
        outState.putString(BUNDLE_SEARCH_QUERY,ConverterJSON.convertSearchQueryToJson(searchQuery));
    }

    public static void saveDatas(Bundle outState, int itemSelected, String modeSelected, String fragmentDisplayed, String modeDevice, LatLng cameraTarget){
        outState.putInt(BUNDLE_ITEM_LIST_SELECTED, itemSelected);
        outState.putString(BUNDLE_MODE_SELECTED, modeSelected);
        outState.putString(BUNDLE_FRAG_DISPLAYED, fragmentDisplayed);
        outState.putString(BUNDLE_DEVICE, modeDevice);

        if(cameraTarget==null){
            outState.putDouble(BUNDLE_CAMERA_TARGET_LAT,0d);
            outState.putDouble(BUNDLE_CAMERA_TARGET_LNG,0d);
        } else {
            outState.putDouble(BUNDLE_CAMERA_TARGET_LAT,cameraTarget.latitude);
            outState.putDouble(BUNDLE_CAMERA_TARGET_LNG,cameraTarget.longitude);
        }
    }

    // ----------------------------------- RESTORE DATA

    public static void recoverDatasMainActivity(Bundle arguments, Bundle savedInstanceState, ListPropertiesFragment fragment, Context context, BaseActivityListener baseActivityListener){

        Bundle bundle = new Bundle();

        if(savedInstanceState!=null){
            bundle = savedInstanceState;
            fragment.setItemSelected(bundle.getInt(BUNDLE_ITEM_LIST_SELECTED,0));
        } else if (arguments!=null){
            bundle = arguments;
        }

        recoverListPropertiesMainActivity(bundle, fragment, context, baseActivityListener);
        recoverDeviceMode(bundle, fragment);
        recoverFragmentDisplayed(bundle,fragment);
    }

    public static void recoverDatasMapsActivity(Bundle arguments, Bundle savedInstanceState, ListPropertiesFragment fragment, Context context, BaseActivityListener baseActivityListener){

        Bundle bundle = new Bundle();

        if(savedInstanceState!=null){
            bundle = savedInstanceState;
            fragment.setItemSelected(bundle.getInt(BUNDLE_ITEM_LIST_SELECTED,0));
        } else if (arguments!=null){
            bundle = arguments;
        }

        recoverListPropertiesMapsActivity(bundle, fragment, context, baseActivityListener);
        recoverDeviceMode(bundle, fragment);
        recoverFragmentDisplayed(bundle,fragment);
    }

    public static void recoverDatasSearchActivity(Bundle arguments, Bundle savedInstanceState, ListPropertiesFragment fragment, Context context, BaseActivityListener baseActivityListener){

        Bundle bundle = new Bundle();

        if(savedInstanceState!=null){
            bundle = savedInstanceState;
            fragment.setItemSelected(bundle.getInt(BUNDLE_ITEM_LIST_SELECTED,0));
        } else if (arguments!=null){
            bundle = arguments;
        }

        recoverListPropertiesSearchActivity(bundle, fragment, context, baseActivityListener);
        recoverDeviceMode(bundle, fragment);
        recoverFragmentDisplayed(bundle,fragment);
    }

                        // ---------- LIST PROPERTIES
    public static void recoverListPropertiesMainActivity(Bundle bundle, ListPropertiesFragment fragment, Context context, BaseActivityListener baseActivityListener){

        List<Property> listProperties = new ArrayList<>();

        if(bundle!=null) {
            if (bundle.getString(BUNDLE_LIST_PROPERTIES, null) == null) {

                PropertyContentProvider propertyContentProvider = new PropertyContentProvider();
                propertyContentProvider.setUtils(context, true);

                final Cursor cursor = propertyContentProvider.query(null, null, null, null, null);

                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            listProperties.add(Property.getPropertyFromCursor(cursor));
                        }
                    }
                    cursor.close();
                }
            } else {
                listProperties = ConverterJSON.convertJsonToListProperty(bundle.getString(BUNDLE_LIST_PROPERTIES, null));
            }
        } else {
            PropertyContentProvider propertyContentProvider = new PropertyContentProvider();
            propertyContentProvider.setUtils(context, true);

            final Cursor cursor = propertyContentProvider.query(null, null, null, null, null);

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        listProperties.add(Property.getPropertyFromCursor(cursor));
                    }
                }
                cursor.close();
            }
        }

        baseActivityListener.setListProperties(listProperties);
        fragment.setListProperties(listProperties);
    }

    public static void recoverListPropertiesMapsActivity(Bundle bundle, ListPropertiesFragment fragment, Context context, BaseActivityListener baseActivityListener){

        List<Property> listProperties = new ArrayList<>();

        if(bundle!=null) {
            if (bundle.getString(BUNDLE_LIST_PROPERTIES, null) == null) {

                LatLng cameraTarget = new LatLng(bundle.getDouble(BUNDLE_CAMERA_TARGET_LAT),bundle.getDouble(BUNDLE_CAMERA_TARGET_LNG));

                List<Property> fullListProperties = new ArrayList<>(UtilsGoogleMap.getAllProperties(context));
                ConfigureMap configureMap = new ConfigureMap(context, null,null, bundle.getInt(BUNDLE_ITEM_LIST_SELECTED,0));
                configureMap.getPropertiesCloseToTarget(cameraTarget,fullListProperties);
                listProperties = configureMap.getListProperties();
                fragment.setCameraTarget(cameraTarget);

            } else {
                LatLng cameraTarget = new LatLng(bundle.getDouble(BUNDLE_CAMERA_TARGET_LAT),bundle.getDouble(BUNDLE_CAMERA_TARGET_LNG));
                fragment.setCameraTarget(cameraTarget);
                listProperties = ConverterJSON.convertJsonToListProperty(bundle.getString(BUNDLE_LIST_PROPERTIES, null));
            }
        }

        baseActivityListener.setListProperties(listProperties);
        fragment.setListProperties(listProperties);
    }

    public static void recoverListPropertiesSearchActivity(Bundle bundle, ListPropertiesFragment fragment, Context context, BaseActivityListener baseActivityListener){

        List<Property> listProperties = new ArrayList<>();

        if(bundle!=null) {
            if (bundle.getString(BUNDLE_LIST_PROPERTIES, null) == null) {

                  SearchQuery searchQuery = ConverterJSON.convertJsonToSearchQuery(bundle.getString(BUNDLE_SEARCH_QUERY));
                  LaunchSearchQuery launchSearchQuery = new LaunchSearchQuery(context,searchQuery);
                  listProperties = launchSearchQuery.getListProperties();
                  fragment.setSearchQuery(searchQuery);

            } else {
                SearchQuery searchQuery = ConverterJSON.convertJsonToSearchQuery(bundle.getString(BUNDLE_SEARCH_QUERY));
                fragment.setSearchQuery(searchQuery);
                listProperties = ConverterJSON.convertJsonToListProperty(bundle.getString(BUNDLE_LIST_PROPERTIES, null));
            }
        }

        baseActivityListener.setListProperties(listProperties);
        fragment.setListProperties(listProperties);
    }

                        // ---------- MODE SELECTED, DEVICE MODE, FRAG DISPLAYED

    public static void recoverModeSelected(Bundle arguments, Bundle savedInstanceState, ListPropertiesFragment fragment){

        Bundle bundle = new Bundle();

        if(savedInstanceState!=null){
            bundle = savedInstanceState;
        } else if (arguments!=null){
            bundle = arguments;
        }

        // if the mode of display is for mapsActivity, remove button return and icons in toolbar
        if (Objects.equals(bundle.getString(BUNDLE_MODE_SELECTED), MODE_DISPLAY_MAPS)) {
            fragment.setModeSelected(MODE_DISPLAY_MAPS);

        } else if (Objects.equals(bundle.getString(BUNDLE_MODE_SELECTED), MODE_DISPLAY)) {
            fragment.setModeSelected(MODE_DISPLAY);

        } else if (Objects.equals(bundle.getString(BUNDLE_MODE_SELECTED), MODE_SEARCH)) {
            fragment.setModeSelected(MODE_SEARCH);
        }
    }

    private static void recoverDeviceMode(Bundle bundle, ListPropertiesFragment fragment){

        if(bundle!=null) {
            if (bundle.getString(BUNDLE_DEVICE)!=null){
                if (Objects.equals(bundle.getString(BUNDLE_DEVICE), MODE_TABLET)) {
                    fragment.setModeDevice(MODE_TABLET);
                } else {
                    fragment.setModeDevice(MODE_PHONE);
                }
            } else {
                fragment.setModeDevice(MODE_PHONE);
            }
        } else {
            fragment.setModeDevice(MODE_PHONE);
        }
    }

    private static void recoverFragmentDisplayed(Bundle bundle, ListPropertiesFragment fragment){

        if(bundle!=null) {
            fragment.setFragmentDisplayed(bundle.getString(BUNDLE_FRAG_DISPLAYED, DISPLAY_FRAG));
        } else {
            fragment.setFragmentDisplayed(DISPLAY_FRAG);
        }
    }
}
