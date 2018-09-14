package com.openclassrooms.realestatemanager.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.R;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.support.test.InstrumentationRegistry.getContext;


public class UtilsImageLoader {

    private BaseActivity baseActivity;
    private String imagePath;
    private ImageView imageView;

    public UtilsImageLoader(String imagePath, ImageView imageView, BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        this.imagePath = imagePath;
        this.imageView = imageView;
        setImageBitmapInView(imagePath,imageView,baseActivity);
    }

    public void setImageBitmapInView(String imagePath, ImageView imageView, BaseActivity baseActivity){

        try {
            Bitmap bitmap;
            if(imagePath==null)
                imageView.setImageDrawable(baseActivity.getApplicationContext().getResources().getDrawable(R.drawable.placeholder));

            File f = new File(imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            String[] galleryPermissions = {READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

            if (EasyPermissions.hasPermissions(baseActivity.getApplicationContext(), galleryPermissions)) {

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),options);

                if(bitmap!=null)
                    imageView.setImageBitmap(bitmap);

            } else {
                EasyPermissions.requestPermissions(baseActivity, "Access for storage",
                        101, galleryPermissions);
            }

        } catch (Exception e) {

        }
    }
}

    /*@AfterPermissionGranted(READ_EXTERNAL_STORAGE)
    private void pickContactWithPermissionsCheck() {
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_CONTACTS)) {
            // Have permission

        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, ,
                    READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }*/