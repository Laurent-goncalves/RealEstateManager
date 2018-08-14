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
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.Utils.ImageLoading;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.LiveDataTestUtil;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.Views.ImagesEditViewHolder;

import java.io.FileNotFoundException;
import java.util.ArrayList;
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
    private static final String MODE_SELECTED = "mode_selected";
    private static final String LAST_PROPERTY_SELECTED = "last_property_selected";
    private EditFragment editFragment;
    private DisplayFragment displayFragment;
    private List<Property> listProperties;
    private ToolbarManager toolbarManager;

    private Property PROPERTY_DEMO = new Property(PROPERTY_ID, "Appartment", 125000d,30.25,1,
            "Nice appartment closed to subway station. \nBig kitchen, 2 bathrooms, 2 WC. \nNice garden with a view on the lake. \nMany shops nearby.",
            "12 Kennedy street","School, Subway",true,"01/06/2018",0d,0d,"Eric",null);

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

        listProperties = new ArrayList<>(); // TODO: to be removed later
        listProperties.add(PROPERTY_DEMO); // TODO: to be removed later

        // Configure toolbar
        toolbarManager = new ToolbarManager(this);
        toolbarManager.configure_toolbar();

        // Show editFragment
        configureAndShowDisplayFragment(PROPERTY_DEMO);
    }

    public void changeToDisplayMode(int propertyId){

        // remove the editFragment
        if(editFragment!=null)
            this.getFragmentManager().beginTransaction().remove(editFragment).commit();

        // add necessary icons
        toolbarManager.addIconsToolbar();

        // show displayFragment
        configureAndShowDisplayFragment(getPropertyFromId(propertyId));
    }

    public void changeToEditMode(int propertyId){

        // remove the editFragment
        if(displayFragment!=null)
            this.getFragmentManager().beginTransaction().remove(displayFragment).commit();

        // remove unecessary icons
        toolbarManager.removeIconsToolbar();

        // show displayFragment
        configureAndShowEditFragment(getPropertyFromId(propertyId));
    }

    private Property getPropertyFromId(int propertyId){

        if(propertyId!=-1 && listProperties!=null){
            for(Property property : listProperties){
                if(property!=null){
                    if(property.getId() == propertyId){
                        return PROPERTY_DEMO; // TODO : mettre property à la place de PROPERTY_DEMO
                    }
                }
            }
        }

        return PROPERTY_DEMO; // TODO to modify after tests
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
            fragmentTransaction.replace(R.id.fragment_position, displayFragment);
            fragmentTransaction.commit();
        }
    }

    public void configureAndShowEditFragment(Property property){

        editFragment = new EditFragment();

        // create a bundle
        Bundle bundle = new Bundle();
        Gson gson = new Gson();

        bundle.putInt(LAST_PROPERTY_SELECTED, 3);

        if(property==null){
            bundle.putString(PROPERTY_JSON, "NEW");
        } else {
            // Add the property to the bundle in json format
            String property_json = gson.toJson(property);
            bundle.putString(PROPERTY_JSON, property_json);

            bundle.putString(MODE_SELECTED, "UPDATE");
        }

        // configure and show the editFragment
        editFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_position, editFragment);
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

    public int getCurrentPropertyDisplayed(){

        return PROPERTY_DEMO.getId();
    }

    public Button getButton() {
        return button;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
