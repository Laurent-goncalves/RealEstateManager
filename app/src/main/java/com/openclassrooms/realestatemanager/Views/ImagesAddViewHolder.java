package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.R;
import butterknife.OnClick;

public class ImagesAddViewHolder extends BaseImageViewHolder {

    public ImagesAddViewHolder(View itemView) {
        super(itemView);
    }

    // ---------------------------------------------------------------------------------------------------
    // ------------------------------------- CONFIGURATION VIEW ------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @Override
    public void configureImagesViews(ImageProperty imageProperty, ImagesEditAdapter adapter, Boolean inEdition, Boolean changesOngoing, Context context, BaseActivity baseActivity) {

        super.configureImagesViews(imageProperty, adapter, inEdition, changesOngoing, context, baseActivity);

        this.imageProperty = new ImageProperty();

        // remove uneccessary views
        image.setVisibility(View.GONE);
        editIcon.setVisibility(View.GONE);
        deleteIcon.setVisibility(View.GONE);
        titleImage.setVisibility(View.GONE);


        if (inEdition){
            //extraPanel.setVisibility(View.VISIBLE);
            view.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.extra_panel_width_expanded);
        } else {
            //extraPanel.setVisibility(View.GONE);
            view.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.extra_panel_width_reduced);
        }

        // display the panel to add a photo
        addPhotoButton.setVisibility(View.VISIBLE);
    }

    // ---------------------------------------------------------------------------------------------------
    // -------------------------------- ON CLICK LISTENER BUTTONS ----------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @OnClick(R.id.buttonImageOK)
    public void ok(){

        if(imagePath!=null) {

            // send a message Toast to the user
            Toast toast = Toast.makeText(context,context.getResources().getString(R.string.image_saved),Toast.LENGTH_LONG);
            toast.show();

            // Update imageProperty
            imageProperty.setImagePath(imagePath);
            imageProperty.setDescription(description);

            // Add new image empty to the list and update the list
            adapter.addNewImageToList(imageProperty, true);

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
        // remove image
        image.setImageBitmap(null);
        image.setVisibility(View.GONE);
        titleImage.setVisibility(View.GONE);

        // add panel "add a photo"
        addPhotoButton.setVisibility(View.VISIBLE);
        closeExtraPanel();
    }

    @OnClick(R.id.icon_add_photo)
    public void openExtraPanelToAddPhoto(){
        openExtraPanel();
    }
}
