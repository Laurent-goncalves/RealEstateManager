package com.openclassrooms.realestatemanager.Controllers.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.SearchQuery;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.ConfigureSearchFragment;
import com.openclassrooms.realestatemanager.Utils.LaunchSearchQuery;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataSearchFragment;
import com.openclassrooms.realestatemanager.Utils.UtilsBaseActivity;
import java.util.Arrays;
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
    @BindView(R.id.address_edit_text_search) android.support.v7.widget.SearchView locationView;
    @BindView(R.id.buttonSearchCancel) Button buttonCancel;
    @BindView(R.id.buttonSearch) Button buttonSearch;
    private static final String MODE_SEARCH = "mode_search";
    private BaseActivityListener baseActivityListener;
    private ConfigureSearchFragment configSearchFrag;
    private SearchQuery searchQuery;
    private Context context;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        // Assign variables
        this.context = getActivity().getApplicationContext();
        searchQuery = new SearchQuery();

        // Restore datas
        SaveAndRestoreDataSearchFragment.recoverDatas(getArguments(),savedInstanceState,this);

        // configure buttons add and edit in toolbar
        if(baseActivityListener!=null)
            baseActivityListener.getToolbarManager().setIconsToolbarSearchPropertiesMode();

        // Configure searchFragment fields
        configSearchFrag = new ConfigureSearchFragment(view,context,this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof BaseActivityListener){
            baseActivityListener = (BaseActivityListener) context;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SaveAndRestoreDataSearchFragment.saveDatas(outState,searchQuery);
    }

    public void stopActivity() {
        if(baseActivityListener!=null) {
            baseActivityListener.launchMainActivity();
            baseActivityListener.stopActivity();
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // ------------------------------------------ LAUNCH QUERY  -------------------------------------------
    // ----------------------------------------------------------------------------------------------------

    public void launchSearchProperties(){
        LaunchSearchQuery launchSearchQuery = new LaunchSearchQuery(context, searchQuery);
        displayResults(launchSearchQuery.getListProperties());
    }

    public void displayResults(List<Property> results) {
        if (results.size() > 0 && baseActivityListener!=null){ // if at least one result, show list properties
            baseActivityListener.setListProperties(results);
            baseActivityListener.setSearchQuery(searchQuery);
            baseActivityListener.configureAndShowListPropertiesFragment(MODE_SEARCH);
        } else
            UtilsBaseActivity.displayError(context, context.getResources().getString(R.string.no_result_found));
    }

    public void resetSearchQuery(){
        searchQuery = new SearchQuery(false,
                Arrays.asList(context.getResources().getStringArray(R.array.type_property)).get(0),
                null,null,0d,0d,0d,0d,0,
                0d,0d,null, Integer.parseInt(context.getResources().getString(R.string.radius)));
        configSearchFrag.setQuery(searchQuery);

        configSearchFrag.configureSearchFragment();

        if(baseActivityListener!=null)
            baseActivityListener.setSearchQuery(searchQuery);
    }

    // ----------------------------------------------------------------------------------------------------
    // -------------------------------------- GETTERS & SETTERS -------------------------------------------
    // ----------------------------------------------------------------------------------------------------

    @Override
    public Context getContext() {
        return context;
    }

    public void setLatLngAddress(LatLng latLng) {
        searchQuery.setSearchLocLat(latLng.latitude);
        searchQuery.setSearchLocLng(latLng.longitude);

        // Enable button save and cancel
        if(baseActivityListener!=null){
            baseActivityListener.getBaseActivity().runOnUiThread(() -> {
                buttonSearch.setEnabled(true);
                buttonCancel.setEnabled(true);
            });
        }
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
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

    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    public BaseActivityListener getBaseActivityListener() {
        return baseActivityListener;
    }
}