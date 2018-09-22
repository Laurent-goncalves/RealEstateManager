package com.openclassrooms.realestatemanager.Controllers.Fragments;


import android.app.Fragment;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openclassrooms.realestatemanager.Controllers.Activities.MapsActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.SearchActivity;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.Provider.ImageContentProvider;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.R;
import java.util.List;
import java.util.Objects;


public class BasePropertyFragment extends Fragment {

    protected Property property;
    protected Context context;
    protected List<ImageProperty> listImages;
    protected SearchActivity searchActivity;
    protected MapsActivity mapsActivity;
    protected String buttonReturnText;
    protected String modeSelected;
    protected String typeEdit;
    protected int idProperty;
    protected BaseActivityListener baseActivityListener;
    protected static final int PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE_MAIN_IMAGE = 77;
    protected static final int PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE_EXTRA_IMAGE = 88;
    protected final static String BUNDLE_DEVICE = "bundle_device";
    protected static final String BUNDLE_MODE_SELECTED = "bundle_mode_selected";
    protected static final String LAST_PROPERTY_SELECTED = "last_property_selected";
    protected static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    protected static final String MODE_DISPLAY = "mode_display";
    protected static final String MODE_SEARCH = "mode_search";
    protected String modeDevice;
    protected final static String MODE_TABLET = "mode_tablet";
    protected final static String MODE_PHONE = "mode_phone";
    protected Boolean permissionAccessStorage;

    public BasePropertyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_property, container, false);
    }

    // -----------------------------------------------------------------------------------------
    // ----------------------------------- RECOVER DATAS ---------------------------------------
    // -----------------------------------------------------------------------------------------

    protected void recoverModeSelected(){

        if(getArguments()!=null) {

            // if the mode of display is for mapsActivity, remove button return and icons in toolbar
            if (Objects.equals(getArguments().getString(BUNDLE_MODE_SELECTED), MODE_DISPLAY_MAPS)) {
                mapsActivity = (MapsActivity) getActivity();
                modeSelected = MODE_DISPLAY_MAPS;
                buttonReturnText = context.getResources().getString(R.string.return_to_the_map);
            } else if (Objects.equals(getArguments().getString(BUNDLE_MODE_SELECTED), MODE_DISPLAY)) {
                modeSelected = MODE_DISPLAY;
                buttonReturnText = context.getResources().getString(R.string.return_to_the_list);
            } else if (Objects.equals(getArguments().getString(BUNDLE_MODE_SELECTED), MODE_SEARCH)) {
                searchActivity = (SearchActivity) getActivity();
                modeSelected = MODE_SEARCH;
                buttonReturnText = context.getResources().getString(R.string.return_to_search_criteria);
            }
        }
    }

    protected void recoverDeviceMode(){

        if(getArguments()!=null) {
            if (getArguments().getString(BUNDLE_DEVICE)!=null){
                if (Objects.equals(getArguments().getString(BUNDLE_DEVICE), MODE_TABLET)) {
                    modeDevice = MODE_TABLET;
                } else {
                    modeDevice = MODE_PHONE;
                }
            } else {
                modeDevice = MODE_PHONE;
            }
        } else {
            modeDevice = MODE_PHONE;
        }
    }

    public void recoverProperty(int idProp){

        if(idProp!=-1){

            PropertyContentProvider propertyContentProvider = new PropertyContentProvider();
            propertyContentProvider.setUtils(context,false);

            final Cursor cursor = propertyContentProvider.query(ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, idProp), null, null, null, null);

            if (cursor != null){
                if(cursor.getCount() >0){
                    while (cursor.moveToNext()) {
                        property=Property.getPropertyFromCursor(cursor);
                    }
                }
                cursor.close();
            }
        }
    }

    public void recoverImagesProperty(int idProp){

        if(idProp!=-1){

            ImageContentProvider imageContentProvider = new ImageContentProvider();
            imageContentProvider.setUtils(context);

            final Cursor cursor = imageContentProvider.query(ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, idProp), null, null, null, null);

            if (cursor != null){
                if(cursor.getCount() >0){
                    while (cursor.moveToNext()) {
                        listImages.add(ImageProperty.getImagePropertyFromCursor(cursor));
                    }
                }
                cursor.close();
            }
        }

        // if an image has the variable "inEdition" as null, set it to "false"
        if(listImages!=null){
            if(listImages.size()>0){
                for(ImageProperty img : listImages){
                    if(img.getInEdition()==null)
                        img.setInEdition(false);
                }
            }
        }
    }

    // -----------------------------------------------------------------------------------------
    // -------------------------------- GETTERS and SETTERS ------------------------------------
    // -----------------------------------------------------------------------------------------

    public List<ImageProperty> getListImages() {
        return listImages;
    }

    public void setModeSelected(String modeSelected) {
        this.modeSelected = modeSelected;
    }

    public String getModeSelected() {
        return modeSelected;
    }

    public String getModeDevice() {
        return modeDevice;
    }

    public void setModeDevice(String modeDevice) {
        this.modeDevice = modeDevice;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public void setListImages(List<ImageProperty> listImages) {
        this.listImages = listImages;
    }

    public void setIdProperty(int idProperty) {
        this.idProperty = idProperty;
        baseActivityListener.setCurrentPositionDisplayed(idProperty);
    }

    public Property getProperty() {
        return property;
    }

    public void setButtonReturnText(String buttonReturnText) {
        this.buttonReturnText = buttonReturnText;
    }

    public void setTypeEdit(String typeEdit) {
        this.typeEdit = typeEdit;
    }
}
