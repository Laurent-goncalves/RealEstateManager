package com.openclassrooms.realestatemanager.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.openclassrooms.realestatemanager.Models.CallbackImageSelect;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import static butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED;

public class ImagesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_property) private ImageView image;
    @BindView(R.id.edit_icon_symbol) private ImageView editIcon;
    @BindView(R.id.delete_icon_symbol) private ImageView deleteIcon;
    @BindView(R.id.icon_add_photo) private LinearLayout addPhotoPanel;
    @BindView(R.id.title_image_property) private TextView titleImage;
    @BindView(R.id.extra_panel) private RelativeLayout extraPanel;
    private int propertyId;
    private View view;
    private PropertyDatabase database;
    private Context context;
    private static String NEW_IMAGE = "newImageToInsert";
    private static String UPDATE_IMAGE = "updateExistingImage";
    private String typeChange;
    private ImageProperty imageProperty;
    private CallbackImageSelect mCallbackImageSelect;

    ImagesViewHolder(View itemView) {
        super(itemView);
        this.view=itemView;
        ButterKnife.bind(this, itemView);
    }

    // ---------------------------------------------------------------------------------------------------
    // -------------------------------- ON CLICK LISTENER BUTTONS ----------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @OnClick(R.id.delete_icon_symbol)
    private void deleteImage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(context.getResources().getString(R.string.delete_image));
        builder.setMessage(context.getResources().getString(R.string.delete_confirmation));
        builder.setPositiveButton(context.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Delete image from database
                        database.imageDao().deleteImage(imageProperty.getId());

                        // Remove image from ImageView
                        image.setImageDrawable(null);

                        // destroy the view
                        view.setVisibility(View.GONE);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnTextChanged(value=R.id.description_image_edit_text, callback = AFTER_TEXT_CHANGED)
    private void changeTitleImage(Editable s){
        if(s.length()==0)
            titleImage.setVisibility(View.GONE);
        else {
            titleImage.setVisibility(View.VISIBLE);
            titleImage.setText(s.toString());
        }
    }

    @OnClick(R.id.edit_icon_symbol)
    private void configureEditButton() {
        openExtraPanel();
    }

    @OnClick(R.id.buttonImageSave)
    private void save(){
        if(typeChange.equals(NEW_IMAGE)) {

            // Insert the new image in database
            if(image.getDrawable()!=null) {
                modifyImageProperty();
                database.imageDao().insertImage(imageProperty);

                Toast toast = Toast.makeText(context,context.getResources().getString(R.string.image_saved),Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(context,context.getResources().getString(R.string.no_image_saved),Toast.LENGTH_LONG);
                toast.show();
            }

        } else { // IF UPDATE IMAGE

            modifyImageProperty();

            // Put the final imageProperty in the database
            database.imageDao().updateImage(imageProperty);

            Toast toast = Toast.makeText(context,context.getResources().getString(R.string.image_updated),Toast.LENGTH_LONG);
            toast.show();
        }

        // Close extra panel
        extraPanel.setVisibility(View.GONE);
    }

    @OnClick(R.id.buttonImageCancel)
    private void cancel(){

        if(imageProperty.getImage()!=null){
            // Re-initialize the views
            image.setImageBitmap(BitmapFactory.decodeByteArray(imageProperty.getImage(), 0,
                    imageProperty.getImage().length));
            titleImage.setText(imageProperty.getDescription());

            // display the icon to edit and delete, image, title
            editIcon.setVisibility(View.VISIBLE);
            deleteIcon.setVisibility(View.VISIBLE);

            // remove the panel uneccessary
            addPhotoPanel.setVisibility(View.GONE);

        } else {
            // remove the icon to edit and delete, image, title
            image.setVisibility(View.GONE);
            editIcon.setVisibility(View.GONE);
            deleteIcon.setVisibility(View.GONE);
            titleImage.setVisibility(View.GONE);

            // remove the panels uneccessary
            addPhotoPanel.setVisibility(View.VISIBLE);
        }

        // Close extra panel
        extraPanel.setVisibility(View.GONE);
    }

    @OnClick(R.id.icon_folder_open)
    private void selectNewImageFromDevice(){
        mCallbackImageSelect.getImageFromGallery(this);
    }

    @OnClick(R.id.icon_add_photo)
    private void openExtraPanelToAddPhoto(){
        extraPanel.setVisibility(View.VISIBLE);
    }


    // ---------------------------------------------------------------------------------------------------
    // ------------------------------------- CONFIGURATION VIEW ------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    public void configureImagesViews(ImageProperty imageProperty, int propertyId, Context context, CallbackImageSelect mCallbackImageSelect) {

        // Initialize variables
        this.context=context;
        this.propertyId=propertyId;
        this.mCallbackImageSelect=mCallbackImageSelect;
        this.database = PropertyDatabase.getInstance(context);

        if(imageProperty.getImage()!=null){ // if an image exist

            // set type of change (UPDATE IMAGE)
            typeChange = UPDATE_IMAGE;
            this.imageProperty = imageProperty;

            // insert image in the imageView
            image.setImageBitmap(BitmapFactory.decodeByteArray(imageProperty.getImage(), 0,
                    imageProperty.getImage().length));

            // insert title under the image
            if(imageProperty.getDescription()!=null){
                titleImage.setVisibility(View.VISIBLE);
                titleImage.setText(imageProperty.getDescription());
            } else
                titleImage.setVisibility(View.GONE);

            // display the icon to edit and delete, image
            image.setVisibility(View.VISIBLE);
            editIcon.setVisibility(View.VISIBLE);
            deleteIcon.setVisibility(View.VISIBLE);

            // remove the panels uneccessary
            addPhotoPanel.setVisibility(View.GONE);
            extraPanel.setVisibility(View.GONE);

        } else {
            // set type of change (NEW IMAGE)
            typeChange = NEW_IMAGE;

            // remove uneccessary views
            image.setVisibility(View.GONE);
            editIcon.setVisibility(View.GONE);
            deleteIcon.setVisibility(View.GONE);
            titleImage.setVisibility(View.GONE);
            extraPanel.setVisibility(View.GONE);

            // display the panel to add a photo
            addPhotoPanel.setVisibility(View.VISIBLE);
        }
    }

    public void setImage(Bitmap bitmap){
        image.setImageBitmap(bitmap);
        addPhotoPanel.setVisibility(View.GONE);
    }

    private void modifyImageProperty(){
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        imageProperty = new ImageProperty(Utils.getBitmapAsByteArray(bitmap), titleImage.getText().toString(), propertyId);
    }

    private void openExtraPanel(){
        // open the panel with fields for changing image
        extraPanel.setVisibility(View.VISIBLE);

        // hide the edit icon
        editIcon.setVisibility(View.INVISIBLE);
        deleteIcon.setVisibility(View.INVISIBLE);
    }
}
