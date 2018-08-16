package com.openclassrooms.realestatemanager.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;

/**
Class for :
 - loading image from the phone device into an imageView (when clicking on button "load image")
 - insert a new imageProperty in database (when clicking on button "OK")
 - modify an existing imageProperty in database (when clicking on button "OK")
 - delete an imageProperty in database (when clicking on button "delete")
 */

public class ImageLoading {

    private static int RESULT_LOAD_IMAGE = 1;
    private PropertyDatabase database;
    private ImageView imageView;
    private String description;
    private byte[] currentImageByte;
    private byte[] initialImageByte;

    public ImageLoading(MainActivity mainActivity, PropertyDatabase database){
        this.database=database;
        /*imageView = mainActivity.getImageView();
        Bitmap bitmap = null;
        
        if(imageView.getDrawable()!=null)
            bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        if(bitmap!=null)
            initialImageByte = Utils.getBitmapAsByteArray(bitmap);*/
    }

    public void setCurrentImageByte(byte[] currentImageByte) {
        this.currentImageByte = currentImageByte;
    }

    // -------------------------------------------------------------------------------------------------------
    // ------------------------------------ INSERT NEW IMAGE IN DATABASE -------------------------------------
    // -------------------------------------------------------------------------------------------------------
/*
    public void inserNewImageDataBase(int idProperty, String description){
        // create a new imageProperty
        ImageProperty imageProperty = new ImageProperty(currentImageByte,description,idProperty);
        // insert the imageProperty in database
        database.imageDao().insertImage(imageProperty);
    }

    // -------------------------------------------------------------------------------------------------------
    // ---------------------------------- UPDATE EXISTING IMAGE IN DATABASE ----------------------------------
    // -------------------------------------------------------------------------------------------------------

    public void updateImageDataBase(int idImage, int idProperty, String description){
        // create a new imageProperty
        ImageProperty imageProperty = new ImageProperty(idImage, currentImageByte, description, idProperty);
        // update the imageProperty in database
        database.imageDao().updateImage(imageProperty);
    }*/

    // -------------------------------------------------------------------------------------------------------
    // --------------------------------------- DELETE IMAGE IN DATABASE --------------------------------------
    // -------------------------------------------------------------------------------------------------------

    public void deleteImageDataBase(int idImage){
        // delete the imageProperty in database
        database.imageDao().deleteImage(idImage);
        // Remove image from imageView
        imageView.setImageBitmap(null);
    }

    // -------------------------------------------------------------------------------------------------------
    // -------------------------------------------- CANCEL REQUEST -------------------------------------------
    // -------------------------------------------------------------------------------------------------------

    public void reinitializeImageProperty(){

        if(initialImageByte!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(initialImageByte, 0, initialImageByte.length);
            imageView.setImageBitmap(bitmap);
        } else
            imageView.setImageBitmap(null);
    }

}
