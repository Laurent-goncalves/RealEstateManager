package com.openclassrooms.realestatemanager.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Models.PlaceNearby.PlaceNearby;
import com.openclassrooms.realestatemanager.Models.PlaceNearby.Result;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.ApiStream;
import com.openclassrooms.realestatemanager.Utils.Utils;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public class ListPointsInterest implements Disposable {

    private List<String> listPointsInterest;
    private List<String> listPointsInterestTemp;
    private Disposable disposable;
    private Context context;
    private EditFragment editFragment;

    public ListPointsInterest(String api_key, LatLng latLng, String radius, Context context, EditFragment editFragment) {
        listPointsInterest = new ArrayList<>();
        listPointsInterestTemp = new ArrayList<>();
        this.context = context;
        this.editFragment=editFragment;

        if(editFragment!=null && latLng==null)
            editFragment.setInterestPoints(listPointsInterest.toString());
        else
            getListPlacesNearby(api_key, latLng, radius);
    }

    private void getListPlacesNearby(String api_key, LatLng latLng, String radius) {

        dispose();
        String location = String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude);

        this.disposable = ApiStream.streamFetchgetSearchNearbyPlaces(api_key,radius,location).subscribeWith(new DisposableObserver<PlaceNearby>() {

            @Override
            public void onNext(PlaceNearby placeNearby) {
                buildPointInterest(placeNearby);
            }

            @Override
            public void onError(Throwable e) {
                String text = context.getResources().getString(R.string.error_interest_points) + "\n" + e.toString();
                Toast toast = Toast.makeText(context,text,Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onComplete() {
                listPointsInterest.addAll(Utils.removeDuplicates(listPointsInterestTemp));

                dispose();

                if(editFragment!=null)
                    editFragment.setInterestPoints(listPointsInterest.toString());
            }
        });
    }

    @SuppressLint("ResourceType")
    private void buildPointInterest(PlaceNearby placeNearby){

        List<String> listTypes = new ArrayList<>();

        // For each place nearby, recover the list of types (point of interest)
        if(placeNearby !=null){
            if(placeNearby.getResults()!=null){
                for(Result result : placeNearby.getResults()){
                    if(result.getTypes()!=null){
                        listTypes.addAll(result.getTypes());
                    }
                }
            }
        }
        listPointsInterestTemp.addAll(Utils.getInterestPoints(listTypes, context));
    }

    @Override
    public void dispose() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    @Override
    public boolean isDisposed() {
        return false;
    }

    public List<String> getListPointsInterest() {
        return listPointsInterest;
    }
}
