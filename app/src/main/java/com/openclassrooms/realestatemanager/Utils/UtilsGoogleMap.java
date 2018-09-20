package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.StyleableRes;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.Provider.MapsContentProvider;

import java.util.ArrayList;
import java.util.List;

public class UtilsGoogleMap {

    private final static String EXTRA_LAT_CURRENT = "latitude_current_location";
    private final static String EXTRA_LONG_CURRENT = "longitude_current_location";
    private final static Double RADIUS_MAPS = 2000d;

    public static List<Property> getAllProperties(Context context){
        List<Property> fullListProperties = new ArrayList<>();

        MapsContentProvider mapsContentProvider = new MapsContentProvider();
        mapsContentProvider.setParametersQuery(context);

        final Cursor cursor = mapsContentProvider.query(null, null, null, null, null);

        if (cursor != null){
            if(cursor.getCount() >0){
                while (cursor.moveToNext()) {
                    fullListProperties.add(Property.getPropertyFromCursor(cursor));
                }
            }
            cursor.close();
        }

        return fullListProperties;
    }

    public static List<Property> filterListPropertiesByLocation(LatLng cameraTarget, List<Property> fullListProperties){

        List<Property> listProperties = new ArrayList<>();

        for(Property property : fullListProperties){

            LatLng propLatLng = new LatLng(property.getLat(),property.getLng());

            if(isLocationInsideBounds(cameraTarget, propLatLng,RADIUS_MAPS)){
                listProperties.add(property);
            }
        }

        return listProperties;
    }

    public static Boolean isLocationInsideBounds(LatLng searchLoc, LatLng propertyLoc, Double radius){
        return SphericalUtil.computeDistanceBetween(searchLoc, propertyLoc) <= radius;
    }

    public static List<String> getInterestPoints(List<String> listTypes, Context context) {

        int index;
        @StyleableRes int indexRes = 1;

        List<String> listPointsInterestTemp = new ArrayList<>();

        // Get list of interests points by type from xml file
        List<String> listTypesRes = new ArrayList<>();
        List<String> listInterestRes = new ArrayList<>();

        List<TypedArray> listTypedArray = ResourceHelper.getMultiTypedArray(context);

        for (TypedArray item : listTypedArray) {
            listTypesRes.add(item.getString(indexRes));
            listInterestRes.add(item.getString(0));
        }

        // For each types, get the point of interest (based on string array in the xml typesPointsInterest)
        for (String type : listTypes) {
            index = Utils.getIndexFromList(type, listTypesRes);
            if (index != -1) {
                listPointsInterestTemp.add(listInterestRes.get(index));
            }
        }

        return listPointsInterestTemp;
    }

    public static LatLng findPlaceHighestLikelihood(@NonNull Task<PlaceLikelihoodBufferResponse> task){

        Place placeHighestLikelihood = null;
        float percentage = 0;

        PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

        for (PlaceLikelihood placeLikelihood : likelyPlaces) {       // for each placeLikelihood

            if(placeLikelihood.getLikelihood() > percentage) {       // if the likelihood is higher than the other ones,
                placeHighestLikelihood = placeLikelihood.getPlace(); //   set this place as placeLikelihood
                percentage = placeLikelihood.getLikelihood();        //   set this percentage as highest likelihood
            }
        }

        LatLng current_location=null;

        if(placeHighestLikelihood!=null)
            current_location = placeHighestLikelihood.getLatLng();

        likelyPlaces.release();

        return current_location;
    }

    public static LatLng findLastCurrentLocation(SharedPreferences sharedPreferences){

        saveLastCurrentLocation(sharedPreferences, new LatLng(48.866298, 2.383746)); // for Travis integration tests

        Float latitude = sharedPreferences.getFloat(EXTRA_LAT_CURRENT,0);
        Float longitude = sharedPreferences.getFloat(EXTRA_LONG_CURRENT,0);

        LatLng last_location;

        if(latitude == 0 && longitude == 0)
            last_location=new LatLng(48.866298, 2.383746);
        else
            last_location = new LatLng(latitude,longitude);

        return last_location;
    }


    public static void saveLastCurrentLocation(SharedPreferences sharedPreferences, LatLng current_loc){
        sharedPreferences.edit().putFloat(EXTRA_LAT_CURRENT,(float) current_loc.latitude).apply();
        sharedPreferences.edit().putFloat(EXTRA_LONG_CURRENT,(float) current_loc.longitude).apply();
    }
}
