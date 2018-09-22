package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.os.Bundle;
import com.openclassrooms.realestatemanager.Controllers.Fragments.DisplayFragment;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.R;


public class SaveAndRestoreDataDisplayFragment {

    private static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    private static final String MODE_DISPLAY = "mode_display";
    private static final String MODE_SEARCH = "mode_search";
    private static final String POSITION_IMAGE_SELECTED = "position_image_selected";
    private static final String BUNDLE_DEVICE = "bundle_device";
    private static final String BUNDLE_MODE_SELECTED = "bundle_mode_selected";
    private static final String LAST_PROPERTY_SELECTED = "last_property_selected";

    // ----------------------------------- CREATE BUNDLE

    public static Bundle createBundleForDisplayFragment(int idProperty, String modeDevice, String modeSelected){

        Bundle bundle = new Bundle();

        bundle.putInt(LAST_PROPERTY_SELECTED, idProperty);
        bundle.putString(BUNDLE_DEVICE, modeDevice);
        bundle.putString(BUNDLE_MODE_SELECTED, modeSelected);

        return bundle;
    }

    // ----------------------------------- SAVE DATA

    public static void saveDatas(Bundle outState, int idProperty, int positionImageSelected, String modeSelected, String modeDevice){

        outState.putInt(POSITION_IMAGE_SELECTED, positionImageSelected);
        outState.putString(BUNDLE_MODE_SELECTED, modeSelected);
        outState.putString(BUNDLE_DEVICE, modeDevice);
        outState.putInt(LAST_PROPERTY_SELECTED, idProperty);
    }

    // ----------------------------------- RESTORE DATA

    public static void recoverDatas(Bundle arguments, Bundle savedInstanceState, DisplayFragment fragment, Context context){

        Bundle bundle;

        if(savedInstanceState!=null)
            bundle = savedInstanceState;
        else
            bundle = arguments;

        // Recover position image selected
        fragment.setPositionImageSelected(bundle.getInt(POSITION_IMAGE_SELECTED,0));

        // Recover id property
        int idProperty=bundle.getInt(LAST_PROPERTY_SELECTED,-1);
        fragment.setIdProperty(idProperty);

        // restore property and images property
        fragment.recoverProperty(idProperty);

        // Add the main image in listImages
        if (fragment.getProperty().getMainImagePath() != null)
            fragment.getListImages().add(new ImageProperty(0, fragment.getProperty().getMainImagePath(), null, idProperty)); // add main image

        // Recover extra images
        fragment.recoverImagesProperty(idProperty);

        // Recover modeDevice and modeSelected
        String modeDevice = bundle.getString(BUNDLE_DEVICE);
        fragment.setModeDevice(modeDevice);

        String modeSelected = bundle.getString(BUNDLE_MODE_SELECTED, MODE_DISPLAY);
        fragment.setModeSelected(modeSelected);

        // Configure button return
        switch (modeSelected) {
            case MODE_DISPLAY_MAPS:
                fragment.setButtonReturnText(context.getResources().getString(R.string.return_to_the_map));
                break;
            case MODE_DISPLAY:
                fragment.setButtonReturnText(context.getResources().getString(R.string.return_to_the_list));
                break;
            case MODE_SEARCH:
                fragment.setButtonReturnText(context.getResources().getString(R.string.return_to_search_criteria));
                break;
        }
    }
}
