package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.app.FragmentTransaction;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Models.CallbackImageSelect;
import com.openclassrooms.realestatemanager.Utils.ImageLoading;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.Views.ImagesViewHolder;

import java.io.FileNotFoundException;
import java.util.List;


public class MainActivity extends AppCompatActivity  {

    private ImageLoading imageLoading;
    private Button button;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_IMAGE_VIEWHOLDER = 2;
    private ImageView imageView;
    private PropertyDatabase database;
    private static int PROPERTY_ID = 3;
    private ImagesViewHolder imagesViewHolder;

    private static Property PROPERTY_DEMO = new Property(PROPERTY_ID, "Appartment", 125000d,30.25,1,
            "description","address","School, Subway",false,"01/06/2018",0d,0d,"Eric");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*this.database = PropertyDatabase.getInstance(getApplicationContext());

        try {
            Property newProperty = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(1));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        database.imageDao().getAllImages().observe(this,ListObserver);


        imageView = (ImageView) findViewById(R.id.activity_main_image);
        button = findViewById(R.id.button_image_loading);



        Button buttonDEL = findViewById(R.id.delete_image_loading);
        Button buttonInsert = findViewById(R.id.insert_button);
        Button buttonUpdate = findViewById(R.id.update_button);
        Button buttonCANCEL = findViewById(R.id.Cancel_button);
        Button buttonSQL = findViewById(R.id.sql_image_loading);

        imageLoading = new ImageLoading(this,this.database);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadNewImageFromDevice();
            }
        });

        buttonDEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(null);
                //imageLoading.deleteImageDataBase(1);
            }
        });

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                imageLoading.inserNewImageDataBase(1,"description");
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                imageLoading.updateImageDataBase(1,1,"description");
            }
        });

        buttonCANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                imageLoading.reinitializeImageProperty();
            }
        });

        buttonSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedatabase(1);
            }
        });*/
        configure_and_show_modif_fragment();



    }


    public void configure_and_show_modif_fragment(){

        EditFragment editFragment = new EditFragment();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainactivity_xml, editFragment);
        fragmentTransaction.commit();


        /*
        https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=48.613032,2.482217&radius=1000&key=AIzaSyCAzX1ILkJlqSsTMkRJHSGEMAQWuqxSxKA
         */

    }

    // -------------------------------------------------------------------------------------------------------
    // -------------------------------------- LOADING IMAGE FROM DEVICE --------------------------------------
    // -------------------------------------------------------------------------------------------------------

    public void loadNewImageFromDevice(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            Bitmap bitmap;

            try {
                if (selectedImage != null) {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                    imageView.setImageBitmap(bitmap);
                    imageLoading.setCurrentImageByte(Utils.getBitmapAsByteArray(bitmap));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RESULT_LOAD_IMAGE_VIEWHOLDER && resultCode == RESULT_OK && null != data){

            Uri selectedImage = data.getData();

            Bitmap bitmap;

            try {
                if (selectedImage != null) {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                    imagesViewHolder.setImage(bitmap);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void updatedatabase(int idImage){
        database.imageDao().getImage(idImage).observe(this,nameObserver);
    }

    final Observer<ImageProperty> nameObserver = new Observer<ImageProperty>() {
        @Override
        public void onChanged(@Nullable final ImageProperty newName) {
            // Update the UI, in this case, a TextView.
            if(newName!=null) {
                Bitmap imageBitmap=BitmapFactory.decodeByteArray(newName.getImage(), 0, newName.getImage().length);
                imageView.setImageBitmap(imageBitmap);
            }
        }
    };

    final Observer<List<ImageProperty>> ListObserver = new Observer<List<ImageProperty>>() {
        @Override
        public void onChanged(@Nullable final List<ImageProperty> newName) {

            for(ImageProperty img : newName)
                System.out.println("eee list=" + img.getId());
        }
    };

    public Button getButton() {
        return button;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void getImageFromGallery(ImagesViewHolder imagesViewHolder) {
        this.imagesViewHolder=imagesViewHolder;
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE_VIEWHOLDER);
    }
}
