package com.openclassrooms.realestatemanager.Views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ImagesDisplayViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_property) ImageView image;
    @BindView(R.id.title_image_property) TextView titleImage;

    public ImagesDisplayViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void configureImagesViews(ImageProperty imageProperty, MainActivity mainActivity) {

        // set image
        if(imageProperty.getImagePath()!=null){
            Utils.setImageBitmapInView(imageProperty.getImagePath(),image,mainActivity);

            /*try {
                Bitmap bitmap;
                File f= new File(imageProperty.getImagePath());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),options);
                image.setImageBitmap(bitmap);


            } catch (Exception e) {
                System.out.println("eee exception = " + e.toString());
            }*/
        }

        // set title
        String title = imageProperty.getDescription();
        titleImage.setText(title);
    }
}
