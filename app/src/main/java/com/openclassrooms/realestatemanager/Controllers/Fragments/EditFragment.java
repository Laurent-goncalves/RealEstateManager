package com.openclassrooms.realestatemanager.Controllers.Fragments;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Models.CallbackImageSelect;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.CheckAndSaveEdit;
import com.openclassrooms.realestatemanager.Utils.ConfigureEditFragment;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.Views.ImageUpdateViewHolder;
import com.openclassrooms.realestatemanager.Views.ImagesAddViewHolder;
import com.openclassrooms.realestatemanager.Views.ImagesEditAdapter;
import java.io.File;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditFragment extends BasePropertyFragment implements CallbackImageSelect {

    @BindView(R.id.address_edit_text) android.support.v7.widget.SearchView searchView;
    @BindView(R.id.listview_dates) LinearLayout linearLayoutDates;
    @BindView(R.id.interest_points_editview) EditText interestView;
    @BindView(R.id.buttonCancel) Button buttonCancel;
    @BindView(R.id.buttonSave) Button buttonSave;
    private TextView datePublish;
    private TextView dateSold;
    private CallbackImageSelect mCallbackImageSelect;
    private int idProperty;
    private String interestPoints;
    private LatLng latLngAddress;
    private Bitmap staticMap;
    private String mainImagePath;
    private RecyclerView recyclerView;
    private ImagesEditAdapter adapter;
    private View view;
    private static final String BUNDLE_TYPE_EDIT = "type_edit";
    private static final String MODE_UPDATE = "UPDATE";
    private String typeEdit;
    @BindView(R.id.main_image_selected) ImageView mainImage;

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);
        recyclerView = view.findViewById(R.id.list_images_property_edit);
        datePublish = linearLayoutDates.findViewById(R.id.publishing_date_selector).findViewById(R.id.date_publish_selected);
        dateSold = linearLayoutDates.findViewById(R.id.selling_date_selector).findViewById(R.id.date_sale_selected);
        setRetainInstance(true);

        // Set the BaseActivity, callback and context
        baseActivity = (BaseActivity) getActivity();
        context = baseActivity.getApplicationContext();
        mCallbackImageSelect = this;
        listImages = new ArrayList<>();

        // We recover in the bundle the property in json format
        if(getArguments()!=null){

            modeSelected = getArguments().getString(MODE_SELECTED,MODE_DISPLAY);
            idProperty=getArguments().getInt(LAST_PROPERTY_SELECTED,-1);
            typeEdit=getArguments().getString(BUNDLE_TYPE_EDIT,MODE_UPDATE);

            if(typeEdit!=null){

                if(typeEdit.equals(MODE_UPDATE)){ // mode update property
                    // Recover the property datas
                    recoverProperty(idProperty);
                    recoverImagesProperty(idProperty);
                } else {
                    // create empty property
                    property = new Property(-1,null,0d,0d,0,null,null,
                            null,false,null,null,0d,0d,null,null,null);
                }
            }
        }

        listImages.add(new ImageProperty()); // Add item for "add a photo"

        configureViews();

        return view;
    }

    @OnClick(R.id.buttonSave)
    public void onClickListenerButtonSave() {
        new CheckAndSaveEdit(this, context, baseActivity, typeEdit);
    }

    @OnClick(R.id.buttonCancel)
    public void onClickListenerButtonCancel() {
        if(property.getId()==-1)
            baseActivity.configureAndShowListPropertiesFragment(MODE_DISPLAY,null);
        else
            baseActivity.changeToDisplayMode(idProperty);
    }

    @OnClick(R.id.main_image_selector)
    public void onClickListener(){
        baseActivity.getMainImage();
    }

    public void setMainImage(String imagePath){
        mainImagePath = imagePath;
        Utils.setImageBitmapInView(imagePath,mainImage,baseActivity);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    File f= new File(mainImagePath);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                    Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),options);
                    mainImage.setImageBitmap(bitmap);

                } else {
                    Toast.makeText(baseActivity, context.getResources().getString(R.string.give_permission), Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    // -------------------------------------------------------------------------------------------
    // ----------------------------------- CONFIGURATION VIEWS -----------------------------------
    // -------------------------------------------------------------------------------------------

    private void configureViews(){
        new ConfigureEditFragment(view,this,context,property,listImages,baseActivity);
    }

    public void setInterestPoints(String interestPoints){

        this.interestPoints = interestPoints;

        // Enable button save and cancel
        baseActivity.runOnUiThread(() -> {
            interestView.setText(Utils.removeHooksFromString(interestPoints));
            buttonSave.setEnabled(true);
            buttonCancel.setEnabled(true);
        });
    }

    @Override
    public void getExtraImageFromGallery(int viewHolderPosition) {
        baseActivity.getExtraImage(viewHolderPosition);
    }

    @Override
    public void alertDeleteImage(int viewHolderPosition) {
        baseActivity.displayAlertDeletion(viewHolderPosition);
    }

    public void proceedToDeletion(int viewHolderPosition){
        ImageUpdateViewHolder holder = (ImageUpdateViewHolder) recyclerView.findViewHolderForLayoutPosition(viewHolderPosition);

        // destroy item from the list
        ImageProperty imageProperty = holder.getImageProperty();
        adapter.deleteImageToList(imageProperty);
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
        this.latLngAddress = latLngAddress;
    }

    public LatLng getLatLngAddress() {
        return latLngAddress;
    }

    public Property getProperty() {
        return property;
    }

    public String getMode() {
        return modeSelected;
    }

    public String getInterestPoints() {
        return interestPoints;
    }

    public CallbackImageSelect getCallbackImageSelect() {
        return mCallbackImageSelect;
    }

    public void setStaticMap(Bitmap staticMap) {
        this.staticMap = staticMap;
    }

    public Bitmap getStaticMap() {
        return staticMap;
    }

    public String getMainImagePath() {
        return mainImagePath;
    }

    public void setMainImagePath(String mainImagePath) {
        this.mainImagePath = mainImagePath;
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
}
