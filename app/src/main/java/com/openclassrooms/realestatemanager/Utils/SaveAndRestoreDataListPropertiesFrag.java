package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.Models.SearchQuery;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class SaveAndRestoreDataListPropertiesFrag {

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

    // ----------------------------------- SAVE DATA

    public static void saveDatas(Bundle outState, int itemSelected, String modeSelected, String fragmentDisplayed, String modeDevice){
        outState.putInt(BUNDLE_ITEM_LIST_SELECTED, itemSelected);
        outState.putString(BUNDLE_MODE_SELECTED, modeSelected);
        outState.putString(BUNDLE_FRAG_DISPLAYED, fragmentDisplayed);
        outState.putString(BUNDLE_DEVICE, modeDevice);
    }

    // ----------------------------------- RESTORE DATA

    public static void recoverDatas(Bundle arguments, Bundle savedInstanceState, ListPropertiesFragment fragment, Context context, BaseActivityListener baseActivityListener){

        Bundle bundle = new Bundle();

        if(savedInstanceState!=null){
            bundle = savedInstanceState;
            fragment.setItemSelected(bundle.getInt(BUNDLE_ITEM_LIST_SELECTED));
        } else if (arguments!=null){
            bundle = arguments;
        }

        recoverListProperties(bundle, fragment, context, baseActivityListener);
        recoverModeSelected(bundle, fragment);
        recoverDeviceMode(bundle, fragment);
        recoverFragmentDisplayed(bundle,fragment);
        recoverPositionItemSelected(bundle, fragment);
    }

    private static void recoverListProperties(Bundle bundle, ListPropertiesFragment fragment, Context context, BaseActivityListener baseActivityListener){

        List<Property> listProperties = new ArrayList<>();

        if(bundle.getString(BUNDLE_LIST_PROPERTIES,null)==null){

            PropertyContentProvider propertyContentProvider = new PropertyContentProvider();
            propertyContentProvider.setUtils(context,true);

            final Cursor cursor = propertyContentProvider.query(null, null, null, null, null);

            if (cursor != null){
                if(cursor.getCount() >0){
                    while (cursor.moveToNext()) {
                        listProperties.add(Property.getPropertyFromCursor(cursor));
                    }
                }
                cursor.close();
            }
        } else {
            listProperties = ConverterJSON.convertJsonToListProperty(bundle.getString(BUNDLE_LIST_PROPERTIES,null));
        }

        baseActivityListener.setListProperties(listProperties);
        fragment.setListProperties(listProperties);
    }

    private static void recoverModeSelected(Bundle bundle, ListPropertiesFragment fragment){

        if(bundle!=null) {

            // if the mode of display is for mapsActivity, remove button return and icons in toolbar
            if (Objects.equals(bundle.getString(BUNDLE_MODE_SELECTED), MODE_DISPLAY_MAPS)) {
                fragment.setModeSelected(MODE_DISPLAY_MAPS);

            } else if (Objects.equals(bundle.getString(BUNDLE_MODE_SELECTED), MODE_DISPLAY)) {
                fragment.setModeSelected(MODE_DISPLAY);

            } else if (Objects.equals(bundle.getString(BUNDLE_MODE_SELECTED), MODE_SEARCH)) {
                fragment.setModeSelected(MODE_SEARCH);
            }
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

    private static void recoverPositionItemSelected(Bundle bundle, ListPropertiesFragment fragment){

        if(bundle!=null) {
            fragment.setItemSelected(bundle.getInt(BUNDLE_ITEM_LIST_SELECTED, -1));
        } else {
            fragment.setItemSelected(-1);
        }
    }


}
