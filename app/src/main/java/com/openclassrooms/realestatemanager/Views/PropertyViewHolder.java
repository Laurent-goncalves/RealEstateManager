package com.openclassrooms.realestatemanager.Views;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.UtilsBaseActivity;
import java.text.NumberFormat;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PropertyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.main_image_property) ImageView mainImage;
    @BindView(R.id.property_type_view) TextView typeTextView;
    @BindView(R.id.property_location_view) TextView locTextView;
    @BindView(R.id.property_cost_view) TextView costTextView;
    @BindView(R.id.item_property_layout) LinearLayout propertyLayout;
    private final static String MODE_TABLET = "mode_tablet";
    private Context context;
    private String modeDevice;

    public PropertyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
    }

    public void configurePropertiesViews(Property property, Context context, String modeDevice, BaseActivityListener baseActivityListener){
        this.context=context;
        this.modeDevice = modeDevice;

        // Main Image
        UtilsBaseActivity.setImage(property.getMainImagePath(), mainImage,baseActivityListener.getBaseActivity());

        // Finalize layout
        finalizeLayout(property);
    }

    private void finalizeLayout(Property property){

        // Type of appartment
        typeTextView.setText(property.getType());

        // Location
        if(property.getAddress()!=null){

            int limit;

            if(modeDevice.equals(MODE_TABLET)) {
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    limit = 20;
                else
                    limit = 35;
            } else {
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    limit = 20;
                else
                    limit = 30;
            }

            // remove characters from property address if address is too long
            if(property.getAddress().length()>limit) {
                String address = property.getAddress().substring(0, limit) + "...";
                locTextView.setText(address);
            } else
                locTextView.setText(property.getAddress());
        }

        // Price
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String price = formatter.format(property.getPrice());
        costTextView.setText(price);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public LinearLayout getPropertyLayout() {
        return propertyLayout;
    }

    public TextView getCostTextView() {
        return costTextView;
    }
}
