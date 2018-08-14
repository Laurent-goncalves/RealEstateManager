package com.openclassrooms.realestatemanager.Controllers.Fragments;


import android.arch.lifecycle.Observer;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.CallbackImageSelect;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.Models.SearchAddress;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Views.ImagesEditAdapter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment implements CallbackImageSelect {

    @BindView(R.id.switch_sold) Switch switchSold;
    @BindView(R.id.list_type_properties) Spinner listProperties;
    @BindView(R.id.price_edit_text) EditText priceEdit;
    @BindView(R.id.listview_dates) LinearLayout linearLayoutDates;
    @BindView(R.id.calendar) CalendarView calendarView;
    @BindView(R.id.surface_edit_text) EditText surfaceEdit;
    @BindView(R.id.nbrooms_property_layout) RelativeLayout relativeLayoutNbRooms;
    @BindView(R.id.address_edit_text) android.support.v7.widget.SearchView addressEdit;
    @BindView(R.id.description_edit_text) EditText descriptionEdit;
    @BindView(R.id.buttonCancel) Button buttonCancel;
    @BindView(R.id.buttonSave) Button buttonSave;
    private TextView nbRooms;
    private TextView datePublish;
    private TextView dateSold;
    private static final String PROPERTY_JSON = "property_json";
    private static final String MODE_SELECTED = "mode_selected";
    private static final String IMAGES_JSON = "images_json";
    private String mode;
    private Property propertyInit;
    private List<ImageProperty> listImages;
    private int roomNb;
    private CallbackImageSelect mCallbackImageSelect;
    private MainActivity mainActivity;
    private Context context;
    private RecyclerView recyclerView;
    private PropertyDatabase database;
    private View view;
    private ImagesEditAdapter adapter;

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit, container, false);
        recyclerView=view.findViewById(R.id.list_images_property_edit);
        ButterKnife.bind(this,view);

        // Set the mainActivity
        mainActivity = (MainActivity) getActivity();
        this.context=mainActivity.getApplicationContext();
        this.database = PropertyDatabase.getInstance(context);

        // We recover in the bundle the property in json format
        if(getArguments()!=null){

            mode=getArguments().getString(MODE_SELECTED,null);

            if(mode!=null){

                if(mode.equals("UPDATE")){ // mode update property

                    // Recover the property datas
                    Gson gson = new Gson();
                    String propJSON = getArguments().getString(PROPERTY_JSON,null);
                    Type propertyType = new TypeToken<Property>(){}.getType();
                    propertyInit = gson.fromJson(propJSON,propertyType);

                    // Recover list of imagesProperty from database and configure images inside recyclerView
                    database.imageDao().getAllImages().observeForever(ListImagesObserver);
                }
            }
        }

        listImages = new ArrayList<>();
        mCallbackImageSelect = this;
        configureAllAreas();
        configure_date_selectors();
        return view;
    }

    private void configureAllAreas(){

        if(propertyInit!=null){

            // configure switch (sold ?)
            configureSwitchSold();

            // configure type of property
            configurePropertyType();

            // configure price
            priceEdit.setText(String.valueOf(propertyInit.getPrice()));

            // configure date publication
            datePublish = linearLayoutDates.findViewById(R.id.publishing_date_selector).findViewById(R.id.date_publish_selected);
            datePublish.setText(propertyInit.getDateSold());

            // configure date sold
            dateSold = linearLayoutDates.findViewById(R.id.selling_date_selector).findViewById(R.id.date_sale_selected);
            dateSold.setText(propertyInit.getDateSold());

            // configure surface
            surfaceEdit.setText(String.valueOf(propertyInit.getSurface()));

            // configure number of rooms
            nbRooms = relativeLayoutNbRooms.findViewById(R.id.room_number_selector).findViewById(R.id.text_selection);
            roomNb = propertyInit.getRoomNumber();
            nbRooms.setText(String.valueOf(roomNb));

            // configure address
            new SearchAddress(this, context);

            // configure description
            descriptionEdit.setText(propertyInit.getDescription());
        }
    }

    @OnClick(R.id.plus_button)
    public void onClickListenerButtonPlus() {
        if(roomNb<10) {
            roomNb++;
            nbRooms.setText(String.valueOf(roomNb));
        }
    }

    @OnClick(R.id.less_button)
    public void onClickListenerButtonLess() {
        if(roomNb>=1) {
            roomNb--;
            nbRooms.setText(String.valueOf(roomNb));
        }
    }

    @OnClick(R.id.buttonSave)
    public void onClickListenerButtonSave() {

        mainActivity.changeToDisplayMode(propertyInit.getId());

    }

    @OnClick(R.id.buttonCancel)
    public void onClickListenerButtonCancel() {


    }

    // -------------------------------------------------------------------------------------------
    // ----------------------------------- CONFIGURATION VIEWS -----------------------------------
    // -------------------------------------------------------------------------------------------

    private void configureImagesProperty(){

        if(context!=null){

            // Set the recyclerView in horizontal direction
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            // Create adapter passing in the sample user data
            adapter = new ImagesEditAdapter(listImages,propertyInit,context,mCallbackImageSelect);
            // Attach the adapter to the recyclerview to populate items
            recyclerView.setAdapter(adapter);
            // Set layout manager to position the items
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    final Observer<List<ImageProperty>> ListImagesObserver = new Observer<List<ImageProperty>>() {
        @Override
        public void onChanged(@Nullable final List<ImageProperty> newName) {
            if (newName != null) {
                if(newName.size() != 0){
                    listImages.addAll(newName);
                }
            }
            listImages.add(new ImageProperty());
            configureImagesProperty();
        }
    };

    private void configurePropertyType(){

        String[] listPropertiesTypes = getResources().getStringArray(R.array.type_property);
        int i = 0;

        for(String type : listPropertiesTypes){
            if(type!=null){
                if(type.equals(propertyInit.getType())){
                    break;
                }
            }
            i++;
        }
        //listProperties.setSelection(i);
    }

    private void configureSwitchSold(){
        if(propertyInit.getSold())
            switchSold.setChecked(true);
        else
            switchSold.setChecked(false);
    }

    private void configure_date_selectors(){

        ImageView expandPublish = linearLayoutDates
                .findViewById(R.id.publishing_date_selector)
                .findViewById(R.id.relativelayout_publish)
                .findViewById(R.id.icon_expand);

        calendarView.setVisibility(View.GONE);

        expandPublish.setOnClickListener(v -> {
            if(calendarView.getVisibility()==View.VISIBLE)
                calendarView.setVisibility(View.GONE);
            else
                calendarView.setVisibility(View.VISIBLE);
        });

        ImageView expandSold = linearLayoutDates
                .findViewById(R.id.selling_date_selector)
                .findViewById(R.id.relativelayout_sold)
                .findViewById(R.id.icon_expand);

        expandSold.setOnClickListener(v -> {
            if(calendarView.getVisibility()==View.VISIBLE)
                calendarView.setVisibility(View.GONE);
            else
                calendarView.setVisibility(View.VISIBLE);
        });
    }

    public void setInterestPoints(String interestPoints){

        propertyInit.setInterestPoints(interestPoints);

        // Enable button save
        this.getButtonSave().setEnabled(true);
    }

    @Override
    public void getImageFromGallery(int viewHolderPosition) {
        mainActivity.getImageFromGallery(viewHolderPosition);
    }

    // ---------------------------------------------------------------------------------------
    // -------------------------------- GETTER and SETTER ------------------------------------
    // ---------------------------------------------------------------------------------------

    public android.support.v7.widget.SearchView getAddressEdit() {
        return addressEdit;
    }

    public Button getButtonSave() {
        return buttonSave;
    }

    public Property getPropertyInit() {
        return propertyInit;
    }

    public void setImage(Uri imageUri, int holderPosition){

        View view = recyclerView.findViewHolderForAdapterPosition(holderPosition).itemView;
        ImageView image = view.findViewById(R.id.image_property);
        image.setVisibility(View.VISIBLE);

        LinearLayout linearLayout = view.findViewById(R.id.icon_add_photo);
        linearLayout.setVisibility(View.GONE);

        image.setImageURI(imageUri);

        // Add new empty photo



        /*Glide.with(context)
                .load(new File(String.valueOf(imageUri))) // Uri of the picture
                .into(image);*/

        //adapter.notifyDataSetChanged();


    }

  /*  private void configure_and_show_calendarView(final TextView date_textview, final String type_date) {

        calendarView.setVisibility(View.VISIBLE); // show calendarView

        calendarView.setOnDateChangeListener((new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                if(is_the_date_ok(type_date,year,month,dayOfMonth)){
                    date_textview.setText(create_string_date(year, month, dayOfMonth)); // change date selected into string

                    if(type_date.equals("Begin date")) // update views with the date selected
                        update_calendar(type_date, year, month, dayOfMonth);
                    else
                        update_calendar(type_date, year, month, dayOfMonth);

                    calendarView.setVisibility(View.GONE); // hide calendar view
                }
            }
        }));
    }*/

}
