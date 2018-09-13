package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Fragments.SearchFragment;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.Provider.SearchContentProvider;
import com.openclassrooms.realestatemanager.R;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;



public class LaunchSearchQuery {

    @BindView(R.id.switch_sold_search) Switch soldView;
    @BindView(R.id.list_type_properties_search) Spinner typePropView;
    @BindView(R.id.start_date_publish_selected_search) TextView startPublishView;
    @BindView(R.id.end_date_publish_selected_search) TextView endPublishView;
    @BindView(R.id.price_inf_search) EditText priceInfView;
    @BindView(R.id.price_sup_search) EditText priceSupView;
    @BindView(R.id.surface_inf_search) EditText surfaceInfView;
    @BindView(R.id.surface_sup_search) EditText surfaceSupView;
    private Context context;
    private SearchFragment searchFragment;

    public LaunchSearchQuery(View view, Context context, SearchFragment searchFragment) {
        ButterKnife.bind(this,view);
        this.context = context;
        this.searchFragment = searchFragment;
        launchSearchProperties();
    }

    // -------------------------------------------------------------------------------------------------------
    // --------------------------------------- 1 - GET CRITERIA ----------------------------------------------
    // -------------------------------------------------------------------------------------------------------

    private int getSold(){
        if(soldView.isChecked())
            return 1;
        else
            return 0;
    }

    private Double getSurfaceInf(){

        if(surfaceInfView.getText()!=null){
            if(surfaceInfView.getText().toString().length()==0) { // if no text written
                return 0d;
            } else
                return Double.parseDouble(surfaceInfView.getText().toString());
        } else
            return 0d;
    }

    private Double getSurfaceSup(){

        if(surfaceSupView.getText()!=null){
            if(surfaceSupView.getText().toString().length()==0) { // if no text written
                return 999d;
            } else
                return Double.parseDouble(surfaceSupView.getText().toString());
        } else
            return 999d;
    }

    private Double getPriceInf(){
        if(priceInfView.getText()!=null){
            if(priceInfView.getText().toString().length()==0) { // if no text written
                return 0d;
            } else
                return Double.parseDouble(priceInfView.getText().toString());
        } else
            return 0d;
    }

    private Double getPriceSup(){
        if(priceSupView.getText()!=null){
            if(priceSupView.getText().toString().length()==0) { // if no text written
                return 9999999d;
            } else
                return Double.parseDouble(priceSupView.getText().toString());
        } else
            return 9999999d;
    }

    private String getTypeProperty(){
        return typePropView.getSelectedItem().toString();
    }

    private String getDateInf(){
        if(startPublishView.getText()!=null){
            if(startPublishView.getText().toString().length()==0) // if no text written
                return "01/01/2000";
            else
                return startPublishView.getText().toString();
        } else
            return "01/01/2000";
    }

    private String getDateSup(){
        if(endPublishView.getText()!=null){
            if(endPublishView.getText().toString().length()==0) // if no text written
                return "31/12/9999";
            else
                return endPublishView.getText().toString();
        } else
            return "31/12/9999";
    }

    private int getRoomNbMin(){
        return searchFragment.getRoomNbMin();
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

        Double radius = Double.parseDouble(context.getResources().getString(R.string.radius));
        List<Property> listProperties = new ArrayList<>();

        if(searchFragment.getSearchLoc()!=null){

            if(listPropertyTemp.size()>0){

                for(Property property : listPropertyTemp){
                    if(property!=null){
                        if(property.getLat()!=0 && property.getLng()!=0){

                            LatLng propertyLoc = new LatLng(property.getLat(),property.getLng());

                            if(Utils.isLocationInsideBounds(searchFragment.getSearchLoc(),propertyLoc,radius))
                                listProperties.add(property);
                        }
                    }
                }
            }

        } else { // if the user has not chosen any specific location
            listProperties.addAll(listPropertyTemp);
        }

        displayResults(listProperties);
    }

    private void displayResults(List<Property> listProperties){
        searchFragment.displayResults(listProperties);
    }
}
