package com.openclassrooms.realestatemanager.Utils;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import java.util.ArrayList;
import java.util.List;

public class CheckAndSaveEdit {

    private EditFragment editFragment;
    private PropertyDatabase mPropertyDatabase;
    private List<ImageProperty> newlistImages;
    private List<ImageProperty> oldlistImages;
    private static final String MODE_UPDATE = "UPDATE";
    private int idUpdateProperty;
    private long idInsertProperty;

    public CheckAndSaveEdit(EditFragment editFragment) {
        this.editFragment = editFragment;
        mPropertyDatabase = editFragment.getDatabase();
        newlistImages = editFragment.getListImages();
        oldlistImages = new ArrayList<>();
        SaveInfoEditFragment();
    }

    private void SaveInfoEditFragment(){

        byte[] mapStatic;
        if(editFragment.getStaticMap()!=null)
            mapStatic = Utils.getBitmapAsByteArray(editFragment.getStaticMap());
        else
            mapStatic = null;

        Double lat;
        Double lng;

        if(editFragment.getLatLngAddress()!=null){
            lat=editFragment.getLatLngAddress().latitude;
            lng=editFragment.getLatLngAddress().longitude;
        } else {
            lat=0d;
            lng=0d;
        }

        String imageString;
        if(editFragment.getMainImageURI()!=null)
            imageString = editFragment.getMainImageURI().toString();
        else
            imageString=null;

        // Create the new property to save or update
        Property propertyToSave = new Property(editFragment.getPropertyInit().getId(), // id property
                editFragment.listProperties.getSelectedItem().toString(), // type property
                Double.parseDouble(editFragment.priceEdit.getText().toString()), // price property
                Double.parseDouble(editFragment.surfaceEdit.getText().toString()), // surface property
                Integer.parseInt(editFragment.nbRooms.getText().toString()),// nb rooms
                editFragment.descriptionEdit.getText().toString(), // description property
                editFragment.addressEdit.getText().toString(), // address property
                editFragment.getInterestPoints(), // list interest points
                editFragment.switchSold.isChecked(), // property sold or not ?
                editFragment.datePublish.getText().toString(), // date start
                editFragment.dateSold.getText().toString(), // date sold
                lat, lng, // latitude & longitude property
                editFragment.estateAgentEdit.getText().toString(), // estate agent name
                mapStatic, imageString);

        if(editFragment.getMode().equals(MODE_UPDATE)){
            idUpdateProperty = mPropertyDatabase.propertyDao().updateProperty(propertyToSave);

            // Recover images from database
       /*       mPropertyDatabase.imageDao().getAllImagesFromProperty(idUpdateProperty).observe(this, new Observer<List<ImageProperty>>() {
                @Override
                public void onChanged(@Nullable List<ImageProperty> imageProperties) {

                }
            });*/


        } else {
            idInsertProperty = mPropertyDatabase.propertyDao().insertProperty(propertyToSave);

            // Recover images from database
            //mPropertyDatabase.imageDao().getAllImagesFromProperty((int)idInsertProperty).observe(ListImagesObserver);
        }
    }

    private void updateListImagesPropertyInDatabase(){

        int counter = 0;

        if(newlistImages!=null){

            // for each image in the new list, check if it's present in oldlist (yes => update the image, no => insert the image)
            for(ImageProperty image : newlistImages){

                if(counter < newlistImages.size()-1) { // if it's NOT the last item of the list

                    // set the idProperty
                    if(editFragment.getMode().equals(MODE_UPDATE))
                        image.setIdProperty(idUpdateProperty);
                    else
                        image.setIdProperty((int)idInsertProperty);

                    // if old list is NOT null
                    if (oldlistImages != null) {
                        if (isInTheList(image, oldlistImages)) {
                            mPropertyDatabase.imageDao().updateImage(image);
                        } else {
                            mPropertyDatabase.imageDao().insertImage(image);
                        }
                    } else {
                        mPropertyDatabase.imageDao().insertImage(image);
                    }
                }
                counter++;
            }
        }

        // for each image in the old list, check if it's present in new list (no => delete the image)
        if(oldlistImages!=null){
            for(ImageProperty image : oldlistImages){
                if(!isInTheList(image, newlistImages)){
                    mPropertyDatabase.imageDao().deleteImage(image.getId());
                }
            }
        }
    }

    private Boolean isInTheList(ImageProperty image, List<ImageProperty> list){

        if(image != null){
            for(ImageProperty img : list){
                if(img!=null){
                    if(img.getId()==image.getId()){
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    final Observer<List<ImageProperty>> ListImagesObserver = new Observer<List<ImageProperty>>() {
        @Override
        public void onChanged(@Nullable final List<ImageProperty> newName) {
            if (newName != null) {
                if(newName.size() != 0){
                    oldlistImages.addAll(newName);
                }
            }
            updateListImagesPropertyInDatabase();
        }
    };
}
