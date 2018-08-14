package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.Models.CallbackImageChange;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.R;
import java.util.List;


public class ImagesDisplayAdapter extends RecyclerView.Adapter<ImagesDisplayViewHolder> {

    private List<ImageProperty> listImages;
    private Context context;
    private CallbackImageChange callbackImageChange;

    public ImagesDisplayAdapter(List<ImageProperty> listImages, Context context, CallbackImageChange callbackImageChange) {
        this.listImages = listImages;
        this.callbackImageChange = callbackImageChange;
        this.context = context;
    }

    @NonNull
    @Override
    public ImagesDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.display_image_item, parent, false);
        return new ImagesDisplayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesDisplayViewHolder holder, int position) {
        if(listImages!=null) {
            if(listImages.get(position)!=null)
                holder.configureImagesViews(listImages.get(position));
        }

        holder.itemView.setOnClickListener(v -> callbackImageChange.changeMainImage(position));
    }

    @Override
    public int getItemCount() {
        if(listImages!=null)
            return listImages.size();
        else
            return 0;
    }
}