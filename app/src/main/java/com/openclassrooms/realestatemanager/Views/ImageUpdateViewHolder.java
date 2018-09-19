package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class ImageUpdateViewHolder extends BaseImageViewHolder {

    private String imagePathInit;
    private String descriptionInit;

    ImageUpdateViewHolder(View itemView) {
        super(itemView);
        this.view=itemView;
        ButterKnife.bind(this, itemView);
    }

    // ---------------------------------------------------------------------------------------------------
    // ------------------------------------- CONFIGURATION VIEW ------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @Override
    public void configureImagesViews(List<ImageProperty> listImages, int position, ImagesEditAdapter adapter, Context context, BaseActivityListener baseActivityListener) {

        super.configureImagesViews(listImages, position, adapter, context, baseActivityListener);

        descriptionInit = imageProperty.getDescription();
        imagePathInit = imageProperty.getImagePath();

        // add image if it exists
        if(imageProperty.getImagePath()!=null){ // if an image exist
            image.setVisibility(View.VISIBLE);
            setExtraImage(imageProperty.getImagePath());
        }

        // Remove or add icons deletion and edit photo
        if(imageProperty.getInEdition()){
            openExtraPanel();
            editIcon.setVisibility(View.GONE);
            deleteIcon.setVisibility(View.GONE);
        } else if (!imageProperty.getInEdition() && isAnImageInEdition(listImages)) {
            closeExtraPanel();
            editIcon.setVisibility(View.GONE);
            deleteIcon.setVisibility(View.GONE);
        } else {
            closeExtraPanel();
            editIcon.setVisibility(View.VISIBLE);
            deleteIcon.setVisibility(View.VISIBLE);
        }
    }

    // ---------------------------------------------------------------------------------------------------
    // -------------------------------- ON CLICK LISTENER BUTTONS ----------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @OnClick(R.id.buttonImageOK)
    public void ok(){

        if(imageProperty.getImagePath()!=null) {
            // send a message Toast to the user
            Toast toast = Toast.makeText(context,context.getResources().getString(R.string.image_updated),Toast.LENGTH_LONG);
            toast.show();

            // Close extra panel
            closeExtraPanel();
            adapter.notifyDataSetChanged();

        } else {

            // send an alert to the user
            Toast toast = Toast.makeText(context,context.getResources().getString(R.string.no_image_saved),Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @OnClick(R.id.buttonImageCancel)
    public void cancel(){

        if(imagePathInit!=null){
            // Re-initialize the views
            setExtraImage(imagePathInit);
            titleImage.setText(descriptionInit);

            // Re-initialize imageProperty
            imageProperty.setImagePath(imagePathInit);
            imageProperty.setDescription(descriptionInit);

            // Close extra panel
            closeExtraPanel();
            adapter.notifyDataSetChanged();
        }
    }
}
