package com.openclassrooms.realestatemanager.Controllers.Fragments;


import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.CallbackImageChange;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.LiveDataTestUtil;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.Views.ImagesDisplayAdapter;

import java.io.File;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayFragment extends Fragment implements CallbackImageChange {
    @BindView(R.id.type_property) TextView typePropView;
    @BindView(R.id.big_image_display) ImageView mainImageView;
    @BindView(R.id.list_images_property_display) RecyclerView listImagesView;
    @BindView(R.id.price_property) TextView priceView;
    @BindView(R.id.description) TextView descriptionView;
    @BindView(R.id.surface_display) TextView surfaceView;
    @BindView(R.id.nb_rooms_display) TextView roomsView;
    @BindView(R.id.address_display) TextView addressView;
    @BindView(R.id.interest_points_textview) TextView interestView;
    @BindView(R.id.static_mapview_property) ImageView mapStaticView;
    @BindView(R.id.sold_text_rotated) TextView soldText;
    @BindView(R.id.estate_agent_text) TextView estateView;
    @BindView(R.id.publication_date_text) TextView publishView;
    @BindView(R.id.sale_date_text) TextView soldView;
    @BindView(R.id.sold_date_layout) LinearLayout soldDateLayout;
    @BindView(R.id.edittext_contribution) EditText contribution;
    @BindView(R.id.edittext_interest_rate) EditText rate;
    @BindView(R.id.edittext_duration) EditText duration;
    @BindView(R.id.text_result) TextView result;
    private static final String LAST_PROPERTY_SELECTED = "last_property_selected";
    private Property property;
    private List<ImageProperty> listImages;
    private Context context;
    private PropertyDatabase database;
    private View view;
    private MainActivity mainActivity;
    private CallbackImageChange callbackImageChange;
    private LiveData<Property> livedataProp;
    private LiveData<List<ImageProperty>> livedataImg;

    public DisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_display_property, container, false);
        ButterKnife.bind(this,view);

        // Set the variables
        mainActivity = (MainActivity) getActivity();
        this.context= mainActivity.getApplicationContext();
        this.database = PropertyDatabase.getInstance(context);
        listImages = new ArrayList<>();
        callbackImageChange = this;

        // We recover in the bundle the property in json format
        if(getArguments()!=null){

            // Recover id of property to display
            int idProp = getArguments().getInt(LAST_PROPERTY_SELECTED);

            // Recover the property datas

            livedataProp = database.propertyDao().getProperty(idProp);
            livedataImg = database.imageDao().getAllImagesFromProperty(idProp);

            livedataProp.observeForever(PropertyObserver);

        }

        return view;
    }

    final Observer<Property> PropertyObserver = new Observer<Property>() {
        @Override
        public void onChanged(@Nullable final Property propertySQL) {
            if (propertySQL != null)
                property = propertySQL;

            livedataProp.removeObserver(this);

            livedataImg.observeForever(ListImagesObserver);

            // Recover images from property
            //database.imageDao().getAllImagesFromProperty(property.getId()).observeForever(ListImagesObserver);
        }
    };

    @OnClick(R.id.button_calculation)
    public void calculateMonthlyPayment(){

        if(contribution.getText()!=null && rate.getText()!=null && duration.getText()!=null){
            if(!contribution.getText().toString().equals("") && !rate.getText().toString().equals("") && !duration.getText().toString().equals("")){

                Double montlyRate = Utils.calculateMonthlyPayment(property.getPrice(),
                                Double.parseDouble(duration.getText().toString()),
                                Double.parseDouble(rate.getText().toString()),
                                Double.parseDouble(contribution.getText().toString()));

                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String moneyString = context.getResources().getString(R.string.montly_payment) + "\n" + formatter.format(montlyRate);

                result.setText(moneyString);
            }
        }
    }

    final Observer<List<ImageProperty>> ListImagesObserver = new Observer<List<ImageProperty>>() {
        @Override
        public void onChanged(@Nullable final List<ImageProperty> newName) {

            if(listImages!=null){
                if (newName != null) {
                    if(newName.size() != 0){
                        listImages.addAll(newName);
                    }
                }

                //ImageProperty emptyImageProperty = new ImageProperty();
                //emptyImageProperty.setIdProperty(property.getId());
                //listImages.add(emptyImageProperty);

                configureImagesProperty();
            }

            livedataImg.removeObserver(this);

            // Configuration views
            configureViews();
        }
    };

    private void configureViews() {

        // set the main image
        if(property.getMainImagePath()!=null)
            Utils.setImageBitmapInView(property.getMainImagePath(), mainImageView, mainActivity);

        // set the type of property
        String typeProp = property.getType();
        typePropView.setText(typeProp);

        // set the description text
        descriptionView.setText(property.getDescription());

        // Remove textview "sold" if property is not sold
        if(!property.getSold())
            soldDateLayout.setVisibility(View.GONE);
        else {
            soldDateLayout.setVisibility(View.VISIBLE);
            String soldDate = property.getDateSold();
            publishView.setText(soldDate);
        }

        // set the price
        String price = property.getPrice().toString() + " $";
        priceView.setText(price);

        // set the surface
        String surface = property.getSurface() + "m²";
        surfaceView.setText(surface);

        // set the number of rooms
        String roomNb = String.valueOf(property.getRoomNumber());
        roomsView.setText(roomNb);

        // set estate agent
        String estateAgent = property.getEstateAgent();
        estateView.setText(estateAgent);

        // set publish date
        String publishDate = property.getDateStart();
        publishView.setText(publishDate);

        // set the address
        String address = property.getAddress();
        addressView.setText(address);

        // set the points of interest
        String points = property.getInterestPoints();
        interestView.setText(points);

        // set static mapView
        if(property.getMap()!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(property.getMap(), 0, property.getMap().length);
            mapStaticView.setImageBitmap(bitmap);
        }
    }

    private void configureImagesProperty(){

        if(context!=null){

            // Set the recyclerView in horizontal direction
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            // Create adapter passing in the sample user data
            ImagesDisplayAdapter adapter = new ImagesDisplayAdapter(listImages, context, callbackImageChange, mainActivity);

            // Attach the adapter to the recyclerview to populate items
            listImagesView.setAdapter(adapter);
            // Set layout manager to position the items
            listImagesView.setLayoutManager(layoutManager);
        }
    }

    public void changeMainImage(int position){

        if(listImages.get(position)!=null){

            Utils.setImageBitmapInView(listImages.get(position).getImagePath(),mainImageView,mainActivity);

            /*try {
                Bitmap bitmap;
                File f= new File(listImages.get(position).getImagePath());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                if (EasyPermissions.hasPermissions(mainActivity, galleryPermissions)) {

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),options);
                    mainImageView.setImageBitmap(bitmap);

                } else {
                    EasyPermissions.requestPermissions(this, "Access for storage",
                            101, galleryPermissions);
                }

            } catch (Exception e) {
                System.out.println("eee exception = " + e.toString());
            }*/
        }
    }
}
