package com.openclassrooms.realestatemanager.Controllers.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.SearchActivity;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.ConfigureSearchFragment;
import com.openclassrooms.realestatemanager.Utils.LaunchSearchQuery;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchFragment extends Fragment {

    @BindView(R.id.price_inf_search) EditText priceInfView;
    @BindView(R.id.price_sup_search) EditText priceSupView;
    @BindView(R.id.surface_inf_search) EditText surfaceInfView;
    @BindView(R.id.surface_sup_search) EditText surfaceSupView;
    @BindView(R.id.start_date_publish_selected_search) TextView startPublishView;
    @BindView(R.id.end_date_publish_selected_search) TextView endPublishView;
    @BindView(R.id.address_edit_text_search) SearchView locationView;
    @BindView(R.id.buttonSearchCancel) Button buttonCancel;
    @BindView(R.id.buttonSearch) Button buttonSearch;
    private static final String MODE_SEARCH = "mode_search";
    private BaseActivity baseActivity;
    private SearchActivity searchActivity;
    private int roomNbMin;
    private Context context;
    private LatLng searchLoc;
    private View view;
    private ConfigureSearchFragment searchConfig;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this,view);

        // Assign variables
        baseActivity = (BaseActivity) getActivity();
        searchActivity= (SearchActivity) getActivity();
        if(baseActivity!=null)
            this.context = baseActivity.getApplicationContext();

        // Configure searchFragment fields
        searchConfig = new ConfigureSearchFragment(view,context,this);
        return view;
    }

    // ----------------------------------------------------------------------------------------------------
    // ------------------------------------------ LAUNCH QUERY  -------------------------------------------
    // ----------------------------------------------------------------------------------------------------

    public void launchSearchProperties(){
        roomNbMin = searchConfig.getRoomNbMin();
        new LaunchSearchQuery(view, context,this);
    }

    public void displayResults(List<Property> results) {
        if (results.size() > 0){ // if at least one result, show list properties
            searchActivity.setListProperties(results);
            baseActivity.configureAndShowListPropertiesFragment(MODE_SEARCH, results);
        } else
            baseActivity.displayError(context.getResources().getString(R.string.no_result_found));
    }

    // ----------------------------------------------------------------------------------------------------
    // -------------------------------------- GETTERS & SETTERS -------------------------------------------
    // ----------------------------------------------------------------------------------------------------

    public void setLatLngAddress(LatLng latLng) {
        searchLoc = latLng;

        // Enable button save and cancel
        baseActivity.runOnUiThread(() -> {
            buttonSearch.setEnabled(true);
            buttonCancel.setEnabled(true);
        });
    }

    public SearchView getLocationView() {
        return locationView;
    }

    public Button getButtonCancel() {
        return buttonCancel;
    }

    public Button getButtonSearch() {
        return buttonSearch;
    }

    public TextView getStartPublishView() {
        return startPublishView;
    }

    public TextView getEndPublishView() {
        return endPublishView;
    }

    public BaseActivity getBaseActivity() {
        return baseActivity;
    }

    public SearchActivity getSearchActivity() {
        return searchActivity;
    }

    public LatLng getSearchLoc() {
        return searchLoc;
    }

    public int getRoomNbMin() {
        return roomNbMin;
    }

    public EditText getPriceInfView() {
        return priceInfView;
    }

    public EditText getPriceSupView() {
        return priceSupView;
    }

    public EditText getSurfaceInfView() {
        return surfaceInfView;
    }

    public EditText getSurfaceSupView() {
        return surfaceSupView;
    }
}