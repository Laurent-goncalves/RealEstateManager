package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.openclassrooms.realestatemanager.Controllers.Fragments.DisplayFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;
import java.util.List;



public class MainActivity extends AppCompatActivity  {

    private static int RESULT_LOAD_IMAGE_VIEWHOLDER = 2;
    private static int RESULT_LOAD_MAIN_IMAGE_ = 3;
    private static final String MODE_SELECTED = "mode_selected";
    private static final String MODE_NEW = "NEW";
    private static final String MODE_UPDATE = "UPDATE";
    private static final String LAST_PROPERTY_SELECTED = "last_property_selected";
    private EditFragment editFragment;
    private DisplayFragment displayFragment;
    private List<Property> listProperties;
    private ToolbarManager toolbarManager;
    private int viewHolderPosition;
    private int currentPositionDisplayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //PropertyDatabase.getInstance(getApplicationContext()).imageDao().deleteAllImage();

        // Configure toolbar
        toolbarManager = new ToolbarManager(this);
        toolbarManager.configure_toolbar();
        currentPositionDisplayed=-1;

        // Show ListPropertiesFragment
        configureAndShowListPropertiesFragment();
    }

    public void displayAlertDeletion(int viewHolderPosition){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getResources().getString(R.string.delete_image));
        builder.setMessage(getResources().getString(R.string.delete_confirmation));
        builder.setPositiveButton(getResources().getString(R.string.confirm), (dialog, id) -> editFragment.alertDeletion(viewHolderPosition))
                .setNegativeButton(R.string.cancel, (dialog, id) -> { });

        AlertDialog dialog = builder.create();
        dialog.show();
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
                        return property;
                    }
                }
            }
        }

        return null; // TODO to modify after tests
    }

    public void configureAndShowDisplayFragment(Property property){
        if(property!=null){

            currentPositionDisplayed = property.getId();
            displayFragment = new DisplayFragment();

            // create a bundle
            Bundle bundle = new Bundle();

            // Add the property Id to the bundle
            bundle.putInt(LAST_PROPERTY_SELECTED, currentPositionDisplayed);

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
        bundle.putInt(LAST_PROPERTY_SELECTED, currentPositionDisplayed);

        if(currentPositionDisplayed==-1){
            bundle.putString(MODE_SELECTED, MODE_NEW);
        } else {
            bundle.putString(MODE_SELECTED, MODE_UPDATE);
        }

        // configure and show the editFragment
        editFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_position, editFragment);
        fragmentTransaction.commit();
    }

    public void configureAndShowListPropertiesFragment(){

        ListPropertiesFragment listPropertiesFragment = new ListPropertiesFragment();

        // configure and show the listPropertiesFragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_position, listPropertiesFragment);
        fragmentTransaction.commit();
    }

    // -------------------------------------------------------------------------------------------------------
    // -------------------------------------- LOADING IMAGE FROM DEVICE --------------------------------------
    // -------------------------------------------------------------------------------------------------------

    public void getMainImage() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_MAIN_IMAGE_);
    }

    public void getExtraImage(int viewHolderPosition) {
        this.viewHolderPosition= viewHolderPosition;
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE_VIEWHOLDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE_VIEWHOLDER && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();

            if(selectedImage!=null){
                String imagePath = Utils.getRealPathFromURI(this,selectedImage);
                editFragment.setExtraImage(imagePath, viewHolderPosition);
            } else
                editFragment.setExtraImage(null, viewHolderPosition);

        } else if (requestCode == RESULT_LOAD_MAIN_IMAGE_ && null != data){
            Uri selectedImage = data.getData();

            if(selectedImage!=null){
                String imagePath = Utils.getRealPathFromURI(this,selectedImage);
                editFragment.setMainImage(imagePath);
            } else
                editFragment.setMainImage(null);
        }
    }

    // -------------------------------------------------------------------------------------------------------
    // ------------------------------------------ GETTER AND SETTER ------------------------------------------
    // -------------------------------------------------------------------------------------------------------

    public int getCurrentPropertyDisplayed(){
        return currentPositionDisplayed;
    }

    public void setListProperties(List<Property> listProperties) {
        this.listProperties = listProperties;
    }
}
