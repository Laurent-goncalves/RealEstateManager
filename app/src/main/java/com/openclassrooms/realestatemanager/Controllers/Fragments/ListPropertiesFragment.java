package com.openclassrooms.realestatemanager.Controllers.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.openclassrooms.realestatemanager.Controllers.Activities.MapsActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.SearchActivity;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.CallbackListProperties;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataListPropertiesFrag;
import com.openclassrooms.realestatemanager.Views.PropertiesRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ListPropertiesFragment extends Fragment implements CallbackListProperties {

    private List<Property> listProperties;
    private CallbackListProperties callbackListProperties;
    private MapsActivity mapsActivity;
    private SearchActivity searchActivity;
    private Context context;
    private String modeDevice;
    private String fragmentDisplayed;
    private BaseActivityListener baseActivityListener;
    private final static String MODE_TABLET = "mode_tablet";
    private static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    private static final String MODE_SEARCH = "mode_search";
    private String modeSelected;
    private int itemSelected;
    @BindView(R.id.list_properties_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fragment_list_properties) FrameLayout fragmentView;

    public ListPropertiesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        // Assign variables
        callbackListProperties = this;
        context = getActivity().getApplicationContext();

        // Restore datas
        SaveAndRestoreDataListPropertiesFrag.recoverDatas(getArguments(),savedInstanceState,this,context,baseActivityListener);
        recoverActivities();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.property_item_list, container, false);

        // Assign views
        ButterKnife.bind(this, view);

        // Configure fragment
        configureFragment();

        return view;
    }

    // ------------------------------------------------------------------------------------------------------
    // --------------------------------------  CONFIGURE FRAGMENT  ------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    private void configureFragment(){

        if(modeDevice.equals(MODE_TABLET)){ // ------------------ TABLET

            if(itemSelected!=-1){ // if one item is selected in the list
                listProperties.get(itemSelected).setSelected(true);
            } else if(!modeSelected.equals(MODE_DISPLAY_MAPS)){ // if no item is selected, select the first one of the list
                showDisplayFragment(0);
            }

            configureListProperties(itemSelected);

        } else { // --------------------------------------------- PHONE
            configureListProperties(-1);
        }
    }

    public void configureListProperties(int position){

        // Set the adapter
        if(listProperties!=null){
            if (listProperties.size() > 0) {

                if (context != null) {

                    // Create adapter passing in the sample user data
                    PropertiesRecyclerViewAdapter adapter = new PropertiesRecyclerViewAdapter(this,listProperties, context, position, callbackListProperties, baseActivityListener, modeSelected, modeDevice);
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
                baseActivityListener.configureAndShowDisplayFragment(modeSelected, listProperties.get(position).getId());
        }
    }

    @Override
    public void changeMarkerMap(int idProp) {
        if(modeSelected.equals(MODE_DISPLAY_MAPS)){
            mapsActivity.getConfigureMap().centerToMarker(idProp);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof BaseActivityListener){
            baseActivityListener = (BaseActivityListener) context;
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // --------------------------------------  SAVE & RESTORE DATAS  ----------------------------------------
    // ------------------------------------------------------------------------------------------------------

    private void recoverActivities(){
        if(getModeSelected().equals(MODE_SEARCH) && getModeDevice().equals(MODE_TABLET))
            setSearchActivity((SearchActivity) getActivity());

        if(getModeSelected().equals(MODE_DISPLAY_MAPS) && getModeDevice().equals(MODE_TABLET))
            setMapsActivity((MapsActivity) getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SaveAndRestoreDataListPropertiesFrag.saveDatas(outState, itemSelected, listProperties, modeSelected, fragmentDisplayed,modeDevice);
    }

    // --------------------------------------------------------------------------------------------------------
    // ------------------------------------ GETTERS AND SETTERS ------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    public String getModeDevice() {
        return modeDevice;
    }

    public String getModeSelected() {
        return modeSelected;
    }

    public void setListProperties(List<Property> listProperties) {
        this.listProperties = listProperties;
    }

    public void setModeDevice(String modeDevice) {
        this.modeDevice = modeDevice;
    }

    public void setModeSelected(String modeSelected) {
        this.modeSelected = modeSelected;
    }

    public void setItemSelected(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    public void setMapsActivity(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    public void setSearchActivity(SearchActivity searchActivity) {
        this.searchActivity = searchActivity;
    }

    public void setFragmentDisplayed(String fragmentDisplayed) {
        this.fragmentDisplayed = fragmentDisplayed;
    }
}
