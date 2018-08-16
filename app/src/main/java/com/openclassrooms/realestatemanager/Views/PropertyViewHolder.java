package com.openclassrooms.realestatemanager.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PropertyViewHolder extends RecyclerView.ViewHolder {

    private View view;
    @BindView(R.id.main_image_property) ImageView mainImage;
    @BindView(R.id.property_type_view) TextView typeTextView;
    @BindView(R.id.property_location_view) TextView locTextView;
    @BindView(R.id.property_cost_view) TextView costTextView;
    @BindView(R.id.item_property_layout) LinearLayout propertyLayout;

    public PropertyViewHolder(View itemView) {
        super(itemView);
        this.view=itemView;
        ButterKnife.bind(this, itemView);
    }

    public void configurePropertiesViews(Property property){

        typeTextView.setText(property.getType());
        locTextView.setText(property.getAddress());


        // Price
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String price = formatter.format(property.getPrice());
        costTextView.setText(price);



        //mainImage.setImageURI();
    }
}
