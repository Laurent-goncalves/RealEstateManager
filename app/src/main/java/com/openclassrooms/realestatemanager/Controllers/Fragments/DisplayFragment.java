package com.openclassrooms.realestatemanager.Controllers.Fragments;


import android.arch.lifecycle.Observer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.CallbackImageChange;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Views.ImagesDisplayAdapter;
import java.lang.reflect.Type;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayFragment extends Fragment implements CallbackImageChange {

    @BindView(R.id.big_image_display) ImageView mainImageView;
    @BindView(R.id.list_images_property_display) RecyclerView listImagesView;
    @BindView(R.id.price_property) TextView priceView;
    @BindView(R.id.description) TextView descriptionView;
    @BindView(R.id.surface_display) TextView surfaceView;
    @BindView(R.id.nb_rooms_display) TextView roomsView;
    @BindView(R.id.address_display) TextView addressView;
    @BindView(R.id.interest_points_textview) TextView interestView;
    @BindView(R.id.static_mapview_property) ImageView mapStaticView;
    private static final String PROPERTY_JSON = "property_json";
    private Property property;
    private List<ImageProperty> listImages;
    private Context context;
    private PropertyDatabase database;
    private View view;
    private MainActivity mainActivity;
    private CallbackImageChange callbackImageChange;

    public DisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_display_property, container, false);
        ButterKnife.bind(this,view);

        // Set the mainActivity
        mainActivity = (MainActivity) getActivity();
        this.context= mainActivity.getApplicationContext();

        // We recover in the bundle the property in json format
        if(getArguments()!=null){

            // Recover the property datas
            Gson gson = new Gson();
            String propJSON = getArguments().getString(PROPERTY_JSON,null);
            Type propertyType = new TypeToken<Property>(){}.getType();
            property = gson.fromJson(propJSON,propertyType);
        }

        // Recover list of imagesProperty from database and configure images inside recyclerView
        this.database = PropertyDatabase.getInstance(context);
        database.imageDao().getAllImages().observeForever(ListImagesObserver);

        callbackImageChange = this;

        configureViews();

        return view;
    }

    private void configureViews() {

        // set the description text
        descriptionView.setText(property.getDescription());

        // set the price
        String price = property.getPrice().toString() + " $";
        priceView.setText(price);

        // set the surface
        String surface = property.getSurface() + "m²";
        surfaceView.setText(surface);

        // set the number of rooms
        roomsView.setText(property.getRoomNumber());

        // set the address
        String address = property.getAddress();
        addressView.setText(address);

        // set the points of interest
        String points = property.getInterestPoints();
        interestView.setText(points);

        // set static mapView
        Bitmap bitmap = BitmapFactory.decodeByteArray(property.getMap(), 0, property.getMap().length);
        mapStaticView.setImageBitmap(bitmap);
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

    private void configureImagesProperty(){

        if(context!=null){

            // Set the recyclerView in horizontal direction
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            // Create adapter passing in the sample user data
            ImagesDisplayAdapter adapter = new ImagesDisplayAdapter(listImages, context, callbackImageChange);

            // Attach the adapter to the recyclerview to populate items
            listImagesView.setAdapter(adapter);
            // Set layout manager to position the items
            listImagesView.setLayoutManager(layoutManager);
        }
    }

    public void changeMainImage(int position){
        mainImageView.setImageURI(listImages.get(position).getImage());
    }

}
