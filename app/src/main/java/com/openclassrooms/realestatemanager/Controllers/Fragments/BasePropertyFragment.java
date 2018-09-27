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
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.Provider.ImageContentProvider;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.R;
import java.util.List;


public class BasePropertyFragment extends Fragment {

    protected static final int PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE_MAIN_IMAGE = 77;
    protected static final int PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE_EXTRA_IMAGE = 88;
    protected static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    protected static final String MODE_DISPLAY = "mode_display";
    protected static final String MODE_SEARCH = "mode_search";
    protected final static String MODE_TABLET = "mode_tablet";
    protected Property property;
    protected Context context;
    protected List<ImageProperty> listImages;
    protected String buttonReturnText;
    protected String modeSelected;
    protected String typeEdit;
    protected int idProperty;
    protected BaseActivityListener baseActivityListener;
    protected String modeDevice;
    protected Boolean permissionAccessStorage;


    public BasePropertyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if(baseActivityListener!=null)
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

    public int getIdProperty() {
        return idProperty;
    }
}
