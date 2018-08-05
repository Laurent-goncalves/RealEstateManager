package com.openclassrooms.realestatemanager.Controllers.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.CallbackImageSelect;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Views.ImagesRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.Views.ImagesViewHolder;

import org.mozilla.javascript.tools.jsc.Main;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment implements CallbackImageSelect {

    @BindView(R.id.switch_sold) Switch switchSold;
    @BindView(R.id.list_type_properties) Spinner listProperties;
    @BindView(R.id.price_edit_text) EditText priceEdit;
    @BindView(R.id.listview_dates) LinearLayout linearLayoutDates;
    @BindView(R.id.calendar) CalendarView calendar;
    @BindView(R.id.surface_edit_text) EditText surfaceEdit;
    @BindView(R.id.nbrooms_property_layout) RelativeLayout relativeLayoutNbRooms;
    @BindView(R.id.address_edit_text) EditText addressEdit;
    @BindView(R.id.description_edit_text) EditText descriptionEdit;
    @BindView(R.id.buttonCancel) Button buttonCancel;
    @BindView(R.id.buttonSave) Button buttonSave;
    private ImageView buttonPlus;
    private ImageView buttonLess;
    private TextView nbRooms;
    private TextView datePublish;
    private TextView dateSold;
    private static final String PROPERTY_JSON = "property_json";
    private static final String IMAGES_JSON = "images_json";
    private Property propertyInit;
    private List<ImageProperty> listImages;
    private Property propertyFinal;
    private int roomNb;
    private CallbackImageSelect mCallbackImageSelect;
    private MainActivity mainActivity;

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        // Set the mainActivity
        mainActivity = (MainActivity) getActivity();

        // We recover in the bundle the property in json format
        if(getArguments()!=null){
            Gson gson = new Gson();
            String propJSON = getArguments().getString(PROPERTY_JSON,null);
            String imgJSON = getArguments().getString(IMAGES_JSON,null);

            Type propertyType = new TypeToken<Property>(){}.getType();
            Type imagesType = new TypeToken<List<ImageProperty>>(){}.getType();

            propertyInit = gson.fromJson(propJSON,propertyType);
            listImages= gson.fromJson(imgJSON,imagesType);
        }

        mCallbackImageSelect = this;
        configureAllAreas();
        return view;
    }

    private void configureAllAreas(){

        if(propertyInit!=null){

            // configure switch (sold ?)
            configureSwitchSold();

            // configure type of property
            configurePropertyType();

            // configure price
            priceEdit.setText(String.valueOf(propertyInit.getPrice()));

            // configure date publication
            datePublish = linearLayoutDates.findViewById(R.id.publishing_date_selector).findViewById(R.id.date_publish_selected);
            datePublish.setText(propertyInit.getDateSold());

            // configure date sold
            dateSold = linearLayoutDates.findViewById(R.id.selling_date_selector).findViewById(R.id.date_sale_selected);
            dateSold.setText(propertyInit.getDateSold());

            // configure surface
            surfaceEdit.setText(String.valueOf(propertyInit.getSurface()));

            // configure number of rooms
            nbRooms = relativeLayoutNbRooms.findViewById(R.id.room_number_selector).findViewById(R.id.text_selection);
            roomNb = propertyFinal.getRoomNumber();
            nbRooms.setText(String.valueOf(roomNb));

            // configure address
            addressEdit.setText(propertyInit.getAddress());

            // configure description
            descriptionEdit.setText(propertyInit.getDescription());
        }
        configureButtonsPlusAndLess();
        configureButtonsCancelAndSave();
    }

    @SuppressLint("CutPasteId")
    private void configureButtonsPlusAndLess(){
        buttonPlus= relativeLayoutNbRooms.findViewById(R.id.room_number_selector).findViewById(R.id.plus_button);

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roomNb<10) {
                    roomNb++;
                    nbRooms.setText(String.valueOf(roomNb));
                }
            }
        });

        buttonLess= relativeLayoutNbRooms.findViewById(R.id.room_number_selector).findViewById(R.id.less_button);

        buttonLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roomNb>=1) {
                    roomNb--;
                    nbRooms.setText(String.valueOf(roomNb));
                }
            }
        });
    }

    private void configureButtonsCancelAndSave(){




    }

    private void configureImagesProperty(){



    }

    private void configurePropertyType(){

        String[] listPropertiesTypes = getResources().getStringArray(R.array.type_property);
        int i = 0;

        for(String type : listPropertiesTypes){
            if(type!=null){
                if(type.equals(propertyInit.getType())){
                    break;
                }
            }
            i++;
        }
        listProperties.setSelection(i);
    }

    private void configureSwitchSold(){
        if(propertyInit.getSold())
            switchSold.setChecked(true);
        else
            switchSold.setChecked(false);
    }

    @Override
    public void getImageFromGallery(ImagesViewHolder imagesViewHolder) {
        mainActivity.getImageFromGallery(imagesViewHolder);
    }
}
