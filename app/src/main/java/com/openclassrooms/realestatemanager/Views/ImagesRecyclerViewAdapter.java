package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.CallbackImageSelect;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;

import java.util.List;

public class ImagesRecyclerViewAdapter extends RecyclerView.Adapter<ImagesViewHolder> {

    private final List<ImageProperty> listImages;
    private Context context;
    private Property property;
    private CallbackImageSelect mCallbackImageSelect;

    public ImagesRecyclerViewAdapter(List<ImageProperty> listImages, Property property, Context context, CallbackImageSelect mCallbackImageSelect) {
        this.listImages= listImages;
        this.context=context;
        this.property=property;
        this.mCallbackImageSelect=mCallbackImageSelect;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.edit_property_item, parent, false);
        return new ImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        if(listImages!=null) {
            if(listImages.get(position)!=null && property!=null)
            holder.configureImagesViews(listImages.get(position),property.getId(),context, mCallbackImageSelect);
        }
    }

    @Override
    public int getItemCount() {
        if(listImages!=null)
            return listImages.size();
        else
            return 0;
    }
}
