package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.Models.SearchAddress;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Views.ImagesEditAdapter;
import java.util.List;



public class ConfigureEditFragment {

    private EditFragment editFragment;
    private Context context;
    private Property propertyInit;
    private ImagesEditAdapter adapter;
    private RecyclerView recyclerView;
    private CalendarView calendarView;
    private List<ImageProperty> listImages;


    public ConfigureEditFragment(EditFragment editFragment, Context context) {
        this.editFragment = editFragment;
        this.context=context;
        propertyInit = editFragment.getProperty();
        listImages = editFragment.getListImages();
        configureAllAreas();
        configureImagesProperty();
    }

    private void configureOnClickListenersDatesSelector() {

        calendarView = editFragment.getCalendarView();
        calendarView.setVisibility(View.GONE); // hide calendarView

        editFragment.linearLayoutDates.findViewById(R.id.publishing_date_selector)
                .findViewById(R.id.relativelayout_publish)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView dateText = editFragment.linearLayoutDates
                        .findViewById(R.id.publishing_date_selector)
                        .findViewById(R.id.relativelayout_publish)
                        .findViewById(R.id.date_publish_selected);

                if(calendarView.getVisibility()==View.GONE){
                    calendarView.setVisibility(View.VISIBLE);
                    configureCalendarView(dateText);
                } else {
                    calendarView.setVisibility(View.GONE);
                }
            }
        });

        editFragment.linearLayoutDates.findViewById(R.id.selling_date_selector)
                .findViewById(R.id.relativelayout_sold)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView dateText = editFragment.linearLayoutDates
                        .findViewById(R.id.selling_date_selector)
                        .findViewById(R.id.relativelayout_sold)
                        .findViewById(R.id.date_sale_selected);

                if(calendarView.getVisibility()==View.GONE){
                    calendarView.setVisibility(View.VISIBLE);
                    configureCalendarView(dateText);
                } else {
                    calendarView.setVisibility(View.GONE);
                }
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

    private void configureAllAreas(){

        if(propertyInit!=null){

            // configure mainImage
            configureMainImage();

            // configure switch (sold ?)
            configureSwitchSold();

            // configure price
            editFragment.priceEdit.setText(String.valueOf(propertyInit.getPrice()));

            // configure estate agent
            editFragment.estateAgentEdit.setText(propertyInit.getEstateAgent());

            // configure date publication
            editFragment.datePublish = editFragment.linearLayoutDates.findViewById(R.id.publishing_date_selector).findViewById(R.id.date_publish_selected);
            editFragment.datePublish.setText(propertyInit.getDateStart());

            // configure date sold
            editFragment.dateSold = editFragment.linearLayoutDates.findViewById(R.id.selling_date_selector).findViewById(R.id.date_sale_selected);
            editFragment.dateSold.setText(propertyInit.getDateSold());

            configureOnClickListenersDatesSelector();

            // configure surface
            editFragment.surfaceEdit.setText(String.valueOf(propertyInit.getSurface()));

            // configure number of rooms
            editFragment.nbRooms = editFragment.relativeLayoutNbRooms.findViewById(R.id.room_number_selector).findViewById(R.id.text_selection);
            editFragment.nbRooms.setText(String.valueOf(propertyInit.getRoomNumber()));

            // configure address
            new SearchAddress(editFragment, propertyInit, context);

            // configure description
            editFragment.descriptionEdit.setText(propertyInit.getDescription());

            // configure static map
            if(propertyInit.getMap()!=null)
                editFragment.setStaticMap(BitmapFactory.decodeByteArray(propertyInit.getMap(),0, propertyInit.getMap().length));

            // configure interest points
            editFragment.interestView.setText(propertyInit.getInterestPoints());
            editFragment.setInterestPoints(propertyInit.getInterestPoints());
        }

        editFragment.buttonPlus.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        editFragment.buttonLess.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    }

    private void configureMainImage(){
        Utils.setImageBitmapInView(propertyInit.getMainImagePath(), editFragment.mainImage, editFragment.getMainActivity());
        editFragment.setMainImagePath(propertyInit.getMainImagePath());
    }

    private void configureImagesProperty(){

        if(context!=null){

            // Set the recyclerView in horizontal direction
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            // Create adapter passing in the sample user data
            adapter=editFragment.getAdapter();
            adapter = new ImagesEditAdapter(listImages,propertyInit,context, editFragment.getCallbackImageSelect(), editFragment.getMainActivity());
            editFragment.setAdapter(adapter);

            // Attach the adapter to the recyclerview to populate items
            recyclerView = editFragment.getRecyclerView();
            recyclerView.setAdapter(adapter);

            // Set layout manager to position the items
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    private void configureSwitchSold(){
        if(propertyInit.getSold())
            editFragment.switchSold.setChecked(true);
        else
            editFragment.switchSold.setChecked(false);
    }
}

