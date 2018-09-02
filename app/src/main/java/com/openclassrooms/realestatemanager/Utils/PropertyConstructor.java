package com.openclassrooms.realestatemanager.Utils;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PropertyConstructor {

    @BindView(R.id.listview_dates) LinearLayout linearLayoutDates;
    @BindView(R.id.switch_sold) Switch switchSold;
    @BindView(R.id.list_type_properties) Spinner listProperties;
    @BindView(R.id.price_edit_text) EditText priceEdit;
    @BindView(R.id.surface_edit_text) EditText surfaceEdit;
    @BindView(R.id.nbrooms_property_layout) RelativeLayout relativeLayoutNbRooms;
    @BindView(R.id.description_edit_text) EditText descriptionEdit;
    @BindView(R.id.estateagent_edit_text) EditText estateAgentEdit;
    @BindView(R.id.interest_points_editview) EditText interestView;
    private EditFragment editFragment;
    private byte[] mapStatic;
    private Double lat;
    private Double lng;
    private String mainImagePath;
    private int id;
    private String type;
    private Double price;
    private Double surface;
    private int roomNumber;
    private String description;
    private String address;
    private String interestPoints;
    private Boolean sold;
    private String dateStart;
    private String datesold;
    private String estateAgent;

    public Property createPropertyFromViews(EditFragment editFragment, View view){

        // assign views and variables
        ButterKnife.bind(this, view);
        this.editFragment = editFragment;

        // Get datas
        getDataForProperty();

        // Return property
        return new Property(id, type,price,surface, roomNumber, description, address, interestPoints, sold,
                dateStart, datesold, lat, lng, estateAgent, mapStatic, mainImagePath);
    }

    private void getDataForProperty() {

        // Get ID property
        id=editFragment.getProperty().getId();

        // Get property type
        type=listProperties.getSelectedItem().toString();

        // get surface
        surface=Double.parseDouble(surfaceEdit.getText().toString());

        // get price
        price=Double.parseDouble(priceEdit.getText().toString());

        // get room number
        TextView nbRooms = relativeLayoutNbRooms.findViewById(R.id.room_number_selector).findViewById(R.id.text_selection);
        roomNumber = Integer.parseInt(nbRooms.getText().toString());

        // get description
        description= descriptionEdit.getText().toString();

        // get address property
        address = editFragment.getSearchView().getQuery().toString();

        // get interest points
        interestPoints =interestView.getText().toString() ;

        // get estate agent
        estateAgent = estateAgentEdit.getText().toString();

        // get sold state
        sold = switchSold.isChecked();

        // get date publish
        TextView datePublish = linearLayoutDates.findViewById(R.id.publishing_date_selector).findViewById(R.id.date_publish_selected);
        dateStart = datePublish.getText().toString();

        // get date sold
        TextView dateSold = linearLayoutDates.findViewById(R.id.selling_date_selector).findViewById(R.id.date_sale_selected);
        datesold = dateSold.getText().toString();

        // Get static map
        if (editFragment.getStaticMap() != null)
            mapStatic = Utils.getBitmapAsByteArray(editFragment.getStaticMap());
        else
            mapStatic = null;

        // Get lat and lng
        if (editFragment.getLatLngAddress() != null) {
            lat = editFragment.getLatLngAddress().latitude;
            lng = editFragment.getLatLngAddress().longitude;
        } else {
            lat = 0d;
            lng = 0d;
        }

        // get Main Image URI
        if (editFragment.getMainImagePath() != null)
            mainImagePath = editFragment.getMainImagePath();
        else
            mainImagePath = null;
    }

}
