package com.openclassrooms.realestatemanager.Models;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Models.LatLngAddress.LatLngAddress;
import com.openclassrooms.realestatemanager.Models.SuggestionsLatLng.Prediction;
import com.openclassrooms.realestatemanager.Models.SuggestionsLatLng.Suggestions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.ApiStream;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;



public class SearchAddress implements Disposable{

    private EditFragment editFragment;
    private MainActivity mainActivity;
    private SearchView searchView;
    private Context context;
    private Disposable disposable;
    private String apiKey;
    private SearchView.SearchAutoComplete searchAutoComplete;
    private List<String> listSuggestions;
    private LatLng latLng;

    public SearchAddress(EditFragment editFragment, Context context) {
        this.editFragment=editFragment;
        this.mainActivity = (MainActivity) editFragment.getActivity();
        this.context=context;
        this.apiKey=context.getResources().getString(R.string.google_maps_key2);
        searchView = editFragment.getAddressEdit();
        configureSearchView();
    }

    private void getListPlacesNearby(String api_key, String address, Boolean submit) {

        dispose();
        listSuggestions = new ArrayList<>();

        this.disposable = ApiStream.streamFetchgetSuggestions(api_key,address).subscribeWith(new DisposableObserver<Suggestions>() {

            @Override
            public void onNext(Suggestions suggestions) {
                buildListSuggestions(suggestions);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("eee error - " + e.toString());
            }

            @Override
            public void onComplete() {
                System.out.println("eee listSuggestions - " + listSuggestions.toString());

                displayListPredictions(submit);
                dispose();
            }
        });
    }

    private void buildListSuggestions(Suggestions suggestions){

        if(suggestions!=null){
            if(suggestions.getPredictions()!=null){
                for(Prediction predic : suggestions.getPredictions()){
                    listSuggestions.add(predic.getDescription());
                }
            }
        }
    }

    private void setOnQueryTextListenerSearchView(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getListPlacesNearby(apiKey,query,true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.length()>5)
                    getListPlacesNearby(apiKey,s,false);
                return false;
            }
        });
    }

    private void setOnClickListenerItemSelectionSearchView(){
        // when clicking on an item from the list autocomplete
        searchAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            searchAutoComplete.setText(listSuggestions.get(position));
            launchLatLngSearch(listSuggestions.get(position));
        });
    }

    private void getLatLngAddress(String api_key, String address) {

        dispose();

        this.disposable = ApiStream.streamFetchgetLatLngAddress(api_key,address).subscribeWith(new DisposableObserver<LatLngAddress>() {

            @Override
            public void onNext(LatLngAddress latLngAddress) {
                if(latLngAddress!=null){
                    if(latLngAddress.getResults()!=null){
                        if(latLngAddress.getResults().get(0)!=null) {
                            if (latLngAddress.getResults().get(0).getGeometry() != null) {
                                if (latLngAddress.getResults().get(0).getGeometry().getLocation() != null) {

                                    latLng = new LatLng(latLngAddress.getResults().get(0).getGeometry().getLocation().getLat(),
                                            latLngAddress.getResults().get(0).getGeometry().getLocation().getLng());

                                    editFragment.getPropertyInit().setLat(latLng.latitude);
                                    editFragment.getPropertyInit().setLng(latLng.longitude);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("eee error - " + e.toString());
            }

            @Override
            public void onComplete() {
                launchSearchInterestPoints(editFragment,latLng);
                dispose();
            }
        });
    }

    private void launchSearchInterestPoints(EditFragment editFragment, LatLng latLng){
        new ListPointsInterest(apiKey,latLng,"1000",context, editFragment);

    }

    private void configureSearchView(){

        // Add query hint in the search area
        searchView.setQueryHint(context.getResources().getString(R.string.enter_address));

        // Assign searchAutoComplete
        searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        // Keep open the searchView
        searchView.setIconifiedByDefault(true);
        searchView.setIconified(false);
        searchView.clearFocus();

        // Hide close button
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setVisibility(View.GONE);

        // set onclick listeners
        setOnClickListenerItemSelectionSearchView();
        setOnQueryTextListenerSearchView();
    }

    private void launchLatLngSearch(String address){

        // the address selected is written in the searchView
        searchAutoComplete.setText(address);

        // the button save is disabled
        editFragment.getButtonSave().setEnabled(false);

        // launch of API request to get Lat and Lng of the address
        getLatLngAddress(apiKey,address);
    }

    public void displayListPredictions(Boolean submit) {
        String[] listSuggArray = listSuggestions.toArray(new String[listSuggestions.size()]);

        ArrayAdapter<String> autocomplete_adapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_dropdown_item_1line, listSuggArray);
        searchAutoComplete.setAdapter(autocomplete_adapter);

        if(submit && listSuggestions.size()==1){
            // the unique item of the list is written in the searchView
            searchAutoComplete.setText(listSuggestions.get(0));

            launchLatLngSearch(listSuggestions.get(0));
        }
    }

    @Override
    public void dispose() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    @Override
    public boolean isDisposed() {
        return false;
    }
}
