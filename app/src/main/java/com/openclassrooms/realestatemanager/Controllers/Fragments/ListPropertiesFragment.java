package com.openclassrooms.realestatemanager.Controllers.Fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.CallbackListProperties;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.Models.Provider.ImageContentProvider;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.Views.PropertiesRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ListPropertiesFragment extends Fragment implements CallbackListProperties {

    private List<Property> listProperties;
    private CallbackListProperties callbackListProperties;
    private MainActivity mainActivity;
    private PropertyDatabase database;
    private View view;
    private LiveData<List<Property>> liveData;
    private ContentResolver mContentResolver;
    private List<String> URIimages;

    public ListPropertiesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackListProperties = this;
        mainActivity = (MainActivity) getActivity();
        this.database = PropertyDatabase.getInstance(mainActivity.getApplicationContext());
        listProperties = new ArrayList<>();

        PropertyContentProvider propertyContentProvider = new PropertyContentProvider(mainActivity.getApplicationContext());
        URIimages = new ArrayList<>();

        //ContentUris.withAppendedId(ImageContentProvider.URI_ITEM, 0)
        /*ImageView imageView = mainActivity.findViewById(R.id.image_test);

        final Cursor cursor = propertyContentProvider.query(null, null, null, null, null);

        if (cursor != null){
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()) {

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(mainActivity.getApplicationContext().getContentResolver(), Uri.parse(cursor.getString(cursor.getColumnIndex("mainImageURI"))));
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }
            }
            cursor.close();
        }

        System.out.println("eee LIST URI = " + URIimages.toString());*/

        liveData=database.propertyDao().getAllProperties();
        liveData.observeForever(ListPropertiesObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.property_item_list, container, false);
        return view;
    }

    private void configureListProperties(){

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            if(context!=null){

                // Create adapter passing in the sample user data
                PropertiesRecyclerViewAdapter adapter = new PropertiesRecyclerViewAdapter(listProperties,context, callbackListProperties, mainActivity);
                // Attach the adapter to the recyclerview to populate items
                recyclerView.setAdapter(adapter);
                // Set layout manager to position the items
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        }
    }

    @Override
    public void showDisplayFragment(int position) {
        mainActivity.configureAndShowDisplayFragment(listProperties.get(position));
    }

    final Observer<List<Property>> ListPropertiesObserver = new Observer<List<Property>>() {
        @Override
        public void onChanged(@Nullable final List<Property> newName) {
            if(newName!=null)
                listProperties.addAll(newName);

            liveData.removeObserver(this);
            configureListProperties();
            mainActivity.setListProperties(listProperties);
        }
    };
}
