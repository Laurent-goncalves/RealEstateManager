package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.CallbackImageChange;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.R;
import java.util.List;


public class ImagesDisplayAdapter extends RecyclerView.Adapter<ImagesDisplayViewHolder> {

    private BaseActivity baseActivity;
    private List<ImageProperty> listImages;
    private Context context;
    private CallbackImageChange callbackImageChange;
    private int positionSelected;

    public ImagesDisplayAdapter(List<ImageProperty> listImages, Context context, CallbackImageChange callbackImageChange, BaseActivity baseActivity) {
        this.listImages = listImages;
        this.callbackImageChange = callbackImageChange;
        this.context = context;
        this.baseActivity=baseActivity;
        this.positionSelected = 0;
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
                holder.configureImagesViews(listImages.get(position),baseActivity,positionSelected);
        }

        holder.itemView.setOnClickListener(v -> {
            positionSelected = position;
            callbackImageChange.changeMainImage(position);
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
