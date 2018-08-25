package com.openclassrooms.realestatemanager.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Models.LatLngAddress.LatLngAddress;
import com.openclassrooms.realestatemanager.Models.SuggestionsLatLng.Prediction;
import com.openclassrooms.realestatemanager.Models.SuggestionsLatLng.Suggestions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.ApiStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
    private MainActivity mainActivity;
    private EditText addressView;
    private Context context;
    private Disposable disposable;
    private String apiKeyPredic;
    private String apiKeyMaps;
    private List<String> listSuggestions;
    private LatLng latLng;
    private LatLngAddress latLngAddressResult;
    private ListView listView;
    private String[] listSuggArray;

    public SearchAddress(EditFragment editFragment, Property property, Context context) {
        this.editFragment=editFragment;
        this.mainActivity = (MainActivity) editFragment.getActivity();
        this.context=context;
        this.apiKeyPredic=context.getResources().getString(R.string.google_maps_key2);
        this.apiKeyMaps=context.getResources().getString(R.string.google_static_maps_key);
        addressView = editFragment.getAddressEdit();
        listView = editFragment.listView;

        if(property.getAddress()!=null)
            addressView.setText(property.getAddress());
        else
            addressView.setText(context.getResources().getString(R.string.enter_address));

        setOnTextChangeListenerAddressView();
        setOnItemSelected();
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------- CONFIGURATION VIEWS AND LISTENERS ------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private void setOnItemSelected(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchLatLngSearch(listSuggArray[position]);
                addressView.setText(listSuggArray[position]);
                listView.setVisibility(View.GONE);
            }
        });
    }

    private void setOnTextChangeListenerAddressView(){

        addressView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s!=null){
                    if(s.equals(context.getResources().getString(R.string.enter_address)))
                        addressView.setText(null);
                }

                listView.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s!=null){
                    if(s.length()>5){
                        getListPlacesNearby(apiKeyPredic, s.toString());
                    } else if (s.length()==0)
                        addressView.setText(context.getResources().getString(R.string.enter_address));
                }
            }
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

    private void getListPlacesNearby(String api_key, String address) {

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
                createListPredictions();
                dispose();
            }
        });
    }

    public void createListPredictions() {
        listSuggArray = new String[listSuggestions.size()];
        listSuggArray = listSuggestions.toArray(listSuggArray);

        listView.setVisibility(View.VISIBLE);

        // setting list adapter
        listView.setAdapter(new ArrayAdapter<String>(context, R.layout.list_item, listSuggArray));
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

        // the buttons save and cancel are disabled
        editFragment.getButtonSave().setEnabled(false);
        editFragment.getButtonCancel().setEnabled(false);

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

                                        editFragment.setLatLngAddress(latLng);
                                    }
                                }
                            }
                        }
                    }
                }

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
                    "&zoom=13&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C" + latLng.latitude + "," + latLng.longitude +
                    "key=" + context.getResources().getString(R.string.google_static_maps_key);

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











                try {

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
                    InputStream is = connection.getInputStream();

                    Bitmap img = BitmapFactory.decodeStream(is, null, options);
                    editFragment.setStaticMap(img);

                } catch (IOException e) {
                    e.printStackTrace();
                }

 */
