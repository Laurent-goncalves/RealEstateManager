package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.CallbackImageChange;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.R;
import java.util.List;


public class ImagesDisplayAdapter extends RecyclerView.Adapter<ImagesDisplayViewHolder> {

    private BaseActivityListener baseActivityListener;
    private List<ImageProperty> listImages;
    private Context context;
    private CallbackImageChange callbackImageChange;
    private int positionSelected;

    public ImagesDisplayAdapter(List<ImageProperty> listImages, Context context, int positionImageSelected, CallbackImageChange callbackImageChange, BaseActivityListener baseActivityListener) {
        this.listImages = listImages;
        this.callbackImageChange = callbackImageChange;
        this.context = context;
        this.baseActivityListener=baseActivityListener;
        this.positionSelected = positionImageSelected;
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
            if(listImages.get(holder.getAdapterPosition())!=null && baseActivityListener!=null)
                holder.configureImagesViews(listImages.get(holder.getAdapterPosition()), baseActivityListener, positionSelected);
        }

        if(holder.getAdapterPosition() == positionSelected)
            callbackImageChange.changeMainImage(positionSelected);

        holder.itemView.setOnClickListener(v -> {
            positionSelected = holder.getAdapterPosition();
            callbackImageChange.changeMainImage(holder.getAdapterPosition());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        if(listImages!=null)
            return listImages.size();
        else
            return 0;
    }
}
