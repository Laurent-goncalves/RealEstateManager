package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.CallbackImageSelect;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import java.util.List;


public class ImagesEditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ImageProperty> listImages;
    private Context context;
    private Property property;
    private CallbackImageSelect mCallbackImageSelect;
    private BaseActivityListener baseActivityListener;

    public ImagesEditAdapter(List<ImageProperty> listImages, Property property, Context context, CallbackImageSelect mCallbackImageSelect, BaseActivityListener baseActivityListener) {
        this.listImages= listImages;
        this.context=context;
        this.property=property;
        this.mCallbackImageSelect=mCallbackImageSelect;
        this.baseActivityListener=baseActivityListener;
    }

    @Override
    public int getItemViewType(int position) {
        if(listImages!=null){
            if(position == listImages.size()-1) // if it's the last image of the list
                return 1; // return type ImagesAddViewHolder
            else
                return 0; // return type ImageUpdateViewHolder
        }
        return 0; // return 0 by default
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.edit_image_item, parent, false);

        if(viewType ==0)
            return new ImageUpdateViewHolder(view);
        else
            return new ImagesAddViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {

            case 0: // update

                ImageUpdateViewHolder viewHolderUpdate = (ImageUpdateViewHolder)holder;

                if(listImages!=null) {
                    if(listImages.get(holder.getAdapterPosition())!=null && property!=null) {
                        viewHolderUpdate.configureImagesViews(listImages, holder.getAdapterPosition(),this, context, baseActivityListener);
                        viewHolderUpdate.selectPhotoIcon.setOnClickListener(v -> mCallbackImageSelect.getExtraImageFromGallery(holder.getAdapterPosition()));
                        viewHolderUpdate.deleteIcon.setOnClickListener(v -> mCallbackImageSelect.alertDeleteImage(holder.getAdapterPosition()));
                    }
                }
            break;

            case 1: // add

                ImagesAddViewHolder viewHolderAdd = (ImagesAddViewHolder) holder;

                if(listImages!=null) {
                    if(listImages.get(holder.getAdapterPosition())!=null && property!=null) {
                        viewHolderAdd.configureImagesViews(listImages,holder.getAdapterPosition(),this, context, baseActivityListener);
                        viewHolderAdd.selectPhotoIcon.setOnClickListener(v -> mCallbackImageSelect.getExtraImageFromGallery(holder.getAdapterPosition()));
                    }
                }
            break;
        }
    }

    public void addNewImageToList(){
        ImageProperty newImageProperty = new ImageProperty(); // New empty item in the list
        listImages.add(newImageProperty);
    }

    public void deleteImageToList(int position){
        listImages.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(listImages!=null)
            return listImages.size();
        else
            return 0;
    }
}