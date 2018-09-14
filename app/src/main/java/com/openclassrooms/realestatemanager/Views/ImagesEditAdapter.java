package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Models.CallbackImageSelect;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import java.util.List;


public class ImagesEditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ImageProperty> listImages;
    private Context context;
    private Property property;
    private CallbackImageSelect mCallbackImageSelect;
    private int positionEdited;
    private ListPropertiesFragment.BaseActivityListener baseActivityListener;

    public ImagesEditAdapter(List<ImageProperty> listImages, Property property, Context context, CallbackImageSelect mCallbackImageSelect, ListPropertiesFragment.BaseActivityListener baseActivityListener) {
        this.listImages= listImages;
        this.context=context;
        this.property=property;
        this.positionEdited = -1;
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
                    if(listImages.get(position)!=null && property!=null) {

                        if(positionEdited==-1)
                            viewHolderUpdate.configureImagesViews(listImages.get(position),this, false, false, context, baseActivityListener);
                        else if(positionEdited==position)
                            viewHolderUpdate.configureImagesViews(listImages.get(position),this, true, true, context, baseActivityListener);
                        else
                            viewHolderUpdate.configureImagesViews(listImages.get(position),this, false, true, context, baseActivityListener);

                        viewHolderUpdate.selectPhotoIcon.setOnClickListener(v -> mCallbackImageSelect.getExtraImageFromGallery(holder.getAdapterPosition()));
                        viewHolderUpdate.deleteIcon.setOnClickListener(v -> mCallbackImageSelect.alertDeleteImage(holder.getAdapterPosition()));
                    }
                }

            break;

            case 1: // add

                ImagesAddViewHolder viewHolderAdd = (ImagesAddViewHolder) holder;

                if(listImages!=null) {
                    if(listImages.get(position)!=null && property!=null) {

                        if(positionEdited==position)
                            viewHolderAdd.configureImagesViews(listImages.get(position),this,true, true, context, baseActivityListener);
                        else
                            viewHolderAdd.configureImagesViews(listImages.get(position),this,false,false, context, baseActivityListener);

                        viewHolderAdd.selectPhotoIcon.setOnClickListener(v -> mCallbackImageSelect.getExtraImageFromGallery(holder.getAdapterPosition()));
                    }
                }

            break;
        }
    }

    public void addNewImageToList(ImageProperty imageProperty, Boolean newItem){

        if(newItem){ // if new item in the list
            listImages.remove(listImages.size()-1); // remove the last item
            listImages.add(imageProperty); // add the new item

            // New empty item in the list
            ImageProperty newImageProperty = new ImageProperty();
            newImageProperty.setIdProperty(imageProperty.getIdProperty());
            listImages.add(newImageProperty);

        } else {
            updateItemList(imageProperty);
        }
        notifyDataSetChanged();
    }

    public void setPositionEdited(int positionEdited){
        this.positionEdited = positionEdited;
        notifyDataSetChanged();
    }

    private void updateItemList(ImageProperty imageProperty){

        int index = 0;

        // Find index of the imageProperty to update
        for(ImageProperty image : listImages){
            if(image.getId() == imageProperty.getId()){
                // Update all parameters
                listImages.get(index).setDescription(imageProperty.getDescription());
                listImages.get(index).setImagePath(imageProperty.getImagePath());
                break;
            } else
                index++;
        }
    }

    public void deleteImageToList(ImageProperty imageProperty){

        int index = 0;

        for(ImageProperty image : listImages){
            if(image.getId() == imageProperty.getId()){
                listImages.remove(index);
                notifyDataSetChanged();
                break;
            }
            else
                index++;
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