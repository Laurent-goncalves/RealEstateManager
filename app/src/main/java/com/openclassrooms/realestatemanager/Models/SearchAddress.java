package com.openclassrooms.realestatemanager.Models;

import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Models.SuggestionsLatLng.Prediction;
import com.openclassrooms.realestatemanager.Models.SuggestionsLatLng.Suggestions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.ApiStream;

import java.lang.reflect.Field;
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
        });
    }

    private void configureSearchView(){

        // Add query hint in the search area
        searchView.setQueryHint(context.getResources().getString(R.string.enter_address));

        // Assign searchAutoComplete
        searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);




        searchView.setIconifiedByDefault(true);
        //searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.clearFocus();
        //searchView.requestFocusFromTouch();

        // Hide close button
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setVisibility(View.GONE);

        // set onclick listeners
        setOnClickListenerItemSelectionSearchView();
        setOnQueryTextListenerSearchView();
    }

    public void displayListPredictions(Boolean submit) {
        String[] listSuggArray = listSuggestions.toArray(new String[listSuggestions.size()]);

        ArrayAdapter<String> autocomplete_adapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_dropdown_item_1line, listSuggArray);
        searchAutoComplete.setAdapter(autocomplete_adapter);

        if(submit && listSuggestions.size()==1){
            searchAutoComplete.setText(listSuggestions.get(0));
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
