package com.openclassrooms.realestatemanager.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;


public class ImagesDisplayViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_property) ImageView image;
    @BindView(R.id.title_image_property) TextView titleImage;
    private View view;

    public ImagesDisplayViewHolder(View itemView) {
        super(itemView);
        view=itemView;
        ButterKnife.bind(this, itemView);
    }

    public void configureImagesViews(ImageProperty imageProperty, MainActivity mainActivity, int positionSelected) {

        // set image
        if(imageProperty.getImagePath()!=null){
            Utils.setImageBitmapInView(imageProperty.getImagePath(),image,mainActivity);
        }

        // set title
        String title = imageProperty.getDescription();
        if(title==null)
            titleImage.setVisibility(GONE);
        else
            titleImage.setText(title);

        // add or remove background (borders around) according to position selected
        if(this.getAdapterPosition()==positionSelected)
            view.setBackgroundResource(R.drawable.background_image_selected);
        else
            view.setBackground(null);

    }
}
