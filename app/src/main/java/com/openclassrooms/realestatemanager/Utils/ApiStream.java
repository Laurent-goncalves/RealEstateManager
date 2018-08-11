package com.openclassrooms.realestatemanager.Utils;

import com.openclassrooms.realestatemanager.Models.PlaceNearby.PlaceNearby;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


public class ApiStream {

    public static Observable<PlaceNearby> streamFetchgetSearchNearbyPlaces(String api_key, String radius, String location){
        ApiService search_nearby_request = ApiService.retrofit.create(ApiService.class);

        return search_nearby_request.getNearbyPlaces(api_key,radius,location)
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread());
                .observeOn(Schedulers.newThread());  // TEST
    }
}
