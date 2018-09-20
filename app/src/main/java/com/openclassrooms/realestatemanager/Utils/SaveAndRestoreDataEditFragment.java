package com.openclassrooms.realestatemanager.Utils;

import android.os.Bundle;
import android.os.Parcelable;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import java.util.ArrayList;
import java.util.List;


public class SaveAndRestoreDataEditFragment {

    private final static String BUNDLE_DEVICE = "bundle_device";
    private static final String BUNDLE_TYPE_EDIT = "type_edit";
    private static final String BUNDLE_PROPERTY = "bundle_property";
    private static final String BUNDLE_LIST_IMAGES_PROPERTY = "bundle_list_images_property";
    private static final String MODE_SELECTED = "mode_selected";
    private static final String MODE_DISPLAY = "mode_display";
    private static final String LAST_PROPERTY_SELECTED = "last_property_selected";
    private static final String MODE_UPDATE = "UPDATE";
    private static final String RECYCLERVIEW_STATE_KEY = "recycler_view_state";

    // ----------------------------------- SAVE DATA

    public static void saveDatas(Bundle outState, Property property, List<ImageProperty> listImages, String modeSelected, String modeDevice, String typeEdit, int idProperty, ConfigureEditFragment config){

        outState.putString(BUNDLE_PROPERTY, ConverterJSON.convertPropertyToJson(property));
        outState.putString(BUNDLE_LIST_IMAGES_PROPERTY, ConverterJSON.convertListImagesPropertyToJson(listImages));
        outState.putString(BUNDLE_TYPE_EDIT, typeEdit);
        outState.putString(MODE_SELECTED, modeSelected);
        outState.putString(BUNDLE_DEVICE, modeDevice);
        outState.putInt(LAST_PROPERTY_SELECTED, idProperty);

        if(config!=null) {
            Parcelable recyclerViewState = config.getLayoutManager().onSaveInstanceState();
            outState.putParcelable(RECYCLERVIEW_STATE_KEY, recyclerViewState);
        }
    }

    // ----------------------------------- RESTORE DATA

    public static void recoverDatas(Bundle arguments, Bundle savedInstanceState, EditFragment fragment, ConfigureEditFragment config){

        Bundle bundle = new Bundle();
        Property property = new Property(-1,null,0d,0d,0,null,null,
                null,false,null,null,0d,0d,null,null,null);
        List<ImageProperty> listImages;

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

            for(ImageProperty img : listImages){
                if(img.getInEdition()==null)
                    img.setInEdition(false);
            }

            //listImages.add(new ImageProperty()); // Add item for "add a photo"
            fragment.setListImages(listImages);

            // Restore recyclerView
            if(config!=null) {
                Parcelable recyclerViewState = bundle.getParcelable(RECYCLERVIEW_STATE_KEY);
                config.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            }

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
                } else {
                    // create empty property and empty list images
                    fragment.setProperty(property);
                }
            }
        }

        String modeDevice = bundle.getString(BUNDLE_DEVICE);
        fragment.setModeDevice(modeDevice);

        String modeSelected = bundle.getString(MODE_SELECTED,MODE_DISPLAY);
        fragment.setModeSelected(modeSelected);
    }
}
