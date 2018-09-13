package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.R;
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
    public void configureImagesViews(ImageProperty imageProperty, ImagesEditAdapter adapter, Boolean inEdition, Boolean changesOngoing, Context context, BaseActivity baseActivity) {

        super.configureImagesViews(imageProperty, adapter, inEdition, changesOngoing, context, baseActivity);

        this.imageProperty=imageProperty;

        if(imageProperty.getImagePath()!=null){ // if an image exist
            imagePathInit = imageProperty.getImagePath();
            imagePath = imageProperty.getImagePath();
            image.setVisibility(View.VISIBLE);

            setExtraImage(imagePath);
        }

        // insert title under the image
        if(imageProperty.getDescription()!=null){
            descriptionInit = imageProperty.getDescription();
            description = imageProperty.getDescription();
            titleImage.setVisibility(View.VISIBLE);
            titleImage.setText(imageProperty.getDescription());
            descripEdit.setText(imageProperty.getDescription());
        } else
            titleImage.setVisibility(View.GONE);

        // Remove or add views
        if(inEdition && changesOngoing){
            editIcon.setVisibility(View.GONE);
            deleteIcon.setVisibility(View.GONE);
            extraPanel.setVisibility(View.VISIBLE);
            view.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.extra_panel_width_expanded);
        } else if (!inEdition && changesOngoing) {
            editIcon.setVisibility(View.GONE);
            deleteIcon.setVisibility(View.GONE);
            extraPanel.setVisibility(View.GONE);
            view.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.extra_panel_width_reduced);
        } else {
            editIcon.setVisibility(View.VISIBLE);
            deleteIcon.setVisibility(View.VISIBLE);
            extraPanel.setVisibility(View.GONE);
            view.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.extra_panel_width_reduced);
        }

        // remove the panels uneccessary
        addPhotoButton.setVisibility(View.GONE);
    }

    // ---------------------------------------------------------------------------------------------------
    // -------------------------------- ON CLICK LISTENER BUTTONS ----------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @OnClick(R.id.buttonImageOK)
    public void ok(){

        if(imagePath!=null) {
            // send a message Toast to the user
            Toast toast = Toast.makeText(context,context.getResources().getString(R.string.image_added),Toast.LENGTH_LONG);
            toast.show();

            // set new image and new description
            imageProperty.setImagePath(String.valueOf(imagePath));
            imageProperty.setDescription(description);

            // Add new image empty to the list and update the list
            adapter.addNewImageToList(imageProperty, false);

            // Close extra panel
            closeExtraPanel();

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

            // Close extra panel
            closeExtraPanel();
        }
    }
}
