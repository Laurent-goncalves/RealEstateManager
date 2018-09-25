package com.openclassrooms.realestatemanager.Utils;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.widget.ImageView;
import android.widget.Toast;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Models.CallbackPropertyAdapter;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Views.PropertyViewHolder;
import java.io.File;
import pub.devrel.easypermissions.EasyPermissions;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;


public class UtilsBaseActivity {

    private static final String MODE_PHONE = "mode_phone";
    private static final int RESULT_LOAD_IMAGE_VIEWHOLDER = 2;
    private static final int RESULT_LOAD_MAIN_IMAGE_ = 3;
    private static final int PERMISSIONS_REQUEST_ACCESS_IMAGE_GALLERY = 202;
    private static final int PERMISSIONS_REQUEST_ACCESS_MAIN_IMAGE_GALLERY = 203;
    private static final int PERMISSIONS_REQUEST_ACCESS_EXTRA_IMAGE_GALLERY = 204;
    private static final String MODE_TABLET = "mode_tablet";

    public static void askForConfirmationToLeaveEditMode(BaseActivity baseActivity, String modeSelected, String modeDevice, String fragmentDisplayed, int idProp) {

        Context context = baseActivity.getApplicationContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity);
        builder.setCancelable(true);
        builder.setTitle(context.getResources().getString(R.string.warning_title));
        builder.setMessage(context.getResources().getString(R.string.leave_edit_mode));
        builder.setPositiveButton(context.getResources().getString(R.string.confirm), (dialog, id) -> {
                    if(idProp!=-1) { // modify existing property
                        baseActivity.configureAndShowDisplayFragment(modeSelected, idProp);
                        if(modeDevice.equals(MODE_TABLET))
                            baseActivity.getListPropertiesFragment().changeSelectedItemInList(idProp,fragmentDisplayed);
                    } else { // add a new property
                        if(modeDevice.equals(MODE_TABLET)) {
                            baseActivity.configureAndShowDisplayFragment(modeSelected, baseActivity.getLastIdPropertyDisplayed());
                            baseActivity.getListPropertiesFragment().changeSelectedItemInList(baseActivity.getLastIdPropertyDisplayed(), fragmentDisplayed);
                        } else
                            baseActivity.configureAndShowListPropertiesFragment(modeSelected);
                    }
                }
        )
                .setNegativeButton(R.string.cancel, (dialog, id) -> { });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void askToChangeOfPropertyToSelect(BaseActivity baseActivity,PropertyViewHolder holder, int position, CallbackPropertyAdapter callbackPropertyAdapter) {
        baseActivity.setItemSelected(position);

        Context context = baseActivity.getApplicationContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity);
        builder.setCancelable(true);
        builder.setTitle(context.getResources().getString(R.string.warning_title));
        builder.setMessage(context.getResources().getString(R.string.change_item_list));
        builder.setPositiveButton(context.getResources().getString(R.string.confirm), (dialog, id) -> callbackPropertyAdapter.proceedToChangeOfPropertySelection(holder,position))
                .setNegativeButton(R.string.cancel, (dialog, id) -> { });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void askForConfirmationToDeleteImage(BaseActivity baseActivity, int viewHolderPosition){
        Context context = baseActivity.getApplicationContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity);
        builder.setCancelable(true);
        builder.setTitle(context.getResources().getString(R.string.delete_image));
        builder.setMessage(context.getResources().getString(R.string.delete_confirmation));
        builder.setPositiveButton(context.getResources().getString(R.string.confirm), (dialog, id) -> baseActivity.getEditFragment().proceedToDeletion(viewHolderPosition))
                .setNegativeButton(R.string.cancel, (dialog, id) -> { });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void askForConfirmationToResetSearchQuery(BaseActivity baseActivity){
        Context context = baseActivity.getApplicationContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity);
        builder.setCancelable(true);
        builder.setTitle(context.getResources().getString(R.string.warning_title));
        builder.setMessage(context.getResources().getString(R.string.reset_confirmation));
        builder.setPositiveButton(context.getResources().getString(R.string.confirm), (dialog, id) -> baseActivity.getSearchFragment().resetSearchQuery())
                .setNegativeButton(R.string.cancel, (dialog, id) -> { });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void displayError(Context context, String error){
        Toast.makeText(context,error,Toast.LENGTH_LONG).show();
    }

    public static void showSnackBar(BaseActivity baseActivity, String text) {
        Snackbar.make(baseActivity.findViewById(R.id.fragment_position), text, Snackbar.LENGTH_LONG).show();
    }

    // -------------------------------------------------------------------------------------------------------
    // -------------------------------------- LOADING IMAGE FROM DEVICE --------------------------------------
    // -------------------------------------------------------------------------------------------------------

    public static void getMainImage(BaseActivity baseActivity) {

        Context context = baseActivity.getApplicationContext();

        String[] galleryPermissions = {READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(context, galleryPermissions)) {

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            baseActivity.startActivityForResult(i, RESULT_LOAD_MAIN_IMAGE_);

        } else {
            EasyPermissions.requestPermissions(baseActivity, "Access for storage",
                    PERMISSIONS_REQUEST_ACCESS_MAIN_IMAGE_GALLERY, galleryPermissions);
        }
    }

    public static void getExtraImage(BaseActivity baseActivity, int viewHolderPosition) {

        baseActivity.setViewHolderPosition(viewHolderPosition);
        String[] galleryPermissions = {READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(baseActivity.getApplicationContext(), galleryPermissions)) {

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            baseActivity.startActivityForResult(i, RESULT_LOAD_IMAGE_VIEWHOLDER);

        } else {
            EasyPermissions.requestPermissions(baseActivity, "Access for storage",
                    PERMISSIONS_REQUEST_ACCESS_EXTRA_IMAGE_GALLERY, galleryPermissions);
        }
    }

    public static void setImage(String imagePath, ImageView imageView, BaseActivity baseActivity) {

        Context context = baseActivity.getApplicationContext();

        try {
            Bitmap bitmap;

            if(imagePath==null){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder));
            } else {

                File f = new File(imagePath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                String[] galleryPermissions = {READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                if (EasyPermissions.hasPermissions(context, galleryPermissions)) {

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),options);

                    if(bitmap!=null)
                        imageView.setImageBitmap(bitmap);

                } else {
                    EasyPermissions.requestPermissions(baseActivity, "Access for storage",
                            PERMISSIONS_REQUEST_ACCESS_IMAGE_GALLERY, galleryPermissions);
                    baseActivity.setImagePath(imagePath);
                    baseActivity.setImageView(imageView);
                }
            }
        } catch (Exception e) {
            Toast.makeText(context,context.getResources().getString(R.string.error_image_load),Toast.LENGTH_LONG).show();
        }
    }

    public static void handleOnActivityResult(int requestCode, int resultCode, Intent data, BaseActivity baseActivity){

        // --------------------------- For extra images
        if (requestCode == RESULT_LOAD_IMAGE_VIEWHOLDER && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();

            if(selectedImage!=null){ // if URI is not null, recover real path from URI and send it to editfragment
                String imagePath = Utils.getRealPathFromURI(baseActivity,selectedImage);
                baseActivity.getEditFragment().setExtraImage(imagePath, baseActivity.getViewHolderPosition());
            } else
                baseActivity.getEditFragment().setExtraImage(null, baseActivity.getViewHolderPosition());
            // --------------------------- For main image
        } else if (requestCode == RESULT_LOAD_MAIN_IMAGE_ && null != data){
            Uri selectedImage = data.getData();

            if(selectedImage!=null){ // if URI is not null, recover real path from URI and send it to editfragment
                String imagePath = Utils.getRealPathFromURI(baseActivity,selectedImage);
                baseActivity.getEditFragment().setMainImage(imagePath);
            } else
                baseActivity.getEditFragment().setMainImage(null);
        } else
            displayError(baseActivity.getApplicationContext(), baseActivity.getApplicationContext().getResources().getString(R.string.error_image_recovering));

    }

    public static void handleOnRequestPermissionResult(int requestCode, @NonNull int[] grantResults, BaseActivity baseActivity){

        Context context = baseActivity.getApplicationContext();

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_IMAGE_GALLERY: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setImage(baseActivity.getImagePath(), baseActivity.getImageView(), baseActivity);
                } else {
                    displayError(context,context.getResources().getString(R.string.give_permission));
                }
                break;
            }
            case PERMISSIONS_REQUEST_ACCESS_MAIN_IMAGE_GALLERY: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMainImage(baseActivity);
                } else {
                    displayError(context,context.getResources().getString(R.string.give_permission));
                }
                break;
            }
            case PERMISSIONS_REQUEST_ACCESS_EXTRA_IMAGE_GALLERY: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMainImage(baseActivity);
                } else {
                    displayError(context,context.getResources().getString(R.string.give_permission));
                }
                break;
            }
        }
    }

    public static void setModeDevice(BaseActivity baseActivity){
        if(baseActivity.findViewById(R.id.fragment_list) != null) { // MODE TABLET
            baseActivity.setModeDevice(MODE_TABLET);
        } else { // MODE PHONE
            baseActivity.setModeDevice(MODE_PHONE);
        }
    }
}
