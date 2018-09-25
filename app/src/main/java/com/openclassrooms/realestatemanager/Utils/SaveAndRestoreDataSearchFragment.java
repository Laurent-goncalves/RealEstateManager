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