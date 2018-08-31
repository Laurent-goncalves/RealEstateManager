package com.openclassrooms.realestatemanager.Controllers.Fragments;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.CalendarDialog;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends BasePropertyFragment implements CallbackImageSelect {

    @BindView(R.id.switch_sold) public Switch switchSold;
    @BindView(R.id.list_type_properties) public Spinner listProperties;
    @BindView(R.id.price_edit_text) public EditText priceEdit;
    @BindView(R.id.listview_dates) public LinearLayout linearLayoutDates;
    @BindView(R.id.surface_edit_text) public EditText surfaceEdit;
    @BindView(R.id.nbrooms_property_layout) public RelativeLayout relativeLayoutNbRooms;
    @BindView(R.id.address_edit_text) public android.support.v7.widget.SearchView searchView;
    @BindView(R.id.description_edit_text) public EditText descriptionEdit;
    @BindView(R.id.buttonCancel) public Button buttonCancel;
    @BindView(R.id.buttonSave) public Button buttonSave;
    @BindView(R.id.main_image_selected) public ImageView mainImage;
    @BindView(R.id.estateagent_edit_text) public EditText estateAgentEdit;
    @BindView(R.id.plus_button) public ImageButton buttonPlus;
    @BindView(R.id.less_button) public ImageButton buttonLess;
    @BindView(R.id.interest_points_editview) public EditText interestView;
    public TextView nbRooms;
    public TextView datePublish;
    public TextView dateSold;
    private int roomNb;
    private CallbackImageSelect mCallbackImageSelect;
    private RecyclerView recyclerView;
    private ImagesEditAdapter adapter;
    private int idProperty;
    private String interestPoints;
    private LatLng latLngAddress;
    private Bitmap staticMap;
    private String mainImagePath;
    private View view;
    private CalendarDialog calendarDialog;
    private static final String PUBLISH_DATE = "publish_date";
    private static final String SOLD_DATE = "sold_date";

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit, container, false);
        recyclerView= view.findViewById(R.id.list_images_property_edit);
        ButterKnife.bind(this, view);

        // Set the BaseActivity, callback and context
        baseActivity = (BaseActivity) getActivity();
        context = baseActivity.getApplicationContext();
        mCallbackImageSelect = this;
        listImages = new ArrayList<>();

        // We recover in the bundle the property in json format
        if(getArguments()!=null){

            mode=getArguments().getString(MODE_SELECTED,null);
            idProperty=getArguments().getInt(LAST_PROPERTY_SELECTED,-1);

            if(mode!=null){

                if(mode.equals("UPDATE")){ // mode update property
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

    @OnClick(R.id.plus_button)
    public void onClickListenerButtonPlus() {
        if(roomNb<10) {
            roomNb++;
            nbRooms.setText(String.valueOf(roomNb));
        }
        buttonPlus.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        new Handler().postDelayed(() -> buttonPlus.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN), 100);
    }

    @OnClick(R.id.less_button)
    public void onClickListenerButtonLess() {
        if(roomNb>=1) {
            roomNb--;
            nbRooms.setText(String.valueOf(roomNb));
        }

        buttonLess.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        new Handler().postDelayed(() -> buttonLess.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN), 100);
    }

    @OnClick(R.id.buttonSave)
    public void onClickListenerButtonSave() {
        new CheckAndSaveEdit(this, context);
        baseActivity.changeToDisplayMode(idProperty);
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
                    Toast.makeText(baseActivity, "Please give your permission.", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    // -------------------------------------------------------------------------------------------
    // ----------------------------------- CONFIGURATION VIEWS -----------------------------------
    // -------------------------------------------------------------------------------------------


    private void configureViews(){
        new ConfigureEditFragment(this,context);

        RelativeLayout relativeLayoutPublish = linearLayoutDates.findViewById(R.id.publishing_date_selector)
                .findViewById(R.id.relativelayout_publish);

        RelativeLayout relativeLayoutSold = linearLayoutDates.findViewById(R.id.selling_date_selector)
                .findViewById(R.id.relativelayout_sold);

        datePublish = relativeLayoutPublish.findViewById(R.id.date_publish_selected);

        dateSold = relativeLayoutSold.findViewById(R.id.date_sale_selected);

        linearLayoutDates.findViewById(R.id.publishing_date_selector)
                .setOnClickListener(v -> showCalendarView(PUBLISH_DATE));

        relativeLayoutPublish.findViewById(R.id.icon_expand)
                .setOnClickListener(v -> showCalendarView(PUBLISH_DATE));

        linearLayoutDates.findViewById(R.id.selling_date_selector)
                .setOnClickListener(v -> showCalendarView(SOLD_DATE));

        relativeLayoutSold.findViewById(R.id.icon_expand)
                .setOnClickListener(v -> showCalendarView(SOLD_DATE));
    }

    private void showCalendarView(String dateType){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("calendarDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        calendarDialog = CalendarDialog.newInstance(dateType);
        calendarDialog.setTargetFragment(this,0);
        calendarDialog.show(ft, "calendarDialog");
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

    public void alertDeletion(int viewHolderPosition){
        ImageUpdateViewHolder holder = (ImageUpdateViewHolder) recyclerView.findViewHolderForLayoutPosition(viewHolderPosition);

        // destroy item from the list
        ImageProperty imageProperty = holder.getImageProperty();
        adapter.deleteImageToList(imageProperty);
    }

    // ---------------------------------------------------------------------------------------
    // -------------------------------- GETTER and SETTER ------------------------------------
    // ---------------------------------------------------------------------------------------

    public android.support.v7.widget.SearchView getSearchView() {
        return searchView;
    }

    public Button getButtonSave() {
        return buttonSave;
    }

    public Button getButtonCancel() {
        return buttonCancel;
    }

    public void setExtraImage(String imagePath, int holderPosition){

        View view = recyclerView.findViewHolderForAdapterPosition(holderPosition).itemView;
        ImageView image = view.findViewById(R.id.image_property);
        image.setVisibility(View.VISIBLE);

        if(recyclerView.findViewHolderForAdapterPosition(holderPosition).getItemViewType()==1) {
            ImagesAddViewHolder holder = (ImagesAddViewHolder) recyclerView.findViewHolderForLayoutPosition(holderPosition);
            holder.setExtraImage(imagePath);
        } else {
            ImageUpdateViewHolder holder = (ImageUpdateViewHolder) recyclerView.findViewHolderForLayoutPosition(holderPosition);
            holder.setExtraImage(imagePath);
        }
    }

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
        return mode;
    }

    public String getInterestPoints() {
        return interestPoints;
    }

    public CallbackImageSelect getCallbackImageSelect() {
        return mCallbackImageSelect;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public ImagesEditAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ImagesEditAdapter adapter) {
        this.adapter = adapter;
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

    public View getEditView() {
        return view;
    }

    public TextView getDatePublish() {
        return datePublish;
    }

    public TextView getDateSold() {
        return dateSold;
    }
}
