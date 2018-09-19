package com.openclassrooms.realestatemanager.Controllers.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.CallbackImageSelect;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.CheckAndSaveEdit;
import com.openclassrooms.realestatemanager.Utils.ConfigureEditFragment;
import com.openclassrooms.realestatemanager.Utils.SaveAndRestoreDataEditFragment;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.Views.ImageUpdateViewHolder;
import com.openclassrooms.realestatemanager.Views.ImagesAddViewHolder;
import com.openclassrooms.realestatemanager.Views.ImagesEditAdapter;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditFragment extends BasePropertyFragment implements CallbackImageSelect {

    @BindView(R.id.address_edit_text) android.support.v7.widget.SearchView searchView;
    @BindView(R.id.nbrooms_property_layout) RelativeLayout relativeLayoutNbRooms;
    @BindView(R.id.interest_points_editview) EditText interestView;
    @BindView(R.id.listview_dates) LinearLayout linearLayoutDates;
    @BindView(R.id.main_image_selected) ImageView mainImage;
    @BindView(R.id.buttonCancel) Button buttonCancel;
    @BindView(R.id.buttonSave) Button buttonSave;
    private TextView datePublish;
    private TextView dateSold;
    private CallbackImageSelect mCallbackImageSelect;
    private String mainImagePath;
    private RecyclerView recyclerView;
    private ImagesEditAdapter adapter;
    private View view;
    private int viewHolderPosition;
    private BaseActivity baseActivity;
    private ConfigureEditFragment configFragment;
    private Parcelable mListState;

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);

        // Assign views and variables
        initialization();

        // Restore datas
        SaveAndRestoreDataEditFragment.recoverDatas(getArguments(),savedInstanceState,this, configFragment);

        // Launch views configuration
        configureViews();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SaveAndRestoreDataEditFragment.saveDatas(outState,property,listImages,modeSelected,typeEdit,idProperty,configFragment);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof BaseActivityListener){
            baseActivityListener = (BaseActivityListener) context;
        }
    }

    // -------------------------------------------------------------------------------------------
    // ------------------------- LISTENERS FOR BUTTONS CANCEL AND SAVE ---------------------------
    // -------------------------------------------------------------------------------------------

    @OnClick(R.id.buttonSave)
    public void onClickListenerButtonSave() {
        new CheckAndSaveEdit(this, context, baseActivityListener, typeEdit);
    }

    @OnClick(R.id.buttonCancel)
    public void onClickListenerButtonCancel() {
        if(property.getId()==-1)
            baseActivityListener.configureAndShowListPropertiesFragment(MODE_DISPLAY,null);
        else
            baseActivityListener.changeToDisplayMode(idProperty);
    }

    // -------------------------------------------------------------------------------------------
    // ----------------------------------- CONFIGURATION VIEWS -----------------------------------
    // -------------------------------------------------------------------------------------------

    private void initialization(){

        recyclerView = view.findViewById(R.id.list_images_property_edit);
        datePublish = linearLayoutDates.findViewById(R.id.publishing_date_selector).findViewById(R.id.date_publish_selected);
        dateSold = linearLayoutDates.findViewById(R.id.selling_date_selector).findViewById(R.id.date_sale_selected);

        // Set the callback and context
        baseActivity = (BaseActivity) getActivity();
        context = getActivity().getApplicationContext();
        mCallbackImageSelect = this;
        listImages = new ArrayList<>();
    }

    private void configureViews(){
        configFragment = new ConfigureEditFragment(view,this,context,property,listImages,baseActivityListener);
    }

    // -------------------------------------------------------------------------------------------
    // ----------------------------------- SELECTION IMAGES --------------------------------------
    // -------------------------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE_MAIN_IMAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionAccessStorage = true;
                    baseActivityListener.getMainImage();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.give_permission), Toast.LENGTH_LONG).show();
                }
                break;
            }
            case PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE_EXTRA_IMAGE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionAccessStorage = true;
                    baseActivityListener.getExtraImage(viewHolderPosition);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.give_permission), Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @OnClick(R.id.main_image_selector)
    public void onClickListener(){
        baseActivityListener.getMainImage();
    }

    @Override
    public void getExtraImageFromGallery(int viewHolderPosition) {
        this.viewHolderPosition = viewHolderPosition;
        baseActivityListener.getExtraImage(viewHolderPosition);
    }

    @Override
    public void alertDeleteImage(int viewHolderPosition) {
        baseActivityListener.displayAlertDeletion(viewHolderPosition);
    }

    public void proceedToDeletion(int viewHolderPosition){
        //ImageUpdateViewHolder holder = (ImageUpdateViewHolder) recyclerView.findViewHolderForLayoutPosition(viewHolderPosition);
        // destroy item from the list
        //ImageProperty imageProperty = holder.getImageProperty();
        adapter.deleteImageToList(viewHolderPosition);
    }

    public void setExtraImage(String imagePath, int holderPosition){

        // Find the view where to put the image
        View view = recyclerView.findViewHolderForAdapterPosition(holderPosition).itemView;
        ImageView image = view.findViewById(R.id.image_property);
        image.setVisibility(View.VISIBLE);

        // check the position of the viewHolder. If it's the last one, it's an ImagesAddViewHolder; if not, it's an ImageUpdateViewHolder
        if(recyclerView.findViewHolderForAdapterPosition(holderPosition).getItemViewType()==1) {
            ImagesAddViewHolder holder = (ImagesAddViewHolder) recyclerView.findViewHolderForLayoutPosition(holderPosition);
            holder.setExtraImage(imagePath);
        } else {
            ImageUpdateViewHolder holder = (ImageUpdateViewHolder) recyclerView.findViewHolderForLayoutPosition(holderPosition);
            holder.setExtraImage(imagePath);
        }
    }

    // ---------------------------------------------------------------------------------------
    // -------------------------------- GETTER and SETTER ------------------------------------
    // ---------------------------------------------------------------------------------------

    public void setLatLngAddress(LatLng latLngAddress) {
        property.setLat(latLngAddress.latitude);
        property.setLng(latLngAddress.longitude);
    }

    public void setStaticMap(Bitmap staticMap) {
        property.setMap(Utils.getBitmapAsByteArray(staticMap));
    }

    public Property getProperty() {
        return property;
    }

    public CallbackImageSelect getCallbackImageSelect() {
        return mCallbackImageSelect;
    }

    public String getMainImagePath() {
        return mainImagePath;
    }

    public void setMainImage(String imagePath){
        mainImagePath = imagePath;
        property.setMainImagePath(mainImagePath);
        baseActivityListener.setImage(imagePath,mainImage);
    }

    public void setMainImagePath(String mainImagePath) {
        this.mainImagePath = mainImagePath;
    }

    public void setInterestPoints(String interestPoints){

        // Enable button save and cancel
        baseActivity.runOnUiThread(() -> {
            interestView.setText(Utils.removeHooksFromString(interestPoints));
            buttonSave.setEnabled(true);
            buttonCancel.setEnabled(true);
        });
    }

    public void setAdapter(ImagesEditAdapter adapter) {
        this.adapter = adapter;
    }

    public TextView getDatePublish() {
        return datePublish;
    }

    public TextView getDateSold() {
        return dateSold;
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public Button getButtonCancel() {
        return buttonCancel;
    }

    public Button getButtonSave() {
        return buttonSave;
    }

    public BaseActivity getBaseActivity() {
        return baseActivity;
    }
}