package com.openclassrooms.realestatemanager.Utils;

import com.openclassrooms.realestatemanager.Models.LatLngAddress.LatLngAddress;
import com.openclassrooms.realestatemanager.Models.PlaceNearby.PlaceNearby;
import com.openclassrooms.realestatemanager.Models.SuggestionsLatLng.Suggestions;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.Observable;

public interface ApiService {

    // NEARBY SEARCH REQUEST
    @GET("place/nearbysearch/json")
    Observable<PlaceNearby> getNearbyPlaces(@Query("key") String api,
                                            @Query("radius") String radius,
                                            @Query("location") String location);

    // LAT LNG FROM ADDRESS
    @GET("geocode/json")
    Observable<LatLngAddress> getLatLngAddress(@Query("key") String api,
                                               @Query("address") String address);

    // GET SUGGESTIONS ADDRESS
    @GET("place/autocomplete/json")
    Observable<Suggestions> getSuggestions(@Query("key") String api,
                                           @Query("input") String address,
                                           @Query("country") String country);


    HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(logging).retryOnConnectionFailure(false);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build();

//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=48.613032,2.482217&radius=1000&key=AIzaSyCAzX1ILkJlqSsTMkRJHSGEMAQWuqxSxKA
}
