package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.app.FragmentTransaction;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.openclassrooms.realestatemanager.Controllers.Fragments.DisplayFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Utils.ImageLoading;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.LiveDataTestUtil;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.Views.ImagesEditViewHolder;

import java.io.FileNotFoundException;
import java.util.List;


public class MainActivity extends AppCompatActivity  {

    private ImageLoading imageLoading;
    private Button button;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_IMAGE_VIEWHOLDER = 2;
    private ImageView imageView;
    private PropertyDatabase database;
    private static int PROPERTY_ID = 3;
    private ImagesEditViewHolder imagesViewHolder;
    private static final String PROPERTY_JSON = "property_json";
    private EditFragment editFragment;
    private DisplayFragment displayFragment;
    private List<Property> listProperties;

    private static Property PROPERTY_DEMO = new Property(PROPERTY_ID, "Appartment", 125000d,30.25,1,
            "description","address","School, Subway",false,"01/06/2018",0d,0d,"Eric",null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.database = PropertyDatabase.getInstance(getApplicationContext());

        try {
            listProperties = LiveDataTestUtil.getValue(this.database.propertyDao().getAllProperties());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        configureAndShowEditFragment(PROPERTY_DEMO);
    }

    public void changeToDisplayMode(int propertyId){

        // remove the editFragment
        this.getFragmentManager().beginTransaction().remove(editFragment).commit();

        // show displayFragment
        configureAndShowDisplayFragment(getPropertyFromId(propertyId));
    }

    public void changeToEditMode(int propertyId){

        // remove the editFragment
        this.getFragmentManager().beginTransaction().remove(displayFragment).commit();

        // show displayFragment
        configureAndShowEditFragment(getPropertyFromId(propertyId));
    }

    private Property getPropertyFromId(int propertyId){

        for(Property property : listProperties){
            if(property!=null){
                if(property.getId() == propertyId){
                    return property;
                }
            }
        }
        return null;
    }

    public void configureAndShowDisplayFragment(Property property){
        if(property!=null){
            displayFragment = new DisplayFragment();

            // create a bundle
            Bundle bundle = new Bundle();
            Gson gson = new Gson();

            // Add the property to the bundle in json format
            String property_json = gson.toJson(property);
            bundle.putString(PROPERTY_JSON, property_json);

            // configure and show the editFragment
            displayFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainactivity_xml, displayFragment);
            fragmentTransaction.commit();
        }
    }

    public void configureAndShowEditFragment(Property property){

        editFragment = new EditFragment();

        // create a bundle
        Bundle bundle = new Bundle();
        Gson gson = new Gson();

        // Add the property to the bundle in json format
        String property_json = gson.toJson(property);
        bundle.putString(PROPERTY_JSON, property_json);

        // configure and show the editFragment
        editFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainactivity_xml, editFragment);
        fragmentTransaction.commit();
    }

    // -------------------------------------------------------------------------------------------------------
    // -------------------------------------- LOADING IMAGE FROM DEVICE --------------------------------------
    // -------------------------------------------------------------------------------------------------------

    public void getImageFromGallery(int viewHolderPosition) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.putExtra("VIEWHOLDERPOSITION",viewHolderPosition);
        startActivityForResult(i, RESULT_LOAD_IMAGE_VIEWHOLDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            Bitmap bitmap;

            try {
                if (selectedImage != null) {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                    imageView.setImageBitmap(bitmap);
                    imageLoading.setCurrentImageByte(Utils.getBitmapAsByteArray(bitmap));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RESULT_LOAD_IMAGE_VIEWHOLDER && resultCode == RESULT_OK && null != data){

            Uri selectedImage = data.getData();

            editFragment.setImage(selectedImage, data.getIntExtra("VIEWHOLDERPOSITION",0));
        }
    }

    // -------------------------------------------------------------------------------------------------------
    // ------------------------------------------ GETTER AND SETTER ------------------------------------------
    // -------------------------------------------------------------------------------------------------------

    public Button getButton() {
        return button;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
