package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
    private static final String BUNDLE_ID_PROP_LIST_SELECTED = "bundle_id_property_in_the_list";
    private final static String BUNDLE_DEVICE = "bundle_device";
    private final static String MODE_TABLET = "mode_tablet";
    private final static String MODE_PHONE = "mode_phone";
    private static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    private static final String MODE_SEARCH = "mode_search";
    private static final String MODE_DISPLAY = "mode_display";
    private static final String BUNDLE_MODE_SELECTED = "bundle_mode_selected";
    private final static String DISPLAY_FRAG = "fragment_display";
    private static final String BUNDLE_CAMERA_TARGET_NE_LAT = "bundle_camera_target_north_east_lat";
    private static final String BUNDLE_CAMERA_TARGET_NE_LNG = "bundle_camera_target_north_east_lng";
    private static final String BUNDLE_CAMERA_TARGET_SW_LAT = "bundle_camera_target_south_west_lat";
    private static final String BUNDLE_CAMERA_TARGET_SW_LNG = "bundle_camera_target_south_west_lng";


    // ----------------------------------- CREATE BUNDLE

    public static Bundle createBundleForListPropertiesFragment(int idProperty, String modeDevice, String modeSelected, String fragDisplayed, LatLngBounds camBounds, SearchQuery searchQuery){

        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_ID_PROP_LIST_SELECTED, idProperty);
        bundle.putString(BUNDLE_DEVICE, modeDevice);
        bundle.putString(BUNDLE_FRAG_DISPLAYED, fragDisplayed);
        bundle.putString(BUNDLE_SEARCH_QUERY, ConverterJSON.convertSearchQueryToJson(searchQuery));

        if(camBounds!=null) {
            bundle.putDouble(BUNDLE_CAMERA_TARGET_NE_LAT, camBounds.northeast.latitude);
            bundle.putDouble(BUNDLE_CAMERA_TARGET_NE_LNG, camBounds.northeast.longitude);
            bundle.putDouble(BUNDLE_CAMERA_TARGET_SW_LAT, camBounds.southwest.latitude);
            bundle.putDouble(BUNDLE_CAMERA_TARGET_SW_LNG, camBounds.southwest.longitude);
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

    public static void saveDatas(Bundle outState, int idProperty, String modeSelected, String fragmentDisplayed, String modeDevice){
        outState.putInt(BUNDLE_ID_PROP_LIST_SELECTED, idProperty);
        outState.putString(BUNDLE_MODE_SELECTED, modeSelected);
        outState.putString(BUNDLE_FRAG_DISPLAYED, fragmentDisplayed);
        outState.putString(BUNDLE_DEVICE, modeDevice);
    }

    public static void saveDatas(Bundle outState, int idProperty, String modeSelected, String fragmentDisplayed, String modeDevice, SearchQuery searchQuery){
        outState.putInt(BUNDLE_ID_PROP_LIST_SELECTED, idProperty);
        outState.putString(BUNDLE_MODE_SELECTED, modeSelected);
        outState.putString(BUNDLE_FRAG_DISPLAYED, fragmentDisplayed);
        outState.putString(BUNDLE_DEVICE, modeDevice);
        outState.putString(BUNDLE_SEARCH_QUERY,ConverterJSON.convertSearchQueryToJson(searchQuery));
    }

    public static void saveDatas(Bundle outState, int idProperty, String modeSelected, String fragmentDisplayed, String modeDevice, LatLngBounds camBounds){
        outState.putInt(BUNDLE_ID_PROP_LIST_SELECTED, idProperty);
        outState.putString(BUNDLE_MODE_SELECTED, modeSelected);
        outState.putString(BUNDLE_FRAG_DISPLAYED, fragmentDisplayed);
        outState.putString(BUNDLE_DEVICE, modeDevice);

        if(camBounds==null){
            outState.putDouble(BUNDLE_CAMERA_TARGET_NE_LAT,0d);
            outState.putDouble(BUNDLE_CAMERA_TARGET_NE_LNG,0d);
            outState.putDouble(BUNDLE_CAMERA_TARGET_SW_LAT,0d);
            outState.putDouble(BUNDLE_CAMERA_TARGET_SW_LNG,0d);
        } else {
            outState.putDouble(BUNDLE_CAMERA_TARGET_NE_LAT,camBounds.northeast.latitude);
            outState.putDouble(BUNDLE_CAMERA_TARGET_NE_LNG,camBounds.northeast.longitude);
            outState.putDouble(BUNDLE_CAMERA_TARGET_SW_LAT,camBounds.southwest.latitude);
            outState.putDouble(BUNDLE_CAMERA_TARGET_SW_LNG,camBounds.southwest.longitude);
        }
    }

    // ----------------------------------- RESTORE DATA

    public static void recoverDatasMainActivity(Bundle arguments, Bundle savedInstanceState, ListPropertiesFragment fragment, Context context, BaseActivityListener baseActivityListener){

        Bundle bundle = new Bundle();

        if(savedInstanceState!=null){
            bundle = savedInstanceState;
        } else if (arguments!=null){
            bundle = arguments;
        }

        recoverListPropertiesMainActivity(bundle, fragment, context, baseActivityListener);
        recoverItemSelectedInList(bundle, fragment);
        recoverDeviceMode(bundle, fragment);
        recoverFragmentDisplayed(bundle, fragment);
    }

    public static void recoverDatasMapsActivity(Bundle arguments, Bundle savedInstanceState, ListPropertiesFragment fragment, Context context, BaseActivityListener baseActivityListener){

        Bundle bundle = new Bundle();

        if(savedInstanceState!=null){
            bundle = savedInstanceState;
        } else if (arguments!=null){
            bundle = arguments;
        }

        recoverListPropertiesMapsActivity(bundle, fragment, context, baseActivityListener);
        recoverItemSelectedInList(bundle, fragment);
        recoverDeviceMode(bundle, fragment);
        recoverFragmentDisplayed(bundle,fragment);
    }

    public static void recoverDatasSearchActivity(Bundle arguments, Bundle savedInstanceState, ListPropertiesFragment fragment, Context context, BaseActivityListener baseActivityListener){

        Bundle bundle = new Bundle();

        if(savedInstanceState!=null){
            bundle = savedInstanceState;
        } else if (arguments!=null){
            bundle = arguments;
        }

        recoverListPropertiesSearchActivity(bundle, fragment, context, baseActivityListener);
        recoverItemSelectedInList(bundle, fragment);
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
            LatLng camBoundsNE = new LatLng(bundle.getDouble(BUNDLE_CAMERA_TARGET_NE_LAT),bundle.getDouble(BUNDLE_CAMERA_TARGET_NE_LNG));
            LatLng camBoundsSW= new LatLng(bundle.getDouble(BUNDLE_CAMERA_TARGET_SW_LAT),bundle.getDouble(BUNDLE_CAMERA_TARGET_SW_LNG));
            LatLngBounds camBounds = new LatLngBounds(camBoundsSW,camBoundsNE);

            List<Property> fullListProperties = new ArrayList<>(UtilsGoogleMap.getAllProperties(context));
            ConfigureMap configureMap = new ConfigureMap(context, null,null, bundle.getInt(BUNDLE_ID_PROP_LIST_SELECTED));
            configureMap.getPropertiesCloseToTarget(camBounds,fullListProperties);
            listProperties = configureMap.getListProperties();
            fragment.setListProperties(listProperties);
            fragment.setCameraBounds(camBounds);
        }

        baseActivityListener.setListProperties(listProperties);
        fragment.setListProperties(listProperties);
    }

    public static void recoverListPropertiesSearchActivity(Bundle bundle, ListPropertiesFragment fragment, Context context, BaseActivityListener baseActivityListener){

        List<Property> listProperties = new ArrayList<>();

        if(bundle!=null) {
              SearchQuery searchQuery = ConverterJSON.convertJsonToSearchQuery(bundle.getString(BUNDLE_SEARCH_QUERY));
              LaunchSearchQuery launchSearchQuery = new LaunchSearchQuery(context,searchQuery);
              listProperties = launchSearchQuery.getListProperties();
              fragment.setSearchQuery(searchQuery);
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

    private static void recoverItemSelectedInList(Bundle bundle, ListPropertiesFragment fragment) {
        int idProperty = bundle.getInt(BUNDLE_ID_PROP_LIST_SELECTED,-1);
        List<Property> listProperties=fragment.getListProperties();
        int position = Utils.getIndexPropertyFromList(idProperty,listProperties);
        fragment.setItemSelected(position);
    }
}
