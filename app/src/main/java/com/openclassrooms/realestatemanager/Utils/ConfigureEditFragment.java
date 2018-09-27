package com.openclassrooms.realestatemanager.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.CalendarDialog;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.SearchAddress;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Views.ImagesEditAdapter;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

public class ConfigureEditFragment {

    @BindView(R.id.listview_dates) LinearLayout linearLayoutDates;
    @BindView(R.id.switch_sold) Switch switchSold;
    @BindView(R.id.list_type_properties) Spinner listProperties;
    @BindView(R.id.price_edit_text) EditText priceEdit;
    @BindView(R.id.surface_edit_text) EditText surfaceEdit;
    @BindView(R.id.nbrooms_property_layout) RelativeLayout relativeLayoutNbRooms;
    @BindView(R.id.address_edit_text) android.support.v7.widget.SearchView searchView;
    @BindView(R.id.main_image_selected) ImageView mainImage;
    @BindView(R.id.description_edit_text) EditText descriptionEdit;
    @BindView(R.id.estateagent_edit_text) EditText estateAgentEdit;
    @BindView(R.id.plus_button) ImageButton buttonPlus;
    @BindView(R.id.less_button) ImageButton buttonLess;
    @BindView(R.id.interest_points_editview) EditText interestView;
    @BindView(R.id.buttonCancel) Button buttonCancel;
    @BindView(R.id.buttonSave) Button buttonSave;
    private static final String PUBLISH_DATE = "publish_date";
    private static final String SOLD_DATE = "sold_date";
    private BaseActivityListener baseActivityListener;
    private TextView dateSold;
    private View view;
    private int roomNb;
    private TextView nbRooms;
    private Context context;
    private Property property;
    private EditFragment editFragment;
    private List<ImageProperty> listImages;
    private LinearLayoutManager layoutManager;


    public ConfigureEditFragment(View view, EditFragment editFragment, Context context, Property property, List<ImageProperty> listImages, BaseActivityListener baseActivityListener) {
        ButterKnife.bind(this, view);
        this.view=view;
        this.context=context;
        this.property = property;
        this.editFragment=editFragment;
        this.baseActivityListener=baseActivityListener;
        this.listImages = listImages;
        configureAllAreas();
        configureImagesProperty();
    }

    // ----------------------------------------------------------------------------------------------------------
    // ------------------------------------- CONFIGURATION EDITFRAGMENT -----------------------------------------
    // ----------------------------------------------------------------------------------------------------------

    private void configureAllAreas(){

        if(property!=null){

            // configure mainImage
            configureMainImage();

            // configure switch (sold ?)
            configureSwitchSold();

            // configure type property
            listProperties.setSelection(Utils.getIndexFromList(property.getType(), Arrays.asList(context.getResources().getStringArray(R.array.type_property))));

            // configure price
            priceEdit.setText(String.valueOf(property.getPrice()));

            // configure estate agent
            estateAgentEdit.setText(property.getEstateAgent());

            // configure date selectors
            configureDateSelector();

            // configure surface
            surfaceEdit.setText(String.valueOf(property.getSurface()));

            // configure number of rooms
            nbRooms = relativeLayoutNbRooms.findViewById(R.id.room_number_selector).findViewById(R.id.text_selection);
            nbRooms.setText(String.valueOf(property.getRoomNumber()));
            roomNb = property.getRoomNumber();

            // configure address
            new SearchAddress(editFragment, property, context);

            // configure description
            descriptionEdit.setText(property.getDescription());

            // configure static map
            if(property.getMap()!=null)
                editFragment.setStaticMap(BitmapFactory.decodeByteArray(property.getMap(),0, property.getMap().length));

            // configure interest points
            interestView.setText(property.getInterestPoints());
            editFragment.setInterestPoints(property.getInterestPoints());

            // set lat lng of the property
            editFragment.setLatLngAddress(new LatLng(property.getLat(),property.getLng()));
        }

        // Configure edittext and button to hide keyboard when clicking outside view
        configureViewsToHideKeyboard();

        buttonPlus.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        buttonLess.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    }

    private void configureMainImage(){
        if(baseActivityListener!=null)
            UtilsBaseActivity.setImage(property.getMainImagePath(), mainImage, baseActivityListener.getBaseActivity());
        editFragment.setMainImagePath(property.getMainImagePath());
    }

    private void configureDateSelector(){

        // configure date publication
        View viewPublish = linearLayoutDates.findViewById(R.id.publishing_date_selector);
        TextView datePublish = viewPublish.findViewById(R.id.date_publish_selected);
        datePublish.setText(property.getDateStart());

        // configure date sold

        View viewSold = linearLayoutDates.findViewById(R.id.selling_date_selector);
        dateSold = viewSold.findViewById(R.id.date_sale_selected);
        dateSold.setText(property.getDateSold());

        RelativeLayout relativeLayoutPublish = viewPublish.findViewById(R.id.relativelayout_publish);

        RelativeLayout relativeLayoutSold =viewSold.findViewById(R.id.relativelayout_sold);

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

        FragmentTransaction ft = editFragment.getFragmentManager().beginTransaction();
        Fragment prev = editFragment.getFragmentManager().findFragmentByTag("calendarDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        CalendarDialog calendarDialog = CalendarDialog.newInstance(dateType);
        calendarDialog.setTargetFragment(editFragment,0);
        calendarDialog.show(ft, "calendarDialog");
    }

    private void configureImagesProperty(){

        if(context!=null){

            // Set the recyclerView in horizontal direction
            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            if(listImages.size()>0) {
                if (listImages.get(listImages.size() - 1).getImagePath() != null)
                    listImages.add(new ImageProperty());
            } else {
                listImages.add(new ImageProperty());
            }

            // Create adapter passing in the sample user data
            ImagesEditAdapter adapter = new ImagesEditAdapter(listImages, property, context, editFragment.getCallbackImageSelect(), baseActivityListener);
            editFragment.setAdapter(adapter);

            // Attach the adapter to the recyclerview to populate items
            RecyclerView recyclerView = view.findViewById(R.id.list_images_property_edit);
            recyclerView.setAdapter(adapter);

            // Set layout manager to position the items
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    private void configureSwitchSold(){
        if(property.getSold()) {
            switchSold.setChecked(true);
        } else {
            switchSold.setChecked(false);
        }
    }

    private void configureViewsToHideKeyboard(){

        priceEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
        surfaceEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
        descriptionEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
        estateAgentEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
        interestView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
        searchView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
        buttonCancel.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
        buttonSave.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // ----------------------------------------------------------------------------------------------------------
    // ---------------------------------- LISTENERS FOR UPDATING PROPERTY ---------------------------------------
    // ----------------------------------------------------------------------------------------------------------

    @OnTextChanged(R.id.surface_edit_text)
    public void onSurfaceChanged(){
        if(surfaceEdit.getText().toString().length()==0)
            editFragment.getProperty().setSurface(0d);
        else
            editFragment.getProperty().setSurface(Double.parseDouble(surfaceEdit.getText().toString()));
    }

    @OnTextChanged(R.id.price_edit_text)
    public void onPriceChanged(){
        if(priceEdit.getText().toString().length()==0)
            editFragment.getProperty().setPrice(0d);
        else
            editFragment.getProperty().setPrice(Double.parseDouble(priceEdit.getText().toString()));
    }

    @OnTextChanged(R.id.estateagent_edit_text)
    public void onEstateAgentChanged(){
        editFragment.getProperty().setEstateAgent(estateAgentEdit.getText().toString());
    }

    @OnTextChanged(R.id.interest_points_editview)
    public void onInterestPointsChanged(){
        editFragment.getProperty().setInterestPoints(interestView.getText().toString());
    }

    @OnTextChanged(R.id.description_edit_text)
    public void onDescriptionChanged(){
        editFragment.getProperty().setDescription(descriptionEdit.getText().toString());
    }

    @OnItemSelected(R.id.list_type_properties)
    public void onTypePropertySelected(){
        editFragment.getProperty().setType(listProperties.getSelectedItem().toString());
    }

    @OnClick(R.id.switch_sold)
    public void onClickSwitchSold(){
        if(!switchSold.isChecked()){
            dateSold.setText("");
        }
        editFragment.getProperty().setSold(switchSold.isChecked());
    }

    @OnClick(R.id.plus_button)
    public void onClickListenerButtonPlus() {
        if(roomNb<10) {
            roomNb++;
            editFragment.getProperty().setRoomNumber(roomNb);
            nbRooms.setText(String.valueOf(roomNb));
        }
        buttonPlus.setColorFilter(context.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        new Handler().postDelayed(() -> buttonPlus.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN), 100);
    }

    @OnClick(R.id.less_button)
    public void onClickListenerButtonLess() {
        if(roomNb>=1) {
            roomNb--;
            editFragment.getProperty().setRoomNumber(roomNb);
            nbRooms.setText(String.valueOf(roomNb));
        }

        buttonLess.setColorFilter(context.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        new Handler().postDelayed(() -> buttonLess.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN), 100);
    }

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }
}

