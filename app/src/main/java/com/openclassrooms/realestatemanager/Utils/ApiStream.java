package com.openclassrooms.realestatemanager.Utils;

import com.openclassrooms.realestatemanager.Models.LatLngAddress.LatLngAddress;
import com.openclassrooms.realestatemanager.Models.PlaceNearby.PlaceNearby;
import com.openclassrooms.realestatemanager.Models.SuggestionsLatLng.Suggestions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ApiStream {

    public static Observable<PlaceNearby> streamFetchgetSearchNearbyPlaces(String api_key, String radius, String location){
        ApiService search_nearby_request = ApiService.retrofit.create(ApiService.class);

        return search_nearby_request.getNearbyPlaces(api_key,radius,location)
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread());
                .observeOn(Schedulers.newThread());  // TEST
    }

    public static Observable<LatLngAddress> streamFetchgetLatLngAddress(String api_key, String address){
        ApiService latlng_request = ApiService.retrofit.create(ApiService.class);

        return latlng_request.getLatLngAddress(api_key,address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());// TEST
                //.observeOn(Schedulers.newThread());

    }

    public static Observable<Suggestions> streamFetchgetSuggestions(String api_key, String address){
        ApiService autocomplete_request = ApiService.retrofit.create(ApiService.class);

        return autocomplete_request.getSuggestions(api_key,address,"FR")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
                //.observeOn(Schedulers.newThread());  // TEST
    }

}
