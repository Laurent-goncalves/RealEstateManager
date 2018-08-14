package com.openclassrooms.realestatemanager.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ImagesDisplayViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_property) ImageView image;
    @BindView(R.id.title_image_property) TextView titleImage;

    public ImagesDisplayViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void configureImagesViews(ImageProperty imageProperty) {

        // set image
        image.setImageURI(imageProperty.getImage());

        // set title
        String title = imageProperty.getDescription();
        titleImage.setText(title);
    }
}
