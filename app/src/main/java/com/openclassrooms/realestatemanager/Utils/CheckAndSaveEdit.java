package com.openclassrooms.realestatemanager.Utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.Provider.ImageContentProvider;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import java.util.ArrayList;
import java.util.List;


public class CheckAndSaveEdit {

    private EditFragment editFragment;
    private List<ImageProperty> newlistImages;
    private List<ImageProperty> oldlistImages;
    private static final String MODE_UPDATE = "UPDATE";
    private Context context;
    private Property propertyToSave;
    private int idProp;
    private PropertyContentProvider propertyContentProvider;
    private ImageContentProvider imageContentProvider;

    public CheckAndSaveEdit(EditFragment editFragment, Context context) {
        this.editFragment = editFragment;
        this.context=context;
        idProp = editFragment.getProperty().getId();
        newlistImages = editFragment.getListImages();
        oldlistImages = new ArrayList<>();
        SaveInfoEditFragment();
    }

    private void SaveInfoEditFragment(){

        // Create the property to save
        createPropertyToSave();

        // Create contentProviders
        createContentProviders();

        if(editFragment.getMode().equals(MODE_UPDATE)){

            propertyContentProvider.update(ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, propertyToSave.getId()),
                    Property.createContentValuesFromPropertyUpdate(propertyToSave),null,null);

            // Recover images from database
            recoverImagesProperty(idProp);

            // Update the list of images
            updateListImagesPropertyInDatabase(idProp);

        } else {
            // insert new property
            final Uri uri = propertyContentProvider.insert(PropertyContentProvider.URI_ITEM,
                    Property.createContentValuesFromPropertyInsert(propertyToSave));

            // insert new images
            insertListImagesPropertyInDatabase(uri);
        }
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------- 1. CREATE PROPERTY TO SAVE -------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void createPropertyToSave(){

        PropertyConstructor propertyConstructor = new PropertyConstructor();
        propertyToSave = propertyConstructor.createPropertyFromViews(editFragment, editFragment.getView());
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------- 2. CREATE CONTENT PROVIDERS ------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void createContentProviders(){

        propertyContentProvider = new PropertyContentProvider();
        propertyContentProvider.setUtils(context,false);

        imageContentProvider = new ImageContentProvider();
        imageContentProvider.setUtils(context);
    }

    // ------------------------------------------------------------------------------------------------------------
    // ---------------------------- 3. RECOVER THE OLD LIST OF IMAGES ---------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void recoverImagesProperty(int idProp){

        ImageContentProvider imageContentProvider = new ImageContentProvider();
        imageContentProvider.setUtils(context);

        final Cursor cursor = imageContentProvider.query(ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, idProp), null, null, null, null);

        if (cursor != null){
            if(cursor.getCount() >0){
                while (cursor.moveToNext()) {
                    oldlistImages.add(ImageProperty.getImagePropertyFromCursor(cursor));
                }
            }
            cursor.close();
        }
    }

    // ------------------------------------------------------------------------------------------------------------
    // ---------------------------- 4. INSERT _ UPDATE _ DELETE IMAGES --------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void insertListImagesPropertyInDatabase(Uri uri){

        int counter = 0;

        if(newlistImages!=null){

            for(ImageProperty image : newlistImages) {

                if (counter < newlistImages.size() - 1) { // if it's NOT the last item of the list

                    int idProperty = (int) ContentUris.parseId(uri);

                    // Add the idProperty (foreign key) to the image
                    image.setIdProperty(idProperty);

                    // Save image in Database
                    imageContentProvider.insert(ContentUris.withAppendedId(ImageContentProvider.URI_ITEM, idProperty)
                            ,ImageProperty.createContentValuesFromImagePropertyInsert(image));
                }
            }
        }
    }

    private void updateListImagesPropertyInDatabase(int idProperty){

        int counter = 0;

        newlistImages.remove(newlistImages.size()-1); // delete the last image

        if(newlistImages!=null){

            // for each image in the new list, check if it's present in oldlist (yes => update the image, no => insert the image)
            for(ImageProperty image : newlistImages){

                if(counter <= newlistImages.size()-1) { // if it's NOT the last item of the list

                    // set the idProperty
                    image.setIdProperty(idProperty);

                    // if old list is NOT null
                    if (oldlistImages != null) {
                        if (Utils.isInTheList(image, oldlistImages)) {
                            imageContentProvider.update(ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, idProperty),
                                    ImageProperty.createContentValuesFromImagePropertyUpdate(image),null,null);
                        } else {
                            imageContentProvider.insert(ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, idProperty),
                                    ImageProperty.createContentValuesFromImagePropertyInsert(image));
                        }
                    } else {
                        imageContentProvider.insert(ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, idProperty),
                                ImageProperty.createContentValuesFromImagePropertyInsert(image));
                    }
                }
                counter++;
            }
        }

        // for each image in the old list, check if it's present in new list (no => delete the image)
        if(oldlistImages!=null){
            for(ImageProperty image : oldlistImages){
                if(!Utils.isInTheList(image, newlistImages)){
                    imageContentProvider.delete(ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, image.getId()),null,null);
                }
            }
        }
    }
}
