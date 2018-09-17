package com.openclassrooms.realestatemanager.Utils;

import android.os.Bundle;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import java.util.ArrayList;
import java.util.List;


public class SaveAndRestoreDataEditFragment {

    private static final String BUNDLE_TYPE_EDIT = "type_edit";
    private static final String BUNDLE_PROPERTY = "bundle_property";
    private static final String BUNDLE_LIST_IMAGES_PROPERTY = "bundle_list_images_property";
    private static final String MODE_SELECTED = "mode_selected";
    private static final String MODE_DISPLAY = "mode_display";
    private static final String LAST_PROPERTY_SELECTED = "last_property_selected";
    private static final String MODE_UPDATE = "UPDATE";

    // ----------------------------------- SAVE DATA

    public static void saveDatas(Bundle outState, Property property, List<ImageProperty> listImages, String modeSelected, String typeEdit, int idProperty){

        listImages.remove(listImages.size()-1);
        outState.putString(BUNDLE_PROPERTY, ConverterJSON.convertPropertyToJson(property));
        outState.putString(BUNDLE_LIST_IMAGES_PROPERTY, ConverterJSON.convertListImagesPropertyToJson(listImages));
        outState.putString(BUNDLE_TYPE_EDIT, typeEdit);
        outState.putString(MODE_SELECTED, modeSelected);
        outState.putInt(LAST_PROPERTY_SELECTED, idProperty);
    }

    // ----------------------------------- RESTORE DATA

    public static void recoverDatas(Bundle arguments, Bundle savedInstanceState, EditFragment fragment){

        Bundle bundle = new Bundle();
        Property property = new Property(-1,null,0d,0d,0,null,null,
                null,false,null,null,0d,0d,null,null,null);
        List<ImageProperty> listImages = new ArrayList<>();

        if(savedInstanceState!=null){
            bundle = savedInstanceState;

            // Recover type edit
            String typeEdit = bundle.getString(BUNDLE_TYPE_EDIT, MODE_UPDATE);
            fragment.setTypeEdit(typeEdit);

            // restore property
            property = ConverterJSON.convertJsonToProperty(bundle.getString(BUNDLE_PROPERTY));
            fragment.setProperty(property);

            // restore Images property
            listImages = ConverterJSON.convertJsonToListImagesProperty(bundle.getString(BUNDLE_LIST_IMAGES_PROPERTY));

            if(listImages==null)
                listImages = new ArrayList<>();

            listImages.add(new ImageProperty()); // Add item for "add a photo"
            fragment.setListImages(listImages);

        } else if (arguments!=null){
            bundle = arguments;

            // Recover type edit
            String typeEdit = bundle.getString(BUNDLE_TYPE_EDIT, MODE_UPDATE);
            fragment.setTypeEdit(typeEdit);

            // Recover id property
            int idProperty=bundle.getInt(LAST_PROPERTY_SELECTED,-1);
            fragment.setIdProperty(idProperty);

            // restore property
            if(typeEdit!=null){

                if(typeEdit.equals(MODE_UPDATE)){ // mode update property
                    // Recover the property datas
                    fragment.recoverProperty(idProperty);
                    fragment.recoverImagesProperty(idProperty);
                    fragment.getListImages().add(new ImageProperty()); // Add item for "add a photo"
                } else {
                    // create empty property and empty list images
                    fragment.setProperty(property);
                    listImages.add(new ImageProperty()); // Add item for "add a photo"
                    fragment.setListImages(listImages);
                }
            }
        }

        String modeSelected = bundle.getString(MODE_SELECTED,MODE_DISPLAY);
        fragment.setModeSelected(modeSelected);
    }

}
