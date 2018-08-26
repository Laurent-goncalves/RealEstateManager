package com.openclassrooms.realestatemanager.Controllers.Fragments;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.CallbackImageChange;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.Models.Provider.ImageContentProvider;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.Views.ImagesDisplayAdapter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
    private ImagesDisplayAdapter adapter;

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
            recoverProperty(idProp);

            // Recover images from the property
            recoverImagesProperty(idProp);
        }

        configureViews();
        configureImagesProperty();

        return view;
    }

    private void recoverProperty(int idProp){

        PropertyContentProvider propertyContentProvider = new PropertyContentProvider();
        propertyContentProvider.setUtils(context,false);

        final Cursor cursor = propertyContentProvider.query(ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, idProp), null, null, null, null);

        if (cursor != null){
            if(cursor.getCount() >0){
                while (cursor.moveToNext()) {
                    property=Property.getPropertyFromCursor(cursor);
                }
            }
            cursor.close();
        }
    }

    private void recoverImagesProperty(int idProp){

        // mainImage à mettre dans la liste
        listImages.add(new ImageProperty(0, property.getMainImagePath(), null, property.getId()));

        // Recover images with Content Provider
        ImageContentProvider imageContentProvider = new ImageContentProvider();
        imageContentProvider.setUtils(context);

        final Cursor cursor = imageContentProvider.query(ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, idProp), null, null, null, null);

        if (cursor != null){
            if(cursor.getCount() >0){
                while (cursor.moveToNext()) {
                    listImages.add(ImageProperty.getImagePropertyFromCursor(cursor));
                }
            }
            cursor.close();
        }

        if(listImages.size()==1) // if there is only the mainImage in the list, hide the recyclerView
            listImagesView.setVisibility(View.GONE);
    }

    @OnClick(R.id.buttonReturnToList)
    public void returnToList(){
        mainActivity.configureAndShowListPropertiesFragment();
    }

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
        if(!property.getSold()) {
            soldDateLayout.setVisibility(View.GONE);
            soldText.setVisibility(View.GONE);
        } else {
            soldDateLayout.setVisibility(View.VISIBLE);
            String soldDate = property.getDateSold();
            soldView.setText(soldDate);
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
        String points = Utils.removeHooksFromString(property.getInterestPoints());
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
            adapter = new ImagesDisplayAdapter(listImages, context, callbackImageChange, mainActivity);

            // Attach the adapter to the recyclerview to populate items
            listImagesView.setAdapter(adapter);
            // Set layout manager to position the items
            listImagesView.setLayoutManager(layoutManager);
        }
    }

    public void changeMainImage(int position){
        if(listImages.get(position)!=null){
            Utils.setImageBitmapInView(listImages.get(position).getImagePath(),mainImageView,mainActivity);
        }
    }
}
