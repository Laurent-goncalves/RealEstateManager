package com.openclassrooms.realestatemanager.Controllers.Fragments;


import android.app.Fragment;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.MapsActivity;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.Provider.ImageContentProvider;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.R;
import java.util.List;


public class BasePropertyFragment extends Fragment {

    protected Property property;
    protected Context context;
    protected List<ImageProperty> listImages;
    protected BaseActivity baseActivity;
    protected MainActivity mainActivity;
    protected MapsActivity mapsActivity;
    protected String mode;
    protected static final String LAST_PROPERTY_SELECTED = "last_property_selected";
    protected static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    protected static final String MODE_DISPLAY = "mode_display";
    protected static final String MODE_SELECTED = "mode_selected";


    public BasePropertyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_property, container, false);
    }

    protected void recoverProperty(int idProp){

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

    protected void recoverImagesProperty(int idProp){

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
    }

    public List<ImageProperty> getListImages() {
        return listImages;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }
}
