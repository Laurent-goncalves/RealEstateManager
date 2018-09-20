package com.openclassrooms.realestatemanager.Views;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class ImagesDisplayViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_property) ImageView image;
    @BindView(R.id.title_image_property) TextView titleImage;
    private View view;

    public ImagesDisplayViewHolder(View itemView) {
        super(itemView);
        view=itemView;
        ButterKnife.bind(this, itemView);
    }

    public void configureImagesViews(ImageProperty imageProperty, BaseActivityListener baseActivityListener, int positionSelected) {

        // set image
        baseActivityListener.setImage(imageProperty.getImagePath(),image);

        // set title and layout
        finalizeLayoutImage(imageProperty, positionSelected);
    }

    private void finalizeLayoutImage(ImageProperty imageProperty, int positionSelected){
        // set title
        String title = imageProperty.getDescription();
        if(title==null)
            titleImage.setVisibility(GONE);
        else {
            titleImage.setVisibility(VISIBLE);
            titleImage.setText(title);
        }

        // add or remove background (borders around) according to position selected
        if(this.getAdapterPosition()==positionSelected)
            view.setBackgroundResource(R.drawable.background_image_selected);
        else
            view.setBackground(null);
    }


}
