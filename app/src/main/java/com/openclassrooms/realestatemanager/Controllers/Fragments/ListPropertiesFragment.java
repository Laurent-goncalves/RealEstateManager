package com.openclassrooms.realestatemanager.Controllers.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.CallbackListProperties;
import com.openclassrooms.realestatemanager.Views.PropertiesRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ListPropertiesFragment extends Fragment implements CallbackListProperties {

    private static final String PROPERTIES_JSON = "list_properties_json";
    private List<Property> listProperties;
    private CallbackListProperties callbackListProperties;
    private MainActivity mainActivity;

    public ListPropertiesFragment() {
    }

    public static ListPropertiesFragment newInstance(List<Property> listProperties) {
        ListPropertiesFragment fragment = new ListPropertiesFragment();

        // Convert listProperties into JSON string object and put in bundle
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String list_properties_json = gson.toJson(listProperties);
        args.putString(PROPERTIES_JSON, list_properties_json);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackListProperties = this;
        mainActivity = (MainActivity) getActivity();

        if (getArguments() != null) {

            Gson gson = new Gson();
            String json = getArguments().getString(PROPERTIES_JSON,null);
            Type list_places = new TypeToken<ArrayList<Property>>(){}.getType();
            listProperties = gson.fromJson(json,list_places);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.property_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            if(context!=null){

                // Create adapter passing in the sample user data
                PropertiesRecyclerViewAdapter adapter = new PropertiesRecyclerViewAdapter(listProperties,context, callbackListProperties);
                // Attach the adapter to the recyclerview to populate items
                recyclerView.setAdapter(adapter);
                // Set layout manager to position the items
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        }
        return view;
    }

    @Override
    public void showDisplayFragment(int position) {
        mainActivity.configureAndShowDisplayFragment(listProperties.get(position));
    }
}
