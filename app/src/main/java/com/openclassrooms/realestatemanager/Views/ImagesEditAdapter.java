package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.Models.CallbackImageSelect;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;

import java.util.List;

public class ImagesEditAdapter extends RecyclerView.Adapter<ImagesEditViewHolder> {

    private final List<ImageProperty> listImages;
    private Context context;
    private Property property;
    private CallbackImageSelect mCallbackImageSelect;

    public ImagesEditAdapter(List<ImageProperty> listImages, Property property, Context context, CallbackImageSelect mCallbackImageSelect) {
        this.listImages= listImages;
        this.context=context;
        this.property=property;
        this.mCallbackImageSelect=mCallbackImageSelect;
    }

    @NonNull
    @Override
    public ImagesEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.edit_image_item, parent, false);
        return new ImagesEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImagesEditViewHolder holder, final int position) {

        if(listImages!=null) {
            if(listImages.get(position)!=null && property!=null)
            holder.configureImagesViews(listImages.get(position),property.getId(),context);
        }

        holder.selectPhotoIcon.setOnClickListener(v -> mCallbackImageSelect.getImageFromGallery(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        if(listImages!=null)
            return listImages.size();
        else
            return 0;
    }
}
