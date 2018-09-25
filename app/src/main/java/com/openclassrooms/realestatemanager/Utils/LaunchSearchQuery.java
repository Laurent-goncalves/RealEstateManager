package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.database.Cursor;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.Provider.SearchContentProvider;
import com.openclassrooms.realestatemanager.Models.SearchQuery;
import java.util.ArrayList;
import java.util.List;


public class LaunchSearchQuery {

    private Context context;
    private SearchQuery searchQuery;
    private List<Property> listProperties;

    public LaunchSearchQuery(Context context, SearchQuery searchQuery) {
        this.context = context;
        this.searchQuery = searchQuery;
        this.listProperties = new ArrayList<>();

        if(searchQuery!=null)
            launchSearchProperties();
    }

    // -------------------------------------------------------------------------------------------------------
    // --------------------------------------- 1 - GET CRITERIA ----------------------------------------------
    // -------------------------------------------------------------------------------------------------------

    private int getSold(){
        if(searchQuery.getSoldStatus())
            return 1;
        else
            return 0;
    }

    private Double getSurfaceInf(){
        if(searchQuery.getSurfaceInf()==0)
            return 0d;
        else
            return searchQuery.getSurfaceInf();
    }

    private Double getSurfaceSup(){
        if(searchQuery.getSurfaceSup()==0)
            return 999d;
        else
            return searchQuery.getSurfaceSup();
    }

    private Double getPriceInf(){
        if(searchQuery.getPriceInf()==0)
            return 0d;
        else
            return searchQuery.getPriceInf();
    }

    private Double getPriceSup(){
        if(searchQuery.getPriceSup()==0)
            return 9999999d;
        else
            return searchQuery.getPriceSup();
    }

    private String getTypeProperty(){
        return searchQuery.getTypeProperty();
    }

    private String getDateInf(){

        if(searchQuery.getDatePublishStart()==null)
            return "01/01/2000";
        else {
            if (searchQuery.getDatePublishStart().length() == 0) // if no text written
                return "01/01/2000";
            else
                return searchQuery.getDatePublishStart();
        }
    }

    private String getDateSup(){

        if(searchQuery.getDatePublishEnd()==null)
            return "31/12/9999";
        else {
            if (searchQuery.getDatePublishEnd().length() == 0) // if no text written
                return "31/12/9999";
            else
                return searchQuery.getDatePublishEnd();
        }
    }

    private int getRoomNbMin(){
        return searchQuery.getRoomNbMin();
    }

    // -------------------------------------------------------------------------------------------------------
    // --------------------------------------- 2 - LAUNCH SEARCH ---------------------------------------------
    // -------------------------------------------------------------------------------------------------------

    public void launchSearchProperties(){

        SearchContentProvider searchContentProvider = new SearchContentProvider();
        searchContentProvider.setParametersQuery(context, getSold(),getSurfaceInf(),getSurfaceSup(),getPriceInf(),getPriceSup(),getRoomNbMin(),getTypeProperty());
        List<Property> listPropertyTemp = new ArrayList<>();

        final Cursor cursor = searchContentProvider.query(null, null, null, null, null);

        if (cursor != null){
            if(cursor.getCount() >0){
                while (cursor.moveToNext()) {
                    listPropertyTemp.add(Property.getPropertyFromCursor(cursor));
                }
            }
            cursor.close();
        }

        filterResultsByDatePublish(listPropertyTemp);
    }

    // -------------------------------------------------------------------------------------------------------
    // ------------------------------- 3 - FILTER RESULTS and SEND RESULTS -----------------------------------
    // -------------------------------------------------------------------------------------------------------

    private void filterResultsByDatePublish(List<Property> listPropertyTemp){

        String dateinf = getDateInf();
        String datesup = getDateSup();
        List<Property> listPropertiesTemp = new ArrayList<>();

        if(listPropertyTemp.size()>0){

            for(Property property : listPropertyTemp){
                if(property!=null){
                    if(property.getDateStart()!=null){
                        if(Utils.isDateInsidePeriod(property.getDateStart(),dateinf,datesup))
                            listPropertiesTemp.add(property);
                    }
                }
            }
        }

        filterResultsByLocation(listPropertiesTemp);
    }

    private void filterResultsByLocation(List<Property> listPropertyTemp){

        Double radius = Double.parseDouble(String.valueOf(searchQuery.getRadius()));
        LatLng searchLoc = new LatLng(searchQuery.getSearchLocLat(),searchQuery.getSearchLocLng());

        if(searchLoc.latitude!=0 && searchLoc.longitude!=0){

            if(listPropertyTemp.size()>0){

                for(Property property : listPropertyTemp){
                    if(property!=null){
                        if(property.getLat()!=0 && property.getLng()!=0){

                            LatLng propertyLoc = new LatLng(property.getLat(),property.getLng());

                            if(UtilsGoogleMap.isLocationInsideBounds(searchLoc,propertyLoc,radius))
                                listProperties.add(property);
                        }
                    }
                }
            }

        } else { // if the user has not chosen any specific location
            listProperties.addAll(listPropertyTemp);
        }
    }

    public List<Property> getListProperties() {
        return listProperties;
    }

    public void setListProperties(List<Property> listProperties) {
        this.listProperties = listProperties;
    }
}
