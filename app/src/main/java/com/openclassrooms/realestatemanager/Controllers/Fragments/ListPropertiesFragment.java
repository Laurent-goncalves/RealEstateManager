package com.openclassrooms.realestatemanager.Controllers.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Models.CallbackListProperties;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.Views.PropertiesRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ListPropertiesFragment extends Fragment implements CallbackListProperties {

    private List<Property> listProperties;
    private CallbackListProperties callbackListProperties;
    private BaseActivity baseActivity;
    private View view;
    private static final String MODE_SEARCH = "mode_search";
    private static final String MODE_DISPLAY = "mode_display";
    private static final String MODE_SELECTED = "mode_selected";
    private static final String LIST_PROPERTIES_JSON = "list_properties_json";

    public ListPropertiesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackListProperties = this;
        baseActivity = (BaseActivity) getActivity();
        listProperties = new ArrayList<>();

        if(getArguments()!=null){
            if(getArguments().getString(MODE_SELECTED)!=null){
                if(Objects.requireNonNull(getArguments().getString(MODE_SELECTED)).equals(MODE_DISPLAY)){
                    recoverListProperties();
                } else {
                    Gson gson = new Gson();
                    String json = getArguments().getString(LIST_PROPERTIES_JSON,null);
                    Type listPropType = new TypeToken<ArrayList<Property>>(){}.getType();
                    listProperties = gson.fromJson(json,listPropType);
                }
            }
        }

        configureListProperties();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.property_item_list, container, false);
        configureListProperties();
        return view;
    }

    private void recoverListProperties(){
        PropertyContentProvider propertyContentProvider = new PropertyContentProvider();
        propertyContentProvider.setUtils(baseActivity.getApplicationContext(),true);

        final Cursor cursor = propertyContentProvider.query(null, null, null, null, null);

        if (cursor != null){
            if(cursor.getCount() >0){
                while (cursor.moveToNext()) {
                    listProperties.add(Property.getPropertyFromCursor(cursor));
                }
            }
            cursor.close();
        }
        baseActivity.setListProperties(listProperties);
    }

    private void configureListProperties(){

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            if(context!=null){

                // Create adapter passing in the sample user data
                PropertiesRecyclerViewAdapter adapter = new PropertiesRecyclerViewAdapter(listProperties,context, callbackListProperties, baseActivity);
                // Attach the adapter to the recyclerview to populate items
                recyclerView.setAdapter(adapter);
                // Set layout manager to position the items
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        }
    }

    @Override
    public void showDisplayFragment(int position) {
        baseActivity.configureAndShowDisplayFragment(listProperties.get(position).getId());
    }
}
