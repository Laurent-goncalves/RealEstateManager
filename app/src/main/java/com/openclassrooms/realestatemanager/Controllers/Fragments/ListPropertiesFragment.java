package com.openclassrooms.realestatemanager.Controllers.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.model.LatLngBounds;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.CallbackListProperties;
import com.openclassrooms.realestatemanager.Models.MapsActivityListener;
import com.openclassrooms.realestatemanager.Models.SearchQuery;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataListPropertiesFrag;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.Views.PropertiesRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import java.util.List;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ListPropertiesFragment extends Fragment implements CallbackListProperties {

    @BindView(R.id.list_properties_recycler_view) RecyclerView recyclerView;
    private final static String LIST_FRAG = "fragment_list";
    private final static String MODE_TABLET = "mode_tablet";
    private static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    private static final String MODE_SEARCH = "mode_search";
    private static final String MODE_DISPLAY = "mode_display";
    private final static String DISPLAY_FRAG = "fragment_display";
    private final static String MAPS_FRAG = "fragment_maps";
    private final static String GRAY_COLOR = "GRAY";
    private final static String WHITE_COLOR = "WHITE";
    private CallbackListProperties callbackListProperties;
    private BaseActivityListener baseActivityListener;
    private MapsActivityListener mapsActivityListener;
    private PropertiesRecyclerViewAdapter adapter;
    private Context context;
    private String modeDevice;
    private String fragmentDisplayed;
    private List<Property> listProperties;
    private String modeSelected;
    private int itemSelected;
    private SearchQuery searchQuery;
    private LatLngBounds cameraBounds;


    public ListPropertiesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        // Assign variables
        callbackListProperties = this;
        context = getActivity().getApplicationContext();

        // Restore datas
        SaveAndRestoreDataListPropertiesFrag.recoverModeSelected(getArguments(),savedInstanceState,this);

        switch(modeSelected){
            case MODE_DISPLAY:
                SaveAndRestoreDataListPropertiesFrag.recoverDatasMainActivity(getArguments(),savedInstanceState,this,context,baseActivityListener);
                break;
            case MODE_DISPLAY_MAPS:
                SaveAndRestoreDataListPropertiesFrag.recoverDatasMapsActivity(getArguments(),savedInstanceState,this,context,baseActivityListener);
                break;
            case MODE_SEARCH:
                SaveAndRestoreDataListPropertiesFrag.recoverDatasSearchActivity(getArguments(),savedInstanceState,this,context,baseActivityListener);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.property_item_list, container, false);

        // Assign views
        ButterKnife.bind(this, view);

        // Configure fragment
        if(listProperties!=null){
            if(listProperties.size()>0)
                configureFragment(savedInstanceState);
            else
                Utils.colorFragmentList(GRAY_COLOR,modeDevice,fragmentDisplayed, baseActivityListener.getBaseActivity());
        } else
            Utils.colorFragmentList(GRAY_COLOR,modeDevice, fragmentDisplayed, baseActivityListener.getBaseActivity());

        return view;
    }

    // ------------------------------------------------------------------------------------------------------
    // --------------------------------------  CONFIGURE FRAGMENT  ------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    private void configureFragment(Bundle savedInstanceState){

        if(baseActivityListener!=null){
            if(modeDevice.equals(MODE_TABLET)){ // ------------------ TABLET

                if(itemSelected!=-1){ // if one item is selected in the list
                    listProperties.get(itemSelected).setSelected(true);
                }

                if(!modeSelected.equals(MODE_DISPLAY_MAPS) && fragmentDisplayed.equals(LIST_FRAG) && savedInstanceState == null){ // if no item is selected, select the first one of the list
                    showDisplayFragment(0);
                } else if(modeSelected.equals(MODE_DISPLAY_MAPS) && fragmentDisplayed.equals(MAPS_FRAG) && savedInstanceState == null) {
                    changeMarkerMap(Objects.requireNonNull(listProperties).get(0).getId());
                }

                // configure list of proporties
                configureListProperties(itemSelected);

            } else { // --------------------------------------------- PHONE
                configureListProperties(-1);
            }

            // configure buttons add and edit
            baseActivityListener.getToolbarManager().setIconsToolbarListPropertiesMode(modeSelected);
        }
    }

    public void removeSelectedItemInList(){

        if(listProperties!=null && adapter!=null){
            if(listProperties.size()>0){
                for(Property property : listProperties){
                    property.setSelected(false);
                }
            }
            adapter.setListProperties(listProperties);
            adapter.setPropertySelected(-1);
            adapter.notifyDataSetChanged();
        }
    }

    public void changeSelectedItemInList(int idProperty, String fragmentDisplayed){

        this.fragmentDisplayed=fragmentDisplayed;

        if(idProperty!=-1 && listProperties!=null){
            itemSelected = Utils.getIndexPropertyFromList(idProperty, listProperties);
            listProperties.get(itemSelected).setSelected(true);
            configureListProperties(itemSelected);
        } else
            configureListProperties(-1);
    }

    public void configureListProperties(int position){

        // Set the adapter
        if(listProperties!=null){
            if (listProperties.size() > 0) {

                    Utils.colorFragmentList(WHITE_COLOR,modeDevice, fragmentDisplayed,baseActivityListener.getBaseActivity());

                if (context != null) {

                    if(position>=0)
                        listProperties.get(position).setSelected(true);

                    // Create adapter passing in the sample user data
                    adapter = new PropertiesRecyclerViewAdapter(this, listProperties, context, position, callbackListProperties, baseActivityListener, modeSelected, modeDevice);
                    // Attach the adapter to the recyclerview to populate items
                    recyclerView.setAdapter(adapter);
                    // Set layout manager to position the items
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                }

            } else {
                recyclerView.setAdapter(null); // remove adapter and color in gray the fragment
                    Utils.colorFragmentList(GRAY_COLOR,modeDevice, fragmentDisplayed, baseActivityListener.getBaseActivity());
            }
        } else {
            recyclerView.setAdapter(null); // remove adapter and color in gray the fragment
                Utils.colorFragmentList(GRAY_COLOR,modeDevice, fragmentDisplayed, baseActivityListener.getBaseActivity());
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

        itemSelected = Utils.getIndexPropertyFromList(idProp,listProperties);
        baseActivityListener.setCurrentPositionDisplayed(idProp);

        if(modeSelected.equals(MODE_DISPLAY_MAPS)){
            mapsActivityListener.getConfigureMap().selectMarker(idProp); // select marker in the map
            mapsActivityListener.getConfigureMap().setIdProperty(idProp);
        }

        changeSelectedItemInList(idProp,fragmentDisplayed); // change item selected in the list

        if(fragmentDisplayed.equals(DISPLAY_FRAG))
            showDisplayFragment(Utils.getIndexPropertyFromList(idProp,listProperties));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof BaseActivityListener){
            baseActivityListener = (BaseActivityListener) context;
        }

        if(context instanceof MapsActivityListener){
            mapsActivityListener = (MapsActivityListener) context;
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // --------------------------------------  SAVE & RESTORE DATAS  ----------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public void refreshListProperties(Bundle bundle, String modeSelected){

        this.modeSelected = modeSelected;

        switch(modeSelected){
            case MODE_DISPLAY_MAPS:
                SaveAndRestoreDataListPropertiesFrag.recoverDatasMapsActivity(bundle,null,this, context, baseActivityListener);
                break;
            case MODE_DISPLAY:
                SaveAndRestoreDataListPropertiesFrag.recoverDatasMainActivity(bundle, null,this, context, baseActivityListener);
                break;
            case MODE_SEARCH:
                SaveAndRestoreDataListPropertiesFrag.recoverDatasSearchActivity(bundle, null,this, context, baseActivityListener);
                break;
        }
        configureListProperties(itemSelected);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int idToSave;

        if(itemSelected<listProperties.size() && itemSelected>=0)
            idToSave = listProperties.get(itemSelected).getId();
        else
            idToSave = -1;

        switch(modeSelected){
            case MODE_DISPLAY_MAPS:
                SaveAndRestoreDataListPropertiesFrag.saveDatas(outState, idToSave, modeSelected, fragmentDisplayed, modeDevice, cameraBounds);
                break;
            case MODE_DISPLAY:
                SaveAndRestoreDataListPropertiesFrag.saveDatas(outState, idToSave, modeSelected, fragmentDisplayed,modeDevice);
                break;
            case MODE_SEARCH:
                SaveAndRestoreDataListPropertiesFrag.saveDatas(outState, idToSave, modeSelected, fragmentDisplayed,modeDevice,searchQuery);
                break;
        }
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

    public void setFragmentDisplayed(String fragmentDisplayed) {
        this.fragmentDisplayed = fragmentDisplayed;
    }

    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void setCameraBounds(LatLngBounds cameraBounds) {
        this.cameraBounds = cameraBounds;
    }

    public List<Property> getListProperties() {
        return listProperties;
    }

    public String getFragmentDisplayed() {
        return fragmentDisplayed;
    }
}
