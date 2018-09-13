package com.openclassrooms.realestatemanager.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;
import java.text.NumberFormat;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PropertyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.main_image_property) ImageView mainImage;
    @BindView(R.id.property_type_view) TextView typeTextView;
    @BindView(R.id.property_location_view) TextView locTextView;
    @BindView(R.id.property_cost_view) TextView costTextView;
    @BindView(R.id.item_property_layout) LinearLayout propertyLayout;

    public PropertyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void configurePropertiesViews(Property property, BaseActivity baseActivity){
        // Main Image
        Utils.setImageBitmapInView(property.getMainImagePath(),mainImage, baseActivity);

        // Finalize layout
        finalizeLayout(property);
    }

    private void finalizeLayout(Property property){

        // Type of appartment
        typeTextView.setText(property.getType());

        // Location
        if(property.getAddress()!=null){
            if(property.getAddress().length()>25) {

                String address = property.getAddress().substring(0, 25) + "...";
                locTextView.setText(address);
            } else
                locTextView.setText(property.getAddress());
        }

        // Price
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String price = formatter.format(property.getPrice());
        costTextView.setText(price);
    }

    public LinearLayout getPropertyLayout() {
        return propertyLayout;
    }

    public TextView getCostTextView() {
        return costTextView;
    }
}
