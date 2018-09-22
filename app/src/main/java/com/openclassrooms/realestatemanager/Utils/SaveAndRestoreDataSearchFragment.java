package com.openclassrooms.realestatemanager.Utils;

import android.os.Bundle;
import com.openclassrooms.realestatemanager.Controllers.Fragments.SearchFragment;
import com.openclassrooms.realestatemanager.Models.SearchQuery;
import com.openclassrooms.realestatemanager.R;
import java.util.Arrays;


public class SaveAndRestoreDataSearchFragment {

    private static final String BUNDLE_MODE_SELECTED = "bundle_mode_selected";
    private static final String BUNDLE_DEVICE = "bundle_device";
    private static final String BUNDLE_SEARCH_QUERY = "bundle_search_query";
    private static final String MODE_SEARCH = "mode_search";

    // ----------------------------------- CREATE BUNDLE

    public static Bundle createBundleForSearchFragment(String modeDevice, SearchQuery searchQuery){

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_DEVICE, modeDevice);
        bundle.putString(BUNDLE_MODE_SELECTED, MODE_SEARCH);
        bundle.putString(BUNDLE_SEARCH_QUERY, ConverterJSON.convertSearchQueryToJson(searchQuery));

        return bundle;
    }

    // ----------------------------------- SAVE DATA

    public static void saveDatas(Bundle outState, SearchQuery query){
        outState.putString(BUNDLE_SEARCH_QUERY, ConverterJSON.convertSearchQueryToJson(query));
    }

    // ----------------------------------- RESTORE DATA

    public static void recoverDatas(Bundle arguments, Bundle savedInstanceState, SearchFragment fragment){

        Bundle bundle = new Bundle();

        if(savedInstanceState!=null){
            bundle = savedInstanceState;
        } else if (arguments!=null){
            bundle = arguments;
        }

        SearchQuery searchQuery = ConverterJSON.convertJsonToSearchQuery(bundle.getString(BUNDLE_SEARCH_QUERY));
        if(searchQuery==null)
            fragment.setSearchQuery(new SearchQuery(false,
                    Arrays.asList(fragment.getContext().getResources().getStringArray(R.array.type_property)).get(0),
                    null,null,0d,0d,0d,0d,0,
                    0d,0d,null, Integer.parseInt(fragment.getContext().getResources().getString(R.string.radius))));
        else
            fragment.setSearchQuery(searchQuery);
    }
}

/*


    private static final String BUNDLE_TYPE_PROPERTY = "bundle_type_property";
    private static final String BUNDLE_SOLD_STATUS = "bundle_sold_status";
    private static final String BUNDLE_PUBLISH_DATE_START = "BUNDLE_PUBLISH_DATE_START";
    private static final String BUNDLE_PUBLISH_DATE_END = "BUNDLE_PUBLISH_DATE_END";
    private static final String BUNDLE_PRICE_INF = "bundle_price_inf";
    private static final String BUNDLE_PRICE_SUP = "bundle_price_sup";
    private static final String BUNDLE_SURFACE_INF = "bundle_surface_inf";
    private static final String BUNDLE_SURFACE_SUP = "bundle_surface_sup";
    private static final String BUNDLE_ROOM_NB_MIN = "bundle_room_number_inf";
    private static final String BUNDLE_ADDRESS_LAT = "bundle_address_lat";
    private static final String BUNDLE_ADDRESS_LNG = "bundle_address_lng";
    private static final String BUNDLE_ADDRESS = "bundle_address";
    private static final String BUNDLE_RADIUS = "bundle_radius";




        outState.putBoolean(BUNDLE_SOLD_STATUS, query.getSoldStatus());
        outState.putString(BUNDLE_TYPE_PROPERTY, query.getTypeProperty());

        outState.putString(BUNDLE_PUBLISH_DATE_START, query.getDatePublishStart());
        outState.putString(BUNDLE_PUBLISH_DATE_END, query.getDatePublishEnd());

        outState.putDouble(BUNDLE_PRICE_INF, query.getPriceInf());
        outState.putDouble(BUNDLE_PRICE_SUP, query.getPriceSup());

        outState.putDouble(BUNDLE_SURFACE_INF, query.getSurfaceInf());
        outState.putDouble(BUNDLE_SURFACE_SUP, query.getSurfaceSup());

        outState.putInt(BUNDLE_ROOM_NB_MIN, query.getRoomNbMin());

        outState.putDouble(BUNDLE_ADDRESS_LAT, query.getSearchLocLat());
        outState.putDouble(BUNDLE_ADDRESS_LNG, query.getSearchLocLng());
        outState.putString(BUNDLE_ADDRESS, query.getAddress());
        outState.putInt(BUNDLE_RADIUS, query.getRadius());




        fragment.getSearchQuery().setSoldStatus(bundle.getBoolean(BUNDLE_SOLD_STATUS,false));

        String DefaultTypeProperty = fragment.getContext().getResources().getStringArray(R.array.type_property)[0];
        fragment.getSearchQuery().setTypeProperty(bundle.getString(BUNDLE_TYPE_PROPERTY,DefaultTypeProperty));

        fragment.getSearchQuery().setDatePublishStart(bundle.getString(BUNDLE_PUBLISH_DATE_START));
        fragment.getSearchQuery().setDatePublishEnd(bundle.getString(BUNDLE_PUBLISH_DATE_END));

        fragment.getSearchQuery().setPriceInf(bundle.getDouble(BUNDLE_PRICE_INF));
        fragment.getSearchQuery().setPriceSup(bundle.getDouble(BUNDLE_PRICE_SUP));

        fragment.getSearchQuery().setSurfaceInf(bundle.getDouble(BUNDLE_SURFACE_INF));
        fragment.getSearchQuery().setSurfaceSup(bundle.getDouble(BUNDLE_SURFACE_SUP));

        fragment.getSearchQuery().setRoomNbMin(bundle.getInt(BUNDLE_ROOM_NB_MIN,0));

        fragment.getSearchQuery().setSearchLocLat(bundle.getDouble(BUNDLE_ADDRESS_LAT, 0d));
        fragment.getSearchQuery().setSearchLocLng(bundle.getDouble(BUNDLE_ADDRESS_LNG,0d));
        fragment.getSearchQuery().setAddress(bundle.getString(BUNDLE_ADDRESS));
        fragment.getSearchQuery().setRadius(bundle.getInt(BUNDLE_RADIUS,1000));

 */