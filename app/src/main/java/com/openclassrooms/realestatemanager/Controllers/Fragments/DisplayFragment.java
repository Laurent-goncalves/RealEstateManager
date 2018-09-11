package com.openclassrooms.realestatemanager.Controllers.Fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.MapsActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.SearchActivity;
import com.openclassrooms.realestatemanager.Models.CallbackImageChange;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.SimulationTool;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.Views.ImagesDisplayAdapter;
import java.text.NumberFormat;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DisplayFragment extends BasePropertyFragment implements CallbackImageChange {

    @BindView(R.id.type_property) TextView typePropView;
    @BindView(R.id.big_image_display) ImageView mainImageView;
    @BindView(R.id.list_images_property_display) RecyclerView listImagesView;
    @BindView(R.id.price_property) TextView priceView;
    @BindView(R.id.description) TextView descriptionView;
    @BindView(R.id.surface_display) TextView surfaceView;
    @BindView(R.id.nb_rooms_display) TextView roomsView;
    @BindView(R.id.address_display) TextView addressView;
    @BindView(R.id.interest_points_textview) TextView interestView;
    @BindView(R.id.static_mapview_property) ImageView mapStaticView;
    @BindView(R.id.sold_text_rotated) TextView soldText;
    @BindView(R.id.estate_agent_text) TextView estateView;
    @BindView(R.id.publication_date_text) TextView publishView;
    @BindView(R.id.sale_date_text) TextView soldView;
    @BindView(R.id.sold_date_layout) LinearLayout soldDateLayout;
    @BindView(R.id.buttonReturn) Button buttonReturn;
    private CallbackImageChange callbackImageChange;
    private int idProp;
    private ImagesDisplayAdapter adapter;
    private SearchActivity searchActivity;

    public DisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_property, container, false);
        ButterKnife.bind(this, view);

        // Set the variables
        listImages = new ArrayList<>();
        callbackImageChange = this;
        baseActivity = (BaseActivity) getActivity();
        context = baseActivity.getApplicationContext();

        // We recover in the bundle the property in json format
        if(getArguments()!=null){

            // recover mode selected (search, list, map)
            recoverModeSelected();

            // recover mode of device (phone or tablet)
            recoverDeviceMode();

            // configure button return
            configureButtonReturn();

            // Recover id of property to display
            idProp = getArguments().getInt(LAST_PROPERTY_SELECTED);

            // Recover the property datas
            recoverProperty(idProp);

            // Recover images from the property
            if(property.getMainImagePath()!=null)
                listImages.add(new ImageProperty(0, property.getMainImagePath(),null,idProp)); // add main image

            recoverImagesProperty(idProp);
        }

        if(property!=null){
            configureViews();
            configureImagesProperty();
        }

        return view;
    }

    @OnClick(R.id.buttonReturn)
    public void returnToList(){

        if(modeDevice.equals(MODE_TABLET)){ // ----------- TABLET MODE

            switch(modeSelected){
                case MODE_SEARCH:
                    searchActivity.getListFragLayout().setBackgroundColor(context.getResources().getColor(R.color.colorGrey));
                    baseActivity.returnToSearchCriteria();
                    break;
                case MODE_DISPLAY_MAPS:
                    mapsActivity.changeToMapMode(idProp);
                    break;
            }
        } else {     // ----------- PHONE MODE
            switch (modeSelected) {

                case MODE_DISPLAY:
                    baseActivity.configureAndShowListPropertiesFragment(MODE_DISPLAY, null);
                    break;
                case MODE_SEARCH:
                    baseActivity.returnToSearchCriteria();
                    break;
                case MODE_DISPLAY_MAPS:
                    mapsActivity.changeToMapMode(idProp);
                    break;
            }
        }
    }

    @OnClick(R.id.buttonSimulation)
    public void launchSimulation(){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        SimulationTool simulationTool = SimulationTool.newInstance(property);
        simulationTool.show(ft, "dialog");
    }

    // ---------------------------------------------------------------------------------------------------
    // ---------------------------------  CONFIGURE VIEWS  -----------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    private void configureButtonReturn(){

        if(modeDevice.equals(MODE_TABLET) && modeSelected.equals(MODE_DISPLAY))
            buttonReturn.setVisibility(View.GONE);
        else {
            buttonReturn.setVisibility(View.VISIBLE);
            buttonReturn.setText(buttonReturnText);
        }

        if(modeDevice.equals(MODE_TABLET) && modeSelected.equals(MODE_SEARCH)){
            searchActivity = (SearchActivity) getActivity();
        }
    }

    private void configureViews() {

        // set the main image
        if(property.getMainImagePath()!=null && baseActivity!=null)
            Utils.setImageBitmapInView(property.getMainImagePath(), mainImageView, baseActivity);
        /*else if (property.getMainImagePath()!=null && mapsActivity!=null)
            Utils.setImageBitmapInView(property.getMainImagePath(), mainImageView, mapsActivity);*/

        // set the type of property
        String typeProp = property.getType();
        typePropView.setText(typeProp);

        // set the description text
        descriptionView.setText(property.getDescription());

        // Remove textview "sold" if property is not sold
        if(!property.getSold()) {
            soldDateLayout.setVisibility(View.GONE);
            soldText.setVisibility(View.GONE);
        } else {
            soldDateLayout.setVisibility(View.VISIBLE);
            String soldDate = property.getDateSold();
            soldView.setText(soldDate);
        }

        // set the price
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String price = formatter.format(property.getPrice());
        priceView.setText(price);

        // set the surface
        String surface = property.getSurface() + "m²";
        surfaceView.setText(surface);

        // set the number of rooms
        String roomNb = String.valueOf(property.getRoomNumber());
        roomsView.setText(roomNb);

        // set estate agent
        String estateAgent = property.getEstateAgent();
        estateView.setText(estateAgent);

        // set publish date
        String publishDate = property.getDateStart();
        publishView.setText(publishDate);

        // set the address
        String address = property.getAddress();
        addressView.setText(address);

        // set the points of interest
        String points = Utils.removeHooksFromString(property.getInterestPoints());
        interestView.setText(points);

        // set static mapView
        if(property.getMap()!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(property.getMap(), 0, property.getMap().length);
            mapStaticView.setImageBitmap(bitmap);
        }
    }

    private void configureImagesProperty(){

        if(context!=null){

            if(listImages.size()==0)

                listImagesView.setVisibility(View.GONE);

            else {

                // Set the recyclerView in horizontal direction
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

                // Create adapter passing in the sample user data
                if(baseActivity!=null)
                    adapter = new ImagesDisplayAdapter(listImages, context, callbackImageChange, baseActivity);
                /*else if(mapsActivity!=null)
                    adapter = new ImagesDisplayAdapter(listImages, context, callbackImageChange, mapsActivity);*/

                // Attach the adapter to the recyclerview to populate items
                listImagesView.setAdapter(adapter);
                // Set layout manager to position the items
                listImagesView.setLayoutManager(layoutManager);
            }
        }
    }

    public void changeMainImage(int position){
        if(listImages.get(position)!=null && baseActivity!=null)
            Utils.setImageBitmapInView(listImages.get(position).getImagePath(),mainImageView, baseActivity);
        /*else if(listImages.get(position)!=null && mapsActivity!=null)
            Utils.setImageBitmapInView(listImages.get(position).getImagePath(),mainImageView, mapsActivity);*/
    }

}
