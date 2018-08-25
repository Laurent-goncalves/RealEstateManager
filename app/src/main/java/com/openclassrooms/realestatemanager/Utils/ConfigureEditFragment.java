package com.openclassrooms.realestatemanager.Utils;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.Models.SearchAddress;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Views.ImagesEditAdapter;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class ConfigureEditFragment {

    private EditFragment editFragment;
    private Context context;
    private Property propertyInit;
    private ImagesEditAdapter adapter;
    private RecyclerView recyclerView;
    private CalendarView calendarView;
    private List<ImageProperty> listImages;
    private LiveData<List<ImageProperty>> imagesLiveData;
    private TextView dateText;
    private TextView dateSold;

    public ConfigureEditFragment(EditFragment editFragment, Context context, PropertyDatabase database) {
        this.editFragment = editFragment;
        this.context=context;
        propertyInit = editFragment.getPropertyInit();
        listImages = editFragment.getListImages();

        // Recover list of imagesProperty from database and configure images inside recyclerView
        imagesLiveData = database.imageDao().getAllImagesFromProperty(propertyInit.getId());
        imagesLiveData.observeForever(ListImagesObserver);

        configureAllAreas();
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

    final Observer<List<ImageProperty>> ListImagesObserver = new Observer<List<ImageProperty>>() {
        @Override
        public void onChanged(@Nullable final List<ImageProperty> newName) {
            if (newName != null) {
                if(newName.size() != 0){
                    listImages.addAll(newName);
                }
            }

            imagesLiveData.removeObserver(this);
            listImages.add(new ImageProperty()); // Add item for "add a photo"
            editFragment.setListImages(listImages);
            configureImagesProperty();
        }
    };

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
            editFragment.datePublish.setText(propertyInit.getDateSold());

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
        }

        editFragment.buttonPlus.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        editFragment.buttonLess.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    }

    private void configureMainImage(){

        if(propertyInit.getMainImagePath()!=null) {
            Utils.setImageBitmapInView(propertyInit.getMainImagePath(),editFragment.mainImage,editFragment.getMainActivity());
            /*try {
                Bitmap bitmap;
                File f= new File(propertyInit.getMainImagePath());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),options);
                editFragment.mainImage.setImageBitmap(bitmap);


            } catch (Exception e) {
                System.out.println("eee exception = " + e.toString());
            }*/
        }
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

