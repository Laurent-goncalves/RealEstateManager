package com.openclassrooms.realestatemanager.Views;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

public class PropertyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.main_image_property) ImageView mainImage;
    @BindView(R.id.property_type_view) TextView typeTextView;
    @BindView(R.id.property_location_view) TextView locTextView;
    @BindView(R.id.property_cost_view) TextView costTextView;
    @BindView(R.id.item_property_layout) LinearLayout propertyLayout;

    public PropertyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void configurePropertiesViews(Property property, Context context, MainActivity mainActivity){

        // Type of appartment
        typeTextView.setText(property.getType());

        // Location
        locTextView.setText(property.getAddress());

        // Price
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String price = formatter.format(property.getPrice());
        costTextView.setText(price);

        // Main Image

        if(property.getMainImagePath()!=null) {

            Utils.setImageBitmapInView(property.getMainImagePath(),mainImage,mainActivity);

            /*try {
                Bitmap bitmap;
                File f= new File(property.getMainImagePath());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                if (EasyPermissions.hasPermissions(mainActivity, galleryPermissions)) {

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),options);
                    mainImage.setImageBitmap(bitmap);

                } else {
                    EasyPermissions.requestPermissions(mainActivity, "Access for storage",
                            101, galleryPermissions);
                }

            } catch (Exception e) {
                System.out.println("eee exception = " + e.toString());
            }*/
        }


    }
}
