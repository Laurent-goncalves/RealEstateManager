package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;
import com.openclassrooms.realestatemanager.Controllers.Fragments.DisplayFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.SearchFragment;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;
import java.util.List;
import butterknife.BindView;



public class BaseActivity extends AppCompatActivity {

    protected static int RESULT_LOAD_IMAGE_VIEWHOLDER = 2;
    protected static int RESULT_LOAD_MAIN_IMAGE_ = 3;
    protected static final String MODE_SELECTED = "mode_selected";
    protected static final String MODE_NEW = "NEW";
    protected static final String MODE_UPDATE = "UPDATE";
    protected static final String LAST_PROPERTY_SELECTED = "last_property_selected";
    protected static final String MODE_SEARCH = "mode_search";
    protected static final String MODE_DISPLAY = "mode_display";
    protected static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    protected static final String LIST_PROPERTIES_JSON = "list_properties_json";
    protected final static String EXTRA_PROPERTY_ID = "property_id";
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.activity_main_nav_view) NavigationView navigationView;
    protected List<Property> listProperties;
    protected ToolbarManager toolbarManager;
    protected EditFragment editFragment;
    protected DisplayFragment displayFragment;
    protected ListPropertiesFragment listPropertiesFragment;
    protected int currentPositionDisplayed;
    protected int viewHolderPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void changeToDisplayMode(int propertyId){

        // remove the editFragment
        if(editFragment!=null)
            this.getFragmentManager().beginTransaction().remove(editFragment).commit();

        // show displayFragment
        configureAndShowDisplayFragment(propertyId);
    }

    public void changeToEditMode(int propertyId){

        // remove the editFragment
        if(displayFragment!=null)
            this.getFragmentManager().beginTransaction().remove(displayFragment).commit();

        // show displayFragment
        configureAndShowEditFragment(propertyId);
    }

    public void configureAndShowDisplayFragment(int propertyId){

        // change icons toolbar
        toolbarManager.setIconsToolbarDisplayMode();

        currentPositionDisplayed = propertyId; // TODO check if necessary
        displayFragment = new DisplayFragment();

        // create a bundle
        Bundle bundle = new Bundle();

        // Add the property Id to the bundle
        bundle.putInt(LAST_PROPERTY_SELECTED, propertyId);
        bundle.putBoolean(MODE_DISPLAY_MAPS,false);

        // configure and show the editFragment
        displayFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_position, displayFragment);
        fragmentTransaction.commit();
    }

    public void configureAndShowEditFragment(int propertyId){

        // change icons toolbar
        toolbarManager.setIconsToolbarEditMode();

        editFragment = new EditFragment();

        // create a bundle
        Bundle bundle = new Bundle();
        bundle.putInt(LAST_PROPERTY_SELECTED, propertyId);

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

    public void configureAndShowListPropertiesFragment(String modeSelected, List<Property> listProp){

        // change icons toolbar
        toolbarManager.setIconsToolbarListPropertiesMode();

        listPropertiesFragment = new ListPropertiesFragment();

        Bundle bundle = new Bundle();

        if(modeSelected.equals(MODE_DISPLAY))
            bundle.putString(MODE_SELECTED,MODE_DISPLAY);
        else {
            bundle.putString(MODE_SELECTED,MODE_SEARCH);
            Gson gson = new Gson();
            String listPropertiesJson = gson.toJson(listProp);
            bundle.putString(LIST_PROPERTIES_JSON, listPropertiesJson);
        }

        // configure and show the listPropertiesFragment
        listPropertiesFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_position, listPropertiesFragment);
        fragmentTransaction.commit();
    }

    public void configureAndShowSearchFragment(){

        SearchFragment searchFragment = new SearchFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_position, searchFragment);
        fragmentTransaction.commit();

    }

    public void launchMapsActivity(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void launchSearchActivity(){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void launchMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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

    public void setListProperties(List<Property> listProperties) {
        this.listProperties = listProperties;
    }

    public EditFragment getEditFragment() {
        return editFragment;
    }

    public int getCurrentPropertyDisplayed(){
        return currentPositionDisplayed;
    }

    public void setCurrentPositionDisplayed(int currentPositionDisplayed) {
        this.currentPositionDisplayed = currentPositionDisplayed;
    }

    public ToolbarManager getToolbarManager() {
        return toolbarManager;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }
}