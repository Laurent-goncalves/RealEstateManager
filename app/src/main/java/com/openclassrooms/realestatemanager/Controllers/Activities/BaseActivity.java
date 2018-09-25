package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.google.android.gms.maps.model.LatLngBounds;
import com.openclassrooms.realestatemanager.Controllers.Fragments.DisplayFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.SearchFragment;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.SearchQuery;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataDisplayFragment;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataEditFragment;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataListPropertiesFrag;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataSearchFragment;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.Utils.UtilsBaseActivity;
import java.util.List;
import butterknife.BindView;


public class BaseActivity extends AppCompatActivity implements BaseActivityListener {

    protected static final String LAST_PROPERTY_SELECTED = "last_property_selected";
    protected static final String BUNDLE_MODE_SELECTED = "bundle_mode_selected";
    protected static final String MODE_DISPLAY = "mode_display";
    protected final static String LIST_FRAG = "fragment_list";
    protected final static String SEARCH_FRAG = "fragment_search";
    protected final static String DISPLAY_FRAG = "fragment_display";
    protected final static String EDIT_FRAG = "fragment_edit";
    protected final static String BUNDLE_DEVICE = "bundle_device";
    protected final static String MODE_TABLET = "mode_tablet";
    protected final static String MODE_PHONE = "mode_phone";
    protected final static String GRAY_COLOR = "GRAY";
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.activity_main_nav_view) NavigationView navigationView;
    protected static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    protected int lastIdPropertyDisplayed;
    protected String fragmentDisplayed;
    protected List<Property> listProperties;
    protected ToolbarManager toolbarManager;
    protected EditFragment editFragment;
    protected DisplayFragment displayFragment;
    protected SearchFragment searchFragment;
    protected ListPropertiesFragment listPropertiesFragment;
    protected int idProperty;
    protected int itemSelected;
    protected int viewHolderPosition;
    protected String modeDevice;
    protected String modeSelected;
    protected String imagePath;
    protected ImageView imageView;
    protected SearchQuery searchQuery;
    protected LatLngBounds cameraBounds;
    protected View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // -------------------------------------------------------------------------------------------------------------
    // ----------------------------------- FRAGMENTS CONFIGURATION -------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------

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

        // update itemSelected of listPropFragment
        if(listPropertiesFragment!=null && propertyId==-1)
            listPropertiesFragment.setItemSelected(-1);

        // show displayFragment
        configureAndShowEditFragment(modeSelected, propertyId);
    }

    public void returnToSearchCriteria(){

        // remove the displayFragment
        if(displayFragment!=null)
            this.getFragmentManager().beginTransaction().remove(displayFragment).commit();

        searchFragment = new SearchFragment();
        Utils.colorFragmentList(GRAY_COLOR, modeDevice,fragmentDisplayed, this);

        // Create a bundle
        Bundle bundle = SaveAndRestoreDataSearchFragment.createBundleForSearchFragment(modeDevice,searchQuery);
        searchFragment.setArguments(bundle);

        FragmentTransaction fragTransReplace = getFragmentManager().beginTransaction();
        fragTransReplace.replace(R.id.fragment_position, searchFragment);
        fragTransReplace.commit();

        if(listPropertiesFragment!=null) {
            FragmentTransaction fragTransRemove = getFragmentManager().beginTransaction();
            fragTransRemove.remove(listPropertiesFragment);
            fragTransRemove.commit();
        }
    }

    public void configureAndShowDisplayFragment(String modeSelected, int propertyId){

        // Initialize variables
        lastIdPropertyDisplayed = propertyId;
        idProperty = propertyId;
        displayFragment = new DisplayFragment();
        fragmentDisplayed = DISPLAY_FRAG;

        if(listPropertiesFragment!=null)
            listPropertiesFragment.setFragmentDisplayed(fragmentDisplayed);

        // create a bundle
        Bundle bundle = SaveAndRestoreDataDisplayFragment.createBundleForDisplayFragment(idProperty,modeDevice,modeSelected);
        displayFragment.setArguments(bundle);

        // configure and show the editFragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_position, displayFragment, DISPLAY_FRAG);
        fragmentTransaction.commit();
    }

    public void configureAndShowEditFragment(String modeSelected, int propertyId) {

        // Initialize variables
        idProperty = propertyId;
        editFragment = new EditFragment();
        fragmentDisplayed = EDIT_FRAG;

        if (listPropertiesFragment != null){
            listPropertiesFragment.setFragmentDisplayed(fragmentDisplayed);

            if(propertyId==-1 && listProperties !=null) // if new property, unselect the selected item in the list
                listPropertiesFragment.removeSelectedItemInList();
        }

        // Create a bundle
        Bundle bundle = SaveAndRestoreDataEditFragment.createBundleForEditFragment(idProperty, modeDevice, modeSelected);
        editFragment.setArguments(bundle);

        // configure and show the editFragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_position, editFragment, EDIT_FRAG);
        fragmentTransaction.commit();
    }

    public void configureAndShowListPropertiesFragment(String modeSelected){

        // Initialize variables
        listPropertiesFragment = new ListPropertiesFragment();
        fragmentDisplayed = LIST_FRAG;
        this.modeSelected = modeSelected;

        // Create a bundle
        Bundle bundle = SaveAndRestoreDataListPropertiesFrag.createBundleForListPropertiesFragment(-1
                ,modeDevice,modeSelected,fragmentDisplayed,cameraBounds,searchQuery);

         // configure and show the listPropertiesFragment
        listPropertiesFragment.setArguments(bundle);

        if(modeDevice.equals(MODE_TABLET)){ // MODE TABLET
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_list, listPropertiesFragment,LIST_FRAG);
            fragmentTransaction.commit();
        } else { // MODE PHONE
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_position, listPropertiesFragment,LIST_FRAG);
            fragmentTransaction.commit();
        }
    }

    public void refreshListPropertiesFragment(Bundle bundle, String modeSelected){
        if(listPropertiesFragment!=null)
            listPropertiesFragment.refreshListProperties(bundle, modeSelected);
    }

    public void changePropertySelectedInList(int idProperty){
        if(listPropertiesFragment!=null){
            listPropertiesFragment.changeSelectedItemInList(idProperty, fragmentDisplayed);
        }
    }

    // -------------------------------------------------------------------------------------------------------------
    // ------------------------------- ACTIVITIES LAUNCHERS AND STOPPER --------------------------------------------
    // -------------------------------------------------------------------------------------------------------------

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

    @Override
    public void stopActivity() {
        finish();
    }

    // -------------------------------------------------------------------------------------------------------------
    // ------------------------------- RESULTS ACTIVITY AND PERMISSION ---------------------------------------------
    // -------------------------------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UtilsBaseActivity.handleOnActivityResult(requestCode,resultCode,data,this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        UtilsBaseActivity.handleOnRequestPermissionResult(requestCode, grantResults,this);
    }

    // -------------------------------------------------------------------------------------------------------
    // ------------------------------------------ GETTER AND SETTER ------------------------------------------
    // -------------------------------------------------------------------------------------------------------

    @Override
    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery=searchQuery;
        if(listPropertiesFragment!=null){
            listPropertiesFragment.setSearchQuery(searchQuery);
        }
    }

    @Override
    public String getFragmentDisplayed() {
        return fragmentDisplayed;
    }

    public BaseActivity getBaseActivity(){
        return this;
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    public void setListProperties(List<Property> listProperties) {
        this.listProperties = listProperties;
    }

    public List<Property> getListProperties() {
        return listProperties;
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

    public SearchFragment getSearchFragment() {
        return searchFragment;
    }

    public void setFragmentDisplayed(String fragmentDisplayed) {
        this.fragmentDisplayed = fragmentDisplayed;
    }

    public void setModeSelected(String modeSelected) {
        this.modeSelected = modeSelected;
    }

    public ToolbarManager getToolbarManager() {
        return toolbarManager;
    }

    public void setLastIdPropertyDisplayed(int lastIdPropertyDisplayed) {
        this.lastIdPropertyDisplayed = lastIdPropertyDisplayed;
    }

    public LatLngBounds getCameraBounds() {
        return cameraBounds;
    }

    public void setCameraBounds(LatLngBounds cameraBounds) {
        this.cameraBounds = cameraBounds;
        if(listPropertiesFragment!=null)
            listPropertiesFragment.setCameraBounds(cameraBounds);
    }

    public void setItemSelected(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    public int getLastIdPropertyDisplayed() {
        return lastIdPropertyDisplayed;
    }

    public void setViewHolderPosition(int viewHolderPosition) {
        this.viewHolderPosition = viewHolderPosition;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setModeDevice(String modeDevice) {
        this.modeDevice = modeDevice;
    }

    public int getViewHolderPosition() {
        return viewHolderPosition;
    }

    public String getImagePath() {
        return imagePath;
    }

    public ImageView getImageView() {
        return imageView;
    }
}