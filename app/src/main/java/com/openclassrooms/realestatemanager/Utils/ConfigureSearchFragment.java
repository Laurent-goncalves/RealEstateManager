package com.openclassrooms.realestatemanager.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.openclassrooms.realestatemanager.Controllers.Fragments.SearchFragment;
import com.openclassrooms.realestatemanager.Models.CalendarDialog;
import com.openclassrooms.realestatemanager.Models.SearchAddress;
import com.openclassrooms.realestatemanager.Models.SearchQuery;
import com.openclassrooms.realestatemanager.R;
import java.util.Arrays;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;


public class ConfigureSearchFragment {

    @BindView(R.id.address_edit_text_search) android.support.v7.widget.SearchView locationView;
    @BindView(R.id.switch_sold_search) Switch soldSwitch;
    @BindView(R.id.list_type_properties_search) Spinner listPropTypes;
    @BindView(R.id.price_inf_search) EditText priceInfView;
    @BindView(R.id.price_sup_search) EditText priceSupView;
    @BindView(R.id.surface_inf_search) EditText surfaceInfView;
    @BindView(R.id.surface_sup_search) EditText surfaceSupView;
    @BindView(R.id.seekbar_radius) SeekBar seekbarRadius;
    @BindView(R.id.radius_value) TextView radiusView;
    @BindView(R.id.start_date_publish_selected_search) TextView startPublishView;
    @BindView(R.id.end_date_publish_selected_search) TextView endPublishView;
    @BindView(R.id.start_icon_expand_search) ImageButton iconExpandStart;
    @BindView(R.id.end_icon_expand_search) ImageButton iconExpandEnd;
    @BindView(R.id.relativelayout_rooms) RelativeLayout layoutRooms;
    @BindView(R.id.buttonSearch) Button buttonSearch;
    @BindView(R.id.buttonSearchCancel) Button buttonCancel;
    private Context context;
    private SearchFragment searchFragment;
    private static final String PUBLISH_DATE_START = "publish_date_start";
    private static final String PUBLISH_DATE_END = "publish_date_end";
    private ImageButton buttonPlus;
    private ImageButton buttonLess;
    private SearchQuery query;
    private TextView nbRoomsView;


    public ConfigureSearchFragment(View view, Context context, SearchFragment searchFragment) {
        ButterKnife.bind(this, view);
        this.context = context;
        this.searchFragment = searchFragment;
        this.query = searchFragment.getSearchQuery();
        configureSearchFragment();
    }

    // ----------------------------------------------------------------------------------------------------------
    // ------------------------------------ CONFIGURATION SEARCHFRAGMENT ----------------------------------------
    // ----------------------------------------------------------------------------------------------------------

    public void configureSearchFragment(){

        configureDateSelector();

        // configure switch sold
        if(query.getSoldStatus()==null)
            soldSwitch.setChecked(false);
        else
            soldSwitch.setChecked(query.getSoldStatus());

        // configure type property
        if(query.getTypeProperty()==null)
            listPropTypes.setSelection(0);
        else
            listPropTypes.setSelection(Utils.getIndexFromList(query.getTypeProperty(), Arrays.asList(context.getResources().getStringArray(R.array.type_property))));

        // configure publication date start
        startPublishView.setText(query.getDatePublishStart());

        // configure publication date end
        endPublishView.setText(query.getDatePublishEnd());

        // configure price inf
        if(query.getPriceInf()!=0)
            priceInfView.setText(String.valueOf(query.getPriceInf()));
        else
            priceInfView.setText(null);

        // configure price sup
        if(query.getPriceSup()!=0)
            priceSupView.setText(String.valueOf(query.getPriceSup()));
        else
            priceSupView.setText(null);

        // configure surface inf
        if(query.getSurfaceInf()!=0)
            surfaceInfView.setText(String.valueOf(query.getSurfaceInf()));
        else
            surfaceInfView.setText(null);

        // configure surface sup
        if(query.getSurfaceSup()!=0)
            surfaceSupView.setText(String.valueOf(query.getSurfaceSup()));
        else
            surfaceSupView.setText(null);

        // configure room number
        configureRoomNumberView();

        // configure address
        new SearchAddress(searchFragment,context);
        SearchView.SearchAutoComplete searchAutoComplete = locationView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setText(query.getAddress());

        // configure radius
        configureSeekBarForRadius();

        // Configure edittext and button to hide keyboard when clicking outside view
        configureViewsToHideKeyboard();

        // Initialize searchQuery
        if(searchFragment.getBaseActivityListener()!=null)
            searchFragment.getBaseActivityListener().setSearchQuery(null);
    }

    private void configureRoomNumberView(){
        LinearLayout buttonSelect = layoutRooms.findViewById(R.id.room_number_selector_search);
        nbRoomsView = buttonSelect.findViewById(R.id.text_selection);
        buttonPlus = buttonSelect.findViewById(R.id.plus_button);
        buttonLess = buttonSelect.findViewById(R.id.less_button);

        buttonLess.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        buttonPlus.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        nbRoomsView.setText(context.getResources().getString(R.string.any));
        updateRoomNumber();
    }

    private void configureDateSelector(){

        startPublishView.setOnClickListener(v -> showCalendarView(PUBLISH_DATE_START));
        iconExpandStart.setOnClickListener(v -> showCalendarView(PUBLISH_DATE_START));

        endPublishView.setOnClickListener(v -> showCalendarView(PUBLISH_DATE_END));
        iconExpandEnd.setOnClickListener(v -> showCalendarView(PUBLISH_DATE_END));
    }

    private void showCalendarView(String dateType){

        FragmentTransaction ft = searchFragment.getFragmentManager().beginTransaction();
        Fragment prev = searchFragment.getFragmentManager().findFragmentByTag("calendarDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        CalendarDialog calendarDialog = CalendarDialog.newInstance(dateType);
        calendarDialog.setTargetFragment(searchFragment,0);
        calendarDialog.show(ft, "calendarDialog");
    }

    private void configureSeekBarForRadius(){

        // Initialize seekbar (value = 1000m)
        String text = String.valueOf(query.getRadius()) + " m";
        seekbarRadius.setProgress(query.getRadius());
        radiusView.setText(text);

        // set on change listener
        seekbarRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = String.valueOf(progress) + " m";
                query.setRadius(progress);
                radiusView.setText(text);
                searchFragment.getSearchQuery().setRadius(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void updateRoomNumber(){
        if(query.getRoomNbMin()==0)
            nbRoomsView.setText(context.getResources().getString(R.string.any));
        else {
            String text = "+" + String.valueOf(query.getRoomNbMin());
            nbRoomsView.setText(text);
        }
        searchFragment.getSearchQuery().setRoomNbMin(query.getRoomNbMin());
    }

    private void configureViewsToHideKeyboard(){

        locationView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
        priceInfView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
        priceSupView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
        surfaceInfView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
        surfaceSupView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
        buttonSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                hideKeyboard(v);
        });
        buttonCancel.setOnFocusChangeListener((v, hasFocus) -> {
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
    // ---------------------------------- LISTENERS FOR UPDATING SEARCH QUERY -----------------------------------
    // ----------------------------------------------------------------------------------------------------------

    @OnClick(R.id.switch_sold_search)
    public void onClickSwitchSold(){
        searchFragment.getSearchQuery().setSoldStatus(soldSwitch.isChecked());
    }

    @OnItemSelected(R.id.list_type_properties_search)
    public void onTypePropertySelected(){
        searchFragment.getSearchQuery().setTypeProperty(listPropTypes.getSelectedItem().toString());
    }

    @OnTextChanged(R.id.price_inf_search)
    public void onPriceInfChanged(){
        if(priceInfView.getText().toString().length()==0)
            searchFragment.getSearchQuery().setPriceInf(0d);
        else
            searchFragment.getSearchQuery().setPriceInf(Double.parseDouble(priceInfView.getText().toString()));
    }

    @OnTextChanged(R.id.price_sup_search)
    public void onPriceSupChanged(){
        if(priceSupView.getText().toString().length()==0)
            searchFragment.getSearchQuery().setPriceSup(0d);
        else
            searchFragment.getSearchQuery().setPriceSup(Double.parseDouble(priceSupView.getText().toString()));
    }

    @OnTextChanged(R.id.surface_inf_search)
    public void onSurfaceInfChanged(){
        if(surfaceInfView.getText().toString().length()==0)
            searchFragment.getSearchQuery().setSurfaceInf(0d);
        else
            searchFragment.getSearchQuery().setSurfaceInf(Double.parseDouble(surfaceInfView.getText().toString()));
    }

    @OnTextChanged(R.id.surface_sup_search)
    public void onSurfaceSupChanged(){
        if(surfaceSupView.getText().toString().length()==0)
            searchFragment.getSearchQuery().setSurfaceSup(0d);
        else
            searchFragment.getSearchQuery().setSurfaceSup(Double.parseDouble(surfaceSupView.getText().toString()));
    }

    @OnClick(R.id.plus_button)
    public void onClickListenerButtonPlus() {
        if(query.getRoomNbMin()<10) {
            int roomNb = query.getRoomNbMin();
            roomNb++;
            query.setRoomNbMin(roomNb);
            updateRoomNumber();
        }

        changeColorButton(100,buttonPlus);
    }

    @OnClick(R.id.less_button)
    public void onClickListenerButtonLess() {
        if(query.getRoomNbMin()>=1) {
            int roomNb = query.getRoomNbMin();
            roomNb--;
            query.setRoomNbMin(roomNb);
            updateRoomNumber();
        }
        changeColorButton(101,buttonLess);
    }

    private void changeColorButton(int duration, ImageButton button){
        button.setColorFilter(context.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        new Handler().postDelayed(() -> button.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN), duration);
    }

    @OnClick(R.id.buttonReset)
    public void reset(){
        if(searchFragment.getBaseActivityListener()!=null)
            UtilsBaseActivity.askForConfirmationToResetSearchQuery(searchFragment.getBaseActivityListener().getBaseActivity());
    }

    @OnClick(R.id.buttonSearchCancel)
    public void cancel(){
        searchFragment.stopActivity();
    }

    @OnClick(R.id.buttonSearch)
    public void search(){
        if(searchFragment.getBaseActivityListener()!=null)
            searchFragment.getBaseActivityListener().setSearchQuery(searchFragment.getSearchQuery());
        searchFragment.launchSearchProperties();
    }

    public void setQuery(SearchQuery query) {
        this.query = query;
    }
}
