package com.openclassrooms.realestatemanager.Controllers.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.MapsActivity;
import com.openclassrooms.realestatemanager.Models.CallbackListProperties;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.Views.PropertiesRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListPropertiesFragment extends Fragment implements CallbackListProperties {

    private List<Property> listProperties;
    private CallbackListProperties callbackListProperties;
    private PropertiesRecyclerViewAdapter adapter;
    private BaseActivity baseActivity;
    private MapsActivity mapsActivity;
    private View view;
    private Context context;
    private static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    private static final String MODE_SEARCH = "mode_search";
    private static final String MODE_DISPLAY = "mode_display";
    private static final String MODE_SELECTED = "mode_selected";
    private static final String LIST_PROPERTIES_JSON = "list_properties_json";
    private static final String BUNDLE_MODE_SELECTED = "bundle_mode_selected";
    private String modeSelected;
    @BindView(R.id.list_properties_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fragment_list_properties) FrameLayout fragmentView;

    public ListPropertiesFragment() {
    }

    /*private Property PROPERTY_DEMO = new Property(0, "Appartment", 125000d,30.25d,1,
            "description","address","School, Subway",false,"01/06/2018","02/06/2018",0d,0d,"Eric",null,null);
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        callbackListProperties = this;
        baseActivity = (BaseActivity) getActivity();
        listProperties = new ArrayList<>();

        recoverModeSelected();

        if(modeSelected.equals(MODE_DISPLAY)){
            recoverListProperties(); // recover all properties
        } else {
            Gson gson = new Gson();
            String json = getArguments().getString(LIST_PROPERTIES_JSON,null);
            Type listPropType = new TypeToken<ArrayList<Property>>(){}.getType();
            listProperties = gson.fromJson(json,listPropType);
        }

        showDisplayFragment(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.property_item_list, container, false);
        ButterKnife.bind(this, view);
        configureListProperties();
        return view;
    }

    private void recoverListProperties(){

        listProperties = new ArrayList<>();

        PropertyContentProvider propertyContentProvider = new PropertyContentProvider();
        propertyContentProvider.setUtils(baseActivity.getApplicationContext(),true);

        final Cursor cursor = propertyContentProvider.query(null, null, null, null, null);

        if (cursor != null){
            if(cursor.getCount() >0){
                while (cursor.moveToNext()) {
                    listProperties.add(Property.getPropertyFromCursor(cursor));
                }
            }
            cursor.close();
        }
        baseActivity.setListProperties(listProperties);
    }

    private void configureListProperties(){

        // Set the adapter
        if(listProperties!=null){
            if (listProperties.size() > 0) {

                if(baseActivity!=null)
                    context = baseActivity.getApplicationContext();
                if(mapsActivity!=null)
                    context = mapsActivity.getApplicationContext();

                if(context!=null){

                    // Create adapter passing in the sample user data
                    PropertiesRecyclerViewAdapter adapter = new PropertiesRecyclerViewAdapter(listProperties,context, callbackListProperties, baseActivity, modeSelected);
                    // Attach the adapter to the recyclerview to populate items
                    recyclerView.setAdapter(adapter);
                    // Set layout manager to position the items
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                }
            } else {
                fragmentView.setBackgroundColor(Color.GRAY);
            }
        } else {
            fragmentView.setBackgroundColor(Color.GRAY);
        }
    }

    public void refreshListProperties(int position){

        // Set the adapter
        if(listProperties!=null){
            if (listProperties.size() > 0) {

                if (baseActivity != null)
                    context = baseActivity.getApplicationContext();
                if (mapsActivity != null)
                    context = mapsActivity.getApplicationContext();

                if (context != null) {

                    // Create adapter passing in the sample user data
                    if (baseActivity != null)
                        adapter = new PropertiesRecyclerViewAdapter(listProperties, context, position, callbackListProperties, baseActivity, modeSelected);
                    else if (mapsActivity != null)
                        adapter = new PropertiesRecyclerViewAdapter(listProperties, context, position, callbackListProperties, mapsActivity, modeSelected);

                    // Attach the adapter to the recyclerview to populate items
                    recyclerView.setAdapter(adapter);
                    // Set layout manager to position the items
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                }

            } else {
                fragmentView.setBackgroundColor(Color.GRAY);
            }
        } else {
            fragmentView.setBackgroundColor(Color.GRAY);
        }
    }

    @Override
    public void showDisplayFragment(int position) {
        if(listProperties!=null) {
            if (listProperties.size() > 0)
                baseActivity.configureAndShowDisplayFragment(modeSelected, listProperties.get(position).getId());
        }
    }

    @Override
    public void changeMarkerMap(int idProp) {
        if(modeSelected.equals(MODE_DISPLAY_MAPS)){
            mapsActivity.getGoogleMapUtils().centerToMarker(idProp);
        }
    }

    public void refresh(int idProp) {

        // Recover list of properties and position of the idProp
        recoverListProperties();
        int position = Utils.getIdPositionFromList(idProp, listProperties);

        listProperties.get(position).setSelected(true);

        // Configure the list of properties
        refreshListProperties(position);

        // Show property
        showDisplayFragment(position);
    }

    private void recoverModeSelected(){

        if(getArguments()!=null) {

            // if the mode of display is for mapsActivity, remove button return and icons in toolbar
            if (Objects.equals(getArguments().getString(BUNDLE_MODE_SELECTED), MODE_DISPLAY_MAPS)) {
                mapsActivity = (MapsActivity) getActivity();
                modeSelected = MODE_DISPLAY_MAPS;

            } else if (Objects.equals(getArguments().getString(BUNDLE_MODE_SELECTED), MODE_DISPLAY)) {
                baseActivity = (BaseActivity) getActivity();
                modeSelected = MODE_DISPLAY;

            } else if (Objects.equals(getArguments().getString(BUNDLE_MODE_SELECTED), MODE_SEARCH)) {
                baseActivity = (BaseActivity) getActivity();
                modeSelected = MODE_SEARCH;
            }
        }
    }
}
