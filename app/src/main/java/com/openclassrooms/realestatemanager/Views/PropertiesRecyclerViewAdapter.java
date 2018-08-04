package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;

import java.util.List;


public class PropertiesRecyclerViewAdapter extends RecyclerView.Adapter<PropertyViewHolder>  {

    private final List<Property> listProperties;
    private Context context;

    public PropertiesRecyclerViewAdapter(List<Property> listProperties, Context context) {
        this.listProperties = listProperties;
        this.context=context;
    }

    @Override
    public PropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.property_item, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        if(listProperties!=null)
            holder.configurePropertiesViews(listProperties.get(position));
    }

    @Override
    public int getItemCount() {
        if(listProperties!=null)
            return listProperties.size();
        else
            return 0;
    }

}
