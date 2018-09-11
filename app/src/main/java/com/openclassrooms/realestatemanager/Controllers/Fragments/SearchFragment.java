package com.openclassrooms.realestatemanager.Controllers.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.SearchActivity;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.Provider.SearchContentProvider;
import com.openclassrooms.realestatemanager.Models.SearchAddress;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    @BindView(R.id.switch_sold_search) Switch soldView;
    @BindView(R.id.list_type_properties_search) Spinner typePropView;
    @BindView(R.id.start_date_publish_selected_search) TextView startPublishView;
    @BindView(R.id.end_date_publish_selected_search) TextView endPublishView;
    @BindView(R.id.price_inf_search) EditText priceInfView;
    @BindView(R.id.price_sup_search) EditText priceSupView;
    @BindView(R.id.surface_inf_search) EditText surfaceInfView;
    @BindView(R.id.surface_sup_search) EditText surfaceSupView;
    @BindView(R.id.address_edit_text_search) SearchView locationView;
    @BindView(R.id.relativelayout_rooms) RelativeLayout layoutRooms;
    @BindView(R.id.calendar_search) CalendarView calendarView;
    @BindView(R.id.linearlayout_dates_search) LinearLayout layoutDates;
    @BindView(R.id.buttonSearchCancel) Button buttonCancel;
    @BindView(R.id.buttonSearch) Button buttonSearch;
    private static final String MODE_SEARCH = "mode_search";
    private ImageButton buttonPlus;
    private ImageButton buttonLess;
    private int roomNbMin;
    private List<Property> listProperties;
    private BaseActivity baseActivity;
    private SearchActivity searchActivity;
    private TextView nbRoomsView;
    private Context context;
    private LatLng searchLoc;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this,view);

        baseActivity = (BaseActivity) getActivity();
        searchActivity= (SearchActivity) getActivity();
        if(baseActivity!=null)
            this.context = baseActivity.getApplicationContext();

        configureSearchFragment();
        return view;
    }

    @SuppressLint("CutPasteId")
    private void configureSearchFragment(){

        roomNbMin = 0;
        nbRoomsView = layoutRooms.findViewById(R.id.room_number_selector_search).findViewById(R.id.text_selection);
        buttonPlus = layoutRooms.findViewById(R.id.room_number_selector_search).findViewById(R.id.plus_button);
        buttonLess = layoutRooms.findViewById(R.id.room_number_selector_search).findViewById(R.id.less_button);

        buttonLess.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        buttonPlus.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        nbRoomsView.setText(getResources().getString(R.string.any));

        configureOnClickListenersDatesSelector();
        new SearchAddress(this,context);
    }

    private void configureOnClickListenersDatesSelector() {

        calendarView.setVisibility(View.GONE); // hide calendarView

        layoutDates.findViewById(R.id.start_relativelayout_publish_search)
                .setOnClickListener(v -> {
                    TextView dateText = baseActivity.findViewById(R.id.start_date_publish_selected_search);

                    if(calendarView.getVisibility()==View.GONE){
                        calendarView.setVisibility(View.VISIBLE);
                        configureCalendarView(dateText);
                    } else {
                        calendarView.setVisibility(View.GONE);
                    }
                });

        layoutDates.findViewById(R.id.end_relativelayout_publish_search)
                .setOnClickListener(v -> {
                    TextView dateText = baseActivity.findViewById(R.id.end_date_publish_selected_search);

                    if(calendarView.getVisibility()==View.GONE){
                        calendarView.setVisibility(View.VISIBLE);
                        configureCalendarView(dateText);
                    } else {
                        calendarView.setVisibility(View.GONE);
                    }
                });
    }

    private void configureCalendarView(final TextView dateTextview) {

        calendarView.setVisibility(View.VISIBLE); // show calendarView

        calendarView.setOnDateChangeListener(((view, year, month, dayOfMonth) -> {
            String dateText = Utils.create_string_date(year, month, dayOfMonth);
            dateTextview.setText(dateText); // change date selected into string
            calendarView.setVisibility(View.GONE); // hide calendar view
        }));
    }

    @OnClick(R.id.plus_button)
    public void onClickListenerButtonPlus() {
        if(roomNbMin<10) {
            roomNbMin++;
            if(roomNbMin==0)
                nbRoomsView.setText(getResources().getString(R.string.any));
            else {
                String text = "+" + String.valueOf(roomNbMin);
                nbRoomsView.setText(text);
            }
        }
        buttonPlus.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        new Handler().postDelayed(() -> buttonPlus.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN), 100);
    }

    @OnClick(R.id.less_button)
    public void onClickListenerButtonLess() {
        if(roomNbMin>=1) {
            roomNbMin--;
            if(roomNbMin==0)
                nbRoomsView.setText(getResources().getString(R.string.any));
            else {
                String text = "+" + String.valueOf(roomNbMin);
                nbRoomsView.setText(text);
            }
        }

        buttonLess.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        new Handler().postDelayed(() -> buttonLess.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN), 100);
    }

    @OnClick(R.id.buttonSearchCancel)
    public void cancel(){
        searchActivity.launchMainActivity();
    }

    @OnClick(R.id.buttonSearch)
    public void search(){
        launchSearchProperties();
    }

    private int getSold(){
        if(soldView.isChecked())
            return 1;
        else
            return 0;
    }

    private Double getSurfaceInf(){

        if(surfaceInfView.getText()!=null){
            if(surfaceInfView.getText().toString().length()==0) { // if no text written
                return 0d;
            } else
                return Double.parseDouble(surfaceInfView.getText().toString());
        } else
            return 0d;
    }

    private Double getSurfaceSup(){

        if(surfaceSupView.getText()!=null){
            if(surfaceSupView.getText().toString().length()==0) { // if no text written
                return 999d;
            } else
                return Double.parseDouble(surfaceSupView.getText().toString());
        } else
            return 999d;
    }

    private Double getPriceInf(){
        if(priceInfView.getText()!=null){
            if(priceInfView.getText().toString().length()==0) { // if no text written
                return 0d;
            } else
                return Double.parseDouble(priceInfView.getText().toString());
        } else
            return 0d;
    }

    private Double getPriceSup(){
        if(priceSupView.getText()!=null){
            if(priceSupView.getText().toString().length()==0) { // if no text written
                return 9999999d;
            } else
                return Double.parseDouble(priceSupView.getText().toString());
        } else
            return 9999999d;
    }

    private String getTypeProperty(){
        return typePropView.getSelectedItem().toString();
    }

    private String getDateInf(){
        if(startPublishView.getText()!=null){
            if(startPublishView.getText().toString().length()==0) // if no text written
                return "01/01/2000";
            else
                return startPublishView.getText().toString();
        } else
            return "01/01/2000";
    }

    private String getDateSup(){
        if(endPublishView.getText()!=null){
            if(endPublishView.getText().toString().length()==0) // if no text written
                return "31/12/9999";
            else
                return endPublishView.getText().toString();
        } else
            return "31/12/9999";
    }

    private void launchSearchProperties(){

        SearchContentProvider searchContentProvider = new SearchContentProvider();
        searchContentProvider.setParametersQuery(context, getSold(),getSurfaceInf(),getSurfaceSup(),getPriceInf(),getPriceSup(),roomNbMin,getTypeProperty());
        List<Property> listPropertyTemp = new ArrayList<>();

        final Cursor cursor = searchContentProvider.query(null, null, null, null, null);

        if (cursor != null){
            if(cursor.getCount() >0){
                while (cursor.moveToNext()) {
                    listPropertyTemp.add(Property.getPropertyFromCursor(cursor));
                }
            }
            cursor.close();
        }

        filterResultsByDatePublish(listPropertyTemp);
    }

    private void filterResultsByDatePublish(List<Property> listPropertyTemp){

        String dateinf = getDateInf();
        String datesup = getDateSup();
        List<Property> listPropertiesTemp = new ArrayList<>();

        if(listPropertyTemp.size()>0){

            for(Property property : listPropertyTemp){
                if(property!=null){
                    if(property.getDateStart()!=null){
                        if(Utils.isDateInsidePeriod(property.getDateStart(),dateinf,datesup))
                            listPropertiesTemp.add(property);
                    }
                }
            }
        }

        filterResultsByLocation(listPropertiesTemp);
    }

    private void filterResultsByLocation(List<Property> listPropertyTemp){

        Double radius = Double.parseDouble(context.getResources().getString(R.string.radius));
        listProperties = new ArrayList<>();

        if(searchLoc!=null){

            if(listPropertyTemp.size()>0){

                for(Property property : listPropertyTemp){
                    if(property!=null){
                        if(property.getLat()!=0 && property.getLng()!=0){

                            LatLng propertyLoc = new LatLng(property.getLat(),property.getLng());

                            if(Utils.isLocationInsideBounds(searchLoc,propertyLoc,radius))
                                listProperties.add(property);
                        }
                    }
                }
            }

        } else { // if the user has not chosen any specific location
            listProperties.addAll(listPropertyTemp);
        }

        displayResults();
    }

    public void setLatLngAddress(LatLng latLng) {
        searchLoc = latLng;

        // Enable button save and cancel
        baseActivity.runOnUiThread(() -> {
            buttonSearch.setEnabled(true);
            buttonCancel.setEnabled(true);
        });
    }

    private void displayResults(){
        if(listProperties.size()>0) // if at least one result, show list properties
            baseActivity.configureAndShowListPropertiesFragment(MODE_SEARCH, listProperties);
        else
            baseActivity.displayError(context.getResources().getString(R.string.no_result_found));
    }

    public SearchView getLocationView() {
        return locationView;
    }

    public Button getButtonCancel() {
        return buttonCancel;
    }

    public Button getButtonSearch() {
        return buttonSearch;
    }


}
