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
import android.widget.Toast;
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
    protected static final String BUNDLE_TYPE_EDIT = "type_edit";
    protected static final String BUNDLE_MODE_SELECTED = "bundle_mode_selected";
    protected static final String MODE_SEARCH = "mode_search";
    protected static final String MODE_DISPLAY = "mode_display";
    protected static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    protected static final String LIST_PROPERTIES_JSON = "list_properties_json";
    protected final static String EXTRA_PROPERTY_ID = "property_id";
    protected final static String BUNDLE_PROP_ID = "property_id";

    protected final static String BUNDLE_FRAG = "fragment_displayed";
    protected final static String LIST_FRAG = "fragment_list";
    protected final static String SEARCH_FRAG = "fragment_search";
    protected final static String DISPLAY_FRAG = "fragment_display";
    protected final static String EDIT_FRAG = "fragment_edit";

    protected final static String BUNDLE_DEVICE = "bundle_device";
    protected final static String MODE_TABLET = "mode_tablet";
    protected final static String MODE_PHONE = "mode_phone";

    protected String fragmentDisplayed;
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.activity_main_nav_view) NavigationView navigationView;
    protected List<Property> listProperties;
    protected ToolbarManager toolbarManager;
    protected EditFragment editFragment;
    protected DisplayFragment displayFragment;
    protected SearchFragment searchFragment;
    protected ListPropertiesFragment listPropertiesFragment;
    protected int idProperty;
    protected int viewHolderPosition;
    protected String modeDevice;
    protected String modeSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setModeDevice(){
        if(findViewById(R.id.fragment_list) != null) { // MODE TABLET
            modeDevice=MODE_TABLET;
        } else { // MODE PHONE
            modeDevice=MODE_PHONE;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_FRAG, fragmentDisplayed);
        outState.putString(BUNDLE_DEVICE, modeDevice);
        outState.putString(BUNDLE_MODE_SELECTED, modeSelected);
        outState.putInt(BUNDLE_PROP_ID, idProperty);
    }

    protected void showSaveInstanceFragment(Bundle bundle){

        fragmentDisplayed = bundle.getString(BUNDLE_FRAG,null);
        modeDevice = bundle.getString(BUNDLE_DEVICE,null);
        modeSelected = bundle.getString(BUNDLE_MODE_SELECTED,null);

        if(fragmentDisplayed !=null){
            switch (fragmentDisplayed) {
                case LIST_FRAG:
                    configureAndShowListPropertiesFragment(MODE_DISPLAY, null);
                    break;
                case SEARCH_FRAG:
                    configureAndShowSearchFragment();
                    break;
                case DISPLAY_FRAG:
                    configureAndShowDisplayFragment(modeSelected, bundle.getInt(BUNDLE_PROP_ID));
                    break;
                case EDIT_FRAG:
                    configureAndShowEditFragment(bundle.getInt(BUNDLE_PROP_ID));
                    break;
            }
        }
    }

    public void changeToDisplayMode(int propertyId){

        // remove the editFragment
        if(editFragment!=null)
            this.getFragmentManager().beginTransaction().remove(editFragment).commit();

        // show displayFragment
        configureAndShowDisplayFragment(modeSelected, propertyId);
    }

    public void changeToEditMode(int propertyId){

        // remove the editFragment
        if(displayFragment!=null)
            this.getFragmentManager().beginTransaction().remove(displayFragment).commit();

        // show displayFragment
        configureAndShowEditFragment(propertyId);
    }

    public void returnToSearchCriteria(){

        // change icons toolbar
        if(toolbarManager!=null)
            toolbarManager.setIconsToolbarListPropertiesMode(modeSelected);

        // remove the displayFragment
        if(displayFragment!=null)
            this.getFragmentManager().beginTransaction().remove(displayFragment).commit();

        FragmentTransaction fragTransReplace = getFragmentManager().beginTransaction();
        fragTransReplace.replace(R.id.fragment_position, searchFragment);
        fragTransReplace.commit();

        FragmentTransaction fragTransRemove = getFragmentManager().beginTransaction();
        fragTransRemove.remove(listPropertiesFragment);
        fragTransRemove.commit();
    }

    public void configureAndShowDisplayFragment(String modeSelected, int propertyId){

        // change icons toolbar
        if(toolbarManager!=null)
            toolbarManager.setIconsToolbarDisplayMode(modeSelected, modeDevice);

        idProperty = propertyId;
        displayFragment = new DisplayFragment();
        fragmentDisplayed = DISPLAY_FRAG;

        // create a bundle
        Bundle bundle = new Bundle();
        bundle.putInt(LAST_PROPERTY_SELECTED, propertyId);
        bundle.putString(BUNDLE_DEVICE, modeDevice);
        bundle.putString(BUNDLE_MODE_SELECTED, modeSelected);

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
        fragmentDisplayed = EDIT_FRAG;
        idProperty = propertyId;

        // create a bundle
        Bundle bundle = new Bundle();
        bundle.putInt(LAST_PROPERTY_SELECTED, propertyId);
        bundle.putString(BUNDLE_MODE_SELECTED, modeSelected);

        if(propertyId==-1){
            bundle.putString(BUNDLE_TYPE_EDIT, MODE_NEW);
        } else {
            bundle.putString(BUNDLE_TYPE_EDIT, MODE_UPDATE);
        }

        // configure and show the editFragment
        editFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_position, editFragment);
        fragmentTransaction.commit();
    }

    public void configureAndShowListPropertiesFragment(String modeSelected, List<Property> listProp){

        // change icons toolbar
        if(toolbarManager!=null)
            toolbarManager.setIconsToolbarListPropertiesMode(modeSelected);

        listPropertiesFragment = new ListPropertiesFragment();
        fragmentDisplayed = LIST_FRAG;

        // Create a bundle
        Bundle bundle = new Bundle();

        bundle.putString(BUNDLE_DEVICE, modeDevice);

        if(modeSelected.equals(MODE_DISPLAY))
            bundle.putString(BUNDLE_MODE_SELECTED,MODE_DISPLAY);
        else if(modeSelected.equals(MODE_SEARCH)) {
            bundle.putString(BUNDLE_MODE_SELECTED,MODE_SEARCH);
            Gson gson = new Gson();
            String listPropertiesJson = gson.toJson(listProp);
            bundle.putString(LIST_PROPERTIES_JSON, listPropertiesJson);
        } else if (modeSelected.equals(MODE_DISPLAY_MAPS)) {
            bundle.putString(BUNDLE_MODE_SELECTED,MODE_DISPLAY_MAPS);
            Gson gson = new Gson();
            String listPropertiesJson = gson.toJson(listProp);
            bundle.putString(LIST_PROPERTIES_JSON, listPropertiesJson);
        }

        // configure and show the listPropertiesFragment
        listPropertiesFragment.setArguments(bundle);

        if(modeDevice.equals(MODE_TABLET)){ // MODE TABLET
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_list, listPropertiesFragment);
            fragmentTransaction.commit();
        } else { // MODE PHONE
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_position, listPropertiesFragment);
            fragmentTransaction.commit();
        }
    }

    public void configureAndShowSearchFragment(){

        // change icons toolbar
        toolbarManager.setIconsToolbarSearchPropertiesMode();

        fragmentDisplayed = SEARCH_FRAG;
        searchFragment = new SearchFragment();

        // Create bundle
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_DEVICE, modeDevice);
        bundle.putString(BUNDLE_MODE_SELECTED, modeSelected);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
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
        builder.setPositiveButton(getResources().getString(R.string.confirm), (dialog, id) -> editFragment.proceedToDeletion(viewHolderPosition))
                .setNegativeButton(R.string.cancel, (dialog, id) -> { });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void displayError(String error){
        Toast toast = Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG);
        toast.show();
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

        // --------------------------- For extra images
        if (requestCode == RESULT_LOAD_IMAGE_VIEWHOLDER && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();

            if(selectedImage!=null){ // if URI is not null, recover real path from URI and send it to editfragment
                String imagePath = Utils.getRealPathFromURI(this,selectedImage);
                editFragment.setExtraImage(imagePath, viewHolderPosition);
            } else
                editFragment.setExtraImage(null, viewHolderPosition);
        // --------------------------- For main image
        } else if (requestCode == RESULT_LOAD_MAIN_IMAGE_ && null != data){
            Uri selectedImage = data.getData();

            if(selectedImage!=null){ // if URI is not null, recover real path from URI and send it to editfragment
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

    public ListPropertiesFragment getListPropertiesFragment() {
        return listPropertiesFragment;
    }

    public int getCurrentPropertyDisplayed(){
        return idProperty;
    }

    public void setCurrentPositionDisplayed(int idProperty) {
        this.idProperty = idProperty;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public String getModeDevice() {
        return modeDevice;
    }
}