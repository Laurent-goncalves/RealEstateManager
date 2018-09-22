package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Fragments.DisplayFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.SearchFragment;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.CallbackPropertyAdapter;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.SearchQuery;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.ConverterJSON;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataActivity;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataDisplayFragment;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataEditFragment;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataListPropertiesFrag;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataSearchFragment;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.Views.PropertyViewHolder;
import java.io.File;
import java.util.List;
import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class BaseActivity extends AppCompatActivity implements BaseActivityListener {

    protected static int RESULT_LOAD_IMAGE_VIEWHOLDER = 2;
    protected static int RESULT_LOAD_MAIN_IMAGE_ = 3;
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
    protected int lastIdPropertyDisplayed;
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.activity_main_nav_view) NavigationView navigationView;
    protected static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    protected static final int PERMISSIONS_REQUEST_ACCESS_IMAGE_GALLERY = 202;
    protected static final int PERMISSIONS_REQUEST_ACCESS_MAIN_IMAGE_GALLERY = 203;
    protected static final int PERMISSIONS_REQUEST_ACCESS_EXTRA_IMAGE_GALLERY = 204;
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
    protected LatLng cameraTarget;
    protected View view;

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
        configureAndShowEditFragment(modeSelected, propertyId);
    }

    public void returnToSearchCriteria(){

        // remove the displayFragment
        if(displayFragment!=null)
            this.getFragmentManager().beginTransaction().remove(displayFragment).commit();

        searchFragment = new SearchFragment();
        Utils.colorFragmentList("GRAY", modeDevice,this);

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

        listPropertiesFragment = new ListPropertiesFragment();
        fragmentDisplayed = LIST_FRAG;
        this.modeSelected = modeSelected;

        // Create a bundle
        Bundle bundle = SaveAndRestoreDataListPropertiesFrag.createBundleForListPropertiesFragment(itemSelected,modeDevice,modeSelected,
                fragmentDisplayed,cameraTarget,searchQuery);

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

    public void changePropertySelectedInList(int idProperty){
        if(listPropertiesFragment!=null){
            listPropertiesFragment.changeSelectedItemInList(idProperty, fragmentDisplayed);
        }
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

    @Override
    public void changeOfPropertySelected(PropertyViewHolder holder, int position, CallbackPropertyAdapter callbackPropertyAdapter) {
        itemSelected = position;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getResources().getString(R.string.warning_title));
        builder.setMessage(getResources().getString(R.string.change_item_list));
        builder.setPositiveButton(getResources().getString(R.string.confirm), (dialog, id) -> callbackPropertyAdapter.proceedToChangeOfPropertySelection(holder,position))
                .setNegativeButton(R.string.cancel, (dialog, id) -> { });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery=searchQuery;
        if(listPropertiesFragment!=null){
            listPropertiesFragment.setSearchQuery(searchQuery);
        }
    }





    @Override
    public void showSnackBar(String text) {
        Snackbar.make(this.findViewById(R.id.fragment_position), text, Snackbar.LENGTH_LONG).show();
    }

    public void askForConfirmationToLeaveEditMode(String modeSelected, int idProp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getResources().getString(R.string.warning_title));
        builder.setMessage(getResources().getString(R.string.leave_edit_mode));
        builder.setPositiveButton(getResources().getString(R.string.confirm), (dialog, id) -> {
                    if(idProp!=-1) { // modify existing property
                        configureAndShowDisplayFragment(modeSelected, idProp);
                        if(modeDevice.equals(MODE_TABLET))
                            getListPropertiesFragment().changeSelectedItemInList(idProp,fragmentDisplayed);
                    } else { // add a new property
                        if(modeDevice.equals(MODE_TABLET)) {
                            configureAndShowDisplayFragment(modeSelected, lastIdPropertyDisplayed);
                            getListPropertiesFragment().changeSelectedItemInList(lastIdPropertyDisplayed, fragmentDisplayed);
                        } else
                            configureAndShowListPropertiesFragment(modeSelected);
                    }
                }
            )
                .setNegativeButton(R.string.cancel, (dialog, id) -> { });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // -------------------------------------------------------------------------------------------------------
    // -------------------------------------- LOADING IMAGE FROM DEVICE --------------------------------------
    // -------------------------------------------------------------------------------------------------------

    public void getMainImage() {
        String[] galleryPermissions = {READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this.getApplicationContext(), galleryPermissions)) {

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_MAIN_IMAGE_);

        } else {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    PERMISSIONS_REQUEST_ACCESS_MAIN_IMAGE_GALLERY, galleryPermissions);
        }
    }

    public void getExtraImage(int viewHolderPosition) {

        this.viewHolderPosition= viewHolderPosition;
        String[] galleryPermissions = {READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this.getApplicationContext(), galleryPermissions)) {

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE_VIEWHOLDER);

        } else {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    PERMISSIONS_REQUEST_ACCESS_EXTRA_IMAGE_GALLERY, galleryPermissions);
        }
    }



    @Override
    public void stopActivity() {
        finish();
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
        } else
            displayError(getResources().getString(R.string.error_image_recovering));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_IMAGE_GALLERY: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setImage(imagePath, imageView);
                } else {
                    displayError(getApplicationContext().getResources().getString(R.string.give_permission));
                }
                break;
            }
            case PERMISSIONS_REQUEST_ACCESS_MAIN_IMAGE_GALLERY: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMainImage();
                } else {
                    displayError(getApplicationContext().getResources().getString(R.string.give_permission));
                }
                break;
            }
            case PERMISSIONS_REQUEST_ACCESS_EXTRA_IMAGE_GALLERY: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMainImage();
                } else {
                    displayError(getApplicationContext().getResources().getString(R.string.give_permission));
                }
                break;
            }
        }
    }

    @Override
    public void setImage(String imagePath, ImageView imageView) {

        try {
            Bitmap bitmap;

            if(imagePath==null){
                imageView.setImageDrawable(this.getApplicationContext().getResources().getDrawable(R.drawable.placeholder));
            } else {

                File f = new File(imagePath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                String[] galleryPermissions = {READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                if (EasyPermissions.hasPermissions(this.getApplicationContext(), galleryPermissions)) {

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),options);

                    if(bitmap!=null)
                        imageView.setImageBitmap(bitmap);

                } else {
                    EasyPermissions.requestPermissions(this, "Access for storage",
                            PERMISSIONS_REQUEST_ACCESS_IMAGE_GALLERY, galleryPermissions);
                    this.imagePath=imagePath;
                    this.imageView=imageView;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(),getResources().getString(R.string.error_image_load),Toast.LENGTH_LONG).show();
        }
    }

    // -------------------------------------------------------------------------------------------------------
    // ------------------------------------------ GETTER AND SETTER ------------------------------------------
    // -------------------------------------------------------------------------------------------------------

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

    public LatLng getCameraTarget() {
        return cameraTarget;
    }

    public void setCameraTarget(LatLng cameraTarget) {
        this.cameraTarget = cameraTarget;
        if(listPropertiesFragment!=null)
            listPropertiesFragment.setCameraTarget(cameraTarget);
    }
}



/*

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

 */