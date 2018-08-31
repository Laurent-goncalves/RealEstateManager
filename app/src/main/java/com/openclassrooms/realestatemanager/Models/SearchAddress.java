package com.openclassrooms.realestatemanager.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.SearchActivity;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.SearchFragment;
import com.openclassrooms.realestatemanager.Models.LatLngAddress.LatLngAddress;
import com.openclassrooms.realestatemanager.Models.SuggestionsLatLng.Prediction;
import com.openclassrooms.realestatemanager.Models.SuggestionsLatLng.Suggestions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.ApiStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 After the user enter at least 5 digits of an address in the searchView, API requests are sent to get the accurate address (1).
 The user can select an address among predictions proposed. When user selects an item in the list, the following steps are performed:
    - an API request is sent to get the Lat and Lng of the address (2)
    - the static Map is recovered (3)
    - an API request is sent to get the interest points nearby the Lat and Lng found (4)
 */

public class SearchAddress implements Disposable{

    private EditFragment editFragment;
    private SearchFragment searchFragment;
    private MainActivity mainActivity;
    private SearchActivity searchActivity;
    private SearchView searchView;
    private Context context;
    private Disposable disposable;
    private String apiKeyPredic;
    private String apiKeyMaps;
    private SearchView.SearchAutoComplete searchAutoComplete;
    private List<String> listSuggestions;
    private LatLng latLng;
    private LatLngAddress latLngAddressResult;

    public SearchAddress(EditFragment editFragment, Property property, Context context) {
        this.editFragment=editFragment;
        this.mainActivity = (MainActivity) editFragment.getActivity();
        this.context=context;
        this.apiKeyPredic=context.getResources().getString(R.string.google_maps_key2);
        this.apiKeyMaps=context.getResources().getString(R.string.google_static_maps_key);
        searchView = editFragment.getSearchView();
        configureSearchView(property);
    }

    public SearchAddress(SearchFragment searchFragment, Context context) {
        this.searchFragment=searchFragment;
        this.searchActivity = (SearchActivity) searchFragment.getActivity();
        this.context=context;
        this.apiKeyPredic=context.getResources().getString(R.string.google_maps_key2);
        this.apiKeyMaps=context.getResources().getString(R.string.google_static_maps_key);
        searchView = searchFragment.getLocationView();
        configureSearchView(null);
    }

    // --------------------------------------------------------------------------------------------------------
    // ---------------------------------CONFIGURATION VIEWS AND LISTENERS -------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private void configureSearchView(Property property){

        // Set the address in searchView if exists
        if(property!=null){
            if(property.getAddress()==null) {
                searchView.setQueryHint(context.getResources().getString(R.string.enter_address));
            } else
                searchView.setQuery(property.getAddress(),false);
        }

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

    private void setOnQueryTextListenerSearchView(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                    getListPlacesNearby(apiKeyPredic,query,true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.length()>5)
                    getListPlacesNearby(apiKeyPredic,s,false);
                return false;
            }
        });
    }

    private void setOnClickListenerItemSelectionSearchView(){
        // when clicking on an item from the list autocomplete
        searchAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String address = listSuggestions.get(position);
            searchAutoComplete.setText(address);
            launchLatLngSearch(address);
        });
    }

    @Override
    public void dispose() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    @Override
    public boolean isDisposed() {
        return false;
    }

    // --------------------------------------------------------------------------------------------------------
    // ------------------------------- 1 - API REQUEST FOR ADDRESS PREDICTIONS --------------------------------
    // --------------------------------------------------------------------------------------------------------

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
                displayListPredictions(submit);
                dispose();
            }
        });
    }

    public void displayListPredictions(Boolean submit) {
        String[] listSuggArray = listSuggestions.toArray(new String[listSuggestions.size()]);

        ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_dropdown_item_1line, listSuggArray);
        searchAutoComplete.setAdapter(autocompleteAdapter);

        if(submit && listSuggestions.size()>=1){
            // the unique item of the list is written in the searchView
            searchAutoComplete.setText(listSuggestions.get(0));
            //launchLatLngSearch(listSuggestions.get(0));
            launchLatLngSearch(listSuggArray[0]);
        }
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

    // --------------------------------------------------------------------------------------------------------
    // ------------------------------ 2 - API REQUEST FOR LAT & LNG OF THE ADDRESS ----------------------------
    // --------------------------------------------------------------------------------------------------------

    private void launchLatLngSearch(String address){

        // the address selected is written in the searchView
        editFragment.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchAutoComplete.setText(address);
            }
        });


        // the button save is disabled
        if(editFragment!=null){
            editFragment.getButtonSave().setEnabled(false);
            editFragment.getButtonCancel().setEnabled(false);
        } else {
            searchFragment.getButtonSearch().setEnabled(false);
            searchFragment.getButtonCancel().setEnabled(false);
        }

        // launch of API request to get Lat and Lng of the address
        getLatLngAddress(apiKeyMaps,address);
    }

    private void getLatLngAddress(String api_key, String address) {

        dispose();

        this.disposable = ApiStream.streamFetchgetLatLngAddress(api_key,address).subscribeWith(new DisposableObserver<LatLngAddress>() {

            @Override
            public void onNext(LatLngAddress latLngAddress) {
                latLngAddressResult = latLngAddress;
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("eee error - " + e.toString());
            }

            @Override
            public void onComplete() {

                if(latLngAddressResult!=null){
                    if(latLngAddressResult.getResults()!=null){
                        if(latLngAddressResult.getResults().size()>0) {
                            if (latLngAddressResult.getResults().get(0) != null) {
                                if (latLngAddressResult.getResults().get(0).getGeometry() != null) {
                                    if (latLngAddressResult.getResults().get(0).getGeometry().getLocation() != null) {

                                        latLng = new LatLng(latLngAddressResult.getResults().get(0).getGeometry().getLocation().getLat(),
                                                latLngAddressResult.getResults().get(0).getGeometry().getLocation().getLng());

                                        if(editFragment!=null)
                                            editFragment.setLatLngAddress(latLng);
                                        else
                                            searchFragment.setLatLngAddress(latLng);
                                    }
                                }
                            }
                        }
                    }
                }
                if(editFragment!=null)
                    getStaticMapFromAddress(latLng);

                dispose();
            }
        });
    }

    // --------------------------------------------------------------------------------------------------------
    // ------------------------ 3 - API REQUEST TO GET THE STATIC MAP OF THE ADDRESS --------------------------
    // --------------------------------------------------------------------------------------------------------

    @SuppressLint("StaticFieldLeak")
    private void getStaticMapFromAddress(LatLng latLng){

        if(latLng!=null){

            String urlImage = "https://maps.googleapis.com/maps/api/staticmap?center=" + latLng.latitude + "," + latLng.longitude +
                    "&zoom=20&size=800x400&maptype=roadmap&markers=color:blue%7Clabel:S%7C" + latLng.latitude + "," + latLng.longitude +
                    "&key=" + context.getResources().getString(R.string.google_static_maps_key);

            // Get static Map through Async task
            MyAsync obj = new MyAsync(urlImage) {
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    editFragment.setStaticMap(bitmap);
                }
            };

            obj.execute();
        }

        // Get interest points
        launchSearchInterestPoints(editFragment,latLng);
    }

    // --------------------------------------------------------------------------------------------------------
    // ------------------------------ 4 - API REQUEST FOR INTEREST POINTS SEARCH ------------------------------
    // --------------------------------------------------------------------------------------------------------

    private void launchSearchInterestPoints(EditFragment editFragment, LatLng latLng){
        new ListPointsInterest(apiKeyPredic,latLng,"1000",context, editFragment);
    }

    // -------------------------------------------------------------------------------------------------------
    // ------------------------------------------ INNER CLASS ASYNC TASK ---------------------------------------
    // -------------------------------------------------------------------------------------------------------

    private static class MyAsync extends AsyncTask<Void, Void, Bitmap> {

        private String urlImage;

        public MyAsync(String urlImage) {
            this.urlImage = urlImage;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            try {
                URL url = new URL(urlImage);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}

/*

https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&key=AIzaSyAo8Xn0NUmP-GeDRDwyxzasemNR9nen6sw

 */
