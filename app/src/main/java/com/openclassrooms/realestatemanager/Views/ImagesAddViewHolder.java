package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.R;

import java.util.List;

import butterknife.OnClick;

public class ImagesAddViewHolder extends BaseImageViewHolder {

    public ImagesAddViewHolder(View itemView) {
        super(itemView);
    }

    // ---------------------------------------------------------------------------------------------------
    // ------------------------------------- CONFIGURATION VIEW ------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @Override
    public void configureImagesViews(List<ImageProperty> listImages, int position, ImagesEditAdapter adapter, Context context, BaseActivityListener baseActivityListener) {

        super.configureImagesViews(listImages,position, adapter, context, baseActivityListener);

        // remove uneccessary views
        editIcon.setVisibility(View.GONE);
        deleteIcon.setVisibility(View.GONE);
    }

    // ---------------------------------------------------------------------------------------------------
    // -------------------------------- ON CLICK LISTENER BUTTONS ----------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @OnClick(R.id.buttonImageOK)
    public void ok(){

        if(imageProperty.getImagePath()!=null) {

            // Add new image empty to the list and update the list
            adapter.addNewImageToList();

            // Close extra panel
            closeExtraPanel();
            adapter.notifyDataSetChanged();

            // send a message Toast to the user
            Toast toast = Toast.makeText(context,context.getResources().getString(R.string.image_added),Toast.LENGTH_LONG);
            toast.show();

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

        // Re-initialize imageProperty
        imageProperty.setImagePath(null);
        imageProperty.setDescription(null);

        // add panel "add a photo"
        addPhotoButton.setVisibility(View.VISIBLE);
        closeExtraPanel();
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.icon_add_photo)
    public void openExtraPanelToAddPhoto(){
        openExtraPanel();
        adapter.notifyDataSetChanged();
    }
}
